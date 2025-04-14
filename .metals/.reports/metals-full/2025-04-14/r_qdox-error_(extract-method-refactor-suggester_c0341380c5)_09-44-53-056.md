error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15136.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15136.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15136.java
text:
```scala
C@@lassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("proceedTests_.xml", getClass());

/**
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

package org.springframework.aop.aspectj;

import static org.junit.Assert.*;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.Ordered;

/**
 * Test for SPR-3522. Arguments changed on a call to proceed should be
 * visible to advice further down the invocation chain.
 *
 * @author Adrian Colyer
 * @author Chris Beams
 */
public final class ProceedTests {

	private SimpleBean testBean;

	private ProceedTestingAspect firstTestAspect;

	private ProceedTestingAspect secondTestAspect;


	@Before
	public void setUp() {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("proceedTests.xml", getClass());
		testBean = (SimpleBean) ctx.getBean("testBean");
		firstTestAspect = (ProceedTestingAspect) ctx.getBean("firstTestAspect");
		secondTestAspect = (ProceedTestingAspect) ctx.getBean("secondTestAspect");
	}

	@Test
	public void testSimpleProceedWithChangedArgs() {
		this.testBean.setName("abc");
		assertEquals("Name changed in around advice", "ABC", this.testBean.getName());
	}

	@Test
	public void testGetArgsIsDefensive() {
		this.testBean.setAge(5);
		assertEquals("getArgs is defensive", 5, this.testBean.getAge());
	}

	@Test
	public void testProceedWithArgsInSameAspect() {
		this.testBean.setMyFloat(1.0F);
		assertTrue("value changed in around advice", this.testBean.getMyFloat() > 1.9F);
		assertTrue("changed value visible to next advice in chain", this.firstTestAspect.getLastBeforeFloatValue() > 1.9F);
	}

	@Test
	public void testProceedWithArgsAcrossAspects() {
		this.testBean.setSex("male");
		assertEquals("value changed in around advice","MALE", this.testBean.getSex());
		assertEquals("changed value visible to next before advice in chain","MALE", this.secondTestAspect.getLastBeforeStringValue());
		assertEquals("changed value visible to next around advice in chain","MALE", this.secondTestAspect.getLastAroundStringValue());
	}


}


interface SimpleBean {
	
	void setName(String name);
	String getName();
	void setAge(int age);
	int getAge();
	void setMyFloat(float f);
	float getMyFloat();
	void setSex(String sex);
	String getSex();
}


class SimpleBeanImpl implements SimpleBean {

	private int age;
	private float aFloat;
	private String name;
	private String sex;
	
	public int getAge() {
		return age;
	}

	public float getMyFloat() {
		return aFloat;
	}

	public String getName() {
		return name;
	}

	public String getSex() {
		return sex;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setMyFloat(float f) {
		this.aFloat = f;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
}


class ProceedTestingAspect implements Ordered {
	
	private String lastBeforeStringValue;
	private String lastAroundStringValue;
	private float lastBeforeFloatValue;
	private int order;
	
	public void setOrder(int order) { this.order = order; }
	public int getOrder() { return this.order; }
	
	public Object capitalize(ProceedingJoinPoint pjp, String value) throws Throwable {
		return pjp.proceed(new Object[] {value.toUpperCase()});
	}
	
	public Object doubleOrQuits(ProceedingJoinPoint pjp) throws Throwable {
		int value = ((Integer) pjp.getArgs()[0]).intValue();
		pjp.getArgs()[0] = new Integer(value * 2);
		return pjp.proceed();
	}
	
	public Object addOne(ProceedingJoinPoint pjp, Float value) throws Throwable {
		float fv = value.floatValue();
		return pjp.proceed(new Object[] {new Float(fv + 1.0F)});
	}
	
	public void captureStringArgument(JoinPoint tjp, String arg) {
		if (!tjp.getArgs()[0].equals(arg)) {
			throw new IllegalStateException(
					"argument is '" + arg + "', " +
					"but args array has '" + tjp.getArgs()[0] + "'"
					);
		}
		this.lastBeforeStringValue = arg;
	}
	
	public Object captureStringArgumentInAround(ProceedingJoinPoint pjp, String arg) throws Throwable {
		if (!pjp.getArgs()[0].equals(arg)) {
			throw new IllegalStateException(
					"argument is '" + arg + "', " +
					"but args array has '" + pjp.getArgs()[0] + "'");
		}
		this.lastAroundStringValue = arg;
		return pjp.proceed();
	}
	
	public void captureFloatArgument(JoinPoint tjp, float arg) {
		float tjpArg = ((Float) tjp.getArgs()[0]).floatValue();
		if (Math.abs(tjpArg - arg) > 0.000001) {
			throw new IllegalStateException(
					"argument is '" + arg + "', " +
					"but args array has '" + tjpArg + "'"
					);
		}
		this.lastBeforeFloatValue = arg;
	}
	
	public String getLastBeforeStringValue() {
		return this.lastBeforeStringValue;
	}
	
	public String getLastAroundStringValue() {
		return this.lastAroundStringValue;
	}
	
	public float getLastBeforeFloatValue() {
		return this.lastBeforeFloatValue;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15136.java