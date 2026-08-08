package modelo.documento.dto;

public class ArticuloDTO extends DocumentoDTO {

    private static final long serialVersionUID = 1L;
    private String ssn;

    public ArticuloDTO(BuilderArticulo builder) {
        super(builder);
        this.ssn = builder.ssn;
    }

    public String getSsn() {
        return ssn;
    }

    @Override
    public String toString() {
    	return this.getTitulo()+" "+this.ssn;
    }
    
    public static class BuilderArticulo extends BuilderDoc {
        private String ssn;

        public BuilderArticulo setSsn(String ssn) {
            this.ssn = ssn;
            return this;
        }

        @Override
        public BuilderArticulo setIdDocumento(int idDocumento) {
            super.setIdDocumento(idDocumento);
            return this;
        }

        @Override
        public BuilderArticulo setTitulo(String titulo) {
            super.setTitulo(titulo);
            return this;
        }

        @Override
        public BuilderArticulo setFechaPublicacion(String fechaPublicacion) {
            super.setFechaPublicacion(fechaPublicacion);
            return this;
        }
        
        @Override
        public BuilderArticulo setAutores(String autores) {
            super.setAutores(autores);
            return this;
        }

        @Override
        public BuilderArticulo setDiaPublicacion(String diaPublicacion) {
            super.setDiaPublicacion(diaPublicacion);
            return this;
        }
        
        @Override
        public BuilderArticulo setMesPublicacion(String mesPublicacion) {
            super.setMesPublicacion(mesPublicacion);
            return this;
        }

        @Override
        public BuilderArticulo setEditorial(String editorial) {
            super.setEditorial(editorial);
            return this;
        }

        @Override
        public BuilderArticulo setEstado(String estado) {
            super.setEstado(estado);
            return this;
        }

        @Override
        public BuilderArticulo setPropietario(String propietario) {
            super.setPropietario(propietario);
            return this;
        }

        @Override
        public BuilderArticulo setTipo(String tipo) {
            super.setTipo(tipo);
            return this;
        }

        @Override
        public ArticuloDTO build() {
            return new ArticuloDTO(this);
        }
    }
}