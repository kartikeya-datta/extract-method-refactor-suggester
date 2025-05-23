error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9755.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9755.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9755.java
text:
```scala
r@@esult.setSourceRange(getStartPosition(), getLength());

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jdt.core.dom;

import java.util.ArrayList;
import java.util.List;

/**
 * Local type declaration statement AST node type.
 * <p>
 * This kind of node is used to convert a type declaration
 * node into a statement node by wrapping it.
 * </p>
 * For JLS2:
 * <pre>
 * TypeDeclarationStatement:
 *    TypeDeclaration
 * </pre>
 * For JLS3, the kinds of type declarations grew to include enum declarations:
 * <pre>
 * TypeDeclarationStatement:
 *    TypeDeclaration
 *    EnumDeclaration
 * </pre>
 * Although allowed at the AST, not all arrangements of AST nodes are meaningful;
 * in particular, only class and enum declarations are meaningful in the context of 
 * a block.
 * 
 * @since 2.0
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public class TypeDeclarationStatement extends Statement {
	
	/**
	 * The "typeDeclaration" structural property of this node type (JLS2 API only).
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor TYPE_DECLARATION_PROPERTY = 
		new ChildPropertyDescriptor(TypeDeclarationStatement.class, "typeDeclaration", TypeDeclaration.class, MANDATORY, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "declaration" structural property of this node type (added in JLS3 API).
	 * @since 3.1
	 */
	public static final ChildPropertyDescriptor DECLARATION_PROPERTY = 
		new ChildPropertyDescriptor(TypeDeclarationStatement.class, "declaration", AbstractTypeDeclaration.class, MANDATORY, CYCLE_RISK); //$NON-NLS-1$

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
	 * @since 3.1
	 */
	private static final List PROPERTY_DESCRIPTORS_3_0;
	
	static {
		List propertyList = new ArrayList(2);
		createPropertyList(TypeDeclarationStatement.class, propertyList);
		addProperty(TYPE_DECLARATION_PROPERTY, propertyList);
		PROPERTY_DESCRIPTORS_2_0 = reapPropertyList(propertyList);
		
		propertyList = new ArrayList(2);
		createPropertyList(TypeDeclarationStatement.class, propertyList);
		addProperty(DECLARATION_PROPERTY, propertyList);
		PROPERTY_DESCRIPTORS_3_0 = reapPropertyList(propertyList);
	}

	/**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 * 
	 * @param apiLevel the API level; one of the
	 * <code>AST.JLS*</code> constants

	 * @return a list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor})
	 * @since 3.0
	 */
	public static List propertyDescriptors(int apiLevel) {
		if (apiLevel == AST.JLS2_INTERNAL) {
			return PROPERTY_DESCRIPTORS_2_0;
		} else {
			return PROPERTY_DESCRIPTORS_3_0;
		}
	}
			
	/**
	 * The type declaration; lazily initialized; defaults to a unspecified, 
	 * but legal, type declaration. In JLS2, corresponds to TYPE_DECLARATION_PROPERTY.
     * After JLS2, corresponds to DECLARATION_PROPERTY.
     * @see #typeDeclProperty
	 */
	private AbstractTypeDeclaration typeDecl = null;
    
    /**
     * The child property stored on the <code>typeDecl</code> instance variable.
     * In JLS2, corresponds to TYPE_DECLARATION_PROPERTY. After JLS2, corresponds to 
     * DECLARATION_PROPERTY.
     * 
     * @return the property corresponding to the <code>typeDecl</code> instance variable;
     * never <code>null</code>
     */
    private ChildPropertyDescriptor typeDeclProperty () {
        if (getAST().apiLevel() == AST.JLS2_INTERNAL) {
            return TYPE_DECLARATION_PROPERTY;
        } else {
            return DECLARATION_PROPERTY;
        }
    }


	/**
	 * Creates a new unparented local type declaration statement node owned 
	 * by the given AST. By default, the local type declaration is an
	 * unspecified, but legal, type declaration.
	 * <p>
	 * N.B. This constructor is package-private.
	 * </p>
	 * 
	 * @param ast the AST that is to own this node
	 */
	TypeDeclarationStatement(AST ast) {
		super(ast);
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
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == TYPE_DECLARATION_PROPERTY) {
			if (get) {
				return getTypeDeclaration();
			} else {
				setTypeDeclaration((TypeDeclaration) child);
				return null;
			}
		}
		if (property == DECLARATION_PROPERTY) {
			if (get) {
				return getDeclaration();
			} else {
				setDeclaration((AbstractTypeDeclaration) child);
				return null;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetChildProperty(property, get, child);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final int getNodeType0() {
		return TYPE_DECLARATION_STATEMENT;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone0(AST target) {
		TypeDeclarationStatement result = 
			new TypeDeclarationStatement(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.copyLeadingComment(this);
		result.setDeclaration(
			(AbstractTypeDeclaration) getDeclaration().clone(target));
		return result;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final boolean subtreeMatch0(ASTMatcher matcher, Object other) {
		// dispatch to correct overloaded match method
		return matcher.match(this, other);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	void accept0(ASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren) {
			acceptChild(visitor, getDeclaration());
		}
		visitor.endVisit(this);
	}
	
	/**
	 * Returns the abstract type declaration of this local type declaration
	 * statement (added in JLS3 API).
	 * 
	 * @return the type declaration node
	 * @since 3.1
	 */ 
	public AbstractTypeDeclaration getDeclaration() {
		if (this.typeDecl == null) {
			// lazy init must be thread-safe for readers
			synchronized (this) {
				if (this.typeDecl == null) {
					preLazyInit();
					this.typeDecl = new TypeDeclaration(this.ast);
					postLazyInit(this.typeDecl, typeDeclProperty());
				}
			}
		}
		return this.typeDecl;
	}
		
	/**
	 * Sets the abstract type declaration of this local type declaration
	 * statement (added in JLS3 API).
	 * 
	 * @param decl the type declaration node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 * @since 3.1
	 */ 
	public void setDeclaration(AbstractTypeDeclaration decl) {
		if (decl == null) {
			throw new IllegalArgumentException();
		}
		// a TypeDeclarationStatement may occur inside an 
		// TypeDeclaration - must check cycles
		ASTNode oldChild = this.typeDecl;
		ChildPropertyDescriptor typeDeclProperty = typeDeclProperty();
		preReplaceChild(oldChild, decl, typeDeclProperty);
		this.typeDecl= decl;
		postReplaceChild(oldChild, decl, typeDeclProperty);
	}
	
	/**
	 * Returns the type declaration of this local type declaration
	 * statement (JLS2 API only).
	 * 
	 * @return the type declaration node
	 * @exception UnsupportedOperationException if this operation is used in
	 * an AST later than JLS2
	 * @deprecated In the JLS3 API, this method is replaced by 
	 * {@link #getDeclaration()}, which returns <code>AbstractTypeDeclaration</code>
	 * instead of <code>TypeDeclaration</code>.
	 */ 
	public TypeDeclaration getTypeDeclaration() {
		return internalGetTypeDeclaration();
	}
	
	/**
	 * Internal synonym for deprecated method. Used to avoid
	 * deprecation warnings.
	 * @since 3.1
	 */
	/*package*/ final TypeDeclaration internalGetTypeDeclaration() {
		supportedOnlyIn2();
		return (TypeDeclaration) getDeclaration();
	}
		
	/**
	 * Sets the type declaration of this local type declaration
	 * statement (JLS2 API only).
	 * 
	 * @param decl the type declaration node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 * @exception UnsupportedOperationException if this operation is used in
	 * an AST later than JLS2
     * @deprecated In the JLS3 API, this method is replaced by 
     * {@link #setDeclaration(AbstractTypeDeclaration)} which takes
     * <code>AbstractTypeDeclaration</code> instead of
     * <code>TypeDeclaration</code>.
	 */ 
	public void setTypeDeclaration(TypeDeclaration decl) {
		internalSetTypeDeclaration(decl);
	}
	
	/**
	 * Internal synonym for deprecated method. Used to avoid
	 * deprecation warnings.
	 * @since 3.1
	 */
	/*package*/ final void internalSetTypeDeclaration(TypeDeclaration decl) {
	    supportedOnlyIn2();
		// forward to non-deprecated replacement method
		setDeclaration(decl);
	}
	
	/**
	 * Resolves and returns the binding for the class or interface declared in
	 * this type declaration statement.
	 * <p>
	 * Note that bindings are generally unavailable unless requested when the
	 * AST is being built.
	 * </p>
	 * 
	 * @return the binding, or <code>null</code> if the binding cannot be 
	 *    resolved
	 */	
	public ITypeBinding resolveBinding() {
		// forward request to the wrapped type declaration
		AbstractTypeDeclaration d = getDeclaration();
		if (d instanceof TypeDeclaration) {
			return ((TypeDeclaration) d).resolveBinding();
		} else if (d instanceof AnnotationTypeDeclaration) {
			return ((AnnotationTypeDeclaration) d).resolveBinding();
		} else {
			// shouldn't happen
			return null;
		}
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		return super.memSize() + 1 * 4;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int treeSize() {
		return
			memSize()
			+ (this.typeDecl == null ? 0 : getDeclaration().treeSize());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9755.java