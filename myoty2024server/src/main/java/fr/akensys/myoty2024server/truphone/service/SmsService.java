package fr.akensys.myoty2024server.truphone.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import fr.akensys.myoty2024server.error.SimCardNotFoundException;
import fr.akensys.myoty2024server.truphone.entity.SMS_History;
import fr.akensys.myoty2024server.truphone.entity.SimCard;
import fr.akensys.myoty2024server.truphone.models.SMS_Command;
import fr.akensys.myoty2024server.truphone.models.SMS_Info;
import fr.akensys.myoty2024server.truphone.models.SMS_Info.DeliveryReport;
import fr.akensys.myoty2024server.truphone.repository.SimCardRepo;
import fr.akensys.myoty2024server.truphone.repository.SmsRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class SmsService {

    private final SmsRepo smsRepo;
    private final SimCardRepo simCardRepo;
    private final WebClient.Builder webClientBuilder;
    private final String TOKEN = "456554db3785c41d24bc16e8df3819a5d163ceb2";
    private static final String URL = "https://iot.truphone.com/api/";

    public void sendSms(SMS_Command request)
    {
    List<Long> iccids = request.getIccid();

        for(Long iccid : iccids)
        {
            SimCard simCard = simCardRepo.findByIccid(iccid).orElseThrow(() -> new SimCardNotFoundException("Aucune carte SIM trouvé avec cet iccid : " + iccid));

        webClientBuilder.build()
            .post()
            .uri(URL + "v2.0/sims/send_sms")
            .header("Authorization", "Token " + TOKEN)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(SMS_Command.class)  
            .block();

        System.out.println("SMS sent for: " + iccid);
        }
    }

    public void getSmsHistory()
    {
        webClientBuilder.build()
        .get()
        .uri(URL + "v2.0/sims/send_sms/status")
        .header("Authorization", "Token " + TOKEN)
        .retrieve()
        .bodyToFlux(SMS_Info.class)
        .flatMap((response) -> {
            return smsRepo.findById(response.getId())
                    .map(existingSms -> {
                        return Mono.empty();
                    })
                    .orElseGet(() -> {
                        SMS_History sms_History = buildSmsHistory(response);
                        smsRepo.save(sms_History);
                        return Mono.empty();
                    });
        })
        .blockLast();
    }

    public SMS_History buildSmsHistory(SMS_Info response) {

        List<DeliveryReport> deliveryReports = response.getDeliveryReport();
        List<String> deliveryStatus = new ArrayList<>();

        for (DeliveryReport deliveryReport : deliveryReports) {
            deliveryStatus.add(deliveryReport.getDeliveryStatus());
        }

        return SMS_History.builder()
        .id(response.getId())
        .content(response.getContent())
        .dateSubmitted(response.getDateSubmitted())
        .deliveryReport(deliveryStatus)
        .build();
    }

    public List<SMS_History> getAllSmsInDb() {
        return smsRepo.findAll();
    }
}
