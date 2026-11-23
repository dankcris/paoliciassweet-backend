package cl.duoc.paoliciassweet.comprobantes_service.service;

import cl.duoc.paoliciassweet.comprobantes_service.model.Comprobante;
import cl.duoc.paoliciassweet.comprobantes_service.repository.ComprobanteRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

/**
 * Guarda el comprobante directamente en PostgreSQL (columna bytea), de modo que
 * el archivo persiste aunque el contenedor se reinicie en Render.
 */
@Service
public class ComprobanteService {

    private final ComprobanteRepository repo;

    public ComprobanteService(ComprobanteRepository repo) {
        this.repo = repo;
    }

    public Comprobante guardarArchivo(Long reservaId, Integer montoAbonado, MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe adjuntar un archivo válido");
        }
        try {
            String original = archivo.getOriginalFilename() != null && !archivo.getOriginalFilename().isBlank()
                    ? archivo.getOriginalFilename()
                    : "comprobante.jpg";

            Comprobante c = new Comprobante();
            c.setReservaId(reservaId);
            c.setNombreArchivo(original);
            c.setRutaArchivo(original.replaceAll("[^a-zA-Z0-9._-]", "_"));
            c.setTipoContenido(archivo.getContentType());
            c.setMontoAbonado(montoAbonado);
            c.setContenido(archivo.getBytes());
            c.setEstado("RECIBIDO");
            return repo.save(c);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el comprobante");
        }
    }

    public List<Comprobante> listarTodos() {
        return repo.findAll();
    }

    public List<Comprobante> listarPorReserva(Long reservaId) {
        return repo.findByReservaIdOrderByIdDesc(reservaId);
    }

    public Comprobante obtenerPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comprobante no encontrado"));
    }

    public Resource cargarArchivo(Comprobante c) {
        if (c.getContenido() == null || c.getContenido().length == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Archivo no encontrado");
        }
        return new ByteArrayResource(c.getContenido());
    }
}
