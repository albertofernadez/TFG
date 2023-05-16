package com.registro.usuarios.exception;

public class UserNotEnabledException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8531279297330726706L;

	public UserNotEnabledException() {
		super("Usuario no habilitado");
	}

	public UserNotEnabledException(String message) {
		super("Usuario no habilitado, pongase en contacto con el Administrador");
	}

}
