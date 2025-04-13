error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/156.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/156.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/156.java
text:
```scala
public static final S@@tring BUSY_PROPERTY = "SITE_BUSY"; //$NON-NLS-1$

/**********************************************************************
 * Copyright (c) 2003,2004 IBM Corporation and others. All rights reserved.   This
 * program and the accompanying materials are made available under the terms of
 * the Common Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: 
 * IBM - Initial API and implementation
 **********************************************************************/
package org.eclipse.ui.progress;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.util.IPropertyChangeListener;

/**
 * IWorkbenchPartProgressService is an IProgressService that adds API for 
 * jobs that change the state in a PartSite while they are being 
 * run.
 * 
 * WorkbenchParts may access an instance of IWorkbenchSiteProgressService
 * by calling
 * <code>getSite.getAdapter(IWorkbenchSiteProgressService.class);</code>
 * 
 * This interface is not intended to be implemented by client
 * plug-ins.
 * 
 * @see WorkbenchPart.getJobChangeListener()
 * @since 3.0
 */
public interface IWorkbenchSiteProgressService extends IProgressService {
	
	/**
	 * The property that is sent with busy notifications.
	 */
	public static final String BUSY_PROPERTY = "SITE_BUSY";
	
	/**
	 * Jobs scheduled with this method will cause the part's presentation 
	 * to be changed to indicate that the part is busy and in a transient 
	 * state until the job completes. Parts can also add customized busy 
	 * indication by overriding <code>WorkbenchPart.setBusy()</code>.
	 * If useHalfBusyCursor is true then the cursor will change to
	 * the half busy cursor for the duration of the job.
	 * @param job. The job to schedule
	 * @param delay. The delay in scheduling.
	 * @param useHalfBusyCursor. A boolean to indicate if the half busy
	 * 		cursor should be used while this job is running.
	 * @see Job.schedule(long)
	 */
	public void schedule(Job job, long delay, boolean useHalfBusyCursor);
	
	/**
	 * Jobs scheduled with this method will cause the part's presentation 
	 * to be changed to indicate that the part is busy and in a transient 
	 * state until the job completes. Parts can also add customized busy 
	 * indication by overriding <code>WorkbenchPart.setBusy</code>.
	 * @param job. The job to schedule
	 * @param delay. The delay in scheduling.
	 * @see Job.schedule(long)
	 */
	public void schedule(Job job, long delay);
	
	/**
	 * Jobs scheduled with this method will cause the part's presentation 
	 * to be changed to indicate that the part is busy and in a transient 
	 * state until the job completes. Parts can also add customized busy 
	 * indication by overriding <code>WorkbenchPart.setBusy</code>.
	 * @param job. The job to schedule
	 * @see Job.schedule()
	 */
	public void schedule(Job job);
	
	/**
	 * Use the half busy cursor in this part during the execution
	 * of this job.
	 * @param job
	 * @deprecated Use schedule (Job,long,boolean) instead
	 */
	public void useHalfBusyCursor(Job job);
	
	/**
	 * Add an IPropertyChangeListener to the list of listeners
	 * on the receiver.
	 * @param listener IPropertyChangeListener
	 */
	public void addPropertyChangeListener(IPropertyChangeListener listener);
	
	/**
	 * Remove an IPropertyChangeListener to the list of listeners
	 * on the receiver.
	 * @param listener IPropertyChangeListener
	 */
	public void removePropertyChangeListener(IPropertyChangeListener listener);
	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/156.java