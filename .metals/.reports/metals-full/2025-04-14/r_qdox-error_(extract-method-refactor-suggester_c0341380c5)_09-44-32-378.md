error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2437.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2437.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2437.java
text:
```scala
f@@dc.register (myStringEquals, true);

/*
Copyright (c) 2008 Arno Haase.
All rights reserved. This program and the accompanying materials
are made available under the terms of the Eclipse Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/epl-v10.html

Contributors:
    Arno Haase - initial API and implementation
 */
package org.eclipse.xtend.backend.expr;

import static org.eclipse.xtend.backend.testhelpers.BackendTestHelper.SOURCE_POS;
import static org.eclipse.xtend.backend.testhelpers.BackendTestHelper.createEmptyExecutionContext;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.xtend.backend.common.ExecutionContext;
import org.eclipse.xtend.backend.common.ExpressionBase;
import org.eclipse.xtend.backend.common.NamedFunction;
import org.eclipse.xtend.backend.functions.FunctionDefContextFactory;
import org.eclipse.xtend.backend.functions.FunctionDefContextInternal;
import org.eclipse.xtend.backend.testhelpers.CheckEvaluationExpression;
import org.eclipse.xtend.backend.testhelpers.NamedFunctionFactory;
import org.eclipse.xtend.backend.types.CompositeTypesystem;
import org.eclipse.xtend.backend.types.builtin.StringType;
import org.eclipse.xtend.backend.util.Pair;
import org.junit.Test;


/**
 * 
 * @author Arno Haase (http://www.haase-consulting.com)
 */
public class SwitchExpressionTest {
    
    @Test public void testLogic () {
        final CheckEvaluationExpression switchExpr = new CheckEvaluationExpression ("a");
        
        final CheckEvaluationExpression caseAExpr = new CheckEvaluationExpression ("a"); 
        final CheckEvaluationExpression valueAExpr = new CheckEvaluationExpression ("aValue"); 
        
        final CheckEvaluationExpression caseBExpr = new CheckEvaluationExpression ("b"); 
        final CheckEvaluationExpression valueBExpr = new CheckEvaluationExpression ("bValue"); 
        
        final CheckEvaluationExpression defaultExpr = new CheckEvaluationExpression ("defaultValue");
        
        final List<Pair<ExpressionBase, ExpressionBase>> cases = new ArrayList<Pair<ExpressionBase,ExpressionBase>> ();
        cases.add (new Pair<ExpressionBase, ExpressionBase> (caseAExpr, valueAExpr));
        cases.add (new Pair<ExpressionBase, ExpressionBase> (caseBExpr, valueBExpr));
        
        final SwitchExpression expr = new SwitchExpression (switchExpr, cases, defaultExpr, SOURCE_POS);
        
        assertEquals ("aValue", expr.evaluate (createEmptyExecutionContext()));
        assertEquals (1, switchExpr._evalCounter);
        assertEquals (1, caseAExpr._evalCounter);
        assertEquals (1, valueAExpr._evalCounter);
        assertEquals (0, caseBExpr._evalCounter);
        assertEquals (0, valueBExpr._evalCounter);
        assertEquals (0, defaultExpr._evalCounter);
        
        switchExpr._value = "b";
        assertEquals ("bValue", expr.evaluate (createEmptyExecutionContext()));
        assertEquals (2, switchExpr._evalCounter);
        assertEquals (2, caseAExpr._evalCounter);
        assertEquals (1, valueAExpr._evalCounter);
        assertEquals (1, caseBExpr._evalCounter);
        assertEquals (1, valueBExpr._evalCounter);
        assertEquals (0, defaultExpr._evalCounter);
        
        switchExpr._value = "c";
        assertEquals ("defaultValue", expr.evaluate (createEmptyExecutionContext()));
        assertEquals (3, switchExpr._evalCounter);
        assertEquals (3, caseAExpr._evalCounter);
        assertEquals (1, valueAExpr._evalCounter);
        assertEquals (2, caseBExpr._evalCounter);
        assertEquals (1, valueBExpr._evalCounter);
        assertEquals (1, defaultExpr._evalCounter);
        
        switchExpr._value = null;
        assertEquals ("defaultValue", expr.evaluate (createEmptyExecutionContext()));
        assertEquals (4, switchExpr._evalCounter);
        assertEquals (4, caseAExpr._evalCounter);
        assertEquals (1, valueAExpr._evalCounter);
        assertEquals (3, caseBExpr._evalCounter);
        assertEquals (1, valueBExpr._evalCounter);
        assertEquals (2, defaultExpr._evalCounter);
    }

    @Test public void testUsesOperatorEquals () {
        final CheckEvaluationExpression switchExpr = new CheckEvaluationExpression ("xyz");
        
        final CheckEvaluationExpression caseAExpr = new CheckEvaluationExpression ("a"); 
        final CheckEvaluationExpression valueAExpr = new CheckEvaluationExpression ("aValue"); 
        
        final CheckEvaluationExpression defaultExpr = new CheckEvaluationExpression ("defaultValue");
        
        final List<Pair<ExpressionBase, ExpressionBase>> cases = new ArrayList<Pair<ExpressionBase,ExpressionBase>> ();
        cases.add (new Pair<ExpressionBase, ExpressionBase> (caseAExpr, valueAExpr));
        
        final SwitchExpression expr = new SwitchExpression (switchExpr, cases, defaultExpr, SOURCE_POS);

        
        // register an equals function that returns "true" for any two strings
        final NamedFunction myStringEquals = new NamedFunctionFactory ("operatorEquals", StringType.INSTANCE, StringType.INSTANCE) {
            public Object invoke (ExecutionContext ctx, Object[] params) {
                return true;
            }
        }.create(); 
        
        final FunctionDefContextInternal fdc = new FunctionDefContextFactory (new CompositeTypesystem ()).create();
        fdc.register (myStringEquals);
        
        final ExecutionContext ctx = createEmptyExecutionContext();
        assertEquals ("defaultValue", expr.evaluate (ctx));
        
        ctx.setFunctionDefContext(fdc);
        assertEquals ("aValue", expr.evaluate (ctx));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2437.java