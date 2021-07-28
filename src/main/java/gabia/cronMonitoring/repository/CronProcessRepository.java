package gabia.cronMonitoring.repository;

import gabia.cronMonitoring.entity.CronProcess;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CronProcessRepository extends JpaRepository<CronProcess, Long> {

    List<CronProcess> findAllByCronJob_Id(UUID cronJobId);

    Optional<CronProcess> findByPid(String pid);

    CronProcess save(CronProcess cronProcess);

    Page<CronProcess> findAllByCronJob_Id(UUID cronJobId, Pageable pageable);

}
