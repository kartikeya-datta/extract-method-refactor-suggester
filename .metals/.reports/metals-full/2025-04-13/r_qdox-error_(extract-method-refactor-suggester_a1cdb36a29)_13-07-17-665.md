error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14296.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14296.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14296.java
text:
```scala
r@@eturn hostController.getOldHostModel();

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

package org.jboss.as.host.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.as.domain.client.api.HostUpdateResult;
import org.jboss.as.domain.client.api.ServerIdentity;
import org.jboss.as.domain.client.api.ServerStatus;
import org.jboss.as.domain.controller.HostControllerClient;
import org.jboss.as.domain.controller.ModelUpdateResponse;
import org.jboss.as.model.AbstractDomainModelUpdate;
import org.jboss.as.model.AbstractHostModelUpdate;
import org.jboss.as.model.AbstractServerModelUpdate;
import org.jboss.as.model.DomainModel;
import org.jboss.as.model.HostModel;
import org.jboss.as.model.ServerModel;
import org.jboss.as.model.UpdateFailedException;
import org.jboss.as.model.UpdateResultHandlerResponse;

/**
 * A client to integrate with a local domain controller instance.
 *
 * @author John Bailey
 */
public class NewLocalDomainControllerClient implements HostControllerClient {

    private final NewHostController hostController;

    /**
     * Create an instance with a host controller.
     *
     * @param hostController The local host controller instance.
     */
    public NewLocalDomainControllerClient(final NewHostController hostController) {
        this.hostController = hostController;
    }

    /** {@inheritDoc} */
    @Override
    public String getId() {
        return hostController.getName();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isActive() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void updateDomainModel(final DomainModel domain) {
        hostController.setDomain(domain);
    }

    @Override
    public HostModel getHostModel() {
        return hostController.getHostModel();
    }

    /** {@inheritDoc} */
    @Override
    public List<HostUpdateResult<?>> updateHostModel(List<AbstractHostModelUpdate<?>> updates) {
        return hostController.applyHostUpdates(updates);
    }

    /** {@inheritDoc} */
    @Override
    public List<ModelUpdateResponse<List<ServerIdentity>>> updateDomainModel(List<AbstractDomainModelUpdate<?>> updates) {
        final List<ModelUpdateResponse<List<ServerIdentity>>> responses = new ArrayList<ModelUpdateResponse<List<ServerIdentity>>>(updates.size());
        for(AbstractDomainModelUpdate<?> update : updates) {
            ModelUpdateResponse<List<ServerIdentity>> response = executeUpdate(update);
            responses.add(response);
            if (!response.isSuccess())
                break;
        }
        return responses;
    }

    @Override
    public Map<ServerIdentity, ServerStatus> getServerStatuses() {
        return hostController.getServerStatuses();
    }

    @Override
    public ServerModel getServerModel(String serverName) {
        return hostController.getServerModel(serverName);
    }

    @Override
    public List<UpdateResultHandlerResponse<?>> updateServerModel(final String serverName, final List<AbstractServerModelUpdate<?>> updates, final boolean allowOverallRollback) {
        return hostController.applyServerUpdates(serverName, updates, allowOverallRollback);
    }

    @Override
    public ServerStatus restartServer(String serverName, long gracefulTimeout) {
        return hostController.restartServer(serverName, gracefulTimeout);
    }

    @Override
    public ServerStatus startServer(String serverName) {
        return hostController.startServer(serverName);
    }

    @Override
    public ServerStatus stopServer(String serverName, long gracefulTimeout) {
        return hostController.stopServer(serverName, gracefulTimeout);
    }

    private ModelUpdateResponse<List<ServerIdentity>> executeUpdate(AbstractDomainModelUpdate<?> domainUpdate) {
        try {
            final List<ServerIdentity> result = hostController.getModelManager().applyDomainModelUpdate(domainUpdate, false);
            return new ModelUpdateResponse<List<ServerIdentity>>(result);
        } catch (UpdateFailedException e) {
            return new ModelUpdateResponse<List<ServerIdentity>>(e);
        }
    }

    // TODO use executeUpdate(AbstractHostModelUpdate<?> hostUpdate) with a "HostUpdateApplier" API
    private ModelUpdateResponse<List<ServerIdentity>> executeUpdate(AbstractHostModelUpdate<?> hostUpdate) {
        try {
            final List<ServerIdentity> result = hostController.getModelManager().applyHostModelUpdate(hostUpdate);
            return new ModelUpdateResponse<List<ServerIdentity>>(result);
        } catch (UpdateFailedException e) {
            return new ModelUpdateResponse<List<ServerIdentity>>(e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14296.java