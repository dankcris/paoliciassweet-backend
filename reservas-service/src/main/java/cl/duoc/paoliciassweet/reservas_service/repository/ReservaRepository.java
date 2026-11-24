package cl.duoc.paoliciassweet.reservas_service.repository;

import cl.duoc.paoliciassweet.reservas_service.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByClienteEmailIgnoreCaseOrderByIdDesc(String clienteEmail);
    List<Reserva> findAllByOrderByIdDesc();
}