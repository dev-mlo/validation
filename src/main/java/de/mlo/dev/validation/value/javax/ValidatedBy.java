package de.mlo.dev.validation.value.javax;

import de.mlo.dev.validation.value.IsValueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author mlo
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {ValidatorConstraintValidation.class})
public @interface ValidatedBy {
    String message() default "Error";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    Class<? extends IsValueValidator<?>>[] processors();

    /**
     * If multiple validators are defined the execution can be done parallel
     *
     * @return true, if the given {@link #processors()} should be executed parallel
     */
    boolean parallel() default false;
}
