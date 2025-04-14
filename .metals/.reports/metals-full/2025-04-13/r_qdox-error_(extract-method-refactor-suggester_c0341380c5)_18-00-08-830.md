error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13903.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13903.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13903.java
text:
```scala
public v@@oid applyUpdateBootAction(BootUpdateContext updateContext) {

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

/**
 * An update that applies to a server model and/or a running server instance.
 * <p>
 * An update can optionally indicate that when applying it to a running server, a restart of the server
 * is required for the update to take full effect.  However such updates <b>may</b> still make a runtime change
 * as long as that change does not disrupt the current operation of the server.  In addition, the runtime change
 * should not mandatorily depend on the non-runtime component of any updates which in turn require a restart.  If
 * a restart-required change cannot be executed safely at runtime, then it should not be made at all.
 *
 * @param <R> the type of result that is returned by this update type
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public abstract class AbstractServerModelUpdate<R> extends AbstractModelUpdate<ServerModel, R> {

    private static final long serialVersionUID = 1977647714406421073L;

    private final boolean requiresRestart;
    private final boolean isDeploymentUpdate;

    /**
     * Construct a new instance.  The {@code requiresRestart} flag is set to {@code false}.
     */
    protected AbstractServerModelUpdate() {
        this(false, false);
    }

    /**
     * Construct a new instance.
     *
     * @param requiresRestart {@code true} if this update requires a restart, {@code false} otherwise
     * @param isDeployment {@code true} if the update should be applied to the deployment batch
     */
    protected AbstractServerModelUpdate(final boolean requiresRestart, final boolean isDeployment) {
        this.requiresRestart = requiresRestart;
        this.isDeploymentUpdate = isDeployment;
    }

    /** {@inheritDoc} */
    @Override
    public final Class<ServerModel> getModelElementType() {
        return ServerModel.class;
    }

    /**
     * Determine whether this update requires a restart to take effect.
     *
     * @return {@code true} if a restart is required
     */
    public final boolean requiresRestart() {
        return requiresRestart;
    }

    /**
     * Determine whether this update should be applied to the deployment service batch.
     *
     * @return {@code true} if the update should be applied to the deployment batch
     */
    public boolean isDeploymentUpdate() {
        return isDeploymentUpdate;
    }

    /** {@inheritDoc} */
    @Override
    protected abstract void applyUpdate(ServerModel element) throws UpdateFailedException;

    /**
     * Apply this update to a running service container.  The given result handler is called with the result of the
     * application.  By default, this method does nothing but report success.
     *
     * @param updateContext the update context
     * @param resultHandler the handler to call back with the result
     * @param param the parameter value to pass to the result handler
     */
    public <P> void applyUpdate(UpdateContext updateContext, UpdateResultHandler<? super R, P> resultHandler, P param) {
        resultHandler.handleSuccess(null, param);
    }

    /**
     * Apply the boot action for this update.  This action is only executed when the update is processed during
     * server startup.  By default, this method simply invokes {@link #applyUpdate(UpdateContext, UpdateResultHandler, Object)}
     * directly, but this behavior should be overriden if a different action must be taken at boot time.
     *
     * @param updateContext the update context
     */
    public void applyUpdateBootAction(UpdateContext updateContext) {
        applyUpdate(updateContext, UpdateResultHandler.NULL, null);
    }

    /** {@inheritDoc} */
    @Override
    protected final AbstractServerModelUpdate<R> getServerModelUpdate() {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public abstract AbstractServerModelUpdate<?> getCompensatingUpdate(ServerModel original);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13903.java