package de.mlo.dev.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author mlo
 */
class ValidationMessageTest {

    @Test
    void of() {
        ValidationMessage msg = ValidationMessage.of("ER-001", "Fail");
        assertEquals("ER-001", msg.getCode());
        assertEquals("Fail", msg.getText());
        assertEquals("Fail", msg.getUnformattedText());
        assertEquals(0, msg.getParameters().length);

        msg = ValidationMessage.of(null, null);
        assertNull(msg.getCode());
        assertNull(msg.getText());
        assertNull(msg.getUnformattedText());
        assertEquals(0, msg.getParameters().length);
    }

    @Test
    void empty() {
        ValidationMessage msg = ValidationMessage.empty();
        assertEquals(0, msg.getParameters().length);
        assertNull(msg.getText());
        assertNull(msg.getCode());
        assertNull(msg.getUnformattedText());
    }

    @Test
    void justCode() {
        ValidationMessage msg = ValidationMessage.justCode("ER-001");
        assertEquals("ER-001", msg.getCode());
        assertNull(msg.getText());
        assertNull(msg.getUnformattedText());
        assertEquals(0, msg.getParameters().length);

        msg = ValidationMessage.justCode(null);
        assertNull(msg.getCode());
        assertNull(msg.getText());
        assertNull(msg.getUnformattedText());
        assertEquals(0, msg.getParameters().length);
    }

    @Test
    void justText() {
        ValidationMessage msg = ValidationMessage.justText("Fail");
        assertNull(msg.getCode());
        assertEquals("Fail", msg.getText());
        assertEquals("Fail", msg.getUnformattedText());
        assertEquals(0, msg.getParameters().length);

        msg = ValidationMessage.justText(null);
        assertNull(msg.getCode());
        assertNull(msg.getText());
        assertNull(msg.getUnformattedText());
        assertEquals(0, msg.getParameters().length);
    }

    @Test
    void formattedText() {
        ValidationMessage msg = ValidationMessage.formattedText("%s - {1}", 1, 2);
        assertNull(msg.getCode());
        assertEquals("1 - 2", msg.getText());
        assertEquals("%s - {1}", msg.getUnformattedText());
        assertEquals(1, msg.getParameters()[0]);
        assertEquals(2, msg.getParameters()[1]);
    }

    @Test
    void formatted() {
        ValidationMessage msg = ValidationMessage.formatted("ER-001", "%s - {1}", 1, 2);
        assertEquals("ER-001", msg.getCode());
        assertEquals("1 - 2", msg.getText());
        assertEquals("%s - {1}", msg.getUnformattedText());
        assertEquals(1, msg.getParameters()[0]);
        assertEquals(2, msg.getParameters()[1]);
    }


    @Test
    void testToString() {
        ValidationMessage msg = ValidationMessage.formatted("ER-001", "%s - {1}", 1, 2);
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
}