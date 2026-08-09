document.addEventListener("DOMContentLoaded", function() {
    let data={
        "nombre":localStorage.getItem("usuario")
    }
    api.post(BASE_URL + '/usuario/consultar', data)
    .then(data => {
        insertarDato("nombre", data.nombre || "No disponible");
        insertarDato("correo", data.correoElectronico || "No disponible");
        insertarDato("direccion", data.direccionFisica || "No disponible");
        insertarDato("telefono", data.numeroTelefonico || "No disponible");
    })
    .catch(error => {
        console.error('Error:', error);
    });
});

function insertarDato(id, valor) {
    document.getElementById(id).querySelector("span").textContent = valor;
}