error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/343.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/343.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/343.java
text:
```scala
public F@@uzzyBoolean fastMatch(FastMatchInfo type) {

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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aspectj.util.FileUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.Advice;
import org.aspectj.weaver.CrosscuttingMembers;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.NameMangler;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.aspectj.weaver.ResolvedTypeX;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.TypeX;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Test;


public class CflowPointcut extends Pointcut {
	private Pointcut entry;
	private boolean isBelow;
	private int[] freeVars;
	
	/**
	 * Used to indicate that we're in the context of a cflow when concretizing if's
	 * 
	 * Will be removed or replaced with something better when we handle this
	 * as a non-error
	 */
	public static final ResolvedPointcutDefinition CFLOW_MARKER = 
		new ResolvedPointcutDefinition(null, 0, null, TypeX.NONE, Pointcut.makeMatchesNothing(Pointcut.RESOLVED));

	
	public CflowPointcut(Pointcut entry, boolean isBelow, int[] freeVars) {
		this.entry = entry;
		this.isBelow = isBelow;
		this.freeVars = freeVars;
	}
    
    public FuzzyBoolean fastMatch(ResolvedTypeX type) {
		return FuzzyBoolean.MAYBE;
	}
    
	public FuzzyBoolean match(Shadow shadow) {
		//??? this is not maximally efficient
		return FuzzyBoolean.MAYBE;
	}

	public void write(DataOutputStream s) throws IOException {
		s.writeByte(Pointcut.CFLOW);
		entry.write(s);
		s.writeBoolean(isBelow);
		FileUtil.writeIntArray(freeVars, s);
		writeLocation(s);
	}
	public static Pointcut read(DataInputStream s, ISourceContext context) throws IOException {

		CflowPointcut ret = new CflowPointcut(Pointcut.read(s, context), s.readBoolean(), FileUtil.readIntArray(s));
		ret.readLocation(context, s);
		return ret;
	}

	public void resolveBindings(IScope scope, Bindings bindings) {
		if (bindings == null) {
			entry.resolveBindings(scope, null);
			entry.state = RESOLVED;
			freeVars = new int[0];
		} else {
			//??? for if's sake we might need to be more careful here
			Bindings entryBindings = new Bindings(bindings.size());
			
			entry.resolveBindings(scope, entryBindings);
			entry.state = RESOLVED;
			
			freeVars = entryBindings.getUsedFormals();
			
			bindings.mergeIn(entryBindings, scope);
		}
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof CflowPointcut)) return false;
		CflowPointcut o = (CflowPointcut)other;
		return o.entry.equals(this.entry) && o.isBelow == this.isBelow;
	}
    public int hashCode() {
        int result = 17;
        result = 37*result + entry.hashCode();
        result = 37*result + (isBelow ? 0 : 1);
        return result;
    }
	public String toString() {
		return "cflow" + (isBelow ? "below" : "") + "(" + entry + ")";
	}

	public Test findResidue(Shadow shadow, ExposedState state) {
		throw new RuntimeException("unimplemented");
	}
	
	
	public Pointcut concretize1(ResolvedTypeX inAspect, IntMap bindings) {
		//make this remap from formal positions to arrayIndices
		IntMap entryBindings = new IntMap();
		for (int i=0, len=freeVars.length; i < len; i++) {
			int freeVar = freeVars[i];
			//int formalIndex = bindings.get(freeVar);
			entryBindings.put(freeVar, i);
		}
		entryBindings.copyContext(bindings);
		//System.out.println(this + " bindings: " + entryBindings);
		
		World world = inAspect.getWorld();
		
		Pointcut concreteEntry;
		
		ResolvedTypeX concreteAspect = bindings.getConcreteAspect();
		
		CrosscuttingMembers xcut = concreteAspect.crosscuttingMembers;		
		Collection previousCflowEntries = xcut.getCflowEntries();
		
		entryBindings.pushEnclosingDefinition(CFLOW_MARKER);
		try {
			concreteEntry = entry.concretize(inAspect, entryBindings);
		} finally {
			entryBindings.popEnclosingDefinitition();
		}

		List innerCflowEntries = new ArrayList(xcut.getCflowEntries());
		innerCflowEntries.removeAll(previousCflowEntries);

		
		ResolvedMember cflowField = new ResolvedMember(
			Member.FIELD, concreteAspect, Modifier.STATIC | Modifier.PUBLIC | Modifier.FINAL,
					NameMangler.cflowStack(xcut), 
					TypeX.forName(NameMangler.CFLOW_STACK_TYPE).getSignature());
		//System.out.println("adding field to: " + inAspect + " field " + cflowField);
					
		// add field and initializer to inAspect
		//XXX and then that info above needs to be mapped down here to help with
		//XXX getting the exposed state right
		concreteAspect.crosscuttingMembers.addConcreteShadowMunger(
				Advice.makeCflowEntry(world, concreteEntry, isBelow, cflowField, freeVars.length, innerCflowEntries));
		
		concreteAspect.crosscuttingMembers.addTypeMunger(
			world.makeCflowStackFieldAdder(cflowField));
			
			
		List slots = new ArrayList();
		for (int i=0, len=freeVars.length; i < len; i++) {
			int freeVar = freeVars[i];
			
			// we don't need to keep state that isn't actually exposed to advice
			//??? this means that we will store some state that we won't actually use, optimize this later
			if (!bindings.hasKey(freeVar)) continue; 
			
			int formalIndex = bindings.get(freeVar);
			ResolvedTypeX formalType =
				bindings.getAdviceSignature().getParameterTypes()[formalIndex].resolve(world);
			
			ConcreteCflowPointcut.Slot slot = 
				new ConcreteCflowPointcut.Slot(formalIndex, formalType, i);
			slots.add(slot);
		}
		
		return new ConcreteCflowPointcut(cflowField, slots);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/343.java