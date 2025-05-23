error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9566.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9566.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 692
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9566.java
text:
```scala
public class ExpressionStateTests extends AbstractExpressionTests {

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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Operation;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.testresources.Inventor;

/**
 * Tests for the expression state object - some features are not yet exploited in the language (eg nested scopes)
 *
 * @author Andy Clement
 */
public class ExpressionStateTests extends ExpressionTestCase {

	@Test
	public void testConstruction() {
		EvaluationContext context = TestScenarioCreator.getTestEvaluationContext();
		ExpressionState state = new ExpressionState(context);
		assertEquals(context,state.getEvaluationContext());
	}

	// Local variables are in variable scopes which come and go during evaluation.  Normal variables are
	// accessible through the evaluation context

	@Test
	public void testLocalVariables() {
		ExpressionState state = getState();

		Object value = state.lookupLocalVariable("foo");
		assertNull(value);

		state.setLocalVariable("foo",34);
		value = state.lookupLocalVariable("foo");
		assertEquals(34,value);

		state.setLocalVariable("foo",null);
		value = state.lookupLocalVariable("foo");
		assertEquals(null,value);
	}

	@Test
	public void testVariables() {
		ExpressionState state = getState();
		TypedValue typedValue = state.lookupVariable("foo");
		assertEquals(TypedValue.NULL,typedValue);

		state.setVariable("foo",34);
		typedValue = state.lookupVariable("foo");
		assertEquals(34,typedValue.getValue());
		assertEquals(Integer.class,typedValue.getTypeDescriptor().getType());

		state.setVariable("foo","abc");
		typedValue = state.lookupVariable("foo");
		assertEquals("abc",typedValue.getValue());
		assertEquals(String.class,typedValue.getTypeDescriptor().getType());
	}

	@Test
	public void testNoVariableInteference() {
		ExpressionState state = getState();
		TypedValue typedValue = state.lookupVariable("foo");
		assertEquals(TypedValue.NULL,typedValue);

		state.setLocalVariable("foo",34);
		typedValue = state.lookupVariable("foo");
		assertEquals(TypedValue.NULL,typedValue);

		state.setVariable("goo","hello");
		assertNull(state.lookupLocalVariable("goo"));
	}

	@Test
	public void testLocalVariableNestedScopes() {
		ExpressionState state = getState();
		assertEquals(null,state.lookupLocalVariable("foo"));

		state.setLocalVariable("foo",12);
		assertEquals(12,state.lookupLocalVariable("foo"));

		state.enterScope(null);
		assertEquals(12,state.lookupLocalVariable("foo")); // found in upper scope

		state.setLocalVariable("foo","abc");
		assertEquals("abc",state.lookupLocalVariable("foo")); // found in nested scope

		state.exitScope();
		assertEquals(12,state.lookupLocalVariable("foo")); // found in nested scope
	}

	@Test
	public void testRootContextObject() {
		ExpressionState state = getState();
		assertEquals(Inventor.class,state.getRootContextObject().getValue().getClass());

		// although the root object is being set on the evaluation context, the value in the 'state' remains what it was when constructed
		((StandardEvaluationContext) state.getEvaluationContext()).setRootObject(null);
		assertEquals(Inventor.class,state.getRootContextObject().getValue().getClass());
		// assertEquals(null, state.getRootContextObject().getValue());

		state = new ExpressionState(new StandardEvaluationContext());
		assertEquals(TypedValue.NULL,state.getRootContextObject());


		((StandardEvaluationContext)state.getEvaluationContext()).setRootObject(null);
		assertEquals(null,state.getRootContextObject().getValue());
	}

	@Test
	public void testActiveContextObject() {
		ExpressionState state = getState();
		assertEquals(state.getRootContextObject().getValue(),state.getActiveContextObject().getValue());

		try {
			state.popActiveContextObject();
			fail("stack should be empty...");
		} catch (EmptyStackException ese) {
			// success
		}

		state.pushActiveContextObject(new TypedValue(34));
		assertEquals(34,state.getActiveContextObject().getValue());

		state.pushActiveContextObject(new TypedValue("hello"));
		assertEquals("hello",state.getActiveContextObject().getValue());

		state.popActiveContextObject();
		assertEquals(34,state.getActiveContextObject().getValue());

		state.popActiveContextObject();
		assertEquals(state.getRootContextObject().getValue(),state.getActiveContextObject().getValue());

		state = new ExpressionState(new StandardEvaluationContext());
		assertEquals(TypedValue.NULL,state.getActiveContextObject());
	}

	@Test
	public void testPopulatedNestedScopes() {
		ExpressionState state = getState();
		assertNull(state.lookupLocalVariable("foo"));

		state.enterScope("foo",34);
		assertEquals(34,state.lookupLocalVariable("foo"));

		state.enterScope(null);
		state.setLocalVariable("foo",12);
		assertEquals(12,state.lookupLocalVariable("foo"));

		state.exitScope();
		assertEquals(34,state.lookupLocalVariable("foo"));

		state.exitScope();
		assertNull(state.lookupLocalVariable("goo"));
	}

	@Test
	public void testRootObjectConstructor() {
		EvaluationContext ctx = getContext();
		// TypedValue root = ctx.getRootObject();
		// supplied should override root on context
		ExpressionState state = new ExpressionState(ctx,new TypedValue("i am a string"));
		TypedValue stateRoot = state.getRootContextObject();
		assertEquals(String.class,stateRoot.getTypeDescriptor().getType());
		assertEquals("i am a string",stateRoot.getValue());
	}

	@Test
	public void testPopulatedNestedScopesMap() {
		ExpressionState state = getState();
		assertNull(state.lookupLocalVariable("foo"));
		assertNull(state.lookupLocalVariable("goo"));

		Map<String,Object> m = new HashMap<String,Object>();
		m.put("foo",34);
		m.put("goo","abc");

		state.enterScope(m);
		assertEquals(34,state.lookupLocalVariable("foo"));
		assertEquals("abc",state.lookupLocalVariable("goo"));

		state.enterScope(null);
		state.setLocalVariable("foo",12);
		assertEquals(12,state.lookupLocalVariable("foo"));
		assertEquals("abc",state.lookupLocalVariable("goo"));

		state.exitScope();
		state.exitScope();
		assertNull(state.lookupLocalVariable("foo"));
		assertNull(state.lookupLocalVariable("goo"));
	}

	@Test
	public void testOperators() throws Exception {
		ExpressionState state = getState();
		try {
			state.operate(Operation.ADD,1,2);
			fail("should have failed");
		} catch (EvaluationException ee) {
			SpelEvaluationException sEx = (SpelEvaluationException)ee;
			assertEquals(SpelMessage.OPERATOR_NOT_SUPPORTED_BETWEEN_TYPES,sEx.getMessageCode());
		}

		try {
			state.operate(Operation.ADD,null,null);
			fail("should have failed");
		} catch (EvaluationException ee) {
			SpelEvaluationException sEx = (SpelEvaluationException)ee;
			assertEquals(SpelMessage.OPERATOR_NOT_SUPPORTED_BETWEEN_TYPES,sEx.getMessageCode());
		}
	}

	@Test
	public void testComparator() {
		ExpressionState state = getState();
		assertEquals(state.getEvaluationContext().getTypeComparator(),state.getTypeComparator());
	}

	@Test
	public void testTypeLocator() throws EvaluationException {
		ExpressionState state = getState();
		assertNotNull(state.getEvaluationContext().getTypeLocator());
		assertEquals(Integer.class,state.findType("java.lang.Integer"));
		try {
			state.findType("someMadeUpName");
			fail("Should have failed to find it");
		} catch (EvaluationException ee) {
			SpelEvaluationException sEx = (SpelEvaluationException)ee;
			assertEquals(SpelMessage.TYPE_NOT_FOUND,sEx.getMessageCode());
		}
	}

	@Test
	public void testTypeConversion() throws EvaluationException {
		ExpressionState state = getState();
		String s = (String)state.convertValue(34, TypeDescriptor.valueOf(String.class));
		assertEquals("34",s);

		s = (String)state.convertValue(new TypedValue(34), TypeDescriptor.valueOf(String.class));
		assertEquals("34",s);
	}

	@Test
	public void testPropertyAccessors() {
		ExpressionState state = getState();
		assertEquals(state.getEvaluationContext().getPropertyAccessors(),state.getPropertyAccessors());
	}

	/**
	 * @return a new ExpressionState
	 */
	private ExpressionState getState() {
		EvaluationContext context = TestScenarioCreator.getTestEvaluationContext();
		ExpressionState state = new ExpressionState(context);
		return state;
	}

	private EvaluationContext getContext() {
		return TestScenarioCreator.getTestEvaluationContext();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9566.java