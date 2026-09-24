package cl.pedidos360.bff.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CatalogClient {

    private final RestClient restClient;

    public CatalogClient(
            RestClient.Builder builder,
            @Value("${services.catalog.url:http://localhost:8081}") String catalogUrl) {

        this.restClient = builder
                .baseUrl(catalogUrl)
                .build();
    }

    public String getProducts() {
        return restClient
                .get()
                .uri("/api/products")
                .retrieve()
                .body(String.class);
    }

    // NUEVO: Permite enviar la creación de un producto al MS Catálogo
    public String addProduct(String productJson) {
        return restClient
                .post()
                .uri("/api/products")
                .header("Content-Type", "application/json")
                .body(productJson)
                .retrieve()
                .body(String.class);
    }
}