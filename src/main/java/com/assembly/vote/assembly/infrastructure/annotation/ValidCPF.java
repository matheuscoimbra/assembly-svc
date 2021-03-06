package com.assembly.vote.assembly.infrastructure.annotation;


import com.assembly.vote.assembly.infrastructure.config.CpfValidatorConfig;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CpfValidatorConfig.class)
public @interface ValidCPF {
    String message() default "Cpf inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}