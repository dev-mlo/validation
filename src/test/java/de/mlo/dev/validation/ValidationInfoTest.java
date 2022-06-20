package de.mlo.dev.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mlo
 */
class ValidationInfoTest {

    @Test
    void testConstructors() {
        ValidationInfo info = new ValidationInfo(true, null);
        assertTrue(info.isValid());
        assertFalse(info.isInvalid());
        assertNull(info.getMessage());

        info = new ValidationInfo(true, "Successful");
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
    }

    @Test
    void testInvalid() {
        ValidationInfo invalid = ValidationInfo.invalid("Fail");
        assertTrue(invalid.isInvalid());
        assertEquals("Fail", invalid.getMessage());

        invalid = ValidationInfo.invalid(null);
        assertTrue(invalid.isInvalid());
        assertNull(invalid.getMessage());
    }

    @Test
    void testInvalidWithFormatter() {
        ValidationInfo invalid = ValidationInfo.invalid("%s", "Format Test");
        assertTrue(invalid.isInvalid());
        assertEquals("Format Test", invalid.getMessage());
    }

    @Test
    void testCompareTo() {
        ValidationInfo valid = ValidationInfo.valid();
        ValidationInfo invalid = ValidationInfo.invalid("Fail");
        assertEquals(0, valid.compareTo(ValidationInfo.valid()));
        assertEquals(0, invalid.compareTo(ValidationInfo.invalid("Fail")));
        assertEquals(-1, valid.compareTo(invalid));
        assertEquals(1, invalid.compareTo(valid));
    }
}