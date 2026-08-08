package modelo.documento.dto;

import modelo.documento.dto.DocumentoDTO.BuilderDoc;

public class DocumentoFactory {
    public static BuilderDoc getBuilder(String tipo) {
        switch (tipo.toLowerCase()) {
            case "articulo":
                return new ArticuloDTO.BuilderArticulo();
            case "libro":
                return new LibroDTO.BuilderLibro(); 
            case "ponencia":
                return new PonenciaDTO.BuilderPonencia();
            default:
                throw new IllegalArgumentException("Tipo de documento no soportado: " + tipo);
        }
    }
}
