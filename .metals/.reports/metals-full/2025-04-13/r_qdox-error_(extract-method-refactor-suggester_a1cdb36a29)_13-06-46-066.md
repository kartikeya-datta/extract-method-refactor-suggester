error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1663.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1663.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1663.java
text:
```scala
public v@@oid handleEvent(IRemoteCallEvent event) {

package org.eclipse.ecf.remoteservice;

import org.eclipse.ecf.remoteservice.events.*;

/**
 * Abstract implementer of IRemoteCallListener.  This utility class may be used
 * to simplify the implementation of IRemoteCallListener.
 * 
 * @since 3.0
 */
public abstract class AbstractRemoteCallListener implements IRemoteCallListener {

	protected IRemoteCall remoteCall;
	protected IRemoteServiceReference remoteReference;

	public void handleServiceEvent(IRemoteServiceEvent event) {
		if (event instanceof IRemoteCallStartEvent)
			this.handleRemoteCallStartEvent((IRemoteCallStartEvent) event);
		else if (event instanceof IRemoteCallCompleteEvent)
			this.handleRemoteCallCompleteEvent((IRemoteCallCompleteEvent) event);
	}

	protected IRemoteCall getRemoteCall() {
		return remoteCall;
	}

	protected IRemoteServiceReference getRemoteServiceReference() {
		return remoteReference;
	}

	protected void handleRemoteCallCompleteEvent(IRemoteCallCompleteEvent event) {
		if (event.hadException())
			handleRemoteCallException(event.getException());
		else
			handleRemoteCallComplete(event.getResponse());
		// In either case, null out references
		remoteCall = null;
		remoteReference = null;
	}

	/**
	 * Handle remote call complete.  If the remote call completes successfully,
	 * this method will then be called with the given result of the call passed
	 * as the parameter.  If the remote call throws an exception, then {@link #handleRemoteCallException(Throwable)}
	 * will be called instead.
	 * 
	 * @param result the result of the remote call.  May be <code>null</code>.
	 * @see #handleRemoteCallException(Throwable)
	 */
	protected abstract void handleRemoteCallComplete(Object result);

	/**
	 * Handle remote call exception.  If the remote call does not complete successfully,
	 * this method will be called with the Throwable exception that occurred.  If
	 * it did complete successfully, then 
	 * 
	 * @param exception the Throwable that occurred during execution of the remote call. 
	 * Will not be <code>null</code>.
	 */
	protected abstract void handleRemoteCallException(Throwable exception);

	protected void handleRemoteCallStartEvent(IRemoteCallStartEvent event) {
		remoteCall = event.getCall();
		remoteReference = event.getReference();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1663.java