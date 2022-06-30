package de.mlo.dev.validation.basic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * <p>
 * The {@link ValidatorGroup} groups several validation statements.
 * A group does not affect the parent validator directly. The parent validator
 * may react on the group's validation result and may stop the validation
 * process.
 * </p>
 * <p>
 * To start a new group, use the {@link Validator#groupBuilder()} and call
 * {@link #build()} when you are done.
 * </p>
 *
 * @author mlo
 */
public class ValidatorGroup extends Validator {
    private final Validator parent;

    ValidatorGroup(Validator parent) {
        this.parent = Objects.requireNonNull(parent);
    }

    @NotNull
    @Override
    public ValidatorGroup add(@Nullable ValidationStatement statement) {
        return (ValidatorGroup) super.add(statement);
    }

    @NotNull
    @Override
    public ValidatorGroup add(@Nullable ValidationSummarizer validationSummarizer) {
        return (ValidatorGroup) super.add(validationSummarizer);
    }

    @Override
    public Validator build() {
        parent.add(this);
        return parent;
    }

    @Override
    @NotNull
    public ValidatorGroup setValidateStopOnFirstFail() {
        return (ValidatorGroup) super.setValidateStopOnFirstFail();
    }

    @Override
    @NotNull
    public ValidatorGroup setValidateAll() {
        return (ValidatorGroup) super.setValidateAll();
    }

    @Override
    @NotNull
    public ValidatorGroup setValidationRunner(@NotNull ValidationRunner validationRunner) {
        return (ValidatorGroup) super.setValidationRunner(validationRunner);
    }


}
