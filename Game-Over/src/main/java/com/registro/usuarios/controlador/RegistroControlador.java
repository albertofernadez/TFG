package com.registro.usuarios.controlador;

import com.registro.usuarios.interfaceService.IusuarioService;
import com.registro.usuarios.interfaceService.IvaloracionService;
import com.registro.usuarios.servicio.*;

import java.util.*;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.registro.usuarios.interfaceService.IproductoService;
import com.registro.usuarios.modelo.DetalleOrden;
import com.registro.usuarios.modelo.Estadisticas;
import com.registro.usuarios.modelo.Orden;
import com.registro.usuarios.modelo.Valoracion;
import com.registro.usuarios.modelo.productosOrdenados;
import com.registro.usuarios.modelo.Producto;
import com.registro.usuarios.modelo.Usuario;
import com.registro.usuarios.servicio.ProductoService;

import java.sql.*;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;



@Controller
@RequestMapping("/")
public class RegistroControlador {

	private static final Logger log = Logger.getLogger(RegistroControlador.class);

	//private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private IproductoService service;

	@Autowired
	private IusuarioService servicio;

	@Autowired
	private IvaloracionService comentarioServicio;

	@Autowired
	private ValoracionService valoracionService ;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private IOrdenService ordenService;

	@Autowired
	private IDetalleOrdenService detalleOrdenService;

	@Autowired
	private IUsuarioService usuarioService;



	// Para Almacenar detalles de la orden
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();

	// Almacena datos de la orden
	Orden orden = new Orden();

	

	@GetMapping("/adminProductos")
	public String adminProducto(Model model, String busqueda, @RequestParam(defaultValue = "0") int page, Authentication authentication) {

		List<Producto> productos;
		if (busqueda != null) {
			productos = service.buscador(busqueda);
		} else {
			productos = service.listar();
		}

		Page<Producto> productosPage = service.listarr(PageRequest.of(page, 6));
		List<Producto> productost = productosPage.getContent();

		model.addAttribute("productos", productost);
		model.addAttribute("nombrePlat", service.getNombrePlataforma(2));
		model.addAttribute("productosPage", productosPage);

		List<Producto> totalProductPage = service.listar();
		long total = totalProductPage.size();
		model.addAttribute("totalProductos", total);

		return "adminProductos";
	}
	
	@GetMapping("/")
	public String Index() {
		return "index";
	}

	@GetMapping("/hkjkj")
	public String Ordenar (Model model, String busqueda, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int pageSize, Authentication authentication)  {		
		List<productosOrdenados> pOrdenados = new ArrayList<>();

		// Verifica si se proporciona una cadena de búsqueda
		if (busqueda != null && !busqueda.isEmpty()) {
			// Llama a la función buscador() para buscar productos
			pOrdenados = buscador(busqueda);
		} else {
			// Llama a la función listar() para obtener todos los productos
			pOrdenados = listar();
		}
		
		int start = page * pageSize;
		int end = Math.min((start + pageSize), pOrdenados.size());
		long totalProductos = pOrdenados.size();
		List<productosOrdenados> productosPaginados = pOrdenados.subList(start, end);

		model.addAttribute("pOrdenados", productosPaginados);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", (int) Math.ceil((double) pOrdenados.size() / pageSize));
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("busqueda", busqueda);
		
		
		model.addAttribute("totalProductos", totalProductos);
		
	    if (authentication != null && authentication.isAuthenticated()) {
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        String emailLogueado = userDetails.getUsername();
	        Usuario usuario = usuarioService.findByEmail(emailLogueado);
	        model.addAttribute("avatar", usuario.getFoto());
	    }

      if (busqueda != null) {
		    return "busqueda";
	    }else {
		    return "index";
	    }


	}
	
