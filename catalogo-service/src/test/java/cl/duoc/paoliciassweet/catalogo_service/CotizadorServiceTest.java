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
}