package de.mlo.dev.validation.value.javax;

import de.mlo.dev.validation.ValidationMessage;
import de.mlo.dev.validation.value.IsValueValidator;
import de.mlo.dev.validation.value.ValueValidationResult;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author mlo
 */
public class ValidatorConstraintValidation implements ConstraintValidator<ValidatedBy, Object> {

    private ValidatedBy validatedBy;

    @SuppressWarnings("rawtypes")
    private final List<IsValueValidator> validators = new ArrayList<>();

    @Override
    public void initialize(ValidatedBy constraintAnnotation) {
        this.validators.clear();
        this.validatedBy = constraintAnnotation;
        Class<? extends IsValueValidator<?>>[] validatorClasses = this.validatedBy.processors();
        for (Class<? extends IsValueValidator<?>> validatorClass : validatorClasses) {
            this.validators.add(initValidator(validatorClass));
        }
    }

    private IsValueValidator<?> initValidator(Class<? extends IsValueValidator<?>> validatorClass) {
        try {
            return validatorClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new ValidatorInstantiationException(validatedBy, validatorClass, e);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Stream<IsValueValidator> stream = validators.stream();
        if (validatedBy.parallel() && validators.size() > 1) {
            stream = stream.parallel();
        }
        ValueValidationResult finalResult = stream.map(validator -> validator.validate(value))
                .reduce(new ValueValidationResult(value), ValueValidationResult::add);

        if (finalResult.isInvalid()) {
            context.disableDefaultConstraintViolation();
            for (ValidationMessage message : finalResult.getMessages()) {
                context.buildConstraintViolationWithTemplate(message.getText()).addConstraintViolation();
            }
        }
        return finalResult.isValid();
    }
}
