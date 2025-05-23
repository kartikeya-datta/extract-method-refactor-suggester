error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7658.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7658.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7658.java
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
 * Type declaration AST node type. A type declaration
 * is the union of a class declaration and an interface declaration.
 * For 2.0 (corresponding to JLS2):
 * <pre>
 * TypeDeclaration:
 * 		ClassDeclaration
 * 		InterfaceDeclaration
 * ClassDeclaration:
 *      [ Javadoc ] { Modifier } <b>class</b> Identifier
 *			[ <b>extends</b> Type]
 *			[ <b>implements</b> Type { <b>,</b> Type } ]
 *			<b>{</b> { ClassBodyDeclaration | <b>;</b> } <b>}</b>
 * InterfaceDeclaration:
 *      [ Javadoc ] { Modifier } <b>interface</b> Identifier
 *			[ <b>extends</b> Type { <b>,</b> Type } ]
 * 			<b>{</b> { InterfaceBodyDeclaration | <b>;</b> } <b>}</b>
 * </pre>
 * For 3.0 (corresponding to JLS3), type parameters and reified modifiers
 * (and annotations) were added, and the superclass type name and superinterface 
 * types names are generalized to type so that parameterized types can be 
 * referenced:
 * <pre>
 * TypeDeclaration:
 * 		ClassDeclaration
 * 		InterfaceDeclaration
 * ClassDeclaration:
 *      [ Javadoc ] { ExtendedModifier } <b>class</b> Identifier
 *			[ <b>&lt;</b> TypeParameter { <b>,</b> TypeParameter } <b>&gt;</b> ]
 *			[ <b>extends</b> Type ]
 *			[ <b>implements</b> Type { <b>,</b> Type } ]
 *			<b>{</b> { ClassBodyDeclaration | <b>;</b> } <b>}</b>
 * InterfaceDeclaration:
 *      [ Javadoc ] { ExtendedModifier } <b>interface</b> Identifier
 *			[ <b>&lt;</b> TypeParameter { <b>,</b> TypeParameter } <b>&gt;</b> ]
 *			[ <b>extends</b> Type { <b>,</b> Type } ]
 * 			<b>{</b> { InterfaceBodyDeclaration | <b>;</b> } <b>}</b>
 * </pre>
 * <p>
 * When a Javadoc comment is present, the source
 * range begins with the first character of the "/**" comment delimiter.
 * When there is no Javadoc comment, the source range begins with the first
 * character of the first modifier or annotation (if any), or the
 * first character of the "class" or "interface" keyword (if no
 * modifiers or annotations). The source range extends through the last character of the "}"
 * token following the body declarations.
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
public class TypeDeclaration extends AbstractTypeDeclaration {
	
	/**
	 * The "javadoc" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor JAVADOC_PROPERTY = 
		internalJavadocPropertyFactory(TypeDeclaration.class);

	/**
	 * The "modifiers" structural property of this node type (2.0 API only).
	 * @since 3.0
	 * TODO (jeem) - @deprecated Replaced by {@link #MODIFIERS2_PROPERTY} in the 3.0 API.
	 */
	public static final SimplePropertyDescriptor MODIFIERS_PROPERTY = 
		internalModifiersPropertyFactory(TypeDeclaration.class);
	
	/**
	 * The "modifiers" structural property of this node type (added in 3.0 API).
	 * @since 3.0
	 */
	public static final ChildListPropertyDescriptor MODIFIERS2_PROPERTY = 
		internalModifiers2PropertyFactory(TypeDeclaration.class);
	
	/**
	 * The "interface" structural property of this node type.
	 * @since 3.0
	 */
	public static final SimplePropertyDescriptor INTERFACE_PROPERTY = 
		new SimplePropertyDescriptor(TypeDeclaration.class, "interface", boolean.class, MANDATORY); //$NON-NLS-1$
	
