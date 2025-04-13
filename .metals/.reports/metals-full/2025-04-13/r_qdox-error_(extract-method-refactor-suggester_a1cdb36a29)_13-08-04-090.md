error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13217.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13217.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13217.java
text:
```scala
C@@rosscuttingMembers xcut = new CrosscuttingMembers(inAspect,true);

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.weaver.bcel;

import java.io.*;
import java.lang.reflect.Modifier;
import java.util.*;

import org.aspectj.weaver.*;
import org.aspectj.weaver.patterns.*;
import org.aspectj.weaver.patterns.SimpleScope;

public class PointcutResidueTestCase extends WeaveTestCase {
	{
		regenerate = false;
	}
	public PointcutResidueTestCase(String name) {
		super(name);
	}
		
    String[] none = new String[0];
    
    
    // -----
    
    
    
    
    
    // ----   
    
    
	
	public void testArgResidue1() throws IOException {
		checkMultiArgWeave(
			"StringResidue1", 
			"call(* *(java.lang.Object, java.lang.Object)) && args(java.lang.String, java.lang.String)");
	}
	
	public void testArgResidue2() throws IOException {
		checkMultiArgWeave(
			"StringResidue2", 
			"call(* *(java.lang.Object, java.lang.Object)) && args(.., java.lang.String)");
	}
    public void testArgResidue3() throws IOException {
		checkMultiArgWeave(
			"StringResidue3", 
			"call(* *(java.lang.Object, java.lang.Object)) && args(java.lang.String, ..)");
	}	
	
	// BETAX this is a beta feature.    
//    public void testArgResidue4() throws IOException {
//        checkMultiArgWeave(
//            "StringResidue4",
//            "call(* *(java.lang.Object, java.lang.Object)) && args(.., java.lang.String, ..)");
//    }
    
    public void testMultiArgState() throws IOException {
        checkWeave(
            "StateResidue",
            "MultiArgHelloWorld",
            "call(* *(java.lang.Object, java.lang.Object)) && args(s, ..)",
            new String[] { "java.lang.String" },
            new String[] { "s" });
        checkWeave(
            "StateResidue",
            "MultiArgHelloWorld",
            "call(* *(java.lang.Object, java.lang.Object)) && args(s, *)",
            new String[] { "java.lang.String" },
            new String[] { "s" });
    }



	public void testAdd() throws IOException {
		checkDynamicWeave("AddResidue", "call(public * add(..)) && target(java.util.ArrayList)");
		checkDynamicWeave("AddResidue", "call(public * add(..)) && (target(java.util.ArrayList) || target(java.lang.String))");
		checkDynamicWeave("AddResidue", "call(public * add(..)) && this(java.io.Serializable) && target(java.util.ArrayList) && !this(java.lang.Integer)");
	}	
	
	public void testNot() throws IOException {
		checkDynamicWeave("AddNotResidue", "call(public * add(..)) && !target(java.util.ArrayList)");
		checkDynamicWeave("AddNotResidue", "call(public * add(..)) && !(target(java.util.ArrayList) || target(java.lang.String)) ");
		checkDynamicWeave("AddNotResidue", "call(public * add(..)) && target(java.lang.Object) && !target(java.util.ArrayList)");
	}

    public void testState() throws IOException {
        checkWeave(
            "AddStateResidue",
            "DynamicHelloWorld",
            "call(public * add(..)) && target(list)",
            new String[] { "java.util.ArrayList" },
            new String[] { "list" });
        checkWeave(
            "AddStateResidue",
            "DynamicHelloWorld",
            "target(foo) && !target(java.lang.Integer) && call(public * add(..))",
            new String[] { "java.util.ArrayList" },
            new String[] { "foo" });
        checkDynamicWeave(
            "AddResidue",
            "call(public * add(..)) && (target(java.util.ArrayList) || target(java.lang.String))");
        checkDynamicWeave(
            "AddResidue",
            "call(public * add(..)) && this(java.io.Serializable) && target(java.util.ArrayList) && !this(java.lang.Integer)");
    }
	
	public void testNoResidueArgs() throws IOException {
		checkDynamicWeave("NoResidue", "call(public * add(..)) && args(java.lang.Object)");
		checkDynamicWeave("NoResidue", "call(public * add(..)) && args(*)");
		checkDynamicWeave("NoResidue", "call(public * add(..))");
	}

	// ---- cflow tests
	
    public void testCflowState() throws IOException {
        checkWeave(
            "CflowStateResidue",
            "DynamicHelloWorld",
            "cflow(call(public * add(..)) && target(list)) && execution(public void main(..))",
            new String[] { "java.util.ArrayList" },
            new String[] { "list" });
//        checkWeave(
//            "CflowStateResidue",
//            "DynamicHelloWorld",
//            "cflow(call(public * add(..)) && target(list)) && this(obj) && execution(public void doit(..))",
//            new String[] { "java.lang.Object", "java.util.ArrayList" },
//            new String[] { "obj", "list" });
//        checkWeave(
//            "AddStateResidue",
//            "DynamicHelloWorld",
//            "target(foo) && !target(java.lang.Integer) && call(public * add(..))",
//            new String[] { "java.util.ArrayList" },
//            new String[] { "foo" });
//        checkDynamicWeave(
//            "AddResidue",
//            "call(public * add(..)) && (target(java.util.ArrayList) || target(java.lang.String))");
//        checkDynamicWeave(
//            "AddResidue",
//            "call(public * add(..)) && this(java.io.Serializable) && target(java.util.ArrayList) && !this(java.lang.Integer)");
    }
	




	// ----
	
    private void checkDynamicWeave(String label, String pointcutSource) throws IOException {
		checkWeave(label, "DynamicHelloWorld", pointcutSource, new String[0], new String[0]);
	}
	
    private void checkMultiArgWeave(String label, String pointcutSource) throws IOException {
		checkWeave(label, "MultiArgHelloWorld", pointcutSource, new String[0], new String[0]);
	}		
	
    private void checkWeave(
        String label,
        String filename,
        String pointcutSource,
        String[] formalTypes,
        String[] formalNames)
        throws IOException 
    {
        final Pointcut sp = Pointcut.fromString(pointcutSource);
        final Pointcut rp =
            sp.resolve(
            	new SimpleScope(
            	world,
                SimpleScope.makeFormalBindings(UnresolvedType.forNames(formalTypes),
                formalNames)
                ));

        ShadowMunger pp =
            new BcelAdvice(
                AdviceKind.Before,
                rp,
                MemberImpl.method(
                    UnresolvedType.forName("Aspect"),
                    Modifier.STATIC,
                    "ajc_before_0",
                    MemberImpl.typesToSignature(
                        ResolvedType.VOID,
                        UnresolvedType.forNames(formalTypes),false)),
            	0, -1, -1, null, null);

		ResolvedType inAspect = world.resolve("Aspect");
		CrosscuttingMembers xcut = new CrosscuttingMembers(inAspect);
		inAspect.crosscuttingMembers = xcut;
		
        ShadowMunger cp = pp.concretize(inAspect, world, null);
        
        xcut.addConcreteShadowMunger(cp);
        
        //System.out.println("extras: " + inAspect.getExtraConcreteShadowMungers());
//        List advice = new ArrayList();
//        advice.add(cp);
//        advice.addAll(inAspect.getExtraConcreteShadowMungers());
        weaveTest(new String[] { filename }, label, xcut.getShadowMungers());

        checkSerialize(rp);
    }
	
	public void weaveTest(String name, String outName, ShadowMunger planner) throws IOException {
        List l = new ArrayList(1);
        l.add(planner);
        weaveTest(name, outName, l);
    }


	public void checkSerialize(Pointcut p) throws IOException {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bo);
		p.write(out);
		out.close();
		
		ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
		VersionedDataInputStream in = new VersionedDataInputStream(bi);
		Pointcut newP = Pointcut.read(in, null);
		
		assertEquals("write/read", p, newP);	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13217.java