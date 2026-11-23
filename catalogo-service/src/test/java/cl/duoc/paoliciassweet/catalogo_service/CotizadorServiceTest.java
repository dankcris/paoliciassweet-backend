package cl.duoc.paoliciassweet.catalogo_service;

import cl.duoc.paoliciassweet.catalogo_service.dto.CotizacionDto;
import cl.duoc.paoliciassweet.catalogo_service.service.CotizadorService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CotizadorServiceTest {

    private final CotizadorService service = new CotizadorService();

    @Test
    void cotizarSinDescuentoHasta20Asistentes() {
        CotizacionDto res = service.cotizar(3200, 15);
        assertEquals(20, res.tramo());
        assertEquals(0, res.descuentoPct());
        assertEquals(64000, res.subtotal());
        assertEquals(64000, res.total());
        assertEquals(32000, res.abono());
    }

    @Test
    void cotizarConDescuentoDel5PorCientoEntre21Y50() {
        CotizacionDto res = service.cotizar(5000, 35);
        assertEquals(40, res.tramo());
        assertEquals(5, res.descuentoPct());
        assertEquals(200000, res.subtotal());
        assertEquals(190000, res.total());
        assertEquals(95000, res.abono());
    }

    @Test
    void tramoRedondeaHaciaArribaDeDiezEnDiez() {
        assertEquals(30, service.calcularTramo(21));
        assertEquals(30, service.calcularTramo(30));
    }

    @Test
    void tramoRespetaMinimoYMaximo() {
        assertEquals(10, service.calcularTramo(3));
        assertEquals(100, service.calcularTramo(250));
    }

    @Test
    void descuentoPorVolumenSegunTramo() {
        assertEquals(0, service.calcularDescuentoPct(20));
        assertEquals(5, service.calcularDescuentoPct(21));
        assertEquals(5, service.calcularDescuentoPct(50));
        assertEquals(10, service.calcularDescuentoPct(60));
    }

    @Test
    void cotizarCienPersonasAplicaDescuentoDelDiezPorCiento() {
        CotizacionDto res = service.cotizar(1000, 100);
        assertEquals(100000, res.subtotal());
        assertEquals(90000, res.total());
        assertEquals(45000, res.abono());
    }
}
