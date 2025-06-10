package com.greenwich.todo.service;

import com.greenwich.todo.dto.request.TodolistRequestDTO;
import com.greenwich.todo.dto.request.TodolistUpdateDTO;
import com.greenwich.todo.dto.response.PageResponseDTO;
import com.greenwich.todo.dto.response.TodolistResponseDTO;
import com.greenwich.todo.entity.Todolist;

public interface TodolistServiceI {

//    Todolist findTodolistObjById(Long id);
    TodolistResponseDTO findTodolistById(Long id);
    TodolistResponseDTO updatePartialTodolist(Long id, TodolistUpdateDTO todolist);
    TodolistResponseDTO createTodolist(TodolistRequestDTO todolist);
    TodolistResponseDTO updateTodolist(Long id, TodolistRequestDTO todolist);
    boolean deleteTodolist(Long id);
    TodolistResponseDTO[] getAllTodolistsByUserId(Long userId);
    TodolistResponseDTO[] getAllTodolists();
    PageResponseDTO<TodolistResponseDTO> getAllTodoListsPaginated(int page, int size);
    TodolistResponseDTO[] getAllTodolistsByTitle(String title);
}
