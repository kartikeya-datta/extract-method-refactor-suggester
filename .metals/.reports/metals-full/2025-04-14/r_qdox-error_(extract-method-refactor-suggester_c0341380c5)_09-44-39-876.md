error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9017.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9017.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9017.java
text:
```scala
A@@STNode clone0(AST target) {

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

package org.eclipse.jdt.core.dom;

import java.util.Iterator;
import java.util.List;

/**
 * Method declaration AST node type. A method declaration
 * is the union of a method declaration and a constructor declaration.
 * For 2.0 (corresponding to JLS2):
 * <pre>
 * MethodDeclaration:
 *    [ Javadoc ] { Modifier } ( Type | <b>void</b> ) Identifier <b>(</b>
 *        [ FormalParameter 
 * 		     { <b>,</b> FormalParameter } ] <b>)</b> {<b>[</b> <b>]</b> }
 *        [ <b>throws</b> TypeName { <b>,</b> TypeName } ] ( Block | <b>;</b> )
 * ConstructorDeclaration:
 *    [ Javadoc ] { Modifier } Identifier <b>(</b>
 * 		  [ FormalParameter
 * 			 { <b>,</b> FormalParameter } ] <b>)</b>
 *        [<b>throws</b> TypeName { <b>,</b> TypeName } ] Block
 * </pre>
 * For 3.0 (corresponding to JLS3), type parameters and reified modifiers
 * (and annotations) were added:
 * <pre>
 * MethodDeclaration:
 *    [ Javadoc ] { ExtendedModifier }
 *		  [ <b>&lt;</b> TypeParameter { <b>,</b> TypeParameter } <b>&gt;</b> ]
 *        ( Type | <b>void</b> ) Identifier <b>(</b>
 *        [ FormalParameter 
 * 		     { <b>,</b> FormalParameter } ] <b>)</b> {<b>[</b> <b>]</b> }
 *        [ <b>throws</b> TypeName { <b>,</b> TypeName } ] ( Block | <b>;</b> )
 * ConstructorDeclaration:
 *    [ Javadoc ] { ExtendedModifier } Identifier <b>(</b>
 * 		  [ FormalParameter
 * 			 { <b>,</b> FormalParameter } ] <b>)</b>
 *        [<b>throws</b> TypeName { <b>,</b> TypeName } ] Block
 * </pre>
 * <p>
 * When a Javadoc comment is present, the source
 * range begins with the first character of the "/**" comment delimiter.
 * When there is no Javadoc comment, the source range begins with the first
 * character of the first modifier keyword (if modifiers), or the
 * first character of the "&lt;" token (method, no modifiers, type parameters), 
 * or the first character of the return type (method, no modifiers, no type
 * parameters), or the first character of the identifier (constructor, 
 * no modifiers). The source range extends through the last character of the
 * ";" token (if no body), or the last character of the block (if body).
 * </p>
 * <p>
 * Note: Support for generic types is an experimental language feature 
 * under discussion in JSR-014 and under consideration for inclusion
 * in the 1.5 release of J2SE. The support here is therefore tentative
 * and subject to change.
 * </p>
 *
 * @since 2.0 
 */
public class MethodDeclaration extends BodyDeclaration {
	
	/**
	 * The "javadoc" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor JAVADOC_PROPERTY = 
		internalJavadocPropertyFactory(MethodDeclaration.class);

	/**
	 * The "modifiers" structural property of this node type (2.0 API only).
	 * @since 3.0
	 * TODO (jeem) - @deprecated Replaced by {@link #MODIFIERS2_PROPERTY} in the 3.0 API.
	 */
	public static final SimplePropertyDescriptor MODIFIERS_PROPERTY = 
		internalModifiersPropertyFactory(MethodDeclaration.class);
	
	/**
	 * The "modifiers" structural property of this node type (added in 3.0 API).
	 * @since 3.0
	 */
	public static final ChildListPropertyDescriptor MODIFIERS2_PROPERTY = 
		internalModifiers2PropertyFactory(MethodDeclaration.class);
	
	/**
	 * The "constructor" structural property of this node type.
	 * @since 3.0
	 */
	public static final SimplePropertyDescriptor CONSTRUCTOR_PROPERTY = 
		new SimplePropertyDescriptor(MethodDeclaration.class, "constructor", boolean.class, MANDATORY); //$NON-NLS-1$
	
	/**
	 * The "name" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor NAME_PROPERTY = 
		new ChildPropertyDescriptor(MethodDeclaration.class, "name", SimpleName.class, MANDATORY, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "returnType" structural property of this node type (2.0 API only).
	 * @since 3.0
	 * TODO (jeem) - @deprecated Replaced by {@link #RETURN_TYPE2_PROPERTY} in the 3.0 API.
	 */
	public static final ChildPropertyDescriptor RETURN_TYPE_PROPERTY = 
		new ChildPropertyDescriptor(MethodDeclaration.class, "returnType", Type.class, MANDATORY, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "returnType2" structural property of this node type (added in 3.0 API).
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor RETURN_TYPE2_PROPERTY = 
		new ChildPropertyDescriptor(MethodDeclaration.class, "returnType2", Type.class, OPTIONAL, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "extraDimensions" structural property of this node type.
	 * @since 3.0
	 */
	public static final SimplePropertyDescriptor EXTRA_DIMENSIONS_PROPERTY = 
		new SimplePropertyDescriptor(MethodDeclaration.class, "extraDimensions", int.class, MANDATORY); //$NON-NLS-1$
	
	/**
	 * The "typeParameters" structural property of this node type (added in 3.0 API).
	 * @since 3.0
	 */
	public static final ChildListPropertyDescriptor TYPE_PARAMETERS_PROPERTY = 
		new ChildListPropertyDescriptor(MethodDeclaration.class, "typeParameters", TypeParameter.class, NO_CYCLE_RISK); //$NON-NLS-1$
	
	/**
	 * The "parameters" structural property of this node type).
	 * @since 3.0
	 */
	public static final ChildListPropertyDescriptor PARAMETERS_PROPERTY = 
		new ChildListPropertyDescriptor(MethodDeclaration.class, "parameters", SingleVariableDeclaration.class, CYCLE_RISK); //$NON-NLS-1$
	
	/**
	 * The "thrownExceptions" structural property of this node type).
	 * @since 3.0
	 */
	public static final ChildListPropertyDescriptor THROWN_EXCEPTIONS_PROPERTY = 
		new ChildListPropertyDescriptor(MethodDeclaration.class, "thrownExceptions", Name.class, NO_CYCLE_RISK); //$NON-NLS-1$
	
	/**
	 * The "body" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor BODY_PROPERTY = 
		new ChildPropertyDescriptor(MethodDeclaration.class, "body", Block.class, OPTIONAL, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 * @since 3.0
	 */
	private static final List PROPERTY_DESCRIPTORS_2_0;
	
	/**
	 * A list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 * @since 3.0
	 */
	private static final List PROPERTY_DESCRIPTORS_3_0;
	
	static {
		createPropertyList(MethodDeclaration.class);
		addProperty(JAVADOC_PROPERTY);
		addProperty(MODIFIERS_PROPERTY);
		addProperty(CONSTRUCTOR_PROPERTY);
		addProperty(RETURN_TYPE_PROPERTY);
		addProperty(NAME_PROPERTY);
		addProperty(PARAMETERS_PROPERTY);
		addProperty(EXTRA_DIMENSIONS_PROPERTY);
		addProperty(THROWN_EXCEPTIONS_PROPERTY);
		addProperty(BODY_PROPERTY);
		PROPERTY_DESCRIPTORS_2_0 = reapPropertyList();
		
		createPropertyList(MethodDeclaration.class);
		addProperty(JAVADOC_PROPERTY);
		addProperty(MODIFIERS2_PROPERTY);
		addProperty(CONSTRUCTOR_PROPERTY);
		addProperty(TYPE_PARAMETERS_PROPERTY);
		addProperty(RETURN_TYPE2_PROPERTY);
		addProperty(NAME_PROPERTY);
		addProperty(PARAMETERS_PROPERTY);
		addProperty(EXTRA_DIMENSIONS_PROPERTY);
		addProperty(THROWN_EXCEPTIONS_PROPERTY);
		addProperty(BODY_PROPERTY);
		PROPERTY_DESCRIPTORS_3_0 = reapPropertyList();
	}

	/**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 * 
	 * @param apiLevel the API level; one of the AST.LEVEL_* constants
	 * @return a list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor})
	 * @since 3.0
	 */
	public static List propertyDescriptors(int apiLevel) {
		if (apiLevel == AST.LEVEL_2_0) {
			return PROPERTY_DESCRIPTORS_2_0;
		} else {
			return PROPERTY_DESCRIPTORS_3_0;
		}
	}
			
	/**
	 * <code>true</code> for a constructor, <code>false</code> for a method.
	 * Defaults to method.
	 */
	private boolean isConstructor = false;
	
	/**
	 * The method name; lazily initialized; defaults to an unspecified,
	 * legal Java identifier.
	 */
	private SimpleName methodName = null;

	/**
	 * The parameter declarations 
	 * (element type: <code>SingleVariableDeclaration</code>).
	 * Defaults to an empty list.
	 */
	private ASTNode.NodeList parameters =
		new ASTNode.NodeList(PARAMETERS_PROPERTY);
	
	/**
	 * The return type.
	 * 2.0 bahevior: lazily initialized; defaults to void.
	 * 3.0 behavior; lazily initialized; defaults to void; null allowed.
	 * Note that this field is ignored for constructor declarations.
	 */
	private Type returnType = null;
	
	/**
	 * Indicated whether the return type has been initialized.
	 * @sicne 3.0
	 */
	private boolean returnType2Initialized = false;
	
	/**
	 * The type paramters (element type: <code>TypeParameter</code>). 
	 * Null in 2.0. Added in 3.0; defaults to an empty list
	 * (see constructor).
	 * @since 3.0
	 */
	private ASTNode.NodeList typeParameters = null;

	/**
	 * The number of array dimensions that appear after the parameters, rather
	 * than after the return type itself; defaults to 0.
	 * 
	 * @since 2.1
	 */
	private int extraArrayDimensions = 0;

	/**
	 * The list of thrown exception names (element type: <code>Name</code>).
	 * Defaults to an empty list.
	 */
	private ASTNode.NodeList thrownExceptions =
		new ASTNode.NodeList(THROWN_EXCEPTIONS_PROPERTY);

	/**
	 * The method body, or <code>null</code> if none.
	 * Defaults to none.
	 */
	private Block optionalBody = null;
	
	/**
	 * Creates a new AST node for a method declaration owned 
	 * by the given AST. By default, the declaration is for a method of an
	 * unspecified, but legal, name; no modifiers; no javadoc; no type 
	 * parameters; void return type; no parameters; no array dimensions after 
	 * the parameters; no thrown exceptions; and no body (as opposed to an
	 * empty body).
	 * <p>
	 * N.B. This constructor is package-private; all subclasses must be 
	 * declared in the same package; clients are unable to declare 
	 * additional subclasses.
	 * </p>
	 * 
	 * @param ast the AST that is to own this node
	 */
	MethodDeclaration(AST ast) {
		super(ast);
		if (ast.apiLevel >= AST.LEVEL_3_0) {
			this.typeParameters = new ASTNode.NodeList(TYPE_PARAMETERS_PROPERTY);
		}
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 * @since 3.0
	 */
	final List internalStructuralPropertiesForType(int apiLevel) {
		return propertyDescriptors(apiLevel);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final int internalGetSetIntProperty(SimplePropertyDescriptor property, boolean get, int value) {
		if (property == MODIFIERS_PROPERTY) {
			if (get) {
				return getModifiers();
			} else {
				setModifiers(value);
				return 0;
			}
		}
		if (property == EXTRA_DIMENSIONS_PROPERTY) {
			if (get) {
				return getExtraDimensions();
			} else {
				setExtraDimensions(value);
				return 0;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetIntProperty(property, get, value);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final boolean internalGetSetBooleanProperty(SimplePropertyDescriptor property, boolean get, boolean value) {
		if (property == CONSTRUCTOR_PROPERTY) {
			if (get) {
				return isConstructor();
			} else {
				setConstructor(value);
				return false;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetBooleanProperty(property, get, value);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == JAVADOC_PROPERTY) {
			if (get) {
				return getJavadoc();
			} else {
				setJavadoc((Javadoc) child);
				return null;
			}
		}
		if (property == NAME_PROPERTY) {
			if (get) {
				return getName();
			} else {
				setName((SimpleName) child);
				return null;
			}
		}
		if (property == RETURN_TYPE_PROPERTY) {
			if (get) {
				return getReturnType();
			} else {
				setReturnType((Type) child);
				return null;
			}
		}
		if (property == RETURN_TYPE2_PROPERTY) {
			if (get) {
				return getReturnType2();
			} else {
				setReturnType2((Type) child);
				return null;
			}
		}
		if (property == BODY_PROPERTY) {
			if (get) {
				return getBody();
			} else {
				setBody((Block) child);
				return null;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetChildProperty(property, get, child);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final List internalGetChildListProperty(ChildListPropertyDescriptor property) {
		if (property == MODIFIERS2_PROPERTY) {
			return modifiers();
		}
		if (property == TYPE_PARAMETERS_PROPERTY) {
			return typeParameters();
		}
		if (property == PARAMETERS_PROPERTY) {
			return parameters();
		}
		if (property == THROWN_EXCEPTIONS_PROPERTY) {
			return thrownExceptions();
		}
		// allow default implementation to flag the error
		return super.internalGetChildListProperty(property);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on BodyDeclaration.
	 */
	final ChildPropertyDescriptor internalJavadocProperty() {
		return JAVADOC_PROPERTY;
	}

	/* (omit javadoc for this method)
	 * Method declared on BodyDeclaration.
	 */
	final ChildListPropertyDescriptor internalModifiers2Property() {
		return MODIFIERS2_PROPERTY;
	}

	/* (omit javadoc for this method)
	 * Method declared on BodyDeclaration.
	 */
	final SimplePropertyDescriptor internalModifiersProperty() {
		return MODIFIERS_PROPERTY;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	public int getNodeType() {
		return METHOD_DECLARATION;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone(AST target) {
		MethodDeclaration result = new MethodDeclaration(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setJavadoc(
			(Javadoc) ASTNode.copySubtree(target, getJavadoc()));
		if (this.ast.apiLevel == AST.LEVEL_2_0) {
			result.setModifiers(getModifiers());
			result.setReturnType(
					(Type) ASTNode.copySubtree(target, getReturnType()));
		}
		if (this.ast.apiLevel >= AST.LEVEL_3_0) {
			result.modifiers().addAll(ASTNode.copySubtrees(target, modifiers()));
			result.typeParameters().addAll(
					ASTNode.copySubtrees(target, typeParameters()));
			result.setReturnType2(
					(Type) ASTNode.copySubtree(target, getReturnType2()));
		}
		result.setConstructor(isConstructor());
		result.setExtraDimensions(getExtraDimensions());
		result.setName((SimpleName) getName().clone(target));
		result.parameters().addAll(
			ASTNode.copySubtrees(target, parameters()));
		result.thrownExceptions().addAll(
			ASTNode.copySubtrees(target, thrownExceptions()));
		result.setBody(
			(Block) ASTNode.copySubtree(target, getBody()));
		return result;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	public boolean subtreeMatch(ASTMatcher matcher, Object other) {
		// dispatch to correct overloaded match method
		return matcher.match(this, other);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	void accept0(ASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren) {
			// visit children in normal left to right reading order
			acceptChild(visitor, getJavadoc());
			if (this.ast.apiLevel == AST.LEVEL_2_0) {
				acceptChild(visitor, getReturnType());
			} else {
				acceptChildren(visitor, this.modifiers);
				acceptChildren(visitor, this.typeParameters);
				acceptChild(visitor, getReturnType2());
			}
			// n.b. visit return type even for constructors
			acceptChild(visitor, getName());
			acceptChildren(visitor, this.parameters);
			acceptChildren(visitor, this.thrownExceptions);
			acceptChild(visitor, getBody());
		}
		visitor.endVisit(this);
	}
	
	/**
	 * Returns whether this declaration declares a constructor or a method.
	 * 
	 * @return <code>true</code> if this is a constructor declaration,
	 *    and <code>false</code> if this is a method declaration
	 */ 
	public boolean isConstructor() {
		return this.isConstructor;
	}
	
	/**
	 * Sets whether this declaration declares a constructor or a method.
	 * 
	 * @param isConstructor <code>true</code> for a constructor declaration,
	 *    and <code>false</code> for a method declaration
	 */ 
	public void setConstructor(boolean isConstructor) {
		preValueChange(CONSTRUCTOR_PROPERTY);
		this.isConstructor = isConstructor;
		postValueChange(CONSTRUCTOR_PROPERTY);
	}

	/**
	 * Returns the live ordered list of type parameters of this method
	 * declaration (added in 3.0 API). This list is non-empty for parameterized methods.
	 * <p>
	 * Note that these children are not relevant for constructor declarations
	 * (although it does still figure in subtree equality comparisons
	 * and visits), and is devoid of the binding information ordinarily
	 * available.
	 * </p>
	 * <p>
	 * Note: Support for generic types is an experimental language feature 
	 * under discussion in JSR-014 and under consideration for inclusion
	 * in the 1.5 release of J2SE. The support here is therefore tentative
	 * and subject to change.
	 * </p>
	 * 
	 * @return the live list of type parameters
	 *    (element type: <code>TypeParameter</code>)
	 * @exception UnsupportedOperationException if this operation is used in
	 * a 2.0 AST
	 * @since 3.0
	 */ 
	public List typeParameters() {
		// more efficient than just calling unsupportedIn2() to check
		if (this.typeParameters == null) {
			unsupportedIn2();
		}
		return this.typeParameters;
	}
	
	/**
	 * Returns the name of the method declared in this method declaration.
	 * For a constructor declaration, this should be the same as the name 
	 * of the class.
	 * 
	 * @return the method name node
	 */ 
	public SimpleName getName() {
		if (this.methodName == null) {
			preLazyInit();
			this.methodName = new SimpleName(this.ast);
			postLazyInit(this.methodName, NAME_PROPERTY);
		}
		return this.methodName;
	}
	
	/**
	 * Sets the name of the method declared in this method declaration to the
	 * given name. For a constructor declaration, this should be the same as 
	 * the name of the class.
	 * 
	 * @param methodName the new method name
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 */ 
	public void setName(SimpleName methodName) {
		if (methodName == null) {
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.methodName;
		preReplaceChild(oldChild, methodName, NAME_PROPERTY);
		this.methodName = methodName;
		postReplaceChild(oldChild, methodName, NAME_PROPERTY);
	}

	/**
	 * Returns the live ordered list of method parameter declarations for this
	 * method declaration.
	 * 
	 * @return the live list of method parameter declarations
	 *    (element type: <code>SingleVariableDeclaration</code>)
	 */ 
	public List parameters() {
		return this.parameters;
	}
	
	/**
	 * Returns whether this method declaration declares a
	 * variable arity method (added in 3.0 API). The convenience method checks
	 * whether the last parameter is so marked.
	 * <p>
	 * Note: Varible arity methods are an experimental language feature 
	 * under discussion in JSR-201 and under consideration for inclusion
	 * in the 1.5 release of J2SE. The support here is therefore tentative
	 * and subject to change.
	 * </p>
	 * 
	 * @return <code>true</code> if this is a variable arity method declaration,
	 *    and <code>false</code> otherwise
	 * @exception UnsupportedOperationException if this operation is used in
	 * a 2.0 AST
	 * @see SingleVariableDeclaration#isVarargs()
	 * @since 3.0
	 */ 
	public boolean isVarargs() {
		// more efficient than just calling unsupportedIn2() to check
		if (this.modifiers == null) {
			unsupportedIn2();
		}
		if (parameters().isEmpty()) {
			return false;
		} else {
			SingleVariableDeclaration v = (SingleVariableDeclaration) parameters().get(parameters().size() - 1);
			return v.isVarargs();
		}
	}
	
	/**
	 * @since 3.0
	 * @deprecated Renamed isVarargs
	 * TODO (jeem) - Remove before M9
	 */ 
	public boolean isVariableArity() {
		return isVarargs();
	}
	
	/**
	 * Returns the live ordered list of thrown exception names in this method 
	 * declaration.
	 * 
	 * @return the live list of exception names
	 *    (element type: <code>Name</code>)
	 */ 
	public List thrownExceptions() {
		return this.thrownExceptions;
	}
	
	/**
	 * Returns the return type of the method declared in this method 
	 * declaration, exclusive of any extra array dimensions (2.0 API only). 
	 * This is one of the few places where the void type is meaningful.
	 * <p>
	 * Note that this child is not relevant for constructor declarations
	 * (although, it does still figure in subtree equality comparisons
	 * and visits), and is devoid of the binding information ordinarily
	 * available.
	 * </p>
	 * 
	 * @return the return type, possibly the void primitive type
	 * @exception UnsupportedOperationException if this operation is used in
	 * an AST later than 2.0
	 * TBD (jeem ) - deprecated In the 3.0 API, this method is replaced by
	 * <code>getReturnType2</code>, which may return <code>null</code>.
	 */ 
	public Type getReturnType() {
	    supportedOnlyIn2();
		if (this.returnType == null) {
			preLazyInit();
			this.returnType = this.ast.newPrimitiveType(PrimitiveType.VOID);
			postLazyInit(this.returnType, RETURN_TYPE_PROPERTY);
		}
		return this.returnType;
	}

	/**
	 * Sets the return type of the method declared in this method declaration
	 * to the given type, exclusive of any extra array dimensions (2.0 API only). This is one
	 * of the few places where the void type is meaningful.
	 * <p>
	 * Note that this child is not relevant for constructor declarations
	 * (although it does still figure in subtree equality comparisons and visits).
	 * </p>
	 * 
	 * @param type the new return type, possibly the void primitive type
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 * @exception UnsupportedOperationException if this operation is used in
	 * an AST later than 2.0
	 * TBD (jeem ) - deprecated In the 3.0 API, this method is replaced by
	 * <code>setReturnType2</code>, which accepts <code>null</code>.
	 */ 
	public void setReturnType(Type type) {
	    supportedOnlyIn2();
		if (type == null) {
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.returnType;
		preReplaceChild(oldChild, type, RETURN_TYPE_PROPERTY);
		this.returnType = type;
		postReplaceChild(oldChild, type, RETURN_TYPE_PROPERTY);
	}

	/**
	 * Returns the return type of the method declared in this method 
	 * declaration, exclusive of any extra array dimensions (added in 3.0 API). 
	 * This is one of the few places where the void type is meaningful.
	 * <p>
	 * Note that this child is not relevant for constructor declarations
	 * (although, if present, it does still figure in subtree equality comparisons
	 * and visits), and is devoid of the binding information ordinarily
	 * available. In the 2.0 API, the return type is mandatory. 
	 * In the 3.0 API, the return type is optional.
	 * </p>
	 * 
	 * @return the return type, possibly the void primitive type,
	 * or <code>null</code> if none
	 * @exception UnsupportedOperationException if this operation is used in
	 * a 2.0 AST
	 * @since 3.0
	 */ 
	public Type getReturnType2() {
	    unsupportedIn2();
		if (this.returnType == null && !this.returnType2Initialized) {
			preLazyInit();
			this.returnType = this.ast.newPrimitiveType(PrimitiveType.VOID);
			this.returnType2Initialized = true;
			postLazyInit(this.returnType, RETURN_TYPE2_PROPERTY);
		}
		return this.returnType;
	}

	/**
	 * Sets the return type of the method declared in this method declaration
	 * to the given type, exclusive of any extra array dimensions (added in 3.0 API).
	 * This is one of the few places where the void type is meaningful.
	 * <p>
	 * Note that this child is not relevant for constructor declarations
	 * (although it does still figure in subtree equality comparisons and visits).
	 * In the 2.0 API, the return type is mandatory. 
	 * In the 3.0 API, the return type is optional.
	 * </p>
	 * 
	 * @param type the new return type, possibly the void primitive type,
	 * or <code>null</code> if none
	 * @exception UnsupportedOperationException if this operation is used in
	 * a 2.0 AST
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 * @since 3.0
	 */ 
	public void setReturnType2(Type type) {
	    unsupportedIn2();
		this.returnType2Initialized = true;
		ASTNode oldChild = this.returnType;
		preReplaceChild(oldChild, type, RETURN_TYPE2_PROPERTY);
		this.returnType = type;
		postReplaceChild(oldChild, type, RETURN_TYPE2_PROPERTY);
	}

	/**
	 * Returns the number of extra array dimensions over and above the 
	 * explicitly-specified return type.
	 * <p>
	 * For example, <code>int foo()[][]</code> has a return type of 
	 * <code>int</code> and two extra array dimensions; 
	 * <code>int[][] foo()</code> has a return type of <code>int[][]</code>
	 * and zero extra array dimensions. The two constructs have different
	 * ASTs, even though there are really syntactic variants of the same
	 * method declaration.
	 * </p>
	 * 
	 * @return the number of extra array dimensions
	 * @since 2.1
	 */ 
	public int getExtraDimensions() {
		return this.extraArrayDimensions;
	}

	/**
	 * Sets the number of extra array dimensions over and above the 
	 * explicitly-specified return type.
	 * <p>
	 * For example, <code>int foo()[][]</code> is rendered as a return
	 * type of <code>int</code> with two extra array dimensions; 
	 * <code>int[][] foo()</code> is rendered as a return type of 
	 * <code>int[][]</code> with zero extra array dimensions. The two
	 * constructs have different ASTs, even though there are really syntactic
	 * variants of the same method declaration.
	 * </p>
	 * 
	 * @param dimensions the number of array dimensions
	 * @exception IllegalArgumentException if the number of dimensions is
	 *    negative
	 * @since 2.1
	 */ 
	public void setExtraDimensions(int dimensions) {
		if (dimensions < 0) {
			throw new IllegalArgumentException();
		}
		preValueChange(EXTRA_DIMENSIONS_PROPERTY);
		this.extraArrayDimensions = dimensions;
		postValueChange(EXTRA_DIMENSIONS_PROPERTY);
	}

	/**
	 * Returns the body of this method declaration, or <code>null</code> if 
	 * this method has <b>no</b> body.
	 * <p>
	 * Note that there is a subtle difference between having no body and having
	 * an empty body ("{}").
	 * </p>
	 * 
	 * @return the method body, or <code>null</code> if this method has no
	 *    body
	 */ 
	public Block getBody() {
		return this.optionalBody;
	}

	/**
	 * Sets or clears the body of this method declaration.
	 * <p>
	 * Note that there is a subtle difference between having no body 
	 * (as in <code>"void foo();"</code>) and having an empty body (as in
	 * "void foo() {}"). Abstract methods, and methods declared in interfaces,
	 * have no body. Non-abstract methods, and all constructors, have a body.
	 * </p>
	 * 
	 * @param body the block node, or <code>null</code> if 
	 *    there is none
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */ 
	public void setBody(Block body) {
		// a MethodDeclaration may occur in a Block - must check cycles
		ASTNode oldChild = this.optionalBody;
		preReplaceChild(oldChild, body, BODY_PROPERTY);
		this.optionalBody = body;
		postReplaceChild(oldChild, body, BODY_PROPERTY);
	}

	/**
	 * Resolves and returns the binding for the method or constructor declared
	 * in this method or constructor declaration.
	 * <p>
	 * Note that bindings are generally unavailable unless requested when the
	 * AST is being built.
	 * </p>
	 * 
	 * @return the binding, or <code>null</code> if the binding cannot be 
	 *    resolved
	 */	
	public IMethodBinding resolveBinding() {
		return this.ast.getBindingResolver().resolveMethod(this);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	void appendDebugString(StringBuffer buffer) {
		buffer.append("MethodDeclaration[");//$NON-NLS-1$
		buffer.append(isConstructor() ? "constructor " : "method ");//$NON-NLS-2$//$NON-NLS-1$
		buffer.append(getName().getIdentifier());
		buffer.append("(");//$NON-NLS-1$
		for (Iterator it = parameters().iterator(); it.hasNext(); ) {
			SingleVariableDeclaration d = (SingleVariableDeclaration) it.next();
			d.getType().appendPrintString(buffer);
			if (it.hasNext()) {
				buffer.append(";");//$NON-NLS-1$
			}
		}
		buffer.append(")");//$NON-NLS-1$
		buffer.append("]");//$NON-NLS-1$
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		return super.memSize() + 9 * 4;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int treeSize() {
		return
			memSize()
			+ (this.optionalDocComment == null ? 0 : getJavadoc().treeSize())
			+ (this.modifiers == null ? 0 : this.modifiers.listSize())
			+ (this.typeParameters == null ? 0 : this.typeParameters.listSize())
			+ (this.methodName == null ? 0 : getName().treeSize())
			+ (this.returnType == null ? 0 : this.returnType.treeSize())
			+ this.parameters.listSize()
			+ this.thrownExceptions.listSize()
			+ (this.optionalBody == null ? 0 : getBody().treeSize());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9017.java