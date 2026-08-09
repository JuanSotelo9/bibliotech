const notificacion = {
    error(mensaje) { mostrar(mensaje, 'error'); },
    exito(mensaje) { mostrar(mensaje, 'exito'); },
    info(mensaje) { mostrar(mensaje, 'info'); }
};

function crearEstilosNotificacion() {
    const style = document.createElement("style");
    style.id = "notificacion-estilos";
    style.textContent = `
        #notificacion-contenedor {
            position: fixed;
            top: 16px;
            right: 16px;
            z-index: 9999;
            display: flex;
            flex-direction: column;
            gap: 8px;
            max-width: 340px;
        }
        .notificacion-toast {
            padding: 12px 16px;
            border-radius: 8px;
            color: #fff;
            font-family: Arial, sans-serif;
            font-size: 14px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
            transition: opacity 0.3s ease, transform 0.3s ease;
        }
        .notificacion-toast.error { background: #dc3545; }
        .notificacion-toast.exito { background: #28a745; }
        .notificacion-toast.info { background: #17a2b8; }
        .notificacion-toast-salir { opacity: 0; transform: translateX(20px); }
    `;
    document.head.appendChild(style);
}

function mostrar(mensaje, tipo) {
    if (!document.getElementById("notificacion-estilos")) {
        crearEstilosNotificacion();
    }

    let contenedor = document.getElementById("notificacion-contenedor");
    if (!contenedor) {
        contenedor = document.createElement("div");
        contenedor.id = "notificacion-contenedor";
        document.body.appendChild(contenedor);
    }

    const toast = document.createElement("div");
    toast.className = `notificacion-toast ${tipo}`;
    toast.textContent = mensaje;
    contenedor.appendChild(toast);

    setTimeout(() => {
        toast.classList.add("notificacion-toast-salir");
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}
