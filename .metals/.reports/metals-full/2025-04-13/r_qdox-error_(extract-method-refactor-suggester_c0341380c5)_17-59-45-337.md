error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8252.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8252.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8252.java
text:
```scala
r@@eturn converter.getDatePattern(getLocale());

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
package org.apache.wicket.datetime.markup.html.form;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.markup.html.form.AbstractTextComponent.ITextFormatProvider;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.Args;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

/**
 * A TextField that is mapped to a <code>java.util.Date</code> object and that uses Joda time to
 * parse and format values.
 * <p>
 * You should use on of the factory methods to construct the kind you want or use the public
 * constructor and pass in the converter to use.
 * </p>
 * <p>
 * This component tries to apply the time zone difference between the client and server. See the
 * {@link DateConverter#getApplyTimeZoneDifference() date converter} of this package for more
 * information on that.
 * </p>
 * 
 * @see StyleDateConverter
 * @see DateTime
 * @see DateTimeFormat
 * @see DateTimeZone
 * 
 * @author eelcohillenius
 */
public class DateTextField extends TextField<Date> implements ITextFormatProvider
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new DateTextField defaulting to using a short date pattern
	 * 
	 * @param id
	 *            The id of the text field
	 * @param model
	 *            The model
	 * @param datePattern
	 *            The pattern to use. Must be not null. See {@link SimpleDateFormat} for available
	 *            patterns.
	 * @return DateTextField
	 */
	public static DateTextField forDatePattern(String id, IModel<Date> model, String datePattern)
	{
		return new DateTextField(id, model, new PatternDateConverter(datePattern, true));
	}

	/**
	 * Creates a new DateTextField defaulting to using a short date pattern
	 * 
	 * @param id
	 *            The id of the text field
	 * @param datePattern
	 *            The pattern to use. Must be not null. See {@link SimpleDateFormat} for available
	 *            patterns.
	 * @return DateTextField
	 */
	public static DateTextField forDatePattern(String id, String datePattern)
	{
		return forDatePattern(id, null, datePattern);
	}

	/**
	 * Creates a new DateTextField using the provided date style.
	 * 
	 * @param id
	 *            The id of the text field
	 * @param model
	 *            The model
	 * @param dateStyle
	 *            Date style to use. The first character is the date style, and the second character
	 *            is the time style. Specify a character of 'S' for short style, 'M' for medium, 'L'
	 *            for long, and 'F' for full. A date or time may be ommitted by specifying a style
	 *            character '-'. See {@link DateTimeFormat#forStyle(String)}.
	 * @return DateTextField
	 */
	public static DateTextField forDateStyle(String id, IModel<Date> model, String dateStyle)
	{
		return new DateTextField(id, model, new StyleDateConverter(dateStyle, true));
	}

	/**
	 * Creates a new DateTextField using the provided date style.
	 * 
	 * @param id
	 *            The id of the text field
	 * @param dateStyle
	 *            Date style to use. The first character is the date style, and the second character
	 *            is the time style. Specify a character of 'S' for short style, 'M' for medium, 'L'
	 *            for long, and 'F' for full. A date or time may be ommitted by specifying a style
	 *            character '-'. See {@link DateTimeFormat#forStyle(String)}.
	 * @return DateTextField
	 */
	public static DateTextField forDateStyle(String id, String dateStyle)
	{
		return forDateStyle(id, null, dateStyle);
	}

	/**
	 * Creates a new DateTextField defaulting to using a short date pattern
	 * 
	 * @param id
	 *            The id of the text field
	 * @return DateTextField
	 */
	public static DateTextField forShortStyle(String id)
	{
		return forShortStyle(id, null, true);
	}

	/**
	 * Creates a new DateTextField defaulting to using a short date pattern
	 * 
	 * @param id
	 *            The id of the text field
	 * @param model
	 *            The model
	 * @return DateTextField
	 */
	public static DateTextField forShortStyle(String id, IModel<Date> model,
		boolean applyTimeZoneDifference)
	{
		return new DateTextField(id, model, new StyleDateConverter(applyTimeZoneDifference));
	}

	/**
	 * Creates a new DateTextField using the provided converter.
	 * 
	 * @param id
	 *            The id of the text field
	 * @param converter
	 *            the date converter
	 * @return DateTextField
	 */
	public static DateTextField withConverter(String id, DateConverter converter)
	{
		return withConverter(id, null, converter);
	}

	/**
	 * Creates a new DateTextField using the provided converter.
	 * 
	 * @param id
	 *            The id of the text field
	 * @param model
	 *            The model
	 * @param converter
	 *            the date converter
	 * @return DateTextField
	 */
	public static DateTextField withConverter(String id, IModel<Date> model, DateConverter converter)
	{
		return new DateTextField(id, model, converter);
	}

	/**
	 * The converter for the TextField
	 */
	private final DateConverter converter;

	/**
	 * Construct with a converter.
	 * 
	 * @param id
	 *            The component id
	 * @param model
	 *            The model
	 * @param converter
	 *            The converter to use
	 */
	public DateTextField(String id, IModel<Date> model, DateConverter converter)
	{
		super(id, model, Date.class);

		Args.notNull(converter, "converter");
		converter.setComponent(this);
		this.converter = converter;
	}

	/**
	 * @return The specialized converter.
	 * @see org.apache.wicket.Component#getConverter(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final <C> IConverter<C> getConverter(Class<C> clazz)
	{
		if (Date.class.isAssignableFrom(clazz))
		{
			return (IConverter<C>)converter;
		}
		else
		{
			return super.getConverter(clazz);
		}
	}

	/**
	 * @see org.apache.wicket.markup.html.form.AbstractTextComponent.ITextFormatProvider#getTextFormat()
	 */
	public final String getTextFormat()
	{
		return converter.getDatePattern();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8252.java