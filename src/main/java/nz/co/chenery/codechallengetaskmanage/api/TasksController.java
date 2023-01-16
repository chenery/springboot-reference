package nz.co.chenery.codechallengetaskmanage.api;

import jakarta.validation.Valid;
import nz.co.chenery.codechallengetaskmanage.api.dto.TaskResponse;
import nz.co.chenery.codechallengetaskmanage.api.dto.TaskUpsertRequest;
import nz.co.chenery.codechallengetaskmanage.exception.TaskNotFoundException;
import nz.co.chenery.codechallengetaskmanage.service.TasksService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TasksController {

    private final TasksService tasksService;

    public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    // todo should support limit or pagination
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponse>> getTasks(@RequestParam(required = false) boolean overdue) {
        if (overdue) {
            return new ResponseEntity<>(tasksService.findOverDueTasks(LocalDate.now()), HttpStatus.OK);
        }
        return new ResponseEntity<>(tasksService.findAllTasks(), HttpStatus.OK);
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskResponse> postTask(@Valid @RequestBody TaskUpsertRequest taskCreateRequest) {
        return new ResponseEntity<>(tasksService.createTask(taskCreateRequest), HttpStatus.CREATED);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(tasksService.findTaskById(id), HttpStatus.OK);
        } catch (TaskNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot fetch Task. Task not found.", e);
        }
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTaskById(@PathVariable Long id) {
        try {
            tasksService.deleteTaskById(id);
            return ResponseEntity.ok().build();
        } catch (TaskNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot delete Task. Task not found", e);
        }
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskResponse> updateTaskById(@PathVariable Long id, @Valid @RequestBody TaskUpsertRequest taskUpsertRequest) {
        return new ResponseEntity<>(tasksService.updateTask(id, taskUpsertRequest), HttpStatus.OK);
    }
}
