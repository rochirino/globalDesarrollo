package org.example.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DnaValidator implements ConstraintValidator<ValidDna, String[]> {

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {

        if (dna == null || dna.length == 0)
            return false;

        int n = dna.length;

        for (String row : dna) {
            if (row == null || row.length() != n) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("La matriz debe ser NxN")
                        .addConstraintViolation();
                return false;
            }

            if (!row.matches("[ATCG]+")) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Solo se permiten caracteres A,T,C,G")
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}
