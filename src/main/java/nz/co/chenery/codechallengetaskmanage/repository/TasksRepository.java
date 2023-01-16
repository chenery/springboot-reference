package nz.co.chenery.codechallengetaskmanage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TasksRepository extends JpaRepository<PersistedTask, Long> {

    @Query("SELECT t FROM PersistedTask t WHERE t.dueDate < :now")
    List<PersistedTask> findOverdueTasks(LocalDate now);
}
