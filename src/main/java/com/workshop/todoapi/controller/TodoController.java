package com.workshop.todoapi.controller;

import com.workshop.todoapi.model.Todo;
import com.workshop.todoapi.service.TodoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class TodoController {
    
    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);
    
    private final TodoService todoService;
    
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    
    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos(
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy) {
        logger.debug("GET /api/todos?sortBy={}", sortBy);
        List<Todo> todos = todoService.getAllTodos(sortBy);
        return ResponseEntity.ok(todos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        logger.debug("GET /api/todos/{}", id);
        return todoService.getTodoById(id)
                .map(todo -> ResponseEntity.ok(todo))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody Todo todo) {
        logger.debug("POST /api/todos: {}", todo.getTitle());
        Todo createdTodo = todoService.createTodo(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(
            @PathVariable Long id, 
            @Valid @RequestBody Todo todoUpdates) {
        logger.debug("PUT /api/todos/{}: {}", id, todoUpdates.getTitle());
        return todoService.updateTodo(id, todoUpdates)
                .map(todo -> ResponseEntity.ok(todo))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Todo> toggleTodo(@PathVariable Long id) {
        logger.debug("PATCH /api/todos/{}/toggle", id);
        return todoService.toggleTodo(id)
                .map(todo -> ResponseEntity.ok(todo))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        logger.debug("DELETE /api/todos/{}", id);
        if (todoService.deleteTodo(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/count")
    public ResponseEntity<Long> getTodoCount() {
        logger.debug("GET /api/todos/count");
        long count = todoService.getTodoCount();
        return ResponseEntity.ok(count);
    }
}