error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10561.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10561.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10561.java
text:
```scala
A@@rrayBinding objectArrayBinding = scope.createArrayType(scope.getJavaLangObject(), 1);

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

import org.aspectj.ajdt.internal.compiler.lookup.*;
import org.aspectj.weaver.*;
import org.aspectj.org.eclipse.jdt.internal.compiler.ClassFile;
import org.aspectj.org.eclipse.jdt.internal.compiler.CompilationResult;
import org.aspectj.org.eclipse.jdt.internal.compiler.ast.*;
import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.*;
import org.aspectj.org.eclipse.jdt.internal.compiler.parser.Parser;

/**
 * An inter-type constructor declaration.
 * 
 * This will generate two implementation methods in the aspect, the main one for the body
 * of the constructor, and an additional <code>preMethod</code> for the code that 
 * runs before the super constructor is called.
 * 
 * @author Jim Hugunin
 */
public class InterTypeConstructorDeclaration extends InterTypeDeclaration {	
	private MethodDeclaration preMethod;
	private ExplicitConstructorCall explicitConstructorCall = null;
	
	public InterTypeConstructorDeclaration(CompilationResult result, TypeReference onType) {
		super(result, onType);
	}
	
	public void parseStatements(Parser parser, CompilationUnitDeclaration unit) {
		if (ignoreFurtherInvestigation)
			return;
	    parser.parse(this, unit); 
	}

	public void resolve(ClassScope upperScope) {
		if (munger == null || binding == null) ignoreFurtherInvestigation = true;
		if (ignoreFurtherInvestigation) return;

		explicitConstructorCall = null;
		if (statements != null && statements.length > 0 && 
			statements[0] instanceof ExplicitConstructorCall)
		{
			explicitConstructorCall = (ExplicitConstructorCall) statements[0];
			statements = AstUtil.remove(0, statements);
		}
		
		preMethod = makePreMethod(upperScope, explicitConstructorCall);
		
		binding.parameters  = AstUtil.insert(onTypeBinding, binding.parameters);
		this.arguments = AstUtil.insert(
			AstUtil.makeFinalArgument("ajc$this_".toCharArray(), onTypeBinding),
			this.arguments);
			
		super.resolve(upperScope);
	}

	private MethodDeclaration makePreMethod(ClassScope scope, 
											ExplicitConstructorCall explicitConstructorCall)
	{
		EclipseFactory world = EclipseFactory.fromScopeLookupEnvironment(scope);
		
		TypeX aspectTypeX = EclipseFactory.fromBinding(binding.declaringClass);
		TypeX targetTypeX = EclipseFactory.fromBinding(onTypeBinding);
		
		ArrayBinding objectArrayBinding = scope.createArray(scope.getJavaLangObject(), 1);
		
		
		MethodDeclaration pre = new MethodDeclaration(compilationResult);
		pre.modifiers = AccPublic | AccStatic;
		pre.returnType = AstUtil.makeTypeReference(objectArrayBinding);
		pre.selector = NameMangler.postIntroducedConstructor(aspectTypeX, targetTypeX).toCharArray();
		
		
		pre.arguments = AstUtil.copyArguments(this.arguments);
		
		//XXX should do exceptions
		
		pre.scope = new MethodScope(scope, pre, true);
		//??? do we need to do anything with scope???
		
		pre.binding = world.makeMethodBinding(
			AjcMemberMaker.preIntroducedConstructor(aspectTypeX, targetTypeX, 
					EclipseFactory.fromBindings(binding.parameters)));
		
		pre.bindArguments();
		pre.bindThrownExceptions();
		
		
		if (explicitConstructorCall == null) {
			pre.statements = new Statement[] {};
		} else {
			pre.statements = new Statement[] {
				explicitConstructorCall
			};
		}
		
		InterTypeScope newParent =
			new InterTypeScope(scope, onTypeBinding);
		pre.scope.parent = newParent;

		pre.resolveStatements(); //newParent);
		
		
		
		int nParams = pre.arguments.length;
		MethodBinding explicitConstructor = null;
		if (explicitConstructorCall != null) {
			explicitConstructor = explicitConstructorCall.binding;
			if (explicitConstructor.alwaysNeedsAccessMethod()) {
				explicitConstructor = explicitConstructor.getAccessMethod(true);
			}
		}
		
		int nExprs;
		if (explicitConstructor == null) nExprs = 0;
		else nExprs = explicitConstructor.parameters.length;
		
		
		ArrayInitializer init = new ArrayInitializer();
		init.expressions = new Expression[nExprs + nParams];
		int index = 0;
		for (int i=0; i < nExprs; i++) {
			if (i >= explicitConstructorCall.arguments.length) {
				init.expressions[index++] = new NullLiteral(0, 0);
				continue;
			}
			
			
			Expression arg = explicitConstructorCall.arguments[i];
			ResolvedMember conversionMethod = 
				AjcMemberMaker.toObjectConversionMethod(EclipseFactory.fromBinding(explicitConstructorCall.binding.parameters[i]));
			if (conversionMethod != null) {
				arg = new KnownMessageSend(world.makeMethodBindingForCall(conversionMethod),
					new CastExpression(new NullLiteral(0, 0), 
						AstUtil.makeTypeReference(world.makeTypeBinding(AjcMemberMaker.CONVERSIONS_TYPE))),
				    new Expression[] {arg });
			}
			init.expressions[index++] = arg;
		}
		
		for (int i=0; i < nParams; i++) {
			LocalVariableBinding binding = pre.arguments[i].binding;
			Expression arg = AstUtil.makeResolvedLocalVariableReference(binding);
			ResolvedMember conversionMethod = 
				AjcMemberMaker.toObjectConversionMethod(EclipseFactory.fromBinding(binding.type));
			if (conversionMethod != null) {
				arg = new KnownMessageSend(world.makeMethodBindingForCall(conversionMethod),
					new CastExpression(new NullLiteral(0, 0), 
						AstUtil.makeTypeReference(world.makeTypeBinding(AjcMemberMaker.CONVERSIONS_TYPE))),
				    new Expression[] {arg });
			}
			init.expressions[index++] = arg;
		}
		
		init.binding =objectArrayBinding;
		
		ArrayAllocationExpression newArray = new ArrayAllocationExpression();
		newArray.initializer = init;
		newArray.type = AstUtil.makeTypeReference(scope.getJavaLangObject());
		newArray.dimensions = new Expression[1];
		newArray.constant = NotAConstant;
		

		
		
		pre.statements = new Statement[] {
			new ReturnStatement(newArray, 0, 0),
		};
		return pre;
	}





	  
	public EclipseTypeMunger build(ClassScope classScope) {
		EclipseFactory world = EclipseFactory.fromScopeLookupEnvironment(classScope);
		
		resolveOnType(classScope);
		if (ignoreFurtherInvestigation) return null;
		
		binding = classScope.referenceContext.binding.resolveTypesFor(binding);
				
		if (isTargetAnnotation(classScope,"constructor")) return null; // Error message output in isTargetAnnotation
		if (isTargetEnum(classScope,"constructor")) return null;       // Error message output in isTargetEnum
		
		if (onTypeBinding.isInterface()) {
			classScope.problemReporter().signalError(sourceStart, sourceEnd,
							"can't define constructors on interfaces");
			ignoreFurtherInvestigation = true;
			return null;
		}
		
		if (onTypeBinding.isNestedType()) {
			classScope.problemReporter().signalError(sourceStart, sourceEnd,
				"can't define constructors on nested types (compiler limitation)");
			ignoreFurtherInvestigation = true;
			return null;
		}	
		
		ResolvedTypeX declaringTypeX = world.fromEclipse(onTypeBinding);
		ResolvedTypeX aspectType = world.fromEclipse(classScope.referenceContext.binding);
		
		ResolvedMember bindingAsMember = EclipseFactory.makeResolvedMember(binding);
		
		ResolvedMember signature =
			new ResolvedMember(Member.CONSTRUCTOR, declaringTypeX, declaredModifiers, 
					ResolvedTypeX.VOID, "<init>", bindingAsMember.getParameterTypes(),
					world.fromEclipse(binding.thrownExceptions));			
		ResolvedMember syntheticInterMember =
			AjcMemberMaker.interConstructor(declaringTypeX,  signature, aspectType);
		
		NewConstructorTypeMunger myMunger = 
			new NewConstructorTypeMunger(signature, syntheticInterMember, null, null);
		setMunger(myMunger);
		myMunger.check(world.getWorld());
		
		this.selector = binding.selector =
			NameMangler.postIntroducedConstructor(
				EclipseFactory.fromBinding(binding.declaringClass),
				declaringTypeX).toCharArray();
		
		return new EclipseTypeMunger(world, myMunger, aspectType, this);
	}
	
	
	private AjAttribute makeAttribute(EclipseFactory world) {
		if (explicitConstructorCall != null && !(explicitConstructorCall.binding instanceof ProblemMethodBinding)) {
			MethodBinding explicitConstructor = explicitConstructorCall.binding;
			if (explicitConstructor.alwaysNeedsAccessMethod()) {
				explicitConstructor = explicitConstructor.getAccessMethod(true);
			}
			
			
			((NewConstructorTypeMunger)munger).setExplicitConstructor(
				EclipseFactory.makeResolvedMember(explicitConstructor));
		} else {
			((NewConstructorTypeMunger)munger).setExplicitConstructor(
				new ResolvedMember(Member.CONSTRUCTOR, 
					EclipseFactory.fromBinding(onTypeBinding.superclass()),
					0, ResolvedTypeX.VOID, "<init>", TypeX.NONE));
		}
		return new AjAttribute.TypeMunger(munger);
	}
	
	
	public void generateCode(ClassScope classScope, ClassFile classFile) {
		if (ignoreFurtherInvestigation) return;
		EclipseFactory world = EclipseFactory.fromScopeLookupEnvironment(classScope);
		classFile.extraAttributes.add(new EclipseAttributeAdapter(makeAttribute(world)));
		super.generateCode(classScope, classFile);
		
		preMethod.generateCode(classScope, classFile);
	}
	protected Shadow.Kind getShadowKindForBody() {
		return Shadow.ConstructorExecution;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10561.java