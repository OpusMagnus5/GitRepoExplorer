package pl.bodzioch.damian.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Getter
public class AppException extends RuntimeException {

    private final List<String> messageParams;
    private final HttpStatus httpStatus;

    public AppException(String message, HttpStatus httpStatus) {
        super(message);
        this.messageParams = Collections.emptyList();
        this.httpStatus = httpStatus;
    }

    public AppException(String message, HttpStatus httpStatus, Exception exception) {
        super(message, exception);
        this.messageParams = Collections.emptyList();
        this.httpStatus = httpStatus;
    }
}
