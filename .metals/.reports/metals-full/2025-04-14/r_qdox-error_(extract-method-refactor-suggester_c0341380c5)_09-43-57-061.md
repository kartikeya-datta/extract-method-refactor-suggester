error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3236.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3236.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3236.java
text:
```scala
i@@f ((erasure.modifiers & AccInterface) == 0) {

/*******************************************************************************
 * Copyright (c) 2000, 2004 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v0.5 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.jdt.internal.compiler.lookup;

import java.util.Map;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.Wildcard;

/**
 * Binding for a type parameter, held by source/binary type or method.
 */
public class TypeVariableBinding extends ReferenceBinding {

	public Binding declaringElement; // binding of declaring type or method 
	public int rank; // declaration rank, can be used to match variable in parameterized type

	/**
	 * Denote the first explicit (binding) bound amongst the supertypes (from declaration in source)
	 * If no superclass was specified, then it denotes the first superinterface, or null if none was specified.
	 */
	public ReferenceBinding firstBound; 

	// actual resolved variable supertypes (if no superclass bound, then associated to Object)
	public ReferenceBinding superclass;
	public ReferenceBinding[] superInterfaces; 
	public char[] genericTypeSignature;

	public TypeVariableBinding(char[] sourceName, Binding declaringElement, int rank) {
		this.sourceName = sourceName;
		this.declaringElement = declaringElement;
		this.rank = rank;
		this.modifiers = AccPublic | AccGenericSignature; // treat type var as public
		this.tagBits |= HasTypeVariable;
	}

	public int kind() {
		return TYPE_PARAMETER;
	}	
	
	/**
	 * Returns true if the argument type satisfies all bounds of the type parameter
	 */
	public boolean boundCheck(Substitution substitution, TypeBinding argumentType) {

		if (argumentType == NullBinding || this == argumentType) 
			return true;
		if (!(argumentType instanceof ReferenceBinding || argumentType.isArrayType()))
			return false;	
		
	    if (argumentType.isWildcard()) {
	        WildcardBinding wildcard = (WildcardBinding) argumentType;
	        switch (wildcard.kind) {
	        	case Wildcard.SUPER :
		            if (!boundCheck(substitution, wildcard.bound)) return false;
		            break;
				case Wildcard.UNBOUND :
					if (this == wildcard.typeVariable()) 
						return true;
					break;	        		
	        }
	    }
//		if (this == argumentType) 
//			return true;
		boolean hasSubstitution = substitution != null;
		if (this.superclass.id != T_JavaLangObject && !argumentType.isCompatibleWith(hasSubstitution ? Scope.substitute(substitution, this.superclass) : this.superclass)) {
		    return false;
		}
	    for (int i = 0, length = this.superInterfaces.length; i < length; i++) {
	        if (!argumentType.isCompatibleWith(hasSubstitution ? Scope.substitute(substitution, this.superInterfaces[i]) : this.superInterfaces[i])) {
				return false;
	        }
	    }
	    return true;
	}
	
	/**
	 * @see org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding#canBeInstantiated()
	 */
	public boolean canBeInstantiated() {
		return false;
	}
	/**
	 * Collect the substitutes into a map for certain type variables inside the receiver type
	 * e.g.   Collection<T>.collectSubstitutes(Collection<List<X>>, Map), will populate Map with: T --> List<X>
	 */
	public void collectSubstitutes(Scope scope, TypeBinding otherType, Map substitutes, int constraint) {
		
		// cannot infer anything from a null type
		if (otherType == NullBinding) return;
	
		if (otherType.isBaseType()) {
			TypeBinding boxedType = scope.environment().computeBoxingType(otherType);
			if (boxedType == otherType) return;
			otherType = boxedType;
		}
		
		// reverse constraint, to reflect variable on rhs:   A << T --> T >: A
		int variableConstraint;
		switch(constraint) {
			case CONSTRAINT_EQUAL :
				variableConstraint = CONSTRAINT_EQUAL;
				break;
			case CONSTRAINT_EXTENDS :
				variableConstraint = CONSTRAINT_SUPER;
				break;
			default:
			//case CONSTRAINT_SUPER :
				variableConstraint = CONSTRAINT_EXTENDS;
				break;
		}
	    TypeBinding[][] variableSubstitutes = (TypeBinding[][])substitutes.get(this);
	    if (variableSubstitutes != null) {
		    insertLoop: {
		    	TypeBinding[] constraintSubstitutes = variableSubstitutes[variableConstraint];
		    	int length;
		    	if (constraintSubstitutes == null) {
		    		length = 0;
		    		constraintSubstitutes = new TypeBinding[1];
		    	} else {
		    		length = constraintSubstitutes.length;
			        for (int i = 0; i < length; i++) {
			        	TypeBinding substitute = constraintSubstitutes[i];
			            if (substitute == otherType) return; // already there
			            if (substitute == null) {
			                constraintSubstitutes[i] = otherType;
			                break insertLoop;
			            }
			        }
			        // no free spot found, need to grow
			        System.arraycopy(constraintSubstitutes, 0, constraintSubstitutes = new TypeBinding[2*length], 0, length);
		    	}
		        constraintSubstitutes[length] = otherType;
		        variableSubstitutes[variableConstraint] = constraintSubstitutes;
		    }
	    }
	}
	
	public char[] constantPoolName() { /* java/lang/Object */ 
	    if (this.firstBound != null) {
			return this.firstBound.constantPoolName();
	    }
	    return this.superclass.constantPoolName(); // java/lang/Object
	}
	/*
	 * declaringUniqueKey : genericTypeSignature
	 * p.X<T> { ... } --> Lp/X<TT;>;:TT;
	 */
	public char[] computeUniqueKey() {
		char[] declaringKey = this.declaringElement.computeUniqueKey();
		int declaringLength = declaringKey.length;
		char[] sig = genericTypeSignature();
		int sigLength = sig.length;
		char[] uniqueKey = new char[declaringLength + 1 + sigLength];
		System.arraycopy(declaringKey, 0, uniqueKey, 0, declaringLength);
		uniqueKey[declaringLength] = ':';
		System.arraycopy(sig, 0, uniqueKey, declaringLength+1, sigLength);
		return uniqueKey;
	}
	/**
	 * @see org.eclipse.jdt.internal.compiler.lookup.TypeBinding#debugName()
	 */
	public String debugName() {
	    return new String(this.sourceName);		
	}		
	public TypeBinding erasure() {
	    if (this.firstBound != null) {
			return this.firstBound.erasure();
	    }
	    return this.superclass; // java/lang/Object
	}	

/**
 * Find supertype which erases to a given well-known type, or null if not found
 * (using id avoids triggering the load of well-known type: 73740)
 * NOTE: only works for erasures of well-known types, as random other types may share
 * same id though being distincts.
 * Override super-method since erasure() is answering firstBound (first supertype) already
 */
public ReferenceBinding findSuperTypeErasingTo(int erasureId, boolean erasureIsClass) {

//    if (this.id == erasureId) return this; // no ID for type variable
    ReferenceBinding currentType = this;
    // iterate superclass to avoid recording interfaces if searched supertype is class
    if (erasureIsClass) {
		while ((currentType = currentType.superclass()) != null) { 
			if (currentType.id == erasureId || currentType.erasure().id == erasureId) return currentType;
		}    
		return null;
    }
	ReferenceBinding[][] interfacesToVisit = new ReferenceBinding[5][];
	int lastPosition = -1;
	do {
		ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
		if (itsInterfaces != NoSuperInterfaces) {
			if (++lastPosition == interfacesToVisit.length)
				System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[lastPosition * 2][], 0, lastPosition);
			interfacesToVisit[lastPosition] = itsInterfaces;
		}
	} while ((currentType = currentType.superclass()) != null);
			
	for (int i = 0; i <= lastPosition; i++) {
		ReferenceBinding[] interfaces = interfacesToVisit[i];
		for (int j = 0, length = interfaces.length; j < length; j++) {
			if ((currentType = interfaces[j]).id == erasureId || currentType.erasure().id == erasureId)
				return currentType;

			ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
			if (itsInterfaces != NoSuperInterfaces) {
				if (++lastPosition == interfacesToVisit.length)
					System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[lastPosition * 2][], 0, lastPosition);
				interfacesToVisit[lastPosition] = itsInterfaces;
			}
		}
	}
	return null;
}
/**
 * Find supertype which erases to a given type, or null if not found
 * Override super-method since erasure() is answering firstBound (first supertype) already
 */
public ReferenceBinding findSuperTypeErasingTo(ReferenceBinding erasure) {

    if (this == erasure) return this;
    ReferenceBinding currentType = this;
    if (erasure.isClass()) {
		while ((currentType = currentType.superclass()) != null) {
			if (currentType == erasure || currentType.erasure() == erasure) return currentType;
		}
		return null;
    }
	ReferenceBinding[][] interfacesToVisit = new ReferenceBinding[5][];
	int lastPosition = -1;
	do {
		ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
		if (itsInterfaces != NoSuperInterfaces) {
			if (++lastPosition == interfacesToVisit.length)
				System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[lastPosition * 2][], 0, lastPosition);
			interfacesToVisit[lastPosition] = itsInterfaces;
		}
	} while ((currentType = currentType.superclass()) != null);
			
	for (int i = 0; i <= lastPosition; i++) {
		ReferenceBinding[] interfaces = interfacesToVisit[i];
		for (int j = 0, length = interfaces.length; j < length; j++) {
			if ((currentType = interfaces[j]) == erasure || currentType.erasure() == erasure)
				return currentType;

			ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
			if (itsInterfaces != NoSuperInterfaces) {
				if (++lastPosition == interfacesToVisit.length)
					System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[lastPosition * 2][], 0, lastPosition);
				interfacesToVisit[lastPosition] = itsInterfaces;
			}
		}
	}
	return null;
}
	
	/**
	 * T::Ljava/util/Map;:Ljava/io/Serializable;
	 * T:LY<TT;>
	 */
	public char[] genericSignature() {
	    StringBuffer sig = new StringBuffer(10);
	    sig.append(this.sourceName).append(':');
	   	int interfaceLength = this.superInterfaces.length;
	    if (interfaceLength == 0 || this.firstBound == this.superclass) {
	        sig.append(this.superclass.genericTypeSignature());
	    }
		for (int i = 0; i < interfaceLength; i++) {
		    sig.append(':').append(this.superInterfaces[i].genericTypeSignature());
		}
		int sigLength = sig.length();
		char[] genericSignature = new char[sigLength];
		sig.getChars(0, sigLength, genericSignature, 0);					
		return genericSignature;
	}
	/**
	 * T::Ljava/util/Map;:Ljava/io/Serializable;
	 * T:LY<TT;>
	 */
	public char[] genericTypeSignature() {
	    if (this.genericTypeSignature != null) return this.genericTypeSignature;
		return this.genericTypeSignature = CharOperation.concat('T', this.sourceName, ';');
	}

	/**
	 * Returns true if the type variable is directly bound to a given type
	 */
	public boolean isErasureBoundTo(TypeBinding type) {
		if (this.superclass.erasure() == type) 
			return true;
		for (int i = 0, length = this.superInterfaces.length; i < length; i++) {
			if (this.superInterfaces[i].erasure() == type)
				return true;
		}
		return false;
	}
	/**
	 * Returns true if the type was declared as a type variable
	 */
	public boolean isTypeVariable() {
	    return true;
	}
	
	/** 
	 * Returns the original type variable for a given variable.
	 * Only different from receiver for type variables of generic methods of parameterized types
	 * e.g. X<U> {   <V1 extends U> U foo(V1)   } --> X<String> { <V2 extends String> String foo(V2)  }  
	 *         and V2.original() --> V1
	 */
	public TypeVariableBinding original() {
		if (this.declaringElement.kind() == Binding.METHOD) {
			MethodBinding originalMethod = ((MethodBinding)this.declaringElement).original();
			if (originalMethod != this.declaringElement) {
				return originalMethod.typeVariables[this.rank];
			}
		} else {
			ReferenceBinding originalType = (ReferenceBinding)((ReferenceBinding)this.declaringElement).erasure();
			if (originalType != this.declaringElement) {
				return originalType.typeVariables()[this.rank];
			}
		}
		return this;
	}
	
	/**
     * @see org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding#readableName()
     */
    public char[] readableName() {
        return this.sourceName;
    }
   
	ReferenceBinding resolve(LookupEnvironment environment) {
		if ((this.modifiers & AccUnresolved) == 0)
			return this;
	
		if (this.superclass != null)
			this.superclass = BinaryTypeBinding.resolveUnresolvedType(this.superclass, environment, true);
		if (this.firstBound != null)
			this.firstBound = BinaryTypeBinding.resolveUnresolvedType(this.firstBound, environment, true);
		ReferenceBinding[] interfaces = this.superInterfaces;
		for (int i = interfaces.length; --i >= 0;)
			interfaces[i] = BinaryTypeBinding.resolveUnresolvedType(interfaces[i], environment, true);
		this.modifiers &= ~AccUnresolved;
	
		// finish resolving the types
		if (this.superclass != null)
			this.superclass = BinaryTypeBinding.resolveType(this.superclass, environment, true);
		if (this.firstBound != null)
			this.firstBound = BinaryTypeBinding.resolveType(this.firstBound, environment, true);
		for (int i = interfaces.length; --i >= 0;)
			interfaces[i] = BinaryTypeBinding.resolveType(interfaces[i], environment, true);
		return this;
	}
	
	/**
     * @see org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding#shortReadableName()
     */
    public char[] shortReadableName() {
        return this.readableName();
    }
	public ReferenceBinding superclass() {
		return superclass;
	}
	public ReferenceBinding[] superInterfaces() {
		return superInterfaces;
	}	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer(10);
		buffer.append('<').append(this.sourceName);//.append('[').append(this.rank).append(']');
		if (this.superclass != null && this.firstBound == this.superclass) {
		    buffer.append(" extends ").append(this.superclass.debugName()); //$NON-NLS-1$
		}
		if (this.superInterfaces != null && this.superInterfaces != NoSuperInterfaces) {
		   if (this.firstBound != this.superclass) {
		        buffer.append(" extends "); //$NON-NLS-1$
	        }
		    for (int i = 0, length = this.superInterfaces.length; i < length; i++) {
		        if (i > 0 || this.firstBound == this.superclass) {
		            buffer.append(" & "); //$NON-NLS-1$
		        }
				buffer.append(this.superInterfaces[i].debugName());
			}
		}
		buffer.append('>');
		return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3236.java