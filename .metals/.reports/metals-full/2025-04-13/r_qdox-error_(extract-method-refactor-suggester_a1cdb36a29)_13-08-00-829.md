error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13766.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13766.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13766.java
text:
```scala
e@@valuateAndCheckError("null.toString()",SpelMessages.METHOD_CALL_ON_NULL_OBJECT_NOT_ALLOWED);

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

/**
 * Tests invocation of methods.
 * 
 * @author Andy Clement
 */
@SuppressWarnings("unused")
public class MethodInvocationTests extends ExpressionTestCase {

	public void testSimpleAccess01() {
		evaluate("getPlaceOfBirth().getCity()", "SmilJan", String.class);
	}

	// public void testBuiltInProcessors() {
	// evaluate("new int[]{1,2,3,4}.count()", 4, Integer.class);
	// evaluate("new int[]{4,3,2,1}.sort()[3]", 4, Integer.class);
	// evaluate("new int[]{4,3,2,1}.average()", 2, Integer.class);
	// evaluate("new int[]{4,3,2,1}.max()", 4, Integer.class);
	// evaluate("new int[]{4,3,2,1}.min()", 1, Integer.class);
	// evaluate("new int[]{4,3,2,1,2,3}.distinct().count()", 4, Integer.class);
	// evaluate("{1,2,3,null}.nonnull().count()", 3, Integer.class);
	// evaluate("new int[]{4,3,2,1,2,3}.distinct().count()", 4, Integer.class);
	// }

	public void testStringClass() {
		evaluate("new java.lang.String('hello').charAt(2)", 'l', Character.class);
		evaluate("new java.lang.String('hello').charAt(2).equals('l'.charAt(0))", true, Boolean.class);
		evaluate("'HELLO'.toLowerCase()", "hello", String.class);
		evaluate("'   abcba '.trim()", "abcba", String.class);
	}

	public void testNonExistentMethods() {
		// name is ok but madeup() does not exist
		evaluateAndCheckError("name.madeup()", SpelMessages.METHOD_NOT_FOUND, 5);
	}

	public void testWidening01() {
		// widening of int 3 to double 3 is OK
		evaluate("new Double(3.0d).compareTo(8)", -1, Integer.class);
		evaluate("new Double(3.0d).compareTo(3)", 0, Integer.class);
		evaluate("new Double(3.0d).compareTo(2)", 1, Integer.class);
	}

	public void testArgumentConversion01() {
		// Rely on Double>String conversion for calling startsWith()
		evaluate("new String('hello 2.0 to you').startsWith(7.0d)", false, Boolean.class);
		evaluate("new String('7.0 foobar').startsWith(7.0d)", true, Boolean.class);
	}

	public void testVarargsInvocation01() {
		// Calling 'public int aVarargsMethod(String... strings)'
		evaluate("aVarargsMethod('a','b','c')", 3, Integer.class);
		evaluate("aVarargsMethod('a')", 1, Integer.class);
		evaluate("aVarargsMethod()", 0, Integer.class);
		evaluate("aVarargsMethod(1,2,3)", 3, Integer.class); // all need converting to strings
		evaluate("aVarargsMethod(1)", 1, Integer.class); // needs string conversion
		evaluate("aVarargsMethod(1,'a',3.0d)", 3, Integer.class); // first and last need conversion
		// evaluate("aVarargsMethod(new String[]{'a','b','c'})", 3, Integer.class);
	}

	public void testVarargsInvocation02() {
		// Calling 'public int aVarargsMethod2(int i, String... strings)' - returns int+length_of_strings
		evaluate("aVarargsMethod2(5,'a','b','c')", 8, Integer.class);
		evaluate("aVarargsMethod2(2,'a')", 3, Integer.class);
		evaluate("aVarargsMethod2(4)", 4, Integer.class);
		evaluate("aVarargsMethod2(8,2,3)", 10, Integer.class);
		evaluate("aVarargsMethod2(9)", 9, Integer.class);
		evaluate("aVarargsMethod2(2,'a',3.0d)", 4, Integer.class);
		// evaluate("aVarargsMethod2(8,new String[]{'a','b','c'})", 11, Integer.class);
	}
	
	public void testInvocationOnNullContextObject() {
		evaluateAndCheckError("null.toString()",SpelMessages.ATTEMPTED_METHOD_CALL_ON_NULL_CONTEXT_OBJECT);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13766.java