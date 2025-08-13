package com.shyn9yskhan.gym_crm_system.dto;

import java.time.LocalDate;

public class UpdateTraineeRequest {
    private LocalDate dateOfBirth;
    private String address;

    public UpdateTraineeRequest() {
    }

    public UpdateTraineeRequest(LocalDate dateOfBirth, String address) {
        this.dateOfBirth = dateOfBirth;
        this.address = address;
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
