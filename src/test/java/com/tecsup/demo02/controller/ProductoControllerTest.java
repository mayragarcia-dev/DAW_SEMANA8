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

    // TEST PUT - Actualizar producto
    @Test
    void testActualizarProducto() throws Exception {
        // Crear producto primero
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Teclado");
        dto.setPrecio(100);
        dto.setStock(10);
        dto.setCategoria("Accesorios");

        String response = mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        // Actualizar
        ProductoDTO dtoUpdate = new ProductoDTO();
        dtoUpdate.setNombre("Teclado Mecanico");
        dtoUpdate.setPrecio(150);
        dtoUpdate.setStock(5);
        dtoUpdate.setCategoria("Gaming");

        mockMvc.perform(put("/api/productos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Teclado Mecanico"))
                .andExpect(jsonPath("$.precio").value(150))
                .andExpect(jsonPath("$.stock").value(5));
    }

    // TEST PUT - Actualizar producto no existente
    @Test
    void testActualizarProductoNoExistente() throws Exception {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Test");
        dto.setPrecio(10);
        dto.setStock(2);
        dto.setCategoria("Test");

        mockMvc.perform(put("/api/productos/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    // TEST PUT - Validación en actualización
    @Test
    void testValidacionActualizacion() throws Exception {
        // Crear producto
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Mouse");
        dto.setPrecio(50);
        dto.setStock(10);
        dto.setCategoria("Accesorios");

        String response = mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        // Actualizar con nombre vacío
        ProductoDTO dtoUpdate = new ProductoDTO();
        dtoUpdate.setNombre("");
        dtoUpdate.setPrecio(50);
        dtoUpdate.setStock(10);
        dtoUpdate.setCategoria("Accesorios");

        mockMvc.perform(put("/api/productos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoUpdate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre").value("El nombre es obligatorio"));
    }

    // TEST DELETE - Eliminar producto
    @Test
    void testEliminarProducto() throws Exception {
        // Crear producto
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Temporal");
        dto.setPrecio(20);
        dto.setStock(5);
        dto.setCategoria("Test");

        String response = mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        // Eliminar
        mockMvc.perform(delete("/api/productos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Eliminado correctamente"));

        // Verificar que no existe
        mockMvc.perform(get("/api/productos/{id}", id))
                .andExpect(status().isNotFound());
    }

    // TEST DELETE - Eliminar producto no existente
    @Test
    void testEliminarProductoNoExistente() throws Exception {
        mockMvc.perform(delete("/api/productos/999"))
                .andExpect(status().isNotFound());
    }
}