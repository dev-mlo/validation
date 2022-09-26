package de.mlo.dev.validation;

public class Statements {
    private Statements(){

    }

    public static ValidationInfo notNull(Object value){
        if( value == null){
            return ValidationInfo.invalid("Value was null");
        }
        return ValidationInfo.valid();
    }

    public static ValidationInfo notBlank(String value){
        if( value == null || value.isBlank()){
            return ValidationInfo.invalid("Value was blank");
        }
        return ValidationInfo.valid();
    }

    public static ValidationInfo positive(Number number){
        if( number.doubleValue() <= 0){
            return ValidationInfo.invalid("Number {0} must be positive", number);
        }
        return ValidationInfo.valid();
    }

    public static ValidationInfo betweenExcluded(int numberToTest, int startExcluded, int endExcluded){
        if(numberToTest <= startExcluded){
            return ValidationInfo.invalid("Number {0} must be greater than {1}", numberToTest, startExcluded);
        } else if( numberToTest >= endExcluded){
            return ValidationInfo.invalid("Number {0} must be lower than {1}", numberToTest, endExcluded);
        }
        return ValidationInfo.valid();
    }

    public static ValidationInfo betweenIncluded(int numberToTest, int startExcluded, int endExcluded){
        if(numberToTest < startExcluded){
            return ValidationInfo.invalid("Number {0} must be greater than {1}", numberToTest, startExcluded);
        } else if( numberToTest > endExcluded){
            return ValidationInfo.invalid("Number {0} must be lower than {1}", numberToTest, endExcluded);
        }
        return ValidationInfo.valid();
    }
}
