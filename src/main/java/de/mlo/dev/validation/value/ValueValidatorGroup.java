package de.mlo.dev.validation.value;

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

    @Override
    public ValueValidator<V> build() {
        parent.add(this);
        return parent;
    }
}
