package de.mlo.dev.validation;

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

    @Override
    Validator build() {
        parent.add(this);
        return parent;
    }
}
