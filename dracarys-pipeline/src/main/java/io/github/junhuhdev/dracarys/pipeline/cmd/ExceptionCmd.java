//package io.github.junhuhdev.dracarys.pipeline.cmd;
//
//import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
//import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;
//import io.github.junhuhdev.dracarys.pipeline.event.FaultEvent;
//import io.github.junhuhdev.dracarys.pipeline.jdbc.EventJdbcRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//@RequiredArgsConstructor
//@Component
//@Slf4j
//public class ExceptionCmd implements Command {
//
//    private final EventJdbcRepository eventJdbcRepository;
//
//    @Override
//    public ChainContext execute(ChainContext ctx, Chain chain) {
//        try {
//            return chain.proceed(ctx);
//        } catch (Exception e) {
//            log.error("Exception was thrown while processing event={}", ctx.getEventTransaction(), e);
//            ctx.store(new FaultEvent(e));
//            eventJdbcRepository.update(ctx.getEventTransaction());
//        }
//        return ctx;
//    }
//}
