error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8101.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8101.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8101.java
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
import java.util.List;
import java.util.Set;

import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.BCException;
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
public class WithinAnnotationPointcut extends NameBindingPointcut {

	private AnnotationTypePattern annotationTypePattern;
	private ShadowMunger munger;
	
	/**
	 * 
	 */
	public WithinAnnotationPointcut(AnnotationTypePattern type) {
		super();
		this.annotationTypePattern = type;
	}
	
	public WithinAnnotationPointcut(AnnotationTypePattern type, ShadowMunger munger) {
	    this(type);
	    this.munger = munger;
	}

    public AnnotationTypePattern getAnnotationTypePattern() {
        return annotationTypePattern;
    }

	public Set couldMatchKinds() {
		return Shadow.ALL_SHADOW_KINDS;
	}
	
	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.Pointcut#fastMatch(org.aspectj.weaver.patterns.FastMatchInfo)
	 */
	public FuzzyBoolean fastMatch(FastMatchInfo info) {
	    return annotationTypePattern.fastMatches(info.getType());
	}

	public FuzzyBoolean fastMatch(Class targetType) {
		// TODO AMC
		return FuzzyBoolean.MAYBE;
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.Pointcut#match(org.aspectj.weaver.Shadow)
	 */
	protected FuzzyBoolean matchInternal(Shadow shadow) {
		ResolvedType enclosingType = shadow.getIWorld().resolve(shadow.getEnclosingType(),true);
		if (enclosingType == ResolvedType.MISSING) {
			IMessage msg = new Message(
			    WeaverMessages.format(WeaverMessages.CANT_FIND_TYPE_WITHINPCD,
			    		              shadow.getEnclosingType().getName()),
				shadow.getSourceLocation(),true,new ISourceLocation[]{getSourceLocation()});
			shadow.getIWorld().getMessageHandler().handleMessage(msg);
		}
		annotationTypePattern.resolve(shadow.getIWorld());
		return annotationTypePattern.matches(enclosingType);
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.Pointcut#resolveBindings(org.aspectj.weaver.patterns.IScope, org.aspectj.weaver.patterns.Bindings)
	 */
	protected void resolveBindings(IScope scope, Bindings bindings) {
		annotationTypePattern = (ExactAnnotationTypePattern) annotationTypePattern.resolveBindings(scope,bindings,true);
		// must be either a Var, or an annotation type pattern
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
		ExactAnnotationTypePattern newType = (ExactAnnotationTypePattern) annotationTypePattern.remapAdviceFormals(bindings);		
		Pointcut ret = new WithinAnnotationPointcut(newType, bindings.getEnclosingAdvice());
        ret.copyLocationFrom(this);
        return ret;
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.Pointcut#findResidue(org.aspectj.weaver.Shadow, org.aspectj.weaver.patterns.ExposedState)
	 */
	protected Test findResidueInternal(Shadow shadow, ExposedState state) {
		if (annotationTypePattern instanceof BindingAnnotationTypePattern) {
			BindingAnnotationTypePattern btp = (BindingAnnotationTypePattern)annotationTypePattern;
			UnresolvedType annotationType = btp.annotationType;
			Var var = shadow.getWithinAnnotationVar(annotationType);
			
			// This should not happen, we shouldn't have gotten this far 
			// if we weren't going to find the annotation
			if (var == null) 
				throw new BCException("Impossible! annotation=["+annotationType+
                        "]  shadow=["+shadow+" at "+shadow.getSourceLocation()+
						   "]    pointcut is at ["+getSourceLocation()+"]");
			
			// Check if we have already bound something to this formal
			if ((state.get(btp.getFormalIndex())!=null)  &&(lastMatchedShadowId == shadow.shadowId)) {
//				ISourceLocation pcdSloc = getSourceLocation(); 
//				ISourceLocation shadowSloc = shadow.getSourceLocation();
//				Message errorMessage = new Message(
//					"Cannot use @pointcut to match at this location and bind a formal to type '"+var.getType()+
//					"' - the formal is already bound to type '"+state.get(btp.getFormalIndex()).getType()+"'"+
//					".  The secondary source location points to the problematic binding.",
//					shadowSloc,true,new ISourceLocation[]{pcdSloc}); 
//				shadow.getIWorld().getMessageHandler().handleMessage(errorMessage);
				state.setErroneousVar(btp.getFormalIndex());
			}
			state.set(btp.getFormalIndex(),var);
		} 
		return Literal.TRUE;
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
		s.writeByte(Pointcut.ATWITHIN);
		annotationTypePattern.write(s);
		writeLocation(s);
	}

	public static Pointcut read(VersionedDataInputStream s, ISourceContext context) throws IOException {
		AnnotationTypePattern type = AnnotationTypePattern.read(s, context);
		WithinAnnotationPointcut ret = new WithinAnnotationPointcut((ExactAnnotationTypePattern)type);
		ret.readLocation(context, s);
		return ret;
	}
	
	/* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof WithinAnnotationPointcut)) return false;
        WithinAnnotationPointcut other = (WithinAnnotationPointcut) obj;
        return other.annotationTypePattern.equals(this.annotationTypePattern);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return 17 + 19*annotationTypePattern.hashCode();
    }
    
	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("@within(");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8101.java