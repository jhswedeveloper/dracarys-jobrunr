package io.github.junhuhdev.dracarys.pipeline.chain;

import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.cmd.CompletionCmd;
import io.github.junhuhdev.dracarys.pipeline.cmd.ExceptionHandlerCmd;
import io.github.junhuhdev.dracarys.pipeline.cmd.SaveAsLastCmd;
import io.github.junhuhdev.dracarys.pipeline.common.FirstGenericArgOf;
import io.github.junhuhdev.dracarys.pipeline.common.Voidy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 * One chain per event to process
 */
public abstract class ChainBase<REQUEST extends Command.Request> implements Chainable {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	@Resource
	private ListableBeanFactory beanFactory;
	@Autowired
	private ObjectProvider<Command.Middleware> middlewares;
	@Autowired
	private PreChain preChain;
	@Autowired
	private PostChain postChain;

	protected abstract List<Class<? extends Command>> getCommands();

	protected boolean matches(REQUEST request) {
		Class handlerType = getClass();
		Class commandType = request.getClass();
		return new FirstGenericArgOf(handlerType).isAssignableFrom(commandType);
	}

	@Override
	public ChainContext dispatch(Command.Request command) throws Exception {
		ListIterator<Command.Handler> commands = createCommands();
		Chain chain = new Chain(commands, middlewares);
		return chain.proceed(new ChainContext(command));
	}

	protected ChainContext dispatchRetry(List<Command> listOfCmds) throws Exception {
		ListIterator<Command.Handler> commands = createCommands();
		Chain chain = new Chain(commands, middlewares);
		return chain.proceed(new ChainContext(listOfCmds));
	}

	private ListIterator<Command.Handler> createCommands() {
		List<Command.Handler> commands = new ArrayList<>();
		initCmdBeans(commands, preChain.getCommands());
		initCmdBeans(commands, getCommands());
		initCmdBeans(commands, postChain.getCommands());
		return commands.listIterator();
	}

	private void initCmdBeans(List<Command.Handler> commands, List<Class<? extends Command>> listOfCmds) {
		for (var cmd : listOfCmds) {
			var clazz = Arrays.stream(cmd.getDeclaredClasses())
					.filter(r -> r.getSimpleName().equalsIgnoreCase("Handler"))
					.findFirst();
			clazz.ifPresentOrElse(handlerClazz -> {
				var bean = (Command.Handler) beanFactory.getBean(handlerClazz);
				commands.add(bean);
			}, () -> log.error("Failed to find cmd={}", cmd));
		}
	}

	@Component
	static class PreChain extends ChainBase<Voidy> {

		@Override
		protected List<Class<? extends Command>> getCommands() {
			return List.of(
					ExceptionHandlerCmd.class,
					SaveAsLastCmd.class);
		}

	}

	@Component
	static class PostChain extends ChainBase<Voidy> {

		@Override
		protected List<Class<? extends Command>> getCommands() {
			return List.of(CompletionCmd.class);
		}

	}

}
