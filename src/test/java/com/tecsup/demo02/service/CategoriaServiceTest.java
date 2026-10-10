package com.tecsup.demo02.service;

import com.tecsup.demo02.model.Categoria;
import com.tecsup.demo02.repository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriaServiceTest {

    @Mock
    private CategoriaRepository repo;

    @InjectMocks
    private CategoriaService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarCategorias() {
        List<Categoria> lista = List.of(
                new Categoria(1L, "Electronicos", "Dispositivos electronicos"),
                new Categoria(2L, "Ropa", "Prendas de vestir")
        );

        when(repo.findAll()).thenReturn(lista);

        List<Categoria> resultado = service.listar();

        assertEquals(2, resultado.size());
        assertEquals("Electronicos", resultado.get(0).getNombre());
        verify(repo, times(1)).findAll();
    }

    @Test
    void testGuardarCategoria() {
        Categoria categoria = new Categoria(1L, "Deportes", "Articulos deportivos");

        when(repo.save(categoria)).thenReturn(categoria);

        Categoria resultado = service.guardar(categoria);

        assertNotNull(resultado);
        assertEquals("Deportes", resultado.getNombre());
        verify(repo).save(categoria);
    }

    @Test
    void testObtenerCategoria() {
        Categoria categoria = new Categoria(1L, "Hogar", "Articulos para el hogar");

        when(repo.findById(1L)).thenReturn(Optional.of(categoria));

        Categoria resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Hogar", resultado.getNombre());
        verify(repo).findById(1L);
    }

    @Test
    void testObtenerCategoriaNoExistente() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        Categoria resultado = service.buscarPorId(99L);

        assertNull(resultado);
        verify(repo).findById(99L);
    }

    @Test
    void testEliminarCategoria() {
        doNothing().when(repo).deleteById(1L);

        service.eliminar(1L);

        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void testBuscarPorNombre() {
        List<Categoria> lista = List.of(
                new Categoria(1L, "Electronicos", "Dispositivos"),
                new Categoria(2L, "Electrodomesticos", "Para el hogar")
        );

        when(repo.findByNombreContaining("Electro")).thenReturn(lista);

        List<Categoria> resultado = service.buscarPorNombre("Electro");

        assertEquals(2, resultado.size());
        verify(repo).findByNombreContaining("Electro");
    }

    @Test
    void testExistePorNombre() {
        when(repo.existsByNombre("Electronicos")).thenReturn(true);
        when(repo.existsByNombre("Inexistente")).thenReturn(false);

        assertTrue(service.existePorNombre("Electronicos"));
        assertFalse(service.existePorNombre("Inexistente"));

        verify(repo, times(2)).existsByNombre(anyString());
    }
}