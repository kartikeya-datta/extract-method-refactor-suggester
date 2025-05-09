error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14477.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14477.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14477.java
text:
```scala
final S@@tringBuilder buffer = new StringBuilder();

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

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.wicket.util.parse.metapattern.MetaPattern;
import org.apache.wicket.util.parse.metapattern.parsers.VariableAssignmentParser;
import org.apache.wicket.util.string.IStringIterator;
import org.apache.wicket.util.string.StringList;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.string.StringValueConversionException;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.time.Time;


/**
 * A <code>IValueMap</code> implementation that holds values, parses <code>String</code>s, and
 * exposes a variety of convenience methods.
 * <p>
 * In addition to a no-arg constructor and a copy constructor that takes a <code>Map</code>
 * argument, <code>ValueMap</code>s can be constructed using a parsing constructor.
 * <code>ValueMap(String)</code> will parse values from the string in comma separated key/value
 * assignment pairs. For example, <code>new ValueMap("a=9,b=foo")</code>.
 * <p>
 * Values can be retrieved from the <code>ValueMap</code> in the usual way or with methods that do
 * handy conversions to various types, including <code>String</code>, <code>StringValue</code>,
 * <code>int</code>, <code>long</code>, <code>double</code>, <code>Time</code> and
 * <code>Duration</code>.
 * <p>
 * The <code>makeImmutable</code> method will make the underlying <code>Map</code> immutable.
 * Further attempts to change the <code>Map</code> will result in a <code>RuntimeException</code>.
 * <p>
 * The <code>toString</code> method converts a <code>ValueMap</code> object to a readable key/value
 * string for diagnostics.
 * 
 * @author Jonathan Locke
 * @author Doug Donohoe
 * @since 1.2.6
 */
public class ValueMap extends LinkedHashMap<String, Object> implements IValueMap
{
	/** an empty <code>ValueMap</code>. */
	public static final ValueMap EMPTY_MAP;

	/** create EMPTY_MAP, make immutable * */
	static
	{
		EMPTY_MAP = new ValueMap();
		EMPTY_MAP.makeImmutable();
	}

	private static final long serialVersionUID = 1L;

	/**
	 * <code>true</code> if this <code>ValueMap</code> has been made immutable.
	 */
	private boolean immutable = false;

	/**
	 * Constructs empty <code>ValueMap</code>.
	 */
	public ValueMap()
	{
		super();
	}

	/**
	 * Copy constructor.
	 * 
	 * @param map
	 *            the <code>ValueMap</code> to copy
	 */
	public ValueMap(final Map<? extends String, ? extends Object> map)
	{
		super();

		super.putAll(map);
	}

	/**
	 * Constructor.
	 * <p>
	 * NOTE: Please use <code>RequestUtils.decodeParameters()</code> if you wish to properly decode
	 * a request URL.
	 * 
	 * @param keyValuePairs
	 *            list of key/value pairs separated by commas. For example, "
	 *            <code>param1=foo,param2=bar</code>"
	 */
	public ValueMap(final String keyValuePairs)
	{
		this(keyValuePairs, ",");
	}

	/**
	 * Constructor.
	 * <p>
	 * NOTE: Please use <code>RequestUtils.decodeParameters()</code> if you wish to properly decode
	 * a request URL.
	 * 
	 * @param keyValuePairs
	 *            list of key/value pairs separated by a given delimiter. For example, "
	 *            <code>param1=foo,param2=bar</code>" where delimiter is "<code>,</code>".
	 * @param delimiter
	 *            delimiter <code>String</code> used to separate key/value pairs
	 */
	public ValueMap(final String keyValuePairs, final String delimiter)
	{
		super();

		int start = 0;
		int equalsIndex = keyValuePairs.indexOf('=');
		int delimiterIndex = keyValuePairs.indexOf(delimiter, equalsIndex);
		if (delimiterIndex == -1)
		{
			delimiterIndex = keyValuePairs.length();
		}
		while (equalsIndex != -1)
		{
			if (delimiterIndex < keyValuePairs.length())
			{
				int equalsIndex2 = keyValuePairs.indexOf('=', delimiterIndex + 1);
				if (equalsIndex2 != -1)
				{
					delimiterIndex = keyValuePairs.lastIndexOf(delimiter, equalsIndex2);
				}
				else
				{
					delimiterIndex = keyValuePairs.length();
				}
			}
			String key = keyValuePairs.substring(start, equalsIndex);
			String value = keyValuePairs.substring(equalsIndex + 1, delimiterIndex);
			add(key, value);
			if (delimiterIndex < keyValuePairs.length())
			{
				start = delimiterIndex + 1;
				equalsIndex = keyValuePairs.indexOf('=', start);
				if (equalsIndex != -1)
				{
					delimiterIndex = keyValuePairs.indexOf(delimiter, equalsIndex);
					if (delimiterIndex == -1)
					{
						delimiterIndex = keyValuePairs.length();
					}
				}
			}
			else
			{
				equalsIndex = -1;
			}
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param keyValuePairs
	 *            list of key/value pairs separated by a given delimiter. For example, "
	 *            <code>param1=foo,param2=bar</code>" where delimiter is "<code>,</code>".
	 * @param delimiter
	 *            delimiter string used to separate key/value pairs
	 * @param valuePattern
	 *            pattern for value. To pass a simple regular expression, pass "
	 *            <code>new MetaPattern(regexp)</code>".
	 */
	public ValueMap(final String keyValuePairs, final String delimiter,
		final MetaPattern valuePattern)
	{
		super();

		// Get list of strings separated by the delimiter
		final StringList pairs = StringList.tokenize(keyValuePairs, delimiter);

		// Go through each string in the list
		for (IStringIterator iterator = pairs.iterator(); iterator.hasNext();)
		{
			// Get the next key value pair
			final String pair = iterator.next();

			// Parse using metapattern parser for variable assignments
			final VariableAssignmentParser parser = new VariableAssignmentParser(pair, valuePattern);

			// Does it parse?
			if (parser.matches())
			{
				// Succeeded. Put key and value into map
				put(parser.getKey(), parser.getValue());
			}
			else
			{
				throw new IllegalArgumentException("Invalid key value list: '" + keyValuePairs +
					'\'');
			}
		}
	}

	/**
	 * @see java.util.Map#clear()
	 */
	@Override
	public final void clear()
	{
		checkMutability();
		super.clear();
	}

	/**
	 * @see IValueMap#getBoolean(String)
	 */
	public final boolean getBoolean(final String key) throws StringValueConversionException
	{
		return getStringValue(key).toBoolean();
	}

	/**
	 * @see IValueMap#getDouble(String)
	 */
	public final double getDouble(final String key) throws StringValueConversionException
	{
		return getStringValue(key).toDouble();
	}

	/**
	 * @see IValueMap#getDouble(String, double)
	 */
	public final double getDouble(final String key, final double defaultValue)
		throws StringValueConversionException
	{
		return getStringValue(key).toDouble(defaultValue);
	}

	/**
	 * @see IValueMap#getDuration(String)
	 */
	public final Duration getDuration(final String key) throws StringValueConversionException
	{
		return getStringValue(key).toDuration();
	}

	/**
	 * @see IValueMap#getInt(String)
	 */
	public final int getInt(final String key) throws StringValueConversionException
	{
		return getStringValue(key).toInt();
	}

	/**
	 * @see IValueMap#getInt(String, int)
	 */
	public final int getInt(final String key, final int defaultValue)
		throws StringValueConversionException
	{
		return getStringValue(key).toInt(defaultValue);
	}

	/**
	 * @see IValueMap#getLong(String)
	 */
	public final long getLong(final String key) throws StringValueConversionException
	{
		return getStringValue(key).toLong();
	}

	/**
	 * @see IValueMap#getLong(String, long)
	 */
	public final long getLong(final String key, final long defaultValue)
		throws StringValueConversionException
	{
		return getStringValue(key).toLong(defaultValue);
	}

	/**
	 * @see IValueMap#getString(String, String)
	 */
	public final String getString(final String key, final String defaultValue)
	{
		final String value = getString(key);
		return value != null ? value : defaultValue;
	}

	/**
	 * @see IValueMap#getString(String)
	 */
	public final String getString(final String key)
	{
		final Object o = get(key);
		if (o == null)
		{
			return null;
		}
		else if (o.getClass().isArray() && Array.getLength(o) > 0)
		{
			// if it is an array just get the first value
			final Object arrayValue = Array.get(o, 0);
			if (arrayValue == null)
			{
				return null;
			}
			else
			{
				return arrayValue.toString();
			}

		}
		else
		{
			return o.toString();
		}
	}

	/**
	 * @see IValueMap#getCharSequence(String)
	 */
	public final CharSequence getCharSequence(final String key)
	{
		final Object o = get(key);
		if (o == null)
		{
			return null;
		}
		else if (o.getClass().isArray() && Array.getLength(o) > 0)
		{
			// if it is an array just get the first value
			final Object arrayValue = Array.get(o, 0);
			if (arrayValue == null)
			{
				return null;
			}
			else
			{
				if (arrayValue instanceof CharSequence)
				{
					return (CharSequence)arrayValue;
				}
				return arrayValue.toString();
			}

		}
		else
		{
			if (o instanceof CharSequence)
			{
				return (CharSequence)o;
			}
			return o.toString();
		}
	}

	/**
	 * @see IValueMap#getStringArray(String)
	 */
	public String[] getStringArray(final String key)
	{
		final Object o = get(key);
		if (o == null)
		{
			return null;
		}
		else if (o instanceof String[])
		{
			return (String[])o;
		}
		else if (o.getClass().isArray())
		{
			int length = Array.getLength(o);
			String[] array = new String[length];
			for (int i = 0; i < length; i++)
			{
				final Object arrayValue = Array.get(o, i);
				if (arrayValue != null)
				{
					array[i] = arrayValue.toString();
				}
			}
			return array;
		}
		return new String[] { o.toString() };
	}

	/**
	 * @see IValueMap#getStringValue(String)
	 */
	public StringValue getStringValue(final String key)
	{
		return StringValue.valueOf(getString(key));
	}

	/**
	 * @see IValueMap#getTime(String)
	 */
	public final Time getTime(final String key) throws StringValueConversionException
	{
		return getStringValue(key).toTime();
	}

	/**
	 * @see IValueMap#isImmutable()
	 */
	public final boolean isImmutable()
	{
		return immutable;
	}

	/**
	 * @see IValueMap#makeImmutable()
	 */
	public final IValueMap makeImmutable()
	{
		immutable = true;
		return this;
	}

	/**
	 * @see java.util.Map#put(Object, Object)
	 */
	@Override
	public Object put(final String key, final Object value)
	{
		checkMutability();
		return super.put(key, value);
	}

	/**
	 * Adds the value to this <code>ValueMap</code> with the given key. If the key already is in the
	 * <code>ValueMap</code> it will combine the values into a <code>String</code> array, else it
	 * will just store the value itself.
	 * 
	 * @param key
	 *            the key to store the value under
	 * @param value
	 *            the value that must be added/merged to the <code>ValueMap</code>
	 * @return the value itself if there was no previous value, or a <code>String</code> array with
	 *         the combined values
	 */
	public final Object add(final String key, final String value)
	{
		checkMutability();
		final Object o = get(key);
		if (o == null)
		{
			return put(key, value);
		}
		else if (o.getClass().isArray())
		{
			int length = Array.getLength(o);
			String destArray[] = new String[length + 1];
			for (int i = 0; i < length; i++)
			{
				final Object arrayValue = Array.get(o, i);
				if (arrayValue != null)
				{
					destArray[i] = arrayValue.toString();
				}
			}
			destArray[length] = value;

			return put(key, destArray);
		}
		else
		{
			return put(key, new String[] { o.toString(), value });
		}
	}

	/**
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(final Map<? extends String, ? extends Object> map)
	{
		checkMutability();
		super.putAll(map);
	}

	/**
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public Object remove(final Object key)
	{
		checkMutability();
		return super.remove(key);
	}

	/**
	 * @see IValueMap#getKey(String)
	 */
	public String getKey(final String key)
	{
		for (Object keyValue : keySet())
		{
			if (keyValue instanceof String)
			{
				String keyString = (String)keyValue;
				if (key.equalsIgnoreCase(keyString))
				{
					return keyString;
				}
			}
		}
		return null;
	}

	/**
	 * Generates a <code>String</code> representation of this object.
	 * 
	 * @return <code>String</code> representation of this <code>ValueMap</code> consistent with the
	 *         tag-attribute style of markup elements. For example: <code>a="x" b="y" c="z"</code>.
	 */
	@Override
	public String toString()
	{
		final StringBuffer buffer = new StringBuffer();
		boolean first = true;
		for (Map.Entry<String, Object> entry : entrySet())
		{
			if (first == false)
			{
				buffer.append(' ');
			}
			first = false;

			buffer.append(entry.getKey());
			buffer.append(" = \"");
			final Object value = entry.getValue();
			if (value == null)
			{
				buffer.append("null");
			}
			else if (value.getClass().isArray())
			{
				buffer.append(Arrays.asList((Object[])value));
			}
			else
			{
				buffer.append(value);
			}

			buffer.append('\"');
		}
		return buffer.toString();
	}

	/**
	 * Throws an exception if <code>ValueMap</code> is immutable.
	 */
	private void checkMutability()
	{
		if (immutable)
		{
			throw new UnsupportedOperationException("Map is immutable");
		}
	}

	// //
	// // getAs convenience methods
	// //

	/**
	 * @see IValueMap#getAsBoolean(String)
	 * 
	 */
	public Boolean getAsBoolean(String key)
	{
		if (!containsKey(key))
		{
			return null;
		}

		try
		{
			return getBoolean(key);
		}
		catch (StringValueConversionException ignored)
		{
			return null;
		}
	}

	/**
	 * @see IValueMap#getAsBoolean(String, boolean)
	 * 
	 */
	public boolean getAsBoolean(String key, boolean defaultValue)
	{
		if (!containsKey(key))
		{
			return defaultValue;
		}

		try
		{
			return getBoolean(key);
		}
		catch (StringValueConversionException ignored)
		{
			return defaultValue;
		}
	}

	/**
	 * @see IValueMap#getAsInteger(String)
	 */
	public Integer getAsInteger(String key)
	{
		if (!containsKey(key))
		{
			return null;
		}

		try
		{
			return getInt(key);
		}
		catch (StringValueConversionException ignored)
		{
			return null;
		}
	}

	/**
	 * @see IValueMap#getAsInteger(String, int)
	 */
	public int getAsInteger(String key, int defaultValue)
	{
		try
		{
			return getInt(key, defaultValue);
		}
		catch (StringValueConversionException ignored)
		{
			return defaultValue;
		}
	}

	/**
	 * @see IValueMap#getAsLong(String)
	 */
	public Long getAsLong(String key)
	{
		if (!containsKey(key))
		{
			return null;
		}

		try
		{
			return getLong(key);
		}
		catch (StringValueConversionException ignored)
		{
			return null;
		}
	}

	/**
	 * @see IValueMap#getAsLong(String, long)
	 */
	public long getAsLong(String key, long defaultValue)
	{
		try
		{
			return getLong(key, defaultValue);
		}
		catch (StringValueConversionException ignored)
		{
			return defaultValue;
		}
	}

	/**
	 * @see IValueMap#getAsDouble(String)
	 */
	public Double getAsDouble(String key)
	{
		if (!containsKey(key))
		{
			return null;
		}

		try
		{
			return getDouble(key);
		}
		catch (StringValueConversionException ignored)
		{
			return null;
		}
	}

	/**
	 * @see IValueMap#getAsDouble(String, double)
	 */
	public double getAsDouble(final String key, final double defaultValue)
	{
		try
		{
			return getDouble(key, defaultValue);
		}
		catch (StringValueConversionException ignored)
		{
			return defaultValue;
		}
	}

	/**
	 * @see IValueMap#getAsDuration(String)
	 */
	public Duration getAsDuration(final String key)
	{
		return getAsDuration(key, null);
	}

	/**
	 * @see IValueMap#getAsDuration(String, Duration)
	 */
	public Duration getAsDuration(final String key, final Duration defaultValue)
	{
		if (!containsKey(key))
		{
			return defaultValue;
		}

		try
		{
			return getDuration(key);
		}
		catch (StringValueConversionException ignored)
		{
			return defaultValue;
		}
	}

	/**
	 * @see IValueMap#getAsTime(String)
	 */
	public Time getAsTime(final String key)
	{
		return getAsTime(key, null);
	}

	/**
	 * @see IValueMap#getAsTime(String, Time)
	 */
	public Time getAsTime(final String key, final Time defaultValue)
	{
		if (!containsKey(key))
		{
			return defaultValue;
		}

		try
		{
			return getTime(key);
		}
		catch (StringValueConversionException ignored)
		{
			return defaultValue;
		}
	}

	/**
	 * @see org.apache.wicket.util.value.IValueMap#getAsEnum(java.lang.String, java.lang.Class)
	 */
	public <T extends Enum<T>> T getAsEnum(final String key, final Class<T> eClass)
	{
		return getEnumImpl(key, eClass, null);
	}

	/**
	 * @see org.apache.wicket.util.value.IValueMap#getAsEnum(java.lang.String, java.lang.Enum)
	 */
	public <T extends Enum<T>> T getAsEnum(final String key, final T defaultValue)
	{
		if (defaultValue == null)
		{
			throw new IllegalArgumentException("Default value cannot be null");
		}

		return getEnumImpl(key, defaultValue.getClass(), defaultValue);
	}

	/**
	 * @see org.apache.wicket.util.value.IValueMap#getAsEnum(java.lang.String, java.lang.Class,
	 *      java.lang.Enum)
	 */
	public <T extends Enum<T>> T getAsEnum(final String key, final Class<T> eClass,
		final T defaultValue)
	{
		return getEnumImpl(key, eClass, defaultValue);
	}

	/**
	 * get enum implementation
	 * 
	 * @param key
	 * @param eClass
	 * @param defaultValue
	 * @param <T>
	 * @return Enum
	 */
	@SuppressWarnings( { "unchecked" })
	private <T extends Enum<T>> T getEnumImpl(final String key, final Class<?> eClass,
		final T defaultValue)
	{
		if (eClass == null)
		{
			throw new IllegalArgumentException("eClass value cannot be null");
		}

		String value = getString(key);
		if (value == null)
		{
			return defaultValue;
		}

		Method valueOf = null;
		try
		{
			valueOf = eClass.getMethod("valueOf", String.class);
		}
		catch (NoSuchMethodException e)
		{
			throw new RuntimeException("Could not find method valueOf(String s) for " +
				eClass.getName(), e);
		}

		try
		{
			return (T)valueOf.invoke(eClass, value);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException("Could not invoke method valueOf(String s) on " +
				eClass.getName(), e);
		}
		catch (InvocationTargetException e)
		{
			// IllegalArgumentException thrown if enum isn't defined - just return default
			if (e.getCause() instanceof IllegalArgumentException)
			{
				return defaultValue;
			}
			throw new RuntimeException(e); // shouldn't happen
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14477.java