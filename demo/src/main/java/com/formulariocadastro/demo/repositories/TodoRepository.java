package com.formulariocadastro.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.formulariocadastro.demo.models.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    
}
