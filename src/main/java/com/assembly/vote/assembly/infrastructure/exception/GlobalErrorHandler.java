package com.assembly.vote.assembly.infrastructure.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Configuration
@Order(-2)
@RestControllerAdvice
@Log4j2
public class GlobalErrorHandler implements ErrorWebExceptionHandler {


    public static final String UTF_8 = "UTF-8";

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
        log.error(throwable.getMessage(), throwable);

        var message = Objects.nonNull(throwable.getLocalizedMessage()) ? throwable.getLocalizedMessage() : "";
        var errorDetails = new ErrorDetails(System.currentTimeMillis(),
                message,
                throwable.getMessage());

        var bufferFactory = serverWebExchange.getResponse().bufferFactory();
        var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        httpStatus = constraintViolation(throwable, errorDetails, httpStatus);
        httpStatus = domainBusinessException(throwable, errorDetails, httpStatus);
        httpStatus = tooManyRequestException(throwable, errorDetails, httpStatus);
        httpStatus = unauthorizedRequestException(throwable, errorDetails, httpStatus);
        httpStatus = notFoundException(throwable, errorDetails, httpStatus);
        httpStatus = infrastructureException(throwable, errorDetails, httpStatus);
        httpStatus = resourceNotFoundException(throwable, errorDetails, httpStatus);

        serverWebExchange.getResponse().setStatusCode(httpStatus);
        serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String errorDetailsMessage = "{ "+"\"error\":"+" \""+errorDetails.getMessage()+"\""+" }";
        var dataBuffer = bufferFactory.wrap(errorDetailsMessage.getBytes(StandardCharsets.UTF_8));

        return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
    }
    protected HttpStatus unauthorizedRequestException(Throwable throwable, ErrorDetails errorDetails, HttpStatus httpStatus) {
        if (throwable instanceof UnauthorizedException) {
            errorDetails.setMessage(((UnauthorizedException) throwable).getReason());
            httpStatus = HttpStatus.UNAUTHORIZED;
        } else if (throwable instanceof ResourceAccessException) {
            errorDetails.setMessage("Não foi possível consumir o recurso remoto.");
            httpStatus = HttpStatus.UNAUTHORIZED;
        }
        return httpStatus;
    }

    protected HttpStatus notFoundException(Throwable throwable, ErrorDetails errorDetails, HttpStatus httpStatus) {
        if (throwable instanceof NotFoundException) {
            errorDetails.setMessage(((NotFoundException) throwable).getReason());
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (throwable instanceof ResourceAccessException) {
            errorDetails.setMessage("Não foi possível consumir o recurso remoto.");
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return httpStatus;
    }


    protected HttpStatus domainBusinessException(Throwable throwable, ErrorDetails errorDetails, HttpStatus httpStatus) {
        if (throwable instanceof DomainBusinessException) {
            errorDetails.setMessage(((DomainBusinessException) throwable).getReason());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        } else if (throwable instanceof ResourceAccessException) {
            errorDetails.setMessage("Não foi possível consumir o recurso remoto.");
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        }
        return httpStatus;
    }

    protected HttpStatus tooManyRequestException(Throwable throwable, ErrorDetails errorDetails, HttpStatus httpStatus) {
        if (throwable instanceof TooManyRequestException) {
            errorDetails.setMessage(((TooManyRequestException) throwable).getReason());
            httpStatus = HttpStatus.TOO_MANY_REQUESTS;
        } else if (throwable instanceof ResourceAccessException) {
            errorDetails.setMessage("Não foi possível consumir o recurso remoto.");
            httpStatus = HttpStatus.TOO_MANY_REQUESTS;
        }
        return httpStatus;
    }

    protected HttpStatus infrastructureException(final
                                                 Throwable throwable,
                                                 final ErrorDetails errorDetails,
                                                 final HttpStatus httpStatus) {
        if (throwable instanceof InfrastructureException) {
            errorDetails.setMessage(((InfrastructureException) throwable).getMessage());
        }
        return httpStatus;
    }

    protected HttpStatus resourceNotFoundException(Throwable throwable, ErrorDetails errorDetails, HttpStatus httpStatus) {
        if (throwable instanceof NotFoundException) {
            var message = ((NotFoundException) throwable).getMessage();
            if (Objects.nonNull(message)) {
                errorDetails.setMessage(message);
            }
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return httpStatus;
    }

    protected HttpStatus constraintViolation(Throwable throwable, ErrorDetails errorDetails, HttpStatus httpStatus) {
        if (throwable instanceof WebExchangeBindException) {
            var webExchangeBindException = (WebExchangeBindException) throwable;
            if (Objects.nonNull(webExchangeBindException.getAllErrors())) {
                StringBuilder stringBuilder = new StringBuilder();
                webExchangeBindException.getAllErrors().forEach(objectError -> {
                    stringBuilder.append(objectError.getDefaultMessage() + "\n");
                });
                errorDetails.setMessage(stringBuilder.toString());
            }
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        }
        return httpStatus;
    }


}
