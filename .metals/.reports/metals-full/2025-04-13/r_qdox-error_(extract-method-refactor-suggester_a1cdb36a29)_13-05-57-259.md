error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6936.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6936.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6936.java
text:
```scala
c@@har[] source = signature.replace('/','.').replace('$','.').toCharArray();

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.search.matching;

import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.search.SearchPattern;


public class JavaSearchPattern extends SearchPattern {
	
	/*
	 * Whether this pattern is case sensitive.
	 */
	boolean isCaseSensitive;

	/*
	 * One of R_EXACT_MATCH, R_PREFIX_MATCH, R_PATTERN_MATCH, R_REGEXP_MATCH.
	 */
	int matchMode;
	
	// Signatures and arguments for parameterized types search
	char[][] typeSignatures;
	char[][][] typeArguments;

	protected JavaSearchPattern(int patternKind, int matchRule) {
		super(matchRule);
		((InternalSearchPattern)this).kind = patternKind;
		this.isCaseSensitive = (matchRule & R_CASE_SENSITIVE) != 0;
		this.matchMode = matchRule - (this.isCaseSensitive ? R_CASE_SENSITIVE : 0);
	}
	
	public SearchPattern getBlankPattern() {
		return null;
	}

	int getMatchMode() {
		return this.matchMode;
	}

	boolean isCaseSensitive () {
		return this.isCaseSensitive;
	}
	
	/**
	 * Returns whether the pattern includess type arguments information or not.
	 * @return true if pattern has signature *and* type arguments
	 */
	public boolean isParameterized() {
		return this.typeSignatures != null && this.typeArguments != null;
	}

	/* (non-Javadoc)
	 * Compute a IJavaElement signature or a string pattern signature to store
	 * its type arguments. Recurse when signature is qualified to store signatures and
	 * type arguments also for of all enclosing types.
	 */
	void computeSignature(String signature) {
		// In case of IJavaElement signature, replace '/' by '.'
		char[] source = signature.replace('/','.').toCharArray();

		// Init counters and arrays
		char[][] signatures = new char[10][];
		int signaturesCount = 0;
		int[] lengthes = new int [10];
		int typeArgsCount = 0;
		int paramOpening = 0;
		boolean parameterized = false;
		
		// Scan each signature character
		for (int idx=0, ln = source.length; idx < ln; idx++) {
			switch (source[idx]) {
				case '>':
					paramOpening--;
					if (paramOpening == 0)  {
						if (signaturesCount == lengthes.length) {
							System.arraycopy(signatures, 0, signatures = new char[signaturesCount+10][], 0, signaturesCount);
							System.arraycopy(lengthes, 0, lengthes = new int[signaturesCount+10], 0, signaturesCount);
						}
						lengthes[signaturesCount] = typeArgsCount;
						typeArgsCount = 0;
					}
					break;
				case '<':
					paramOpening++;
					if (paramOpening == 1) {
						typeArgsCount = 0;
						parameterized = true;
					}
					break;
				case '*':
				case ';':
					if (paramOpening == 1) typeArgsCount++;
					break;
				case '.':
					if (paramOpening == 0)  {
						if (signaturesCount == lengthes.length) {
							System.arraycopy(signatures, 0, signatures = new char[signaturesCount+10][], 0, signaturesCount);
							System.arraycopy(lengthes, 0, lengthes = new int[signaturesCount+10], 0, signaturesCount);
						}
						signatures[signaturesCount] = new char[idx+1];
						System.arraycopy(source, 0, signatures[signaturesCount], 0, idx);
						signatures[signaturesCount][idx] = Signature.C_SEMICOLON;
						signaturesCount++;
					}
					break;
			}
		}
		
		// Store signatures and type arguments
		this.typeSignatures = new char[signaturesCount+1][];
		if (parameterized)
			this.typeArguments = new char[signaturesCount+1][][];
		this.typeSignatures[0] = source;
		if (parameterized) {
			this.typeArguments[0] = Signature.getTypeArguments(source);
			if (lengthes[signaturesCount] != this.typeArguments[0].length) {
				// TODO (frederic) abnormal signature => should raise an error
			}
		}
		for (int i=1, j=signaturesCount-1; i<=signaturesCount; i++, j--){
			this.typeSignatures[i] = signatures[j];
			if (parameterized) {
				this.typeArguments[i] = Signature.getTypeArguments(signatures[j]);
				if (lengthes[j] != this.typeArguments[i].length) {
					// TODO (frederic) abnormal signature => should raise an error
				}
			}
		}
	}

	/*
	 * Optimization of implementation above (uses cached matchMode and isCaseSenistive)
	 */
	public boolean matchesName(char[] pattern, char[] name) {
		if (pattern == null) return true; // null is as if it was "*"
		if (name != null) {
			switch (this.matchMode) {
				case R_EXACT_MATCH :
					return CharOperation.equals(pattern, name, this.isCaseSensitive);
				case R_PREFIX_MATCH :
					return CharOperation.prefixEquals(pattern, name, this.isCaseSensitive);
				case R_PATTERN_MATCH :
					if (!this.isCaseSensitive)
						pattern = CharOperation.toLowerCase(pattern);
					return CharOperation.match(pattern, name, this.isCaseSensitive);
				case R_REGEXP_MATCH :
					// TODO (frederic) implement regular expression match
					return true;
			}
		}
		return false;
	}
	protected StringBuffer print(StringBuffer output) {
		output.append(", "); //$NON-NLS-1$
		if (this.typeSignatures != null && this.typeSignatures.length > 0) {
			output.append("signature:\""); //$NON-NLS-1$
			output.append(this.typeSignatures[0]);
			output.append("\", "); //$NON-NLS-1$
		}
		switch(getMatchMode()) {
			case R_EXACT_MATCH : 
				output.append("exact match, "); //$NON-NLS-1$
				break;
			case R_PREFIX_MATCH :
				output.append("prefix match, "); //$NON-NLS-1$
				break;
			case R_PATTERN_MATCH :
				output.append("pattern match, "); //$NON-NLS-1$
				break;
		}
		if (isCaseSensitive())
			output.append("case sensitive"); //$NON-NLS-1$
		else
			output.append("case insensitive"); //$NON-NLS-1$
		return output;
	}
	public final String toString() {
		return print(new StringBuffer(30)).toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6936.java