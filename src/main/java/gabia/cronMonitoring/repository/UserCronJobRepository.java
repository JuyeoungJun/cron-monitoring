package gabia.cronMonitoring.repository;

import gabia.cronMonitoring.entity.UserCronJob;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCronJobRepository extends JpaRepository<UserCronJob, Long> {

    List<UserCronJob> findByUserAccount(String account);

    void deleteByCronJobIdAndUserAccount(UUID cronJobId, String account);

    UserCronJob save(UserCronJob userCronJob);

    Optional<UserCronJob> findByUserAccountAndCronJobId(String account, UUID cronJobId);

    Page<UserCronJob> findByUserAccount(String account, Pageable pageable);
}
