package com.splitnice.authservice;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@OpenAPIDefinition(
        info = @Info(
                title = "Luv Bhardwaj Splitnice API",
                version = "1.0",
                description = "API documentation for managing students"
        )
)
@SpringBootApplication
public class AuthserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthserviceApplication.class, args);
    }
}
