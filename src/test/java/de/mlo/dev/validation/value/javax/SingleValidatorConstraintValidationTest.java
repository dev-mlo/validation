package de.mlo.dev.validation.value.javax;

import de.mlo.dev.validation.ValidationInfo;
import de.mlo.dev.validation.value.ValueValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author mlo
 */
class SingleValidatorConstraintValidationTest {

    @Test
    void test() {
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()){
            Validator validator = factory.getValidator();

            TestBean testBean = new TestBean()
                    .setTestField("");
            List<ConstraintViolation<TestBean>> result = new ArrayList<>(validator.validate(testBean));
            result.sort(Comparator.comparing(ConstraintViolation::getMessage));
            assertEquals(2, result.size());
            assertEquals("Field is empty", result.get(0).getMessage());
            assertEquals("Text is empty", result.get(1).getMessage());
        }
    }

    @Accessors(chain = true)
    @Setter
    @Getter
    @ValidatedBy(processors = TestBeanValidator.class)
    public static class TestBean{
        @ValidatedBy(processors = TestFieldValidator.class)
        private String testField;
    }

    public static class TestBeanValidator extends ValueValidator<TestBean> {
        public TestBeanValidator(){
            add(this::validateField);
        }

        public @NotNull ValidationInfo validateField(TestBean testBean){
            if( testBean.getTestField().isEmpty()){
                return ValidationInfo.invalid("Field is empty");
            }
            return ValidationInfo.valid();
        }
    }

    public static class TestFieldValidator extends ValueValidator<String> {
        public TestFieldValidator() {
            add(this::validateEmpty);
        }

        public @NotNull ValidationInfo validateEmpty(String text) {
            if (text.isEmpty()) {
                return ValidationInfo.invalid("Text is empty");
            }
            return ValidationInfo.valid();
        }
    }
}