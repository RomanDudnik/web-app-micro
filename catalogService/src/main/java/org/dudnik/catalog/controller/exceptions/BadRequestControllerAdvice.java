package org.dudnik.catalog.controller.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.BindException;

import java.util.Locale;

/**
 * Класс для обработки ошибок и валидации заполнения форм
 * задействуем AOP
 * используем аннотацию @ControllerAdvice - для обработки ошибок
 */

@ControllerAdvice
@RequiredArgsConstructor
public class BadRequestControllerAdvice {

    // внедрение зависимости - локализатора сообщений
    private final MessageSource messageSource;

    // обработка ошибок валидации
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(BindException exception, Locale locale) {
        // возвращаем ошибки
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST,
                        this.messageSource.getMessage("errors.400.name", new Object[0],
                                "errors.400.name", locale));
        problemDetail.setProperty("errors", exception.getAllErrors().stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .toList());

        return ResponseEntity.badRequest()
                .body(problemDetail);
    }
}
