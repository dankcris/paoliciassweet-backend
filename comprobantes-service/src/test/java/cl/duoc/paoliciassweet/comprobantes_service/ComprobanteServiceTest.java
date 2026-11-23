package cl.duoc.paoliciassweet.comprobantes_service;

import cl.duoc.paoliciassweet.comprobantes_service.model.Comprobante;
import cl.duoc.paoliciassweet.comprobantes_service.repository.ComprobanteRepository;
import cl.duoc.paoliciassweet.comprobantes_service.service.ComprobanteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComprobanteServiceTest {

    @Mock
    private ComprobanteRepository repo;

    private ComprobanteService service;

    @BeforeEach
    void setUp() {
        service = new ComprobanteService(repo);
    }

    @Test
    void guardarArchivoAlmacenaBytesYMetadatos() {
        when(repo.save(any(Comprobante.class))).thenAnswer(inv -> inv.getArgument(0));
        MockMultipartFile archivo = new MockMultipartFile(
                "archivo", "transferencia 1.jpg", "image/jpeg", new byte[]{1, 2, 3});

        Comprobante c = service.guardarArchivo(10L, 48000, archivo);

        assertEquals(10L, c.getReservaId());
        assertEquals(48000, c.getMontoAbonado());
        assertEquals("transferencia 1.jpg", c.getNombreArchivo());
        assertEquals("transferencia_1.jpg", c.getRutaArchivo());
        assertEquals("image/jpeg", c.getTipoContenido());
        assertArrayEquals(new byte[]{1, 2, 3}, c.getContenido());
        assertEquals("RECIBIDO", c.getEstado());
    }

    @Test
    void guardarArchivoSinNombreUsaNombrePorDefecto() {
        when(repo.save(any(Comprobante.class))).thenAnswer(inv -> inv.getArgument(0));
        MockMultipartFile archivo = new MockMultipartFile("archivo", "", "image/png", new byte[]{9});
        assertEquals("comprobante.jpg", service.guardarArchivo(1L, null, archivo).getNombreArchivo());
    }

    @Test
    void guardarArchivoVacioLanzaBadRequest() {
        MockMultipartFile vacio = new MockMultipartFile("archivo", "a.jpg", "image/jpeg", new byte[0]);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.guardarArchivo(1L, 1000, vacio));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        verify(repo, never()).save(any());
    }

    @Test
    void guardarArchivoNuloLanzaBadRequest() {
        assertThrows(ResponseStatusException.class, () -> service.guardarArchivo(1L, 1000, null));
    }

    @Test
    void listarPorReservaDelegaEnElRepositorio() {
        when(repo.findByReservaIdOrderByIdDesc(4L)).thenReturn(List.of(new Comprobante()));
        assertEquals(1, service.listarPorReserva(4L).size());
    }

    @Test
    void listarTodosDelegaEnElRepositorio() {
        when(repo.findAll()).thenReturn(List.of(new Comprobante(), new Comprobante()));
        assertEquals(2, service.listarTodos().size());
    }

    @Test
    void obtenerPorIdInexistenteLanzaNotFound() {
        when(repo.findById(8L)).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.obtenerPorId(8L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void cargarArchivoEntregaLosBytesGuardados() throws IOException {
        Comprobante c = new Comprobante();
        c.setContenido(new byte[]{5, 6});
        Resource r = service.cargarArchivo(c);
        assertEquals(2, r.contentLength());
    }

    @Test
    void cargarArchivoSinContenidoLanzaNotFound() {
        assertThrows(ResponseStatusException.class, () -> service.cargarArchivo(new Comprobante()));
    }
}
