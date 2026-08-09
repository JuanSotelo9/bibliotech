document.addEventListener("DOMContentLoaded", function () {
    localStorage.removeItem("Documento");
    localStorage.removeItem("usuario");
    localStorage.removeItem("titulo");
    api.get(BASE_URL + '/usuario/datos')
    .then(data => {
        insertarDato("nombre", data.nombre);
        insertarDato("correo", data.correoElectronico);
        insertarDato("direccion", data.direccionFisica);
        insertarDato("telefono", data.numeroTelefonico);
        
        return api.get(BASE_URL + '/usuario/documentos');
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

function insertarDato(id, valor) {
    let elemento = document.getElementById(id);
    if (elemento) {
        document.querySelectorAll(`p[data-dato="${id}"]`).forEach(p => p.remove());
        let nuevoP = document.createElement("p");
        nuevoP.innerText = valor;
        nuevoP.dataset.dato = id;
        elemento.parentNode.insertBefore(nuevoP, elemento.nextSibling);
    }
}

function eliminarDocumento(id) {

    let data = {
        "iddocumento": id
    }
    api.post(BASE_URL + '/documento/eliminar', data)
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
    let data = {
        "iddocumento": id
    }
    api.post(BASE_URL + '/documento/habilitar', data)
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
    let data = {
        "iddocumento": id
    }
    api.post(BASE_URL + '/documento', data)
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

function borrarToken(){
    localStorage.removeItem("token")
}