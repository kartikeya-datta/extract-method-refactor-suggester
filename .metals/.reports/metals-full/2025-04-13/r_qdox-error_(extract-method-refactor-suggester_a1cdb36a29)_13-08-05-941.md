error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6306.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6306.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6306.java
text:
```scala
final B@@atchBuilder batch = updateContext.getServiceTarget();

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.model.socket;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jboss.as.model.AbstractModelElementUpdate;
import org.jboss.as.model.UpdateContext;
import org.jboss.as.model.UpdateFailedException;
import org.jboss.as.model.UpdateResultHandler;
import org.jboss.as.services.net.NetworkInterfaceBinding;
import org.jboss.as.services.net.NetworkInterfaceService;
import org.jboss.msc.service.BatchBuilder;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceController.Mode;

/**
 * A {@code InterfaceElement} update.
 *
 * @author Emanuel Muckenhuber
 */
public final class InterfaceAdd extends AbstractNetworkInterfaceUpdate {

    private static final long serialVersionUID = 2597565379666344003L;
    private final String name;
    private boolean anyLocalV4;
    private boolean anyLocalV6;
    private boolean anyLocal;
    private Collection<AbstractInterfaceCriteriaElement<?>> interfaceCriteria = Collections.emptySet();

    public InterfaceAdd(String name, boolean anyLocalV4, boolean anyLocalV6, boolean anyLocal, Collection<AbstractInterfaceCriteriaElement<?>> interfaceCriteria) {
        this.name = name;
        this.anyLocalV4 = anyLocalV4;
        this.anyLocalV6 = anyLocalV6;
        this.anyLocal = anyLocal;
        this.interfaceCriteria = interfaceCriteria;
    }

    public InterfaceAdd(final InterfaceElement networkInterface) {
        name = networkInterface.getName();
        anyLocal = networkInterface.isAnyLocalAddress();
        anyLocalV4 = networkInterface.isAnyLocalV4Address();
        anyLocalV6 = networkInterface.isAnyLocalV6Address();
        interfaceCriteria = networkInterface.getCriteriaElements();
    }

    public String getName() {
        return name;
    }

    public boolean isFullySpecified() {
        return anyLocal || anyLocalV4 || anyLocalV6 || (interfaceCriteria != null && interfaceCriteria.size() > 0);
    }

    /** {@inheritDoc} */
    @Override
    public void applyUpdate(InterfaceElement element) throws UpdateFailedException {
        if(interfaceCriteria != null && interfaceCriteria.size() > 0) {
            for(AbstractInterfaceCriteriaElement<?> criteria : interfaceCriteria) {
                element.addCriteria(criteria);
            }
        }
        if(anyLocal) element.setAnyLocal(anyLocal);
        if(anyLocalV4) element.setAnyLocalV4(anyLocalV4);
        if(anyLocalV6) element.setAnyLocalV6(anyLocalV6);
    }

    /** {@inheritDoc} */
    @Override
    public AbstractModelElementUpdate<InterfaceElement> getCompensatingUpdate(InterfaceElement original) {
        return null;
    }


    public <P> void applyUpdate(UpdateContext updateContext, UpdateResultHandler<? super Void,P> resultHandler, P param) {
        final BatchBuilder batch = updateContext.getBatchBuilder();
        batch.addService(NetworkInterfaceService.JBOSS_NETWORK_INTERFACE.append(name), createInterfaceService())
            .addListener(new UpdateResultHandler.ServiceStartListener<P>(resultHandler, param))
            .setInitialMode(Mode.ON_DEMAND)
            .install();
    }

    /**
     * Create a {@link NetworkInterfaceService}.
     *
     * @return the interface service
     */
    Service<NetworkInterfaceBinding> createInterfaceService() {
        Set<InterfaceCriteria> criteria = new HashSet<InterfaceCriteria>();
        for(final AbstractInterfaceCriteriaElement<?> element : interfaceCriteria) {
            criteria.add(element.getInterfaceCriteria());
        }
        return new NetworkInterfaceService(name, anyLocalV4, anyLocalV6, anyLocal, new OverallInterfaceCriteria(criteria));
    }

    /** Overall interface criteria. */
    static final class OverallInterfaceCriteria implements InterfaceCriteria {
        private static final long serialVersionUID = -5417786897309925997L;
        private final Set<InterfaceCriteria> interfaceCriteria;

        public OverallInterfaceCriteria(Set<InterfaceCriteria> criteria) {
            interfaceCriteria = criteria;
        }

        /** {@inheritDoc} */
        public boolean isAcceptable(NetworkInterface networkInterface, InetAddress address) throws SocketException {
            for (InterfaceCriteria criteria : interfaceCriteria) {
                if (! criteria.isAcceptable(networkInterface, address))
                    return false;
            }
            return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6306.java