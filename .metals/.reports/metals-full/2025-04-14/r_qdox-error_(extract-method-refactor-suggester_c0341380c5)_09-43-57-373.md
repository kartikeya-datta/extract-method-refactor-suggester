error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/879.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/879.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[13,1]

error in qdox parser
file content:
```java
offset: 583
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/879.java
text:
```scala
public interface IChatRoomInfo extends IAdaptable {

/****************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/
p@@ackage org.eclipse.ecf.presence.chat;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.identity.ID;

/**
 * Chat room information. Instances implementing this interface are returned by
 * {@link IChatRoomManager#getChatRoomInfo(String)} or
 * {@link IChatRoomManager#getChatRoomsInfo()}
 * 
 * @see IChatRoomManager#getChatRoomInfo(String)
 */
public interface IRoomInfo extends IAdaptable {
	/**
	 * Get a description for this room
	 * 
	 * @return String description
	 */
	public String getDescription();

	/**
	 * Get subject/topic for room
	 * 
	 * @return String topic
	 */
	public String getSubject();

	/**
	 * Get the ID associated with this room
	 * 
	 * @return ID for room
	 */
	public ID getRoomID();

	/**
	 * Get a count of the current number of room participants
	 * 
	 * @return int number of participants
	 */
	public int getParticipantsCount();

	/**
	 * Get a long name for room
	 * 
	 * @return String name
	 */
	public String getName();

	/**
	 * 
	 * @return true if room is persistent, false otherwise
	 */
	public boolean isPersistent();

	/**
	 * 
	 * @return true if room connect requires password, false otherwise
	 */
	public boolean requiresPassword();

	/**
	 * 
	 * @return true if room is moderated, false otherwise
	 */
	public boolean isModerated();

	/**
	 * 
	 * @return ID of connected user
	 */
	public ID getConnectedID();

	/**
	 * Create a new IChatRoomContainer instance
	 * 
	 * @return non-null IChatRoomContainer instance. Will not return null.
	 * @throws ContainerCreateException
	 *             if chat room container cannot be made
	 */
	public IChatRoomContainer createChatRoomContainer()
			throws ContainerCreateException;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/879.java