	/**
	 * The "name" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor NAME_PROPERTY = 
		new ChildPropertyDescriptor(TypeDeclaration.class, "name", SimpleName.class, MANDATORY, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "superclass" structural property of this node type (2.0 API only).
	 * @since 3.0
	 * TODO (jeem) - @deprecated Replaced by {@link #SUPERCLASS_TYPE_PROPERTY} in the 3.0 API.
	 */
	public static final ChildPropertyDescriptor SUPERCLASS_PROPERTY = 
		new ChildPropertyDescriptor(TypeDeclaration.class, "superclass", Name.class, OPTIONAL, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "superInterfaces" structural property of this node type (2.0 API only).
	 * @since 3.0
	 * TODO (jeem) - @deprecated Replaced by {@link #SUPER_INTERFACE_TYPES_PROPERTY} in the 3.0 API.
	 */
	public static final ChildListPropertyDescriptor SUPER_INTERFACES_PROPERTY = 
		new ChildListPropertyDescriptor(TypeDeclaration.class, "superInterfaces", Name.class, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "superclassType" structural property of this node type (added in 3.0 API).
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor SUPERCLASS_TYPE_PROPERTY = 
		new ChildPropertyDescriptor(TypeDeclaration.class, "superclassType", Type.class, OPTIONAL, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "superInterfaceTypes" structural property of this node type (added in 3.0 API).
	 * @since 3.0
	 */
	public static final ChildListPropertyDescriptor SUPER_INTERFACE_TYPES_PROPERTY = 
		new ChildListPropertyDescriptor(TypeDeclaration.class, "superInterfaceTypes", Type.class, NO_CYCLE_RISK); //$NON-NLS-1$
	
	/**
	 * The "typeParameters" structural property of this node type (added in 3.0 API).
	 * @since 3.0
	 */
	public static final ChildListPropertyDescriptor TYPE_PARAMETERS_PROPERTY = 
		new ChildListPropertyDescriptor(TypeDeclaration.class, "typeParameters", TypeParameter.class, NO_CYCLE_RISK); //$NON-NLS-1$
	
	/**
	 * The "bodyDeclarations" structural property of this node type (added in 3.0 API).
	 * @since 3.0
	 */
	public static final ChildListPropertyDescriptor BODY_DECLARATIONS_PROPERTY = 
		internalBodyDeclarationPropertyFactory(TypeDeclaration.class);
	
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
		createPropertyList(TypeDeclaration.class);
		addProperty(JAVADOC_PROPERTY);
		addProperty(MODIFIERS_PROPERTY);
		addProperty(INTERFACE_PROPERTY);
		addProperty(NAME_PROPERTY);
		addProperty(SUPERCLASS_PROPERTY);
		addProperty(SUPER_INTERFACES_PROPERTY);
		addProperty(BODY_DECLARATIONS_PROPERTY);
		PROPERTY_DESCRIPTORS_2_0 = reapPropertyList();
		
		createPropertyList(TypeDeclaration.class);
		addProperty(JAVADOC_PROPERTY);
		addProperty(MODIFIERS2_PROPERTY);
		addProperty(INTERFACE_PROPERTY);
		addProperty(NAME_PROPERTY);
		addProperty(TYPE_PARAMETERS_PROPERTY);
		addProperty(SUPERCLASS_TYPE_PROPERTY);
		addProperty(SUPER_INTERFACE_TYPES_PROPERTY);
		addProperty(BODY_DECLARATIONS_PROPERTY);
		PROPERTY_DESCRIPTORS_3_0 = reapPropertyList();
	}

	/**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 * 
	 * @param apiLevel the API level; one of the
	 * <code>AST.LEVEL_*</code>LEVEL

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
	 * <code>true</code> for an interface, <code>false</code> for a class.
	 * Defaults to class.
	 */
	private boolean isInterface = false;
	
	/**
	 * The type paramters (element type: <code>TypeParameter</code>). 
	 * Null in 2.0. Added in 3.0; defaults to an empty list
	 * (see constructor).
	 * @since 3.0
	 */
	private ASTNode.NodeList typeParameters = null;

	/**
	 * The optional superclass name; <code>null</code> if none.
	 * Defaults to none. Note that this field is not used for
	 * interface declarations. Not used in 3.0.
	 */
	private Name optionalSuperclassName = null;

	/**
	 * The superinterface names (element type: <code>Name</code>). 
	 * 2.0 only; defaults to an empty list. Not used in 3.0.
	 * (see constructor).
	 * 
	 */
	private ASTNode.NodeList superInterfaceNames = null;

	/**
	 * The optional superclass type; <code>null</code> if none.
	 * Defaults to none. Note that this field is not used for
	 * interface declarations. Null in 2.0. Added in 3.0.
	 * @since 3.0
	 */
	private Type optionalSuperclassType = null;

	/**
	 * The superinterface types (element type: <code>Type</code>). 
	 * Null in 2.0. Added in 3.0; defaults to an empty list
	 * (see constructor).
	 * @since 3.0
	 */
	private ASTNode.NodeList superInterfaceTypes = null;

	/**
	 * Creates a new AST node for a type declaration owned by the given 
	 * AST. By default, the type declaration is for a class of an
	 * unspecified, but legal, name; no modifiers; no javadoc; 
	 * no type parameters; no superclass or superinterfaces; and an empty list
	 * of body declarations.
	 * <p>
	 * N.B. This constructor is package-private; all subclasses must be 
	 * declared in the same package; clients are unable to declare 
	 * additional subclasses.
	 * </p>
	 * 
	 * @param ast the AST that is to own this node
	 */
	TypeDeclaration(AST ast) {
		super(ast);
		if (ast.apiLevel == AST.LEVEL_2_0) {
			this.superInterfaceNames = new ASTNode.NodeList(SUPER_INTERFACES_PROPERTY);
		}
		if (ast.apiLevel >= AST.LEVEL_3_0) {
			this.typeParameters = new ASTNode.NodeList(TYPE_PARAMETERS_PROPERTY);
			this.superInterfaceTypes = new ASTNode.NodeList(SUPER_INTERFACE_TYPES_PROPERTY);
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
		// allow default implementation to flag the error
		return super.internalGetSetIntProperty(property, get, value);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final boolean internalGetSetBooleanProperty(SimplePropertyDescriptor property, boolean get, boolean value) {
		if (property == INTERFACE_PROPERTY) {
			if (get) {
				return isInterface();
			} else {
				setInterface(value);
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
		if (property == SUPERCLASS_PROPERTY) {
			if (get) {
				return getSuperclass();
			} else {
				setSuperclass((Name) child);
				return null;
			}
		}
		if (property == SUPERCLASS_TYPE_PROPERTY) {
			if (get) {
				return getSuperclassType();
			} else {
				setSuperclassType((Type) child);
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
		if (property == SUPER_INTERFACES_PROPERTY) {
			return superInterfaces();
		}
		if (property == SUPER_INTERFACE_TYPES_PROPERTY) {
			return superInterfaceTypes();
		}
		if (property == BODY_DECLARATIONS_PROPERTY) {
			return bodyDeclarations();
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
	 * Method declared on AbstractTypeDeclaration.
	 */
	final ChildPropertyDescriptor internalNameProperty() {
		return NAME_PROPERTY;
	}

	/* (omit javadoc for this method)
	 * Method declared on AbstractTypeDeclaration.
	 */
	final ChildListPropertyDescriptor internalBodyDeclarationsProperty() {
		return BODY_DECLARATIONS_PROPERTY;
	}


	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	public int getNodeType() {
		return TYPE_DECLARATION;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone(AST target) {
		TypeDeclaration result = new TypeDeclaration(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setJavadoc(
			(Javadoc) ASTNode.copySubtree(target, getJavadoc()));
		if (this.ast.apiLevel == AST.LEVEL_2_0) {
			result.setModifiers(getModifiers());
			result.setSuperclass(
					(Name) ASTNode.copySubtree(target, getSuperclass()));
			result.superInterfaces().addAll(
					ASTNode.copySubtrees(target, superInterfaces()));
		}
		result.setInterface(isInterface());
		result.setName((SimpleName) getName().clone(target));
		if (this.ast.apiLevel >= AST.LEVEL_3_0) {
			result.modifiers().addAll(ASTNode.copySubtrees(target, modifiers()));
			result.typeParameters().addAll(
					ASTNode.copySubtrees(target, typeParameters()));
			result.setSuperclassType(
					(Type) ASTNode.copySubtree(target, getSuperclassType()));
			result.superInterfaceTypes().addAll(
					ASTNode.copySubtrees(target, superInterfaceTypes()));
		}
		result.bodyDeclarations().addAll(
			ASTNode.copySubtrees(target, bodyDeclarations()));
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
			if (this.ast.apiLevel == AST.LEVEL_2_0) {
				acceptChild(visitor, getJavadoc());
				acceptChild(visitor, getName());
				acceptChild(visitor, getSuperclass());
				acceptChildren(visitor, this.superInterfaceNames);
				acceptChildren(visitor, this.bodyDeclarations);
			}
			if (this.ast.apiLevel >= AST.LEVEL_3_0) {
				acceptChild(visitor, getJavadoc());
				acceptChildren(visitor, this.modifiers);
				acceptChild(visitor, getName());
				acceptChildren(visitor, this.typeParameters);
				acceptChild(visitor, getSuperclassType());
				acceptChildren(visitor, this.superInterfaceTypes);
				acceptChildren(visitor, this.bodyDeclarations);
			}
		}
		visitor.endVisit(this);
	}
	
	/**
	 * Returns whether this type declaration declares a class or an 
	 * interface.
	 * 
	 * @return <code>true</code> if this is an interface declaration,
	 *    and <code>false</code> if this is a class declaration
	 */ 
	public boolean isInterface() {
		return this.isInterface;
	}
	
	/**
	 * Sets whether this type declaration declares a class or an 
	 * interface.
	 * 
	 * @param isInterface <code>true</code> if this is an interface
	 *    declaration, and <code>false</code> if this is a class
	 * 	  declaration
	 */ 
	public void setInterface(boolean isInterface) {
		preValueChange(INTERFACE_PROPERTY);
		this.isInterface = isInterface;
		postValueChange(INTERFACE_PROPERTY);
	}

	/**
	 * Returns the live ordered list of type parameters of this type 
	 * declaration (added in 3.0 API). This list is non-empty for parameterized types.
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
	 * Returns the name of the superclass declared in this type
	 * declaration, or <code>null</code> if there is none (2.0 API only).
	 * <p>
	 * Note that this child is not relevant for interface 
	 * declarations (although it does still figure in subtree
	 * equality comparisons).
	 * </p>
	 * 
	 * @return the superclass name node, or <code>null</code> if 
	 *    there is none
	 * @exception UnsupportedOperationException if this operation is used in
	 * an AST later than 2.0
	 * TBD (jeem ) - deprecated In the 3.0 API, this method is replaced by
	 * <code>getSuperclassType</code>,
	 * which returns a <code>Type</code> instead of a <code>Name</code>.
	 */ 
	public Name getSuperclass() {
	    supportedOnlyIn2();
		return this.optionalSuperclassName;
	}

	/**
	* Returns the superclass declared in this type
	* declaration, or <code>null</code> if there is none (added in 3.0 API).
	* <p>
	* Note that this child is not relevant for interface 
	* declarations (although it does still figure in subtree
	* equality comparisons).
	* </p>
	* 
	* @return the superclass type node, or <code>null</code> if 
	*    there is none
	* @exception UnsupportedOperationException if this operation is used in
	* a 2.0 AST
	* @since 3.0
	*/ 
	public Type getSuperclassType() {
	    unsupportedIn2();
		return this.optionalSuperclassType;
	}

	/**
	 * Sets or clears the name of the superclass declared in this type
	 * declaration (2.0 API only).
	 * <p>
	 * Note that this child is not relevant for interface 
	 * declarations (although it does still figure in subtree
	 * equality comparisons).
	 * </p>
	 * 
	 * @param superclassName the superclass name node, or <code>null</code> if 
	 *    there is none
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 * @exception UnsupportedOperationException if this operation is used in
	 * an AST later than 2.0
	 * TBD (jeem ) deprecated In the 3.0 API, this method is replaced by <code>setType</code>,
	 * which expects a <code>Type</code> instead of a <code>Name</code>.
	 */ 
	public void setSuperclass(Name superclassName) {
	    supportedOnlyIn2();
		ASTNode oldChild = this.optionalSuperclassName;
		preReplaceChild(oldChild, superclassName, SUPERCLASS_PROPERTY);
		this.optionalSuperclassName = superclassName;
		postReplaceChild(oldChild, superclassName, SUPERCLASS_PROPERTY);
	}

	/**
	 * Sets or clears the superclass declared in this type
	 * declaration (added in 3.0 API).
	 * <p>
	 * Note that this child is not relevant for interface declarations
	 * (although it does still figure in subtree equality comparisons).
	 * </p>
	 * 
	 * @param superclassType the superclass type node, or <code>null</code> if 
	 *    there is none
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 * @exception UnsupportedOperationException if this operation is used in
	 * a 2.0 AST
	 * @since 3.0
	 */ 
	public void setSuperclassType(Type superclassType) {
	    unsupportedIn2();
		ASTNode oldChild = this.optionalSuperclassType;
		preReplaceChild(oldChild, superclassType, SUPERCLASS_TYPE_PROPERTY);
		this.optionalSuperclassType = superclassType;
		postReplaceChild(oldChild, superclassType, SUPERCLASS_TYPE_PROPERTY);
 	}

	/**
	 * Returns the live ordered list of names of superinterfaces of this type 
	 * declaration (2.0 API only). For a class declaration, these are the names
	 * of the interfaces that this class implements; for an interface
	 * declaration, these are the names of the interfaces that this interface
	 * extends.
	 * 
	 * @return the live list of interface names
	 *    (element type: <code>Name</code>)
	 * @exception UnsupportedOperationException if this operation is used in
	 * an AST later than 2.0
	 * TBD (jeem ) - deprecated In the 3.0 API, this method is replaced by 
	 * <code>superInterfaceTypes()</code>
	 */ 
	public List superInterfaces() {
		// more efficient than just calling supportedOnlyIn2() to check
		if (this.superInterfaceNames == null) {
			supportedOnlyIn2();
		}
		return this.superInterfaceNames;
	}
	
	/**
	 * Returns the live ordered list of superinterfaces of this type 
	 * declaration (added in 3.0 API). For a class declaration, these are the interfaces
	 * that this class implements; for an interface declaration,
	 * these are the interfaces that this interface extends.
	 * 
	 * @return the live list of interface types
	 *    (element type: <code>Type</code>)
	 * @exception UnsupportedOperationException if this operation is used in
	 * a 2.0 AST
	 * @since 3.0
	 */ 
	public List superInterfaceTypes() {
		// more efficient than just calling unsupportedIn2() to check
		if (this.superInterfaceTypes == null) {
			unsupportedIn2();
		}
		return this.superInterfaceTypes;
	}
	
	/**
	 * Returns the ordered list of field declarations of this type 
	 * declaration. For a class declaration, these are the
	 * field declarations; for an interface declaration, these are
	 * the constant declarations.
	 * <p>
	 * This convenience method returns this node's body declarations
	 * with non-fields filtered out. Unlike <code>bodyDeclarations</code>,
	 * this method does not return a live result.
	 * </p>
	 * 
	 * @return the (possibly empty) list of field declarations
	 */ 
	public FieldDeclaration[] getFields() {
		List bd = bodyDeclarations();
		int fieldCount = 0;
		for (Iterator it = bd.listIterator(); it.hasNext(); ) {
			if (it.next() instanceof FieldDeclaration) {
				fieldCount++;
			}
		}
		FieldDeclaration[] fields = new FieldDeclaration[fieldCount];
		int next = 0;
		for (Iterator it = bd.listIterator(); it.hasNext(); ) {
			Object decl = it.next();
			if (decl instanceof FieldDeclaration) {
				fields[next++] = (FieldDeclaration) decl;
			}
		}
		return fields;
	}

	/**
	 * Returns the ordered list of method declarations of this type 
	 * declaration.
	 * <p>
	 * This convenience method returns this node's body declarations
	 * with non-methods filtered out. Unlike <code>bodyDeclarations</code>,
	 * this method does not return a live result.
	 * </p>
	 * 
	 * @return the (possibly empty) list of method (and constructor) 
	 *    declarations
	 */ 
	public MethodDeclaration[] getMethods() {
		List bd = bodyDeclarations();
		int methodCount = 0;
		for (Iterator it = bd.listIterator(); it.hasNext(); ) {
			if (it.next() instanceof MethodDeclaration) {
				methodCount++;
			}
		}
		MethodDeclaration[] methods = new MethodDeclaration[methodCount];
		int next = 0;
		for (Iterator it = bd.listIterator(); it.hasNext(); ) {
			Object decl = it.next();
			if (decl instanceof MethodDeclaration) {
				methods[next++] = (MethodDeclaration) decl;
			}
		}
		return methods;
	}

	/**
	 * Returns the ordered list of member type declarations of this type 
	 * declaration.
	 * <p>
	 * This convenience method returns this node's body declarations
	 * with non-types filtered out. Unlike <code>bodyDeclarations</code>,
	 * this method does not return a live result.
	 * </p>
	 * 
	 * @return the (possibly empty) list of member type declarations
	 */ 
	public TypeDeclaration[] getTypes() {
		List bd = bodyDeclarations();
		int typeCount = 0;
		for (Iterator it = bd.listIterator(); it.hasNext(); ) {
			if (it.next() instanceof TypeDeclaration) {
				typeCount++;
			}
		}
		TypeDeclaration[] memberTypes = new TypeDeclaration[typeCount];
		int next = 0;
		for (Iterator it = bd.listIterator(); it.hasNext(); ) {
			Object decl = it.next();
			if (decl instanceof TypeDeclaration) {
				memberTypes[next++] = (TypeDeclaration) decl;
			}
		}
		return memberTypes;
	}

	/**
	 * Resolves and returns the binding for the class or interface declared in
	 * this type declaration.
	 * <p>
	 * Note that bindings are generally unavailable unless requested when the
	 * AST is being built.
	 * </p>
	 * 
	 * @return the binding, or <code>null</code> if the binding cannot be 
	 *    resolved
	 */	
	public ITypeBinding resolveBinding() {
		return this.ast.getBindingResolver().resolveType(this);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	void appendDebugString(StringBuffer buffer) {
		buffer.append("TypeDeclaration["); //$NON-NLS-1$
		buffer.append(isInterface()
		   ? "interface " //$NON-NLS-1$
		   : "class "); //$NON-NLS-2$//$NON-NLS-1$
		buffer.append(getName().getIdentifier());
		buffer.append(" "); //$NON-NLS-1$
		for (Iterator it = bodyDeclarations().iterator(); it.hasNext();) {
			BodyDeclaration d = (BodyDeclaration) it.next();
			d.appendDebugString(buffer);
			if (it.hasNext()) {
				buffer.append(";"); //$NON-NLS-1$
			}
		}
		buffer.append("]"); //$NON-NLS-1$
	}
		
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		return super.memSize() + 6 * 4;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int treeSize() {
		return memSize()
			+ (this.optionalDocComment == null ? 0 : getJavadoc().treeSize())
			+ (this.modifiers == null ? 0 : this.modifiers.listSize())
			+ (this.typeName == null ? 0 : getName().treeSize())
			+ (this.typeParameters == null ? 0 : this.typeParameters.listSize())
			+ (this.optionalSuperclassName == null ? 0 : getSuperclass().treeSize())
			+ (this.optionalSuperclassType == null ? 0 : getSuperclassType().treeSize())
			+ (this.superInterfaceNames == null ? 0 : this.superInterfaceNames.listSize())
			+ (this.superInterfaceTypes == null ? 0 : this.superInterfaceTypes.listSize())
			+ this.bodyDeclarations.listSize();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7658.java