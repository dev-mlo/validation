package de.mlo.dev.validation.basic;

import de.mlo.dev.validation.ValidationInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mlo
 */
class ValidationRunnersTest {

    @Test
    void testValidateAll() {
        ValidationResult result = ValidationRunners.VALIDATE_ALL.validate(List.of(ValidationResult::new));
        assertTrue(result.isValid());
        assertEquals(0, result.getValidationInfos().size());

        result = ValidationRunners.VALIDATE_ALL.validate(List.of(() -> ValidationResult.invalid("Fail")));
        assertTrue(result.isInvalid());
        assertEquals(1, result.getValidationInfos().size());
        assertEquals("Fail", result.getMessage());

        result = ValidationRunners.VALIDATE_ALL.validate(List.of(
                ValidationResult::new,
                () -> ValidationResult.invalid("Fail"),
                ValidationResult::new));
        assertFalse(result.isValid());
        assertEquals(1, result.getValidationInfos().size());
        assertEquals("Fail", result.getMessage());
    }

    @Test
    void testValidateStopOnFirstFail() {
        ValidationResult result = ValidationRunners.VALIDATE_STOP_ON_FIRST_FAIL.validate(List.of(ValidationResult::new));
        assertTrue(result.isValid());
        assertEquals(0, result.getValidationInfos().size());

        result = ValidationRunners.VALIDATE_STOP_ON_FIRST_FAIL.validate(List.of(() -> ValidationResult.invalid("Fail")));
        assertTrue(result.isInvalid());
        assertEquals(1, result.getValidationInfos().size());

        result = ValidationRunners.VALIDATE_STOP_ON_FIRST_FAIL.validate(List.of(
                () -> new ValidationResult().add(ValidationInfo.valid("Success")),
                () -> ValidationResult.invalid("Fail 1"),
                () -> ValidationResult.invalid("Fail 2")));
        assertFalse(result.isValid());
        assertEquals(1, result.getValidationInfos().size());
        assertEquals(2, result.getAllValidationInfos().size());
        assertEquals("Success - Fail 1", result.getMessage(" - "));
    }
}