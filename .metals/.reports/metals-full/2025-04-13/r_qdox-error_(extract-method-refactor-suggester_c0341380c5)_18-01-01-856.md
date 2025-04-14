error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3029.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3029.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3029.java
text:
```scala
private C@@ommandRegistryEvent commandRegistryEvent;

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

package org.eclipse.ui.internal.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class AbstractCommandRegistry implements ICommandRegistry {

	private ICommandRegistryEvent commandRegistryEvent;
	private List commandRegistryListeners;
	
	protected List activeKeyConfigurationDefinitions = Collections.EMPTY_LIST;
	protected List activityBindingDefinitions = Collections.EMPTY_LIST;
	protected List categoryDefinitions = Collections.EMPTY_LIST; 
	protected List commandDefinitions = Collections.EMPTY_LIST; 
	protected List imageBindingDefinitions = Collections.EMPTY_LIST;
	protected List keyConfigurationDefinitions = Collections.EMPTY_LIST;	
	protected List keySequenceBindingDefinitions = Collections.EMPTY_LIST;
	
	protected AbstractCommandRegistry() {
	}

	public void addCommandRegistryListener(ICommandRegistryListener commandRegistryListener) {
		if (commandRegistryListener == null)
			throw new NullPointerException();
			
		if (commandRegistryListeners == null)
			commandRegistryListeners = new ArrayList();
		
		if (!commandRegistryListeners.contains(commandRegistryListener))
			commandRegistryListeners.add(commandRegistryListener);
	}

	public List getActiveKeyConfigurationDefinitions() {
		return activeKeyConfigurationDefinitions;
	}

	public List getActivityBindingDefinitions() {
		return activityBindingDefinitions;
	}

	public List getCategoryDefinitions() {
		return categoryDefinitions;
	}
	
	public List getCommandDefinitions() {
		return commandDefinitions;
	}

	public List getImageBindingDefinitions() {
		return imageBindingDefinitions;
	}
	
	public List getKeyConfigurationDefinitions() {
		return keyConfigurationDefinitions;
	}

	public List getKeySequenceBindingDefinitions() {
		return keySequenceBindingDefinitions;
	}

	public void removeCommandRegistryListener(ICommandRegistryListener commandRegistryListener) {
		if (commandRegistryListener == null)
			throw new NullPointerException();
			
		if (commandRegistryListeners != null)
			commandRegistryListeners.remove(commandRegistryListener);
	}

	protected void fireCommandRegistryChanged() {
		if (commandRegistryListeners != null) {
			for (int i = 0; i < commandRegistryListeners.size(); i++) {
				if (commandRegistryEvent == null)
					commandRegistryEvent = new CommandRegistryEvent(this);
							
				((ICommandRegistryListener) commandRegistryListeners.get(i)).commandRegistryChanged(commandRegistryEvent);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3029.java