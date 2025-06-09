package com.greenwich.todo.repository;

import com.greenwich.todo.entity.Todolist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodolistRepository extends JpaRepository<Todolist, Long> {
}
