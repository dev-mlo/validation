package de.mlo.dev.validation.value;

public interface IsValueValidator<V>{

    ValueValidationResult<V> validate(V value);
}
