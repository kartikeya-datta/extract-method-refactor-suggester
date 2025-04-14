error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6857.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6857.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6857.java
text:
```scala
J@@avaProject project = (JavaProject) this.javaModel.getJavaProject(projectName);

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

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.core.runtime.preferences.PreferenceModifyListener;
import org.eclipse.jdt.core.*;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class JavaCorePreferenceModifyListener extends PreferenceModifyListener {
	
	static int PREFIX_LENGTH = JavaModelManager.CP_CONTAINER_PREFERENCES_PREFIX.length();
	JavaModel javaModel = JavaModelManager.getJavaModelManager().getJavaModel();

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.PreferenceModifyListener#preApply(org.eclipse.core.runtime.preferences.IEclipsePreferences)
	 */
	public IEclipsePreferences preApply(IEclipsePreferences node) {
		Preferences instance = node.node(InstanceScope.SCOPE);
		cleanJavaCore(instance.node(JavaCore.PLUGIN_ID));
		return super.preApply(node);
	}
	
	/**
	 * Clean imported preferences from obsolete keys.
	 *
	 * @param preferences JavaCore preferences.
	 */
	void cleanJavaCore(Preferences preferences) {
		try {
			String[] keys = preferences.keys();
			for (int k = 0, kl= keys.length; k<kl; k++) {
				String key = keys[k];
				if (key.startsWith(JavaModelManager.CP_CONTAINER_PREFERENCES_PREFIX) && !isJavaProjectAccessible(key)) {
					preferences.remove(key);
				}
			}
		} catch (BackingStoreException e) {
			// do nothing
		}
	}

	/**
	 * Returns whether a java project referenced in property key
	 * is still longer accessible or not.
	 * 
	 * @param propertyName
	 * @return true if a project is referenced in given key and this project
	 * 	is still accessible, false otherwise.
	 */
	boolean isJavaProjectAccessible(String propertyName) {
		int index = propertyName.indexOf('|', PREFIX_LENGTH);
		if (index > 0) {
			final String projectName = propertyName.substring(PREFIX_LENGTH, index).trim();
			JavaProject project = (JavaProject) javaModel.getJavaProject(projectName);
			if (project.getProject().isAccessible()) {
				return true;
			}
		}
		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6857.java