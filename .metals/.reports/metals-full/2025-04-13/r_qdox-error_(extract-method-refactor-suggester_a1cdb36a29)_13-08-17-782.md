error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/50.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/50.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/50.java
text:
```scala
s@@tart(exporter);

/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.springframework.jmx.export.assembler;

import java.util.HashMap;
import java.util.Map;

import javax.management.Descriptor;
import javax.management.MBeanInfo;
import javax.management.MBeanParameterInfo;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;

import org.junit.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.jmx.IJmxTestBean;
import org.springframework.jmx.JmxTestBean;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.metadata.JmxAttributeSource;
import org.springframework.jmx.support.ObjectNameManager;
import org.springframework.tests.aop.interceptor.NopInterceptor;

import static org.junit.Assert.*;

/**
 * @author Rob Harrop
 * @author Chris Beams
 */
public abstract class AbstractMetadataAssemblerTests extends AbstractJmxAssemblerTests {

	protected static final String QUEUE_SIZE_METRIC = "QueueSize";

	protected static final String CACHE_ENTRIES_METRIC = "CacheEntries";

	@Test
	public void testDescription() throws Exception {
		ModelMBeanInfo info = getMBeanInfoFromAssembler();
		assertEquals("The descriptions are not the same", "My Managed Bean",
				info.getDescription());
	}

	@Test
	public void testAttributeDescriptionOnSetter() throws Exception {
		ModelMBeanInfo inf = getMBeanInfoFromAssembler();
		ModelMBeanAttributeInfo attr = inf.getAttribute(AGE_ATTRIBUTE);
		assertEquals("The description for the age attribute is incorrect",
				"The Age Attribute", attr.getDescription());
	}

	@Test
	public void testAttributeDescriptionOnGetter() throws Exception {
		ModelMBeanInfo inf = getMBeanInfoFromAssembler();
		ModelMBeanAttributeInfo attr = inf.getAttribute(NAME_ATTRIBUTE);
		assertEquals("The description for the name attribute is incorrect",
				"The Name Attribute", attr.getDescription());
	}

	/**
	 * Tests the situation where the attribute is only defined on the getter.
	 */
	@Test
	public void testReadOnlyAttribute() throws Exception {
		ModelMBeanInfo inf = getMBeanInfoFromAssembler();
		ModelMBeanAttributeInfo attr = inf.getAttribute(AGE_ATTRIBUTE);
		assertFalse("The age attribute should not be writable", attr.isWritable());
	}

	@Test
	public void testReadWriteAttribute() throws Exception {
		ModelMBeanInfo inf = getMBeanInfoFromAssembler();
		ModelMBeanAttributeInfo attr = inf.getAttribute(NAME_ATTRIBUTE);
		assertTrue("The name attribute should be writable", attr.isWritable());
		assertTrue("The name attribute should be readable", attr.isReadable());
	}

	/**
	 * Tests the situation where the property only has a getter.
	 */
	@Test
	public void testWithOnlySetter() throws Exception {
		ModelMBeanInfo inf = getMBeanInfoFromAssembler();
		ModelMBeanAttributeInfo attr = inf.getAttribute("NickName");
		assertNotNull("Attribute should not be null", attr);
	}

	/**
	 * Tests the situation where the property only has a setter.
	 */
	@Test
	public void testWithOnlyGetter() throws Exception {
		ModelMBeanInfo info = getMBeanInfoFromAssembler();
		ModelMBeanAttributeInfo attr = info.getAttribute("Superman");
		assertNotNull("Attribute should not be null", attr);
	}

	@Test
	public void testManagedResourceDescriptor() throws Exception {
		ModelMBeanInfo info = getMBeanInfoFromAssembler();
		Descriptor desc = info.getMBeanDescriptor();

		assertEquals("Logging should be set to true", "true", desc.getFieldValue("log"));
		assertEquals("Log file should be jmx.log", "jmx.log", desc.getFieldValue("logFile"));
		assertEquals("Currency Time Limit should be 15", "15", desc.getFieldValue("currencyTimeLimit"));
		assertEquals("Persist Policy should be OnUpdate", "OnUpdate", desc.getFieldValue("persistPolicy"));
		assertEquals("Persist Period should be 200", "200", desc.getFieldValue("persistPeriod"));
		assertEquals("Persist Location should be foo", "./foo", desc.getFieldValue("persistLocation"));
		assertEquals("Persist Name should be bar", "bar.jmx", desc.getFieldValue("persistName"));
	}

	@Test
	public void testAttributeDescriptor() throws Exception {
		ModelMBeanInfo info = getMBeanInfoFromAssembler();
		Descriptor desc = info.getAttribute(NAME_ATTRIBUTE).getDescriptor();

		assertEquals("Default value should be foo", "foo", desc.getFieldValue("default"));
		assertEquals("Currency Time Limit should be 20", "20", desc.getFieldValue("currencyTimeLimit"));
		assertEquals("Persist Policy should be OnUpdate", "OnUpdate", desc.getFieldValue("persistPolicy"));
		assertEquals("Persist Period should be 300", "300", desc.getFieldValue("persistPeriod"));
	}

	@Test
	public void testOperationDescriptor() throws Exception {
		ModelMBeanInfo info = getMBeanInfoFromAssembler();
		Descriptor desc = info.getOperation("myOperation").getDescriptor();

		assertEquals("Currency Time Limit should be 30", "30", desc.getFieldValue("currencyTimeLimit"));
		assertEquals("Role should be \"operation\"", "operation", desc.getFieldValue("role"));
	}

	@Test
	public void testOperationParameterMetadata() throws Exception {
		ModelMBeanInfo info = getMBeanInfoFromAssembler();
		ModelMBeanOperationInfo oper = info.getOperation("add");
		MBeanParameterInfo[] params = oper.getSignature();

		assertEquals("Invalid number of params", 2, params.length);
		assertEquals("Incorrect name for x param", "x", params[0].getName());
		assertEquals("Incorrect type for x param", int.class.getName(), params[0].getType());

		assertEquals("Incorrect name for y param", "y", params[1].getName());
		assertEquals("Incorrect type for y param", int.class.getName(), params[1].getType());
	}

	@Test
	public void testWithCglibProxy() throws Exception {
		IJmxTestBean tb = createJmxTestBean();
		ProxyFactory pf = new ProxyFactory();
		pf.setTarget(tb);
		pf.addAdvice(new NopInterceptor());
		Object proxy = pf.getProxy();

		MetadataMBeanInfoAssembler assembler = (MetadataMBeanInfoAssembler) getAssembler();

		MBeanExporter exporter = new MBeanExporter();
		exporter.setBeanFactory(getContext());
		exporter.setAssembler(assembler);

		String objectName = "spring:bean=test,proxy=true";

		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put(objectName, proxy);
		exporter.setBeans(beans);
		exporter.afterPropertiesSet();

		MBeanInfo inf = getServer().getMBeanInfo(ObjectNameManager.getInstance(objectName));
		assertEquals("Incorrect number of operations", getExpectedOperationCount(), inf.getOperations().length);
		assertEquals("Incorrect number of attributes", getExpectedAttributeCount(), inf.getAttributes().length);

		assertTrue("Not included in autodetection", assembler.includeBean(proxy.getClass(), "some bean name"));
	}

	@Test
	public void testMetricDescription() throws Exception {
		ModelMBeanInfo inf = getMBeanInfoFromAssembler();
		ModelMBeanAttributeInfo metric = inf.getAttribute(QUEUE_SIZE_METRIC);
		ModelMBeanOperationInfo operation = inf.getOperation("getQueueSize");
		assertEquals("The description for the queue size metric is incorrect",
				"The QueueSize metric", metric.getDescription());
		assertEquals("The description for the getter operation of the queue size metric is incorrect",
				"The QueueSize metric", operation.getDescription());
	}

	@Test
	public void testMetricDescriptor() throws Exception {
		ModelMBeanInfo info = getMBeanInfoFromAssembler();
		Descriptor desc = info.getAttribute(QUEUE_SIZE_METRIC).getDescriptor();
		assertEquals("Currency Time Limit should be 20", "20", desc.getFieldValue("currencyTimeLimit"));
		assertEquals("Persist Policy should be OnUpdate", "OnUpdate", desc.getFieldValue("persistPolicy"));
		assertEquals("Persist Period should be 300", "300", desc.getFieldValue("persistPeriod"));
		assertEquals("Unit should be messages", "messages",desc.getFieldValue("units"));
		assertEquals("Display Name should be Queue Size", "Queue Size",desc.getFieldValue("displayName"));
		assertEquals("Metric Type should be COUNTER", "COUNTER",desc.getFieldValue("metricType"));
		assertEquals("Metric Category should be utilization", "utilization",desc.getFieldValue("metricCategory"));
	}

	@Test
	public void testMetricDescriptorDefaults() throws Exception {
		ModelMBeanInfo info = getMBeanInfoFromAssembler();
		Descriptor desc = info.getAttribute(CACHE_ENTRIES_METRIC).getDescriptor();
		assertNull("Currency Time Limit should not be populated", desc.getFieldValue("currencyTimeLimit"));
		assertNull("Persist Policy should not be populated", desc.getFieldValue("persistPolicy"));
		assertNull("Persist Period should not be populated", desc.getFieldValue("persistPeriod"));
		assertNull("Unit should not be populated", desc.getFieldValue("units"));
		assertEquals("Display Name should be populated by default via JMX", CACHE_ENTRIES_METRIC,desc.getFieldValue("displayName"));
		assertEquals("Metric Type should be GAUGE", "GAUGE",desc.getFieldValue("metricType"));
		assertNull("Metric Category should not be populated", desc.getFieldValue("metricCategory"));
	}

	@Override
	protected abstract String getObjectName();

	@Override
	protected int getExpectedAttributeCount() {
		return 6;
	}

	@Override
	protected int getExpectedOperationCount() {
		return 9;
	}

	protected IJmxTestBean createJmxTestBean() {
		return new JmxTestBean();
	}

	@Override
	protected MBeanInfoAssembler getAssembler() {
		MetadataMBeanInfoAssembler assembler = new MetadataMBeanInfoAssembler();
		assembler.setAttributeSource(getAttributeSource());
		return assembler;
	}

	protected abstract JmxAttributeSource getAttributeSource();

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/50.java