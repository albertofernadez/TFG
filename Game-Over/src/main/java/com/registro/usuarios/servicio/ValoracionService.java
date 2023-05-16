package com.registro.usuarios.servicio;

import com.registro.usuarios.modelo.Producto;
import com.registro.usuarios.modelo.Valoracion;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ValoracionService {
	
    List<Valoracion> getAllValoraciones();
    Valoracion get(int id);

    Page<Valoracion> findPaginatedByProductId(int id_producto, int pageNo, int pageSize, String sortField, String sortDirection);
    
	Page<Valoracion> findPaginatedByProductIdAndPuntuacion(int id_producto, int puntuacion, int pageNo, int pageSize,
			String sortField, String sortDirection);
}
