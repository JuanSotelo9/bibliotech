const dom = {
    insertarDato(id, valor) {
        const elemento = document.getElementById(id);
        if (!elemento) return;

        const span = elemento.querySelector("span");
        if (span) {
            span.textContent = valor;
            return;
        }

        document.querySelectorAll(`p[data-dato="${id}"]`).forEach(p => p.remove());
        const nuevoP = document.createElement("p");
        nuevoP.innerText = valor;
        nuevoP.dataset.dato = id;
        elemento.parentNode.insertBefore(nuevoP, elemento.nextSibling);
    }
};
