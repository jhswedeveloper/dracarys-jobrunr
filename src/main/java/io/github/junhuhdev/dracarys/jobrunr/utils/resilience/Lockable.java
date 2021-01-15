package io.github.junhuhdev.dracarys.jobrunr.utils.resilience;

import org.jobrunr.utils.resilience.Lock;

public interface Lockable {

    Lock lock();
}
