error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17176.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17176.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[12,1]

error in qdox parser
file content:
```java
offset: 678
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17176.java
text:
```scala
public interface IIncomingFileTransferReceiveStartEvent extends IIncomingFileTransferEvent {

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 *               Cloudsmith, Inc. - additional API and implementation
 ******************************************************************************/
p@@ackage org.eclipse.ecf.filetransfer.events;

import java.io.*;
import org.eclipse.ecf.filetransfer.*;
import org.eclipse.ecf.filetransfer.identity.IFileID;

/**
 * Event sent to {@link IFileTransferListener} associated with
 * {@link IIncomingFileTransfer} instances
 * 
 */
public interface IIncomingFileTransferReceiveStartEvent extends IFileTransferEvent {

	/**
	 * Get IFileID for incoming file
	 * 
	 * @return IFileID for this file transfer event. Will not be
	 *         <code>null</code>.
	 */
	public IFileID getFileID();

	/**
	 * Get incoming file transfer object by specifying a local File instance to
	 * save the received contents to.
	 * 
	 * @param localFileToSave
	 *            the file on the local file system to receive and save the
	 *            remote file. Must not be <code>null</code>. If the file
	 *            already exists, its content will be overwritten by any data
	 *            received.
	 * @return IIncomingFileTransfer the incoming file transfer object. Will not
	 *         be <code>null</code>.
	 * @throws IOException
	 *             if localFileToSave cannot be opened for writing
	 */
	public IIncomingFileTransfer receive(File localFileToSave) throws IOException;

	/**
	 * Just like {@link #receive(File)} but this method also give the caller
	 * a chance to provide a factory that creates the job that will perform the
	 * actual file transfer. The intended use for this is when the user of the
	 * framework needs more elaborate control over such jobs such as waiting for a
	 * group of parallel file transfers to complete. Such functionality can for
	 * instance exploit the Eclipse runtime concept of Job families.
	 * 
	 * @param localFileToSave
	 *            the file on the local file system to receive and save the
	 *            remote file. Must not be <code>null</code>. If the file
	 *            already exists, its content will be overwritten by any data
	 *            received.
	 * @param fileTransferJob A subclass of {@link FileTransferJob} to use to run the actual transfer.  If
	 *         <code>null</code>, provider will create default implementation.  NOTE: the given job should
	 *         *not* be scheduled/started prior to being provided as a parameter to this method.
	 * @return IIncomingFileTransfer the incoming file transfer object. NOTE:
	 *         the caller is responsible for calling
	 *         {@link OutputStream#close()} on the OutputStream provided. If the
	 *         stream provided is buffered, then
	 *         {@link BufferedOutputStream#flush()} should be called to
	 *         guaranteed that the data received is actually written to the
	 *         given OutputStream.
	 * @throws IOException
	 *             if streamToStore cannot be opened for writing
	 *             
	 * @since 2.0.0 milestone 6
	 */
	public IIncomingFileTransfer receive(File localFileToSave, FileTransferJob fileTransferJob) throws IOException;

	/**
	 * Get incoming file transfer by specifying an OutputStream instance to save
	 * the received contents to. NOTE: the caller is responsible for calling
	 * {@link OutputStream#close()} on the OutputStream provided. If the stream
	 * provided is buffered, then {@link BufferedOutputStream#flush()} should be
	 * called to guaranteed that the data received is actually written to the
	 * given OutputStream.
	 * 
	 * @param streamToStore
	 *            the output stream to store the incoming file. Must not be
	 *            <code>null</code>.
	 * @return IIncomingFileTransfer the incoming file transfer object. NOTE:
	 *         the caller is responsible for calling
	 *         {@link OutputStream#close()} on the OutputStream provided. If the
	 *         stream provided is buffered, then
	 *         {@link BufferedOutputStream#flush()} should be called to
	 *         guaranteed that the data received is actually written to the
	 *         given OutputStream.
	 * @throws IOException
	 *             if streamToStore cannot be opened for writing
	 */
	public IIncomingFileTransfer receive(OutputStream streamToStore) throws IOException;

	/**
	 * Just like {@link #receive(OutputStream)} but this method also give the caller
	 * a chance to provide a factory that creates the job that will perform the
	 * actual file transfer. The intended use for this is when the user of the
	 * framework needs more elaborate control over such jobs such as waiting for a
	 * group of parallel file transfers to complete. Such functionality can for
	 * instance exploit the Eclipse runtime concept of Job families.
	 * 
	 * @param streamToStore
	 *            the output stream to store the incoming file. Must not be
	 *            <code>null</code>.
	 * @param fileTransferJob A subclass of {@link FileTransferJob} to use to run the actual transfer.  If
	 *         <code>null</code>, provider will create default implementation.  NOTE: the given job should
	 *         *not* be scheduled/started prior to being provided as a parameter to this method.
	 * @return IIncomingFileTransfer the incoming file transfer object. NOTE:
	 *         the caller is responsible for calling
	 *         {@link OutputStream#close()} on the OutputStream provided. If the
	 *         stream provided is buffered, then
	 *         {@link BufferedOutputStream#flush()} should be called to
	 *         guaranteed that the data received is actually written to the
	 *         given OutputStream.
	 * @throws IOException
	 *             if streamToStore cannot be opened for writing
	 *             
	 * @since 2.0.0 milestone 6
	 */
	public IIncomingFileTransfer receive(OutputStream streamToStore, FileTransferJob fileTransferJob) throws IOException;

	/**
	 * Cancel incoming file transfer
	 */
	public void cancel();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17176.java