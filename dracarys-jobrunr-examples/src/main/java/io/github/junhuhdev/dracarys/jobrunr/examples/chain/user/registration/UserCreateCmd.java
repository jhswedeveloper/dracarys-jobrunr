package io.github.junhuhdev.dracarys.jobrunr.examples.chain.user.registration;

import io.github.junhuhdev.dracarys.jobrunr.examples.chain.common.EmailSendCmd;
import io.github.junhuhdev.dracarys.jobrunr.examples.jpa.entity.UserEntity;
import io.github.junhuhdev.dracarys.jobrunr.examples.jpa.repository.UserRepository;
import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Data
public class UserCreateCmd implements Command {

    @Slf4j
    @RequiredArgsConstructor
    @Component
    static class Handler implements Command.Handler {

        private final UserRepository userRepository;

        @Override
        public ChainContext execute(ChainContext ctx, Chain chain) throws Exception {
            log.info("Running (2) UserCreateCmd");
            var request = ctx.getFirst(UserRegistrationChain.UserRegistrationRequest.class);
            var user = new UserEntity();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user = userRepository.save(user);
            ctx.store(EmailSendCmd.builder()
                     .from("noreply@dracarys.com")
                     .to(request.getEmail())
                     .content(String.format("Welcome to Dracarys %s", request.getName()))
                     .build());
            return chain.proceed(ctx);
        }
    }
}
