package cl.pedidos360.bff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/catalog/products")
public class CatalogController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.catalog.url}/api/products")
    private String CATALOG_SERVICE_URL;

    @GetMapping
    public ResponseEntity<Object[]> getProducts() {
        Object[] response = restTemplate.getForObject(CATALOG_SERVICE_URL, Object[].class);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody Object product) {
        Object response = restTemplate.postForObject(CATALOG_SERVICE_URL, product, Object.class);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody Object product) {
        restTemplate.put(CATALOG_SERVICE_URL + "/" + id, product);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        restTemplate.delete(CATALOG_SERVICE_URL + "/" + id);
        return ResponseEntity.noContent().build();
    }
}