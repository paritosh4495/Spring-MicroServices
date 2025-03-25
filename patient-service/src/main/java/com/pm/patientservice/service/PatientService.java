package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exceptions.EmailAlreadyExistsException;
import com.pm.patientservice.exceptions.PatientNotFoundException;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toDTO).collect(Collectors.toList());
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {

        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A Patient with this email " + "already Exists " + patientRequestDTO.getEmail());
        }
        Patient patient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));

        billingServiceGrpcClient.createBillingAccount(patient.getId().toString(), patient.getName(), patient.getEmail());
        return PatientMapper.toDTO(patient);
    }
//

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient with id " + id + " does not exists"));

        System.out.println("Patient " + patient + " is found and is ready for update");

        if (patientRequestDTO.getEmail() != null && !patientRequestDTO.getEmail().equals(patient.getEmail())) {

            if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
                throw new EmailAlreadyExistsException("A Patient with this email " + "already Exists " + patientRequestDTO.getEmail());
            } else {
                patient.setEmail(patientRequestDTO.getEmail());
            }
        }
        if (patientRequestDTO.getName() != null) {
            patient.setName(patientRequestDTO.getName());
        }
        if (patientRequestDTO.getAddress() != null) {
            patient.setAddress(patientRequestDTO.getAddress());
        }
        if (patientRequestDTO.getDateOfBirth() != null) {
            patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        }

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);

    }


    public void deletePatient(UUID id) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient with id " + id + " does not exists"));
        patientRepository.delete(patient);
    }

}
