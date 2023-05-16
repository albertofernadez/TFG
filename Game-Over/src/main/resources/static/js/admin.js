function eliminarProducto(id_producto) {
	swal({
	  title: "¿Seguro de eliminar este producto?",
	  icon: "warning",
	  buttons: ["Cancelar", true],
	  dangerMode: true,
	})
	.then((OK) => {
	  if (OK) {
	  	$.ajax({
	  		url:"/adminProductosEliminar/"+id_producto,
	  	});
	    swal("El producto se ha borrado con éxito.", {
	      icon: "success",
	    }).then((ok)=>{
	    	if(ok){
	    		location.href="/adminProductos";
	    	}
	    });
	  } else {
	  }
	});
}

function desactivarUsuario(id) {
	swal({
	  title: "¿Seguro de desactivar este usuario?",
	  icon: "warning",
	  buttons: ["Cancelar", true],
	  dangerMode: true,
	})
	.then((OK) => {
	  if (OK) {
	  	$.ajax({
	  		url:"/adminUsuariosDesactivar/"+id,
	  	});
	    swal("El usuario se ha desactivado con éxito.", {
	      icon: "success",
	    }).then((ok)=>{
	    	if(ok){
	    		location.href="/adminUsuarios";
	    	}
	    });
	  } else {
	  }
	});
}

function activarUsuario(id) {
	swal({
	  title: "¿Seguro de activar este usuario?",
	  icon: "warning",
	  buttons: ["Cancelar", true],
	  dangerMode: true,
	})
	.then((OK) => {
	  if (OK) {
	  	$.ajax({
	  		url:"/adminUsuariosActivar/"+id,
	  	});
	    swal("El usuario se ha activado con éxito.", {
	      icon: "success",
	    }).then((ok)=>{
	    	if(ok){
	    		location.href="/adminUsuarios";
	    	}
	    });
	  } else {
	  }
	});
}