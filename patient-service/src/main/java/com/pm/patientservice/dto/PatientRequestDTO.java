package com.pm.patientservice.dto;

import com.pm.patientservice.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatientRequestDTO {

    @NotBlank(message = "Name is Required!")
    @Size(max = 100, message = "Name Cannot Exceed 100 Characters")
    private String name;

    @NotBlank(message = "Email is Required!")
    @Email(message = "Email Should be valid")
    private String email;

    @NotBlank(message = "Address is Required")
    private String address;

    @NotBlank(message = "DOB is reequired")
    private String dateOfBirth;

    @NotBlank( groups = CreatePatientValidationGroup.class, message = "Registered date is required")
    private String registeredDate;


}
