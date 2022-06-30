package de.mlo.dev.validation.value.javax;

import de.mlo.dev.validation.ValidationMessage;
import de.mlo.dev.validation.basic.ValidationResult;
import de.mlo.dev.validation.value.IsValueValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;

/**
 * @author mlo
 */
public class ValidatorConstraintValidation implements ConstraintValidator<ValidatedBy, Object> {

    private ValidatedBy validatedBy;

    @SuppressWarnings("rawtypes")
    private IsValueValidator validator;

    @Override
    public void initialize(ValidatedBy constraintAnnotation) {
        try {
            this.validatedBy = constraintAnnotation;
            Class<? extends IsValueValidator<?>> validatorClass = this.validatedBy.processor();
            this.validator = validatorClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if( value == null) {
            return true;
        }

        ValidationResult result = validator.validate(value);
        if(result.isInvalid()){
            context.disableDefaultConstraintViolation();
            for (ValidationMessage message : result.getMessages()) {
                context.buildConstraintViolationWithTemplate(message.getText()).addConstraintViolation();
            }
        }
        return result.isValid();
    }
}
