package cl.duoc.paoliciassweet.reservas_service.controller;

import cl.duoc.paoliciassweet.reservas_service.dto.ReservaRequest;
import cl.duoc.paoliciassweet.reservas_service.model.Reserva;
import cl.duoc.paoliciassweet.reservas_service.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Reserva> listar(@RequestParam(required = false) String email) {
        return service.listar(email);
    }

    @GetMapping("/{id}")
    public Reserva obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reserva crear(@Valid @RequestBody ReservaRequest req) {
        return service.crear(req);
    }

    @PutMapping("/{id}")
    public Reserva actualizar(@PathVariable Long id, @Valid @RequestBody ReservaRequest req) {
        return service.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @PutMapping("/{id}/estado")
    public Reserva cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String estado = body.getOrDefault("estado", "CONFIRMADA");
        return service.actualizarEstado(id, estado);
    }
}