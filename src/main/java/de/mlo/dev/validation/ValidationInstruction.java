package de.mlo.dev.validation;

/**
 * @author mlo
 */
@FunctionalInterface
public interface ValidationInstruction {

    /**
     * The implementation must contain any validation logic. If the validation fails
     * return a {@link ValidationInfo} with a message, which describes the error. Use
     * the {@link ValidationInfo#invalid(String)} function in this case.<br>
     * If the validation was successful you can use the {@link ValidationInfo#valid()}
     * function. This creates an information object with no message (usually the message
     * is not important if the validation succeed).
     *
     * @return The information if the validation succeed or not. Check the
     * {@link ValidationInfo#getMessage()} if the validation failed.
     */
    ValidationInfo validate();
}
