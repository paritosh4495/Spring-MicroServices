package com.pm.patientservice.exceptions;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(String s) {
        super(s);
    }
}
