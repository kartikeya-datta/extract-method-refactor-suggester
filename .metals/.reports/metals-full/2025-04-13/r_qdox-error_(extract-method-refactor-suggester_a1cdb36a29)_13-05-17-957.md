error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1261.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1261.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1261.java
text:
```scala
b@@uffer.append(')');

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.codegen;

import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;

public class ExceptionLabel extends Label {
	
	public int ranges[] = {POS_NOT_SET,POS_NOT_SET};
	public int count = 0; // incremented each time placeStart or placeEnd is called
	public TypeBinding exceptionType;
	
public ExceptionLabel(CodeStream codeStream, TypeBinding exceptionType) {

	super(codeStream);
	this.exceptionType = exceptionType;
}

public void place() {

	// register the handler inside the codeStream then normal place
	codeStream.registerExceptionHandler(this);
	if (CodeStream.DEBUG) System.out.println("\t\t\t\t<place at: "+codeStream.position+" - "+ this); //$NON-NLS-1$ //$NON-NLS-2$
	this.position = codeStream.position;
}

public void placeEnd() {
	int endPosition = codeStream.position;
	if (this.ranges[this.count-1] == endPosition) { // start == end ?
		// discard empty exception handler
		this.count--;
	} else {
		this.ranges[this.count++] = codeStream.position;
	}
}

public void placeStart() {
	int startPosition = codeStream.position;
	if (this.count > 0 && this.ranges[this.count-1] == startPosition) { // start == previous end ?
		// reopen current handler
		this.count--;
		return;
	}
	// only need to grow on even additions (i.e. placeStart only)
	int length;
	if (this.count == (length = this.ranges.length)) {
		System.arraycopy(this.ranges, 0, this.ranges = new int[length*2], 0, length);
	}
	this.ranges[this.count++] = codeStream.position;
}
public String toString() {
	String basic = getClass().getName();
	basic = basic.substring(basic.lastIndexOf('.')+1);
	StringBuffer buffer = new StringBuffer(basic); 
	buffer.append('@').append(Integer.toHexString(hashCode()));
	buffer.append("(type=").append(this.exceptionType == null ? null : this.exceptionType.readableName()); //$NON-NLS-1$
	buffer.append(", position=").append(position); //$NON-NLS-1$
	buffer.append(", ranges = "); //$NON-NLS-1$
	if (this.count == 0) {
		buffer.append("[]"); //$NON-NLS-1$
	} else {
		for (int i = 0; i < this.count; i++) {
			if (i % 2 == 0) {
				buffer.append("[").append(ranges[i]); //$NON-NLS-1$
			} else { 
				buffer.append(",").append(ranges[i]).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		if (this.count % 2 == 1) {
			buffer.append(",?]"); //$NON-NLS-1$
		}
	}
	buffer.append(')'); //$NON-NLS-1$
	return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1261.java