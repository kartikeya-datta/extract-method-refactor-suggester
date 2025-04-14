error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9301.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9301.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9301.java
text:
```scala
n@@ew Class<?>[] {getPersistenceManagerInterface()}, new PersistenceManagerInvocationHandler());

/*
 * Copyright 2002-2013 the original author or authors.
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

package org.springframework.orm.jdo.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.jdo.DefaultJdoDialect;
import org.springframework.orm.jdo.JdoDialect;
import org.springframework.orm.jdo.PersistenceManagerFactoryUtils;
import org.springframework.util.Assert;

/**
 * Proxy that implements the {@link javax.jdo.PersistenceManager} interface,
 * delegating to the current thread-bound PersistenceManager (the Spring-managed
 * transactional PersistenceManager or the single OpenPersistenceManagerInView
 * PersistenceManager, if any) on each invocation. This class makes such a
 * Spring-style PersistenceManager proxy available for bean references.
 *
 * <p>The main advantage of this proxy is that it allows DAOs to work with a
 * plain JDO PersistenceManager reference in JDO 3.0 style
 * (see {@link javax.jdo.PersistenceManagerFactory#getPersistenceManagerProxy()}),
 * while still participating in Spring's resource and transaction management.
 *
 * <p>The behavior of this proxy matches the behavior that the JDO 3.0 spec
 * defines for a PersistenceManager proxy. Hence, DAOs could seamlessly switch
 * between {@link StandardPersistenceManagerProxyBean} and this Spring-style proxy,
 * receiving the reference through Dependency Injection. This will work without
 * any Spring API dependencies in the DAO code!
 *
 * @author Juergen Hoeller
 * @since 3.0
 * @see StandardPersistenceManagerProxyBean
 * @see javax.jdo.PersistenceManagerFactory#getPersistenceManagerProxy()
 * @see org.springframework.orm.jdo.PersistenceManagerFactoryUtils#getPersistenceManager
 * @see org.springframework.orm.jdo.PersistenceManagerFactoryUtils#releasePersistenceManager
 */
public class SpringPersistenceManagerProxyBean implements FactoryBean<PersistenceManager>, InitializingBean {

	private PersistenceManagerFactory persistenceManagerFactory;

	private JdoDialect jdoDialect;

	private Class<? extends PersistenceManager> persistenceManagerInterface = PersistenceManager.class;

	private boolean allowCreate = true;

	private PersistenceManager proxy;


	/**
	 * Set the target PersistenceManagerFactory for this proxy.
	 */
	public void setPersistenceManagerFactory(PersistenceManagerFactory persistenceManagerFactory) {
		this.persistenceManagerFactory = persistenceManagerFactory;
	}

	/**
	 * Return the target PersistenceManagerFactory for this proxy.
	 */
	protected PersistenceManagerFactory getPersistenceManagerFactory() {
		return this.persistenceManagerFactory;
	}

	/**
	 * Set the JDO dialect to use for this proxy.
	 * <p>Default is a DefaultJdoDialect based on the PersistenceManagerFactory's
	 * underlying DataSource, if any.
	 */
	public void setJdoDialect(JdoDialect jdoDialect) {
		this.jdoDialect = jdoDialect;
	}

	/**
	 * Return the JDO dialect to use for this proxy.
	 */
	protected JdoDialect getJdoDialect() {
		return this.jdoDialect;
	}

	/**
	 * Specify the PersistenceManager interface to expose,
	 * possibly including vendor extensions.
	 * <p>Default is the standard {@code javax.jdo.PersistenceManager} interface.
	 */
	public void setPersistenceManagerInterface(Class<? extends PersistenceManager> persistenceManagerInterface) {
		this.persistenceManagerInterface = persistenceManagerInterface;
		Assert.notNull(persistenceManagerInterface, "persistenceManagerInterface must not be null");
		Assert.isAssignable(PersistenceManager.class, persistenceManagerInterface);
	}

	/**
	 * Return the PersistenceManager interface to expose.
	 */
	protected Class<? extends PersistenceManager> getPersistenceManagerInterface() {
		return this.persistenceManagerInterface;
	}

	/**
	 * Set whether the PersistenceManagerFactory proxy is allowed to create
	 * a non-transactional PersistenceManager when no transactional
	 * PersistenceManager can be found for the current thread.
	 * <p>Default is "true". Can be turned off to enforce access to
	 * transactional PersistenceManagers, which safely allows for DAOs
	 * written to get a PersistenceManager without explicit closing
	 * (i.e. a {@code PersistenceManagerFactory.getPersistenceManager()}
	 * call without corresponding {@code PersistenceManager.close()} call).
	 * @see org.springframework.orm.jdo.PersistenceManagerFactoryUtils#getPersistenceManager(javax.jdo.PersistenceManagerFactory, boolean)
	 */
	public void setAllowCreate(boolean allowCreate) {
		this.allowCreate = allowCreate;
	}

	/**
	 * Return whether the PersistenceManagerFactory proxy is allowed to create
	 * a non-transactional PersistenceManager when no transactional
	 * PersistenceManager can be found for the current thread.
	 */
	protected boolean isAllowCreate() {
		return this.allowCreate;
	}

	@Override
	public void afterPropertiesSet() {
		if (getPersistenceManagerFactory() == null) {
			throw new IllegalArgumentException("Property 'persistenceManagerFactory' is required");
		}
		// Build default JdoDialect if none explicitly specified.
		if (this.jdoDialect == null) {
			this.jdoDialect = new DefaultJdoDialect(getPersistenceManagerFactory().getConnectionFactory());
		}
		this.proxy = (PersistenceManager) Proxy.newProxyInstance(
				getPersistenceManagerFactory().getClass().getClassLoader(),
				new Class[] {getPersistenceManagerInterface()}, new PersistenceManagerInvocationHandler());
	}


	@Override
	public PersistenceManager getObject() {
		return this.proxy;
	}

	@Override
	public Class<? extends PersistenceManager> getObjectType() {
		return getPersistenceManagerInterface();
	}

	@Override
	public boolean isSingleton() {
		return true;
	}


	/**
	 * Invocation handler that delegates close calls on PersistenceManagers to
	 * PersistenceManagerFactoryUtils for being aware of thread-bound transactions.
	 */
	private class PersistenceManagerInvocationHandler implements InvocationHandler {

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			// Invocation on PersistenceManager interface coming in...

			if (method.getName().equals("equals")) {
				// Only consider equal when proxies are identical.
				return (proxy == args[0]);
			}
			else if (method.getName().equals("hashCode")) {
				// Use hashCode of PersistenceManager proxy.
				return System.identityHashCode(proxy);
			}
			else if (method.getName().equals("toString")) {
				// Deliver toString without touching a target EntityManager.
				return "Spring PersistenceManager proxy for target factory [" + getPersistenceManagerFactory() + "]";
			}
			else if (method.getName().equals("getPersistenceManagerFactory")) {
				// Return PersistenceManagerFactory without creating a PersistenceManager.
				return getPersistenceManagerFactory();
			}
			else if (method.getName().equals("isClosed")) {
				// Proxy is always usable.
				return false;
			}
			else if (method.getName().equals("close")) {
				// Suppress close method.
				return null;
			}

			// Invoke method on target PersistenceManager.
			PersistenceManager pm = PersistenceManagerFactoryUtils.doGetPersistenceManager(
					getPersistenceManagerFactory(), isAllowCreate());
			try {
				Object retVal = method.invoke(pm, args);
				if (retVal instanceof Query) {
					PersistenceManagerFactoryUtils.applyTransactionTimeout(
							(Query) retVal, getPersistenceManagerFactory());
				}
				return retVal;
			}
			catch (InvocationTargetException ex) {
				throw ex.getTargetException();
			}
			finally {
				PersistenceManagerFactoryUtils.doReleasePersistenceManager(pm, getPersistenceManagerFactory());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9301.java