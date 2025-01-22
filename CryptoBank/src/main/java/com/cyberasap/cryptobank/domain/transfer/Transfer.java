package com.cyberasap.cryptobank.domain.transfer;

import com.cyberasap.cryptobank.domain.bankaccount.BankAccount;
import com.cyberasap.cryptobank.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="transfer_table")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transfer_table_generator")
    @SequenceGenerator(name = "transfer_table_generator", sequenceName = "transfer_table_seq")
    private Long id;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private Long timestamp;

    @Column(nullable = false)
    private Boolean isApproved;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="sender_bank_account_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BankAccount senderBankAccount;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="receiver_bank_account_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BankAccount receiverBankAccount;
}
