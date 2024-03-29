package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = {ReleaseDateValidator.class})
public @interface ReleaseDate {
    String value();
    String message() default "Дата релиза должна быть позже дня рождения кино";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
