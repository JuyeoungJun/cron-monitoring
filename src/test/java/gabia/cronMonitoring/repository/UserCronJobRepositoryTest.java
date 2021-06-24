package gabia.cronMonitoring.repository;

import gabia.cronMonitoring.entity.CronJob;
import gabia.cronMonitoring.entity.CronServer;
import gabia.cronMonitoring.entity.User;
import gabia.cronMonitoring.entity.UserCronJob;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserCronJobRepositoryTest {

    @Autowired
    UserCronJobRepository userCronJobRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CronJobRepositoryDataJpa cronJobRepository;

    @Autowired
    CronServerRepository cronServerRepository;

    @Test
    public void findAllByUser_Id() {

        CronServer cronServer = new CronServer("0.0.0.0");

        CronServer cronServer1 = cronServerRepository.save(cronServer);

        User user = new User();
        user.setAccount("test");
        user.setEmail("test");
        user.setName("test");
        user.setPassword("test");

        CronJob cronJob = new CronJob();
        cronJob.setServer(cronServer1);
        cronJob.setCronExpr("test");
        cronJob.setCronName("test");

        CronJob cronJob2 = new CronJob();
        cronJob2.setServer(cronServer1);
        cronJob2.setCronExpr("test2");
        cronJob2.setCronName("test2");

        User savedUser = userRepository.save(user);
        CronJob savedCronJob1 = cronJobRepository.save(cronJob);
        CronJob savedCronJob2 = cronJobRepository.save(cronJob2);

        UserCronJob userCronJob1 = UserCronJob.builder()
            .user(savedUser)
            .cronJob(savedCronJob1)
            .build();

        UserCronJob userCronJob2 = UserCronJob.builder()
            .user(savedUser)
            .cronJob(savedCronJob2)
            .build();

        UserCronJob savedUserCronJob1 = userCronJobRepository.save(userCronJob1);
        UserCronJob savedUserCronJob2 = userCronJobRepository.save(userCronJob2);

        List<UserCronJob> all = userCronJobRepository.findByUserAccount("test");

        Assertions.assertEquals(savedUserCronJob1.getUser().getAccount(),
            all.get(0).getUser().getAccount());
        Assertions.assertEquals(savedUserCronJob1.getCronJob().getId(),
            all.get(0).getCronJob().getId());
        Assertions.assertEquals(savedUserCronJob2.getUser().getAccount(),
            all.get(1).getUser().getAccount());
        Assertions.assertEquals(savedUserCronJob2.getCronJob().getId(),
            all.get(1).getCronJob().getId());



    }
}