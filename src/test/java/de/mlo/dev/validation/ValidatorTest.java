package de.mlo.dev.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author mlo
 */
class ValidatorTest {

    @Test
    void testValidateAll() {
        ValidationResult result = new Validator()
                .add(ValidationInfo::valid)
                .add(ValidationInfo::valid)
                .add(ValidationInfo::valid)
                .validateAll();
        assertEquals(3, result.getInfos().size());
        assertTrue(result.isValid());

        result = new Validator()
                .add(ValidationInfo::valid)
                .add(() -> ValidationInfo.invalid("Fail"))
                .add(ValidationInfo::valid)
                .validateAll();
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

        ValidationInstruction thirdInstruction = mock(ValidationInstruction.class);
        result = new Validator()
                .add(ValidationInfo::valid)
                .add(() -> ValidationInfo.invalid("Fail"))
                .add(thirdInstruction)
                .validateStopOnFirstFail();
        assertEquals(2, result.getInfos().size());
        assertTrue(result.isInvalid());
        verify(thirdInstruction, times(0)).validate();
    }

    @Test
    void testAdd() {
        ValidationResult result = new Validator()
                .add(null)
                .add(ValidationInfo::valid)
                .validateAll();
        assertTrue(result.isValid());
    }
}