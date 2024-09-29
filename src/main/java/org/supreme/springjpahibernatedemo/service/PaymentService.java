package org.supreme.springjpahibernatedemo.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.supreme.springjpahibernatedemo.models.entities.Account;
import org.supreme.springjpahibernatedemo.models.entities.PaymentReceipt;
import org.supreme.springjpahibernatedemo.repository.AccountsRepository;
import org.supreme.springjpahibernatedemo.repository.PaymentReceiptRepository;

@Slf4j
@Service
public class PaymentService {

  @Autowired AccountsRepository accountsRepository;
  @Autowired PaymentReceiptRepository paymentReceiptRepository;

  @Autowired
  private EntityManager entityManager;

  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void transfer(Long fromAccount, Long toAccount, Long amt, PaymentReceipt paymentReceipt) {

    Optional<Account> account = accountsRepository.findById(toAccount);
    Long initAmt = Long.valueOf(account.get().getBalance());

    entityManager.refresh(account.get());
    Optional<Account> account2 = accountsRepository.findById(toAccount);

    if(!initAmt.equals(account2.get().getBalance())){
      log.error("Non Repeatable read detected!, v1 : {}, v2 : {}", initAmt, account2.get().getBalance());
    }

    if (account.isPresent()) {
      Account acc = account.get();
      acc.setBalance(acc.getBalance() + amt);
      acc.setUpdatedAt(Timestamp.from(Instant.now()));
      accountsRepository.save(acc);
    }

//    Optional<Account> account2 = accountsRepository.findById(toAccount);
//
//    if((initAmt + amt) != account2.get().getBalance()){
//      log.error("Non Repeatable read detected!");
//    }

    paymentReceipt.setStatus("SUCCESS");
    addReceipt(paymentReceipt);
  }

  public void addReceipt(PaymentReceipt paymentReceipt) {
    paymentReceipt.setUpdatedAt(Timestamp.from(Instant.now()));
    paymentReceiptRepository.save(paymentReceipt);
  }
}
