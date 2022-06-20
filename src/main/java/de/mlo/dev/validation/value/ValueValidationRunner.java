package de.mlo.dev.validation.value;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author mlo
 */
@FunctionalInterface
public interface ValueValidationRunner<V> {

    /**
     * Has to execute the given {@link ValueValidationSummarizer#validate(Object) instructions}
     * and has to aggregate the all results to one.<br>
     * It is up to the runner, how to execute the instructions and when the process
     * stops.<br>
     * <br>
     * Predefined runners:
     * <ul>
     *     <li>{@link ValueValidationRunners#validateAll(Object, List)} - Executes all instructions</li>
     *     <li>{@link ValueValidationRunners#validateStopOnFirstFail(Object, List)} - Executes the
     *     instructions and stops if one instruction failed</li>
     * </ul>
     *
     * @param value      The value which has to be validated
     * @param validators A list of instructions which has to be executed. The list contains the
     *                   instruction in the order they have been added
     * @return An aggregated result of the given instructions
     */
    @NotNull
    ValueValidationResult<V> validate(V value, List<ValueValidationSummarizer<V>> validators);
}
