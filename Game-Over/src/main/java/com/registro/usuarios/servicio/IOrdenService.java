package com.registro.usuarios.servicio;

import java.util.List;
import java.util.Optional;

import com.registro.usuarios.modelo.Orden;
import com.registro.usuarios.modelo.Usuario;

public interface IOrdenService {
	List<Orden> findAll();
	Optional<Orden> findById(Integer id);
	Orden save (Orden orden);
	String generarNumeroOrden();
	List<Orden> findByUsuario (Usuario usuario);
}
