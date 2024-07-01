package fr.akensys.myoty2024server.truphone.models;

import java.util.List;

import lombok.Data;

@Data
public class SmsCommand {

    private List<Long> iccid;
    String text; 
}
