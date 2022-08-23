package de.mlo.dev.validation.basic;

import de.mlo.dev.validation.ValidationInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author mlo
 */
class ValidatorTest {

    @Test
    void testValidateAllWithCombinedValidators() {
        Validator failValidator = new Validator()
                .add(() -> ValidationInfo.invalid("Fail 1"))
                .add(() -> ValidationInfo.invalid("Fail 2"));

        Validator successValidator = new Validator()
                .add(() -> ValidationInfo.valid("Success 1"))
                .add(() -> ValidationInfo.valid("Success 2"));

        ValidationResult result = new Validator()
                .setValidateAll()
                .add(failValidator)
                .add(successValidator)
                .validate();
        assertTrue(result.isInvalid());
        assertEquals(4, result.getAllValidationInfos().size());
        assertEquals(2, result.getValidationInfos().size());
        assertEquals("Fail 1", result.getMessages().get(0).getText());
        assertEquals("Fail 2", result.getMessages().get(1).getText());
        assertEquals("Success 1", result.getMessages().get(2).getText());
        assertEquals("Success 2", result.getMessages().get(3).getText());
    }

    @Test
    void testValidateStopOnFirstFailWithCombinedValidators() {
        Validator failValidator = new Validator()
                .add(() -> ValidationInfo.invalid("Fail 1"))
                .add(() -> ValidationInfo.invalid("Fail 2"));

        Validator successValidator = new Validator()
                .add(() -> ValidationInfo.valid("Success 1"))
                .add(() -> ValidationInfo.valid("Success 2"));

        ValidationResult result = new Validator()
                .add(failValidator)
                .add(successValidator)
                .validateAndStopOnFirstFail();
        assertTrue(result.isInvalid());
        assertEquals(2, result.getValidationInfos().size());
        assertEquals("Fail 1", result.getMessages().get(0).getText());
        assertEquals("Fail 2", result.getMessages().get(1).getText());
    }

    @Test
    void testValidateWithCombinedValidatorsAndSubValidators() {

        // Will be executed first
        Validator withSubValidator = new Validator()
                .add(() -> ValidationInfo.valid("Success 1"))
                .add(() -> ValidationInfo.valid("Success 2"));

        // ... second, because it is a sub-validator of the first one
        Validator subFailValidator = new Validator()
                .add(() -> ValidationInfo.invalid("Sub fail 1"))
                .add(() -> ValidationInfo.invalid("Sub fail 2"));
        withSubValidator.add(subFailValidator);

        // ... last
        Validator failValidator = new Validator()
                .add(() -> ValidationInfo.invalid("Fail 1"))
                .add(() -> ValidationInfo.invalid("Fail 2"));


        // validateStopOnFirstFail
        ValidationResult result = new Validator()
                .add(withSubValidator) // <-- Will fail because of the sub-validator
                .add(failValidator) // <-- Won't be executed
                .validateAndStopOnFirstFail();
        assertTrue(result.isInvalid());
        assertEquals(4, result.getAllValidationInfos().size());
        assertEquals(2, result.getValidationInfos().size());
        assertEquals("Success 1", result.getMessages().get(0).getText());
        assertEquals("Success 2", result.getMessages().get(1).getText());
        assertEquals("Sub fail 1", result.getMessages().get(2).getText());
        assertEquals("Sub fail 2", result.getMessages().get(3).getText());


        // validateAll
        result = new Validator()
                .setValidateAll()
                .add(withSubValidator) // <-- Will fail because of the sub-validator
                .add(failValidator) // <-- Will be executed and fail
                .validate();
        assertTrue(result.isInvalid());
        assertEquals(6, result.getAllValidationInfos().size());
        assertEquals(4, result.getValidationInfos().size());
        assertEquals("Success 1", result.getMessages().get(0).getText());
        assertEquals("Success 2", result.getMessages().get(1).getText());
        assertEquals("Sub fail 1", result.getMessages().get(2).getText());
        assertEquals("Sub fail 2", result.getMessages().get(3).getText());
        assertEquals("Fail 1", result.getMessages().get(4).getText());
        assertEquals("Fail 2", result.getMessages().get(5).getText());
    }

    @Test
    void testValidateAll() {
        ValidationResult result = new Validator()
                .add(ValidationInfo::valid)
                .add(ValidationInfo::valid)
                .add(ValidationInfo::valid)
                .validate();
        assertEquals(3, result.getAllValidationInfos().size());
        assertEquals(0, result.getValidationInfos().size());
        assertTrue(result.isValid());

        result = new Validator()
                .add(ValidationInfo::valid)
                .add(() -> ValidationInfo.invalid("Fail"))
                .add(ValidationInfo::valid)
                .validate();
        assertEquals(3, result.getAllValidationInfos().size());
        assertEquals(1, result.getValidationInfos().size());
        assertTrue(result.isInvalid());
    }

