package de.mlo.dev.validation.value;

import de.mlo.dev.validation.ValidationResult;

/**
 * @author mlo
 */
public class ValueValidationResult<V> extends ValidationResult {

    private final V value;

    /**
     * Creates a new {@link ValueValidationResult} which contains validation info about the given value.
     *
     * @param value The value which is validated
     */
    public ValueValidationResult(V value) {
        this.value = value;
    }

    public static <V> ValueValidationResult<V> of(V value, ValidationResult result) {
        ValueValidationResult<V> wrapped = new ValueValidationResult<>(value);
        wrapped.add(result);
        return wrapped;
    }

    public V getValue() {
        return value;
    }
}
