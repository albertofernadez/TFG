package com.registro.usuarios.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "valoracion")
public class Valoracion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_valoracion;
	
	@ManyToOne
	@JoinColumn(name = "producto_id_producto")
	private Producto producto;
	
	@ManyToOne
	@JoinColumn(name = "cliente_id_cliente")
	private Usuario usuario;
	
	private String comentario;
	private int puntuacion;
	
	
	public Valoracion() {
		super();
	}

	public Valoracion(int id_valoracion, Producto producto, Usuario usuario, String comentario, int puntuacion) {
		super();
		this.id_valoracion = id_valoracion;
		this.producto = producto;
		this.usuario = usuario;
		this.comentario = comentario;
		this.puntuacion = puntuacion;
	}

	public int getId_valoracion() {
		return id_valoracion;
	}

	public void setId_valoracion(int id_valoracion) {
		this.id_valoracion = id_valoracion;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public int getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}

}
