package fr.akensys.myoty2024server.truphone.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import fr.akensys.myoty2024server.truphone.entity.SimCard;


public interface SimCardRepo extends JpaRepository<SimCard, Long>{

    Optional<SimCard> findByIccid(Long iccid);
}
