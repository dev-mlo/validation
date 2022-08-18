package de.mlo.dev.validation.basic;

import de.mlo.dev.validation.ValidationInfo;
import de.mlo.dev.validation.ValidationMessage;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author mlo
 */
@ToString
@EqualsAndHashCode
public class ValidationResult implements Iterable<ValidationInfo> {

    /**
     * The system line separator
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private final List<ValidationInfo> infos = new ArrayList<>();
    private boolean valid = true;

    /**
     * Convenient function: Creates a {@link ValidationResult} with on {@link ValidationInfo}
     * which is <i>invalid</i> and contains the given message.<br>
     * <b>Caution</b>: If a process combines {@link ValidationResult results} the end result
     * will be invalid.
     *
     * @param message A description why the result is invalid
     * @return A new instance of a {@link ValidationResult}
     */
    public static ValidationResult invalid(String message) {
        return new ValidationResult()
                .add(ValidationInfo.invalid(message));
    }

    /**
     * Merges the given {@link ValidationResult} into this result. The given
     * result object is not touched. The {@link ValidationInfo}s from the
     * given container are transferred to this container.
     *
     * @param validationResult An already existent result container
     * @return An instance of this {@link ValidationResult}
     */
    public ValidationResult add(ValidationResult validationResult) {
        return add(validationResult.infos);
    }

    /**
     * Adds one or more {@link ValidationInfo}s at the end of the list
     *
     * @param first Single information about the validation process
     * @param more  A list of information about the validation process
     * @return An instance of this {@link ValidationResult}
     */
    public ValidationResult add(ValidationInfo first, ValidationInfo... more) {
        add(first);
        for (ValidationInfo info : more) {
            add(info);
        }
        return this;
    }

    /**
     * Adds a bunch of {@link ValidationInfo}s at the end of the list
     *
     * @param validationInfos A list of information about the validation process
     * @return An instance of this {@link ValidationResult}
     */
    public ValidationResult add(Collection<ValidationInfo> validationInfos) {
        for (ValidationInfo info : validationInfos) {
            add(info);
        }
        return this;
    }

    /**
     * Adds a new {@link ValidationInfo} at the end of the list.
     *
     * @param validationInfo Single information about the validation process
     * @return An instance of this {@link ValidationResult}
     */
    public ValidationResult add(ValidationInfo validationInfo) {
        if (validationInfo != null) {
            this.infos.add(validationInfo);
            this.valid = valid && validationInfo.isValid();
        }
        return this;
    }

    /**
     * @return <code>true</code> if all {@link ValidationInfo}s are valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * @return <code>true</code> if one of the {@link ValidationInfo}s is invalid
     */
    public boolean isInvalid() {
        return !valid;
    }

    /**
     * @return All added {@link ValidationInfo}. The order is maintained.
     */
    public List<ValidationInfo> getInfos() {
        return new ArrayList<>(infos);
    }

    /**
     * @return A combined message from all {@link ValidationInfo}s. The messages are
     * separated by the system default line separator
     */
    public String getMessage() {
        return getMessage(LINE_SEPARATOR);
    }

    /**
     * @param delimiter A custom delimiter which is inserted between messages
     * @return A combined message from all {@link ValidationInfo}s. The messages
     * are separated by the given delimiter
     */
    public String getMessage(String delimiter) {
        return infos.stream()
                .map(ValidationInfo::getMessage)
                .map(ValidationMessage::getText)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(delimiter));
    }

    /**
     * @return A list of all messages from the {@link ValidationInfo}s.
     */
    public List<ValidationMessage> getMessages() {
        return infos.stream()
                .map(ValidationInfo::getMessage)
                .filter(ValidationMessage::isNotEmpty)
                .collect(Collectors.toList());
    }

    public List<String> getMessagesTextList() {
        return infos.stream()
                .map(ValidationInfo::getMessageText)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * @return A set of all technical codes
     */
    public Set<String> getCodes() {
        return infos.stream()
                .map(ValidationInfo::getMessage)
                .map(ValidationMessage::getCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public Set<String> getFields(){
        return infos.stream()
                .map(ValidationInfo::getField)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public Map<String, Set<String>> getFieldMessages(){
        return getFields().stream()
                .collect(Collectors.toMap(Function.identity(), this::getMessagesTextList));
    }

    public List<ValidationMessage> getMessages(String field){
        return getMessages().stream()
                .filter(message -> field.equals(message.getField()))
                .collect(Collectors.toList());
    }

    public Set<String> getMessagesTextList(String field){
        return getMessages(field).stream()
                .map(ValidationMessage::getText)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public Iterator<ValidationInfo> iterator() {
        return infos.iterator();
    }
}
