error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8633.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8633.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8633.java
text:
```scala
i@@f ((optionValue = optionsMap.get(OPTION_PerformDiscouragedReferenceCheck)) != null) {

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.codeassist.impl;

import java.util.Map;

import org.eclipse.jdt.core.compiler.CharOperation;

public class AssistOptions {
	/**
	 * Option IDs
	 */
	public static final String OPTION_PerformVisibilityCheck =
		"org.eclipse.jdt.core.codeComplete.visibilityCheck"; 	//$NON-NLS-1$
	public static final String OPTION_ForceImplicitQualification =
		"org.eclipse.jdt.core.codeComplete.forceImplicitQualification"; 	//$NON-NLS-1$
	public static final String OPTION_FieldPrefixes =
		"org.eclipse.jdt.core.codeComplete.fieldPrefixes"; 	//$NON-NLS-1$
	public static final String OPTION_StaticFieldPrefixes =
		"org.eclipse.jdt.core.codeComplete.staticFieldPrefixes"; 	//$NON-NLS-1$
	public static final String OPTION_LocalPrefixes =
		"org.eclipse.jdt.core.codeComplete.localPrefixes"; 	//$NON-NLS-1$
	public static final String OPTION_ArgumentPrefixes =
		"org.eclipse.jdt.core.codeComplete.argumentPrefixes"; 	//$NON-NLS-1$
	public static final String OPTION_FieldSuffixes =
		"org.eclipse.jdt.core.codeComplete.fieldSuffixes"; 	//$NON-NLS-1$
	public static final String OPTION_StaticFieldSuffixes =
		"org.eclipse.jdt.core.codeComplete.staticFieldSuffixes"; 	//$NON-NLS-1$
	public static final String OPTION_LocalSuffixes =
		"org.eclipse.jdt.core.codeComplete.localSuffixes"; 	//$NON-NLS-1$
	public static final String OPTION_ArgumentSuffixes =
		"org.eclipse.jdt.core.codeComplete.argumentSuffixes"; 	//$NON-NLS-1$
	public static final String OPTION_PerformForbiddenReferenceCheck =
		"org.eclipse.jdt.core.codeComplete.forbiddenReferenceCheck"; 	//$NON-NLS-1$
	public static final String OPTION_PerformDiscouragedReferenceCheck =
		"org.eclipse.jdt.core.codeComplete.discouragedReferenceCheck"; 	//$NON-NLS-1$
	
	public static final String ENABLED = "enabled"; //$NON-NLS-1$
	public static final String DISABLED = "disabled"; //$NON-NLS-1$
	
	public boolean checkVisibility = false;
	public boolean checkForbiddenReference = false;
	public boolean checkDiscouragedReference = false;
	public boolean forceImplicitQualification = false;
	public char[][] fieldPrefixes = null;
	public char[][] staticFieldPrefixes = null;
	public char[][] localPrefixes = null;
	public char[][] argumentPrefixes = null;
	public char[][] fieldSuffixes = null;
	public char[][] staticFieldSuffixes = null;
	public char[][] localSuffixes = null;
	public char[][] argumentSuffixes = null;

	/** 
	 * Initializing the assist options with default settings
	 */
	public AssistOptions() {
		// Initializing the assist options with default settings
	}

	/** 
	 * Initializing the assist options with external settings
	 */
	public AssistOptions(Map settings) {
		if (settings == null)
			return;

		set(settings);
	}
	public void set(Map optionsMap) {

		Object optionValue;
		if ((optionValue = optionsMap.get(OPTION_PerformVisibilityCheck)) != null) {
			if (ENABLED.equals(optionValue)) {
				this.checkVisibility = true;
			} else if (DISABLED.equals(optionValue)) {
				this.checkVisibility = false;
			}
		}
		if ((optionValue = optionsMap.get(OPTION_ForceImplicitQualification)) != null) {
			if (ENABLED.equals(optionValue)) {
				this.forceImplicitQualification = true;
			} else if (DISABLED.equals(optionValue)) {
				this.forceImplicitQualification = false;
			}
		}
		if ((optionValue = optionsMap.get(OPTION_FieldPrefixes)) != null) {
			if (optionValue instanceof String) {
				String stringValue = (String) optionValue;
				if (stringValue.length() > 0){
					this.fieldPrefixes = CharOperation.splitAndTrimOn(',', stringValue.toCharArray());
				} else {
					this.fieldPrefixes = null;
				}
			}
		}
		if ((optionValue = optionsMap.get(OPTION_StaticFieldPrefixes)) != null) {
			if (optionValue instanceof String) {
				String stringValue = (String) optionValue;
				if (stringValue.length() > 0){
					this.staticFieldPrefixes = CharOperation.splitAndTrimOn(',', stringValue.toCharArray());
				} else {
					this.staticFieldPrefixes = null;
				}
			}
		}
		if ((optionValue = optionsMap.get(OPTION_LocalPrefixes)) != null) {
			if (optionValue instanceof String) {
				String stringValue = (String) optionValue;
				if (stringValue.length() > 0){
					this.localPrefixes = CharOperation.splitAndTrimOn(',', stringValue.toCharArray());
				} else {
					this.localPrefixes = null;
				}
			}
		}
		if ((optionValue = optionsMap.get(OPTION_ArgumentPrefixes)) != null) {
			if (optionValue instanceof String) {
				String stringValue = (String) optionValue;
				if (stringValue.length() > 0){
					this.argumentPrefixes = CharOperation.splitAndTrimOn(',', stringValue.toCharArray());
				} else {
					this.argumentPrefixes = null;
				}
			}
		}
		if ((optionValue = optionsMap.get(OPTION_FieldSuffixes)) != null) {
			if (optionValue instanceof String) {
				String stringValue = (String) optionValue;
				if (stringValue.length() > 0){
					this.fieldSuffixes = CharOperation.splitAndTrimOn(',', stringValue.toCharArray());
				} else {
					this.fieldSuffixes = null;
				}
			}
		}
		if ((optionValue = optionsMap.get(OPTION_StaticFieldSuffixes)) != null) {
			if (optionValue instanceof String) {
				String stringValue = (String) optionValue;
				if (stringValue.length() > 0){
					this.staticFieldSuffixes = CharOperation.splitAndTrimOn(',', stringValue.toCharArray());
				} else {
					this.staticFieldSuffixes = null;
				}
			}
		}
		if ((optionValue = optionsMap.get(OPTION_LocalSuffixes)) != null) {
			if (optionValue instanceof String) {
				String stringValue = (String) optionValue;
				if (stringValue.length() > 0){
					this.localSuffixes = CharOperation.splitAndTrimOn(',', stringValue.toCharArray());
				} else {
					this.localSuffixes = null;
				}
			}
		}
		if ((optionValue = optionsMap.get(OPTION_ArgumentSuffixes)) != null) {
			if (optionValue instanceof String) {
				String stringValue = (String) optionValue;
				if (stringValue.length() > 0){
					this.argumentSuffixes = CharOperation.splitAndTrimOn(',', stringValue.toCharArray());
				} else {
					this.argumentSuffixes = null;
				}
			}
		}
		if ((optionValue = optionsMap.get(OPTION_PerformForbiddenReferenceCheck)) != null) {
			if (ENABLED.equals(optionValue)) {
				this.checkForbiddenReference = true;
			} else if (DISABLED.equals(optionValue)) {
				this.checkForbiddenReference = false;
			}
		}
		if ((optionValue = optionsMap.get(OPTION_PerformForbiddenReferenceCheck)) != null) {
			if (ENABLED.equals(optionValue)) {
				this.checkDiscouragedReference = true;
			} else if (DISABLED.equals(optionValue)) {
				this.checkDiscouragedReference = false;
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8633.java