package nz.co.chenery.codechallengetaskmanage.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class TaskUpsertRequest {

    @NotNull
    @Size(min = 1, max = 256)
    String title;
    @Size(max = 1024)
    String description;
    LocalDate dueDate;
    // better as Enum
    @Size(max = 10)
    String status;
}
