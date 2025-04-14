error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5761.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5761.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5761.java
text:
```scala
r@@eturn (Collection<?>) getParametersMethod().invoke(null);

package org.junit.runners;

import static org.junit.Assert.assertEquals;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.internal.runners.CompositeRunner;
import org.junit.internal.runners.MethodValidator;
import org.junit.internal.runners.TestClassMethodsRunner;
import org.junit.internal.runners.TestClassRunner;

/** <p>The custom runner <code>Parameterized</code> implements parameterized
 * tests. When running a parameterized test class, instances are created for the
 * cross-product of the test methods and the test data elements.</p>
 * 
 * For example, to test a Fibonacci function, write:
 * <pre>
 * &#064;RunWith(Parameterized.class)
 * public class FibonacciTest {
 *    &#064;Parameters
 *    public static Collection<Object[]> data() {
 *          return Arrays.asList(new Object[][] { { 0, 0 }, { 1, 1 }, { 2, 1 },
 *             { 3, 2 }, { 4, 3 }, { 5, 5 }, { 6, 8 } });
 *    }
 *
 *    private int fInput;
 *    private int fExpected;
 *
 *    public FibonacciTest(int input, int expected) {
 *       fInput= input;
 *       fExpected= expected;
 *    }
 *
 *    &#064;Test public void test() {
 *       assertEquals(fExpected, Fibonacci.compute(fInput));
 *    }
 * }
 * </pre>
 * 
 * <p>Each instance of <code>FibonacciTest</code> will be constructed using the two-argument
 * constructor and the data values in the <code>&#064;Parameters</code> method.</p>
 */
public class Parameterized extends TestClassRunner {
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface Parameters {
	}

	public static Collection<Object[]> eachOne(Object... params) {
		List<Object[]> results= new ArrayList<Object[]>();
		for (Object param : params)
			results.add(new Object[] { param });
		return results;
	}

	// TODO: single-class this extension
	
	private static class TestClassRunnerForParameters extends TestClassMethodsRunner {
		private final Object[] fParameters;

		private final int fParameterSetNumber;

		private final Constructor<?> fConstructor;

		private TestClassRunnerForParameters(Class<?> klass, Object[] parameters, int i) {
			super(klass);
			fParameters= parameters;
			fParameterSetNumber= i;
			fConstructor= getOnlyConstructor();
		}

		@Override
		protected Object createTest() throws Exception {
			return fConstructor.newInstance(fParameters);
		}
		
		@Override
		protected String getName() {
			return String.format("[%s]", fParameterSetNumber);
		}
		
		@Override
		protected String testName(final Method method) {
			return String.format("%s[%s]", method.getName(), fParameterSetNumber);
		}

		private Constructor<?> getOnlyConstructor() {
			Constructor<?>[] constructors= getTestClass().getConstructors();
			assertEquals(1, constructors.length);
			return constructors[0];
		}
	}
	
	// TODO: I think this now eagerly reads parameters, which was never the point.
	
	public static class RunAllParameterMethods extends CompositeRunner {
		private final Class<?> fKlass;

		public RunAllParameterMethods(Class<?> klass) throws Exception {
			super(klass.getName());
			fKlass= klass;
			int i= 0;
			for (final Object each : getParametersList()) {
				if (each instanceof Object[])
					super.add(new TestClassRunnerForParameters(klass, (Object[])each, i++));
				else
					throw new Exception(String.format("%s.%s() must return a Collection of arrays.", fKlass.getName(), getParametersMethod().getName()));
			}
		}

		private Collection<?> getParametersList() throws IllegalAccessException, InvocationTargetException, Exception {
			return (Collection) getParametersMethod().invoke(null);
		}
		
		private Method getParametersMethod() throws Exception {
			for (Method each : fKlass.getMethods()) {
				if (Modifier.isStatic(each.getModifiers())) {
					Annotation[] annotations= each.getAnnotations();
					for (Annotation annotation : annotations) {
						if (annotation.annotationType() == Parameters.class)
							return each;
					}
				}
			}
			throw new Exception("No public static parameters method on class "
					+ getName());
		}
	}
	
	public Parameterized(final Class<?> klass) throws Exception {
		super(klass, new RunAllParameterMethods(klass));
	}
	
	@Override
	protected void validate(MethodValidator methodValidator) {
		methodValidator.validateStaticMethods();
		methodValidator.validateInstanceMethods();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5761.java