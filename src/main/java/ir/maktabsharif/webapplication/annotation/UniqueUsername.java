package ir.maktabsharif.webapplication.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {
    String message() default "نام کاربری قبلا انتخاب شده است.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
