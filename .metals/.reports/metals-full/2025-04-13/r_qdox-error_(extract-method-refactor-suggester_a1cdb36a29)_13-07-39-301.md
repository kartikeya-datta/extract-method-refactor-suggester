error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7007.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7007.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7007.java
text:
```scala
m@@ng.addToDeep(getCategory(),node);

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.dialogs;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.SelectionEnabler;
import org.eclipse.ui.internal.LegacyResourceSupport;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.CategorizedPageRegistryReader;
import org.eclipse.ui.internal.registry.PropertyPagesRegistryReader;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * This property page contributor is created from page entry
 * in the registry. Since instances of this class are created
 * by the workbench, there is no danger of premature loading
 * of plugins.
 */

public class RegistryPageContributor implements IPropertyPageContributor, IAdaptable {
    private String pageId;
    /**
     * The list of subpages (immediate children) of this node (element type:
     * <code>RegistryPageContributor</code>).
     */
    private Collection subPages = new ArrayList();

    private boolean adaptable = false;

    private IConfigurationElement pageElement;

    private SoftReference filterProperties;

    /**
     * PropertyPageContributor constructor.
     */
    public RegistryPageContributor(String pageId, IConfigurationElement element) {
        this.pageId = pageId;
        this.pageElement = element;
        adaptable = Boolean.valueOf(pageElement.getAttribute(PropertyPagesRegistryReader.ATT_ADAPTABLE)).booleanValue();
    }

	/**
     * Implements the interface by creating property page specified with
     * the configuration element.
     */
    public boolean contributePropertyPages(PropertyPageManager mng,
            IAdaptable element) {
        PropertyPageNode node = new PropertyPageNode(this, element);
        
        if(getCategory() == null){
        	mng.addToRoot(node);
        	return true;
        }
        mng.addTo(getCategory(),node);
    
        return true;
    }

    /**
     * Creates the page based on the information in the configuration element.
     */
    public IWorkbenchPropertyPage createPage(IAdaptable element)
            throws CoreException {
        IWorkbenchPropertyPage ppage = null;
        ppage = (IWorkbenchPropertyPage) WorkbenchPlugin.createExtension(
                pageElement, PropertyPagesRegistryReader.ATT_CLASS);

        ppage.setTitle(getPageName());
        
        Object adapted = element;
        if(adaptable) {
        	adapted = LegacyResourceSupport.getAdapter(element, getObjectClass());
        	if(adapted == null) {
            	String message = "Error adapting selection to " + getObjectClass() +  //$NON-NLS-1$
            			". Property page " + pageId + " is being ignored"; //$NON-NLS-1$ //$NON-NLS-2$            	
            	throw new CoreException(new Status(IStatus.ERROR, WorkbenchPlugin.PI_WORKBENCH,
                        IStatus.ERROR,message, null));
           }
        }
        
        ppage.setElement((IAdaptable)adapted);

        return ppage;
    }

    /**
     * Returns page icon as defined in the registry.
     */
    public ImageDescriptor getPageIcon() {
    	String iconName = pageElement.getAttribute(PropertyPagesRegistryReader.ATT_ICON);
        if (iconName == null)
            return null;
        return AbstractUIPlugin.imageDescriptorFromPlugin(pageElement
                .getNamespace(), iconName);
    }

    /**
     * Returns page ID as defined in the registry.
     */

    public String getPageId() {
        return pageId;
    }

    /**
     * Returns plugin ID as defined in the registry.
     */

    public String getPluginId() {
        return pageElement.getNamespace();
    }

    /**
     * Returns page name as defined in the registry.
     */

    public String getPageName() {
        return pageElement.getAttribute(PropertyPagesRegistryReader.ATT_NAME);
    }

    /**
     * Return true if name filter is not defined in the registry for this page,
     * or if name of the selected object matches the name filter.
     */
    public boolean isApplicableTo(Object object) {
        // Test name filter
        String nameFilter = pageElement
                .getAttribute(PropertyPagesRegistryReader.ATT_NAME_FILTER);
        if (nameFilter != null) {
            String objectName = object.toString();
            if (object instanceof IAdaptable) {
                IWorkbenchAdapter adapter = (IWorkbenchAdapter) ((IAdaptable) object)
                        .getAdapter(IWorkbenchAdapter.class);
                if (adapter != null) {
                    String elementName = adapter.getLabel(object);
                    if (elementName != null) {
                        objectName = elementName;
                    }
                }
            }
            if (!SelectionEnabler.verifyNameMatch(objectName, nameFilter))
                return false;
        }

        // Test custom filter	
        if (getFilterProperties() == null)
            return true;
        IActionFilter filter = null;

        // Do the free IResource adapting
		Object adaptedObject = LegacyResourceSupport.getAdaptedResource(object);
		if(adaptedObject != null) {
			object = adaptedObject;
		}

        if (object instanceof IActionFilter) {
            filter = (IActionFilter) object;
        } else if (object instanceof IAdaptable) {
            filter = (IActionFilter) ((IAdaptable) object)
                    .getAdapter(IActionFilter.class);
        }

        if (filter != null) 
            return testCustom(object, filter);

        return true;
    }

    /**
     * Returns whether the object passes a custom key value filter
     * implemented by a matcher.
     */
    private boolean testCustom(Object object, IActionFilter filter) {
        Map filterProperties = getFilterProperties();
		
        if (filterProperties == null)
            return false;
        Iterator iter = filterProperties.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            String value = (String) filterProperties.get(key);
            if (!filter.testAttribute(object, key, value))
                return false;
        }
        return true;
    }

    /*
     * @see IObjectContributor#canAdapt()
     */
    public boolean canAdapt() {
        return adaptable;
    }
    
	public String getObjectClass() {
		return pageElement.getAttribute(PropertyPagesRegistryReader.ATT_OBJECTCLASS);
	}
	
    /**
	 * Get the id of the category.
	 * @return String
	 * @since 3.1
	 */
	public String getCategory() {
		return pageElement.getAttribute(CategorizedPageRegistryReader.ATT_CATEGORY);
	}
	/**
	 * Return the children of the receiver.
	 * @return Collection
	 */
	public Collection getSubPages() {
		return subPages;
	}
	
	
	/**
	 * Add child to the list of children.
	 * @param child
	 */
	public void addSubPage(RegistryPageContributor child){
		subPages.add(child);
	}

    private Map getFilterProperties () {
    	if (filterProperties == null || filterProperties.get() == null) {
    		Map map = new HashMap();
    		filterProperties = new SoftReference(map);
	        IConfigurationElement[] children = pageElement.getChildren();
	        for (int i = 0; i < children.length; i++) {
	            processChildElement(map, children[i]);
	        }
    	}
	    return (Map) filterProperties.get();
    }
    
	/**
	 * Get the child with the given id.
	 * @param id
	 * @return RegistryPageContributor
	 */
	public Object getChild(String id) {
		Iterator iterator = subPages.iterator();
		while(iterator.hasNext()){
			RegistryPageContributor next = (RegistryPageContributor) iterator.next();
			if(next.getPageId().equals(id))
				return next;
		}
		return null;
	}
	
    /**
     * Parses child element and processes it 
     * @since 3.1
     */
    private void processChildElement(Map map, IConfigurationElement element) {
        String tag = element.getName();
        if (tag.equals(PropertyPagesRegistryReader.TAG_FILTER)) {
            String key = element.getAttribute(PropertyPagesRegistryReader.ATT_FILTER_NAME);
            String value = element.getAttribute(PropertyPagesRegistryReader.ATT_FILTER_VALUE);
            if (key == null || value == null)
                return;
            map.put(key, value);
        }
    }
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 * @since 3.1
	 */
	public Object getAdapter(Class adapter) {
		if (adapter.equals(IConfigurationElement.class)) {
			return getConfigurationElement();
		}
		return null;
	}

	/**
	 * @return the configuration element
	 * @since 3.1
	 */
	private IConfigurationElement getConfigurationElement() {
		return pageElement;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7007.java