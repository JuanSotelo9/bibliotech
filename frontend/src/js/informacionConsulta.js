document.addEventListener("DOMContentLoaded", function() {
    let data={
        "nombre":localStorage.getItem("usuario")
    }
    fetch('/api/usuario/consultar', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem("token")}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (response.status === 401) {
            alert("Sesión expirada. Por favor, inicia sesión de nuevo.");
            localStorage.removeItem("token");
            window.location.href = "login.html";
            throw new Error("Usuario no autorizado (401)");
        }
        if (!response.ok) {
            throw new Error(`Error HTTP: ${response.status}`);
        }
        return response.json();
    })
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