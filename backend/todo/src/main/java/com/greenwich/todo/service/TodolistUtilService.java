package com.greenwich.todo.service;

import com.greenwich.todo.dto.response.TaskResponseDTO;
import com.greenwich.todo.entity.Todolist;
import com.greenwich.todo.exception.ResourceNotFoundException;
import com.greenwich.todo.repository.TodolistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TodolistUtilService {

    private final TodolistRepository todolistRepository;

    public TodolistUtilService(TodolistRepository todolistRepository) {
        this.todolistRepository = todolistRepository;
    }

    public Todolist findTodolistObjById(Long id) {
        try {
            Todolist todo = todolistRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Todolist not found with id: " + id));
            if(todo != null) {
                log.info("Todolist found with id: {}", id);
            }
            return todo;
        } catch (Exception e) {
            log.error("Error while finding todolist by id: {}", e.getMessage());
            throw e; //throw cho controller xử lí hoặc globalhandler xử lí
        }
    }


}
