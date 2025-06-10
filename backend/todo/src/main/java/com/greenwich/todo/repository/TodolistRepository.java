package com.greenwich.todo.repository;

import com.greenwich.todo.entity.Todolist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodolistRepository extends JpaRepository<Todolist, Long> {
}
