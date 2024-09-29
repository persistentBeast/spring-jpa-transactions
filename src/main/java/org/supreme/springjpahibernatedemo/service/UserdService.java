package org.supreme.springjpahibernatedemo.service;

import com.opencsv.CSVReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.supreme.springjpahibernatedemo.dao.UserDao;
import org.supreme.springjpahibernatedemo.models.entities.User;

@Slf4j
@Service
public class UserdService {

  @Autowired private UserDao userDao;

  private final ExecutorService executorService = Executors.newFixedThreadPool(20);

  private static final int BATCH_SIZE = 10;

  public void processUsersFromCSV() {

    List<Future<Boolean>> tasks = new ArrayList<>();
    try (CSVReader reader =
        new CSVReader(new InputStreamReader(new ClassPathResource("users.csv").getInputStream()))) {
      List<User> users = new ArrayList<>();
      String[] line;
      reader.readNext(); // Skip header
      Random random = new Random();
      // Generate a random number between 1 and 100
      while ((line = reader.readNext()) != null) {
        User user = parseCsvLineToUser(line);
//        Timestamp t = user.getBirthday();
//        LocalDateTime localDateTime = t.toLocalDateTime();
//
//        int randomNumber = random.nextInt(100) + 1;
//
//        // Add 30 minutes
//        LocalDateTime newDateTime = localDateTime.plusMinutes(randomNumber);

        // Convert back to Timestamp
//        Timestamp newTimestamp = Timestamp.valueOf(newDateTime);
//        user.setBirthday(newTimestamp);
        users.add(user);

        if (users.size() == BATCH_SIZE) {
          UserTask task = new UserTask(users, userDao);
          Future<Boolean> result = executorService.submit(task);
          users = new ArrayList<>(); // Reset for next batch
          tasks.add(result);
        }
      }

      // Submit remaining users (if any)
      if (!users.isEmpty()) {
        UserTask task = new UserTask(users, userDao);
        Future<Boolean> result = executorService.submit(task);
        tasks.add(result);
      }

      for (Future<Boolean> f : tasks) {
        f.get();
      }

      log.info("All tasks completed");

    } catch (Exception e) {
      log.error("Error reading CSV file", e);
    }
  }

  private User parseCsvLineToUser(String[] line) {
    return User.builder()
        .id(Integer.valueOf(line[0]))
        .city(line[1])
        .rating(Double.parseDouble(line[2]))
        .birthday(Timestamp.valueOf(line[3])) // Assuming date in 'yyyy-MM-dd HH:mm:ss' format
        .build();
  }

  private void submitBatchToExecutor(List<User> users) {

    log.info("Submitted a batch of {} users for processing", users.size());
  }

  public void shutdownExecutorService() {
    executorService.shutdown();
    log.info("Executor service shut down");
  }
}
