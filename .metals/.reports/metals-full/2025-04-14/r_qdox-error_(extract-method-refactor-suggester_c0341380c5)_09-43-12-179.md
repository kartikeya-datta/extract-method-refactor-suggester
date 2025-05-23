error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8577.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8577.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8577.java
text:
```scala
public O@@bject getBean(String name, Object... args) throws BeansException {

/*
 * Copyright 2002-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.jndi.support;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.jndi.JndiLocatorSupport;
import org.springframework.jndi.TypeMismatchNamingException;

/**
 * Simple JNDI-based implementation of Spring's
 * {@link org.springframework.beans.factory.BeanFactory} interface.
 * Does not support enumerating bean definitions, hence doesn't implement
 * the {@link org.springframework.beans.factory.ListableBeanFactory} interface.
 *
 * <p>This factory resolves given bean names as JNDI names within the
 * J2EE application's "java:comp/env/" namespace. It caches the resolved
 * types for all obtained objects, and optionally also caches shareable
 * objects (if they are explicitly marked as
 * {@link #addShareableResource shareable resource}.
 *
 * <p>The main intent of this factory is usage in combination with Spring's
 * {@link org.springframework.context.annotation.CommonAnnotationBeanPostProcessor},
 * configured as "resourceFactory" for resolving <code>@Resource</code>
 * annotations as JNDI objects without intermediate bean definitions.
 * It may be used for similar lookup scenarios as well, of course,
 * in particular if BeanFactory-style type checking is required.
 *
 * @author Juergen Hoeller
 * @since 2.5
 * @see org.springframework.beans.factory.support.DefaultListableBeanFactory
 * @see org.springframework.context.annotation.CommonAnnotationBeanPostProcessor
 */
public class SimpleJndiBeanFactory extends JndiLocatorSupport implements BeanFactory {

	/** JNDI names of resources that are known to be shareable, i.e. can be cached */
	private final Set<String> shareableResources = new HashSet<String>();

	/** Cache of shareable singleton objects: bean name --> bean instance */
	private final Map<String, Object> singletonObjects = new HashMap<String, Object>();

	/** Cache of the types of nonshareable resources: bean name --> bean type */
	private final Map<String, Class> resourceTypes = new HashMap<String, Class>();


	public SimpleJndiBeanFactory() {
		setResourceRef(true);
	}


	/**
	 * Set a list of names of shareable JNDI resources,
	 * which this factory is allowed to cache once obtained.
	 * @param shareableResources the JNDI names
	 * (typically within the "java:comp/env/" namespace)
	 */
	public void setShareableResources(String[] shareableResources) {
		this.shareableResources.addAll(Arrays.asList(shareableResources));
	}

	/**
	 * Add the name of a shareable JNDI resource,
	 * which this factory is allowed to cache once obtained.
	 * @param shareableResource the JNDI name
	 * (typically within the "java:comp/env/" namespace)
	 */
	public void addShareableResource(String shareableResource) {
		this.shareableResources.add(shareableResource);
	}


	public Object getBean(String name) throws BeansException {
		return getBean(name, Object.class);
	}

	public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		try {
			if (isSingleton(name)) {
				return doGetSingleton(name, requiredType);
			}
			else {
				return lookup(name, requiredType);
			}
		}
		catch (NameNotFoundException ex) {
			throw new NoSuchBeanDefinitionException(name, "not found in JNDI environment");
		}
		catch (TypeMismatchNamingException ex) {
			throw new BeanNotOfRequiredTypeException(name, ex.getRequiredType(), ex.getActualType());
		}
		catch (NamingException ex) {
			throw new BeanDefinitionStoreException("JNDI environment", name, "JNDI lookup failed", ex);
		}
	}

	public Object getBean(String name, Object[] args) throws BeansException {
		if (args != null) {
			throw new UnsupportedOperationException(
					"SimpleJndiBeanFactory does not support explicit bean creation arguments)");
		}
		return getBean(name);
	}

	public boolean containsBean(String name) {
		if (this.singletonObjects.containsKey(name) || this.resourceTypes.containsKey(name)) {
			return true;
		}
		try {
			doGetType(name);
			return true;
		}
		catch (NamingException ex) {
			return false;
		}
	}

	public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		return this.shareableResources.contains(name);
	}

	public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
		return !this.shareableResources.contains(name);
	}

	public boolean isTypeMatch(String name, Class targetType) throws NoSuchBeanDefinitionException {
		Class type = getType(name);
		return (targetType == null || (type != null && targetType.isAssignableFrom(type)));
	}

	public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		try {
			return doGetType(name);
		}
		catch (NameNotFoundException ex) {
			throw new NoSuchBeanDefinitionException(name, "not found in JNDI environment");
		}
		catch (NamingException ex) {
			return null;
		}
	}

	public String[] getAliases(String name) {
		return new String[0];
	}


	@SuppressWarnings("unchecked")
	private <T> T doGetSingleton(String name, Class<T> requiredType) throws NamingException {
		synchronized (this.singletonObjects) {
			if (this.singletonObjects.containsKey(name)) {
				Object jndiObject = this.singletonObjects.get(name);
				if (requiredType != null && !requiredType.isInstance(jndiObject)) {
					throw new TypeMismatchNamingException(
							convertJndiName(name), requiredType, (jndiObject != null ? jndiObject.getClass() : null));
				}
				return (T) jndiObject;
			}
			T jndiObject = lookup(name, requiredType);
			this.singletonObjects.put(name, jndiObject);
			return jndiObject;
		}
	}

	private Class doGetType(String name) throws NamingException {
		if (isSingleton(name)) {
			Object jndiObject = doGetSingleton(name, null);
			return (jndiObject != null ? jndiObject.getClass() : null);
		}
		else {
			synchronized (this.resourceTypes) {
				if (this.resourceTypes.containsKey(name)) {
					return this.resourceTypes.get(name);
				}
				else {
					Object jndiObject = lookup(name, null);
					Class type = (jndiObject != null ? jndiObject.getClass() : null);
					this.resourceTypes.put(name, type);
					return type;
				}
			}
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8577.java