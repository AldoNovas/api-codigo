package prueba.dev.sps.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HelloController.class)
class HelloControllerIntTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void pruebaSaludo() throws Exception {
        RequestBuilder request = get("/saludo");
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals("Hola, Amigo", result.getResponse().getContentAsString());
    }

    @Test
    public void pruebaSaludoConNombre() throws Exception {
        mvc.perform(get("/saludo?nombre=Sanpy"))
                .andExpect(content().string("Hola, Sanpy"));
    }
}

