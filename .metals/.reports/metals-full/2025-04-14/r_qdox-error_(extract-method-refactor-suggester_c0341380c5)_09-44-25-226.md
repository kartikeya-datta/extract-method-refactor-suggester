error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14022.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14022.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14022.java
text:
```scala
S@@tringBuffer msg = new StringBuffer();

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
package org.apache.wicket.spring;

import java.lang.ref.WeakReference;

import org.apache.wicket.proxy.IProxyTargetLocator;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.util.lang.Objects;
import org.apache.wicket.util.string.Strings;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

/**
 * Implementation of {@link IProxyTargetLocator} that can locate beans within a
 * spring application context. Beans are looked up by the combination of name
 * and type, if name is omitted only type is used.
 * 
 * @author Igor Vaynberg (ivaynberg)
 * @author Istvan Devai
 */
public class SpringBeanLocator implements IProxyTargetLocator
{
	// Weak reference so we don't hold up WebApp classloader garbage collection.
	private transient WeakReference/* <Class> */beanTypeCache;

	private String beanTypeName;

	private String beanName;

	private ISpringContextLocator springContextLocator;

	private Boolean singletonCache = null;

	/**
	 * Constructor
	 * 
	 * @param beanType
	 *            bean class
	 * @param locator
	 *            spring context locator
	 */
	public SpringBeanLocator(Class beanType, ISpringContextLocator locator)
	{
		this(null, beanType, locator);
	}

	/**
	 * Constructor
	 * 
	 * @param beanName
	 *            bean name
	 * @param beanType
	 *            bean class
	 * @param locator
	 *            spring context locator
	 */
	public SpringBeanLocator(String beanName, Class beanType,
			ISpringContextLocator locator)
	{
		if (locator == null)
		{
			throw new IllegalArgumentException("[locator] argument cannot be null");
		}
		if (beanType == null)
		{
			throw new IllegalArgumentException("[beanType] argument cannot be null");
		}

		this.beanTypeCache = new WeakReference(beanType);
		this.beanTypeName = beanType.getName();
		this.springContextLocator = locator;
		this.beanName = beanName;
		this.springContextLocator = locator;
	}

	/**
	 * Returns the name of the Bean as registered to Spring. Throws IllegalState
	 * exception if none or more then one beans are found.
	 * 
	 * @param ctx
	 *            spring application context
	 * @param clazz
	 *            bean class
	 * @throws IllegalStateException
	 * @return spring name of the bean
	 */
	private final String getBeanNameOfClass(ApplicationContext ctx, Class clazz)
	{
		String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(ctx, clazz);
		if (names.length == 0)
		{
			throw new IllegalStateException("bean of type ["
					+ clazz.getName() + "] not found");
		}
		if (names.length > 1)
		{
			StringBuilder msg = new StringBuilder();
			msg.append("more then one bean of type [");
			msg.append(clazz.getName());
			msg.append("] found, you have to specify the name of the bean ");
			msg.append("(@SpringBean(name=\"foo\")) in order to resolve this conflict. ");

			msg.append("Matched beans: ");
			msg.append(Strings.join(",", names));

			throw new IllegalStateException(msg.toString());
		}
		return names[0];
	}

	/**
	 * @return returns whether the bean (the locator is supposed to istantiate)
	 *         is a singleton or not
	 */
	public boolean isSingletonBean()
	{
		if (singletonCache == null)
		{
			singletonCache = Boolean.valueOf(getSpringContext()
					.isSingleton(getBeanName()));
		}
		return singletonCache.booleanValue();
	}

	/**
	 * @return bean class this locator is configured with
	 */
	public Class getBeanType()
	{
		Class clazz = beanTypeCache == null ? null : (Class) beanTypeCache.get();
		if (clazz == null)
		{
			beanTypeCache = new WeakReference(clazz = Classes.resolveClass(beanTypeName));
			if (clazz == null)
			{
				throw new RuntimeException("SpringBeanLocator could not find class ["
						+ beanTypeName + "] needed to locate the ["
						+ ((beanName != null) ? (beanName) : ("bean name not specified"))
						+ "] bean");
			}
		}
		return clazz;
	}

	/**
	 * @see org.apache.wicket.proxy.IProxyTargetLocator#locateProxyTarget()
	 */
	public Object locateProxyTarget()
	{
		final ApplicationContext context = getSpringContext();

		if (beanName != null && beanName.length() > 0)
		{
			return lookupSpringBean(context, beanName, getBeanType());
		}
		else
		{
			return lookupSpringBean(context, getBeanType());
		}
	}

	private ApplicationContext getSpringContext()
	{
		final ApplicationContext context = springContextLocator.getSpringContext();

		if (context == null)
		{
			throw new IllegalStateException(
					"spring application context locator returned null");
		}
		return context;
	}

	/**
	 * @return bean name this locator is configured with
	 */
	public final String getBeanName()
	{
		if (beanName == null || "".equals(beanName))
		{
			beanName = getBeanNameOfClass(getSpringContext(), getBeanType());

		}
		return beanName;
	}

	/**
	 * @return context locator this locator is configured with
	 */
	public final ISpringContextLocator getSpringContextLocator()
	{
		return springContextLocator;
	}

	/**
	 * Looks up a bean by its class. Throws IllegalState exception if none or
	 * more then one beans are found.
	 * 
	 * @param ctx
	 *            spring application context
	 * 
	 * @param clazz
	 *            bean class
	 * @throws IllegalStateException
	 * @return found bean
	 */
	private final Object lookupSpringBean(ApplicationContext ctx, Class clazz)
	{
		return lookupSpringBean(ctx, getBeanNameOfClass(ctx, clazz), clazz);
	}

	/**
	 * Looks up a bean by its name and class. Throws IllegalState exception if
	 * bean not found.
	 * 
	 * @param ctx
	 *            spring application context
	 * 
	 * @param name
	 *            bean name
	 * @param clazz
	 *            bean class
	 * @throws IllegalStateException
	 * @return found bean
	 */
	private static Object lookupSpringBean(ApplicationContext ctx, String name,
			Class clazz)
	{
		try
		{
			return ctx.getBean(name, clazz);
		}
		catch (NoSuchBeanDefinitionException e)
		{
			throw new IllegalStateException("bean with name ["
					+ name + "] and class [" + clazz.getName() + "] not found");
		}
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		if (obj instanceof SpringBeanLocator)
		{
			SpringBeanLocator other = (SpringBeanLocator) obj;
			return beanTypeName.equals(other.beanTypeName)
					&& Objects.equal(beanName, other.beanName);
		}
		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int hashcode = beanTypeName.hashCode();
		if (beanName != null)
		{
			hashcode = hashcode + (127 * beanName.hashCode());
		}
		return hashcode;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14022.java