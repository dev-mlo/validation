open module de.mlo.dev.validation {
    requires org.apache.logging.log4j;
    requires lombok;
    requires org.jetbrains.annotations;
    requires org.hibernate.validator;
    requires jakarta.validation;
    exports de.mlo.dev.validation;
    exports de.mlo.dev.validation.value;
    exports de.mlo.dev.validation.basic;
    exports de.mlo.dev.validation.value.jakarta;
}