error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1385.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1385.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1385.java
text:
```scala
.@@getNamespace() : null;

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
package org.eclipse.ui.internal.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IPluginContribution;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.SelectionEnabler;
import org.eclipse.ui.internal.LegacyResourceSupport;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.internal.registry.RegistryReader;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.model.IWorkbenchAdapter2;
import org.eclipse.ui.model.WorkbenchAdapter;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.wizards.IWizardCategory;
import org.eclipse.ui.wizards.IWizardDescriptor;

/**
 * Instances represent registered wizards.
 */
public class WorkbenchWizardElement extends WorkbenchAdapter implements
        IAdaptable, IPluginContribution, IWizardDescriptor {
    private String id;
    
    private ImageDescriptor imageDescriptor;

    private SelectionEnabler selectionEnabler;

    private IConfigurationElement configurationElement;

    private ImageDescriptor descriptionImage;
    
    private WizardCollectionElement parentCategory;
    
	/**
	 * TODO: DO we need to  make this API?
	 */
	public static final String TAG_PROJECT = "project"; //$NON-NLS-1$

	private static final String [] EMPTY_TAGS = new String[0];

	private static final String [] PROJECT_TAGS = new String[] {TAG_PROJECT};

    
    /**
     * Create a new instance of this class
     * 
     * @param configurationElement
     * @since 3.1
     */
    public WorkbenchWizardElement(IConfigurationElement configurationElement) {
        this.configurationElement = configurationElement;
        id = configurationElement.getAttribute(IWorkbenchRegistryConstants.ATT_ID);
    }

    /**
     * Answer a boolean indicating whether the receiver is able to handle the
     * passed selection
     * 
     * @return boolean
     * @param selection
     *            IStructuredSelection
     */
    public boolean canHandleSelection(IStructuredSelection selection) {
        return getSelectionEnabler().isEnabledForSelection(selection);
    }

    /**
     * Answer the selection for the reciever based on whether the it can handle
     * the selection. If it can return the selection. If it can handle the
     * adapted to IResource value of the selection. If it satisfies neither of
     * these conditions return an empty IStructuredSelection.
     * 
     * @return IStructuredSelection
     * @param selection
     *            IStructuredSelection
     */
    public IStructuredSelection adaptedSelection(IStructuredSelection selection) {
        if (canHandleSelection(selection))
            return selection;

        IStructuredSelection adaptedSelection = convertToResources(selection);
        if (canHandleSelection(adaptedSelection))
            return adaptedSelection;

        //Couldn't find one that works so just return
        return StructuredSelection.EMPTY;
    }

    /**
     * Create an the instance of the object described by the configuration
     * element. That is, create the instance of the class the isv supplied in
     * the extension point.
     * @return the new object
     * @throws CoreException 
     */
    public Object createExecutableExtension() throws CoreException {
        return WorkbenchPlugin.createExtension(configurationElement,
                IWorkbenchRegistryConstants.ATT_CLASS);
    }

    /**
     * Returns an object which is an instance of the given class associated
     * with this object. Returns <code>null</code> if no such object can be
     * found.
     */
    public Object getAdapter(Class adapter) {
        if (adapter == IWorkbenchAdapter.class
 adapter == IWorkbenchAdapter2.class) {
            return this;
        }
        else if (adapter == IPluginContribution.class) {
        	return this;
        }
        else if (adapter == IConfigurationElement.class) {
        	return configurationElement;
        }
        return Platform.getAdapterManager().getAdapter(this, adapter);
    }

    /**
     * @return IConfigurationElement
     */
    public IConfigurationElement getConfigurationElement() {
        return configurationElement;
    }

    /**
     * Answer the description parameter of this element
     * 
     * @return java.lang.String
     */
    public String getDescription() {
        return RegistryReader.getDescription(configurationElement);
    }

    /**
     * Answer the icon of this element.
     */
    public ImageDescriptor getImageDescriptor() {
    	if (imageDescriptor == null) {
    		String iconName = configurationElement
                    .getAttribute(IWorkbenchRegistryConstants.ATT_ICON);
	        if (iconName == null) 
	        	return null;
            imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(
                    configurationElement.getNamespace(), iconName);    
    	}
        return imageDescriptor;
    }

    /**
     * Returns the name of this wizard element.
     */
    public ImageDescriptor getImageDescriptor(Object element) {
        return getImageDescriptor();
    }
    
    /**
     * Returns the name of this wizard element.
     */
    public String getLabel(Object element) {
        return configurationElement.getAttribute(IWorkbenchRegistryConstants.ATT_NAME);
    }

    /**
     * Answer self's action enabler, creating it first iff necessary
     */
    protected SelectionEnabler getSelectionEnabler() {
        if (selectionEnabler == null)
            selectionEnabler = new SelectionEnabler(configurationElement);

        return selectionEnabler;
    }

    /**
     * Attempt to convert the elements in the passed selection into resources
     * by asking each for its IResource property (iff it isn't already a
     * resource). If all elements in the initial selection can be converted to
     * resources then answer a new selection containing these resources;
     * otherwise answer an empty selection.
     * 
     * @param originalSelection the original selection
     * @return the converted selection or an empty selection
     */
    private IStructuredSelection convertToResources(
            IStructuredSelection originalSelection) {
        //	@issue resource-specific code should be pushed into IDE
        Class resourceClass = LegacyResourceSupport.getResourceClass();
        if (resourceClass == null) {
            return originalSelection;
        }

        List result = new ArrayList();
        Iterator elements = originalSelection.iterator();

        while (elements.hasNext()) {
            Object currentElement = elements.next();
            if (resourceClass.isInstance(currentElement)) { // already a resource
                result.add(currentElement);
            } else if (!(currentElement instanceof IAdaptable)) { // cannot be converted to resource
                return StructuredSelection.EMPTY; // so fail
            } else {
                Object adapter = ((IAdaptable) currentElement)
                        .getAdapter(resourceClass);
                if (!(resourceClass.isInstance(adapter))) // chose not to be converted to resource
                    return StructuredSelection.EMPTY; // so fail
                result.add(adapter); // add the converted resource
            }
        }

        // all converted fine, answer new selection
        return new StructuredSelection(result.toArray());
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPluginContribution#getLocalId()
     */
    public String getLocalId() {
        return getId();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPluginContribution#getPluginId()
     */
    public String getPluginId() {
        return (configurationElement != null) ? configurationElement
                .getDeclaringExtension().getNamespace() : null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.wizards.INewWizardDescriptor#getDescriptionImage()
     */
    public ImageDescriptor getDescriptionImage() {
    	if (descriptionImage == null) {
    		String descImage = configurationElement.getAttribute(IWorkbenchRegistryConstants.ATT_DESCRIPTION_IMAGE);
    		if (descImage == null)
    			return null;
            descriptionImage = AbstractUIPlugin.imageDescriptorFromPlugin(
                    configurationElement.getNamespace(), descImage);
    	}
        return descriptionImage;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.wizards.INewWizardDescriptor#getHelpHref()
     */
    public String getHelpHref() {
        return configurationElement.getAttribute(IWorkbenchRegistryConstants.ATT_HELP_HREF);
    }
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.wizards.INewWizardDescriptor#createWizard()
	 */
	public IWorkbenchWizard createWizard() throws CoreException {
		return (IWorkbenchWizard) createExecutableExtension();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPartDescriptor#getId()
	 */
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPartDescriptor#getLabel()
	 */
	public String getLabel() {		
		return getLabel(this);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.wizards.INewWizardDescriptor#getCategory()
	 */
	public IWizardCategory getCategory() {
		return (IWizardCategory) getParent(this);
	}
	
	/**
	 * Return the collection.
	 * 
	 * @return the collection
	 * @since 3.1
	 */
	public WizardCollectionElement getCollectionElement() {
		return (WizardCollectionElement) getParent(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.wizards.IWizardDescriptor#getTags()
	 */
	public String [] getTags() {
 
        String flag = configurationElement.getAttribute(IWorkbenchRegistryConstants.ATT_PROJECT);
        if (Boolean.valueOf(flag).booleanValue()) {
        	return PROJECT_TAGS;
        }
        
        return EMPTY_TAGS;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getParent(java.lang.Object)
	 */
	public Object getParent(Object object) {
		return parentCategory;
	}

	/**
	 * Set the parent category.
	 * 
	 * @param parent the parent category
	 * @since 3.1
	 */
	public void setParent(WizardCollectionElement parent) {
		parentCategory = parent;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1385.java