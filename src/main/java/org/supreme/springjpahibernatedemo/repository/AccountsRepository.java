package org.supreme.springjpahibernatedemo.repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.supreme.springjpahibernatedemo.models.entities.Account;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {

//  @Override
//  @Lock(LockModeType.PESSIMISTIC_WRITE)
//  Optional<Account> findById(Long aLong);


}
