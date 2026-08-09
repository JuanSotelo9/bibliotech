document.addEventListener("DOMContentLoaded", () => {
    if (!auth.validarSesion()) return;
    fetchReservas();
});

function fetchReservas() {
    usuarioService.obtenerReservas()
        .then(reservas => agregarReservas(reservas))
        .catch(error => console.error("Error:", error));
}

function agregarReservas(reservas) {
    const tabla = document.getElementById("tabla-reservas");
    tabla.innerHTML = "";

    reservas.forEach(reserva => {
        let fila = document.createElement("tr");

        fila.innerHTML = `
            <td>${reserva.titulo || ""}</td>
            <td>${reserva.tipo || ""}</td>
            <td>${reserva.fechareserva || ""}</td>
            <td>${reserva.fechaentrega === "null" ? "" : reserva.fechaentrega}</td>
            <td>${generarAccion(reserva)}</td>
        `;

        tabla.appendChild(fila);
    });
}

function generarAccion(reserva) {
    if (reserva.estado === "Reservado") {
        return `<button onclick="entregar(event, ${reserva.idreserva})">Entregar</button>`;
    } else if (reserva.estado === "Entregado") {
        return "Entregado";
    }
    return "";
}

function entregar(event, idreserva) {
    const boton = event.currentTarget;
    dom.cargando(boton, documentoService.entregar(idreserva))
    .then(() => {
        notificacion.exito("Reserva entregada correctamente.");
        setTimeout(() => location.reload(), 1200);
    })
    .catch(error => console.error("Error:", error));
    
}

