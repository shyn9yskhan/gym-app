package com.shyn9yskhan.gym_crm_system;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shyn9yskhan.gym_crm_system.model.Trainee;
import com.shyn9yskhan.gym_crm_system.model.Trainer;
import com.shyn9yskhan.gym_crm_system.model.Training;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class DataLoader {

    @Value("${storage.trainee.file}")
    private Resource traineeFile;

    @Value("${storage.trainer.file}")
    private Resource trainerFile;

    @Value("${storage.training.file}")
    private Resource trainingFile;

    private final Map<String, Trainee> traineeStore;
    private final Map<String, Trainer> trainerStore;
    private final Map<String, Training> trainingStore;
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    public DataLoader(
            @Qualifier("traineeStorage") Map<String, Trainee> traineeStore,
            @Qualifier("trainerStorage") Map<String, Trainer> trainerStore,
            @Qualifier("trainingStorage") Map<String, Training> trainingStore
    ) {
        this.traineeStore  = traineeStore;
        this.trainerStore  = trainerStore;
        this.trainingStore = trainingStore;
    }

    @PostConstruct
    public void init() {
        loadEntities(traineeFile, new TypeReference<List<Trainee>>() {}, traineeStore, "getUserId");
        loadEntities(trainerFile, new TypeReference<List<Trainer>>() {}, trainerStore, "getUserId");
        loadEntities(trainingFile, new TypeReference<List<Training>>() {}, trainingStore, "getTrainingId");
    }

    private <T> void loadEntities(
            Resource resource,
            TypeReference<List<T>> type,
            Map<String, T> store,
            String idGetter
    ) {
        try (InputStream is = resource.getInputStream()) {
            List<T> items = mapper.readValue(is, type);
            for (T item : items) {
                String id = (String) item.getClass().getMethod(idGetter).invoke(item);
                store.put(id, item);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load " + resource.getFilename(), e);
        }
    }
}