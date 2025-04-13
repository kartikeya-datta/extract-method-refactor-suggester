error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9452.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9452.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9452.java
text:
```scala
i@@f (definitionsToNatures == null || event.getDelta() == null)

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

package org.eclipse.ui.internal.decorators;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelUpdateValidator;
import org.eclipse.ui.internal.WorkbenchPlugin;

/**
 * The ResourceUpdateListener listens for resource changes for the declarative
 * decorators.
 * 
 * @since 3.2
 * 
 */
public class ResourceUpdateListener implements IResourceChangeListener {

	private HashMap definitionsToNatures = new HashMap();

	private HashMap projectsNaturesCache = new HashMap();

	private Collection allNatures = new HashSet();

	/**
	 * Create a new instance of the receiver.
	 */
	ResourceUpdateListener() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
	 */
	public void resourceChanged(IResourceChangeEvent event) {

		if (definitionsToNatures == null)
			return;

		IResourceDelta[] deltas = event.getDelta().getAffectedChildren();
		for (int i = 0; i < deltas.length; i++) {
			if ((deltas[i].getFlags() | IResourceDelta.DESCRIPTION) > 0) {
				IResource resource = deltas[i].getResource();
				if (checkProjectFileUpdate(deltas[i]))
					WorkbenchPlugin.getDefault().getDecoratorManager()
							.updateForValueChanged(resource, getValidator());
			}
			
			//Clear them if they are removed
			if((deltas[i].getFlags() | IResourceDelta.REMOVED) > 0){
				projectsNaturesCache.remove(deltas[i].getResource());
			}
		}

	}

	/**
	 * Return the validator for the receiver.
	 * 
	 * @return ILabelUpdateValidator
	 */
	private ILabelUpdateValidator getValidator() {
		return new ILabelUpdateValidator() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.ILabelUpdateValidator#shouldUpdate(java.lang.Object)
			 */
			public String shouldUpdate(Object element) {
				IProject project = (IProject) element;
				String[] natures;
				try {
					natures = project.getDescription().getNatureIds();
				} catch (CoreException e) {
					return null;// As the natures are unreachable don't make it
								// worse
				}
				if (projectsNaturesCache.containsKey(project)) {
					String[] cachedNatures = (String[]) projectsNaturesCache
							.get(project);
					if (naturesChanged(natures, cachedNatures))
						return DecoratingLabelProvider.UPDATE_LABEL;
					return null;
				}
				projectsNaturesCache.put(project, natures);
				return DecoratingLabelProvider.UPDATE_LABEL;

			}
		};
	}

	/**
	 * Return whether or not any of the natures we are interested in changed.
	 * 
	 * @param natures
	 * @param cachedNatures
	 * @return boolean
	 */
	protected boolean naturesChanged(String[] natures, String[] cachedNatures) {
		Collection newNatures = new HashSet();
		Collection allNatures = getAllNatures();
		for (int i = 0; i < natures.length; i++) {
			if (allNatures.contains(natures[i]))// Only add what we are looking
												// for
				newNatures.add(natures[i]);
		}

		for (int i = 0; i < cachedNatures.length; i++) {
			String oldNature = cachedNatures[i];
			if (!allNatures.contains(oldNature))// Do we care?
				continue;
			if (newNatures.contains(oldNature))
				newNatures.remove(oldNature);
			else
				return true;// At least one was removed
		}

		// Are any left?
		return newNatures.size() > 0;
	}

	private Collection getAllNatures() {
		if (allNatures == null) {
			allNatures = new HashSet();
			Iterator iterator = definitionsToNatures.values().iterator();
			while (iterator.hasNext()) {
				String[] values = (String[]) iterator.next();
				for (int i = 0; i < values.length; i++) {
					allNatures.add(values[i]);
				}
			}
		}
		return allNatures;
	}

	/**
	 * Check for project file updates.
	 * 
	 * @param parentDelta
	 *            A delta on a project.
	 * @return boolean <code>true</code> if an update is required.
	 */
	private boolean checkProjectFileUpdate(IResourceDelta parentDelta) {
		IResourceDelta[] deltas = parentDelta
				.getAffectedChildren(IResourceDelta.CHANGED);
		for (int i = 0; i < deltas.length; i++) {
			IResourceDelta delta = deltas[i];
			if ((delta.getFlags() | IResourceDelta.CONTENT) > 0) {
				IResource resource = delta.getResource();
				if (resource.getType() == IResource.FILE) {
					if (resource.getName().equals(
							IProjectDescription.DESCRIPTION_FILE_NAME))
						return true;
				}
			}
		}
		return false;

	}

	/**
	 * Dispose the receiver.
	 */
	void dispose() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	/**
	 * Listen for description
	 * 
	 * @param definition
	 *            the definition being enabled or disabled.
	 * @param isEnabled
	 *            whether or not this is a notification of enablement or
	 *            disablement
	 * @param natureValues
	 *            The values we are listening for
	 */
	void listenFor(LightweightDecoratorDefinition definition,
			boolean isEnabled, String[] natureValues) {

		if (isEnabled) {
			if (definitionsToNatures == null)
				definitionsToNatures = new HashMap();
			definitionsToNatures.put(definition.getId(), natureValues);

		} else {
			if (definitionsToNatures != null) {
				definitionsToNatures.remove(definition.getId());
				if (definitionsToNatures.isEmpty())
					definitionsToNatures = null;// reduce heap if it is empty

			}
		}
		allNatures = null; // Clear all of the cached natures

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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9452.java