package fr.akensys.myoty2024server.truphone.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fr.akensys.myoty2024server.truphone.entity.SmsHistory;
import fr.akensys.myoty2024server.truphone.entity.Tags;
import fr.akensys.myoty2024server.truphone.entity.SimCard;
import fr.akensys.myoty2024server.truphone.entity.TeltonikaDevice;
import fr.akensys.myoty2024server.truphone.models.SmsCommand;
import fr.akensys.myoty2024server.truphone.models.DeviceUpdateInfo;
import fr.akensys.myoty2024server.truphone.models.SimCardUpdateInfo;
import fr.akensys.myoty2024server.truphone.models.SimCardUpdateStatus;
import fr.akensys.myoty2024server.truphone.service.DeviceService;
import fr.akensys.myoty2024server.truphone.service.SimCardService;
import fr.akensys.myoty2024server.truphone.service.SmsService;
import fr.akensys.myoty2024server.truphone.service.TagsService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/truphone")
public class TruphoneController {

    public final SimCardService simCardService;
    public final TagsService tagsService;
    public final DeviceService deviceService;
    public final SmsService smsService;


    //SimCard Controller
    @PutMapping("/updateDataBase")
    public ResponseEntity<List<SimCard>> getSimCards()
    {
        getDevices();
        getTags();
       simCardService.saveSimCardsFromApi();
       simCardService.updateAllSimCardsWithLatestSeenDate();
       simCardService.updateAllSimCardWithDataStatus();
       simCardService.updateAdressIp();
       List<SimCard> simCards =  simCardService.getAllSimCardsInDb();
       
        return ResponseEntity.ok(simCards);
    }

    @PostMapping("/modifySimCard")
    public ResponseEntity<String> setSimCard(@RequestBody SimCardUpdateInfo request) {
        simCardService.updateSimCard(request);
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



    //Tags Controller
    @PostMapping("/getTags")
    public ResponseEntity<List<Tags>> getTags() {
        tagsService.saveTagsFromApi();
        List<Tags> tags = tagsService.getAllTagsInDb();
        return ResponseEntity.ok(tags);
    }

    @PostMapping("/createTag")
    public ResponseEntity<String> createTag(@RequestBody Tags request) {
        tagsService.createTag(request);
        getTags();
        return ResponseEntity.ok(" Tag created successfully" + request);
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
    


    //Device Controller
    @PostMapping("/getDevices")
    public ResponseEntity<List<TeltonikaDevice>> getDevices()
    {
       deviceService.saveDeviceFromApi();
       List<TeltonikaDevice> teltonikaDevices=  deviceService.getAllDevicesInDb();
        return ResponseEntity.ok(teltonikaDevices);
    }

    @PostMapping("/modifyDevice")
    public ResponseEntity<String> setSimCard(@RequestBody DeviceUpdateInfo request) {
        deviceService.updateSimCard( request);
        return ResponseEntity.ok("Device updated");
    }


    //Sms Controller
    @PostMapping("/sendSms")
    public ResponseEntity<String> sendSms(@RequestBody SmsCommand request) {
        
        smsService.sendSms(request);
        return ResponseEntity.ok((" sms sent : " + request));
    }

    @PostMapping("/getSmsHistory")
    public ResponseEntity<List<SmsHistory>> getSmsHistory() {

        smsService.getSmsHistory();
       List<SmsHistory> sms_Histories =  smsService.getAllSmsInDb();
        return ResponseEntity.ok(sms_Histories);
    }
    
    
}
