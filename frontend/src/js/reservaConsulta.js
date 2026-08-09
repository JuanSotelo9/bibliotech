document.addEventListener("DOMContentLoaded", () => {
    fetchReservas();
});

function fetchReservas() {
    api.get(BASE_URL + '/usuario/reservas')
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
        return `<button onclick="entregar(${reserva.idreserva})">Entregar</button>`;
    } else if (reserva.estado === "Entregado") {
        return "Entregado";
    }
    return "";
}

function entregar(idreserva) {
    let data = {
        "idreserva": idreserva
    }
    api.post(BASE_URL + "/documento/entregar", data)
    .then(() => {
        alert("Reserva entregada correctamente.");
        location.reload();
    })
    .catch(error => console.error("Error:", error));
    
}

