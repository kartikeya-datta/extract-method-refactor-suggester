error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/890.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/890.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/890.java
text:
```scala
a@@ssertTrue("Prototype creation took too long: " + sw.getTotalTimeMillis(), sw.getTotalTimeMillis() < 4500);

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

package org.springframework.context.annotation;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.tests.sample.beans.ITestBean;
import org.springframework.tests.sample.beans.TestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.tests.Assume;
import org.springframework.tests.TestGroup;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.StopWatch;

/**
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 2.5
 */
public class AnnotationProcessorPerformanceTests {

	private static final Log factoryLog = LogFactory.getLog(DefaultListableBeanFactory.class);

	@Test
	public void testPrototypeCreationWithResourcePropertiesIsFastEnough() {
		Assume.group(TestGroup.PERFORMANCE);
		Assume.notLogging(factoryLog);
		GenericApplicationContext ctx = new GenericApplicationContext();
		AnnotationConfigUtils.registerAnnotationConfigProcessors(ctx);
		ctx.refresh();

		RootBeanDefinition rbd = new RootBeanDefinition(ResourceAnnotatedTestBean.class);
		rbd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		ctx.registerBeanDefinition("test", rbd);
		ctx.registerBeanDefinition("spouse", new RootBeanDefinition(TestBean.class));
		TestBean spouse = (TestBean) ctx.getBean("spouse");
		StopWatch sw = new StopWatch();
		sw.start("prototype");
		for (int i = 0; i < 100000; i++) {
			TestBean tb = (TestBean) ctx.getBean("test");
			assertSame(spouse, tb.getSpouse());
		}
		sw.stop();
		//System.out.println(sw.getTotalTimeMillis());
		assertTrue("Prototype creation took too long: " + sw.getTotalTimeMillis(), sw.getTotalTimeMillis() < 4000);
	}

	@Test
	public void testPrototypeCreationWithOverriddenResourcePropertiesIsFastEnough() {
		Assume.group(TestGroup.PERFORMANCE);
		Assume.notLogging(factoryLog);
		GenericApplicationContext ctx = new GenericApplicationContext();
		AnnotationConfigUtils.registerAnnotationConfigProcessors(ctx);
		ctx.refresh();

		RootBeanDefinition rbd = new RootBeanDefinition(ResourceAnnotatedTestBean.class);
		rbd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		rbd.getPropertyValues().add("spouse", new RuntimeBeanReference("spouse"));
		ctx.registerBeanDefinition("test", rbd);
		ctx.registerBeanDefinition("spouse", new RootBeanDefinition(TestBean.class));
		TestBean spouse = (TestBean) ctx.getBean("spouse");
		StopWatch sw = new StopWatch();
		sw.start("prototype");
		for (int i = 0; i < 100000; i++) {
			TestBean tb = (TestBean) ctx.getBean("test");
			assertSame(spouse, tb.getSpouse());
		}
		sw.stop();
		//System.out.println(sw.getTotalTimeMillis());
		assertTrue("Prototype creation took too long: " + sw.getTotalTimeMillis(), sw.getTotalTimeMillis() < 4000);
	}

	@Test
	public void testPrototypeCreationWithAutowiredPropertiesIsFastEnough() {
		Assume.group(TestGroup.PERFORMANCE);
		Assume.notLogging(factoryLog);
		GenericApplicationContext ctx = new GenericApplicationContext();
		AnnotationConfigUtils.registerAnnotationConfigProcessors(ctx);
		ctx.refresh();

		RootBeanDefinition rbd = new RootBeanDefinition(AutowiredAnnotatedTestBean.class);
		rbd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		ctx.registerBeanDefinition("test", rbd);
		ctx.registerBeanDefinition("spouse", new RootBeanDefinition(TestBean.class));
		TestBean spouse = (TestBean) ctx.getBean("spouse");
		StopWatch sw = new StopWatch();
		sw.start("prototype");
		for (int i = 0; i < 100000; i++) {
			TestBean tb = (TestBean) ctx.getBean("test");
			assertSame(spouse, tb.getSpouse());
		}
		sw.stop();
		//System.out.println(sw.getTotalTimeMillis());
		assertTrue("Prototype creation took too long: " + sw.getTotalTimeMillis(), sw.getTotalTimeMillis() < 4000);
	}

	@Test
	public void testPrototypeCreationWithOverriddenAutowiredPropertiesIsFastEnough() {
		Assume.group(TestGroup.PERFORMANCE);
		Assume.notLogging(factoryLog);
		GenericApplicationContext ctx = new GenericApplicationContext();
		AnnotationConfigUtils.registerAnnotationConfigProcessors(ctx);
		ctx.refresh();

		RootBeanDefinition rbd = new RootBeanDefinition(AutowiredAnnotatedTestBean.class);
		rbd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		rbd.getPropertyValues().add("spouse", new RuntimeBeanReference("spouse"));
		ctx.registerBeanDefinition("test", rbd);
		ctx.registerBeanDefinition("spouse", new RootBeanDefinition(TestBean.class));
		TestBean spouse = (TestBean) ctx.getBean("spouse");
		StopWatch sw = new StopWatch();
		sw.start("prototype");
		for (int i = 0; i < 100000; i++) {
			TestBean tb = (TestBean) ctx.getBean("test");
			assertSame(spouse, tb.getSpouse());
		}
		sw.stop();
		//System.out.println(sw.getTotalTimeMillis());
		assertTrue("Prototype creation took too long: " + sw.getTotalTimeMillis(), sw.getTotalTimeMillis() < 4000);
	}


	private static class ResourceAnnotatedTestBean extends TestBean {

		@Override
		@Resource @Required
		public void setSpouse(ITestBean spouse) {
			super.setSpouse(spouse);
		}
	}


	private static class AutowiredAnnotatedTestBean extends TestBean {

		@Override
		@Autowired @Required
		public void setSpouse(ITestBean spouse) {
			super.setSpouse(spouse);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/890.java