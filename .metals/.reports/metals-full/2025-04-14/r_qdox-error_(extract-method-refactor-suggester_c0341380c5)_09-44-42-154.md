error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7041.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7041.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7041.java
text:
```scala
p@@arser = PointcutParser.getPointcutParserSupportingAllPrimitivesAndUsingSpecifiedClassloaderForResolution(this.getClass().getClassLoader());

/* *******************************************************************
 * Copyright (c) 2005 Contributors.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: 
 *   Adrian Colyer			Initial implementation
 * ******************************************************************/
package org.aspectj.weaver.tools;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

import org.aspectj.lang.annotation.Pointcut;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author colyer
 *
 */
public class Java15PointcutExpressionTest extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite("Java15PointcutExpressionTest");
		suite.addTestSuite(Java15PointcutExpressionTest.class);
		return suite;
	}
	
	private PointcutParser parser;
	private Method a;
	private Method b;
	private Method c;
	
	public void testAtThis() {
		PointcutExpression atThis = parser.parsePointcutExpression("@this(org.aspectj.weaver.tools.Java15PointcutExpressionTest.MyAnnotation)");
		ShadowMatch sMatch1 = atThis.matchesMethodExecution(a);
		ShadowMatch sMatch2 = atThis.matchesMethodExecution(b);
		assertTrue("maybe matches A",sMatch1.maybeMatches());
		assertTrue("maybe matches B",sMatch2.maybeMatches());
		JoinPointMatch jp1 = sMatch1.matchesJoinPoint(new A(), new A(), new Object[0]);
		assertFalse("does not match",jp1.matches());
		JoinPointMatch jp2 = sMatch2.matchesJoinPoint(new B(), new B(), new Object[0]);
		assertTrue("matches",jp2.matches());
	}
	
	public void testAtTarget() {
		PointcutExpression atTarget = parser.parsePointcutExpression("@target(org.aspectj.weaver.tools.Java15PointcutExpressionTest.MyAnnotation)");
		ShadowMatch sMatch1 = atTarget.matchesMethodExecution(a);
		ShadowMatch sMatch2 = atTarget.matchesMethodExecution(b);
		assertTrue("maybe matches A",sMatch1.maybeMatches());
		assertTrue("maybe matches B",sMatch2.maybeMatches());
		JoinPointMatch jp1 = sMatch1.matchesJoinPoint(new A(), new A(), new Object[0]);
		assertFalse("does not match",jp1.matches());
		JoinPointMatch jp2 = sMatch2.matchesJoinPoint(new B(), new B(), new Object[0]);
		assertTrue("matches",jp2.matches());		
	}
	
	public void testAtThisWithBinding() {
		PointcutParameter param = parser.createPointcutParameter("a",MyAnnotation.class);
		B myB = new B();
		MyAnnotation bAnnotation = B.class.getAnnotation(MyAnnotation.class);
		PointcutExpression atThis = parser.parsePointcutExpression("@this(a)",A.class,new PointcutParameter[] {param});
		ShadowMatch sMatch1 = atThis.matchesMethodExecution(a);
		ShadowMatch sMatch2 = atThis.matchesMethodExecution(b);
		assertTrue("maybe matches A",sMatch1.maybeMatches());
		assertTrue("maybe matches B",sMatch2.maybeMatches());
		JoinPointMatch jp1 = sMatch1.matchesJoinPoint(new A(), new A(), new Object[0]);
		assertFalse("does not match",jp1.matches());
		JoinPointMatch jp2 = sMatch2.matchesJoinPoint(myB, myB, new Object[0]);
		assertTrue("matches",jp2.matches());
		assertEquals(1,jp2.getParameterBindings().length);
		assertEquals("should be myB's annotation",bAnnotation,jp2.getParameterBindings()[0].getBinding());
	}
	
	public void testAtTargetWithBinding() {
		PointcutParameter param = parser.createPointcutParameter("a",MyAnnotation.class);
		B myB = new B();
		MyAnnotation bAnnotation = B.class.getAnnotation(MyAnnotation.class);
		PointcutExpression atThis = parser.parsePointcutExpression("@target(a)",A.class,new PointcutParameter[] {param});
		ShadowMatch sMatch1 = atThis.matchesMethodExecution(a);
		ShadowMatch sMatch2 = atThis.matchesMethodExecution(b);
		assertTrue("maybe matches A",sMatch1.maybeMatches());
		assertTrue("maybe matches B",sMatch2.maybeMatches());
		JoinPointMatch jp1 = sMatch1.matchesJoinPoint(new A(), new A(), new Object[0]);
		assertFalse("does not match",jp1.matches());
		JoinPointMatch jp2 = sMatch2.matchesJoinPoint(myB, myB, new Object[0]);
		assertTrue("matches",jp2.matches());
		assertEquals(1,jp2.getParameterBindings().length);
		assertEquals("should be myB's annotation",bAnnotation,jp2.getParameterBindings()[0].getBinding());
	}
	
	public void testAtArgs() {
		PointcutExpression atArgs = parser.parsePointcutExpression("@args(..,org.aspectj.weaver.tools.Java15PointcutExpressionTest.MyAnnotation)");
		ShadowMatch sMatch1 = atArgs.matchesMethodExecution(a);
		ShadowMatch sMatch2 = atArgs.matchesMethodExecution(c);
		assertTrue("never matches A",sMatch1.neverMatches());
		assertTrue("maybe matches C",sMatch2.maybeMatches());
		JoinPointMatch jp2 = sMatch2.matchesJoinPoint(new B(), new B(), new Object[]{new A(),new B()});
		assertTrue("matches",jp2.matches());	
		
		atArgs = parser.parsePointcutExpression("@args(org.aspectj.weaver.tools.Java15PointcutExpressionTest.MyAnnotation,org.aspectj.weaver.tools.Java15PointcutExpressionTest.MyAnnotation)");
		sMatch1 = atArgs.matchesMethodExecution(a);
		sMatch2 = atArgs.matchesMethodExecution(c);
		assertTrue("never matches A",sMatch1.neverMatches());
		assertTrue("maybe matches C",sMatch2.maybeMatches());
		JoinPointMatch jp1 = sMatch2.matchesJoinPoint(new A(), new A(), new Object[] {new A(), new B()});
		assertFalse("does not match",jp1.matches());
		jp2 = sMatch2.matchesJoinPoint(new B(), new B(), new Object[] {new B(),new B()});
		assertTrue("matches",jp2.matches());					
	}
	
	public void testAtArgsWithBinding() {
		PointcutParameter p1 = parser.createPointcutParameter("a",MyAnnotation.class);
		PointcutParameter p2 = parser.createPointcutParameter("b", MyAnnotation.class);
		PointcutExpression atArgs = parser.parsePointcutExpression("@args(..,a)",A.class,new PointcutParameter[] {p1});
		ShadowMatch sMatch2 = atArgs.matchesMethodExecution(c);
		assertTrue("maybe matches C",sMatch2.maybeMatches());
		JoinPointMatch jp2 = sMatch2.matchesJoinPoint(new B(), new B(), new Object[]{new A(),new B()});
		assertTrue("matches",jp2.matches());
		assertEquals(1,jp2.getParameterBindings().length);
		MyAnnotation bAnnotation = B.class.getAnnotation(MyAnnotation.class);
		assertEquals("annotation on B",bAnnotation,jp2.getParameterBindings()[0].getBinding());
		
		atArgs = parser.parsePointcutExpression("@args(a,b)",A.class,new PointcutParameter[] {p1,p2});
		sMatch2 = atArgs.matchesMethodExecution(c);
		assertTrue("maybe matches C",sMatch2.maybeMatches());
		jp2 = sMatch2.matchesJoinPoint(new B(), new B(), new Object[] {new B(),new B()});
		assertTrue("matches",jp2.matches());							
		assertEquals(2,jp2.getParameterBindings().length);
		assertEquals("annotation on B",bAnnotation,jp2.getParameterBindings()[0].getBinding());
		assertEquals("annotation on B",bAnnotation,jp2.getParameterBindings()[1].getBinding());		
	}
	
	public void testAtWithin() {
		PointcutExpression atWithin = parser.parsePointcutExpression("@within(org.aspectj.weaver.tools.Java15PointcutExpressionTest.MyAnnotation)");
		ShadowMatch sMatch1 = atWithin.matchesMethodExecution(a);
		ShadowMatch sMatch2 = atWithin.matchesMethodExecution(b);
		assertTrue("does not match a",sMatch1.neverMatches());
		assertTrue("matches b",sMatch2.alwaysMatches());
	}
	
	public void testAtWithinWithBinding() {
		PointcutParameter p1 = parser.createPointcutParameter("x",MyAnnotation.class);
		PointcutExpression atWithin = parser.parsePointcutExpression("@within(x)",B.class,new PointcutParameter[] {p1});
		ShadowMatch sMatch1 = atWithin.matchesMethodExecution(a);
		ShadowMatch sMatch2 = atWithin.matchesMethodExecution(b);
		assertTrue("does not match a",sMatch1.neverMatches());
		assertTrue("matches b",sMatch2.alwaysMatches());
		JoinPointMatch jpm = sMatch2.matchesJoinPoint(new B(), new B(), new Object[0]);
		assertTrue(jpm.matches());
		assertEquals(1,jpm.getParameterBindings().length);
		MyAnnotation bAnnotation = B.class.getAnnotation(MyAnnotation.class);
		assertEquals("annotation on B",bAnnotation,jpm.getParameterBindings()[0].getBinding());		
	}
	
	public void testAtWithinCode() {
		PointcutExpression atWithinCode = parser.parsePointcutExpression("@withincode(org.aspectj.weaver.tools.Java15PointcutExpressionTest.MyAnnotation)");
		ShadowMatch sMatch1 = atWithinCode.matchesMethodCall(a,b);
		ShadowMatch sMatch2 = atWithinCode.matchesMethodCall(a,a);
		assertTrue("does not match from b",sMatch1.neverMatches());
		assertTrue("matches from a",sMatch2.alwaysMatches());		
	}
	
	public void testAtWithinCodeWithBinding() {
		PointcutParameter p1 = parser.createPointcutParameter("x",MyAnnotation.class);
		PointcutExpression atWithinCode = parser.parsePointcutExpression("@withincode(x)",A.class,new PointcutParameter[] {p1});
		ShadowMatch sMatch2 = atWithinCode.matchesMethodCall(a,a);
		assertTrue("matches from a",sMatch2.alwaysMatches());
		JoinPointMatch jpm = sMatch2.matchesJoinPoint(new A(), new A(), new Object[0]);
		assertEquals(1,jpm.getParameterBindings().length);
		MyAnnotation annOna = a.getAnnotation(MyAnnotation.class);
		assertEquals("MyAnnotation on a",annOna,jpm.getParameterBindings()[0].getBinding());
	}
	
	public void testAtAnnotation() {
		PointcutExpression atAnnotation = parser.parsePointcutExpression("@annotation(org.aspectj.weaver.tools.Java15PointcutExpressionTest.MyAnnotation)");
		ShadowMatch sMatch1 = atAnnotation.matchesMethodCall(b,a);
		ShadowMatch sMatch2 = atAnnotation.matchesMethodCall(a,a);
		assertTrue("does not match call to b",sMatch1.neverMatches());
		assertTrue("matches call to a",sMatch2.alwaysMatches());				
	}
	
	public void testAtAnnotationWithBinding() {
		PointcutParameter p1 = parser.createPointcutParameter("x",MyAnnotation.class);
		PointcutExpression atAnnotation = parser.parsePointcutExpression("@annotation(x)",A.class,new PointcutParameter[] {p1});
		ShadowMatch sMatch2 = atAnnotation.matchesMethodCall(a,a);
		assertTrue("matches call to a",sMatch2.alwaysMatches());				
		JoinPointMatch jpm = sMatch2.matchesJoinPoint(new A(), new A(), new Object[0]);
		assertTrue(jpm.matches());
		assertEquals(1,jpm.getParameterBindings().length);
		MyAnnotation annOna = a.getAnnotation(MyAnnotation.class);
		assertEquals("MyAnnotation on a",annOna,jpm.getParameterBindings()[0].getBinding());		
	}
	
	public void testReferencePointcutNoParams() {
		PointcutExpression pc = parser.parsePointcutExpression("foo()",C.class,new PointcutParameter[0]);
		ShadowMatch sMatch1 = pc.matchesMethodCall(a,b);
		ShadowMatch sMatch2 = pc.matchesMethodExecution(a);
		assertTrue("no match on call",sMatch1.neverMatches());
		assertTrue("match on execution",sMatch2.alwaysMatches());
		
		pc = parser.parsePointcutExpression("org.aspectj.weaver.tools.Java15PointcutExpressionTest.C.foo()");
		sMatch1 = pc.matchesMethodCall(a,b);
		sMatch2 = pc.matchesMethodExecution(a);
		assertTrue("no match on call",sMatch1.neverMatches());
		assertTrue("match on execution",sMatch2.alwaysMatches());
	}
	
	public void testReferencePointcutParams() {
		PointcutParameter p1 = parser.createPointcutParameter("x",A.class);
		PointcutExpression pc = parser.parsePointcutExpression("goo(x)",C.class,new PointcutParameter[] {p1});

		ShadowMatch sMatch1 = pc.matchesMethodCall(a,b);
		ShadowMatch sMatch2 = pc.matchesMethodExecution(a);
		assertTrue("no match on call",sMatch1.neverMatches());
		assertTrue("match on execution",sMatch2.maybeMatches());
		A anA = new A();
		JoinPointMatch jpm = sMatch2.matchesJoinPoint(anA, new A(), new Object[0]);
		assertTrue(jpm.matches());
		assertEquals("should be bound to anA",anA,jpm.getParameterBindings()[0].getBinding());

	}
	
	public void testExecutionWithClassFileRetentionAnnotation() {
		PointcutExpression pc1 = parser.parsePointcutExpression("execution(@org.aspectj.weaver.tools.Java15PointcutExpressionTest.MyAnnotation * *(..))");
		PointcutExpression pc2 = parser.parsePointcutExpression("execution(@org.aspectj.weaver.tools.Java15PointcutExpressionTest.MyClassFileRetentionAnnotation * *(..))");
		ShadowMatch sMatch = pc1.matchesMethodExecution(a);
		assertTrue("matches",sMatch.alwaysMatches());
		sMatch = pc2.matchesMethodExecution(a);
		assertTrue("no match",sMatch.neverMatches());
		sMatch = pc1.matchesMethodExecution(b);
		assertTrue("no match",sMatch.neverMatches());
		sMatch = pc2.matchesMethodExecution(b);
		assertTrue("matches",sMatch.alwaysMatches());
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		parser = new PointcutParser();
		a = A.class.getMethod("a");
		b = B.class.getMethod("b");
		c = B.class.getMethod("c",new Class[] {A.class,B.class});
	}

	@Retention(RetentionPolicy.RUNTIME)
	private @interface MyAnnotation {}
	
	private @interface MyClassFileRetentionAnnotation {}
	
	private static class A {
		@MyAnnotation public void a() {}
	}
	
	@MyAnnotation
	private static class B {
		@MyClassFileRetentionAnnotation public void b() {}
		public void c(A anA, B aB) {}
	}
	
	private static class C {
		
		@Pointcut("execution(* *(..))")
		public void foo() {}
		
		@Pointcut(value="execution(* *(..)) && this(x)", argNames="x")
		public void goo(A x) {}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7041.java