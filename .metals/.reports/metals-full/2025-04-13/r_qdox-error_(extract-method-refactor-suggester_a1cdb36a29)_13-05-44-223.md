error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1733.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1733.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1733.java
text:
```scala
t@@his.kind = SELECTION_PARSER | TEXT_PARSE;

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.codeassist.select;

import java.util.List;

import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.internal.codeassist.SelectionEngine;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.parser.JavadocParser;

/**
 * Parser specialized for decoding javadoc comments which includes code selection.
 */
public class SelectionJavadocParser extends JavadocParser {
	
	int selectionStart;
	int selectionEnd;
	ASTNode selectedNode;

	public SelectionJavadocParser(SelectionParser sourceParser) {
		super(sourceParser);
		this.kind = SELECTION_PARSER;
	}

	/*
	 * Do not parse comment if selection is not included.
	 */
	public boolean checkDeprecation(int commentPtr) {
		this.selectionStart = ((SelectionParser)sourceParser).selectionStart;
		this.selectionEnd = ((SelectionParser)sourceParser).selectionEnd;
		this.javadocStart = this.sourceParser.scanner.commentStarts[commentPtr];
		this.javadocEnd = this.sourceParser.scanner.commentStops[commentPtr];
		if (this.javadocStart <= this.selectionStart && this.selectionEnd <= this.javadocEnd) {
			if (SelectionEngine.DEBUG) {
				System.out.println("SELECTION in Javadoc:"); //$NON-NLS-1$
			}
			super.checkDeprecation(commentPtr);
		} else {
			this.docComment = null;
		}
		return false;
	}

	/*
	 * Replace stored Javadoc node with specific selection one.
	 */
	protected boolean commentParse() {
		this.docComment = new SelectionJavadoc(this.javadocStart, this.javadocEnd);
		return super.commentParse();
	}

	/*
	 * Create argument expression and store it if it includes selection.
	 */
	protected Object createArgumentReference(char[] name, int dim, boolean isVarargs, Object typeRef, long[] dimPositions, long argNamePos) throws InvalidInputException {
		// Create argument as we may need it after
		Expression expression = (Expression) super.createArgumentReference(name, dim, isVarargs, typeRef, dimPositions, argNamePos);
		// See if selection is in argument
		int start = ((TypeReference)typeRef).sourceStart;
		int end = ((TypeReference)typeRef).sourceEnd;
		if (start <= this.selectionStart && this.selectionEnd <= end) {
			selectedNode = expression;
			this.abort = true;
			if (SelectionEngine.DEBUG) {
				System.out.println("	selected argument="+selectedNode); //$NON-NLS-1$
			}
		}
		return expression;
	}

	/*
	 * Verify if field identifier positions include selection.
	 * If so, create field reference, store it and abort comment parse.
	 * Otherwise return null as we do not need this reference.
	 */
	protected Object createFieldReference(Object receiver) throws InvalidInputException {
		int start = (int) (this.identifierPositionStack[0] >>> 32);
		int end = (int) this.identifierPositionStack[0];
		if (start <= this.selectionStart && this.selectionEnd <= end) {
			selectedNode = (ASTNode) super.createFieldReference(receiver);
			this.abort = true;
			if (SelectionEngine.DEBUG) {
				System.out.println("	selected field="+selectedNode); //$NON-NLS-1$
			}
		}
		return null;
	}

	/*
	 * Verify if method identifier positions include selection.
	 * If so, create field reference, store it and abort comment parse.
	 * Otherwise return null as we do not need this reference.
	 */
	protected Object createMethodReference(Object receiver, List arguments) throws InvalidInputException {
		int start = (int) (this.identifierPositionStack[0] >>> 32);
		int end = (int) this.identifierPositionStack[0];
		if (start <= this.selectionStart && this.selectionEnd <= end) {
			selectedNode = (ASTNode) super.createMethodReference(receiver, arguments);
			this.abort = true;
			if (SelectionEngine.DEBUG) {
				System.out.println("	selected method="+selectedNode); //$NON-NLS-1$
			}
		}
		return null;
	}

	/*
	 * Create type reference and verify if it includes selection.
	 * If so, store it and abort comment parse.
	 * Otherwise return null as we do not need this reference.
	 */
	protected Object createTypeReference(int primitiveToken) {
		// Need to create type ref in case it was needed by members
		TypeReference typeRef = (TypeReference) super.createTypeReference(primitiveToken);
	
		// See if node is concerned by selection
		if (typeRef.sourceStart <= this.selectionStart && this.selectionEnd <= typeRef.sourceEnd) {
			// See if selection is in one of tokens of qualification
			if (typeRef instanceof JavadocQualifiedTypeReference) {
				JavadocQualifiedTypeReference qualifiedTypeRef = (JavadocQualifiedTypeReference) typeRef;
				int size = qualifiedTypeRef.tokens.length - 1;
				for (int i=0; i<size; i++) {
					int start = (int) (qualifiedTypeRef.sourcePositions[i] >>> 32);
					int end = (int) qualifiedTypeRef.sourcePositions[i];
					if (start <= this.selectionStart && this.selectionEnd <= end) {
						int pos = i + 1;
						char[][] tokens = new char[pos][];
						System.arraycopy(this.identifierStack, this.identifierPtr+1, tokens, 0, pos);
						long[] positions = new long[pos];
						System.arraycopy(this.identifierPositionStack, this.identifierPtr + 1, positions, 0, pos);
						selectedNode = new JavadocQualifiedTypeReference(tokens, positions, this.tagSourceStart, this.tagSourceEnd);
						this.abort = true; // we got selected node => cancel parse
						if (SelectionEngine.DEBUG) {
							System.out.println("	selected partial qualified type="+selectedNode); //$NON-NLS-1$
						}
						return typeRef;
					}
				}
				// Selection is in last token => we'll store type ref as this
			}
			// Store type ref as selected node
			selectedNode = typeRef;
			this.abort = true; // we got selected node => cancel parse
			if (SelectionEngine.DEBUG) {
				System.out.println("	selected type="+selectedNode); //$NON-NLS-1$
			}
		}
		return typeRef;
	}

	/*
	 * Push param reference and verify if it includes selection.
	 * If so, store it and abort comment parse.
	 */
	protected boolean pushParamName(boolean isTypeParam) {
		if (super.pushParamName(isTypeParam)) {
			Expression expression = (Expression) astStack[astPtr--];
			// See if expression is concerned by selection
			if (expression.sourceStart <= this.selectionStart && this.selectionEnd <= expression.sourceEnd) {
				selectedNode = expression;
				this.abort = true; // we got selected node => cancel parse
				if (SelectionEngine.DEBUG) {
					System.out.println("	selected param="+selectedNode); //$NON-NLS-1$
				}
			}
		}
		return false;
	}

	/*
	 * Store selected node into doc comment.
	 */
	protected void updateDocComment() {
		if (selectedNode instanceof Expression) {
			((SelectionJavadoc) this.docComment).selectedNode = (Expression) selectedNode;
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1733.java