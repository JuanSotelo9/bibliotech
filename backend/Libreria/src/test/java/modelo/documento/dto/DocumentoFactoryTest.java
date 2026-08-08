package modelo.documento.dto;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DocumentoFactoryTest {

    @Test
    void getBuilder_articulo() {
        assertInstanceOf(ArticuloDTO.BuilderArticulo.class, DocumentoFactory.getBuilder("articulo"));
    }

    @Test
    void getBuilder_libro() {
        assertInstanceOf(LibroDTO.BuilderLibro.class, DocumentoFactory.getBuilder("libro"));
    }

    @Test
    void getBuilder_ponencia() {
        assertInstanceOf(PonenciaDTO.BuilderPonencia.class, DocumentoFactory.getBuilder("ponencia"));
    }

    @Test
    void getBuilder_tipoNoSoportado() {
        assertThrows(IllegalArgumentException.class, () -> DocumentoFactory.getBuilder("revista"));
    }
}
