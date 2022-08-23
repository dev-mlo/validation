package de.mlo.dev.validation.value;

import de.mlo.dev.validation.basic.ValidationRunner;
import de.mlo.dev.validation.basic.ValidationSummarizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The {@link ValueValidator} helps to group, chain and execute {@link ValueValidationStatement}s.
 * <hr>
 * Example:
 * <pre>{@code
 * public ValueValidationResult validate(Person person){
 *     return new ValueValidator()
 *          .add(this::validateName)
 *          .add(this::validateAge)
 *          .validateAll(person);
 * }
 *
 * ValidationInfo validateName(Person person){
 *     if(person.getName().isBlank()){
 *        return ValidationInfo.invalid("Name is empty");
 *     }
 *     return ValidationInfo.valid();
 * }
 *
 * ValidationInfo validateAge(Person person){
 *     if(person.getAge() <= 0){
 *         return ValidationInfo.invalid("Age must be positive value")
 *     }
 *     return ValidationInfo.valid();
 * }
 * }</pre>
 * <hr>
 * Logging: Depending on the {@link ValueValidationRunner} you choose you will get
 * detailed information about the validation process if you set the log level
 * for this package to debug. The used logging framework ist
 * <code>org.apache.logging.log4j.Logger</code>
 *
 * @author mlo
 */
public class ValueValidator<V> implements IsValueValidator<V>, ValueValidationSummarizer<V> {
    /**
     * The list contains {@link ValueValidationSummarizer} which has to be executed
     * from a {@link ValueValidationRunner}
     */
    private final List<ValueValidationSummarizer<V>> aggregators = new ArrayList<>();

    /**
     * The applied {@link ValueValidationRunner} defines how the list of added
     * statements are executed.
     */
    private ValueValidationRunner<V> validationRunner = ValueValidationRunners::validateAll;

    /**
     * Use this factory method to avoid casting.
     *
     * @param type The type of the value to validate.
     * @param <V>  The type of the value to validate.
     * @return A new instance of {@link ValueValidator}.
     */
    @SuppressWarnings("unused")
    public static <V> ValueValidator<V> create(Class<V> type) {
        return new ValueValidator<>();
    }

    /**
     * Adds a new {@link ValueValidationStatement} to the list of existent statements.
     * The added statement will be appended at the end of the list. The order of
     * the statements is maintained.<br>
     * Execute the added statements which {@link #validate(Object)}
     * <hr>
     * Example:
     * <pre>{@code
     * public ValidationResult validate(Person person){
     *     return new ValueValidator()
     *      .add((p) -> validateName(p.getName())
     *      .add((p) -> validateAge(p.getAge())
     *      .add((p) -> validateAddress(p.getAddress())
     *      .validate(person);
     * }
     * }</pre>
     * <hr>
     * For more complex stuff like grouping and nesting you can use the other
     * {@link #add(ValueValidationSummarizer) add} function which accepts whole
     * {@link ValueValidator valdiators} and {@link ValueValidationResult results}.
     *
     * @param statement A new statement to add to the end of the list of statements.
     *                  Null values are ignored
     * @return An instance of this {@link ValueValidator} so you can chain 'add' calls
     */
    @NotNull
    public ValueValidator<V> add(@Nullable ValueValidationStatement<V> statement) {
        return add((ValueValidationSummarizer<V>) statement);
    }

    /**
     * Adds a {@link ValueValidationSummarizer}. A {@link ValueValidationSummarizer summarizer} can
     * execute multiple parts of the validation process and can aggregate the result of
     * these.<br>
     * This function allows you to nest different {@link ValueValidator validators} with their own
     * runner.
     * <hr>
     * Example: Check that the given person is not null and if this is true, the persons fields
     * will be validated all
     * <pre>{@code
     * public ValueValidationResult validatePerson(Person person){
     *     return new ValueValidator()
     *      .add(new ValueValidator().add((p) -> validatePersonNotNull(p))
     *      .add(new ValueValidator()
     *          .add((p) -> validateName(p.getName())
     *          .add((p) -> validateAge(p.getAge()))
     *      .validateStopOnFirstFail(person)
     * }
     * }</pre>
     *
     * @param validationSummarizer A {@link ValueValidationSummarizer} which can be a {@link ValueValidator}
     *                             or {@link ValueValidationResult} for example. If the parameter is null
     *                             it will be ignored.
     * @return The instance of this validator
     */
    @NotNull
    public ValueValidator<V> add(@Nullable ValueValidationSummarizer<V> validationSummarizer) {
        if (validationSummarizer != null) {
            aggregators.add(validationSummarizer);
        }
        return this;
    }

