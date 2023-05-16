package com.registro.usuarios.servicio;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import com.registro.usuarios.dto.UsuarioRegistroDTO;
import com.registro.usuarios.exception.UserNotEnabledException;
import com.registro.usuarios.modelo.Rol;
import com.registro.usuarios.modelo.Usuario;
import com.registro.usuarios.repositorio.UsuarioRepositorio;

@Service
public class UsuarioServicioImpl implements UsuarioServicio {

	
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public UsuarioServicioImpl(UsuarioRepositorio usuarioRepositorio) {
		super();
		this.usuarioRepositorio = usuarioRepositorio;
	}

	@Override
	public Usuario guardar(UsuarioRegistroDTO registroDTO) throws ConstraintViolationException {
		
		Usuario usuario = new Usuario(			
							registroDTO.getNombre(),
							registroDTO.getFoto(),
							registroDTO.getApellido(),
							registroDTO.getEmail(),
							registroDTO.isEnabled(),
							passwordEncoder.encode(registroDTO.getPassword()),Arrays.asList(new Rol("USER")));
		
		return usuarioRepositorio.save(usuario);
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, UserNotEnabledException {
		Usuario usuario = usuarioRepositorio.findByEmail(username);
		if(usuario == null) {
			throw new UsernameNotFoundException("Email o contraseña erroneos");		
		}else if (!(usuario).isEnabled()) {
			throw new UserNotEnabledException("Usuario deshabilitado. Pongase en contacto con el administrador.");			
		}
		return new User(usuario.getEmail(),usuario.getPassword(), mapearAutoridadesRoles(usuario.getRoles()));
	}
	
	
	private Collection<? extends GrantedAuthority> mapearAutoridadesRoles(Collection<Rol> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getNombre())).collect(Collectors.toList());
	}
	
	@Override
	public List<Usuario> listarUsuarios() {
		return usuarioRepositorio.findAll();
	}

	@Override
	public Optional<Usuario> findById(Long id) {
		return usuarioRepositorio.findById(id);
	}		
}
