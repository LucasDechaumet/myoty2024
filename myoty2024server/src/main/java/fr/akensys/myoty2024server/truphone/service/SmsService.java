package fr.akensys.myoty2024server.truphone.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import fr.akensys.myoty2024server.error.SimCardNotFoundException;
import fr.akensys.myoty2024server.truphone.entity.SmsHistory;
import fr.akensys.myoty2024server.truphone.models.SmsCommand;
import fr.akensys.myoty2024server.truphone.models.SmsInfo;
import fr.akensys.myoty2024server.truphone.models.SmsInfo.DeliveryReport;
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

    public void sendSms(SmsCommand request)
    {
    List<Long> iccids = request.getIccid();

        for(Long iccid : iccids)
        {
            simCardRepo.findByIccid(iccid).orElseThrow(() -> new SimCardNotFoundException("Aucune carte SIM trouvé avec cet iccid : " + iccid));

        webClientBuilder.build()
            .post()
            .uri(URL + "v2.0/sims/send_sms")
            .header("Authorization", "Token " + TOKEN)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(SmsCommand.class)  
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
        .bodyToFlux(SmsInfo.class)
        .flatMap((response) -> {
            return smsRepo.findById(response.getId())
                    .map(existingSms -> {
                        return Mono.empty();
                    })
                    .orElseGet(() -> {
                        SmsHistory sms_History = buildSmsHistory(response);
                        smsRepo.save(sms_History);
                        return Mono.empty();
                    });
        })
        .blockLast();
    }

    public SmsHistory buildSmsHistory(SmsInfo response) {

        List<DeliveryReport> deliveryReports = response.getDeliveryReport();
        List<String> deliveryStatus = new ArrayList<>();

        for (DeliveryReport deliveryReport : deliveryReports) {
            deliveryStatus.add(deliveryReport.getDeliveryStatus());
        }

        return SmsHistory.builder()
        .id(response.getId())
        .content(response.getContent())
        .dateSubmitted(response.getDateSubmitted())
        .deliveryReport(deliveryStatus)
        .build();
    }

    public List<SmsHistory> getAllSmsInDb() {
        return smsRepo.findAll();
    }
}
