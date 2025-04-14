error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3121.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3121.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3121.java
text:
```scala
r@@eturn controller.boot(bootOperations, OperationMessageHandler.logging, ModelController.OperationTransactionControl.COMMIT, rollbackOnRuntimeFailure);

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

package org.jboss.as.controller;

import java.util.List;
import java.util.concurrent.ExecutorService;

import org.jboss.as.controller.client.OperationMessageHandler;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.persistence.ConfigurationPersistenceException;
import org.jboss.as.controller.persistence.ConfigurationPersister;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

import static org.jboss.as.controller.ControllerLogger.ROOT_LOGGER;
import static org.jboss.as.controller.ControllerMessages.MESSAGES;

/**
 * A base class for controller services.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public abstract class AbstractControllerService implements Service<ModelController> {

    /**
     * Name of the system property to set to control the stack size for the thread used to process boot operations.
     * The boot sequence can have a very deep stack, so if needed setting this property can be used to create a larger
     * memory area for storing data on the stack.
     *
     * @see #DEFAULT_BOOT_STACK_SIZE
     */
    public static final String BOOT_STACK_SIZE_PROPERTY = "jboss.boot.thread.stack.size";

    /**
     * The default stack size for the thread used to process boot operations.
     *
     * @see #BOOT_STACK_SIZE_PROPERTY
     */
    public static final int DEFAULT_BOOT_STACK_SIZE = 2 * 1024 * 1024;

    private static int getBootStackSize() {
        String prop = SecurityActions.getSystemProperty(BOOT_STACK_SIZE_PROPERTY);
        if (prop == null) {
            return  DEFAULT_BOOT_STACK_SIZE;
        } else {
            int base = 1;
            String multiple = prop;
            int lastIdx = prop.length() - 1;
            if (lastIdx > 0) {
                char last = prop.charAt(lastIdx);
                if ('k' == last || 'K' == last) {
                    multiple = prop.substring(0, lastIdx);
                    base = 1024;
                } else if ('m' == last || 'M' == last) {
                    multiple = prop.substring(0, lastIdx);
                    base = 1024 * 1024;
                }
            }
            try {
                return Integer.parseInt(multiple) * base;
            } catch (NumberFormatException e) {
                ROOT_LOGGER.invalidSystemPropertyValue(prop, BOOT_STACK_SIZE_PROPERTY, DEFAULT_BOOT_STACK_SIZE);
                return DEFAULT_BOOT_STACK_SIZE;
            }
        }
    }

    private final ProcessType processType;
    private final RunningModeControl runningModeControl;
    private final DescriptionProvider rootDescriptionProvider;
    private final ControlledProcessState processState;
    private final OperationStepHandler prepareStep;
    private final InjectedValue<ExecutorService> injectedExecutorService = new InjectedValue<ExecutorService>();
    private final ExpressionResolver expressionResolver;
    private volatile ModelControllerImpl controller;
    private ConfigurationPersister configurationPersister;

    /**
     * Construct a new instance.
     *
     * @param processType             the type of process being controlled
     * @param runningModeControl      the controller of the process' running mode
     * @param configurationPersister  the configuration persister
     * @param processState            the controlled process state
     * @param rootDescriptionProvider the root description provider
     * @param prepareStep             the prepare step to prepend to operation execution
     * @param expressionResolver      the expression resolver
     */
    protected AbstractControllerService(final ProcessType processType, final RunningModeControl runningModeControl,
                                        final ConfigurationPersister configurationPersister,
                                        final ControlledProcessState processState, final DescriptionProvider rootDescriptionProvider,
                                        final OperationStepHandler prepareStep, final ExpressionResolver expressionResolver) {
        this.processType = processType;
        this.runningModeControl = runningModeControl;
        this.configurationPersister = configurationPersister;
        this.rootDescriptionProvider = rootDescriptionProvider;
        this.processState = processState;
        this.prepareStep = prepareStep;
        this.expressionResolver = expressionResolver != null ? expressionResolver : ExpressionResolver.DEFAULT;

    }


    public void start(final StartContext context) throws StartException {

        if (configurationPersister == null) {
            throw MESSAGES.persisterNotInjected();
        }
        final ServiceController<?> serviceController = context.getController();
        final ServiceContainer container = serviceController.getServiceContainer();
        final ServiceTarget target = context.getChildTarget();
        final ExecutorService executorService = injectedExecutorService.getOptionalValue();
        final ModelControllerImpl controller = new ModelControllerImpl(container, target,
                ManagementResourceRegistration.Factory.create(rootDescriptionProvider),
                new ContainerStateMonitor(container, serviceController),
                configurationPersister, processType, runningModeControl, prepareStep,
                processState, executorService, expressionResolver);
        initModel(controller.getRootResource(), controller.getRootRegistration());
        this.controller = controller;

        final long bootStackSize = getBootStackSize();
        final Thread bootThread = new Thread(null, new Runnable() {
            public void run() {
                try {
                    try {
                        boot(new BootContext() {
                            public ServiceTarget getServiceTarget() {
                                return target;
                            }
                        });
                    } finally {
                        processState.setRunning();
                    }
                } catch (Throwable t) {
                    container.shutdown();
                    if (t instanceof StackOverflowError) {
                        ROOT_LOGGER.errorBootingContainer(t, bootStackSize, BOOT_STACK_SIZE_PROPERTY);
                    } else {
                        ROOT_LOGGER.errorBootingContainer(t);
                    }
                }

            }
        }, "Controller Boot Thread", bootStackSize);
        bootThread.start();
    }

    /**
     * Boot the controller.  Called during service start.
     *
     * @param context the boot context
     * @throws ConfigurationPersistenceException
     *          if the configuration failed to be loaded
     */
    protected void boot(final BootContext context) throws ConfigurationPersistenceException {
        boot(configurationPersister.load(), false);
        finishBoot();
    }

    protected boolean boot(List<ModelNode> bootOperations, boolean rollbackOnRuntimeFailure) throws ConfigurationPersistenceException {
        return controller.boot(bootOperations, OperationMessageHandler.logging, ModelController.OperationTransactionControl.COMMIT, false);
    }

    protected void finishBoot() throws ConfigurationPersistenceException {
        controller.finishBoot();
        configurationPersister.successfulBoot();
    }

    public void stop(final StopContext context) {
        controller = null;
    }

    public ModelController getValue() throws IllegalStateException, IllegalArgumentException {
        final ModelController controller = this.controller;
        if (controller == null) {
            throw new IllegalStateException();
        }
        return controller;
    }

    public InjectedValue<ExecutorService> getExecutorServiceInjector() {
        return injectedExecutorService;
    }

    protected void setConfigurationPersister(final ConfigurationPersister persister) {
        this.configurationPersister = persister;
    }

    protected abstract void initModel(Resource rootResource, ManagementResourceRegistration rootRegistration);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3121.java