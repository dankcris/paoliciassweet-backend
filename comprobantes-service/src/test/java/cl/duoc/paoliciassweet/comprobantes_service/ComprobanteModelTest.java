package cl.duoc.paoliciassweet.comprobantes_service;

import cl.duoc.paoliciassweet.comprobantes_service.model.Comprobante;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ComprobanteModelTest {

    @Test
    void comprobanteIniciaConEstadoRecibido() {
        Comprobante c = new Comprobante();
        c.setReservaId(1L);
        c.setNombreArchivo("transferencia.jpg");
        c.setMontoAbonado(50000);

        assertEquals("RECIBIDO", c.getEstado());
        assertEquals(1L, c.getReservaId());
    }
}