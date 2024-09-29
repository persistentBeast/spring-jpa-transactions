package org.supreme.springjpahibernatedemo.controller;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.supreme.springjpahibernatedemo.models.entities.PaymentReceipt;
import org.supreme.springjpahibernatedemo.service.PaymentService;

@RestController
@Slf4j
public class PaymentController {

  @Autowired PaymentService paymentService;

  @PostMapping("/pay")
  public ResponseEntity<String> pay(
      @RequestParam(name = "from_acc") Long fromAccount,
      @RequestParam(name = "to_acc") Long toAccount,
      @RequestParam(name = "amt") Long amt) {
    PaymentReceipt paymentReceipt =
        PaymentReceipt.builder()
            .id(UUID.randomUUID().toString())
            .status("WAITING")
            .createdAt(Timestamp.from(Instant.now()))
            .updatedAt(Timestamp.from(Instant.now()))
            .receiverAccountId(toAccount)
            .senderAccountId(fromAccount)
            .amount(amt)
            .build();
    try {
      paymentService.addReceipt(paymentReceipt);
      paymentService.transfer(fromAccount, toAccount, amt, paymentReceipt);

      return ResponseEntity.ok("Payment is success!");

    } catch (Exception e) {

      log.error("Error in payment, err : {}", e.getMessage());
      paymentReceipt.setStatus("FAILED");
      paymentService.addReceipt(paymentReceipt);

      return ResponseEntity.internalServerError().body("Payment is failed!");
    }
  }
}
