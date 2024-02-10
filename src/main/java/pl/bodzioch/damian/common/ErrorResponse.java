package pl.bodzioch.damian.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse implements Serializable {

    private Integer status;
    private String message;
}
