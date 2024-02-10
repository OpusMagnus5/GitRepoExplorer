package pl.bodzioch.damian.advice;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.bodzioch.damian.common.ErrorResponse;
import pl.bodzioch.damian.exception.AppException;

import java.util.Locale;

@Order(1)
@RestControllerAdvice
@AllArgsConstructor
public class CustomControllerAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler({AppException.class})
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(ex.getMessage(), ex.getMessageParams().toArray(), locale);

        return ResponseEntity.status(ex.getHttpStatus()).body(new ErrorResponse(ex.getHttpStatus().value(), message));
    }
}
