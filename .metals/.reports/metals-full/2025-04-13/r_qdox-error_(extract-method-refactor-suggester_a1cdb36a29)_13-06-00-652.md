error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9421.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9421.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9421.java
text:
```scala
public static S@@tring InternalError;

/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.progress;

import org.eclipse.osgi.util.NLS;

public class ProgressMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.ui.internal.progress.messages";//$NON-NLS-1$

	public static String PendingUpdateAdapter_PendingLabel;
	public static String JobInfo_DoneMessage;
	public static String JobInfo_DoneNoProgressMessage;
	public static String JobInfo_NoTaskNameDoneMessage;
	public static String JobsViewPreferenceDialog_Note;
	public static String JobErrorDialog_CustomJobText;
	public static String JobInfo_UnknownProgress;
	public static String JobInfo_Waiting;
	public static String JobInfo_Sleeping;
	public static String JobInfo_System;
	public static String JobInfo_Cancelled;
	public static String JobInfo_Cancel_Requested;
	public static String JobInfo_Error;
	public static String JobInfo_Blocked;
	public static String JobInfo_Finished;
	public static String JobInfo_FinishedAt;
	public static String JobErrorDialog_CloseDialogMessage;
	public static String Error;
	public static String DeferredTreeContentManager_NotDeferred;
	public static String DeferredTreeContentManager_AddingChildren;
	public static String DeferredTreeContentManager_FetchingName;
	public static String ProgressView_CancelAction;
	public static String ProgressView_ClearAllAction;
	public static String ProgressView_NoOperations;
	
	public static String NewProgressView_RemoveAllJobsToolTip;
	public static String NewProgressView_CancelJobToolTip;
	public static String NewProgressView_ClearJobToolTip;
	public static String NewProgressView_errorDialogTitle;
	public static String NewProgressView_errorDialogMessage;
	public static String ProgressAnimationItem_tasks;
	public static String ProgressAnimationItem_ok;
	public static String ProgressAnimationItem_error;
	public static String SubTaskInfo_UndefinedTaskName;
	public static String DeferredTreeContentManager_ClearJob;
	public static String ProgressContentProvider_UpdateProgressJob;
	public static String JobErrorDialog_MultipleErrorsTitle;
	public static String ProgressManager_openJobName;
	public static String ProgressManager_showInDialogName;
	public static String ProgressMonitorJobsDialog_DetailsTitle;
	public static String ProgressMonitorJobsDialog_HideTitle;
	public static String ErrorNotificationManager_OpenErrorDialogJob;
	public static String AnimationManager_AnimationStart;
	public static String ProgressFloatingWindow_EllipsisValue;
	public static String BlockedJobsDialog_UserInterfaceTreeElement;
	public static String BlockedJobsDialog_BlockedTitle;
	public static String WorkbenchSiteProgressService_CursorJob;
	public static String ProgressMonitorFocusJobDialog_UserDialogJob;
	public static String ProgressMonitorFocusJobDialog_CLoseDialogJob;
	public static String ProgressMonitorFocusJobDialog_RunInBackgroundButton;

	public static String JobErrorDialog_MultipleErrorsMessage;
	public static String JobErrorDialog_CloseDialogTitle;
	public static String JobsViewPreferenceDialog_Title;
	public static String JobErrorDialog_DoNotShowAgainMessage;

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, ProgressMessages.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9421.java