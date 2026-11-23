package cl.duoc.paoliciassweet.reservas_service.service;

import cl.duoc.paoliciassweet.reservas_service.dto.ReservaRequest;
import cl.duoc.paoliciassweet.reservas_service.model.Reserva;
import cl.duoc.paoliciassweet.reservas_service.repository.ReservaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
public class ReservaService {

    public static final Set<String> ESTADOS_VALIDOS = Set.of(
            "PENDIENTE", "COMPROBANTE_ENVIADO", "EN_REVISION", "ABONO_50", "COMPLETADO", "CANCELADA");

    private final ReservaRepository repo;

    public ReservaService(ReservaRepository repo) {
        this.repo = repo;
    }

    public List<Reserva> listar(String email) {
        if (email != null && !email.isBlank()) {
            return repo.findByClienteEmailIgnoreCaseOrderByIdDesc(email);
        }
        return repo.findAllByOrderByIdDesc();
    }

    public Reserva obtenerPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada"));
    }

    public Reserva crear(ReservaRequest req) {
        Reserva r = new Reserva();
        copiarDatos(r, req);
        r.setEstado("PENDIENTE");
        return repo.save(r);
    }

    public Reserva actualizar(Long id, ReservaRequest req) {
        Reserva r = obtenerPorId(id);
        copiarDatos(r, req);
        return repo.save(r);
    }

    public void eliminar(Long id) {
        Reserva r = obtenerPorId(id);
        repo.delete(r);
    }

    private void copiarDatos(Reserva r, ReservaRequest req) {
        r.setClienteNombre(req.clienteNombre());
        r.setClienteTelefono(req.clienteTelefono());
        r.setClienteEmail(req.clienteEmail());
        r.setProductoId(req.productoId());
        r.setProductoNombre(req.productoNombre());
        r.setAsistentes(req.asistentes());
        r.setFechaEvento(req.fechaEvento());
        r.setDireccionEvento(req.direccionEvento());
        r.setNotas(req.notas());
        r.setTotal(req.total());
        r.setAbono(req.abono());
    }

    public Reserva actualizarEstado(Long id, String nuevoEstado) {
        String estado = nuevoEstado == null ? "" : nuevoEstado.trim().toUpperCase();
        if (!ESTADOS_VALIDOS.contains(estado)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado no válido: " + nuevoEstado);
        }
        Reserva r = obtenerPorId(id);
        r.setEstado(estado);
        return repo.save(r);
    }
}