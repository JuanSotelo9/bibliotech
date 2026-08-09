document.addEventListener("DOMContentLoaded", function () {
    localStorage.removeItem("Documento");
    localStorage.removeItem("usuario");
    localStorage.removeItem("titulo");
    usuarioService.obtenerDatos()
    .then(data => {
        dom.insertarDato("nombre", data.nombre);
        dom.insertarDato("correo", data.correoElectronico);
        dom.insertarDato("direccion", data.direccionFisica);
        dom.insertarDato("telefono", data.numeroTelefonico);
        
        return documentoService.obtenerPorUsuario();
    })
    .then(data => {
        let documentos = data;
        const tbody = document.querySelector(".tabla-container tbody");
    
        tbody.innerHTML = "";
        
        documentos.forEach(doc => {
            const tr = document.createElement("tr");
            
            const tdTitulo = document.createElement("td");
            const enlace = document.createElement("a");
            enlace.href = "#";
            enlace.textContent = doc.titulo.trim() === "" ? "sin_titulo" : doc.titulo;
            enlace.onclick = () => verDocumento(doc.id);
            tdTitulo.appendChild(enlace);
            tr.appendChild(tdTitulo);
            
            const tdAccion = document.createElement("td");
            
            if (doc.estado === "Disponible") {
                const boton = document.createElement("button");
                boton.textContent = "Eliminar";
                boton.onclick = () => eliminarDocumento(doc.id);
                tdAccion.appendChild(boton);
            } else if (doc.estado === "Eliminado") {
                const boton = document.createElement("button");
                boton.textContent = "Habilitar";
                boton.onclick = () => habilitarDocumento(doc.id);
                tdAccion.appendChild(boton);
            } else if (doc.estado === "Reservado") {
                tdAccion.textContent = "Reservado";
            }
            
            tr.appendChild(tdAccion);
            tbody.appendChild(tr);
        });
    })
    .catch(error => {
        console.error('Error:', error);
    });
});

function eliminarDocumento(id) {
    documentoService.eliminar(id)
        .then(data => {
            if (data.mensaje === "Actualizado") {
                alert("Eliminado Correctamente");
                location.reload();
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function habilitarDocumento(id) {
    documentoService.habilitar(id)
        .then(data => {
            if (data.mensaje === "Actualizado") {
                alert("Documento Activado Correctamente");
                location.reload();
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function verDocumento(id) {
    documentoService.buscarPorId(id)
        .then(data => {
            localStorage.setItem("Documento", JSON.stringify(data))
            location.replace("descripcionDocumento.html");
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function habilitarBoton() {
    const input = document.getElementById("buscarDocumento");
    const boton = document.getElementById("botonBuscar");
    boton.disabled = input.value.trim() === "";
}

function guardarBusqueda(event) {
    const input = document.getElementById("buscarDocumento").value.trim();

    if (input) {
        localStorage.setItem("titulo", input);
        window.location.href = "documentos.html";
    }
}

function borrarToken(event) {
    event.preventDefault();
    auth.clearToken();
    window.location.href = "login.html";
}