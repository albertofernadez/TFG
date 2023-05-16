package com.registro.usuarios.servicio;

import java.util.List;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.registro.usuarios.dto.UsuarioRegistroDTO;
import com.registro.usuarios.modelo.Usuario;

public interface UsuarioServicio extends UserDetailsService{

	public Usuario guardar(UsuarioRegistroDTO registroDTO) throws ConstraintViolationException;
	
	public List<Usuario> listarUsuarios();
	
	Optional<Usuario> findById(Long id);
		
}
