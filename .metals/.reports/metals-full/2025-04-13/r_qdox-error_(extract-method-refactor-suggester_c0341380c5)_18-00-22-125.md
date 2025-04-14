error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3360.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3360.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3360.java
text:
```scala
public I@@ChatRoomInfo[] getChatRoomInfos();

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
package org.eclipse.ecf.presence.chatroom;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ecf.presence.IPresenceContainerAdapter;

/**
 * Chat room manager. Entry point for getting access to chat rooms managed by
 * this manager. Access to objects implementing this interface is provided by
 * {@link IPresenceContainerAdapter#getChatRoomManager()}
 * 
 */
public interface IChatRoomManager extends IAdaptable {
	/**
	 * Add invitation listener
	 * 
	 * @param listener
	 *            the invitation listener to add
	 */
	public void addInvitationListener(IChatRoomInvitationListener listener);

	/**
	 * Remove invitation listener
	 * 
	 * @param listener
	 *            the invitation listener to remove
	 */
	public void removeInvitationListener(IChatRoomInvitationListener listener);

	/**
	 * Get parent IChatRoomManager. If this manager is the root, then this
	 * method returns null.
	 * 
	 * @return IChatRoomManager instance if this manager has a parent. Returns
	 *         null if this manager is the root of the hierarchy
	 */
	public IChatRoomManager getParent();

	/**
	 * Get any children managers of this IChatRoomManager. If this chat room
	 * manager has children chat room managers, then the returned array will
	 * have more than zero elements. If this IChatRoomManager has no children,
	 * then a zero-length array will be returned.
	 * 
	 * @return IChatRoomManager[] of children for this chat room manager. If no
	 *         children, a zero-length array will be returned. Null will not be
	 *         returned.
	 */
	public IChatRoomManager[] getChildren();

	/**
	 * Get detailed room info for given room name
	 * 
	 * @param roomname
	 *            the name of the room to get detailed info for. If null, the
	 *            room info is assumed to be a room associated with the chat
	 *            room manager instance itself. For example, for IRC, the chat
	 *            room manager is also a chat room where message can be
	 *            sent/received
	 * @return IChatRoomInfo an instance that provides the given info. Null if
	 *         no chat room info associated with given name or null
	 */
	public IChatRoomInfo getChatRoomInfo(String roomname);

	/**
	 * Get detailed room info for all chat rooms associated with this manager
	 * 
	 * @return IChatRoomInfo an array of instances that provide info for all
	 *         chat rooms
	 */
	public IChatRoomInfo[] getChatRoomsInfo();

	// XXX these two methods should ultimately be added to IChatRoomManager
	// public void addInvitationListener(IChatRoomInvitationListener listener);
	// public IChatRoomContainer createChatRoomContainer() throws
	// ContainerCreateException;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3360.java