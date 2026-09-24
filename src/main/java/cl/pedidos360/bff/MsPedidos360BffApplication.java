package cl.pedidos360.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MsPedidos360BffApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsPedidos360BffApplication.class, args);
    }

    // AGREGAR ESTE MÉTODO
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}