package cl.duoc.paoliciassweet.catalogo_service.service;

import cl.duoc.paoliciassweet.catalogo_service.dto.CotizacionDto;
import org.springframework.stereotype.Service;

@Service
public class CotizadorService {

    public int calcularTramo(int asistentes) {
        int tramo = ((asistentes + 9) / 10) * 10;
        return Math.max(10, Math.min(100, tramo));
    }

    public int calcularDescuentoPct(int tramo) {
        if (tramo <= 20) return 0;
        if (tramo <= 50) return 5;
        return 10;
    }

    public CotizacionDto cotizar(int precioPorPersona, int asistentes) {
        int tramo = calcularTramo(asistentes);
        int descuento = calcularDescuentoPct(tramo);
        int subtotal = precioPorPersona * tramo;
        int total = subtotal - (subtotal * descuento / 100);
        int abono = (total + 1) / 2;
        return new CotizacionDto(asistentes, tramo, descuento, subtotal, total, abono);
    }
}