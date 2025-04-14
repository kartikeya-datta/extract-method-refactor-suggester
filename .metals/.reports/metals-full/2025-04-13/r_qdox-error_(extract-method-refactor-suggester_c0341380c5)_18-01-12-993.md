error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9732.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9732.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9732.java
text:
```scala
d@@efaultOptionsMap.put(JavaCore.CORE_JAVA_BUILD_RECREATE_MODIFIED_CLASS_FILES_IN_OUTPUT_FOLDER, JavaCore.IGNORE);

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
package org.eclipse.jdt.internal.core;

import java.util.*;

import org.eclipse.core.runtime.preferences.*;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;

/**
 * JavaCore eclipse preferences initializer.
 * Initially done in JavaCore.initializeDefaultPreferences which was deprecated
 * with new eclipse preferences mechanism.
 */
public class JavaCorePreferenceInitializer extends AbstractPreferenceInitializer {

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		// If modified, also modify the method JavaModelManager#getDefaultOptionsNoInitialization()
		// Get options names set
		HashSet optionNames = JavaModelManager.getJavaModelManager().optionNames;
		
		// Compiler settings
		Map defaultOptionsMap = new CompilerOptions().getMap(); // compiler defaults
		
		// Override some compiler defaults
		defaultOptionsMap.put(JavaCore.COMPILER_LOCAL_VARIABLE_ATTR, JavaCore.GENERATE);
		defaultOptionsMap.put(JavaCore.COMPILER_CODEGEN_UNUSED_LOCAL, JavaCore.PRESERVE);
		defaultOptionsMap.put(JavaCore.COMPILER_TASK_TAGS, JavaCore.DEFAULT_TASK_TAGS);
		defaultOptionsMap.put(JavaCore.COMPILER_TASK_PRIORITIES, JavaCore.DEFAULT_TASK_PRIORITIES);
		defaultOptionsMap.put(JavaCore.COMPILER_TASK_CASE_SENSITIVE, JavaCore.ENABLED);
		defaultOptionsMap.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
		defaultOptionsMap.put(JavaCore.COMPILER_PB_FORBIDDEN_REFERENCE, JavaCore.ERROR);
		
		// Builder settings
		defaultOptionsMap.put(JavaCore.CORE_JAVA_BUILD_RESOURCE_COPY_FILTER, ""); //$NON-NLS-1$
		defaultOptionsMap.put(JavaCore.CORE_JAVA_BUILD_INVALID_CLASSPATH, JavaCore.ABORT); 
		defaultOptionsMap.put(JavaCore.CORE_JAVA_BUILD_DUPLICATE_RESOURCE, JavaCore.WARNING); 
		defaultOptionsMap.put(JavaCore.CORE_JAVA_BUILD_CLEAN_OUTPUT_FOLDER, JavaCore.CLEAN); 
		defaultOptionsMap.put(JavaCore.CORE_JAVA_BUILD_MAKE_OUTPUT_FOLDER_CONSISTENT, JavaCore.IGNORE); 

		// JavaCore settings
		defaultOptionsMap.put(JavaCore.CORE_JAVA_BUILD_ORDER, JavaCore.IGNORE); 
		defaultOptionsMap.put(JavaCore.CORE_INCOMPLETE_CLASSPATH, JavaCore.ERROR); 
		defaultOptionsMap.put(JavaCore.CORE_CIRCULAR_CLASSPATH, JavaCore.ERROR); 
		defaultOptionsMap.put(JavaCore.CORE_INCOMPATIBLE_JDK_LEVEL, JavaCore.IGNORE); 
		defaultOptionsMap.put(JavaCore.CORE_ENABLE_CLASSPATH_EXCLUSION_PATTERNS, JavaCore.ENABLED); 
		defaultOptionsMap.put(JavaCore.CORE_ENABLE_CLASSPATH_MULTIPLE_OUTPUT_LOCATIONS, JavaCore.ENABLED); 

		// encoding setting comes from resource plug-in
		optionNames.add(JavaCore.CORE_ENCODING);

		// Formatter settings
		Map codeFormatterOptionsMap = DefaultCodeFormatterConstants.getEclipseDefaultSettings(); // code formatter defaults
		for (Iterator iter = codeFormatterOptionsMap.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next();
			String optionName = (String) entry.getKey();
			defaultOptionsMap.put(optionName, entry.getValue());
			optionNames.add(optionName);
		}

		// CodeAssist settings
		defaultOptionsMap.put(JavaCore.CODEASSIST_VISIBILITY_CHECK, JavaCore.DISABLED);
		defaultOptionsMap.put(JavaCore.CODEASSIST_DEPRECATION_CHECK, JavaCore.DISABLED);
		defaultOptionsMap.put(JavaCore.CODEASSIST_IMPLICIT_QUALIFICATION, JavaCore.DISABLED);
		defaultOptionsMap.put(JavaCore.CODEASSIST_FIELD_PREFIXES, ""); //$NON-NLS-1$
		defaultOptionsMap.put(JavaCore.CODEASSIST_STATIC_FIELD_PREFIXES, ""); //$NON-NLS-1$
		defaultOptionsMap.put(JavaCore.CODEASSIST_LOCAL_PREFIXES, ""); //$NON-NLS-1$
		defaultOptionsMap.put(JavaCore.CODEASSIST_ARGUMENT_PREFIXES, ""); //$NON-NLS-1$
		defaultOptionsMap.put(JavaCore.CODEASSIST_FIELD_SUFFIXES, ""); //$NON-NLS-1$
		defaultOptionsMap.put(JavaCore.CODEASSIST_STATIC_FIELD_SUFFIXES, ""); //$NON-NLS-1$
		defaultOptionsMap.put(JavaCore.CODEASSIST_LOCAL_SUFFIXES, ""); //$NON-NLS-1$
		defaultOptionsMap.put(JavaCore.CODEASSIST_ARGUMENT_SUFFIXES, ""); //$NON-NLS-1$
		defaultOptionsMap.put(JavaCore.CODEASSIST_FORBIDDEN_REFERENCE_CHECK, JavaCore.ENABLED);
		defaultOptionsMap.put(JavaCore.CODEASSIST_DISCOURAGED_REFERENCE_CHECK, JavaCore.DISABLED);
		defaultOptionsMap.put(JavaCore.CODEASSIST_CAMEL_CASE_MATCH, JavaCore.ENABLED);
		
		// Time out for parameter names
		defaultOptionsMap.put(JavaCore.TIMEOUT_FOR_PARAMETER_NAME_FROM_ATTACHED_JAVADOC, "50"); //$NON-NLS-1$
		
		// Store default values to default preferences
	 	IEclipsePreferences defaultPreferences = ((IScopeContext) new DefaultScope()).getNode(JavaCore.PLUGIN_ID);
		for (Iterator iter = defaultOptionsMap.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next();
			String optionName = (String) entry.getKey();
			defaultPreferences.put(optionName, (String)entry.getValue());
			optionNames.add(optionName);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9732.java