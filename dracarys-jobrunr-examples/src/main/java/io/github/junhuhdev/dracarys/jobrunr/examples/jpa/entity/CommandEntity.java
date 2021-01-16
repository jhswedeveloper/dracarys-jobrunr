package io.github.junhuhdev.dracarys.jobrunr.examples.jpa.entity;

import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "COMMAND")
public class CommandEntity  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	@Column(name = "COMMAND_CLASS")
	private String commandClass;
	@Column(name = "COMMAND")
	private String command;
	@Column(name = "CREATED")
	@Builder.Default
	private LocalDateTime created = LocalDateTime.now();

}
