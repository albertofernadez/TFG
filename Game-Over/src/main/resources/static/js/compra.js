document.querySelector('#formcompra').addEventListener('submit', function(event) {
  event.preventDefault(); // Detener el evento submit predeterminado

  swal({
    title: "¿Seguro que desea hacer esta compra?",
    icon: "warning",
    buttons: ["Cancelar", true],
    dangerMode: true,
  })
  .then((OK) => {
    if (OK) {
      this.submit(); // Enviar el formulario
    } else {
      // El usuario canceló la acción, no se hace nada
    }
  });
});
