package org.supreme.springjpahibernatedemo.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;

import lombok.*;

@Entity
@Getter
@Table(name = "receipts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class PaymentReceipt {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "sender_acc_id")
  private Long senderAccountId;

  @Column(name = "receiver_acc_id")
  private Long receiverAccountId;

  @Column(name = "amount")
  private Long amount;

  @Column(name = "status")
  private String status;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @Column(name = "updated_at")
  private Timestamp updatedAt;
}
