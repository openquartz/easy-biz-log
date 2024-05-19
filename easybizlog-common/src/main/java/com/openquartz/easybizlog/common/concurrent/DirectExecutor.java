package com.openquartz.easybizlog.common.concurrent;

import java.util.concurrent.Executor;

/**
 * 直接执行执行器
 * @author svnee
 */
public class DirectExecutor implements Executor {

    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
