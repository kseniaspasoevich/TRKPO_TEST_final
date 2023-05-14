package documents.jpa.exceprions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, reason="Constraints Exception")
public class ConstraintsException extends RuntimeException{
}
