package cl.duoc.paoliciassweet.reservas_service;

import cl.duoc.paoliciassweet.reservas_service.dto.ReservaRequest;
import cl.duoc.paoliciassweet.reservas_service.model.Reserva;
import cl.duoc.paoliciassweet.reservas_service.repository.ReservaRepository;
import cl.duoc.paoliciassweet.reservas_service.service.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository repo;

    private ReservaService service;

    @BeforeEach
    void setUp() {
        service = new ReservaService(repo);
    }

    private ReservaRequest request() {
        return new ReservaRequest("Camila González", "+56912345678", "camila@ejemplo.cl",
                1L, "Torta Tres Leches", 30, "2026-12-05", "Providencia", "Bloque: Tarde", 96000, 48000);
    }

    private Reserva reserva(Long id) {
        Reserva r = new Reserva();
        r.setId(id);
        r.setClienteNombre("Camila");
        r.setEstado("PENDIENTE");
        return r;
    }

    @Test
    void listarSinEmailRetornaTodas() {
        when(repo.findAllByOrderByIdDesc()).thenReturn(List.of(reserva(1L), reserva(2L)));
        assertEquals(2, service.listar(null).size());
        assertEquals(2, service.listar("  ").size());
        verify(repo, never()).findByClienteEmailIgnoreCaseOrderByIdDesc(any());
    }

    @Test
    void listarConEmailFiltraPorCliente() {
        when(repo.findByClienteEmailIgnoreCaseOrderByIdDesc("camila@ejemplo.cl")).thenReturn(List.of(reserva(1L)));
        assertEquals(1, service.listar("camila@ejemplo.cl").size());
    }

    @Test
    void obtenerPorIdInexistenteLanzaNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.obtenerPorId(99L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void crearGuardaReservaPendienteConLosDatosDelRequest() {
        when(repo.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));
        Reserva creada = service.crear(request());
        assertEquals("PENDIENTE", creada.getEstado());
        assertEquals("Camila González", creada.getClienteNombre());
        assertEquals(30, creada.getAsistentes());
        assertEquals(48000, creada.getAbono());
        assertEquals("2026-12-05", creada.getFechaEvento());
    }

    @Test
    void actualizarModificaLosDatosDeLaReserva() {
        Reserva existente = reserva(5L);
        when(repo.findById(5L)).thenReturn(Optional.of(existente));
        when(repo.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));
        Reserva r = service.actualizar(5L, request());
        assertEquals("Torta Tres Leches", r.getProductoNombre());
        assertEquals(96000, r.getTotal());
        assertEquals("PENDIENTE", r.getEstado());
    }

    @Test
    void actualizarEstadoNormalizaAMayusculas() {
        when(repo.findById(1L)).thenReturn(Optional.of(reserva(1L)));
        when(repo.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));
        assertEquals("ABONO_50", service.actualizarEstado(1L, " abono_50 ").getEstado());
    }

    @Test
    void actualizarEstadoInvalidoLanzaBadRequest() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.actualizarEstado(1L, "INVENTADO"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        verify(repo, never()).save(any());
    }

    @Test
    void actualizarEstadoNuloLanzaBadRequest() {
        assertThrows(ResponseStatusException.class, () -> service.actualizarEstado(1L, null));
    }

    @Test
    void eliminarBorraLaReservaExistente() {
        Reserva r = reserva(3L);
        when(repo.findById(3L)).thenReturn(Optional.of(r));
        service.eliminar(3L);
        verify(repo).delete(r);
    }

    @Test
    void eliminarReservaInexistenteLanzaNotFound() {
        when(repo.findById(7L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> service.eliminar(7L));
        verify(repo, never()).delete(any(Reserva.class));
    }
}
