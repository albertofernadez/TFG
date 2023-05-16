package com.registro.usuarios.modelo;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "cliente", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cliente")
	private Long id;

	private String foto;
	private String dni;
	private String nombre;


	@Column(name = "apellido")
	private String apellido;
	private String direccion;
	private String email;
	private String password;
	private boolean enabled;
	
	@OneToMany(mappedBy = "usuario")
	private Set<Valoracion> valoracion;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "usuarios_roles", joinColumns = @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente"), inverseJoinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "id"))
	private Collection<Rol> roles;

	@OneToMany(mappedBy = "usuario")
	private List<Orden> ordenes;

	
	
	public Usuario() {
		super();

	}


	public Usuario(String nombre, String foto, String apellido, String email, boolean enabled, String password,  Collection<Rol> roles) {
		super();
		this.nombre = nombre;
		this.foto = "../assets/img/icons/gta1.jpg";
		this.apellido = apellido;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.roles = roles;
	}


	public Usuario(Long id, String dni, String nombre, String foto, String apellido, String direccion, String email, boolean enabled, String password,
			Set<Valoracion> valoracion, Collection<Rol> roles) {
		
		super();
		this.id = id;
		this.dni = dni;
		this.nombre = nombre;
		this.foto = foto;
		this.apellido = apellido;
		this.direccion = direccion;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.valoracion = valoracion;
		this.roles = roles;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

	public Collection<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Rol> roles) {
		this.roles = roles;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Valoracion> getValoracion() {
		return valoracion;
	}

	public void setValoracion(Set<Valoracion> valoracion) {
		this.valoracion = valoracion;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", foto=" + foto + ", dni=" + dni + ", nombre=" + nombre + ", apellido=" + apellido + ", direccion="
				+ direccion + ", email=" + email + ", password=" + password + ", enabled=" + enabled + ", valoracion="
				+ valoracion + ", roles=" + roles + ", ordenes=" + ordenes + "]";
	}

}
