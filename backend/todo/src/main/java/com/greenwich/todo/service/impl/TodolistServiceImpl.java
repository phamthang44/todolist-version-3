package com.greenwich.todo.service.impl;

import com.greenwich.todo.entity.Todolist;
import com.greenwich.todo.repository.TodolistRepository;
import com.greenwich.todo.service.TodolistServiceI;
import org.springframework.stereotype.Service;

@Service
public class TodolistServiceImpl implements TodolistServiceI {
    private final TodolistRepository todolistRepository;

    public TodolistServiceImpl(TodolistRepository todolistRepository) {
        this.todolistRepository = todolistRepository;
    }

    @Override
    public Todolist findTodolistById(Long id) {
        return todolistRepository.findById(id)
                //tạm thời Runtime thôi
                .orElseThrow(() -> new RuntimeException("Todolist not found with id: " + id));
    }


}
