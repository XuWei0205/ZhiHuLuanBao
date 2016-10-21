package com.hanyu.zhihuluanbao.managers;

import com.hanyu.zhihuluanbao.commons.BaseTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 全局任务类，任务使用全局线程池，暂仅支持cachedThreadPool
 * Created by hehuajia on 16/2/3.
 */
public class TaskManager {

    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    private TaskManager() {

    }

    /**
     * TaskManager被加载时，TaskManagerHolder不会被初始化。这是一种延迟加载的策略，避免了获取单例使用同步关键字
     */
    private static class TaskManagerHolder {
        /* 创建类时构造实例，天生对多线程友好*/
        private static TaskManager instance = new TaskManager();
    }

    public static TaskManager getIns() {
        return TaskManagerHolder.instance;
    }

    /**
     * 执行异步任务
     * @param runnable
     */
    public void executeTask(Runnable runnable) {
        cachedThreadPool.execute(runnable);
    }

    /**
     * 执行同步任务
     * @param task
     */
    public void executeTask(BaseTask task) {
        task.executeTask();
    }
    /**
     * 执行异步任务
     * @param task
     */
    public void executeAsyncTask(final BaseTask task) {
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                task.executeAsyncTask();
            }
        });
    }
}
