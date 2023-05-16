package com.registro.usuarios.modelo;


public class Estadisticas {
	
	private int idProducto;
	private String foto;
	private String nombre;
	private int cantidad;
	private float total;
	
	
	public Estadisticas() {
		super();
	}
	
	public Estadisticas(int idProducto, String foto, String nombre, int cantidad, float total) {
		super();
		this.idProducto = idProducto;
		this.foto = foto;
		this.nombre = nombre;
		this.cantidad = cantidad;
		this.total = total;
	}

	public int getIdProducto() {
		return idProducto;
	}
	
	public void setIdProducto(int producto_id_producto) {
		this.idProducto = producto_id_producto;
	}
	
	public String getFoto() {
		return foto;
	}
	
	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	public float getTotal() {
		float totaldefinitivo = (float) (Math.round(total * 100.0) / 100.0);
		return totaldefinitivo;
	}
	
	public void setTotal(float total) {
		this.total = total;
	}

	
	@Override
	public String toString() {
		return "Estadisticas [producto_id_producto=" + idProducto + ", foto=" + foto + ", nombre=" + nombre
				+ ", cantidad=" + cantidad + ", total=" + total + "]";
	}
	
	

}
