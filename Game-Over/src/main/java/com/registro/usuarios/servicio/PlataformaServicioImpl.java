package com.registro.usuarios.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.registro.usuarios.interfaceService.IplataformaService;
import com.registro.usuarios.modelo.Plataforma;



@Service
public class PlataformaServicioImpl implements PlataformaServicio {
	
	@Autowired
	private IplataformaService plataformaRepositorio;

	@Override
	public List<Plataforma> listaPlataforma() {
		return plataformaRepositorio.findAll();
	}


}
