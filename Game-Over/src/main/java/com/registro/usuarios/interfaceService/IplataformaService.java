package com.registro.usuarios.interfaceService;

import org.springframework.data.jpa.repository.JpaRepository;

import com.registro.usuarios.modelo.Categoria;
import com.registro.usuarios.modelo.Plataforma;

public interface IplataformaService extends JpaRepository<Plataforma, Integer> {

}
