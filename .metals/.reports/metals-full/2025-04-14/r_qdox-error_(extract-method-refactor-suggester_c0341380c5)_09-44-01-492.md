error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9454.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9454.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9454.java
text:
```scala
public v@@oid putAll(final Map<? extends String, ?> map)

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
package org.apache.wicket.util.value;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.string.StringValueConversionException;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.time.Time;


/**
 * An implementation of <code>IValueMap</code> that makes a copy when a caller tries to change an
 * immutable <code>Map</code>. That is, the <code>Map</code> may or may not be immutable, but if it
 * is, a copy is made.
 * 
 * @author Johan Compagner
 * @author Doug Donohoe
 * @since 1.2.6
 */
public class CopyOnWriteValueMap implements IValueMap, Serializable
{
	private static final long serialVersionUID = 1L;

	/** the wrapped <code>IValueMap</code> */
	private IValueMap wrapped;

	/**
	 * Constructor.
	 * 
	 * @param wrapped
	 *            the wrapped <code>IValueMap</code>
	 */
	public CopyOnWriteValueMap(final IValueMap wrapped)
	{
		this.wrapped = wrapped;
	}

	/**
	 * @see java.util.Map#clear()
	 */
	public void clear()
	{
		checkAndCopy();
		wrapped.clear();
	}

	/**
	 * Checks if this <code>IValueMap</code> is immutable. If it is, this method makes a new
	 * <code>IValueMap</code> using the <code>ValueMap</code> copy constructor, and sets it to be
	 * this <code>CopyOnWriteValueMap</code>.
	 */
	private void checkAndCopy()
	{
		if (wrapped.isImmutable())
		{
			wrapped = new ValueMap(wrapped);
		}
	}

	/**
	 * @see java.util.Map#containsKey(Object)
	 */
	public boolean containsKey(final Object key)
	{
		return wrapped.containsKey(key);
	}

	/**
	 * @see java.util.Map#containsValue(Object)
	 */
	public boolean containsValue(final Object value)
	{
		return wrapped.containsValue(value);
	}

	/**
	 * @see java.util.Map#entrySet()
	 */
	public Set<Entry<String, Object>> entrySet()
	{
		checkAndCopy();
		return wrapped.entrySet();
	}

	/**
	 * @see java.util.Map#equals(Object)
	 */
	@Override
	public boolean equals(final Object o)
	{
		return wrapped.equals(o);
	}

	/**
	 * @see java.util.Map#get(Object)
	 */
	public Object get(final Object key)
	{
		return wrapped.get(key);
	}

	/**
	 * @see IValueMap#getBoolean(String)
	 */
	public boolean getBoolean(final String key) throws StringValueConversionException
	{
		return wrapped.getBoolean(key);
	}

	/**
	 * @see IValueMap#getCharSequence(String)
	 */
	public CharSequence getCharSequence(final String key)
	{
		return wrapped.getCharSequence(key);
	}

	/**
	 * @see IValueMap#getDouble(String)
	 */
	public double getDouble(final String key) throws StringValueConversionException
	{
		return wrapped.getDouble(key);
	}

	/**
	 * @see IValueMap#getDouble(String, double)
	 */
	public double getDouble(final String key, final double defaultValue)
		throws StringValueConversionException
	{
		return wrapped.getDouble(key, defaultValue);
	}

	/**
	 * @see IValueMap#getDuration(String)
	 */
	public Duration getDuration(final String key) throws StringValueConversionException
	{
		return wrapped.getDuration(key);
	}

	/**
	 * @see IValueMap#getInt(String, int)
	 */
	public int getInt(final String key, final int defaultValue)
		throws StringValueConversionException
	{
		return wrapped.getInt(key, defaultValue);
	}

	/**
	 * @see IValueMap#getInt(String)
	 */
	public int getInt(final String key) throws StringValueConversionException
	{
		return wrapped.getInt(key);
	}

	/**
	 * @see IValueMap#getKey(String)
	 */
	public String getKey(final String key)
	{
		return wrapped.getKey(key);
	}

	/**
	 * @see IValueMap#getLong(String, long)
	 */
	public long getLong(final String key, final long defaultValue)
		throws StringValueConversionException
	{
		return wrapped.getLong(key, defaultValue);
	}

	/**
	 * @see IValueMap#getLong(String)
	 */
	public long getLong(final String key) throws StringValueConversionException
	{
		return wrapped.getLong(key);
	}

	/**
	 * @see IValueMap#getString(String, String)
	 */
	public String getString(final String key, final String defaultValue)
	{
		return wrapped.getString(key, defaultValue);
	}

	/**
	 * @see IValueMap#getString(String)
	 */
	public String getString(final String key)
	{
		return wrapped.getString(key);
	}

	/**
	 * @see IValueMap#getStringArray(String)
	 */
	public String[] getStringArray(final String key)
	{
		return wrapped.getStringArray(key);
	}

	/**
	 * @see IValueMap#getStringValue(String)
	 */
	public StringValue getStringValue(final String key)
	{
		return wrapped.getStringValue(key);
	}

	/**
	 * @see IValueMap#getTime(String)
	 */
	public Time getTime(final String key) throws StringValueConversionException
	{
		return wrapped.getTime(key);
	}

	/**
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty()
	{
		return wrapped.isEmpty();
	}

	/**
	 * @see IValueMap#isImmutable()
	 */
	public boolean isImmutable()
	{
		return false;
	}

	/**
	 * @see java.util.Map#keySet()
	 */
	public Set<String> keySet()
	{
		checkAndCopy();
		return wrapped.keySet();
	}

	/**
	 * @see IValueMap#makeImmutable()
	 */
	public IValueMap makeImmutable()
	{
		return wrapped.makeImmutable();
	}

	/**
	 * @see java.util.Map#put(Object, Object)
	 */
	public Object put(final String key, final Object value)
	{
		checkAndCopy();
		return wrapped.put(key, value);
	}

	/**
	 * @see java.util.Map#putAll(Map)
	 */
	public void putAll(final Map<? extends String, ? extends Object> map)
	{
		checkAndCopy();
		wrapped.putAll(map);
	}

	/**
	 * @see java.util.Map#remove(Object)
	 */
	public Object remove(final Object key)
	{
		checkAndCopy();
		return wrapped.remove(key);
	}

	/**
	 * @see java.util.Map#size()
	 */
	public int size()
	{
		return wrapped.size();
	}

	/**
	 * @see java.util.Map#values()
	 */
	public Collection<Object> values()
	{
		return wrapped.values();
	}

	/**
	 * @see IValueMap#toString()
	 */
	@Override
	public String toString()
	{
		return super.toString();
	}

	// //
	// // getAs convenience methods
	// //

	/**
	 * @see IValueMap#getAsBoolean(String)
	 * 
	 */
	public Boolean getAsBoolean(final String key)
	{
		return wrapped.getAsBoolean(key);
	}

	/**
	 * @see IValueMap#getAsBoolean(String, boolean)
	 * 
	 */
	public boolean getAsBoolean(final String key, final boolean defaultValue)
	{
		return wrapped.getAsBoolean(key, defaultValue);
	}

	/**
	 * @see IValueMap#getAsInteger(String)
	 */
	public Integer getAsInteger(final String key)
	{
		return wrapped.getAsInteger(key);
	}

	/**
	 * @see IValueMap#getAsInteger(String, int)
	 */
	public int getAsInteger(final String key, final int defaultValue)
	{
		return wrapped.getAsInteger(key, defaultValue);
	}

	/**
	 * @see IValueMap#getAsLong(String)
	 */
	public Long getAsLong(final String key)
	{
		return wrapped.getAsLong(key);
	}

	/**
	 * @see IValueMap#getAsLong(String, long)
	 */
	public long getAsLong(final String key, final long defaultValue)
	{
		return wrapped.getAsLong(key, defaultValue);
	}

	/**
	 * @see IValueMap#getAsDouble(String)
	 */
	public Double getAsDouble(final String key)
	{
		return wrapped.getAsDouble(key);
	}

	/**
	 * @see IValueMap#getAsDouble(String, double)
	 */
	public double getAsDouble(final String key, final double defaultValue)
	{
		return wrapped.getAsDouble(key, defaultValue);
	}

	/**
	 * @see IValueMap#getAsDuration(String)
	 */
	public Duration getAsDuration(final String key)
	{
		return wrapped.getAsDuration(key);
	}

	/**
	 * @see IValueMap#getAsDuration(String, Duration)
	 */
	public Duration getAsDuration(final String key, final Duration defaultValue)
	{
		return wrapped.getAsDuration(key, defaultValue);
	}

	/**
	 * @see IValueMap#getAsTime(String)
	 */
	public Time getAsTime(final String key)
	{
		return wrapped.getAsTime(key);
	}

	/**
	 * @see IValueMap#getAsTime(String, Time)
	 */
	public Time getAsTime(final String key, final Time defaultValue)
	{
		return wrapped.getAsTime(key, defaultValue);
	}

	/**
	 * @see IValueMap#getAsEnum(String, Class)
	 */
	public <T extends Enum<T>> T getAsEnum(final String key, final Class<T> eClass)
	{
		return wrapped.getAsEnum(key, eClass);
	}

	/**
	 * @see IValueMap#getAsEnum
	 */
	public <T extends Enum<T>> T getAsEnum(final String key, final T defaultValue)
	{
		return wrapped.getAsEnum(key, defaultValue);
	}

	/**
	 * @see IValueMap#getAsEnum(String, Class, Enum)
	 */
	public <T extends Enum<T>> T getAsEnum(final String key, final Class<T> eClass,
		final T defaultValue)
	{
		return wrapped.getAsEnum(key, eClass, defaultValue);
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9454.java