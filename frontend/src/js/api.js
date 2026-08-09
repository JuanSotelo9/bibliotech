let redirigiendoLogin = false;

function handle401() {
    auth.clearToken();
    if (!redirigiendoLogin) {
        redirigiendoLogin = true;
        notificacion.info("Sesión expirada. Redirigiendo al login...");
        setTimeout(() => { window.location.href = "login.html"; }, 1500);
    }
    throw new Error("Usuario no autorizado (401)");
}

function authHeaders() {
    return auth.getAuthHeaders();
}

async function handleErrorResponse(response) {
    let mensaje = `Error HTTP: ${response.status}`;
    try {
        const err = await response.json();
        mensaje = `${err.codigo}: ${err.detalle}`;
    } catch (_) {}
    notificacion.error(mensaje);
    throw new Error(mensaje);
}

async function checkResponse(response) {
    if (response.status === 401) {
        handle401();
    }
    if (!response.ok) {
        await handleErrorResponse(response);
    }
    return response.json();
}

const api = {
    get(url) {
        loading.mostrar();
        return fetch(url, {
            method: 'GET',
            headers: authHeaders()
        }).finally(() => loading.ocultar())
          .then(checkResponse);
    },

    post(url, body) {
        loading.mostrar();
        return fetch(url, {
            method: 'POST',
            headers: authHeaders(),
            body: JSON.stringify(body)
        }).finally(() => loading.ocultar())
          .then(checkResponse);
    },

    postPublic(url, body) {
        loading.mostrar();
        return fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        }).finally(() => loading.ocultar())
          .then(response => {
              if (!response.ok) {
                  return handleErrorResponse(response);
              }
              return response.json();
          });
    }
};
