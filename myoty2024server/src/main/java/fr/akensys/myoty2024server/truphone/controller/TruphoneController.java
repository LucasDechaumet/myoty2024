package fr.akensys.myoty2024server.truphone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.akensys.myoty2024server.truphone.entity.SimCard;
import fr.akensys.myoty2024server.truphone.models.SimCardResponse;
import fr.akensys.myoty2024server.truphone.service.SimCardService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;


@RestController
@RequiredArgsConstructor
@RequestMapping("/truphone")
public class TruphoneController {

    public final SimCardService simCardService;

    @PostMapping("/getSimCards")
    public ResponseEntity<Flux<SimCard>> getSimCards()
    {
        Flux<SimCardResponse> simCardResponses = simCardService.saveSimCardsFromApi();
        Flux<SimCard> simCards = simCardService.getAllSimCardsInDb(simCardResponses);
        return ResponseEntity.ok(simCards);
    }


}
