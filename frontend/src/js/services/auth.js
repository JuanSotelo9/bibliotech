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
    }
};
