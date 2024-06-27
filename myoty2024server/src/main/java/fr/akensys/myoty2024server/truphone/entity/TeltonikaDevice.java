package fr.akensys.myoty2024server.truphone.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "teltonika_device", uniqueConstraints = @UniqueConstraint(columnNames = "imei"))
public class TeltonikaDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 50)
    private String imei ; 

    @Size(max=20)
    private String simCard;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created_at; 

    @UpdateTimestamp
    private LocalDateTime updated_at;

    private String date_first_registered;

    
    @Size(max=100)
    private String manufacturer;

    @Size(max=50) 
    private String model;

    private String name;

    private String category;

    private double latitude; 

    private double longitude;

    private double speed;

}
