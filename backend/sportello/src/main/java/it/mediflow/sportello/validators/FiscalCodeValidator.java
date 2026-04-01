package it.mediflow.sportello.validators;

import it.mediflow.sportello.annotations.FiscalCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FiscalCodeValidator implements ConstraintValidator<FiscalCode, String> {

    // Valida la struttura del CF: 6 lettere (cognome+nome), 2 cifre (anno),
    // 1 lettera (mese), 2 cifre (giorno+sesso), 1 lettera + 3 cifre (comune), 1 lettera (controllo)
    private static final String REGEX_CF = "^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) return true; // il null verrà gestito dal @NotNull

        return value.toUpperCase().trim().matches(REGEX_CF);
    }
}