    @Test
    void testValidateStopOnFirstFail() {
        ValidationResult result = new Validator()
                .add(ValidationInfo::valid)
                .add(ValidationInfo::valid)
                .add(ValidationInfo::valid)
                .validateAndStopOnFirstFail();
        assertEquals(3, result.getAllValidationInfos().size());
        assertEquals(0, result.getValidationInfos().size());
        assertTrue(result.isValid());

        ValidationStatement thirdStatement = mock(ValidationStatement.class);
        result = new Validator()
                .add(ValidationInfo::valid)
                .add(() -> ValidationInfo.invalid("Fail"))
                .add(thirdStatement)
                .validateAndStopOnFirstFail();
        assertEquals(2, result.getAllValidationInfos().size());
        assertEquals(1, result.getValidationInfos().size());
        assertTrue(result.isInvalid());
        verify(thirdStatement, times(0)).execute();
    }

    @Test
    void testAdd() {
        ValidationResult result = new Validator()
                .add(null)
                .add(ValidationInfo::valid)
                .validate();
        assertTrue(result.isValid());
    }

    @Test
    void testGroup() {
        // One statement and one group with one statement
        ValidationResult result = new Validator()
                .add(ValidationInfo::valid)
                .groupBuilder()
                .add(ValidationInfo::valid)
                .build()
                .validate();
        assertTrue(result.isValid());
        assertEquals(2, result.getAllValidationInfos().size());
        assertEquals(0, result.getValidationInfos().size());
        assertEquals(0, result.getMessages().size());
        assertEquals(0, result.getMessagesTextList().size());


        // Group fail should prevent execution of further validation statements
        result = new Validator()
                .groupBuilder()
                .add(() -> ValidationInfo.invalid("Fail"))
                .build()
                .add(ValidationInfo::valid)
                .validateAndStopOnFirstFail();
        assertEquals(1, result.getAllValidationInfos().size());
        assertEquals(1, result.getValidationInfos().size());
        assertEquals(1, result.getMessages().size());
        assertEquals(1, result.getMessagesTextList().size());
        assertEquals("Fail", result.getMessage());


        // The groups themselves will stop the execution when on statement failed but the
        // whole validator will execute all groups
        result = new Validator()
                .groupBuilder()
                .add(() -> ValidationInfo.valid("Valid Group 1.1"))
                .add(() -> ValidationInfo.invalid("Fail Group 1.1"))
                .add(() -> ValidationInfo.valid("Valid Group 1.2: Wont be execute due to previous fail"))
                .setValidateAndStopOnFirstFail()
                .build()
                .groupBuilder()
                .add(() -> ValidationInfo.valid("Valid Group 2.1"))
                .add(() -> ValidationInfo.invalid("Fail Group 2.1"))
                .add(() -> ValidationInfo.valid("Valid Group 2.2: Wont be execute due to previous fail"))
                .setValidateAndStopOnFirstFail()
                .build()
                .validate();
        assertEquals(4, result.getAllValidationInfos().size());
        assertEquals(2, result.getValidationInfos().size());
        assertEquals(4, result.getMessages().size());
        assertEquals(4, result.getMessagesTextList().size());
        assertTrue(result.getMessagesTextList().containsAll(List.of(
                "Valid Group 1.1", "Fail Group 1.1", "Valid Group 2.1", "Fail Group 2.1")));
    }

    @Test
    void testNestedGroups() {
        ValidationResult result = new Validator()
                .add(() -> ValidationInfo.valid("Root Validator 1"))
                .groupBuilder()
                .add(() -> ValidationInfo.valid("Valid Group 1.1"))
                .groupBuilder()
                .add(() -> ValidationInfo.valid("Valid Group 1.1.1"))
                .add(() -> ValidationInfo.invalid("Fail Group 1.1.1"))
                .add(() -> ValidationInfo.valid("Valid Group 1.1.2: Wont be executed due to previous fail"))
                .setValidateAndStopOnFirstFail()
                .build()
                .add(() -> ValidationInfo.invalid("Fail Group 1.1"))
                .add(() -> ValidationInfo.valid("Valid Group 1.2"))
                .setValidateAll()
                .build()
                .add(() -> ValidationInfo.valid("Root Validator 2"))
                .validate();
        assertEquals(7, result.getAllValidationInfos().size());
        assertEquals(2, result.getValidationInfos().size());
        assertEquals(7, result.getMessages().size());
        assertEquals(7, result.getMessagesTextList().size());
        assertTrue(result.getMessagesTextList().containsAll(List.of(
                "Root Validator 1", "Valid Group 1.1", "Valid Group 1.1.1", "Fail Group 1.1.1",
                "Fail Group 1.1", "Valid Group 1.2", "Root Validator 2")));
    }

    @Test
    void testBuild() {
        Validator validator = new Validator();
        assertEquals(validator, validator.build());
    }

    @Test
    void testSetValidator() {
        Validator validator = new Validator();
        validator.setValidationRunner(validators -> ValidationResult.invalid("Always wrong"));
        ValidationResult result = validator.validate();
        assertEquals("Always wrong", result.getMessage());
    }
}