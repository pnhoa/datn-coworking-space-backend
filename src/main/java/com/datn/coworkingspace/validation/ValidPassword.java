package com.datn.coworkingspace.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidPassword {
	
	String message() default "Invalid password";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};

}
