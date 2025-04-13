error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/504.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/504.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/504.java
text:
```scala
S@@canner scanner = getAST().scanner;

/*******************************************************************************
 * Copyright (c) 2001 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v0.5 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.jdt.core.dom;

import org.eclipse.jdt.internal.compiler.parser.InvalidInputException;
import org.eclipse.jdt.internal.compiler.parser.Scanner;

/**
 * String literal nodes.
 * 
 * @since 2.0
 */
public class StringLiteral extends Expression {

	/**
	 * String of characters that have a special escape equivalent.
	 * Paralleled by QUOTED_SPECIALS.
	 */
	private static final String SPECIALS = "\b\t\n\f\r\"\'\\";//$NON-NLS-1$

	/**
	 * String of single-letter escape equivalents.
	 * Parallel to SPECIALS.
	 */
	private static final String QUOTED_SPECIALS = "btnfr\"\'\\";//$NON-NLS-1$

	/**
	 * The literal string, including quotes and escapes; defaults to the 
	 * literal for the empty string.
	 */
	private String escapedValue = "\"\"";//$NON-NLS-1$

	/**
	 * Creates a new unparented string literal node owned by the given AST.
	 * By default, the string literal denotes the empty string.
	 * <p>
	 * N.B. This constructor is package-private.
	 * </p>
	 * 
	 * @param ast the AST that is to own this node
	 */
	StringLiteral(AST ast) {
		super(ast);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone(AST target) {
		StringLiteral result = new StringLiteral(target);
		result.setEscapedValue(getEscapedValue());
		return result;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	boolean equalSubtrees(Object other) {
		if (!(other instanceof StringLiteral)) {
			return false;
		}
		StringLiteral o = (StringLiteral) other;
		return ASTNode.equals(getEscapedValue(), o.getEscapedValue());
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	void accept0(ASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		visitor.endVisit(this);
	}
	
	/**
	 * Returns the string value of this literal node to the given string
	 * literal token. The token is the sequence of characters that would appear
	 * in the source program, including enclosing double quotes and embedded
	 * escapes.
	 * 
	 * @return the string literal token, including enclosing double
	 *    quotes and embedded escapes
	 */ 
	public String getEscapedValue() {
		return escapedValue;
	}
		
	/**
	 * Sets the string value of this literal node to the given string literal
	 * token. The token is the sequence of characters that would appear in the
	 * source program, including enclosing double quotes and embedded escapes.
	 * For example,
	 * <ul>
	 * <li><code>""</code> <code>setLiteral("\"\"")</code></li>
	 * <li><code>"hello world"</code> <code>setLiteral("\"hello world\"")</code></li>
	 * <li><code>"boo\nhoo"</code> <code>setLiteral("\"boo\\nhoo\"")</code></li>
	 * </ul>
	 * 
	 * @param token the string literal token, including enclosing double
	 *    quotes and embedded escapes
	 * @exception $precondition-violation:invalid-argument$
	 */ 
	public void setEscapedValue(String token) {
		if (token == null) {
			throw new IllegalArgumentException();
		}
		Scanner scanner = this.getAST().scanner;
		char[] source = token.toCharArray();
		scanner.setSourceBuffer(source);
		scanner.resetTo(0, source.length);
		try {
			int tokenType = scanner.getNextToken();
			switch(tokenType) {
				case Scanner.TokenNameStringLiteral:
					break;
				default:
					throw new IllegalArgumentException();
			}
		} catch(InvalidInputException e) {
			throw new IllegalArgumentException();
		}
		modifying();
		this.escapedValue = token;
	}

	/**
	 * Returns the value of this literal node. 
	 * <p>
	 * For example,
	 * <code>
	 * <pre>
	 * StringLiteral s;
	 * s.setEscapedValue("\"hello\\nworld\"");
	 * assert s.getLiteralValue().equals("hello\nworld");
	 * </pre>
	 * </p>
	 * <p>
	 * Note that this is a convenience method that converts from the stored 
	 * string literal token returned by <code>getEscapedLiteral</code>.
	 * </p>
	 * 
	 * @return the string value without enclosing double quotes and embedded
	 *    escapes
	 * @exception $postcondition-violation:invalid-literal$
	 */ 
	public String getLiteralValue() {
		String s = getEscapedValue();
		int len = s.length();
		if (len < 2 || s.charAt(0) != '\"' || s.charAt(len-1) != '\"' ) {
			throw new IllegalArgumentException();
		}
		StringBuffer b = new StringBuffer(len - 2);
		for (int i = 1; i< len - 1; i++) {
			char c = s.charAt(i);
			if (c == '\"') {
				throw new IllegalArgumentException();
			}
			if (c == '\\') {
				// legal: b, t, n, f, r, ", ', \, 0, 1, 2, 3, 4, 5, 6, or 7
				// FIXME
				throw new RuntimeException("not implemented yet");//$NON-NLS-1$
			}
			b.append(c);
		}
		return b.toString();			
	}

	/**
	 * Sets the value of this literal node. 
	 * <p>
	 * For example,
	 * <code>
	 * <pre>
	 * StringLiteral s;
	 * s.setLiteralValue("hello\nworld");
	 * assert s.getEscapedValue("\"hello\\nworld\"");
	 * assert s.getLiteralValue().equals("hello\nworld");
	 * </pre>
	 * </p>
	 * <p>
	 * Note that this is a convenience method that converts to the stored 
	 * string literal token acceptable to <code>setEscapedLiteral</code>.
	 * </p>
	 * 
	 * @param literal the string value without enclosing double quotes and 
	 *    embedded escapes
	 * @exception $precondition-violation:invalid-argument$
	 */
	public void setLiteralValue(String value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		int len = value.length();
		StringBuffer b = new StringBuffer(len + 2);
		
		// FIXME - this does not do Unicode escaping
		b.append('\"'); // opening delimiter
		for (int i = 0; i < len; i++) {
			char c = value.charAt(i);
			int p = SPECIALS.indexOf(c);
			if (p >= 0) {
				b.append('\\');
				b.append(QUOTED_SPECIALS.charAt(p));
			} else {
				b.append(c);
			}
		}
		b.append('\"'); // closing delimiter
		setEscapedValue(b.toString());
	}
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		int size = BASE_NODE_SIZE + 1 * 4;
		if (escapedValue != null) {
			size += HEADERS + 2 * 4 + HEADERS + 2 * escapedValue.length();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/504.java