package cl.duoc.paoliciassweet.catalogo_service.config;

import cl.duoc.paoliciassweet.catalogo_service.model.Producto;
import cl.duoc.paoliciassweet.catalogo_service.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner cargarCatalogoInicial(ProductoRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.saveAll(List.of(
                    new Producto("Torta tres leches", "Bizcocho esponjoso bañado en tres leches con crema chantilly.", "Tortas", 3200),
                    new Producto("Torta mil hojas manjar", "Capas crujientes de hojarasca con manjar casero y nueces.", "Tortas", 3500),
                    new Producto("Torta chocolate personalizada", "Torta de chocolate con diseño y mensaje a pedido.", "Tortas", 4200),
                    new Producto("Coffee break corporativo", "Café, té, jugos, mini sándwiches y pastelería fina.", "Banquetería", 4800),
                    new Producto("Cóctel de bienvenida", "Canapés fríos y calientes, mini empanadas y bebestibles.", "Banquetería", 6900),
                    new Producto("Almuerzo ejecutivo", "Entrada, plato de fondo y postre servido en mesa.", "Banquetería", 9800),
                    new Producto("Menú vegetariano", "Entrada, fondo y postre sin carnes. Apto para vegetarianos.", "Banquetería", 8900),
                    new Producto("Menú sin gluten", "Preparado sin gluten, apto para celíacos. Indica alergias al reservar.", "Banquetería", 9400)
                ));
            }
        };
    }
}