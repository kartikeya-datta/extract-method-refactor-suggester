error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2836.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2836.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2836.java
text:
```scala
static final i@@nt JAVADOC_TAGS_INDEX_MASK = 0xFFFF;

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

package org.eclipse.jdt.internal.formatter.comment;

/**
 * Javadoc tag constants.
 * 
 * @since 3.0
 */
public interface IJavaDocTagConstants {

	/** Javadoc single break tag */
	public static final char[][] JAVADOC_SINGLE_BREAK_TAG= new char[][] { "br".toCharArray() }; //$NON-NLS-1$

	/** Javadoc code tags */
	public static final char[][] JAVADOC_CODE_TAGS= new char[][] { "pre".toCharArray() }; //$NON-NLS-1$

	/** Javadoc break tags */
	public static final char[][] JAVADOC_BREAK_TAGS = new char[][] {
		"dd".toCharArray(), //$NON-NLS-1$
		"dt".toCharArray(), //$NON-NLS-1$
		"li".toCharArray(), //$NON-NLS-1$
		"td".toCharArray(), //$NON-NLS-1$
		"th".toCharArray(), //$NON-NLS-1$
		"tr".toCharArray(), //$NON-NLS-1$
		"h1".toCharArray(), //$NON-NLS-1$
		"h2".toCharArray(), //$NON-NLS-1$
		"h3".toCharArray(), //$NON-NLS-1$
		"h4".toCharArray(), //$NON-NLS-1$
		"h5".toCharArray(), //$NON-NLS-1$
		"h6".toCharArray(), //$NON-NLS-1$
		"q".toCharArray() //$NON-NLS-1$
	};

	/** Javadoc immutable tags */
	public static final char[][] JAVADOC_IMMUTABLE_TAGS= new char[][] {
			"code".toCharArray(), //$NON-NLS-1$
			"em".toCharArray(), //$NON-NLS-1$
			"pre".toCharArray(), //$NON-NLS-1$
			"q".toCharArray(), //$NON-NLS-1$
			"tt".toCharArray() //$NON-NLS-1$
	};

	/** Javadoc new line tags */
	public static final char[][] JAVADOC_NEWLINE_TAGS= new char[][] {
			"dd".toCharArray(), //$NON-NLS-1$
			"dt".toCharArray(), //$NON-NLS-1$
			"li".toCharArray(), //$NON-NLS-1$
			"td".toCharArray(), //$NON-NLS-1$
			"th".toCharArray(), //$NON-NLS-1$
			"tr".toCharArray(), //$NON-NLS-1$
			"h1".toCharArray(), //$NON-NLS-1$
			"h2".toCharArray(), //$NON-NLS-1$
			"h3".toCharArray(), //$NON-NLS-1$
			"h4".toCharArray(), //$NON-NLS-1$
			"h5".toCharArray(), //$NON-NLS-1$
			"h6".toCharArray(), //$NON-NLS-1$
			"q".toCharArray() //$NON-NLS-1$
	};

	/** Javadoc parameter tags */
	// TODO (eric) should have another name than 'param' for the following tags
	// TODO (eric) investigate how and why this list was created
	public static final char[][] JAVADOC_PARAM_TAGS= new char[][] {
			"@exception".toCharArray(), //$NON-NLS-1$
			"@param".toCharArray(), //$NON-NLS-1$
			"@serialField".toCharArray(), //$NON-NLS-1$
			"@throws".toCharArray() //$NON-NLS-1$
	};

	/** Javadoc separator tags */
	public static final char[][] JAVADOC_SEPARATOR_TAGS= new char[][] {
			"dl".toCharArray(), //$NON-NLS-1$
			"hr".toCharArray(), //$NON-NLS-1$
			"nl".toCharArray(), //$NON-NLS-1$
			"p".toCharArray(), //$NON-NLS-1$
			"pre".toCharArray(), //$NON-NLS-1$
			"ul".toCharArray(), //$NON-NLS-1$
			"ol".toCharArray() //$NON-NLS-1$
	};

	/** Javadoc tag prefix */
	public static final char JAVADOC_TAG_PREFIX= '@';

	/** Link tag postfix */
	public static final char LINK_TAG_POSTFIX= '}';

	/** Link tag prefix */
	public static final String LINK_TAG_PREFIX_STRING = "{@"; //$NON-NLS-1$

	public static final char[] LINK_TAG_PREFIX= LINK_TAG_PREFIX_STRING.toCharArray();

	
	/** Comment root tags */
	public static final char[][] COMMENT_ROOT_TAGS= new char[][] {
			"@deprecated".toCharArray(), //$NON-NLS-1$
			"@see".toCharArray(), //$NON-NLS-1$
			"@since".toCharArray(), //$NON-NLS-1$
			"@version".toCharArray() //$NON-NLS-1$
	};

	/** Tag prefix of comment tags */
	public static final char COMMENT_TAG_PREFIX= '@';
	
	/** BLOCK COMMENTS */
	public static final String BLOCK_HEADER = "/*"; //$NON-NLS-1$
	public static final int BLOCK_HEADER_LENGTH = BLOCK_HEADER.length();
	public static final String JAVADOC_HEADER = "/**"; //$NON-NLS-1$
	public static final int JAVADOC_HEADER_LENGTH = JAVADOC_HEADER.length();
	public static final String BLOCK_LINE_PREFIX = " * "; //$NON-NLS-1$
	public static final int BLOCK_LINE_PREFIX_LENGTH = BLOCK_LINE_PREFIX.length();
	public static final String BLOCK_FOOTER = "*/"; //$NON-NLS-1$
	public static final int BLOCK_FOOTER_LENGTH = BLOCK_FOOTER.length();

	/** LINE COMMENTS */
	public static final String LINE_COMMENT_PREFIX = "// "; //$NON-NLS-1$
	public static final int LINE_COMMENT_PREFIX_LENGTH = LINE_COMMENT_PREFIX.length();
	
	/** JAVADOC STAR */
	public static final String JAVADOC_STAR = "*"; //$NON-NLS-1$
	
	/*
	 *  Tags IDs
	 */
	static final int JAVADOC_TAGS_INDEX_MASK = 0x00FF;
	static final int JAVADOC_TAGS_ID_MASK = 0xFF00;
	static final int JAVADOC_SINGLE_BREAK_TAG_ID = 0x100;
	static final int JAVADOC_CODE_TAGS_ID = 0x200;
	static final int JAVADOC_BREAK_TAGS_ID = 0x400;
	static final int JAVADOC_IMMUTABLE_TAGS_ID = 0x800;
	static final int JAVADOC_SEPARATOR_TAGS_ID = 0x1000;
	static final int JAVADOC_SINGLE_TAGS_ID = JAVADOC_SINGLE_BREAK_TAG_ID; // ID max for tags ID with no opening/closing (e.g. <bla>....</bla>)
	static final int JAVADOC_CLOSED_TAG = 0x10000;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2836.java