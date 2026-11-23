package cl.duoc.paoliciassweet.catalogo_service.dto;

public record CotizacionDto(
    int asistentes,
    int tramo,
    int descuentoPct,
    int subtotal,
    int total,
    int abono
) {}