package com.registro.usuarios.interfaceService;

import org.springframework.data.jpa.repository.JpaRepository;

import com.registro.usuarios.modelo.Categoria;

public interface IcategoriaService extends JpaRepository<Categoria, Integer> {

}
