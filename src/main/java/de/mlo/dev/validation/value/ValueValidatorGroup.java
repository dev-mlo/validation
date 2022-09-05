package de.mlo.dev.validation.value;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
public class ValueValidatorGroup<V> extends ValueValidator<V> {

    private final ValueValidator<V> parent;

    ValueValidatorGroup(ValueValidator<V> parent) {
        this.parent = parent;
    }

    @NotNull
    @Override
    public ValueValidatorGroup<V> add(@Nullable ValueValidationStatement<V> statement) {
        return (ValueValidatorGroup<V>) super.add(statement);
    }

    @NotNull
    @Override
    public ValueValidatorGroup<V> addSummarizer(@Nullable ValueValidationSummarizer<V> validationSummarizer) {
        return (ValueValidatorGroup<V>) super.addSummarizer(validationSummarizer);
    }

    @NotNull
    @Override
    public ValueValidator<V> build() {
        parent.addSummarizer(this);
        return parent;
    }

    @NotNull
    @Override
    public ValueValidatorGroup<V> setValidateAll() {
        return (ValueValidatorGroup<V>) super.setValidateAll();
    }

    @NotNull
    @Override
    public ValueValidatorGroup<V> setValidateAndStopOnFirstFail() {
        return (ValueValidatorGroup<V>) super.setValidateAndStopOnFirstFail();
    }

    @NotNull
    @Override
    public ValueValidatorGroup<V> setValidationRunner(@NotNull ValueValidationRunner<V> validationRunner) {
        return (ValueValidatorGroup<V>) super.setValidationRunner(validationRunner);
    }
}
