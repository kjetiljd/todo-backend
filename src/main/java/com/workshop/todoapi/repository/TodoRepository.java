package com.workshop.todoapi.repository;

import com.workshop.todoapi.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    
    List<Todo> findByCompletedOrderByCreatedAtDesc(boolean completed);
    
    List<Todo> findAllByOrderByCreatedAtDesc();
    
    @Query("SELECT t FROM Todo t ORDER BY " +
           "CASE t.priority " +
           "WHEN 'HIGH' THEN 1 " +
           "WHEN 'MEDIUM' THEN 2 " +
           "WHEN 'LOW' THEN 3 " +
           "END, t.createdAt DESC")
    List<Todo> findAllOrderByPriorityAndCreatedAt();
    
    @Query("SELECT t FROM Todo t ORDER BY " +
           "CASE WHEN t.dueDate IS NULL THEN 1 ELSE 0 END, " +
           "t.dueDate ASC, t.createdAt DESC")
    List<Todo> findAllOrderByDueDateAndCreatedAt();
    
    List<Todo> findAllByOrderByTitleAsc();
    
    @Query("SELECT t FROM Todo t ORDER BY t.completed ASC, t.createdAt DESC")
    List<Todo> findAllOrderByCompletionAndCreatedAt();
}