package cl.duoc.paoliciassweet.reservas_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservaRequest(
    @NotBlank String clienteNombre,
    @NotBlank String clienteTelefono,
    String clienteEmail,
    @NotNull Long productoId,
    @NotBlank String productoNombre,
    @NotNull @Min(10) Integer asistentes,
    @NotBlank String fechaEvento,
    String direccionEvento,
    String notas,
    @NotNull @Min(0) Integer total,
    @NotNull @Min(0) Integer abono
) {}