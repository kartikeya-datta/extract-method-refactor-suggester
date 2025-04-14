error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11140.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11140.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11140.java
text:
```scala
i@@nAspect.crosscuttingMembers.addLateTypeMunger(world.concreteTypeMunger(munger, inAspect));

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


package org.aspectj.weaver.patterns;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.Advice;
import org.aspectj.weaver.AjcMemberMaker;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.PerObjectInterfaceTypeMunger;
import org.aspectj.weaver.ResolvedTypeMunger;
import org.aspectj.weaver.ResolvedTypeX;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.World;
import org.aspectj.weaver.bcel.BcelAccessForInlineMunger;
import org.aspectj.weaver.ataspectj.Ajc5MemberMaker;
import org.aspectj.weaver.ast.Expr;
import org.aspectj.weaver.ast.Test;
import org.aspectj.weaver.ast.Var;

public class PerObject extends PerClause {
	private boolean isThis;
	private Pointcut entry;
	private static final Set thisKindSet = new HashSet(Shadow.ALL_SHADOW_KINDS);
	private static final Set targetKindSet = new HashSet(Shadow.ALL_SHADOW_KINDS);
	static {
		for (Iterator iter = Shadow.ALL_SHADOW_KINDS.iterator(); iter.hasNext();) {
			Shadow.Kind kind = (Shadow.Kind) iter.next();
			if (kind.neverHasThis()) thisKindSet.remove(kind);
			if (kind.neverHasTarget()) targetKindSet.remove(kind);
		}
	}
	
	public PerObject(Pointcut entry, boolean isThis) {
		this.entry = entry;
		this.isThis = isThis;
	}

	public Set couldMatchKinds() {
		return isThis ? thisKindSet : targetKindSet;
	}
	
	// -----
	public FuzzyBoolean fastMatch(FastMatchInfo type) {
		return FuzzyBoolean.MAYBE;
	}
	
	
    protected FuzzyBoolean matchInternal(Shadow shadow) {
    	//System.err.println("matches " + this + " ? " + shadow + ", " + shadow.hasTarget());
    	//??? could probably optimize this better by testing could match
    	if (isThis) return FuzzyBoolean.fromBoolean(shadow.hasThis());
    	else return FuzzyBoolean.fromBoolean(shadow.hasTarget());
    }

    public void resolveBindings(IScope scope, Bindings bindings) {
    	// assert bindings == null;
    	entry.resolve(scope);  
    }
    
    private Var getVar(Shadow shadow) {
    	return isThis ? shadow.getThisVar() : shadow.getTargetVar();
    }

    protected Test findResidueInternal(Shadow shadow, ExposedState state) {
    	Expr myInstance =
    		Expr.makeCallExpr(AjcMemberMaker.perObjectAspectOfMethod(inAspect),
    							new Expr[] {getVar(shadow)}, inAspect);
    	state.setAspectInstance(myInstance);
    	return Test.makeCall(AjcMemberMaker.perObjectHasAspectMethod(inAspect), 
    			new Expr[] { getVar(shadow) });
    }



	public PerClause concretize(ResolvedTypeX inAspect) {
		PerObject ret = new PerObject(entry, isThis);
		
		ret.inAspect = inAspect;
		if (inAspect.isAbstract()) return ret;
		
		
		World world = inAspect.getWorld();
		
		Pointcut concreteEntry = entry.concretize(inAspect, 0, null);
		//concreteEntry = new AndPointcut(this, concreteEntry);
		//concreteEntry.state = Pointcut.CONCRETE;
		inAspect.crosscuttingMembers.addConcreteShadowMunger(
				Advice.makePerObjectEntry(world, concreteEntry, isThis, inAspect));

        // FIXME AV - don't use lateMunger here due to test "inheritance, around advice and abstract pointcuts"
        // see #75442 thread. Issue with weaving order.
		ResolvedTypeMunger munger =
			new PerObjectInterfaceTypeMunger(inAspect, concreteEntry);
		inAspect.crosscuttingMembers.addTypeMunger(world.concreteTypeMunger(munger, inAspect));

        //ATAJ: add a munger to add the aspectOf(..) to the @AJ aspects
        if (inAspect.isAnnotationStyleAspect()) {
            inAspect.crosscuttingMembers.addLateTypeMunger(
                    inAspect.getWorld().makePerClauseAspect(inAspect, getKind())
            );
        }

        //ATAJ inline around advice support
        if (Ajc5MemberMaker.isAnnotationStyleAspect(inAspect)) {
            inAspect.crosscuttingMembers.addLateTypeMunger(new BcelAccessForInlineMunger(inAspect));
        }

		return ret;
	}

    public void write(DataOutputStream s) throws IOException {
    	PEROBJECT.write(s);
    	entry.write(s);
    	s.writeBoolean(isThis);
    	writeLocation(s);
    }
    
	public static PerClause readPerClause(VersionedDataInputStream s, ISourceContext context) throws IOException {
		PerClause ret = new PerObject(Pointcut.read(s, context), s.readBoolean());
		ret.readLocation(context, s);
		return ret;
	}
	
	public PerClause.Kind getKind() {
		return PEROBJECT;
	}

    public boolean isThis() {
        return isThis;
    }

	public String toString() {
		return "per" + (isThis ? "this" : "target") +
			"(" + entry + ")";
	}
	
	public String toDeclarationString() {
		return toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11140.java