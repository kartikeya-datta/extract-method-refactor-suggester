error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14297.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14297.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 675
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14297.java
text:
```scala
public final class BeanNamePointcutMatchingTests {

/*
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

p@@ackage org.springframework.aop.aspectj;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.TestBean;

/**
 * Tests for matching of bean() pointcut designator.
 *
 * @author Ramnivas Laddad
 * @author Chris Beams
 */
public class BeanNamePointcutMatchingTests {

	@Test
	public void testMatchingPointcuts() {
		assertMatch("someName", "bean(someName)");

		// Spring bean names are less restrictive compared to AspectJ names (methods, types etc.) 
		// MVC Controller-kind 
		assertMatch("someName/someOtherName", "bean(someName/someOtherName)");
		assertMatch("someName/foo/someOtherName", "bean(someName/*/someOtherName)");
		assertMatch("someName/foo/bar/someOtherName", "bean(someName/*/someOtherName)");
		assertMatch("someName/*/**", "bean(someName/*)");
		// JMX-kind
		assertMatch("service:name=traceService", "bean(service:name=traceService)");
		assertMatch("service:name=traceService", "bean(service:name=*)");
		assertMatch("service:name=traceService", "bean(*:name=traceService)");

		// Wildcards
		assertMatch("someName", "bean(*someName)");
		assertMatch("someName", "bean(*Name)");
		assertMatch("someName", "bean(*)");
		assertMatch("someName", "bean(someName*)");
		assertMatch("someName", "bean(some*)");
		assertMatch("someName", "bean(some*Name)");
		assertMatch("someName", "bean(*some*Name*)");
		assertMatch("someName", "bean(*s*N*)");

		// Or, and, not expressions
		assertMatch("someName", "bean(someName) || bean(someOtherName)");
		assertMatch("someOtherName", "bean(someName) || bean(someOtherName)");
		
		assertMatch("someName", "!bean(someOtherName)");
		
		assertMatch("someName", "bean(someName) || !bean(someOtherName)");
		assertMatch("someName", "bean(someName) && !bean(someOtherName)");
	}

	@Test
	public void testNonMatchingPointcuts() {
		assertMisMatch("someName", "bean(someNamex)");
		assertMisMatch("someName", "bean(someX*Name)");

		// And, not expressions
		assertMisMatch("someName", "bean(someName) && bean(someOtherName)");
		assertMisMatch("someName", "!bean(someName)");
		assertMisMatch("someName", "!bean(someName) && bean(someOtherName)");
		assertMisMatch("someName", "!bean(someName) || bean(someOtherName)");
	}

	private void assertMatch(String beanName, String pcExpression) {
		assertTrue("Unexpected mismatch for bean \"" + beanName + "\" for pcExpression \"" + pcExpression + "\"",
				matches(beanName, pcExpression));
	}

	private void assertMisMatch(String beanName, String pcExpression) {
		assertFalse("Unexpected match for bean \"" + beanName + "\" for pcExpression \"" + pcExpression + "\"",
				matches(beanName, pcExpression));
	}

	private static boolean matches(final String beanName, String pcExpression) {
		@SuppressWarnings("serial")
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut() {
			protected String getCurrentProxiedBeanName() {
				return beanName;
			}
		};
		pointcut.setExpression(pcExpression);
		return pointcut.matches(TestBean.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14297.java