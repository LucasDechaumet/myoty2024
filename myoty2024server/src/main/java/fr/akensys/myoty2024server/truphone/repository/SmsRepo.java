package fr.akensys.myoty2024server.truphone.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import fr.akensys.myoty2024server.truphone.entity.SMS_History;

public interface SmsRepo extends JpaRepository<SMS_History, Long> {

    Optional<SMS_History> findById(Long id); 
}
