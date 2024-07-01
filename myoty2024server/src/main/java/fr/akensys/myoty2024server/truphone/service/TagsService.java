package fr.akensys.myoty2024server.truphone.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import fr.akensys.myoty2024server.error.SimCardNotFoundException;
import fr.akensys.myoty2024server.truphone.entity.Tags;
import fr.akensys.myoty2024server.truphone.models.SimCardResponse;
import fr.akensys.myoty2024server.truphone.repository.SimCardRepo;
import fr.akensys.myoty2024server.truphone.repository.TagsRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@Service
@Transactional
@RequiredArgsConstructor
public class TagsService {

    private final SimCardRepo simCardRepo;
    private final TagsRepo tagsRepo;
    private final WebClient.Builder webClientBuilder;
    private final String TOKEN = "456554db3785c41d24bc16e8df3819a5d163ceb2";
    private static final String URL = "https://iot.truphone.com/api/";

    public void saveTagsFromApi() {
        webClientBuilder.build()
        .get()
        .uri(URL + "v2.0/tags")
        .header("Authorization", "Token " + TOKEN)
        .retrieve()
        .bodyToFlux(SimCardResponse.class)
        .flatMap((response) -> {
            return tagsRepo.findByLabel(response.getLabel())
                .map(existingDevice -> {
                    return Mono.empty();
                })
                .orElseGet(() -> {
                    Tags tags = buildTags(response);
                    tagsRepo.save(tags);
                    return Mono.empty();
                });
        })
        .blockLast();

    }

    private Tags buildTags(SimCardResponse response) {

        return Tags.builder()
        .label(response.getLabel())
        .description(response.getDescription())
        .build();
    }

    public List<Tags> getAllTagsInDb() {
        return tagsRepo.findAll();
    }

    public void createTag(Tags request) {
        Tags createdTag = webClientBuilder.build()
            .post()
            .uri(URL + "v2.0/tags")
            .header("Authorization", "Token " + TOKEN)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(Tags.class)
            .block(); 

        if (createdTag != null) {

            Tags tagsEntity = new Tags();

            tagsEntity.setLabel(request.getLabel());
            tagsEntity.setDescription(request.getDescription());
            tagsRepo.save(tagsEntity);
        }
    }


    public void deleteTag(String label)
    {
        webClientBuilder.build()
                .delete()
                .uri(URL + label)
                .header("Authorization", "Token " + TOKEN) 
                .retrieve()
                .toBodilessEntity()
                .block();

                Tags tagEntity = tagsRepo.findByLabel(label).orElseThrow(() -> new SimCardNotFoundException("Aucun tag trouvé avec ce libellé : " + label));
                tagsRepo.delete(tagEntity);
    }


    // public void assignTagToSimCard(String label, Tags simCards)
    // {

        
    //     tagsRepo.findByLabel(label).orElseThrow(() -> new SimCardNotFoundException("Aucun tag trouvé avec ce libellé : " + label));

    //     List<Long> iccids = simCards.getSimCards();
    //     for(Long iccid : iccids)
    //     {
    //         simCardRepo.findByIccid(iccid).orElseThrow(() -> new SimCardNotFoundException("Aucune carte SIM trouvé avec cet iccid : " + iccid));
        
    //      webClientBuilder.build()
    //         .post()
    //         .uri(URL + "v2.0/tags/" + label + "/sims")
    //         .header("Authorization", "Token " + TOKEN)
    //         .bodyValue(simCards)
    //         .retrieve()
    //         .bodyToMono(Tags.class)  
    //         .block();

    //         // List<String> newTag = simCard.getTags();
    //         //     newTag.add(label);
    //         //     simCard.setTags(newTag);

    //         //     simCardRepo.save(simCard);

    //     }
    // }

}
