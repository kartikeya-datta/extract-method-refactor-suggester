error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/494.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/494.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/494.java
text:
```scala
public static final S@@tring DECORATION_PROJECT = "resource";

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

package org.eclipse.ecf.example.collab;

public interface ClientPluginConstants {
//	public static final String PLUGIN_ID = "org.eclipse.ecf.example.collab";

	public static final String DEFAULT_WIN32_APPSHARE_NAME = "appsharewin32display";
    public static final String DEFAULT_FILE_TRANSFER_CHUNKTIME_NAME = "filetransferchunksize";
    public static final String DEFAULT_FILE_TRANSFER_DELAY_NAME= "filetransferdelay";

    /*
     * Contants for perference items.
     */
	public static final String PREF_USE_CHAT_WINDOW = "useChatWindow";
	public static final String PREF_DISPLAY_TIMESTAMP = "displayTimeStamp";
	public static final String PREF_CHAT_FONT = "chatFont";
	public static final String PREF_FILE_TRANSFER_RATE = "fileTransferRate";
	public static final String PREF_CONFIRM_FILE_SEND = "confirmFileSend";
	public static final String PREF_CONFIRM_REMOTE_VIEW = "confirmRemoteView";
	public static final String PREF_FILE_SEND_PATH = "findSendPath";
	public static final String PREF_CONFIRM_FILE_RECEIVE = "confirmFileReceive";
	public static final String PREF_ME_TEXT_COLOR = "prefMeTextColor";
	public static final String PREF_OTHER_TEXT_COLOR = "prefOtherTextColor";
	public static final String PREF_SYSTEM_TEXT_COLOR = "prefSystemTextColor";
	
	public static final String PREF_START_SERVER = "startServerOnStartup";
	public static final String PREF_REGISTER_SERVER = "registerServerOnStartup";
	
	public static final String PREF_SHAREDEDITOR_PLAY_EVENTS_IMMEDIATELY = "sharedEditorShowEvents";
	public static final String PREF_SHAREDEDITOR_ASK_RECEIVER = "sharedEditorAskReceiver";
	public static final String PREF_STORE_PASSWORD = "storePassword";
	/*
	 * Contstants used to describe decoration images.
	 */
	public static final String DECORATION_PROJECT = "project";
	public static final String DECORATION_USER = "user";
	public static final String DECORATION_TIME = "time";
	public static final String DECORATION_TASK = "task";
	public static final String DECORATION_SEND = "send";
	public static final String DECORATION_RECEIVE = "receive";
	public static final String DECORATION_PRIVATE = "private";
	public static final String DECORATION_SYSTEM_MESSAGE = "system message";
	
	public static final String SHARED_MARKER_TYPE = "org.eclipse.ecf.example.collab.sharedmarker";
	public static final String SHARED_MARKER_KEY = "owner";

	public static final String DECORATION_DEFAULT_PROVIDER = "DECORATION_DEFAULT_PROVIDER";

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/494.java