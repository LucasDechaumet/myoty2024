package fr.akensys.myoty2024server.truphone.models;

import java.util.List;

import lombok.Data;

@Data
public class SMS_Info {

    private Long id;
    private String dateSubmitted; 
    private String content; 
    private List<DeliveryReport> deliveryReport;

    @Data
    public static class DeliveryReport
    {
        private String iccid; 
        private String deliveryStatus;
    }
}
