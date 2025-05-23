error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12342.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12342.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 672
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12342.java
text:
```scala
public class AfterReturningAdviceBindingTests {

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

p@@ackage org.springframework.aop.aspectj;

import org.junit.Before;
import org.junit.Test;

import org.springframework.aop.aspectj.AfterReturningAdviceBindingTestAspect.AfterReturningAdviceBindingCollaborator;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.tests.sample.beans.ITestBean;
import org.springframework.tests.sample.beans.TestBean;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

/**
 * Tests for various parameter binding scenarios with before advice.
 *
 * @author Adrian Colyer
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public final class AfterReturningAdviceBindingTests {

	private AfterReturningAdviceBindingTestAspect afterAdviceAspect;

	private ITestBean testBeanProxy;

	private TestBean testBeanTarget;

	private AfterReturningAdviceBindingCollaborator mockCollaborator;


	public void setAfterReturningAdviceAspect(AfterReturningAdviceBindingTestAspect anAspect) {
		this.afterAdviceAspect = anAspect;
	}

	@Before
	public void setUp() throws Exception {
		ClassPathXmlApplicationContext ctx =
			new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());

		afterAdviceAspect = (AfterReturningAdviceBindingTestAspect) ctx.getBean("testAspect");

		mockCollaborator = mock(AfterReturningAdviceBindingCollaborator.class);
		afterAdviceAspect.setCollaborator(mockCollaborator);

		testBeanProxy = (ITestBean) ctx.getBean("testBean");
		assertTrue(AopUtils.isAopProxy(testBeanProxy));

		// we need the real target too, not just the proxy...
		this.testBeanTarget = (TestBean) ((Advised)testBeanProxy).getTargetSource().getTarget();
	}


	@Test
	public void testOneIntArg() {
		testBeanProxy.setAge(5);
		verify(mockCollaborator).oneIntArg(5);
	}

	@Test
	public void testOneObjectArg() {
		testBeanProxy.getAge();
		verify(mockCollaborator).oneObjectArg(this.testBeanProxy);
	}

	@Test
	public void testOneIntAndOneObjectArgs() {
		testBeanProxy.setAge(5);
		verify(mockCollaborator).oneIntAndOneObject(5,this.testBeanProxy);
	}

	@Test
	public void testNeedsJoinPoint() {
		testBeanProxy.getAge();
		verify(mockCollaborator).needsJoinPoint("getAge");
	}

	@Test
	public void testNeedsJoinPointStaticPart() {
		testBeanProxy.getAge();
		verify(mockCollaborator).needsJoinPointStaticPart("getAge");
	}

	@Test
	public void testReturningString() {
		testBeanProxy.setName("adrian");
		testBeanProxy.getName();
		verify(mockCollaborator).oneString("adrian");
	}

	@Test
	public void testReturningObject() {
		testBeanProxy.returnsThis();
		verify(mockCollaborator).oneObjectArg(this.testBeanTarget);
	}

	@Test
	public void testReturningBean() {
		testBeanProxy.returnsThis();
		verify(mockCollaborator).oneTestBeanArg(this.testBeanTarget);
	}

	@Test
	public void testReturningBeanArray() {
		this.testBeanTarget.setSpouse(new TestBean());
		ITestBean[] spouses = this.testBeanTarget.getSpouses();
		testBeanProxy.getSpouses();
		verify(mockCollaborator).testBeanArrayArg(spouses);
	}

	@Test
	public void testNoInvokeWhenReturningParameterTypeDoesNotMatch() {
		testBeanProxy.setSpouse(this.testBeanProxy);
		testBeanProxy.getSpouse();
		verifyZeroInteractions(mockCollaborator);
	}

	@Test
	public void testReturningByType() {
		testBeanProxy.returnsThis();
		verify(mockCollaborator).objectMatchNoArgs();
	}

	@Test
	public void testReturningPrimitive() {
		testBeanProxy.setAge(20);
		testBeanProxy.haveBirthday();
		verify(mockCollaborator).oneInt(20);
	}

}


final class AfterReturningAdviceBindingTestAspect extends AdviceBindingTestAspect {

	private AfterReturningAdviceBindingCollaborator getCollaborator() {
		return (AfterReturningAdviceBindingCollaborator) this.collaborator;
	}

	public void oneString(String name) {
		getCollaborator().oneString(name);
	}

	public void oneTestBeanArg(TestBean bean) {
		getCollaborator().oneTestBeanArg(bean);
	}

	public void testBeanArrayArg(ITestBean[] beans) {
		getCollaborator().testBeanArrayArg(beans);
	}

	public void objectMatchNoArgs() {
		getCollaborator().objectMatchNoArgs();
	}

	public void stringMatchNoArgs() {
		getCollaborator().stringMatchNoArgs();
	}

	public void oneInt(int result) {
		getCollaborator().oneInt(result);
	}

	interface AfterReturningAdviceBindingCollaborator extends AdviceBindingCollaborator {

		void oneString(String s);
		void oneTestBeanArg(TestBean b);
		void testBeanArrayArg(ITestBean[] b);
		void objectMatchNoArgs();
		void stringMatchNoArgs();
		void oneInt(int result);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12342.java