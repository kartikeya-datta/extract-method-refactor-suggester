error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7682.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7682.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7682.java
text:
```scala
r@@awArguments[i] =  environment.convertToRawType(originalVariables[i].upperBound());

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.lookup;

import org.eclipse.jdt.internal.compiler.ast.MessageSend;

/**
 * Binding denoting a generic method after type parameter substitutions got performed.
 * On parameterized type bindings, all methods got substituted, regardless whether
 * their signature did involve generics or not, so as to get the proper declaringClass for
 * these methods.
 */
public class ParameterizedGenericMethodBinding extends ParameterizedMethodBinding implements Substitution {

    public TypeBinding[] typeArguments; 
    private LookupEnvironment environment;
    public boolean inferredReturnType;
    public boolean wasInferred; // only set to true for instances resulting from method invocation inferrence
    public boolean isRaw; // set to true for method behaving as raw for substitution purpose
    private MethodBinding tiebreakMethod;
    public boolean isUnchecked; // transient flag set during inference (warning: bindings are shared, so flag cannot be trusted beyond)
	
	/**
	 * Perform inference of generic method type parameters and/or expected type
	 */	
	public static MethodBinding computeCompatibleMethod(MethodBinding originalMethod, TypeBinding[] arguments, Scope scope, InvocationSite invocationSite) {
		
		ParameterizedGenericMethodBinding methodSubstitute;
		TypeVariableBinding[] typeVariables = originalMethod.typeVariables;
		TypeBinding[] substitutes = invocationSite.genericTypeArguments();
		
		computeSubstitutes: {
			if (substitutes != null) {
				// explicit type arguments got supplied
				if (substitutes.length != typeVariables.length) {
			        // incompatible due to wrong arity
			        return new ProblemMethodBinding(originalMethod, originalMethod.selector, substitutes, ProblemReasons.TypeParameterArityMismatch);
				}
				methodSubstitute = scope.environment().createParameterizedGenericMethod(originalMethod, substitutes);
				break computeSubstitutes;
			}
			
			// perform type argument inference (15.12.2.7)
				
			// initializes the map of substitutes (var --> type[][]{ equal, extends, super}
			TypeBinding[] parameters = originalMethod.parameters;
			InferenceContext inferenceContext = new InferenceContext(originalMethod);
			methodSubstitute = inferFromArgumentTypes(scope, originalMethod, arguments, parameters, inferenceContext);
			if (methodSubstitute == null) 
				return null;
			// substitutes may hold null to denote unresolved vars, but null arguments got replaced with respective original variable in param method
			
			// 15.12.2.8 - inferring unresolved type arguments
			if (inferenceContext.hasUnresolvedTypeArgument()) {
				TypeBinding expectedType = null;
				if (methodSubstitute.returnType != TypeBinding.VOID) {
					// if message invocation has expected type
					if (invocationSite instanceof MessageSend) {
						MessageSend message = (MessageSend) invocationSite;
						expectedType = message.expectedType;
					}
					if (expectedType == null) expectedType = scope.getJavaLangObject(); // assume Object by default
				}
				methodSubstitute = methodSubstitute.inferFromExpectedType(scope, expectedType, inferenceContext);
				if (methodSubstitute == null) 
					return null;
			}
		}

		// bounds check
		if (!methodSubstitute.isRaw) {
			for (int i = 0, length = typeVariables.length; i < length; i++) {
			    TypeVariableBinding typeVariable = typeVariables[i];
			    TypeBinding substitute = methodSubstitute.typeArguments[i];
				switch (typeVariable.boundCheck(methodSubstitute, substitute)) {
					case TypeConstants.MISMATCH :
				        // incompatible due to bound check
						int argLength = arguments.length;
						TypeBinding[] augmentedArguments = new TypeBinding[argLength + 2]; // append offending substitute and typeVariable 
						System.arraycopy(arguments, 0, augmentedArguments, 0, argLength);
						augmentedArguments[argLength] = substitute;
						augmentedArguments[argLength+1] = typeVariable;
				        return new ProblemMethodBinding(methodSubstitute, originalMethod.selector, augmentedArguments, ProblemReasons.ParameterBoundMismatch);
					case TypeConstants.UNCHECKED :
						// tolerate unchecked bounds
						methodSubstitute.isUnchecked = true;
						break;
				}
			}
		}
		return methodSubstitute;
	}

	/**
	 * Collect argument type mapping, handling varargs
	 */
	private static ParameterizedGenericMethodBinding inferFromArgumentTypes(Scope scope, MethodBinding originalMethod, TypeBinding[] arguments, TypeBinding[] parameters, InferenceContext inferenceContext) {

		if (originalMethod.isVarargs()) {
			int paramLength = parameters.length;
			int minArgLength = paramLength - 1;
			int argLength = arguments.length;
			// process mandatory arguments
			for (int i = 0; i < minArgLength; i++) {
				parameters[i].collectSubstitutes(scope, arguments[i], inferenceContext, TypeConstants.CONSTRAINT_EXTENDS);
				if (inferenceContext.status == InferenceContext.FAILED) return null; // impossible substitution
			}
			// process optional arguments
			if (minArgLength < argLength) {
				TypeBinding varargType = parameters[minArgLength]; // last arg type - as is ?
				TypeBinding lastArgument = arguments[minArgLength];
				checkVarargDimension: {
					if (paramLength == argLength) {
						if (lastArgument == TypeBinding.NULL) break checkVarargDimension;
						switch (lastArgument.dimensions()) {
							case 0 :
								break; // will remove one dim
							case 1 :
								if (!lastArgument.leafComponentType().isBaseType()) break checkVarargDimension;
								break; // will remove one dim
							default :
								break checkVarargDimension;
						}
					}
					// eliminate one array dimension
					varargType = ((ArrayBinding)varargType).elementsType(); 
				}
				for (int i = minArgLength; i < argLength; i++) {
					varargType.collectSubstitutes(scope, arguments[i], inferenceContext, TypeConstants.CONSTRAINT_EXTENDS);
					if (inferenceContext.status == InferenceContext.FAILED) return null; // impossible substitution
				}
			}
		} else {
			int paramLength = parameters.length;
			for (int i = 0; i < paramLength; i++) {
				parameters[i].collectSubstitutes(scope, arguments[i], inferenceContext, TypeConstants.CONSTRAINT_EXTENDS);
				if (inferenceContext.status == InferenceContext.FAILED) return null; // impossible substitution
			}
		}
		if (inferenceContext.status == InferenceContext.RAW_SUBSTITUTION) {
			// raw generic method inferred
			return scope.environment().createParameterizedGenericMethod(originalMethod, (RawTypeBinding)null);
		}
		TypeVariableBinding[] originalVariables = originalMethod.typeVariables;
		if (!resolveSubstituteConstraints(scope, originalVariables , inferenceContext, false/*ignore Ti<:Uk*/)) 
			return null; // impossible substitution

		// apply inferred variable substitutions - replacing unresolved variable with original ones in param method
		TypeBinding[] inferredSustitutes = inferenceContext.substitutes;
		TypeBinding[] actualSubstitutes = inferredSustitutes;
		for (int i = 0, varLength = originalVariables.length; i < varLength; i++) {
			if (inferredSustitutes[i] == null) {
				if (actualSubstitutes == inferredSustitutes) {
					System.arraycopy(inferredSustitutes, 0, actualSubstitutes = new TypeBinding[varLength], 0, i); // clone to replace null with original variable in param method
				}
				actualSubstitutes[i] = originalVariables[i];
			} else if (actualSubstitutes != inferredSustitutes) {
				actualSubstitutes[i] = inferredSustitutes[i];
			}
		}
		ParameterizedGenericMethodBinding paramMethod = scope.environment().createParameterizedGenericMethod(originalMethod, actualSubstitutes);
		return paramMethod;
	}
	
	private static boolean resolveSubstituteConstraints(Scope scope, TypeVariableBinding[] typeVariables, InferenceContext inferenceContext, boolean considerEXTENDSConstraints) {
		TypeBinding[] substitutes = inferenceContext.substitutes;
		int varLength = typeVariables.length;
		// check Tj=U constraints
		nextTypeParameter: 
			for (int i = 0; i < varLength; i++) {
				TypeVariableBinding current = typeVariables[i];
				TypeBinding substitute = substitutes[i];
				if (substitute != null) continue nextTypeParameter; // already inferred previously
				TypeBinding [] equalSubstitutes = inferenceContext.getSubstitutes(current, TypeConstants.CONSTRAINT_EQUAL);
				if (equalSubstitutes != null) {
					nextConstraint:
						for (int j = 0, equalLength = equalSubstitutes.length; j < equalLength; j++) {
							TypeBinding equalSubstitute = equalSubstitutes[j];
							if (equalSubstitute == null) continue nextConstraint;
							if (equalSubstitute == current) {
								// try to find a better different match if any in subsequent equal candidates
								for (int k = j+1; k < equalLength; k++) {
									equalSubstitute = equalSubstitutes[k];
									if (equalSubstitute != current && equalSubstitute != null) {
										substitutes[i] = equalSubstitute;
										continue nextTypeParameter;
									}
								}
								substitutes[i] = current;
								continue nextTypeParameter;
							}
//							if (equalSubstitute.isTypeVariable()) {
//								TypeVariableBinding variable = (TypeVariableBinding) equalSubstitute;
//								// substituted by a variable of the same method, ignore
//								if (variable.rank < varLength && typeVariables[variable.rank] == variable) {
//									// TODO (philippe) rewrite all other constraints to use current instead.
//									continue nextConstraint;
//								}
//							}
							substitutes[i] = equalSubstitute;
							continue nextTypeParameter; // pick first match, applicability check will rule out invalid scenario where others were present
						}
				}
			}
		if (inferenceContext.hasUnresolvedTypeArgument()) {
			// check Tj>:U constraints
			nextTypeParameter: 
				for (int i = 0; i < varLength; i++) {
					TypeVariableBinding current = typeVariables[i];
					TypeBinding substitute = substitutes[i];
					if (substitute != null) continue nextTypeParameter; // already inferred previously
					TypeBinding [] bounds = inferenceContext.getSubstitutes(current, TypeConstants.CONSTRAINT_SUPER);
					if (bounds == null) continue nextTypeParameter;
					TypeBinding mostSpecificSubstitute = scope.lowerUpperBound(bounds);
					if (mostSpecificSubstitute == null) {
						return false; // incompatible
					}
					if (mostSpecificSubstitute != TypeBinding.VOID) {
						substitutes[i] = mostSpecificSubstitute;
					}
				}
		}
		if (considerEXTENDSConstraints && inferenceContext.hasUnresolvedTypeArgument()) {
			// check Tj<:U constraints
			nextTypeParameter: 
				for (int i = 0; i < varLength; i++) {
					TypeVariableBinding current = typeVariables[i];
					TypeBinding substitute = substitutes[i];
					if (substitute != null) continue nextTypeParameter; // already inferred previously
					TypeBinding [] bounds = inferenceContext.getSubstitutes(current, TypeConstants.CONSTRAINT_EXTENDS);
					if (bounds == null) continue nextTypeParameter;
					TypeBinding[] glb = Scope.greaterLowerBound(bounds);
					TypeBinding mostSpecificSubstitute = null;
					if (glb != null) mostSpecificSubstitute = glb[0]; // TODO (philippe) need to improve
						//TypeBinding mostSpecificSubstitute = scope.greaterLowerBound(bounds);
						if (mostSpecificSubstitute != null) {
							substitutes[i] = mostSpecificSubstitute;
						}
					} 
		}
		return true;
	}
	
	/**
	 * Create raw generic method for raw type (double substitution from type vars with raw type arguments, and erasure of method variables)
	 * Only invoked for non-static generic methods of raw type
	 */
	public ParameterizedGenericMethodBinding(MethodBinding originalMethod, RawTypeBinding rawType, LookupEnvironment environment) {

		TypeVariableBinding[] originalVariables = originalMethod.typeVariables;
		int length = originalVariables.length;
		TypeBinding[] rawArguments = new TypeBinding[length];
		for (int i = 0; i < length; i++) {
			rawArguments[i] =  environment.convertToRawType(originalVariables[i].erasure());
		}		
	    this.isRaw = true;
	    this.tagBits = originalMethod.tagBits;
	    this.environment = environment;
		this.modifiers = originalMethod.modifiers;
		this.selector = originalMethod.selector;
		this.declaringClass = rawType == null ? originalMethod.declaringClass : rawType;
	    this.typeVariables = Binding.NO_TYPE_VARIABLES;
	    this.typeArguments = rawArguments;
	    this.originalMethod = originalMethod;
		boolean ignoreRawTypeSubstitution = rawType == null || originalMethod.isStatic();
	    this.parameters = Scope.substitute(this, ignoreRawTypeSubstitution
	    									? originalMethod.parameters // no substitution if original was static
	    									: Scope.substitute(rawType, originalMethod.parameters));
	    this.thrownExceptions = Scope.substitute(this, 	ignoreRawTypeSubstitution 
	    									? originalMethod.thrownExceptions // no substitution if original was static
	    									: Scope.substitute(rawType, originalMethod.thrownExceptions));
	    this.returnType = Scope.substitute(this, ignoreRawTypeSubstitution 
	    									? originalMethod.returnType // no substitution if original was static
	    									: Scope.substitute(rawType, originalMethod.returnType));
	    this.wasInferred = false; // not resulting from method invocation inferrence
	}
    
    /**
     * Create method of parameterized type, substituting original parameters with type arguments.
     */
	public ParameterizedGenericMethodBinding(MethodBinding originalMethod, TypeBinding[] typeArguments, LookupEnvironment environment) {

	    this.environment = environment;
		this.modifiers = originalMethod.modifiers;
		this.selector = originalMethod.selector;
		this.declaringClass = originalMethod.declaringClass;
	    this.typeVariables = Binding.NO_TYPE_VARIABLES;
	    this.typeArguments = typeArguments;
	    this.isRaw = false;
	    this.tagBits = originalMethod.tagBits;
	    this.originalMethod = originalMethod;
	    this.parameters = Scope.substitute(this, originalMethod.parameters);
	    this.thrownExceptions = Scope.substitute(this, originalMethod.thrownExceptions);
	    this.returnType = Scope.substitute(this, originalMethod.returnType);
	    this.wasInferred = true;// resulting from method invocation inferrence
	}

	/*
	 * parameterizedDeclaringUniqueKey dot selector originalMethodGenericSignature percent typeArguments
	 * p.X<U> { <T> void bar(T t, U u) { new X<String>().bar(this, "") } } --> Lp/X<Ljava/lang/String;>;.bar<T:Ljava/lang/Object;>(TT;Ljava/lang/String;)V%<Lp/X;>
	 */
	public char[] computeUniqueKey(boolean isLeaf) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.originalMethod.computeUniqueKey(false/*not a leaf*/));
		buffer.append('%');
		buffer.append('<');
		if (!this.isRaw) {
			int length = this.typeArguments.length;
			for (int i = 0; i < length; i++) {
				TypeBinding typeArgument = this.typeArguments[i];
				buffer.append(typeArgument.computeUniqueKey(false/*not a leaf*/));
			}
		}
		buffer.append('>');
		int resultLength = buffer.length();
		char[] result = new char[resultLength];
		buffer.getChars(0, resultLength, result, 0);	
		return result;
		
	}
	
	/**
	 * @see org.eclipse.jdt.internal.compiler.lookup.Substitution#environment()
	 */
	public LookupEnvironment environment() {
		return this.environment;
	}
	/**
	 * Returns true if some parameters got substituted.
	 * NOTE: generic method invocation delegates to its declaring method (could be a parameterized one)
	 */
	public boolean hasSubstitutedParameters() {
		// generic parameterized method can represent either an invocation or a raw generic method
		if (this.wasInferred) 
			return this.originalMethod.hasSubstitutedParameters();
		return super.hasSubstitutedParameters();
	}
	/**
	 * Returns true if the return type got substituted.
	 * NOTE: generic method invocation delegates to its declaring method (could be a parameterized one)
	 */
	public boolean hasSubstitutedReturnType() {
		if (this.inferredReturnType) 
			return this.originalMethod.hasSubstitutedReturnType();
		return super.hasSubstitutedReturnType();
	}
	/**
	 * Given some type expectation, and type variable bounds, perform some inference.
	 * Returns true if still had unresolved type variable at the end of the operation
	 */
	private ParameterizedGenericMethodBinding inferFromExpectedType(Scope scope, TypeBinding expectedType, InferenceContext inferenceContext) {
	    TypeVariableBinding[] originalVariables = this.originalMethod.typeVariables; // immediate parent (could be a parameterized method)
		int varLength = originalVariables.length;
		
		computeSubstitutes: {
		    // infer from expected return type
			if (expectedType != null) {
			    this.returnType.collectSubstitutes(scope, expectedType, inferenceContext, TypeConstants.CONSTRAINT_SUPER);
			    if (inferenceContext.status == InferenceContext.FAILED) return null; // impossible substitution
			}
		    // infer from bounds of type parameters
			for (int i = 0; i < varLength; i++) {
				TypeVariableBinding originalVariable = originalVariables[i];
				TypeBinding argument = this.typeArguments[i];
				boolean argAlreadyInferred = argument != originalVariable;
				if (originalVariable.firstBound == originalVariable.superclass) {
					TypeBinding substitutedBound = Scope.substitute(this, originalVariable.superclass);
					argument.collectSubstitutes(scope, substitutedBound, inferenceContext, TypeConstants.CONSTRAINT_SUPER);
					if (inferenceContext.status == InferenceContext.FAILED) return null; // impossible substitution
					// JLS 15.12.2.8 claims reverse inference shouldn't occur, however it improves inference
					// e.g. given: <E extends Object, S extends Collection<E>> S test1(S param)
					//                   invocation: test1(new Vector<String>())    will infer: S=Vector<String>  and with code below: E=String
					if (argAlreadyInferred) {
						substitutedBound.collectSubstitutes(scope, argument, inferenceContext, TypeConstants.CONSTRAINT_EXTENDS);
						if (inferenceContext.status == InferenceContext.FAILED) return null; // impossible substitution
					}
				}
				for (int j = 0, max = originalVariable.superInterfaces.length; j < max; j++) {
					TypeBinding substitutedBound = Scope.substitute(this, originalVariable.superInterfaces[j]);
					argument.collectSubstitutes(scope, substitutedBound, inferenceContext, TypeConstants.CONSTRAINT_SUPER);
					if (inferenceContext.status == InferenceContext.FAILED) return null; // impossible substitution
					// JLS 15.12.2.8 claims reverse inference shouldn't occur, however it improves inference
					if (argAlreadyInferred) {
						substitutedBound.collectSubstitutes(scope, argument, inferenceContext, TypeConstants.CONSTRAINT_EXTENDS);
						if (inferenceContext.status == InferenceContext.FAILED) return null; // impossible substitution
					}
				}
			}
			if (inferenceContext.status == InferenceContext.RAW_SUBSTITUTION) {
		    	// raw generic method inferred
		    	this.isRaw = true;
				this.isUnchecked = false;
		    	for (int i = 0; i < varLength; i++) {
		    		this.typeArguments[i] = originalVariables[i].upperBound();
		    	}
		    	break computeSubstitutes;
			}		
			if (!resolveSubstituteConstraints(scope, originalVariables, inferenceContext, true/*consider Ti<:Uk*/)) 
				return null; // incompatible
			// this.typeArguments = substitutes; - no op since side effects got performed during #resolveSubstituteConstraints
	    	for (int i = 0; i < varLength; i++) {
	    		TypeBinding substitute = inferenceContext.substitutes[i];
	    		if (substitute != null) {
	    			this.typeArguments[i] = inferenceContext.substitutes[i];
	    		} else {
	    			// remaining unresolved variable are considered to be Object (or their bound actually)
		    		this.typeArguments[i] = originalVariables[i].upperBound();
		    	}
	    	}
		}
		// may still need an extra substitution at the end (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=121369)
		// to properly substitute a remaining unresolved variable which also appear in a formal bound
		this.typeArguments = Scope.substitute(this, this.typeArguments);
		// adjust method types to reflect latest inference
		TypeBinding oldReturnType = this.returnType;
		this.returnType = Scope.substitute(this, this.returnType);
		this.inferredReturnType = this.returnType != oldReturnType;
	    this.parameters = Scope.substitute(this, this.parameters);
	    this.thrownExceptions = Scope.substitute(this, this.thrownExceptions);
	    return this;
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.lookup.Substitution#isRawSubstitution()
	 */
	public boolean isRawSubstitution() {
		return this.isRaw;
	}
	
	/**
	 * @see org.eclipse.jdt.internal.compiler.lookup.Substitution#substitute(org.eclipse.jdt.internal.compiler.lookup.TypeVariableBinding)
	 */
	public TypeBinding substitute(TypeVariableBinding originalVariable) {
        TypeVariableBinding[] variables = this.originalMethod.typeVariables;
        int length = variables.length;
        // check this variable can be substituted given parameterized type
        if (originalVariable.rank < length && variables[originalVariable.rank] == originalVariable) {
			return this.typeArguments[originalVariable.rank];
        }
        if (!this.isStatic() && this.declaringClass instanceof Substitution) {
        	return ((Substitution)this.declaringClass).substitute(originalVariable);
        }
	    return originalVariable;
	}
	/**
	 * Returns the method to use during tiebreak (usually the method itself).
	 * For generic method invocations, tiebreak needs to use generic method with erasure substitutes.
	 */
	public MethodBinding tiebreakMethod() {
		if (this.tiebreakMethod == null) {
//			if (this.isRaw) {
//				this.tiebreakMethod = this;
//			} else {
//				this.tiebreakMethod = new ParameterizedGenericMethodBinding(this.originalMethod, (RawTypeBinding)null, this.environment);
				TypeVariableBinding[] originalVariables = originalMethod.typeVariables;
				int length = originalVariables.length;
				TypeBinding[] rawArguments = new TypeBinding[length];
				for (int i = 0; i < length; i++)
					rawArguments[i] =  environment.convertToRawType(originalVariables[i].erasure());
				this.tiebreakMethod = this.environment.createParameterizedGenericMethod(this.originalMethod, rawArguments);
//			}
		} 
		return this.tiebreakMethod;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7682.java