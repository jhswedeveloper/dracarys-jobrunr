package io.github.junhuhdev.dracarys.jobrunr.examples.jpa.entity;

import io.github.junhuhdev.dracarys.jobrunr.examples.component.account.Amount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "Account")
public class BankAccountEntity {

	@Id
	@Column(name = "EMAIL")
	private String email;

	@Column(name = "AMOUNT")
	private BigDecimal amount;
}
