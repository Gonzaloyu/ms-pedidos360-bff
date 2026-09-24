package cl.pedidos360.bff.controller;

import cl.pedidos360.bff.client.OrdersClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private final OrdersClient ordersClient;

    public OrdersController(OrdersClient ordersClient) {
        this.ordersClient = ordersClient;
    }

    @GetMapping
    public String getOrders() {
        return ordersClient.getOrders();
    }

    @PostMapping
    public String createOrder(@RequestBody String orderJson) {
        return ordersClient.createOrder(orderJson);
    }

    @PutMapping("/{id}/status")
    public String updateOrderStatus(
            @PathVariable Long id,
            @RequestBody String statusJson) {

        return ordersClient.updateOrderStatus(id, statusJson);
    }
}