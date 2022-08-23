package de.mlo.dev.validation.value;

import de.mlo.dev.validation.ValidationInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author mlo
 */
class ValueValidatorTest {

    @Test
    void testEmpty() {
        ValueValidationResult<Object> result = new ValueValidator<>()
                .validate("Test");
        assertTrue(result.isValid());
        assertTrue(result.getValidationInfos().isEmpty());
    }

    /**
     * All groups and all statements will be executed.
     */
    @Test
    void testGroups() {
        ValueValidationResult<String> result = new ValueValidator<String>()
                .groupBuilder()
                .add(null)
                .add((s) -> ValidationInfo.valid("1"))
                .add((s) -> ValidationInfo.valid("2"))
                .build()
                .groupBuilder()
                .add((s) -> ValidationInfo.valid("3"))
                .add((s) -> ValidationInfo.valid("4"))
                .build()
                .setValidateAll()
                .validate("Test");
        assertTrue(result.getMessagesTextList().contains("1"));
        assertTrue(result.getMessagesTextList().contains("2"));
        assertTrue(result.getMessagesTextList().contains("3"));
        assertTrue(result.getMessagesTextList().contains("4"));
    }

    /**
     * Both group validations are executed.
     */
    @Test
    void testGroups2() {
        ValueValidator<String> validator = ValueValidator.create(String.class)
                .groupBuilder()
                .add((s) -> ValidationInfo.valid("1"))
                .add((s) -> ValidationInfo.invalid("2"))
                .add((s) -> ValidationInfo.valid("3"))
                .setValidateAndStopOnFirstFail()
                .build();
        ValueValidationResult<String> result = validator.validate("Test");
        assertTrue(result.getMessagesTextList().contains("1"));
        assertTrue(result.getMessagesTextList().contains("2"));

        validator = validator.groupBuilder()
                .add((s) -> ValidationInfo.valid("4"))
                .add((s) -> ValidationInfo.invalid("5"))
                .add((s) -> ValidationInfo.valid("6"))
                .setValidateAndStopOnFirstFail()
                .build();

        result = validator.validate("Test");
        assertTrue(result.getMessagesTextList().contains("1"));
        assertTrue(result.getMessagesTextList().contains("2"));
        assertTrue(result.getMessagesTextList().contains("4"));
        assertTrue(result.getMessagesTextList().contains("5"));
    }

    /**
     * If the first group validation fails, the second group is not validated.
     */
    @Test
    void testGroups3() {
        ValueValidationResult<String> result = ValueValidator.create(String.class)
                .groupBuilder()
                .add((s) -> ValidationInfo.valid("1"))
                .add((s) -> ValidationInfo.invalid("2"))
                .add((s) -> ValidationInfo.valid("3"))
                .setValidateAndStopOnFirstFail()
                .add((s) -> ValidationInfo.valid("4"))
                .add((s) -> ValidationInfo.invalid("5"))
                .add((s) -> ValidationInfo.valid("6"))
                .setValidateAndStopOnFirstFail()
                .build()
                .validateStopOnFirstFail("Test");
        assertTrue(result.getMessagesTextList().contains("1"));
        assertTrue(result.getMessagesTextList().contains("2"));
    }
}