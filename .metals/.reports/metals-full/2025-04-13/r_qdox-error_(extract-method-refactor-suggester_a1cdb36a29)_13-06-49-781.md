error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6347.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6347.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6347.java
text:
```scala
t@@his.superclass = superType instanceof ReferenceBinding && !superType.isInterface()

/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.lookup;

import java.util.Map;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.Wildcard;

/*
 * A wildcard acts as an argument for parameterized types, allowing to
 * abstract parameterized types, e.g. List<String> is not compatible with List<Object>, 
 * but compatible with List<?>.
 */
public class WildcardBinding extends ReferenceBinding {

	ReferenceBinding genericType;
	int rank;
    public TypeBinding bound; // when unbound denotes the corresponding type variable (so as to retrieve its bound lazily)
    public TypeBinding[] otherBounds; // only positionned by lub computations (if so, #bound is also set) and associated to EXTENDS mode
	char[] genericSignature;
	public int kind;
	ReferenceBinding superclass;
	ReferenceBinding[] superInterfaces;
	TypeVariableBinding typeVariable; // corresponding variable
	LookupEnvironment environment;
	
	/**
	 * When unbound, the bound denotes the corresponding type variable (so as to retrieve its bound lazily)
	 */
	public WildcardBinding(ReferenceBinding genericType, int rank, TypeBinding bound, TypeBinding[] otherBounds, int kind, LookupEnvironment environment) {
		this.genericType = genericType;
		this.rank = rank;
	    this.kind = kind;
		this.modifiers = AccPublic | AccGenericSignature; // treat wildcard as public
		this.environment = environment;
		initialize(genericType, bound, otherBounds);

		if (genericType instanceof UnresolvedReferenceBinding)
			((UnresolvedReferenceBinding) genericType).addWrapper(this);
		if (bound instanceof UnresolvedReferenceBinding)
			((UnresolvedReferenceBinding) bound).addWrapper(this);
		this.tagBits |=  HasUnresolvedTypeVariables; // cleared in resolve()
	}

	public int kind() {
		return WILDCARD_TYPE;
	}	
		
	/**
	 * Returns true if the argument type satisfies all bounds of the type parameter
	 */
	public boolean boundCheck(TypeBinding argumentType) {
	    switch (this.kind) {
	        case Wildcard.UNBOUND :
	            return true;
	        case Wildcard.EXTENDS :
	            if (argumentType.isCompatibleWith(this.bound)) return true;
	            // check other bounds (lub scenario)
            	for (int i = 0, length = this.otherBounds == null ? 0 : this.otherBounds.length; i < length; i++) {
            		if (argumentType.isCompatibleWith(this.otherBounds[i])) return true;
            	}
            	return false;
	        default: // SUPER
	        	// ? super Exception   ok for:  IOException, since it would be ok for (Exception)ioException
	            return argumentType.isCompatibleWith(this.bound);
	    }
    }
	/**
	 * @see org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding#canBeInstantiated()
	 */
	public boolean canBeInstantiated() {
		// cannot be asked per construction
		return false;
	}
	/**
	 * Collect the substitutes into a map for certain type variables inside the receiver type
	 * e.g.   Collection<T>.collectSubstitutes(Collection<List<X>>, Map), will populate Map with: T --> List<X>
	 */
	public void collectSubstitutes(Scope scope, TypeBinding otherType, Map substitutes, int constraint) {

		if ((this.tagBits & TagBits.HasTypeVariable) == 0) return;
		if (otherType == NullBinding) return;
	
		switch (constraint) {
			case CONSTRAINT_EXTENDS : // A << F
				switch (this.kind) {
					case Wildcard.UNBOUND: // F={?}
//						if (otherType.isWildcard()) {
//							WildcardBinding otherWildcard = (WildcardBinding) otherType;
//							switch(otherWildcard.kind) {
//								case Wildcard.UNBOUND: // A={?} << F={?}  --> 0
//									break;
//								case Wildcard.EXTENDS: // A={? extends V} << F={?} ---> 0
//									break;
//								case Wildcard.SUPER: // A={? super V} << F={?} ---> 0
//									break;
//							}
//						} else { // A=V << F={?} ---> 0
//						}
						break;
					case Wildcard.EXTENDS: // F={? extends U}
						if (otherType.isWildcard()) {
							WildcardBinding otherWildcard = (WildcardBinding) otherType;
							switch(otherWildcard.kind) {
								case Wildcard.UNBOUND: // A={?} << F={? extends U}  --> 0
									break;
								case Wildcard.EXTENDS: // A={? extends V} << F={? extends U} ---> V << U
									this.bound.collectSubstitutes(scope, otherWildcard.bound, substitutes, CONSTRAINT_EXTENDS);
						        	for (int i = 0, length = otherWildcard.otherBounds == null ? 0 : otherWildcard.otherBounds.length; i < length; i++) {
										this.bound.collectSubstitutes(scope, otherWildcard.otherBounds[i], substitutes, CONSTRAINT_EXTENDS);
						        	}									
									break;
								case Wildcard.SUPER: // A={? super V} << F={? extends U} ---> 0
									break;
							}
						} else { // A=V << F={? extends U} ---> V << U
							this.bound.collectSubstitutes(scope, otherType, substitutes, CONSTRAINT_EXTENDS);
						}
						break;
					case Wildcard.SUPER: // F={? super U}
						if (otherType.isWildcard()) {
							WildcardBinding otherWildcard = (WildcardBinding) otherType;
							switch(otherWildcard.kind) {
								case Wildcard.UNBOUND: // A={?} << F={? super U}  --> 0
									break;
								case Wildcard.EXTENDS: // A={? extends V} << F={? super U} ---> 0
									break;
								case Wildcard.SUPER: // A={? super V} << F={? super U} ---> 0
									this.bound.collectSubstitutes(scope, otherWildcard.bound, substitutes, CONSTRAINT_SUPER);
						        	for (int i = 0, length = otherWildcard.otherBounds == null ? 0 : otherWildcard.otherBounds.length; i < length; i++) {
										this.bound.collectSubstitutes(scope, otherWildcard.otherBounds[i], substitutes, CONSTRAINT_SUPER);
						        	}									
									break;
							}
						} else { // A=V << F={? super U} ---> V >> U
							this.bound.collectSubstitutes(scope, otherType, substitutes, CONSTRAINT_SUPER);							
						}						
						break;
				}
				break;
			case CONSTRAINT_EQUAL : // A == F
				switch (this.kind) {
					case Wildcard.UNBOUND: // F={?}
//						if (otherType.isWildcard()) {
//							WildcardBinding otherWildcard = (WildcardBinding) otherType;
//							switch(otherWildcard.kind) {
//								case Wildcard.UNBOUND: // A={?} == F={?}  --> 0
//									break;
//								case Wildcard.EXTENDS: // A={? extends V} == F={?} ---> 0
//									break;
//								case Wildcard.SUPER: // A={? super V} == F={?} ---> 0
//									break;
//							}
//						} else { // A=V == F={?} ---> 0
//						}
						break;
					case Wildcard.EXTENDS: // F={? extends U}
						if (otherType.isWildcard()) {
							WildcardBinding otherWildcard = (WildcardBinding) otherType;
							switch(otherWildcard.kind) {
								case Wildcard.UNBOUND: // A={?} == F={? extends U}  --> 0
									break;
								case Wildcard.EXTENDS: // A={? extends V} == F={? extends U} ---> V == U
									this.bound.collectSubstitutes(scope, otherWildcard.bound, substitutes, CONSTRAINT_EQUAL);
						        	for (int i = 0, length = otherWildcard.otherBounds == null ? 0 : otherWildcard.otherBounds.length; i < length; i++) {
										this.bound.collectSubstitutes(scope, otherWildcard.otherBounds[i], substitutes, CONSTRAINT_EQUAL);
						        	}											
									break;
								case Wildcard.SUPER: // A={? super V} == F={? extends U} ---> 0
									break;
							}
						} else { // A=V == F={? extends U} ---> 0
						}
						break;
					case Wildcard.SUPER: // F={? super U}
						if (otherType.isWildcard()) {
							WildcardBinding otherWildcard = (WildcardBinding) otherType;
							switch(otherWildcard.kind) {
								case Wildcard.UNBOUND: // A={?} == F={? super U}  --> 0
									break;
								case Wildcard.EXTENDS: // A={? extends V} == F={? super U} ---> 0
									break;
								case Wildcard.SUPER: // A={? super V} == F={? super U} ---> 0
									this.bound.collectSubstitutes(scope, otherWildcard.bound, substitutes, CONSTRAINT_EQUAL);
						        	for (int i = 0, length = otherWildcard.otherBounds == null ? 0 : otherWildcard.otherBounds.length; i < length; i++) {
										this.bound.collectSubstitutes(scope, otherWildcard.otherBounds[i], substitutes, CONSTRAINT_EQUAL);
						        	}	
						        	break;
							}
						} else { // A=V == F={? super U} ---> 0
						}						
						break;
				}
				break;
			case CONSTRAINT_SUPER : // A >> F
				switch (this.kind) {
					case Wildcard.UNBOUND: // F={?}
//						if (otherType.isWildcard()) {
//							WildcardBinding otherWildcard = (WildcardBinding) otherType;
//							switch(otherWildcard.kind) {
//								case Wildcard.UNBOUND: // A={?} >> F={?}  --> 0
//									break;
//								case Wildcard.EXTENDS: // A={? extends V} >> F={?} ---> 0
//									break;
//								case Wildcard.SUPER: // A={? super V} >> F={?} ---> 0
//									break;
//							}
//						} else { // A=V >> F={?} ---> 0
//						}
						break;
					case Wildcard.EXTENDS: // F={? extends U}
						if (otherType.isWildcard()) {
							WildcardBinding otherWildcard = (WildcardBinding) otherType;
							switch(otherWildcard.kind) {
								case Wildcard.UNBOUND: // A={?} >> F={? extends U}  --> 0
									break;
								case Wildcard.EXTENDS: // A={? extends V} >> F={? extends U} ---> V >> U
									this.bound.collectSubstitutes(scope, otherWildcard.bound, substitutes, CONSTRAINT_SUPER);
						        	for (int i = 0, length = otherWildcard.otherBounds == null ? 0 : otherWildcard.otherBounds.length; i < length; i++) {
										this.bound.collectSubstitutes(scope, otherWildcard.otherBounds[i], substitutes, CONSTRAINT_SUPER);
						        	}										
									break;
								case Wildcard.SUPER: // A={? super V} >> F={? extends U} ---> 0
									break;
							}
						} else { // A=V == F={? extends U} ---> 0
						}
						break;
					case Wildcard.SUPER: // F={? super U}
						if (otherType.isWildcard()) {
							WildcardBinding otherWildcard = (WildcardBinding) otherType;
							switch(otherWildcard.kind) {
								case Wildcard.UNBOUND: // A={?} >> F={? super U}  --> 0
									break;
								case Wildcard.EXTENDS: // A={? extends V} >> F={? super U} ---> 0
									break;
								case Wildcard.SUPER: // A={? super V} >> F={? super U} ---> V >> U
									this.bound.collectSubstitutes(scope, otherWildcard.bound, substitutes, CONSTRAINT_SUPER);
						        	for (int i = 0, length = otherWildcard.otherBounds == null ? 0 : otherWildcard.otherBounds.length; i < length; i++) {
										this.bound.collectSubstitutes(scope, otherWildcard.otherBounds[i], substitutes, CONSTRAINT_SUPER);
						        	}	
						        	break;
							}
						} else { // A=V >> F={? super U} ---> 0
						}						
						break;
				}
				break;
		}
	}
	
	public char[] computeUniqueKey() {
        switch (this.kind) {
            case Wildcard.UNBOUND : 
                return WILDCARD_STAR;
            case Wildcard.EXTENDS :
                return CharOperation.concat(WILDCARD_PLUS, this.bound.computeUniqueKey());
			default: // SUPER
			    return CharOperation.concat(WILDCARD_MINUS, this.bound.computeUniqueKey());
        }
       }
	
	/**
	 * @see org.eclipse.jdt.internal.compiler.lookup.TypeBinding#debugName()
	 */
	public String debugName() {
	    return toString();		
	}	
	
    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.compiler.lookup.TypeBinding#erasure()
     */
    public TypeBinding erasure() {
    	if (this.kind == Wildcard.EXTENDS)
	        return this.bound.erasure();
    	return typeVariable().erasure();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.compiler.lookup.TypeBinding#signature()
     */
    public char[] genericTypeSignature() {
        if (this.genericSignature == null) {
            switch (this.kind) {
                case Wildcard.UNBOUND : 
                    this.genericSignature = WILDCARD_STAR;
                    break;
                case Wildcard.EXTENDS :
                    this.genericSignature = CharOperation.concat(WILDCARD_PLUS, this.bound.genericTypeSignature());
					break;
				default: // SUPER
				    this.genericSignature = CharOperation.concat(WILDCARD_MINUS, this.bound.genericTypeSignature());
            }
        } 
        return this.genericSignature;
    }
    
	public int hashCode() {
		return this.genericType.hashCode();
	}

	void initialize(ReferenceBinding someGenericType, TypeBinding someBound, TypeBinding[] someOtherBounds) {
		this.genericType = someGenericType;
		this.bound = someBound;
		this.otherBounds = someOtherBounds;
		if (someGenericType != null) {
			this.fPackage = someGenericType.getPackage();
		}
		if (someBound != null) {
		    if (someBound.isTypeVariable())
		        this.tagBits |= HasTypeVariable;
		}
	}

	/**
     * @see org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding#isSuperclassOf(org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding)
     */
    public boolean isSuperclassOf(ReferenceBinding otherType) {
        if (this.kind == Wildcard.SUPER) {
            if (this.bound instanceof ReferenceBinding) {
                return ((ReferenceBinding) this.bound).isSuperclassOf(otherType);
            } else { // array bound
                return otherType.id == T_JavaLangObject;
            }
        }
        return false;
    }

    /**
	 * Returns true if the type is a wildcard
	 */
	public boolean isUnboundWildcard() {
	    return this.kind == Wildcard.UNBOUND;
	}
	
    /**
	 * Returns true if the type is a wildcard
	 */
	public boolean isWildcard() {
	    return true;
	}

    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.compiler.lookup.Binding#readableName()
     */
    public char[] readableName() {
        switch (this.kind) {
            case Wildcard.UNBOUND : 
                return WILDCARD_NAME;
            case Wildcard.EXTENDS :
            	if (this.otherBounds == null) 
	                return CharOperation.concat(WILDCARD_NAME, WILDCARD_EXTENDS, this.bound.readableName());
            	StringBuffer buffer = new StringBuffer(10);
            	buffer.append(this.bound.readableName());
            	for (int i = 0, length = this.otherBounds.length; i < length; i++) {
            		buffer.append('&').append(this.otherBounds[i].readableName());
            	}
            	int length;
				char[] result = new char[length = buffer.length()];
				buffer.getChars(0, length, result, 0);
				return result;	            	
			default: // SUPER
			    return CharOperation.concat(WILDCARD_NAME, WILDCARD_SUPER, this.bound.readableName());
        }
    }
    
	ReferenceBinding resolve() {
		if ((this.tagBits & HasUnresolvedTypeVariables) == 0)
			return this;

		this.tagBits &= ~HasUnresolvedTypeVariables;
		BinaryTypeBinding.resolveType(this.genericType, this.environment, null, 0);
	    switch(this.kind) {
	        case Wildcard.EXTENDS :
	        case Wildcard.SUPER :
				BinaryTypeBinding.resolveType(this.bound, this.environment, null, 0);
				break;
			case Wildcard.UNBOUND :
	    }
		return this;
	}
	
    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.compiler.lookup.Binding#shortReadableName()
     */
    public char[] shortReadableName() {
        switch (this.kind) {
            case Wildcard.UNBOUND : 
                return WILDCARD_NAME;
            case Wildcard.EXTENDS :
            	if (this.otherBounds == null) 
	                return CharOperation.concat(WILDCARD_NAME, WILDCARD_EXTENDS, this.bound.shortReadableName());
            	StringBuffer buffer = new StringBuffer(10);
            	buffer.append(this.bound.shortReadableName());
            	for (int i = 0, length = this.otherBounds.length; i < length; i++) {
            		buffer.append('&').append(this.otherBounds[i].shortReadableName());
            	}
            	int length;
				char[] result = new char[length = buffer.length()];
				buffer.getChars(0, length, result, 0);
				return result;	            	
			default: // SUPER
			    return CharOperation.concat(WILDCARD_NAME, WILDCARD_SUPER, this.bound.shortReadableName());
        }
    }
    
    /**
     * @see org.eclipse.jdt.internal.compiler.lookup.TypeBinding#signature()
     */
    public char[] signature() {
     	// should not be called directly on a wildcard; signature should only be asked on
    	// original methods or type erasures (which cannot denote wildcards at first level)
		if (this.signature == null) {
	        switch (this.kind) {
	            case Wildcard.EXTENDS :
	                return this.bound.signature();
				default: // SUPER | UNBOUND
				    return this.typeVariable().signature();
	        }        
		}
		return this.signature;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding#sourceName()
     */
    public char[] sourceName() {
        switch (this.kind) {
            case Wildcard.UNBOUND : 
                return WILDCARD_NAME;
            case Wildcard.EXTENDS :
                return CharOperation.concat(WILDCARD_NAME, WILDCARD_EXTENDS, this.bound.sourceName());
			default: // SUPER
			    return CharOperation.concat(WILDCARD_NAME, WILDCARD_SUPER, this.bound.sourceName());
        }        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.compiler.lookup.TypeVariableBinding#superclass()
     */
    public ReferenceBinding superclass() {
		if (this.superclass == null) {
			TypeBinding superType = null;
			if (this.kind == Wildcard.EXTENDS && !this.bound.isInterface()) {
				superType = this.bound;
			} else {
				TypeVariableBinding variable = this.typeVariable();
				if (variable != null) superType = variable.firstBound;
			}
			this.superclass = superType != null && !superType.isInterface()
				? (ReferenceBinding) superType
				: environment.getType(JAVA_LANG_OBJECT);
		}

		return this.superclass;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding#superInterfaces()
     */
    public ReferenceBinding[] superInterfaces() {
        if (this.superInterfaces == null) {
        	if (this.typeVariable() != null) {
        		this.superInterfaces = this.typeVariable.superInterfaces();
        	} else {
        		this.superInterfaces = NoSuperInterfaces;
        	}
			if (this.kind == Wildcard.EXTENDS) {
				if (this.bound.isInterface()) {
					// augment super interfaces with the wildcard bound
					int length = this.superInterfaces.length;
					System.arraycopy(this.superInterfaces, 0, this.superInterfaces = new ReferenceBinding[length+1], 1, length);
					this.superInterfaces[0] = (ReferenceBinding) this.bound; // make bound first
				}
				if (this.otherBounds != null) {
					// augment super interfaces with the wildcard otherBounds (interfaces per construction)
					int length = this.superInterfaces.length;
					int otherLength = this.otherBounds.length;
					System.arraycopy(this.superInterfaces, 0, this.superInterfaces = new ReferenceBinding[length+otherLength], 0, length);
					for (int i = 0; i < otherLength; i++) {
						this.superInterfaces[length+i] = (ReferenceBinding) this.otherBounds[i];
					}
				}
			}
        }
        return this.superInterfaces;
    }

	public void swapUnresolved(UnresolvedReferenceBinding unresolvedType, ReferenceBinding resolvedType, LookupEnvironment env) {
		boolean affected = false;
		if (this.genericType == unresolvedType) {
			this.genericType = resolvedType; // no raw conversion
			affected = true;
		} else if (this.bound == unresolvedType) {
			this.bound = resolvedType.isGenericType() ? env.createRawType(resolvedType, resolvedType.enclosingType()) : resolvedType;
			affected = true;
		}
		if (affected) 
			initialize(this.genericType, this.bound, this.otherBounds);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
        switch (this.kind) {
            case Wildcard.UNBOUND : 
                return new String(WILDCARD_NAME);
            case Wildcard.EXTENDS :
            	if (this.otherBounds == null)
                	return new String(CharOperation.concat(WILDCARD_NAME, WILDCARD_EXTENDS, this.bound.debugName().toCharArray()));
            	StringBuffer buffer = new StringBuffer(this.bound.debugName());
            	for (int i = 0, length = this.otherBounds.length; i < length; i++) {
            		buffer.append('&').append(this.otherBounds[i].debugName());
            	}
            	return buffer.toString();
			default: // SUPER
			    return new String(CharOperation.concat(WILDCARD_NAME, WILDCARD_SUPER, this.bound.debugName().toCharArray()));
        }        
	}		
	/**
	 * Returns associated type variable, or null in case of inconsistency
	 */
	public TypeVariableBinding typeVariable() {
		if (this.typeVariable == null) {
			TypeVariableBinding[] typeVariables = this.genericType.typeVariables();
			if (this.rank < typeVariables.length)
				this.typeVariable = typeVariables[this.rank];
		}
		return this.typeVariable;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6347.java