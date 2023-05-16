package com.registro.usuarios.dto;

public class UsuarioRegistroDTO {

	private Long id;
	private String foto;
	private String nombre;
	private String apellido;
	private String email;	
	private String password;
	private boolean enabled;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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


	public UsuarioRegistroDTO(String foto, String nombre, String apellido, String email, String password, boolean enabled) {
		super();
		this.nombre = nombre;
		this.foto = foto;
		this.apellido = apellido;
		this.email = email;
		this.password = password;
		this.enabled = enabled;	}

	public UsuarioRegistroDTO() {

	}

}
