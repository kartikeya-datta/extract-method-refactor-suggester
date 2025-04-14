error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9695.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9695.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9695.java
text:
```scala
c@@har[] ABSTRACT = "abstract".toCharArray(); //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.codeassist.impl;

public interface Keywords {
	int COUNT = 41;
	
	char[] ABSTARCT = "abstract".toCharArray(); //$NON-NLS-1$
	char[] ASSERT = "assert".toCharArray(); //$NON-NLS-1$
	char[] BREAK = "break".toCharArray(); //$NON-NLS-1$
	char[] CASE = "case".toCharArray(); //$NON-NLS-1$
	char[] CATCH = "catch".toCharArray(); //$NON-NLS-1$
	char[] CLASS = "class".toCharArray(); //$NON-NLS-1$
	char[] CONTINUE = "continue".toCharArray(); //$NON-NLS-1$
	char[] DEFAULT = "default".toCharArray(); //$NON-NLS-1$
	char[] DO = "do".toCharArray(); //$NON-NLS-1$
	char[] ELSE = "else".toCharArray(); //$NON-NLS-1$
	char[] EXTENDS = "extends".toCharArray(); //$NON-NLS-1$
	char[] FINAL = "final".toCharArray(); //$NON-NLS-1$
	char[] FINALLY = "finally".toCharArray(); //$NON-NLS-1$
	char[] FOR = "for".toCharArray(); //$NON-NLS-1$
	char[] IF = "if".toCharArray(); //$NON-NLS-1$
	char[] IMPLEMENTS = "implements".toCharArray(); //$NON-NLS-1$
	char[] IMPORT = "import".toCharArray(); //$NON-NLS-1$
	char[] INSTANCEOF = "instanceof".toCharArray(); //$NON-NLS-1$
	char[] INTERFACE = "interface".toCharArray(); //$NON-NLS-1$
	char[] NATIVE = "native".toCharArray(); //$NON-NLS-1$
	char[] NEW = "new".toCharArray(); //$NON-NLS-1$
	char[] PACKAGE = "package".toCharArray(); //$NON-NLS-1$
	char[] PRIVATE = "private".toCharArray(); //$NON-NLS-1$
	char[] PROTECTED = "protected".toCharArray(); //$NON-NLS-1$
	char[] PUBLIC = "public".toCharArray(); //$NON-NLS-1$
	char[] RETURN = "return".toCharArray(); //$NON-NLS-1$
	char[] STATIC = "static".toCharArray(); //$NON-NLS-1$
	char[] STRICTFP = "strictfp".toCharArray(); //$NON-NLS-1$
	char[] SUPER = "super".toCharArray(); //$NON-NLS-1$
	char[] SWITCH = "switch".toCharArray(); //$NON-NLS-1$
	char[] SYNCHRONIZED = "synchronized".toCharArray(); //$NON-NLS-1$
	char[] THIS = "this".toCharArray(); //$NON-NLS-1$
	char[] THROW = "throw".toCharArray(); //$NON-NLS-1$
	char[] THROWS = "throws".toCharArray(); //$NON-NLS-1$
	char[] TRANSIENT = "transient".toCharArray(); //$NON-NLS-1$
	char[] TRY = "try".toCharArray(); //$NON-NLS-1$
	char[] VOLATILE = "volatile".toCharArray(); //$NON-NLS-1$
	char[] WHILE = "while".toCharArray(); //$NON-NLS-1$
	char[] TRUE = "true".toCharArray(); //$NON-NLS-1$
	char[] FALSE = "false".toCharArray(); //$NON-NLS-1$
	char[] NULL = "null".toCharArray(); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9695.java