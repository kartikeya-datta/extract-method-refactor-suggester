error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8104.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8104.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8104.java
text:
```scala
public P@@ointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {

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
import java.lang.reflect.Member;
import java.util.HashSet;
import java.util.Set;

import org.aspectj.bridge.MessageUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.reflect.Factory;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.ast.Literal;
import org.aspectj.weaver.ast.Test;

public class WithincodePointcut extends Pointcut {
	private SignaturePattern signature;
    private static final Set matchedShadowKinds = new HashSet();
    static {
    	matchedShadowKinds.addAll(Shadow.ALL_SHADOW_KINDS);
    	for (int i = 0; i < Shadow.SHADOW_KINDS.length; i++) {
			if (Shadow.SHADOW_KINDS[i].isEnclosingKind()) 
				matchedShadowKinds.remove(Shadow.SHADOW_KINDS[i]);
		}
    	// these next two are needed for inlining of field initializers
    	matchedShadowKinds.add(Shadow.ConstructorExecution);
    	matchedShadowKinds.add(Shadow.Initialization);
    }
	
	public WithincodePointcut(SignaturePattern signature) {
		this.signature = signature;
		this.pointcutKind = WITHINCODE;
	}

    public SignaturePattern getSignature() {
        return signature;
    }

	public Set couldMatchKinds() {
		return matchedShadowKinds;
	}
	
	public FuzzyBoolean fastMatch(FastMatchInfo type) {
		return FuzzyBoolean.MAYBE;
	}
	
	public FuzzyBoolean fastMatch(Class targetType) {
		return FuzzyBoolean.MAYBE;
	}

    
	protected FuzzyBoolean matchInternal(Shadow shadow) {
		//This will not match code in local or anonymous classes as if
		//they were withincode of the outer signature
		return FuzzyBoolean.fromBoolean(
			signature.matches(shadow.getEnclosingCodeSignature(), shadow.getIWorld(), false));
	}

	public FuzzyBoolean match(JoinPoint jp, JoinPoint.StaticPart encJP) {
		return FuzzyBoolean.fromBoolean(signature.matches(encJP));
	}
	
	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.Pointcut#matchesDynamically(java.lang.Object, java.lang.Object, java.lang.Object[])
	 */
	public boolean matchesDynamically(Object thisObject, Object targetObject,
			Object[] args) {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.Pointcut#matchesStatically(java.lang.String, java.lang.reflect.Member, java.lang.Class, java.lang.Class, java.lang.reflect.Member)
	 */
	public FuzzyBoolean matchesStatically(String joinpointKind, Member member,
			Class thisClass, Class targetClass, Member withinCode) {
		if (withinCode == null) return FuzzyBoolean.NO;
		return FuzzyBoolean.fromBoolean(signature.matches(Factory.makeEncSJP(withinCode)));
	}
	
	public void write(DataOutputStream s) throws IOException {
		s.writeByte(Pointcut.WITHINCODE);
		signature.write(s);
		writeLocation(s);
	}
	
	public static Pointcut read(VersionedDataInputStream s, ISourceContext context) throws IOException {
		WithincodePointcut ret = new WithincodePointcut(SignaturePattern.read(s, context));
		ret.readLocation(context, s);
		return ret;
	}

	public void resolveBindings(IScope scope, Bindings bindings) {
		signature = signature.resolveBindings(scope, bindings);
		
		// look for inappropriate use of parameterized types and tell user...
		HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor 
			visitor = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
		signature.getDeclaringType().traverse(visitor, null);
		if (visitor.wellHasItThen/*?*/()) {
			scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.WITHINCODE_DOESNT_SUPPORT_PARAMETERIZED_DECLARING_TYPES),
					getSourceLocation()));
		}						
		
		visitor = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
		signature.getThrowsPattern().traverse(visitor, null);
		if (visitor.wellHasItThen/*?*/()) {
			scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.NO_GENERIC_THROWABLES),
					getSourceLocation()));
		}					
	}
	
	public void resolveBindingsFromRTTI() {
		signature = signature.resolveBindingsFromRTTI();
	}

	public void postRead(ResolvedType enclosingType) {
		signature.postRead(enclosingType);
	}

	public boolean equals(Object other) {
		if (!(other instanceof WithincodePointcut)) return false;
		WithincodePointcut o = (WithincodePointcut)other;
		return o.signature.equals(this.signature);
	}
    public int hashCode() {
        int result = 43;
        result = 37*result + signature.hashCode();
        return result;
    }

	public String toString() {
		return "withincode(" + signature + ")";
	}

	protected Test findResidueInternal(Shadow shadow, ExposedState state) {
		return match(shadow).alwaysTrue() ? Literal.TRUE : Literal.FALSE;
	}
	
	
	public Pointcut concretize1(ResolvedType inAspect, IntMap bindings) {
		Pointcut ret = new WithincodePointcut(signature);
		ret.copyLocationFrom(this);
		return ret;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8104.java