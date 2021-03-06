package de.mlo.dev.validation.basic;

import org.jetbrains.annotations.NotNull;

/**
 * @author mlo
 */
public interface ValidationSummarizer {

    /**
     * Has to execute one or more parts of the validation process. The result
     * of all executed parts should be returned as an aggregated
     * {@link ValidationResult}.
     *
     * @return The result of the validation process which can contain zero,
     * one or more information about an executed part.
     */
    @NotNull
    ValidationResult validate();
}
