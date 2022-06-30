package de.mlo.dev.validation.value;

import de.mlo.dev.validation.basic.ValidationResult;

public interface IsValueValidator<V>{

    ValueValidationResult<V> validate(V value);
}
