error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15571.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15571.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15571.java
text:
```scala
i@@f (inAspect.isAbstract()) return; // we don't do mungers for abstract aspects

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


package org.aspectj.weaver;

import java.util.*;

import org.aspectj.weaver.patterns.*;
import org.aspectj.weaver.patterns.PerClause;
import org.aspectj.bridge.*;


/**
 * This holds on to all members that have an invasive effect outside of
 * there own compilation unit.  These members need to be all gathered up and in
 * a world before any weaving can take place.
 * 
 * They are also important in the compilation process and need to be gathered
 * up before the inter-type declaration weaving stage (unsurprisingly).
 * 
 * All members are concrete.
 */

public class CrosscuttingMembers {
	private ResolvedTypeX inAspect;
	private World world;
	
	private PerClause perClause;
	
	private List shadowMungers = new ArrayList(4);
	private List typeMungers = new ArrayList(4);
	
	private List declareParents = new ArrayList(4);
	private List declareSofts = new ArrayList(0);
	private List declareDominates = new ArrayList(4);
	
	
	public CrosscuttingMembers(ResolvedTypeX inAspect) {
		this.inAspect = inAspect;
		this.world = inAspect.getWorld();
	}
	
//	public void addConcreteShadowMungers(Collection c) {
//		shadowMungers.addAll(c);
//	}
	
	public void addConcreteShadowMunger(ShadowMunger m) {
		// assert m is concrete
		shadowMungers.add(m);
	}

	public void addShadowMungers(Collection c) {
		for (Iterator i = c.iterator(); i.hasNext(); ) {
			addShadowMunger( (ShadowMunger)i.next() );
		}
	}
	
	private void addShadowMunger(ShadowMunger m) {
		//if (inAspect.isAbstract()) return; // we don't do mungers for abstract aspects
		addConcreteShadowMunger(m.concretize(inAspect, world, perClause));
	}
	
	public void addTypeMungers(Collection c) {
		typeMungers.addAll(c);
	}
	
	public void addTypeMunger(ConcreteTypeMunger m) {
		if (m == null) return; //???
		typeMungers.add(m);
	}
	
	public void addDeclares(Collection c) {
		for (Iterator i = c.iterator(); i.hasNext(); ) {
			addDeclare( (Declare)i.next() );
		}
	}
		
		
		
	public void addDeclare(Declare declare) {
		// this is not extensible, oh well
		if (declare instanceof DeclareErrorOrWarning) {
			ShadowMunger m = new Checker((DeclareErrorOrWarning)declare);
			addShadowMunger(m);
		} else if (declare instanceof DeclareDominates) {
			declareDominates.add(declare);
		} else if (declare instanceof DeclareParents) {
			DeclareParents dp = (DeclareParents)declare;
			exposeTypes(dp.getParents().getExactTypes());
			declareParents.add(dp);
		} else if (declare instanceof DeclareSoft) {
			DeclareSoft d = (DeclareSoft)declare;
			Pointcut concretePointcut = d.getPointcut().concretize(inAspect, 0);
			declareSofts.add(new DeclareSoft(d.getException(), concretePointcut));
			ShadowMunger m = Advice.makeSoftener(world, concretePointcut, d.getException());
			addConcreteShadowMunger(m);
		} else {
			throw new RuntimeException("unimplemented");
		}
	}
	
	public void exposeTypes(Collection typesToExpose) {
		for (Iterator i = typesToExpose.iterator(); i.hasNext(); ) {
			exposeType((TypeX)i.next());
		}
	}
	
	public void exposeType(TypeX typeToExpose) {
		if (typeToExpose == ResolvedTypeX.MISSING) return;
		
		ResolvedMember member = new ResolvedMember(
			Member.STATIC_INITIALIZATION, typeToExpose, 0, ResolvedTypeX.VOID, "", TypeX.NONE);
		addTypeMunger(world.concreteTypeMunger(
			new PrivilegedAccessMunger(member), inAspect));
	}
	
	public void addPrivilegedAccesses(Collection accessedMembers) {
		for (Iterator i = accessedMembers.iterator(); i.hasNext(); ) {
			addPrivilegedAccess( (ResolvedMember)i.next() );
		}
	}

	private void addPrivilegedAccess(ResolvedMember member) {
		//System.err.println("add priv access: " + member);
		addTypeMunger(world.concreteTypeMunger(new PrivilegedAccessMunger(member), inAspect));
	}


	
	public Collection getCflowEntries() {
		ArrayList ret = new ArrayList();
		for (Iterator i = shadowMungers.iterator(); i.hasNext(); ) {
			ShadowMunger m = (ShadowMunger)i.next();
			if (m instanceof Advice) {
				Advice a = (Advice)m;
				if (a.getKind().isCflow()) {
					ret.add(a);
				}
			}
		}
		return ret;
	}

	public boolean replaceWith(CrosscuttingMembers other) {
		boolean changed = false;
		if (!perClause.equals(other.perClause)) {
			changed = true;
			perClause = other.perClause;
		}
		
		//XXX all of the below should be set equality rather than list equality
		if (!shadowMungers.equals(other.shadowMungers)) {
			changed = true;
			shadowMungers = other.shadowMungers;
		}
		
		if (!typeMungers.equals(other.typeMungers)) {
			changed = true;
			typeMungers = other.typeMungers;
		}
		
		if (!declareDominates.equals(other.declareDominates)) {
			changed = true;
			declareDominates = other.declareDominates;
		}
		
		if (!declareParents.equals(other.declareParents)) {
			changed = true;
			declareParents = other.declareParents;
		}
		
		if (!declareSofts.equals(other.declareSofts)) {
			changed = true;
			declareSofts = other.declareSofts;
		}
		
		return changed;
	}

	public PerClause getPerClause() {
		return perClause;
	}

	public void setPerClause(PerClause perClause) {
		this.perClause = perClause.concretize(inAspect);
	}

	public List getDeclareDominates() {
		return declareDominates;
	}

	public List getDeclareParents() {
		return declareParents;
	}

	public List getDeclareSofts() {
		return declareSofts;
	}

	public List getShadowMungers() {
		return shadowMungers;
	}

	public List getTypeMungers() {
		return typeMungers;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15571.java