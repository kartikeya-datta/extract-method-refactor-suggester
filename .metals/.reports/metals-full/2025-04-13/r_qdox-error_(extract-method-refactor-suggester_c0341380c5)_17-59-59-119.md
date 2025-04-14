error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/638.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/638.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/638.java
text:
```scala
public S@@tring convertToString(final Number value, final Locale locale)

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
package org.apache.wicket.util.convert.converter;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.wicket.util.convert.ConversionException;


/**
 * Base class for all number converters.
 * 
 * @author Jonathan Locke
 * 
 */
public abstract class AbstractNumberConverter<N extends Number> extends AbstractConverter<N>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param locale
	 * @return Returns the numberFormat.
	 */
	public abstract NumberFormat getNumberFormat(Locale locale);


	/**
	 * Parses a value as a String and returns a Number.
	 * 
	 * @param value
	 *            The object to parse (after converting with toString())
	 * @param min
	 *            The minimum allowed value
	 * @param max
	 *            The maximum allowed value
	 * @param locale
	 * @return The number
	 * @throws ConversionException
	 *             if value is unparsable or out of range
	 */
	protected N parse(Object value, final double min, final double max, Locale locale)
	{
		if (locale == null)
		{
			locale = Locale.getDefault();
		}

		if (value == null)
		{
			return null;
		}
		else if (value instanceof String)
		{
			// Convert spaces to no-break space (U+00A0) to fix problems with
			// browser conversions.
			// Space is not valid thousands-separator, but no-br space is.
			String v = (String)value;
			value = v.replace(' ', '\u00A0');
		}

		final NumberFormat numberFormat = getNumberFormat(locale);
		final N number = parse(numberFormat, value, locale);

		if (number == null)
		{
			return null;
		}

		if (number.doubleValue() < min)
		{
			throw newConversionException("Value cannot be less than " + min, value, locale).setFormat(
				numberFormat);
		}

		if (number.doubleValue() > max)
		{
			throw newConversionException("Value cannot be greater than " + max, value, locale).setFormat(
				numberFormat);
		}

		return number;
	}

	/**
	 * @see org.apache.wicket.util.convert.IConverter#convertToString(C, Locale)
	 */
	@Override
	public String convertToString(final Number value, Locale locale)
	{
		NumberFormat fmt = getNumberFormat(locale);
		if (fmt != null)
		{
			return fmt.format(value);
		}
		return value.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/638.java