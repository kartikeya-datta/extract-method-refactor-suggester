error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12361.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12361.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 669
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12361.java
text:
```scala
public class GenericParameterMatchingTests {

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

p@@ackage org.springframework.aop.aspectj.generic;

import java.util.Collection;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

/**
 * Tests that poitncut matching is correct with generic method parameter.
 * See SPR-3904 for more details.
 *
 * @author Ramnivas Laddad
 * @author Chris Beams
 */
public final class GenericParameterMatchingTests {

	private CounterAspect counterAspect;

	private GenericInterface<String> testBean;


	@SuppressWarnings("unchecked")
	@org.junit.Before
	public void setUp() {
		ClassPathXmlApplicationContext ctx =
			new ClassPathXmlApplicationContext(getClass().getSimpleName() + "-context.xml", getClass());

		counterAspect = (CounterAspect) ctx.getBean("counterAspect");
		counterAspect.reset();

		testBean = (GenericInterface<String>) ctx.getBean("testBean");
	}


	@Test
	public void testGenericInterfaceGenericArgExecution() {
		testBean.save("");
		assertEquals(1, counterAspect.genericInterfaceGenericArgExecutionCount);
	}

	@Test
	public void testGenericInterfaceGenericCollectionArgExecution() {
		testBean.saveAll(null);
		assertEquals(1, counterAspect.genericInterfaceGenericCollectionArgExecutionCount);
	}

	@Test
	public void testGenericInterfaceSubtypeGenericCollectionArgExecution() {
		testBean.saveAll(null);
		assertEquals(1, counterAspect.genericInterfaceSubtypeGenericCollectionArgExecutionCount);
	}


	static interface GenericInterface<T> {

		public void save(T bean);

		public void saveAll(Collection<T> beans);
	}


	static class GenericImpl<T> implements GenericInterface<T> {

		@Override
		public void save(T bean) {
		}

		@Override
		public void saveAll(Collection<T> beans) {
		}
	}


	@Aspect
	static class CounterAspect {

		int genericInterfaceGenericArgExecutionCount;
		int genericInterfaceGenericCollectionArgExecutionCount;
		int genericInterfaceSubtypeGenericCollectionArgExecutionCount;

		public void reset() {
			genericInterfaceGenericArgExecutionCount = 0;
			genericInterfaceGenericCollectionArgExecutionCount = 0;
			genericInterfaceSubtypeGenericCollectionArgExecutionCount = 0;
		}

		@Pointcut("execution(* org.springframework.aop.aspectj.generic.GenericParameterMatchingTests.GenericInterface.save(..))")
		public void genericInterfaceGenericArgExecution() {}

		@Pointcut("execution(* org.springframework.aop.aspectj.generic.GenericParameterMatchingTests.GenericInterface.saveAll(..))")
		public void GenericInterfaceGenericCollectionArgExecution() {}

		@Pointcut("execution(* org.springframework.aop.aspectj.generic.GenericParameterMatchingTests.GenericInterface+.saveAll(..))")
		public void genericInterfaceSubtypeGenericCollectionArgExecution() {}

		@Before("genericInterfaceGenericArgExecution()")
		public void incrementGenericInterfaceGenericArgExecution() {
			genericInterfaceGenericArgExecutionCount++;
		}

		@Before("GenericInterfaceGenericCollectionArgExecution()")
		public void incrementGenericInterfaceGenericCollectionArgExecution() {
			genericInterfaceGenericCollectionArgExecutionCount++;
		}

		@Before("genericInterfaceSubtypeGenericCollectionArgExecution()")
		public void incrementGenericInterfaceSubtypeGenericCollectionArgExecution() {
			genericInterfaceSubtypeGenericCollectionArgExecutionCount++;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12361.java