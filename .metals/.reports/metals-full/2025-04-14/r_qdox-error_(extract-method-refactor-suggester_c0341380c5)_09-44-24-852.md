error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11635.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11635.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11635.java
text:
```scala
s@@etValueExpectError("new org.springframework.expression.spel.testresources.Inventor().inventions[1]",SpelMessage.CANNOT_INDEX_INTO_NULL_VALUE);

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.testresources.PlaceOfBirth;


/**
 * Tests set value expressions.
 * 
 * @author Keith Donald
 * @author Andy Clement
 */
public class SetValueTests extends ExpressionTestCase {

	private final static boolean DEBUG = false;

	@Test
	public void testSetProperty() {
		setValue("wonNobelPrize", true);
	}
	
	@Test
	public void testSetNestedProperty() {
		setValue("placeOfBirth.city", "Wien");
	}

	@Test
	public void testSetArrayElementValue() {
		setValue("inventions[0]", "Just the telephone");
	}

	@Test
	public void testErrorCase() {
		setValueExpectError("3=4", null);
	}

	@Test
	public void testSetElementOfNull() {
		setValueExpectError("new org.springframework.expression.spel.testresources.Inventor().inventions[1]",SpelMessages.CANNOT_INDEX_INTO_NULL_VALUE);
	}

	@Test
	public void testSetArrayElementValueAllPrimitiveTypes() {
		setValue("arrayContainer.ints[1]", 3);
		setValue("arrayContainer.floats[1]", 3.0f);
		setValue("arrayContainer.booleans[1]", false);
		setValue("arrayContainer.doubles[1]", 3.4d);
		setValue("arrayContainer.shorts[1]", (short)3);
		setValue("arrayContainer.longs[1]", 3L);
		setValue("arrayContainer.bytes[1]", (byte) 3);
		setValue("arrayContainer.chars[1]", (char) 3);
	}

	@Test
	public void testSetArrayElementValueAllPrimitiveTypesErrors() {
		// none of these sets are possible due to (expected) conversion problems
		setValueExpectError("arrayContainer.ints[1]", "wibble");
		setValueExpectError("arrayContainer.floats[1]", "dribble");
		setValueExpectError("arrayContainer.booleans[1]", "nein");
		setValueExpectError("arrayContainer.doubles[1]", new ArrayList<String>());
		setValueExpectError("arrayContainer.shorts[1]", new ArrayList<String>());
		setValueExpectError("arrayContainer.longs[1]", new ArrayList<String>());
		setValueExpectError("arrayContainer.bytes[1]", "NaB");
		setValueExpectError("arrayContainer.chars[1]", "NaC");
	}
	
	@Test
	public void testSetArrayElementNestedValue() {
		setValue("placesLived[0].city", "Wien");
	}
	
	@Test
	public void testSetListElementValue() {
		setValue("placesLivedList[0]", new PlaceOfBirth("Wien"));
	}
	
	@Test
	public void testSetGenericListElementValueTypeCoersionfail() {
		// no type converter registered for String > PlaceOfBirth
		setValueExpectError("placesLivedList[0]", "Wien");
	}

	@Test
	public void testSetGenericListElementValueTypeCoersionOK() {
		setValue("booleanList[0]", "true", Boolean.TRUE);
	}
	
	@Test
	public void testSetListElementNestedValue() {
		setValue("placesLived[0].city", "Wien");
	}

	@Test
	public void testSetArrayElementInvalidIndex() {
		setValueExpectError("placesLived[23]", "Wien");
		setValueExpectError("placesLivedList[23]", "Wien");
	}
	
	@Test
	public void testSetMapElements() {
		setValue("testMap['montag']","lundi");
	}
	
	@Test
	public void testIndexingIntoUnsupportedType() {
		setValueExpectError("'hello'[3]", 'p');
	}
	
	@Test
	public void testSetPropertyTypeCoersion() {
		setValue("publicBoolean", "true", Boolean.TRUE);
	}

	@Test
	public void testSetPropertyTypeCoersionThroughSetter() {
		setValue("SomeProperty", "true", Boolean.TRUE);
	}
	
	@Test
	public void testAssign() throws Exception {	
		StandardEvaluationContext eContext = TestScenarioCreator.getTestEvaluationContext();
		Expression e = parse("publicName='Andy'");
		Assert.assertFalse(e.isWritable(eContext));
		Assert.assertEquals("Andy",e.getValue(eContext));
	}

	/*
	 * Testing the coercion of both the keys and the values to the correct type
	 */
	@Test
	public void testSetGenericMapElementRequiresCoercion() throws Exception {
		StandardEvaluationContext eContext = TestScenarioCreator.getTestEvaluationContext();
		Expression e = parse("mapOfStringToBoolean[42]");
		Assert.assertNull(e.getValue(eContext));
		
		// Key should be coerced to string representation of 42
		e.setValue(eContext, "true");
		
		// All keys should be strings
		Set ks = parse("mapOfStringToBoolean.keySet()").getValue(eContext,Set.class);
		for (Object o: ks) {
			Assert.assertEquals(String.class,o.getClass());
		}
		
		// All values should be booleans
		Collection vs = parse("mapOfStringToBoolean.values()").getValue(eContext,Collection.class);
		for (Object o: vs) {
			Assert.assertEquals(Boolean.class,o.getClass());
		}
		
		// One final test check coercion on the key for a map lookup
		Object o = e.getValue(eContext);
		Assert.assertEquals(Boolean.TRUE,o);
	}
	

	private Expression parse(String expressionString) throws Exception {
		return parser.parseExpression(expressionString);
	}
	
	/**
	 * Call setValue() but expect it to fail.
	 */
	protected void setValueExpectError(String expression, Object value) {
		try {
			Expression e = parser.parseExpression(expression);
			if (e == null) {
				Assert.fail("Parser returned null for expression");
			}
			if (DEBUG) {
				SpelUtilities.printAbstractSyntaxTree(System.out, e);
			}
			StandardEvaluationContext lContext = TestScenarioCreator.getTestEvaluationContext();
			e.setValue(lContext, value);
			Assert.fail("expected an error");
		} catch (ParseException pe) {
			pe.printStackTrace();
			Assert.fail("Unexpected Exception: " + pe.getMessage());
		} catch (EvaluationException ee) {
			// success!
		}
	}

	protected void setValue(String expression, Object value) {
		try {
			Expression e = parser.parseExpression(expression);
			if (e == null) {
				Assert.fail("Parser returned null for expression");
			}
			if (DEBUG) {
				SpelUtilities.printAbstractSyntaxTree(System.out, e);
			}
			StandardEvaluationContext lContext = TestScenarioCreator.getTestEvaluationContext();
			Assert.assertTrue("Expression is not writeable but should be", e.isWritable(lContext));
			e.setValue(lContext, value);
			Assert.assertEquals("Retrieved value was not equal to set value", value, e.getValue(lContext));
		} catch (EvaluationException ee) {
			ee.printStackTrace();
			Assert.fail("Unexpected Exception: " + ee.getMessage());
		} catch (ParseException pe) {
			pe.printStackTrace();
			Assert.fail("Unexpected Exception: " + pe.getMessage());
		}
	}

	/**
	 * For use when coercion is happening during a setValue().  The expectedValue should be
	 * the coerced form of the value.
	 */
	protected void setValue(String expression, Object value, Object expectedValue) {
		try {
			Expression e = parser.parseExpression(expression);
			if (e == null) {
				Assert.fail("Parser returned null for expression");
			}
			if (DEBUG) {
				SpelUtilities.printAbstractSyntaxTree(System.out, e);
			}
			StandardEvaluationContext lContext = TestScenarioCreator.getTestEvaluationContext();
			Assert.assertTrue("Expression is not writeable but should be", e.isWritable(lContext));
			e.setValue(lContext, value);
			Object a = expectedValue;
			Object b = e.getValue(lContext);
			if (!a.equals(b)) {
				Assert.fail("Not the same: ["+a+"] type="+a.getClass()+"  ["+b+"] type="+b.getClass());
//				Assert.assertEquals("Retrieved value was not equal to set value", expectedValue, e.getValue(lContext));
			}
		} catch (EvaluationException ee) {
			ee.printStackTrace();
			Assert.fail("Unexpected Exception: " + ee.getMessage());
		} catch (ParseException pe) {
			pe.printStackTrace();
			Assert.fail("Unexpected Exception: " + pe.getMessage());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11635.java