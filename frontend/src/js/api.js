function handle401() {
    alert("Sesion expirada. Por favor, inicia sesion de nuevo.");
    localStorage.removeItem("token");
    window.location.href = "login.html";
    throw new Error("Usuario no autorizado (401)");
}

function authHeaders() {
    return {
        'Authorization': `Bearer ${localStorage.getItem("token")}`,
        'Content-Type': 'application/json'
    };
}

async function handleErrorResponse(response) {
    let mensaje = `Error HTTP: ${response.status}`;
    try {
        const err = await response.json();
        mensaje = `${err.codigo}: ${err.detalle}`;
    } catch (_) {}
    alert(mensaje);
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
        return fetch(url, {
            method: 'GET',
            headers: authHeaders()
        }).then(checkResponse);
    },

    post(url, body) {
        return fetch(url, {
            method: 'POST',
            headers: authHeaders(),
            body: JSON.stringify(body)
        }).then(checkResponse);
    },

    postPublic(url, body) {
        return fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        }).then(response => {
            if (!response.ok) {
                return handleErrorResponse(response);
            }
            return response.json();
        });
    }
};
