package cl.duoc.paoliciassweet.comprobantes_service.repository;

import cl.duoc.paoliciassweet.comprobantes_service.model.Comprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComprobanteRepository extends JpaRepository<Comprobante, Long> {
    List<Comprobante> findByReservaIdOrderByIdDesc(Long reservaId);
}