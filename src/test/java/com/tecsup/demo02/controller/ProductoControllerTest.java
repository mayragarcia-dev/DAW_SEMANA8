package com.tecsup.demo02.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.demo02.dto.ProductoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // TEST POST
    @Test
    void testGuardarProducto() throws Exception {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Mouse");
        dto.setPrecio(80);
        dto.setStock(20);
        dto.setCategoria("Accesorios");

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Mouse"));
    }

    // TEST GET
    @Test
    void testListarProductos() throws Exception {
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk());
    }

    // TEST VALIDACIÓN
    @Test
    void testValidacionNombreVacio() throws Exception {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("");
        dto.setPrecio(10);
        dto.setStock(2);
        dto.setCategoria("Test");

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // TEST VALIDACIÓN PRECIO NEGATIVO
    @Test
    void testValidacionPrecioNegativo() throws Exception {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Test");
        dto.setPrecio(-5);
        dto.setStock(2);
        dto.setCategoria("Test");

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // TEST VALIDACIÓN CATEGORÍA VACÍA
    @Test
    void testValidacionCategoriaVacia() throws Exception {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Test");
        dto.setPrecio(10);
        dto.setStock(2);
        dto.setCategoria("");

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}