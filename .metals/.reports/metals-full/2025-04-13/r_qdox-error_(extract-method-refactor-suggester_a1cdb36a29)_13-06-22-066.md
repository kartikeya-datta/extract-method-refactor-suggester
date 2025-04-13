error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14012.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14012.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14012.java
text:
```scala
i@@f (explicitConstructorCall != null && !(explicitConstructorCall.binding instanceof ProblemMethodBinding)) {

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


package org.aspectj.ajdt.internal.compiler.ast;

import org.aspectj.ajdt.internal.compiler.lookup.*;
import org.aspectj.weaver.*;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.parser.Parser;


public class InterTypeConstructorDeclaration extends InterTypeDeclaration {	
	private MethodDeclaration preMethod;
	private ExplicitConstructorCall explicitConstructorCall = null;
	
	public InterTypeConstructorDeclaration(CompilationResult result, TypeReference onType) {
		super(result, onType);
	}
	
	public void parseStatements(Parser parser, CompilationUnitDeclaration unit) {
		if (ignoreFurtherInvestigation)
			return;
	    parser.parseAsConstructor(this, unit);
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
		EclipseWorld world = EclipseWorld.fromScopeLookupEnvironment(scope);
		
		TypeX aspectTypeX = EclipseWorld.fromBinding(binding.declaringClass);
		TypeX targetTypeX = EclipseWorld.fromBinding(onTypeBinding);
		
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
					world.fromBindings(binding.parameters)));
		
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

		pre.resolveStatements(newParent);
		
		
		
		int nParams = pre.arguments.length;
		MethodBinding explicitConstructor = null;
		if (explicitConstructorCall != null) {
			explicitConstructor = explicitConstructorCall.binding;
			if (explicitConstructor.alwaysNeedsAccessMethod()) {
				explicitConstructor = explicitConstructor.getAccessMethod();
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
				AjcMemberMaker.toObjectConversionMethod(world.fromBinding(explicitConstructorCall.binding.parameters[i]));
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
				AjcMemberMaker.toObjectConversionMethod(world.fromBinding(binding.type));
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




	public void build(ClassScope classScope, CrosscuttingMembers xcut) {
		EclipseWorld world = EclipseWorld.fromScopeLookupEnvironment(classScope);

		binding = classScope.referenceContext.binding.resolveTypesFor(binding);
		
		resolveOnType(classScope);
		if (ignoreFurtherInvestigation) return;
		
		
		if (onTypeBinding.isInterface()) {
			ignoreFurtherInvestigation = true;
			return;
		}
		
		if (onTypeBinding.isNestedType()) {
			classScope.problemReporter().signalError(sourceStart, sourceEnd,
				"can't define constructors on nested types (compiler limitation)");
			ignoreFurtherInvestigation = true;
			return;
		}	
		
		ResolvedTypeX declaringTypeX = world.fromEclipse(onTypeBinding);
		ResolvedTypeX aspectType = world.fromEclipse(classScope.referenceContext.binding);
		
		ResolvedMember bindingAsMember = world.makeResolvedMember(binding);
		
		ResolvedMember signature =
			new ResolvedMember(Member.CONSTRUCTOR, declaringTypeX, declaredModifiers, 
					ResolvedTypeX.VOID, "<init>", bindingAsMember.getParameterTypes());
					
		ResolvedMember syntheticInterMember =
			AjcMemberMaker.interConstructor(declaringTypeX,  signature, aspectType);
		
		NewConstructorTypeMunger myMunger = 
			new NewConstructorTypeMunger(signature, syntheticInterMember, null, null);
		this.munger = myMunger;
		
		this.selector = binding.selector =
			NameMangler.postIntroducedConstructor(
				EclipseWorld.fromBinding(binding.declaringClass),
				declaringTypeX).toCharArray();
		
		xcut.addTypeMunger(new EclipseTypeMunger(myMunger, aspectType, this));
	}
	
	
	private AjAttribute makeAttribute(EclipseWorld world) {
		if (explicitConstructorCall != null) {
			MethodBinding explicitConstructor = explicitConstructorCall.binding;
			if (explicitConstructor.alwaysNeedsAccessMethod()) {
				explicitConstructor = explicitConstructor.getAccessMethod();
			}
			
			
			((NewConstructorTypeMunger)munger).setExplicitConstructor(
				world.makeResolvedMember(explicitConstructor));
		} else {
			((NewConstructorTypeMunger)munger).setExplicitConstructor(
				new ResolvedMember(Member.CONSTRUCTOR, 
					EclipseWorld.fromBinding(onTypeBinding.superclass()),
					0, ResolvedTypeX.VOID, "<init>", TypeX.NONE));
		}
		return new AjAttribute.TypeMunger(munger);
	}
	
	
	public void generateCode(ClassScope classScope, ClassFile classFile) {
		if (ignoreFurtherInvestigation) return;
		EclipseWorld world = EclipseWorld.fromScopeLookupEnvironment(classScope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14012.java