	public List<productosOrdenados> listar() {
		List<productosOrdenados> productos = new ArrayList<>();
		try {
			Connection connection = ConexionJDBC.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT p.*, COALESCE(SUM(d.cantidad), 0) AS cantidad_vendida FROM producto p LEFT JOIN detalles d ON p.id_producto = d.producto_id_producto GROUP BY p.id_producto ORDER BY cantidad_vendida DESC;");
			while (resultSet.next()) {
				productosOrdenados producto = new productosOrdenados();
				producto.setIdProducto(resultSet.getInt("id_producto"));
				producto.setNombre(resultSet.getString("nombre"));
				producto.setFoto(resultSet.getString("foto"));
				producto.setDescripcion(resultSet.getString("descripcion"));
				producto.setPrecio(resultSet.getFloat("precio"));
				producto.setStock(resultSet.getInt("stock"));
				producto.setDescatalogado(resultSet.getInt("descatalogado"));
				producto.setCantidad_vendida(resultSet.getInt("cantidad_vendida"));
				productos.add(producto);
			}
			connection.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return productos;
	}


	public List<productosOrdenados> buscador(String busqueda) {
		List<productosOrdenados> productos = new ArrayList<>();
		try {
			Connection connection = ConexionJDBC.getConnection();
			PreparedStatement statement = connection.prepareStatement("SELECT p.*, COALESCE(SUM(d.cantidad), 0) AS cantidad_vendida FROM producto p LEFT JOIN detalles d ON p.id_producto = d.producto_id_producto WHERE p.nombre LIKE ? GROUP BY p.id_producto ORDER BY cantidad_vendida DESC;");
			statement.setString(1, "%" + busqueda + "%");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				productosOrdenados producto = new productosOrdenados();
				producto.setIdProducto(resultSet.getInt("id_producto"));
				producto.setNombre(resultSet.getString("nombre"));
				producto.setFoto(resultSet.getString("foto"));
				producto.setDescripcion(resultSet.getString("descripcion"));
				producto.setPrecio(resultSet.getFloat("precio"));
				producto.setStock(resultSet.getInt("stock"));
				producto.setDescatalogado(resultSet.getInt("descatalogado"));
				producto.setCantidad_vendida(resultSet.getInt("cantidad_vendida"));
				productos.add(producto);
			}
			connection.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return productos;
	}

	@GetMapping("/perfil")
	public String entrarPerfil(Model model, String perfile) {		
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		UserDetails userDetail = (UserDetails) auth.getPrincipal();

		String emailLogueado = userDetail.getUsername();

		model.addAttribute("clientes", servicio.mostrarPerfiles(emailLogueado));
		
		Usuario usuario = usuarioService.findByEmail(emailLogueado);
		
		List<Orden> ordenes = ordenService.findByUsuario(usuario);
		
		model.addAttribute("ordenes", ordenes);
		
		model.addAttribute("avatar", usuario.getFoto());
		
		return "perfil";
		
	}
	
	
	@GetMapping("/guardarFoto")
	public String guardarFoto(String nuevaFoto, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		UserDetails userDetail = (UserDetails) auth.getPrincipal();

		String emailLogueado = userDetail.getUsername();

		model.addAttribute("clientes", servicio.mostrarPerfiles(emailLogueado));
		
		Usuario usuario = usuarioService.findByEmail(emailLogueado);
		
		usuario.setFoto(nuevaFoto);
		
		usuarioService.save(usuario);
		
		model.addAttribute("avatar", usuario.getFoto());
		
		return "perfil";		
	}


	@GetMapping("/detalles/{id_producto}")
	public String view(@PathVariable("id_producto") int id_producto, Model model, Model model1, Model model2,
					   Model model3, Model model4, Model model5, Model model6, HttpServletRequest request, Authentication authentication) {

		return buscarProductosPorId(id_producto, 1, "puntuacion", "desc", model, model1, model2, model3, model4, model5, model6, request, authentication);

	}
	@GetMapping("/detalles/{id_producto}/page/{pageNo}") // /{estrellas}
	public String buscarProductosPorId(@PathVariable("id_producto") int id_producto, @PathVariable (value = "pageNo") int pageNo,
									   @RequestParam("sortField") String sortField,
									   @RequestParam("sortDir") String sortDir, Model model,  Model model1, Model model2,
									   Model model3, Model model4, Model model5, Model model6, HttpServletRequest request, Authentication authentication) {

		//-1. Productos
		Producto p = service.getById(id_producto);
		model.addAttribute("producto", p);

		//-2. Paginacion Valoraciones
		int pageSize = 3;
		
		Page<Valoracion> page;
		
		List<Valoracion> listValoraciones;
		
		String estrellas = request.getParameter("estrellas");
		
		if (estrellas == null) {
			page = valoracionService.findPaginatedByProductId(id_producto, pageNo, pageSize, sortField, sortDir);
		} else {
			int numEstrellas = Integer.parseInt(estrellas);
			page = valoracionService.findPaginatedByProductIdAndPuntuacion(id_producto, numEstrellas, pageNo, pageSize, sortField, sortDir);
			model6.addAttribute("estrellas", estrellas);
		}
		
		listValoraciones = page.getContent();
		
		model2.addAttribute("currentPage", pageNo);
		model2.addAttribute("totalPages", page.getTotalPages());
		model2.addAttribute("totalItems", page.getTotalElements());

		model2.addAttribute("sortField", sortField);
		model2.addAttribute("sortDir", sortDir);
		model2.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model2.addAttribute("valoraciones", listValoraciones);
		
		//-3. Encontrar perfil
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		UserDetails userDetail = (UserDetails) auth.getPrincipal();

		String emailLogueado = userDetail.getUsername();

		model4.addAttribute("usuarios", servicio.mostrarPerfiles(emailLogueado));

		int id_cliente = servicio.cogerPerfilId(emailLogueado);

		//-4. Añadir Valoración
		if (comentarioServicio.comprobar(id_cliente, id_producto).isEmpty()) {
			Valoracion valoracion = new Valoracion();
			model3.addAttribute("valoracion", valoracion);
		} else {
			int id_valoracion = comentarioServicio.cogerValoracionId(id_cliente, id_producto);
			
			Optional<Valoracion> valoracion = comentarioServicio.findById(id_valoracion);
			model3.addAttribute("valoracion", valoracion);
		}
		
		//-5. Nota media
		
		Float media = comentarioServicio.mediaValoraciones(id_producto);
		String mediaRedondeo;
		
		if (media != null) {
		    if (Math.floor(media) == media) {
		        int mediaInt = (int) media.floatValue();
		        mediaRedondeo = Integer.toString(mediaInt);
		    } else {
		        mediaRedondeo = String.format("%.1f", media);
		    }
		    model5.addAttribute("media", mediaRedondeo);
		} else {
		    model5.addAttribute("media", 0);
		}
		
		MDC.put("id_cliente", Integer.toString(id_cliente));
		MDC.put("usuario", emailLogueado);
		log.info("Visitado el producto " + p.getNombre() + " [" + id_producto + "]");
		MDC.remove("id_cliente");
		MDC.remove("usuario");
		
	    if (authentication != null && authentication.isAuthenticated()) {
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        Usuario usuario = usuarioService.findByEmail(emailLogueado);
	        model.addAttribute("avatar", usuario.getFoto());
	    }
		

		return "detalles";
	}

	@PostMapping("/comentario")
	public String enviarComment(int producto, int usuario, @Validated String comentario, int puntuacion, HttpServletRequest request) {
		if (comentarioServicio.comprobar(usuario, producto).isEmpty()) {
			comentarioServicio.guardarComentarios(producto, usuario, comentario, puntuacion);
		} else {
			int id_valoracion = comentarioServicio.cogerValoracionId(usuario, producto);
			
			comentarioServicio.actualizarComentarios(id_valoracion, comentario, puntuacion);
		}
			
		String referer = request.getHeader("Referer");
		return "redirect:" + referer; 
	}	
	
	@GetMapping("/login")
	public String iniciarSesion(Model model, String error) {			
			//int codeError = 0 ;
//			if (error != null) {
//				 model.addAttribute("errorUser", "Usuario no habilitado Prueba");
//	        } else {
//	        	model.addAttribute("errorTest2", "Usuario no habilitado Prueba2");
//	        }
		return "login";
	}

	@GetMapping("/modificarPerfil/{id}")
	public String modificarPerfil(@PathVariable Long id, Model model) {
		Optional<Usuario> usuario = servicio.findById(id);
		model.addAttribute("usuario", usuario);
		return "modificarPerfil";		
	}

	@PostMapping("/saves")
	public String save(String nombre, String apellido, String direccion, String dni, Long id, boolean enabled) {
		servicio.actualizarPerfil(id, nombre, apellido, direccion, dni, enabled);
		return "redirect:/perfil";
	}

	@GetMapping("/palas")
	public String listarVideojuegos(Model model, @RequestParam(defaultValue = "0") int page, Authentication authentication) {

		Boolean plataforma = false;

		Page<Producto> productosPage = service.mostrarVideojuegoss(PageRequest.of(page, 6));
		List<Producto> productos = productosPage.getContent();

		model.addAttribute("productos", productos);
		model.addAttribute("nombreCat", service.getNombreCategoria(1));
		model.addAttribute("plataforma", plataforma);
		model.addAttribute("productosPage", productosPage);

		List<Producto> totalProductPage = service.mostrarVideojuegos();
		long total = totalProductPage.size();
		model.addAttribute("totalProductos", total);

		if (authentication != null && authentication.isAuthenticated()) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String emailLogueado = userDetails.getUsername();
			Usuario usuario = usuarioService.findByEmail(emailLogueado);
			model.addAttribute("avatar", usuario.getFoto());
		}

		return "categoria";

	}

