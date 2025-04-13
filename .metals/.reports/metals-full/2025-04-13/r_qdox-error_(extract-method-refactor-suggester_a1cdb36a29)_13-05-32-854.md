error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/475.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/475.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,70]

error in qdox parser
file content:
```java
offset: 70
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/475.java
text:
```scala
+ " xpand2::Type,\n" + " xpand2::Feature,\n" + " xpand2::Property\n}")@@;

/*******************************************************************************
 * Copyright (c) 2005, 2007 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/

package org.eclipse.xtend.expression.parser;

import junit.framework.TestCase;

import org.eclipse.internal.xtend.expression.ast.BooleanLiteral;
import org.eclipse.internal.xtend.expression.ast.Case;
import org.eclipse.internal.xtend.expression.ast.Cast;
import org.eclipse.internal.xtend.expression.ast.ChainExpression;
import org.eclipse.internal.xtend.expression.ast.Expression;
import org.eclipse.internal.xtend.expression.ast.FeatureCall;
import org.eclipse.internal.xtend.expression.ast.IfExpression;
import org.eclipse.internal.xtend.expression.ast.IntegerLiteral;
import org.eclipse.internal.xtend.expression.ast.ListLiteral;
import org.eclipse.internal.xtend.expression.ast.NullLiteral;
import org.eclipse.internal.xtend.expression.ast.OperationCall;
import org.eclipse.internal.xtend.expression.ast.StringLiteral;
import org.eclipse.internal.xtend.expression.ast.SwitchExpression;
import org.eclipse.internal.xtend.xtend.parser.ParseFacade;

/**
 * @author Sven Efftinge (http://www.efftinge.de)
 * @author Arno Haase
 */
public class ParserTest extends TestCase {

	private Expression parse(final String expression) {
		return ParseFacade.expression(expression);
	}

    public final void testSimple() {
        final Expression expr = parse("true == null");
        assertTrue(expr instanceof OperationCall);
        final OperationCall op = (OperationCall) expr;
        assertTrue(op.getTarget() instanceof BooleanLiteral);
        assertTrue(op.getParams()[0] instanceof NullLiteral);
    }

    public final void testSimple2() {
        final Expression expr = parse("String.feature.test(true, {\"test\",\"hallo\"})");
        assertTrue(expr instanceof OperationCall);
        final OperationCall op = (OperationCall) expr;
        assertEquals("test", op.getName().getValue());

        assertTrue(op.getTarget() instanceof FeatureCall);
        assertTrue(op.getTarget() instanceof FeatureCall);
        FeatureCall tl = (FeatureCall) op.getTarget();
        assertEquals("feature", tl.getName().getValue());
        tl = (FeatureCall) tl.getTarget();
        assertEquals("String", tl.getName().getValue());

        assertTrue(op.getParams().length == 2);
        assertTrue(op.getParams()[0] instanceof BooleanLiteral);
        final ListLiteral colLit = (ListLiteral) op.getParams()[1];
        assertEquals(2, colLit.getElements().length);
        assertEquals("\"test\"", ((StringLiteral) colLit.getElements()[0]).getLiteralValue().getValue());
        assertEquals("\"hallo\"", ((StringLiteral) colLit.getElements()[1]).getLiteralValue().getValue());
    }

    public final void testIfExpression() {
        final Expression expr = parse("(client.sIdent1 != null) ? client.sIdent1 : \"XXXXXXXX\"");
        assertTrue(expr instanceof IfExpression);
    }
    
    public void testIfExpression2() throws Exception {
		final IfExpression e = (IfExpression) parse("if true then true else false");
		assertEquals("true",e.getCondition().toString());
	}
    public void testIfExpression3() throws Exception {
    	final IfExpression e = (IfExpression) parse("if true then true else if false then false");
    	assertEquals(null,((IfExpression)e.getElsePart()).getElsePart());
    }

    public final void testEscaped() {
        final Expression expr = parse("\"\\\"\"");
        assertTrue(expr instanceof StringLiteral);
        assertEquals("\"", ((StringLiteral) expr).getValue());
    }

    public final void testNot() {
        final Expression expr = parse("! ts.checked");
        assertNotNull(expr);
    }

    public final void testCast() {
        final Expression expr = parse("(List[InnerType]) anExpr");
        assertNotNull(expr);
        final Cast cast = (Cast) expr;
        assertEquals("List[InnerType]", cast.getType().getValue());
        final FeatureCall fc = (FeatureCall) cast.getTarget();
        assertEquals("anExpr", fc.getName().getValue());
        assertNull(fc.getTarget());
    }

    public final void testSwitch() {
        SwitchExpression expr = (SwitchExpression) parse("switch { default : true }");
        assertNull(expr.getSwitchExpr());
        assertTrue(expr.getCases().isEmpty());
        assertEquals("true", ((BooleanLiteral) expr.getDefaultExpr()).getLiteralValue().getValue());

        expr = (SwitchExpression) parse("switch (\"test\") { case \"horst\": false default : true }");
        assertEquals("\"test\"", ((StringLiteral) expr.getSwitchExpr()).getLiteralValue().getValue());
        final Case c = expr.getCases().get(0);
        assertEquals("\"horst\"", ((StringLiteral) c.getCondition()).getLiteralValue().getValue());
        assertEquals("false", ((BooleanLiteral) c.getThenPart()).getLiteralValue().getValue());

        assertEquals("true", ((BooleanLiteral) expr.getDefaultExpr()).getLiteralValue().getValue());
    }

    public final void testChainExpression() {
        final ChainExpression expr = (ChainExpression) parse("1 -> 2 -> 3 -> 4");
        assertEquals("4", expr.getNext().toString());
        assertEquals("1->2->3", expr.getFirst().toString());
    }

    public final void testPositionInfo() {
        final Expression exp = parse("\n\n\n1");
        assertEquals(4, exp.getLine());
        assertEquals(3, exp.getStart());
        assertEquals(4, exp.getEnd());

    }

    public final void testPositionInfo2() {
        final Expression exp = parse("/*\n\n\n*/1");
        assertEquals(4, exp.getLine());
        assertEquals(7, exp.getStart());
        assertEquals(8, exp.getEnd());
    }

    public final void testPositionInfo3() {
        final OperationCall exp = (OperationCall) parse("'/*\n\n\n*/'+1");
        assertEquals(1, exp.getLine());
        assertEquals(0, exp.getStart());
        assertEquals(11, exp.getEnd());
        final StringLiteral target = (StringLiteral) exp.getTarget();
        assertEquals(1, target.getLine());
        assertEquals(0, target.getStart());
        assertEquals(9, target.getEnd());
        final IntegerLiteral param = (IntegerLiteral) exp.getParams()[0];
        assertEquals(4, param.getLine());
        assertEquals(10, param.getStart());
        assertEquals(11, param.getEnd());
    }

    public final void testTypeLiterals() {
        final Expression e = parse("{" + "  Object,\n" + " String,\n" + "Collection,\n" + " Set,\n" + " List,\n"
                + " oaw::Type,\n" + " oaw::Feature,\n" + " oaw::Property\n}");

        assertNotNull(e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/475.java