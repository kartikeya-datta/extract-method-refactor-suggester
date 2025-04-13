error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9568.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9568.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 687
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9568.java
text:
```scala
public class InProgressTests extends AbstractExpressionTests {

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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.springframework.expression.spel.standard.SpelExpression;

/**
 * These are tests for language features that are not yet considered 'live'. Either missing implementation or
 * documentation.
 *
 * Where implementation is missing the tests are commented out.
 *
 * @author Andy Clement
 */
public class InProgressTests extends ExpressionTestCase {

	@Test
	public void testRelOperatorsBetween01() {
		evaluate("1 between listOneFive", "true", Boolean.class);
		// no inline list building at the moment
		// evaluate("1 between {1, 5}", "true", Boolean.class);
	}

	@Test
	public void testRelOperatorsBetweenErrors01() {
		evaluateAndCheckError("1 between T(String)", SpelMessage.BETWEEN_RIGHT_OPERAND_MUST_BE_TWO_ELEMENT_LIST, 10);
	}

	@Test
	public void testRelOperatorsBetweenErrors03() {
		evaluateAndCheckError("1 between listOfNumbersUpToTen",
				SpelMessage.BETWEEN_RIGHT_OPERAND_MUST_BE_TWO_ELEMENT_LIST, 10);
	}

	// PROJECTION
	@Test
	public void testProjection01() {
		evaluate("listOfNumbersUpToTen.![#this<5?'y':'n']", "[y, y, y, y, n, n, n, n, n, n]", ArrayList.class);
		// inline list creation not supported at the moment
		// evaluate("{1,2,3,4,5,6,7,8,9,10}.!{#isEven(#this)}", "[n, y, n, y, n, y, n, y, n, y]", ArrayList.class);
	}

	@Test
	public void testProjection02() {
		// inline map creation not supported at the moment
		// evaluate("#{'a':'y','b':'n','c':'y'}.![value=='y'?key:null].nonnull().sort()", "[a, c]", ArrayList.class);
		evaluate("mapOfNumbersUpToTen.![key>5?value:null]",
				"[null, null, null, null, null, six, seven, eight, nine, ten]", ArrayList.class);
	}

	@Test
	public void testProjection05() {
		evaluateAndCheckError("'abc'.![true]", SpelMessage.PROJECTION_NOT_SUPPORTED_ON_TYPE);
		evaluateAndCheckError("null.![true]", SpelMessage.PROJECTION_NOT_SUPPORTED_ON_TYPE);
		evaluate("null?.![true]", null, null);
	}

	@Test
	public void testProjection06() throws Exception {
		SpelExpression expr = (SpelExpression) parser.parseExpression("'abc'.![true]");
		assertEquals("'abc'.![true]", expr.toStringAST());
	}

	// SELECTION

	@Test
	public void testSelection02() {
		evaluate("testMap.keySet().?[#this matches '.*o.*']", "[monday]", ArrayList.class);
		evaluate("testMap.keySet().?[#this matches '.*r.*'].contains('saturday')", "true", Boolean.class);
		evaluate("testMap.keySet().?[#this matches '.*r.*'].size()", "3", Integer.class);
	}

	@Test
	public void testSelectionError_NonBooleanSelectionCriteria() {
		evaluateAndCheckError("listOfNumbersUpToTen.?['nonboolean']",
				SpelMessage.RESULT_OF_SELECTION_CRITERIA_IS_NOT_BOOLEAN);
	}

	@Test
	public void testSelection03() {
		evaluate("mapOfNumbersUpToTen.?[key>5].size()", "5", Integer.class);
	}

	@Test
	public void testSelection04() {
		evaluateAndCheckError("mapOfNumbersUpToTen.?['hello'].size()",
				SpelMessage.RESULT_OF_SELECTION_CRITERIA_IS_NOT_BOOLEAN);
	}

	@Test
	public void testSelection05() {
		evaluate("mapOfNumbersUpToTen.?[key>11].size()", "0", Integer.class);
		evaluate("mapOfNumbersUpToTen.^[key>11]", null, null);
		evaluate("mapOfNumbersUpToTen.$[key>11]", null, null);
		evaluate("null?.$[key>11]", null, null);
		evaluateAndCheckError("null.?[key>11]", SpelMessage.INVALID_TYPE_FOR_SELECTION);
		evaluateAndCheckError("'abc'.?[key>11]", SpelMessage.INVALID_TYPE_FOR_SELECTION);
	}

	@Test
	public void testSelectionFirst01() {
		evaluate("listOfNumbersUpToTen.^[#isEven(#this) == 'y']", "2", Integer.class);
	}

	@Test
	public void testSelectionFirst02() {
		evaluate("mapOfNumbersUpToTen.^[key>5].size()", "1", Integer.class);
	}

	@Test
	public void testSelectionLast01() {
		evaluate("listOfNumbersUpToTen.$[#isEven(#this) == 'y']", "10", Integer.class);
	}

	@Test
	public void testSelectionLast02() {
		evaluate("mapOfNumbersUpToTen.$[key>5]", "{10=ten}", HashMap.class);
		evaluate("mapOfNumbersUpToTen.$[key>5].size()", "1", Integer.class);
	}

	@Test
	public void testSelectionAST() throws Exception {
		SpelExpression expr = (SpelExpression) parser.parseExpression("'abc'.^[true]");
		assertEquals("'abc'.^[true]", expr.toStringAST());
		expr = (SpelExpression) parser.parseExpression("'abc'.?[true]");
		assertEquals("'abc'.?[true]", expr.toStringAST());
		expr = (SpelExpression) parser.parseExpression("'abc'.$[true]");
		assertEquals("'abc'.$[true]", expr.toStringAST());
	}

	// Constructor invocation
	@Test
	public void testSetConstruction01() {
		evaluate("new java.util.HashSet().addAll({'a','b','c'})", "true", Boolean.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9568.java