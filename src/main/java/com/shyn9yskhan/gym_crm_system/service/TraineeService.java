package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.dao.TraineeDAO;
import com.shyn9yskhan.gym_crm_system.dto.TraineeDTO;
import com.shyn9yskhan.gym_crm_system.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TraineeService {

    @Autowired
    private TraineeDAO traineeDAO;

    public Trainee createTrainee(TraineeDTO traineeDTO) {
        String firstname = traineeDTO.getFirstname();
        String lastname = traineeDTO.getLastname();
        String base = firstname + "." + lastname;
        String username = makeUniqueUsername(base);

        String password = RandomGenerator.generatePassword();
        boolean isActive = true;
        LocalDate dateOfBirth = traineeDTO.getDateOfBirth();
        String address = traineeDTO.getAddress();
        String userId = RandomGenerator.generateUserId();

        Trainee trainee = new Trainee(firstname, lastname, username, password, isActive, dateOfBirth, address, userId);
        return traineeDAO.createTrainee(trainee);
    }

    public boolean updateTrainee(String traineeId, TraineeDTO traineeDTO) {
        Trainee existing = traineeDAO.getTrainee(traineeId);
        if (existing == null) {
            return false;
        }

        String firstname = traineeDTO.getFirstname();
        String lastname  = traineeDTO.getLastname();
        existing.setFirstname(firstname);
        existing.setLastname(lastname);

        String base = firstname + "." + lastname;
        existing.setUsername(makeUniqueUsername(base));

        existing.setDateOfBirth(traineeDTO.getDateOfBirth());
        existing.setAddress(traineeDTO.getAddress());

        return traineeDAO.updateTrainee(traineeId, existing);
    }

    public boolean deleteTrainee(String traineeId) {
        return traineeDAO.deleteTrainee(traineeId);
    }

    public Trainee getTrainee(String traineeId) {
        return traineeDAO.getTrainee(traineeId);
    }


    private String makeUniqueUsername(String base) {
        if (!traineeDAO.existsByUsername(base)) {
            return base;
        }

        List<String> similar = traineeDAO.findUsernamesByBase(base);
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