package fr.akensys.myoty2024server.truphone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.akensys.myoty2024server.truphone.entity.Tags;
import java.util.Optional;


public interface TagsRepo extends JpaRepository<Tags, Long>{

    Optional<Tags> findByLabel(String label);
}
