error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8588.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8588.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8588.java
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

import java.util.List;

import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.internal.compiler.parser.Scanner;
import org.eclipse.jdt.internal.compiler.parser.TerminalTokens;

/**
 * AST node for a simple name. A simple name is an identifier other than
 * a keyword, boolean literal ("true", "false") or null literal ("null").
 * <pre>
 * SimpleName:
 *     Identifier
 * </pre>
 * 
 * @since 2.0
 */
public class SimpleName extends Name {

	/**
	 * The "identifier" structural property of this node type.
	 * 
	 * @since 3.0
	 */
	public static final SimplePropertyDescriptor IDENTIFIER_PROPERTY = 
		new SimplePropertyDescriptor(SimpleName.class, "identifier", String.class, MANDATORY); //$NON-NLS-1$
	
	/**
	 * A list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 * @since 3.0
	 */
	private static final List PROPERTY_DESCRIPTORS;
	
	static {
		createPropertyList(SimpleName.class);
		addProperty(IDENTIFIER_PROPERTY);
		PROPERTY_DESCRIPTORS = reapPropertyList();
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
		return PROPERTY_DESCRIPTORS;
	}
	
	/**
	 * An unspecified (but externally observable) legal Java identifier.
	 */
	private static final String MISSING_IDENTIFIER = "MISSING";//$NON-NLS-1$
	
	/**
	 * The identifier; defaults to a unspecified, legal Java identifier.
	 */
	private String identifier = MISSING_IDENTIFIER;
	
	/**
	 * Creates a new AST node for a simple name owned by the given AST.
	 * The new node has an unspecified, legal Java identifier.
	 * <p>
	 * N.B. This constructor is package-private; all subclasses must be 
	 * declared in the same package; clients are unable to declare 
	 * additional subclasses.
	 * </p>
	 * 
	 * @param ast the AST that is to own this node
	 */
	SimpleName(AST ast) {
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
	final Object internalGetSetObjectProperty(SimplePropertyDescriptor property, boolean get, Object value) {
		if (property == IDENTIFIER_PROPERTY) {
			if (get) {
				return getIdentifier();
			} else {
				setIdentifier((String) value);
				return null;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetObjectProperty(property, get, value);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	public int getNodeType() {
		return SIMPLE_NAME;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone(AST target) {
		SimpleName result = new SimpleName(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setIdentifier(getIdentifier());
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
		visitor.visit(this);
		visitor.endVisit(this);
	}

	/**
	 * Returns this node's identifier.
	 * 
	 * @return the identifier of this node
	 */ 
	public String getIdentifier() {
		return this.identifier;
	}
	
	/**
	 * Sets the identifier of this node to the given value.
	 * The identifier should be legal according to the rules
	 * of the Java language. Note that keywords are not legal
	 * identifiers.
	 * <p>
	 * Note that the list of keywords may depend on the version of the
	 * language (determined when the AST object was created).
	 * </p>
	 * 
	 * @param identifier the identifier of this node
	 * @exception IllegalArgumentException if the identifier is invalid
	 */ 
	public void setIdentifier(String identifier) {
		if (identifier == null) {
			throw new IllegalArgumentException();
		}
		Scanner scanner = this.ast.scanner;
		char[] source = identifier.toCharArray();
		scanner.setSource(source);
		scanner.resetTo(0, source.length);
		try {
			int tokenType = scanner.getNextToken();
			switch(tokenType) {
				case TerminalTokens.TokenNameIdentifier:
					break;
				default:
					throw new IllegalArgumentException();
			}
		} catch(InvalidInputException e) {
			throw new IllegalArgumentException();
		}
		preValueChange(IDENTIFIER_PROPERTY);
		this.identifier = identifier;
		postValueChange(IDENTIFIER_PROPERTY);
	}

	/**
	 * Returns whether this simple name represents a name that is being defined,
	 * as opposed to one being referenced. The following positions are considered
	 * ones where a name is defined:
	 * <ul>
	 * <li>The type name in a <code>TypeDeclaration</code> node.</li>
	 * <li>The method name in a <code>MethodDeclaration</code> node
	 * providing <code>isConstructor</code> is <code>false</code>.</li>
	 * <li>The variable name in any type of <code>VariableDeclaration</code>
	 * node.</li>
	 * <li>The enum type name in a <code>EnumDeclaration</code> node.</li>
	 * <li>The enum constant name in an <code>EnumConstantDeclaration</code>
	 * node.</li>
	 * <li>The variable name in an <code>EnhancedForStatement</code>
	 * node.</li>
	 * <li>The type variable name in a <code>TypeParameter</code>
	 * node.</li>
	 * <li>The type name in an <code>AnnotationTypeDeclaration</code> node.</li>
	 * <li>The member name in an <code>AnnotationTypeMemberDeclaration</code> node.</li>
	 * </ul>
	 * <p>
	 * Note that this is a convenience method that simply checks whether
	 * this node appears in the declaration position relative to its parent.
	 * It always returns <code>false</code> if this node is unparented.
	 * </p>
	 * 
	 * @return <code>true</code> if this node declares a name, and 
	 *    <code>false</code> otherwise
	 */ 
	public boolean isDeclaration() {
		StructuralPropertyDescriptor d = getLocationInParent();
		if (d == null) {
			// unparented node
			return false;
		}
		ASTNode parent = getParent();
		if (parent instanceof TypeDeclaration) {
			return (d == TypeDeclaration.NAME_PROPERTY);
		}
		if (parent instanceof MethodDeclaration) {
			MethodDeclaration p = (MethodDeclaration) parent;
			// could be the name of the method or constructor
			return !p.isConstructor() && (d == MethodDeclaration.NAME_PROPERTY);
		}
		if (parent instanceof SingleVariableDeclaration) {
			return (d == SingleVariableDeclaration.NAME_PROPERTY);
		}
		if (parent instanceof VariableDeclarationFragment) {
			return (d == VariableDeclarationFragment.NAME_PROPERTY);
		}
		if (parent instanceof EnumDeclaration) {
			return (d == EnumDeclaration.NAME_PROPERTY);
		}
		if (parent instanceof EnumConstantDeclaration) {
			return (d == EnumConstantDeclaration.NAME_PROPERTY);
		}
		if (parent instanceof TypeParameter) {
			return (d == TypeParameter.NAME_PROPERTY);
		}
		if (parent instanceof AnnotationTypeDeclaration) {
			return (d == AnnotationTypeDeclaration.NAME_PROPERTY);
		}
		if (parent instanceof AnnotationTypeMemberDeclaration) {
			return (d == AnnotationTypeMemberDeclaration.NAME_PROPERTY);
		}
		return false;
	}
		
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		int size = BASE_NAME_NODE_SIZE + 1 * 4;
		if (identifier != MISSING_IDENTIFIER) {
			// everything but our missing id costs
			size += stringSize(identifier);
		}
		return size;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int treeSize() {
		return memSize();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8588.java