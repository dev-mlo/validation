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
    public static ValidationInfo valid(String field, @Nullable String text) {
        return valid(ValidationMessage.justText(field, text));
    }

    @NotNull
    public static ValidationInfo valid(@Nullable ValidationMessage message) {
        return new ValidationInfo(true, message);
    }

    @NotNull
    public static Builder buildValid() {
        return Builder.valid();
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
    public static ValidationInfo invalidCode(@Nullable String code) {
        return new ValidationInfo(false, ValidationMessage.justCode(code));
    }

    @NotNull
    public static ValidationInfo invalidCode(@Nullable String code, @Nullable String message) {
        return new ValidationInfo(false, ValidationMessage.of(null, code, message));
    }

    @NotNull
    public static ValidationInfo invalidField(@Nullable String field, @Nullable String message){
        return invalid(ValidationMessage.justText(field, message));
    }

    @NotNull
    public static ValidationInfo invalidFieldCode(@Nullable String field, @Nullable String code){
        return invalid(ValidationMessage.justCode(field, code));
    }

    @NotNull
    public static ValidationInfo invalidFieldCode(@Nullable String field, @Nullable String code, @Nullable String message){
        return invalid(ValidationMessage.of(field, code, message));
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

    public static Builder buildInvalid() {
        return Builder.invalid();
    }

    public static Builder build(boolean valid) {
        return new Builder(valid);
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

    /**
     * The affected field name which was validated. This property is optional.
     *
     * @return The affected field name which was validated
     */
    @Nullable
    public String getField() {
        return message.getField();
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

    public static class Builder {

        private final boolean valid;
        private String field;
        private String code;
        private String message;
        private Object[] parameter;

        private Builder(boolean valid) {
            this.valid = valid;
        }

        private static Builder valid() {
            return new Builder(true);
        }

        private static Builder invalid() {
            return new Builder(false);
        }

        public Builder field(String field){
            this.field = field;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder parameter(Object... parameter) {
            this.parameter = parameter;
            return this;
        }

        public ValidationInfo build() {
            return new ValidationInfo(valid, new ValidationMessage(field, code, message, parameter));
        }
    }
}
