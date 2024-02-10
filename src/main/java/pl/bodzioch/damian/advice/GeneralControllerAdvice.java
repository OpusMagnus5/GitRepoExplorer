package pl.bodzioch.damian.advice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.bodzioch.damian.common.ErrorResponse;

import java.util.Locale;

@Order(2)
@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GeneralControllerAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleAppException(Exception ex) {
        log.error("An unexpected Error occurred.", ex);
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage("general.error", new Object[0], locale);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), message));
    }
}
