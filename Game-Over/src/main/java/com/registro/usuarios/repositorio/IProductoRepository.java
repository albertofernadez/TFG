package com.registro.usuarios.repositorio;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.registro.usuarios.modelo.Producto;
import com.registro.usuarios.modelo.Valoracion;



@Repository
public interface IProductoRepository extends JpaRepository<Producto, Integer> {

	Page<Producto> findAll(Specification<Producto> especificacion, Pageable pageable);

}
