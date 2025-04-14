error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11079.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11079.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11079.java
text:
```scala
A@@smManager.getDefault().createNewASM(); // forget what you know...

/* *******************************************************************
 * Copyright (c) 2005 Contributors.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: 
 * Andy Clement          initial implementation
 * ******************************************************************/
package org.aspectj.ajdt.internal.core.builder;

import java.io.File;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.aspectj.asm.AsmManager;


/**
 * Central point for all things incremental...
 * - keeps track of the state recorded for each different config file
 * - allows limited interaction with these states
 *
 * - records dependency/change info for particular classpaths
 *   > this will become what JDT keeps in its 'State' object when its finished
 */
public class IncrementalStateManager {

	// FIXME asc needs an API through Ajde for trashing its contents
	// FIXME asc needs some memory mgmt (softrefs?) to recover memory
	// SECRETAPI will consume more memory, so turn on at your own risk ;)  Set to 'true' when memory usage is understood
	public  static boolean recordIncrementalStates = false;
	public  static boolean debugIncrementalStates = false;
	private static Hashtable incrementalStates = new Hashtable();
	
	public static void recordSuccessfulBuild(String buildConfig, AjState state) {
		if (!recordIncrementalStates) return;
		incrementalStates.put(buildConfig,state);
	}
	
	public static boolean removeIncrementalStateInformationFor(String buildConfig) {
		return incrementalStates.remove(buildConfig)!=null;
	}
	
	public static void clearIncrementalStates() {
		for (Iterator iter = incrementalStates.values().iterator(); iter.hasNext();) {
			AjState element = (AjState) iter.next();
			element.wipeAllKnowledge();
		}
		incrementalStates.clear();
		AsmManager.getDefault().createNewASM(null); // forget what you know...
	}
	
	public static Set getConfigFilesKnown() {
		return incrementalStates.keySet();
	}

	public static AjState retrieveStateFor(String configFile) {
		return (AjState)incrementalStates.get(configFile);
	}
	
	// now, managing changes to entries on a classpath
	
	public static AjState findStateManagingOutputLocation(File location) {
		Collection allStates = incrementalStates.values();
		if (debugIncrementalStates) System.err.println("> findStateManagingOutputLocation("+location+") has "+allStates.size()+" states to look through");
		for (Iterator iter = allStates.iterator(); iter.hasNext();) {
			AjState element = (AjState) iter.next();
			AjBuildConfig ajbc = element.getBuildConfig();
			if (ajbc==null) {
				// FIXME asc why can it ever be null?
				if (debugIncrementalStates) System.err.println("  No build configuration for state "+element);
				continue;
			}
			File outputDir = ajbc.getOutputDir();
			if (outputDir == null) {
				// FIXME why can it ever be null? due to using outjar?
				if (debugIncrementalStates) System.err.println("  output directory for "+ajbc+" is null");
				continue;
			}
			if (outputDir.equals(location)) {
				if (debugIncrementalStates) System.err.println("< findStateManagingOutputLocation("+location+") returning "+element);
				return element;
			}
		}
		if (debugIncrementalStates) System.err.println("< findStateManagingOutputLocation("+location+") returning null");
		return null;
	}
	
	// FIXME asc needs a persistence mechanism for storing/loading all state info
	// FIXME asc needs to understand two config files might point at the same output dir... what to do about this?
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11079.java