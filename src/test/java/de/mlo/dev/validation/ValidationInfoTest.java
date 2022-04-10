package de.mlo.dev.validation;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mlo
 */
class ValidationInfoTest {

    @Test
    void testConstructors() {
        ValidationInfo info = new ValidationInfo(true, (String) null);
        assertTrue(info.isValid());
        assertFalse(info.isInvalid());
        assertNull(info.getMessage());

        info = new ValidationInfo(true, "Successful");
        assertTrue(info.isValid());
        assertFalse(info.isInvalid());
        assertEquals("Successful", info.getMessage());

        info = new ValidationInfo(true, (Supplier<String>) null);
        assertTrue(info.isValid());
        assertFalse(info.isInvalid());
        assertNull(info.getMessage());

        info = new ValidationInfo(true, () -> "Successful");
        assertTrue(info.isValid());
        assertFalse(info.isInvalid());
        assertEquals("Successful", info.getMessage());
    }

    @Test
    void testValid() {
        ValidationInfo valid = ValidationInfo.valid();
        assertTrue(valid.isValid());
        assertNull(valid.getMessage());

        valid = ValidationInfo.valid("Successful");
        assertTrue(valid.isValid());
        assertEquals("Successful", valid.getMessage());

        valid = ValidationInfo.valid(() -> "Successful");
        assertTrue(valid.isValid());
        assertEquals("Successful", valid.getMessage());
    }

    @Test
    void testInvalid() {
        ValidationInfo invalid = ValidationInfo.invalid("Fail");
        assertTrue(invalid.isInvalid());
        assertEquals("Fail", invalid.getMessage());

        invalid = ValidationInfo.invalid((String) null);
        assertTrue(invalid.isInvalid());
        assertNull(invalid.getMessage());

        invalid = ValidationInfo.invalid(() -> "Fail");
        assertTrue(invalid.isInvalid());
        assertEquals("Fail", invalid.getMessage());

        invalid = ValidationInfo.invalid((Supplier<String>) null);
        assertTrue(invalid.isInvalid());
        assertNull(invalid.getMessage());
    }
}