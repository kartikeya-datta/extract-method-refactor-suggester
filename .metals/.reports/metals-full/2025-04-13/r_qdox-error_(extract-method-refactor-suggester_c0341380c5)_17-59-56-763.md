error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9252.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9252.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9252.java
text:
```scala
C@@lass<?> adminServiceClass = getClass().getClassLoader().loadClass(ADMIN_SERVICE_FACTORY_CLASS);

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

package org.springframework.jmx.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.management.MBeanServer;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jmx.MBeanServerNotFoundException;

/**
 * {@link FactoryBean} that obtains a WebSphere {@link javax.management.MBeanServer}
 * reference through WebSphere's proprietary {@code AdminServiceFactory} API,
 * available on WebSphere 5.1 and higher.
 *
 * <p>Exposes the {@code MBeanServer} for bean references.
 * This FactoryBean is a direct alternative to {@link MBeanServerFactoryBean},
 * which uses standard JMX 1.2 API to access the platform's MBeanServer.
 *
 * <p>See Javadoc for WebSphere's <a href="http://bit.ly/UzccDt">{@code
 * AdminServiceFactory}</a> and <a href="http://bit.ly/TRlX2r">{@code MBeanFactory}</a>.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 2.0.3
 * @see javax.management.MBeanServer
 * @see MBeanServerFactoryBean
 */
public class WebSphereMBeanServerFactoryBean implements FactoryBean<MBeanServer>, InitializingBean {

	private static final String ADMIN_SERVICE_FACTORY_CLASS = "com.ibm.websphere.management.AdminServiceFactory";

	private static final String GET_MBEAN_FACTORY_METHOD = "getMBeanFactory";

	private static final String GET_MBEAN_SERVER_METHOD = "getMBeanServer";


	private MBeanServer mbeanServer;


	@Override
	public void afterPropertiesSet() throws MBeanServerNotFoundException {
		try {
			/*
			 * this.mbeanServer = AdminServiceFactory.getMBeanFactory().getMBeanServer();
			 */
			Class adminServiceClass = getClass().getClassLoader().loadClass(ADMIN_SERVICE_FACTORY_CLASS);
			Method getMBeanFactoryMethod = adminServiceClass.getMethod(GET_MBEAN_FACTORY_METHOD);
			Object mbeanFactory = getMBeanFactoryMethod.invoke(null);
			Method getMBeanServerMethod = mbeanFactory.getClass().getMethod(GET_MBEAN_SERVER_METHOD);
			this.mbeanServer = (MBeanServer) getMBeanServerMethod.invoke(mbeanFactory);
		}
		catch (ClassNotFoundException ex) {
			throw new MBeanServerNotFoundException("Could not find WebSphere's AdminServiceFactory class", ex);
		}
		catch (InvocationTargetException ex) {
			throw new MBeanServerNotFoundException(
					"WebSphere's AdminServiceFactory.getMBeanFactory/getMBeanServer method failed", ex.getTargetException());
		}
		catch (Exception ex) {
			throw new MBeanServerNotFoundException(
					"Could not access WebSphere's AdminServiceFactory.getMBeanFactory/getMBeanServer method", ex);
		}
	}


	@Override
	public MBeanServer getObject() {
		return this.mbeanServer;
	}

	@Override
	public Class<? extends MBeanServer> getObjectType() {
		return (this.mbeanServer != null ? this.mbeanServer.getClass() : MBeanServer.class);
	}

	@Override
	public boolean isSingleton() {
		return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9252.java