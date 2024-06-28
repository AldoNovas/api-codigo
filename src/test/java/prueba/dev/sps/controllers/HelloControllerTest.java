package prueba.dev.sps.controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {

    @Test
    void hello() {
        HelloController controller = new HelloController(); // Arrange
        String response = controller.hello("Mundo"); // Act
        assertEquals("Hola, Mundo", response);// Assert
    }
}