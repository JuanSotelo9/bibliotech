document.addEventListener("DOMContentLoaded", function () {
    localStorage.removeItem("Documento");
    localStorage.removeItem("usuario");
    localStorage.removeItem("titulo");
    fetch('/api/usuario/datos', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem("token")}`,
            'Content-Type': 'application/json'
        }
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
        insertarDato("nombre", data.nombre);
        insertarDato("correo", data.correoElectronico);
        insertarDato("direccion", data.direccionFisica);
        insertarDato("telefono", data.numeroTelefonico);
        
        return fetch('/api/usuario/documentos', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("token")}`,
                'Content-Type': 'application/json'
            }
        });
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error al obtener los documentos del usuario');
        }
        return response.json();
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
        let nuevoP = document.createElement("p");
        nuevoP.innerText = valor;
        elemento.parentNode.insertBefore(nuevoP, elemento.nextSibling);
    }
}

function eliminarDocumento(id) {

    let data = {
        "iddocumento": id
    }
    fetch('/api/documento/eliminar', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem("token")}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    }).then(response =>{
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
    }).then(data => {
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
    fetch('/api/documento/habilitar', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem("token")}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    }).then(response =>{
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
    }).then(data => {
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
    fetch('/api/documento', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem("token")}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    }).then(response =>{
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
    }).then(data => {
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