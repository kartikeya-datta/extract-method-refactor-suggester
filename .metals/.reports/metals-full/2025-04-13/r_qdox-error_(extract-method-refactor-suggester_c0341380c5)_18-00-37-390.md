error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11632.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11632.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11632.java
text:
```scala
e@@valuateAndCheckError("new FooBar()",SpelMessage.CONSTRUCTOR_INVOCATION_PROBLEM);

/*
 * Copyright 2002-2009 the original author or authors.
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

package org.springframework.expression.spel;

import org.junit.Test;

/**
 * Tests invocation of constructors.
 * 
 * @author Andy Clement
 */
public class ConstructorInvocationTests extends ExpressionTestCase {

	@Test
	public void testTypeConstructors() {
		evaluate("new String('hello world')", "hello world", String.class);
	}
	
	@Test
	public void testNonExistentType() {
		evaluateAndCheckError("new FooBar()",SpelMessages.CONSTRUCTOR_INVOCATION_PROBLEM);
	}
	
	@Test
	public void testVarargsInvocation01() {
		// Calling 'Fruit(String... strings)'
		evaluate("new org.springframework.expression.spel.testresources.Fruit('a','b','c').stringscount()", 3, Integer.class);
		evaluate("new org.springframework.expression.spel.testresources.Fruit('a').stringscount()", 1, Integer.class);
		evaluate("new org.springframework.expression.spel.testresources.Fruit().stringscount()", 0, Integer.class);
		evaluate("new org.springframework.expression.spel.testresources.Fruit(1,2,3).stringscount()", 3, Integer.class); // all need converting to strings
		evaluate("new org.springframework.expression.spel.testresources.Fruit(1).stringscount()", 1, Integer.class); // needs string conversion
		evaluate("new org.springframework.expression.spel.testresources.Fruit(1,'a',3.0d).stringscount()", 3, Integer.class); // first and last need conversion
	}

	@Test
	public void testVarargsInvocation02() {
	    // Calling 'Fruit(int i, String... strings)' - returns int+length_of_strings
		evaluate("new org.springframework.expression.spel.testresources.Fruit(5,'a','b','c').stringscount()", 8, Integer.class);
		evaluate("new org.springframework.expression.spel.testresources.Fruit(2,'a').stringscount()", 3, Integer.class);
		evaluate("new org.springframework.expression.spel.testresources.Fruit(4).stringscount()", 4, Integer.class);
		evaluate("new org.springframework.expression.spel.testresources.Fruit(8,2,3).stringscount()", 10, Integer.class);
		evaluate("new org.springframework.expression.spel.testresources.Fruit(9).stringscount()", 9, Integer.class);
		evaluate("new org.springframework.expression.spel.testresources.Fruit(2,'a',3.0d).stringscount()", 4, Integer.class);
		evaluate("new org.springframework.expression.spel.testresources.Fruit(8,stringArrayOfThreeItems).stringscount()", 11, Integer.class);
	}
	
	/*
	 * These tests are attempting to call constructors where we need to widen or convert the argument in order to
	 * satisfy a suitable constructor.
	 */
	@Test
	public void testWidening01() {
		// widening of int 3 to double 3 is OK
		evaluate("new Double(3)", 3.0d, Double.class);
		// widening of int 3 to long 3 is OK
		evaluate("new Long(3)", 3L, Long.class);
	}

	@Test
	public void testArgumentConversion01() {
		// Closest ctor will be new String(String) and converter supports Double>String
		evaluate("new String(3.0d)", "3.0", String.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11632.java