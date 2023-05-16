package com.registro.usuarios.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

public class GlobalExceptionHandler {

	@ExceptionHandler(UserNotEnabledException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public ErrorResponse handleNoRecordFoundException(UserNotEnabledException ex) {

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage("Usuario no habilitado contacte con el administrador.");
		return errorResponse;
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorResponse handleNoRecordFoundException(UsernameNotFoundException ex) {

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage("Email o contraseña erroneo.");
		return errorResponse;
	}

}
