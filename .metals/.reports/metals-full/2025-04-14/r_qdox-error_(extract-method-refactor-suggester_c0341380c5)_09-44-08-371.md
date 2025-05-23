error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3268.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3268.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3268.java
text:
```scala
i@@f (!this.receiver.isThis()) { // TODO need to check if shouldn't be isImplicitThis check (and then removed)

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.eval;
 
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.CompoundAssignment;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.IntLiteral;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.CodeStream;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.ParameterizedFieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemFieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeVariableBinding;

public class CodeSnippetFieldReference extends FieldReference implements ProblemReasons, EvaluationConstants {

	EvaluationContext evaluationContext;
	FieldBinding delegateThis;
/**
 * CodeSnippetFieldReference constructor comment.
 * @param source char[]
 * @param pos long
 */
public CodeSnippetFieldReference(char[] source, long pos, EvaluationContext evaluationContext) {
	super(source, pos);
	this.evaluationContext = evaluationContext;
}
public void generateAssignment(BlockScope currentScope, CodeStream codeStream, Assignment assignment, boolean valueRequired) {

	if (this.codegenBinding.canBeSeenBy(this.receiverType, this, currentScope)) {
		this.receiver.generateCode(currentScope, codeStream, !this.codegenBinding.isStatic());
		assignment.expression.generateCode(currentScope, codeStream, true);
		fieldStore(codeStream, this.codegenBinding, null, valueRequired);
	} else {
		((CodeSnippetCodeStream) codeStream).generateEmulationForField(this.codegenBinding);
		this.receiver.generateCode(currentScope, codeStream, !this.codegenBinding.isStatic());
		if (this.codegenBinding.isStatic()) { // need a receiver?
			codeStream.aconst_null();
		}
		assignment.expression.generateCode(currentScope, codeStream, true);
		if (valueRequired) {
			if ((this.codegenBinding.type == LongBinding) || (this.codegenBinding.type == DoubleBinding)) {
				codeStream.dup2_x2();
			} else {
				codeStream.dup_x2();
			}
		}
		((CodeSnippetCodeStream) codeStream).generateEmulatedWriteAccessForField(this.codegenBinding);
	}
	if (valueRequired){
		codeStream.generateImplicitConversion(assignment.implicitConversion);
	}
}
/**
 * Field reference code generation
 *
 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
 * @param valueRequired boolean
 */
public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {

	int pc = codeStream.position;
	if (this.constant != Constant.NotAConstant) {
		if (valueRequired) {
			codeStream.generateConstant(this.constant, this.implicitConversion);
		}
	} else {
		boolean isStatic = this.codegenBinding.isStatic();
		this.receiver.generateCode(currentScope, codeStream, !isStatic);
		if (valueRequired) {
			Constant fieldConstant = this.codegenBinding.constant();
			if (fieldConstant == Constant.NotAConstant) {
				if (this.codegenBinding.declaringClass == null) { // array length
					codeStream.arraylength();
				} else {
					if (this.codegenBinding.canBeSeenBy(this.receiverType, this, currentScope)) {
						if (isStatic) {
							codeStream.getstatic(this.codegenBinding);
						} else {
							codeStream.getfield(this.codegenBinding);
						}
					} else {
						if (isStatic) {
							// we need a null on the stack to use the reflect emulation
							codeStream.aconst_null();
						}
						((CodeSnippetCodeStream) codeStream).generateEmulatedReadAccessForField(this.codegenBinding);
					}
				}
				codeStream.generateImplicitConversion(this.implicitConversion);
			} else {
				if (!isStatic) {
					codeStream.invokeObjectGetClass(); // perform null check
					codeStream.pop();
				}
				codeStream.generateConstant(fieldConstant, this.implicitConversion);
			}
		} else {
			if (!isStatic){
				codeStream.invokeObjectGetClass(); // perform null check
				codeStream.pop();
			}
		}
	}
	codeStream.recordPositionsFrom(pc, this.sourceStart);
}

public void generateCompoundAssignment(BlockScope currentScope, CodeStream codeStream, Expression expression, int operator, int assignmentImplicitConversion, boolean valueRequired) {
	
	boolean isStatic;
	if (this.codegenBinding.canBeSeenBy(this.receiverType, this, currentScope)) {
		this.receiver.generateCode(currentScope, codeStream, !(isStatic = this.codegenBinding.isStatic()));
		if (isStatic) {
			codeStream.getstatic(this.codegenBinding);
		} else {
			codeStream.dup();
			codeStream.getfield(this.codegenBinding);
		}
		int operationTypeID;
		switch(operationTypeID = (this.implicitConversion & IMPLICIT_CONVERSION_MASK) >> 4) {
			case T_JavaLangString :
			case T_JavaLangObject :
			case T_undefined :
				codeStream.generateStringConcatenationAppend(currentScope, null, expression);
				break;
			default :
				// promote the array reference to the suitable operation type
				codeStream.generateImplicitConversion(this.implicitConversion);
				// generate the increment value (will by itself  be promoted to the operation value)
				if (expression == IntLiteral.One){ // prefix operation
					codeStream.generateConstant(expression.constant, this.implicitConversion);			
				} else {
					expression.generateCode(currentScope, codeStream, true);
				}		
				// perform the operation
				codeStream.sendOperator(operator, operationTypeID);
				// cast the value back to the array reference type
				codeStream.generateImplicitConversion(assignmentImplicitConversion);
		}
		fieldStore(codeStream, this.codegenBinding, null, valueRequired);
	} else {
		this.receiver.generateCode(currentScope, codeStream, !(isStatic = this.codegenBinding.isStatic()));
		if (isStatic) {
			// used to store the value
			((CodeSnippetCodeStream) codeStream).generateEmulationForField(this.codegenBinding);
			codeStream.aconst_null();

			// used to retrieve the actual value
			codeStream.aconst_null();
			((CodeSnippetCodeStream) codeStream).generateEmulatedReadAccessForField(this.codegenBinding);
		} else {
			// used to store the value
			((CodeSnippetCodeStream) codeStream).generateEmulationForField(this.binding);
			this.receiver.generateCode(currentScope, codeStream, !(isStatic = this.codegenBinding.isStatic()));

			// used to retrieve the actual value
			codeStream.dup();
			((CodeSnippetCodeStream) codeStream).generateEmulatedReadAccessForField(this.codegenBinding);
							
		}
		int operationTypeID;
		if ((operationTypeID = (this.implicitConversion & IMPLICIT_CONVERSION_MASK) >> 4) == T_JavaLangString) {
			codeStream.generateStringConcatenationAppend(currentScope, null, expression);
		} else {
			// promote the array reference to the suitable operation type
			codeStream.generateImplicitConversion(this.implicitConversion);
			// generate the increment value (will by itself  be promoted to the operation value)
			if (expression == IntLiteral.One){ // prefix operation
				codeStream.generateConstant(expression.constant, this.implicitConversion);			
			} else {
				expression.generateCode(currentScope, codeStream, true);
			}		
			// perform the operation
			codeStream.sendOperator(operator, operationTypeID);
			// cast the value back to the array reference type
			codeStream.generateImplicitConversion(assignmentImplicitConversion);
		}
		// current stack is:
		// field receiver value
		if (valueRequired) {
			if ((this.codegenBinding.type == LongBinding) || (this.codegenBinding.type == DoubleBinding)) {
				codeStream.dup2_x2();
			} else {
				codeStream.dup_x2();
			}
		}
		// current stack is:
		// value field receiver value				
		((CodeSnippetCodeStream) codeStream).generateEmulatedWriteAccessForField(this.codegenBinding);
	}
}
public void generatePostIncrement(BlockScope currentScope, CodeStream codeStream, CompoundAssignment postIncrement, boolean valueRequired) {
	boolean isStatic;
	if (this.codegenBinding.canBeSeenBy(this.receiverType, this, currentScope)) {
		this.receiver.generateCode(currentScope, codeStream, !(isStatic = this.codegenBinding.isStatic()));
		if (isStatic) {
			codeStream.getstatic(this.codegenBinding);
		} else {
			codeStream.dup();
			codeStream.getfield(this.codegenBinding);
		}
		if (valueRequired) {
			if (isStatic) {
				if ((this.codegenBinding.type == LongBinding) || (this.codegenBinding.type == DoubleBinding)) {
					codeStream.dup2();
				} else {
					codeStream.dup();
				}
			} else { // Stack:  [owner][old field value]  ---> [old field value][owner][old field value]
				if ((this.codegenBinding.type == LongBinding) || (this.codegenBinding.type == DoubleBinding)) {
					codeStream.dup2_x1();
				} else {
					codeStream.dup_x1();
				}
			}
		}
		codeStream.generateConstant(postIncrement.expression.constant, this.implicitConversion);
		codeStream.sendOperator(postIncrement.operator, this.codegenBinding.type.id);
		codeStream.generateImplicitConversion(postIncrement.preAssignImplicitConversion);
		fieldStore(codeStream, this.codegenBinding, null, false);
	} else {
		this.receiver.generateCode(currentScope, codeStream, !(isStatic = this.codegenBinding.isStatic()));
		if (this.codegenBinding.isStatic()) {
			codeStream.aconst_null();
		}
		// the actual stack is: receiver
		codeStream.dup();
		// the actual stack is: receiver receiver
		((CodeSnippetCodeStream) codeStream).generateEmulatedReadAccessForField(this.codegenBinding);
		// the actual stack is: receiver value
		// receiver value
		// value receiver value 							dup_x1 or dup2_x1 if value required
		// value value receiver value						dup_x1 or dup2_x1
		// value value receiver								pop or pop2
		// value value receiver field						generateEmulationForField
		// value value field receiver 						swap
		// value field receiver value field receiver 		dup2_x1 or dup2_x2
		// value field receiver value 				 		pop2
		// value field receiver newvalue 				 	generate constant + op
		// value 											store
		if (valueRequired) {
			if ((this.codegenBinding.type == LongBinding) || (this.codegenBinding.type == DoubleBinding)) {
				codeStream.dup2_x1();
			} else {
				codeStream.dup_x1();
			}
		}
		if ((this.codegenBinding.type == LongBinding) || (this.codegenBinding.type == DoubleBinding)) {
			codeStream.dup2_x1();
			codeStream.pop2();
		} else {
			codeStream.dup_x1();
			codeStream.pop();
		}
		((CodeSnippetCodeStream) codeStream).generateEmulationForField(this.codegenBinding);
		codeStream.swap();
		
		if ((this.codegenBinding.type == LongBinding) || (this.codegenBinding.type == DoubleBinding)) {
			codeStream.dup2_x2();
		} else {
			codeStream.dup2_x1();
		}
		codeStream.pop2();

		codeStream.generateConstant(postIncrement.expression.constant, this.implicitConversion);
		codeStream.sendOperator(postIncrement.operator, this.codegenBinding.type.id);
		codeStream.generateImplicitConversion(postIncrement.preAssignImplicitConversion);
		((CodeSnippetCodeStream) codeStream).generateEmulatedWriteAccessForField(this.codegenBinding);
	}
}
/*
 * No need to emulate access to protected fields since not implicitly accessed
 */
public void manageSyntheticAccessIfNecessary(BlockScope currentScope, FlowInfo flowInfo, boolean isReadAccess){
	// The private access will be managed through the code generation

	if (!flowInfo.isReachable()) return;
	// if field from parameterized type got found, use the original field at codegen time
	if (this.binding instanceof ParameterizedFieldBinding) {
	    ParameterizedFieldBinding parameterizedField = (ParameterizedFieldBinding) this.binding;
	    this.codegenBinding = parameterizedField.originalField;
	    // extra cast needed if field type was type variable
	    if (this.codegenBinding.type.isTypeVariable()) {
	        TypeVariableBinding variableReturnType = (TypeVariableBinding) this.codegenBinding.type;
	        if (variableReturnType.firstBound != parameterizedField.type) { // no need for extra cast if same as first bound anyway
			    this.genericCast = parameterizedField.type.erasure();
	        }
	    }
	} else {
	    this.codegenBinding = this.binding;
	}
		
	// if the binding declaring class is not visible, need special action
	// for runtime compatibility on 1.2 VMs : change the declaring class of the binding
	// NOTE: from target 1.2 on, field's declaring class is touched if any different from receiver type
	TypeBinding someReceiverType = this.delegateThis != null ? this.delegateThis.type : this.receiverType;
	if (this.binding.declaringClass != someReceiverType
			&& !someReceiverType.isArrayType()
			&& this.binding.declaringClass != null // array.length
			&& this.binding.constant() == Constant.NotAConstant) {
	
		CompilerOptions options = currentScope.compilerOptions();
		if ((options.targetJDK >= ClassFileConstants.JDK1_2
				&& (options.complianceLevel >= ClassFileConstants.JDK1_4 || !receiver.isImplicitThis() || !this.codegenBinding.isStatic())
				&& this.binding.declaringClass.id != T_JavaLangObject) // no change for Object fields
 !this.binding.declaringClass.canBeSeenBy(currentScope)) {

			this.codegenBinding =
				currentScope.enclosingSourceType().getUpdatedFieldBinding(
					this.codegenBinding,
					(ReferenceBinding) someReceiverType.erasure());
		}
	}	
}
public TypeBinding resolveType(BlockScope scope) {
	// Answer the signature type of the field.
	// constants are propaged when the field is final
	// and initialized with a (compile time) constant 

	// regular receiver reference 
	this.receiverType = this.receiver.resolveType(scope);
	if (this.receiverType == null){
		this.constant = Constant.NotAConstant;
		return null;
	}
	// the case receiverType.isArrayType and token = 'length' is handled by the scope API
	this.codegenBinding = this.binding = scope.getField(this.receiverType, this.token, this);
	FieldBinding firstAttempt = this.binding;
	boolean isNotVisible = false;
	if (!this.binding.isValidBinding()) {
		if (this.binding instanceof ProblemFieldBinding
			&& ((ProblemFieldBinding) this.binding).problemId() == NotVisible) {
				isNotVisible = true;
				if (this.evaluationContext.declaringTypeName != null) {
					this.delegateThis = scope.getField(scope.enclosingSourceType(), DELEGATE_THIS, this);
					if (this.delegateThis == null){  // if not found then internal error, field should have been found
						this.constant = Constant.NotAConstant;
						scope.problemReporter().invalidField(this, this.receiverType);
						return null;
					}
				} else {
					this.constant = Constant.NotAConstant;
					scope.problemReporter().invalidField(this, this.receiverType);
					return null;
				}
			CodeSnippetScope localScope = new CodeSnippetScope(scope);
			this.codegenBinding = this.binding = localScope.getFieldForCodeSnippet(this.delegateThis.type, this.token, this);
		}
	}

	if (!this.binding.isValidBinding()) {
		this.constant = Constant.NotAConstant;
		if (isNotVisible) {
			this.codegenBinding = this.binding = firstAttempt;
		}
		scope.problemReporter().invalidField(this, this.receiverType);
		return null;
	}

	if (isFieldUseDeprecated(this.binding, scope, (this.bits & IsStrictlyAssigned) !=0)) {
		scope.problemReporter().deprecatedField(this.binding, this);
	}
	// check for this.x in static is done in the resolution of the receiver
	this.constant = this.receiver.isImplicitThis() ? this.binding.constant() : Constant.NotAConstant;
	if (!this.receiver.isThis()) {
		this.constant = Constant.NotAConstant;
	}
	return this.resolvedType = this.binding.type;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3268.java