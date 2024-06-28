package fr.akensys.myoty2024server.truphone.models;

import java.util.List;

import lombok.Data;

@Data
public class SimCardUpdateStatus {

        private List<Long> iccid;
        private String status;

}
