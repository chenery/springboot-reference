package nz.co.chenery.codechallengetaskmanage.service;

import jakarta.persistence.EntityNotFoundException;
import nz.co.chenery.codechallengetaskmanage.api.dto.TaskResponse;
import nz.co.chenery.codechallengetaskmanage.api.dto.TaskUpsertRequest;
import nz.co.chenery.codechallengetaskmanage.exception.TaskNotFoundException;
import nz.co.chenery.codechallengetaskmanage.repository.PersistedTask;
import nz.co.chenery.codechallengetaskmanage.repository.TasksRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TasksService {

    private final TasksRepository tasksRepository;

    public TasksService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    public List<TaskResponse> findAllTasks() {
        return tasksRepository.findAll().stream()
                .map(toTaskDtoMapper)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> findOverDueTasks(LocalDate now) {
        return tasksRepository.findOverdueTasks(now).stream()
                .map(toTaskDtoMapper)
                .collect(Collectors.toList());
    }

    public TaskResponse findTaskById(Long id) {
        try {
            return toTaskDtoMapper.apply(tasksRepository.getReferenceById(id));
        } catch (EntityNotFoundException e) {
            throw new TaskNotFoundException();
        }
    }

    public void deleteTaskById(Long id) {
        try {
            tasksRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new TaskNotFoundException();
        }
    }

    public TaskResponse createTask(TaskUpsertRequest taskCreateRequest) {
        // todo Maybe we should check due date is not in the past?
        PersistedTask persistedTask = tasksRepository.save(PersistedTask.builder()
                .title(taskCreateRequest.getTitle())
                .description(taskCreateRequest.getDescription())
                .dueDate(taskCreateRequest.getDueDate())
                .creationDate(LocalDate.now())
                .status(taskCreateRequest.getStatus()).build());

        return toTaskDtoMapper.apply(persistedTask);
    }

    public TaskResponse updateTask(Long taskId, TaskUpsertRequest taskUpdateRequest) {
        PersistedTask existingTask = tasksRepository.getReferenceById(taskId);
        existingTask.setTitle(taskUpdateRequest.getTitle());
        existingTask.setDescription(taskUpdateRequest.getDescription());
        existingTask.setStatus(taskUpdateRequest.getStatus());
        existingTask.setDueDate(taskUpdateRequest.getDueDate());

        PersistedTask persistedTask = tasksRepository.save(existingTask);

        return toTaskDtoMapper.apply(persistedTask);
    }

    // todo make testable component
    private final Function<PersistedTask, TaskResponse> toTaskDtoMapper = persistedTask -> TaskResponse.builder()
            .id(persistedTask.getId())
            .title(persistedTask.getTitle())
            .description(persistedTask.getDescription())
            .dueDate(persistedTask.getDueDate())
            .creationDate(persistedTask.getCreationDate())
            .status(persistedTask.getStatus()).build();
}
