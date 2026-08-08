package modelo.documento.dto;

public class PonenciaDTO extends DocumentoDTO {

    private static final long serialVersionUID = 1L;
    private String congreso;
    private String isbn;

    public PonenciaDTO(BuilderPonencia builder) {
        super(builder);
        this.congreso = builder.congreso;
        this.isbn = builder.isbn;
    }

    public String getCongreso() {
        return congreso;
    }

    public String getIsbn() {
        return isbn;
    }
    
    @Override
    public String toString() {
    	return this.getTitulo()+" "+this.getIsbn()+" "+this.congreso;
    }

    public static class BuilderPonencia extends BuilderDoc {
        private String congreso;
        private String isbn;

        public BuilderPonencia setCongreso(String congreso) {
            this.congreso = congreso;
            return this;
        }

        public BuilderPonencia setIsbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        @Override
        public BuilderPonencia setIdDocumento(int idDocumento) {
            super.setIdDocumento(idDocumento);
            return this;
        }

        @Override
        public BuilderPonencia setTitulo(String titulo) {
            super.setTitulo(titulo);
            return this;
        }

        @Override
        public BuilderPonencia setFechaPublicacion(String fechaPublicacion) {
            super.setFechaPublicacion(fechaPublicacion);
            return this;
        }
        
        @Override
        public BuilderPonencia setAutores(String autores) {
            super.setAutores(autores);
            return this;
        }

        @Override
        public BuilderPonencia setDiaPublicacion(String diaPublicacion) {
            super.setDiaPublicacion(diaPublicacion);
            return this;
        }

        @Override
        public BuilderPonencia setMesPublicacion(String mesPublicacion) {
            super.setMesPublicacion(mesPublicacion);
            return this;
        }
        
        @Override
        public BuilderPonencia setEditorial(String editorial) {
            super.setEditorial(editorial);
            return this;
        }

        @Override
        public BuilderPonencia setEstado(String estado) {
            super.setEstado(estado);
            return this;
        }

        @Override
        public BuilderPonencia setPropietario(String propietario) {
            super.setPropietario(propietario);
            return this;
        }

        @Override
        public BuilderPonencia setTipo(String tipo) {
            super.setTipo(tipo);
            return this;
        }

        @Override
        public PonenciaDTO build() {
            return new PonenciaDTO(this);
        }
    }
}