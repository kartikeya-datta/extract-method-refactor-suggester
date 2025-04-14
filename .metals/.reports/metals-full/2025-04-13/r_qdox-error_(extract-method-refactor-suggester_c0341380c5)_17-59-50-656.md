error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9306.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9306.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9306.java
text:
```scala
public v@@oid setResourceAdapterClass(Class<?> resourceAdapterClass) {

/*
 * Copyright 2002-2012 the original author or authors.
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

package org.springframework.jca.support;

import javax.resource.ResourceException;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.XATerminator;
import javax.resource.spi.work.WorkManager;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * {@link org.springframework.beans.factory.FactoryBean} that bootstraps
 * the specified JCA 1.5 {@link javax.resource.spi.ResourceAdapter},
 * starting it with a local {@link javax.resource.spi.BootstrapContext}
 * and exposing it for bean references. It will also stop the ResourceAdapter
 * on context shutdown. This corresponds to 'non-managed' bootstrap in a
 * local environment, according to the JCA 1.5 specification.
 *
 * <p>This is essentially an adapter for bean-style bootstrapping of a
 * JCA ResourceAdapter, allowing the BootstrapContext or its elements
 * (such as the JCA WorkManager) to be specified through bean properties.
 *
 * @author Juergen Hoeller
 * @since 2.0.3
 * @see #setResourceAdapter
 * @see #setBootstrapContext
 * @see #setWorkManager
 * @see javax.resource.spi.ResourceAdapter#start(javax.resource.spi.BootstrapContext)
 * @see javax.resource.spi.ResourceAdapter#stop()
 */
public class ResourceAdapterFactoryBean implements FactoryBean<ResourceAdapter>, InitializingBean, DisposableBean {

	private ResourceAdapter resourceAdapter;

	private BootstrapContext bootstrapContext;

	private WorkManager workManager;

	private XATerminator xaTerminator;


	/**
	 * Specify the target JCA ResourceAdapter as class, to be instantiated
	 * with its default configuration.
	 * <p>Alternatively, specify a pre-configured ResourceAdapter instance
	 * through the "resourceAdapter" property.
	 * @see #setResourceAdapter
	 */
	public void setResourceAdapterClass(Class resourceAdapterClass) {
		Assert.isAssignable(ResourceAdapter.class, resourceAdapterClass);
		this.resourceAdapter = (ResourceAdapter) BeanUtils.instantiateClass(resourceAdapterClass);
	}

	/**
	 * Specify the target JCA ResourceAdapter, passed in as configured instance
	 * which hasn't been started yet. This will typically happen as an
	 * inner bean definition, configuring the ResourceAdapter instance
	 * through its vendor-specific bean properties.
	 */
	public void setResourceAdapter(ResourceAdapter resourceAdapter) {
		this.resourceAdapter = resourceAdapter;
	}

	/**
	 * Specify the JCA BootstrapContext to use for starting the ResourceAdapter.
	 * <p>Alternatively, you can specify the individual parts (such as the
	 * JCA WorkManager) as individual references.
	 * @see #setWorkManager
	 * @see #setXaTerminator
	 */
	public void setBootstrapContext(BootstrapContext bootstrapContext) {
		this.bootstrapContext = bootstrapContext;
	}

	/**
	 * Specify the JCA WorkManager to use for bootstrapping the ResourceAdapter.
	 * @see #setBootstrapContext
	 */
	public void setWorkManager(WorkManager workManager) {
		this.workManager = workManager;
	}

	/**
	 * Specify the JCA XATerminator to use for bootstrapping the ResourceAdapter.
	 * @see #setBootstrapContext
	 */
	public void setXaTerminator(XATerminator xaTerminator) {
		this.xaTerminator = xaTerminator;
	}


	/**
	 * Builds the BootstrapContext and starts the ResourceAdapter with it.
	 * @see javax.resource.spi.ResourceAdapter#start(javax.resource.spi.BootstrapContext)
	 */
	@Override
	public void afterPropertiesSet() throws ResourceException {
		if (this.resourceAdapter == null) {
			throw new IllegalArgumentException("'resourceAdapter' or 'resourceAdapterClass' is required");
		}
		if (this.bootstrapContext == null) {
			this.bootstrapContext = new SimpleBootstrapContext(this.workManager, this.xaTerminator);
		}
		this.resourceAdapter.start(this.bootstrapContext);
	}


	@Override
	public ResourceAdapter getObject() {
		return this.resourceAdapter;
	}

	@Override
	public Class<? extends ResourceAdapter> getObjectType() {
		return (this.resourceAdapter != null ? this.resourceAdapter.getClass() : ResourceAdapter.class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}


	/**
	 * Stops the ResourceAdapter.
	 * @see javax.resource.spi.ResourceAdapter#stop()
	 */
	@Override
	public void destroy() {
		this.resourceAdapter.stop();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9306.java