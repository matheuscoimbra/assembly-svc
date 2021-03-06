package com.assembly.vote.assembly.infrastructure.config;


import com.assembly.vote.assembly.infrastructure.annotation.ValidCPF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.reactive.function.client.WebClient;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
@Configurable
public class CpfValidatorConfig implements ConstraintValidator<ValidCPF, String> {
    private final WebClient client;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {


        value = value.replaceAll("\\s", "");

        if ("".equals(value)) {
            return false;
        }

        try {
            String cpf = value;
            var result = client.get().uri(uriBuilder -> uriBuilder.path("/{CPF}").build(cpf)).retrieve().bodyToMono(String.class);
            AtomicBoolean isValid  = new AtomicBoolean(false);
            result.doOnNext(s -> {
                if(s.equals("ABLE_TO_VOTE")){
                    isValid.set(true);
                }
            }).subscribe();
            return isValid.get();

        } catch (Exception e) {
            throw new RuntimeException("Informe um CPF válido");
        }

    }
}
