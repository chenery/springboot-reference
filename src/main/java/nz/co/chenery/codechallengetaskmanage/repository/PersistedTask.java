package nz.co.chenery.codechallengetaskmanage.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Entity
@Table(name = "tasks")
public class PersistedTask {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    Long id;
    @Column(length = 256, nullable = false)
    String title;
    @Column(length = 1024)
    String description;
    @Column(name = "due_date")
    @Temporal(TemporalType.DATE)
    LocalDate dueDate;
    @Column(name = "creation_date", nullable = false)
    @Temporal(TemporalType.DATE)
    LocalDate creationDate;
    // better as Enum
    @Column(length = 10)
    String status;
}
