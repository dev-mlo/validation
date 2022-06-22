package de.mlo.dev.validation.basic;

import de.mlo.dev.validation.ValidationInfo;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link ValidationStatement} is the smallest part of a validation Process.
 *
 * @author mlo
 */
@FunctionalInterface
public interface ValidationStatement extends ValidationSummarizer {

    /**
     * The implementation must contain any validation logic. If the validation fails
     * return a {@link ValidationInfo} with a message, which describes the error. Use
     * the {@link ValidationInfo#invalid(String)} function in this case.<br>
     * If the validation was successful you can use the {@link ValidationInfo#valid()}
     * function. This creates an information object with no message (usually the message
     * is not important if the validation succeed, but it may help with debugging).<br>
     * Example:
     * <pre>{@code
     * public ValidationInfo validate(){
     *     String name = unbindName();
     *     if (name.isBlank()){
     *         return ValidationInfo.invalid("Name is blank");
     *     }
     *     return ValidationInfo.valid();
     * }
     * }</pre>
     * {@link ValidationStatement Statements} can be grouped and chained by using a
     * {@link Validator}
     *
     * @return The information if the validation succeed or not. Check the
     * {@link ValidationInfo#getMessage()} if the validation failed. Returning
     * <code>null</code> will result in a {@link NullPointerException}
     */
    @NotNull
    ValidationInfo execute();

    /**
     * Wraps the result of {@link #execute()} in a {@link ValidationResult}
     * <hr>
     * {@inheritDoc}
     *
     * @return A wrapped {@link ValidationInfo}. The result contains exactly one
     * info
     */
    @NotNull
    @Override
    default ValidationResult validate() {
        return new ValidationResult().add(execute());
    }
}
