error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5851.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5851.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5851.java
text:
```scala
public v@@oid consumeLocalVar(char[] varName, int occurrenceCount) {

/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.util;

public class KeyKind extends BindingKeyParser {

	public static final int F_TYPE = 0x00001;
	public static final int F_METHOD = 0x00010;
	public static final int F_FIELD = 0x00011;
	public static final int F_TYPE_PARAMETER = 0x00100;
	public static final int F_LOCAL_VAR = 0x00101;
	public static final int F_MEMBER = 0x00110;
	public static final int F_LOCAL = 0x00111;
	public static final int F_PARAMETERIZED_TYPE = 0x01000;
	public static final int F_RAW_TYPE = 0x01001;
	public static final int F_WILDCARD_TYPE = 0x01010;
	public static final int F_PARAMETERIZED_METHOD = 0x01011;
	public static final int F_CAPTURE = 0x01111;
	public static final int F_CONSTRUCTOR = 0x10000;
	
	public int flags = 0;
	private KeyKind innerKeyKind;
	
	public KeyKind(BindingKeyParser parser) {
		super(parser);
	}
	
	public KeyKind(String key) {
		super(key);
	}
	
	public void consumeBaseType(char[] baseTypeSig) {
		this.flags |= F_TYPE;
	}

	public void consumeCapture(int position) {
		this.flags |= F_CAPTURE;
	}
	
	public void consumeField(char[] fieldName) {
		this.flags |= F_FIELD;
	}

	public void consumeLocalType(char[] uniqueKey) {
		this.flags |= F_LOCAL;
	}

	public void consumeLocalVar(char[] varName) {
		this.flags |= F_LOCAL_VAR;
	}

	public void consumeMemberType(char[] simpleTypeName) {
		this.flags |= F_MEMBER;
	}

	public void consumeMethod(char[] selector, char[] signature) {
		this.flags |= F_METHOD;
		if (selector.length == 0)
			this.flags |= F_CONSTRUCTOR;
	}

	public void consumeParameterizedGenericMethod() {
		this.flags |= F_PARAMETERIZED_METHOD;
	}

	public void consumeParameterizedType(char[] simpleTypeName, boolean isRaw) {
		this.flags |= isRaw ? F_RAW_TYPE : F_PARAMETERIZED_TYPE;
	}
	
	public void consumeParser(BindingKeyParser parser) {
		this.innerKeyKind = (KeyKind) parser;
	}

	public void consumeRawType() {
		this.flags |= F_RAW_TYPE;
	}

	public void consumeTopLevelType() {
		this.flags |= F_TYPE;
	}

	public void consumeTypeParameter(char[] typeParameterName) {
		this.flags |= F_TYPE_PARAMETER;
	}
	
	public void consumeTypeWithCapture() {
		this.flags = this.innerKeyKind.flags;
	}

	public void consumeWildCard(int kind) {
		this.flags |= F_WILDCARD_TYPE;
	}

	public BindingKeyParser newParser() {
		return new KeyKind(this);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5851.java