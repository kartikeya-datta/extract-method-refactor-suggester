error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8494.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8494.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8494.java
text:
```scala
s@@uper.putAll(map);

/*
 * $Id$ $Revision:
 * 1.5 $ $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.util.value;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import wicket.util.parse.metapattern.parsers.VariableAssignmentParser;
import wicket.util.string.IStringIterator;
import wicket.util.string.StringList;
import wicket.util.string.StringValue;
import wicket.util.string.StringValueConversionException;
import wicket.util.time.Duration;
import wicket.util.time.Time;

/**
 * A Map implementation that holds values, parses strings and exposes a variety
 * of convenience methods.
 * <p>
 * In addition to a no-arg constructor and a copy constructor that takes a Map
 * argument, ValueMaps can be constructed using a parsing constructor.
 * ValueMap(String) will parse values from the string in comma separated
 * key/value assignment pairs. For example, new ValueMap("a=9,b=foo").
 * <p>
 * Values can be retrieved from the map in the usual way or with methods that do
 * handy conversions to various types, including String, StringValue, int, long,
 * double, Time and Duration.
 * <p>
 * The makeImmutable method will make the underlying map immutable. Further
 * attempts to change the map will result in a runtime exception.
 * <p>
 * The toString() method converts a ValueMap object to a readable key/value
 * string for diagnostics.
 * 
 * @author Jonathan Locke
 */
public class ValueMap extends HashMap
{
	/** An empty ValueMap. */
	public static final ValueMap EMPTY_MAP = new ValueMap();
	
	private static final long serialVersionUID = 1L;
	
	/** True if this value map has been made immutable. */
	private boolean immutable = false;

	/**
	 * Constructs empty value map.
	 */
	public ValueMap()
	{
	}

	/**
	 * Copy constructor.
	 * 
	 * @param map
	 *            The map to copy
	 */
	public ValueMap(final Map map)
	{
		putAll(map);
	}

	/**
	 * Constructor.
	 * 
	 * @param keyValuePairs
	 *            List of key value pairs separated by commas. For example,
	 *            "param1=foo,param2=bar"
	 */
	public ValueMap(final String keyValuePairs)
	{
		this(keyValuePairs, ",");
	}

	/**
	 * Constructor.
	 * 
	 * @param keyValuePairs
	 *            List of key value pairs separated by a given delimiter. For
	 *            example, "param1=foo,param2=bar" where delimiter is ",".
	 * @param delimiter
	 *            Delimiter string used to separate key/value pairs
	 */
	public ValueMap(final String keyValuePairs, final String delimiter)
	{
		// Get list of strings separated by the delimiter
		final StringList pairs = StringList.tokenize(keyValuePairs, delimiter);

		// Go through each string in the list
		for (IStringIterator iterator = pairs.iterator(); iterator.hasNext();)
		{
			// Get the next key value pair
			final String pair = iterator.next();

			// Parse using metapattern parser for variable assignments
			final VariableAssignmentParser parser = new VariableAssignmentParser(pair);

			// Does it parse?
			if (parser.matches())
			{
				// Succeeded. Put key and value into map
				put(parser.getKey(), parser.getValue());
			}
			else
			{
				throw new IllegalArgumentException("Invalid key value list: '" + keyValuePairs
						+ "'");
			}
		}
	}

	/**
	 * @see java.util.Map#clear()
	 */
	public final void clear()
	{
		checkMutability();
		super.clear();
	}

	/**
	 * Gets a boolean value by key.
	 *
	 * @param key The key
	 * @return The value
	 * @throws StringValueConversionException
	 */
	public final boolean getBoolean(final String key) throws StringValueConversionException
	{
		return getStringValue(key).toBoolean();
	}

	/**
	 * Gets a double value by key.
	 * 
	 * @param key
	 *            The key
	 * @return The value
	 * @throws StringValueConversionException
	 */
	public final double getDouble(final String key) throws StringValueConversionException
	{
		return getStringValue(key).toDouble();
	}

	/**
	 * Gets a duration.
	 * 
	 * @param key
	 *            The key
	 * @return The value
	 * @throws StringValueConversionException
	 */
	public final Duration getDuration(final String key) throws StringValueConversionException
	{
		return getStringValue(key).toDuration();
	}

	/**
	 * Gets an int.
	 * 
	 * @param key
	 *            The key
	 * @return The value
	 * @throws StringValueConversionException
	 */
	public final int getInt(final String key) throws StringValueConversionException
	{
		return getStringValue(key).toInt();
	}

	/**
	 * Gets an int, using a default if not found.
	 * 
	 * @param key
	 *            The key
	 * @param defaultValue
	 *            Value to use if no value in map
	 * @return The value
	 * @throws StringValueConversionException
	 */
	public final int getInt(final String key, final int defaultValue)
			throws StringValueConversionException
	{
		final StringValue value = getStringValue(key);
		return (value != null) ? value.toInt() : defaultValue;
	}

	/**
	 * Gets a long.
	 * 
	 * @param key
	 *            The key
	 * @return The value
	 * @throws StringValueConversionException
	 */
	public final long getLong(final String key) throws StringValueConversionException
	{
		return getStringValue(key).toLong();
	}

	/**
	 * Gets a long using a default if not found.
	 * 
	 * @param key
	 *            The key
	 * @param defaultValue
	 *            Value to use if no value in map
	 * @return The value
	 * @throws StringValueConversionException
	 */
	public final long getLong(final String key, final long defaultValue)
			throws StringValueConversionException
	{
		final StringValue value = getStringValue(key);
		return (value != null) ? value.toLong() : defaultValue;
	}

	/**
	 * Gets a string by key.
	 * 
	 * @param key
	 *            The get
	 * @return The string
	 */
	public final String getString(final String key)
	{
		final Object o = getStringValue(key);

		if (o == null)
		{
			return null;
		}
		else
		{
			return o.toString();
		}
	}

	/**
	 * Gets a StringValue by key.
	 * 
	 * @param key
	 *            The key
	 * @return The string value object
	 */
	public StringValue getStringValue(final String key)
	{
		return StringValue.valueOf(get(key));
	}

	/**
	 * Gets a time.
	 * 
	 * @param key
	 *            The key
	 * @return The value
	 * @throws StringValueConversionException
	 */
	public final Time getTime(final String key) throws StringValueConversionException
	{
		return getStringValue(key).toTime();
	}

	/**
	 * Gets whether this value map is made immutable.
	 * @return whether this value map is made immutable
	 */
	public final boolean isImmutable()
	{
		return immutable;
	}

	/**
	 * Makes this value map immutable by changing the underlying map
	 * representation to a collections "unmodifiableMap". After calling this
	 * method, any attempt to modify this map will result in a runtime exception
	 * being thrown by the collections classes.
	 */
	public final void makeImmutable()
	{
		this.immutable = true;
	}

	/**
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(final Object key, final Object value)
	{
		checkMutability();
		return super.put(key, value);
	}

	/**
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(final Map map)
	{
		checkMutability();
		super.putAll(map);
	}

	/**
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Object remove(final Object key)
	{
		checkMutability();
		return super.remove(key);
	}

	/**
	 * @return Debug string representation of this map
	 */
	public final String toDebugString()
	{
		return "[" + toString() + "]";
	}

	/**
	 * Gets a string representation of this object
	 * 
	 * @return String representation of map consistent with tag attribute style
	 *         of markup elements, for example: a="x" b="y" c="z"
	 */
	public String toString()
	{
		final StringBuffer buffer = (StringBuffer)new StringBuffer();
		for (final Iterator iterator = entrySet().iterator(); iterator.hasNext();)
		{
			Map.Entry entry = (Map.Entry)iterator.next();
			buffer.append(entry.getKey() + " = \"" + entry.getValue() + "\"");
			if (iterator.hasNext())
			{
				buffer.append(' ');
			}
		}
		return buffer.toString();
	}
	
	/**
	 * Throw exception if map is immutable.
	 */
	private final void checkMutability()
	{
		if (immutable)
		{
			throw new UnsupportedOperationException("Map is immutable");
		}
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8494.java