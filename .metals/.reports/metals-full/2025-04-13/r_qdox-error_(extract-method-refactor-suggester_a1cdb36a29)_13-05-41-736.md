error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3193.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3193.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3193.java
text:
```scala
D@@EFAULT_INCLUDE_SERVER = Boolean.getBoolean(str);

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
import java.io.Serializable;
import java.util.Date;

public class FileTransferParams implements Serializable {
	private static final long serialVersionUID = -2871056005778727843L;
	protected static int DEFAULT_CHUNK_SIZE = 1024;
	protected static int DEFAULT_WAIT_TIME = 1000;
	protected static int DEFAULT_FILE_LENGTH = -1;
	protected static boolean DEFAULT_INCLUDE_SERVER = false;
	// Suggested remote file name
	protected File remoteFile;
	protected int chunkSize;
	protected int waitTime;
	protected Date startDate;
	protected boolean includeServer;
	protected long length;
	protected float rate;
	protected FileTransferListener progressListener;

	static {
		try {
			String str = System.getProperty(FileTransferParams.class.getName()
					+ ".FILECHUNKSIZE", "" + DEFAULT_CHUNK_SIZE);
			DEFAULT_CHUNK_SIZE = Integer.parseInt(str);
			str = System.getProperty(FileTransferParams.class.getName()
					+ ".FILEWAITTIME", DEFAULT_WAIT_TIME + "");
			DEFAULT_WAIT_TIME = Integer.parseInt(str);
			str = System.getProperty(FileTransferParams.class.getName()
					+ ".FILELENGTH", DEFAULT_FILE_LENGTH + "");
			DEFAULT_FILE_LENGTH = Integer.parseInt(str);
			str = System.getProperty(FileTransferParams.class.getName()
					+ ".FILEINCLUDESERVER", "false");
			DEFAULT_INCLUDE_SERVER = Boolean.valueOf(str).booleanValue();
		} catch (Exception e) {
		}
	}

	public FileTransferParams(File aFile, int chunkSize, int waitTime,
			Date startDate, boolean includeServer, long length,
			FileTransferListener listener) {
		remoteFile = aFile;
		if (chunkSize == -1)
			this.chunkSize = DEFAULT_CHUNK_SIZE;
		else
			this.chunkSize = chunkSize;
		this.waitTime = waitTime;
		if (waitTime == -1)
			this.waitTime = DEFAULT_WAIT_TIME;
		else
			this.waitTime = waitTime;
		this.startDate = startDate;
		this.includeServer = includeServer;
		this.length = length;
		this.rate = (chunkSize * 8) / ((float) waitTime / (float) 1000);
		this.progressListener = listener;
	}

	public FileTransferParams() {
		this(null, DEFAULT_CHUNK_SIZE, DEFAULT_WAIT_TIME, null,
				DEFAULT_INCLUDE_SERVER, DEFAULT_FILE_LENGTH, null);
	}

	public File getRemoteFile() {
		return remoteFile;
	}

	public void setRemoteFile(File aFile) {
		remoteFile = aFile;
	}

	public int getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(int size) {
		chunkSize = size;
	}

	public int getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(int wait) {
		waitTime = wait;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date aDate) {
		startDate = aDate;
	}

	public boolean getIncludeServer() {
		return includeServer;
	}

	public void setIncludeServer(boolean include) {
		includeServer = include;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long len) {
		length = len;
	}

	public float getRate() {
		return rate;
	}

	protected FileTransferListener getProgressListener() {
		return progressListener;
	}

	protected void setProgressListener(FileTransferListener list) {
		progressListener = list;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("FileTransferParams[");
		sb.append(remoteFile).append(";").append(chunkSize).append(";");
		sb.append(waitTime).append(";");
		sb.append(startDate).append(";").append(includeServer).append(";");
		sb.append(length).append(";").append(rate).append(";");
		sb.append(progressListener).append("]");
		return sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3193.java