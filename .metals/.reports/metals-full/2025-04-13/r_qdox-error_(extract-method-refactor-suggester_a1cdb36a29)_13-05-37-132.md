error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6303.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6303.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6303.java
text:
```scala
final S@@erviceTarget target = updateContext.getServiceTarget().subTarget();

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

import java.util.concurrent.TimeUnit;

import org.jboss.as.deployment.scanner.DeploymentScannerService;
import org.jboss.msc.service.ServiceTarget;

/**
 * Update adding a new {@code DeploymentRepositoryElement} to a {@code ServerModel}.
 *
 * @author Emanuel Muckenhuber
 */
public class ServerDeploymentRepositoryAdd extends AbstractServerModelUpdate<Void> {

    private static final long serialVersionUID = -1611269698053636197L;
    private static final String DEFAULT_NAME = "default";

    private final String path;
    private String name;
    private String relativeTo;
    private int interval = 0;
    private boolean enabled = true;

    public ServerDeploymentRepositoryAdd(String path, int interval, boolean enabled) {
        super(false, true);
        this.path = path;
        this.interval = interval;
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRelativePath() {
        return relativeTo;
    }
    public void setRelativeTo(String relativePath) {
        this.relativeTo = relativePath;
    }

    /** {@inheritDoc} */
    @Override
    protected void applyUpdate(ServerModel element) throws UpdateFailedException {
        final String repositoryName = repositoryName();
        if(! element.addDeploymentRepository(repositoryName)) {
            throw new UpdateFailedException("duplicate deployment repository " + repositoryName);
        }
        final DeploymentRepositoryElement repository = element.getDeploymentRepository(repositoryName);
        repository.setInterval(interval);
        repository.setEnabled(enabled);
        repository.setPath(path);
        repository.setRelativeTo(relativeTo);
    }

    /** {@inheritDoc} */
    @Override
    public AbstractServerModelUpdate<?> getCompensatingUpdate(ServerModel original) {
        return new ServerDeploymentRepositoryRemove(repositoryName());
    }

    /** {@inheritDoc} */
    @Override
    public <P> void applyUpdate(UpdateContext updateContext, UpdateResultHandler<? super Void,P> resultHandler, P param) {
        final ServiceTarget target = updateContext.getBatchBuilder().subTarget();
        target.addListener(new UpdateResultHandler.ServiceStartListener<P>(resultHandler, param));
        DeploymentScannerService.addService(target, repositoryName(), relativeTo, path, interval, TimeUnit.MILLISECONDS, enabled);
    }

    private String repositoryName() {
        return name != null ? name : DEFAULT_NAME;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6303.java