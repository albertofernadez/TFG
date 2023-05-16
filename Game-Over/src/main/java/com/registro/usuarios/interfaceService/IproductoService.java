package com.registro.usuarios.interfaceService;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.registro.usuarios.modelo.Producto;

public interface IproductoService extends JpaRepository<Producto, Integer> {
	
	@Query(value="SELECT * FROM Producto p WHERE p.descatalogado = 0", nativeQuery=true)
	List<Producto> listar();

	@Query(value="SELECT * FROM Producto p WHERE p.descatalogado = 0",
			nativeQuery=true)
	Page<Producto> listarr(Pageable pageable);
	
	@Query(value="SELECT * FROM Producto p WHERE p.nombre LIKE %:busqueda% AND p.descatalogado = 0", nativeQuery=true)
	List<Producto> buscador(@Param("busqueda") String busqueda);
	
	@Query(value="SELECT * FROM Producto p WHERE p.categoria_id_categoria = 1 AND p.descatalogado = 0", nativeQuery=true)
	List<Producto> mostrarVideojuegos();

	@Query(value="SELECT * FROM Producto p WHERE p.categoria_id_categoria = 1 AND p.descatalogado = 0",
			nativeQuery=true)
	Page<Producto> mostrarVideojuegoss(Pageable pageable);
	
	@Query(value="SELECT * FROM Producto p WHERE p.categoria_id_categoria = 2 AND p.descatalogado = 0", nativeQuery=true)
	List<Producto> mostrarAccesorios();

	@Query(value="SELECT * FROM Producto p WHERE p.categoria_id_categoria = 2 AND p.descatalogado = 0",
			nativeQuery=true)
	Page<Producto> mostrarAccesorioss(Pageable pageable);
	
	@Query(value="SELECT * FROM Producto p WHERE p.categoria_id_categoria = 3 AND p.descatalogado = 0", nativeQuery=true)
	List<Producto> mostrarElectronica();

	@Query(value="SELECT * FROM Producto p WHERE p.categoria_id_categoria = 3 AND p.descatalogado = 0",
			nativeQuery=true)
	Page<Producto> mostrarElectronicaa(Pageable pageable);
	
	@Query(value="SELECT * FROM Producto p WHERE p.plataforma_id_plataforma = 1 AND p.descatalogado = 0", nativeQuery=true)
	List<Producto> mostrarVarias();

	@Query(value="SELECT * FROM Producto p WHERE p.plataforma_id_plataforma = 1 AND p.descatalogado = 0",
			nativeQuery=true)
	Page<Producto> mostrarVariass(Pageable pageable);
	
	@Query(value="SELECT * FROM Producto p WHERE p.plataforma_id_plataforma = 2 AND p.descatalogado = 0", nativeQuery=true)
	List<Producto> mostrarPlayStation();

	@Query(value="SELECT * FROM Producto p WHERE p.plataforma_id_plataforma = 2 AND p.descatalogado = 0",
			nativeQuery=true)
	Page<Producto> mostrarPlayStationn(Pageable pageable);

	@Query(value="SELECT * FROM Producto p WHERE p.plataforma_id_plataforma = 3 AND p.descatalogado = 0",
			nativeQuery=true)
	Page<Producto> mostrarSwitchh(Pageable pageable);
	
	@Query(value="SELECT * FROM Producto p WHERE p.plataforma_id_plataforma = 3 AND p.descatalogado = 0", nativeQuery=true)
	List<Producto> mostrarSwitch();
	
	@Query(value="SELECT * FROM Producto p WHERE p.plataforma_id_plataforma = 4 AND p.descatalogado = 0", nativeQuery=true)
	List<Producto> mostrarxBox();

	@Query(value="SELECT * FROM Producto p WHERE p.plataforma_id_plataforma = 4 AND p.descatalogado = 0",
			nativeQuery=true)
	Page<Producto> mostrarXboxx(Pageable pageable);
	
	@Query(value="SELECT * FROM Producto p WHERE p.plataforma_id_plataforma = 5 AND p.descatalogado = 0", nativeQuery=true)
	List<Producto> mostrarPC();

	@Query(value="SELECT * FROM Producto p WHERE p.plataforma_id_plataforma = 5 AND p.descatalogado = 0",
			nativeQuery=true)
	Page<Producto> mostrarPCC(Pageable pageable);
	
	@Query(value="SELECT * FROM Producto p WHERE p.descatalogado = 1", nativeQuery=true)
	List<Producto> mostrarDescatalogado();

	@Query(value="SELECT * FROM Producto p WHERE p.descatalogado = 1",
			nativeQuery=true)
	Page<Producto> mostrarDescatalogadoo(Pageable pageable);
	
	@Query(value="SELECT nombre FROM Categoria c WHERE c.id_categoria LIKE %:id_categoria%", nativeQuery=true)
	String  getNombreCategoria(@Param("id_categoria") Integer id_categoria);
	
	@Query(value="SELECT nombre FROM Plataforma p WHERE p.id_plataforma LIKE %:id_plataforma%", nativeQuery=true)
	String  getNombrePlataforma(@Param("id_plataforma") Integer id_plataforma);
}
