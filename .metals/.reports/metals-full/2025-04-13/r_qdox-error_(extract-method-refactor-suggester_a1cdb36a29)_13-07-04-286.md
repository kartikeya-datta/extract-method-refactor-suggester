error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15685.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15685.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15685.java
text:
```scala
R@@esolvedTypeX iter = world.getCoreType(TypeX.forRawTypeNames("java.util.Iterator"));

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

import java.lang.reflect.Modifier;

import org.aspectj.weaver.*;

/**
 * This is a test case for the nameType parts of worlds.
 */
public class WorldTestCase extends AbstractWorldTestCase {

    public WorldTestCase(String name) {
        super(name);
    }
        
    private final BcelWorld world 
        = new BcelWorld(BcweaverTests.TESTDATA_PATH + "/tracing.jar");

	protected World getWorld() {
		return world;
	}

	// XXX fix the various XXXs before expecting this test to work
    public void xtestTraceJar() {
        ResolvedTypeX trace = world.resolve(TypeX.forName("Trace"),true);
        assertTrue("Couldnt find type Trace",trace!=ResolvedTypeX.MISSING);
        fieldsTest(trace, Member.NONE);
        /*Member constr = */Member.methodFromString("void Trace.<init>()"); 
        //XXX need attribute fix - 
        //methodsTest(trace, new Member[] { constr });

        interfacesTest(trace, ResolvedTypeX.NONE);
        superclassTest(trace, TypeX.OBJECT);
        isInterfaceTest(trace, false);
        isClassTest(trace, false);
        isAspectTest(trace, true);

        pointcutsTest(trace, 
            new Member[] {
                Member.pointcut(trace, "traced", "(Ljava/lang/Object;)V"),
            });

        modifiersTest(trace.findPointcut("traced"), 
            Modifier.PUBLIC | Modifier.ABSTRACT);
        
        mungersTest(trace, 
            new ShadowMunger[] {
				world.shadowMunger("before(foo): traced(foo) -> void Trace.ajc_before_4(java.lang.Object))",
            					0),
				world.shadowMunger("afterReturning(foo): traced(foo) -> void Trace.ajc_afterreturning_3(java.lang.Object, java.lang.Object))",
            					Advice.ExtraArgument),
				world.shadowMunger("around(): execution(* doit(..)) -> java.lang.Object Trace.ajc_around_2(org.aspectj.runtime.internal.AroundClosure))",
            					Advice.ExtraArgument),
				world.shadowMunger("around(foo): traced(foo) -> java.lang.Object Trace.ajc_around_1(java.lang.Object, org.aspectj.runtime.internal.AroundClosure))",
            					Advice.ExtraArgument),
            });
        
        ResolvedTypeX myTrace = world.resolve(TypeX.forName("MyTrace"),true);
        assertTrue("Couldnt find type MyTrace",myTrace!=ResolvedTypeX.MISSING);

        interfacesTest(myTrace, ResolvedTypeX.NONE);
        superclassTest(myTrace, trace);
        isInterfaceTest(myTrace, false);
        isClassTest(myTrace, false);
        isAspectTest(myTrace, true);

        //XXX need attribute fix - 
        //fieldsTest(myTrace, Member.NONE);


        pointcutsTest(trace, 
            new Member[] {
                Member.pointcut(trace, "traced", "(Ljava/lang/Object;)V"),
            });

        modifiersTest(myTrace.findPointcut("traced"), 
            Modifier.PUBLIC);
        
        // this tests for declared mungers
        mungersTest(myTrace, ShadowMunger.NONE);        
        
    }

    public void testIterator() {
        int abstractPublic = Modifier.ABSTRACT | Modifier.PUBLIC;
        ResolvedTypeX iter = world.getCoreType(TypeX.forName("java.util.Iterator"));
      
        modifiersTest(iter, abstractPublic | Modifier.INTERFACE);
        fieldsTest(iter, ResolvedMember.NONE);
        methodsTest(iter, 
            new Member[] {
                Member.method(iter, 0, "hasNext", "()Z"),
                Member.method(iter, 0, "remove", "()V"),
                Member.method(iter, 0, "next", "()Ljava/lang/Object;"),
                });
        ResolvedMember remove = iter.lookupMethod(Member.method(iter, 0, "remove", "()V"));
        assertNotNull("iterator doesn't have remove" , remove);
        modifiersTest(remove, abstractPublic | Modifier.INTERFACE);
        exceptionsTest(remove, TypeX.NONE);

        ResolvedMember clone = iter.lookupMethod(Member.method(TypeX.OBJECT, 0, "clone", "()Ljava/lang/Object;"));
        assertNotNull("iterator doesn't have clone" , clone);
        modifiersTest(clone, Modifier.PROTECTED | Modifier.NATIVE);
        exceptionsTest(clone, TypeX.forNames(new String[] {"java.lang.CloneNotSupportedException"}));

        interfacesTest(iter, ResolvedTypeX.NONE);
        superclassTest(iter, TypeX.OBJECT);
        pointcutsTest(iter, ResolvedMember.NONE);
        mungersTest(iter, ShadowMunger.NONE);
        isInterfaceTest(iter, true);
        isClassTest(iter, false);
        isAspectTest(iter, false);
    }

	public void testObjectCoersion() {
		assertCouldBeCoercibleFrom("java.lang.Object", "java.lang.String");
		assertCouldBeCoercibleFrom("java.lang.Integer", "java.lang.Object");
		assertCouldBeCoercibleFrom("java.io.Serializable", "java.lang.Runnable");		
		assertCouldBeCoercibleFrom("java.util.Stack", "java.lang.Runnable");		
		assertCouldNotBeCoercibleFrom("java.lang.Runnable", "java.lang.Integer");				
		assertCouldNotBeCoercibleFrom("java.lang.Integer", "java.lang.String");			
		assertCouldNotBeCoercibleFrom("java.lang.Integer", "java.lang.Runnable");							
	}

	// ----

	private void assertCouldBeCoercibleFrom(String a, String b) {
		isCoerceableFromTest(world.resolve(a), world.resolve(b), true);
	}

	private void assertCouldNotBeCoercibleFrom(String a, String b) {
		isCoerceableFromTest(world.resolve(a), world.resolve(b), false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15685.java