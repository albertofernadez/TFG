package com.registro.usuarios.interfaceService;

//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.registro.usuarios.modelo.Valoracion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import javax.transaction.Transactional;

public interface IvaloracionService extends JpaRepository<Valoracion, Integer> {
    
    @Modifying
    @Transactional
    @Query(value="INSERT INTO `valoracion` (`id_valoracion`, `producto_id_producto`, `cliente_id_cliente`, `comentario`, `puntuacion`) VALUES (NULL, :producto, :usuario, :comentario, :puntuacion)", nativeQuery=true)
    int guardarComentarios(@Param("producto") int producto, @Param("usuario") int usuario, @Param("comentario") String comentario, @Param("puntuacion") int puntuacion);
    
    @Modifying
    @Transactional
    @Query(value="UPDATE `valoracion` SET `comentario` = :comentario, `puntuacion` = :puntuacion WHERE `valoracion`.`id_valoracion` = :id_valoracion", nativeQuery=true)
    int actualizarComentarios(@Param("id_valoracion") int id_valoracion, @Param("comentario") String comentario, @Param("puntuacion") int puntuacion);
    
    @Query(value="SELECT * FROM valoracion WHERE cliente_id_cliente = :usuario AND producto_id_producto = :producto", nativeQuery=true)
	List<Valoracion> comprobar(@Param("usuario") int usuario, @Param("producto") int producto);
    
    @Query(value="SELECT id_valoracion FROM valoracion WHERE cliente_id_cliente = :usuario AND producto_id_producto = :producto", nativeQuery=true)
	int cogerValoracionId(@Param("usuario") int usuario, @Param("producto") int producto);
    
    @Query(value="SELECT AVG(puntuacion) FROM `valoracion` WHERE producto_id_producto = :producto", nativeQuery=true)
    Float mediaValoraciones(@Param("producto") int producto);

}
