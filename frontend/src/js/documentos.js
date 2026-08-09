let libros = [];
let articulos = [];
let ponencias = [];
let indices = { libros: 0, articulos: 0, ponencias: 0 };
const tipos = {
    get libros() { return libros; },
    get articulos() { return articulos; },
    get ponencias() { return ponencias; }
};

async function fetchDocuments() {
    try {
        const documentos = await documentoService.buscarPorTitulo(localStorage.getItem("titulo"));
        console.log("Documentos recibidos:", documentos);

        libros = documentos.filter(doc => doc.tipo === "libro");
        articulos = documentos.filter(doc => doc.tipo === "articulo");
        ponencias = documentos.filter(doc => doc.tipo === "ponencia");

        displayDocument("libros");
        displayDocument("articulos");
        displayDocument("ponencias");

    } catch (error) {
        console.error('Error al obtener documentos:', error);
    }
}

function displayDocument(tipo) {
    const container = document.getElementById(tipo + "Fields");
    container.innerHTML = "";
    const data = tipos[tipo][indices[tipo]] || null;

    if (data) {
        for (let key in data) {
            if (!["idDocumento", "tipo", "mesPublicacion", "diaPublicacion"].includes(key)) {
                let value = data[key];
                container.innerHTML += `<div><strong>${key.toUpperCase()}:</strong> ${value ? value : ""}</div>`;
            }
        }

        container.innerHTML += `<button class="botonVerDocumento" onclick="ver(${data.idDocumento})">Ver</button>`;
    } else {
        container.innerHTML = "<div>No hay documentos</div>";
    }
}

function nextDocument(tipo) {
    if (indices[tipo] < tipos[tipo].length - 1) {
        indices[tipo]++;
    } else {
        indices[tipo] = 0;
    }
    displayDocument(tipo);
}

function habilitarBoton() {
    const input = document.getElementById("buscarDocumento");
    const boton = document.getElementById("botonBuscar");
    boton.disabled = input.value.trim() === "";
}

function realizarConsulta(){
    const input = document.getElementById("buscarDocumento").value.trim();

    if (input) {
        localStorage.setItem("titulo", input);
        window.location.href = "documentos.html";
    }
}

function ver(id){
    documentoService.buscarPorId(id)
        .then(data => {
            localStorage.setItem("Documento", JSON.stringify(data))
            location.replace("descripcionDocumento.html");
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

document.addEventListener("DOMContentLoaded", () => {
    if (!auth.validarSesion()) return;
    fetchDocuments();
});