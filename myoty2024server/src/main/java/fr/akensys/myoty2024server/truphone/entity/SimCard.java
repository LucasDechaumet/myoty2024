package fr.akensys.myoty2024server.truphone.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "sim_card", uniqueConstraints = @UniqueConstraint(columnNames = "iccid"))
public class SimCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created_at;

    @CreationTimestamp
    private LocalDateTime updated_at;

    @Column(unique = true)
    private Long iccid;  

    private String label;

    private Long primaryMsisdn;

    private String rate_plan;

    private String sim_status;

    private String smsMo;

    private String smsMt;

    private Long device;

    private List<String> tags;

    private String country;
    
    private String network;

}

