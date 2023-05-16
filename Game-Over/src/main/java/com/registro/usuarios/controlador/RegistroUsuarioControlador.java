package com.registro.usuarios.controlador;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.registro.usuarios.dto.UsuarioRegistroDTO;
import com.registro.usuarios.servicio.UsuarioServicio;

@Controller
@RequestMapping("/signup")
@SessionAttributes({"direccionMail"})
public class RegistroUsuarioControlador {
	
	private static final Logger log = Logger.getLogger(RegistroUsuarioControlador.class);

	private UsuarioServicio usuarioServicio;

	public RegistroUsuarioControlador(UsuarioServicio usuarioServicio) {
		super();
		this.usuarioServicio = usuarioServicio;
	}
	
	@ModelAttribute("usuario")
	public UsuarioRegistroDTO retornarNuevoUsuarioRegistroDTO() {
		return new UsuarioRegistroDTO();
	}

	@GetMapping
	public String mostrarFormularioDeRegistro() {
		return "signup";
	}
	
	@PostMapping
	public String registrarCuentaDeUsuario(@ModelAttribute("usuario") UsuarioRegistroDTO registroDTO) {
		try{
			usuarioServicio.guardar(registroDTO);
			
		}catch(Exception e) {
			log.error("Usuario duplicado.");
			return "redirect:/signup?fallo";		
		}
		log.info("Usuario se ha registrado.");
		return "redirect:/signup?exito";  
	}


}
