package com.formulariocadastro.demo.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.formulariocadastro.demo.models.Todo;
import com.formulariocadastro.demo.repositories.TodoRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;



@Controller
public class homeController {

    // injeçao do repository 

    private final TodoRepository todoRepository;

    public homeController(TodoRepository todoRepository){
        this.todoRepository =  todoRepository;
    }


    @GetMapping("/")
    public ModelAndView list(){
        return new ModelAndView(
        "todo/list", 
        Map.of("todos", 
        todoRepository.findAll())
        );
    }

    @GetMapping("/create")
    public ModelAndView create(){
        return new ModelAndView(
        "todo/form", 
        Map.of("todo", 
        new Todo())
        );
    }

    @PostMapping("/create")
    public String create(Todo todo) {
        todoRepository.save(todo);
        
        return "redirect:/";
    }
    
    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        var todo = todoRepository.findById(id);
        if (todo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } 
        return new ModelAndView("todo/form", Map.of("todo", todo.get()));
    }

    @PostMapping("/edit/{id}")
    public String edit(Todo todo,@PathVariable Long id) {
        todoRepository.save(todo);
        
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id) {
        var todo = todoRepository.findById(id);
        if (todo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } 
        return new ModelAndView("todo/delete", Map.of("todo", todo.get()));

    }

    @PostMapping("/delete/{id}")
    public String delete(Todo todo) {
        todoRepository.delete(todo);
        
        return "redirect:/";
    }
    
    
    
}