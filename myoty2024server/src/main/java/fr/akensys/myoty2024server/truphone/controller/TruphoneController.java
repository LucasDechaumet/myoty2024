package fr.akensys.myoty2024server.truphone.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fr.akensys.myoty2024server.truphone.entity.SimCard;
import fr.akensys.myoty2024server.truphone.models.SimCardUpdateInfo;
import fr.akensys.myoty2024server.truphone.models.SimCardUpdateStatus;
import fr.akensys.myoty2024server.truphone.models.SimCardResponse.Tags;
import fr.akensys.myoty2024server.truphone.service.SimCardService;
import fr.akensys.myoty2024server.truphone.service.TagsService;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/truphone")
public class TruphoneController {

    public final SimCardService simCardService;
    public final TagsService tagsService;


    @PostMapping("/getSimCards")
    public ResponseEntity<List<SimCard>> getSimCards()
    {
       simCardService.saveSimCardsFromApi();
       List<SimCard> simCards =  simCardService.getAllSimCardsInDb();
        return ResponseEntity.ok(simCards);
    }

@PostMapping("/updateAllSimCardCdr")
    public ResponseEntity<String> updateSimCardCdr() {
        simCardService.updateAllSimCardsWithLatestCdrData();
        return ResponseEntity.ok("SimCard updated with latest CDR data");
    }

    @PatchMapping("/setSimCard/{iccid}")
    public ResponseEntity<String> setSimCard(@PathVariable Long iccid, @RequestBody SimCardUpdateInfo request) {
        simCardService.updateSimCard(iccid, request);
        return ResponseEntity.ok("SimCard updated");
    }
    
    @PostMapping("/changeSimStatus")
    public ResponseEntity<String> updateSimStatus(@RequestBody SimCardUpdateStatus request) {
        simCardService.changeSimCardStatus(request);
        return ResponseEntity.ok("simCard Status Updated");
    }

    @PostMapping("/changeDataStatus")
    public ResponseEntity<String> updateDataStatus(@RequestBody SimCardUpdateStatus request) {
        simCardService.changeSimCardDataStatus(request);
        return ResponseEntity.ok("Data Status Updated");
    }

    @PostMapping("/createTag")
    public ResponseEntity<String> createTag(@RequestBody Tags request) {

        tagsService.createTag(request);
        return ResponseEntity.ok(" Tag created successfully");
    }

    @DeleteMapping("/deleteTag/{label}")
    public ResponseEntity<String> deleteTag(@PathVariable String label)
    {
        tagsService.deleteTag(label);
        return ResponseEntity.ok("tag: " + label + " deleted" );
    }
    
    // @PostMapping("/assignTagToSim/{label}")
    // public ResponseEntity<String> assignTagToSimCard(@PathVariable String label, @RequestBody Tags request) {
        
    //     tagsService.assignTagToSimCard(label, request);
    //     return ResponseEntity.ok("tag:" + label + " is assigned");
    // }
    

}
