error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2584.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2584.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2584.java
text:
```scala
r@@eturn  (annotationTypes!=null);

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


package org.aspectj.weaver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aspectj.bridge.ISourceLocation;
import org.aspectj.weaver.Member.Kind;

/**
 * This is the declared member, i.e. it will always correspond to an
 * actual method/... declaration
 */
public class ResolvedMemberImpl extends MemberImpl implements IHasPosition, AnnotatedElement, TypeVariableDeclaringElement, ResolvedMember {
    
    public String[] parameterNames = null;
    protected UnresolvedType[] checkedExceptions = UnresolvedType.NONE;
    /**
     * if this member is a parameterized version of a member in a generic type,
     * then this field holds a reference to the member we parameterize.
     */
    protected ResolvedMember backingGenericMember = null;
        
    protected Set annotationTypes = null;
	// Some members are 'created' to represent other things (for example ITDs).  These
	// members have their annotations stored elsewhere, and this flag indicates that is
	// the case.  It is up to the caller to work out where that is!
	// Once determined the caller may choose to stash the annotations in this member...
	private boolean isAnnotatedElsewhere = false; // this field is not serialized.
	private boolean isAjSynthetic = true;
    
    // generic methods have type variables
	private UnresolvedType[] typeVariables;
    
    // these three fields hold the source location of this member
	protected int start, end;
	protected ISourceContext sourceContext = null;
    
    //XXX deprecate this in favor of the constructor below
	public ResolvedMemberImpl(
		Kind kind,
		UnresolvedType declaringType,
		int modifiers,
		UnresolvedType returnType,
		String name,
		UnresolvedType[] parameterTypes)
	{
		super(kind, declaringType, modifiers, returnType, name, parameterTypes);
	}

    
    
	public ResolvedMemberImpl(
		Kind kind,
		UnresolvedType declaringType,
		int modifiers,
		UnresolvedType returnType,
		String name,
		UnresolvedType[] parameterTypes,
		UnresolvedType[] checkedExceptions) 
	{
		super(kind, declaringType, modifiers, returnType, name, parameterTypes);
		this.checkedExceptions = checkedExceptions;
	}
    
	public ResolvedMemberImpl(
			Kind kind,
			UnresolvedType declaringType,
			int modifiers,
			UnresolvedType returnType,
			String name,
			UnresolvedType[] parameterTypes,
			UnresolvedType[] checkedExceptions,
			ResolvedMember backingGenericMember) 
		{
			this(kind, declaringType, modifiers, returnType, name, parameterTypes,checkedExceptions);
			this.backingGenericMember = backingGenericMember;
			this.isAjSynthetic = backingGenericMember.isAjSynthetic();
		}
	
	public ResolvedMemberImpl(
		Kind kind,
		UnresolvedType declaringType,
		int modifiers,
		String name,
		String signature) 
	{
		super(kind, declaringType, modifiers, name, signature);
	}    
	
    /**
     * Compute the full set of signatures for a member. This walks up the hierarchy
     * giving the ResolvedMember in each defining type in the hierarchy. A shadowMember
     * can be created with a target type (declaring type) that does not actually define
     * the member. This is ok as long as the member is inherited in the declaring type.
     * Each declaring type in the line to the actual declaring type is added as an additional
     * signature. For example:
     * 
     * class A { void foo(); }
     * class B extends A {}
     * 
     * shadowMember : void B.foo()
     * 
     * gives  { void B.foo(), void A.foo() }
     * @param joinPointSignature
     * @param inAWorld
     */
    public static JoinPointSignature[] getJoinPointSignatures(Member joinPointSignature, World inAWorld) {
    	
    	// Walk up hierarchy creating one member for each type up to and including the
    	// first defining type
    	ResolvedType originalDeclaringType = joinPointSignature.getDeclaringType().resolve(inAWorld);
    	ResolvedMemberImpl firstDefiningMember = (ResolvedMemberImpl) joinPointSignature.resolve(inAWorld);
    	if (firstDefiningMember == null) {
    		return new JoinPointSignature[0];
    	} 
    	// declaringType can be unresolved if we matched a synthetic member generated by Aj...
    	// should be fixed elsewhere but add this resolve call on the end for now so that we can
    	// focus on one problem at a time...
    	ResolvedType firstDefiningType = firstDefiningMember.getDeclaringType().resolve(inAWorld);
    	if (firstDefiningType != originalDeclaringType) {
    		if (joinPointSignature.getKind() == Member.CONSTRUCTOR) {
    			return new JoinPointSignature[0];
    		} 
//    		else if (shadowMember.isStatic()) {
//    			return new ResolvedMember[] {firstDefiningMember};
//    		}
    	}

    	List declaringTypes = new ArrayList();
    	accumulateTypesInBetween(originalDeclaringType, 
    							 firstDefiningType,
    							 declaringTypes);
    	List memberSignatures = new ArrayList();
    	for (Iterator iter = declaringTypes.iterator(); iter.hasNext();) {
			ResolvedType declaringType = (ResolvedType) iter.next();
			ResolvedMember member = firstDefiningMember.withSubstituteDeclaringType(declaringType);
			memberSignatures.add(member);
		}

    	if (shouldWalkUpHierarchyFor(firstDefiningMember)) {
	       	// now walk up the hierarchy from the firstDefiningMember and include the signature for
	    	// every type between the firstDefiningMember and the root defining member.
	    	Iterator superTypeIterator = firstDefiningType.getDirectSupertypes();
	    	List typesAlreadyVisited = new ArrayList();
	    	accumulateMembersMatching(firstDefiningMember,superTypeIterator,typesAlreadyVisited,memberSignatures);
    	}
    	
    	JoinPointSignature[] ret = new JoinPointSignature[memberSignatures.size()];
    	memberSignatures.toArray(ret);
    	return ret;
    }
    
    private static boolean shouldWalkUpHierarchyFor(Member aMember) {
    	if (aMember.getKind() == Member.CONSTRUCTOR) return false;
    	if (aMember.getKind() == Member.FIELD) return false;
    	if (aMember.isStatic()) return false;
    	return true;
    }
    
    /**
     * Build a list containing every type between subtype and supertype, inclusively. 
     */
    private static void accumulateTypesInBetween(ResolvedType subType, ResolvedType superType, List types) {
    	types.add(subType);
    	if (subType == superType) {
    		return;
    	} else {
    		for (Iterator iter = subType.getDirectSupertypes(); iter.hasNext();) {
				ResolvedType parent = (ResolvedType) iter.next();
				if (superType.isAssignableFrom(parent)) {
					accumulateTypesInBetween(parent, superType,types);
				}
			}
    	}
    }
    
    /**
     * We have a resolved member, possibly with type parameter references as parameters or return
     * type. We need to find all its ancestor members. When doing this, a type parameter matches
     * regardless of bounds (bounds can be narrowed down the hierarchy).
     */
    private static void accumulateMembersMatching(
    		ResolvedMemberImpl memberToMatch,
    		Iterator typesToLookIn,
    		List typesAlreadyVisited,
    		List foundMembers) {
    	while(typesToLookIn.hasNext()) {
    		ResolvedType toLookIn = (ResolvedType) typesToLookIn.next();
			if (!typesAlreadyVisited.contains(toLookIn)) {
				typesAlreadyVisited.add(toLookIn);
				ResolvedMemberImpl foundMember = (ResolvedMemberImpl) toLookIn.lookupResolvedMember(memberToMatch);
				if (foundMember != null) {
					List declaringTypes = new ArrayList();
					// declaring type can be unresolved if the member can from an ITD...
					ResolvedType resolvedDeclaringType = foundMember.getDeclaringType().resolve(toLookIn.getWorld());
					accumulateTypesInBetween(toLookIn, resolvedDeclaringType, declaringTypes);
				   	for (Iterator iter = declaringTypes.iterator(); iter.hasNext();) {
						ResolvedType declaringType = (ResolvedType) iter.next();
						typesAlreadyVisited.add(declaringType);
						ResolvedMember member = foundMember.withSubstituteDeclaringType(declaringType);
						foundMembers.add(member);
					}				   	
					if (toLookIn.isParameterizedType() && (foundMember.backingGenericMember != null)) {
						foundMembers.add(new JoinPointSignature(foundMember.backingGenericMember,foundMember.declaringType.resolve(toLookIn.getWorld())));
					}
					accumulateMembersMatching(foundMember,toLookIn.getDirectSupertypes(),typesAlreadyVisited,foundMembers);
					// if this was a parameterized type, look in the generic type that backs it too
				}	
			}
		}
    }

    
    // ----

    public final int getModifiers(World world) {
        return modifiers;
    }
    public final int getModifiers() {
        return modifiers;
    }

	// ----
	

    public final UnresolvedType[] getExceptions(World world) {
        return getExceptions();
    }
    
    public UnresolvedType[] getExceptions() {
        return checkedExceptions;
    }
    
    public ShadowMunger getAssociatedShadowMunger() {
		return null;
    }
    
    // ??? true or false?
    public boolean isAjSynthetic() {
    	return isAjSynthetic;
    }
	
	public boolean hasAnnotations() {
		return  (annotationTypes==null);
	}

    public boolean hasAnnotation(UnresolvedType ofType) {
         // The ctors don't allow annotations to be specified ... yet - but
        // that doesn't mean it is an error to call this method.
        // Normally the weaver will be working with subtypes of 
        // this type - BcelField/BcelMethod
        if (annotationTypes==null) return false;
		return annotationTypes.contains(ofType);
    }
    
    public ResolvedType[] getAnnotationTypes() {
    	// The ctors don't allow annotations to be specified ... yet - but
    	// that doesn't mean it is an error to call this method.
    	// Normally the weaver will be working with subtypes of
    	// this type - BcelField/BcelMethod
    	if (annotationTypes == null) return null;
		return (ResolvedType[])annotationTypes.toArray(new ResolvedType[]{});
    }
    
	public void setAnnotationTypes(UnresolvedType[] annotationtypes) {
		if (annotationTypes == null) annotationTypes = new HashSet();
		for (int i = 0; i < annotationtypes.length; i++) {
			UnresolvedType typeX = annotationtypes[i];
			annotationTypes.add(typeX);
		}
	}
	
	public void addAnnotation(AnnotationX annotation) {
   	    // FIXME asc only allows for annotation types, not instances - should it?
		if (annotationTypes == null) annotationTypes = new HashSet();
		annotationTypes.add(annotation.getSignature());
	}
	    
    public boolean isBridgeMethod() {
    	return (modifiers & Constants.ACC_BRIDGE)!=0;
    }
    
    public boolean isVarargsMethod() {
    	return (modifiers & Constants.ACC_VARARGS)!=0;
    }
    
	public boolean isSynthetic() {
		return false;
	}
    
    public void write(DataOutputStream s) throws IOException {
    	getKind().write(s);
    	getDeclaringType().write(s);
    	s.writeInt(modifiers);
    	s.writeUTF(getName());
    	s.writeUTF(getSignature());
		UnresolvedType.writeArray(getExceptions(), s);

		s.writeInt(getStart());
		s.writeInt(getEnd());

		// Write out any type variables...
		if (typeVariables==null) {
			s.writeInt(0);
		} else {
			s.writeInt(typeVariables.length);
			for (int i = 0; i < typeVariables.length; i++) {
				typeVariables[i].write(s);
			}
		}
    }

    public static void writeArray(ResolvedMember[] members, DataOutputStream s) throws IOException {
		s.writeInt(members.length);
		for (int i = 0, len = members.length; i < len; i++) {
			members[i].write(s);
		}
    }

    
    public static ResolvedMemberImpl readResolvedMember(VersionedDataInputStream s, ISourceContext sourceContext) throws IOException {
    	ResolvedMemberImpl m = new ResolvedMemberImpl(Kind.read(s), UnresolvedType.read(s), s.readInt(), s.readUTF(), s.readUTF());
		m.checkedExceptions = UnresolvedType.readArray(s);

		m.start = s.readInt();
		m.end = s.readInt();
		m.sourceContext = sourceContext;
		
		// Read in the type variables...
		if (s.getMajorVersion()>=AjAttribute.WeaverVersionInfo.WEAVER_VERSION_MAJOR_AJ150) {
			int tvcount = s.readInt();
			if (tvcount!=0) {
				m.typeVariables = new UnresolvedType[tvcount];
				for (int i=0;i<tvcount;i++) {
					m.typeVariables[i]=UnresolvedType.read(s);
				}
			}
		}
		return m;
    }
    
    public static ResolvedMember[] readResolvedMemberArray(VersionedDataInputStream s, ISourceContext context) throws IOException {
    	int len = s.readInt();
		ResolvedMember[] members = new ResolvedMember[len];
		for (int i=0; i < len; i++) {
			members[i] = ResolvedMemberImpl.readResolvedMember(s, context);
		}
		return members;
    }
    
    
    
	public ResolvedMember resolve(World world) {
        // make sure all the pieces of a resolvedmember really are resolved
        if (annotationTypes!=null) {
          Set r = new HashSet();
          for (Iterator iter = annotationTypes.iterator(); iter.hasNext();) {
			UnresolvedType element = (UnresolvedType) iter.next();
			r.add(world.resolve(element));
		  }
		  annotationTypes = r;
	    }
        declaringType = declaringType.resolve(world);
        if (declaringType.isRawType()) declaringType = ((ReferenceType)declaringType).getGenericType();
		if (typeVariables!=null && typeVariables.length>0) {
			for (int i = 0; i < typeVariables.length; i++) {
				UnresolvedType array_element = typeVariables[i];
				typeVariables[i] = typeVariables[i].resolve(world);
	}
		}
		if (parameterTypes!=null && parameterTypes.length>0) {
			for (int i = 0; i < parameterTypes.length; i++) {
				UnresolvedType array_element = parameterTypes[i];
				parameterTypes[i] = parameterTypes[i].resolve(world);
			}
		}
	
		returnType = returnType.resolve(world);return this;
	}
	
	public ISourceContext getSourceContext(World world) {
		return getDeclaringType().resolve(world).getSourceContext();
	}

	public final String[] getParameterNames() {
		return parameterNames;
	}
	public final String[] getParameterNames(World world) {
		return getParameterNames();
	}
	
	public AjAttribute.EffectiveSignatureAttribute getEffectiveSignature() {
		return null;
	}
	
    public ISourceLocation getSourceLocation() {
    	//System.out.println("get context: " + this + " is " + sourceContext);
    	if (sourceContext == null) {
    		//System.err.println("no context: " + this);
    		return null;
    	}
    	return sourceContext.makeSourceLocation(this);
    }
    
	public int getEnd() {
		return end;
	}

	public ISourceContext getSourceContext() {
		return sourceContext;
	}

	public int getStart() {
		return start;
	}

	public void setPosition(int sourceStart, int sourceEnd) {
		this.start = sourceStart;
		this.end = sourceEnd;
	}

	public void setSourceContext(ISourceContext sourceContext) {
		this.sourceContext = sourceContext;
	}
	
	public boolean isAbstract() {
		return Modifier.isAbstract(modifiers);
	}

	public boolean isPublic() {
		return Modifier.isPublic(modifiers);
	}
    
    public boolean isProtected() {
        return Modifier.isProtected(modifiers);   
    }
	
	public boolean isNative() {
		return Modifier.isNative(modifiers);
	}
    
    public boolean isDefault() {
        return !(isPublic() || isProtected() || isPrivate());
    }

	public boolean isVisible(ResolvedType fromType) {
		World world = fromType.getWorld();
		return ResolvedType.isVisible(getModifiers(), getDeclaringType().resolve(world),
					fromType);
	}
	public void setCheckedExceptions(UnresolvedType[] checkedExceptions) {
		this.checkedExceptions = checkedExceptions;
	}

	public void setAnnotatedElsewhere(boolean b) {
		isAnnotatedElsewhere = b;
	}

	public boolean isAnnotatedElsewhere() {
		return isAnnotatedElsewhere;
	}
	
	/**
	 * Get the UnresolvedType for the return type, taking generic signature into account
	 */
	public UnresolvedType getGenericReturnType() {
		return getReturnType();
	}
	
	/**
	 * Get the TypeXs of the parameter types, taking generic signature into account
	 */
	public UnresolvedType[] getGenericParameterTypes() {
		return getParameterTypes();
	}
	
	// return a resolved member in which all type variables in the signature of this
	// member have been replaced with the given bindings.
	// the isParameterized flag tells us whether we are creating a raw type version or not
	// if isParameterized List<T> will turn into List<String> (for example), 
	// but if !isParameterized List<T> will turn into List.
	public ResolvedMemberImpl parameterizedWith(UnresolvedType[] typeParameters,ResolvedType newDeclaringType, boolean isParameterized) {
		if (!this.getDeclaringType().isGenericType()) {
			throw new IllegalStateException("Can't ask to parameterize a member of a non-generic type");
		}
		TypeVariable[] typeVariables = getDeclaringType().getTypeVariables();
		if (typeVariables.length != typeParameters.length) {
			throw new IllegalStateException("Wrong number of type parameters supplied");
		}
		Map typeMap = new HashMap();
		for (int i = 0; i < typeVariables.length; i++) {
			typeMap.put(typeVariables[i].getName(), typeParameters[i]);
		}
		UnresolvedType parameterizedReturnType = parameterize(getGenericReturnType(),typeMap,isParameterized);
		UnresolvedType[] parameterizedParameterTypes = new UnresolvedType[getGenericParameterTypes().length];
		for (int i = 0; i < parameterizedParameterTypes.length; i++) {
			parameterizedParameterTypes[i] = 
				parameterize(getGenericParameterTypes()[i], typeMap,isParameterized);
		}
		return new ResolvedMemberImpl(
					getKind(),
					newDeclaringType,
					getModifiers(),
					parameterizedReturnType,
					getName(),
					parameterizedParameterTypes,
					getExceptions(),
					this
				);
	}
	
	
	public void setTypeVariables(UnresolvedType[] types) {
		typeVariables = types;
	}
	
	public UnresolvedType[] getTypeVariables() {
		return typeVariables;
	}
	
	private UnresolvedType parameterize(UnresolvedType aType, Map typeVariableMap, boolean inParameterizedType) {
		if (aType instanceof TypeVariableReferenceType) {
			String variableName = ((TypeVariableReferenceType)aType).getTypeVariable().getName();
			if (!typeVariableMap.containsKey(variableName)) {
				return aType; // if the type variable comes from the method (and not the type) thats OK
			}
			return (UnresolvedType) typeVariableMap.get(variableName);
		} else if (aType.isParameterizedType()) {
			if (inParameterizedType) {
				return aType.parameterize(typeVariableMap);
			} else {
				return aType.getRawType();
			}
		} 
		return aType;		
	}
	
	
	/**
	 * If this member is defined by a parameterized super-type, return the erasure
	 * of that member.
	 * For example:
	 * interface I<T> { T foo(T aTea); }
	 * class C implements I<String> {
	 *   String foo(String aString) { return "something"; }
	 * }
	 * The resolved member for C.foo has signature String foo(String). The
	 * erasure of that member is Object foo(Object)  -- use upper bound of type
	 * variable.
	 * A type is a supertype of itself.
	 */
	public ResolvedMember getErasure() {
		if (calculatedMyErasure) return myErasure;
		calculatedMyErasure = true;
		ResolvedType resolvedDeclaringType = (ResolvedType) getDeclaringType();
		// this next test is fast, and the result is cached.
		if (!resolvedDeclaringType.hasParameterizedSuperType()) {
			return null;
		} else {
			// we have one or more parameterized super types.
			// this member may be defined by one of them... we need to find out.
			Collection declaringTypes = this.getDeclaringTypes(resolvedDeclaringType.getWorld());
			for (Iterator iter = declaringTypes.iterator(); iter.hasNext();) {
				ResolvedType aDeclaringType = (ResolvedType) iter.next();
				if (aDeclaringType.isParameterizedType()) {
					// we've found the (a?) parameterized type that defines this member.
					// now get the erasure of it
					ResolvedMemberImpl matchingMember = (ResolvedMemberImpl) aDeclaringType.lookupMemberNoSupers(this);
					if (matchingMember != null && matchingMember.backingGenericMember != null) {
						myErasure = matchingMember.backingGenericMember;
						return myErasure;
					}
				}
			}
		}
		return null;
	}
	
	private ResolvedMember myErasure = null;
	private boolean calculatedMyErasure = false;
	
	
	/**
      * For ITDs, we use the default factory methods to build a resolved member, then alter a couple of characteristics
      * using this method - this is safe.
      */
	public void resetName(String newName) {this.name = newName;}
	public void resetKind(Kind newKind)   {this.kind=newKind;  }
    public void resetModifiers(int newModifiers) {this.modifiers=newModifiers;}

	public void resetReturnTypeToObjectArray() {
		returnType = UnresolvedType.OBJECTARRAY;
	}
		
	/**
	 * Returns a copy of this member but with the declaring type swapped.
	 * Copy only needs to be shallow.
	 * @param newDeclaringType
	 */
	private JoinPointSignature withSubstituteDeclaringType(ResolvedType newDeclaringType) {
		JoinPointSignature ret = new JoinPointSignature(this,newDeclaringType);
		return ret;
	}
	
	/**
	 * Returns true if this member matches the other. The matching takes into account
	 * name and parameter types only. When comparing parameter types, we allow any type
	 * variable to match any other type variable regardless of bounds.
	 */
	public boolean matches(ResolvedMember aCandidateMatch) {
		ResolvedMemberImpl candidateMatchImpl = (ResolvedMemberImpl)aCandidateMatch;
		if (!getName().equals(aCandidateMatch.getName())) return false;
		UnresolvedType[] myParameterTypes = getGenericParameterTypes();
		UnresolvedType[] candidateParameterTypes = aCandidateMatch.getGenericParameterTypes();
		if (myParameterTypes.length != candidateParameterTypes.length) return false;
		String myParameterSignature = getParameterSigWithBoundsRemoved();
		String candidateParameterSignature = candidateMatchImpl.getParameterSigWithBoundsRemoved();
		if (myParameterSignature.equals(candidateParameterSignature)) {
			return true;
		} else {
			// try erasure
			myParameterSignature = getParameterSigErasure();
			candidateParameterSignature = candidateMatchImpl.getParameterSigErasure();
			return myParameterSignature.equals(candidateParameterSignature);
		}
	}
	
	/** converts e.g. <T extends Number>.... List<T>  to just Ljava/util/List<T;>;
	 * whereas the full signature would be Ljava/util/List<T:Ljava/lang/Number;>;
	 */
	private String myParameterSignatureWithBoundsRemoved = null;
	/**
	 * converts e.g. <T extends Number>.... List<T>  to just Ljava/util/List;
	 */
	private String myParameterSignatureErasure = null;
	
	// does NOT produce a meaningful java signature, but does give a unique string suitable for
	// comparison.
	private String getParameterSigWithBoundsRemoved() {
		if (myParameterSignatureWithBoundsRemoved != null) return myParameterSignatureWithBoundsRemoved;
		StringBuffer sig = new StringBuffer();
		UnresolvedType[] myParameterTypes = getGenericParameterTypes();
		for (int i = 0; i < myParameterTypes.length; i++) {
			appendSigWithTypeVarBoundsRemoved(myParameterTypes[i], sig);
		}
		myParameterSignatureWithBoundsRemoved = sig.toString();
		return myParameterSignatureWithBoundsRemoved;
	}
	
	private String getParameterSigErasure() {
		if (myParameterSignatureErasure != null) return myParameterSignatureErasure;
		StringBuffer sig = new StringBuffer();
		UnresolvedType[] myParameterTypes = getParameterTypes();
		for (int i = 0; i < myParameterTypes.length; i++) {
			UnresolvedType thisParameter = myParameterTypes[i];
			if (thisParameter.isTypeVariableReference()) {
				sig.append(thisParameter.getUpperBound().getSignature());
			} else {
				sig.append(thisParameter.getSignature());
			}
		}
		myParameterSignatureErasure = sig.toString();
		return myParameterSignatureErasure;		
	}
	
	// does NOT produce a meaningful java signature, but does give a unique string suitable for
	// comparison.
	private void appendSigWithTypeVarBoundsRemoved(UnresolvedType aType, StringBuffer toBuffer) {
		if (aType.isTypeVariableReference()) {
			toBuffer.append("T;");
		} else if (aType.isParameterizedType()) {
			toBuffer.append(aType.getRawType().getSignature());
			toBuffer.append("<");
			for (int i = 0; i < aType.getTypeParameters().length; i++) {
				appendSigWithTypeVarBoundsRemoved(aType.getTypeParameters()[i], toBuffer);
			}
			toBuffer.append(">;");
		} else {
			toBuffer.append(aType.getSignature());
		}
	}
	
   public String toGenericString() {
    	StringBuffer buf = new StringBuffer();
    	buf.append(getGenericReturnType().getSimpleName());
    	buf.append(' ');
   		buf.append(declaringType.getName());
        buf.append('.');
   		buf.append(name);
    	if (kind != FIELD) {
    		buf.append("(");
    		UnresolvedType[] params = getGenericParameterTypes();
            if (params.length != 0) {
                buf.append(params[0].getSimpleName());
        		for (int i=1, len = params.length; i < len; i++) {
                    buf.append(", ");
        		    buf.append(params[i].getSimpleName());
        		}
            }
    		buf.append(")");
    	}
    	return buf.toString();    	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2584.java