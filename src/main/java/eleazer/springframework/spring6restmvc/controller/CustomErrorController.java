package eleazer.springframework.spring6restmvc.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity handleInsertErrors(MethodArgumentNotValidException exception) {
        List errorList = exception.getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                    return errorMap;
                }).collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errorList);
    }

    @ExceptionHandler(TransactionSystemException.class)
    ResponseEntity handeJPAViolations(TransactionSystemException exception) {
        ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();

        if (exception.getCause().getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException cvException = (ConstraintViolationException) exception.getCause().getCause();

            List errors = cvException.getConstraintViolations().stream()
                    .map(error -> {
                        Map<String, String> errorMap = new HashMap<>();
                        errorMap.put(error.getPropertyPath().toString(), error.getMessage());
                        return errorMap;

                    }).collect(Collectors.toList());

            return responseEntity.body(errors);
        }
        return responseEntity.build();

    }
}
