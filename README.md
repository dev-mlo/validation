# Validation

[![Maven Central](https://img.shields.io/maven-central/v/de.mlo-dev/validation.svg)](http://search.maven.org/#search|ga|1|g%3Ade.mlo-dev)

The validation framework supports you to organize the validation process of your data. You can add, group, chain and
execute multiple validation statements and aggregate the result

## Getting started

For Maven

```xml

<dependency>
    <groupId>de.mlo-dev</groupId>
    <artifactId>validation</artifactId>
    <version>0.2.1</version>
</dependency>
```

For Gradle

```gradle
implementation group: 'de.mlo-dev', name: 'validation', version: '0.1.0'
```

If you are using Java modules:

```
requires de.mlo.dev.validation
```

## Usage

### Basic

It is recommended that you separate validation statements into functions. This makes the code clearer and makes it more
testable. To execute the statements use the ```Validator```. The
```Validator``` will execute the added statement and aggregates the result. Check the returned
```ValidationResult``` if process was successful or not.

```java
public class PersonValidator {
    public ValidationResult validate() {
        return new Validator()
                .add(this::validateName)
                .add(this::validateAge)
                .validate();
    }

    ValidationInfo validateName() {
        String name = unbindName();
        if (name.isBlank()) {
            return ValidationInfo.invalid("Name is empty");
        }
        return ValidationInfo.valid();
    }

    ValidationInfo validateAge() {
        int age = unbindAge();
        if (age <= 0) {
            return ValdationInfo.invalid("Age must be positive value");
        }
        return ValidationInfo.valid();
    }
}
```

***

### Value dependencies

Validate data that depends on each other using ```setValidateStopOnFirstFail```.

```java
public class DependingValueValidation {
    public ValidationResult validateContact(Person person) {
        return new Validator()
                .add(() -> validateNull(person))
                .add(() -> validateName(person.getName())) // Won't be executed if 'person' was null
                .add(() -> validateAddress(person.getAddress())) // Won't be executed if 'person' was null or 'name' was empty
                .setValidateStopOnFirstFail()
                .validate();
    }
}
```

***

### Groups

Group multiple validation processes to one and define separate validation strategies.

In the following example all groups will be executed but if one statement within a group fails, no more statements of
the group will be executed.

```java
public class Groups {

    public ValidationResult validateContact() {
        return new Validator()
                .groupBuilder()
                .add(this::validateName)
                .add(this::validateAge)
                .setValidateStopOnFirstFail()
                .build()
                .groupBuilder()
                .add(this::validateStreet)
                .add(this::validateZipAndTown)
                .setValidateStopOnFirstFail()
                .build()
                .validate();
    }
}
```

You can also define separate

***

## Customization

The validator comes with two modes: validate all or stop on first fail. You can add your own execution logic by setting
your own ```ValidationRunner``` using
```Validator.setValidationRunner(...)```.

```java
class CustomRunnerValidation {
    public ValidationResult validate() {
        return new Validator()
                .setValidationRunner(this::execute)
                .validate();
    }

    /** This runner fails if there is no instruction to execute */
    ValidationResult execute(List<ValidationInstruction> instructionList) {
        if (instructionList.isEmpty()) {
            return ValidationResult.invalid("No instructions found");
        }
        return ValidationRunners.VALIDATE_ALL.validate(instructionList);
    }
}
```

***

## Type specific validator

Use the ```ValueValidator``` to validate a specific type. This allows you to build reusable Validators.

```java
import de.mlo.dev.validation.basic.ValidationResult;

public class PersonValidator {

    public static ValueValidator<Person> PERSON_VALIDATOR = createValidator();

    static ValidationResult validate(Person person) {
        return PERSON_VALIDATOR.validate(person);
    }

    static ValueValidator<Person> createValidator() {
        return ValueValidator.create(Person.class)
                .add(PersonValidator::validateName)
                .add(PersonValidator::validateAge);
    }

    static ValidationInfo validateName(Person person) {
        if (person.getName().isBlank()) {
            return ValidationInfo.invalid("Name is empty");
        }
        return ValidationInfo.valid();
    }

    static ValidationInfo validateAge(Person person) {
        if (person.getAge() <= 0) {
            return ValdationInfo.invalid("Age must be positive value");
        }
        return ValidationInfo.valid(person);
    }
}
```