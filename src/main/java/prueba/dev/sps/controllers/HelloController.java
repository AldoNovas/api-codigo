package prueba.dev.sps.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/saludo")
    public String hello(@RequestParam(name = "nombre", defaultValue = "Amigo") String nombre) {
        return String.format("Hola, %s", nombre);
    }
}