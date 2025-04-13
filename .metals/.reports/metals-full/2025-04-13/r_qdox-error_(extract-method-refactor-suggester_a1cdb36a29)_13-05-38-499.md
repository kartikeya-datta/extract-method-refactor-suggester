error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7571.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7571.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7571.java
text:
```scala
S@@ortedSet definedContextIds = new TreeSet(contextElementsById.keySet());

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

package org.eclipse.ui.internal.contexts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.contexts.IContext;
import org.eclipse.ui.contexts.IContextManager;
import org.eclipse.ui.contexts.IContextManagerEvent;
import org.eclipse.ui.contexts.IContextManagerListener;
import org.eclipse.ui.internal.util.Util;

public final class ContextManager implements IContextManager {

	private SortedSet activeContextIds = new TreeSet();
	private SortedMap contextElementsById = new TreeMap();
	private IContextManagerEvent contextManagerEvent;
	private List contextManagerListeners;
	private SortedMap contextsById = new TreeMap();
	private SortedSet definedContextIds = new TreeSet();
	private RegistryReader registryReader;

	public ContextManager() {
		super();
		updateDefinedContextIds();
	}

	public void addContextManagerListener(IContextManagerListener contextManagerListener)
		throws IllegalArgumentException {
		if (contextManagerListener == null)
			throw new IllegalArgumentException();
			
		if (contextManagerListeners == null)
			contextManagerListeners = new ArrayList();
		
		if (!contextManagerListeners.contains(contextManagerListener))
			contextManagerListeners.add(contextManagerListener);
	}

	public SortedSet getActiveContextIds() {
		return Collections.unmodifiableSortedSet(activeContextIds);
	}

	public IContext getContext(String contextId)
		throws IllegalArgumentException {
		if (contextId == null)
			throw new IllegalArgumentException();
			
		IContext context = (IContext) contextsById.get(contextId);
		
		if (context == null) {
			context = new Context(this, contextId);
			contextsById.put(contextId, context);
		}
		
		return context;
	}

	public SortedSet getDefinedContextIds() {
		return Collections.unmodifiableSortedSet(activeContextIds);
	}

	public void removeContextManagerListener(IContextManagerListener contextManagerListener)
		throws IllegalArgumentException {
		if (contextManagerListener == null)
			throw new IllegalArgumentException();
			
		if (contextManagerListeners != null) {
			contextManagerListeners.remove(contextManagerListener);
			
			if (contextManagerListeners.isEmpty())
				contextManagerListeners = null;
		}
	}

	public void setActiveContextIds(SortedSet activeContextIds)
		throws IllegalArgumentException {
		activeContextIds = Util.safeCopy(activeContextIds, String.class);
		SortedSet activatingContextIds = new TreeSet();
		SortedSet deactivatingContextIds = new TreeSet();
		Iterator iterator = activeContextIds.iterator();

		while (iterator.hasNext()) {
			String id = (String) iterator.next();
		
			if (!this.activeContextIds.contains(id))
				activatingContextIds.add(id);
		}

		iterator = this.activeContextIds.iterator();
		
		while (iterator.hasNext()) {
			String id = (String) iterator.next();
		
			if (!activeContextIds.contains(id))
				deactivatingContextIds.add(id);					
		}		

		SortedSet contextChanges = new TreeSet();
		contextChanges.addAll(activatingContextIds);		
		contextChanges.addAll(deactivatingContextIds);			

		if (!contextChanges.isEmpty()) {
			this.activeContextIds = activeContextIds;	
			fireContextManagerChanged();

			iterator = contextChanges.iterator();
		
			while (iterator.hasNext())
				fireContextChanged((String) iterator.next());
		}
	}

	public void updateDefinedContextIds() {
		if (registryReader == null)
			registryReader = new RegistryReader(Platform.getPluginRegistry());
		
		registryReader.load();
		SortedMap contextElementsById = ContextElement.sortedMapById(registryReader.getContextElements());		
		SortedSet contextElementAdditions = new TreeSet();		
		SortedSet contextElementChanges = new TreeSet();
		SortedSet contextElementRemovals = new TreeSet();		
		Iterator iterator = contextElementsById.entrySet().iterator();
		
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String id = (String) entry.getKey();
			ContextElement contextElement = (ContextElement) entry.getValue();
			
			if (!this.contextElementsById.containsKey(id))
				contextElementAdditions.add(id);
			else if (!Util.equals(contextElement, this.contextElementsById.get(id)))
				contextElementChanges.add(id);								
		}

		iterator = this.contextElementsById.entrySet().iterator();
		
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String id = (String) entry.getKey();
			ContextElement contextElement = (ContextElement) entry.getValue();
			
			if (!contextElementsById.containsKey(id))
				contextElementRemovals.add(id);						
		}

		SortedSet contextChanges = new TreeSet();
		contextChanges.addAll(contextElementAdditions);		
		contextChanges.addAll(contextElementChanges);		
		contextChanges.addAll(contextElementRemovals);
		
		if (!contextChanges.isEmpty()) {
			this.contextElementsById = contextElementsById;		
			SortedSet definedContextIds = (SortedSet) contextElementsById.keySet();

			if (!Util.equals(definedContextIds, this.definedContextIds)) {	
				this.definedContextIds = definedContextIds;
				fireContextManagerChanged();
			}

			iterator = contextChanges.iterator();
		
			while (iterator.hasNext())
				fireContextChanged((String) iterator.next());
		}
	}

	ContextElement getContextElement(String contextId) {
		return (ContextElement) contextElementsById.get(contextId);
	}
	
	private void fireContextChanged(String contextId) {
		Context context = (Context) contextsById.get(contextId);
		
		if (context != null) 
			context.fireContextChanged();		
	}

	private void fireContextManagerChanged() {
		if (contextManagerListeners != null) {
			Iterator iterator = contextManagerListeners.iterator();
			
			if (iterator.hasNext()) {
				if (contextManagerEvent == null)
					contextManagerEvent = new ContextManagerEvent(this);
				
				while (iterator.hasNext())	
					((IContextManagerListener) iterator.next()).contextManagerChanged(contextManagerEvent);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7571.java