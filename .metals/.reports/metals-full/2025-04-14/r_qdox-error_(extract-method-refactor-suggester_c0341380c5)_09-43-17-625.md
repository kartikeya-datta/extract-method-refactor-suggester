error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5308.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5308.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5308.java
text:
```scala
t@@his.packageName = CharOperation.splitOn('/', typeName, 0, lastIndex);

/*******************************************************************************
 * Copyright (c) 2000, 2001, 2002 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v0.5 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.jdt.internal.core.builder;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;

import java.io.*;

public class SourceFile implements ICompilationUnit {

	public char[] fileName;
	public char[] mainTypeName;
	public char[][] packageName;
	public String encoding;
	
	public SourceFile(String fileName, String initialTypeName, String encoding) {

		this.fileName = fileName.toCharArray();
		CharOperation.replace(this.fileName, '\\', '/');
	
		char[] typeName = initialTypeName.toCharArray();
		int lastIndex = CharOperation.lastIndexOf('/', typeName);
		this.mainTypeName = CharOperation.subarray(typeName, lastIndex + 1, -1);
		this.packageName = CharOperation.splitOn('/', typeName, 0, lastIndex - 1);
	
		this.encoding = encoding;
	}
	
	public SourceFile(String fileName, char[] mainTypeName, char[][] packageName, String encoding) {

		this.fileName = fileName.toCharArray();
		CharOperation.replace(this.fileName, '\\', '/');
	
		this.mainTypeName = mainTypeName;
		this.packageName = packageName;
	
		this.encoding = encoding;
	}
	
	public char[] getContents() {

		// otherwise retrieve it
		BufferedReader reader = null;
		try {
			File file = new File(new String(fileName));
			InputStreamReader streamReader =
				this.encoding == null
					? new InputStreamReader(new FileInputStream(file))
					: new InputStreamReader(new FileInputStream(file), this.encoding);
			reader = new BufferedReader(streamReader);
			int length = (int) file.length();
			char[] contents = new char[length];
			int len = 0;
			int readSize = 0;
			while ((readSize != -1) && (len != length)) {
				// See PR 1FMS89U
				// We record first the read size. In this case len is the actual read size.
				len += readSize;
				readSize = reader.read(contents, len, length - len);
			}
			reader.close();
			// See PR 1FMS89U
			// Now we need to resize in case the default encoding used more than one byte for each
			// character
			if (len != length)
				System.arraycopy(contents, 0, (contents = new char[len]), 0, len);		
			return contents;
		} catch (FileNotFoundException e) {
			throw new AbortCompilation(true, new MissingSourceFileException(new String(fileName)));
		} catch (IOException e) {
			if (reader != null) {
				try {
					reader.close();
				} catch(IOException ioe) {
				}
			}
			throw new AbortCompilation(true, new MissingSourceFileException(new String(fileName)));
		}
	}
	
	public char[] getFileName() {
		return fileName;
	}
	
	public char[] getMainTypeName() {
		return mainTypeName;
	}
	
	public char[][] getPackageName() {
		return packageName;
	}
	
	public String toString() {
		return "SourceFile[" //$NON-NLS-1$
			+ new String(fileName) + "]";  //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5308.java