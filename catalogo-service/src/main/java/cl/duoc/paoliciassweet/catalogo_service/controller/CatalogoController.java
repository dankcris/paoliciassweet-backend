package cl.duoc.paoliciassweet.catalogo_service.controller;

import cl.duoc.paoliciassweet.catalogo_service.dto.CotizacionDto;
import cl.duoc.paoliciassweet.catalogo_service.dto.CotizacionRequest;
import cl.duoc.paoliciassweet.catalogo_service.model.Producto;
import cl.duoc.paoliciassweet.catalogo_service.repository.ProductoRepository;
import cl.duoc.paoliciassweet.catalogo_service.service.CotizadorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CatalogoController {

    private final ProductoRepository repo;
    private final CotizadorService cotizador;

    public CatalogoController(ProductoRepository repo, CotizadorService cotizador) {
        this.repo = repo;
        this.cotizador = cotizador;
    }

    @GetMapping("/productos")
    public List<Producto> listarProductos() {
        return repo.findAll(Sort.by("categoria", "nombre"));
    }

    @GetMapping("/productos/{id}")
    public Producto obtenerProducto(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    @PostMapping("/cotizaciones")
    public CotizacionDto cotizar(@Valid @RequestBody CotizacionRequest req) {
        Producto producto = obtenerProducto(req.productoId());
        return cotizador.cotizar(producto.getPrecioPorPersona(), req.asistentes());
    }
}