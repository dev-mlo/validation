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
        assertNotNull(info.getMessage());
        assertNull(info.getMessageText());
        assertNull(info.getMessageCode());

        info = new ValidationInfo(true, ValidationMessage.justText("Successful"));
        assertTrue(info.isValid());
        assertFalse(info.isInvalid());
        assertEquals("Successful", info.getMessage().getText());
    }

    @Test
    void testValid() {
        ValidationInfo valid = ValidationInfo.valid();
        assertTrue(valid.isValid());
        assertNotNull(valid.getMessage());
        assertNull(valid.getMessageText());
        assertNull(valid.getMessageCode());

        valid = ValidationInfo.valid("Successful");
        assertTrue(valid.isValid());
        assertEquals("Successful", valid.getMessage().getText());
    }

    @Test
    void testInvalid() {
        ValidationInfo invalid = ValidationInfo.invalid("Fail");
        assertTrue(invalid.isInvalid());
        assertEquals("Fail", invalid.getMessage().getText());

        invalid = ValidationInfo.invalid(ValidationMessage.justText("Fail"));
        assertTrue(invalid.isInvalid());
        assertEquals("Fail", invalid.getMessage().getText());

        invalid = ValidationInfo.invalid((String) null);
        assertTrue(invalid.isInvalid());
        assertNotNull(invalid.getMessage());
        assertNull(invalid.getMessageText());
        assertNull(invalid.getMessageCode());

        invalid = ValidationInfo.invalid((ValidationMessage) null);
        assertTrue(invalid.isInvalid());
        assertNotNull(invalid.getMessage());
        assertNull(invalid.getMessageText());
        assertNull(invalid.getMessageCode());
    }

    @Test
    void testInvalidWithFormatter() {
        ValidationInfo invalid = ValidationInfo.invalid("%s", "Format Test");
        assertTrue(invalid.isInvalid());
        assertEquals("Format Test", invalid.getMessage().getText());
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