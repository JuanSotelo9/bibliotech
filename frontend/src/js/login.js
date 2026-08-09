const form = document.getElementById('formLogin');
const submitBtn = document.getElementById('submitBtn');
const nombreInput = document.getElementById('nombre');
const contrasenaInput = document.getElementById('contrasena');

submitBtn.disabled = true;

function validarCampos() {
    if (nombreInput.value.trim() !== "" && contrasenaInput.value.trim() !== "") {
        submitBtn.disabled = false;
    } else {
        submitBtn.disabled = true;
    }
}

nombreInput.addEventListener('input', validarCampos);
contrasenaInput.addEventListener('input', validarCampos);

submitBtn.addEventListener('click', function (event) { // REST
    event.preventDefault(); // Evita el envío del formulario

    const formData = new FormData(form);
    const data = {
        nombre: formData.get('nombre'),
        contrasena: formData.get('contrasena')
    };

    usuarioService.login(data)
        .then(data => {
            if (data.mensaje) {
                alert(data.mensaje);
            } else if (data.token) {
                auth.setToken(data.token);
                location.replace("paginaPrincipal.html");
            }
        }).catch(error => {
            console.error('Error:', error);
        });
});