    /**
     * Starts a new {@link ValueValidatorGroup validation group} which can have multiple
     * {@link ValueValidationStatement statements} and can aggregate the result of these. Use
     * the group like this {@link ValueValidator}. When you are done with the group you can
     * finish it with {@link ValueValidatorGroup#build()}.
     *
     * @return A new instance of {@link ValueValidatorGroup}. Return to this validator with
     * {@link ValueValidatorGroup#build()}
     */
    public ValueValidatorGroup<V> groupBuilder() {
        return new ValueValidatorGroup<>(this);
    }

    /**
     * This will start the validation process. The execution process depends on the used
     * runner but usually the added {@link ValueValidationStatement statements} will be executed
     * in the order they have been added.
     * <hr>
     * Runners:
     * <ul>
     *     <li>The default runner executes all added statements</li>
     *     <li>Use {@link #setValidateAndStopOnFirstFail()} to apply a runner which stops
     *     if one statement fails</li>
     *     <li>Use {@link #setValidationRunner(ValueValidationRunner)} to apply a custom runner</li>
     * </ul>
     *
     * @param value The value to validate.
     * @return The result of the validation process.
     */
    @NotNull
    public ValueValidationResult<V> validate(V value) {
        return validationRunner.validate(value, aggregators);
    }

    /**
     * Shortcut for
     * <pre>{@code
     * validator.setValidateAndStopOnFirstFail().validate();
     * }</pre>
     * Executes the added {@link ValueValidationStatement}s in the order they have been
     * added until the first {@link ValueValidationStatement} fail. If a single statement
     * fails, the {@link ValueValidationResult#isValid()} function will return <code>false</code>.
     * Only if all statement passes the test the result will be valid
     * ({@link ValueValidationResult#isValid()} will be <code>true</code>).<br>
     * The validation result also contains the result of every single statements
     * and can aggregate all failure messages: {@link ValueValidationResult#getMessage()}.
     *
     * @param value The value to validate.
     * @return The result of the validation process.
     */
    @NotNull
    public ValueValidationResult<V> validateStopOnFirstFail(V value) {
        return setValidateAndStopOnFirstFail().validate(value);
    }

    /**
     * Executes all added {@link ValueValidationStatement}s in the order they have been
     * added. If a single statement fails, the {@link ValueValidationResult#isValid()}
     * function will return <code>false</code>. Only if all statement passes the test
     * the result will be valid ({@link ValueValidationResult#isValid()} will be
     * <code>true</code>).<br>
     * The validation result also contains the result of every single statements
     * and can aggregate all failure messages: {@link ValueValidationResult#getMessage()}.
     *
     * @return The instance of this validator
     */
    @NotNull
    public ValueValidator<V> setValidateAll() {
        return setValidationRunner(ValueValidationRunners::validateAll);
    }

    /**
     * Executes the added {@link ValueValidationStatement}s in the order they have been
     * added until the first {@link ValueValidationStatement} fail. If a single statement
     * fails, the {@link ValueValidationResult#isValid()} function will return <code>false</code>.
     * Only if all statement passes the test the result will be valid
     * ({@link ValueValidationResult#isValid()} will be <code>true</code>).<br>
     * The validation result also contains the result of every single statements
     * and can aggregate all failure messages: {@link ValueValidationResult#getMessage()}.
     *
     * @return An aggregated {@link ValueValidationResult}
     */
    @NotNull
    public ValueValidator<V> setValidateAndStopOnFirstFail() {
        return setValidationRunner(ValueValidationRunners::validateStopOnFirstFail);
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
    public ValueValidator<V> setValidationRunner(@NotNull ValueValidationRunner<V> validationRunner) {
        this.validationRunner = Objects.requireNonNull(validationRunner);
        return this;
    }

    /**
     * This function ends the group building. Must be overridden in the subclass.
     *
     * @return The subclass should return its parent validator
     */
    ValueValidator<V> build() {
        return this;
    }
}
