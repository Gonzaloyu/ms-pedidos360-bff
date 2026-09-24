package cl.pedidos360.bff.controller;

import cl.pedidos360.bff.client.CatalogClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final CatalogClient catalogClient;

    public CatalogController(CatalogClient catalogClient) {
        this.catalogClient = catalogClient;
    }

    @GetMapping("/products")
    public String getProducts() {
        return catalogClient.getProducts();
    }

    // NUEVO: Endpoint para agregar productos
    @PostMapping("/products")
    public String addProduct(@RequestBody String productJson) {
        return catalogClient.addProduct(productJson);
    }
}