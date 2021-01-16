package io.github.junhuhdev.dracarys.pipeline.chain;

import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.cmd.ExceptionCmd;
import io.github.junhuhdev.dracarys.pipeline.cmd.FinalizeCmd;
import io.github.junhuhdev.dracarys.pipeline.cmd.LockCmd;
import io.github.junhuhdev.dracarys.pipeline.cmd.SaveAsLastCmd;
import io.github.junhuhdev.dracarys.pipeline.cmd.SuccessfulCmd;
import io.github.junhuhdev.dracarys.pipeline.common.Conditional;
import io.github.junhuhdev.dracarys.pipeline.event.EventTransaction;
import io.github.junhuhdev.dracarys.pipeline.jdbc.EventJdbcRepository;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * One chain per event to process
 */
public abstract class ChainBase implements Chainable, Conditional {

	@Resource
	private ListableBeanFactory beanFactory;
	@Autowired
	private PreChain preChain;
	@Autowired
	private PostChain postChain;
	@Autowired
	private EventJdbcRepository eventJdbcRepository;

	protected abstract List<Class<? extends Command>> getCommands();

	private ListIterator<Command> createCommands() {
		List<Command> commands = new ArrayList<>();
		addCommands(commands, preChain.getCommands());
		addCommands(commands, this.getCommands());
		addCommands(commands, postChain.getCommands());
		return commands.listIterator();
	}

	private void addCommands(List<Command> commands, List<Class<? extends Command>> listOfCmds) {
		for (var cmd : listOfCmds) {
			Command bean = (Command) beanFactory.getBean(cmd);
			commands.add(bean);
		}
	}

	@Override
	public ChainContext resume(String referenceId) throws Exception {
		var event = eventJdbcRepository.findByReferenceId(referenceId);
		return dispatch(event);
	}

	@Override
	public ChainContext dispatch(EventTransaction event) throws Exception {
		ListIterator<Command> commands = this.createCommands();
		Chain chain = new Chain(commands);
		return chain.proceed(new ChainContext(event));
	}

	@Component
	static class PreChain extends ChainBase {

		@Override
		protected List<Class<? extends Command>> getCommands() {
			return List.of(
					LockCmd.class,
					ExceptionCmd.class,
					SaveAsLastCmd.class);
		}

		@Override
		public boolean isMixable() {
			return false;
		}

	}

	@Component
	static class PostChain extends ChainBase {

		@Override
		protected List<Class<? extends Command>> getCommands() {
			return List.of(
					SuccessfulCmd.class,
					FinalizeCmd.class);
		}

		@Override
		public boolean isMixable() {
			return false;
		}

	}

}
