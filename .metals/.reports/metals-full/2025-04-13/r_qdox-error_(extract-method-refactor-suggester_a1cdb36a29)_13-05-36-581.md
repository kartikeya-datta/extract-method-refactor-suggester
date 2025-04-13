error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/785.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/785.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/785.java
text:
```scala
i@@f (enclosingType.isMissing()) {

/* *******************************************************************
 * Copyright (c) 2005 IBM
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Andy Clement     initial implementation 
 * ******************************************************************/

package org.aspectj.weaver.patterns;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.Advice;
import org.aspectj.weaver.AjcMemberMaker;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.Member;
//import org.aspectj.weaver.PerTypeWithinTargetTypeMunger;
import org.aspectj.weaver.PerTypeWithinTargetTypeMunger;
import org.aspectj.weaver.ResolvedTypeMunger;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.World;
import org.aspectj.weaver.bcel.BcelAccessForInlineMunger;
import org.aspectj.weaver.ast.Expr;
import org.aspectj.weaver.ast.Literal;
import org.aspectj.weaver.ast.Test;


// PTWIMPL Represents a parsed pertypewithin()
public class PerTypeWithin extends PerClause {

	private TypePattern typePattern;
	
	// Any shadow could be considered within a pertypewithin() type pattern
	private static final Set kindSet = new HashSet(Shadow.ALL_SHADOW_KINDS);
	
	public TypePattern getTypePattern() {
		return typePattern;
	}
	
	public PerTypeWithin(TypePattern p) {
		this.typePattern = p;
	}

	public Object accept(PatternNodeVisitor visitor, Object data) {
		return visitor.visit(this,data);
	}
	
	public Set couldMatchKinds() {
		return kindSet;
	}
	
	public Pointcut parameterizeWith(Map typeVariableMap) {
		PerTypeWithin ret = new PerTypeWithin(typePattern.parameterizeWith(typeVariableMap));
		ret.copyLocationFrom(this);
		return ret;
	}
	
	// -----
	public FuzzyBoolean fastMatch(FastMatchInfo info) {
		if (typePattern.annotationPattern instanceof AnyAnnotationTypePattern) {
			return isWithinType(info.getType());
		}
		return FuzzyBoolean.MAYBE;
	}
		
    protected FuzzyBoolean matchInternal(Shadow shadow) {
    	ResolvedType enclosingType = shadow.getIWorld().resolve(shadow.getEnclosingType(),true);
    	if (enclosingType == ResolvedType.MISSING) {
    		//PTWIMPL ?? Add a proper message
    		IMessage msg = new Message(
    				"Cant find type pertypewithin matching...",
					shadow.getSourceLocation(),true,new ISourceLocation[]{getSourceLocation()});
    		shadow.getIWorld().getMessageHandler().handleMessage(msg);
    	}
    	
    	// See pr106554 - we can't put advice calls in an interface when the advice is defined
    	// in a pertypewithin aspect - the JPs only exist in the static initializer and can't 
    	// call the localAspectOf() method.
    	if (enclosingType.isInterface()) return FuzzyBoolean.NO;
    	
    	typePattern.resolve(shadow.getIWorld());
    	return isWithinType(enclosingType);
    }

    public void resolveBindings(IScope scope, Bindings bindings) {
    	typePattern = typePattern.resolveBindings(scope, bindings, false, false);
    }
    
    protected Test findResidueInternal(Shadow shadow, ExposedState state) {
    	Member ptwField = AjcMemberMaker.perTypeWithinField(shadow.getEnclosingType(),inAspect);
    	
    	Expr myInstance =
    		Expr.makeCallExpr(AjcMemberMaker.perTypeWithinLocalAspectOf(shadow.getEnclosingType(),inAspect/*shadow.getEnclosingType()*/),
    				Expr.NONE,inAspect);
    	state.setAspectInstance(myInstance);
    	
    	// this worked at one point
    	//Expr myInstance = Expr.makeFieldGet(ptwField,shadow.getEnclosingType().resolve(shadow.getIWorld()));//inAspect);
    	//state.setAspectInstance(myInstance);
    	
    	
//   	return Test.makeFieldGetCall(ptwField,null,Expr.NONE);
    	// cflowField, cflowCounterIsValidMethod, Expr.NONE
    	
    	// This is what is in the perObject variant of this ...
//    	Expr myInstance =
//    		Expr.makeCallExpr(AjcMemberMaker.perTypeWithinAspectOfMethod(inAspect),
//    							new Expr[] {getVar(shadow)}, inAspect);
//    	state.setAspectInstance(myInstance);
//    	return Test.makeCall(AjcMemberMaker.perTypeWithinHasAspectMethod(inAspect), 
//    			new Expr[] { getVar(shadow) });
//    	

    	
    	 return match(shadow).alwaysTrue()?Literal.TRUE:Literal.FALSE;
    }
    

	public PerClause concretize(ResolvedType inAspect) {
		PerTypeWithin ret = new PerTypeWithin(typePattern);
		ret.copyLocationFrom(this);
		ret.inAspect = inAspect;
		if (inAspect.isAbstract()) return ret;
		
		
		World world = inAspect.getWorld();
		
		SignaturePattern sigpat = new SignaturePattern(
				Member.STATIC_INITIALIZATION,
				ModifiersPattern.ANY,
				TypePattern.ANY,
				TypePattern.ANY,//typePattern,
				NamePattern.ANY,
				TypePatternList.ANY,
				ThrowsPattern.ANY,
				AnnotationTypePattern.ANY
				);
		
		Pointcut staticInitStar = new KindedPointcut(Shadow.StaticInitialization,sigpat);
		Pointcut withinTp= new WithinPointcut(typePattern);
		Pointcut andPcut = new AndPointcut(staticInitStar,withinTp);
		// We want the pointcut to be 'staticinitialization(*) && within(<typepattern>' -
		// we *cannot* shortcut this to staticinitialization(<typepattern>) because it
		// doesnt mean the same thing.

		// This munger will initialize the aspect instance field in the matched type
		inAspect.crosscuttingMembers.addConcreteShadowMunger(Advice.makePerTypeWithinEntry(world, andPcut, inAspect));
		
		ResolvedTypeMunger munger = new PerTypeWithinTargetTypeMunger(inAspect, ret);
		inAspect.crosscuttingMembers.addTypeMunger(world.concreteTypeMunger(munger, inAspect));

        //ATAJ: add a munger to add the aspectOf(..) to the @AJ aspects
        if (inAspect.isAnnotationStyleAspect() && !inAspect.isAbstract()) {
            inAspect.crosscuttingMembers.addLateTypeMunger(
                    inAspect.getWorld().makePerClauseAspect(inAspect, getKind())
            );
        }

        //ATAJ inline around advice support - don't use a late munger to allow around inling for itself
        if (inAspect.isAnnotationStyleAspect() && !inAspect.getWorld().isXnoInline()) {
            inAspect.crosscuttingMembers.addTypeMunger(new BcelAccessForInlineMunger(inAspect));
        }

		return ret;
		
	}

    public void write(DataOutputStream s) throws IOException {
    	PERTYPEWITHIN.write(s);
    	typePattern.write(s);
    	writeLocation(s);
    }
    
	public static PerClause readPerClause(VersionedDataInputStream s, ISourceContext context) throws IOException {
		PerClause ret = new PerTypeWithin(TypePattern.read(s, context));
		ret.readLocation(context, s);
		return ret;
	}
	
	public PerClause.Kind getKind() {
		return PERTYPEWITHIN;
	}
	
	public String toString() {
		return "pertypewithin("+typePattern+")";
	}
	
	public String toDeclarationString() {
		return toString();
	}
	
	private FuzzyBoolean isWithinType(ResolvedType type) {
		while (type != null) {
			if (typePattern.matchesStatically(type)) {
				return FuzzyBoolean.YES;
			}
			type = type.getDeclaringType();
		}
		return FuzzyBoolean.NO;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/785.java