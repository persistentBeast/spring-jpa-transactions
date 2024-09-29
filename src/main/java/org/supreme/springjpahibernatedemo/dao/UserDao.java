package org.supreme.springjpahibernatedemo.dao;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.supreme.springjpahibernatedemo.models.entities.User;
import org.supreme.springjpahibernatedemo.repository.UserRepository;

@Repository
@Slf4j
public class UserDao {

  @Autowired private UserRepository userRepository;

  @Transactional
  @Retryable(
          retryFor = PessimisticLockingFailureException.class,
          backoff = @Backoff(
                  value = 200,
                  maxDelay = 2000,
                  multiplier = 3
          ),
          maxAttempts = 3,
          recover = "recoverfunc"
  )
  public void saveAllUser(List<User> userList) {
    userRepository.saveAll(userList);
  }

  @Transactional
  public User getUser(Integer id){
    return userRepository.findById(id).get();
  }

  @Recover
  private void recoverfunc(Exception e, List<User> userList){
    log.error("[recoverfunc]--------------------------------------------------------DEADLOCK--------------------------------------------------------");
  }
}
