package de.mlo.dev.validation;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Objects;

/**
 * The {@link ValidationInfo} contains a single result of a validation process.
 * Multiple {@link ValidationInfo}s can be aggregated with the {@link ValidationResult}.<br>
 * To chain and group validation processes you can use the {@link Validator}.
 *
 * @author mlo
 */
@ToString
@EqualsAndHashCode
public class ValidationInfo implements Comparable<ValidationInfo> {

    private static final ValidationInfo VALID = new ValidationInfo(true, null);
    private final boolean valid;
    private final String message;

    /**
     * Creates a new {@link ValidationInfo}. You can also use the {@link #valid()}
     * or {@link #invalid(String)} function to create a new info object.
     *
     * @param valid   true, if the validation process was successful
     * @param message A detailed message if the validation failed. If the validation
     *                was successful the message usually is not necessary.
     */
    public ValidationInfo(boolean valid, @Nullable String message) {
        this.valid = valid;
        this.message = message;
    }

    /**
     * Creates a new {@link ValidationInfo} which indicates that the validation
     * process was successful: {@link #isValid()} returns <code>true</code>.<br>
     * The message will be <code>null</code> in this case. If you would like to add
     * a message, you can use {@link #valid(String)} but this is usually not
     * necessary.
     *
     * @return A new {@link ValidationInfo} which indicates that the validation
     * process was successful
     */
    @NotNull
    public static ValidationInfo valid() {
        return VALID;
    }

    /**
     * Creates a new {@link ValidationInfo} which indicates that the validation
     * process was successful: {@link #isValid()} returns <code>true</code>.
     * You also can add a message but this is usually not necessary if a
     * validation was successful.
     *
     * @param message A detailed message of the validation process
     * @return A new {@link ValidationInfo} which indicates that the validation
     * process was successful
     */
    @NotNull
    public static ValidationInfo valid(@Nullable String message) {
        return new ValidationInfo(true, message);
    }

    /**
     * Creates a new {@link ValidationInfo} which indicates that the validation
     * process was <b>not</b> successful: {@link #isValid()} returns
     * <code>false</code>.<br>
     * In this case you should give a detailed messages what went wrong. It
     * is not mandatory (you can pass <code>null</code>) but it is recommended.
     *
     * @param message A detailed message, what went wrong
     * @return A new {@link ValidationInfo} which indicates that the validation
     * process was <b>not</b> successful
     */
    @NotNull
    public static ValidationInfo invalid(@Nullable String message) {
        return new ValidationInfo(false, message);
    }

    /**
     * Creates a new {@link ValidationInfo} which indicates that the validation
     * process was <b>not</b> successful: {@link #isValid()} returns
     * <code>false</code>.<br>
     * In this case you should give a detailed messages what went wrong.<br>
     * This function has a built-in formatter which uses
     * {@link String#format(String, Object...)}
     *
     * @param messageFormat A detailed message, what went wrong
     * @param args          Objects which should replace the placeholder within the message
     * @return A new {@link ValidationInfo} which indicates that the validation
     * process was <b>not</b> successful
     * @see String#format(String, Object...)
     */
    @NotNull
    public static ValidationInfo invalid(@NotNull String messageFormat, Object... args) {
        return new ValidationInfo(false, String.format(messageFormat, args));
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
    @Nullable
    public String getMessage() {
        return message;
    }

    @Override
    public int compareTo(@NotNull ValidationInfo o) {
        return Comparator.comparing(ValidationInfo::isValid)
                .thenComparing(info -> Objects.requireNonNullElse(info.getMessage(), ""))
                .compare(this, o);
    }
}
