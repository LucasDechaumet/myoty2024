package fr.akensys.myoty2024server.truphone.models;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimCardUpdate {

    private Long iccid; 
    private String label;
    private Long primaryMsisdn;
    private String rate_plan; 
    private String sim_status; 
    private boolean smsMo; 
    private boolean smsMt;
    private Long imei; 
    private String tags; 
    
}
