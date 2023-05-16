const select = document.querySelector('#select');
const opciones = document.querySelector('#opciones');
const contenidoSelect = document.querySelector('#select .contenido-select');
const hiddenInput = document.querySelector("#inputSelect");

select.addEventListener('click', () => {
	select.classList.toggle('active');
	opciones.classList.toggle('active');
});