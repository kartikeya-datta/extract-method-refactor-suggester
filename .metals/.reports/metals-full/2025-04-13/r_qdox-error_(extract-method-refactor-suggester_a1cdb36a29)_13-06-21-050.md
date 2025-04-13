error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13630.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13630.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13630.java
text:
```scala
public static final S@@tring COMPILER_TERMINATE_AFTER_COMPILATION             = AjCompilerOptions.OPTION_TerminateAfterCompilation;

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
package org.aspectj.ajdt.core;

import java.util.Map;

import org.aspectj.ajdt.internal.core.builder.AjCompilerOptions;
import org.aspectj.org.eclipse.jdt.core.JavaCore;

/**
 * This is the plugin class for AspectJ.
 */
public class AspectJCore extends JavaCore {

	public static final String COMPILER_PB_INVALID_ABSOLUTE_TYPE_NAME  = AjCompilerOptions.OPTION_ReportInvalidAbsoluteTypeName;
	public static final String COMPILER_PB_INVALID_WILDCARD_TYPE_NAME  = AjCompilerOptions.OPTION_ReportInvalidWildcardTypeName;
	public static final String COMPILER_PB_UNRESOLVABLE_MEMBER         = AjCompilerOptions.OPTION_ReportUnresolvableMember;
	public static final String COMPILER_PB_TYPE_NOT_EXPOSED_TO_WEAVER  = AjCompilerOptions.OPTION_ReportTypeNotExposedToWeaver;
	public static final String COMPILER_PB_SHADOW_NOT_IN_STRUCTURE     = AjCompilerOptions.OPTION_ReportShadowNotInStructure;
	public static final String COMPILER_PB_UNMATCHED_SUPERTYPE_IN_CALL = AjCompilerOptions.OPTION_ReportUnmatchedSuperTypeInCall;
	public static final String COMPILER_PB_CANNOT_IMPLEMENT_LAZY_TJP   = AjCompilerOptions.OPTION_ReportCannotImplementLazyTJP;
	public static final String COMPILER_PB_NEED_SERIAL_VERSION_UID     = AjCompilerOptions.OPTION_ReportNeedSerialVersionUIDField;
	public static final String COMPILER_PB_INCOMPATIBLE_SERIAL_VERSION = AjCompilerOptions.OPTION_ReportIncompatibleSerialVersion;
	
	public static final String COMPILER_NO_WEAVE             = AjCompilerOptions.OPTION_NoWeave;
	public static final String COMPILER_SERIALIZABLE_ASPECTS = AjCompilerOptions.OPTION_XSerializableAspects;
	public static final String COMPILER_LAZY_TJP             = AjCompilerOptions.OPTION_XLazyThisJoinPoint;
	public static final String COMPILER_NO_ADVICE_INLINE     = AjCompilerOptions.OPTION_XNoInline;
	public static final String COMPILER_NOT_REWEAVABLE       = AjCompilerOptions.OPTION_XNotReweavable;
	
	public AspectJCore() {
		super();
	}
	
	public static AspectJCore getAspectJCore() {
		return (AspectJCore) getPlugin();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.JavaCore#getCompilerOptions()
	 */
	protected Map getCompilerOptions() {
		return new AjCompilerOptions().getMap();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13630.java