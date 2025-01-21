package com.cyberasap.cryptobank.domain.bankaccount;

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
@Table(name="bank_account_table")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_account_table_generator")
    @SequenceGenerator(name = "bank_account_table_generator", sequenceName = "bank_account_table_seq")
    private Long id;

    @Column(nullable = false, length = 34)
    private String iban;

    @Column(nullable = false)
    private Long creationTime;

    @Column(nullable = false)
    private Integer amount;

    @Lob
    @Column(nullable = false, length = 392)
    private String publicKey;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
