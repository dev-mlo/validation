package de.mlo.dev.validation.basic;

import de.mlo.dev.validation.ValidationInfo;
import org.junit.jupiter.api.Test;

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
        assertEquals(4, result.getInfos().size());
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
                .validateStopOnFirstFail();
        assertTrue(result.isInvalid());
        assertEquals(2, result.getInfos().size());
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
                .validateStopOnFirstFail();
        assertTrue(result.isInvalid());
        assertEquals(4, result.getInfos().size());
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
        assertEquals(6, result.getInfos().size());
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
        assertEquals(3, result.getInfos().size());
        assertTrue(result.isValid());

        result = new Validator()
                .add(ValidationInfo::valid)
                .add(() -> ValidationInfo.invalid("Fail"))
                .add(ValidationInfo::valid)
                .validate();
        assertEquals(3, result.getInfos().size());
        assertTrue(result.isInvalid());
    }

    @Test
    void testValidateStopOnFirstFail() {
        ValidationResult result = new Validator()
                .add(ValidationInfo::valid)
                .add(ValidationInfo::valid)
                .add(ValidationInfo::valid)
                .validateStopOnFirstFail();
        assertEquals(3, result.getInfos().size());
        assertTrue(result.isValid());

        ValidationStatement thirdStatement = mock(ValidationStatement.class);
        result = new Validator()
                .add(ValidationInfo::valid)
                .add(() -> ValidationInfo.invalid("Fail"))
                .add(thirdStatement)
                .validateStopOnFirstFail();
        assertEquals(2, result.getInfos().size());
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
}