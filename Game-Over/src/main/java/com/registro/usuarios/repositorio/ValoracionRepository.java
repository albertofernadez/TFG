package com.registro.usuarios.repositorio;

import com.registro.usuarios.modelo.Valoracion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValoracionRepository extends JpaRepository<Valoracion, Integer> {

    Page<Valoracion> findAll(Specification<Valoracion> especificacion, Pageable pageable);

}


