error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4787.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4787.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4787.java
text:
```scala
public C@@lass<?> getType(String name) throws NoSuchBeanDefinitionException {

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

package org.springframework.beans.factory.support;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.BeanIsNotAFactoryException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

/**
 * Static {@link org.springframework.beans.factory.BeanFactory} implementation
 * which allows to register existing singleton instances programmatically.
 * Does not have support for prototype beans or aliases.
 *
 * <p>Serves as example for a simple implementation of the
 * {@link org.springframework.beans.factory.ListableBeanFactory} interface,
 * managing existing bean instances rather than creating new ones based on bean
 * definitions, and not implementing any extended SPI interfaces (such as
 * {@link org.springframework.beans.factory.config.ConfigurableBeanFactory}).
 *
 * <p>For a full-fledged factory based on bean definitions, have a look
 * at {@link DefaultListableBeanFactory}.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 06.01.2003
 * @see DefaultListableBeanFactory
 */
public class StaticListableBeanFactory implements ListableBeanFactory {

	/** Map from bean name to bean instance */
	private final Map<String, Object> beans = new HashMap<String, Object>();


	/**
	 * Add a new singleton bean.
	 * Will overwrite any existing instance for the given name.
	 * @param name the name of the bean
	 * @param bean the bean instance
	 */
	public void addBean(String name, Object bean) {
		this.beans.put(name, bean);
	}


	//---------------------------------------------------------------------
	// Implementation of BeanFactory interface
	//---------------------------------------------------------------------

	public Object getBean(String name) throws BeansException {
		String beanName = BeanFactoryUtils.transformedBeanName(name);
		Object bean = this.beans.get(beanName);

		if (bean == null) {
			throw new NoSuchBeanDefinitionException(beanName,
					"Defined beans are [" + StringUtils.collectionToCommaDelimitedString(this.beans.keySet()) + "]");
		}

		// Don't let calling code try to dereference the
		// bean factory if the bean isn't a factory
		if (BeanFactoryUtils.isFactoryDereference(name) && !(bean instanceof FactoryBean)) {
			throw new BeanIsNotAFactoryException(beanName, bean.getClass());
		}

		if (bean instanceof FactoryBean && !BeanFactoryUtils.isFactoryDereference(name)) {
			try {
				return ((FactoryBean) bean).getObject();
			}
			catch (Exception ex) {
				throw new BeanCreationException(beanName, "FactoryBean threw exception on object creation", ex);
			}
		}
		else {
			return bean;
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		Object bean = getBean(name);
		if (requiredType != null && !requiredType.isAssignableFrom(bean.getClass())) {
			throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
		}
		return (T) bean;
	}

	@SuppressWarnings("unchecked")
	public Object getBean(String name, Object[] args) throws BeansException {
		if (args != null) {
			throw new UnsupportedOperationException(
					"StaticListableBeanFactory does not support explicit bean creation arguments)");
		}
		return getBean(name);
	}

	public boolean containsBean(String name) {
		return this.beans.containsKey(name);
	}

	public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		Object bean = getBean(name);
		// In case of FactoryBean, return singleton status of created object.
		return (bean instanceof FactoryBean && ((FactoryBean) bean).isSingleton());
	}

	public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
		Object bean = getBean(name);
		// In case of FactoryBean, return prototype status of created object.
		return ((bean instanceof SmartFactoryBean && ((SmartFactoryBean) bean).isPrototype()) ||
				(bean instanceof FactoryBean && !((FactoryBean) bean).isSingleton()));
	}

	public boolean isTypeMatch(String name, Class targetType) throws NoSuchBeanDefinitionException {
		Class type = getType(name);
		return (targetType == null || (type != null && targetType.isAssignableFrom(type)));
	}

	public Class getType(String name) throws NoSuchBeanDefinitionException {
		String beanName = BeanFactoryUtils.transformedBeanName(name);

		Object bean = this.beans.get(beanName);
		if (bean == null) {
			throw new NoSuchBeanDefinitionException(beanName,
					"Defined beans are [" + StringUtils.collectionToCommaDelimitedString(this.beans.keySet()) + "]");
		}

		if (bean instanceof FactoryBean && !BeanFactoryUtils.isFactoryDereference(name)) {
			// If it's a FactoryBean, we want to look at what it creates, not the factory class.
			return ((FactoryBean) bean).getObjectType();
		}
		return bean.getClass();
	}

	public String[] getAliases(String name) {
		return new String[0];
	}


	//---------------------------------------------------------------------
	// Implementation of ListableBeanFactory interface
	//---------------------------------------------------------------------

	public boolean containsBeanDefinition(String name) {
		return this.beans.containsKey(name);
	}

	public int getBeanDefinitionCount() {
		return this.beans.size();
	}

	public String[] getBeanDefinitionNames() {
		return StringUtils.toStringArray(this.beans.keySet());
	}

	public String[] getBeanNamesForType(Class type) {
		return getBeanNamesForType(type, true, true);
	}

	public String[] getBeanNamesForType(Class type, boolean includeNonSingletons, boolean includeFactoryBeans) {
		boolean isFactoryType = (type != null && FactoryBean.class.isAssignableFrom(type));
		List<String> matches = new ArrayList<String>();
		for (String name : this.beans.keySet()) {
			Object beanInstance = this.beans.get(name);
			if (beanInstance instanceof FactoryBean && !isFactoryType) {
				if (includeFactoryBeans) {
					Class objectType = ((FactoryBean) beanInstance).getObjectType();
					if (objectType != null && (type == null || type.isAssignableFrom(objectType))) {
						matches.add(name);
					}
				}
			}
			else {
				if (type == null || type.isInstance(beanInstance)) {
					matches.add(name);
				}
			}
		}
		return StringUtils.toStringArray(matches);
	}

	public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
		return getBeansOfType(type, true, true);
	}

	@SuppressWarnings("unchecked")
	public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean includeFactoryBeans)
			throws BeansException {

		boolean isFactoryType = (type != null && FactoryBean.class.isAssignableFrom(type));
		Map<String, T> matches = new HashMap<String, T>();

		for (Map.Entry<String, Object> entry : beans.entrySet()) {
			String beanName = entry.getKey();
			Object beanInstance = entry.getValue();
			// Is bean a FactoryBean?
			if (beanInstance instanceof FactoryBean && !isFactoryType) {
				if (includeFactoryBeans) {
					// Match object created by FactoryBean.
					FactoryBean factory = (FactoryBean) beanInstance;
					Class objectType = factory.getObjectType();
					if ((includeNonSingletons || factory.isSingleton()) &&
							objectType != null && (type == null || type.isAssignableFrom(objectType))) {
						matches.put(beanName, getBean(beanName, type));
					}
				}
			}
			else {
				if (type == null || type.isInstance(beanInstance)) {
					// If type to match is FactoryBean, return FactoryBean itself.
					// Else, return bean instance.
					if (isFactoryType) {
						beanName = FACTORY_BEAN_PREFIX + beanName;
					}
					matches.put(beanName, (T) beanInstance);
				}
			}
		}
		return matches;
	}

	public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType)
			throws BeansException {

		return getBeansWithAnnotation(annotationType, true, true);
	}

	public Map<String, Object> getBeansWithAnnotation(
			Class<? extends Annotation> annotationType, boolean includeNonSingletons, boolean allowEagerInit) {

		Map<String, Object> results = new LinkedHashMap<String, Object>();
		for (String beanName : this.beans.keySet()) {
			if (findAnnotationOnBean(beanName, annotationType) != null) {
				results.put(beanName, getBean(beanName));
			}
		}
		return results;
	}

	public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) {
		return AnnotationUtils.findAnnotation(getType(beanName), annotationType);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4787.java