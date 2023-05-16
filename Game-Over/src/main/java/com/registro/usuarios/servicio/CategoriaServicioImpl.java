package com.registro.usuarios.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.registro.usuarios.interfaceService.IcategoriaService;
import com.registro.usuarios.modelo.Categoria;

@Service
public class CategoriaServicioImpl implements CategoriaServicio {
	
	@Autowired
	private IcategoriaService categoriaRepositorio;

	@Override
	public List<Categoria> listaCategoria() {
		return categoriaRepositorio.findAll();
	}

}
