error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8414.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8414.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8414.java
text:
```scala
a@@ssertEquals("[java.util.List]", ClassUtils.classNamesToString(List.class));

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

package org.springframework.util;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.DerivedTestBean;
import org.springframework.beans.IOther;
import org.springframework.beans.ITestBean;
import org.springframework.beans.TestBean;

/**
 * @author Colin Sampaleanu
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Rick Evans
 */
public class ClassUtilsTests extends TestCase {

	public void setUp() {
		InnerClass.noArgCalled = false;
		InnerClass.argCalled = false;
		InnerClass.overloadedCalled = false;
	}

	public void testIsPresent() throws Exception {
		assertTrue(ClassUtils.isPresent("java.lang.String"));
		assertFalse(ClassUtils.isPresent("java.lang.MySpecialString"));
	}

	public void testForName() throws ClassNotFoundException {
		assertEquals(String.class, ClassUtils.forName("java.lang.String"));
		assertEquals(String[].class, ClassUtils.forName("java.lang.String[]"));
		assertEquals(String[].class, ClassUtils.forName(String[].class.getName()));
		assertEquals(String[][].class, ClassUtils.forName(String[][].class.getName()));
		assertEquals(String[][][].class, ClassUtils.forName(String[][][].class.getName()));
		assertEquals(TestBean.class, ClassUtils.forName("org.springframework.beans.TestBean"));
		assertEquals(TestBean[].class, ClassUtils.forName("org.springframework.beans.TestBean[]"));
		assertEquals(TestBean[].class, ClassUtils.forName(TestBean[].class.getName()));
		assertEquals(TestBean[][].class, ClassUtils.forName("org.springframework.beans.TestBean[][]"));
		assertEquals(TestBean[][].class, ClassUtils.forName(TestBean[][].class.getName()));
	}

	public void testForNameWithPrimitiveClasses() throws ClassNotFoundException {
		assertEquals(boolean.class, ClassUtils.forName("boolean"));
		assertEquals(byte.class, ClassUtils.forName("byte"));
		assertEquals(char.class, ClassUtils.forName("char"));
		assertEquals(short.class, ClassUtils.forName("short"));
		assertEquals(int.class, ClassUtils.forName("int"));
		assertEquals(long.class, ClassUtils.forName("long"));
		assertEquals(float.class, ClassUtils.forName("float"));
		assertEquals(double.class, ClassUtils.forName("double"));
	}

	public void testForNameWithPrimitiveArrays() throws ClassNotFoundException {
		assertEquals(boolean[].class, ClassUtils.forName("boolean[]"));
		assertEquals(byte[].class, ClassUtils.forName("byte[]"));
		assertEquals(char[].class, ClassUtils.forName("char[]"));
		assertEquals(short[].class, ClassUtils.forName("short[]"));
		assertEquals(int[].class, ClassUtils.forName("int[]"));
		assertEquals(long[].class, ClassUtils.forName("long[]"));
		assertEquals(float[].class, ClassUtils.forName("float[]"));
		assertEquals(double[].class, ClassUtils.forName("double[]"));
	}

	public void testForNameWithPrimitiveArraysInternalName() throws ClassNotFoundException {
		assertEquals(boolean[].class, ClassUtils.forName(boolean[].class.getName()));
		assertEquals(byte[].class, ClassUtils.forName(byte[].class.getName()));
		assertEquals(char[].class, ClassUtils.forName(char[].class.getName()));
		assertEquals(short[].class, ClassUtils.forName(short[].class.getName()));
		assertEquals(int[].class, ClassUtils.forName(int[].class.getName()));
		assertEquals(long[].class, ClassUtils.forName(long[].class.getName()));
		assertEquals(float[].class, ClassUtils.forName(float[].class.getName()));
		assertEquals(double[].class, ClassUtils.forName(double[].class.getName()));
	}

	public void testGetShortName() {
		String className = ClassUtils.getShortName(getClass());
		assertEquals("Class name did not match", "ClassUtilsTests", className);
	}

	public void testGetShortNameForObjectArrayClass() {
		String className = ClassUtils.getShortName(Object[].class);
		assertEquals("Class name did not match", "Object[]", className);
	}

	public void testGetShortNameForMultiDimensionalObjectArrayClass() {
		String className = ClassUtils.getShortName(Object[][].class);
		assertEquals("Class name did not match", "Object[][]", className);
	}

	public void testGetShortNameForPrimitiveArrayClass() {
		String className = ClassUtils.getShortName(byte[].class);
		assertEquals("Class name did not match", "byte[]", className);
	}

	public void testGetShortNameForMultiDimensionalPrimitiveArrayClass() {
		String className = ClassUtils.getShortName(byte[][][].class);
		assertEquals("Class name did not match", "byte[][][]", className);
	}

	public void testGetShortNameForInnerClass() {
		String className = ClassUtils.getShortName(InnerClass.class);
		assertEquals("Class name did not match", "ClassUtilsTests.InnerClass", className);
	}

	public void testGetShortNameForCglibClass() {
		TestBean tb = new TestBean();
		ProxyFactory pf = new ProxyFactory();
		pf.setTarget(tb);
		pf.setProxyTargetClass(true);
		TestBean proxy = (TestBean) pf.getProxy();
		String className = ClassUtils.getShortName(proxy.getClass());
		assertEquals("Class name did not match", "TestBean", className);
	}

	public void testGetShortNameAsProperty() {
		String shortName = ClassUtils.getShortNameAsProperty(this.getClass());
		assertEquals("Class name did not match", "classUtilsTests", shortName);
	}

	public void testGetClassFileName() {
		assertEquals("String.class", ClassUtils.getClassFileName(String.class));
		assertEquals("ClassUtilsTests.class", ClassUtils.getClassFileName(getClass()));
	}

	public void testGetPackageName() {
		assertEquals("java.lang", ClassUtils.getPackageName(String.class));
		assertEquals(getClass().getPackage().getName(), ClassUtils.getPackageName(getClass()));
	}

	public void testGetQualifiedName() {
		String className = ClassUtils.getQualifiedName(getClass());
		assertEquals("Class name did not match", "org.springframework.util.ClassUtilsTests", className);
	}

	public void testGetQualifiedNameForObjectArrayClass() {
		String className = ClassUtils.getQualifiedName(Object[].class);
		assertEquals("Class name did not match", "java.lang.Object[]", className);
	}

	public void testGetQualifiedNameForMultiDimensionalObjectArrayClass() {
		String className = ClassUtils.getQualifiedName(Object[][].class);
		assertEquals("Class name did not match", "java.lang.Object[][]", className);
	}

	public void testGetQualifiedNameForPrimitiveArrayClass() {
		String className = ClassUtils.getQualifiedName(byte[].class);
		assertEquals("Class name did not match", "byte[]", className);
	}

	public void testGetQualifiedNameForMultiDimensionalPrimitiveArrayClass() {
		String className = ClassUtils.getQualifiedName(byte[][].class);
		assertEquals("Class name did not match", "byte[][]", className);
	}

	public void testHasMethod() throws Exception {
		assertTrue(ClassUtils.hasMethod(Collection.class, "size", null));
		assertTrue(ClassUtils.hasMethod(Collection.class, "remove", new Class[] {Object.class}));
		assertFalse(ClassUtils.hasMethod(Collection.class, "remove", null));
		assertFalse(ClassUtils.hasMethod(Collection.class, "someOtherMethod", null));
	}

	public void testGetMethodIfAvailable() throws Exception {
		Method method = ClassUtils.getMethodIfAvailable(Collection.class, "size", null);
		assertNotNull(method);
		assertEquals("size", method.getName());

		method = ClassUtils.getMethodIfAvailable(Collection.class, "remove", new Class[] {Object.class});
		assertNotNull(method);
		assertEquals("remove", method.getName());

		assertNull(ClassUtils.getMethodIfAvailable(Collection.class, "remove", null));
		assertNull(ClassUtils.getMethodIfAvailable(Collection.class, "someOtherMethod", null));
	}

	public void testGetMethodCountForName() {
		assertEquals("Verifying number of overloaded 'print' methods for OverloadedMethodsClass.", 2,
				ClassUtils.getMethodCountForName(OverloadedMethodsClass.class, "print"));
		assertEquals("Verifying number of overloaded 'print' methods for SubOverloadedMethodsClass.", 4,
				ClassUtils.getMethodCountForName(SubOverloadedMethodsClass.class, "print"));
	}

	public void testCountOverloadedMethods() {
		assertFalse(ClassUtils.hasAtLeastOneMethodWithName(TestBean.class, "foobar"));
		// no args
		assertTrue(ClassUtils.hasAtLeastOneMethodWithName(TestBean.class, "hashCode"));
		// matches although it takes an arg
		assertTrue(ClassUtils.hasAtLeastOneMethodWithName(TestBean.class, "setAge"));
	}

	public void testNoArgsStaticMethod() throws IllegalAccessException, InvocationTargetException {
		Method method = ClassUtils.getStaticMethod(InnerClass.class, "staticMethod", (Class[]) null);
		method.invoke(null, (Object[]) null);
		assertTrue("no argument method was not invoked.",
				InnerClass.noArgCalled);
	}

	public void testArgsStaticMethod() throws IllegalAccessException, InvocationTargetException {
		Method method = ClassUtils.getStaticMethod(InnerClass.class, "argStaticMethod",
				new Class[] {String.class});
		method.invoke(null, new Object[] {"test"});
		assertTrue("argument method was not invoked.", InnerClass.argCalled);
	}

	public void testOverloadedStaticMethod() throws IllegalAccessException, InvocationTargetException {
		Method method = ClassUtils.getStaticMethod(InnerClass.class, "staticMethod",
				new Class[] {String.class});
		method.invoke(null, new Object[] {"test"});
		assertTrue("argument method was not invoked.",
				InnerClass.overloadedCalled);
	}

	public void testClassPackageAsResourcePath() {
		String result = ClassUtils.classPackageAsResourcePath(Proxy.class);
		assertTrue(result.equals("java/lang/reflect"));
	}

	public void testAddResourcePathToPackagePath() {
		String result = "java/lang/reflect/xyzabc.xml";
		assertEquals(result, ClassUtils.addResourcePathToPackagePath(Proxy.class, "xyzabc.xml"));
		assertEquals(result, ClassUtils.addResourcePathToPackagePath(Proxy.class, "/xyzabc.xml"));

		assertEquals("java/lang/reflect/a/b/c/d.xml",
				ClassUtils.addResourcePathToPackagePath(Proxy.class, "a/b/c/d.xml"));
	}

	public void testGetAllInterfaces() {
		DerivedTestBean testBean = new DerivedTestBean();
		List ifcs = Arrays.asList(ClassUtils.getAllInterfaces(testBean));
		assertEquals("Correct number of interfaces", 7, ifcs.size());
		assertTrue("Contains Serializable", ifcs.contains(Serializable.class));
		assertTrue("Contains ITestBean", ifcs.contains(ITestBean.class));
		assertTrue("Contains IOther", ifcs.contains(IOther.class));
	}

	public void testClassNamesToString() {
		List ifcs = new LinkedList();
		ifcs.add(Serializable.class);
		ifcs.add(Runnable.class);
		assertEquals("[interface java.io.Serializable, interface java.lang.Runnable]", ifcs.toString());
		assertEquals("[java.io.Serializable, java.lang.Runnable]", ClassUtils.classNamesToString(ifcs));

		List classes = new LinkedList();
		classes.add(LinkedList.class);
		classes.add(Integer.class);
		assertEquals("[class java.util.LinkedList, class java.lang.Integer]", classes.toString());
		assertEquals("[java.util.LinkedList, java.lang.Integer]", ClassUtils.classNamesToString(classes));

		assertEquals("[interface java.util.List]", Collections.singletonList(List.class).toString());
		assertEquals("[java.util.List]", ClassUtils.classNamesToString(Collections.singletonList(List.class)));

		assertEquals("[]", Collections.EMPTY_LIST.toString());
		assertEquals("[]", ClassUtils.classNamesToString(Collections.EMPTY_LIST));
	}


	public static class InnerClass {

		static boolean noArgCalled;
		static boolean argCalled;
		static boolean overloadedCalled;

		public static void staticMethod() {
			noArgCalled = true;
		}

		public static void staticMethod(String anArg) {
			overloadedCalled = true;
		}

		public static void argStaticMethod(String anArg) {
			argCalled = true;
		}
	}

	private static class OverloadedMethodsClass {
		public void print(String messages) {
			/* no-op */
		}
		public void print(String[] messages) {
			/* no-op */
		}
	}

	private static class SubOverloadedMethodsClass extends OverloadedMethodsClass{
		public void print(String header, String[] messages) {
			/* no-op */
		}
		void print(String header, String[] messages, String footer) {
			/* no-op */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8414.java