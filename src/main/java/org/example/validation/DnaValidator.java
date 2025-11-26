package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.dto.DnaRequest;

public class DnaValidator implements ConstraintValidator<ValidDna, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String[] dna;
        if (value instanceof DnaRequest) {
            dna = ((DnaRequest) value).getDna();
        } else if (value instanceof String[]) {
            dna = (String[]) value;
        } else {
            return false;
        }

        if (dna == null || dna.length == 0) {
            return false;
        }

        int n = dna.length;
        if (n < 4) return false;

        for (String row : dna) {
            if (row == null || row.length() != n) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("debe ser NxN").addConstraintViolation();
                return false;
            }
            if (!row.matches("[ATCG]+")) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("solo A,T,C,G").addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}