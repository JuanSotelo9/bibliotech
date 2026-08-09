document.addEventListener("DOMContentLoaded", function() {
    usuarioService.consultarUsuario(localStorage.getItem("usuario"))
    .then(data => {
        dom.insertarDato("nombre", data.nombre || "No disponible");
        dom.insertarDato("correo", data.correoElectronico || "No disponible");
        dom.insertarDato("direccion", data.direccionFisica || "No disponible");
        dom.insertarDato("telefono", data.numeroTelefonico || "No disponible");
    })
    .catch(error => {
        console.error('Error:', error);
    });
});