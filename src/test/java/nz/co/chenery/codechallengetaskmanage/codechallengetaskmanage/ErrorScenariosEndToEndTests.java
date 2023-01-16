package nz.co.chenery.codechallengetaskmanage.codechallengetaskmanage;

import nz.co.chenery.codechallengetaskmanage.api.dto.TaskUpsertRequest;
import nz.co.chenery.codechallengetaskmanage.repository.TasksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

/**
 *  End-to-end tests that show the ERROR behaviour of the API
 *  todo consider to move these test to controller unit tests with mock Spring MVC
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ErrorScenariosEndToEndTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private TasksRepository tasksRepository;

    @BeforeEach
    void cleanData() {
        tasksRepository.deleteAll();
    }

    @Test
    void createInvalidTask_returns4xxError_invalidStatus() {
        TaskUpsertRequest createRequest = TaskUpsertRequest.builder()
                .title("title-updated")
                .description("desc-updated")
                .status("status-that-is-too-long")
                .dueDate(LocalDate.now().plusDays(7))
                .build();

        webTestClient.post().uri("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createRequest)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void createInvalidTask_returns4xxError_missingTitle() {
        TaskUpsertRequest createRequest = TaskUpsertRequest.builder().build();

        webTestClient.post().uri("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createRequest)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void getTask_returns4xxError_taskNotFound() {
        webTestClient.get().uri("/api/tasks/2342342")
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void deleteTask_returns4xxError_taskNotFound() {
        webTestClient.delete().uri("/api/tasks/2342342")
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void getTasks_returns4xxError_invalidParam() {
        webTestClient.get().uri("/api/tasks?overdue=somethingWrong")
                .exchange()
                .expectStatus().is4xxClientError();
    }
}


