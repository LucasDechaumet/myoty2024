package fr.akensys.myoty2024server.truphone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.akensys.myoty2024server.truphone.entity.TeltonikaDevice;


public interface DeviceRepo extends JpaRepository<TeltonikaDevice, Long> {

    Optional<TeltonikaDevice> findByImei(String imei);

}
