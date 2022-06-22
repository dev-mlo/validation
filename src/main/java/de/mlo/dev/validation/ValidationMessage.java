package de.mlo.dev.validation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mlo
 */
public class ValidationMessage implements Comparable<ValidationMessage> {
    private static final ValidationMessage EMPTY = new ValidationMessage(null, null);
    private final String text;
    private final Object[] parameters;
    private final String code;

    /**
     * Creates a new {@link ValidationMessage} with a technical code und a text template with parameters.
     *
     * @param code       A technical code which represents the message text
     * @param text       A text template which can contain placeholders from {@link String#format(String, Object...)}
     *                   and {@link MessageFormat#format(String, Object...)} like "%s" and "{0}"
     * @param parameters The arguments for the placeholders. The amount of arguments should match the number of
     *                   placeholders from the text template
     */
    public ValidationMessage(@Nullable String code, @Nullable String text, @Nullable Object... parameters) {
        this.code = code;
        this.text = text;
        this.parameters = Objects.requireNonNull(parameters);
    }

    /**
     * Creates a new messages object with a code and a text.
     *
     * @param code A technical code which represents the message text
     * @param text A detailed text about the failed or succeed validation
     * @return A new instance of a {@link ValidationMessage}
     */
    @NotNull
    public static ValidationMessage of(@Nullable String code, @Nullable String text) {
        return new ValidationMessage(code, text);
    }

    /**
     * @return An empty {@link ValidationMessage} without a code and without a text
     */
    @NotNull
    public static ValidationMessage empty() {
        return EMPTY;
    }

    /**
     * Creates a new {@link ValidationMessage} with a technical code
     *
     * @param code A technical code which represents the message text
     * @return A new instance of {@link ValidationMessage}
     */
    @NotNull
    public static ValidationMessage justCode(@Nullable String code) {
        return of(code, null);
    }

    /**
     * Creates a new {@link ValidationMessage} with a text
     *
     * @param text The text should be a detailed message about the failed or succeed validation part.
     * @return A new instance of {@link ValidationMessage}
     */
    @NotNull
    public static ValidationMessage justText(@Nullable String text) {
        return of(null, text);
    }

    /**
     * Creates a new {@link ValidationMessage} with a text template with parameters.
     *
     * @param text A text template which can contain placeholders from {@link String#format(String, Object...)}
     *             and {@link MessageFormat#format(String, Object...)} like "%s" and "{0}". The text should
     *             be a detailed message about the failed or succeed validation part.
     * @param args The arguments for the placeholders. The amount of arguments should match the number of
     *             placeholders from the text template
     * @return A new instance of {@link ValidationMessage}
     */
    @NotNull
    public static ValidationMessage formattedText(@NotNull String text, Object... args) {
        return formatted(null, text, args);
    }

    /**
     * Creates a new {@link ValidationMessage} with a technical code und a text template with parameters.
     *
     * @param code A technical code which represents the message text
     * @param text A text template which can contain placeholders from {@link String#format(String, Object...)}
     *             and {@link MessageFormat#format(String, Object...)} like "%s" and "{0}"
     * @param args The arguments for the placeholders. The amount of arguments should match the number of
     *             placeholders from the text template
     * @return A new instance of {@link ValidationMessage}
     */
    @NotNull
    public static ValidationMessage formatted(@Nullable String code, @NotNull String text, @NotNull Object... args) {
        return new ValidationMessage(code, text, args);
    }

    private static String format(String format, Object... args) {
        if (args == null || format == null) {
            return format;
        }
        String msg = String.format(format, args);
        msg = MessageFormat.format(msg, args);
        return msg;
    }

    /**
     * A formatted detailed message about the result of a validation step. This function
     * gives you the text without the placeholders. You can use the Placeholders which are
     * used in {@link String#format(String, Object...)} and
     * {@link MessageFormat#format(String, Object...)}.
     * <pre>
     * Examples
     * The country ''{0}'' does not exist
     * The country '%s' does not exist
     * </pre>
     * <br>
     * To get the raw text template you can use {@link #getUnformattedText()}
     *
     * @return The resolved formatted text
     */
    @Nullable
    public String getText() {
        return format(text, parameters);
    }

    /**
     * The placeholder of the text are not replaced. This function gives you the given raw
     * text template. You can format the text on your own. The needed parameters can be found
     * in {@link #getParameters()}.<br>
     * To get a resolved version you can use {@link #getText()}
     *
     * @return The raw unformatted text template
     */
    @Nullable
    public String getUnformattedText() {
        return text;
    }

    /**
     * The parameters are used to replace placeholders in the given text template. The parameters
     * can be used to format the message on your own with the {@link #getUnformattedText() unformatted text}
     * <br>
     * See {@link String#format(String, Object...)} and {@link MessageFormat#format(String, Object...)}
     *
     * @return The array of the given parameters
     */
    @NotNull
    public Object[] getParameters() {
        return parameters;
    }

    /**
     * <p>
     * The code identifies the type of the message. The message may differ for the same code depending
     * on the parameters, but the meaning of the message should be the same.
     * </p>
     * <p>
     * <pre>
     * Example
     * Code          = NO_COUNTRY
     * Text-Template = The country ''{0}'' does not exist
     * Text 1        = The country 'Narnia' does not exist
     * Text 2        = The country 'Genovia' does not exist
     * </pre>
     * </p>
     *
     * @return A technical code which represents the message text
     */
    @Nullable
    public String getCode() {
        return code;
    }

    public boolean isEmpty() {
        return code == null && text == null;
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    @Override
    public String toString() {
        String c = code != null ? "Code: " + code : null;
        String m = text != null ? "Message: " + getText() : null;
        return Stream.of(c, m).filter(Objects::nonNull).collect(Collectors.joining(" | "));
    }

    @Override
    public int compareTo(@NotNull ValidationMessage o) {
        return Comparator
                .comparing(ValidationMessage::getCode, Comparator.nullsLast(String::compareToIgnoreCase))
                .thenComparing(ValidationMessage::getText, Comparator.nullsLast(String::compareToIgnoreCase))
                .compare(this, o);
    }
}
