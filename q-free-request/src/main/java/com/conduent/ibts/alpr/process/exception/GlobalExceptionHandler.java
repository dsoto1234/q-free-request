package com.conduent.ibts.alpr.process.exception;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

private static final Logger exceptionLog = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(value = { ParseException.class })
	public ResponseEntity<Object> handleParseException(ParseException e) {

		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

		exceptionLog.error("Exception Occurred while processing {}", e.getMessage());
		return new ResponseEntity<>(e.getMessage(), httpStatus);
	}
	

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> handleException(Exception e) {

		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

		exceptionLog.error("Exception Occurred while processing {}", e.getMessage());
		return new ResponseEntity<>(e.getMessage(), httpStatus);
	}
}
