package com.formulariocadastro.demo.controllers;

import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.formulariocadastro.demo.models.Todo;
import com.formulariocadastro.demo.repositories.TodoRepository;

import jakarta.validation.Valid;

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
        todoRepository.findAll(Sort.by("deadline")))
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
    public String create(@Valid Todo todo, BindingResult result) {
        if (result.hasErrors()){
            return "todo/form"; 
        }
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
    public String edit(@Valid Todo todo, BindingResult result) {
        if (result.hasErrors()){
            return "todo/form";
        }
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
    
    @PostMapping("/finish/{id}")
    public String finish(@PathVariable Long id){
        // o todooptional optional<Todo> e retornado por todoRepository.findById(id) para evitar null da classe findbyid caso viesse a ser
        var todooptional = todoRepository.findById(id);
        if (todooptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        var todo = todooptional.get();
        todo.markHasFinished();
        todoRepository.save(todo);
        return "redirect:/";
    }
    
    
}
