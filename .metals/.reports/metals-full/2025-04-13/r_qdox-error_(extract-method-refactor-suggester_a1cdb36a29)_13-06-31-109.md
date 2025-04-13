error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5669.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5669.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5669.java
text:
```scala
s@@yntheticAccessor = binding.getAccessMethod(isSuperAccess());

/*******************************************************************************
 * Copyright (c) 2000, 2001, 2002 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v0.5 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Palo Alto Research Center, Incorporated - AspectJ adaptation
 ******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

/**
 * AspectJ - support for MethodBinding.alwaysNeedsAccessMethod
 */
public class MessageSend extends Expression implements InvocationSite {
	public Expression receiver ;
	public char[] selector ;
	public Expression[] arguments ;
	public MethodBinding binding, codegenBinding;

	public long nameSourcePosition ; //(start<<32)+end

	public MethodBinding syntheticAccessor;

	public TypeBinding receiverType, qualifyingType;
	
public MessageSend() {
	
}
public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {

	flowInfo = receiver.analyseCode(currentScope, flowContext, flowInfo, !binding.isStatic()).unconditionalInits();
	if (arguments != null) {
		int length = arguments.length;
		for (int i = 0; i < length; i++) {
			flowInfo = arguments[i].analyseCode(currentScope, flowContext, flowInfo).unconditionalInits();
		}
	}
	ReferenceBinding[] thrownExceptions;
	if ((thrownExceptions = binding.thrownExceptions) != NoExceptions) {
		// must verify that exceptions potentially thrown by this expression are caught in the method
		flowContext.checkExceptionHandlers(thrownExceptions, this, flowInfo, currentScope);
	}
	// if invoking through an enclosing instance, then must perform the field generation -- only if reachable
	manageEnclosingInstanceAccessIfNecessary(currentScope);
	manageSyntheticAccessIfNecessary(currentScope);	
	return flowInfo;
}
/**
 * MessageSend code generation
 *
 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
 * @param valueRequired boolean
 */ 
public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {

	int pc = codeStream.position;

	// generate receiver/enclosing instance access
	boolean isStatic = codegenBinding.isStatic();
	// outer access ?
	if (!isStatic && ((bits & DepthMASK) != 0) && (receiver == ThisReference.ThisImplicit)){
		// outer method can be reached through emulation if implicit access
		Object[] path = currentScope.getExactEmulationPath(currentScope.enclosingSourceType().enclosingTypeAt((bits & DepthMASK) >> DepthSHIFT));
		if (path == null) {
			// emulation was not possible (should not happen per construction)
			currentScope.problemReporter().needImplementation();
		} else {
			codeStream.generateOuterAccess(path, this, currentScope);
		}
	} else {
		receiver.generateCode(currentScope, codeStream, !isStatic);
	}
	// generate arguments
	if (arguments != null){
		for (int i = 0, max = arguments.length; i < max; i++){
			arguments[i].generateCode(currentScope, codeStream, true);
		}
	}
	// actual message invocation
	if (syntheticAccessor == null){
		if (isStatic){
			codeStream.invokestatic(codegenBinding);
		} else {
			if( (receiver.isSuper()) || codegenBinding.isPrivate()){
				codeStream.invokespecial(codegenBinding);
			} else {
				if (codegenBinding.declaringClass.isInterface()){
					codeStream.invokeinterface(codegenBinding);
				} else {
					codeStream.invokevirtual(codegenBinding);
				}
			}
		}
	} else {
		codeStream.invokestatic(syntheticAccessor);
	}
	// operation on the returned value
	if (valueRequired){
		// implicit conversion if necessary
		codeStream.generateImplicitConversion(implicitConversion);
	} else {
		// pop return value if any
		switch(binding.returnType.id){
			case T_long :
			case T_double :
				codeStream.pop2();
				break;
			case T_void :
				break;
			default:
				codeStream.pop();
		}
	}
	codeStream.recordPositionsFrom(pc, (int)(this.nameSourcePosition >>> 32)); // highlight selector
}
public boolean isSuperAccess() {	
	return receiver.isSuper();
}
public boolean isTypeAccess() {	
	return receiver != null && receiver.isTypeReference();
}
public void manageEnclosingInstanceAccessIfNecessary(BlockScope currentScope) {
	if (((bits & DepthMASK) != 0) && !binding.isStatic() && (receiver == ThisReference.ThisImplicit)) {
		ReferenceBinding compatibleType = currentScope.enclosingSourceType();
		// the declaringClass of the target binding must be compatible with the enclosing
		// type at <depth> levels outside
		for (int i = 0, depth = (bits & DepthMASK) >> DepthSHIFT; i < depth; i++) {
			compatibleType = compatibleType.enclosingType();
		}
		currentScope.emulateOuterAccess((SourceTypeBinding) compatibleType, false); // request cascade of accesses
	}
}
public void manageSyntheticAccessIfNecessary(BlockScope currentScope){
	if (binding.alwaysNeedsAccessMethod()) {
		syntheticAccessor = binding.getAccessMethod();
		return;
	}


	if (binding.isPrivate()){

		// depth is set for both implicit and explicit access (see MethodBinding#canBeSeenBy)		
		if (currentScope.enclosingSourceType() != binding.declaringClass){
		
			syntheticAccessor = ((SourceTypeBinding)binding.declaringClass).addSyntheticMethod(binding);
			currentScope.problemReporter().needToEmulateMethodAccess(binding, this);
			return;
		}

	} else if (receiver instanceof QualifiedSuperReference){ // qualified super

		// qualified super need emulation always
		SourceTypeBinding destinationType = (SourceTypeBinding)(((QualifiedSuperReference)receiver).currentCompatibleType);
		syntheticAccessor = destinationType.addSyntheticMethod(binding);
		currentScope.problemReporter().needToEmulateMethodAccess(binding, this);
		return;

	} else if (binding.isProtected()){

		SourceTypeBinding enclosingSourceType;
		if (((bits & DepthMASK) != 0) 
				&& binding.declaringClass.getPackage() 
					!= (enclosingSourceType = currentScope.enclosingSourceType()).getPackage()){

			SourceTypeBinding currentCompatibleType = (SourceTypeBinding)enclosingSourceType.enclosingTypeAt((bits & DepthMASK) >> DepthSHIFT);
			syntheticAccessor = currentCompatibleType.addSyntheticMethod(binding);
			currentScope.problemReporter().needToEmulateMethodAccess(binding, this);
			return;
		}
	}
	// if the binding declaring class is not visible, need special action
	// for runtime compatibility on 1.2 VMs : change the declaring class of the binding
	// NOTE: from 1.4 on, method's declaring class is touched if any different from receiver type
	// and not from Object or implicit static method call.	
	if (binding.declaringClass != this.qualifyingType
		&& !this.qualifyingType.isArrayType()
		&& ((currentScope.environment().options.complianceLevel >= CompilerOptions.JDK1_4
				&& (receiver != ThisReference.ThisImplicit || !binding.isStatic())
				&& binding.declaringClass.id != T_Object) // no change for Object methods
 !binding.declaringClass.canBeSeenBy(currentScope))) {

		this.codegenBinding = currentScope.enclosingSourceType().getUpdatedMethodBinding(binding, (ReferenceBinding) this.qualifyingType);
	}
}

public TypeBinding resolveType(BlockScope scope) {
	// Answer the signature return type
	// Base type promotion

	constant = NotAConstant;
	this.qualifyingType = this.receiverType = receiver.resolveType(scope); 
	
	// will check for null after args are resolved
	TypeBinding[] argumentTypes = NoParameters;
	if (arguments != null) {
		boolean argHasError = false; // typeChecks all arguments 
		int length = arguments.length;
		argumentTypes = new TypeBinding[length];
		for (int i = 0; i < length; i++){
			if ((argumentTypes[i] = arguments[i].resolveType(scope)) == null){
				argHasError = true;
			}
		}
		if (argHasError){
			MethodBinding closestMethod = null;
			if(receiverType instanceof ReferenceBinding) {
				// record any selector match, for clients who may still need hint about possible method match
				this.codegenBinding = this.binding = scope.findMethod((ReferenceBinding)receiverType, selector, new TypeBinding[]{}, this);
			}			
			return null;
		}
	}
	if (this.receiverType == null)
		return null;

	// base type cannot receive any message
	if (this.receiverType.isBaseType()) {
		scope.problemReporter().errorNoMethodFor(this, this.receiverType, argumentTypes);
		return null;
	}

	resolveMethodBinding(scope, argumentTypes);
	if (!binding.isValidBinding()) {
		if (binding.declaringClass == null) {
			if (this.receiverType instanceof ReferenceBinding) {
				binding.declaringClass = (ReferenceBinding) this.receiverType;
			} else { // really bad error ....
				scope.problemReporter().errorNoMethodFor(this, this.receiverType, argumentTypes);
				return null;
			}
		}
		scope.problemReporter().invalidMethod(this, binding);
		// record the closest match, for clients who may still need hint about possible method match
		if (binding.problemId() == ProblemReasons.NotFound){
			this.codegenBinding = this.binding = ((ProblemMethodBinding)binding).closestMatch;
		}
		return null;
	}
	if (!binding.isStatic()) {
		// the "receiver" must not be a type, i.e. a NameReference that the TC has bound to a Type
		if (receiver instanceof NameReference) {
			if ((((NameReference) receiver).bits & BindingIds.TYPE) != 0) {
				scope.problemReporter().mustUseAStaticMethod(this, binding);
				return null;
			}
		}
	}
	if (arguments != null)
		for (int i = 0; i < arguments.length; i++)
			arguments[i].implicitWidening(binding.parameters[i], argumentTypes[i]);

	//-------message send that are known to fail at compile time-----------
	if (binding.isAbstract()) {
		if (receiver.isSuper()) {
			scope.problemReporter().cannotDireclyInvokeAbstractMethod(this, binding);
			return null;
		}
		// abstract private methods cannot occur nor abstract static............
	}
	if (isMethodUseDeprecated(binding, scope))
		scope.problemReporter().deprecatedMethod(binding, this);

	return binding.returnType;
}
protected void resolveMethodBinding(
	BlockScope scope,
	TypeBinding[] argumentTypes) {
	this.codegenBinding = this.binding = 
		receiver == ThisReference.ThisImplicit
			? scope.getImplicitMethod(selector, argumentTypes, this)
			: scope.getMethod(this.receiverType, selector, argumentTypes, this); 
}
public void setActualReceiverType(ReferenceBinding receiverType) {
	this.qualifyingType = receiverType;
}
public void setDepth(int depth) {
	if (depth > 0) {
		bits &= ~DepthMASK; // flush previous depth if any
		bits |= (depth & 0xFF) << DepthSHIFT; // encoded on 8 bits
	}
}
public void setFieldIndex(int depth) {
	// ignore for here
}

public String toStringExpression(){
	
	String s = ""; //$NON-NLS-1$
	if (receiver != ThisReference.ThisImplicit)
		s = s + receiver.toStringExpression()+"."; //$NON-NLS-1$
	s = s + new String(selector) + "(" ; //$NON-NLS-1$
	if (arguments != null)
		for (int i = 0; i < arguments.length ; i ++)
		{	s = s + arguments[i].toStringExpression();
			if ( i != arguments.length -1 ) s = s + " , " ;};; //$NON-NLS-1$
	s =s + ")" ; //$NON-NLS-1$
	return s;
}

public void traverse(IAbstractSyntaxTreeVisitor visitor, BlockScope blockScope) {
	if (visitor.visit(this, blockScope)) {
		receiver.traverse(visitor, blockScope);
		if (arguments != null) {
			int argumentsLength = arguments.length;
			for (int i = 0; i < argumentsLength; i++)
				arguments[i].traverse(visitor, blockScope);
		}
	}
	visitor.endVisit(this, blockScope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5669.java