package com.greenwich.todo.repository;

import com.greenwich.todo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//cần định nghĩa object là gì, id của nó thuộc lọ gì

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
