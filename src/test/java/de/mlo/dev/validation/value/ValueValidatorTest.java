package de.mlo.dev.validation.value;

import de.mlo.dev.validation.Statements;
import de.mlo.dev.validation.ValidationInfo;
import lombok.Builder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author mlo
 */
class ValueValidatorTest {

    @Test
    void testEmpty() {
        ValueValidationResult<Object> result = new ValueValidator<>()
                .validate("Test");
        assertTrue(result.isValid());
        assertTrue(result.getValidationInfos().isEmpty());
    }

    /**
     * All groups and all statements will be executed.
     */
    @Test
    void testGroups() {
        ValueValidationResult<String> result = new ValueValidator<String>()
                .groupBuilder()
                .add(null)
                .add((s) -> ValidationInfo.valid("1"))
                .add((s) -> ValidationInfo.valid("2"))
                .build()
                .groupBuilder()
                .add((s) -> ValidationInfo.valid("3"))
                .add((s) -> ValidationInfo.valid("4"))
                .build()
                .setValidateAll()
                .validate("Test");
        assertTrue(result.getMessagesTextList().contains("1"));
        assertTrue(result.getMessagesTextList().contains("2"));
        assertTrue(result.getMessagesTextList().contains("3"));
        assertTrue(result.getMessagesTextList().contains("4"));
    }

    /**
     * Both group validations are executed.
     */
    @Test
    void testGroups2() {
        ValueValidator<String> validator = ValueValidator.create(String.class)
                .groupBuilder()
                .add((s) -> ValidationInfo.valid("1"))
                .add((s) -> ValidationInfo.invalid("2"))
                .add((s) -> ValidationInfo.valid("3"))
                .setValidateAndStopOnFirstFail()
                .build();
        ValueValidationResult<String> result = validator.validate("Test");
        assertTrue(result.getMessagesTextList().contains("1"));
        assertTrue(result.getMessagesTextList().contains("2"));

        validator = validator.groupBuilder()
                .add((s) -> ValidationInfo.valid("4"))
                .add((s) -> ValidationInfo.invalid("5"))
                .add((s) -> ValidationInfo.valid("6"))
                .setValidateAndStopOnFirstFail()
                .build();

        result = validator.validate("Test");
        assertTrue(result.getMessagesTextList().contains("1"));
        assertTrue(result.getMessagesTextList().contains("2"));
        assertTrue(result.getMessagesTextList().contains("4"));
        assertTrue(result.getMessagesTextList().contains("5"));
    }

    /**
     * If the first group validation fails, the second group is not validated.
     */
    @Test
    void testGroups3() {
        ValueValidationResult<String> result = ValueValidator.create(String.class)
                .groupBuilder()
                .add((s) -> ValidationInfo.valid("1"))
                .add((s) -> ValidationInfo.invalid("2"))
                .add((s) -> ValidationInfo.valid("3"))
                .setValidateAndStopOnFirstFail()
                .add((s) -> ValidationInfo.valid("4"))
                .add((s) -> ValidationInfo.invalid("5"))
                .add((s) -> ValidationInfo.valid("6"))
                .setValidateAndStopOnFirstFail()
                .build()
                .validateAndStopOnFirstFail("Test");
        assertTrue(result.getMessagesTextList().contains("1"));
        assertTrue(result.getMessagesTextList().contains("2"));
    }

    @Test
    void testSwitchValue(){
        ValueValidator<ParentBean> validator = ValueValidator.create(ParentBean.class)
                .add(p -> Statements.notBlank(p.header))
                .switchValue(ParentBean::getChildBean)
                .add(c -> Statements.notBlank(c.childText))
                .switchBack(ParentBean.class)
                .add(p -> Statements.notBlank(p.content));
        assertThat(validator.getParentValidator()).isNull();

        ParentBean parentBean = ParentBean.builder().childBean(new ChildBean()).build();
        ValueValidationResult<ParentBean> result = validator.validate(parentBean);
        assertThat(result.isValid()).isFalse();
        assertThat(result.getValue()).isEqualTo(parentBean);
        assertThat(result.getValidationInfos()).hasSize(3);
    }

    @Test
    void testSwitchValueAndGroup(){
        ValueValidator<ParentBean> validator = ValueValidator.create(ParentBean.class)
                .add(p -> Statements.notBlank(p.header)) // 1
                .switchValue(ParentBean::getChildBean)
                .groupBuilder()
                .add(c -> Statements.notBlank(c.childText)) // 2
                .add(c -> Statements.notBlank(c.childText)) // 3
                .build()
                .switchBack(ParentBean.class)
                .add(p -> Statements.notBlank(p.content)); // 4
        assertThat(validator.getParentValidator()).isNull();

        ParentBean parentBean = ParentBean.builder().childBean(new ChildBean()).build();
        ValueValidationResult<ParentBean> result = validator.validate(parentBean);
        assertThat(result.isValid()).isFalse();
        assertThat(result.getValue()).isEqualTo(parentBean);
        assertThat(result.getValidationInfos()).hasSize(4);
    }

    @Test
    void testSwitchValueWithSupplier(){
        ValueValidator<Integer> validator = ValueValidator.create(Integer.class)
                .add(Statements::positive) // 1
                .switchValue(() -> "")
                .add(Statements::notBlank)
                .switchBack();
        assertThat(validator.getParentValidator()).isNull();

        ValueValidationResult<Integer> result = validator.validate(0);
        assertThat(result.isValid()).isFalse();
        assertThat(result.getValue()).isEqualTo(0);
        assertThat(result.getValidationInfos()).hasSize(2);
    }

    @Builder
    private static class ParentBean{
        private final String header;
        private final String content;
        private final ChildBean childBean;

        public ChildBean getChildBean() {
            return childBean;
        }
    }

    private static class ChildBean{
        String childText;
    }
}