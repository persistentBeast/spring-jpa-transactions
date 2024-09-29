package org.supreme.springjpahibernatedemo.service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.supreme.springjpahibernatedemo.dao.UserDao;
import org.supreme.springjpahibernatedemo.models.entities.User;

@Slf4j
public class UserTask implements Callable<Boolean> {

  List<User> input;
  UserDao userDao;

  public UserTask(List<User> input, UserDao userDao) {
    this.input = input;
    this.userDao = userDao;
  }

  @Override
  public Boolean call() throws Exception {

    try {

      //      for(User user : input){
      //        User s = userRepository.findById(user.getId()).orElse(null);
      //      }

      userDao.saveAllUser(input);
      log.info(
          "Batch saved successfully : {}",
          input.stream().map(User::getId).collect(Collectors.toSet()));
    } catch (PessimisticLockingFailureException e) {
      log.error(
          "--------------------------------------------------------DEADLOCK--------------------------------------------------------");
      throw e;
    }
    return true;
  }
}
