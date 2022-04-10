# Validation

The validation framework helps to group, chain and execute validation instructions

```java
public class MyValidator{
    public ValidationResult validate(){
        return new Validator()
             .add(this::validateName)
             .add(this::validateAge)
             .validateAll();
    }
    
    ValidationInfo validateName(){
        String name = unbindName();
        if(name.isBlank()){
           return ValidationInfo.invalid("Name is empty");
        }
        return ValidationInfo.valid();
    }
    
    ValidationInfo validateAge(){
        int age = unbindAge();
        if(age <= 0){
            return ValdationInfo.invalid("Age must be positive value");
        }
        return ValidationInfo.valid();
    }
}
```