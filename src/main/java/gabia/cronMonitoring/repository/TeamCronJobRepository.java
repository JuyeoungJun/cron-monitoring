package gabia.cronMonitoring.repository;

import gabia.cronMonitoring.entity.TeamCronJob;
import gabia.cronMonitoring.entity.UserCronJob;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamCronJobRepository extends JpaRepository<TeamCronJob, Long> {

    List<TeamCronJob> findByTeamAccount(String account);

    TeamCronJob save(TeamCronJob teamCronJob);

    void deleteByCronJobIdAndTeamAccount(UUID cronJobId, String account);

    Optional<TeamCronJob> findByTeamAccountAndCronJobId(String account, UUID CronJobId);

    Page<TeamCronJob> findByTeamAccount(String account, Pageable pageable);
}
