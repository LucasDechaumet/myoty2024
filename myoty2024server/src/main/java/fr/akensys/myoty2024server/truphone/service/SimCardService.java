package fr.akensys.myoty2024server.truphone.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import fr.akensys.myoty2024server.error.SimCardNotFoundException;
import fr.akensys.myoty2024server.truphone.entity.SimCard;
import fr.akensys.myoty2024server.truphone.models.CdrResponse;
import fr.akensys.myoty2024server.truphone.models.SimCardResponse;
import fr.akensys.myoty2024server.truphone.models.SimCardUpdateInfo;
import fr.akensys.myoty2024server.truphone.models.SimCardUpdateStatus;
import fr.akensys.myoty2024server.truphone.models.SimCardResponse.Tags;
import fr.akensys.myoty2024server.truphone.repository.SimCardRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class SimCardService {

    private final SimCardRepo simCardRepo;
    private final WebClient.Builder webClientBuilder;
    private final String TOKEN = "456554db3785c41d24bc16e8df3819a5d163ceb2";
    private static final String URL = "https://iot.truphone.com/api/";

    public void saveSimCardsFromApi() {
        webClientBuilder.build()
            .get()
            .uri(URL + "v2.0/sims")
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

    public void updateAllSimCardsWithLatestCdrData() {
        List<SimCard> simCards = simCardRepo.findAll();
        for (SimCard simCard : simCards) {
            updateSimCardWithLatestCdrData(simCard.getIccid());
        }
    }
    
    public void updateSimCardWithLatestCdrData(Long iccid) {
        List<CdrResponse> cdrResponses = webClientBuilder.build()
            .get()
            .uri(URL + "v2.0/sims/" + iccid + "/cdrs")
            .header("Authorization", "Token " + TOKEN)
            .retrieve()
            .bodyToFlux(CdrResponse.class)
            .collectList()
            .block();

        if (cdrResponses != null && !cdrResponses.isEmpty()) {
            CdrResponse latestCdr = cdrResponses.get(0); // Récupérer la première entrée

            SimCard simCard = simCardRepo.findByIccid(iccid)
                .orElseThrow(() -> new SimCardNotFoundException("Aucune carte SIM trouvée avec cet iccid : " + iccid));

            simCard.setCountry(latestCdr.getCountry());
            simCard.setNetwork(latestCdr.getNetwork());

            simCardRepo.save(simCard);
        }
    }

    private SimCard buildSimCard(SimCardResponse response) {

        List<Tags> tags = response.getTags();
        List<String> tags_label = new ArrayList<>();
        for (Tags tag : tags) {
            tags_label.add(tag.getLabel());
        }

        return SimCard.builder()
            .iccid(response.getIccid())
            .label(response.getLabel())
            .primaryMsisdn(response.getPrimaryMsisdn())
            .rate_plan(response.getSubscription().getServicePackId())
            .device(response.getImei())
            .sim_status(response.getSubscription().getSubscriptionStatus())
            .smsMo(response.getSubscription().getBearerServices().getSmsMo())
            .smsMt(response.getSubscription().getBearerServices().getSmsMt())
            .tags(tags_label)
            .build();
    }


    public List<SimCard> getAllSimCardsInDb() {
        return simCardRepo.findAll();
    }


    public void patchSimCard(SimCard simCard, SimCardUpdateInfo simCardUpdate) {
        simCard.setIccid(simCardUpdate.getIccid() != null ? simCardUpdate.getIccid() : simCard.getIccid());
        simCard.setLabel(simCardUpdate.getLabel() != null ? simCardUpdate.getLabel() : simCard.getLabel());
        
        simCardRepo.save(simCard);
    }

    
    public void updateSimCard(Long iccid, SimCardUpdateInfo requestUpdate)
    {
        SimCard simCard = simCardRepo.findByIccid(iccid).orElseThrow(() -> new SimCardNotFoundException("Aucune carte SIM trouvé avec cet iccid : " + iccid));
        webClientBuilder.build()
        .patch()
        .uri(URL + "v2.2/sims/" +iccid)
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
        simCard.setSim_status(request.getStatus());
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
        simCard.setSim_status(request.getStatus());
        simCardRepo.save(simCard);
        }
    }

    

}
