error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9569.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9569.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 684
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9569.java
text:
```scala
public class LiteralTests extends AbstractExpressionTests {

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

p@@ackage org.springframework.expression.spel;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Tests the evaluation of basic literals: boolean, integer, hex integer, long, real, null, date
 *
 * @author Andy Clement
 */
public class LiteralTests extends ExpressionTestCase {

	@Test
	public void testLiteralBoolean01() {
		evaluate("false", "false", Boolean.class);
	}

	@Test
	public void testLiteralBoolean02() {
		evaluate("true", "true", Boolean.class);
	}

	@Test
	public void testLiteralInteger01() {
		evaluate("1", "1", Integer.class);
	}

	@Test
	public void testLiteralInteger02() {
		evaluate("1415", "1415", Integer.class);
	}

	@Test
	public void testLiteralString01() {
		evaluate("'Hello World'", "Hello World", String.class);
	}

	@Test
	public void testLiteralString02() {
		evaluate("'joe bloggs'", "joe bloggs", String.class);
	}

	@Test
	public void testLiteralString03() {
		evaluate("'hello'", "hello", String.class);
	}

	@Test
	public void testLiteralString04() {
		evaluate("'Tony''s Pizza'", "Tony's Pizza", String.class);
		evaluate("'Tony\\r''s Pizza'", "Tony\\r's Pizza", String.class);
	}

	@Test
	public void testLiteralString05() {
		evaluate("\"Hello World\"", "Hello World", String.class);
	}

	@Test
	public void testLiteralString06() {
		evaluate("\"Hello ' World\"", "Hello ' World", String.class);
	}

	@Test
	public void testHexIntLiteral01() {
		evaluate("0x7FFFF", "524287", Integer.class);
		evaluate("0x7FFFFL", 524287L, Long.class);
		evaluate("0X7FFFF", "524287", Integer.class);
		evaluate("0X7FFFFl", 524287L, Long.class);
	}

	@Test
	public void testLongIntLiteral01() {
		evaluate("0xCAFEBABEL", 3405691582L, Long.class);
	}

	@Test
	public void testLongIntInteractions01() {
		evaluate("0x20 * 2L", 64L, Long.class);
		// ask for the result to be made into an Integer
		evaluateAndAskForReturnType("0x20 * 2L", 64, Integer.class);
		// ask for the result to be made into an Integer knowing that it will not fit
		evaluateAndCheckError("0x1220 * 0xffffffffL", Integer.class, SpelMessage.TYPE_CONVERSION_ERROR, 0);
	}

	@Test
	public void testSignedIntLiterals() {
		evaluate("-1", -1, Integer.class);
		evaluate("-0xa", -10, Integer.class);
		evaluate("-1L", -1L, Long.class);
		evaluate("-0x20l", -32L, Long.class);
	}

	@Test
	public void testLiteralReal01_CreatingDoubles() {
		evaluate("1.25", 1.25d, Double.class);
		evaluate("2.99", 2.99d, Double.class);
		evaluate("-3.141", -3.141d, Double.class);
		evaluate("1.25d", 1.25d, Double.class);
		evaluate("2.99d", 2.99d, Double.class);
		evaluate("-3.141d", -3.141d, Double.class);
		evaluate("1.25D", 1.25d, Double.class);
		evaluate("2.99D", 2.99d, Double.class);
		evaluate("-3.141D", -3.141d, Double.class);
	}

	@Test
	public void testLiteralReal02_CreatingFloats() {
		// For now, everything becomes a double...
		evaluate("1.25f", 1.25f, Float.class);
		evaluate("2.5f", 2.5f, Float.class);
		evaluate("-3.5f", -3.5f, Float.class);
		evaluate("1.25F", 1.25f, Float.class);
		evaluate("2.5F", 2.5f, Float.class);
		evaluate("-3.5F", -3.5f, Float.class);
	}

	@Test
	public void testLiteralReal03_UsingExponents() {
		evaluate("6.0221415E+23", "6.0221415E23", Double.class);
		evaluate("6.0221415e+23", "6.0221415E23", Double.class);
		evaluate("6.0221415E+23d", "6.0221415E23", Double.class);
		evaluate("6.0221415e+23D", "6.0221415E23", Double.class);
		evaluate("6E2f", 6E2f, Float.class);
	}

	@Test
	public void testLiteralReal04_BadExpressions() {
		parseAndCheckError("6.1e23e22", SpelMessage.MORE_INPUT, 6, "e22");
		parseAndCheckError("6.1f23e22", SpelMessage.MORE_INPUT, 4, "23e22");
	}

	@Test
	public void testLiteralNull01() {
		evaluate("null", null, null);
	}

	@Test
	public void testConversions() {
		// getting the expression type to be what we want - either:
		evaluate("new Integer(37).byteValue()", (byte) 37, Byte.class); // calling byteValue() on Integer.class
		evaluateAndAskForReturnType("new Integer(37)", (byte) 37, Byte.class); // relying on registered type converters
	}

	@Test
	public void testNotWritable() throws Exception {
		SpelExpression expr = (SpelExpression)parser.parseExpression("37");
		assertFalse(expr.isWritable(new StandardEvaluationContext()));
		expr = (SpelExpression)parser.parseExpression("37L");
		assertFalse(expr.isWritable(new StandardEvaluationContext()));
		expr = (SpelExpression)parser.parseExpression("true");
		assertFalse(expr.isWritable(new StandardEvaluationContext()));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9569.java