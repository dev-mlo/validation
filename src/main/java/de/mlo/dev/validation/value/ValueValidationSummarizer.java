package de.mlo.dev.validation.value;

import de.mlo.dev.validation.basic.ValidationResult;
import org.jetbrains.annotations.NotNull;

/**
 * @author mlo
 */
@FunctionalInterface
public interface ValueValidationSummarizer<V> {

    /**
     * Has to execute one or more parts of the validation process. The result
     * of all executed parts should be returned as an aggregated
     * {@link ValidationResult}.
     *
     * @param value The value to validate.
     * @return The result of the validation process which can contain zero,
     * one or more information about an executed part.
     */
    @NotNull
    ValidationResult validate(V value);
}
