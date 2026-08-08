document.addEventListener("DOMContentLoaded", () => {
    fetchReservas();
});

function fetchReservas() {
    fetch('/api/usuario/reservas', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem("token")}`,
            'Content-Type': 'application/json'
        }
    }).then(response => {
            if (response.status === 401) {
                alert("Sesión expirada. Por favor, inicia sesión de nuevo.");
                localStorage.removeItem("token");
                window.location.href = "login.html";
                throw new Error("Usuario no autorizado (401)");
            }
            if (!response.ok) {
                throw new Error("Error al obtener reservas");
            }
            return response.json();
    }).then(reservas => agregarReservas(reservas))
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
    fetch("/api/documento/entregar", {
        method: "POST",
        headers: {
            'Authorization': `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json",
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
            throw new Error("Error al obtener reservas");
        }
        return response.json();
    })
    .then(() => {
        alert("Reserva entregada correctamente.");
        location.reload();
    })
    .catch(error => console.error("Error:", error));
    
}

