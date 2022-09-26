package de.mlo.dev.validation.value;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/**
 * <p>
 * The ValueValidatorConditional only starts the validation if a given
 * condition is met.
 * </p>
 * <p>
 * To start a new conditional validator, use the
 * {@link ValueValidator#conditionBuilder(Predicate)} and call
 * {@link #build()} when you are done.
 * </p>
 *
 * @author mlo
 */
public class ValueValidatorConditional<V, P extends ValueValidator<V>> extends ValueValidator<V> {

    private static final Logger LOGGER = LogManager.getLogger(ValueValidatorConditional.class.getName());

    private final P parent;
    private final Predicate<V> condition;

    ValueValidatorConditional(P parent, Predicate<V> condition) {
        this.parent = parent;
        this.condition = condition;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ValueValidatorConditional<V, P> add(@Nullable ValueValidationStatement<V> statement) {
        return (ValueValidatorConditional<V, P>) super.add(statement);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ValueValidatorConditional<V, P> addSummarizer(@Nullable ValueValidationSummarizer<V> validationSummarizer) {
        return (ValueValidatorConditional<V, P>) super.addSummarizer(validationSummarizer);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public ValueValidatorGroup<V, ? extends ValueValidatorConditional<V, P>> groupBuilder() {
        return (ValueValidatorGroup<V, ? extends ValueValidatorConditional<V, P>>) super.groupBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public ValueValidatorConditional<V, ? extends ValueValidatorConditional<V, P>> conditionBuilder(Predicate<V> condition) {
        return (ValueValidatorConditional<V, ? extends ValueValidatorConditional<V, P>>) super.conditionBuilder(condition);
    }

    /**
     * <p>
     * The validation won't be executed if the given condition is not met.
     * </p>
     * <hr>
     * {@inheritDoc}
     */
    @Override
    public @NotNull ValueValidationResult<V> validate(V value) {
        if (condition.test(value)) {
            return super.validate(value);
        }
        LOGGER.debug("Skip validating (condition not met)");
        return new ValueValidationResult<>(value);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public P build() {
        parent.addSummarizer(this);
        return parent;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ValueValidatorConditional<V, P> setValidateAll() {
        return (ValueValidatorConditional<V, P>) super.setValidateAll();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ValueValidatorConditional<V, P> setValidateAndStopOnFirstFail() {
        return (ValueValidatorConditional<V, P>) super.setValidateAndStopOnFirstFail();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ValueValidatorConditional<V, P> setValidationRunner(@NotNull ValueValidationRunner<V> validationRunner) {
        return (ValueValidatorConditional<V, P>) super.setValidationRunner(validationRunner);
    }
}
