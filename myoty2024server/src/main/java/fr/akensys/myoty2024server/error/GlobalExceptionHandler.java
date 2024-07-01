package fr.akensys.myoty2024server.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import fr.akensys.myoty2024server.error.models.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SimCardNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSimCardNotFoundException(SimCardNotFoundException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), System.currentTimeMillis(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexcepted error occurred: " + ex.getMessage(), System.currentTimeMillis(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
