error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5963.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5963.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,3]

error in qdox parser
file content:
```java
offset: 3
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5963.java
text:
```scala
: c@@onfigurationElement.getAttribute(IWorkbenchRegistryConstants.ATT_PARENT_CATEGORY);

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
package org.eclipse.ui.internal.registry;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPluginContribution;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * Category provides for hierarchical grouping of elements
 * registered in the registry. One extension normally defines
 * a category, and other reference it via its ID.
 * <p>
 * A category may specify its parent category in order to
 * achieve hierarchy.
 * </p>
 */
public class Category implements IWorkbenchAdapter, IPluginContribution, IAdaptable {
    /**
     * Name of the miscellaneous category
     */
    public final static String MISC_NAME = WorkbenchMessages.ICategory_other;

    /**
     * Identifier of the miscellaneous category
     */
    public final static String MISC_ID = "org.eclipse.ui.internal.otherCategory"; //$NON-NLS-1$

    private String id;

    private String name;

    private String[] parentPath;

    private ArrayList elements;

    private IConfigurationElement configurationElement;

	private String pluginId;

    /**
     * Creates an instance of <code>Category</code> as a
     * miscellaneous category.
     */
    public Category() {
        this.id = MISC_ID;
        this.name = MISC_NAME;
        this.pluginId = MISC_ID; // TODO: remove hack for bug 55172
    }

    /**
     * Creates an instance of <code>Category</code> with
     * an ID and label.
     * 
     * @param id the unique identifier for the category
     * @param label the presentation label for this category
     */
    public Category(String id, String label) {
        this.id = id;
        this.name = label;
    }

    /**
     * Creates an instance of <code>Category</code> using the
     * information from the specified configuration element.
     * 
     * @param configElement the <code>IConfigurationElement<code> containing
     * 		the ID, label, and optional parent category path.
     * @throws WorkbenchException if the ID or label is <code>null</code
     */
    public Category(IConfigurationElement configElement)
            throws WorkbenchException {
        id = configElement.getAttribute(IWorkbenchRegistryConstants.ATT_ID);

        configurationElement = configElement;
        if (id == null || getLabel() == null)
            throw new WorkbenchException("Invalid category: " + id); //$NON-NLS-1$
    }


    /**
     * Add an element to this category.
     * 
     * @param element the element to add
     */
    public void addElement(Object element) {
        if (elements == null)
            elements = new ArrayList(5);
        elements.add(element);
    }

    /* (non-Javadoc)
     * Method declared on IAdaptable.
     */
    public Object getAdapter(Class adapter) {
        if (adapter == IWorkbenchAdapter.class)
            return this;
        else if (adapter == IConfigurationElement.class)
            return configurationElement;
        else
            return null;
    }

    /* (non-Javadoc)
     * Method declared on IWorkbenchAdapter.
     */
    public Object[] getChildren(Object o) {
        return getElements().toArray();
    }

    /* (non-Javadoc)
     * Method declared on IWorkbenchAdapter.
     */
    public ImageDescriptor getImageDescriptor(Object object) {
        return WorkbenchImages.getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER);
    }

    /* (non-Javadoc)
     * Method declared on IWorkbenchAdapter.
     */
    public String getLabel(Object o) {
        return getLabel();
    }

    /**
     * Return the id for this category.
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Return the label for this category.
     * 
     * @return the label
     */
    public String getLabel() {
        return configurationElement == null ? name : configurationElement
				.getAttribute(IWorkbenchRegistryConstants.ATT_NAME);
    }

    /**
     * Return the parent path for this category.
     * 
     * @return the parent path
     */
    public String[] getParentPath() {
    	if (parentPath != null)
    		return parentPath;
    	
    	String unparsedPath = getRawParentPath();
        if (unparsedPath != null) {
            StringTokenizer stok = new StringTokenizer(unparsedPath, "/"); //$NON-NLS-1$
            parentPath = new String[stok.countTokens()];
            for (int i = 0; stok.hasMoreTokens(); i++) {
                parentPath[i] = stok.nextToken();
            }
        }

        return parentPath;
    }
    
    /**
     * Return the unparsed parent path.  May be <code>null</code>.
     * 
     * @return the unparsed parent path or <code>null</code>
     */
    public String getRawParentPath() {
        return configurationElement == null ? null
                : configurationElement.getAttribute(IWorkbenchRegistryConstants.ATT_PARENT);
    }

    /**
     * Return the root path for this category.
     * 
     * @return the root path
     */
    public String getRootPath() {
        String[] path = getParentPath();
        if (path != null && path.length > 0)
            return path[0];
        
        return id;
    }

    /**
     * Return the elements contained in this category.
     * 
     * @return the elements
     */
    public ArrayList getElements() {
        return elements;
    }

    /**
     * Return whether a given object exists in this category.
     * 
     * @param o the object to search for
     * @return whether the object is in this category
     */
    public boolean hasElement(Object o) {
        if (elements == null)
            return false;
        if (elements.isEmpty())
            return false;
        return elements.contains(o);
    }

    /**
     * Return whether this category has child elements.
     * 
     * @return whether this category has child elements
     */
    public boolean hasElements() {
        if (elements != null)
            return !elements.isEmpty();
        
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.model.IWorkbenchAdapter#getParent(java.lang.Object)
     */
    public Object getParent(Object o) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.activities.support.IPluginContribution#getLocalId()
     */
    public String getLocalId() {
        return id;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.activities.support.IPluginContribution#getPluginId()
     */
    public String getPluginId() {
        return configurationElement == null ? pluginId : configurationElement
				.getNamespace();
    }

	/**
	 * Clear all elements from this category.
	 * 
	 * @since 3.1
	 */
	public void clear() {
		if (elements != null) {
			elements.clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5963.java