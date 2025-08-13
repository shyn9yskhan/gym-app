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
import jakarta.persistence.JoinTable;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trainee")
public class TraineeEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @org.hibernate.annotations.GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private Set<TrainerEntity> trainers = new HashSet<>();

    public TraineeEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Set<TrainerEntity> getTrainers() {
        return trainers;
    }

    public void setTrainers(Set<TrainerEntity> trainers) {
        this.trainers = trainers;
    }

    public void addTrainer(TrainerEntity trainer) {
        if (trainer == null) return;
        if (trainers == null) trainers = new HashSet<>();
        trainers.add(trainer);
        if (trainer.getTrainees() == null) trainer.setTrainees(new HashSet<>());
        trainer.getTrainees().add(this);
    }

    public void removeTrainer(TrainerEntity trainer) {
        if (trainer == null || trainers == null) return;
        trainers.remove(trainer);
        if (trainer.getTrainees() != null) trainer.getTrainees().remove(this);
    }
}
