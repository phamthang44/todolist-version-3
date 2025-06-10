package com.greenwich.todo.service.impl;

import com.greenwich.todo.dto.request.TodolistRequestDTO;
import com.greenwich.todo.dto.request.TodolistUpdateDTO;
import com.greenwich.todo.dto.response.PageResponseDTO;
import com.greenwich.todo.dto.response.TaskResponseDTO;
import com.greenwich.todo.dto.response.TodolistResponseDTO;
import com.greenwich.todo.entity.Task;
import com.greenwich.todo.entity.Todolist;
import com.greenwich.todo.exception.ResourceNotFoundException;
import com.greenwich.todo.repository.TodolistRepository;
import com.greenwich.todo.service.TodolistServiceI;
import com.greenwich.todo.service.UserUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class TodolistServiceImpl implements TodolistServiceI {

    private final TodolistRepository todolistRepository;
    private final UserUtilService userUtilService;

    public TodolistServiceImpl(TodolistRepository todolistRepository, UserUtilService userUtilService) {
        this.todolistRepository = todolistRepository;
        this.userUtilService = userUtilService;
    }


    /*
     *
     *  this function return Todolist object by id
     *  using internal layer
     * */
    public Todolist findTodolistObjById(Long id) {
        try {
            Todolist todo = todolistRepository.findById(id)
                    //tạm thời Runtime thôi
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

    @Override
    /*
    *
    *  this function return TodolistResponseDTO object by id
    *  using in controller layer
    * */
    public TodolistResponseDTO findTodolistById(Long id) {
        try {
            Todolist todolist = findTodolistObjById(id);
            if (todolist == null) {
                log.error("Todolist not found with id : {}", id);
                throw new ResourceNotFoundException("Todolist not found with id : " + id);
            }
            List<Task> tasks = todolist.getTasks();
            List<TaskResponseDTO> taskResponseDTO = convertTasksToResponseDTO(tasks);

            return TodolistResponseDTO.fromEntity(todolist, taskResponseDTO);
        } catch (Exception e) {
            // Log the exception or handle it as needed
            log.error("Error while finding todolist: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public TodolistResponseDTO createTodolist(TodolistRequestDTO todolist) {
        try {
            if (todolist == null) {
                throw new IllegalArgumentException("Todolist request data cannot be null");
            }
            Todolist newTodolist = new Todolist();

            newTodolist.setTitle(todolist.getTitle());
            newTodolist.setDescription(todolist.getDescription());
            newTodolist.setUser(userUtilService.findUserById(todolist.getUserId()));
            List<Task> tasks = newTodolist.getTasks();
            if (tasks.isEmpty()) {
                log.warn("No tasks found in the Todolist request data");
            }

            return TodolistResponseDTO.fromEntity(
                    todolistRepository.save(newTodolist),
                    convertTasksToResponseDTO(newTodolist.getTasks())
            );
        } catch (Exception e) {
            log.error("Todolist request data is null while creating new Todolist: {}", e.getMessage());
            throw e; //throw cho controller xử lí hoặc globalhandler xử lí
        }
    }

    @Override
    @Transactional
    public TodolistResponseDTO updatePartialTodolist(Long id, TodolistUpdateDTO todolist) {
        try {
            if (todolist == null) {
                throw new IllegalArgumentException("Todolist request data cannot be null");
            }
            if(Objects.equals(todolist.getTitle(), "")) {
                throw new IllegalArgumentException("Todolist title cannot be empty");
            }
            Todolist existingTodolist = findTodolistObjById(id);
            if (existingTodolist == null) {
                log.error("Todolist not found with id: {}", id);
                throw new ResourceNotFoundException("Todolist not found with id: " + id);
            }

            existingTodolist.setTitle(todolist.getTitle());
            existingTodolist.setDescription(todolist.getDescription());

            return TodolistResponseDTO.fromEntity(
                    todolistRepository.save(existingTodolist),
                    convertTasksToResponseDTO(existingTodolist.getTasks())
            );
        } catch (Exception e) {
            log.error("Error while updating todolist: {}", e.getMessage());
            throw e; //throw cho controller xử lí hoặc globalhandler xử lí
        }
    }

    @Override
    @Transactional
    public TodolistResponseDTO updateTodolist(Long id, TodolistRequestDTO todolist) {
        try {
            if (todolist == null) {
                throw new IllegalArgumentException("Todolist request data cannot be null");
            }
            Todolist existingTodolist = findTodolistObjById(id);
            if (existingTodolist == null) {
                log.error("Todolist not found with id: {}", id);
                throw new ResourceNotFoundException("Todolist not found with id: " + id);
            }

            existingTodolist.setTitle(todolist.getTitle());
            existingTodolist.setDescription(todolist.getDescription());

            return TodolistResponseDTO.fromEntity(
                    todolistRepository.save(existingTodolist),
                    convertTasksToResponseDTO(existingTodolist.getTasks())
            );
        } catch (Exception e) {
            log.error("Error while updating todolist: {}", e.getMessage());
            throw e; //throw cho controller xử lí hoặc globalhandler xử lí
        }
    }

    @Override
    @Transactional
    public boolean deleteTodolist(Long id) {
        try {
            Todolist existingTodolist = findTodolistObjById(id);
            if (existingTodolist == null) {
                log.error("Todolist not found with id: {}", id);
                throw new ResourceNotFoundException("Todolist not found with id: " + id);
            }
            todolistRepository.delete(existingTodolist);
            log.info("Todolist deleted successfully with id : {}", id);
            return true;
        } catch (Exception e) {
            log.error("Error while deleting todolist: {}", e.getMessage());
            throw e; //throw cho controller xử lí hoặc globalhandler xử lí
        }
    }

    @Override
    public TodolistResponseDTO[] getAllTodolistsByUserId(Long userId) {
        return new TodolistResponseDTO[0];
    }

    @Override
    public PageResponseDTO<TodolistResponseDTO> getAllTodoListsPaginated(int pageNo, int pageSize) {
        log.info("Retrieving all todoLists with pagination: pageNo={}, pageSize={}", pageNo, pageSize);

        if (pageNo < 1 || pageSize < 1) {
            log.error("Invalid pagination parameters: pageNo={}, pageSize={}", pageNo, pageSize);
            throw new IllegalArgumentException("Page number and size must be greater than 0");
        }
        List<Todolist> todoLists = todolistRepository.findAll();
        // Spring page index bắt đầu từ 0, trong khi ta nhận 1-based
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize,
                Sort.by("createdAt").descending());

        Page<Todolist> page = todolistRepository.findAll(pageable);

        List<TodolistResponseDTO> pageDto = todoLists
                .stream()
                .map(todolist -> TodolistResponseDTO.fromEntity(
                        todolist,
                        convertTasksToResponseDTO(todolist.getTasks())
                ))
                .toList();

        return new PageResponseDTO<>(
                pageDto,
                page.getNumber() + 1,      // đổi lại về 1-based
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast());
    }

    @Override
    public TodolistResponseDTO[] getAllTodolists() {
        try {
                List<Todolist> todoLists = todolistRepository.findAll();
                if (todoLists.isEmpty()) {
                    log.warn("No todoLists found");
                    return new TodolistResponseDTO[0];
                }
                return todoLists.stream()
                        .map(todolist -> TodolistResponseDTO.fromEntity(
                                todolist,
                                convertTasksToResponseDTO(todolist.getTasks())
                        ))
                        .toArray(TodolistResponseDTO[]::new);
        } catch (Exception e) {
            log.error("Error while retrieving all todolists: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public TodolistResponseDTO[] getAllTodolistsByTitle(String title) {
        return new TodolistResponseDTO[0];
    }

    private List<TaskResponseDTO> convertTasksToResponseDTO(List<Task> tasks) {
        return tasks.stream()
                .map(TaskResponseDTO::from)
                .toList();
    }


}
