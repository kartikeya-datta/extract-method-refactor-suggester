error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2777.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2777.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2777.java
text:
```scala
t@@hrow new AbortCompilation(compilationResult);

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


package org.aspectj.ajdt.internal.compiler.ast;

import java.lang.reflect.Modifier;

import org.aspectj.ajdt.internal.compiler.lookup.EclipseFactory;
import org.aspectj.ajdt.internal.compiler.lookup.EclipseTypeMunger;
import org.aspectj.weaver.*;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.codegen.CodeStream;
//import org.eclipse.jdt.internal.compiler.flow.FlowContext;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.flow.InitializationFlowContext;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;

/**
 * An inter-type method declaration.
 *
 * @author Jim Hugunin
 */
public class InterTypeMethodDeclaration extends InterTypeDeclaration {
	public InterTypeMethodDeclaration(CompilationResult result, TypeReference onType) {
		super(result, onType);
	}

	public void parseStatements(Parser parser, CompilationUnitDeclaration unit) {
		if (ignoreFurtherInvestigation)
			return;
		if (!Modifier.isAbstract(declaredModifiers)) {
			parser.parse(this, unit);
		}
	}
	

	public void analyseCode(
		ClassScope currentScope,
		InitializationFlowContext flowContext,
		FlowInfo flowInfo)
	{
		if (Modifier.isAbstract(declaredModifiers)) return;
		
		super.analyseCode(currentScope, flowContext, flowInfo);
	}
	
	public void resolve(ClassScope upperScope) {
		if (munger == null) ignoreFurtherInvestigation = true;
		if (ignoreFurtherInvestigation) return;
		
		if (!Modifier.isStatic(declaredModifiers)) {
			this.arguments = AstUtil.insert(
				AstUtil.makeFinalArgument("ajc$this_".toCharArray(), onTypeBinding),
				this.arguments);
			binding.parameters  = AstUtil.insert(onTypeBinding, binding.parameters);
		}
			
		super.resolve(upperScope);
	}
	public void resolveStatements() {
        if ((modifiers & AccSemicolonBody) != 0) {
            if ((declaredModifiers & AccAbstract) == 0)
                scope.problemReporter().methodNeedingAbstractModifier(this);
        } else {
            // the method HAS a body --> abstract native modifiers are forbiden
            if (((declaredModifiers & AccAbstract) != 0))
                scope.problemReporter().methodNeedingNoBody(this);
        }        
        
        
		if (!Modifier.isAbstract(declaredModifiers)) super.resolveStatements();
		if (Modifier.isStatic(declaredModifiers)) {
			// Check the target for ITD is not an interface
			if (onTypeBinding.isInterface()) {
				scope.problemReporter().signalError(sourceStart, sourceEnd,
					"methods in interfaces cannot be declared static");
			}
		}
	}
	
	

	public EclipseTypeMunger build(ClassScope classScope) {
		EclipseFactory world = EclipseFactory.fromScopeLookupEnvironment(classScope);
		
		resolveOnType(classScope);
		if (ignoreFurtherInvestigation) return null;
		
		binding = classScope.referenceContext.binding.resolveTypesFor(binding);
		if (binding == null) {
			// if binding is null, we failed to find a type used in the method params, this error
			// has already been reported. 
			throw new AbortCompilation();
		}
		ResolvedMember sig = new ResolvedMember(Member.METHOD, EclipseFactory.fromBinding(onTypeBinding),
			declaredModifiers, EclipseFactory.fromBinding(binding.returnType), new String(declaredSelector),
			EclipseFactory.fromBindings(binding.parameters),
			world.fromEclipse(binding.thrownExceptions));
		
		NewMethodTypeMunger myMunger = new NewMethodTypeMunger(sig, null);
		setMunger(myMunger);
		ResolvedTypeX aspectType = world.fromEclipse(classScope.referenceContext.binding);
		ResolvedMember me =
			myMunger.getDispatchMethod(aspectType);
		this.selector = binding.selector = me.getName().toCharArray();
		
		return new EclipseTypeMunger(world, myMunger, aspectType, this);
	}
	
	
	private AjAttribute makeAttribute() {
		return new AjAttribute.TypeMunger(munger);
	}
	
	
	public void generateCode(ClassScope classScope, ClassFile classFile) {
		if (ignoreFurtherInvestigation) {
			//System.err.println("no code for " + this);
			return;
		}
		
		classFile.extraAttributes.add(new EclipseAttributeAdapter(makeAttribute()));
		
		if (!Modifier.isAbstract(declaredModifiers)) {
			super.generateCode(classScope, classFile);
		}
		
		generateDispatchMethod(classScope, classFile);
	}
	
	public void generateDispatchMethod(ClassScope classScope, ClassFile classFile) {
		EclipseFactory world = EclipseFactory.fromScopeLookupEnvironment(classScope);
		
		TypeX aspectType = EclipseFactory.fromBinding(classScope.referenceContext.binding);
		ResolvedMember signature = munger.getSignature();
		
		ResolvedMember dispatchMember = 
			AjcMemberMaker.interMethodDispatcher(signature, aspectType);
		MethodBinding dispatchBinding = world.makeMethodBinding(dispatchMember);
		MethodBinding introducedMethod = 
			world.makeMethodBinding(AjcMemberMaker.interMethod(signature, aspectType, onTypeBinding.isInterface()));
		
		classFile.generateMethodInfoHeader(dispatchBinding);
		int methodAttributeOffset = classFile.contentsOffset;
		int attributeNumber = classFile.generateMethodInfoAttribute(dispatchBinding, 
				makeEffectiveSignatureAttribute(signature, Shadow.MethodCall, false));
		int codeAttributeOffset = classFile.contentsOffset;
		classFile.generateCodeAttributeHeader();
		CodeStream codeStream = classFile.codeStream;
		codeStream.reset(this, classFile);
		
		codeStream.initializeMaxLocals(dispatchBinding);
		
		MethodBinding methodBinding = introducedMethod;
		TypeBinding[] parameters = methodBinding.parameters;
		int length = parameters.length;
		int resolvedPosition;
		if (methodBinding.isStatic())
			resolvedPosition = 0;
		else {
			codeStream.aload_0();
			resolvedPosition = 1;
		}
		for (int i = 0; i < length; i++) {
			codeStream.load(parameters[i], resolvedPosition);
			if ((parameters[i] == DoubleBinding) || (parameters[i] == LongBinding))
				resolvedPosition += 2;
			else
				resolvedPosition++;
		}
//		TypeBinding type;
		if (methodBinding.isStatic())
			codeStream.invokestatic(methodBinding);
		else {
			if (methodBinding.declaringClass.isInterface()){
				codeStream.invokeinterface(methodBinding);
			} else {
				codeStream.invokevirtual(methodBinding);
			}
		}
		AstUtil.generateReturn(dispatchBinding.returnType, codeStream);

		classFile.completeCodeAttribute(codeAttributeOffset);
		attributeNumber++;
		classFile.completeMethodInfo(methodAttributeOffset, attributeNumber);
	}

	
	protected Shadow.Kind getShadowKindForBody() {
		return Shadow.MethodExecution;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2777.java