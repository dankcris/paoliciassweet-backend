package cl.duoc.paoliciassweet.reservas_service.service;

import cl.duoc.paoliciassweet.reservas_service.dto.ReservaRequest;
import cl.duoc.paoliciassweet.reservas_service.model.Reserva;
import cl.duoc.paoliciassweet.reservas_service.repository.ReservaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ReservaService {

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
        r.setEstado("PENDIENTE");
        return repo.save(r);
    }

    public Reserva actualizarEstado(Long id, String nuevoEstado) {
        Reserva r = obtenerPorId(id);
        r.setEstado(nuevoEstado.toUpperCase());
        return repo.save(r);
    }
}