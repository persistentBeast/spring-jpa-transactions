package org.supreme.springjpahibernatedemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.supreme.springjpahibernatedemo.dao.UserDao;
import org.supreme.springjpahibernatedemo.models.entities.User;
import org.supreme.springjpahibernatedemo.service.UserdService;

@RestController
@Slf4j
public class UserController {

  @Autowired public UserdService userdService;

  @Autowired public UserDao userDao;

  @PostMapping("/add/users")
  public ResponseEntity<String> add() {
    userdService.processUsersFromCSV();
    return ResponseEntity.ok("Done");
  }

  @GetMapping("/get/users/{id}")
  public ResponseEntity<User> add(@PathVariable Integer id) {
    return ResponseEntity.ok(userDao.getUser(id));
  }
}
