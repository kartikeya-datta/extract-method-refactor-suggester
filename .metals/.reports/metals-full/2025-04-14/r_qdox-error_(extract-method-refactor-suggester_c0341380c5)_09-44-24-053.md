error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6247.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6247.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6247.java
text:
```scala
M@@anagementResourceRegistration bufPool = bufPoolRoot.registerSubModel(PathElement.pathElement(NAME), new DescriptionProvider() {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.platform.mbean;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CORE_SERVICE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PLATFORM_MBEAN;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_OPERATION;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.BUFFER_POOL_PATH;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.CLASS_LOADING_PATH;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.COMPILATION_PATH;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.DUMP_ALL_THREADS;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.FIND_DEADLOCKED_THREADS;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.FIND_MONITOR_DEADLOCKED_THREADS;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.GARBAGE_COLLECTOR_PATH;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.GET_LOGGER_LEVEL;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.GET_PARENT_LOGGER_NAME;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.GET_THREAD_CPU_TIME;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.GET_THREAD_INFO;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.GET_THREAD_INFOS;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.GET_THREAD_USER_TIME;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.LOGGING_PATH;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.MEMORY_MANAGER_PATH;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.MEMORY_PATH;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.MEMORY_POOL_PATH;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.OPERATING_SYSTEM_PATH;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.RESET_PEAK_THREAD_COUNT;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.RESET_PEAK_USAGE;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.RUNTIME_PATH;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.SET_LOGGER_LEVEL;
import static org.jboss.as.platform.mbean.PlatformMBeanConstants.THREADING_PATH;

import java.lang.management.ManagementFactory;
import java.util.Locale;

import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.common.CommonProviders;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelNode;

/**
 * Utility for registering platform mbean resources with a parent resource registration (either a server
 * or host level registration.)
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class PlatformMBeanResourceRegistrar {

    public static void registerPlatformMBeanResources(final ManagementResourceRegistration parent) {

        ManagementResourceRegistration root = parent.registerSubModel(PathElement.pathElement(CORE_SERVICE, PLATFORM_MBEAN), new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return PlatformMBeanDescriptions.getRootResource(locale);
            }
        });

        // Classloading
        ManagementResourceRegistration classloading = root.registerSubModel(CLASS_LOADING_PATH, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return PlatformMBeanDescriptions.getClassLoadingResource(locale);
            }
        });
        ClassLoadingMXBeanAttributeHandler.INSTANCE.register(classloading);

        // Compilation -- not all platforms have this one
        if (ManagementFactory.getCompilationMXBean() != null) {
            ManagementResourceRegistration compilation = root.registerSubModel(COMPILATION_PATH, new DescriptionProvider() {
                @Override
                public ModelNode getModelDescription(Locale locale) {
                    return PlatformMBeanDescriptions.getCompilationResource(locale);
                }
            });
            compilation.registerOperationHandler(READ_RESOURCE_OPERATION, CompilationMXBeanReadResourceHandler.INSTANCE, CommonProviders.READ_RESOURCE_PROVIDER);
            CompilationMXBeanAttributeHandler.INSTANCE.register(compilation);
        }

        // Garbage Collector
        ManagementResourceRegistration gcRoot = root.registerSubModel(GARBAGE_COLLECTOR_PATH, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return PlatformMBeanDescriptions.getGarbageCollectorRootResource(locale);
            }
        });
        ManagementResourceRegistration gc = gcRoot.registerSubModel(PathElement.pathElement(NAME), new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return PlatformMBeanDescriptions.getGarbageCollectorResource(locale);
            }
        });
        GarbageCollectorMXBeanAttributeHandler.INSTANCE.register(gc);

        // Memory Manager
        ManagementResourceRegistration memMgrRoot = root.registerSubModel(MEMORY_MANAGER_PATH, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return PlatformMBeanDescriptions.getMemoryManagerRootResource(locale);
            }
        });
        ManagementResourceRegistration memMgr = memMgrRoot.registerSubModel(PathElement.pathElement(NAME), new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return PlatformMBeanDescriptions.getMemoryManagerResource(locale);
            }
        });
        MemoryManagerMXBeanAttributeHandler.INSTANCE.register(memMgr);

        // Memory
        ManagementResourceRegistration memory = root.registerSubModel(MEMORY_PATH, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return PlatformMBeanDescriptions.getMemoryResource(locale);
            }
        });
        MemoryMXBeanAttributeHandler.INSTANCE.register(memory);
        memory.registerOperationHandler(PlatformMBeanConstants.GC, MemoryMXBeanGCHandler.INSTANCE, MemoryMXBeanGCHandler.INSTANCE);
        // TODO notifications

        // Memory Pool
        ManagementResourceRegistration memPoolRoot = root.registerSubModel(MEMORY_POOL_PATH, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return PlatformMBeanDescriptions.getMemoryPoolRootResource(locale);
            }
        });
        ManagementResourceRegistration memPool = memPoolRoot.registerSubModel(PathElement.pathElement(NAME), new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return PlatformMBeanDescriptions.getMemoryPoolResource(locale);
            }
        });
        memPool.registerOperationHandler(READ_RESOURCE_OPERATION, MemoryPoolMXBeanReadResourceHandler.INSTANCE, CommonProviders.READ_RESOURCE_PROVIDER);
        memPool.registerOperationHandler(RESET_PEAK_USAGE, MemoryPoolMXBeanResetPeakUsageHandler.INSTANCE, MemoryPoolMXBeanResetPeakUsageHandler.INSTANCE);
        MemoryPoolMXBeanAttributeHandler.INSTANCE.register(memPool);

        // Operating System
        ManagementResourceRegistration opSys = root.registerSubModel(OPERATING_SYSTEM_PATH, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return PlatformMBeanDescriptions.getOperatingSystemResource(locale);
            }
        });
        opSys.registerOperationHandler(READ_RESOURCE_OPERATION, OperatingSystemMXBeanReadResourceHandler.INSTANCE, CommonProviders.READ_RESOURCE_PROVIDER);
        OperatingSystemMXBeanAttributeHandler.INSTANCE.register(opSys);

        // Runtime
        ManagementResourceRegistration runtime = root.registerSubModel(RUNTIME_PATH, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return PlatformMBeanDescriptions.getRuntimeResource(locale);
            }
        });
        runtime.registerOperationHandler(READ_RESOURCE_OPERATION, RuntimeMXBeanReadResourceHandler.INSTANCE, CommonProviders.READ_RESOURCE_PROVIDER);
        RuntimeMXBeanAttributeHandler.INSTANCE.register(runtime);

        // Threads
        ManagementResourceRegistration threads = root.registerSubModel(THREADING_PATH, new DescriptionProvider() {
            @Override
            public ModelNode getModelDescription(Locale locale) {
                return PlatformMBeanDescriptions.getThreadingResource(locale);
            }
        });
        threads.registerOperationHandler(READ_RESOURCE_OPERATION, ThreadMXBeanReadResourceHandler.INSTANCE, CommonProviders.READ_RESOURCE_PROVIDER);
        threads.registerOperationHandler(RESET_PEAK_THREAD_COUNT, ThreadMXBeanResetPeakThreadCountHandler.INSTANCE,
                ThreadMXBeanResetPeakThreadCountHandler.INSTANCE);
        threads.registerOperationHandler(FIND_DEADLOCKED_THREADS, ThreadMXBeanFindDeadlockedThreadsHandler.INSTANCE,
                ThreadMXBeanFindDeadlockedThreadsHandler.INSTANCE);
        threads.registerOperationHandler(FIND_MONITOR_DEADLOCKED_THREADS, ThreadMXBeanFindMonitorDeadlockedThreadsHandler.INSTANCE,
                ThreadMXBeanFindMonitorDeadlockedThreadsHandler.INSTANCE);
        threads.registerOperationHandler(GET_THREAD_INFO, ThreadMXBeanThreadInfoHandler.INSTANCE, ThreadMXBeanThreadInfoHandler.INSTANCE);
        threads.registerOperationHandler(GET_THREAD_INFOS, ThreadMXBeanThreadInfosHandler.INSTANCE, ThreadMXBeanThreadInfosHandler.INSTANCE);
        threads.registerOperationHandler(GET_THREAD_CPU_TIME, ThreadMXBeanCpuTimeHandler.INSTANCE, ThreadMXBeanCpuTimeHandler.INSTANCE);
        threads.registerOperationHandler(GET_THREAD_USER_TIME, ThreadMXBeanUserTimeHandler.INSTANCE, ThreadMXBeanUserTimeHandler.INSTANCE);
        threads.registerOperationHandler(DUMP_ALL_THREADS, ThreadMXBeanDumpAllThreadsHandler.INSTANCE, ThreadMXBeanDumpAllThreadsHandler.INSTANCE);
        ThreadMXBeanAttributeHandler.INSTANCE.register(threads);

        if (PlatformMBeanUtil.JVM_MAJOR_VERSION > 6) {

            // BufferPoolMXBean
            ManagementResourceRegistration bufPoolRoot = root.registerSubModel(BUFFER_POOL_PATH, new DescriptionProvider() {
                @Override
                public ModelNode getModelDescription(Locale locale) {
                    return PlatformMBeanDescriptions.getBufferPoolRootResource(locale);
                }
            });
            ManagementResourceRegistration bufPool = memPoolRoot.registerSubModel(PathElement.pathElement(NAME), new DescriptionProvider() {
                @Override
                public ModelNode getModelDescription(Locale locale) {
                    return PlatformMBeanDescriptions.getBufferPoolResource(locale);
                }
            });
            BufferPoolMXBeanAttributeHandler.INSTANCE.register(bufPool);

            // PlatformLoggingMXBean
            ManagementResourceRegistration logging = root.registerSubModel(LOGGING_PATH, new DescriptionProvider() {
                @Override
                public ModelNode getModelDescription(Locale locale) {
                    return PlatformMBeanDescriptions.getPlatformLoggingResource(locale);
                }
            });
            logging.registerOperationHandler(GET_LOGGER_LEVEL, PlatformLoggingMXBeanGetLoggerLevelHandler.INSTANCE, PlatformLoggingMXBeanGetLoggerLevelHandler.INSTANCE);
            logging.registerOperationHandler(SET_LOGGER_LEVEL, PlatformLoggingMXBeanSetLoggerLevelHandler.INSTANCE, PlatformLoggingMXBeanSetLoggerLevelHandler.INSTANCE);
            logging.registerOperationHandler(GET_PARENT_LOGGER_NAME, PlatformLoggingMXBeanGetParentLoggerNameHandler.INSTANCE, PlatformLoggingMXBeanGetParentLoggerNameHandler.INSTANCE);
            PlatformLoggingMXBeanAttributeHandler.INSTANCE.register(logging);

        }

    }

    private PlatformMBeanResourceRegistrar() {
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6247.java