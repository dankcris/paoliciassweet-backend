package cl.duoc.paoliciassweet.comprobantes_service.controller;

import cl.duoc.paoliciassweet.comprobantes_service.model.Comprobante;
import cl.duoc.paoliciassweet.comprobantes_service.service.ComprobanteService;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/comprobantes")
@CrossOrigin(origins = "*")
public class ComprobanteController {

    private final ComprobanteService service;

    public ComprobanteController(ComprobanteService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Comprobante subirComprobante(
            @RequestParam("reservaId") Long reservaId,
            @RequestParam(value = "montoAbonado", required = false) Integer montoAbonado,
            @RequestParam("archivo") MultipartFile archivo) {
        return service.guardarArchivo(reservaId, montoAbonado, archivo);
    }

    @GetMapping
    public List<Comprobante> listar() {
        return service.listarTodos();
    }

    @GetMapping("/reserva/{reservaId}")
    public List<Comprobante> listarPorReserva(@PathVariable Long reservaId) {
        return service.listarPorReserva(reservaId);
    }

    @GetMapping("/{id}/archivo")
    public ResponseEntity<Resource> verArchivo(@PathVariable Long id) {
        Comprobante c = service.obtenerPorId(id);
        Resource file = service.cargarArchivo(c);
        MediaType mediaType = c.getTipoContenido() != null
                ? MediaType.parseMediaType(c.getTipoContenido())
                : MediaType.APPLICATION_OCTET_STREAM;
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + c.getNombreArchivo() + "\"")
                .body(file);
    }
}