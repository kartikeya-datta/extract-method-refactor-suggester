error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3605.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3605.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3605.java
text:
```scala
protected A@@jAttribute.AdviceAttribute attribute; // the pointcut field is ignored

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

import org.aspectj.bridge.*;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.weaver.patterns.*;

public abstract class Advice extends ShadowMunger {

	AjAttribute.AdviceAttribute attribute; // the pointcut field is ignored

    protected AdviceKind kind; // alias of attribute.getKind()
    protected Member signature;
    
    // not necessarily declaring aspect, this is a semantics change from 1.0
    protected ResolvedTypeX concreteAspect; // null until after concretize
    
    protected List innerCflowEntries = Collections.EMPTY_LIST;  // just for cflow*Entry kinds
    protected int nFreeVars; // just for cflow*Entry kinds
    
    protected TypePattern exceptionType; // just for Softener kind

    public static Advice makeCflowEntry(World world, Pointcut entry, boolean isBelow, Member stackField, int nFreeVars, List innerCflowEntries) {
    	Advice ret = world.concreteAdvice(isBelow ? AdviceKind.CflowBelowEntry : AdviceKind.CflowEntry,
    	      entry, stackField, 0, entry);
    	      //0);
    	ret.innerCflowEntries = innerCflowEntries;
    	ret.nFreeVars = nFreeVars;
    	return ret;
    }

    public static Advice makePerCflowEntry(World world, Pointcut entry, boolean isBelow, 
    								Member stackField, ResolvedTypeX inAspect, List innerCflowEntries)
    {
    	Advice ret = world.concreteAdvice(isBelow ? AdviceKind.PerCflowBelowEntry : AdviceKind.PerCflowEntry,
    	      entry, stackField, 0, entry);
    	ret.innerCflowEntries = innerCflowEntries;
    	ret.concreteAspect = inAspect;
    	return ret;
    }

    public static Advice makePerObjectEntry(World world, Pointcut entry, boolean isThis, 
    								ResolvedTypeX inAspect)
    {
    	Advice ret = world.concreteAdvice(isThis ? AdviceKind.PerThisEntry : AdviceKind.PerTargetEntry,
    	      entry, null, 0, entry);
  
    	ret.concreteAspect = inAspect;
    	return ret;
    }
    
    public static Advice makeSoftener(World world, Pointcut entry, TypePattern exceptionType) {
    	Advice ret = world.concreteAdvice(AdviceKind.Softener,
    	      entry, null, 0, entry);  
  
    	ret.exceptionType = exceptionType;
    	//System.out.println("made ret: " + ret + " with " + exceptionType);
    	return ret;
    }
    	

    public Advice(AjAttribute.AdviceAttribute attribute, Pointcut pointcut, Member signature)
    {
    	super(pointcut, attribute.getStart(), attribute.getEnd(), attribute.getSourceContext());
		this.attribute = attribute;
		this.kind = attribute.getKind(); // alias
		this.signature = signature;
    }    

	
	public boolean match(Shadow shadow, World world) {
		if (super.match(shadow, world)) {
			if (shadow.getKind() == Shadow.ExceptionHandler) {
				if (kind.isAfter() || kind == AdviceKind.Around) {
					world.showMessage(IMessage.WARNING,
	    				"Only before advice is supported on handler join points (compiler limitation)", 
	    				getSourceLocation(), shadow.getSourceLocation());
					return false;
				}
			}
			
			
    		if (hasExtraParameter() && kind == AdviceKind.AfterReturning) {
    			return getExtraParameterType().isConvertableFrom(shadow.getReturnType(), world);
    		} else if (kind == AdviceKind.PerTargetEntry) {
    			return shadow.hasTarget();
    		} else if (kind == AdviceKind.PerThisEntry) {
    			return shadow.hasThis();
    		} else if (kind == AdviceKind.Around) {
    			if (shadow.getKind() == Shadow.PreInitialization) {
	    			world.showMessage(IMessage.ERROR,
	    				"around on pre-initialization not supported (compiler limitation)", 
	    				getSourceLocation(), shadow.getSourceLocation());
					return false;
    			} else if (shadow.getKind() == Shadow.Initialization) {
	    			world.showMessage(IMessage.ERROR,
	    				"around on initialization not supported (compiler limitation)", 
	    				getSourceLocation(), shadow.getSourceLocation());
					return false;
    			} else {
    				if (!getSignature().getReturnType().isConvertableFrom(shadow.getReturnType(), world)) {
    					//System.err.println(this + ", " + sourceContext + ", " + start);
    					
    					world.getMessageHandler().handleMessage(
	    				MessageUtil.error("incompatible return type applying to " + shadow,
	    					getSourceLocation()));
	    				//XXX need a crosscutting error message here
	    				return false;
    				}
    			}
    		}
    		return true; 
    	} else {
    		return false;
    	}
	}


	// ----

	public AdviceKind getKind() {
		return kind;
	}

	public Member getSignature() {
		return signature;
	}
	
	public boolean hasExtraParameter() {
		return (getExtraParameterFlags() & ExtraArgument) != 0;
	}

	protected int getExtraParameterFlags() {
		return attribute.getExtraParameterFlags();
	}

	protected int getExtraParameterCount() {
		return countOnes(getExtraParameterFlags() & ParameterMask);
	}
	
	public static int countOnes(int bits) {
		int ret = 0;
		while (bits != 0) {
			if ((bits & 1) != 0) ret += 1;
			bits = bits >> 1;
		}
		return ret;
	}
	
	public int getBaseParameterCount() {
		return getSignature().getParameterTypes().length - getExtraParameterCount();
	}

	public TypeX getExtraParameterType() {
		if (!hasExtraParameter()) return ResolvedTypeX.MISSING;
		return signature.getParameterTypes()[getBaseParameterCount()];
	}
	
	public TypeX getDeclaringAspect() {
		return signature.getDeclaringType();
	}

	protected String extraParametersToString() {
		if (getExtraParameterFlags() == 0) {
			return "";
		} else {
			return "(extraFlags: " + getExtraParameterFlags() + ")";
		}
    }

	public Pointcut getPointcut() {
		return pointcut;
	}

	// ----
 
    /** @param fromType is guaranteed to be a non-abstract aspect
     *  @param perClause has been concretized at a higher level
     */
    public ShadowMunger concretize(ResolvedTypeX fromType, World world, PerClause clause) {
    	// assert !fromType.isAbstract();
        Pointcut p = pointcut.concretize(fromType, signature.getArity(), this);
        if (clause != null) {
        	p = new AndPointcut(clause, p);
        	p.state = Pointcut.CONCRETE;
        }
        
		Advice munger = world.concreteAdvice(attribute, p, signature);
		munger.concreteAspect = fromType;
    	//System.err.println("concretizing here " + p + " with clause " + clause);
        return munger;
    }

	// ---- from object

	public String toString() {
		return "("
			+ getKind()
            + extraParametersToString() 
			+ ": "
			+ pointcut
			+ "->"
			+ signature
			+ ")";
	}
    public boolean equals(Object other) {
        if (! (other instanceof Advice)) return false;
        Advice o = (Advice) other;
        return o.attribute.equals(attribute) 
        	&& o.pointcut.equals(pointcut) 
        	&& o.signature.equals(signature);
    }
    private volatile int hashCode = 0;
    public int hashCode() {
        if (hashCode == 0) {
            int result = 17;
            result = 37*result + kind.hashCode();
            result = 37*result + pointcut.hashCode();
            if (signature != null) result = 37*result + signature.hashCode();
            hashCode = result;
        }
        return hashCode;
    }
 
    // ---- fields


	public static final int ExtraArgument = 1;
	public static final int ThisJoinPoint = 2;
	public static final int ThisJoinPointStaticPart = 4;
	public static final int ThisEnclosingJoinPointStaticPart = 8;
	public static final int ParameterMask = 0xf;
	
	public static final int CanInline = 0x40;


	// for testing only	
	public void setLexicalPosition(int lexicalPosition) {
		start = lexicalPosition;
	}

	public ResolvedTypeX getConcreteAspect() {
		return concreteAspect;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3605.java