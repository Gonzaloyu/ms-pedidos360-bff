package cl.pedidos360.bff.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OrdersClient {

    private final RestClient restClient;

    public OrdersClient(
            RestClient.Builder builder,
            @Value("${services.orders.url:http://localhost:8083}") String ordersUrl) {

        this.restClient = builder
                .baseUrl(ordersUrl)
                .build();
    }

    public String getOrders() {
        return restClient
                .get()
                .uri("/api/orders")
                .retrieve()
                .body(String.class);
    }
    public String getOrdersByClient(String clientId) {
        return restClient
            .get()
            .uri("/api/orders/client/{clientId}", clientId)
            .retrieve()
            .body(String.class);
    }
    public String createOrder(String orderJson) {
        return restClient
                .post()
                .uri("/api/orders")
                .header("Content-Type", "application/json")
                .body(orderJson)
                .retrieve()
                .body(String.class);
    }

    public String updateOrderStatus(
            Long id,
            String statusJson) {

        return restClient
                .put()
                .uri("/api/orders/" + id + "/status")
                .header("Content-Type", "application/json")
                .body(statusJson)
                .retrieve()
                .body(String.class);
    }
}