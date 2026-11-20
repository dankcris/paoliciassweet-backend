package cl.duoc.paoliciassweet.comprobantes_service.service;

import cl.duoc.paoliciassweet.comprobantes_service.model.Comprobante;
import cl.duoc.paoliciassweet.comprobantes_service.repository.ComprobanteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class ComprobanteService {

    private final ComprobanteRepository repo;
    private final Path uploadDir;

    public ComprobanteService(ComprobanteRepository repo, @Value("${app.upload.dir:/app/uploads}") String dir) {
        this.repo = repo;
        this.uploadDir = Paths.get(dir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear la carpeta de comprobantes", e);
        }
    }

    public Comprobante guardarArchivo(Long reservaId, Integer montoAbonado, MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe adjuntar un archivo válido");
        }
        try {
            String original = archivo.getOriginalFilename() != null ? archivo.getOriginalFilename() : "comprobante.jpg";
            String nombreUnico = UUID.randomUUID() + "_" + original.replaceAll("[^a-zA-Z0-9._-]", "_");
            Path destino = this.uploadDir.resolve(nombreUnico);
            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            Comprobante c = new Comprobante();
            c.setReservaId(reservaId);
            c.setNombreArchivo(original);
            c.setRutaArchivo(nombreUnico);
            c.setTipoContenido(archivo.getContentType());
            c.setMontoAbonado(montoAbonado);
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
        try {
            Path archivo = this.uploadDir.resolve(c.getRutaArchivo()).normalize();
            Resource resource = new UrlResource(archivo.toUri());
            if (resource.exists()) return resource;
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Archivo físico no encontrado");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se pudo leer el archivo");
        }
    }
}