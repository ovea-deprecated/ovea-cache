package com.ovea.cache.concurrent;

import java.util.concurrent.Executor;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ImmediateExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
