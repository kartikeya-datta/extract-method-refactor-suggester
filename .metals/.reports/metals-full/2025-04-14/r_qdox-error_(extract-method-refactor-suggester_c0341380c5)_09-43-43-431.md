error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2265.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2265.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2265.java
text:
```scala
t@@hrow new IllegalArgumentException("Unknown literal: " + literal); //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2005, 2007 Remy Suen
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Remy Suen <remy.suen@gmail.com> - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.protocol.msn;

/**
 * <p>
 * The Status class represents the different states that a user can be in.
 * </p>
 * 
 * <p>
 * <b>Note:</b> This class/interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is being made available at this early stage to solicit feedback
 * from pioneering adopters on the understanding that any code that uses this
 * API will almost certainly be broken (repeatedly) as the API evolves.
 * </p>
 */
public final class Status {

	public static final Status ONLINE = new Status("NLN"); //$NON-NLS-1$

	public static final Status BUSY = new Status("BSY"); //$NON-NLS-1$

	public static final Status BE_RIGHT_BACK = new Status("BRB"); //$NON-NLS-1$

	public static final Status AWAY = new Status("AWY"); //$NON-NLS-1$
	
	public static final Status IDLE = new Status("IDL"); //$NON-NLS-1$

	public static final Status ON_THE_PHONE = new Status("PHN"); //$NON-NLS-1$

	public static final Status OUT_TO_LUNCH = new Status("LUN"); //$NON-NLS-1$

	public static final Status APPEAR_OFFLINE = new Status("HDN"); //$NON-NLS-1$

	public static final Status OFFLINE = new Status(null);

	private String literal;

	static Status getStatus(String literal) {
		if (literal.equals("NLN")) { //$NON-NLS-1$
			return ONLINE;
		} else if (literal.equals("AWY")) { //$NON-NLS-1$
			return AWAY;
		} else if (literal.equals("IDL")) { //$NON-NLS-1$
			return IDLE;
		} else if (literal.equals("BSY")) { //$NON-NLS-1$
			return BUSY;
		} else if (literal.equals("BRB")) { //$NON-NLS-1$
			return BE_RIGHT_BACK;
		} else if (literal.equals("PHN")) { //$NON-NLS-1$
			return ON_THE_PHONE;
		} else if (literal.equals("LUN")) { //$NON-NLS-1$
			return OUT_TO_LUNCH;
		} else if (literal.equals("HDN")) { //$NON-NLS-1$
			return APPEAR_OFFLINE;
		} else {
			throw new IllegalArgumentException("Unknown literal: " + literal);
		}
	}

	private Status(String literal) {
		this.literal = literal;
	}

	String getLiteral() {
		return literal;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2265.java