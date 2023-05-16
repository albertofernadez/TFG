package com.registro.usuarios.interfaceService;

//import com.registro.usuarios.modelo.Producto;
import com.registro.usuarios.modelo.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import javax.transaction.Transactional;

public interface IusuarioService extends JpaRepository<Usuario, Long> {

    @Query(value="SELECT * FROM Cliente WHERE email = :emailLogueado", nativeQuery=true)
    List<Usuario> mostrarPerfiles(@Param("emailLogueado") String emailLogueado);
    
    @Query(value="SELECT id_cliente FROM cliente WHERE email = :emailLogueado", nativeQuery=true)
    int cogerPerfilId(@Param("emailLogueado") String emailLogueado);
    
    @Query(value="SELECT * FROM Cliente", nativeQuery=true)
    List<Usuario> listar();
    
    @Query(value="SELECT * FROM Cliente c WHERE c.enabled = 1", nativeQuery=true)
    List<Usuario> mostrarActivado();
    
	@Query(value="SELECT * FROM Cliente c WHERE c.enabled = 0", nativeQuery=true)
	List<Usuario> mostrarDesactivado();
	
	@Query(value="SELECT * FROM Cliente c WHERE c.id_cliente = id", nativeQuery=true)
	List<Usuario> nostrarId();
	
	@Query(value="SELECT * FROM Cliente c WHERE c.nombre LIKE %:busqueda% OR c.apellido LIKE %:busqueda% OR c.dni LIKE %:busqueda% OR c.email LIKE %:busqueda%", nativeQuery=true)
	List<Usuario> buscador(@Param("busqueda") String busqueda);

//    @Query(value="UPDATE `cliente` SET `nombre` = :nombre, `apellido` = :apellido, `direccion` = :direccion, `dni` = :dni WHERE `cliente`.`id_cliente` = :id", nativeQuery=true)
//    int actualizarPerfil(@Param("id") long id, @Param("nombre") String nombre, @Param("apellido") String apellido, @Param("direccion") String direccion, @Param("dni") String dni);
    @Modifying
    @Transactional    
    @Query(value="UPDATE `cliente` SET `nombre` = :nombre, `apellido` = :apellido, `direccion` = :direccion, `dni` = :dni, `enabled` = :enabled  WHERE `cliente`.`id_cliente` = :id", nativeQuery=true)
    int actualizarPerfil(@Param("id") long id, @Param("nombre") String nombre, @Param("apellido") String apellido, @Param("direccion") String direccion, @Param("dni") String dni, @Param("enabled") boolean enabled);

    @Modifying
    @Transactional    
    @Query(value="UPDATE `cliente` SET `direccion` = :direccion, `dni` = :dni  WHERE `cliente`.`id_cliente` = :id", nativeQuery=true)
    int actualizarDniDir(@Param("id") long id, @Param("direccion") String direccion, @Param("dni") String dni);

    @Modifying
    @Transactional    
    @Query(value="UPDATE `cliente` SET `dni` = :dni  WHERE `cliente`.`id_cliente` = :id", nativeQuery=true)
    int actualizarDni(@Param("id") long id, @Param("dni") String dni);

    @Modifying
    @Transactional    
    @Query(value="UPDATE `cliente` SET `direccion` = :direccion  WHERE `cliente`.`id_cliente` = :id", nativeQuery=true)
    int actualizarDir(@Param("id") long id, @Param("direccion") String direccion);

} 
