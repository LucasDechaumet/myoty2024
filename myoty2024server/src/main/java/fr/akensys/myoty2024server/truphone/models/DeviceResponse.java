package fr.akensys.myoty2024server.truphone.models;

import lombok.Data;

@Data
public class DeviceResponse {

    private String imei; 
    private String organization; 
    private String dateFirstRegistered; 
    private String dateLastestUpdate;
    private String label; 
    private String description; 
    private Long iccid;  

}
