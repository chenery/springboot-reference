package nz.co.chenery.codechallengetaskmanage.api.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

/**
 *
 */
@Value
@Builder
public class TaskResponse {

    Long id;
    String title;
    String description;
    LocalDate dueDate;
    LocalDate creationDate;
    // better as Enum
    String status;
}
