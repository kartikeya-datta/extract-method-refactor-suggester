error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4400.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4400.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4400.java
text:
```scala
r@@eturn new ServerModelDeploymentAdd(uniqueName, runtimeName, hash);

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

package org.jboss.as.model;

import org.jboss.as.deployment.client.api.server.ServerDeploymentActionResult;


/**
 * Update used when adding deployment element to a ServerGroup.
 *
 * @author Brian Stansberry
 */
public class ServerGroupDeploymentAdd extends AbstractModelUpdate<ServerGroupElement, ServerDeploymentActionResult> {
    private static final long serialVersionUID = 5773083013951607950L;

    private final String uniqueName;
    private final String runtimeName;
    private final byte[] hash;
    private final boolean start;

    /**
     * Construct a new instance.
     *
     * @param uniqueName the user-supplied unique name of the deployment
     * @param runtimeName the name by which the deployment should be known
     *                    in the runtime
     * @param hash the hash of the deployment
     * @param start whether the deployment should be started
     *              (i.e. deployed into the runtime) by default if
     *              it is mapped to a server
     *
     */
    public ServerGroupDeploymentAdd(final String uniqueName, final String runtimeName, final byte[] hash, final boolean start) {
        if (uniqueName == null)
            throw new IllegalArgumentException("uniqueName is null");
        if (runtimeName == null)
            throw new IllegalArgumentException("runtimeName is null");
        if (hash == null)
            throw new IllegalArgumentException("hash is null");
        this.uniqueName = uniqueName;
        this.runtimeName = runtimeName;
        this.hash = hash;
        this.start = start;
    }

    /**
     * Get the user-supplied unique name of the deployment.
     *
     * @return the unique name. Will not be {@code null}
     */
    public String getUniqueName() {
        return uniqueName;
    }

    /**
     * Gets the name by which the deployment should be known in the runtime.
     *
     * @return the runtime name. Will not be {@code null}
     */
    public String getRuntimeName() {
        return runtimeName;
    }

    /**
     * Gets the hash of the deployment.
     *
     * @return the hash. Will not be {@code null}
     */
    public byte[] getHash() {
        return hash;
    }

    /**
     * Gets whether the deployment should be started (i.e. deployed into the
     * runtime) by default if it is mapped to a server
     *
     * @return <code>true</code> if the deployment should be deployed by default
     */
    public boolean isStart() {
        return start;
    }

    @Override
    public ServerGroupDeploymentRemove getCompensatingUpdate(ServerGroupElement original) {
        return new ServerGroupDeploymentRemove(uniqueName);
    }

    @Override
    protected ServerModelDeploymentAdd getServerModelUpdate() {
        return new ServerModelDeploymentAdd(uniqueName, runtimeName, hash, start);
    }

    @Override
    protected void applyUpdate(ServerGroupElement element) throws UpdateFailedException {
        if (element.getDeployment(uniqueName) != null) {
            throw new UpdateFailedException("Deployment " + uniqueName + " already added");
        }
        element.addDeployment(uniqueName, runtimeName, hash, start);
    }

    @Override
    public Class<ServerGroupElement> getModelElementType() {
        return ServerGroupElement.class;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4400.java