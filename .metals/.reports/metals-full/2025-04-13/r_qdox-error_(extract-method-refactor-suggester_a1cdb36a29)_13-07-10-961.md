error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9710.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9710.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9710.java
text:
```scala
c@@ontext.completeStep(OperationContext.RollbackHandler.NOOP_ROLLBACK_HANDLER);

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

import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.AbstractServiceListener;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceListener;
import org.jboss.msc.service.ServiceName;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class ServiceVerificationHandler extends AbstractServiceListener<Object> implements ServiceListener<Object>, OperationStepHandler {
    private final Set<ServiceController<?>> set = new HashSet<ServiceController<?>>();
    private final Set<ServiceController<?>> failed = new HashSet<ServiceController<?>>();
    private final Set<ServiceController<?>> problem = new HashSet<ServiceController<?>>();
    private int outstanding;

    public synchronized void execute(final OperationContext context, final ModelNode operation) {

        // Wait for services to reach rest state.
        // Additionally...
        // Temp workaround to MSC issue of geting STARTING_TO_STARTED notification for parent service before
        // getting the PROBLEM_TO_START_REQUESTED notification for dependent services. If there are
        // services in PROBLEM state, wait up to 100ms to give them a chance to transition to START_REQUESTED.

        long start = 0;
        long settleTime = 100;
        while (outstanding > 0 || (settleTime > 0 && !problem.isEmpty())) {
            try {
                long wait = outstanding > 0 ? 0 : settleTime;
                wait(wait);
                if (outstanding == 0) {
                    if (start == 0) {
                        start = System.currentTimeMillis();
                    } else {
                        settleTime -= System.currentTimeMillis() - start;
                    }
                } else {
                    start = 0;
                    settleTime = 100;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                context.getFailureDescription().set("Operation cancelled");
                context.completeStep();
                return;
            }
        }

        if (!failed.isEmpty() || !problem.isEmpty()) {
            final ModelNode failureDescription = context.getFailureDescription();
            ModelNode failedList = null;
            for (ServiceController<?> controller : failed) {
                if (failedList == null) {
                    failedList = failureDescription.get("Failed services");
                }
                failedList.get(controller.getName().getCanonicalName()).set(controller.getStartException().toString());
            }
            ModelNode problemList = null;
            for (ServiceController<?> controller : problem) {
                if (!controller.getImmediateUnavailableDependencies().isEmpty()) {
                    if (problemList == null) {
                        problemList = failureDescription.get("Services with missing/unavailable dependencies");
                    }
                    final StringBuilder problem = new StringBuilder();
                    problem.append(controller.getName().getCanonicalName());
                    problem.append(" missing [ ");
                    for(Iterator<ServiceName> i = controller.getImmediateUnavailableDependencies().iterator(); i.hasNext(); ) {
                        ServiceName missing = i.next();
                        problem.append(missing.getCanonicalName());
                        if(i.hasNext()) {
                            problem.append(", ");
                        }
                    }
                    problem.append(" ]");
                    problemList.add(problem.toString());
                }
            }
            if (context.isRollbackOnRuntimeFailure()) {
                context.setRollbackOnly();
            }
        }
        for (ServiceController<?> controller : set) {
            controller.removeListener(this);
        }
        context.completeStep();
    }

    public synchronized void listenerAdded(final ServiceController<?> controller) {
        set.add(controller);
        if (!controller.getSubstate().isRestState()) {
            outstanding++;
        }
    }

    public synchronized void transition(final ServiceController<?> controller, final ServiceController.Transition transition) {
        switch (transition) {
            case STARTING_to_START_FAILED: {
                failed.add(controller);
                break;
            }
            case START_FAILED_to_STARTING: {
                failed.remove(controller);
                break;
            }
            case START_REQUESTED_to_PROBLEM: {
                problem.add(controller);
                break;
            }
            case PROBLEM_to_START_REQUESTED: {
                problem.remove(controller);
                break;
            }
        }
        if (transition.leavesRestState()) {
            outstanding++;
        } else if (transition.entersRestState()) {
            if (outstanding-- == 1) {
                notifyAll();
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9710.java