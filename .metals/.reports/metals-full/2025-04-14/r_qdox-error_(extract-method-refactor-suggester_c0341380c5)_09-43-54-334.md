error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5395.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5395.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5395.java
text:
```scala
protected transient C@@omparator comparator;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.jorphan.collections;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

// NOTUSED import org.apache.jorphan.logging.LoggingManager;
// NOTUSED import org.apache.log.Logger;

/**
 * SortedHashTree is a different implementation of the {@link HashTree}
 * collection class. In the SortedHashTree, the ordering of values in the tree
 * is made explicit via the compare() function of objects added to the tree.
 * This works in exactly the same fashion as it does for a SortedSet.
 * 
 * @see HashTree
 * @see HashTreeTraverser
 * 
 * @author mstover1 at apache.org
 * @version $Revision$
 */
public class SortedHashTree extends HashTree implements Serializable {
	// NOTUSED private static Logger log = LoggingManager.getLoggerForClass();
	protected Comparator comparator;

	public SortedHashTree() {
		data = new TreeMap();
	}

	public SortedHashTree(Comparator comper) {
		comparator = comper;
		data = new TreeMap(comparator);
	}

	public SortedHashTree(Object key) {
		data = new TreeMap();
		data.put(key, new SortedHashTree());
	}

	public SortedHashTree(Object key, Comparator comper) {
		comparator = comper;
		data = new TreeMap(comparator);
		data.put(key, new SortedHashTree(comparator));
	}

	public SortedHashTree(Collection keys) {
		data = new TreeMap();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			data.put(it.next(), new SortedHashTree());
		}
	}

	public SortedHashTree(Collection keys, Comparator comper) {
		comparator = comper;
		data = new TreeMap(comparator);
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			data.put(it.next(), new SortedHashTree(comparator));
		}
	}

	public SortedHashTree(Object[] keys) {
		data = new TreeMap();
		for (int x = 0; x < keys.length; x++) {
			data.put(keys[x], new SortedHashTree());
		}
	}

	public SortedHashTree(Object[] keys, Comparator comper) {
		comparator = comper;
		data = new TreeMap(comparator);
		for (int x = 0; x < keys.length; x++) {
			data.put(keys[x], new SortedHashTree(comparator));
		}
	}

	protected HashTree createNewTree() {
		if (comparator == null) {
			return new SortedHashTree();
		} else {
			return new SortedHashTree(comparator);
		}
	}

	protected HashTree createNewTree(Object key) {
		if (comparator == null) {
			return new SortedHashTree(key);
		} else {
			return new SortedHashTree(key, comparator);
		}
	}

	protected HashTree createNewTree(Collection values) {
		if (comparator == null) {
			return new SortedHashTree(values);
		} else {
			return new SortedHashTree(values, comparator);
		}
	}

	public Object clone() {
		HashTree newTree = null;
		if (comparator == null) {
			newTree = new SortedHashTree();
		} else {
			newTree = new SortedHashTree(comparator);
		}
		newTree.data = (Map) ((HashMap) data).clone();
		return newTree;
	}

	/**
	 * @param comparator
	 *            The comparator to set.
	 */
	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5395.java