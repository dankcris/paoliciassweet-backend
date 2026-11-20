package cl.duoc.paoliciassweet.catalogo_service.repository;

import cl.duoc.paoliciassweet.catalogo_service.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}