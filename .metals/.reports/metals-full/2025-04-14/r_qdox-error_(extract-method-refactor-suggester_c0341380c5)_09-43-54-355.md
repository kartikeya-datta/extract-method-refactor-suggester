error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6154.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6154.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6154.java
text:
```scala
S@@tringBuilder sb = new StringBuilder();

/*
 * Copyright 2002-2007 the original author or authors.
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

package org.springframework.aop.target;

import java.io.NotSerializableException;
import java.io.ObjectStreamException;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.aop.TargetSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

/**
 * Base class for {@link org.springframework.aop.TargetSource} implementations
 * that are based on a Spring {@link org.springframework.beans.factory.BeanFactory},
 * delegating to Spring-managed bean instances.
 *
 * <p>Subclasses can create prototype instances or lazily access a
 * singleton target, for example. See {@link LazyInitTargetSource} and
 * {@link AbstractPrototypeBasedTargetSource}'s subclasses for concrete strategies.
 *
 * <p>BeanFactory-based TargetSources are serializable. This involves
 * disconnecting the current target and turning into a {@link SingletonTargetSource}.
 *
 * @author Juergen Hoeller
 * @author Rod Johnson
 * @since 1.1.4
 * @see org.springframework.beans.factory.BeanFactory#getBean
 * @see LazyInitTargetSource
 * @see PrototypeTargetSource
 * @see ThreadLocalTargetSource
 * @see CommonsPoolTargetSource
 */
public abstract class AbstractBeanFactoryBasedTargetSource
		implements TargetSource, BeanFactoryAware, Serializable {

	/** use serialVersionUID from Spring 1.2.7 for interoperability */
	private static final long serialVersionUID = -4721607536018568393L;


	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	/** Name of the target bean we will create on each invocation */
	private String targetBeanName;

	/** Class of the target */
	private Class targetClass;

	/**
	 * BeanFactory that owns this TargetSource. We need to hold onto this
	 * reference so that we can create new prototype instances as necessary.
	 */
	private BeanFactory beanFactory;


	/**
	 * Set the name of the target bean in the factory.
	 * <p>The target bean should not be a singleton, else the same instance will
	 * always be obtained from the factory, resulting in the same behavior as
	 * provided by {@link SingletonTargetSource}.
	 * @param targetBeanName name of the target bean in the BeanFactory
	 * that owns this interceptor
	 * @see SingletonTargetSource
	 */
	public void setTargetBeanName(String targetBeanName) {
		this.targetBeanName = targetBeanName;
	}

	/**
	 * Return the name of the target bean in the factory.
	 */
	public String getTargetBeanName() {
		return this.targetBeanName;
	}

	/**
	 * Specify the target class explicitly, to avoid any kind of access to the
	 * target bean (for example, to avoid initialization of a FactoryBean instance).
	 * <p>Default is to detect the type automatically, through a <code>getType</code>
	 * call on the BeanFactory (or even a full <code>getBean</code> call as fallback).
	 */
	public void setTargetClass(Class targetClass) {
		this.targetClass = targetClass;
	}

	/**
	 * Set the owning BeanFactory. We need to save a reference so that we can
	 * use the <code>getBean</code> method on every invocation.
	 */
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		if (this.targetBeanName == null) {
			throw new IllegalStateException("Property'targetBeanName' is required");
		}
		this.beanFactory = beanFactory;
	}

	/**
	 * Return the owning BeanFactory.
	 */
	public BeanFactory getBeanFactory() {
		return this.beanFactory;
	}


	public synchronized Class getTargetClass() {
		if (this.targetClass == null && this.beanFactory != null) {
			// Determine type of the target bean.
			this.targetClass = this.beanFactory.getType(this.targetBeanName);
			if (this.targetClass == null) {
				if (logger.isTraceEnabled()) {
					logger.trace("Getting bean with name '" + this.targetBeanName + "' in order to determine type");
				}
				this.targetClass = this.beanFactory.getBean(this.targetBeanName).getClass();
			}
		}
		return this.targetClass;
	}

	public boolean isStatic() {
		return false;
	}

	public void releaseTarget(Object target) throws Exception {
		// Nothing to do here.
	}


	/**
	 * Copy configuration from the other AbstractBeanFactoryBasedTargetSource object.
	 * Subclasses should override this if they wish to expose it.
	 * @param other object to copy configuration from
	 */
	protected void copyFrom(AbstractBeanFactoryBasedTargetSource other) {
		this.targetBeanName = other.targetBeanName;
		this.targetClass = other.targetClass;
		this.beanFactory = other.beanFactory;
	}

	/**
	 * Replaces this object with a SingletonTargetSource on serialization.
	 * Protected as otherwise it won't be invoked for subclasses.
	 * (The <code>writeReplace()</code> method must be visible to the class
	 * being serialized.)
	 * <p>With this implementation of this method, there is no need to mark
	 * non-serializable fields in this class or subclasses as transient.
	 */
	protected Object writeReplace() throws ObjectStreamException {
		if (logger.isDebugEnabled()) {
			logger.debug("Disconnecting TargetSource [" + this + "]");
		}
		try {
			// Create disconnected SingletonTargetSource.
			return new SingletonTargetSource(getTarget());
		}
		catch (Exception ex) {
			logger.error("Cannot get target for disconnecting TargetSource [" + this + "]", ex);
			throw new NotSerializableException(
					"Cannot get target for disconnecting TargetSource [" + this + "]: " + ex);
		}
	}


	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !getClass().equals(other.getClass())) {
			return false;
		}
		AbstractBeanFactoryBasedTargetSource otherTargetSource = (AbstractBeanFactoryBasedTargetSource) other;
		return (ObjectUtils.nullSafeEquals(this.beanFactory, otherTargetSource.beanFactory) &&
				ObjectUtils.nullSafeEquals(this.targetBeanName, otherTargetSource.targetBeanName));
	}

	@Override
	public int hashCode() {
		int hashCode = getClass().hashCode();
		hashCode = 13 * hashCode + ObjectUtils.nullSafeHashCode(this.beanFactory);
		hashCode = 13 * hashCode + ObjectUtils.nullSafeHashCode(this.targetBeanName);
		return hashCode;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(ClassUtils.getShortName(getClass()));
		sb.append(" for target bean '").append(this.targetBeanName).append("'");
		if (this.targetClass != null) {
			sb.append(" of type [").append(this.targetClass.getName()).append("]");
		}
		return sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6154.java