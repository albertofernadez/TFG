package com.registro.usuarios.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.registro.usuarios.modelo.Usuario;
import com.registro.usuarios.repositorio.UsuarioRepositorio;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Override
	public List<Usuario> findAll() {
		return usuarioRepositorio.findAll();
	}

	@Override
	public Optional<Usuario> findById(Long id) {
		return usuarioRepositorio.findById(id);
	}

	@Override
	public Usuario save(Usuario usuario) {
		return usuarioRepositorio.save(usuario);
	}

	@Override
	public Usuario findByEmail(String email) {
		return usuarioRepositorio.findByEmail(email);
	}
}
