package cl.pedidos360.bff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    // URL del microservicio de órdenes real en el puerto 8080
    private final String ORDERS_SERVICE_URL = "http://localhost:8080/api/orders";

    // 1. Obtener todas las órdenes
    @GetMapping
    public ResponseEntity<Object[]> getOrders() {
        Object[] response = restTemplate.getForObject(ORDERS_SERVICE_URL, Object[].class);
        return ResponseEntity.ok(response);
    }

    // 2. Crear nueva orden
    @PostMapping
    public ResponseEntity<Object> createOrder(@RequestBody Object orderData) {
        Object response = restTemplate.postForObject(ORDERS_SERVICE_URL, orderData, Object.class);
        return ResponseEntity.ok(response);
    }

    // 3. Actualizar estado de una orden
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestBody Object statusData) {
        restTemplate.put(ORDERS_SERVICE_URL + "/" + id + "/status", statusData);
        return ResponseEntity.ok().build();
    }
}