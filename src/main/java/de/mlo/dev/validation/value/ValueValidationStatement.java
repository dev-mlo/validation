package de.mlo.dev.validation.value;

import de.mlo.dev.validation.ValidationInfo;
import de.mlo.dev.validation.basic.ValidationResult;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link ValueValidationStatement} is the smallest part of a validation Process.
 *
 * @author mlo
 */
@FunctionalInterface
public interface ValueValidationStatement<V> extends ValueValidationSummarizer<V> {

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
     * {@link ValueValidationStatement Statements} can be grouped and chained by using a
     * {@link ValueValidator}
     *
     * @param value The value to validate
     * @return The information if the validation succeed or not. Check the
     * {@link ValidationInfo#getMessage()} if the validation failed. Returning
     * <code>null</code> will result in a {@link NullPointerException}
     */
    @NotNull
    ValidationInfo execute(V value);

    /**
     * Wraps the result of {@link #execute(Object)} in a {@link ValidationResult}
     * <hr>
     * {@inheritDoc}
     *
     * @param value The value to validate
     * @return A wrapped {@link ValidationInfo}. The result contains exactly one info
     */
    @NotNull
    @Override
    default ValidationResult validate(V value) {
        return new ValidationResult().add(execute(value));
    }
}
