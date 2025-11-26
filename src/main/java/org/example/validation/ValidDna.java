package org.example.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DnaValidator.class)
@Documented
public @interface ValidDna {
    String message() default "ADN inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
