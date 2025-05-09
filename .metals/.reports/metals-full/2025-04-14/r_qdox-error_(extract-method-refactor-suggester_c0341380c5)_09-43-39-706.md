error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9640.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9640.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9640.java
text:
```scala
a@@dd(element, desc.getConfigurationElement().getDeclaringExtension().getNamespace());

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
package org.eclipse.ui.internal.registry;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.internal.IWorkbenchConstants;
import org.eclipse.ui.internal.WorkbenchPlugin;

/**
 * The central manager for view descriptors.
 */
public class ViewRegistry extends RegistryManager implements IViewRegistry, IRegistryChangeListener {
	private List views;
	private boolean dirtyViews;
	private List categories;
	private boolean dirtyCategories;
	private Category miscCategory;
	protected static final String TAG_DESCRIPTION = "description";	//$NON-NLS-1$

	private class ViewRegistryElement {
		private List viewDescriptors;
		private List categoryDescriptors;
		
		public ViewRegistryElement() {
			viewDescriptors = new ArrayList();
			categoryDescriptors = new ArrayList();
		}

		public void addCategory(Category element) {
			categoryDescriptors.add(element);
		}
		
		public void addViewDescriptor(IViewDescriptor element) {
			viewDescriptors.add(element);
		}
		
		public List getCategories() {
			return categoryDescriptors;
		}
		
		public List getViewDescriptors() {
			return viewDescriptors;
		}
}
/**
 * Create a new ViewRegistry.
 */
public ViewRegistry() {
	super(WorkbenchPlugin.PI_WORKBENCH, IWorkbenchConstants.PL_VIEWS);
	views = new ArrayList();
	dirtyViews = true;
	categories = new ArrayList();
	dirtyCategories = true;
	InternalPlatform.getDefault().getRegistry().addRegistryChangeListener(this);
}
/**
 * Add a category to the registry.
 */
public void add(Category desc) {
	/* fix for 1877 */
	if (findCategory(desc.getId()) == null) {
		// Mark categories list as dirty
		dirtyCategories = true;
		ViewRegistryElement element = new ViewRegistryElement();
		element.addCategory(desc);
		add(element, desc.getPluginId());
	}
}
/**
 * Add a descriptor to the registry.
 */
public void add(IViewDescriptor desc) {
	dirtyViews = true;
	ViewRegistryElement element = new ViewRegistryElement();
	element.addViewDescriptor(desc);
	add(element, desc.getConfigurationElement().getDeclaringExtension().getDeclaringPluginDescriptor().getUniqueIdentifier());
}
/* (non-Javadoc)
 * @see org.eclipse.ui.internal.registry.aaRegistryCacheaa#buildNewCacheObject(org.eclipse.core.runtime.IExtensionDelta)
 */
public Object buildNewCacheObject(IExtensionDelta delta) {
	IExtension extension = delta.getExtension();
	if (extension == null)
		return null;
	IConfigurationElement[] elements = extension.getConfigurationElements();
	ViewRegistryElement regElement = new ViewRegistryElement();
	for (int i = 0; i < elements.length; i++) {
		IConfigurationElement singleton = elements[i];
		String id = singleton.getAttribute(IWorkbenchConstants.TAG_ID);
		if (singleton.getName().equals(IWorkbenchConstants.TAG_VIEW)) {
			// We want to create a view descriptor
			if (find(id) != null)
				// This view already exists.  Ignore this new one.
				continue;
			try {
				String descText = ""; //$NON-NLS-1$
				IConfigurationElement[] children = singleton.getChildren(TAG_DESCRIPTION);
				if (children.length >= 1) {
					descText = children[0].getValue();
				}
				ViewDescriptor desc = new ViewDescriptor(singleton, descText);
				regElement.addViewDescriptor(desc);
				dirtyViews = true;
			} catch (CoreException e) {
				// log an error since its not safe to open a dialog here
				WorkbenchPlugin.log("Unable to create view descriptor." , e.getStatus());//$NON-NLS-1$
			}
		} else if (singleton.getName().equals("category")) { //$NON-NLS-1$
			try {
				// We want to create a category
				if (findCategory(id) != null)
					// This category already exists.  Ignore this new one.
					continue;
				Category category = new Category(singleton);
				regElement.addCategory(category);
				dirtyCategories = true;
			} catch (WorkbenchException e) {
				WorkbenchPlugin.log("Unable to create view category.", e.getStatus());//$NON-NLS-1$
			}
		}
	}
	List categories = regElement.getCategories();
	List views = regElement.getViewDescriptors();
	if ((categories == null || categories.size() == 0) && 
			(views == null || views.size() == 0)) {
		return null;
	}
	return regElement;
}
/**
 * Find a descriptor in the registry.
 */
public IViewDescriptor find(String id) {
	buildViews();
	Iterator enum = views.iterator();
	while (enum.hasNext()) {
		IViewDescriptor desc = (IViewDescriptor) enum.next();
		if (id.equals(desc.getID())) {
			return desc;
		}
	}
	return null;
}
/**
 * Find a category with a given name.
 */
public Category findCategory(String id) {
	buildCategories();
	Iterator enum = categories.iterator();
	while (enum.hasNext()) {
		Category cat = (Category) enum.next();
		if (id.equals(cat.getRootPath())) {
			return cat;
		}
	}
	return null;
}
/**
 * Get the list of view categories.
 */
public Category [] getCategories() {
	buildCategories();
	int nSize = categories.size();
	Category [] retArray = new Category[nSize];
	categories.toArray(retArray);
	return retArray;
}
/**
 * Return the view category count.
 */
public int getCategoryCount() {
	buildCategories();
	return categories.size();
}
/**
 * Returns the Misc category.
 * This may be null if there are no miscellaneous views.
 */
public Category getMiscCategory() {
	return miscCategory;
}
/**
 * Return the view count.
 */
public int getViewCount() {
	buildViews();
	return views.size();
}
/**
 * Get an enumeration of view descriptors.
 */
public IViewDescriptor [] getViews() {
	buildViews();
	int nSize = views.size();
	IViewDescriptor [] retArray = new IViewDescriptor[nSize];
	views.toArray(retArray);
	return retArray;
}
/**
 * Adds each view in the registry to a particular category.
 * The view category may be defined in xml.  If not, the view is
 * added to the "misc" category.
 */
public void mapViewsToCategories() {
	buildCategories();
	buildViews();
	Iterator enum = views.iterator();
	while (enum.hasNext()) {
		IViewDescriptor desc = (IViewDescriptor) enum.next();
		Category cat = null;
		String [] catPath = desc.getCategoryPath();
		if (catPath != null) {
			String rootCat = catPath[0];
			cat = (Category)findCategory(rootCat);
		}	
		if (cat != null) {
			if (!cat.hasElement(desc)) {
				cat.addElement(desc);
			}
		} else {
			if (miscCategory == null) {
				miscCategory = new Category();
				add(miscCategory);
				buildCategories();
			}
			if (catPath != null) {
				// If we get here, this view specified a category which
				// does not exist. Add this view to the 'Other' category
				// but give out a message (to the log only) indicating 
				// this has been done.
				String fmt = "Category {0} not found for view {1}.  This view added to ''{2}'' category."; //$NON-NLS-1$
				WorkbenchPlugin.log(MessageFormat.format(fmt, new Object[]{catPath[0], desc.getID(), miscCategory.getLabel()}));  //$NON-NLS-1$
			}
			miscCategory.addElement(desc);
		}
	}
}

private void buildViews() {
	if (dirtyViews) {
		// Build up the view arraylist
		views = new ArrayList();
		Object[] regElements = getRegistryObjects();
		if (regElements == null) {
			dirtyViews = false;
			return;
		}
		for (int i = 0; i < regElements.length; i++) {
			ViewRegistryElement element = (ViewRegistryElement)regElements[i];
			List viewDescs = element.getViewDescriptors();
			if (viewDescs != null && viewDescs.size() != 0) {
				Iterator iter = viewDescs.iterator();
				while (iter.hasNext()) {
					IViewDescriptor view = (IViewDescriptor)iter.next();
					views.add(view);
				}
			}
		}
		dirtyViews = false;
	}
}

private void buildCategories() {
	if (dirtyCategories) {
		// Build up the categories arraylist
		categories = new ArrayList();
		Object[] regElements = getRegistryObjects();
		if (regElements == null) {
			dirtyCategories = false;
			return;
		}
		for (int i = 0; i < regElements.length; i++) {
			ViewRegistryElement element = (ViewRegistryElement)regElements[i];
			List tempCategories = element.getCategories();
			if (tempCategories != null && tempCategories.size() != 0) {
				Iterator iter = tempCategories.iterator();
				while (iter.hasNext()) {
					Category category = (Category)iter.next();
					categories.add(category);
				}
			}
		}
		dirtyCategories = false;
	}
}
public void postChangeProcessing() {
	mapViewsToCategories();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9640.java