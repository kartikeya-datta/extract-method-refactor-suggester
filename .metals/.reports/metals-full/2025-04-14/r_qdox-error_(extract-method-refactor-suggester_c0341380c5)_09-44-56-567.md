error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9567.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9567.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 701
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9567.java
text:
```scala
public class ExpressionWithConversionTests extends AbstractExpressionTests {

/*
 * Copyright 2002-2013 the original author or authors.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.TypeConverter;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import static org.junit.Assert.*;

/**
 * Expression evaluation where the TypeConverter plugged in is the
 * {@link org.springframework.core.convert.support.GenericConversionService}.
 *
 * @author Andy Clement
 * @author Dave Syer
 */
public class ExpressionWithConversionTests extends ExpressionTestCase {

	private static List<String> listOfString = new ArrayList<String>();
	private static TypeDescriptor typeDescriptorForListOfString = null;
	private static List<Integer> listOfInteger = new ArrayList<Integer>();
	private static TypeDescriptor typeDescriptorForListOfInteger = null;

	static {
		listOfString.add("1");
		listOfString.add("2");
		listOfString.add("3");
		listOfInteger.add(4);
		listOfInteger.add(5);
		listOfInteger.add(6);
	}

	@Before
	public void setUp() throws Exception {
		ExpressionWithConversionTests.typeDescriptorForListOfString = new TypeDescriptor(ExpressionWithConversionTests.class.getDeclaredField("listOfString"));
		ExpressionWithConversionTests.typeDescriptorForListOfInteger = new TypeDescriptor(ExpressionWithConversionTests.class.getDeclaredField("listOfInteger"));
	}


	/**
	 * Test the service can convert what we are about to use in the expression evaluation tests.
	 */
	@Test
	public void testConversionsAvailable() throws Exception {
		TypeConvertorUsingConversionService tcs = new TypeConvertorUsingConversionService();

		// ArrayList containing List<Integer> to List<String>
		Class<?> clazz = typeDescriptorForListOfString.getElementTypeDescriptor().getType();
		assertEquals(String.class,clazz);
		List l = (List) tcs.convertValue(listOfInteger, TypeDescriptor.forObject(listOfInteger), typeDescriptorForListOfString);
		assertNotNull(l);

		// ArrayList containing List<String> to List<Integer>
		clazz = typeDescriptorForListOfInteger.getElementTypeDescriptor().getType();
		assertEquals(Integer.class,clazz);

		l = (List) tcs.convertValue(listOfString, TypeDescriptor.forObject(listOfString), typeDescriptorForListOfString);
		assertNotNull(l);
	}

	@Test
	public void testSetParameterizedList() throws Exception {
		StandardEvaluationContext context = TestScenarioCreator.getTestEvaluationContext();
		Expression e = parser.parseExpression("listOfInteger.size()");
		assertEquals(0,e.getValue(context,Integer.class).intValue());
		context.setTypeConverter(new TypeConvertorUsingConversionService());
		// Assign a List<String> to the List<Integer> field - the component elements should be converted
		parser.parseExpression("listOfInteger").setValue(context,listOfString);
		assertEquals(3,e.getValue(context,Integer.class).intValue()); // size now 3
		Class clazz = parser.parseExpression("listOfInteger[1].getClass()").getValue(context,Class.class); // element type correctly Integer
		assertEquals(Integer.class,clazz);
	}

	@Test
	public void testCoercionToCollectionOfPrimitive() throws Exception {

		class TestTarget {
			@SuppressWarnings("unused")
			public int sum(Collection<Integer> numbers) {
				int total = 0;
				for (int i : numbers) {
					total += i;
				}
				return total;
			}
		}

		StandardEvaluationContext evaluationContext = new StandardEvaluationContext();

		TypeDescriptor collectionType = new TypeDescriptor(new MethodParameter(TestTarget.class.getDeclaredMethod(
				"sum", Collection.class), 0));
		// The type conversion is possible
		assertTrue(evaluationContext.getTypeConverter()
				.canConvert(TypeDescriptor.valueOf(String.class), collectionType));
		// ... and it can be done successfully
		assertEquals("[1, 2, 3, 4]", evaluationContext.getTypeConverter().convertValue("1,2,3,4", TypeDescriptor.valueOf(String.class), collectionType).toString());

		evaluationContext.setVariable("target", new TestTarget());

		// OK up to here, so the evaluation should be fine...
		// ... but this fails
		int result = (Integer) parser.parseExpression("#target.sum(#root)").getValue(evaluationContext, "1,2,3,4");
		assertEquals("Wrong result: " + result, 10, result);

	}

	@Test
	public void testConvert() {
		Foo root = new Foo("bar");
		Collection<String> foos = Collections.singletonList("baz");

		StandardEvaluationContext context = new StandardEvaluationContext(root);

		// property access
		Expression expression = parser.parseExpression("foos");
		expression.setValue(context, foos);
		Foo baz = root.getFoos().iterator().next();
		assertEquals("baz", baz.value);

		// method call
		expression = parser.parseExpression("setFoos(#foos)");
		context.setVariable("foos", foos);
		expression.getValue(context);
		baz = root.getFoos().iterator().next();
		assertEquals("baz", baz.value);

		// method call with result from method call
		expression = parser.parseExpression("setFoos(getFoosAsStrings())");
		expression.getValue(context);
		baz = root.getFoos().iterator().next();
		assertEquals("baz", baz.value);

		// method call with result from method call
		expression = parser.parseExpression("setFoos(getFoosAsObjects())");
		expression.getValue(context);
		baz = root.getFoos().iterator().next();
		assertEquals("baz", baz.value);
	}


	/**
	 * Type converter that uses the core conversion service.
	 */
	private static class TypeConvertorUsingConversionService implements TypeConverter {

		private final ConversionService service = new DefaultConversionService();

		@Override
		public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
			return this.service.canConvert(sourceType, targetType);
		}

		@Override
		public Object convertValue(Object value, TypeDescriptor sourceType, TypeDescriptor targetType) throws EvaluationException {
			return this.service.convert(value, sourceType, targetType);
		}
	}


	public static class Foo {

		public final String value;

		private Collection<Foo> foos;

		public Foo(String value) {
			this.value = value;
		}

		public void setFoos(Collection<Foo> foos) {
			this.foos = foos;
		}

		public Collection<Foo> getFoos() {
			return this.foos;
		}

		public Collection<String> getFoosAsStrings() {
			return Collections.singletonList("baz");
		}

		public Collection<?> getFoosAsObjects() {
			return Collections.singletonList("baz");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9567.java