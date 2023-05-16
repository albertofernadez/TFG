package com.registro.usuarios.modelo;

public class productosOrdenados {
	
	private int idProducto;
	private String nombre;
	private String foto;
	private String descripcion;
	private float precio;
	private int stock;
	private int categoria_id_categoria;
	private int plataforma_id_plataforma;
	private int descatalogado;
	private int cantidad_vendida;
	
	
	public int getIdProducto() {
		return idProducto;
	}
	public void setIdProducto(int idProducto) {
		this.idProducto = idProducto;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getCategoria_id_categoria() {
		return categoria_id_categoria;
	}
	public void setCategoria_id_categoria(int categoria_id_categoria) {
		this.categoria_id_categoria = categoria_id_categoria;
	}
	public int getPlataforma_id_plataforma() {
		return plataforma_id_plataforma;
	}
	public void setPlataforma_id_plataforma(int plataforma_id_plataforma) {
		this.plataforma_id_plataforma = plataforma_id_plataforma;
	}
	public int getDescatalogado() {
		return descatalogado;
	}
	public void setDescatalogado(int descatalogado) {
		this.descatalogado = descatalogado;
	}
	public int getCantidad_vendida() {
		return cantidad_vendida;
	}
	public void setCantidad_vendida(int cantidad_vendida) {
		this.cantidad_vendida = cantidad_vendida;
	}
	
	@Override
	public String toString() {
		return "productosOrdenados [idProducto=" + idProducto + ", nombre=" + nombre + ", foto=" + foto + 
				", descripcion=" + descripcion + ", precio=" + precio + ", stock=" + stock + 
				", categoria_id_categoria=" + categoria_id_categoria + ", plataforma_id_plataforma=" + 
				plataforma_id_plataforma + ", descatalogado=" + descatalogado + ", cantidad_vendida=" + cantidad_vendida + "]";
	}

}
