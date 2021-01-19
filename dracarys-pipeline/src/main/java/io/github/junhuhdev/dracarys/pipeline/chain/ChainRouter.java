package io.github.junhuhdev.dracarys.pipeline.chain;

import io.github.junhuhdev.dracarys.jobrunr.jobs.context.JobRunrDashboardLogger;
import io.github.junhuhdev.dracarys.pipeline.api.CommandStorageApi;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.cmd.FaultCmd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Component
public class ChainRouter implements Chainable {

	private final static Logger log = new JobRunrDashboardLogger(LoggerFactory.getLogger(ChainRouter.class));
	private final List<ChainBase> chains;
	private final CommandStorageApi commandStorageApi;

	@Override
	public ChainContext dispatch(Command.Request cmd) throws Exception {
		var chainMatches = chains.stream()
				.filter(chain -> chain.matches(cmd))
				.collect(toList());
		log.info("---> Routing to chains {}", chainMatches.stream().map(r -> r.getClass().getSimpleName()).collect(toList()));
		if (chainMatches.size() > 1) {
			throw new IllegalStateException(String.format("Found more than one chain to route type=%s", cmd.getClass().getSimpleName()));
		}
		if (isEmpty(chainMatches)) {
			throw new IllegalStateException("No chain to process workflow=" + cmd.getClass().getSimpleName());
		}
		var cmds = commandStorageApi.findByReferenceId(cmd.getReferenceId());
		var shouldRetry = cmds.size() > 1;
		ChainContext ctx = null;
		for (var chain : chainMatches) {
			if (shouldRetry) {
				log.info("---> Retrying attempt={}, chain={}, id={}", cmds.stream().filter(r -> r instanceof FaultCmd).count(), chain.getClass().getSimpleName(), cmd.getReferenceId());
				var status = commandStorageApi.findStatus(cmd.getReferenceId());
				ctx = chain.dispatchRetry(cmds, status);
			} else {
				log.info("---> Started chain={}, id={}", chain.getClass().getSimpleName(), cmd.getReferenceId());
				ctx = chain.dispatch(cmd);
			}
			log.info("<--- Completed chain={}", chain.getClass().getSimpleName());
		}
		return ctx;
	}

}
