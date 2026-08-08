package modelo.otros.dto;

import java.io.Serializable;

public class ReservaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idReserva;
    private String fechaReserva;
    private String fechaEntrega;
    private String estado;
    private String usuario;
    private int documento;

    public ReservaDTO(BuilderReserva builder) {
        this.idReserva = builder.idReserva;
        this.fechaReserva = builder.fechaReserva;
        this.fechaEntrega = builder.fechaEntrega;
        this.estado = builder.estado;
        this.usuario = builder.usuario;
        this.documento = builder.documento;
    }

    public int getIdReserva() { return idReserva; }

    public String getFechaReserva() { return fechaReserva; }

    public String getFechaEntrega() { return fechaEntrega; }

    public String getEstado() { return estado; }

    public String getUsuario() { return usuario; }

    public int getDocumento() { return documento; }

    @Override
    public String toString() {
        return "ReservaDTO{idReserva=" + idReserva + ", fechaReserva='" + fechaReserva + "', fechaEntrega='" + fechaEntrega + "', estado='" + estado + "', usuario=" + usuario + ", documento=" + documento + "}";
    }

    public static class BuilderReserva {
        private int idReserva;
        private String fechaReserva;
        private String fechaEntrega;
        private String estado;
        private String usuario;
        private int documento;

        public BuilderReserva setIdReserva(int idReserva) {
            this.idReserva = idReserva;
            return this;
        }

        public BuilderReserva setFechaReserva(String fechaReserva) {
            this.fechaReserva = fechaReserva;
            return this;
        }

        public BuilderReserva setFechaEntrega(String fechaEntrega) {
            this.fechaEntrega = fechaEntrega;
            return this;
        }

        public BuilderReserva setEstado(String estado) {
            this.estado = estado;
            return this;
        }

        public BuilderReserva setUsuario(String usuario) {
            this.usuario = usuario;
            return this;
        }

        public BuilderReserva setDocumento(int documento) {
            this.documento = documento;
            return this;
        }

        public ReservaDTO build() {
            return new ReservaDTO(this);
        }
    }
}
