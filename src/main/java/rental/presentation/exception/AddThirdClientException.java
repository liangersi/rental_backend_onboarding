package rental.presentation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class AddThirdClientException extends IllegalArgumentException {
    public AddThirdClientException(String message) {
        super(message);
    }
}
