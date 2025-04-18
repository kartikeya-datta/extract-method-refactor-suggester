error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2487.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2487.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2487.java
text:
```scala
S@@tring pluginId = ext.getNamespace();

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
package org.eclipse.ui.internal.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;

public abstract class RegistryManager implements IRegistryChangeListener {
	private String elementId;
	private String extPtId;
	private HashMap cache;
	public static final int REGISTRY_CACHE_STATE_UNKNOWN = 0;
	public static final int REGISTRY_CACHE_STATE_ACTIVE = 1;
	public static final int REGISTRY_CACHE_STATE_DELETED = 2;
	public static final int REGISTRY_CACHE_STATE_MAX = 2;
	public static final String INTERNAL_REGISTRY_ADDITION = "InternalRegistryAddition"; //$NON-NLS-1$
	private int numObjects = 0;
	private class RegistryElement {
		private int state;
		private ArrayList realObjects = null;
		
		public RegistryElement(Object obj) {
			state = REGISTRY_CACHE_STATE_UNKNOWN;
			if (realObjects == null) {
				realObjects = new ArrayList();
			}
			realObjects.add(obj);
			numObjects++;
		}
		
		public void addNewObject (Object obj) {
			if (realObjects == null) {
				realObjects = new ArrayList();
			}
			realObjects.add(obj);
			numObjects++;
		}
		
		public void changeState(int newState) {
			if ((newState > REGISTRY_CACHE_STATE_UNKNOWN) && 
				(newState <= REGISTRY_CACHE_STATE_MAX))
				state = newState;
		}
		
		public ArrayList getRealObjects() {
			return realObjects;
		}
		
		public int howManyObjects() {
			return numObjects;
		}
	}
	
	public RegistryManager(String elementId, String extPtId) {
		this.elementId = elementId;
		this.extPtId = extPtId;
		cache = new HashMap();
	}
	
	public RegistryManager getCache() {
		return this;
	}
	
	public Object[] getRegistryObjects() {
		Object[] regElements = cache.values().toArray();
		if (regElements.length == 0)
			return null;
		Object[] ret = new Object[numObjects];
		int retIdx = 0;
		for (int i = 0; i < regElements.length; i++) {
			ArrayList listElement = ((RegistryElement)regElements[i]).getRealObjects();
			if (listElement != null) {
				ListIterator iter = listElement.listIterator();
				while (iter.hasNext()) {
					ret[retIdx++] = iter.next();
				}
			}
		}
		return ret;
	}
	
	public void registryChanged(IRegistryChangeEvent event) {
		int numDeltas = 0;
		try {
			// Just retrieve any changes relating to the extension point
			// org.eclipse.ui.perspectives
			IExtensionDelta delta[] = event.getExtensionDeltas(elementId, extPtId);
			numDeltas = delta.length;
			for (int i = 0; i < numDeltas; i++) {
				add (delta[i]);
			}
		} finally {
			if (numDeltas > 0) {
				// Only do the post-change processing if something was
				// actually changed in the registry.  If there were no
				// deltas of relevance to this registry, there should be
				// no need to do any extra post-change processing.
				postChangeProcessing();
			}
		}
	}
	
	public void add(IExtensionDelta delta) {
		IExtensionPoint extPt = delta.getExtensionPoint();
		IExtension ext = delta.getExtension();
		// Get the name of the plugin that is adding this extension.  The
		// name of the plugin that adds the extension point is us.
		String pluginId = ext.getDeclaringPluginDescriptor().getUniqueIdentifier();
		add(buildNewCacheObject(delta), pluginId);
	}
	
	public void add(Object element, String pluginId) {
		if (element == null)
			// Nothing to add, so just return.
			return;
		String toUsePluginId = pluginId;
		if (pluginId == null || pluginId.length() == 0) {
			// This element is being added to the registry but is not
			// associated with a regular plug-in.  Likely, the element
			// was created programmatically and needs to exist in the
			// registry.  An example is the 'Other' category for views.
			// Use the key INTERNAL_REGISTRY_ADDITION for these elements so
			// they will not be removed from the registry with any plug-in
			// removal.
			toUsePluginId = INTERNAL_REGISTRY_ADDITION;
		}
		RegistryElement regElement = (RegistryElement)cache.get(toUsePluginId);
		if (regElement == null) {
			regElement = new RegistryElement(element);
			cache.put(toUsePluginId, regElement);
		} else {
			regElement.addNewObject(element);
		}
	}
	
	/**
	 * This is a generic method that is expected to be over-written by the
	 * parent class.  It should return a new element with all the relevant
	 * information from the delta.
	 * @param delta a delta from a listener on extension events
	 * @return a new object to be added to the registry cache
	 */
	abstract public Object buildNewCacheObject (IExtensionDelta delta);
	/**
	 * This is a generic method that is expected to be implemented by the
	 * parent class.  It should do any processing necessary once deltas
	 * have been processed and the registry modified.  Note that in some
	 * cases there may be no extra processing required.
	 */
	abstract public void postChangeProcessing();
	/**
	 * Flag a series of elements in this registry cache as 'to be removed'.
	 * This does not actually remove these elements as some processing may
	 * be required before they are removed.
	 * 
	 * @param plugins a list of plug-in ids for each plug-in that is being
	 * removed.  Only those elements in the registry cache that correspond
	 * to one of these plug-ins will be flagged for removal.
	 */
	public void remove(String[] plugins) {
		for(int i = 0; i < plugins.length; i++) {
			remove(plugins[i]);
		}
	}
	
	public void remove(String pluginId) {
		RegistryElement element = (RegistryElement)cache.get(pluginId);
		if (element != null)
			element.changeState(REGISTRY_CACHE_STATE_DELETED);
	}
	
	/**
	 * Actually go through and remove any elements from the registry that
	 * are flagged for removal.  This assumes that any processing required
	 * by the parent (child?) prior to removal of this element has been 
	 * completed.
	 */
	public void cleanRegistry() {
		Set elements = cache.keySet();
		Iterator iter = elements.iterator();
		while (iter.hasNext()) {
			String pluginId = (String)iter.next();
			RegistryElement elem = (RegistryElement)cache.get(pluginId);
			if (elem != null && elem.state == REGISTRY_CACHE_STATE_DELETED) {
				cache.remove(pluginId);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2487.java