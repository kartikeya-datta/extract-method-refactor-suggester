error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3939.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3939.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3939.java
text:
```scala
o@@p.get("address").add("host", host);

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

package org.jboss.as.controller.client.helpers.domain.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.Operation;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.OperationResult;
import org.jboss.as.controller.client.ResultHandler;
import org.jboss.as.controller.client.helpers.domain.DomainClient;
import org.jboss.as.controller.client.helpers.domain.DomainDeploymentManager;
import org.jboss.as.controller.client.helpers.domain.ServerIdentity;
import org.jboss.as.controller.client.helpers.domain.ServerStatus;
import org.jboss.dmr.ModelNode;

/**
 * Domain client implementation.
 *
 * @author John Bailey
 */
public class DomainClientImpl implements DomainClient {

    private volatile DomainDeploymentManager deploymentManager;
    private final ModelControllerClient delegate;

    public DomainClientImpl(InetAddress address, int port) {
        this.delegate = ModelControllerClient.Factory.create(address, port);
    }

    @Override
    public OperationResult execute(ModelNode operation, ResultHandler handler) {
        return execute(OperationBuilder.Factory.create(operation).build(), handler);
    }

    @Override
    public ModelNode execute(ModelNode operation) throws CancellationException, IOException {
        return execute(OperationBuilder.Factory.create(operation).build());
    }

    @Override
    public OperationResult execute(Operation operation, ResultHandler handler) {
        return delegate.execute(operation, handler);
    }

    @Override
    public ModelNode execute(Operation operation) throws CancellationException, IOException {
        return delegate.execute(operation);
    }

    @Override
    public byte[] addDeploymentContent(InputStream stream) {
        ModelNode op = new ModelNode();
        op.get("operation").set("upload-deployment-stream");
        op.get("input-stream-index").set(0);
        Operation operation = OperationBuilder.Factory.create(op).addInputStream(stream).build();
        ModelNode result = executeForResult(operation);
        return result.asBytes();
    }

    @Override
    public DomainDeploymentManager getDeploymentManager() {
        if (deploymentManager == null) {
            synchronized (this) {
                if (deploymentManager == null) {
                    deploymentManager = new DomainDeploymentManagerImpl(this);
                }
            }
        }
        return deploymentManager;
    }

    @Override
    public List<String> getHostControllerNames() {
        ModelNode op = new ModelNode();
        op.get("operation").set("read-children-names");
        op.get("child-type").set("host");
        ModelNode result = executeForResult(OperationBuilder.Factory.create(op).build());
        List<String> hosts = new ArrayList<String>();
        for (ModelNode host : result.asList()) {
            hosts.add(host.asString());
        }
        return hosts;
    }

    @Override
    public Map<ServerIdentity, ServerStatus> getServerStatuses() {
        Map<ServerIdentity, ServerStatus> result = new HashMap<ServerIdentity, ServerStatus>();
        List<String> hosts = getHostControllerNames();
        for (String host : hosts) {
            Set<String> servers = getServerNames(host);
            for (String server : servers) {
                ModelNode address = new ModelNode();
                address.add("host", host);
                address.add("server-config", server);
                String group = readAttribute("group", address).asString();
                ServerStatus status = Enum.valueOf(ServerStatus.class, readAttribute("status", address).asString());
                ServerIdentity id = new ServerIdentity(host, group, server);
                result.put(id, status);
            }

        }
        return result;
    }

    private Set<String> getServerNames(String host) {
        ModelNode op = new ModelNode();
        op.get("operation").set("read-children-names");
        op.get("child-type").set("server-config");
        op.get("address").set("host", host);
        ModelNode result = executeForResult(OperationBuilder.Factory.create(op).build());
        Set<String> servers = new HashSet<String>();
        for (ModelNode server : result.asList()) {
            servers.add(server.asString());
        }
        return servers;
    }

    private ModelNode readAttribute(String name, ModelNode address) {
        ModelNode op = new ModelNode();
        op.get("operation").set("read-attribute");
        op.get("address").set(address);
        op.get("name").set(name);
        return executeForResult(OperationBuilder.Factory.create(op).build());
    }

    @Override
    public ServerStatus startServer(String hostControllerName, String serverName) {

        final ModelNode op = new ModelNode();
        op.get("operation").set("start");
        ModelNode address = op.get("address");
        address.add("host", hostControllerName);
        address.add("server", serverName);
        ModelNode result = executeForResult(OperationBuilder.Factory.create(op).build());
        String status = result.asString();
        return Enum.valueOf(ServerStatus.class, status);
    }

    @Override
    public ServerStatus stopServer(String hostControllerName, String serverName, long gracefulShutdownTimeout, TimeUnit timeUnit) {
//        long ms = gracefulShutdownTimeout < 0 ? - 1 : timeUnit.toMillis(gracefulShutdownTimeout);

        final ModelNode op = new ModelNode();
        op.get("operation").set("stop");
        ModelNode address = op.get("address");
        address.add("host", hostControllerName);
        address.add("server", serverName);
        // FIXME add graceful shutdown
        ModelNode result = executeForResult(OperationBuilder.Factory.create(op).build());
        String status = result.asString();
        return Enum.valueOf(ServerStatus.class, status);
    }

    @Override
    public ServerStatus restartServer(String hostControllerName, String serverName, long gracefulShutdownTimeout, TimeUnit timeUnit) {
//        long ms = gracefulShutdownTimeout < 0 ? - 1 : timeUnit.toMillis(gracefulShutdownTimeout);

        final ModelNode op = new ModelNode();
        op.get("operation").set("restart");
        ModelNode address = op.get("address");
        address.add("host", hostControllerName);
        address.add("server", serverName);
        // FIXME add graceful shutdown
        ModelNode result = executeForResult(OperationBuilder.Factory.create(op).build());
        String status = result.asString();
        return Enum.valueOf(ServerStatus.class, status);
    }

    boolean isDeploymentNameUnique(final String deploymentName) {
        final ModelNode op = new ModelNode();
        op.get("operation").set("read-children-names");
        op.get("child-type").set("deployment");
        final ModelNode result = executeForResult(OperationBuilder.Factory.create(op).build());
        final Set<String> deploymentNames = new HashSet<String>();
        if (result.isDefined()) {
            for (ModelNode node : result.asList()) {
                deploymentNames.add(node.asString());
            }
        }
        return !deploymentNames.contains(deploymentName);
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }

    ModelNode executeForResult(Operation op) {
        try {
            ModelNode result = delegate.execute(op);
            if (result.hasDefined("outcome") && "success".equals(result.get("outcome").asString())) {
                return result.get("result");
            }
            else if (result.hasDefined("failure-description")) {
                throw new RuntimeException(result.get("failure-description").toString());
            }
            else if (result.hasDefined("domain-failure-description")) {
                throw new RuntimeException(result.get("domain-failure-description").toString());
            }
            else if (result.hasDefined("host-failure-descriptions")) {
                throw new RuntimeException(result.get("host-failure-descriptions").toString());
            }
            else {
                throw new RuntimeException("Operation outcome is " + result.get("outcome").asString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3939.java