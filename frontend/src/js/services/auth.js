const auth = {
    getToken() {
        return localStorage.getItem("token");
    },
    setToken(token) {
        localStorage.setItem("token", token);
    },
    clearToken() {
        localStorage.removeItem("token");
    },
    getAuthHeaders() {
        return {
            'Authorization': `Bearer ${auth.getToken()}`,
            'Content-Type': 'application/json'
        };
    },
    tokenExpirado(token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
            if (!payload.exp) return false;
            return payload.exp * 1000 < Date.now();
        } catch (_) {
            return true;
        }
    },
    validarSesion() {
        const token = auth.getToken();
        if (!token || auth.tokenExpirado(token)) {
            auth.clearToken();
            window.location.href = "login.html";
            return false;
        }
        return true;
    }
};
