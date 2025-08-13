package com.shyn9yskhan.gym_crm_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trainer")
public class TrainerEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @org.hibernate.annotations.GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "specialization_id", nullable = false)
    private TrainingTypeEntity specialization;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToMany(mappedBy = "trainers", fetch = FetchType.LAZY)
    private Set<TraineeEntity> trainees = new HashSet<>();

    public TrainerEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TrainingTypeEntity getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingTypeEntity specialization) {
        this.specialization = specialization;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Set<TraineeEntity> getTrainees() {
        return trainees;
    }

    public void setTrainees(Set<TraineeEntity> trainees) {
        this.trainees = trainees;
    }

    public void addTrainee(TraineeEntity trainee) {
        if (trainee == null) return;
        if (trainees == null) trainees = new HashSet<>();
        trainees.add(trainee);
        if (trainee.getTrainers() == null) trainee.setTrainers(new HashSet<>());
        trainee.getTrainers().add(this);
    }

    public void removeTrainee(TraineeEntity trainee) {
        if (trainee == null || trainees == null) return;
        trainees.remove(trainee);
        if (trainee.getTrainers() != null) trainee.getTrainers().remove(this);
    }
}
