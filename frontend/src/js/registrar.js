const regForm = document.getElementById("formRegister");
const regSubmitBtn = document.getElementById("formSubmitBtn");

regSubmitBtn.disabled = true;

const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const phoneRegex = /^\d{10}$/;

function validarFormulario() {
    const nombre = document.getElementById("nombre").value.trim();
    const telefono = document.getElementById("telefono").value.trim();
    const direccion = document.getElementById("direccion").value.trim();
    const correo = document.getElementById("correoElectronico").value.trim();
    const contrasena = document.getElementById("contrasena").value.trim();

    if (!nombre || !telefono || !direccion || !correo || !contrasena) {
        regSubmitBtn.disabled = true;
        return;
    }

    if (!phoneRegex.test(telefono) || !emailRegex.test(correo)) {
        regSubmitBtn.disabled = true;
        return;
    }

    regSubmitBtn.disabled = false;
}

document.querySelectorAll("#formRegister input").forEach(input => {
    input.addEventListener("input", validarFormulario);
});

regSubmitBtn.addEventListener("click", function(event) {
    event.preventDefault();

    const regFormData = new FormData(regForm);

    const regData = {
        nombre: regFormData.get("nombre"),
        numeroTelefonico: regFormData.get("telefono"),
        direccionFisica: regFormData.get("direccion"),
        correoElectronico: regFormData.get("correoElectronico"),
        contrasena: regFormData.get("contrasena")
    };

    fetch("/api/usuario/registrar", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(regData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Error HTTP: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        if (data.mensaje === "Creado") {
            alert("Usuario creado exitosamente");
            location.replace("login.html");
        } else {
            alert(data.mensaje);
        }
    })
    .catch(error => {
        console.error("Error:", error);
    });
});
