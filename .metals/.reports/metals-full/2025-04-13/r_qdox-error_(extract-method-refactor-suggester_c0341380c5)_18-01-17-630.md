error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3355.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3355.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3355.java
text:
```scala
r@@eturn name + " (" + id + ')';	 //$NON-NLS-1$

/************************************************************************
Copyright (c) 2003 IBM Corporation and others.
All rights reserved.   This program and the accompanying materials
are made available under the terms of the Common Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v10.html

Contributors:
	IBM - Initial implementation
************************************************************************/

package org.eclipse.ui.internal.commands;

import java.text.Collator;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public final class KeyConfiguration implements Comparable {

	private final static int HASH_INITIAL = 11;
	private final static int HASH_FACTOR = 21;

	private static Comparator nameComparator;
	
	public static KeyConfiguration create(String description, String id, String name, String parent, String plugin)
		throws IllegalArgumentException {
		return new KeyConfiguration(description, id, name, parent, plugin);
	}

	public static Comparator nameComparator() {
		if (nameComparator == null)
			nameComparator = new Comparator() {
				public int compare(Object left, Object right) {
					return Collator.getInstance().compare(((KeyConfiguration) left).name, ((KeyConfiguration) right).name);
				}	
			};		
		
		return nameComparator;
	}

	public static SortedMap sortedMapById(List keyConfigurations)
		throws IllegalArgumentException {
		if (keyConfigurations == null)
			throw new IllegalArgumentException();

		SortedMap sortedMap = new TreeMap();			
		Iterator iterator = keyConfigurations.iterator();
		
		while (iterator.hasNext()) {
			Object object = iterator.next();
			
			if (!(object instanceof KeyConfiguration))
				throw new IllegalArgumentException();
				
			KeyConfiguration keyConfiguration = (KeyConfiguration) object;
			sortedMap.put(keyConfiguration.id, keyConfiguration);									
		}			
		
		return sortedMap;
	}

	public static SortedMap sortedMapByName(List keyConfigurations)
		throws IllegalArgumentException {
		if (keyConfigurations == null)
			throw new IllegalArgumentException();

		SortedMap sortedMap = new TreeMap();			
		Iterator iterator = keyConfigurations.iterator();
		
		while (iterator.hasNext()) {
			Object object = iterator.next();
			
			if (!(object instanceof KeyConfiguration))
				throw new IllegalArgumentException();
				
			KeyConfiguration keyConfiguration = (KeyConfiguration) object;
			sortedMap.put(keyConfiguration.name, keyConfiguration);									
		}			
		
		return sortedMap;
	}

	private String description;
	private String id;
	private String name;
	private String parent;
	private String plugin;
	
	private KeyConfiguration(String description, String id, String name, String parent, String plugin)
		throws IllegalArgumentException {
		super();
		
		if (id == null || name == null)
			throw new IllegalArgumentException();
		
		this.description = description;
		this.id = id;
		this.name = name;
		this.parent = parent;
		this.plugin = plugin;
	}
	
	public int compareTo(Object object) {
		KeyConfiguration item = (KeyConfiguration) object;
		int compareTo = id.compareTo(item.id);
		
		if (compareTo == 0) {		
			compareTo = name.compareTo(item.name);			
		
			if (compareTo == 0) {
				Util.compare(description, item.description);
				
				if (compareTo == 0) {
					compareTo = Util.compare(parent, item.parent);

					if (compareTo == 0)
						compareTo = Util.compare(plugin, item.plugin);								
				}							
			}
		}
		
		return compareTo;	
	}
	
	public boolean equals(Object object) {
		if (!(object instanceof KeyConfiguration))
			return false;

		KeyConfiguration keyConfiguration = (KeyConfiguration) object;	
		return Util.equals(description, keyConfiguration.description) && id.equals(keyConfiguration.id) && name.equals(keyConfiguration.name) && Util.equals(parent, keyConfiguration.parent) && 
			Util.equals(plugin, keyConfiguration.plugin);
	}

	public String getDescription() {
		return description;	
	}
	
	public String getId() {
		return id;	
	}
	
	public String getName() {
		return name;
	}	

	public String getParent() {
		return parent;
	}

	public String getPlugin() {
		return plugin;
	}

	public int hashCode() {
		int result = HASH_INITIAL;
		result = result * HASH_FACTOR + Util.hashCode(description);
		result = result * HASH_FACTOR + id.hashCode();
		result = result * HASH_FACTOR + name.hashCode();
		result = result * HASH_FACTOR + Util.hashCode(parent);
		result = result * HASH_FACTOR + Util.hashCode(plugin);
		return result;
	}
	
	public String toString() {
		return name + " (" + id + ')';	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3355.java