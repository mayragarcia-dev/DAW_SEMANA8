package com.tecsup.demo02.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.demo02.dto.CategoriaDTO;
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
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // TEST POST - Crear categoria
    @Test
    void testGuardarCategoria() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Tecnologia");
        dto.setDescripcion("Productos tecnologicos");

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Tecnologia"))
                .andExpect(jsonPath("$.descripcion").value("Productos tecnologicos"));
    }

    // TEST POST - Crear categoria duplicada (debe fallar)
    @Test
    void testGuardarCategoriaDuplicada() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Electronicos");
        dto.setDescripcion("Dispositivos");

        // Primera creacion
        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        // Segunda creacion con mismo nombre
        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    // TEST GET - Listar categorias
    @Test
    void testListarCategorias() throws Exception {
        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk());
    }

    // TEST GET - Obtener por ID
    @Test
    void testObtenerCategoriaPorId() throws Exception {
        // Primero creamos una categoria
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Libros");
        dto.setDescripcion("Libros y revistas");

        String response = mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Extraer ID de la respuesta
        Long id = objectMapper.readTree(response).get("id").asLong();

        // Ahora obtenerla
        mockMvc.perform(get("/api/categorias/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Libros"));
    }

    // TEST GET - Obtener por ID inexistente
    @Test
    void testObtenerCategoriaNoExistente() throws Exception {
        mockMvc.perform(get("/api/categorias/999"))
                .andExpect(status().isNotFound());
    }

    // TEST GET - Buscar por nombre
    @Test
    void testBuscarPorNombre() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Juguetes");
        dto.setDescripcion("Juguetes para ninos");

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/categorias/nombre/Juguetes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juguetes"));
    }

    // TEST PUT - Actualizar categoria
    @Test
    void testActualizarCategoria() throws Exception {
        // Crear categoria
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Muebles");
        dto.setDescripcion("Muebles para hogar");

        String response = mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        // Actualizar
        CategoriaDTO dtoUpdate = new CategoriaDTO();
        dtoUpdate.setNombre("Muebles y Decoracion");
        dtoUpdate.setDescripcion("Muebles y decoracion para el hogar");

        mockMvc.perform(put("/api/categorias/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Muebles y Decoracion"));
    }

    // TEST PUT - Actualizar categoria no existente
    @Test
    void testActualizarCategoriaNoExistente() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Test");
        dto.setDescripcion("Test");

        mockMvc.perform(put("/api/categorias/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    // TEST DELETE - Eliminar categoria
    @Test
    void testEliminarCategoria() throws Exception {
        // Crear categoria
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Temporal");
        dto.setDescripcion("Para eliminar");

        String response = mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        // Eliminar
        mockMvc.perform(delete("/api/categorias/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Eliminado correctamente"));

        // Verificar que no existe
        mockMvc.perform(get("/api/categorias/{id}", id))
                .andExpect(status().isNotFound());
    }

    // TEST VALIDACION - Nombre vacio
    @Test
    void testValidacionNombreVacio() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("");
        dto.setDescripcion("Test");

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre").value("El nombre es obligatorio"));
    }

    // TEST VALIDACION - Nombre muy largo
    @Test
    void testValidacionNombreMuyLargo() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("a".repeat(101));
        dto.setDescripcion("Test");

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre").value("El nombre no puede exceder 100 caracteres"));
    }

    // TEST VALIDACION - Descripcion muy larga
    @Test
    void testValidacionDescripcionMuyLarga() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Test");
        dto.setDescripcion("a".repeat(501));

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.descripcion").value("La descripcion no puede exceder 500 caracteres"));
    }
}