package de.mlo.dev.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author mlo
 */
public class ValidationResult {

    /**
     * The system line separator
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private final List<ValidationInfo> infos = new ArrayList<>();
    private boolean valid = true;

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
            this.add(info);
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
                .filter(Objects::nonNull)
                .collect(Collectors.joining(delimiter));
    }

    /**
     * @return A list of all messages from the {@link ValidationInfo}s.
     */
    public List<String> getMessages() {
        return infos.stream()
                .map(ValidationInfo::getMessage)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
