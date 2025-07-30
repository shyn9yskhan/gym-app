package com.shyn9yskhan.gym_crm_system.dto;

import java.time.LocalDate;

public class TraineeDto {
    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;
    private String address;

    public TraineeDto() {
    }

    public TraineeDto(String firstname, String lastname, LocalDate dateOfBirth, String address) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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
}
