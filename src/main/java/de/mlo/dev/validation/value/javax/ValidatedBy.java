package de.mlo.dev.validation.value.javax;

import de.mlo.dev.validation.value.IsValueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
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

    Class<? extends IsValueValidator<?>> processor();
}
