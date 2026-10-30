package cl.pedidos360.bff.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Puerta de entrada de la administración de RabbitMQ.
 * La autorización (solo ROLE_ADMINISTRADOR) se define en SecurityConfig para /api/admin/**.
 * El BFF no tiene lógica de negocio: valida el acceso y reenvía al microservicio administrador,
 * respetando el código HTTP y el cuerpo que éste responde (400, 404, 409...).
 */
@RestController
@RequestMapping("/api/admin")
public class RabbitAdminController {

    private final RestTemplate restTemplate;
    private final String adminUrl;

    public RabbitAdminController(RestTemplate restTemplate,
                                 @Value("${services.rabbit-admin.url}") String adminUrl) {
        this.restTemplate = restTemplate;
        this.adminUrl = adminUrl;
    }

    @GetMapping("/queues")
    public ResponseEntity<String> listarColas() {
        return reenviar(HttpMethod.GET, uri("/api/queues"), null);
    }

    @PostMapping("/queues")
    public ResponseEntity<String> crearCola(@RequestBody String cuerpo) {
        return reenviar(HttpMethod.POST, uri("/api/queues"), cuerpo);
    }

    @GetMapping("/queues/{name}")
    public ResponseEntity<String> obtenerCola(@PathVariable String name) {
        return reenviar(HttpMethod.GET, uri("/api/queues/{name}", name), null);
    }

    @DeleteMapping("/queues/{name}")
    public ResponseEntity<String> eliminarCola(@PathVariable String name) {
        return reenviar(HttpMethod.DELETE, uri("/api/queues/{name}", name), null);
    }

    @DeleteMapping("/queues/{name}/messages")
    public ResponseEntity<String> vaciarCola(@PathVariable String name) {
        return reenviar(HttpMethod.DELETE, uri("/api/queues/{name}/messages", name), null);
    }

    // --------------------------- Exchanges --------------------------

    @PostMapping("/exchanges")
    public ResponseEntity<String> crearExchange(@RequestBody String cuerpo) {
        return reenviar(HttpMethod.POST, uri("/api/exchanges"), cuerpo);
    }

    @DeleteMapping("/exchanges/{name}")
    public ResponseEntity<String> eliminarExchange(@PathVariable String name) {
        return reenviar(HttpMethod.DELETE, uri("/api/exchanges/{name}", name), null);
    }

    // ---------------------------- Bindings --------------------------

    @PostMapping("/bindings")
    public ResponseEntity<String> crearBinding(@RequestBody String cuerpo) {
        return reenviar(HttpMethod.POST, uri("/api/bindings"), cuerpo);
    }

    @DeleteMapping("/bindings")
    public ResponseEntity<String> eliminarBinding(@RequestParam String queue,
                                                  @RequestParam String exchange,
                                                  @RequestParam(defaultValue = "") String routingKey) {
        return reenviar(HttpMethod.DELETE,
                uri("/api/bindings?queue={queue}&exchange={exchange}&routingKey={routingKey}",
                        queue, exchange, routingKey),
                null);
    }

    // ----------------------------- Apoyo ----------------------------

    private URI uri(String plantilla, Object... variables) {
        return UriComponentsBuilder.fromUriString(adminUrl + plantilla)
                .encode()
                .buildAndExpand(variables)
                .toUri();
    }

    private ResponseEntity<String> reenviar(HttpMethod metodo, URI destino, String cuerpo) {
        HttpHeaders cabeceras = new HttpHeaders();
        cabeceras.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<String> respuesta = restTemplate.exchange(
                    destino, metodo, new HttpEntity<>(cuerpo, cabeceras), String.class);
            return ResponseEntity.status(respuesta.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(respuesta.getBody());

        } catch (HttpStatusCodeException e) {
            // El administrador respondió con un error (400, 404, 409...): se entrega tal cual.
            return ResponseEntity.status(e.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getResponseBodyAsString());

        } catch (ResourceAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\":\"El servicio administrador de RabbitMQ no responde\"}");
        }
    }
}