package de.mlo.dev.validation.value;

import de.mlo.dev.validation.ValidationInfo;
import de.mlo.dev.validation.basic.ValidationResult;

import java.util.Collection;

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

    @SuppressWarnings("unchecked")
    @Override
    public ValueValidationResult<V> add(ValidationResult validationResult) {
        return (ValueValidationResult<V>) super.add(validationResult);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ValueValidationResult<V> add(ValidationInfo validationInfo) {
        return (ValueValidationResult<V>) super.add(validationInfo);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ValueValidationResult<V> add(ValidationInfo first, ValidationInfo... more) {
        return (ValueValidationResult<V>) super.add(first, more);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ValueValidationResult<V> add(Collection<ValidationInfo> validationInfos) {
        return (ValueValidationResult<V>) super.add(validationInfos);
    }
}
