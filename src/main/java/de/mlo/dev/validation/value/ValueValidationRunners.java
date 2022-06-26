package de.mlo.dev.validation.value;

import de.mlo.dev.validation.basic.ValidationResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author mlo
 */
public class ValueValidationRunners {
    private static final Logger LOGGER = LogManager.getLogger(ValueValidationRunners.class.getName());

    private ValueValidationRunners() {
    }

    /**
     * Executes all added {@link ValueValidationStatement}s in the order they have been
     * added. If a single instruction fails, the {@link ValueValidationResult#isValid()}
     * function will return <code>false</code>. Only if all instruction passes the test
     * the result will be valid ({@link ValueValidationResult#isValid()} will be
     * <code>true</code>).<br>
     * The validation result also contains the result of every single instructions
     * and can aggregate all failure messages: {@link ValueValidationResult#getMessage()}.
     *
     * @param value        The value which has to be validated
     * @param instructions The instructions which have to be executed
     * @param <V>          The type of the value
     * @return An aggregated {@link ValueValidationResult}
     */
    @NotNull
    static <V> ValueValidationResult<V> validateAll(V value, List<ValueValidationSummarizer<V>> instructions) {
        LOGGER.debug("Start validating {} instructions", instructions.size());
        ValueValidationResult<V> result = new ValueValidationResult<>(value);
        for (int i = 0; i < instructions.size(); i++) {
            ValidationResult info = instructions.get(i).validate(value);
            result.add(info);
            if (info.isInvalid()) {
                LOGGER.debug("Validation failed: Instruction number {} | message: {}", i, info.getMessage());
            }
        }
        if (result.isInvalid()) {
            LOGGER.debug("Validation was successful");
        } else {
            LOGGER.debug("Validation failed");
        }
        return result;
    }

    /**
     * Executes the given {@link ValueValidationStatement}s in the order they have been
     * added until the first {@link ValueValidationStatement} fail. If a single instruction
     * fails, the {@link ValueValidationResult#isValid()} function will return <code>false</code>.
     * Only if all instruction passes the test the result will be valid
     * ({@link ValueValidationResult#isValid()} will be <code>true</code>).<br>
     * The validation result also contains the result of every single instructions
     * and can aggregate all failure messages: {@link ValueValidationResult#getMessage()}.
     *
     * @param value        The value which has to be validated
     * @param instructions The instructions which have to be executed
     * @param <V>          The type of the value
     * @return An aggregated {@link ValueValidationResult}
     */
    @NotNull
    static <V> ValueValidationResult<V> validateStopOnFirstFail(V value, List<ValueValidationSummarizer<V>> instructions) {
        LOGGER.debug("Start validating {} instructions", instructions.size());
        ValueValidationResult<V> result = new ValueValidationResult<>(value);
        for (int i = 0; i < instructions.size(); i++) {
            ValidationResult info = instructions.get(i).validate(value);
            result.add(info);
            if (result.isInvalid()) {
                LOGGER.debug("Validation failed: Instruction number {} | message: {}", i + 1, info.getMessage());
                return result;
            } else {
                LOGGER.debug("Instruction number {} passed", i + 1);
            }
        }
        LOGGER.debug("Validation was successful");
        return result;
    }
}
