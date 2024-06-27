package fr.akensys.myoty2024server.truphone.models;

import lombok.Data;

@Data
public class CdrResponse {
    
    private String id;
    private String simCard;
    private String startDate;
    private String endDate;
    private int duration;
    private String origin;
    private String destination;
    private String country;
    private String network;
    private String type;
}