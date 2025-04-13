error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4727.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4727.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4727.java
text:
```scala
t@@his.kind = SOURCE_PARSER | TEXT_VERIF;

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
package org.eclipse.jdt.internal.compiler;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.parser.JavadocParser;
import org.eclipse.jdt.internal.compiler.parser.Parser;

public class SourceJavadocParser extends JavadocParser {

	// Store categories identifiers parsed in javadoc
	int categoriesPtr = -1;
	char[][] categories = CharOperation.NO_CHAR_CHAR;

public SourceJavadocParser(Parser sourceParser) {
	super(sourceParser);
	this.kind = SOURCE_PARSER | TEXT_PARSE | TEXT_VERIF;
}

public boolean checkDeprecation(int commentPtr) {
	this.categoriesPtr = -1;
	boolean result = super.checkDeprecation(commentPtr);
	if (this.categoriesPtr > -1) {
		System.arraycopy(this.categories, 0, this.categories = new char[this.categoriesPtr+1][], 0, this.categoriesPtr+1);
	} else {
		this.categories = CharOperation.NO_CHAR_CHAR;
	}
	return result;
}

/* (non-Javadoc)
 * @see org.eclipse.jdt.internal.compiler.parser.AbstractCommentParser#parseIdentifierTag()
 */
protected boolean parseIdentifierTag(boolean report) {
	if (super.parseIdentifierTag(report)) {
		if (this.tagValue == TAG_CATEGORY_VALUE) {
			int length = this.categories.length;
			if (++this.categoriesPtr >= length) {
				System.arraycopy(this.categories, 0, this.categories = new char[length+5][], 0, length);
			}
			this.categories[this.categoriesPtr] = this.identifierStack[this.identifierPtr--];
		}
		return true;
	}
	return false;
}

/* (non-Javadoc)
 * @see org.eclipse.jdt.internal.compiler.parser.JavadocParser#parseSimpleTag()
 */
protected void parseSimpleTag() {
	
	// Read first char
	// readChar() code is inlined to balance additional method call in checkDeprectation(int)
	char first = this.source[this.index++];
	if (first == '\\' && this.source[this.index] == 'u') {
		int c1, c2, c3, c4;
		int pos = this.index;
		this.index++;
		while (this.source[this.index] == 'u')
			this.index++;
		if (!(((c1 = Character.getNumericValue(this.source[this.index++])) > 15 || c1 < 0)
 ((c2 = Character.getNumericValue(this.source[this.index++])) > 15 || c2 < 0)
 ((c3 = Character.getNumericValue(this.source[this.index++])) > 15 || c3 < 0) || ((c4 = Character.getNumericValue(this.source[this.index++])) > 15 || c4 < 0))) {
			first = (char) (((c1 * 16 + c2) * 16 + c3) * 16 + c4);
		} else {
			this.index = pos;
		}
	}

	// switch on first tag char
	switch (first) {
		case 'd': // perhaps @deprecated tag?
	        if ((readChar() == 'e') &&
					(readChar() == 'p') && (readChar() == 'r') &&
					(readChar() == 'e') && (readChar() == 'c') &&
					(readChar() == 'a') && (readChar() == 't') &&
					(readChar() == 'e') && (readChar() == 'd')) {
				// ensure the tag is properly ended: either followed by a space, a tab, line end or asterisk.
				char c = readChar();
				if (Character.isWhitespace(c) || c == '*') {
					this.tagValue = TAG_DEPRECATED_VALUE;
					this.deprecated = true;
				}
	        }
			break;
		case 'c': // perhaps @category tag?
	        if ((readChar() == 'a') &&
					(readChar() == 't') && (readChar() == 'e') &&
					(readChar() == 'g') && (readChar() == 'o') &&
					(readChar() == 'r') && (readChar() == 'y')) {
				// ensure the tag is properly ended: either followed by a space, a tab, line end or asterisk.
				char c = readChar();
				if (Character.isWhitespace(c) || c == '*') {
					this.tagValue = TAG_CATEGORY_VALUE;
					if (this.scanner.source == null) {
						this.scanner.setSource(this.source);
					}
					this.scanner.resetTo(this.index, this.scanner.eofPosition);
					while (this.index < this.lineEnd) {
						parseIdentifierTag(false); // Do not report missing identifier
						consumeToken();
					}
				}
	        }
			break;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4727.java