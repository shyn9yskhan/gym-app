package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.dao.TrainerDAO;
import com.shyn9yskhan.gym_crm_system.dto.TrainerDTO;
import com.shyn9yskhan.gym_crm_system.model.Trainer;
import com.shyn9yskhan.gym_crm_system.model.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {

    @Autowired
    TrainerDAO trainerDAO;

    public Trainer createTrainer(TrainerDTO trainerDTO) {
        String firstname = trainerDTO.getFirstname();
        String lastname = trainerDTO.getLastname();
        String base = firstname + "." + lastname;
        String username = makeUniqueUsername(base);

        String password = RandomGenerator.generatePassword();
        boolean isActive = true;
        String trainingTypeName = trainerDTO.getTrainingTypeName();
        TrainingType trainingType = new TrainingType(trainingTypeName);
        String userId = RandomGenerator.generateUserId();

        Trainer trainer = new Trainer(firstname, lastname, username, password, isActive, trainingType, userId);
        return trainerDAO.createTrainer(trainer);
    }

    public boolean updateTrainer(String trainerId, TrainerDTO trainerDTO) {
        Trainer existing = trainerDAO.getTrainer(trainerId);
        if (existing == null) {
            return false;
        }

        String firstname = trainerDTO.getFirstname();
        String lastname = trainerDTO.getLastname();
        String trainingTypeName = trainerDTO.getTrainingTypeName();
        TrainingType trainingType = new TrainingType(trainingTypeName);
        String base = firstname + "." + lastname;
        String username = makeUniqueUsername(base);

        existing.setFirstname(firstname);
        existing.setLastname(lastname);
        existing.setUsername(username);
        existing.setSpecialization(trainingType);

        return trainerDAO.updateTrainer(trainerId, existing);
    }

    public Trainer getTrainer(String trainerId) {
        return trainerDAO.getTrainer(trainerId);
    }

    private String makeUniqueUsername(String base) {
        if (!trainerDAO.existsByUsername(base)) {
            return base;
        }

        List<String> similar = trainerDAO.findUsernamesByBase(base);
        int max = similar.stream()
                .map(u -> u.substring(base.length()))
                .map(s -> {
                    if (s.isEmpty()) return 0;
                    try { return Integer.parseInt(s); }
                    catch (NumberFormatException ex) { return 0; }
                })
                .max(Integer::compareTo)
                .orElse(0);

        return base + (max + 1);
    }
}