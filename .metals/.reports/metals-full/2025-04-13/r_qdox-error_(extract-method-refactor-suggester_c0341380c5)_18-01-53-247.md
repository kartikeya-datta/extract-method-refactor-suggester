error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/779.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/779.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/779.java
text:
```scala
i@@f (rArgType.isMissing()) {

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
import java.util.Map;
import java.util.Set;

import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
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
public class ArgsAnnotationPointcut extends NameBindingPointcut {

	private AnnotationPatternList arguments;
	private String declarationText;

	/**
	 * 
	 */
	public ArgsAnnotationPointcut(AnnotationPatternList arguments) {
		super();
		this.arguments = arguments;
		this.pointcutKind = ATARGS;
		buildDeclarationText();
	}

    public AnnotationPatternList getArguments() {
        return arguments;
    }

	public Set couldMatchKinds() {
		return Shadow.ALL_SHADOW_KINDS;  // empty args() matches jps with no args
	}
	
	public Pointcut parameterizeWith(Map typeVariableMap) {
		ArgsAnnotationPointcut ret = new ArgsAnnotationPointcut(arguments.parameterizeWith(typeVariableMap));
		ret.copyLocationFrom(this);
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.Pointcut#fastMatch(org.aspectj.weaver.patterns.FastMatchInfo)
	 */
	public FuzzyBoolean fastMatch(FastMatchInfo info) {
		return FuzzyBoolean.MAYBE;
	}
	
	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.Pointcut#match(org.aspectj.weaver.Shadow)
	 */
	protected FuzzyBoolean matchInternal(Shadow shadow) {
		arguments.resolve(shadow.getIWorld());
		FuzzyBoolean ret =
			arguments.matches(shadow.getIWorld().resolve(shadow.getArgTypes()));
		return ret;
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.Pointcut#resolveBindings(org.aspectj.weaver.patterns.IScope, org.aspectj.weaver.patterns.Bindings)
	 */
	protected void resolveBindings(IScope scope, Bindings bindings) {
		if (!scope.getWorld().isInJava5Mode()) {
			scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.ATARGS_ONLY_SUPPORTED_AT_JAVA5_LEVEL),
					getSourceLocation()));
			return;
		}
		arguments.resolveBindings(scope, bindings, true);
		if (arguments.ellipsisCount > 1) {
			scope.message(IMessage.ERROR, this,
					"uses more than one .. in args (compiler limitation)");
		}
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.Pointcut#concretize1(org.aspectj.weaver.ResolvedType, org.aspectj.weaver.IntMap)
	 */
	protected Pointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {
		if (isDeclare(bindings.getEnclosingAdvice())) {
			  // Enforce rule about which designators are supported in declare
			  inAspect.getWorld().showMessage(IMessage.ERROR,
			  		WeaverMessages.format(WeaverMessages.ARGS_IN_DECLARE),
					bindings.getEnclosingAdvice().getSourceLocation(), null);
			  return Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
		}
		AnnotationPatternList list = arguments.resolveReferences(bindings);
		Pointcut ret = new ArgsAnnotationPointcut(list);
		ret.copyLocationFrom(this);
		return ret;
	}

	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.Pointcut#findResidue(org.aspectj.weaver.Shadow, org.aspectj.weaver.patterns.ExposedState)
	 */
	protected Test findResidueInternal(Shadow shadow, ExposedState state) {
		int len = shadow.getArgCount();
	
		// do some quick length tests first
		int numArgsMatchedByEllipsis = (len + arguments.ellipsisCount) - arguments.size();
		if (numArgsMatchedByEllipsis < 0) return Literal.FALSE;  // should never happen
		if ((numArgsMatchedByEllipsis > 0) && (arguments.ellipsisCount == 0)) {
			return Literal.FALSE; // should never happen
		}
		// now work through the args and the patterns, skipping at ellipsis
    	Test ret = Literal.TRUE;
    	int argsIndex = 0;
    	for (int i = 0; i < arguments.size(); i++) {
			if (arguments.get(i) == AnnotationTypePattern.ELLIPSIS) {
				// match ellipsisMatchCount args
				argsIndex += numArgsMatchedByEllipsis;
			} else if (arguments.get(i) == AnnotationTypePattern.ANY) {
				argsIndex++;
			} else {
				// match the argument type at argsIndex with the ExactAnnotationTypePattern
				// we know it is exact because nothing else is allowed in args
				ExactAnnotationTypePattern ap = (ExactAnnotationTypePattern)arguments.get(i);
				UnresolvedType argType = shadow.getArgType(argsIndex);
				ResolvedType rArgType = argType.resolve(shadow.getIWorld());
				if (rArgType == ResolvedType.MISSING) {
					shadow.getIWorld().getLint().cantFindType.signal(
							new String[] {WeaverMessages.format(WeaverMessages.CANT_FIND_TYPE_ARG_TYPE,argType.getName())},
							shadow.getSourceLocation(),
							new ISourceLocation[]{getSourceLocation()}
							);
//	                  IMessage msg = new Message(
//	                    WeaverMessages.format(WeaverMessages.CANT_FIND_TYPE_ARG_TYPE,argType.getName()),
//	                    "",IMessage.ERROR,shadow.getSourceLocation(),null,new ISourceLocation[]{getSourceLocation()});
	            }

				ResolvedType rAnnType = ap.getAnnotationType().resolve(shadow.getIWorld());
				if (ap instanceof BindingAnnotationTypePattern) {
					BindingAnnotationTypePattern btp = (BindingAnnotationTypePattern)ap;
					Var annvar = shadow.getArgAnnotationVar(argsIndex,rAnnType);
					state.set(btp.getFormalIndex(),annvar);
				}
				if (!ap.matches(rArgType).alwaysTrue()) {
					// we need a test...
					ret = Test.makeAnd(ret,
								Test.makeHasAnnotation(
										shadow.getArgVar(argsIndex),
										rAnnType));
				}			
				argsIndex++;
			}
		}   	
    	return ret;
	}

	
	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.NameBindingPointcut#getBindingAnnotationTypePatterns()
	 */
	public List getBindingAnnotationTypePatterns() {
		List l = new ArrayList();
		AnnotationTypePattern[] pats = arguments.getAnnotationPatterns();
		for (int i = 0; i < pats.length; i++) {
			if (pats[i] instanceof BindingAnnotationTypePattern) {
				l.add(pats[i]);
			}
		}
		return l;
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
		s.writeByte(Pointcut.ATARGS);
		arguments.write(s);
		writeLocation(s);
	}
	
	public static Pointcut read(VersionedDataInputStream s, ISourceContext context) throws IOException {
		AnnotationPatternList annotationPatternList = AnnotationPatternList.read(s,context);
		ArgsAnnotationPointcut ret = new ArgsAnnotationPointcut(annotationPatternList);
		ret.readLocation(context, s);
		return ret;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof ArgsAnnotationPointcut)) return false;
		ArgsAnnotationPointcut other = (ArgsAnnotationPointcut) obj;
		return other.arguments.equals(arguments);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return 17 + 37*arguments.hashCode();
	}
	
	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    private void buildDeclarationText() {
        StringBuffer buf = new StringBuffer("@args");
        buf.append(arguments.toString());
        this.declarationText = buf.toString();
    }
    
    public String toString() { return this.declarationText; }

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/779.java