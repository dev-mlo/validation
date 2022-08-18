package de.mlo.dev.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mlo
 */
class ValidationMessageTest {

    @Test
    void testConstructor() {
        ValidationMessage msg = new ValidationMessage(null, null, null, 1, 2, 3);
        assertNull(msg.getText());
        assertNull(msg.getUnformattedText());
        assertNull(msg.getCode());
        assertNull(msg.getField());
        assertEquals(3, msg.getParameters().length);
    }

    @Test
    void of() {
        ValidationMessage msg = ValidationMessage.of("name", "ER-001", "Fail");
        assertEquals("ER-001", msg.getCode());
        assertEquals("name", msg.getField());
        assertEquals("Fail", msg.getText());
        assertEquals("Fail", msg.getUnformattedText());
        assertEquals(0, msg.getParameters().length);

        msg = ValidationMessage.of(null, null, null);
        assertNull(msg.getField());
        assertNull(msg.getCode());
        assertNull(msg.getText());
        assertNull(msg.getUnformattedText());
        assertEquals(0, msg.getParameters().length);
    }

    @Test
    void empty() {
        ValidationMessage msg = ValidationMessage.empty();
        assertEquals(0, msg.getParameters().length);
        assertNull(msg.getField());
        assertNull(msg.getText());
        assertNull(msg.getCode());
        assertNull(msg.getUnformattedText());
    }

    @Test
    void justCode() {
        ValidationMessage msg = ValidationMessage.justCode("ER-001");
        assertNull(msg.getField());
        assertEquals("ER-001", msg.getCode());
        assertNull(msg.getText());
        assertNull(msg.getUnformattedText());
        assertEquals(0, msg.getParameters().length);

        msg = ValidationMessage.justCode(null);
        assertNull(msg.getField());
        assertNull(msg.getCode());
        assertNull(msg.getText());
        assertNull(msg.getUnformattedText());
        assertEquals(0, msg.getParameters().length);
    }

    @Test
    void justText() {
        ValidationMessage msg = ValidationMessage.justText("Fail");
        assertNull(msg.getField());
        assertNull(msg.getCode());
        assertEquals("Fail", msg.getText());
        assertEquals("Fail", msg.getUnformattedText());
        assertEquals(0, msg.getParameters().length);

        msg = ValidationMessage.justText(null);
        assertNull(msg.getField());
        assertNull(msg.getCode());
        assertNull(msg.getText());
        assertNull(msg.getUnformattedText());
        assertEquals(0, msg.getParameters().length);
    }

    @Test
    void formattedText() {
        ValidationMessage msg = ValidationMessage.formattedText("%s - {1}", 1, 2);
        assertNull(msg.getField());
        assertNull(msg.getCode());
        assertEquals("1 - 2", msg.getText());
        assertEquals("%s - {1}", msg.getUnformattedText());
        assertEquals(1, msg.getParameters()[0]);
        assertEquals(2, msg.getParameters()[1]);
    }

    @Test
    void formattedTextWithField() {
        ValidationMessage msg = ValidationMessage.formattedText("name", "%s - {1} - {field}", 1, 2);
        assertEquals("name", msg.getField());
        assertNull(msg.getCode());
        assertEquals("1 - 2 - name", msg.getText());
        assertEquals("%s - {1} - {field}", msg.getUnformattedText());
        assertEquals(1, msg.getParameters()[0]);
        assertEquals(2, msg.getParameters()[1]);
    }

    @Test
    void formatted() {
        ValidationMessage msg = ValidationMessage.formatted("name", "ER-001", "%s - {1}", 1, 2);
        assertEquals("name", msg.getField());
        assertEquals("ER-001", msg.getCode());
        assertEquals("1 - 2", msg.getText());
        assertEquals("%s - {1}", msg.getUnformattedText());
        assertEquals(1, msg.getParameters()[0]);
        assertEquals(2, msg.getParameters()[1]);
    }


    @Test
    void testToString() {
        ValidationMessage msg = ValidationMessage.formatted("name", "ER-001", "%s - {1}", 1, 2);
        assertEquals("Code: ER-001 | Message: 1 - 2", msg.toString());

        msg = ValidationMessage.formattedText("%s - {1}", 1, 2);
        assertEquals("Message: 1 - 2", msg.toString());

        msg = ValidationMessage.justCode("ER-001");
        assertEquals("Code: ER-001", msg.toString());

        msg = ValidationMessage.empty();
        assertEquals("", msg.toString());
    }

    @Test
    void compareTo() {
    }

    @Test
    void testIsEmpty() {
        ValidationMessage msg = ValidationMessage.empty();
        assertTrue(msg.isEmpty());

        msg = ValidationMessage.justCode("ERR-001");
        assertFalse(msg.isEmpty());

        msg = ValidationMessage.justText("Fail");
        assertFalse(msg.isEmpty());
    }
}