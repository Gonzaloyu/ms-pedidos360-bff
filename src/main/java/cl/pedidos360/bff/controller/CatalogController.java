package cl.pedidos360.bff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/catalog/products")
public class CatalogController {

    @Autowired
    private final RestTemplate restTemplate = new RestTemplate();

    // URL de tu microservicio real de catálogo en el puerto 8081
    private final String CATALOG_SERVICE_URL = "http://localhost:8081/api/products";

    // 1. Obtener todos los productos (Ya funcionando)
    @GetMapping
    public ResponseEntity<Object[]> getProducts() {
        Object[] response = restTemplate.getForObject(CATALOG_SERVICE_URL, Object[].class);
        return ResponseEntity.ok(response);
    }

    // 2. Crear producto (Ya funcionando)
    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody Object product) {
        Object response = restTemplate.postForObject(CATALOG_SERVICE_URL, product, Object.class);
        return ResponseEntity.ok(response);
    }

    // 3. AGREGAR ESTO: Actualizar producto por ID
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody Object product) {
        restTemplate.put(CATALOG_SERVICE_URL + "/" + id, product);
        return ResponseEntity.ok().build();
    }

    // 4. AGREGAR ESTO: Eliminar producto por ID (El que faltaba)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        restTemplate.delete(CATALOG_SERVICE_URL + "/" + id);
        return ResponseEntity.noContent().build();
    }
}