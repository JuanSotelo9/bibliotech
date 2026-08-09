const documentoService = {
    crear(datos) {
        return api.post(BASE_URL + '/documento/crear', datos);
    },
    modificar(datos) {
        return api.post(BASE_URL + '/documento/modificar', datos);
    },
    eliminar(iddocumento) {
        return api.post(BASE_URL + '/documento/eliminar', { iddocumento });
    },
    habilitar(iddocumento) {
        return api.post(BASE_URL + '/documento/habilitar', { iddocumento });
    },
    buscarPorTitulo(titulo) {
        return api.post(BASE_URL + '/documento/titulo', { titulo });
    },
    buscarPorId(iddocumento) {
        return api.post(BASE_URL + '/documento', { iddocumento });
    },
    reservar(documento) {
        return api.post(BASE_URL + '/documento/reservar', { documento });
    },
    entregar(idreserva) {
        return api.post(BASE_URL + '/documento/entregar', { idreserva });
    },
    obtenerEventos(iddocumento) {
        return api.post(BASE_URL + '/documento/eventos', { iddocumento });
    },
    obtenerPorUsuario() {
        return api.get(BASE_URL + '/usuario/documentos');
    }
};
