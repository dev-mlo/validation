package de.mlo.dev.validation;

import de.mlo.dev.validation.basic.ValidationResult;
import de.mlo.dev.validation.basic.Validator;
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

    private static final ValidationInfo VALID = new ValidationInfo(true, ValidationMessage.empty());
    private final boolean valid;
    private final ValidationMessage message;

    /**
     * Creates a new {@link ValidationInfo}. You can also use the {@link #valid()}
     * or {@link #invalid(String)} function to create a new info object.
     *
     * @param valid   true, if the validation process was successful
     * @param message A detailed message if the validation failed. If the validation
     *                was successful the message usually is not necessary.
     */
    public ValidationInfo(boolean valid, ValidationMessage message) {
        this.valid = valid;
        this.message = Objects.requireNonNullElse(message, ValidationMessage.empty());
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
     * You also can add a text but this is usually not necessary if a
     * validation was successful.
     *
     * @param text A detailed text of the validation process
     * @return A new {@link ValidationInfo} which indicates that the validation
     * process was successful
     */
    @NotNull
    public static ValidationInfo valid(@Nullable String text) {
        return new ValidationInfo(true, ValidationMessage.justText(text));
    }

    @NotNull
    public static ValidationInfo valid(@Nullable ValidationMessage message) {
        return new ValidationInfo(true, message);
    }

    /**
     * Creates a new {@link ValidationInfo} which indicates that the validation
     * process was <b>not</b> successful: {@link #isValid()} returns
     * <code>false</code>.<br>
     * In this case you should give a detailed messages what went wrong. It
     * is not mandatory (you can pass <code>null</code>) but it is recommended.
     *
     * @param text A detailed text, what went wrong
     * @return A new {@link ValidationInfo} which indicates that the validation
     * process was <b>not</b> successful
     */
    @NotNull
    public static ValidationInfo invalid(@Nullable String text) {
        return new ValidationInfo(false, ValidationMessage.justText(text));
    }

    @NotNull
    public static ValidationInfo invalid(@Nullable ValidationMessage message) {
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
        return new ValidationInfo(false, ValidationMessage.formattedText(messageFormat, args));
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
    @NotNull
    public ValidationMessage getMessage() {
        return message;
    }

    @Nullable
    public String getMessageText() {
        return message.getText();
    }

    @Nullable
    public String getMessageCode() {
        return message.getCode();
    }

    @Override
    public int compareTo(@NotNull ValidationInfo o) {
        return Comparator.comparing(this::compareValid)
                .thenComparing(ValidationInfo::getMessage)
                .compare(this, o);
    }

    private int compareValid(@NotNull ValidationInfo o) {
        return Boolean.compare(isValid(), o.isValid());
    }
}
