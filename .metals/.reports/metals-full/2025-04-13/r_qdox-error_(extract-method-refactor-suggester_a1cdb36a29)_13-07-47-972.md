error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9563.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9563.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 694
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9563.java
text:
```scala
public class BooleanExpressionTests extends AbstractExpressionTests {

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

import org.junit.Test;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.expression.spel.support.StandardTypeConverter;

/**
 * Tests the evaluation of real boolean expressions, these use AND, OR, NOT, TRUE, FALSE
 *
 * @author Andy Clement
 * @author Oliver Becker
 */
public class BooleanExpressionTests extends ExpressionTestCase {

	@Test
	public void testBooleanTrue() {
		evaluate("true", Boolean.TRUE, Boolean.class);
	}

	@Test
	public void testBooleanFalse() {
		evaluate("false", Boolean.FALSE, Boolean.class);
	}

	@Test
	public void testOr() {
		evaluate("false or false", Boolean.FALSE, Boolean.class);
		evaluate("false or true", Boolean.TRUE, Boolean.class);
		evaluate("true or false", Boolean.TRUE, Boolean.class);
		evaluate("true or true", Boolean.TRUE, Boolean.class);
	}

	@Test
	public void testAnd() {
		evaluate("false and false", Boolean.FALSE, Boolean.class);
		evaluate("false and true", Boolean.FALSE, Boolean.class);
		evaluate("true and false", Boolean.FALSE, Boolean.class);
		evaluate("true and true", Boolean.TRUE, Boolean.class);
	}

	@Test
	public void testNot() {
		evaluate("!false", Boolean.TRUE, Boolean.class);
		evaluate("!true", Boolean.FALSE, Boolean.class);

		evaluate("not false", Boolean.TRUE, Boolean.class);
		evaluate("NoT true", Boolean.FALSE, Boolean.class);
	}

	@Test
	public void testCombinations01() {
		evaluate("false and false or true", Boolean.TRUE, Boolean.class);
		evaluate("true and false or true", Boolean.TRUE, Boolean.class);
		evaluate("true and false or false", Boolean.FALSE, Boolean.class);
	}

	@Test
	public void testWritability() {
		evaluate("true and true", Boolean.TRUE, Boolean.class, false);
		evaluate("true or true", Boolean.TRUE, Boolean.class, false);
		evaluate("!false", Boolean.TRUE, Boolean.class, false);
	}

	@Test
	public void testBooleanErrors01() {
		evaluateAndCheckError("1.0 or false", SpelMessage.TYPE_CONVERSION_ERROR, 0);
		evaluateAndCheckError("false or 39.4", SpelMessage.TYPE_CONVERSION_ERROR, 9);
		evaluateAndCheckError("true and 'hello'", SpelMessage.TYPE_CONVERSION_ERROR, 9);
		evaluateAndCheckError(" 'hello' and 'goodbye'", SpelMessage.TYPE_CONVERSION_ERROR, 1);
		evaluateAndCheckError("!35.2", SpelMessage.TYPE_CONVERSION_ERROR, 1);
		evaluateAndCheckError("! 'foob'", SpelMessage.TYPE_CONVERSION_ERROR, 2);
	}

	@Test
	public void testConvertAndHandleNull() { // SPR-9445
		// without null conversion
		evaluateAndCheckError("null or true", SpelMessage.TYPE_CONVERSION_ERROR, 0, "null", "boolean");
		evaluateAndCheckError("null and true", SpelMessage.TYPE_CONVERSION_ERROR, 0, "null", "boolean");
		evaluateAndCheckError("!null", SpelMessage.TYPE_CONVERSION_ERROR, 1, "null", "boolean");
		evaluateAndCheckError("null ? 'foo' : 'bar'", SpelMessage.TYPE_CONVERSION_ERROR, 0, "null", "boolean");

		// with null conversion (null -> false)
		GenericConversionService conversionService = new GenericConversionService() {
			@Override
			protected Object convertNullSource(TypeDescriptor sourceType, TypeDescriptor targetType) {
				return targetType.getType() == Boolean.class ? false : null;
			}
		};
		eContext.setTypeConverter(new StandardTypeConverter(conversionService));

		evaluate("null or true", Boolean.TRUE, Boolean.class, false);
		evaluate("null and true", Boolean.FALSE, Boolean.class, false);
		evaluate("!null", Boolean.TRUE, Boolean.class, false);
		evaluate("null ? 'foo' : 'bar'", "bar", String.class, false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9563.java