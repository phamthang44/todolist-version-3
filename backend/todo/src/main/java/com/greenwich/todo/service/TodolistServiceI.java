package com.greenwich.todo.service;

import com.greenwich.todo.entity.Todolist;

public interface TodolistServiceI {

    Todolist findTodolistById(Long id);
}
