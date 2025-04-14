error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9538.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9538.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9538.java
text:
```scala
C@@opyright (c) 2003 IBM Corporation and others.

/************************************************************************
Copyright (c) 2002 IBM Corporation and others.
All rights reserved.   This program and the accompanying materials
are made available under the terms of the Common Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v10.html

Contributors:
	IBM - Initial implementation
************************************************************************/

package org.eclipse.ui.internal.commands;

public final class KeyBinding implements Comparable {

	private final static int HASH_INITIAL = 37;
	private final static int HASH_FACTOR = 47;
	
	public static KeyBinding create(String command, String keyConfiguration, KeySequence keySequence, String plugin, int rank, String scope)
		throws IllegalArgumentException {
		return new KeyBinding(command, keyConfiguration, keySequence, plugin, rank, scope);
	}

	private String command;
	private String keyConfiguration;
	private KeySequence keySequence;
	private String plugin;
	private int rank;
	private String scope;

	private KeyBinding(String command, String keyConfiguration, KeySequence keySequence, String plugin, int rank, String scope)
		throws IllegalArgumentException {
		super();
		
		if (keyConfiguration == null || keySequence == null || rank < 0 || scope == null)
			throw new IllegalArgumentException();	
		
		this.command = command;	
		this.keyConfiguration = keyConfiguration;
		this.keySequence = keySequence;
		this.plugin = plugin;
		this.rank = rank;
		this.scope = scope;
	}

	public int compareTo(Object object) {
		KeyBinding keyBinding = (KeyBinding) object;
		int compareTo = Util.compare(command, keyBinding.command); 
		
		if (compareTo == 0) {
			compareTo = keyConfiguration.compareTo(keyBinding.keyConfiguration);

			if (compareTo == 0) {		
				compareTo = keySequence.compareTo(keyBinding.keySequence);

				if (compareTo == 0) {		
					compareTo = Util.compare(plugin, keyBinding.plugin);

					if (compareTo == 0) {
						compareTo = rank - keyBinding.rank;

						if (compareTo == 0)
							compareTo = scope.compareTo(keyBinding.scope);
					}
				}
			}
		}

		return compareTo;
	}
	
	public boolean equals(Object object) {
		if (!(object instanceof KeyBinding))
			return false;
		
		KeyBinding keyBinding = (KeyBinding) object;
		return Util.equals(command, keyBinding.command) && keyConfiguration.equals(keyBinding.keyConfiguration) && keySequence.equals(keyBinding.keySequence) && 
			Util.equals(plugin, keyBinding.plugin) && rank == keyBinding.rank && scope.equals(keyBinding.scope);
	}

	public String getCommand() {
		return command;
	}

	public String getKeyConfiguration() {
		return keyConfiguration;
	}
	
	public KeySequence getKeySequence() {
		return keySequence;	
	}

	public String getPlugin() {
		return plugin;
	}

	public int getRank() {
		return rank;	
	}

	public String getScope() {
		return scope;
	}

	public int hashCode() {
		int result = HASH_INITIAL;
		result = result * HASH_FACTOR + Util.hashCode(command);		
		result = result * HASH_FACTOR + keyConfiguration.hashCode();
		result = result * HASH_FACTOR + keySequence.hashCode();		
		result = result * HASH_FACTOR + Util.hashCode(plugin);	
		result = result * HASH_FACTOR + rank;	
		result = result * HASH_FACTOR + scope.hashCode();
		return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9538.java