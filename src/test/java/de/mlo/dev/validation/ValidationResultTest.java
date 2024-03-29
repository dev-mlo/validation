package de.mlo.dev.validation;

import de.mlo.dev.validation.basic.ValidationResult;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mlo
 */
class ValidationResultTest {

    @Test
    void testDefault() {
        ValidationResult result = new ValidationResult();
        assertTrue(result.getValidationInfos().isEmpty());
        assertTrue(result.isValid());
        assertFalse(result.isInvalid());
        assertTrue(result.getMessage().isBlank());
        assertTrue(result.getMessage("\n").isBlank());
        assertTrue(result.getMessages().isEmpty());
    }

    @Test
    void testCreateInvalid() {
        ValidationResult result = ValidationResult.invalid("Fail 1");
        assertTrue(result.isInvalid());
        assertEquals(1, result.getValidationInfos().size());
        assertEquals("Fail 1", result.getMessage());

        result.add(ValidationInfo.valid());
        assertTrue(result.isInvalid());
        assertEquals(2, result.getAllValidationInfos().size());
        assertEquals(1, result.getValidationInfos().size());
        assertEquals("Fail 1", result.getMessage());
    }

    @Test
    void testAdd() {
        ValidationResult result = new ValidationResult()
                .add(ValidationInfo.valid());
        assertTrue(result.isValid());
        assertTrue(result.getMessage().isBlank());
        assertTrue(result.getMessage("\n").isBlank());
        assertEquals(1, result.getAllValidationInfos().size());
        assertEquals(0, result.getValidationInfos().size());
        assertEquals(0, result.getMessages().size());
        assertTrue(result.getMessagesTextList().isEmpty());

        result.add(ValidationInfo.invalid("Fail 1"));
        assertTrue(result.isInvalid());
        assertEquals("Fail 1", result.getMessage());
        assertEquals("Fail 1", result.getMessage("\n"));
        assertEquals(2, result.getAllValidationInfos().size());
        assertEquals(1, result.getValidationInfos().size());
        assertEquals(1, result.getMessages().size());
        assertEquals(1, result.getMessagesTextList().size());

        result.add(ValidationInfo.invalid("Fail 2"));
        assertTrue(result.isInvalid());
        assertTrue(result.getMessage().contains("Fail 1"));
        assertTrue(result.getMessage().contains("Fail 2"));
        assertTrue(result.getMessage("\n").contains("Fail 1"));
        assertTrue(result.getMessage("\n").contains("Fail 1"));
        assertEquals(3, result.getAllValidationInfos().size());
        assertEquals(2, result.getValidationInfos().size());
        assertEquals(2, result.getMessages().size());
        assertEquals(2, result.getMessagesTextList().size());
    }

    @Test
    void testAddMore() {
        ValidationResult result = new ValidationResult().add(
                ValidationInfo.valid(),
                ValidationInfo.invalid("Fail 1"),
                null,
                ValidationInfo.valid());
        assertTrue(result.isInvalid());
        assertEquals("Fail 1", result.getMessage());
        assertEquals("Fail 1", result.getMessage("\n"));
        assertEquals(3, result.getAllValidationInfos().size());
        assertEquals(1, result.getValidationInfos().size());
        assertEquals(1, result.getMessages().size());
        assertEquals(1, result.getMessagesTextList().size());

    }

    @Test
    void testAddCollection() {
        ValidationResult result = new ValidationResult();
        result.add(Arrays.asList(
                ValidationInfo.valid(),
                ValidationInfo.invalid("Fail 1"),
                null,
                ValidationInfo.valid()));
        assertTrue(result.isInvalid());
        assertEquals("Fail 1", result.getMessage());
        assertEquals("Fail 1", result.getMessage("\n"));
        assertEquals(3, result.getAllValidationInfos().size());
        assertEquals(1, result.getValidationInfos().size());
        assertEquals(1, result.getMessages().size());
        assertEquals(1, result.getMessagesTextList().size());
    }

    @Test
    void testAddResult() {
        ValidationResult firstResult = new ValidationResult()
                .add(ValidationInfo.valid());

        ValidationResult secondResult = new ValidationResult()
                .add(ValidationInfo.valid());

        ValidationResult aggregated = new ValidationResult()
                .add(firstResult)
                .add(secondResult);
        assertTrue(aggregated.isValid());
        assertEquals(2, aggregated.getAllValidationInfos().size());
        assertEquals(0, aggregated.getValidationInfos().size());
        assertEquals(0, aggregated.getMessages().size());
        assertEquals(0, aggregated.getMessagesTextList().size());
        assertTrue(aggregated.getMessage().isBlank());

        ValidationResult invalidResult = new ValidationResult()
                .add(ValidationInfo.invalid("Fail"));
        aggregated.add(invalidResult);
        assertTrue(aggregated.isInvalid());
        assertEquals(3, aggregated.getAllValidationInfos().size());
        assertEquals(1, aggregated.getValidationInfos().size());
        assertEquals(1, aggregated.getMessages().size());
        assertEquals(1, aggregated.getMessagesTextList().size());
        assertEquals("Fail", aggregated.getMessage());
    }

    @Test
    void testIterable() {
        ValidationResult result = new ValidationResult()
                .add(ValidationInfo.valid("Success"))
                .add(ValidationInfo.invalid("Fail"));
        Iterator<ValidationInfo> iterator = result.iterator();
        assertEquals("Success", iterator.next().getMessage().getText());
        assertEquals("Fail", iterator.next().getMessage().getText());
        assertFalse(new ValidationResult().iterator().hasNext());
    }

    @Test
    void testGetCodes() {
        ValidationResult result = new ValidationResult()
                .add(ValidationInfo.invalidCode("ER-001"))
                .add(ValidationInfo.invalidCode("ER-002"))
                .add(ValidationInfo.invalid("Fail message"))
                .add(ValidationInfo.invalidCode("ER-003", "Wrong"))
                .add(ValidationInfo.valid(ValidationMessage.justCode("WIN-001")));
        assertThat(result.getCodes()).contains("ER-001", "ER-002", "ER-003", "WIN-001");
        assertThat(result.getFields()).isEmpty();
    }

    @Test
    void testFields(){
        ValidationResult result = new ValidationResult()
                .add(ValidationInfo.invalidField("name", "Name is empty"))
                .add(ValidationInfo.invalidFieldCode("age", "ER-001"))
                .add(ValidationInfo.invalidFieldCode("city", "ER-002", "City is empty"))
                .add(ValidationInfo.invalid(ValidationMessage.of("zip", "ER-003", "ZIP code is empty")));
        assertThat(result.getFields()).contains("name", "age", "city", "zip");
    }

    @Test
    void testMultipleMessagesPerField(){
        ValidationResult result = new ValidationResult()
                .add(ValidationInfo.invalidField("iban", "Invalid format"))
                .add(ValidationInfo.invalidField("iban", "No country info"));
        assertThat(result.getFields()).contains("iban");
        assertThat(result.getMessages("iban")).size().isEqualTo(2);
        assertThat(result.getMessagesTextList("iban")).contains("Invalid format", "No country info");
    }
}