error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6433.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6433.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6433.java
text:
```scala
g@@etProject().setUserProperty(this.property, "has debug"); //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.core;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.eclipse.jdt.core.util.IClassFileReader;
import org.eclipse.jdt.core.util.ICodeAttribute;
import org.eclipse.jdt.core.util.IMethodInfo;
import org.eclipse.jdt.internal.antadapter.AntAdapterMessages;

public class CheckDebugAttributes extends Task {

	private String file;
	private String property;
	
	public void execute() throws BuildException {
		if (this.file == null) {
			throw new BuildException(AntAdapterMessages.getString("checkDebugAttributes.file.argument.cannot.be.null")); //$NON-NLS-1$
		}
		if (this.property == null) {
			throw new BuildException(AntAdapterMessages.getString("checkDebugAttributes.property.argument.cannot.be.null")); //$NON-NLS-1$
		}
		try {
			boolean hasDebugAttributes = false;
			if (org.eclipse.jdt.internal.compiler.util.Util.isArchiveFileName(this.file)) {
				ZipFile jarFile = new ZipFile(this.file);
				for (Enumeration entries = jarFile.entries(); !hasDebugAttributes && entries.hasMoreElements(); ) {
					ZipEntry entry = (ZipEntry) entries.nextElement();
					if (org.eclipse.jdt.internal.compiler.util.Util.isClassFileName(entry.getName())) {
						IClassFileReader classFileReader = ToolFactory.createDefaultClassFileReader(this.file, entry.getName(), IClassFileReader.ALL);
						hasDebugAttributes = checkClassFile(classFileReader);
					}
				}
			} else if (org.eclipse.jdt.internal.compiler.util.Util.isClassFileName(this.file)) {
				IClassFileReader classFileReader = ToolFactory.createDefaultClassFileReader(this.file, IClassFileReader.ALL);
				hasDebugAttributes = checkClassFile(classFileReader);
			} else {
				throw new BuildException(AntAdapterMessages.getString("checkDebugAttributes.file.argument.must.be.a.classfile.or.a.jarfile")); //$NON-NLS-1$
			}
			if (hasDebugAttributes) {
				this.project.setUserProperty(this.property, "has debug"); //$NON-NLS-1$
			}
		} catch (IOException e) {
			throw new BuildException(AntAdapterMessages.getString("checkDebugAttributes.ioexception.occured") + this.file); //$NON-NLS-1$
		}
	}
	
	private boolean checkClassFile(IClassFileReader classFileReader) {
		IMethodInfo[] methodInfos = classFileReader.getMethodInfos();
		for (int i = 0, max = methodInfos.length; i < max; i++) {
			ICodeAttribute codeAttribute = methodInfos[i].getCodeAttribute();
			if (codeAttribute != null && codeAttribute.getLineNumberAttribute() != null) {
				return true;
			}	
		}
		return false;
	}

	public void setFile(String value) {
		this.file = value;
	}
	
	public void setProperty(String value) {
		this.property = value;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6433.java