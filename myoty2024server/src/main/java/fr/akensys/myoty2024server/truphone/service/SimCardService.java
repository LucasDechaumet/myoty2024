package fr.akensys.myoty2024server.truphone.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import fr.akensys.myoty2024server.error.SimCardNotFoundException;
import fr.akensys.myoty2024server.truphone.entity.SimCard;
import fr.akensys.myoty2024server.truphone.entity.TeltonikaDevice;
import fr.akensys.myoty2024server.truphone.models.SimCardResponse;
import fr.akensys.myoty2024server.truphone.models.SimCardUpdateInfo;
import fr.akensys.myoty2024server.truphone.models.SimCardUpdateStatus;
import fr.akensys.myoty2024server.truphone.models.SimCardResponse.TagsReponse;
import fr.akensys.myoty2024server.truphone.repository.DeviceRepo;
import fr.akensys.myoty2024server.truphone.repository.SimCardRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class SimCardService {
    
    private final DeviceRepo deviceRepo;
    private final SimCardRepo simCardRepo;
    private final WebClient.Builder webClientBuilder;
    private final String TOKEN = "456554db3785c41d24bc16e8df3819a5d163ceb2";
    private static final String URL = "https://iot.truphone.com/api/";

    public void saveSimCardsFromApi() {
        webClientBuilder.build()
            .get()
            .uri(URL + "v2.2/sims")
            .header("Authorization", "Token " + TOKEN)
            .retrieve()
            .bodyToFlux(SimCardResponse.class)
            .flatMap((response) -> {
                return simCardRepo.findByIccid(response.getIccid())
                    .map(existingSimCard -> {
                        return Mono.empty();
                    })
                    .orElseGet(() -> {
                        SimCard simCard = buildSimCard(response);
                        simCardRepo.save(simCard);
                        return Mono.empty();
                    });
            })
            .blockLast();

    }

    public void updateAllSimCardsWithLatestSeenDate() {
        List<SimCard> simCards = simCardRepo.findAll();
        for (SimCard simCard : simCards) {
            updateSimCardWithLatestSeenDate(simCard.getIccid());
        }
    }
    
    public void updateSimCardWithLatestSeenDate(Long iccid) {
        List<SimCardResponse> simCardResponses = webClientBuilder.build()
            .get()
            .uri(URL + "v2.0/sims/" + iccid + "/locations")
            .header("Authorization", "Token " + TOKEN)
            .retrieve()
            .bodyToFlux(SimCardResponse.class)
            .collectList()
            .block();

        if (simCardResponses != null && !simCardResponses.isEmpty()) {
            SimCardResponse latestCdr = simCardResponses.get(0); // Récupérer la première entrée

            SimCard simCard = simCardRepo.findByIccid(iccid)
                .orElseThrow(() -> new SimCardNotFoundException("Aucune carte SIM trouvée avec cet iccid : " + iccid));

            simCard.setLast_seen_date(latestCdr.getDate());

            simCardRepo.save(simCard);
        }
    }

    public void updateAllSimCardWithDataStatus() {
        List<SimCard> simCards = simCardRepo.findAll();
        for (SimCard simCard : simCards) {
            updateSimCardWithDataStatus(simCard.getIccid());
        }
    }
    
    public void updateSimCardWithDataStatus(Long iccid) {
        List<SimCardResponse> simCardResponses = webClientBuilder.build()
            .get()
            .uri(URL + "v2.2/sims/" + iccid + "/status")
            .header("Authorization", "Token " + TOKEN)
            .retrieve()
            .bodyToFlux(SimCardResponse.class)
            .collectList()
            .block();

        if (simCardResponses != null && !simCardResponses.isEmpty()) {
            SimCardResponse latestData = simCardResponses.get(0); // Récupérer la première entrée

            SimCard simCard = simCardRepo.findByIccid(iccid)
                .orElseThrow(() -> new SimCardNotFoundException("Aucune carte SIM trouvée avec cet iccid : " + iccid));

                simCard.setSimStatus(latestData.getStatus().getSim_card_service());
                simCard.setDataStatus(latestData.getStatus().getData_service());
                simCard.setSms_Mo_status(latestData.getStatus().getSms_mo_service());
                simCard.setSms_Mt_status(latestData.getStatus().getSms_mt_service());
            simCardRepo.save(simCard);
        }
    }

    public void updateAdressIp() {
        List<SimCardResponse> responses = webClientBuilder.build()
            .get()
            .uri(URL + "v2.0/sims/ongoing_sessions")
            .header("Authorization", "Token " + TOKEN)
            .retrieve()
            .bodyToFlux(SimCardResponse.class)
            .collectList()
            .block();

        if (responses != null) {
            for (SimCardResponse response : responses) {
                updateSimCardIp(response.getIccid(), response.getIpAddress());
            }
        }
    }

    private void updateSimCardIp(Long iccid, String ipAddress) {
        SimCard simCard = simCardRepo.findByIccid(iccid)
            .orElseThrow(() -> new SimCardNotFoundException("Aucune carte SIM trouvé avec cet iccid : " + iccid));
        
        simCard.setIP_address(ipAddress);
        simCardRepo.save(simCard);
    }



    private SimCard buildSimCard(SimCardResponse response) {

        List<TagsReponse> tags = response.getTags();
        List<String> tags_label = new ArrayList<>();
        for (TagsReponse tag : tags) {
            tags_label.add(tag.getLabel());
        }

        SimCard simCard =  SimCard.builder()
            .iccid(response.getIccid())
            .label(response.getLabel())
            .provision_Date(response.getDates().getProvisionDate())
            .Date_first_Activated(response.getDates().getFirstActivationDate())
            .tags(tags_label)
            .build();

            if (response.getImei() != null) {
            TeltonikaDevice device = deviceRepo.findByImei(response.getImei())
                .orElse(null);
            if (device != null) {
                simCard.setTeltonikaDevice(device);
                simCard.setDeviceName(simCard.getTeltonikaDevice().getName());
            }
        }

        return simCard;
    }


    public List<SimCard> getAllSimCardsInDb() {
        return simCardRepo.findAll();
    }


    public void patchSimCard(SimCard simCard, SimCardUpdateInfo simCardUpdate) {
        simCard.setIccid(simCardUpdate.getIccid() != null ? simCardUpdate.getIccid() : simCard.getIccid());
        simCard.setLabel(simCardUpdate.getLabel() != null ? simCardUpdate.getLabel() : simCard.getLabel());
        simCardRepo.save(simCard);
    }

    
    public void updateSimCard(SimCardUpdateInfo requestUpdate)
    {
        Long iccid = requestUpdate.getIccid();
        System.out.println("token = " + TOKEN);
        System.out.println("Request body: " + requestUpdate);
        SimCard simCard = simCardRepo.findByIccid(iccid).orElseThrow(() -> new SimCardNotFoundException("Aucune carte SIM trouvé avec cet iccid : " + iccid));
        webClientBuilder.build()
        .patch()
        .uri(URL + "v2.2/sims/" + iccid)
            .header("Authorization", "Token " + TOKEN)
            .bodyValue(requestUpdate)
            .retrieve()
            .bodyToMono(SimCardResponse.class)
            .block();

        patchSimCard(simCard, requestUpdate);
    }

    public void changeSimCardStatus(SimCardUpdateStatus request) {
        
        List<Long> iccids = request.getIccid();

        for(Long iccid : iccids)
        {
            SimCard simCard = simCardRepo.findByIccid(iccid).orElseThrow(() -> new SimCardNotFoundException("Aucune carte SIM trouvé avec cet iccid : " + iccid));

        webClientBuilder.build()
            .post()
            .uri(URL + "v2.0/sims/change_status")
            .header("Authorization", "Token " + TOKEN)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(SimCardResponse.class)  
            .block();

        System.out.println("sim status updated for: " + iccid);
        simCard.setSimStatus(request.getStatus().getSim_card_service());
        simCardRepo.save(simCard);
        }

    }

    public void changeSimCardDataStatus(SimCardUpdateStatus request)
    {
        List<Long> iccids = request.getIccid();

        for(Long iccid : iccids)
        {
            SimCard simCard = simCardRepo.findByIccid(iccid).orElseThrow(() -> new SimCardNotFoundException("Aucune carte SIM trouvé avec cet iccid : " + iccid));

        webClientBuilder.build()
            .post()
            .uri(URL + "v2.0/sims/change_data_status")
            .header("Authorization", "Token " + TOKEN)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(SimCardResponse.class)  
            .block();

        System.out.println("data status updated for: " + iccid);
        simCard.setDataStatus(request.getStatus().getData_service());
        simCardRepo.save(simCard);
        }
    }

    

}
