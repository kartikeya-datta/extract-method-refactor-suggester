error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3749.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3749.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3749.java
text:
```scala
.@@put(Names.PERCOLATE, settingsBuilder().put("type", "fixed").put("size", availableProcessors).put("queue_size", 1000).build())

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.threadpool;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.MoreExecutors;
import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.FileSystemUtils;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.SizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.util.concurrent.*;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentBuilderString;
import org.elasticsearch.node.settings.NodeSettingsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

import static org.elasticsearch.common.collect.MapBuilder.newMapBuilder;
import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.common.unit.TimeValue.timeValueMinutes;
import static org.elasticsearch.common.unit.TimeValue.timeValueSeconds;

/**
 *
 */
public class ThreadPool extends AbstractComponent {

    public static class Names {
        public static final String SAME = "same";
        public static final String GENERIC = "generic";
        public static final String GET = "get";
        public static final String INDEX = "index";
        public static final String BULK = "bulk";
        public static final String SEARCH = "search";
        public static final String PERCOLATE = "percolate";
        public static final String MANAGEMENT = "management";
        public static final String FLUSH = "flush";
        public static final String MERGE = "merge";
        public static final String REFRESH = "refresh";
        public static final String WARMER = "warmer";
        public static final String SNAPSHOT = "snapshot";
        public static final String OPTIMIZE = "optimize";
    }

    public static final String THREADPOOL_GROUP = "threadpool.";

    private volatile ImmutableMap<String, ExecutorHolder> executors;

    private final ImmutableMap<String, Settings> defaultExecutorTypeSettings;

    private final Queue<ExecutorHolder> retiredExecutors = new ConcurrentLinkedQueue<ExecutorHolder>();

    private final ScheduledThreadPoolExecutor scheduler;

    private final EstimatedTimeThread estimatedTimeThread;

    public ThreadPool() {
        this(ImmutableSettings.Builder.EMPTY_SETTINGS, null);
    }

    @Inject
    public ThreadPool(Settings settings, @Nullable NodeSettingsService nodeSettingsService) {
        super(settings);

        Map<String, Settings> groupSettings = settings.getGroups(THREADPOOL_GROUP);

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        int halfProcMaxAt5 = Math.min(((availableProcessors + 1) / 2), 5);
        int halfProcMaxAt10 = Math.min(((availableProcessors + 1) / 2), 10);
        defaultExecutorTypeSettings = ImmutableMap.<String, Settings>builder()
                .put(Names.GENERIC, settingsBuilder().put("type", "cached").put("keep_alive", "30s").build())
                .put(Names.INDEX, settingsBuilder().put("type", "fixed").put("size", availableProcessors).build())
                .put(Names.BULK, settingsBuilder().put("type", "fixed").put("size", availableProcessors).build())
                .put(Names.GET, settingsBuilder().put("type", "fixed").put("size", availableProcessors).build())
                .put(Names.SEARCH, settingsBuilder().put("type", "fixed").put("size", availableProcessors * 3).put("queue_size", 1000).build())
                .put(Names.PERCOLATE, settingsBuilder().put("type", "fixed").put("size", availableProcessors * 3).put("queue_size", 1000).build())
                .put(Names.MANAGEMENT, settingsBuilder().put("type", "scaling").put("keep_alive", "5m").put("size", 5).build())
                .put(Names.FLUSH, settingsBuilder().put("type", "scaling").put("keep_alive", "5m").put("size", halfProcMaxAt5).build())
                .put(Names.MERGE, settingsBuilder().put("type", "scaling").put("keep_alive", "5m").put("size", halfProcMaxAt5).build())
                .put(Names.REFRESH, settingsBuilder().put("type", "scaling").put("keep_alive", "5m").put("size", halfProcMaxAt10).build())
                .put(Names.WARMER, settingsBuilder().put("type", "scaling").put("keep_alive", "5m").put("size", halfProcMaxAt5).build())
                .put(Names.SNAPSHOT, settingsBuilder().put("type", "scaling").put("keep_alive", "5m").put("size", halfProcMaxAt5).build())
                .put(Names.OPTIMIZE, settingsBuilder().put("type", "fixed").put("size", 1).build())
                .build();

        Map<String, ExecutorHolder> executors = Maps.newHashMap();
        for (Map.Entry<String, Settings> executor : defaultExecutorTypeSettings.entrySet()) {
            executors.put(executor.getKey(), build(executor.getKey(), groupSettings.get(executor.getKey()), executor.getValue()));
        }
        executors.put(Names.SAME, new ExecutorHolder(MoreExecutors.sameThreadExecutor(), new Info(Names.SAME, "same")));
        this.executors = ImmutableMap.copyOf(executors);
        this.scheduler = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1, EsExecutors.daemonThreadFactory(settings, "scheduler"));
        this.scheduler.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        this.scheduler.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        if (nodeSettingsService != null) {
            nodeSettingsService.addListener(new ApplySettings());
        }

        TimeValue estimatedTimeInterval = componentSettings.getAsTime("estimated_time_interval", TimeValue.timeValueMillis(200));
        this.estimatedTimeThread = new EstimatedTimeThread(EsExecutors.threadName(settings, "[timer]"), estimatedTimeInterval.millis());
        this.estimatedTimeThread.start();
    }

    public long estimatedTimeInMillis() {
        return estimatedTimeThread.estimatedTimeInMillis();
    }

    public ThreadPoolInfo info() {
        List<Info> infos = new ArrayList<Info>();
        for (ExecutorHolder holder : executors.values()) {
            String name = holder.info.getName();
            // no need to have info on "same" thread pool
            if ("same".equals(name)) {
                continue;
            }
            infos.add(holder.info);
        }
        return new ThreadPoolInfo(infos);
    }

    public ThreadPoolStats stats() {
        List<ThreadPoolStats.Stats> stats = new ArrayList<ThreadPoolStats.Stats>();
        for (ExecutorHolder holder : executors.values()) {
            String name = holder.info.getName();
            // no need to have info on "same" thread pool
            if ("same".equals(name)) {
                continue;
            }
            int threads = -1;
            int queue = -1;
            int active = -1;
            long rejected = -1;
            int largest = -1;
            long completed = -1;
            if (holder.executor instanceof ThreadPoolExecutor) {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) holder.executor;
                threads = threadPoolExecutor.getPoolSize();
                queue = threadPoolExecutor.getQueue().size();
                active = threadPoolExecutor.getActiveCount();
                largest = threadPoolExecutor.getLargestPoolSize();
                completed = threadPoolExecutor.getCompletedTaskCount();
                RejectedExecutionHandler rejectedExecutionHandler = threadPoolExecutor.getRejectedExecutionHandler();
                if (rejectedExecutionHandler instanceof XRejectedExecutionHandler) {
                    rejected = ((XRejectedExecutionHandler) rejectedExecutionHandler).rejected();
                }
            }
            stats.add(new ThreadPoolStats.Stats(name, threads, queue, active, rejected, largest, completed));
        }
        return new ThreadPoolStats(stats);
    }

    public Executor generic() {
        return executor(Names.GENERIC);
    }

    public Executor executor(String name) {
        Executor executor = executors.get(name).executor;
        if (executor == null) {
            throw new ElasticSearchIllegalArgumentException("No executor found for [" + name + "]");
        }
        return executor;
    }

    public ScheduledExecutorService scheduler() {
        return this.scheduler;
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, TimeValue interval) {
        return scheduler.scheduleWithFixedDelay(new LoggingRunnable(command), interval.millis(), interval.millis(), TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> schedule(TimeValue delay, String name, Runnable command) {
        if (!Names.SAME.equals(name)) {
            command = new ThreadedRunnable(command, executor(name));
        }
        return scheduler.schedule(command, delay.millis(), TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        estimatedTimeThread.running = false;
        estimatedTimeThread.interrupt();
        scheduler.shutdown();
        for (ExecutorHolder executor : executors.values()) {
            if (executor.executor instanceof ThreadPoolExecutor) {
                ((ThreadPoolExecutor) executor.executor).shutdown();
            }
        }
    }

    public void shutdownNow() {
        estimatedTimeThread.running = false;
        estimatedTimeThread.interrupt();
        scheduler.shutdownNow();
        for (ExecutorHolder executor : executors.values()) {
            if (executor.executor instanceof ThreadPoolExecutor) {
                ((ThreadPoolExecutor) executor.executor).shutdownNow();
            }
        }
        while (!retiredExecutors.isEmpty()) {
            ((ThreadPoolExecutor) retiredExecutors.remove().executor).shutdownNow();
        }
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        boolean result = scheduler.awaitTermination(timeout, unit);
        for (ExecutorHolder executor : executors.values()) {
            if (executor.executor instanceof ThreadPoolExecutor) {
                result &= ((ThreadPoolExecutor) executor.executor).awaitTermination(timeout, unit);
            }
        }
        while (!retiredExecutors.isEmpty()) {
            result &= ((ThreadPoolExecutor) retiredExecutors.remove().executor).awaitTermination(timeout, unit);
        }
        return result;
    }

    private ExecutorHolder build(String name, @Nullable Settings settings, Settings defaultSettings) {
        return rebuild(name, null, settings, defaultSettings);
    }

    private ExecutorHolder rebuild(String name, ExecutorHolder previousExecutorHolder, @Nullable Settings settings, Settings defaultSettings) {
        if (Names.SAME.equals(name)) {
            // Don't allow to change the "same" thread executor
            return previousExecutorHolder;
        }
        if (settings == null) {
            settings = ImmutableSettings.Builder.EMPTY_SETTINGS;
        }
        Info previousInfo = previousExecutorHolder != null ? previousExecutorHolder.info : null;
        String type = settings.get("type", previousInfo != null ? previousInfo.getType() : defaultSettings.get("type"));
        ThreadFactory threadFactory = EsExecutors.daemonThreadFactory(this.settings, name);
        if ("same".equals(type)) {
            if (previousExecutorHolder != null) {
                logger.debug("updating thread_pool [{}], type [{}]", name, type);
            } else {
                logger.debug("creating thread_pool [{}], type [{}]", name, type);
            }
            return new ExecutorHolder(MoreExecutors.sameThreadExecutor(), new Info(name, type));
        } else if ("cached".equals(type)) {
            TimeValue defaultKeepAlive = defaultSettings.getAsTime("keep_alive", timeValueMinutes(5));
            if (previousExecutorHolder != null) {
                if ("cached".equals(previousInfo.getType())) {
                    TimeValue updatedKeepAlive = settings.getAsTime("keep_alive", previousInfo.getKeepAlive());
                    if (!previousInfo.getKeepAlive().equals(updatedKeepAlive)) {
                        logger.debug("updating thread_pool [{}], type [{}], keep_alive [{}]", name, type, updatedKeepAlive);
                        ((EsThreadPoolExecutor) previousExecutorHolder.executor).setKeepAliveTime(updatedKeepAlive.millis(), TimeUnit.MILLISECONDS);
                        return new ExecutorHolder(previousExecutorHolder.executor, new Info(name, type, -1, -1, updatedKeepAlive, null));
                    }
                    return previousExecutorHolder;
                }
                if (previousInfo.getKeepAlive() != null) {
                    defaultKeepAlive = previousInfo.getKeepAlive();
                }
            }
            TimeValue keepAlive = settings.getAsTime("keep_alive", defaultKeepAlive);
            if (previousExecutorHolder != null) {
                logger.debug("updating thread_pool [{}], type [{}], keep_alive [{}]", name, type, keepAlive);
            } else {
                logger.debug("creating thread_pool [{}], type [{}], keep_alive [{}]", name, type, keepAlive);
            }
            Executor executor = new EsThreadPoolExecutor(0, Integer.MAX_VALUE,
                    keepAlive.millis(), TimeUnit.MILLISECONDS,
                    new SynchronousQueue<Runnable>(),
                    threadFactory);
            return new ExecutorHolder(executor, new Info(name, type, -1, -1, keepAlive, null));
        } else if ("fixed".equals(type)) {
            int defaultSize = defaultSettings.getAsInt("size", Runtime.getRuntime().availableProcessors() * 5);
            SizeValue defaultQueueSize = defaultSettings.getAsSize("queue", defaultSettings.getAsSize("queue_size", null));
            String defaultRejectSetting = defaultSettings.get("reject_policy", "abort");
            String defaultQueueType = defaultSettings.get("queue_type", "linked");

            if (previousExecutorHolder != null) {
                if ("fixed".equals(previousInfo.getType())) {
                    SizeValue updatedQueueSize = settings.getAsSize("capacity", settings.getAsSize("queue", settings.getAsSize("queue_size", previousInfo.getQueueSize())));
                    String updatedQueueType = settings.get("queue_type", previousInfo.getQueueType());
                    if (Objects.equal(previousInfo.getQueueSize(), updatedQueueSize) && previousInfo.getQueueType().equals(updatedQueueType)) {
                        int updatedSize = settings.getAsInt("size", previousInfo.getMax());
                        String updatedRejectSetting = settings.get("reject_policy", previousInfo.getRejectSetting());
                        if (previousInfo.getMax() != updatedSize) {
                            logger.debug("updating thread_pool [{}], type [{}], size [{}], queue_size [{}], reject_policy [{}], queue_type [{}]", name, type, updatedSize, updatedQueueSize, updatedRejectSetting, updatedQueueType);
                            ((EsThreadPoolExecutor) previousExecutorHolder.executor).setCorePoolSize(updatedSize);
                            ((EsThreadPoolExecutor) previousExecutorHolder.executor).setMaximumPoolSize(updatedSize);
                            return new ExecutorHolder(previousExecutorHolder.executor, new Info(name, type, updatedSize, updatedSize, null, updatedQueueSize, null, updatedRejectSetting, updatedQueueType));
                        }
                        if (!previousInfo.getRejectSetting().equals(updatedRejectSetting)) {
                            logger.debug("updating thread_pool [{}], type [{}], size [{}], queue_size [{}], reject_policy [{}], queue_type [{}]", name, type, updatedSize, updatedQueueSize, updatedRejectSetting, updatedQueueType);
                            ((EsThreadPoolExecutor) previousExecutorHolder.executor).setRejectedExecutionHandler(newRejectedExecutionHandler(name, updatedRejectSetting));
                            return new ExecutorHolder(previousExecutorHolder.executor, new Info(name, type, updatedSize, updatedSize, null, updatedQueueSize, null, updatedRejectSetting, updatedQueueType));
                        }
                        return previousExecutorHolder;
                    }
                }
                if (previousInfo.getMax() >= 0) {
                    defaultSize = previousInfo.getMax();
                }
                defaultQueueSize = previousInfo.getQueueSize();
                if (previousInfo.rejectSetting != null) {
                    defaultRejectSetting = previousInfo.rejectSetting;
                }
                if (previousInfo.getQueueType() != null) {
                    defaultQueueType = previousInfo.getQueueType();
                }
            }

            int size = settings.getAsInt("size", defaultSize);
            SizeValue queueSize = settings.getAsSize("capacity", settings.getAsSize("queue", settings.getAsSize("queue_size", defaultQueueSize)));
            String rejectSetting = settings.get("reject_policy", defaultRejectSetting);
            RejectedExecutionHandler rejectedExecutionHandler = newRejectedExecutionHandler(name, rejectSetting);
            String queueType = settings.get("queue_type", defaultQueueType);
            BlockingQueue<Runnable> workQueue = newQueue(queueSize, queueType);
            logger.debug("creating thread_pool [{}], type [{}], size [{}], queue_size [{}], reject_policy [{}], queue_type [{}]", name, type, size, queueSize, rejectSetting, queueType);
            Executor executor = new EsThreadPoolExecutor(size, size,
                    0L, TimeUnit.MILLISECONDS,
                    workQueue,
                    threadFactory, rejectedExecutionHandler);
            return new ExecutorHolder(executor, new Info(name, type, size, size, null, queueSize, null, rejectSetting, queueType));
        } else if ("scaling".equals(type)) {
            TimeValue defaultKeepAlive = defaultSettings.getAsTime("keep_alive", timeValueMinutes(5));
            int defaultMin = defaultSettings.getAsInt("min", 1);
            int defaultSize = defaultSettings.getAsInt("size", Runtime.getRuntime().availableProcessors() * 5);
            if (previousExecutorHolder != null) {
                if ("scaling".equals(previousInfo.getType())) {
                    TimeValue updatedKeepAlive = settings.getAsTime("keep_alive", previousInfo.getKeepAlive());
                    int updatedMin = settings.getAsInt("min", previousInfo.getMin());
                    int updatedSize = settings.getAsInt("max", settings.getAsInt("size", previousInfo.getMax()));
                    if (!previousInfo.getKeepAlive().equals(updatedKeepAlive) || previousInfo.getMin() != updatedMin || previousInfo.getMax() != updatedSize) {
                        logger.debug("updating thread_pool [{}], type [{}], keep_alive [{}]", name, type, updatedKeepAlive);
                        if (!previousInfo.getKeepAlive().equals(updatedKeepAlive)) {
                            ((EsThreadPoolExecutor) previousExecutorHolder.executor).setKeepAliveTime(updatedKeepAlive.millis(), TimeUnit.MILLISECONDS);
                        }
                        if (previousInfo.getMin() != updatedMin) {
                            ((EsThreadPoolExecutor) previousExecutorHolder.executor).setCorePoolSize(updatedMin);
                        }
                        if (previousInfo.getMax() != updatedSize) {
                            ((EsThreadPoolExecutor) previousExecutorHolder.executor).setMaximumPoolSize(updatedSize);
                        }
                        return new ExecutorHolder(previousExecutorHolder.executor, new Info(name, type, updatedMin, updatedSize, updatedKeepAlive, null));
                    }
                    return previousExecutorHolder;
                }
                if (previousInfo.getKeepAlive() != null) {
                    defaultKeepAlive = previousInfo.getKeepAlive();
                }
                if (previousInfo.getMin() >= 0) {
                    defaultMin = previousInfo.getMin();
                }
                if (previousInfo.getMax() >= 0) {
                    defaultSize = previousInfo.getMax();
                }
            }
            TimeValue keepAlive = settings.getAsTime("keep_alive", defaultKeepAlive);
            int min = settings.getAsInt("min", defaultMin);
            int size = settings.getAsInt("max", settings.getAsInt("size", defaultSize));
            if (previousExecutorHolder != null) {
                logger.debug("updating thread_pool [{}], type [{}], min [{}], size [{}], keep_alive [{}]", name, type, min, size, keepAlive);
            } else {
                logger.debug("creating thread_pool [{}], type [{}], min [{}], size [{}], keep_alive [{}]", name, type, min, size, keepAlive);
            }
            Executor executor = EsExecutors.newScalingExecutorService(min, size, keepAlive.millis(), TimeUnit.MILLISECONDS, threadFactory);
            return new ExecutorHolder(executor, new Info(name, type, min, size, keepAlive, null));
        } else if ("blocking".equals(type)) {
            TimeValue defaultKeepAlive = defaultSettings.getAsTime("keep_alive", timeValueMinutes(5));
            int defaultMin = defaultSettings.getAsInt("min", 1);
            int defaultSize = defaultSettings.getAsInt("size", Runtime.getRuntime().availableProcessors() * 5);
            SizeValue defaultQueueSize = defaultSettings.getAsSize("queue_size", new SizeValue(1000));
            TimeValue defaultWaitTime = defaultSettings.getAsTime("wait_time", timeValueSeconds(60));
            if (previousExecutorHolder != null) {
                if ("blocking".equals(previousInfo.getType())) {
                    SizeValue updatedQueueSize = settings.getAsSize("capacity", settings.getAsSize("queue", settings.getAsSize("queue_size", defaultQueueSize)));
                    TimeValue updatedWaitTime = settings.getAsTime("wait_time", defaultWaitTime);
                    if (previousInfo.getQueueSize().equals(updatedQueueSize) && previousInfo.getWaitTime().equals(updatedWaitTime)) {
                        TimeValue updatedKeepAlive = settings.getAsTime("keep_alive", previousInfo.getKeepAlive());
                        int updatedMin = settings.getAsInt("min", previousInfo.getMin());
                        int updatedSize = settings.getAsInt("max", settings.getAsInt("size", previousInfo.getMax()));
                        if (!previousInfo.getKeepAlive().equals(updatedKeepAlive) || !previousInfo.getWaitTime().equals(settings.getAsTime("wait_time", defaultWaitTime)) ||
                                previousInfo.getMin() != updatedMin || previousInfo.getMax() != updatedSize) {
                            logger.debug("updating thread_pool [{}], type [{}], keep_alive [{}]", name, type, updatedKeepAlive);
                            if (!previousInfo.getKeepAlive().equals(updatedKeepAlive)) {
                                ((EsThreadPoolExecutor) previousExecutorHolder.executor).setKeepAliveTime(updatedKeepAlive.millis(), TimeUnit.MILLISECONDS);
                            }
                            if (previousInfo.getMin() != updatedMin) {
                                ((EsThreadPoolExecutor) previousExecutorHolder.executor).setCorePoolSize(updatedMin);
                            }
                            if (previousInfo.getMax() != updatedSize) {
                                ((EsThreadPoolExecutor) previousExecutorHolder.executor).setMaximumPoolSize(updatedSize);
                            }
                            return new ExecutorHolder(previousExecutorHolder.executor, new Info(name, type, updatedMin, updatedSize, updatedKeepAlive, updatedQueueSize, updatedWaitTime));
                        }
                        return previousExecutorHolder;
                    }
                }
                if (previousInfo.getKeepAlive() != null) {
                    defaultKeepAlive = previousInfo.getKeepAlive();
                }
                if (previousInfo.getMin() >= 0) {
                    defaultMin = previousInfo.getMin();
                }
                if (previousInfo.getMax() >= 0) {
                    defaultSize = previousInfo.getMax();
                }
                if (previousInfo.getQueueSize() != null) {
                    defaultQueueSize = previousInfo.getQueueSize();
                }
                if (previousInfo.getWaitTime() != null) {
                    defaultWaitTime = previousInfo.getWaitTime();
                }
            }
            TimeValue keepAlive = settings.getAsTime("keep_alive", defaultKeepAlive);
            int min = settings.getAsInt("min", defaultMin);
            int size = settings.getAsInt("max", settings.getAsInt("size", defaultSize));
            SizeValue queueSize = settings.getAsSize("capacity", settings.getAsSize("queue", settings.getAsSize("queue_size", defaultQueueSize)));
            TimeValue waitTime = settings.getAsTime("wait_time", defaultWaitTime);
            if (previousExecutorHolder != null) {
                logger.debug("updating thread_pool [{}], type [{}], min [{}], size [{}], queue_size [{}], keep_alive [{}], wait_time [{}]", name, type, min, size, queueSize.singles(), keepAlive, waitTime);
            } else {
                logger.debug("creating thread_pool [{}], type [{}], min [{}], size [{}], queue_size [{}], keep_alive [{}], wait_time [{}]", name, type, min, size, queueSize.singles(), keepAlive, waitTime);
            }
            Executor executor = EsExecutors.newBlockingExecutorService(min, size, keepAlive.millis(), TimeUnit.MILLISECONDS, threadFactory, (int) queueSize.singles(), waitTime.millis(), TimeUnit.MILLISECONDS);
            return new ExecutorHolder(executor, new Info(name, type, min, size, keepAlive, queueSize, waitTime));
        }
        throw new ElasticSearchIllegalArgumentException("No type found [" + type + "], for [" + name + "]");
    }

    public void updateSettings(Settings settings) {
        Map<String, Settings> groupSettings = settings.getGroups("threadpool");
        if (groupSettings.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Settings> executor : defaultExecutorTypeSettings.entrySet()) {
            Settings updatedSettings = groupSettings.get(executor.getKey());
            if (updatedSettings == null) {
                continue;
            }

            ExecutorHolder oldExecutorHolder = executors.get(executor.getKey());
            ExecutorHolder newExecutorHolder = rebuild(executor.getKey(), oldExecutorHolder, updatedSettings, executor.getValue());
            if (!oldExecutorHolder.equals(newExecutorHolder)) {
                executors = newMapBuilder(executors).put(executor.getKey(), newExecutorHolder).immutableMap();
                if (!oldExecutorHolder.executor.equals(newExecutorHolder.executor) && oldExecutorHolder.executor instanceof EsThreadPoolExecutor) {
                    retiredExecutors.add(oldExecutorHolder);
                    ((EsThreadPoolExecutor) oldExecutorHolder.executor).shutdown(new ExecutorShutdownListener(oldExecutorHolder));
                }
            }
        }
    }

    private BlockingQueue<Runnable> newQueue(SizeValue queueSize, String queueType) {
        if (queueSize == null) {
            return ConcurrentCollections.newBlockingQueue();
        } else if (queueSize.singles() == 0) {
            return new SynchronousQueue<Runnable>();
        } else if (queueSize.singles() > 0) {
            if ("linked".equals(queueType)) {
                return new LinkedBlockingQueue<Runnable>((int) queueSize.singles());
            } else if ("array".equals(queueType)) {
                return new ArrayBlockingQueue<Runnable>((int) queueSize.singles());
            } else {
                throw new ElasticSearchIllegalArgumentException("illegal queue_type set to [" + queueType + "], should be either linked or array");
            }
        } else { // queueSize.singles() < 0, just treat it as unbounded queue
            return ConcurrentCollections.newBlockingQueue();
        }
    }

    private RejectedExecutionHandler newRejectedExecutionHandler(String name, String rejectSetting) {
        if ("abort".equals(rejectSetting)) {
            return new EsAbortPolicy();
        } else if ("caller".equals(rejectSetting)) {
            return new ThreadPoolExecutor.CallerRunsPolicy();
        } else {
            throw new ElasticSearchIllegalArgumentException("reject_policy [" + rejectSetting + "] not valid for [" + name + "] thread pool");
        }
    }

    class ExecutorShutdownListener implements EsThreadPoolExecutor.ShutdownListener {

        private ExecutorHolder holder;

        public ExecutorShutdownListener(ExecutorHolder holder) {
            this.holder = holder;
        }

        @Override
        public void onTerminated() {
            retiredExecutors.remove(holder);
        }
    }

    class LoggingRunnable implements Runnable {

        private final Runnable runnable;

        LoggingRunnable(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void run() {
            try {
                runnable.run();
            } catch (Exception e) {
                logger.warn("failed to run {}", e, runnable.toString());
            }
        }

        @Override
        public int hashCode() {
            return runnable.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return runnable.equals(obj);
        }

        @Override
        public String toString() {
            return "[threaded] " + runnable.toString();
        }
    }

    class ThreadedRunnable implements Runnable {

        private final Runnable runnable;

        private final Executor executor;

        ThreadedRunnable(Runnable runnable, Executor executor) {
            this.runnable = runnable;
            this.executor = executor;
        }

        @Override
        public void run() {
            executor.execute(runnable);
        }

        @Override
        public int hashCode() {
            return runnable.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return runnable.equals(obj);
        }

        @Override
        public String toString() {
            return "[threaded] " + runnable.toString();
        }
    }

    static class EstimatedTimeThread extends Thread {

        final long interval;

        volatile boolean running = true;

        volatile long estimatedTimeInMillis;

        EstimatedTimeThread(String name, long interval) {
            super(name);
            this.interval = interval;
            setDaemon(true);
        }

        public long estimatedTimeInMillis() {
            return this.estimatedTimeInMillis;
        }

        @Override
        public void run() {
            while (running) {
                estimatedTimeInMillis = System.currentTimeMillis();
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    running = false;
                    return;
                }
                try {
                    FileSystemUtils.checkMkdirsStall(estimatedTimeInMillis);
                } catch (Exception e) {
                    // ignore
                }
            }
        }
    }

    static class ExecutorHolder {
        public final Executor executor;
        public final Info info;

        ExecutorHolder(Executor executor, Info info) {
            this.executor = executor;
            this.info = info;
        }
    }

    public static class Info implements Streamable, ToXContent {

        private String name;
        private String type;
        private int min;
        private int max;
        private TimeValue keepAlive;
        private SizeValue queueSize;
        private TimeValue waitTime;
        private String rejectSetting;
        private String queueType;

        Info() {

        }

        public Info(String name, String type) {
            this(name, type, -1);
        }

        public Info(String name, String type, int size) {
            this(name, type, size, size, null, null);
        }

        public Info(String name, String type, int min, int max, @Nullable TimeValue keepAlive, @Nullable SizeValue queueSize) {
            this(name, type, min, max, keepAlive, queueSize, null);
        }

        public Info(String name, String type, int min, int max, @Nullable TimeValue keepAlive, @Nullable SizeValue queueSize, @Nullable TimeValue waitTime) {
            this(name, type, min, max, keepAlive, queueSize, waitTime, null, null);
        }

        public Info(String name, String type, int min, int max, @Nullable TimeValue keepAlive, @Nullable SizeValue queueSize, @Nullable TimeValue waitTime, String rejectSetting, String queueType) {
            this.name = name;
            this.type = type;
            this.min = min;
            this.max = max;
            this.keepAlive = keepAlive;
            this.queueSize = queueSize;
            this.waitTime = waitTime;
            this.rejectSetting = rejectSetting;
            this.queueType = queueType;
        }

        public String getName() {
            return this.name;
        }

        public String getType() {
            return this.type;
        }

        public int getMin() {
            return this.min;
        }

        public int getMax() {
            return this.max;
        }

        @Nullable
        public TimeValue getKeepAlive() {
            return this.keepAlive;
        }

        @Nullable
        public SizeValue getQueueSize() {
            return this.queueSize;
        }

        @Nullable
        public TimeValue getWaitTime() {
            return this.waitTime;
        }

        @Nullable
        public String getRejectSetting() {
            return this.rejectSetting;
        }

        @Nullable
        public String getQueueType() {
            return this.queueType;
        }


        @Override
        public void readFrom(StreamInput in) throws IOException {
            name = in.readString();
            type = in.readString();
            min = in.readInt();
            max = in.readInt();
            if (in.readBoolean()) {
                keepAlive = TimeValue.readTimeValue(in);
            }
            if (in.readBoolean()) {
                queueSize = SizeValue.readSizeValue(in);
            }
            if (in.readBoolean()) {
                waitTime = TimeValue.readTimeValue(in);
            }
            rejectSetting = in.readOptionalString();
            queueType = in.readOptionalString();
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            out.writeString(name);
            out.writeString(type);
            out.writeInt(min);
            out.writeInt(max);
            if (keepAlive == null) {
                out.writeBoolean(false);
            } else {
                out.writeBoolean(true);
                keepAlive.writeTo(out);
            }
            if (queueSize == null) {
                out.writeBoolean(false);
            } else {
                out.writeBoolean(true);
                queueSize.writeTo(out);
            }
            if (waitTime == null) {
                out.writeBoolean(false);
            } else {
                out.writeBoolean(true);
                waitTime.writeTo(out);
            }
            out.writeOptionalString(rejectSetting);
            out.writeOptionalString(queueType);
        }

        @Override
        public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
            builder.startObject(name, XContentBuilder.FieldCaseConversion.NONE);
            builder.field(Fields.TYPE, type);
            if (min != -1) {
                builder.field(Fields.MIN, min);
            }
            if (max != -1) {
                builder.field(Fields.MAX, max);
            }
            if (keepAlive != null) {
                builder.field(Fields.KEEP_ALIVE, keepAlive.toString());
            }
            if (queueSize != null) {
                builder.field(Fields.QUEUE_SIZE, queueSize.toString());
            }
            if (waitTime != null) {
                builder.field(Fields.WAIT_TIME, waitTime.toString());
            }
            if (rejectSetting != null) {
                builder.field(Fields.REJECT_POLICY, rejectSetting);
            }
            if (queueType != null) {
                builder.field(Fields.QUEUE_TYPE, queueType);
            }
            builder.endObject();
            return builder;
        }

        static final class Fields {
            static final XContentBuilderString TYPE = new XContentBuilderString("type");
            static final XContentBuilderString MIN = new XContentBuilderString("min");
            static final XContentBuilderString MAX = new XContentBuilderString("max");
            static final XContentBuilderString KEEP_ALIVE = new XContentBuilderString("keep_alive");
            static final XContentBuilderString QUEUE_SIZE = new XContentBuilderString("queue_size");
            static final XContentBuilderString WAIT_TIME = new XContentBuilderString("wait_time");
            static final XContentBuilderString REJECT_POLICY = new XContentBuilderString("reject_policy");
            static final XContentBuilderString QUEUE_TYPE = new XContentBuilderString("queue_type");
        }

    }

    class ApplySettings implements NodeSettingsService.Listener {
        @Override
        public void onRefreshSettings(Settings settings) {
            updateSettings(settings);
        }
    }
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3749.java