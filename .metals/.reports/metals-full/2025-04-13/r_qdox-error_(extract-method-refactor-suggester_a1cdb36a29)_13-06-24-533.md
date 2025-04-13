error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9439.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9439.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9439.java
text:
```scala
public v@@oid putAll(Map<? extends String, ?> m)

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.protocol.http;

import java.util.Map;

import org.apache.wicket.util.collections.MostRecentlyUsedMap;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.time.Time;

/**
 * A map that contains the buffered responses. It has a constraint on the maximum entries that it
 * can contain, and a constraint on the duration of time an entry is considered valid/non-expired
 */
class StoredResponsesMap extends MostRecentlyUsedMap<String, Object>
{
	private static final long serialVersionUID = 1L;

	/**
	 * The actual object that is stored as a value of the map. It wraps the buffered response and
	 * assigns it a creation time.
	 */
	private static class Value
	{
		/** the original response to store */
		private BufferedWebResponse response;

		/** the time when this response is stored */
		private Time creationTime;
	}

	/**
	 * The duration of time before a {@link Value} is considered as expired
	 */
	private final Duration lifetime;

	/**
	 * Construct.
	 * 
	 * @param maxEntries
	 *            how much entries this map can contain
	 * @param lifetime
	 *            the duration of time to keep an entry in the map before considering it expired
	 */
	public StoredResponsesMap(int maxEntries, Duration lifetime)
	{
		super(maxEntries);

		this.lifetime = lifetime;
	}

	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<String, Object> eldest)
	{
		boolean removed = super.removeEldestEntry(eldest);
		if (removed == false)
		{
			Value value = (Value)eldest.getValue();
			Duration elapsedTime = Time.now().subtract(value.creationTime);
			if (lifetime.lessThanOrEqual(elapsedTime))
			{
				removedValue = value.response;
				removed = true;
			}
		}
		return removed;
	}

	@Override
	public BufferedWebResponse put(String key, Object bufferedResponse)
	{
		if (!(bufferedResponse instanceof BufferedWebResponse))
		{
			throw new IllegalArgumentException(StoredResponsesMap.class.getSimpleName() +
				" can store only instances of " + BufferedWebResponse.class.getSimpleName());
		}

		Value value = new Value();
		value.creationTime = Time.now();
		value.response = (BufferedWebResponse)bufferedResponse;
		Value oldValue = (Value)super.put(key, value);

		return oldValue != null ? oldValue.response : null;
	}

	@Override
	public BufferedWebResponse get(Object key)
	{
		BufferedWebResponse result = null;
		Value value = (Value)super.get(key);
		if (value != null)
		{
			Duration elapsedTime = Time.now().subtract(value.creationTime);
			if (lifetime.greaterThan(elapsedTime))
			{
				result = value.response;
			}
			else
			{
				// expired, remove it
				remove(key);
			}
		}
		return result;
	}

	@Override
	public BufferedWebResponse remove(Object key)
	{
		Value removedValue = (Value)super.remove(key);
		return removedValue != null ? removedValue.response : null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m)
	{
		throw new UnsupportedOperationException();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9439.java