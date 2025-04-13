error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15133.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15133.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15133.java
text:
```scala
c@@tx = new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());

/*
 * Copyright 2002-2008 the original author or authors.
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

package org.springframework.aop.aspectj;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.ITestBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Test for correct application of the bean() PCD for XML-based AspectJ aspects.
 *
 * @author Ramnivas Laddad
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public final class BeanNamePointcutTests {

	private ITestBean testBean1;
	private ITestBean testBean2;
	private ITestBean testBeanContainingNestedBean;
	private Map<?, ?> testFactoryBean1;
	private Map<?, ?> testFactoryBean2;
	private Counter counterAspect;

	private ITestBean interceptThis;
	private ITestBean dontInterceptThis;
	private TestInterceptor testInterceptor;
	
	private ClassPathXmlApplicationContext ctx;


	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("bean-name-pointcut-tests.xml", getClass());
		testBean1 = (ITestBean) ctx.getBean("testBean1");
		testBean2 = (ITestBean) ctx.getBean("testBean2");
		testBeanContainingNestedBean = (ITestBean) ctx.getBean("testBeanContainingNestedBean");
		testFactoryBean1 = (Map<?, ?>) ctx.getBean("testFactoryBean1");
		testFactoryBean2 = (Map<?, ?>) ctx.getBean("testFactoryBean2");
		counterAspect = (Counter) ctx.getBean("counterAspect");
		interceptThis = (ITestBean) ctx.getBean("interceptThis");
		dontInterceptThis = (ITestBean) ctx.getBean("dontInterceptThis");
		testInterceptor = (TestInterceptor) ctx.getBean("testInterceptor");
		
		counterAspect.reset();
	}

	// We don't need to test all combination of pointcuts due to BeanNamePointcutMatchingTests

	@Test
	public void testMatchingBeanName() {
		assertTrue("Matching bean must be advised (proxied)", this.testBean1 instanceof Advised);
		// Call two methods to test for SPR-3953-like condition
		this.testBean1.setAge(20);
		this.testBean1.setName("");
		assertEquals("Advice not executed: must have been", 2, this.counterAspect.getCount());
	}

	@Test
	public void testNonMatchingBeanName() {
		assertFalse("Non-matching bean must *not* be advised (proxied)", this.testBean2 instanceof Advised);
		this.testBean2.setAge(20);
		assertEquals("Advice must *not* have been executed", 0, this.counterAspect.getCount());
	}

	@Test
	public void testNonMatchingNestedBeanName() {
		assertFalse("Non-matching bean must *not* be advised (proxied)", this.testBeanContainingNestedBean.getDoctor() instanceof Advised);
	}
	
	@Test
	public void testMatchingFactoryBeanObject() {
		assertTrue("Matching bean must be advised (proxied)", this.testFactoryBean1 instanceof Advised);
		assertEquals("myValue", this.testFactoryBean1.get("myKey"));
		assertEquals("myValue", this.testFactoryBean1.get("myKey"));
		assertEquals("Advice not executed: must have been", 2, this.counterAspect.getCount());
		FactoryBean<?> fb = (FactoryBean<?>) ctx.getBean("&testFactoryBean1");
		assertTrue("FactoryBean itself must *not* be advised", !(fb instanceof Advised));
	}

	@Test
	public void testMatchingFactoryBeanItself() {
		assertTrue("Matching bean must *not* be advised (proxied)", !(this.testFactoryBean2 instanceof Advised));
		FactoryBean<?> fb = (FactoryBean<?>) ctx.getBean("&testFactoryBean2");
		assertTrue("FactoryBean itself must be advised", fb instanceof Advised);
		assertTrue(Map.class.isAssignableFrom(fb.getObjectType()));
		assertTrue(Map.class.isAssignableFrom(fb.getObjectType()));
		assertEquals("Advice not executed: must have been", 2, this.counterAspect.getCount());
	}

	@Test
	public void testPointcutAdvisorCombination() {
		assertTrue("Matching bean must be advised (proxied)", this.interceptThis instanceof Advised);
		assertFalse("Non-matching bean must *not* be advised (proxied)", this.dontInterceptThis instanceof Advised);
		interceptThis.setAge(20);
		assertEquals(1, testInterceptor.interceptionCount);
		dontInterceptThis.setAge(20);
		assertEquals(1, testInterceptor.interceptionCount);
	}


	public static class TestInterceptor implements MethodBeforeAdvice {

		private int interceptionCount;
		
		public void before(Method method, Object[] args, Object target) throws Throwable {
			interceptionCount++;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15133.java