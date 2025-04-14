error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15231.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15231.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15231.java
text:
```scala
r@@eturn "";

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
package wicket.util.convert;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import wicket.IConverterLocator;
import wicket.util.convert.converters.BooleanConverter;
import wicket.util.convert.converters.ByteConverter;
import wicket.util.convert.converters.CharacterConverter;
import wicket.util.convert.converters.DateConverter;
import wicket.util.convert.converters.DoubleConverter;
import wicket.util.convert.converters.FloatConverter;
import wicket.util.convert.converters.IntegerConverter;
import wicket.util.convert.converters.LongConverter;
import wicket.util.convert.converters.ShortConverter;
import wicket.util.convert.converters.SqlDateConverter;
import wicket.util.convert.converters.SqlTimeConverter;
import wicket.util.convert.converters.SqlTimestampConverter;
import wicket.util.lang.Objects;

/**
 * Implementation of IConverter interface, which converts objects from one class
 * to another. This class allows specific type converters implementing the
 * ITypeConverter interface to be registered as conversion operations for
 * specific types. By default this class registers type converters for Date,
 * String and all Java primitive types and their wrapper classes.
 * <p>
 * To convert from a Double value to a String value you can use the generalized
 * converter interface:
 * 
 * <pre>
 * final IConverter converter = new CoverterLocatorFactory().newConverter();
 * converter.setLocale(Locale.US);
 * converter.convert(new Double(7.1), String.class);
 * </pre>
 * 
 * Or this can be accomplished by directly using the StringConverter type
 * conversion class (which is registered as a type converter on the IConverter
 * returned by the converter factory above).
 * 
 * <pre>
 * final StringConverter converter = new StringConverter(Locale.US);
 * converter.convert(new Double(7.1));
 * </pre>
 * 
 * When using Wicket, you should rarely need to use any of the conversion
 * classes directly. There are convenient validators and conversion features
 * built into Wicket that you can use directly.
 * 
 * @see IConverterLocatorFactory
 * @author Eelco Hillenius
 * @author Jonathan Locke
 */
public class ConverterLocator implements IConverterLocator
{
	private static final long serialVersionUID = 1L;

	/** Maps Classes to ITypeConverters. */
	private final Map/*Class<?>, IConverter*/ classToConverter = new HashMap/*Class<?>, IConverter*/();

	/**
	 * CoverterLocator that is to be used when no registered converter is found.
	 */
	private class DefaultConverter implements IConverter
	{
		private Class/*?*/ type;

		/**
		 * Construct.
		 * 
		 * @param type
		 */
		public DefaultConverter(Class/*?*/ type)
		{
			this.type = type;
		}

		private static final long serialVersionUID = 1L;

		/**
		 * @see wicket.util.convert.IConverter#convertToObject(java.lang.String,
		 *      java.util.Locale)
		 */
		public Object convertToObject(String value, Locale locale)
		{
			if (value == null)
			{
				return null;
			}
			if ("".equals(value))
			{
				if (type == String.class)
				{
					return "";
				}
				return null;
			}

			try
			{
				return Objects.convertValue(value, type);
			}
			catch (Exception e)
			{
				throw new ConversionException(e.getMessage(), e).setSourceValue(value);
			}
		}

		/**
		 * @see wicket.util.convert.IConverter#convertToString(java.lang.Object,
		 *      java.util.Locale)
		 */
		public String convertToString(Object value, Locale locale)
		{
			if (value == null || "".equals(value))
			{
				return null;
			}

			return (String)Objects.convertValue(value, String.class);
		}
	};

	/**
	 * Constructor
	 */
	public ConverterLocator()
	{
		set(Boolean.TYPE, BooleanConverter.INSTANCE);
		set(Boolean.class, BooleanConverter.INSTANCE);
		set(Byte.TYPE, ByteConverter.INSTANCE);
		set(Byte.class, ByteConverter.INSTANCE);
		set(Character.TYPE, CharacterConverter.INSTANCE);
		set(Character.class, CharacterConverter.INSTANCE);
		set(Double.TYPE, DoubleConverter.INSTANCE);
		set(Double.class, DoubleConverter.INSTANCE);
		set(Float.TYPE, FloatConverter.INSTANCE);
		set(Float.class, FloatConverter.INSTANCE);
		set(Integer.TYPE, IntegerConverter.INSTANCE);
		set(Integer.class, IntegerConverter.INSTANCE);
		set(Long.TYPE, LongConverter.INSTANCE);
		set(Long.class, LongConverter.INSTANCE);
		set(Short.TYPE, ShortConverter.INSTANCE);
		set(Short.class, ShortConverter.INSTANCE);
		set(Date.class, new DateConverter());
		set(java.sql.Date.class, new SqlDateConverter());
		set(java.sql.Time.class, new SqlTimeConverter());
		set(java.sql.Timestamp.class, new SqlTimestampConverter());
	}

	/**
	 * Converts the given value to class c.
	 * 
	 * @param type
	 *            Class to get the converter for.
	 * 
	 * @return The converter for the given type
	 * 
	 * @see wicket.util.convert.IConverter#convert(java.lang.Object,
	 *      java.lang.Class, Locale)
	 */
	public final IConverter getConverter(Class type)
	{
		// Null is always converted to null
		if (type == null)
		{
			return new DefaultConverter(String.class);
		}


		// Get type converter for class
		final IConverter converter = get(type);
		if (converter == null)
		{
			return new DefaultConverter(type);
		}
		return converter;
	}

	/**
	 * Gets the type converter that is registered for class c.
	 * 
	 * @param c
	 *            The class to get the type converter for
	 * @return The type converter that is registered for class c or null if no
	 *         type converter was registered for class c
	 */
	public final IConverter get(Class/*?*/ c)
	{
		return (IConverter)classToConverter.get(c);
	}

	/**
	 * Removes the type converter currently registered for class c.
	 * 
	 * @param c
	 *            The class for which the converter registration should be
	 *            removed
	 * @return The converter that was registered for class c before removal or
	 *         null if none was registered
	 */
	public final IConverter remove(Class/*?*/ c)
	{
		return (IConverter)classToConverter.remove(c);
	}

	/**
	 * Registers a converter for use with class c.
	 * 
	 * @param converter
	 *            The converter to add
	 * @param c
	 *            The class for which the converter should be used
	 * @return The previous registered converter for class c or null if none was
	 *         registered yet for class c
	 */
	public final IConverter set(final Class/*?*/ c, final IConverter converter)
	{
		if (converter == null)
		{
			throw new IllegalArgumentException("CoverterLocator cannot be null");
		}
		if (c == null)
		{
			throw new IllegalArgumentException("Class cannot be null");
		}
		return (IConverter)classToConverter.put(c, converter);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15231.java