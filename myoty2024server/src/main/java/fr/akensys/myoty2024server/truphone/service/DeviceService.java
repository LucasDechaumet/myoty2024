package fr.akensys.myoty2024server.truphone.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import fr.akensys.myoty2024server.truphone.entity.TeltonikaDevice;
import fr.akensys.myoty2024server.truphone.models.DeviceResponse;
import fr.akensys.myoty2024server.truphone.repository.DeviceRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepo deviceRepo; 
    private final WebClient.Builder webClientBuilder;
    private final String TOKEN = "456554db3785c41d24bc16e8df3819a5d163ceb2";
    private static final String URL = "https://iot.truphone.com/api/";

    public void saveDeviceFromApi() {
        webClientBuilder.build()
        .get()
        .uri(URL + "v2.2/devices")
        .header("Authorization", "Token " + TOKEN)
        .retrieve()
        .bodyToFlux(DeviceResponse.class)
        .flatMap((response) -> {
            return deviceRepo.findByImei(response.getImei())
                .map(existingDevice -> {
                    return Mono.empty();
                })
                .orElseGet(() -> {
                    TeltonikaDevice teltonikaDevice = buildDevice(response);
                    deviceRepo.save(teltonikaDevice);
                    return Mono.empty();
                });
        })
        .blockLast();

    }

    private TeltonikaDevice buildDevice(DeviceResponse response) {


        return TeltonikaDevice.builder()
        .imei(response.getImei())
        .name(response.getLabel())

        .date_first_registered(response.getDateFirstRegistered())
        .build();
    }

    public List<TeltonikaDevice> getAllDevicesInDb() {
        return deviceRepo.findAll();
    }

}
