package com.registro.usuarios.exception;

import javax.naming.AuthenticationException;

public class UsernameNotFoundException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1290044397189097158L;
		
			public UsernameNotFoundException() {			
			}	
		
			public UsernameNotFoundException(String explanation) {
				super("Email o contraseña erroneos");
	}

}
