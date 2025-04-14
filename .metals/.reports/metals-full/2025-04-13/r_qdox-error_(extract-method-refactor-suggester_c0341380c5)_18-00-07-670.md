error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5090.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5090.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5090.java
text:
```scala
r@@eturn new PatternBindingDefinition(activityId, pluginId, activityId);

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.internal.csm.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.ui.IMemento;
import org.eclipse.ui.internal.util.Util;

final class Persistence {

	final static String PACKAGE_BASE = "activities"; //$NON-NLS-1$
	final static String PACKAGE_FULL = "org.eclipse.ui." + PACKAGE_BASE; //$NON-NLS-1$
	final static String TAG_ACTIVITY_BINDING = "activityBinding"; //$NON-NLS-1$	
	final static String TAG_ACTIVITY_ID = "activityId"; //$NON-NLS-1$	
	final static String TAG_DESCRIPTION = "description"; //$NON-NLS-1$
	final static String TAG_ID = "id"; //$NON-NLS-1$
	final static String TAG_NAME = "name"; //$NON-NLS-1$	
	final static String TAG_PARENT_ID = "parentId"; //$NON-NLS-1$
	final static String TAG_PATTERN = "pattern"; //$NON-NLS-1$	
	final static String TAG_PLUGIN_ID = "pluginId"; //$NON-NLS-1$
	final static String TAG_ACTIVITY = "activity"; //$NON-NLS-1$	

	static IActivityPatternBindingDefinition readActivityBindingDefinition(IMemento memento, String pluginIdOverride) {
		if (memento == null)
			throw new NullPointerException();			

		String activityId = memento.getString(TAG_ACTIVITY_ID);
		String pluginId = pluginIdOverride != null ? pluginIdOverride : memento.getString(TAG_PLUGIN_ID);
		String pattern = memento.getString(TAG_PATTERN);
		return new ActivityPatternBindingDefinition(activityId, pluginId, activityId);
	}

	static List readActivityBindingDefinitions(IMemento memento, String name, String pluginIdOverride) {
		if (memento == null || name == null)
			throw new NullPointerException();			
	
		IMemento[] mementos = memento.getChildren(name);
	
		if (mementos == null)
			throw new NullPointerException();
	
		List list = new ArrayList(mementos.length);
	
		for (int i = 0; i < mementos.length; i++)
			list.add(readActivityBindingDefinition(mementos[i], pluginIdOverride));
	
		return list;				
	}

	static IActivityDefinition readActivityDefinition(IMemento memento, String pluginIdOverride) {
		if (memento == null)
			throw new NullPointerException();			

		String description = memento.getString(TAG_DESCRIPTION);
		String id = memento.getString(TAG_ID);
		String name = memento.getString(TAG_NAME);
		String parentId = memento.getString(TAG_PARENT_ID);
		String pluginId = pluginIdOverride != null ? pluginIdOverride : memento.getString(TAG_PLUGIN_ID);
		return new ActivityDefinition(description, id, name, parentId, pluginId);
	}

	static List readActivityDefinitions(IMemento memento, String name, String pluginIdOverride) {
		if (memento == null || name == null)
			throw new NullPointerException();			
	
		IMemento[] mementos = memento.getChildren(name);
	
		if (mementos == null)
			throw new NullPointerException();
	
		List list = new ArrayList(mementos.length);
	
		for (int i = 0; i < mementos.length; i++)
			list.add(readActivityDefinition(mementos[i], pluginIdOverride));
	
		return list;				
	}

	static void writeActivityBindingDefinition(IMemento memento, IActivityPatternBindingDefinition activityBindingDefinition) {
		if (memento == null || activityBindingDefinition == null)
			throw new NullPointerException();

		memento.putString(TAG_ACTIVITY_ID, activityBindingDefinition.getActivityId());
		memento.putString(TAG_PLUGIN_ID, activityBindingDefinition.getPluginId());
		memento.putString(TAG_PATTERN, activityBindingDefinition.getActivityId());
	}

	static void writeActivityBindingDefinitions(IMemento memento, String name, List activityBindingDefinitions) {
		if (memento == null || name == null || activityBindingDefinitions == null)
			throw new NullPointerException();
		
		activityBindingDefinitions = new ArrayList(activityBindingDefinitions);
		Iterator iterator = activityBindingDefinitions.iterator();

		while (iterator.hasNext())
			Util.assertInstance(iterator.next(), IActivityPatternBindingDefinition.class);

		iterator = activityBindingDefinitions.iterator();

		while (iterator.hasNext()) 
			writeActivityBindingDefinition(memento.createChild(name), (IActivityPatternBindingDefinition) iterator.next());
	}

	static void writeActivityDefinition(IMemento memento, IActivityDefinition activityDefinition) {
		if (memento == null || activityDefinition == null)
			throw new NullPointerException();

		memento.putString(TAG_DESCRIPTION, activityDefinition.getDescription());
		memento.putString(TAG_ID, activityDefinition.getId());
		memento.putString(TAG_NAME, activityDefinition.getName());
		memento.putString(TAG_PARENT_ID, activityDefinition.getParentId());
		memento.putString(TAG_PLUGIN_ID, activityDefinition.getPluginId());
	}

	static void writeActivityDefinitions(IMemento memento, String name, List activityDefinitions) {
		if (memento == null || name == null || activityDefinitions == null)
			throw new NullPointerException();
		
		activityDefinitions = new ArrayList(activityDefinitions);
		Iterator iterator = activityDefinitions.iterator();

		while (iterator.hasNext())
			Util.assertInstance(iterator.next(), IActivityDefinition.class);

		iterator = activityDefinitions.iterator();

		while (iterator.hasNext()) 
			writeActivityDefinition(memento.createChild(name), (IActivityDefinition) iterator.next());
	}

	private Persistence() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5090.java