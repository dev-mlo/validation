package de.mlo.dev.validation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The {@link Validator} helps to group, chain and execute {@link ValidationStatement}s.
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
 * <hr>
 * Logging: Depending on the {@link ValidationRunner} you choose you will get
 * detailed information about the validation process if you set the log level
 * for this package to debug. The used logging framework ist
 * <code>org.apache.logging.log4j.Logger</code>
 *
 * @author mlo
 */
public class Validator implements ValidationSummarizer {
    /**
     * The list contains {@link ValidationSummarizer} which has to be executed
     * from a {@link ValidationRunner}
     */
    private final List<ValidationSummarizer> aggregators = new ArrayList<>();

    /**
     * The applied {@link ValidationRunner} defines how the list of added
     * statements are executed.
     */
    private ValidationRunner validationRunner = ValidationRunners.VALIDATE_ALL;

    /**
     * Adds a new {@link ValidationStatement} to the list of existent statements.
     * The added statement will be appended at the end of the list. The order of
     * the statements is maintained.<br>
     * Execute the added statements which {@link #validate()}
     * <hr>
     * Example:
     * <pre>{@code
     * public ValidationResult validate(Person person){
     *     return new Validator()
     *      .add(() -> validateName(person.getName())
     *      .add(() -> validateAge(person.getAge())
     *      .add(() -> validateAddress(person.getAddress())
     *      .validate();
     * }
     * }</pre>
     * <hr>
     * For more complex stuff like grouping and nesting you can use the other
     * {@link #add(ValidationSummarizer) add} function which accepts whole
     * {@link Validator valdiators} and {@link ValidationResult results}.
     *
     * @param statement A new statement to add to the end of the list of statements.
     *                  Null values are ignored
     * @return An instance of this {@link Validator} so you can chain 'add' calls
     */
    @NotNull
    public Validator add(@Nullable ValidationStatement statement) {
        return add((ValidationSummarizer) statement);
    }

    /**
     * Adds a {@link ValidationSummarizer}. A {@link ValidationSummarizer summarizer} can
     * execute multiple parts of the validation process and can aggregate the result of
     * these.<br>
     * This function allows you to nest different {@link Validator validators} with their own
     * runner.
     * <hr>
     * Example: Check that the given person is not null and if this is true, the persons fields
     * will be validated all
     * <pre>{@code
     * public ValidationResult validatePerson(Person person){
     *     return new Validator()
     *      .add(new Validator().add(() -> validatePersonNotNull(person))
     *      .add(new Validator()
     *          .add(() -> validateName(person.getName())
     *          .add(() -> validateAge(person.getAge()))
     *      .validateStopOnFirstFail()
     * }
     * }</pre>
     *
     * @param validationSummarizer A {@link ValidationSummarizer} which can be a {@link Validator}
     *                             or {@link ValidationResult} for example. If the parameter is null
     *                             it will be ignored.
     * @return The instance of this validator
     */
    @NotNull
    public Validator add(@Nullable ValidationSummarizer validationSummarizer) {
        if (validationSummarizer != null) {
            aggregators.add(validationSummarizer);
        }
        return this;
    }

    /**
     * This will start the validation process. The execution process depends on the used
     * runner but usually the added {@link ValidationStatement statements} will be executed
     * in the order they have been added.
     * <hr>
     * Runners:
     * <ul>
     *     <li>The default runner executes all added statements</li>
     *     <li>Use {@link #setValidateStopOnFirstFail()} to apply a runner which stops
     *     if one statement fails</li>
     *     <li>Use {@link #setValidationRunner(ValidationRunner)} to apply a custom runner</li>
     * </ul>
     *
     * @return The aggregated result of all executed statements
     */
    @NotNull
    @Override
    public ValidationResult validate() {
        return validationRunner.validate(aggregators);
    }

    /**
     * Shortcut for
     * <pre>{@code
     * validator.setValidateStopOnFirstFail().validate();
     * }</pre>
     * Executes the added {@link ValidationStatement}s in the order they have been
     * added until the first {@link ValidationStatement} fail. If a single statement
     * fails, the {@link ValidationResult#isValid()} function will return <code>false</code>.
     * Only if all statement passes the test the result will be valid
     * ({@link ValidationResult#isValid()} will be <code>true</code>).<br>
     * The validation result also contains the result of every single statements
     * and can aggregate all failure messages: {@link ValidationResult#getMessage()}.
     *
     * @return The result of the validation process. The result will contain zero
     * or only one information which indicates that the validation failed.
     */
    @NotNull
    public ValidationResult validateStopOnFirstFail() {
        return setValidateStopOnFirstFail().validate();
    }

    /**
     * Executes all added {@link ValidationStatement}s in the order they have been
     * added. If a single statement fails, the {@link ValidationResult#isValid()}
     * function will return <code>false</code>. Only if all statement passes the test
     * the result will be valid ({@link ValidationResult#isValid()} will be
     * <code>true</code>).<br>
     * The validation result also contains the result of every single statements
     * and can aggregate all failure messages: {@link ValidationResult#getMessage()}.
     *
     * @return The instance of this validator
     */
    @NotNull
    public Validator setValidateAll() {
        return setValidationRunner(ValidationRunners.VALIDATE_ALL);
    }

    /**
     * Executes the added {@link ValidationStatement}s in the order they have been
     * added until the first {@link ValidationStatement} fail. If a single statement
     * fails, the {@link ValidationResult#isValid()} function will return <code>false</code>.
     * Only if all statement passes the test the result will be valid
     * ({@link ValidationResult#isValid()} will be <code>true</code>).<br>
     * The validation result also contains the result of every single statements
     * and can aggregate all failure messages: {@link ValidationResult#getMessage()}.
     *
     * @return An aggregated {@link ValidationResult}
     */
    @NotNull
    public Validator setValidateStopOnFirstFail() {
        return setValidationRunner(ValidationRunners.VALIDATE_STOP_ON_FIRST_FAIL);
    }

    /**
     * Sets a custom {@link ValidationRunner}. A runner has to execute
     * {@link ValidationSummarizer statements} and has to aggregate the results of
     * them.
     *
     * @param validationRunner A custom validation runner
     * @return The instance of this validator
     */
    @NotNull
    public Validator setValidationRunner(@NotNull ValidationRunner validationRunner) {
        this.validationRunner = Objects.requireNonNull(validationRunner);
        return this;
    }
}
