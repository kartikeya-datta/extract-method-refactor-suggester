error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7077.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7077.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7077.java
text:
```scala
I@@Scanner scanner = ToolFactory.createScanner(true, true, false, false, true);

/*******************************************************************************
 * Copyright (c) 2001, 2002 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.jdt.core.dom;

import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.compiler.InvalidInputException;

/**
 * Abstract base class of AST nodes that represent statements.
 * There are many kinds of statements.
 * <p>
 * The grammar combines both Statement and BlockStatement.
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
 * </p>
 * 
 * @since 2.0
 */
public abstract class Statement extends ASTNode {
	
	/**
	 * The leading comment, or <code>null</code> if none.
	 * Defaults to none.
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
	 * and must end with "&#42;/. End-of-line comments must begin with "//",
	 * must end with a line delimiter (as per JLS 3.7), and must not contain
	 * line breaks.
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
	 * setLeadingComment("// end-of-line comment without line terminator");  // wrong
	 * setLeadingComment("// broken\n end-of-line comment\n");  // wrong
	 * </pre>
	 * </code>
	 * </p>
	 * 
	 * @param comment the comment string, or <code>null</code> if none
	 * @exception IllegalArgumentException if the comment string is invalid
	 */
	public void setLeadingComment(String comment) {
		if (comment != null) {
			char[] source = comment.toCharArray();
			IScanner scanner = ToolFactory.createScanner(true, true, false, false);
			scanner.resetTo(0, source.length);
			scanner.setSource(source);
			try {
				int token;
				boolean onlyOneComment = false;
				while ((token = scanner.getNextToken()) != ITerminalSymbols.TokenNameEOF) {
					switch(token) {
						case ITerminalSymbols.TokenNameCOMMENT_BLOCK :
						case ITerminalSymbols.TokenNameCOMMENT_JAVADOC :
						case ITerminalSymbols.TokenNameCOMMENT_LINE :
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
		modifying();
		this.optionalLeadingComment = comment;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		int size = BASE_NODE_SIZE + 1 * 4;
		String s = getLeadingComment();
		if (s != null) {
			size += HEADERS + 2 * 4 + HEADERS + 2 * s.length();
		}
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7077.java