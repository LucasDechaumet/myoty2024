package fr.akensys.myoty2024server.truphone.models;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class SimCardResponse {

    private Long iccid;
    private String label;
    private String description;
    private String simType;
    private Long primaryMsisdn;
    private List<String> msisdns;
    private String primaryImsi;
    private List<String> imsis;
    private Map<String, String> gsmKeys;
    private Dates dates;
    private String imei;
    private Subscription subscription;
    private List<String> attributes;
    private List<TagsReponse> tags;
    private LocationTag location_tag;
    private String date;
    private String ipAddress;
    private Status status; 

    @Data
    public static class Dates {
        private String provisionDate;
        private String shippingDate;
        private String deliveryDate;
        private String firstActivationDate;
    }

    @Data
    public static class Subscription {
        private BearerServices bearerServices;
        private String servicePackId;
        private String subscriptionStatus;
        private Apn apn;
    }

    @Data
    public static class BearerServices {
        private String voiceMo;
        private String voiceMt;
        private String smsMo;
        private String smsMt;
        private String gprs;
        private String csd;
        private String ussd;
    }

    @Data
    public static class Apn {
        private String name;
        private String description;
    }

    @Data
    public static class TagsReponse
    {
        private List<String> simCards;
        private String label;
        private String description;
    }

    @Data
    public static class LocationTag
    {
        private String label; 
        private String lng; 
        private String lat; 
    }

    @Data
    public static class Status
    {
        private String sim_card_service;
        private String data_service; 
        private String sms_mo_service;
        private String sms_mt_service;
    }
    
}

