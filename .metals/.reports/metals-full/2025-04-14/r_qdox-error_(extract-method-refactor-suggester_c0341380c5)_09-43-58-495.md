error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9386.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9386.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9386.java
text:
```scala
r@@eturn this.optionalLeadingComment;

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

package org.eclipse.jdt.core.dom;

import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.internal.compiler.parser.Scanner;
import org.eclipse.jdt.internal.compiler.parser.TerminalTokens;

/**
 * Abstract base class of AST nodes that represent statements.
 * There are many kinds of statements.
 * <p>
 * The grammar combines both Statement and BlockStatement.
 * For JLS2:
 * <pre>
 * Statement:
 *    Block
 *    IfStatement
 *    ForStatement
 *    WhileStatement
 *    DoStatement
 *    TryStatement
 *    SwitchStatement
 *    SynchronizedStatement
 *    ReturnStatement
 *    ThrowStatement
 *    BreakStatement
 *    ContinueStatement
 *    EmptyStatement
 *    ExpressionStatement
 *    LabeledStatement
 *    AssertStatement
 *    VariableDeclarationStatement
 *    TypeDeclarationStatement
 *    ConstructorInvocation
 *    SuperConstructorInvocation
 * </pre>
 * For JLS3, an enhanced for node type was added:
 * <pre>
 * Statement:
 *    Block
 *    IfStatement
 *    ForStatement
 *    EnhancedForStatement
 *    WhileStatement
 *    DoStatement
 *    TryStatement
 *    SwitchStatement
 *    SynchronizedStatement
 *    ReturnStatement
 *    ThrowStatement
 *    BreakStatement
 *    ContinueStatement
 *    EmptyStatement
 *    ExpressionStatement
 *    LabeledStatement
 *    AssertStatement
 *    VariableDeclarationStatement
 *    TypeDeclarationStatement
 *    ConstructorInvocation
 *    SuperConstructorInvocation
 * </pre>
 * </p>
 * 
 * @since 2.0
 */
public abstract class Statement extends ASTNode {
	
	/**
	 * The leading comment, or <code>null</code> if none.
	 * Defaults to none.
	 * 
	 * @deprecated The leading comment feature was removed in 2.1.
	 */
	private String optionalLeadingComment = null;
	
	/**
	 * Creates a new AST node for a statement owned by the given AST.
	 * <p>
	 * N.B. This constructor is package-private.
	 * </p>
	 * 
	 * @param ast the AST that is to own this node
	 */
	Statement(AST ast) {
		super(ast);
	}
	
	/**
	 * Returns the leading comment string, including the starting
	 * and ending comment delimiters, and any embedded line breaks.
	 * <p>
	 * A leading comment is a comment that appears before the statement.
	 * It may be either a traditional comment or an end-of-line comment.
	 * Traditional comments must begin with "/&#42;, may contain line breaks,
	 * and must end with "&#42;/. End-of-line comments must begin with "//",
	 * must end with a line delimiter (as per JLS 3.7), and must not contain
	 * line breaks.
	 * </p>
	 * 
	 * @return the comment string, or <code>null</code> if none
	 * @deprecated This feature was removed in the 2.1 release because it was
	 * only a partial, and inadequate, solution to the issue of associating
	 * comments with statements. Furthermore, AST.parseCompilationUnit did not
	 * associate leading comments, making this moot. Clients that need to access
	 * comments preceding a statement should either consult the compilation
	 * unit's {@linkplain CompilationUnit#getCommentList() comment table}
	 * or use a scanner to reanalyze the source text immediately preceding
	 * the statement's source range.
	 */
	public String getLeadingComment() {
		return optionalLeadingComment;
	}

	/**
	 * Sets or clears the leading comment string. The comment
	 * string must include the starting and ending comment delimiters,
	 * and any embedded linebreaks.
	 * <p>
	 * A leading comment is a comment that appears before the statement.
	 * It may be either a traditional comment or an end-of-line comment.
	 * Traditional comments must begin with "/&#42;, may contain line breaks,
	 * and must end with "&#42;/. End-of-line comments must begin with "//"
	 * (as per JLS 3.7), and must not contain line breaks.
	 * </p>
	 * <p>
	 * Examples:
	 * <code>
	 * <pre>
	 * setLeadingComment("/&#42; traditional comment &#42;/");  // correct
	 * setLeadingComment("missing comment delimiters");  // wrong
	 * setLeadingComment("/&#42; unterminated traditional comment ");  // wrong
	 * setLeadingComment("/&#42; broken\n traditional comment &#42;/");  // correct
	 * setLeadingComment("// end-of-line comment\n");  // correct
	 * setLeadingComment("// end-of-line comment without line terminator");  // correct
	 * setLeadingComment("// broken\n end-of-line comment\n");  // wrong
	 * </pre>
	 * </code>
	 * </p>
	 * 
	 * @param comment the comment string, or <code>null</code> if none
	 * @exception IllegalArgumentException if the comment string is invalid
	 * @deprecated This feature was removed in the 2.1 release because it was
	 * only a partial, and inadequate, solution to the issue of associating
	 * comments with statements.
	 */
	public void setLeadingComment(String comment) {
		if (comment != null) {
			char[] source = comment.toCharArray();
			Scanner scanner = this.ast.scanner;
			scanner.resetTo(0, source.length);
			scanner.setSource(source);
			try {
				int token;
				boolean onlyOneComment = false;
				while ((token = scanner.getNextToken()) != TerminalTokens.TokenNameEOF) {
					switch(token) {
						case TerminalTokens.TokenNameCOMMENT_BLOCK :
						case TerminalTokens.TokenNameCOMMENT_JAVADOC :
						case TerminalTokens.TokenNameCOMMENT_LINE :
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
		}
		// we do not consider the obsolete comment as a structureal property
		// but we protect them nevertheless
		checkModifiable();
		this.optionalLeadingComment = comment;
	}

	/**
	 * Copies the leading comment from the given statement.
	 * 
	 * @param source the statement that supplies the leading comment
	 * @since 2.1
	 */
	void copyLeadingComment(Statement source) {
		setLeadingComment(source.getLeadingComment());
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		int size = BASE_NODE_SIZE + 1 * 4 + stringSize(getLeadingComment());
		return size;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9386.java