package srcp.web.controllers.v01ws;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import srcp.exceptions.UserNotFoundException;

import java.util.Objects;

@RestController
@ControllerAdvice(annotations = RestController.class)
public class SRCPController {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFound(RuntimeException e, WebRequest request) {
        return new ResponseEntity<Object>(String.format("User (%s) could not be found", e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
