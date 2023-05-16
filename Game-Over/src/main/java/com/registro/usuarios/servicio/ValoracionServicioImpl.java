package com.registro.usuarios.servicio;

import com.registro.usuarios.modelo.Valoracion;
import com.registro.usuarios.repositorio.ValoracionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

@Service
public class ValoracionServicioImpl implements ValoracionService {

    @Autowired
    private ValoracionRepository valoracionRepository;

    @Override
    public List<Valoracion> getAllValoraciones() { return valoracionRepository.findAll(); }

    @Override
    public Valoracion get(int id_valoracion) {

        Optional<Valoracion> optional = valoracionRepository.findById(id_valoracion);
        Valoracion valoracion = null;
        if (optional.isPresent()) {
            valoracion = optional.get();
        } else {
            throw new RuntimeException(" Valoracion not found for id :: " + id_valoracion);
        }
        return valoracion;

    }

    @Override
    public Page<Valoracion> findPaginatedByProductId(int id_producto, int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        // Creamos un objeto Specification para buscar las valoraciones por id_producto
        Specification<Valoracion> especificacion = Specification.where((valoracion, cq, cb) -> {
            return cb.equal(valoracion.get("producto").get("id_producto"), id_producto);
        });

        // Realizamos la consulta a la base de datos utilizando el repositorio valoracionRepository y la especificación creada
        return valoracionRepository.findAll(especificacion, pageable);
    }
    
    @Override
    public Page<Valoracion> findPaginatedByProductIdAndPuntuacion(int id_producto, int puntuacion, int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        // Creamos un objeto Specification para buscar las valoraciones por id_producto y puntuación
        Specification<Valoracion> especificacion = Specification.where((valoracion, cq, cb) -> {
            Predicate predicadoIdProducto = cb.equal(valoracion.get("producto").get("id_producto"), id_producto);
            Predicate predicadoPuntuacion = cb.equal(valoracion.get("puntuacion"), puntuacion);
            return cb.and(predicadoIdProducto, predicadoPuntuacion);
        });

        // Realizamos la consulta a la base de datos utilizando el repositorio valoracionRepository y la especificación creada
        return valoracionRepository.findAll(especificacion, pageable);
    }


}
