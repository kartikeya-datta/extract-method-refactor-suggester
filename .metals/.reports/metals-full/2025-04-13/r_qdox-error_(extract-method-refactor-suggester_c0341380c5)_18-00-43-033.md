error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13960.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13960.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13960.java
text:
```scala
e@@xecCtx.getResourceManager().setFileEncoding("ISO-8859-1");

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

import junit.framework.TestCase;

import org.eclipse.internal.xpand2.model.XpandDefinition;
import org.eclipse.internal.xtend.expression.parser.SyntaxConstants;
import org.eclipse.xpand2.XpandExecutionContextImpl;
import org.eclipse.xpand2.XpandFacade;
import org.eclipse.xtend.type.impl.java.JavaMetaModel;
import org.eclipse.xtend.type.impl.java.beans.JavaBeansStrategy;
import org.eclipse.xtend.typesystem.Type;

public class AopFeatureTest extends TestCase {
    private XpandExecutionContextImpl execCtx;

    private OutputStringImpl out;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        out = new OutputStringImpl ();
        
        execCtx = new XpandExecutionContextImpl(out, null);
        execCtx.registerMetaModel(new JavaMetaModel("JavaBeans", new JavaBeansStrategy()));

        // ADDED encoding
        execCtx.setFileEncoding("ISO-8859-1");
        execCtx.registerAdvices(prefix() + "Advices1");
    }

    public final void test_test1_Object() {
        final XpandDefinition def = execCtx.findDefinition(prefix() + "Adviced::test1", execCtx.getObjectType(), null);
        def.evaluate(execCtx,"foo");
        assertEquals("12", out.buff.toString());
    }

    public final void test_test2_Object() {
        final XpandDefinition def = execCtx.findDefinition(prefix() + "Adviced::test2", execCtx.getObjectType(), null);
        def.evaluate(execCtx, "foo");
        assertEquals("13", out.buff.toString());
    }

    public final void test_te2st_Object() {
        final XpandDefinition def = execCtx.findDefinition(prefix() + "Adviced::te2st", execCtx.getObjectType(), null);
        def.evaluate(execCtx,"foo");
        assertEquals("14", out.buff.toString());
    }

    public final void test_test1_String() {
        final XpandDefinition def = execCtx.findDefinition(prefix() + "Adviced::test1", execCtx.getStringType(), null);
        def.evaluate(execCtx,"foo");
        assertEquals("1258", out.buff.toString());
    }

    public final void test_test1_StringParam_String() {
        final XpandDefinition def = execCtx.findDefinition(prefix() + "Adviced::test1", execCtx.getStringType(),
                new Type[] { execCtx.getStringType() });
        def.evaluate(execCtx,"foo","bar");
        assertEquals("678", out.buff.toString());
    }

    public final void test_test1_StringParams_String() {
        final XpandDefinition def = execCtx.findDefinition(prefix() + "Adviced::test1", execCtx.getStringType(),
                new Type[] { execCtx.getStringType(), execCtx.getStringType() });
        def.evaluate(execCtx,"Foo","bar","baz");
        assertEquals("78", out.buff.toString());
    }
    
    public final void test_testParamNames_StringParam_String() {
    	XpandFacade xpandFacade = XpandFacade.create(execCtx);
    	xpandFacade.evaluate(prefix() + "Adviced::testParamNames", "foo","bar");
    	assertEquals("barbar", out.buff.toString());
    }

    private String prefix() {
        return getClass().getPackage().getName().replaceAll("\\.", SyntaxConstants.NS_DELIM) + SyntaxConstants.NS_DELIM;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13960.java