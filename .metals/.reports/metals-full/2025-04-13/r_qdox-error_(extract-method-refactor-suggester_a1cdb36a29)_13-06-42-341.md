error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/437.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/437.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/437.java
text:
```scala
public static S@@tring simpleName(final Class c)

/*
 * $Id$ $Revision$
 * $Date$
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
package wicket.util.lang;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

import wicket.markup.MarkupException;
import wicket.util.convert.ConverterFactory;
import wicket.util.convert.IConverter;
import wicket.util.string.Strings;

/**
 * Utilities for dealing with classes.
 * 
 * @author Jonathan Locke
 */
public final class Classes
{
	/**
	 * Instantiation not allowed
	 */
	private Classes()
	{
	}

	/**
	 * Gets the name of a given class
	 * 
	 * @param c
	 *            The class
	 * @return The class name
	 */
	public static String name(final Class c)
	{
		return Strings.lastPathComponent(c.getName(), '.');
	}

	/**
	 * Takes a Class and a relative path to a class and returns any class at
	 * that relative path. For example, if the given Class was java.lang.System
	 * and the relative path was "../util/List", then the java.util.List class
	 * would be returned.
	 * 
	 * @param scope
	 *            The package to start at
	 * @param path
	 *            The relative path to the class
	 * @return The class
	 * @throws ClassNotFoundException
	 */
	public static Class relativeClass(final Class scope, final String path)
			throws ClassNotFoundException
	{
		return Class.forName(Packages.absolutePath(scope, path).replace('/', '.'));
	}

	/**
	 * Invoke the setter method for 'name' on object and provide the 'value'
	 * 
	 * @param object
	 * @param name
	 * @param value
	 * @param locale
	 */
	public static void invokeSetter(final Object object, final String name, final String value,
			final Locale locale)
	{
		// Note: tag attributes are maintained in a LowerCaseKeyValueMap, thus
		// 'name' will be all lowercase.

		// Note: because the attributes are all lowercase, there is slight
		// possibility of error due to naming issues.
		
		// Note: all setters must start with "set"

		// Get the setter for the attribute
		final String methodName = "set" + name;
		final Method[] methods = object.getClass().getMethods();
		Method method = null;
		for (int i = 0; i < methods.length; i++)
		{
			if (methods[i].getName().equalsIgnoreCase(methodName))
			{
				method = methods[i];
			}
		}

		if (method == null)
		{
			throw new MarkupException("Unable to initialize Component. Method with name "
					+ methodName + " not found");
		}

		// The method must have a single parameter
		final Class[] parameterClasses = method.getParameterTypes();
		if (parameterClasses.length != 1)
		{
			throw new MarkupException("Unable to initialize Component. Method with name "
					+ methodName + " must have one and only one parameter");
		}

		// Convert the parameter if necessary, depending on the setter's
		// attribute
		final Class paramClass = parameterClasses[0];
		try
		{
			final IConverter converter = new ConverterFactory().newConverter(Locale.US);
			final Object param = converter.convert(value, paramClass);
			if (param == null)
			{
				throw new MarkupException("Unable to convert value '" + value + "' into "
						+ paramClass + ". May be there is no converter for that type registered?");
			}
			method.invoke(object, new Object[] { param });
		}
		catch (IllegalAccessException ex)
		{
			throw new MarkupException(
					"Unable to initialize Component. Failure while invoking method " + methodName
							+ ". Cause: " + ex);
		}
		catch (InvocationTargetException ex)
		{
			throw new MarkupException(
					"Unable to initialize Component. Failure while invoking method " + methodName
							+ ". Cause: " + ex);
		}
		catch (NumberFormatException ex)
		{
			throw new MarkupException(
					"Unable to initialize Component. Failure while invoking method " + methodName
							+ ". Cause: " + ex);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/437.java