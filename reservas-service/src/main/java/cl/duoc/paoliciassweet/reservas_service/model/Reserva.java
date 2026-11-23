package cl.duoc.paoliciassweet.reservas_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clienteNombre;

    @Column(nullable = false)
    private String clienteTelefono;

    private String clienteEmail;

    @Column(nullable = false)
    private Long productoId;

    @Column(nullable = false)
    private String productoNombre;

    @Column(nullable = false)
    private Integer asistentes;

    @Column(nullable = false)
    private String fechaEvento;

    private String direccionEvento;

    @Column(length = 500)
    private String notas;

    @Column(nullable = false)
    private Integer total;

    @Column(nullable = false)
    private Integer abono;

    @Column(nullable = false)
    private String estado = "PENDIENTE";

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    public Reserva() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
    public String getClienteTelefono() { return clienteTelefono; }
    public void setClienteTelefono(String clienteTelefono) { this.clienteTelefono = clienteTelefono; }
    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }
    public Integer getAsistentes() { return asistentes; }
    public void setAsistentes(Integer asistentes) { this.asistentes = asistentes; }
    public String getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(String fechaEvento) { this.fechaEvento = fechaEvento; }
    public String getDireccionEvento() { return direccionEvento; }
    public void setDireccionEvento(String direccionEvento) { this.direccionEvento = direccionEvento; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
    public Integer getAbono() { return abono; }
    public void setAbono(Integer abono) { this.abono = abono; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}