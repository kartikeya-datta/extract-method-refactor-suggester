error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8099.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8099.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8099.java
text:
```scala
protected P@@ointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {

/* *******************************************************************
 * Copyright (c) 2004 IBM Corporation.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * ******************************************************************/
package org.aspectj.weaver.patterns;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.ShadowMunger;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.ast.Literal;
import org.aspectj.weaver.ast.Test;
import org.aspectj.weaver.ast.Var;

/**
 * @author colyer
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ThisOrTargetAnnotationPointcut extends NameBindingPointcut {

	private boolean isThis;
	private boolean alreadyWarnedAboutDEoW = false;
	private ExactAnnotationTypePattern annotationTypePattern;
	private ShadowMunger munger;
	private static final Set thisKindSet = new HashSet(Shadow.ALL_SHADOW_KINDS);
	private static final Set targetKindSet = new HashSet(Shadow.ALL_SHADOW_KINDS);
	static {
		for (Iterator iter = Shadow.ALL_SHADOW_KINDS.iterator(); iter.hasNext();) {
			Shadow.Kind kind = (Shadow.Kind) iter.next();
			if (kind.neverHasThis()) thisKindSet.remove(kind);
			if (kind.neverHasTarget()) targetKindSet.remove(kind);
		}
	}
	
	/**
	 * 
	 */
	public ThisOrTargetAnnotationPointcut(boolean isThis, ExactAnnotationTypePattern type) {
		super();
		this.isThis = isThis;
		this.annotationTypePattern = type;
	}

	public ThisOrTargetAnnotationPointcut(boolean isThis, ExactAnnotationTypePattern type, ShadowMunger munger) {
	    this(isThis,type);
	    this.munger = munger;
	}

    public ExactAnnotationTypePattern getAnnotationTypePattern() {
        return annotationTypePattern;
    }

	public Set couldMatchKinds() {
		return isThis ? thisKindSet : targetKindSet;
	}
	
	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.Pointcut#fastMatch(org.aspectj.weaver.patterns.FastMatchInfo)
	 */
	public FuzzyBoolean fastMatch(FastMatchInfo info) {
		return FuzzyBoolean.MAYBE;
	}
	
	public FuzzyBoolean fastMatch(Class targetType) {
		return FuzzyBoolean.MAYBE;
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.Pointcut#match(org.aspectj.weaver.Shadow)
	 */
	protected FuzzyBoolean matchInternal(Shadow shadow) {
		if (!couldMatch(shadow)) return FuzzyBoolean.NO;
	    ResolvedType toMatchAgainst = 
	        (isThis ? shadow.getThisType() : shadow.getTargetType() ).resolve(shadow.getIWorld());
	    annotationTypePattern.resolve(shadow.getIWorld());
	    if (annotationTypePattern.matchesRuntimeType(toMatchAgainst).alwaysTrue()) {
	    	return FuzzyBoolean.YES;
	    } else {
	    	// a subtype may match at runtime
	    	return FuzzyBoolean.MAYBE;
	    }
	}

	public boolean isThis() { return isThis; }
	
	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.Pointcut#resolveBindings(org.aspectj.weaver.patterns.IScope, org.aspectj.weaver.patterns.Bindings)
	 */
	protected void resolveBindings(IScope scope, Bindings bindings) {
		annotationTypePattern = (ExactAnnotationTypePattern) annotationTypePattern.resolveBindings(scope,bindings,true);
		// must be either a Var, or an annotation type pattern
		// if annotationType does not have runtime retention, this is an error
		if (annotationTypePattern.annotationType == null) {
			// it's a formal with a binding error
			return;
		}
		ResolvedType rAnnotationType = (ResolvedType) annotationTypePattern.annotationType;
		if (!(rAnnotationType.isAnnotationWithRuntimeRetention())) {
		    IMessage m = MessageUtil.error(
					WeaverMessages.format(WeaverMessages.BINDING_NON_RUNTIME_RETENTION_ANNOTATION,rAnnotationType.getName()),
					getSourceLocation());
			scope.getMessageHandler().handleMessage(m);
		}
		
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.Pointcut#resolveBindingsFromRTTI()
	 */
	protected void resolveBindingsFromRTTI() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.Pointcut#concretize1(org.aspectj.weaver.ResolvedType, org.aspectj.weaver.IntMap)
	 */
	protected Pointcut concretize1(ResolvedType inAspect, IntMap bindings) {
		if (isDeclare(bindings.getEnclosingAdvice())) {
			  // Enforce rule about which designators are supported in declare
			  if (!alreadyWarnedAboutDEoW) {
				  inAspect.getWorld().showMessage(IMessage.ERROR,
				  		WeaverMessages.format(WeaverMessages.THIS_OR_TARGET_IN_DECLARE,isThis?"this":"target"),
						bindings.getEnclosingAdvice().getSourceLocation(), null);
				  alreadyWarnedAboutDEoW = true;
			  }
			  return Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
		}

		ExactAnnotationTypePattern newType = (ExactAnnotationTypePattern) annotationTypePattern.remapAdviceFormals(bindings);		
		ThisOrTargetAnnotationPointcut ret = 
			new ThisOrTargetAnnotationPointcut(isThis, newType, bindings.getEnclosingAdvice());
		ret.alreadyWarnedAboutDEoW = alreadyWarnedAboutDEoW;
        ret.copyLocationFrom(this);
        return ret;
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.Pointcut#findResidue(org.aspectj.weaver.Shadow, org.aspectj.weaver.patterns.ExposedState)
	 */
	/**
	 * The guard here is going to be the hasAnnotation() test - if it gets through (which we cannot determine until runtime) then
	 * we must have a TypeAnnotationAccessVar in place - this means we must *always* have one in place.
	 */
	protected Test findResidueInternal(Shadow shadow, ExposedState state) {
	    if (!couldMatch(shadow)) return Literal.FALSE;
	    boolean alwaysMatches = match(shadow).alwaysTrue();
	    Var var = isThis ? shadow.getThisVar() : shadow.getTargetVar();
	    Var annVar = null;
	    
	    // Are annotations being bound?
	    UnresolvedType annotationType = annotationTypePattern.annotationType;
		if (annotationTypePattern instanceof BindingAnnotationTypePattern) {
			BindingAnnotationTypePattern btp = (BindingAnnotationTypePattern)annotationTypePattern;
			annotationType = btp.annotationType;
			
			annVar = isThis ? shadow.getThisAnnotationVar(annotationType) :
				shadow.getTargetAnnotationVar(annotationType);
			if (annVar == null)
				throw new RuntimeException("Impossible!");
			// Check if we have already bound something to this formal
			if ((state.get(btp.getFormalIndex())!=null) &&(lastMatchedShadowId == shadow.shadowId)) {
//				ISourceLocation pcdSloc = getSourceLocation(); 
//				ISourceLocation shadowSloc = shadow.getSourceLocation();
//				Message errorMessage = new Message(
//					"Cannot use @pointcut to match at this location and bind a formal to type '"+annVar.getType()+
//					"' - the formal is already bound to type '"+state.get(btp.getFormalIndex()).getType()+"'"+
//					".  The secondary source location points to the problematic binding.",
//					shadowSloc,true,new ISourceLocation[]{pcdSloc}); 
//				shadow.getIWorld().getMessageHandler().handleMessage(errorMessage);
				state.setErroneousVar(btp.getFormalIndex());
			}
			state.set(btp.getFormalIndex(),annVar);
		}

		if (alwaysMatches && (annVar == null)) {//change check to verify if its the 'generic' annVar that is being used
		    return Literal.TRUE;
		} else {
	        ResolvedType rType = annotationType.resolve(shadow.getIWorld());
	        return Test.makeHasAnnotation(var,rType);
		}
	}

	
	private boolean couldMatch(Shadow shadow) {
		return isThis ? shadow.hasThis() : shadow.hasTarget();
	}
	
	
	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.NameBindingPointcut#getBindingAnnotationTypePatterns()
	 */
	public List getBindingAnnotationTypePatterns() {
		if (annotationTypePattern instanceof BindingAnnotationTypePattern) {
			List l = new ArrayList();
			l.add(annotationTypePattern);
			return l;
		} else return Collections.EMPTY_LIST;
	}
	
	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.NameBindingPointcut#getBindingTypePatterns()
	 */
	public List getBindingTypePatterns() {
		return Collections.EMPTY_LIST;
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.PatternNode#write(java.io.DataOutputStream)
	 */
	public void write(DataOutputStream s) throws IOException {
	    s.writeByte(Pointcut.ATTHIS_OR_TARGET);
	    s.writeBoolean(isThis);
		annotationTypePattern.write(s);
		writeLocation(s);
	}
	
	public static Pointcut read(VersionedDataInputStream s, ISourceContext context) throws IOException {
	    boolean isThis = s.readBoolean();
		AnnotationTypePattern type = AnnotationTypePattern.read(s, context);
		ThisOrTargetAnnotationPointcut ret = new ThisOrTargetAnnotationPointcut(isThis,(ExactAnnotationTypePattern)type);
		ret.readLocation(context, s);
		return ret;
	}
	
	/* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof ThisOrTargetAnnotationPointcut)) return false;
        ThisOrTargetAnnotationPointcut other = (ThisOrTargetAnnotationPointcut) obj;
        return ( other.annotationTypePattern.equals(this.annotationTypePattern) &&
                 (other.isThis == this.isThis) );
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return 17 + 37*annotationTypePattern.hashCode() + (isThis ? 49 : 13);
    }
    
	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(isThis ? "@this(" : "@target(");
		String annPatt = annotationTypePattern.toString();
		buf.append(annPatt.startsWith("@") ? annPatt.substring(1) : annPatt);
		buf.append(")");
		return buf.toString();   
	}

    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8099.java