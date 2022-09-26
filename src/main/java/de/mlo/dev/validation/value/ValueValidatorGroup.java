package de.mlo.dev.validation.value;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/**
 * <p>
 * The ValueValidatorGroup groups several validation statements.
 * A group does not affect the parent validator directly. The parent validator
 * may react on the group's validation result and may stop the validation
 * process.
 * </p>
 * <p>
 * To start a new group, use the {@link ValueValidator#groupBuilder()} and call
 * {@link #build()} when you are done.
 * </p>
 *
 * @author mlo
 */
public class ValueValidatorGroup<V, P extends ValueValidator<V>> extends ValueValidator<V> {

    private final P parent;

    ValueValidatorGroup(P parent) {
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ValueValidatorGroup<V, P> add(@Nullable ValueValidationStatement<V> statement) {
        return (ValueValidatorGroup<V, P>) super.add(statement);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ValueValidatorGroup<V, P> addSummarizer(@Nullable ValueValidationSummarizer<V> validationSummarizer) {
        return (ValueValidatorGroup<V, P>) super.addSummarizer(validationSummarizer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ValueValidatorGroup<V, ? extends ValueValidatorGroup<V, P>> groupBuilder() {
        return (ValueValidatorGroup<V, ? extends ValueValidatorGroup<V, P>>) super.groupBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public ValueValidatorConditional<V, ? extends ValueValidatorGroup<V, P>> conditionBuilder(Predicate<V> condition) {
        return (ValueValidatorConditional<V, ValueValidatorGroup<V, P>>) super.conditionBuilder(condition);
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

    @Override
    public @NotNull ValueValidationResult<V> validate(V value) {
        return super.validate(value);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ValueValidatorGroup<V, P> setValidateAll() {
        return (ValueValidatorGroup<V, P>) super.setValidateAll();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ValueValidatorGroup<V, P> setValidateAndStopOnFirstFail() {
        return (ValueValidatorGroup<V, P>) super.setValidateAndStopOnFirstFail();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ValueValidatorGroup<V, P> setValidationRunner(@NotNull ValueValidationRunner<V> validationRunner) {
        return (ValueValidatorGroup<V, P>) super.setValidationRunner(validationRunner);
    }
}
