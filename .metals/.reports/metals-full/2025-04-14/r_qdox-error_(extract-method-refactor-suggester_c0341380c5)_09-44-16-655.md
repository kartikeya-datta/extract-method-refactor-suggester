error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8095.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8095.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8095.java
text:
```scala
public P@@ointcut concretize1(ResolvedType inAspect, ResolvedType declaringType,  IntMap bindings) {

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
import java.util.Map;
import java.util.Set;

import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.Checker;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.ShadowMunger;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Literal;
import org.aspectj.weaver.ast.Test;

public class KindedPointcut extends Pointcut {
	Shadow.Kind kind;
	private SignaturePattern signature;
	private Set matchKinds;
    
    private ShadowMunger munger = null; // only set after concretization

    public KindedPointcut(
        Shadow.Kind kind,
        SignaturePattern signature) {
        this.kind = kind;
        this.signature = signature;
        this.pointcutKind = KINDED;
        this.matchKinds = new HashSet();
        matchKinds.add(kind);
    }
    public KindedPointcut(
        Shadow.Kind kind,
        SignaturePattern signature,
        ShadowMunger munger) {
        this(kind, signature);
        this.munger = munger;
    }

    public SignaturePattern getSignature() {
        return signature;
    }

    /* (non-Javadoc)
	 * @see org.aspectj.weaver.patterns.Pointcut#couldMatchKinds()
	 */
	public Set couldMatchKinds() {
		return matchKinds;
	}
	
	public boolean couldEverMatchSameJoinPointsAs(KindedPointcut other) {
		if (this.kind != other.kind) return false;
		String myName = signature.getName().maybeGetSimpleName();
		String yourName = other.signature.getName().maybeGetSimpleName();
		if (myName != null && yourName != null) {
			if ( !myName.equals(yourName)) {
				return false;
			}
		}
		if (signature.getParameterTypes().ellipsisCount == 0) {
			if (other.signature.getParameterTypes().ellipsisCount == 0) {
				if (signature.getParameterTypes().getTypePatterns().length !=
					other.signature.getParameterTypes().getTypePatterns().length) {
					return false;
				}
			}
		}
		return true;
	}
	
    public FuzzyBoolean fastMatch(FastMatchInfo info) {
    	if (info.getKind() != null) {
			if (info.getKind() != kind) return FuzzyBoolean.NO;
    	}

		return FuzzyBoolean.MAYBE;
	}	
	
	public FuzzyBoolean fastMatch(Class targetType) {
		return FuzzyBoolean.fromBoolean(signature.couldMatch(targetType));
	}

	
	protected FuzzyBoolean matchInternal(Shadow shadow) {
		if (shadow.getKind() != kind) return FuzzyBoolean.NO;

		if (!signature.matches(shadow.getMatchingSignature(), shadow.getIWorld(),this.kind == Shadow.MethodCall)){

            if(kind == Shadow.MethodCall) {
                warnOnConfusingSig(shadow);
                //warnOnBridgeMethod(shadow);
            }
            return FuzzyBoolean.NO; 
        }

		return FuzzyBoolean.YES;
	}
	
	
//	private void warnOnBridgeMethod(Shadow shadow) {
//		if (shadow.getIWorld().getLint().noJoinpointsForBridgeMethods.isEnabled()) {
//			ResolvedMember rm = shadow.getSignature().resolve(shadow.getIWorld());
//			if (rm!=null) {
//             	int shadowModifiers = rm.getModifiers(); //shadow.getSignature().getModifiers(shadow.getIWorld());
//			    if (ResolvedType.hasBridgeModifier(shadowModifiers)) {
//				  shadow.getIWorld().getLint().noJoinpointsForBridgeMethods.signal(new String[]{},getSourceLocation(),
//						new ISourceLocation[]{shadow.getSourceLocation()});
//			    }
//            }
//        }
//	}
	
	public FuzzyBoolean match(JoinPoint.StaticPart jpsp) {
		if (jpsp.getKind().equals(kind.getName())) {
			if (signature.matches(jpsp)) {
				return FuzzyBoolean.YES;
			}
		}
		return FuzzyBoolean.NO;
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
	public FuzzyBoolean matchesStatically(String joinpointKind,
			java.lang.reflect.Member member, Class thisClass,
			Class targetClass, java.lang.reflect.Member withinCode) {
		if (joinpointKind.equals(kind.getName()))  {
			return FuzzyBoolean.fromBoolean(signature.matches(targetClass,member));			
		}
		return FuzzyBoolean.NO;
	}
	
	private void warnOnConfusingSig(Shadow shadow) {
		// Don't do all this processing if we don't need to !
		if (!shadow.getIWorld().getLint().unmatchedSuperTypeInCall.isEnabled()) return;
		
        // no warnings for declare error/warning
        if (munger instanceof Checker) return;
        
        World world = shadow.getIWorld();
        
		// warning never needed if the declaring type is any
		UnresolvedType exactDeclaringType = signature.getDeclaringType().getExactType();
        
		ResolvedType shadowDeclaringType =
			shadow.getSignature().getDeclaringType().resolve(world);
        
		if (signature.getDeclaringType().isStar()
 exactDeclaringType== ResolvedType.MISSING)
			return;

        // warning not needed if match type couldn't ever be the declaring type
		if (!shadowDeclaringType.isAssignableFrom(exactDeclaringType.resolve(world))) {
            return;
		}

		// if the method in the declaring type is *not* visible to the
		// exact declaring type then warning not needed.
		int shadowModifiers = shadow.getSignature().resolve(world).getModifiers();
		if (!ResolvedType
			.isVisible(
				shadowModifiers,
				shadowDeclaringType,
				exactDeclaringType.resolve(world))) {
			return;
		}
		
		if (!signature.getReturnType().matchesStatically(shadow.getSignature().getReturnType().resolve(world))) {
			// Covariance issue...
			// The reason we didn't match is that the type pattern for the pointcut (Car) doesn't match the
			// return type for the specific declaration at the shadow. (FastCar Sub.getCar())
			// XXX Put out another XLINT in this case?
			return;
		}
		// PR60015 - Don't report the warning if the declaring type is object and 'this' is an interface
		if (exactDeclaringType.resolve(world).isInterface() && shadowDeclaringType.equals(world.resolve("java.lang.Object"))) {
			return;
		}

		SignaturePattern nonConfusingPattern =
			new SignaturePattern(
				signature.getKind(),
				signature.getModifiers(),
				signature.getReturnType(),
				TypePattern.ANY,
				signature.getName(), 
				signature.getParameterTypes(),
				signature.getThrowsPattern(),
				signature.getAnnotationPattern());

		if (nonConfusingPattern
			.matches(shadow.getSignature(), shadow.getIWorld(),true)) {
                shadow.getIWorld().getLint().unmatchedSuperTypeInCall.signal(
                    new String[] {
                        shadow.getSignature().getDeclaringType().toString(),
                        signature.getDeclaringType().toString()
                    },
                    this.getSourceLocation(),
                    new ISourceLocation[] {shadow.getSourceLocation()} );               
		}
	}

	public boolean equals(Object other) {
		if (!(other instanceof KindedPointcut)) return false;
		KindedPointcut o = (KindedPointcut)other;
		return o.kind == this.kind && o.signature.equals(this.signature);
	}
    
    public int hashCode() {
        int result = 17;
        result = 37*result + kind.hashCode();
        result = 37*result + signature.hashCode();
        return result;
    }
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(kind.getSimpleName());
		buf.append("(");
		buf.append(signature.toString());
		buf.append(")");
		return buf.toString();
	}
	
	
	public void postRead(ResolvedType enclosingType) {
		signature.postRead(enclosingType);
	}

	public void write(DataOutputStream s) throws IOException {
		s.writeByte(Pointcut.KINDED);
		kind.write(s);
		signature.write(s);
		writeLocation(s);
	}
	
	public static Pointcut read(VersionedDataInputStream s, ISourceContext context) throws IOException {
		Shadow.Kind kind = Shadow.Kind.read(s);
		SignaturePattern sig = SignaturePattern.read(s, context);
		KindedPointcut ret = new KindedPointcut(kind, sig);
		ret.readLocation(context, s);
		return ret;
	}

	// XXX note: there is no namebinding in any kinded pointcut.
	// still might want to do something for better error messages
	// We want to do something here to make sure we don't sidestep the parameter
	// list in capturing type identifiers.
	public void resolveBindings(IScope scope, Bindings bindings) {
		if (kind == Shadow.Initialization) {
//			scope.getMessageHandler().handleMessage(
//				MessageUtil.error(
//					"initialization unimplemented in 1.1beta1",
//					this.getSourceLocation()));
		}
		signature = signature.resolveBindings(scope, bindings);
		
		
		if (kind == Shadow.ConstructorExecution) { 		// Bug fix 60936
		  if (signature.getDeclaringType() != null) {
			World world = scope.getWorld();
			UnresolvedType exactType = signature.getDeclaringType().getExactType();
			if (signature.getKind() == Member.CONSTRUCTOR &&
				!exactType.equals(ResolvedType.MISSING) &&
				exactType.resolve(world).isInterface() &&
				!signature.getDeclaringType().isIncludeSubtypes()) {
					world.getLint().noInterfaceCtorJoinpoint.signal(exactType.toString(), getSourceLocation());
				}
		  }
		}
		
		// no parameterized types
		if (kind == Shadow.StaticInitialization) {
			HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor 
				visitor = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
			signature.getDeclaringType().traverse(visitor, null);
			if (visitor.wellHasItThen/*?*/()) {
				scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.NO_STATIC_INIT_JPS_FOR_PARAMETERIZED_TYPES),
						getSourceLocation()));
			}
		}
		
		// no parameterized types in declaring type position
		if ((kind == Shadow.FieldGet) || (kind == Shadow.FieldSet)) {
			HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor 
				visitor = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
			signature.getDeclaringType().traverse(visitor, null);
			if (visitor.wellHasItThen/*?*/()) {
				scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.GET_AND_SET_DONT_SUPPORT_DEC_TYPE_PARAMETERS),
						getSourceLocation()));
			}	
			
			// fields can't have a void type!
			UnresolvedType returnType = signature.getReturnType().getExactType();
			if (returnType == ResolvedType.VOID) {
				scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.FIELDS_CANT_HAVE_VOID_TYPE),
						getSourceLocation()));				
			}
		}
		
		// no join points for initialization and preinitialization of parameterized types
		// no throwable parameterized types
		if ((kind == Shadow.Initialization) || (kind == Shadow.PreInitialization)) {
			HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor 
			visitor = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
			signature.getDeclaringType().traverse(visitor, null);
			if (visitor.wellHasItThen/*?*/()) {
				scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.NO_INIT_JPS_FOR_PARAMETERIZED_TYPES),
						getSourceLocation()));
			}						
			
			visitor = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
			signature.getThrowsPattern().traverse(visitor, null);
			if (visitor.wellHasItThen/*?*/()) {
				scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.NO_GENERIC_THROWABLES),
						getSourceLocation()));
			}									
		}
		
		// no parameterized types in declaring type position
		// no throwable parameterized types
		if ((kind == Shadow.MethodExecution) || (kind == Shadow.ConstructorExecution)) {
			HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor 
			visitor = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
			signature.getDeclaringType().traverse(visitor, null);
			if (visitor.wellHasItThen/*?*/()) {
				scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.EXECUTION_DOESNT_SUPPORT_PARAMETERIZED_DECLARING_TYPES),
						getSourceLocation()));
			}						
			
			visitor = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
			signature.getThrowsPattern().traverse(visitor, null);
			if (visitor.wellHasItThen/*?*/()) {
				scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.NO_GENERIC_THROWABLES),
						getSourceLocation()));
			}									
		}
		
		// no parameterized types in declaring type position
		// no throwable parameterized types
		if ((kind == Shadow.MethodCall) || (kind == Shadow.ConstructorCall)) {
			HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor 
			visitor = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
			signature.getDeclaringType().traverse(visitor, null);
			if (visitor.wellHasItThen/*?*/()) {
				scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.CALL_DOESNT_SUPPORT_PARAMETERIZED_DECLARING_TYPES),
						getSourceLocation()));
			}						
			
			visitor = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
			signature.getThrowsPattern().traverse(visitor, null);
			if (visitor.wellHasItThen/*?*/()) {
				scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.NO_GENERIC_THROWABLES),
						getSourceLocation()));
			}									
		}
	}
	
	public void resolveBindingsFromRTTI() {
		signature = signature.resolveBindingsFromRTTI();
	}
	
	protected Test findResidueInternal(Shadow shadow, ExposedState state) {
		return match(shadow).alwaysTrue() ? Literal.TRUE : Literal.FALSE;
	}
	
	public Pointcut concretize1(ResolvedType inAspect, IntMap bindings) {
		Pointcut ret = new KindedPointcut(kind, signature, bindings.getEnclosingAdvice());
        ret.copyLocationFrom(this);
        return ret;
	}
	
	public Pointcut parameterizeWith(Map typeVariableMap) {
		Pointcut ret = new KindedPointcut(kind, signature.parameterizeWith(typeVariableMap), munger );
        ret.copyLocationFrom(this);
        return ret;
	}

	public Shadow.Kind getKind() {
		return kind;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8095.java