package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {
    private String movieBirthday;

    @Override
    public void initialize(ReleaseDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        movieBirthday = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value.isAfter(LocalDate.parse(movieBirthday));
    }
}
