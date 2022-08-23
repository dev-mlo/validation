package de.mlo.dev.validation.value.javax;

import java.text.MessageFormat;

/**
 * @author mlo
 */
public class ValidatorInstantiationException extends RuntimeException{
    public ValidatorInstantiationException(ValidatedBy validatedBy, Class<?> validatorClass, Throwable parent){
        super(MessageFormat.format("Unable to create an instance of the declared validator '{0}'. " +
                "The Validator must have default constructor.", validatorClass), parent);
    }
}
