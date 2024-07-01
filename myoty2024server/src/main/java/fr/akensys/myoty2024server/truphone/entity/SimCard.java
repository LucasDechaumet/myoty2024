package fr.akensys.myoty2024server.truphone.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

    @UpdateTimestamp
    private LocalDateTime updated_at;

    @Column(unique = true)
    private Long iccid;  

    private String label;

    private String SimStatus; 

    private String DataStatus; 

    private String Sms_Mo_status; 

    private String Sms_Mt_status;

    @OneToOne
    @JoinColumn(name = "teltonika_device_id")
    @JsonManagedReference
    private TeltonikaDevice teltonikaDevice;

    private String deviceName;

    private List<String> tags;

    private String provision_Date;

    private String Date_first_Activated;

    private String last_seen_date; 

    private String IP_address;

    

}

