error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18188.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18188.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18188.java
text:
```scala
n@@ew ChatRoomMessage(sender, targetID, msg)));

/****************************************************************************
 * Copyright (c) 2004, 2007 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/
package org.eclipse.ecf.internal.provider.irc.container;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.ecf.core.AbstractContainer;
import org.eclipse.ecf.core.events.ContainerDisposeEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.util.Trace;
import org.eclipse.ecf.internal.provider.irc.Activator;
import org.eclipse.ecf.internal.provider.irc.IRCDebugOptions;
import org.eclipse.ecf.presence.IIMMessageListener;
import org.eclipse.ecf.presence.chatroom.ChatRoomMessage;
import org.eclipse.ecf.presence.chatroom.ChatRoomMessageEvent;
import org.eclipse.ecf.presence.chatroom.IChatRoomAdminListener;

public abstract class IRCAbstractContainer extends AbstractContainer {

	protected static final String ROOT_ROOMNAME = "/"; //$NON-NLS-1$
	protected static final String COMMAND_PREFIX = "/"; //$NON-NLS-1$
	protected static final String COMMAND_DELIM = " "; //$NON-NLS-1$
	protected static final String JOIN_COMMAND = "JOIN"; //$NON-NLS-1$
	protected static final String LIST_COMMAND = "LIST"; //$NON-NLS-1$
	protected static final String PART_COMMAND = "PART"; //$NON-NLS-1$
	protected static final String NICK_COMMAND = "NICK"; //$NON-NLS-1$
	protected static final String MSG_COMMAND = "MSG"; //$NON-NLS-1$
	protected static final String NOTICE_COMMAND = "NOTICE"; //$NON-NLS-1$
	protected static final String WHOIS_COMMAND = "WHOIS"; //$NON-NLS-1$
	protected static final String QUIT_COMMAND = "QUIT"; //$NON-NLS-1$
	protected static final String AWAY_COMMAND = "AWAY"; //$NON-NLS-1$
	protected static final String TOPIC_COMMAND = "TOPIC"; //$NON-NLS-1$
	protected static final String INVITE_COMMAND = "INVITE"; //$NON-NLS-1$
	protected static final String OPERATOR_PREFIX = "@"; //$NON-NLS-1$

	protected ID localID = null;
	protected ID targetID = null;
	protected List msgListeners = new ArrayList();
	protected ID unknownID = null;
	private ArrayList subjectListeners = new ArrayList();
	
	public IRCAbstractContainer() {
		super();
	}

	protected void trace(String msg) {
		Trace.trace(Activator.PLUGIN_ID, msg);
	}

	protected void traceStack(Throwable t, String msg) {
		Trace.catching(Activator.PLUGIN_ID, IRCDebugOptions.EXCEPTIONS_CATCHING, this.getClass(), msg, t);
	}

	public void fireMessageListeners(ID sender, String msg) {
		for (Iterator i = msgListeners.iterator(); i.hasNext();) {
			IIMMessageListener l = (IIMMessageListener) i.next();
			l.handleMessageEvent(new ChatRoomMessageEvent(sender,
					new ChatRoomMessage(sender, msg)));
		}
	}

	public ID getID() {
		return localID;
	}

	public ID getConnectedID() {
		return targetID;
	}

	protected String[] parseUsers(String usergroup) {
		if (usergroup == null)
			return null;
		StringTokenizer t = new StringTokenizer(usergroup, COMMAND_DELIM);
		int tok = t.countTokens();
		String[] res = new String[tok];
		for (int i = 0; i < tok; i++)
			res[i] = t.nextToken();
		return res;
	}

	protected String[] parseUserNames(String list) {
		StringTokenizer st = new StringTokenizer(list, COMMAND_DELIM);
		int tokens = st.countTokens();
		String[] res = new String[tokens];
		for (int i = 0; i < tokens; i++) {
			res[i] = st.nextToken();
		}
		return res;
	}

	protected String concat(String[] args, int start, String suffix) {
		StringBuffer result = new StringBuffer();
		for (int i = start; i < args.length; i++) {
			result.append(args[i]).append(' ');
		}
		result.append(suffix);
		return result.toString();
	}

	protected ID createIDFromString(String str) {
		if (str == null)
			return unknownID;
		try {
			return IDFactory.getDefault().createStringID(str);
		} catch (IDCreateException e) {
			Activator
					.log(
							"ID creation exception in IRCContainer.getIDForString()", //$NON-NLS-1$
							e);
			return unknownID;
		}
	}

	protected String trimOperator(String user) {
		if (user != null && user.startsWith(OPERATOR_PREFIX))
			return user.substring(OPERATOR_PREFIX.length());
		return user;
	}

	public abstract void disconnect();

	public void dispose() {
		fireContainerEvent(new ContainerDisposeEvent(getID()));
		disconnect();
	}

	protected String[] parseCommandTokens(String message) {
		StringTokenizer st = new StringTokenizer(message, COMMAND_DELIM);
		int countTokens = st.countTokens();
		String toks[] = new String[countTokens];
		for (int i = 0; i < countTokens; i++) {
			toks[i] = st.nextToken();
		}
		return toks;
	}

	protected boolean isCommand(String message) {
		return (message != null && message.startsWith(COMMAND_PREFIX));
	}

	public void addMessageListener(IIMMessageListener l) {
		msgListeners.add(l);
	}

	public void removeMessageListener(IIMMessageListener l) {
		msgListeners.remove(l);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.chatroom.IChatRoomContainer#addChatRoomSubjectListener(org.eclipse.ecf.presence.chatroom.IChatRoomAdminListener)
	 */
	public void addChatRoomAdminListener(
			IChatRoomAdminListener subjectListener) {
		subjectListeners.add(subjectListener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.chatroom.IChatRoomContainer#removeChatRoomSubjectListener(org.eclipse.ecf.presence.chatroom.IChatRoomAdminListener)
	 */
	public void removeChatRoomAdminListener(
			IChatRoomAdminListener subjectListener) {
		subjectListeners.remove(subjectListener);
	}

	/**
	 * @param fromID
	 * @param arg2
	 */
	public void fireSubjectListeners(ID fromID, String newSubject) {
		for(Iterator i=subjectListeners.iterator(); i.hasNext(); ) {
			IChatRoomAdminListener l = (IChatRoomAdminListener) i.next();
			l.handleSubjectChange(fromID, newSubject);
		}
	}

}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18188.java