error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14014.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14014.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14014.java
text:
```scala
b@@inding.isConstructor() ? Member.CONSTRUCTOR : Member.METHOD,

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


package org.aspectj.ajdt.internal.compiler.lookup;

import java.util.*;

import org.aspectj.ajdt.internal.compiler.ast.AstUtil;
import org.aspectj.ajdt.internal.core.builder.*;
import org.aspectj.ajdt.internal.core.builder.AjBuildManager;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.weaver.*;
import org.aspectj.weaver.patterns.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.util.CharOperation;

public class EclipseWorld extends World {
	public static boolean DEBUG = false;
	
	public AjBuildManager buildManager;
	private LookupEnvironment lookupEnvironment;
	
	private Map addedTypeBindings = new HashMap();
	
	public static EclipseWorld forLookupEnvironment(LookupEnvironment env) {
		AjLookupEnvironment aenv = (AjLookupEnvironment)env;
		return aenv.world;
	}
	
	public static EclipseWorld fromScopeLookupEnvironment(Scope scope) {
		return forLookupEnvironment(AstUtil.getCompilationUnitScope(scope).environment);
	}
	
	
	public EclipseWorld(LookupEnvironment lookupEnvironment, IMessageHandler handler) {
		this.lookupEnvironment = lookupEnvironment;
		setMessageHandler(handler);
	}
	
	public Advice concreteAdvice(AdviceKind kind, Pointcut p, Member m, int extraMods,
        int start, int end, ISourceContext context)
    {
    	return new EclipseAdvice(kind, p, m, extraMods, start, end, context);
    }

    public ConcreteTypeMunger concreteTypeMunger(
		ResolvedTypeMunger munger, ResolvedTypeX aspectType)
	{
		return null;
		//throw new RuntimeException("unimplemented");
	}

	protected ResolvedTypeX resolveObjectType(TypeX ty) {
		String n = ty.getName();
		//n = n.replace('$', '.');
		char[][] name = CharOperation.splitOn('.', n.toCharArray()); //ty.getName().toCharArray());
		ReferenceBinding ret = lookupEnvironment.getType(name);
		//System.out.println("name: " + ty.getName() + ", " + ret);
		if (ret == null || ret instanceof ProblemReferenceBinding) return ResolvedTypeX.MISSING;
		return new EclipseObjectType(ty.getSignature(), this, ret);
	}


	public ResolvedTypeX fromEclipse(ReferenceBinding binding) {
		if (binding == null) return ResolvedTypeX.MISSING;
		//??? this seems terribly inefficient
		//System.out.println("resolving: " + binding + ", name = " + getName(binding));
		ResolvedTypeX ret = resolve(fromBinding(binding));
		//System.out.println("      got: " + ret);
		return ret;
	}	
	
	public ResolvedTypeX[] fromEclipse(ReferenceBinding[] bindings) {
		if (bindings == null) {
			return ResolvedTypeX.NONE;
		}
		int len = bindings.length;
		ResolvedTypeX[] ret = new ResolvedTypeX[len];
		for (int i=0; i < len; i++) {
			ret[i] = fromEclipse(bindings[i]);
		}
		return ret;
	}	
	
	
	private static String getName(TypeBinding binding) {
		String packageName = new String(binding.qualifiedPackageName());
		String className = new String(binding.qualifiedSourceName()).replace('.', '$');
		if (packageName.length() > 0) {
			className = packageName + "." + className;
		}
		//XXX doesn't handle arrays correctly (or primitives?)
		return new String(className);
	}



	//??? going back and forth between strings and bindings is a waste of cycles
	public static TypeX fromBinding(TypeBinding binding) {
//		if (binding instanceof ReferenceBinding) {
//			return fromEclipse( (ReferenceBinding)binding);
//		} else {

		if (binding instanceof HelperInterfaceBinding) {
			return ((HelperInterfaceBinding)binding).getTypeX();
		}
	if (binding.qualifiedSourceName() == null) return ResolvedTypeX.MISSING;
			return TypeX.forName(getName(binding));
//		}
	}

	public static TypeX[] fromBindings(TypeBinding[] bindings) {
		int len = bindings.length;
		TypeX[] ret = new TypeX[len];
		for (int i=0; i<len; i++) {
			ret[i] = fromBinding(bindings[i]);
		}
		return ret;
	}

	public static AstNode astForLocation(IHasPosition location) {
		return new EmptyStatement(location.getStart(), location.getEnd());
	}

	public Collection getTypeMungers() {
		return crosscuttingMembersSet.getTypeMungers();
	}
	
	public static ResolvedMember makeResolvedMember(MethodBinding binding) {
		return new ResolvedMember(
			Member.METHOD,
			fromBinding(binding.declaringClass),
			binding.modifiers,
			fromBinding(binding.returnType),
			new String(binding.selector),
			fromBindings(binding.parameters));
		//XXX need to add checked exceptions
	}

	public static ResolvedMember makeResolvedMember(FieldBinding binding) {
		return new ResolvedMember(
			Member.FIELD,
			fromBinding(binding.declaringClass),
			binding.modifiers,
			fromBinding(binding.type),
			new String(binding.name),
			TypeX.NONE);
	}
	
	public TypeBinding makeTypeBinding(TypeX typeX) {
		//System.out.println("in: " + typeX.getSignature() + " have: " + addedTypeBindings);
		TypeBinding ret = (TypeBinding)addedTypeBindings.get(typeX);
		if (ret != null) return ret;
		
		if (typeX.isPrimitive()) {
			if (typeX == ResolvedTypeX.BOOLEAN) return BaseTypes.BooleanBinding;
			if (typeX == ResolvedTypeX.BYTE) return BaseTypes.ByteBinding;
			if (typeX == ResolvedTypeX.CHAR) return BaseTypes.CharBinding;
			if (typeX == ResolvedTypeX.DOUBLE) return BaseTypes.DoubleBinding;
			if (typeX == ResolvedTypeX.FLOAT) return BaseTypes.FloatBinding;
			if (typeX == ResolvedTypeX.INT) return BaseTypes.IntBinding;
			if (typeX == ResolvedTypeX.LONG) return BaseTypes.LongBinding;
			if (typeX == ResolvedTypeX.SHORT) return BaseTypes.ShortBinding;
			if (typeX == ResolvedTypeX.VOID) return BaseTypes.VoidBinding;
			throw new RuntimeException("weird primitive type " + typeX);
		} else if (typeX.isArray()) {
			int dim = 0;
			while (typeX.isArray()) {
				dim++;
				typeX = typeX.getComponentType();
			}
			return new ArrayBinding(makeTypeBinding(typeX), dim);
		} else {
			ResolvedTypeX rt = typeX.resolve(this);
			//System.out.println("typex: " + typeX + ", " + rt);
			if (rt == ResolvedTypeX.MISSING) {
				throw new RuntimeException("shouldn't be missing: " + typeX);
			}
			return ((EclipseObjectType)rt).getBinding();
		}
	}
	
	public TypeBinding[] makeTypeBindings(TypeX[] types) {
		int len = types.length;
		TypeBinding[] ret = new TypeBinding[len];
		
		for (int i = 0; i < len; i++) {
			ret[i] = makeTypeBinding(types[i]);
		}
		return ret;
	}

	
	public FieldBinding makeFieldBinding(ResolvedMember member) {
		return new FieldBinding(member.getName().toCharArray(),
				makeTypeBinding(member.getReturnType()),
				member.getModifiers(),
				(ReferenceBinding)makeTypeBinding(member.getDeclaringType()),
				Constant.NotAConstant);
	}


	public MethodBinding makeMethodBinding(ResolvedMember member) {
		if (member.getExceptions() != null && member.getExceptions().length > 0) {
			throw new RuntimeException("unimplemented");
		}
		return new MethodBinding(member.getModifiers(),
				member.getName().toCharArray(),
				makeTypeBinding(member.getReturnType()),
				makeTypeBindings(member.getParameterTypes()),
				new ReferenceBinding[0],
				(ReferenceBinding)makeTypeBinding(member.getDeclaringType()));
	}
	
	public MethodBinding makeMethodBindingForCall(Member member) {
		return new MethodBinding(member.getCallsiteModifiers(),
				member.getName().toCharArray(),
				makeTypeBinding(member.getReturnType()),
				makeTypeBindings(member.getParameterTypes()),
				new ReferenceBinding[0],
				(ReferenceBinding)makeTypeBinding(member.getDeclaringType()));
	}

	public void finishedCompilationUnit(CompilationUnitDeclaration unit) {
		if (buildManager.doGenerateModel()) {
			AsmBuilder.build(unit, buildManager.getStructureModel());
		}
	}


	public void addTypeBinding(TypeBinding binding) {
		addedTypeBindings.put(fromBinding(binding), binding);
	}

	public Shadow makeShadow(
		AstNode location,
		ReferenceContext context)
	{
		return EclipseShadow.makeShadow(this, location, context);
	}
	
	public Shadow makeShadow(
		ReferenceContext context)
	{
		return EclipseShadow.makeShadow(this, (AstNode)context, context);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14014.java