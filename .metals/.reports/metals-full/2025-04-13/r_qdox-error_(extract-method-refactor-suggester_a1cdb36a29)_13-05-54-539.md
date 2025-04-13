error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7468.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7468.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7468.java
text:
```scala
l@@og.debug("Skipping file : " + targetFile.getAbsolutePath() + " cause it exists already");

/*******************************************************************************
 * Copyright (c) 2005, 2006 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/
package org.eclipse.xpand2.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileHandleImpl implements FileHandle {
	private final Log log = LogFactory.getLog(getClass());

	private StringBuffer buffer = new StringBuffer();

	private File targetFile = null;

	private Outlet outlet = null;

	public FileHandleImpl(final Outlet outlet, final File f) {
		this.outlet = outlet;
		targetFile = f.getAbsoluteFile();
	}

	public Outlet getOutlet() {
		return outlet;
	}

	public CharSequence getBuffer() {
		return buffer;
	}

	public void setBuffer(final CharSequence buffer) {
		this.buffer = new StringBuffer(buffer);
	}

	public File getTargetFile() {
		return targetFile;
	}

	public boolean isAppend() {
		return outlet.isAppend();
	}

	public boolean isOverwrite() {
		return outlet.isOverwrite();
	}

	public String getFileEncoding() {
		return outlet.getFileEncoding();
	}

	public void writeAndClose() {
		try {
			if (!isOverwrite() && targetFile.exists()) {
				log.debug("Skipping file : " + targetFile.getAbsolutePath() + " cause it exists allready");
				return;
			}
			log.debug("Opening file : " + targetFile.getAbsolutePath());
			// create all parent directories
			final File parentDir = targetFile.getParentFile();
			if (!parentDir.exists()) {
				parentDir.mkdirs();
				if (!parentDir.isDirectory())
					throw new RuntimeException("Failed to create parent directories of file " + targetFile.getAbsolutePath());
			}
			outlet.beforeWriteAndClose(this);
			if (outlet.shouldWrite(this)) {
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(targetFile, isAppend());
					out.write(getBytes());
				} finally {
					if (out != null) {
						try {
							out.close();
							outlet.afterClose(this);
						} catch (final IOException e) {
							throw new RuntimeException(e);
						}
					}
				}
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] getBytes() {
		if (getFileEncoding() != null) {
			try {
				return buffer.toString().getBytes(getFileEncoding());
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage(), e);
			}
		}
		return buffer.toString().getBytes();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7468.java