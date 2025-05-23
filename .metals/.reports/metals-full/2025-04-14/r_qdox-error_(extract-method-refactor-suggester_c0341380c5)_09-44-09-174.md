error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9064.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9064.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9064.java
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
 * AST node for a Javadoc-style doc comment.
 * <pre>
 * Javadoc:
 *   <b>/&ast;&ast;</b> { TagElement } <b>&ast;/</b>
 * </pre>
 * 
 * @since 2.0
 */
public class Javadoc extends Comment {
	
	/**
	 * The "comment" structural property of this node type (2.0 API only).
	 * @since 3.0
	 * TODO (jeem) - @deprecated Replaced by {@link #TAGS_PROPERTY} in the 3.0 API.
	 */
	public static final SimplePropertyDescriptor COMMENT_PROPERTY = 
		new SimplePropertyDescriptor(Javadoc.class, "comment", String.class, MANDATORY); //$NON-NLS-1$
	
	/**
	 * The "tags" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildListPropertyDescriptor TAGS_PROPERTY = 
		new ChildListPropertyDescriptor(Javadoc.class, "tags", TagElement.class, CYCLE_RISK); //$NON-NLS-1$

	
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
		createPropertyList(Javadoc.class);
		addProperty(COMMENT_PROPERTY);
		addProperty(TAGS_PROPERTY);
		PROPERTY_DESCRIPTORS_2_0 = reapPropertyList();
		
		createPropertyList(Javadoc.class);
		addProperty(TAGS_PROPERTY);
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
	 * Canonical minimal doc comment.
     * @since 3.0
	 */
	private static final String MINIMAL_DOC_COMMENT = "/** */";//$NON-NLS-1$

	/**
	 * The doc comment string, including opening and closing comment 
	 * delimiters; defaults to a minimal Javadoc comment.
	 * @deprecated The comment string was replaced in the 3.0 release
	 * by a representation of the structure of the doc comment.
	 * For backwards compatibility, it is still funcational as before.
	 */
	private String comment = MINIMAL_DOC_COMMENT;
	
	/**
	 * The list of tag elements (element type: <code>TagElement</code>). 
	 * Defaults to an empty list.
	 * @since 3.0
	 */
	private ASTNode.NodeList tags = 
		new ASTNode.NodeList(TAGS_PROPERTY);

	/**
	 * Creates a new AST node for a doc comment owned by the given AST.
	 * The new node has an empty list of tag elements (and, for backwards
	 * compatability, an unspecified, but legal, doc comment string).
	 * <p>
	 * N.B. This constructor is package-private; all subclasses must be 
	 * declared in the same package; clients are unable to declare 
	 * additional subclasses.
	 * </p>
	 * 
	 * @param ast the AST that is to own this node
	 */
	Javadoc(AST ast) {
		super(ast);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final List internalStructuralPropertiesForType(int apiLevel) {
		return propertyDescriptors(apiLevel);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final Object internalGetSetObjectProperty(SimplePropertyDescriptor property, boolean get, Object value) {
		if (property == COMMENT_PROPERTY) {
			if (get) {
				return getComment();
			} else {
				setComment((String) value);
				return null;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetObjectProperty(property, get, value);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final List internalGetChildListProperty(ChildListPropertyDescriptor property) {
		if (property == TAGS_PROPERTY) {
			return tags();
		}
		// allow default implementation to flag the error
		return super.internalGetChildListProperty(property);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	public int getNodeType() {
		return JAVADOC;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone(AST target) {
		Javadoc result = new Javadoc(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		if (this.ast.apiLevel == AST.LEVEL_2_0) {
			result.setComment(getComment());
		}
		result.tags().addAll(ASTNode.copySubtrees(target, tags()));
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
			acceptChildren(visitor, this.tags);
		}
		visitor.endVisit(this);
	}

	/**
	 * Returns the doc comment string, including the starting
	 * and ending comment delimiters, and any embedded line breaks.
	 * 
	 * @return the doc comment string
	 * @exception UnsupportedOperationException if this operation is used in
	 * an AST later than 2.0
	 * @deprecated The comment string was replaced in the 3.0 release
	 * by a representation of the structure of the doc comment.
	 * See {@link #tags() tags}.
	 */
	public String getComment() {
	    supportedOnlyIn2();
		return this.comment;
	}

	/**
	 * Sets or clears the doc comment string. The documentation
	 * string must include the starting and ending comment delimiters,
	 * and any embedded line breaks.
	 * 
	 * @param docComment the doc comment string
	 * @exception IllegalArgumentException if the Java comment string is invalid
	 * @exception UnsupportedOperationException if this operation is used in
	 * an AST later than 2.0
	 * @deprecated The comment string was replaced in the 3.0 release
	 * by a representation of the structure of the doc comment.
	 * See {@link #tags() tags}.
	 */
	public void setComment(String docComment) {
	    supportedOnlyIn2();
		if (docComment == null) {
			throw new IllegalArgumentException();
		}
		char[] source = docComment.toCharArray();
		Scanner scanner = this.ast.scanner;
		scanner.resetTo(0, source.length);
		scanner.setSource(source);
		try {
			int token;
			boolean onlyOneComment = false;
			while ((token = scanner.getNextToken()) != TerminalTokens.TokenNameEOF) {
				switch(token) {
					case TerminalTokens.TokenNameCOMMENT_JAVADOC :
						if (onlyOneComment) {
							throw new IllegalArgumentException();
						}
						onlyOneComment = true;
						break;
					default:
						onlyOneComment = false;
				}
			}
			if (!onlyOneComment) {
				throw new IllegalArgumentException();
			}
		} catch (InvalidInputException e) {
			throw new IllegalArgumentException();
		}
		preValueChange(COMMENT_PROPERTY);
		this.comment = docComment;
		postValueChange(COMMENT_PROPERTY);
	}
		
	/**
	 * Returns the live list of tag elements that make up this doc 
	 * comment.
	 * <p>
	 * The tag elements cover everything except the starting and ending
	 * comment delimiters, and generally omit leading whitespace 
	 * (including a leading "&ast;") and embedded line breaks.
	 * The first tag element of a typical doc comment represents
	 * all the material before the first explicit doc tag; this
	 * first tag element has a <code>null</code> tag name and
	 * generally contains 1 or more {@link TextElement}s,
	 * and possibly interspersed with tag elements for nested tags
	 * like "{@link String String}".
	 * Subsequent tag elements represent successive top-level doc
	 * tag (e.g., "@param", "@return", "@see").
	 * </p>
	 * <p>
	 * Adding and removing nodes from this list affects this node
	 * dynamically.
	 * </p>
	 * 
	 * @return the live list of tag elements in this doc comment
	 * (element type: <code>TagElement</code>)
	 * @since 3.0
	 */ 
	public List tags() {
		return this.tags;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		int size = super.memSize() + 2 * 4;
		if (this.comment != MINIMAL_DOC_COMMENT) {
			// anything other than the default string takes space
			size += stringSize(this.comment);
		}
		return size;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int treeSize() {
		return memSize() + this.tags.listSize();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9064.java