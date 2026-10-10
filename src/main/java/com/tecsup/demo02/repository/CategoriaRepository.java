package com.tecsup.demo02.repository;

import com.tecsup.demo02.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByNombreContaining(String nombre);

    boolean existsByNombre(String nombre);
}