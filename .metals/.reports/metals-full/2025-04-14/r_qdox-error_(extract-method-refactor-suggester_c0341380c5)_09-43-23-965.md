error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18029.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18029.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18029.java
text:
```scala
r@@eturn configElement != null ? configElement.getContributor().getName() : pluginId;

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ecf.internal.ui.wizards;

import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPluginContribution;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.AdaptableList;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.wizards.IWizardCategory;
import org.eclipse.ui.wizards.IWizardDescriptor;

/**
 * Instances of this class are a collection of WizardCollectionElements, thereby
 * facilitating the definition of tree structures composed of these elements.
 * Instances also store a list of wizards.
 */
public class WizardCollectionElement extends AdaptableList implements
		IPluginContribution, IWizardCategory {
	private String id;

	private String pluginId;

	private String name;

	private WizardCollectionElement parent;

	private AdaptableList wizards = new AdaptableList();

	private IConfigurationElement configElement;

	/**
	 * Creates a new <code>WizardCollectionElement</code>. Parent can be
	 * null.
	 * 
	 * @param id
	 *            the id
	 * @param pluginId
	 *            the plugin
	 * @param name
	 *            the name
	 * @param parent
	 *            the parent
	 */
	public WizardCollectionElement(String id, String pluginId, String name,
			WizardCollectionElement parent) {
		this.name = name;
		this.id = id;
		this.pluginId = pluginId;
		this.parent = parent;
	}

	/**
	 * Creates a new <code>WizardCollectionElement</code>. Parent can be
	 * null.
	 * 
	 * @param element
	 * @param parent
	 * @since 3.1
	 */
	public WizardCollectionElement(IConfigurationElement element,
			WizardCollectionElement parent) {
		configElement = element;
		id = configElement.getAttribute(IWizardRegistryConstants.ATT_ID);
		this.parent = parent;
	}

	/**
	 * Adds a wizard collection to this collection.
	 */
	public AdaptableList add(IAdaptable a) {
		if (a instanceof WorkbenchWizardElement) {
			wizards.add(a);
		} else {
			super.add(a);
		}
		return this;
	}

	/**
	 * Remove a wizard from this collection.
	 */
	public void remove(IAdaptable a) {
		if (a instanceof WorkbenchWizardElement) {
			wizards.remove(a);
		} else {
			super.remove(a);
		}
	}

	/**
	 * Returns the wizard collection child object corresponding to the passed
	 * path (relative to this object), or <code>null</code> if such an object
	 * could not be found.
	 * 
	 * @param searchPath
	 *            org.eclipse.core.runtime.IPath
	 * @return WizardCollectionElement
	 */
	public WizardCollectionElement findChildCollection(IPath searchPath) {
		Object[] children = getChildren(null);
		String searchString = searchPath.segment(0);
		for (int i = 0; i < children.length; ++i) {
			WizardCollectionElement currentCategory = (WizardCollectionElement) children[i];
			if (currentCategory.getId().equals(searchString)) {
				if (searchPath.segmentCount() == 1)
					return currentCategory;

				return currentCategory.findChildCollection(searchPath
						.removeFirstSegments(1));
			}
		}

		return null;
	}

	/**
	 * Returns the wizard category corresponding to the passed id, or
	 * <code>null</code> if such an object could not be found. This recurses
	 * through child categories.
	 * 
	 * @param id
	 *            the id for the child category
	 * @return the category, or <code>null</code> if not found
	 * @since 3.1
	 */
	public WizardCollectionElement findCategory(String id) {
		Object[] children = getChildren(null);
		for (int i = 0; i < children.length; ++i) {
			WizardCollectionElement currentCategory = (WizardCollectionElement) children[i];
			if (id.equals(currentCategory.getId()))
				return currentCategory;
			WizardCollectionElement childCategory = currentCategory
					.findCategory(id);
			if (childCategory != null)
				return childCategory;
		}
		return null;
	}

	/**
	 * Returns this collection's associated wizard object corresponding to the
	 * passed id, or <code>null</code> if such an object could not be found.
	 * 
	 * @param searchId
	 *            the id to search on
	 * @param recursive
	 *            whether to search recursivly
	 * @return the element
	 */
	public WorkbenchWizardElement findWizard(String searchId, boolean recursive) {
		Object[] wizards = getWizards();
		for (int i = 0; i < wizards.length; ++i) {
			WorkbenchWizardElement currentWizard = (WorkbenchWizardElement) wizards[i];
			if (currentWizard.getId().equals(searchId))
				return currentWizard;
		}
		if (!recursive)
			return null;
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			WizardCollectionElement child = (WizardCollectionElement) iterator
					.next();
			WorkbenchWizardElement result = child.findWizard(searchId, true);
			if (result != null)
				return result;
		}
		return null;
	}

	/**
	 * Returns an object which is an instance of the given class associated with
	 * this object. Returns <code>null</code> if no such object can be found.
	 */
	public Object getAdapter(Class adapter) {
		if (adapter == IWorkbenchAdapter.class)
			return this;
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	/**
	 * Returns the unique ID of this element.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the label for this collection.
	 */
	public String getLabel(Object o) {
		return configElement != null ? configElement
				.getAttribute(IWizardRegistryConstants.ATT_NAME) : name;
	}

	/**
	 * Returns the logical parent of the given object in its tree.
	 */
	public Object getParent(Object o) {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.wizards.IWizardCategory#getPath()
	 */
	public IPath getPath() {
		if (parent == null)
			return new Path(""); //$NON-NLS-1$

		return parent.getPath().append(getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.wizards.IWizardCategory#getWizards()
	 */
	public IWizardDescriptor[] getWizards() {
		return (IWizardDescriptor[]) wizards
				.getTypedChildren(IWizardDescriptor.class);
	}

	/**
	 * Return the wizards.
	 * 
	 * @return the wizards
	 * @since 3.1
	 */
	public WorkbenchWizardElement[] getWorkbenchWizardElements() {
		return (WorkbenchWizardElement[]) wizards
				.getTypedChildren(WorkbenchWizardElement.class);
	}

	/**
	 * Returns true if this element has no children and no wizards.
	 * 
	 * @return whether it is empty
	 */
	public boolean isEmpty() {
		return size() == 0 && wizards.size() == 0;
	}

	/**
	 * For debugging purposes.
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer("WizardCollection, "); //$NON-NLS-1$
		buf.append(children.size());
		buf.append(" children, "); //$NON-NLS-1$
		buf.append(wizards.size());
		buf.append(" wizards"); //$NON-NLS-1$
		return buf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getImageDescriptor(java.lang.Object)
	 */
	public ImageDescriptor getImageDescriptor(Object object) {
		return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
				ISharedImages.IMG_OBJ_FOLDER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.activities.support.IPluginContribution#getLocalId()
	 */
	public String getLocalId() {
		return getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.activities.support.IPluginContribution#getPluginId()
	 */
	public String getPluginId() {
		return configElement != null ? configElement.getNamespace() : pluginId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.wizards.IWizardCategory#getParent()
	 */
	public IWizardCategory getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.wizards.IWizardCategory#getCategories()
	 */
	public IWizardCategory[] getCategories() {
		return (IWizardCategory[]) getTypedChildren(IWizardCategory.class);
	}

	/**
	 * Return the collection elements.
	 * 
	 * @return the collection elements
	 * @since 3.1
	 */
	public WizardCollectionElement[] getCollectionElements() {
		return (WizardCollectionElement[]) getTypedChildren(WizardCollectionElement.class);
	}

	/**
	 * Return the raw adapted list of wizards.
	 * 
	 * @return the list of wizards
	 * @since 3.1
	 */
	public AdaptableList getWizardAdaptableList() {
		return wizards;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.wizards.IWizardCategory#getLabel()
	 */
	public String getLabel() {
		return getLabel(this);
	}

	/**
	 * Return the configuration element.
	 * 
	 * @return the configuration element
	 * @since 3.1
	 */
	public IConfigurationElement getConfigurationElement() {
		return configElement;
	}

	/**
	 * Return the parent collection element.
	 * 
	 * @return the parent
	 * @since 3.1
	 */
	public WizardCollectionElement getParentCollection() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.wizards.IWizardCategory#findWizard(java.lang.String)
	 */
	public IWizardDescriptor findWizard(String id) {
		return findWizard(id, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.wizards.IWizardCategory#findCategory(org.eclipse.core.runtime.IPath)
	 */
	public IWizardCategory findCategory(IPath path) {
		return findChildCollection(path);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18029.java