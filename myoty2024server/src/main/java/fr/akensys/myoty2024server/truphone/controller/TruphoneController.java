package fr.akensys.myoty2024server.truphone.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fr.akensys.myoty2024server.truphone.entity.SimCard;
import fr.akensys.myoty2024server.truphone.models.SimCardUpdateInfo;
import fr.akensys.myoty2024server.truphone.models.SimCardUpdateStatus;
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

    @PatchMapping("/setSimCard/{iccid}")
    public ResponseEntity<String> setSimCard(@PathVariable Long iccid, @RequestBody SimCardUpdateInfo request) {
        simCardService.updateSimCard(iccid, request);
        return ResponseEntity.ok("SimCard updated");
    }
    
    @PostMapping("/changeStatus")
    public ResponseEntity<String> updateSimStatus(@RequestBody SimCardUpdateStatus request) {
        simCardService.changeSimCardStatus(request);
        return ResponseEntity.ok("simCardStatusUpdated");
    }
    


}
