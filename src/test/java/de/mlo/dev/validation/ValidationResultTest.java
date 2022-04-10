package de.mlo.dev.validation;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author mlo
 */
class ValidationResultTest {

    @Test
    void testDefault() {
        ValidationResult result = new ValidationResult();
        assertTrue(result.getInfos().isEmpty());
        assertTrue(result.isValid());
        assertTrue(result.getMessage().isBlank());
        assertTrue(result.getMessage("\n").isBlank());
        assertTrue(result.getMessages().isEmpty());
    }

    @Test
    void testAdd() {
        ValidationResult result = new ValidationResult()
                .add(ValidationInfo.valid());
        assertTrue(result.isValid());
        assertTrue(result.getMessage().isBlank());
        assertTrue(result.getMessage("\n").isBlank());
        assertEquals(1, result.getInfos().size());
        assertTrue(result.getMessages().isEmpty());

        result.add(ValidationInfo.invalid("Fail 1"));
        assertTrue(result.isInvalid());
        assertEquals("Fail 1", result.getMessage());
        assertEquals("Fail 1", result.getMessage("\n"));
        assertEquals(2, result.getInfos().size());
        assertEquals(1, result.getMessages().size());

        result.add(ValidationInfo.invalid("Fail 2"));
        assertTrue(result.isInvalid());
        assertTrue(result.getMessage().contains("Fail 1"));
        assertTrue(result.getMessage().contains("Fail 2"));
        assertTrue(result.getMessage("\n").contains("Fail 1"));
        assertTrue(result.getMessage("\n").contains("Fail 1"));
        assertEquals(3, result.getInfos().size());
        assertEquals(2, result.getMessages().size());
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
        assertEquals(3, result.getInfos().size());
        assertEquals(1, result.getMessages().size());

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
        assertEquals(3, result.getInfos().size());
        assertEquals(1, result.getMessages().size());
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
        assertEquals(2, aggregated.getInfos().size());
        assertEquals(0, aggregated.getMessages().size());
        assertTrue(aggregated.getMessage().isBlank());

        ValidationResult invalidResult = new ValidationResult()
                .add(ValidationInfo.invalid("Fail"));
        aggregated.add(invalidResult);
        assertTrue(aggregated.isInvalid());
        assertEquals(3, aggregated.getInfos().size());
        assertEquals(1, aggregated.getMessages().size());
        assertEquals("Fail", aggregated.getMessage());
    }
}