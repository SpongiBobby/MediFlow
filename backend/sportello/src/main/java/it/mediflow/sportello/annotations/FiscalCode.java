package it.mediflow.sportello.annotations;

import it.mediflow.sportello.validators.FiscalCodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FiscalCodeValidator.class)
public @interface FiscalCode {
    String message() default "Fiscal code is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
