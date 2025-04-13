error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3490.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3490.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3490.java
text:
```scala
m@@odel.put("exception", e);

/*
 * $Id$
 * $Revision$ $Date$
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
package wicket.markup.html.form.validation;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import wicket.Session;
import wicket.util.convert.ConversionException;
import wicket.util.convert.IConverter;
import wicket.util.string.Strings;

/**
 * Validates input by trying it to convert to the given type using the
 * {@link wicket.util.convert.IConverter}instance of the component doing the
 * validation.
 * <p>
 * This component adds ${type}, ${exception}, ${locale} and ${format} to the
 * model for OGNL error message interpolation. Format is only valid if the type
 * conversion involves a date.
 * 
 * @author Eelco Hillenius
 * @author Jonathan Locke
 */
public class TypeValidator extends StringValidator
{
	/** The locale to use */
	private Locale locale = null;

	/** The type to use for checking. */
	private Class type;

	/**
	 * Constructor. The current session's locale will be used for conversion.
	 * 
	 * @param type
	 *            The type to use for checking
	 */
	public TypeValidator(final Class type)
	{
		this.type = type;
	}

	/**
	 * Construct. If not-null, the given locale will be used for conversion.
	 * Otherwise the session's locale will be used for conversion.
	 * 
	 * @param type
	 *            The type to use for checking
	 * @param locale
	 *            The locale to use
	 */
	public TypeValidator(final Class type, final Locale locale)
	{
		this.type = type;
		this.locale = locale;
	}

	/**
	 * Gets the type to use for checking.
	 * 
	 * @return the type to use for checking
	 */
	public final Class getType()
	{
		return type;
	}


	/**
	 * Validates input by trying it to convert to the given type using the
	 * {@link wicket.util.convert.IConverter}instance of the component doing
	 * the validation.
	 * 
	 * @see wicket.markup.html.form.validation.StringValidator#onValidate(java.lang.String)
	 */
	public void onValidate(String value)
	{
		// If value is non-empty
		if (!Strings.isEmpty(value))
		{
			// Check value by attempting to convert it
			final IConverter converter = getFormComponent().getConverter();
			try
			{
				converter.convert(value, type);
			}
			catch (ConversionException e)
			{
				error(messageModel(e));
			}
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "[TypeValidator type = " + type + ", locale = " + getLocale() + "]";
	}

	/**
	 * Gets the message context.
	 * 
	 * @param e
	 *            the conversion exception
	 * @return a map with variables for interpolation
	 */
	protected Map messageModel(final ConversionException e)
	{
		final Map model = super.messageModel();
		model.put("type", type);
		final Locale locale = e.getLocale();
		if (locale != null)
		{
			model.put("locale", locale);
		}
		model.put("exception", e.getMessage());
		Format format = e.getFormat();
		if (format instanceof SimpleDateFormat)
		{
			model.put("format", ((SimpleDateFormat)format).toLocalizedPattern());
		}
		return model;
	}

	/**
	 * Gets the locale to use. if null and useLocaled == true, the session's
	 * locale will be used..
	 * 
	 * @return the locale to use
	 */
	private final Locale getLocale()
	{
		if (locale == null)
		{
			return Session.get().getLocale();
		}
		return locale;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3490.java