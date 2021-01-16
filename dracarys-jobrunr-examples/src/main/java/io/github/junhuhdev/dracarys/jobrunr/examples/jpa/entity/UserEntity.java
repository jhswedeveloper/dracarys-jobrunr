package io.github.junhuhdev.dracarys.jobrunr.examples.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "USERS")
public class UserEntity {

	@Id
	@Column(name = "EMAIL")
	private String email;
	@Column(name = "PASSWORD")
	private String password;
	@Column(name = "FIRST_NAME")
	private String firstName;
	@Column(name = "LAST_NAME")
	private String lastName;

}
