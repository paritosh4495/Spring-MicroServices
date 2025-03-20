package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exceptions.EmailAlreadyExistsException;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toDTO).collect(Collectors.toList());
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {

        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A Patient with this email "+ "already Exists "+patientRequestDTO.getEmail());
        }
        Patient patient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        return PatientMapper.toDTO(patient);
    }

}
