package fr.akensys.myoty2024server.truphone.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import fr.akensys.myoty2024server.error.SimCardNotFoundException;
import fr.akensys.myoty2024server.truphone.entity.TagsEntity;
import fr.akensys.myoty2024server.truphone.models.SimCardResponse.Tags;
import fr.akensys.myoty2024server.truphone.repository.SimCardRepo;
import fr.akensys.myoty2024server.truphone.repository.TagsRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class TagsService {

    private final SimCardRepo simCardRepo;
    private final TagsRepo tagsRepo;
    private final WebClient.Builder webClientBuilder;
    private final String TOKEN = "456554db3785c41d24bc16e8df3819a5d163ceb2";
    private static final String URL = "https://iot.truphone.com/api/";

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

            TagsEntity tagsEntity = new TagsEntity();

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

                TagsEntity tagEntity = tagsRepo.findByLabel(label).orElseThrow(() -> new SimCardNotFoundException("Aucun tag trouvé avec ce libellé : " + label));
                tagsRepo.delete(tagEntity);
    }


    // TODO: voir avec lucas 
    // public void assignTagToSimCard(String label, Tags simCards)
    // {

    //     List<Long> iccids = simCards.getSimCards();
    //     TagsEntity tagEntity = tagsRepo.findByLabel(label).orElseThrow(() -> new SimCardNotFoundException("Aucun tag trouvé avec ce libellé : " + label));

    //     for(Long iccid : iccids)
    //     {
    //         SimCard simCard = simCardRepo.findByIccid(iccid).orElseThrow(() -> new SimCardNotFoundException("Aucune carte SIM trouvé avec cet iccid : " + iccid));
        
    //      webClientBuilder.build()
    //         .post()
    //         .uri(URL + "v2.0/tags/" + label + "/sims")
    //         .header("Authorization", "Token " + TOKEN)
    //         .bodyValue(simCards)
    //         .retrieve()
    //         .bodyToMono(Tags.class)  
    //         .block();

    //         List<String> newTag = simCard.getTags();
    //             newTag.add(label);
    //             simCard.setTags(newTag);
    //             simCardRepo.save(simCard);

    //     }
    // }

}
