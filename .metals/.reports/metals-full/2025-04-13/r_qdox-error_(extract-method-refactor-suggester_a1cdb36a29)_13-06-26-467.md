error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11584.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11584.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11584.java
text:
```scala
private static final S@@tring LINK_PREFIX = "http://help.eclipse.org/stable/nftopic/org.eclipse.platform.doc.isv/reference/api/"; //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2007 Remy Suen and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Remy Suen <remy.suen@gmail.com> - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.internal.presence.bot.kosmos;

import java.util.Map;

class Javadoc {

	private static final String LINK_PREFIX = "http://help.eclipse.org/help32/nftopic/org.eclipse.platform.doc.isv/reference/api/"; //$NON-NLS-1$
	private static final String LINK_SUFFIX = ".html"; //$NON-NLS-1$

	private Map javadocs;
	private String fqn;
	private String link;

	Javadoc(Map javadocs, String fullQualifiedName) {
		this.javadocs = javadocs;
		fqn = fullQualifiedName;
		link = LINK_PREFIX + fqn.replaceAll("\\.", "/") + LINK_SUFFIX; //$NON-NLS-1$ //$NON-NLS-2$
	}

	String getField(String field) {
		return link + '#' + field;
	}

	String getMethod(String methodName, String[] array) {
		String ret = link + '#' + methodName + '(';
		for (int i = 0; i < array.length; i++) {
			Object match = javadocs.get(array[i]);
			if (match == null) {
				if (array[i].equals("int") || array[i].equals("float") //$NON-NLS-1$ //$NON-NLS-2$
 array[i].equals("short") || array[i].equals("long") //$NON-NLS-1$ //$NON-NLS-2$
 array[i].equals("byte") //$NON-NLS-1$
 array[i].equals("boolean") //$NON-NLS-1$
 array[i].equals("double") || array[i].equals("char")) { //$NON-NLS-1$ //$NON-NLS-2$
					ret = ret + array[i] + ",%20"; //$NON-NLS-1$
				} else if (array[i].equals("Object") //$NON-NLS-1$
 array[i].equals("Class") //$NON-NLS-1$
 array[i].equals("String")) { //$NON-NLS-1$
					ret = ret + "java.lang." + array[i] + ",%20"; //$NON-NLS-1$ //$NON-NLS-2$
				} else if (array[i].equals("Map") || array[i].equals("List") //$NON-NLS-1$ //$NON-NLS-2$
 array[i].equals("Set") //$NON-NLS-1$
 array[i].equals("Collection")) { //$NON-NLS-1$
					ret = ret + "java.util." + array[i] + ",%20"; //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					ret = ret + array[i] + ",%20"; //$NON-NLS-1$
				}
			} else if (match instanceof Javadoc) {
				ret = ret + ((Javadoc) match).fqn + ",%20"; //$NON-NLS-1$
			} else {
				Javadoc[] docs = (Javadoc[]) match;
				boolean found = false;
				for (int j = 0; j < docs.length; j++) {
					if (array[i].equals(docs[j].fqn)) {
						ret = ret + array[i] + ",%20"; //$NON-NLS-1$
						found = true;
						break;
					}
				}
				if (!found) {
					return null;
				}
			}
		}
		if (ret.endsWith(",%20")) { //$NON-NLS-1$
			ret = ret.substring(0, ret.length() - 4);
		}
		return ret + ')';
	}

	String getDefault() {
		return fqn + " - " + link; //$NON-NLS-1$
	}
	
	public String toString() {
		return fqn;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11584.java