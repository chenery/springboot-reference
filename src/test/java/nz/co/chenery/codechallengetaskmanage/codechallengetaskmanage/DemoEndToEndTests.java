package nz.co.chenery.codechallengetaskmanage.codechallengetaskmanage;

import nz.co.chenery.codechallengetaskmanage.api.dto.TaskResponse;
import nz.co.chenery.codechallengetaskmanage.api.dto.TaskUpsertRequest;
import nz.co.chenery.codechallengetaskmanage.repository.TasksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *  End-to-end tests that show the HAPPY PATH behaviour of the API
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DemoEndToEndTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private TasksRepository tasksRepository;

    @BeforeEach
    void cleanData() {
        tasksRepository.deleteAll();
    }

    @Test
    @Order(1)
    void getTasks_providesAllResults() {
        createTask("One");
        createTask("Two");

        webTestClient.get().uri("/api/tasks")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TaskResponse.class).hasSize(2);
    }

    @Test
    @Order(2)
    void getTaskById_providesSingleResult() {
        TaskResponse taskResponse1 = createTask("One");
        TaskResponse taskResponse2 = createTask("Two");

        webTestClient.get().uri(String.format("/api/tasks/%d", taskResponse1.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class).isEqualTo(taskResponse1);

        webTestClient.get().uri(String.format("/api/tasks/%d", taskResponse2.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class).isEqualTo(taskResponse2);
    }

    @Test
    @Order(3)
    void deleteTaskById_givenTaskExists() {
        TaskResponse taskResponse1 = createTask("One");

        webTestClient.delete().uri(String.format("/api/tasks/%d", taskResponse1.getId()))
                .exchange()
                .expectStatus().isOk();

        // then the task is deleted and no longer accessible via the API
        webTestClient.get().uri(String.format("/api/tasks/%d", taskResponse1.getId()))
                .exchange()
                .expectStatus().isNotFound();

        // and the task is deleted and cannot be deleted twice
        webTestClient.delete().uri(String.format("/api/tasks/%d", taskResponse1.getId()))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(4)
    void updatedTaskById_givenTaskExists() {
        TaskResponse taskResponse1 = createTask("One");

        TaskUpsertRequest updateRequest = TaskUpsertRequest.builder()
                .title("title-updated")
                .description("desc-updated")
                .status("s-updated")
                .dueDate(LocalDate.now().plusDays(7))
                .build();

        webTestClient.put().uri(String.format("/api/tasks/%d", taskResponse1.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(taskResponse1.getId())
                .jsonPath("$.title").isEqualTo("title-updated")
                .jsonPath("$.description").isEqualTo("desc-updated")
                .jsonPath("$.status").isEqualTo("s-updated")
                .jsonPath("$.dueDate").isEqualTo(LocalDate.now().plusDays(7).toString())
                .jsonPath("$.creationDate").isEqualTo(LocalDate.now().toString());
    }

    @Test
    @Order(5)
    void fetchAllOverdueTasks() {
        TaskResponse taskResponse1NotOverDue = createTask("One");
        TaskResponse taskResponse2OverDue = createTask("Two", LocalDate.now().minusDays(1));
        TaskResponse taskResponse3OverDue = createTask("3", LocalDate.now().minusDays(2));
        TaskResponse taskResponse4NotOverDue = createTask("4");

        webTestClient.get().uri("/api/tasks?overdue=true")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TaskResponse.class).hasSize(2).contains(taskResponse2OverDue, taskResponse3OverDue);
    }

    private TaskResponse createTask(String name) {
        LocalDate defaultDueDate = LocalDate.now().plusDays(3);
        return createTask(name, defaultDueDate);
    }

    private TaskResponse createTask(String name, LocalDate dueDate) {
        TaskUpsertRequest taskResponseRequest = TaskUpsertRequest.builder()
                .title(String.format("title%s", name))
                .description(String.format("desc%s", name))
                .dueDate(dueDate)
                .status(String.format("status%s", name)).build();

        TaskResponse createdTaskResponse = webTestClient.post().uri("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(taskResponseRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TaskResponse.class)
                .returnResult().getResponseBody();

        assertNotNull(createdTaskResponse);
        assertTrue(createdTaskResponse.getId() > 0);
        assertEquals(String.format("title%s", name), createdTaskResponse.getTitle());
        assertEquals(String.format("desc%s", name), createdTaskResponse.getDescription());
        assertEquals(String.format("status%s", name), createdTaskResponse.getStatus());
        assertEquals(LocalDate.now().toString(), createdTaskResponse.getCreationDate().toString());
        assertEquals(dueDate.toString(), createdTaskResponse.getDueDate().toString());

        return createdTaskResponse;
    }
}


