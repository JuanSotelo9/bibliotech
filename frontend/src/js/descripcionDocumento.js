const documentoStr = localStorage.getItem("Documento");
document.addEventListener("DOMContentLoaded", () => {
    if (!auth.validarSesion()) return;
    cargarDocumento();
    cargarEventos()
});

function cargarEventos(){
    const documento = JSON.parse(documentoStr);
    documentoService.obtenerEventos(documento.iddocumento)
        .then(data => {
            let listaEventos = data;
            const listaHistorial = document.getElementById("listaHistorial");

            listaEventos.forEach(evento => {
                const li = document.createElement("li");
                li.textContent = evento;
                listaHistorial.appendChild(li);
            });
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function cargarDocumento() {
    if (!documentoStr) {
        notificacion.info("No hay documento cargado.");
        return;
    }

    const documento = JSON.parse(documentoStr);
    const formulario = document.getElementById("formulario");
    let contenido = `
        <div class='campo'><label>Título:</label><input type='text' value='${documento.titulo}' readonly></div>
        <div class='campo'><label>Fecha de Publicación:</label><input type='date' value='${documento.fechaPublicacion}' readonly></div>
        <div class='campo'><label>Autores:</label><input type='text' value='${documento.autores}' readonly></div>
        <div class='campo'><label>Editorial:</label><input type='text' value='${documento.editorial}' readonly></div>
    `;

    if (documento.tipo === "libro") {
        contenido += `
            <div class='campo'><label>ISBN:</label><input type='text' value='${documento.isbn ?? ""}' readonly></div>
            <div class='campo'><label>Número de Páginas:</label><input type='number' value='${documento.numPaginas ?? ""}' readonly></div>
        `;
    } else if (documento.tipo === "articulo") {
        contenido += `<div class='campo'><label>SSN:</label><input type='text' value='${documento.ssn ?? ""}' readonly></div>`;
    } else if (documento.tipo === "ponencia") {
        contenido += `
            <div class='campo'><label>Congreso:</label><input type='text' value='${documento.nombreCongreso ?? ""}' readonly></div>
            <div class='campo'><label>ISBN:</label><input type='text' value='${documento.isbn ?? ""}' readonly></div>
        `;
    }

    contenido += `
        <div class='campo'>
            <label></label>
            <a href="#" onclick="verPropietario('${documento.propietario}')">Haz clic aquí para ver los datos del propietario</a>
        </div>
    `;

    contenido += `<button class="botonSalir" onclick="window.location.href='paginaPrincipal.html'">Salir</button>`;

    if (documento.estado === "Disponible") {
        contenido += `
            <button class="botonModificacionDocumento" onclick="window.location.href='formularioDoc.html'">Ir a Modificar</button>
            <button class="botonReservaDocumento" onclick="reservarDocumento()">Reservar</button>
        `;
    } else if (documento.estado === "Reservado") {
        contenido += `<button class="botonModificacionDocumento" onclick="window.location.href='formularioDoc.html'">Ir a Modificar</button>`;
    }

    formulario.innerHTML = contenido;
}

function verPropietario(propietario) {
    if (!propietario) {
        notificacion.info("No hay datos del propietario.");
        return;
    }

    localStorage.setItem("usuario", propietario);
    window.location.href = "informacionConsulta.html";
}


async function reservarDocumento() {
    try {
        const documento = JSON.parse(localStorage.getItem("Documento")); 

        const reservarData = await documentoService.reservar(documento.iddocumento);

        if (reservarData.mensaje === "Actualizado") {
            notificacion.exito("Documento Reservado");
        }

        const obtenerData = await documentoService.buscarPorId(documento.iddocumento);
        localStorage.setItem("Documento", JSON.stringify(obtenerData));
        location.replace("descripcionDocumento.html");

    } catch (error) {
        console.error('Error:', error);
    }
}
