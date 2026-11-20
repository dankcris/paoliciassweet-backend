package cl.duoc.paoliciassweet.catalogo_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CotizacionRequest(
    @NotNull Long productoId,
    @NotNull @Min(10) @Max(100) Integer asistentes
) {}