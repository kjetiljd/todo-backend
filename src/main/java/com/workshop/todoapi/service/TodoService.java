package com.workshop.todoapi.service;

import com.workshop.todoapi.client.SorterClient;
import com.workshop.todoapi.model.Todo;
import com.workshop.todoapi.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TodoService {
    
    private static final Logger logger = LoggerFactory.getLogger(TodoService.class);
    
    private final TodoRepository todoRepository;
    private final SorterClient sorterClient;
    
    public TodoService(TodoRepository todoRepository, SorterClient sorterClient) {
        this.todoRepository = todoRepository;
        this.sorterClient = sorterClient;
    }
    
    public List<Todo> getAllTodos(String sortBy) {
        List<Todo> todos;
        
        if (sortBy == null || sortBy.equals("createdAt")) {
            todos = todoRepository.findAllByOrderByCreatedAtDesc();
        } else {
            // Get all todos and let the sorting service handle the sorting
            todos = todoRepository.findAll();
            
            // Use external sorting service if available, fallback to local sorting
            if (sorterClient.isHealthy()) {
                logger.debug("Using sorting service for sortBy: {}", sortBy);
                todos = sorterClient.sortTodos(todos, sortBy);
            } else {
                logger.debug("Sorting service unavailable, using local sorting for sortBy: {}", sortBy);
                todos = getLocalSortedTodos(sortBy);
            }
        }
        
        logger.info("Retrieved {} todos with sortBy: {}", todos.size(), sortBy);
        return todos;
    }
    
    private List<Todo> getLocalSortedTodos(String sortBy) {
        return switch (sortBy) {
            case "priority" -> todoRepository.findAllOrderByPriorityAndCreatedAt();
            case "dueDate" -> todoRepository.findAllOrderByDueDateAndCreatedAt();
            case "alphabetical" -> todoRepository.findAllByOrderByTitleAsc();
            case "completion" -> todoRepository.findAllOrderByCompletionAndCreatedAt();
            default -> todoRepository.findAllByOrderByCreatedAtDesc();
        };
    }
    
    @Transactional(readOnly = true)
    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }
    
    public Todo createTodo(Todo todo) {
        logger.info("Creating new todo: {}", todo.getTitle());
        return todoRepository.save(todo);
    }
    
    public Optional<Todo> updateTodo(Long id, Todo todoUpdates) {
        return todoRepository.findById(id)
                .map(existingTodo -> {
                    if (todoUpdates.getTitle() != null) {
                        existingTodo.setTitle(todoUpdates.getTitle());
                    }
                    if (todoUpdates.getDescription() != null) {
                        existingTodo.setDescription(todoUpdates.getDescription());
                    }
                    if (todoUpdates.getCompleted() != null) {
                        existingTodo.setCompleted(todoUpdates.getCompleted());
                    }
                    if (todoUpdates.getPriority() != null) {
                        existingTodo.setPriority(todoUpdates.getPriority());
                    }
                    if (todoUpdates.getDueDate() != null) {
                        existingTodo.setDueDate(todoUpdates.getDueDate());
                    }
                    
                    logger.info("Updating todo: {}", existingTodo.getTitle());
                    return todoRepository.save(existingTodo);
                });
    }
    
    public Optional<Todo> toggleTodo(Long id) {
        return todoRepository.findById(id)
                .map(todo -> {
                    todo.setCompleted(!todo.getCompleted());
                    logger.info("Toggling todo completion: {} -> {}", todo.getTitle(), todo.getCompleted());
                    return todoRepository.save(todo);
                });
    }
    
    public boolean deleteTodo(Long id) {
        if (todoRepository.existsById(id)) {
            logger.info("Deleting todo with id: {}", id);
            todoRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    @Transactional(readOnly = true)
    public long getTodoCount() {
        return todoRepository.count();
    }
}