	@GetMapping("/paleteros")
	public String listarAccesorios(Model model, @RequestParam(defaultValue = "0") int page, Authentication authentication) {

		Boolean plataforma = false;

		Page<Producto> productosPage = service.mostrarAccesorioss(PageRequest.of(page, 6));
		List<Producto> productos = productosPage.getContent();

		model.addAttribute("productos", productos);
		model.addAttribute("nombreCat", service.getNombreCategoria(2));
		model.addAttribute("plataforma", plataforma);
		model.addAttribute("productosPage", productosPage);

		List<Producto> totalProductPage = service.mostrarAccesorios();
		long total = totalProductPage.size();
		model.addAttribute("totalProductos", total);

		if (authentication != null && authentication.isAuthenticated()) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String emailLogueado = userDetails.getUsername();
			Usuario usuario = usuarioService.findByEmail(emailLogueado);
			model.addAttribute("avatar", usuario.getFoto());
		}

		return "categoria";
	}

	

	@GetMapping("/adidas")
	public String listarPlaystation(Model model, @RequestParam(defaultValue = "0") int page, Authentication authentication) {

		Boolean plataforma = true;

		Page<Producto> productosPage = service.mostrarPlayStationn(PageRequest.of(page, 6));
		List<Producto> productos = productosPage.getContent();

		model.addAttribute("productos", productos);
		model.addAttribute("nombrePlat", service.getNombrePlataforma(1));
		model.addAttribute("plataforma", plataforma);
		model.addAttribute("productosPage", productosPage);

		List<Producto> totalProductPage = service.mostrarPlayStation();
		long total = totalProductPage.size();
		model.addAttribute("totalProductos", total);
		
	    if (authentication != null && authentication.isAuthenticated()) {
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        String emailLogueado = userDetails.getUsername();
	        Usuario usuario = usuarioService.findByEmail(emailLogueado);
	        model.addAttribute("avatar", usuario.getFoto());
	    }

		return "categoria";

	}

	@GetMapping("/bullpadel")
	public String listarSwitch(Model model, @RequestParam(defaultValue = "0") int page, Authentication authentication) {

		Boolean plataforma = true;

		Page<Producto> productosPage = service.mostrarSwitchh(PageRequest.of(page, 6));
		List<Producto> productos = productosPage.getContent();

		model.addAttribute("productos", productos);
		model.addAttribute("nombrePlat", service.getNombrePlataforma(2));
		model.addAttribute("plataforma", plataforma);
		model.addAttribute("productosPage", productosPage);

		List<Producto> totalProductPage = service.mostrarSwitch();
		long total = totalProductPage.size();
		model.addAttribute("totalProductos", total);
		
	    if (authentication != null && authentication.isAuthenticated()) {
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        String emailLogueado = userDetails.getUsername();
	        Usuario usuario = usuarioService.findByEmail(emailLogueado);
	        model.addAttribute("avatar", usuario.getFoto());
	    }

		return "categoria";
	}
	
	@GetMapping("/babolat")
	public String listarXbox(Model model, @RequestParam(defaultValue = "0") int page, Authentication authentication) {

		Boolean plataforma = true;
		Page<Producto> productosPage = service.mostrarXboxx(PageRequest.of(page, 6));
		List<Producto> productos = productosPage.getContent();

		model.addAttribute("productos", productos);
		model.addAttribute("nombrePlat", service.getNombrePlataforma(3));
		model.addAttribute("plataforma", plataforma);
		model.addAttribute("productosPage", productosPage);

		List<Producto> totalProductPage = service.mostrarxBox();
		long total = totalProductPage.size();
		model.addAttribute("totalProductos", total);

		if (authentication != null && authentication.isAuthenticated()) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String emailLogueado = userDetails.getUsername();
			Usuario usuario = usuarioService.findByEmail(emailLogueado);
			model.addAttribute("avatar", usuario.getFoto());
		}

		return "categoria";
	}
	
	
	@GetMapping("/dropshot")
	public String listarPc(Model model, @RequestParam(defaultValue = "0") int page, Authentication authentication) {

		Boolean plataforma = true;
		Page<Producto> productosPage = service.mostrarPCC(PageRequest.of(page, 6));
		List<Producto> productos = productosPage.getContent();

		model.addAttribute("productos", productos);
		model.addAttribute("nombrePlat", service.getNombrePlataforma(4));
		model.addAttribute("plataforma", plataforma);
		model.addAttribute("productosPage", productosPage);

		List<Producto> totalProductPage = service.mostrarPC();
		long total = totalProductPage.size();
		model.addAttribute("totalProductos", total);

		if (authentication != null && authentication.isAuthenticated()) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String emailLogueado = userDetails.getUsername();
			Usuario usuario = usuarioService.findByEmail(emailLogueado);
			model.addAttribute("avatar", usuario.getFoto());
		}

		return "categoria";
	}
	
	@GetMapping("/noxt")
	public String listarNox(Model model, @RequestParam(defaultValue = "0") int page, Authentication authentication) {

		Boolean plataforma = true;
		Page<Producto> productosPage = service.mostrarPCC(PageRequest.of(page, 6));
		List<Producto> productos = productosPage.getContent();

		model.addAttribute("productos", productos);
		model.addAttribute("nombrePlat", service.getNombrePlataforma(5));
		model.addAttribute("plataforma", plataforma);
		model.addAttribute("productosPage", productosPage);

		List<Producto> totalProductPage = service.mostrarPC();
		long total = totalProductPage.size();
		model.addAttribute("totalProductos", total);

		if (authentication != null && authentication.isAuthenticated()) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String emailLogueado = userDetails.getUsername();
			Usuario usuario = usuarioService.findByEmail(emailLogueado);
			model.addAttribute("avatar", usuario.getFoto());
		}

		return "categoria";
	}

	@GetMapping("/adminPalas")
	public String adminVideojuegos(Model model, @RequestParam(defaultValue = "0") int page, Authentication authentication) {

		Boolean plataforma = true;
		Page<Producto> productosPage = service.mostrarVideojuegoss(PageRequest.of(page, 6));
		List<Producto> productos = productosPage.getContent();

		model.addAttribute("productos", productos);
		model.addAttribute("nombrePlat", service.getNombrePlataforma(2));
		model.addAttribute("plataforma", plataforma);
		model.addAttribute("productosPage", productosPage);

		List<Producto> totalProductPage = service.mostrarVideojuegos();
		long total = totalProductPage.size();
		model.addAttribute("totalProductos", total);
	    
		return "adminProductos";

	}
	
	@GetMapping("/adminPaleteros")
	public String adminAccesorios(Model model, @RequestParam(defaultValue = "0") int page, Authentication authentication) {

		Boolean plataforma = true;
		Page<Producto> productosPage = service.mostrarAccesorioss(PageRequest.of(page, 6));
		List<Producto> productos = productosPage.getContent();

		model.addAttribute("productos", productos);
		model.addAttribute("nombrePlat", service.getNombrePlataforma(2));
		model.addAttribute("plataforma", plataforma);
		model.addAttribute("productosPage", productosPage);

		List<Producto> totalProductPage = service.mostrarAccesorios();
		long total = totalProductPage.size();
		model.addAttribute("totalProductos", total);

		return "adminProductos";
	}
	
	
	
	

	@GetMapping("/adminAdidas")
	public String adminPlaystation(Model model,  @RequestParam(defaultValue = "0") int page, Authentication authentication) {

		Boolean plataforma = true;
		Page<Producto> productosPage = service.mostrarPlayStationn(PageRequest.of(page, 6));
		List<Producto> productos = productosPage.getContent();

		model.addAttribute("productos", productos);
		model.addAttribute("nombrePlat", service.getNombrePlataforma(2));
		model.addAttribute("plataforma", plataforma);
		model.addAttribute("productosPage", productosPage);

		List<Producto> totalProductPage = service.mostrarPlayStation();
		long total = totalProductPage.size();
		model.addAttribute("totalProductos", total);

		return "adminProductos";
	}
	
	@GetMapping("/adminBabolat")
	public String adminSwitch(Model model,  @RequestParam(defaultValue = "0") int page, Authentication authentication) {

		Boolean plataforma = true;
		Page<Producto> productosPage = service.mostrarSwitchh(PageRequest.of(page, 6));
		List<Producto> productos = productosPage.getContent();

		model.addAttribute("productos", productos);
		model.addAttribute("nombrePlat", service.getNombrePlataforma(2));
		model.addAttribute("plataforma", plataforma);
		model.addAttribute("productosPage", productosPage);

		List<Producto> totalProductPage = service.mostrarSwitch();
		long total = totalProductPage.size();
		model.addAttribute("totalProductos", total);

		return "adminProductos";
	}
	
	@GetMapping("/adminBullpadel")
	public String adminxBox(Model model,  @RequestParam(defaultValue = "0") int page, Authentication authentication) {

		Boolean plataforma = true;
		Page<Producto> productosPage = service.mostrarXboxx(PageRequest.of(page, 6));
		List<Producto> productos = productosPage.getContent();

		model.addAttribute("productos", productos);
		model.addAttribute("nombrePlat", service.getNombrePlataforma(2));
		model.addAttribute("plataforma", plataforma);
		model.addAttribute("productosPage", productosPage);

		List<Producto> totalProductPage = service.mostrarxBox();
		long total = totalProductPage.size();
		model.addAttribute("totalProductos", total);

		return "adminProductos";
	}
	
	@GetMapping("/adminDropshot")
	public String adminPC(Model model,  @RequestParam(defaultValue = "0") int page, Authentication authentication) {

		Boolean plataforma = true;
		Page<Producto> productosPage = service.mostrarPCC(PageRequest.of(page, 6));
		List<Producto> productos = productosPage.getContent();

		model.addAttribute("productos", productos);
		model.addAttribute("nombrePlat", service.getNombrePlataforma(2));
		model.addAttribute("plataforma", plataforma);
		model.addAttribute("productosPage", productosPage);

		List<Producto> totalProductPage = service.mostrarPC();
		long total = totalProductPage.size();
		model.addAttribute("totalProductos", total);

		return "adminProductos";

	}
	
	@GetMapping("/adminDescatalogado")
	public String adminDescatalogado(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize) {
		List<Producto> productos;
		productos = service.mostrarDescatalogado();

	    int start = page * pageSize;
	    int end = Math.min((start + pageSize), productos.size());
	    List<Producto> productosPaginados = productos.subList(start, end);

	    model.addAttribute("productos", productosPaginados);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", (int) Math.ceil((double) productos.size() / pageSize));
	    model.addAttribute("pageSize", pageSize);

	    long totalProductos = productos.size();
	    model.addAttribute("totalProductos", totalProductos);

		return "adminProductos";
	}
	
	@GetMapping("/modificarProducto/{id_producto}")
	public String modificarProducto(@PathVariable("id_producto") int id_producto, Model model, @RequestParam(defaultValue = "0") int page) {
		Producto p = service.getById(id_producto);
		model.addAttribute("producto", p);

		return "modificarProducto";
	}

	@PostMapping("/actualizar")
	public String actualizarProducto(Producto producto, String foto) {
		
		
		if(foto == "") {
			Producto p = new Producto();
			p = service.getById(producto.getId_producto());
			producto.setFoto(p.getFoto());
			service.save(producto);
		}else{
			String ruta = "../assets/img/productos/";
			producto.setFoto(ruta+foto);
			service.save(producto);
		}
		
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		String emailLogueado = userDetail.getUsername();
		
		MDC.put("usuario", emailLogueado);
		MDC.put("id_cliente", "Administrador");
		log.info("Se ha actualizado el producto: " + producto.getNombre() + " [" + producto.getId_producto() + "]");
		MDC.remove("usuario");
		MDC.remove("id_cliente");
		
		return "redirect:/adminProductos#producto-" + producto.getId_producto();
	}

	@GetMapping("/adminProductosEliminar/{id_producto}")
	public String eliminarProducto(@PathVariable int id_producto) {
		Producto producto = service.getById(id_producto);
		producto.setDescatalogado(1);
		service.save(producto);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		String emailLogueado = userDetail.getUsername();
		
		MDC.put("usuario", emailLogueado);
		MDC.put("id_cliente", "Administrador");
		log.info("Se ha eliminado el producto: " + producto.getNombre() + " [" + producto.getId_producto() + "]");
		MDC.remove("usuario");
		MDC.remove("id_cliente");
		
		return "redirect:/adminProductos";
	}
	
	@GetMapping("/agregarProducto")
	public String agregarProducto(Model modelo) {
		Producto producto = new Producto();
		modelo.addAttribute("producto", producto);
		return "agregarProducto";
	}

	@PostMapping("/guardar")
	public String guardarProducto(Producto producto) {
		String ruta = "../assets/img/productos/";
		String foto = ruta + producto.getFoto();
		producto.setFoto(foto);
		service.save(producto);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		String emailLogueado = userDetail.getUsername();
		
		MDC.put("usuario", emailLogueado);
		MDC.put("id_cliente", "Administrador");
		log.info("Se ha creado el producto: " + producto.getNombre() + " [" + producto.getId_producto() + "]");
		MDC.remove("usuario");
		MDC.remove("id_cliente");
		
		return "redirect:/adminProductos";
	}
	
	@GetMapping("/adminUsuarios")
	public String adminUsuario(Model model, String busqueda, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize) {
		List<Usuario> usuarios;
		
		if (busqueda != null) {
			usuarios = servicio.buscador(busqueda);
		} else {
			usuarios = servicio.listar();
		}
		
		model.addAttribute("usuarios", usuarios);

	    return "adminUsuarios";
	}
	
	@GetMapping("/adminUsuariosDesactivar/{id}")
	public String desactivarUsuario(@PathVariable long id) {
		Usuario usuario = servicio.getById(id);
		usuario.setEnabled(false);
		servicio.save(usuario);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		String emailLogueado = userDetail.getUsername();
		
		MDC.put("usuario", emailLogueado);
		MDC.put("id_cliente", "Administrador");
		log.info("Se ha desactivado el usuario: " + usuario.getEmail() + " [" + id + "]");
		MDC.remove("usuario");
		MDC.remove("id_cliente");
		
		return "redirect:/adminUsuarios";
	}
	
	@GetMapping("/adminActivado")
	public String adminActivado(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize) {
		model.addAttribute("usuarios", servicio.mostrarActivado());

	    return "adminUsuarios";
	}
	
	@GetMapping("/adminDesactivado")
	public String adminDesactivado(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize) {
		model.addAttribute("usuarios", servicio.mostrarDesactivado());

	    return "adminUsuarios";
	}
	
	@GetMapping("/adminUsuariosActivar/{id}")
	public String ActivarUsuario(@PathVariable long id) {
		Usuario usuario = servicio.getById(id);
		usuario.setEnabled(true);
		servicio.save(usuario);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		String emailLogueado = userDetail.getUsername();
		
		MDC.put("usuario", emailLogueado);
		MDC.put("id_cliente", "Administrador");
		log.info("Se ha reactivado el usuario: " + usuario.getEmail() + " [" + id + "]");
		MDC.remove("usuario");
		MDC.remove("id_cliente");
		
		return "redirect:/adminDesactivado";
	}
	
	@GetMapping("/adminPedidos")
	public String adminPedidos(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize) {
		model.addAttribute("pedidos", ordenService.findAll());

	    return "adminPedidos";
	}
	
	@GetMapping("/adminEstadisticas")
	public String adminEstadisticas(Model model) {
	    List<Estadisticas> detalles = new ArrayList<>();
	    
	    try {
	        Connection connection = ConexionJDBC.getConnection();
	        Statement statement = connection.createStatement();
	        ResultSet resultSet = statement.executeQuery("SELECT producto_id_producto, nombre, SUM(cantidad), SUM(total), foto FROM detalles GROUP BY producto_id_producto, nombre, foto");
	        
	        while (resultSet.next()) {
	            Estadisticas detalle = new Estadisticas();
	            detalle.setIdProducto(resultSet.getInt("producto_id_producto"));
	            detalle.setNombre(resultSet.getString("nombre"));
	            detalle.setCantidad(resultSet.getInt(3));
	            detalle.setTotal(resultSet.getFloat(4));
	            detalle.setFoto(resultSet.getString("foto"));
	            detalles.add(detalle);
	        }
	        
	        connection.close();
	        
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	    
	    
	    model.addAttribute("detalles", detalles);
	    
	    return "adminEstadisticas";
	}


	

	// añadir al carrito con un solo articulo
	@PostMapping("/añadirCarrito")
	public String añadirCarro(@RequestParam Integer id, Model model) {
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		int cantidad = 1;
		double sumaTotal = 0;
		int sumaTotalProductos = 0;
		double idlistaproducto;

		Optional<Producto> optionalProducto = productoService.get(id);
		producto = optionalProducto.get();
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setTotal((producto.getPrecio()) * cantidad);
		detalleOrden.setPrecio(producto.getPrecio());

		detalleOrden.setProducto(producto);
		detalleOrden.setFoto(producto.getFoto());
		
		producto.setStock(producto.getStock() - cantidad);
	    productoService.save(producto);

		// validar que le producto no se añada 2 veces
		Integer idProducto = producto.getId_producto();
		boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId_producto() == idProducto);

		if (!ingresado) {
			detalles.add(detalleOrden);
		} else {
			detalles.stream().forEach(s -> {
				if (s.getProducto().getId_producto() == idProducto) {
					s.setCantidad(s.getCantidad() + 1);
					double preciosuma =s.getPrecio() * s.getCantidad();
					s.setTotal(Math.round(preciosuma * 100.0) / 100.0);
					s.getProducto().setStock(s.getProducto().getStock()-1);
				}
			});
		}
		

		sumaTotalProductos = detalles.stream().mapToInt(dt -> dt.getCantidad()).sum();
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		orden.setCantidad(sumaTotalProductos);
		orden.setTotal(Math.round(sumaTotal * 100.0) / 100.0);
		model.addAttribute("carrito", detalles);
		model.addAttribute("orden", orden);
		System.out.println(detalles);
		System.out.println(orden);
		return "carrito";
	}

	// quitar un producto del carrito
	@GetMapping("/delete/cart/{id}")
	public String deleteProductoCart(@PathVariable Integer id, Model model) {

		// lista nueva de productos
		List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();

		
	    
		for (DetalleOrden detalleOrden : detalles) {
			if (detalleOrden.getProducto().getId_producto() != id) {
				System.out.println(detalleOrden.getProducto().getId_producto());
				ordenesNueva.add(detalleOrden);
			}else {
				Producto producto = new Producto();
				Optional<Producto> optionalProducto = productoService.get(id);
				producto = optionalProducto.get();
				
				producto.setStock(producto.getStock() + detalleOrden.getCantidad());
			    productoService.save(producto);
			}
		}

		// poner la nueva lista con los productos restantes
		detalles = ordenesNueva;

		double sumaTotal = 0;
		int sumaTotalProductos = 0;
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		sumaTotalProductos = detalles.stream().mapToInt(dt -> dt.getCantidad()).sum();
		orden.setCantidad(sumaTotalProductos);
		orden.setTotal(Math.round(sumaTotal * 100.0) / 100.0);
		System.out.println(detalles);
		model.addAttribute("carrito", detalles);
		model.addAttribute("orden", orden);

		return "redirect:/carrito";
	}

	// quitar un producto del carrito
	@GetMapping("/sumar/cart/{id}")
	public String sumarProductoCart(@PathVariable Integer id, Model model, RedirectAttributes redirectAttrs) {
		double sumaTotal = 0;
		int sumaTotalProductos = 0;
		Producto producto = new Producto();
		Optional<Producto> optionalProducto = productoService.get(id);
		producto = optionalProducto.get();
		// validar que le producto no se añada 2 veces

		boolean existente = detalles.stream().anyMatch(p -> p.getProducto().getId_producto() == id);
		if(producto.getStock()!=0) {
			if (!existente) {

			} else {
				detalles.stream().forEach(s -> {
					if (s.getProducto().getId_producto() == id) {
						s.setCantidad(s.getCantidad() + 1);
						double preciosuma =s.getPrecio() * s.getCantidad();
						s.setTotal(Math.round(preciosuma * 100.0) / 100.0);
						s.getProducto().setStock(s.getProducto().getStock()-1);
					}
				});
			}
			
			
			producto.setStock(producto.getStock() - 1);
		    productoService.save(producto);
			sumaTotalProductos = detalles.stream().mapToInt(dt -> dt.getCantidad()).sum();
			sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
			orden.setCantidad(sumaTotalProductos);
			orden.setTotal(Math.round(sumaTotal * 100.0) / 100.0);
		}
		
		redirectAttrs
        .addFlashAttribute("mensaje", "No hay mas disponible")
        .addFlashAttribute("clase", "success");
		model.addAttribute("carrito", detalles);
		model.addAttribute("orden", orden);

		return "redirect:/carrito";
	}
	
	
	@GetMapping("/cerrarSesion")
	public String cerrarSesion(Model model) {
		double sumaTotal = 0;
		int sumaTotalProductos = 0;
		List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();
		
		System.out.println(detalles);
		for (DetalleOrden detalle : detalles) {
			Producto producto = detalle.getProducto();
			int id = producto.getId_producto();
			Optional<Producto> optionalProducto = productoService.get(id);
			producto = optionalProducto.get();
	        System.out.println(producto);
	        int cantidad = detalle.getCantidad();
	        System.out.println(cantidad);
	        producto.setStock(producto.getStock() + cantidad);
	        System.out.println(producto.getStock());
	        productoService.save(producto);

	    }
	    
		
					List<DetalleOrden> ordenesVacia = new ArrayList<DetalleOrden>();

					
					
					detalles = ordenesVacia;
		orden.setCantidad(sumaTotalProductos);
		orden.setTotal(Math.round(sumaTotal * 100.0) / 100.0);
		model.addAttribute("carrito", detalles);
		model.addAttribute("orden", orden);
		
		
		return "redirect:/logout";
	}
	

	// quitar un producto del carrito
	@GetMapping("/restar/cart/{id}")
	public String restarProductoCart(@PathVariable Integer id, Model model) {
		double sumaTotal = 0;
		int sumaTotalProductos = 0;

		// validar que le producto no se añada 2 veces

		boolean existente = detalles.stream().anyMatch(p -> p.getProducto().getId_producto() == id);

		if (!existente) {

		} else {
			detalles.stream().forEach(s -> {
				if (s.getProducto().getId_producto() == id) {
					s.setCantidad(s.getCantidad() - 1);
					double precioxcantidad = s.getPrecio() * s.getCantidad();
					s.setTotal(Math.round(precioxcantidad * 100.0) / 100.0);
					s.getProducto().setStock(s.getProducto().getStock()+1);
				}
				if (s.getCantidad() == 0) {

					// lista nueva de prodcutos
					List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();

					for (DetalleOrden detalleOrden : detalles) {
						if (detalleOrden.getProducto().getId_producto() != id) {
							System.out.println(detalleOrden.getProducto().getId_producto());
							ordenesNueva.add(detalleOrden);
						}
					}

					// poner la nueva lista con los productos restantes
					detalles = ordenesNueva;

				}
			});
		}
		Producto producto = new Producto();
		Optional<Producto> optionalProducto = productoService.get(id);
		producto = optionalProducto.get();
		
		producto.setStock(producto.getStock() + 1);
	    productoService.save(producto);
		sumaTotalProductos = detalles.stream().mapToInt(dt -> dt.getCantidad()).sum();
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		orden.setCantidad(sumaTotalProductos);
		orden.setTotal(Math.round(sumaTotal * 100.0) / 100.0);
		model.addAttribute("carrito", detalles);
		model.addAttribute("orden", orden);

		return "redirect:/carrito";
	}

	@GetMapping("/carrito")
	public String getCart(Model model, Authentication authentication) {

		model.addAttribute("carrito", detalles);
		model.addAttribute("orden", orden);
		
		if (authentication != null && authentication.isAuthenticated()) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String emailLogueado = userDetails.getUsername();
			Usuario usuario = usuarioService.findByEmail(emailLogueado);
			model.addAttribute("avatar", usuario.getFoto());
		}

		return "carrito";
	}
	
	@GetMapping("/orden")
	public String orden(Model model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		UserDetails userDetail = (UserDetails) auth.getPrincipal();

		String emailLogueado = userDetail.getUsername();
				
	
		List<Usuario>usuario = servicio.mostrarPerfiles(emailLogueado);
		model.addAttribute("usuario", usuario);
		
		model.addAttribute("carrito", detalles);
		model.addAttribute("orden", orden);
		
		return "orden";
	}
	
	// guardar la orden
	@PostMapping("/saveOrder")
	public String saveOrder(String direccion, String dni,HttpServletRequest request) {
		
		Date fechaCreacion = new Date();
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenService.generarNumeroOrden());
		
		
		
		//Obtener email del usuario logeado
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		String emailLogueado = userDetail.getUsername();
		
		
		//usuario
		//Usuario usuario =usuarioService.findById( Integer.parseInt(session.getAttribute("idusuario").toString())  ).get();
		List<Usuario> usuario = servicio.mostrarPerfiles(emailLogueado);
		Usuario usuarioEncontrado = usuario.get(0);
		orden.setUsuario(usuarioEncontrado);
		long idUsuario=usuarioEncontrado.getId();
		orden.setDireccion(direccion);
		if (usuarioEncontrado.getDireccion()==null || usuarioEncontrado.getDireccion().isEmpty()) {
			servicio.actualizarDir(idUsuario, direccion);
		}
		
		if (usuarioEncontrado.getDni()==null || usuarioEncontrado.getDni().isEmpty()) {
			servicio.actualizarDni(idUsuario, dni);
		}
		
		
		
		
		if (orden.getCantidad() == 0) {
			log.info("Carrito vacio");
			//return "/";
			String referer = request.getHeader("Referer");
			return "redirect:" + referer; 
		}else {
			ordenService.save(orden);				
			//guardar detalles
			for (DetalleOrden dt:detalles) {
				dt.setOrden(orden);
				detalleOrdenService.save(dt);								
			}

			String numero = orden.getNumero();
			int id_cliente = servicio.cogerPerfilId(emailLogueado);
			
			///limpiar lista y orden
			orden = new Orden();
			detalles.clear();	
			

			
			MDC.put("id_cliente", Integer.toString(id_cliente));
			MDC.put("usuario", emailLogueado);
			log.info("Realizado el pedido " + numero);
			MDC.remove("id_cliente");
			MDC.remove("usuario");
			
			return "redirect:/";
		}		
	}
		
	@GetMapping("/detalle/{id}")
	public String detalleCompra(@PathVariable Integer id, HttpSession session, Model model) {
		//logger.info("Id de la orden: {}", id);
		Optional<Orden> orden=ordenService.findById(id);
		String numero = orden.get().getNumero();
		Date pedido = orden.get().getFechaCreacion();
		Date entrega = orden.get().getFechaRecibida();
		String direccion = orden.get().getDireccion();
		Double total = orden.get().getTotal();
		
		model.addAttribute("numero", numero);
		model.addAttribute("pedido", pedido);
		model.addAttribute("entrega", entrega);
		model.addAttribute("direccion", direccion);
		model.addAttribute("total", total);

		model.addAttribute("detalles", orden.get().getDetalle());

		
		//session
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		return "detallecompra";
	}
	
	

}
