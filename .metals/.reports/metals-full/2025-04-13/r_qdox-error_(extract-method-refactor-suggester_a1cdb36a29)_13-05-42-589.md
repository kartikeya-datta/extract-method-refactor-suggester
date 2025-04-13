error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7054.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7054.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7054.java
text:
```scala
p@@aramTypes[i - 2] = Class.forName(data[i]);

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
package org.apache.wicket.guice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.apache.wicket.Application;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.proxy.IProxyTargetLocator;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

class GuiceProxyTargetLocator implements IProxyTargetLocator
{
	private static final long serialVersionUID = 1L;

	private final Annotation bindingAnnotation;

	private final boolean optional;

	private final String[] data;

	/** index of argument in the method being injected, or -1 for field */
	private final int argIndex;

	GuiceProxyTargetLocator(Field field, Annotation bindingAnnotation, boolean optional)
	{
		this.bindingAnnotation = bindingAnnotation;
		this.optional = optional;
		data = new String[2];
		data[0] = field.getDeclaringClass().getName();
		data[1] = field.getName();
		argIndex = -1;
	}

	GuiceProxyTargetLocator(Method method, int argIndex, Annotation bindingAnnotation,
			boolean optional)
	{
		this.bindingAnnotation = bindingAnnotation;
		this.optional = optional;
		data = new String[2 + method.getParameterTypes().length];
		data[0] = method.getDeclaringClass().getName();
		data[1] = method.getName();
		for (int i = 0; i < method.getParameterTypes().length; i++)
		{
			data[2 + i] = method.getParameterTypes()[i].getName();
		}
		this.argIndex = argIndex;
	}

	public Object locateProxyTarget()
	{
		final GuiceInjectorHolder holder = Application.get().getMetaData(
				GuiceInjectorHolder.INJECTOR_KEY);

		final Type type;
		try
		{

			Class< ? > clazz = Class.forName(data[0]);
			if (argIndex < 0)
			{
				final Field field = clazz.getDeclaredField(data[1]);
				type = field.getGenericType();
			}
			else
			{
				Class< ? >[] paramTypes = new Class[data.length - 2];
				for (int i = 2; i < data.length; i++)
				{
					paramTypes[2 - i] = Class.forName(data[i]);
				}
				final Method method = clazz.getDeclaredMethod(data[1], paramTypes);
				type = method.getGenericParameterTypes()[argIndex];
			}
		}
		catch (Exception e)
		{
			throw new WicketRuntimeException("Error accessing member: " + data[1] + " of class: " +
					data[0], e);
		}

		// using TypeLiteral to retrieve the key gives us automatic support for
		// Providers and other injectable TypeLiterals
		final Key< ? > key;

		if (bindingAnnotation == null)
		{
			key = Key.get(TypeLiteral.get(type));
		}
		else
		{
			key = Key.get(TypeLiteral.get(type), bindingAnnotation);
		}

		Injector injector = holder.getInjector();

		// if the Inject annotation is marked optional and no binding is found
		// then skip this injection (WICKET-2241)
		if (optional)
		{
			// Guice 2.0 throws a ConfigurationException if no binding is find while 1.0 simply
			// returns null.
			try
			{
				if (injector.getBinding(key) == null)
				{
					return null;
				}
			}
			catch (RuntimeException e)
			{
				return null;
			}
		}

		return injector.getInstance(key);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7054.java