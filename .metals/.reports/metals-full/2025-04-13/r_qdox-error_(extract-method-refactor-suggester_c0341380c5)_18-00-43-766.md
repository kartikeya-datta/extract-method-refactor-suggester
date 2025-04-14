error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15572.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15572.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15572.java
text:
```scala
P@@ointcut concreteEntry = entry.concretize(inAspect, 0, null); //IntMap.EMPTY);

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.weaver.patterns;

import java.io.*;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.List;

import org.aspectj.weaver.*;
import org.aspectj.weaver.ast.*;
import org.aspectj.util.FuzzyBoolean;

public class PerCflow extends PerClause {
	private boolean isBelow;
	private Pointcut entry;
	
	public PerCflow(Pointcut entry, boolean isBelow) {
		this.entry = entry;
		this.isBelow = isBelow;
	}
	
	// -----
	
    public FuzzyBoolean match(Shadow shadow) {
        return FuzzyBoolean.YES;
    }

    public void resolveBindings(IScope scope, Bindings bindings) {
    	// assert bindings == null;
    	entry.resolve(scope);  
    }

    public Test findResidue(Shadow shadow, ExposedState state) {
    	Expr myInstance =
    		Expr.makeCallExpr(AjcMemberMaker.perCflowAspectOfMethod(inAspect),
    							Expr.NONE, inAspect);
    	state.setAspectInstance(myInstance);
    	return Test.makeCall(AjcMemberMaker.perCflowHasAspectMethod(inAspect), Expr.NONE);
    }


	public PerClause concretize(ResolvedTypeX inAspect) {
		PerCflow ret = new PerCflow(entry, isBelow);
		ret.inAspect = inAspect;
		if (inAspect.isAbstract()) return ret;
		
		Member cflowStackField = new ResolvedMember(
			Member.FIELD, inAspect, Modifier.STATIC|Modifier.PUBLIC|Modifier.FINAL,
						TypeX.forName(NameMangler.CFLOW_STACK_TYPE), NameMangler.PERCFLOW_FIELD_NAME, TypeX.NONE);
						
		World world = inAspect.getWorld();
		
		CrosscuttingMembers xcut = inAspect.crosscuttingMembers;
		
		Collection previousCflowEntries = xcut.getCflowEntries();
		Pointcut concreteEntry = entry.concretize(inAspect, IntMap.EMPTY);
		List innerCflowEntries = new ArrayList(xcut.getCflowEntries());
		innerCflowEntries.removeAll(previousCflowEntries);
					
		xcut.addConcreteShadowMunger(
				Advice.makePerCflowEntry(world, concreteEntry, isBelow, cflowStackField, 
									inAspect, innerCflowEntries));
		return ret;
	}

    public void write(DataOutputStream s) throws IOException {
    	PERCFLOW.write(s);
    	entry.write(s);
    	s.writeBoolean(isBelow);
    	writeLocation(s);
    }
    
	public static PerClause readPerClause(DataInputStream s, ISourceContext context) throws IOException {
		PerCflow ret = new PerCflow(Pointcut.read(s, context), s.readBoolean());
		ret.readLocation(context, s);
		return ret;
	}
	
	public PerClause.Kind getKind() {
		return PERCFLOW;
	}
	
	public String toString() {
		return "percflow(" + inAspect + " on " + entry + ")";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15572.java