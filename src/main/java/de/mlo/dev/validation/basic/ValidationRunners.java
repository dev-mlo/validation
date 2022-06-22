package de.mlo.dev.validation.basic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author mlo
 */
public class ValidationRunners {

    private static final Logger LOGGER = LogManager.getLogger(ValidationRunners.class.getName());
    /**
     * Executes all added {@link ValidationStatement}s in the order they have been
     * added. If a single instruction fails, the {@link ValidationResult#isValid()}
     * function will return <code>false</code>. Only if all instruction passes the test
     * the result will be valid ({@link ValidationResult#isValid()} will be
     * <code>true</code>).<br>
     * The validation result also contains the result of every single instructions
     * and can aggregate all failure messages: {@link ValidationResult#getMessage()}.
     */
    public static final ValidationRunner VALIDATE_ALL = ValidationRunners::validateAll;
    /**
     * Executes the given {@link ValidationStatement}s in the order they have been
     * added until the first {@link ValidationStatement} fail. If a single instruction
     * fails, the {@link ValidationResult#isValid()} function will return <code>false</code>.
     * Only if all instruction passes the test the result will be valid
     * ({@link ValidationResult#isValid()} will be <code>true</code>).<br>
     * The validation result also contains the result of every single instructions
     * and can aggregate all failure messages: {@link ValidationResult#getMessage()}.
     */
    public static final ValidationRunner VALIDATE_STOP_ON_FIRST_FAIL = ValidationRunners::validateStopOnFirstFail;

    private ValidationRunners() {
    }

    /**
     * Executes all added {@link ValidationStatement}s in the order they have been
     * added. If a single instruction fails, the {@link ValidationResult#isValid()}
     * function will return <code>false</code>. Only if all instruction passes the test
     * the result will be valid ({@link ValidationResult#isValid()} will be
     * <code>true</code>).<br>
     * The validation result also contains the result of every single instructions
     * and can aggregate all failure messages: {@link ValidationResult#getMessage()}.
     *
     * @return An aggregated {@link ValidationResult}
     */
    @NotNull
    private static ValidationResult validateAll(List<ValidationSummarizer> instructions) {
        LOGGER.debug("Start validating {} instructions", instructions.size());
        ValidationResult result = new ValidationResult();
        for (int i = 0; i < instructions.size(); i++) {
            ValidationResult info = instructions.get(i).validate();
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
     * Executes the added {@link ValidationStatement}s in the order they have been
     * added until the first {@link ValidationStatement} fail. If a single instruction
     * fails, the {@link ValidationResult#isValid()} function will return <code>false</code>.
     * Only if all instruction passes the test the result will be valid
     * ({@link ValidationResult#isValid()} will be <code>true</code>).<br>
     * The validation result also contains the result of every single instructions
     * and can aggregate all failure messages: {@link ValidationResult#getMessage()}.
     *
     * @return An aggregated {@link ValidationResult}
     */
    @NotNull
    private static ValidationResult validateStopOnFirstFail(List<ValidationSummarizer> instructions) {
        LOGGER.debug("Start validating {} instructions", instructions.size());
        ValidationResult result = new ValidationResult();
        for (int i = 0; i < instructions.size(); i++) {
            ValidationResult info = instructions.get(i).validate();
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
