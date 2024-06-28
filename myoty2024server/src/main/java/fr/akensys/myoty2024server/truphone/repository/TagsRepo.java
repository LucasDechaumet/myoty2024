package fr.akensys.myoty2024server.truphone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.akensys.myoty2024server.truphone.entity.TagsEntity;
import java.util.Optional;


public interface TagsRepo extends JpaRepository<TagsEntity, Long>{

    Optional<TagsEntity> findByLabel(String label);
}
