error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3208.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3208.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3208.java
text:
```scala
r@@eturn delimiter + stringToIndent(predecessor.getIndentationReference());

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

package org.eclipse.jdt.internal.formatter.comment;

import java.util.Iterator;
import java.util.ListIterator;

import org.eclipse.jdt.internal.formatter.CodeFormatterVisitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;

/**
 * Multi-comment region in a source code document.
 * 
 * @since 3.0
 */
public class MultiCommentRegion extends CommentRegion implements IJavaDocTagConstants {

	/** Should root tag parameter descriptions be indented after the tag? */
	private final boolean fIndentDescriptions;

	/** Should root tag parameter descriptions be indented? */
	private final boolean fIndentRoots;

	/** Should description of parameters go to the next line? */
	private final boolean fParameterNewLine;

	/** Should root tags be separated from description? */
	private boolean fSeparateRoots;

 	/**
	 * Creates a new multi-comment region.
	 * 
	 * @param document the document which contains the comment region
	 * @param position the position of this comment region in the document
	 * @param formatter the given formatter
	 */
	public MultiCommentRegion(final IDocument document, final Position position, final CodeFormatterVisitor formatter) {
		super(document, position, formatter);

		fIndentRoots= this.preferences.comment_indent_root_tags;
		fIndentDescriptions= this.preferences.comment_indent_parameter_description;
		fSeparateRoots= this.preferences.comment_insert_empty_line_before_root_tags;
		fParameterNewLine= this.preferences.comment_insert_new_line_for_parameter;
	}

	/*
	 * @see org.eclipse.jdt.internal.corext.text.comment.CommentRegion#canAppend(org.eclipse.jdt.internal.corext.text.comment.CommentLine, org.eclipse.jdt.internal.corext.text.comment.CommentRange, org.eclipse.jdt.internal.corext.text.comment.CommentRange, int, int)
	 */
	protected boolean canAppend(final CommentLine line, final CommentRange previous, final CommentRange next, final int index, int count) {

		final boolean blank= next.hasAttribute(COMMENT_BLANKLINE);

		// Avoid wrapping punctuation
		if (next.getLength() <= 2 && !blank && isNonAlphaNumeric(next))
			return true;

		if (fParameterNewLine && line.hasAttribute(COMMENT_PARAMETER) && line.getSize() > 1)
			return false;

		if (previous != null) {
			
			if (index != 0 && (blank || previous.hasAttribute(COMMENT_BLANKLINE) || next.hasAttribute(COMMENT_PARAMETER) || next.hasAttribute(COMMENT_ROOT) || next.hasAttribute(COMMENT_SEPARATOR) || next.hasAttribute(COMMENT_NEWLINE) || previous.hasAttribute(COMMENT_BREAK) || previous.hasAttribute(COMMENT_SEPARATOR)))
				return false;
			
			if (previous.hasAttribute(COMMENT_ROOT))
				return true;

			if (next.hasAttribute(COMMENT_IMMUTABLE) && previous.hasAttribute(COMMENT_IMMUTABLE))
				return true;
		}

		// always append elements that did not have any range separators
		if (!next.hasAttribute(COMMENT_STARTS_WITH_RANGE_DELIMITER)) {
			return true;
		}

		if (fIndentRoots && !line.hasAttribute(COMMENT_ROOT) && !line.hasAttribute(COMMENT_PARAMETER))
			count -= stringToLength(line.getIndentationReference());

		// Avoid appending consecutive immutable ranges, which together exceed the line width
		if (next.hasAttribute(COMMENT_IMMUTABLE) && (previous == null || !previous.hasAttribute(COMMENT_IMMUTABLE))) {
			// Breaking the abstraction by directly accessing the list of ranges for looking ahead
			Iterator iter= getRanges().iterator();
			CommentRange current= null;
			while (iter.hasNext() && current != next)
				current= (CommentRange) iter.next();
			
			if (current != null && iter.hasNext()) {
				try {
					int lineNumber= getDocument().getLineOfOffset(getOffset() + current.getOffset());
					CommentRange last= current;
					while (iter.hasNext()) {
						current= (CommentRange) iter.next();
						if (current.hasAttribute(COMMENT_IMMUTABLE) && getDocument().getLineOfOffset(getOffset() + current.getOffset()) == lineNumber)
							last= current;
						else
							break;
					}
					count -= last.getOffset() + last.getLength() - (next.getOffset() + next.getLength());
				} catch (BadLocationException e) {
					// Should not happen
				}
			}
		}

		return super.canAppend(line, previous, next, index, count);
	}

	/*
	 * @see org.eclipse.jdt.internal.corext.text.comment.CommentRegion#getDelimiter(org.eclipse.jdt.internal.corext.text.comment.CommentLine, org.eclipse.jdt.internal.corext.text.comment.CommentLine, org.eclipse.jdt.internal.corext.text.comment.CommentRange, org.eclipse.jdt.internal.corext.text.comment.CommentRange, java.lang.String)
	 */
	protected String getDelimiter(CommentLine predecessor, CommentLine successor, CommentRange previous, CommentRange next, String indentation) {

		final String delimiter= super.getDelimiter(predecessor, successor, previous, next, indentation);

		if (previous != null) {

			// Blank line before <pre> tag
			if (previous.hasAttribute(COMMENT_IMMUTABLE | COMMENT_SEPARATOR) && !next.hasAttribute(COMMENT_CODE) && !successor.hasAttribute(COMMENT_BLANKLINE))
				return delimiter + delimiter;

			// Blank line after </pre> tag
			else if (next.hasAttribute(COMMENT_IMMUTABLE | COMMENT_SEPARATOR) && !successor.hasAttribute(COMMENT_BLANKLINE) && !predecessor.hasAttribute(COMMENT_BLANKLINE))
				return delimiter + delimiter;

			// Add blank line before first root/parameter tag, if "Blank line before Javadoc tags"
			else if (fSeparateRoots && previous.hasAttribute(COMMENT_PARAGRAPH) && !successor.hasAttribute(COMMENT_BLANKLINE) && !predecessor.hasAttribute(COMMENT_BLANKLINE))
				return delimiter + delimiter;

			else if (fIndentRoots && !predecessor.hasAttribute(COMMENT_ROOT) && !predecessor.hasAttribute(COMMENT_PARAMETER) && !predecessor.hasAttribute(COMMENT_BLANKLINE))
				return delimiter + stringToIndent(predecessor.getIndentationReference(), false);
		}
		return delimiter;
	}

	/*
	 * @see org.eclipse.jdt.internal.corext.text.comment.CommentRegion#getDelimiter(org.eclipse.jdt.internal.corext.text.comment.CommentRange, org.eclipse.jdt.internal.corext.text.comment.CommentRange)
	 */
	protected String getDelimiter(final CommentRange previous, final CommentRange next) {
		// simply preserve range (~ word) breaks
		if (previous != null && !previous.hasAttribute(COMMENT_STARTS_WITH_RANGE_DELIMITER)) {
			return ""; //$NON-NLS-1$
		} else {
			return super.getDelimiter(previous, next);
		}
	}

	/**
	 * Should root tag parameter descriptions be indented after the tag?
	 * 
	 * @return <code>true</code> iff the descriptions should be indented
	 *         after, <code>false</code> otherwise.
	 */
	protected final boolean isIndentDescriptions() {
		return fIndentDescriptions;
	}

	/**
	 * Should root tag parameter descriptions be indented?
	 * 
	 * @return <code>true</code> iff the root tags should be indented,
	 *         <code>false</code> otherwise.
	 */
	protected final boolean isIndentRoots() {
		return fIndentRoots;
	}

	/**
	 * Marks the comment ranges confined by HTML ranges.
	 */
	protected void markHtmlRanges() {
		// Do nothing
	}

	/**
	 * Marks the comment range with its HTML tag attributes.
	 * 
	 * @param range the comment range to mark
	 * @param token token associated with the comment range
	 */
	protected void markHtmlTag(final CommentRange range, final String token) {
		// Do nothing
	}

	/**
	 * Marks the comment range with its javadoc tag attributes.
	 * 
	 * @param range the comment range to mark
	 * @param token token associated with the comment range
	 */
	protected void markJavadocTag(final CommentRange range, final String token) {
		range.markPrefixTag(COMMENT_ROOT_TAGS, COMMENT_TAG_PREFIX, token, COMMENT_ROOT);
	}

	/*
	 * @see org.eclipse.jdt.internal.corext.text.comment.CommentRegion#markRegion()
	 */
	protected void markRegion() {

		int count= 0;
		boolean paragraph= false;

		String token= null;
		CommentRange range= null;

		for (final ListIterator iterator= getRanges().listIterator(); iterator.hasNext();) {

			range= (CommentRange)iterator.next();
			count= range.getLength();

			if (count > 0) {

				token= getText(range.getOffset(), count).toLowerCase();

				markJavadocTag(range, token);
				if (!paragraph && (range.hasAttribute(COMMENT_ROOT) || range.hasAttribute(COMMENT_PARAMETER))) {
					range.setAttribute(COMMENT_PARAGRAPH);
					paragraph= true;
				}
				markHtmlTag(range, token);
			}
		}
		markHtmlRanges();
	}
	
	/*
	 * @see org.eclipse.jdt.internal.corext.text.comment.CommentRegion#createLine()
	 * @since 3.1
	 */
	protected CommentLine createLine() {
		return new MultiCommentLine(this);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3208.java