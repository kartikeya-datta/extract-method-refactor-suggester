error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5865.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5865.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[13,1]

error in qdox parser
file content:
```java
offset: 607
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5865.java
text:
```scala
public class TypeReferencePattern extends IntersectingPattern {

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
p@@ackage org.eclipse.jdt.internal.core.search.matching;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.internal.core.util.Util;

	public class TypeReferencePattern extends AndPattern {
	
	protected char[] qualification;
	protected char[] simpleName;
		
	protected char[] currentCategory;
	
	/* Optimization: case where simpleName == null */
	public int segmentsSize;
	protected char[][] segments;
	protected int currentSegment;

	protected static char[][] CATEGORIES = { REF };
	
	public TypeReferencePattern(char[] qualification, char[] simpleName, int matchRule) {
		this(matchRule);
	
		this.qualification = isCaseSensitive() ? qualification : CharOperation.toLowerCase(qualification);
		this.simpleName = (isCaseSensitive() || isCamelCase())  ? simpleName : CharOperation.toLowerCase(simpleName);
	
		if (simpleName == null)
			this.segments = this.qualification == null ? ONE_STAR_CHAR : CharOperation.splitOn('.', this.qualification);
		else
			this.segments = null;
		
		if (this.segments == null)
			if (this.qualification == null)
				this.segmentsSize =  0;
			else
				this.segmentsSize =  CharOperation.occurencesOf('.', this.qualification) + 1;
		else
			this.segmentsSize = this.segments.length;
	
		((InternalSearchPattern)this).mustResolve = true; // always resolve (in case of a simple name reference being a potential match)
	}
	/*
	 * Instanciate a type reference pattern with additional information for generics search
	 */
	public TypeReferencePattern(char[] qualification, char[] simpleName, String typeSignature, int matchRule) {
		this(qualification, simpleName,matchRule);
		if (typeSignature != null) {
			// store type signatures and arguments
			this.typeSignatures = Util.splitTypeLevelsSignature(typeSignature);
			setTypeArguments(Util.getAllTypeArguments(this.typeSignatures));
			if (hasTypeArguments()) {
				this.segmentsSize = getTypeArguments().length + CharOperation.occurencesOf('/', this.typeSignatures[0]) - 1;
			}
		}
	}
	/*
	 * Instanciate a type reference pattern with additional information for generics search
	 */
	public TypeReferencePattern(char[] qualification, char[] simpleName, IType type, int matchRule) {
		this(qualification, simpleName,matchRule);
		storeTypeSignaturesAndArguments(type);
	}
	TypeReferencePattern(int matchRule) {
		super(TYPE_REF_PATTERN, matchRule);
	}
	public void decodeIndexKey(char[] key) {
		this.simpleName = key;
	}
	public SearchPattern getBlankPattern() {
		return new TypeReferencePattern(R_EXACT_MATCH | R_CASE_SENSITIVE);
	}
	public char[] getIndexKey() {
		if (this.simpleName != null)
			return this.simpleName;
	
		// Optimization, eg. type reference is 'org.eclipse.jdt.core.*'
		if (this.currentSegment >= 0) 
			return this.segments[this.currentSegment];
		return null;
	}
	public char[][] getIndexCategories() {
		return CATEGORIES;
	}
	protected boolean hasNextQuery() {
		if (this.segments == null) return false;
	
		// Optimization, eg. type reference is 'org.eclipse.jdt.core.*'
		// if package has at least 4 segments, don't look at the first 2 since they are mostly
		// redundant (eg. in 'org.eclipse.jdt.core.*' 'org.eclipse' is used all the time)
		return --this.currentSegment >= (this.segments.length >= 4 ? 2 : 0);
	}

	public boolean matchesDecodedKey(SearchPattern decodedPattern) {
		return true; // index key is not encoded so query results all match
	}

	protected void resetQuery() {
		/* walk the segments from end to start as it will find less potential references using 'lang' than 'java' */
		if (this.segments != null)
			this.currentSegment = this.segments.length - 1;
	}
	protected StringBuffer print(StringBuffer output) {
		output.append("TypeReferencePattern: qualification<"); //$NON-NLS-1$
		if (qualification != null) 
			output.append(qualification);
		else
			output.append("*"); //$NON-NLS-1$
		output.append(">, type<"); //$NON-NLS-1$
		if (simpleName != null) 
			output.append(simpleName);
		else
			output.append("*"); //$NON-NLS-1$
		output.append(">"); //$NON-NLS-1$
		return super.print(output);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5865.java