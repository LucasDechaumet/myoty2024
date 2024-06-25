package fr.akensys.myoty2024server.truphone.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import fr.akensys.myoty2024server.truphone.entity.SimCard;
import fr.akensys.myoty2024server.truphone.models.SimCardResponse;
import fr.akensys.myoty2024server.truphone.repository.SimCardRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class SimCardService {


    private final SimCardRepo simCardRepo;
    private final WebClient.Builder webClientBuilder;
    private final String TOKEN = "456554db3785c41d24bc16e8df3819a5d163ceb2";

    private static final String URL = "https://iot.truphone.com/api/";

    // recupere toutes les simCards
    // pour chaque objet tu crees une SimCard
    // save en db
    // return la list des SimCard save

    public Flux<SimCardResponse> saveSimCardsFromApi()
    {
        return webClientBuilder.build()
        .get()
        .uri(URL + "v2.0/sims")
        .header("Authorization", "Token " + TOKEN)
        .retrieve()
        .bodyToFlux(SimCardResponse.class)
        .onErrorResume(WebClientResponseException.class, ex -> {
            return Flux.error(new RuntimeException("Erreur lors de la récupération des données SIM", ex));
        })
        .onErrorResume(Exception.class, ex -> {
            return Flux.error(new RuntimeException("Erreur interne lors de la récupération des données SIM", ex));
        });
    }


    public Flux<SimCard> getAllSimCardsInDb(Flux<SimCardResponse> responses) {
        return responses.flatMap(response -> {
            SimCard simCard = SimCard.builder()
                    .iccid(response.getIccid())
                    .label(response.getLabel())
                    .primaryMsisdn(response.getPrimaryMsisdn())
                    .rate_plan(response.getSubscription().getServicePackId())
                    .imei(response.getImei())
                    .sim_status(response.getSubscription().getSubscriptionStatus())
                    .smsMo(response.getSubscription().getBearerServices().isSmsMo())
                    .smsMt(response.getSubscription().getBearerServices().isSmsMt())
                    .tags(response.getTags().toString())
                    .build();

                    simCardRepo.save(simCard);

            return Mono.just(simCard);
        }).collectList().flatMapMany(Flux::fromIterable);

    }
}




