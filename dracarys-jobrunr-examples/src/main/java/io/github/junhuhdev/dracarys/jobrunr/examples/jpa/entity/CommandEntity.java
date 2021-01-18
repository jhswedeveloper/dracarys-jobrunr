package io.github.junhuhdev.dracarys.jobrunr.examples.jpa.entity;

import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.xstream.XStreamFactory;
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
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "COMMAND")
public class CommandEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "REFERENCE_ID")
	private String referenceId;

	@Column(name = "COMMAND_CLASS")
	private String commandClass;

	@Column(name = "COMMAND")
	private String command;

	@Column(name = "HISTORY")
	private String history;

	@Column(name = "CREATED")
	@Builder.Default
	private LocalDateTime created = LocalDateTime.now();

	@SuppressWarnings("unchecked")
	public List<Command> getHistory() {
		return (List<Command>) XStreamFactory.xstream().fromXML(history);
	}
}
