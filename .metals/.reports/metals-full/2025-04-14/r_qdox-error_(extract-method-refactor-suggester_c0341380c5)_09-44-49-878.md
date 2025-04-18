error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7709.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7709.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7709.java
text:
```scala
v@@isitor.visit(this);

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

import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.internal.compiler.parser.Scanner;

/**
 * String literal nodes.
 * 
 * @since 2.0
 */
public class StringLiteral extends Expression {

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
	public int getNodeType() {
		return STRING_LITERAL;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone(AST target) {
		StringLiteral result = new StringLiteral(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setEscapedValue(getEscapedValue());
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
	 * @exception IllegalArgumentException if the argument is incorrect
	 */ 
	public void setEscapedValue(String token) {
		if (token == null) {
			throw new IllegalArgumentException();
		}
		Scanner scanner = getAST().scanner;
		char[] source = token.toCharArray();
		scanner.setSource(source);
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
	 * @exception IllegalArgumentException if the literal value cannot be converted
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
				char nextChar;
				if ((i + 1) < len - 1) {
					nextChar = s.charAt(i + 1);
					i++;
					switch(nextChar) {
						case 'b' :
							b.append('\b');
							break;
						case 't' :
							b.append('\t');
							break;
						case 'n' :
							b.append('\n');
							break;
						case 'f' :
							b.append('\f');
							break;
						case 'r' :
							b.append('\r');
							break;
						case '\"':
							b.append('\"');
							break;
						case '\'':
							b.append('\'');
							break;
						case '\\':
							b.append('\\');
							break;
						case '0' :
							b.append('\0');
							break;
						case '1' :
							b.append('\1');
							break;
						case '2' :
							b.append('\2');
							break;
						case '3' :
							b.append('\3');
							break;
						case '4' :
							b.append('\4');
							break;
						case '5' :
							b.append('\5');
							break;
						case '6' :
							b.append('\6');
							break;
						case '7' :
							b.append('\7');
							break;
						case 'u' :
							//handle the case of unicode.
							int currentPosition = i + 1;
							int c1 = 0, c2 = 0, c3 = 0, c4 = 0;
							if ((c1 = Character.getNumericValue(s.charAt(currentPosition++))) > 15
 c1 < 0
 (c2 = Character.getNumericValue(s.charAt(currentPosition++))) > 15
 c2 < 0
 (c3 = Character.getNumericValue(s.charAt(currentPosition++))) > 15
 c3 < 0
 (c4 = Character.getNumericValue(s.charAt(currentPosition++))) > 15
 c4 < 0){
								throw new IllegalArgumentException("Invalid string literal"); //$NON-NLS-1$
							} else {
								b.append((char) (((c1 * 16 + c2) * 16 + c3) * 16 + c4));
								i = currentPosition - 1;
							}
							break;
						default:
							throw new IllegalArgumentException("Invalid string literal");//$NON-NLS-1$
					}
				} else {
					throw new IllegalArgumentException("Invalid string literal");//$NON-NLS-1$
				}
			} else {
				b.append(c);
			}
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
	 * @exception IllegalArgumentException if the argument is incorrect
	 */
	public void setLiteralValue(String value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		int len = value.length();
		StringBuffer b = new StringBuffer(len + 2);
		
		b.append("\""); // opening delimiter //$NON-NLS-1$
		for (int i = 0; i < len; i++) {
			char c = value.charAt(i);
			switch(c) {
				case '\b' :
					b.append("\\b"); //$NON-NLS-1$
					break;
				case '\t' :
					b.append("\\t"); //$NON-NLS-1$
					break;
				case '\n' :
					b.append("\\n"); //$NON-NLS-1$
					break;
				case '\f' :
					b.append("\\f"); //$NON-NLS-1$
					break;
				case '\r' :
					b.append("\\r"); //$NON-NLS-1$
					break;
				case '\"':
					b.append("\\\""); //$NON-NLS-1$
					break;
				case '\'':
					b.append("\\\'"); //$NON-NLS-1$
					break;
				case '\\':
					b.append("\\\\"); //$NON-NLS-1$
					break;
				case '\0' :
					b.append("\\0"); //$NON-NLS-1$
					break;
				case '\1' :
					b.append("\\1"); //$NON-NLS-1$
					break;
				case '\2' :
					b.append("\\2"); //$NON-NLS-1$
					break;
				case '\3' :
					b.append("\\3"); //$NON-NLS-1$
					break;
				case '\4' :
					b.append("\\4"); //$NON-NLS-1$
					break;
				case '\5' :
					b.append("\\5"); //$NON-NLS-1$
					break;
				case '\6' :
					b.append("\\6"); //$NON-NLS-1$
					break;
				case '\7' :
					b.append("\\7"); //$NON-NLS-1$
					break;			
				default:
					b.append(c);
			}
		}
		b.append("\""); // closing delimiter //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7709.java