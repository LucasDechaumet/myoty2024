package fr.akensys.myoty2024server.truphone.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created_at;
    @CreationTimestamp
    private LocalDateTime updated_at;

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

