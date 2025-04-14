error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11287.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11287.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11287.java
text:
```scala
public v@@oid onInstantiation(Component component)

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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.proxy.LazyInitProxyFactory;

import com.google.inject.BindingAnnotation;
import com.google.inject.Guice;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;

/**
 * Injects fields/members of components using Guice.
 * <p>
 * Add this to your application in its {@link Application#init()} method like so:
 * 
 * <pre>
 * addComponentInstantiationListener(new GuiceComponentInjector(this));
 * </pre>
 * 
 * <p>
 * There are different constructors for this object depending on how you want to wire things. See
 * the javadoc for the constructors for more information.
 * 
 * @author Alastair Maw
 */
public class GuiceComponentInjector implements IComponentInstantiationListener
{
	/**
	 * Creates a new Wicket GuiceComponentInjector instance.
	 * <p>
	 * Internally this will create a new Guice {@link Injector} instance, with no {@link Module}
	 * instances. This is only useful if your beans have appropriate {@link ImplementedBy}
	 * annotations on them so that they can be automatically picked up with no extra configuration
	 * code.
	 * 
	 * @param app
	 */
	public GuiceComponentInjector(Application app)
	{
		this(app, new Module[0]);
	}

	/**
	 * Creates a new Wicket GuiceComponentInjector instance, using the supplied Guice {@link Module}
	 * instances to create a new Guice {@link Injector} instance internally.
	 * 
	 * @param app
	 * @param modules
	 */
	public GuiceComponentInjector(Application app, Module... modules)
	{
		this(app, Guice.createInjector(app.getConfigurationType().equals(Application.DEVELOPMENT)
				? Stage.DEVELOPMENT
				: Stage.PRODUCTION, modules));
	}

	/**
	 * Creates a new Wicket GuiceComponentInjector instance, using the provided Guice
	 * {@link Injector} instance.
	 * 
	 * @param app
	 * @param injector
	 */
	public GuiceComponentInjector(Application app, Injector injector)
	{
		app.setMetaData(GuiceInjectorHolder.INJECTOR_KEY, new GuiceInjectorHolder(injector));
		app.setMetaData(GuiceTypeStore.TYPESTORE_KEY, new GuiceTypeStore());
	}

	public void inject(Object object)
	{
		Class< ? > current = object.getClass();
		do
		{
			Field[] currentFields = current.getDeclaredFields();
			for (final Field field : currentFields)
			{
				if (!Modifier.isStatic(field.getModifiers()) &&
						field.getAnnotation(Inject.class) != null)
				{
					try
					{
						Annotation bindingAnnotation = findBindingAnnotation(field.getAnnotations());
						Object proxy = LazyInitProxyFactory.createProxy(field.getType(),
								new GuiceProxyTargetLocator(field.getGenericType(),
										bindingAnnotation));

						if (!field.isAccessible())
						{
							field.setAccessible(true);
						}
						field.set(object, proxy);
					}
					catch (IllegalAccessException e)
					{
						throw new WicketRuntimeException("Error Guice-injecting field " +
								field.getName() + " in " + object, e);
					}
					catch (MoreThanOneBindingException e)
					{
						throw new RuntimeException(
								"Can't have more than one BindingAnnotation on field " +
										field.getName() + " of class " +
										object.getClass().getName());
					}
				}
			}
			Method[] currentMethods = current.getDeclaredMethods();
			for (final Method method : currentMethods)
			{
				if (!Modifier.isStatic(method.getModifiers()) &&
						method.getAnnotation(Inject.class) != null)
				{
					Annotation[][] paramAnnotations = method.getParameterAnnotations();
					Class< ? >[] paramTypes = method.getParameterTypes();
					Type[] genericParamTypes = method.getGenericParameterTypes();
					Object[] args = new Object[paramTypes.length];
					for (int i = 0; i < paramTypes.length; i++)
					{
						Type paramType;
						if (genericParamTypes[i] instanceof ParameterizedType)
						{
							paramType = ((ParameterizedType)genericParamTypes[i]).getRawType();
						}
						else
						{
							paramType = paramTypes[i];
						}
						try
						{
							Annotation bindingAnnotation = findBindingAnnotation(paramAnnotations[i]);
							args[i] = LazyInitProxyFactory.createProxy(paramTypes[i],
									new GuiceProxyTargetLocator(genericParamTypes[i],
											bindingAnnotation));
						}
						catch (MoreThanOneBindingException e)
						{
							throw new RuntimeException(
									"Can't have more than one BindingAnnotation on parameter " + i +
											"(" + paramType + ") of method " + method.getName() +
											" of class " + object.getClass().getName());
						}
					}
					try
					{
						method.invoke(object, args);
					}
					catch (IllegalAccessException e)
					{
						throw new WicketRuntimeException(e);
					}
					catch (InvocationTargetException e)
					{
						throw new WicketRuntimeException(e);
					}
				}
			}
			current = current.getSuperclass();
		}
		// Do a null check in case Object isn't in the current classloader.
		while (current != null && current != Object.class);
	}

	public void onInstantiation(Component< ? > component)
	{
		inject(component);
	}

	private Annotation findBindingAnnotation(Annotation[] annotations)
			throws MoreThanOneBindingException
	{
		Annotation bindingAnnotation = null;

		// Work out if we have a BindingAnnotation on this parameter.
		for (int i = 0; i < annotations.length; i++)
		{
			if (annotations[i].annotationType().getAnnotation(BindingAnnotation.class) != null)
			{
				if (bindingAnnotation != null)
				{
					throw new MoreThanOneBindingException();
				}
				bindingAnnotation = annotations[i];
			}
		}
		return bindingAnnotation;
	}

	private static class MoreThanOneBindingException extends Exception
	{
		private static final long serialVersionUID = 1L;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11287.java