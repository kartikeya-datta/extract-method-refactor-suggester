error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16737.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16737.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16737.java
text:
```scala
a@@ssertEquals ("before1 TestParam Testerossa TestParam Testerossa override after1 name: org::eclipse::xtend::middleend::xtend::test::expressions::testSomeFunctionAdviceCtx  paramNames: param,p  paramTypes: String,org::eclipse::xtend::middleend::xtend::test::Person  paramValues: TestParam,Person: Tester Testerossa", result.toString());

/*
Copyright (c) 2008 André Arnold.
All rights reserved. This program and the accompanying materials
are made available under the terms of the Eclipse Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/epl-v10.html

Contributors:
    André Arnold - initial API and implementation
 */
package org.eclipse.xtend.middleend.xtend.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

import org.eclipse.internal.xtend.type.impl.java.JavaMetaModel;
import org.eclipse.xtend.middleend.xtend.XtendBackendFacade;
import org.eclipse.xtend.typesystem.MetaModel;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author André Arnold
 *
 */
public class AopTest extends JavaXtendTest {

	private Collection<MetaModel> mms = new ArrayList<MetaModel> ();
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		mms.add(new JavaMetaModel());
	}
	
	@Test
	public void testAdvicedFunction() {
		Map<String, Object> vars = new HashMap<String, Object> ();
		vars.put("param", "TestParam");
		Object result = XtendBackendFacade.evaluateExpression ("testSomeAdvicedFunction(param)", "org::eclipse::xtend::middleend::xtend::test::expressions", "iso-8859-1", mms, vars, new HashMap<String, Object>(), Arrays.asList("org::eclipse::xtend::middleend::xtend::test::advices"));

		assertEquals ("before1 TestParam TestParam body after1", result.toString());
	}

	@Test
	public void testSomeAdvicedFunction() {
		Map<String, Object> vars = new HashMap<String, Object> ();
		vars.put("param", "TestParam");
		vars.put("p", _person);
		Object result = XtendBackendFacade.evaluateExpression ("testSomeAdvicedFunction(param, p)", "org::eclipse::xtend::middleend::xtend::test::expressions", "iso-8859-1", mms, vars, new HashMap<String, Object>(), Arrays.asList("org::eclipse::xtend::middleend::xtend::test::advices"));

		assertEquals ("before1 TestParam Testerossa TestParam after1", result.toString());
	}

	@Test
	public void testSomeSubPackageFunction() {
		Map<String, Object> vars = new HashMap<String, Object> ();
		vars.put("param1", "TestParam");
		vars.put("param2", 7L);
		Object result = XtendBackendFacade.evaluateExpression ("testSomeFunction(param1, param2)", "org::eclipse::xtend::middleend::xtend::test::sub::subpackage", "iso-8859-1", mms, vars, new HashMap<String, Object>(), Arrays.asList("org::eclipse::xtend::middleend::xtend::test::advices"));

		assertEquals ("before2 TestParam7 after2", result.toString());
	}

	@Test
	public void testAdviceSubPackageFunction() {
		Map<String, Object> vars = new HashMap<String, Object> ();
		vars.put("param1", "TestParam");
		vars.put("param2", 7L);
		Object result = XtendBackendFacade.evaluateExpression ("testAdviceSubPackageFunction(param1, param2)", "org::eclipse::xtend::middleend::xtend::test::expressions", "iso-8859-1", mms, vars, new HashMap<String, Object>(), Arrays.asList("org::eclipse::xtend::middleend::xtend::test::advices"));

		assertEquals ("before2 TestParam7 after2", result.toString());
	}

	@Test
	public void testOtherAdviceSubPackageFunction() {
		Map<String, Object> vars = new HashMap<String, Object> ();
		vars.put("param1", "TestParam");
		vars.put("param2", 7L);
		Object result = XtendBackendFacade.evaluateExpression ("testOtherAdviceSubPackageFunction(param1, param2)", "org::eclipse::xtend::middleend::xtend::test::expressions", "iso-8859-1", mms, vars, new HashMap<String, Object>(), Arrays.asList("org::eclipse::xtend::middleend::xtend::test::advices"));

		assertEquals ("before1 TestParam before2 TestParam7 after2 after1", result.toString());
	}

	@Test
	public void testSomeFunctionAdviceCtx() {
		Map<String, Object> vars = new HashMap<String, Object> ();
		vars.put("param", "TestParam");
		vars.put("p", _person);
		Object result = XtendBackendFacade.evaluateExpression ("testSomeFunctionAdviceCtx(param, p)", "org::eclipse::xtend::middleend::xtend::test::expressions", "iso-8859-1", mms, vars, new HashMap<String, Object>(), Arrays.asList("org::eclipse::xtend::middleend::xtend::test::advices"));

		assertEquals ("before1 TestParam Testerossa TestParam after1 name: org::eclipse::xtend::middleend::xtend::test::expressions::testSomeFunctionAdviceCtx  paramNames: param,p  paramTypes: String,org::eclipse::xtend::middleend::xtend::test::Person  paramValues: TestParam,Person: Tester Testerossa", result.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16737.java