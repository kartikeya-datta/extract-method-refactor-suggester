error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6696.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6696.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6696.java
text:
```scala
private static final S@@tring SERVICE_URL = "service:jmx:jmxmp://localhost:9878";

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

package org.springframework.jmx.support;

import java.net.MalformedURLException;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.junit.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.jmx.AbstractMBeanServerTests;
import org.springframework.tests.Assume;
import org.springframework.tests.TestGroup;

import static org.junit.Assert.*;

/**
 * @author Rob Harrop
 * @author Juergen Hoeller
 */
public class MBeanServerConnectionFactoryBeanTests extends AbstractMBeanServerTests {

	private static final String SERVICE_URL = "service:jmx:jmxmp://localhost:9876";

	private JMXServiceURL getServiceUrl() throws MalformedURLException {
		return new JMXServiceURL(SERVICE_URL);
	}

	private JMXConnectorServer getConnectorServer() throws Exception {
		return JMXConnectorServerFactory.newJMXConnectorServer(getServiceUrl(), null, getServer());
	}

	@Test
	public void testTestValidConnection() throws Exception {
		Assume.group(TestGroup.JMXMP);
		JMXConnectorServer connectorServer = getConnectorServer();
		connectorServer.start();

		try {
			MBeanServerConnectionFactoryBean bean = new MBeanServerConnectionFactoryBean();
			bean.setServiceUrl(SERVICE_URL);
			bean.afterPropertiesSet();

			try {
				MBeanServerConnection connection = bean.getObject();
				assertNotNull("Connection should not be null", connection);

				// perform simple MBean count test
				assertEquals("MBean count should be the same", getServer().getMBeanCount(), connection.getMBeanCount());
			} finally {
				bean.destroy();
			}
		} finally {
			connectorServer.stop();
		}
	}

	@Test
	public void testWithNoServiceUrl() throws Exception {
		MBeanServerConnectionFactoryBean bean = new MBeanServerConnectionFactoryBean();
		try {
			bean.afterPropertiesSet();
			fail("IllegalArgumentException should be raised when no service url is provided");
		} catch (IllegalArgumentException ex) {
			// expected
		}
	}

	@Test
	public void testTestWithLazyConnection() throws Exception {
		Assume.group(TestGroup.JMXMP);
		MBeanServerConnectionFactoryBean bean = new MBeanServerConnectionFactoryBean();
		bean.setServiceUrl(SERVICE_URL);
		bean.setConnectOnStartup(false);
		bean.afterPropertiesSet();

		MBeanServerConnection connection = bean.getObject();
		assertTrue(AopUtils.isAopProxy(connection));

		JMXConnectorServer connector = null;
		try {
			connector = getConnectorServer();
			connector.start();
			assertEquals("Incorrect MBean count", getServer().getMBeanCount(), connection.getMBeanCount());
		} finally {
			bean.destroy();
			if (connector != null) {
				connector.stop();
			}
		}
	}

	@Test
	public void testWithLazyConnectionAndNoAccess() throws Exception {
		MBeanServerConnectionFactoryBean bean = new MBeanServerConnectionFactoryBean();
		bean.setServiceUrl(SERVICE_URL);
		bean.setConnectOnStartup(false);
		bean.afterPropertiesSet();

		MBeanServerConnection connection = bean.getObject();
		assertTrue(AopUtils.isAopProxy(connection));
		bean.destroy();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6696.java