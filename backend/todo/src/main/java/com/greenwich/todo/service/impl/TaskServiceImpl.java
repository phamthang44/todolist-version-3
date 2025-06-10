package com.greenwich.todo.service.impl;

import com.greenwich.todo.dto.request.TaskRequestDTO;
import com.greenwich.todo.dto.request.TaskUpdateDTO;
import com.greenwich.todo.dto.response.TaskResponseDTO;
import com.greenwich.todo.entity.Task;
import com.greenwich.todo.entity.Todolist;
import com.greenwich.todo.exception.ResourceNotFoundException;
import com.greenwich.todo.repository.TaskRepository;
import com.greenwich.todo.service.TaskServiceI;
import com.greenwich.todo.service.TodolistUtilService;
import com.greenwich.todo.util.Priority;
import com.greenwich.todo.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class TaskServiceImpl implements TaskServiceI {

    private final TaskRepository taskRepository;

    @Lazy
    private final TodolistUtilService todolistService;
    
    // Spring tự động inject repository khi tạo bean service
    public TaskServiceImpl(TaskRepository taskRepository, TodolistUtilService todolistService) {
        this.taskRepository = taskRepository;
        this.todolistService = todolistService;
    }


    @Override
    @Transactional
    public TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO) {
        Task newTask = convertToTask(taskRequestDTO);
        Task savedTask = taskRepository.save(newTask);
        return convertToTaskResponseDTO(savedTask);
    }

    @Override
    @Transactional
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequestDTO) {
        Task task = findTaskById(id);

        task.setTitle(taskRequestDTO.getTitle());
        task.setDescription(taskRequestDTO.getDescription());
        task.setStatus(taskRequestDTO.getStatus());
        task.setPriority(taskRequestDTO.getPriority());
        task.setTodolist(todolistService.findTodolistObjById(taskRequestDTO.getTodolistId())); // nếu cần

        Task updatedTask = taskRepository.save(task);
        Task savedTask = taskRepository.save(updatedTask);
        // đầu tiên kiểm tra xem task có tồn tại không, nếu không thì ném ra lỗi
        //tiếp theo là convert từ requestDTO -> entity Task
        //save nó vô db lưu = biến task khác
        //sau đó response như bình thường
        return convertToTaskResponseDTO(savedTask);
    }

    @Override
    @Transactional
    public TaskResponseDTO updateTaskPartial(Long id, TaskUpdateDTO taskUpdateDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (taskUpdateDTO.getTitle() != null) {
            task.setTitle(taskUpdateDTO.getTitle());
        }
        if (taskUpdateDTO.getDescription() != null) {
            task.setDescription(taskUpdateDTO.getDescription());
        }
        if (taskUpdateDTO.getStatus() != null) {
            task.setStatus(taskUpdateDTO.getStatus());
        }
        if (taskUpdateDTO.getPriority() != null) {
            task.setPriority(taskUpdateDTO.getPriority());
        }
        if (taskUpdateDTO.getTodolistId() != null) {
            task.setTodolist(this.todolistService.findTodolistObjById(taskUpdateDTO.getTodolistId()));
        }
        Task saved = taskRepository.save(task);
        return convertToTaskResponseDTO(saved);
    }

    @Override
    public TaskResponseDTO getTaskById(Long id) {
        Task task = findTaskById(id);
        return convertToTaskResponseDTO(task);
    }

    @Override
    @Transactional
    public boolean deleteTask(Long id) {
        if(taskRepository.existsById(id)) {
//            throw new RuntimeException("Task not found with id: " + id);
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }


    @Override
    public TaskResponseDTO[] getAllTasksByTodolistId(Long todolistId) {
        return new TaskResponseDTO[0];
    }

    @Override
    public TaskResponseDTO[] getAllTasksByStatusAndPriority(Long todolistId, Status status, Priority priority) {
        return new TaskResponseDTO[0];
    }

    @Override
    public TaskResponseDTO[] getAllTasksByStatus(Long todolistId, Status status) {
        return new TaskResponseDTO[0];
    }

    @Override
    public TaskResponseDTO[] getAllTasksByPriority(Long todolistId, Priority priority) {
        return new TaskResponseDTO[0];
    }

    private Task convertToTask(TaskRequestDTO taskRequestDTO) {
        Task task = new Task();
        task.setTitle(taskRequestDTO.getTitle());
        task.setDescription(taskRequestDTO.getDescription());
        task.setStatus(taskRequestDTO.getStatus());
        task.setPriority(taskRequestDTO.getPriority());
        task.setTodolist(todolistService.findTodolistObjById(taskRequestDTO.getTodolistId()));
        return task;
    }

    private TaskResponseDTO convertToTaskResponseDTO(Task task) {
        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .todoListId(task.getTodolist().getId())
                .build();
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }
}
