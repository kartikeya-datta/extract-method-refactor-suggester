error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4741.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4741.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4741.java
text:
```scala
C@@lientPlugin.log(MessageLoader.getFormattedString("EclipseFileTransferAndLaunch.EXCEPTION_LAUNCHING", localFile), e1); //$NON-NLS-1$

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

package org.eclipse.ecf.example.collab.share.io;

import java.io.File;
import org.eclipse.ecf.example.collab.share.EclipseCollabSharedObject;
import org.eclipse.ecf.internal.example.collab.ClientPlugin;
import org.eclipse.ecf.internal.example.collab.ui.MessageLoader;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;

public class EclipseFileTransferAndLaunch extends EclipseFileTransfer {

	private static final long serialVersionUID = -7524767418102487435L;

	public EclipseFileTransferAndLaunch() {
	}

	public void sendDone(FileTransferSharedObject obj, Exception e) {
		if (senderUI != null)
			senderUI.sendDone(transferParams.getRemoteFile(), e);
		// Now launch file locally, if it's sucessful
		if (e == null) {
			String senderPath = ((EclipseCollabSharedObject) getContext()
					.getSharedObjectManager().getSharedObject(sharedObjectID))
					.getLocalFullDownloadPath();
			launchFile(new File(new File(senderPath), transferParams
					.getRemoteFile().getName()).getAbsolutePath());
		}
	}

	private void launchFile(String fileName) {
		try {
			Program.launch(fileName);
		} catch (final IllegalArgumentException e1) {
			ClientPlugin.log(MessageLoader.getString("EclipseFileTransferAndLaunch.EXCEPTION_LAUNCHING") + localFile, e1); //$NON-NLS-1$
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					MessageDialog
							.openInformation(
									null,
									MessageLoader
											.getString(MessageLoader.getString("EclipseFileTransferAndLaunch.PROGRAM_LAUNCH_MSGBOX_TITLE")), //$NON-NLS-1$
									MessageLoader
											.getString(NLS
													.bind(
															MessageLoader.getString("EclipseFileTransferAndLaunch.PROGRAM_LAUNCH_MSGBOX_TEXT"), //$NON-NLS-1$
															localFile
																	.getAbsolutePath(),
															e1.getMessage())));
				}
			});
		}
	}

	public void receiveDone(FileTransferSharedObject obj, Exception e) {
		// Need GUI progress indicator here
		if (receiverUI != null)
			receiverUI.receiveDone(getHomeContainerID(), localFile, e);
		// Now...we launch the file
		if (e == null && localFile != null)
			launchFile(localFile.getAbsolutePath());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4741.java