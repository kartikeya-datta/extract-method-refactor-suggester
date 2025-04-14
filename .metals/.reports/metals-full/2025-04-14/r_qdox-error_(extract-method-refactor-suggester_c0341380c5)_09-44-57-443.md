error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/720.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/720.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/720.java
text:
```scala
.@@getNamespace(), 0,

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

import java.util.StringTokenizer;

import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPluginContribution;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.part.NewViewToOldWrapper;
import org.eclipse.ui.internal.part.components.services.IPartDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.views.IViewDescriptor;

/**
 * Capture the attributes of a view extension.
 */
public class ViewDescriptor implements IViewDescriptor, IPluginContribution {
    private String id;

    private ImageDescriptor imageDescriptor;

    private IConfigurationElement configElement;

    private String[] categoryPath;

    private float fastViewWidthRatio;

    private IPartDescriptor viewInfo = new IPartDescriptor() {
		public String getId() {
			return id;
		}

		public String getLabel() {
			return ViewDescriptor.this.getLabel();
		}
        
        /* (non-Javadoc)
         * @see org.eclipse.ui.workbench.services.IPartDescriptor#getImage()
         */
        public ImageDescriptor getImage() {
            return getImageDescriptor();
        }
    };

	/**
	 * The activation token returned when activating the show view handler with
	 * the workbench.
	 */
	private IHandlerActivation handlerActivation;
    
    /**
     * Create a new <code>ViewDescriptor</code> for an extension.
     * 
     * @param e the configuration element
     * @throws CoreException thrown if there are errors in the configuration
     */
    public ViewDescriptor(IConfigurationElement e)
            throws CoreException {
        configElement = e;
        loadFromExtension();
    }

    /**
     * Return the part descriptor.
     * 
     * @return the part descriptor.
     * @since 3.1
     */
    public IPartDescriptor getPartDescriptor() {
    	return viewInfo;
    }
    

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.registry.IViewDescriptor#createView()
     */
    public IViewPart createView() throws CoreException {
        Object extension = WorkbenchPlugin.createExtension(
                getConfigurationElement(),
                IWorkbenchRegistryConstants.ATT_CLASS);
        
        if (extension instanceof IViewPart) {
            return (IViewPart) extension;
        }

        return new NewViewToOldWrapper(getPartDescriptor());
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.registry.IViewDescriptor#getCategoryPath()
     */
    public String[] getCategoryPath() {
        return categoryPath;
    }

    /**
     * Return the configuration element for this descriptor.
     * 
     * @return the configuration element
     */
    public IConfigurationElement getConfigurationElement() {
        return configElement;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.registry.IViewDescriptor#getDescription()
     */
    public String getDescription() {
    	return RegistryReader.getDescription(configElement);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPartDescriptor#getId()
     */
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPartDescriptor#getImageDescriptor()
     */
    public ImageDescriptor getImageDescriptor() {
        if (imageDescriptor != null)
            return imageDescriptor;
        String iconName = configElement.getAttribute(IWorkbenchRegistryConstants.ATT_ICON);
        if (iconName == null)
            return ImageDescriptor.getMissingImageDescriptor();
        IExtension extension = configElement.getDeclaringExtension();
        String extendingPluginId = extension.getNamespace();
        imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(
                extendingPluginId, iconName);
        if (imageDescriptor == null) {
            imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
        }
        
        return imageDescriptor;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPartDescriptor#getLabel()
     */
    public String getLabel() {
        return configElement.getAttribute(IWorkbenchRegistryConstants.ATT_NAME);
    }

    /**
     * Return the accelerator attribute.
     * 
     * @return the accelerator attribute
     */
    public String getAccelerator() {
        return configElement.getAttribute(IWorkbenchRegistryConstants.ATT_ACCELERATOR);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.registry.IViewDescriptor#getFastViewWidthRatio()
     */
    public float getFastViewWidthRatio() {
    	configElement.getAttribute(IWorkbenchRegistryConstants.ATT_RATIO); // check to ensure the element is still valid - exception thrown if it isn't
        return fastViewWidthRatio;
    }

    /**
     * load a view descriptor from the registry.
     */
    private void loadFromExtension() throws CoreException {    	
        id = configElement.getAttribute(IWorkbenchRegistryConstants.ATT_ID);
  
        String category = configElement.getAttribute(IWorkbenchRegistryConstants.TAG_CATEGORY);

        // Sanity check.
        if ((configElement.getAttribute(IWorkbenchRegistryConstants.ATT_NAME) == null)
 (RegistryReader.getClassValue(configElement,
                        IWorkbenchRegistryConstants.ATT_CLASS) == null)) {
            throw new CoreException(new Status(IStatus.ERROR, configElement
                    .getDeclaringExtension().getNamespace(), 0,
                    "Invalid extension (missing label or class name): " + id, //$NON-NLS-1$
                    null));
        }
        
        if (category != null) {
            StringTokenizer stok = new StringTokenizer(category, "/"); //$NON-NLS-1$
            categoryPath = new String[stok.countTokens()];
            // Parse the path tokens and store them
            for (int i = 0; stok.hasMoreTokens(); i++) {
                categoryPath[i] = stok.nextToken();
            }
        }
        
        String ratio = configElement.getAttribute(IWorkbenchRegistryConstants.ATT_RATIO);
        if (ratio != null) {
            try {
                fastViewWidthRatio = new Float(ratio).floatValue();
                if (fastViewWidthRatio > IPageLayout.RATIO_MAX)
                    fastViewWidthRatio = IPageLayout.RATIO_MAX;
                if (fastViewWidthRatio < IPageLayout.RATIO_MIN)
                    fastViewWidthRatio = IPageLayout.RATIO_MIN;
            } catch (NumberFormatException e) {
                fastViewWidthRatio = IPageLayout.DEFAULT_FASTVIEW_RATIO;
            }
        } else {
            fastViewWidthRatio = IPageLayout.DEFAULT_FASTVIEW_RATIO;
        }        
    }

    /**
     * Returns a string representation of this descriptor. For debugging
     * purposes only.
     */
    public String toString() {
        return "View(" + getId() + ")"; //$NON-NLS-2$//$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.activities.support.IPluginContribution#getPluginId()
     */
    public String getPluginId() {
    	String pluginId = configElement.getNamespace();
        return pluginId == null ? "" : pluginId; //$NON-NLS-1$    
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.activities.support.IPluginContribution#getLocalId()
     */
    public String getLocalId() {
        return getId() == null ? "" : getId(); //$NON-NLS-1$	
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.registry.IViewDescriptor#getAllowMultiple()
     */
    public boolean getAllowMultiple() {
    	String string = configElement.getAttribute(IWorkbenchRegistryConstants.ATT_MULTIPLE);    	
        return string == null ? false : Boolean.valueOf(string).booleanValue();
    }

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter.equals(IConfigurationElement.class)) {
			return getConfigurationElement();
		}
		return null;
	}

    /**
	 * Activates a show view handler for this descriptor. This handler can later
	 * be deactivated by calling {@link ViewDescriptor#deactivateHandler()}.
	 * This method will only activate the handler if it is not currently active.
	 * 
	 * @since 3.1
	 */
    public final void activateHandler() {
		if (handlerActivation == null) {
			final IHandler handler = new ShowViewHandler(getId());
			final IHandlerService handlerService = (IHandlerService) PlatformUI
					.getWorkbench().getAdapter(IHandlerService.class);
			handlerActivation = handlerService
					.activateHandler(getId(), handler);
		}
    }
	
	/**
	 * Deactivates the show view handler for this descriptor. This handler was
	 * previously activated by calling {@link ViewDescriptor#activateHandler()}.
	 * This method will only deactivative the handler if it is currently active.
	 * 
	 * @since 3.1
	 */
	public final void deactivateHandler() {
		if (handlerActivation != null) {
			final IHandlerService handlerService = (IHandlerService) PlatformUI
					.getWorkbench().getAdapter(IHandlerService.class);
			handlerService.deactivateHandler(handlerActivation);
			handlerActivation = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/720.java