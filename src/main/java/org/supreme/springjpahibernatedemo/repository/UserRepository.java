package org.supreme.springjpahibernatedemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.supreme.springjpahibernatedemo.models.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {}
