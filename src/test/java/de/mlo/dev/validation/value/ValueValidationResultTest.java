package de.mlo.dev.validation.value;

import de.mlo.dev.validation.basic.ValidationResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author mlo
 */
class ValueValidationResultTest {

    @Test
    void testFactory() {
        ValueValidationResult<String> result = ValueValidationResult.of("Test", ValidationResult.invalid("Fail"));
        assertEquals("Test", result.getValue());
        assertTrue(result.isInvalid());
        assertEquals("Fail", result.getMessage());
    }
}