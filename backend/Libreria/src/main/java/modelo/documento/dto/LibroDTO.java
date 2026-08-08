package modelo.documento.dto;


public class LibroDTO extends DocumentoDTO{
 
	private static final long serialVersionUID = 1L;
	private String isbn; //Atributos de libro
    private String numeroPaginas; //Atributos de libro
   
    public LibroDTO(BuilderLibro builder) {
        super(builder); 
        this.isbn = builder.isbn;
        this.numeroPaginas = builder.numeroPaginas; 
    }

    public String getIsbn() {
        return isbn;
    }
    
    public String getNumeroPaginas() {
		return numeroPaginas;
	}
    
    @Override
    public String toString() {
    	return this.getTitulo()+" "+this.numeroPaginas+" "+this.isbn;
    }

    public static class BuilderLibro extends BuilderDoc {
    	private String isbn; 
        private String numeroPaginas; 
        
        public BuilderLibro setIsbn(String isbn) {
            this.isbn = isbn;
            return this;
        }
        
        public BuilderLibro setNumeroPaginas(String numeroPaginas) {
        	this.numeroPaginas = numeroPaginas; 
        	return this; 
        }
        
        @Override
        public BuilderLibro setIdDocumento(int idDocumento) {
            super.setIdDocumento(idDocumento);
            return this;
        }

        @Override
        public BuilderLibro setTitulo(String titulo) {
            super.setTitulo(titulo);
            return this;
        }

        @Override
        public BuilderLibro setFechaPublicacion(String fechaPublicacion) {
            super.setFechaPublicacion(fechaPublicacion);
            return this;
        }
        
        @Override
        public BuilderLibro setAutores(String autores) {
            super.setAutores(autores);
            return this;
        }

        @Override
        public BuilderLibro setDiaPublicacion(String diaPublicacion) {
            super.setDiaPublicacion(diaPublicacion);
            return this;
        }
        
        @Override
        public BuilderLibro setMesPublicacion(String mesPublicacion) {
            super.setMesPublicacion(mesPublicacion);
            return this;
        }

        @Override
        public BuilderLibro setEditorial(String editorial) {
            super.setEditorial(editorial);
            return this;
        }

        @Override
        public BuilderLibro setEstado(String estado) {
            super.setEstado(estado);
            return this;
        }

        @Override
        public BuilderLibro setPropietario(String propietario) {
            super.setPropietario(propietario);
            return this;
        }  

        @Override
        public BuilderLibro setTipo(String tipo) {
            super.setTipo(tipo);
            return this;
        }

        @Override
        public LibroDTO build() {
            return new LibroDTO(this);
        }
    }
}