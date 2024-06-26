package fr.akensys.myoty2024server.truphone.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fr.akensys.myoty2024server.truphone.entity.SimCard;
import fr.akensys.myoty2024server.truphone.service.SimCardService;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/truphone")
public class TruphoneController {

    public final SimCardService simCardService;

    @PostMapping("/getSimCards")
    public ResponseEntity<List<SimCard>> getSimCards()
    {
       simCardService.saveSimCardsFromApi();
       List<SimCard> simCards =  simCardService.getAllSimCardsInDb();
        return ResponseEntity.ok(simCards);
    }

    
    // @PatchMapping("/setSimCard/{iccid}")
    // public ResponseEntity<Void> setSimCard(@PathVariable Long iccid, @RequestBody SimCardUpdate request) {
    //     simCardService.updateSimCard(iccid, request)
    //             .subscribe(); // Subscribe to trigger the WebClient call

    //     return ResponseEntity.noContent().build();
    // }


}
