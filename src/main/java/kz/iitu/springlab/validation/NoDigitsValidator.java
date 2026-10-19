package kz.iitu.springlab.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoDigitsValidator implements ConstraintValidator<NoDigits, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Пропускаем null, так как для пустых строк есть @NotBlank
        }
        // Возвращает false (выдает ошибку), если в строке есть хотя бы одна цифра
        return !value.matches(".*\\d.*");
    }
}