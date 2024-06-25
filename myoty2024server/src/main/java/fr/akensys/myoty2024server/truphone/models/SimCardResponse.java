package fr.akensys.myoty2024server.truphone.models;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class SimCardResponse {

    private String iccid;
    private String label;
    private String description;
    private String simType;
    private String primaryMsisdn;
    private List<String> msisdns;
    private String primaryImsi;
    private List<String> imsis;
    private Map<String, String> gsmKeys;
    private Dates dates;
    private String imei;
    private Subscription subscription;
    private List<String> attributes;
    private List<Tags> tags;
    private String location_tag;

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
        private boolean voiceMo;
        private boolean voiceMt;
        private boolean smsMo;
        private boolean smsMt;
        private boolean gprs;
        private boolean csd;
        private boolean ussd;
    }

    @Data
    public static class Apn {
        private String name;
        private String description;
    }

    @Data
    public static class Tags 
    {
        private List<String> simCards;
        private String label;
        private String description;
    }
}

