package de.mlo.dev.validation.basic;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author mlo
 */
@FunctionalInterface
public interface ValidationRunner {

    /**
     * Has to execute the given {@link ValidationSummarizer#validate() instructions}
     * and has to aggregate the all results to one.<br>
     * It is up to the runner, how to execute the instructions and when the process
     * stops.<br>
     * <br>
     * Predefined runners:
     * <ul>
     *     <li>{@link ValidationRunners#VALIDATE_ALL} - Executes all instructions</li>
     *     <li>{@link ValidationRunners#VALIDATE_STOP_ON_FIRST_FAIL} - Executes the
     *     instructions and stops if one instruction failed</li>
     * </ul>
     *
     * @param validators A list of instructions which has to be executed. The list
     *                   contains the instruction in the order they have been added
     * @return An aggregated result of the given instructions
     */
    @NotNull
    ValidationResult validate(List<ValidationSummarizer> validators);
}
