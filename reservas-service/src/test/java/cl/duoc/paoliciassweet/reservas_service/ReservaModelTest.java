package cl.duoc.paoliciassweet.reservas_service;

import cl.duoc.paoliciassweet.reservas_service.model.Reserva;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReservaModelTest {

    @Test
    void reservaIniciaConEstadoPendiente() {
        Reserva reserva = new Reserva();
        reserva.setClienteNombre("Paola Pérez");
        reserva.setAsistentes(30);
        reserva.setTotal(96000);
        reserva.setAbono(48000);

        assertEquals("PENDIENTE", reserva.getEstado());
        assertEquals(48000, reserva.getAbono());
    }
}