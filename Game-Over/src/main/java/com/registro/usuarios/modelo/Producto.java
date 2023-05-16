package com.registro.usuarios.modelo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "producto", uniqueConstraints = @UniqueConstraint(columnNames = "id_Producto"))
public class Producto implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_producto;
	private String nombre;
	private String foto;
	private String descripcion;
	private double precio;
	private int stock;
	private int descatalogado;

	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "categoria_id_categoria")
	private Categoria categoria;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "plataforma_id_plataforma")
	private Plataforma plataforma;

	@OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
	private Set<Valoracion> valoracion;

	
	public Producto() {
		super();
	}

	public Producto(int id_producto, String nombre, String foto, String descripcion, double precio, int stock,
				Set<Valoracion> valoracion, Categoria categoria, Plataforma plataforma, int descatalogado) {

		super();
		this.id_producto = id_producto;
		this.nombre = nombre;
		this.foto = foto;
		this.descripcion = descripcion;
		this.precio = precio;
		this.stock = stock;
		this.valoracion = valoracion;
		this.categoria = categoria;
		this.plataforma = plataforma;
		this.descatalogado = descatalogado;
	}

	public int getId_producto() {
		return id_producto;
	}

	public void setId_producto(int id_producto) {
		this.id_producto = id_producto;
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

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
	
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	
	public Plataforma getPlataforma() {
		return plataforma;
	}

	public void setPlataforma(Plataforma plataforma) {
		this.plataforma = plataforma;
	}

	public Set<Valoracion> getValoracion() {
		return valoracion;
	}

	public void setValoracion(Set<Valoracion> valoracion) {
		this.valoracion = valoracion;
	}

		
	public int getDescatalogado() {
		return descatalogado;
	}

	public void setDescatalogado(int descatalogado) {
		this.descatalogado = descatalogado;
	}



	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "Producto [id_producto=" + id_producto + ", nombre=" + nombre + ", foto=" + foto + ", descripcion="
				+ descripcion + ", precio=" + precio + ", stock=" + stock + ", categoria" + categoria + ", plataforma" + plataforma +
				"descatalogado="+ descatalogado + ", valoracion=" + valoracion + "]";
	}

}
