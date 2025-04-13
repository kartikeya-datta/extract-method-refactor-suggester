error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13961.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13961.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13961.java
text:
```scala
r@@esult.getResourceManager().setFileEncoding("iso-8859-1");

/*******************************************************************************
 * Copyright (c) 2005, 2009 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/

package org.eclipse.xpand.internal.tests.evaluate;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.internal.xpand2.ast.Definition;
import org.eclipse.internal.xpand2.ast.ForEachStatement;
import org.eclipse.internal.xpand2.ast.IfStatement;
import org.eclipse.internal.xpand2.ast.TextStatement;
import org.eclipse.internal.xpand2.model.XpandResource;
import org.eclipse.internal.xpand2.parser.XpandParseFacade;
import org.eclipse.internal.xtend.expression.parser.SyntaxConstants;
import org.eclipse.xpand2.XpandExecutionContext;
import org.eclipse.xpand2.XpandExecutionContextImpl;
import org.eclipse.xpand2.XpandFacade;
import org.eclipse.xpand2.output.Output;
import org.eclipse.xtend.expression.Variable;

/**
 * *
 * 
 * @author Sven Efftinge (http://www.efftinge.de) *
 */
public class StatementEvaluatorTest extends TestCase {

	private XpandExecutionContextImpl execCtx;

	private OutputStringImpl out;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		out = new OutputStringImpl();
		execCtx = createCtx(out);
	}

	private XpandExecutionContextImpl createCtx(Output out) {
		final XpandExecutionContextImpl result = new XpandExecutionContextImpl(
				out, null);
		result.setFileEncoding("iso-8859-1");
		return result;
	}

	private XpandResource parse(final String expr) throws Exception {
		return XpandParseFacade.file(new StringReader(expr), null);
	}

	private final char LG = '\u00AB';

	private final char RG = '\u00BB';

	private String tag(final String str) {
		return LG + str + RG;
	}

	public final void testEvaluation() throws Exception {
		final String id = getClass().getPackage().getName().replaceAll("\\.",
				SyntaxConstants.NS_DELIM)
				+ SyntaxConstants.NS_DELIM + "EvaluateStart::start";

		XpandFacade.create(execCtx).evaluate(id, "test", new Object[0]);
		System.out.println(out.buff.toString());
	}

	public final void testIf() throws Exception {
		final XpandResource t = parse(tag("DEFINE test FOR org::eclipse::emf::ecore::EClass")
				+ tag("IF test==1")
				+ "if"
				+ tag("ELSEIF test==2")
				+ "elseif"
				+ tag("ELSE") + "else" + tag("ENDIF") + tag("ENDDEFINE"));
		assertEquals(1, t.getDefinitions().length);

		final IfStatement ifSt = ((IfStatement) ((Definition) t
				.getDefinitions()[0]).getBody()[1]);

		out = new OutputStringImpl();
		final XpandExecutionContext ctx = (XpandExecutionContext) createCtx(out)
				.cloneWithVariable(new Variable("test", new Integer(1)));
		ifSt.evaluate(ctx);
		assertEquals("if", out.buff.toString());

		out = new OutputStringImpl();
		ifSt.evaluate((XpandExecutionContextImpl) createCtx(out)
				.cloneWithVariable(new Variable("test", new Integer(2))));
		assertEquals("elseif", out.buff.toString());

		out = new OutputStringImpl();
		ifSt.evaluate((XpandExecutionContextImpl) createCtx(out)
				.cloneWithVariable(new Variable("test", new Integer(3))));
		assertEquals("else", out.buff.toString());
	}

	public final void testForeach() throws Exception {
		final XpandResource t = parse(tag("DEFINE test FOR org::eclipse::emf::ecore::EClass")
				+ tag("FOREACH tests AS test SEPARATOR ','")
				+ tag("test")
				+ tag("ENDFOREACH") + tag("ENDDEFINE"));
		assertEquals(1, t.getDefinitions().length);

		final ForEachStatement foreachSt = (ForEachStatement) ((Definition) t
				.getDefinitions()[0]).getBody()[1];

		final List<String> tests = new ArrayList<String>();
		out = new OutputStringImpl();
		foreachSt.evaluate((XpandExecutionContextImpl) createCtx(out)
				.cloneWithVariable(new Variable("tests", tests)));
		assertEquals("", out.buff.toString());

		tests.add("hallo");
		out = new OutputStringImpl();
		foreachSt.evaluate((XpandExecutionContextImpl) createCtx(out)
				.cloneWithVariable(new Variable("tests", tests)));
		assertEquals("hallo", out.buff.toString());

		tests.add("Du");
		out = new OutputStringImpl();
		foreachSt.evaluate((XpandExecutionContextImpl) createCtx(out)
				.cloneWithVariable(new Variable("tests", tests)));
		assertEquals("hallo,Du", out.buff.toString());

		tests.add("da");
		out = new OutputStringImpl();
		foreachSt.evaluate((XpandExecutionContextImpl) createCtx(out)
				.cloneWithVariable(new Variable("tests", tests)));
		assertEquals("hallo,Du,da", out.buff.toString());
	}

	public final void testComment() throws Exception {
		final XpandResource t = parse(tag("DEFINE test FOR org::eclipse::emf::ecore::EClass")
				+ "TEST"
				+ tag("REM")
				+ tag("ENDREM")
				+ "test"
				+ tag("FOREACH tests AS test SEPARATOR ','")
				+ tag("test")
				+ tag("ENDFOREACH") + tag("ENDDEFINE"));
		TextStatement text = (TextStatement) ((Definition) t.getDefinitions()[0])
				.getBody()[0];
		assertEquals("TEST", text.getValue());
		text = (TextStatement) ((Definition) t.getDefinitions()[0]).getBody()[1];
		assertEquals("test", text.getValue());
	}

	public final void testMultiParams1() throws Exception {
		final String id = getClass().getPackage().getName().replaceAll("\\.",
				SyntaxConstants.NS_DELIM)
				+ SyntaxConstants.NS_DELIM + "MultiParams::test";
		XpandFacade.create(execCtx)
				.evaluate(id, "A", new Object[] { "B", "C" });
		assertEquals("ABC1", out.buff.toString());
	}

	public final void testMultiParams2() throws Exception {
		final String id = getClass().getPackage().getName().replaceAll("\\.",
				SyntaxConstants.NS_DELIM)
				+ SyntaxConstants.NS_DELIM + "MultiParams::test";
		XpandFacade.create(execCtx).evaluate(id, "A",
				new Object[] { "B", new Integer(1) });
		assertEquals("AB12", out.buff.toString());
	}

	public final void testForeach2() throws Exception {
		final String id = getClass().getPackage().getName().replaceAll("\\.",
				SyntaxConstants.NS_DELIM)
				+ SyntaxConstants.NS_DELIM + "Foreach::test";
		XpandFacade.create(execCtx).evaluate(id, "ABC", new Object[0]);
		final String[] result = out.buff.toString().trim().split(",");
		assertEquals("ABC", result[0].trim());
		assertEquals("AABCBABCC", result[1].trim());
		assertEquals("1A2B3C", result[2].trim());
	}

	@SuppressWarnings("cast")
	public void testBug167428() throws Exception {
		final XpandResource t = parse(tag("DEFINE test FOR String")
				+ tag("EXPAND stuff FOREACH null") + tag("ENDDEFINE")
				+ tag("DEFINE stuff FOR String") + "TEST" + tag("ENDDEFINE"));
		
		out = new OutputStringImpl();
		XpandExecutionContext ctx = (XpandExecutionContext) createCtx(out);
		t.getDefinitionsByName("test")[0]
				.evaluate(ctx,"X");
		System.err.println(out.buff);
	}
	
	public void testExpandForeachNull() throws Exception {
		final XpandResource t = parse(tag("DEFINE test FOR String")
				+ tag("EXPAND stuff FOREACH null") + tag("ENDDEFINE")
				+ tag("DEFINE stuff FOR String") + "TEST" + tag("ENDDEFINE"));
		
		out = new OutputStringImpl();
		XpandExecutionContext ctx = createCtx(out);
		t.getDefinitionsByName("test")[0].evaluate(ctx,"X");
		assertEquals(0,out.buff.toString().length());
	}
	
	public void testExpandForNull() throws Exception {
		final XpandResource t = parse(tag("DEFINE test FOR String")
				+ tag("EXPAND stuff FOR null") + tag("ENDDEFINE")
				+ tag("DEFINE stuff FOR String") + "TEST" + tag("ENDDEFINE"));
		
		out = new OutputStringImpl();
		XpandExecutionContext ctx = createCtx(out);
		t.getDefinitionsByName("test")[0].evaluate(ctx,"X");
		assertEquals(0,out.buff.toString().length());
	}

}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13961.java