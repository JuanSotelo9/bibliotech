const usuarioService = {
    login(datos) {
        return api.postPublic(BASE_URL + '/usuario/login', datos);
    },
    registrar(datos) {
        return api.postPublic(BASE_URL + '/usuario/registrar', datos);
    },
    obtenerDatos() {
        return api.get(BASE_URL + '/usuario/datos');
    },
    consultarUsuario(nombre) {
        return api.post(BASE_URL + '/usuario/consultar', { nombre });
    },
    obtenerReservas() {
        return api.get(BASE_URL + '/usuario/reservas');
    }
};
