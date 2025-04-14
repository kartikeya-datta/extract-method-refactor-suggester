error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5362.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5362.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5362.java
text:
```scala
o@@riginalParameterizedType.type, substitutedArguments, originalParameterizedType.enclosingType());

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.lookup;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.Wildcard;

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
    
    /**
     * Create method of parameterized type, substituting original parameters with type arguments.
     */
	public ParameterizedGenericMethodBinding(MethodBinding originalMethod, TypeBinding[] typeArguments, LookupEnvironment environment) {

	    this.environment = environment;
		this.modifiers = originalMethod.modifiers;
		this.selector = originalMethod.selector;
		this.declaringClass = originalMethod.declaringClass;
	    this.typeVariables = NoTypeVariables;
	    this.typeArguments = typeArguments;
	    this.originalMethod = originalMethod;
	    this.parameters = Scope.substitute(this, originalMethod.parameters);
	    this.thrownExceptions = Scope.substitute(this, originalMethod.thrownExceptions);
	    this.returnType = this.substitute(originalMethod.returnType);
	}
	/**
	 * Create raw generic method for raw type (double substitution from type vars with raw type arguments, and erasure of method variables)
	 */
	public ParameterizedGenericMethodBinding(MethodBinding originalMethod, RawTypeBinding rawType, LookupEnvironment environment) {

		TypeVariableBinding[] originalVariables = originalMethod.typeVariables;
		int length = originalVariables.length;
		TypeBinding[] rawArguments = new TypeBinding[length];
		for (int i = 0; i < length; i++) {
			rawArguments[i] = originalVariables[i].erasure();
		}		
	    this.environment = environment;
		this.modifiers = originalMethod.modifiers;
		this.selector = originalMethod.selector;
		this.declaringClass = rawType;
	    this.typeVariables = NoTypeVariables;
	    this.typeArguments = rawArguments;
	    this.originalMethod = originalMethod;
		boolean isStatic = originalMethod.isStatic();
	    this.parameters = Scope.substitute(this, 
	    										isStatic ? originalMethod.parameters : Scope.substitute(rawType, originalMethod.parameters));
	    this.thrownExceptions = Scope.substitute(this, 
	    										isStatic ? originalMethod.thrownExceptions : Scope.substitute(rawType, originalMethod.thrownExceptions));
	    this.returnType = this.substitute(isStatic ? originalMethod.returnType : rawType.substitute(originalMethod.returnType));
	}
	
	/**
	 * Perform inference of generic method type parameters and/or expected type
	 */	
	public static MethodBinding computeCompatibleMethod(MethodBinding originalMethod, TypeBinding[] arguments, Scope scope, InvocationSite invocationSite) {
		
		ParameterizedGenericMethodBinding methodSubstitute;
		TypeVariableBinding[] typeVariables = originalMethod.typeVariables;
		TypeBinding[] substitutes = invocationSite.genericTypeArguments();
		
		if (substitutes != null) {
			if (substitutes.length != typeVariables.length) {
		        // incompatible due to wrong arity
		        return new ProblemMethodBinding(originalMethod, originalMethod.selector, substitutes, TypeParameterArityMismatch);
			}
			methodSubstitute = new ParameterizedGenericMethodBinding(originalMethod, substitutes, scope.environment());
		} else {
			// perform type inference based on argument types and expected type
			
			// collect substitutes by pattern matching parameters and arguments
			int argLength = arguments.length;
			TypeBinding[] parameters = originalMethod.parameters;
			int varLength = typeVariables.length;
			HashMap collectedSubstitutes = new HashMap(varLength);
			for (int i = 0; i < varLength; i++)
				collectedSubstitutes.put(typeVariables[i], new TypeBinding[1]);
			for (int i = 0; i < argLength; i++)
				parameters[i].collectSubstitutes(arguments[i], collectedSubstitutes);
			substitutes = new TypeBinding[varLength];
			boolean needReturnTypeInference = false;
			for (int i = 0; i < varLength; i++) {
				TypeBinding[] variableSubstitutes = (TypeBinding[]) collectedSubstitutes.get(typeVariables[i]);
				TypeBinding mostSpecificSubstitute = scope.lowerUpperBound(variableSubstitutes);
				//TypeBinding mostSpecificSubstitute = scope.mostSpecificCommonType(variableSubstitutes);
				if (mostSpecificSubstitute == null)
					return null; // incompatible
				if (mostSpecificSubstitute == VoidBinding) {
					needReturnTypeInference = true;
				    mostSpecificSubstitute = typeVariables[i];
				}
				substitutes[i] = mostSpecificSubstitute;
			}
			// apply inferred variable substitutions
			methodSubstitute = new ParameterizedGenericMethodBinding(originalMethod, substitutes, scope.environment());
	
			if (needReturnTypeInference && invocationSite instanceof MessageSend) {
				MessageSend message = (MessageSend) invocationSite;
				TypeBinding expectedType = message.expectedType;
				if (expectedType != null)
					methodSubstitute.inferFromExpectedType(message.expectedType, scope);
			}
		}
		// check bounds
		for (int i = 0, length = typeVariables.length; i < length; i++) {
		    TypeVariableBinding typeVariable = typeVariables[i];
		    if (!typeVariable.boundCheck(methodSubstitute, substitutes[i]))
		        // incompatible due to bound check
		        return new ProblemMethodBinding(methodSubstitute, originalMethod.selector, new TypeBinding[]{substitutes[i], typeVariables[i] }, ParameterBoundMismatch);
		}

		return methodSubstitute;
	}
	
	public void inferFromExpectedType(TypeBinding expectedType, Scope scope) {
	    if (this.returnType == expectedType) 
	        return;
        if ((this.returnType.tagBits & TagBits.HasTypeVariable) == 0) 
            return;
        Map substitutes = new HashMap(1);
        int length = this.typeArguments.length;
        TypeVariableBinding[] originalVariables = this.original().typeVariables;
        boolean hasUnboundParameters = false;
        for (int i = 0; i < length; i++) {
            if (this.typeArguments[i] == originalVariables[i]) {
                hasUnboundParameters = true;
	        	substitutes.put(originalVariables[i], new TypeBinding[1]);
            } else {
	        	substitutes.put(originalVariables[i], new TypeBinding[] { this.typeArguments[i] });
            }
        }
        if (!hasUnboundParameters)
            return;
        returnType.collectSubstitutes(expectedType, substitutes);
		for (int i = 0; i < length; i++) {
			TypeBinding[] variableSubstitutes = (TypeBinding[]) substitutes.get(originalVariables[i]);
			TypeBinding mostSpecificSubstitute = scope.lowerUpperBound(variableSubstitutes);
			//TypeBinding mostSpecificSubstitute = scope.mostSpecificCommonType(variableSubstitutes);
			if (mostSpecificSubstitute == null) {
			    return; // TODO (philippe) should report no way to infer type
			}
			if (mostSpecificSubstitute != VoidBinding) 
				this.typeArguments[i] = mostSpecificSubstitute;
		}
		TypeBinding oldReturnType = this.returnType;
		this.returnType = this.substitute(this.returnType);
		this.inferredReturnType = this.returnType != oldReturnType;
	    this.parameters = Scope.substitute(this, this.parameters);
	    this.thrownExceptions = Scope.substitute(this, this.thrownExceptions);
	}
	
    /**
     * Returns a type, where original type was substituted using the receiver
     * parameterized method.
     */
    public TypeBinding substitute(TypeBinding originalType) {
        
        if ((originalType.tagBits & TagBits.HasTypeVariable) != 0) {
    	    if (originalType.isTypeVariable()) {
    	        TypeVariableBinding originalVariable = (TypeVariableBinding) originalType;
    	        TypeVariableBinding[] variables = this.originalMethod.typeVariables;
    	        int length = variables.length;
    	        // check this variable can be substituted given parameterized type
       		        if (originalVariable.rank < length && variables[originalVariable.rank] == originalVariable) {
    					return this.typeArguments[originalVariable.rank];
       		        }
    	    } else if (originalType.isParameterizedType()) {
    	        ParameterizedTypeBinding originalParameterizedType = (ParameterizedTypeBinding) originalType;
    	        TypeBinding[] originalArguments = originalParameterizedType.arguments;
    	        TypeBinding[] substitutedArguments = Scope.substitute(this, originalArguments);
    	        if (substitutedArguments != originalArguments) {
					identicalVariables: { // if substituted with original variables, then answer the generic type itself
						TypeVariableBinding[] originalVariables = originalParameterizedType.type.typeVariables();
						for (int i = 0, length = originalVariables.length; i < length; i++) {
							if (substitutedArguments[i] != originalVariables[i]) break identicalVariables;
						}
						return originalParameterizedType.type;
					}    	        	
    	            return this.environment.createParameterizedType(
    	                    originalParameterizedType.type, substitutedArguments, originalParameterizedType.enclosingType);
        	    } 
			} else if (originalType.isArrayType()) {
				TypeBinding originalLeafComponentType = originalType.leafComponentType();
				TypeBinding substitute = substitute(originalLeafComponentType); // substitute could itself be array type
				if (substitute != originalLeafComponentType) {
					return this.environment.createArrayType(substitute.leafComponentType(), substitute.dimensions() + originalType.dimensions());
				}
			} else if (originalType.isWildcard()) {
		        WildcardBinding wildcard = (WildcardBinding) originalType;
		        if (wildcard.kind != Wildcard.UNBOUND) {
			        TypeBinding originalBound = wildcard.bound;
			        TypeBinding substitutedBound = substitute(originalBound);
			        if (substitutedBound != originalBound) {
		        		return this.environment.createWildcard(wildcard.genericType, wildcard.rank, substitutedBound, wildcard.kind);
			        }
		        }
    	    }
		} else if (originalType.isGenericType()) {
		    // treat as if parameterized with its type variables
			ReferenceBinding originalGenericType = (ReferenceBinding) originalType;
			TypeVariableBinding[] originalVariables = originalGenericType.typeVariables();
			int length = originalVariables.length;
			TypeBinding[] originalArguments;
			System.arraycopy(originalVariables, 0, originalArguments = new TypeBinding[length], 0, length);
			TypeBinding[] substitutedArguments = Scope.substitute(this, originalArguments);
			if (substitutedArguments != originalArguments) {
				return this.environment.createParameterizedType(
						originalGenericType, substitutedArguments, null);
			}
        }
        return originalType;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5362.java