const loading = {
    _contador: 0,

    mostrar() {
        loading._contador++;
        if (loading._contador === 1) {
            const overlay = document.createElement("div");
            overlay.className = "cargando-overlay";
            overlay.id = "cargando-overlay";
            const spinner = document.createElement("div");
            spinner.className = "cargando-spinner";
            overlay.appendChild(spinner);
            document.body.appendChild(overlay);
        }
    },

    ocultar() {
        loading._contador = Math.max(0, loading._contador - 1);
        if (loading._contador === 0) {
            const overlay = document.getElementById("cargando-overlay");
            if (overlay) overlay.remove();
        }
    }
};
