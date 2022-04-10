package de.mlo.dev.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link Validator} helps to group, chain and execute {@link ValidationInstruction}s.
 * <hr>
 * Example:
 * <pre>{@code
 * public ValidationResult validate(){
 *     return new Validator()
 *          .add(this::validateName)
 *          .add(this::validateAge)
 *          .validateAll();
 * }
 *
 * ValidationInfo validateName(){
 *     String name = unbindName();
 *     if(name.isBlank()){
 *        return ValidationInfo.invalid("Name is empty");
 *     }
 *     return ValidationInfo.valid();
 * }
 *
 * ValidationInfo validateAge(){
 *     int age = unbindAge();
 *     if(age <= 0){
 *         return ValdationInfo.invalid("Age must be positive value")
 *     }
 *     return ValidationInfo.valid();
 * }
 * }</pre>
 *
 * @author mlo
 */
public class Validator {
    private final List<ValidationInstruction> instructions = new ArrayList<>();

    /**
     * Adds a new {@link ValidationInstruction} to the list of existent instructions.
     * The added instruction will be appended at the end of the list. The order of
     * the instructions is maintained.<br>
     * Execute the added instructions which {@link #validateAll()} or
     * {@link #validateStopOnFirstFail()}.
     *
     * @param instruction A new instruction to add to the end of the list of instructions.
     *                    Null values are ignored
     * @return An instance of this {@link Validator} so you can chain 'add' calls
     */
    public Validator add(ValidationInstruction instruction) {
        if (instruction != null) {
            this.instructions.add(instruction);
        }
        return this;
    }

    /**
     * Executes all added {@link ValidationInstruction}s in the order they have been
     * added. If a single instruction fails, the {@link ValidationResult#isValid()}
     * function will return <code>false</code>. Only if all instruction passes the test
     * the result will be valid ({@link ValidationResult#isValid()} will be
     * <code>true</code>).<br>
     * The validation result also contains the result of every single instructions
     * and can aggregate all failure messages: {@link ValidationResult#getMessage()}.
     *
     * @return An aggregated {@link ValidationResult}
     */
    public ValidationResult validateAll() {
        ValidationResult result = new ValidationResult();
        for (ValidationInstruction instruction : instructions) {
            ValidationInfo info = instruction.validate();
            result.add(info);
        }
        return result;
    }

    /**
     * Executes the added {@link ValidationInstruction}s in the order they have been
     * added until the first {@link ValidationInstruction} fail. If a single instruction
     * fails, the {@link ValidationResult#isValid()} function will return <code>false</code>.
     * Only if all instruction passes the test the result will be valid
     * ({@link ValidationResult#isValid()} will be <code>true</code>).<br>
     * The validation result also contains the result of every single instructions
     * and can aggregate all failure messages: {@link ValidationResult#getMessage()}.
     *
     * @return An aggregated {@link ValidationResult}
     */
    public ValidationResult validateStopOnFirstFail() {
        ValidationResult result = new ValidationResult();
        for (ValidationInstruction instruction : instructions) {
            ValidationInfo info = instruction.validate();
            result.add(info);
            if (result.isInvalid()) {
                return result;
            }
        }
        return result;
    }
}
