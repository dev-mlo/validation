package de.mlo.dev.validation;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * The {@link ValidationInfo} contains the result of a validation process.
 * Multiple {@link ValidationInfo}s can be aggregated with the {@link ValidationResult}.<br>
 * To chain and group validation processes you can use the {@link Validator}.
 *
 * @author mlo
 */
public class ValidationInfo {

    private static final Supplier<String> NO_MESSAGE = () -> null;
    private final boolean valid;
    private final Supplier<String> message;

    /**
     * Creates a new {@link ValidationInfo}. You can also use the {@link #valid()}
     * or {@link #invalid(String)} function to create a new info object.
     *
     * @param valid   true, if the validation process was successful
     * @param message A detailed message if the validation failed. If the validation
     *                was successful the message usually is not necessary.
     */
    public ValidationInfo(boolean valid, String message) {
        this(valid, () -> message);
    }

    /**
     * Creates a new {@link ValidationInfo}. You can also use the {@link #valid()}
     * or {@link #invalid(String)} function to create a new info object.
     *
     * @param valid   true, if the validation process was successful
     * @param message A detailed message if the validation failed. If the validation
     *                was successful the message usually is not necessary.
     */
    public ValidationInfo(boolean valid, Supplier<String> message) {
        this.valid = valid;
        this.message = Objects.requireNonNullElse(message, NO_MESSAGE);
    }

    /**
     * Creates a new {@link ValidationInfo} which indicates that the validation
     * process was successful --> {@link #isValid()} returns <code>true</code>.<br>
     * The message will be <code>null</code> in this case. If you would like to add
     * a message, you can use {@link #valid(String)} und this is usually not
     * necessary.
     *
     * @return A new {@link ValidationInfo} which indicates that the validation
     * process was successful
     */
    public static ValidationInfo valid() {
        return new ValidationInfo(true, NO_MESSAGE);
    }

    /**
     * Creates a new {@link ValidationInfo} which indicates that the validation
     * process was successful --> {@link #isValid()} returns <code>true</code>.
     * You also can add a message but this is usually not necessary if a
     * validation was successful.
     *
     * @param message A detailed message of the validation process
     * @return A new {@link ValidationInfo} which indicates that the validation
     * process was successful
     */
    public static ValidationInfo valid(String message) {
        return new ValidationInfo(true, message);
    }

    /**
     * Creates a new {@link ValidationInfo} which indicates that the validation
     * process was successful --> {@link #isValid()} returns <code>true</code>.
     * You also can add a message but this is usually not necessary if a
     * validation was successful.
     *
     * @param message A detailed message of the validation process
     * @return A new {@link ValidationInfo} which indicates that the validation
     * process was successful
     */
    public static ValidationInfo valid(Supplier<String> message) {
        return new ValidationInfo(true, message);
    }

    /**
     * Creates a new {@link ValidationInfo} which indicates the the validation
     * process was <b>successful</b> --> {@link #isValid()} returns
     * <code>false</code>.<br>
     * In this case you should give a detailed messages what went wrong. It
     * is not mandatory (you can pass <code>null</code>) but it is recommended.
     *
     * @param message A detailed message, what went wrong
     * @return A new {@link ValidationInfo} which indicates that the validation
     * process was <b>not</b> successful
     */
    public static ValidationInfo invalid(String message) {
        return new ValidationInfo(false, message);
    }

    /**
     * Creates a new {@link ValidationInfo} which indicates the the validation
     * process was <b>successful</b> --> {@link #isValid()} returns
     * <code>false</code>.<br>
     * In this case you should give a detailed messages what went wrong. It
     * is not mandatory (you can pass <code>null</code>) but it is recommended.
     *
     * @param message A detailed message, what went wrong
     * @return A new {@link ValidationInfo} which indicates that the validation
     * process was <b>not</b> successful
     */
    public static ValidationInfo invalid(Supplier<String> message) {
        return new ValidationInfo(false, message);
    }

    /**
     * @return <code>true</code> if the validation was successful.
     * <code>false</code> if the  validation failed. If the validation failed,
     * check {@link #getMessage()}
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * @return <code>false</code> if the validation was successful.
     * <code>true</code> if the  validation failed. If the validation failed,
     * check {@link #getMessage()}
     */
    public boolean isInvalid() {
        return !valid;
    }

    /**
     * @return A detailed message of the validation process. This usually is
     * filled, if the validation failed. If the validation was successful the
     * message can be null.
     */
    public String getMessage() {
        return message.get();
    }
}
