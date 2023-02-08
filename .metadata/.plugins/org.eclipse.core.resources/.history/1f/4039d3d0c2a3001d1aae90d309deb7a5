package com.bezkoder.spring.security.postgresql.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bezkoder.spring.security.postgresql.exception.BusinessException;
import com.bezkoder.spring.security.postgresql.exception.FileNotFoundException;
import com.bezkoder.spring.security.postgresql.exception.InvalidUserIdException;
import com.bezkoder.spring.security.postgresql.exception.NoCommentsListException;
import com.bezkoder.spring.security.postgresql.exception.UnAuthorizedException;
import com.bezkoder.spring.security.postgresql.exception.UserNotFoundException;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;

/**
 * 
 * @author Siya
 *
 */

@ControllerAdvice
public class MyControllerAdvice extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			errors.put(fieldName, message);
		});
		return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<String> handleBusinessException(BusinessException businessException) {
		return new ResponseEntity<String>("Please enter a valid Id", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserNotFoundException.class)
	protected ResponseEntity<String> handleUserNotFound(UserNotFoundException userNotFound) {
		return new ResponseEntity<String>("User Not Found", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<MessageResponse> handleMaxSizeException(MaxUploadSizeExceededException exc) {
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse("File too large!"));
	}

	@ExceptionHandler(NoCommentsListException.class)
	protected ResponseEntity<String> handleGender(NoCommentsListException noCommentsListException) {
		return new ResponseEntity<String>("This user doesn't posted any comments yet", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(FileNotFoundException.class)
	protected ResponseEntity<String> handleGender(FileNotFoundException fileNotFoundException) {
		return new ResponseEntity<String>("File is empty,Please enter file to upload", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidUserIdException.class)
	protected ResponseEntity<String> handleGender(InvalidUserIdException invalidUserIdException) {
		return new ResponseEntity<String>("This userId doesn't exist,please enter valid inputs", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UnAuthorizedException.class)
	protected ResponseEntity<String> handleGender(UnAuthorizedException unAuthorizedException) {
		return new ResponseEntity<String>("Unauthorized User,Token Expired", HttpStatus.NOT_FOUND);
	}

}
