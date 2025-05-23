error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5759.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5759.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5759.java
text:
```scala
i@@mage = WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_ETOOL_DEF_PERSPECTIVE);

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
package org.eclipse.ui.internal.registry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPluginContribution;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.HandlerSubmission;
import org.eclipse.ui.commands.IHandler;
import org.eclipse.ui.commands.Priority;
import org.eclipse.ui.internal.IWorkbenchConstants;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * PerspectiveDescriptor.
 * <p>
 * A PerspectiveDesciptor has 3 states:
 * </p>
 * <ol>
 * <li>It <code>isPredefined()</code>, in which case it was defined from an
 * extension point.</li>
 * <li>It <code>isPredefined()</code> and <code>hasCustomFile</code>, in
 * which case the user has customized a predefined perspective.</li>
 * <li>It <code>hasCustomFile</code>, in which case the user created a new
 * perspective.</li>
 * </ol>
 * 
 * @author Brock Janiczak (brockj_eclipse@ihug.com.au) - handler registration
 */
public class PerspectiveDescriptor implements IPerspectiveDescriptor,
        IPluginContribution {
    private String id;

    private String pluginId;

    private String originalId;

    private String label;

    private String className;

    private String description;

    private boolean singleton;

    private boolean fixed;

    private ImageDescriptor image;

    static final String ATT_ID = "id";//$NON-NLS-1$

    private static final String ATT_DEFAULT = "default";//$NON-NLS-1$

    private static final String ATT_NAME = "name";//$NON-NLS-1$

    private static final String ATT_ICON = "icon";//$NON-NLS-1$

    private static final String ATT_CLASS = "class";//$NON-NLS-1$

    private static final String ATT_SINGLETON = "singleton";//$NON-NLS-1$

    private static final String ATT_FIXED = "fixed";//$NON-NLS-1$

	private IConfigurationElement configElement;

    /**
     * Create a new empty descriptor.
     * 
     * @param id the id of the new descriptor
     * @param label the label of the new descriptor
     * @param originalDescriptor the descriptor that this descriptor is based on
     */
    public PerspectiveDescriptor(String id, String label,
            PerspectiveDescriptor originalDescriptor) {
        this.id = id;
        this.label = label;
        if (originalDescriptor != null) {
            this.originalId = originalDescriptor.getOriginalId();
            this.image = originalDescriptor.image;

            // This perspective is based on a perspective in some bundle -- if that
            // bundle goes away then I think it makes sense to treat this perspective
            // the same as any other -- so store it with the original descriptor's
            // bundle's list.
            //
            // It might also make sense the other way...removing the following line
            // will allow the perspective to stay around when the originating bundle
            // is unloaded.
            //
            // This might also have an impact on upgrade cases -- should we really be
            // destroying all user customized perspectives when the older version is
            // removed?
            //
            // I'm leaving this here for now since its a good example, but wouldn't be
            // surprised if we ultimately decide on the opposite.
            //
            // The reason this line is important is that this is the value used to
            // put the object into the UI level registry.  When that bundle goes away,
            // the registry will remove the entire list of objects.  So if this desc
            // has been put into that list -- it will go away.
            this.pluginId = originalDescriptor.getPluginId();
        }
    }

    /**
     * Create a descriptor from a config element.
     * 
     * @param id the id of the element to create
     * @param configElement the element to base this perspective on
     * @throws CoreException thrown if there are any missing attributes
     */
    public PerspectiveDescriptor(String id, IConfigurationElement configElement) throws CoreException {
        this.configElement = configElement;
        this.id = id;
        // Sanity check.
        if ((getId() == null) || (getLabel() == null) || (getClassName() == null)) {
            throw new CoreException(new Status(IStatus.ERROR,
                    WorkbenchPlugin.PI_WORKBENCH, 0,
                    "Invalid extension (missing label, id or class name): " + getId(),//$NON-NLS-1$
                    null));
        }

        registerOpenPerspectiveHandler();
    }

    /**
     * Registers a handler for opening this perspective with the command
     * service. This method must be called exactly once per perspective
     * descriptor. This handler must be removed when this perspective descriptor
     * goes away.
     * 
     * @since 3.1
     */
    private void registerOpenPerspectiveHandler() {
        IHandler openPerspectiveHandler = new OpenPerspectiveHandler(getId());
        HandlerSubmission openPerspectiveSubmission = new HandlerSubmission(
                null, null, null, getId(), openPerspectiveHandler, Priority.MEDIUM);
        PlatformUI.getWorkbench().getCommandSupport().addHandlerSubmission(
                openPerspectiveSubmission);
    }
    
    /**
     * Creates a factory for a predefined perspective. If the perspective is not
     * predefined return <code>null</code>.
     * 
     * @return the IPerspectiveFactory or <code>null</code>
     * @throws CoreException if the object could not be instantiated.
     */
    public IPerspectiveFactory createFactory() throws CoreException {
        // if there is an originalId, then use that descriptor instead
        if (originalId != null) {
            // Get the original descriptor to create the factory. If the
            // original is gone then nothing can be done.
            IPerspectiveDescriptor target = ((PerspectiveRegistry) WorkbenchPlugin
                    .getDefault().getPerspectiveRegistry())
                    .findPerspectiveWithId(originalId);

            return target == null ? null : ((PerspectiveDescriptor) target)
                    .createFactory();
        }

        // otherwise try to create the executable extension
        if (configElement != null)
            try {
                return (IPerspectiveFactory) configElement
                            .createExecutableExtension(ATT_CLASS);
            } catch (CoreException e) {
                // do nothing
            }

        return null;
    }

    /**
     * Deletes the custom definition for a perspective..
     */
    public void deleteCustomDefinition() {
        ((PerspectiveRegistry) WorkbenchPlugin.getDefault()
                .getPerspectiveRegistry()).deleteCustomDefinition(this);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPerspectiveDescriptor#getDescription()
     */
    public String getDescription() {
        return configElement == null ? description : RegistryReader.getDescription(configElement);
    }

    /**
     * Returns whether or not this perspective is fixed.
     * 
     * @return whether or not this perspective is fixed
     */
    public boolean getFixed() {
        return configElement == null ? fixed : Boolean.valueOf(configElement.getAttribute(ATT_FIXED)).booleanValue();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPerspectiveDescriptor#getId()
     */
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPerspectiveDescriptor#getImageDescriptor()
     */
    public ImageDescriptor getImageDescriptor() {
    	if (image == null) {
            if (configElement != null) {
                String icon = configElement.getAttribute(ATT_ICON);
                if (icon != null) {
                    image = AbstractUIPlugin.imageDescriptorFromPlugin(
                            configElement.getNamespace(), icon);
                }
                if (image == null) {
                    image = WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_ETOOL_DEF_PERSPECTIVE_HOVER);
                }
            }
    	}
        return image;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPerspectiveDescriptor#getLabel()
     */
    public String getLabel() {
        return configElement == null ? label : configElement.getAttribute(ATT_NAME);
    }

    /**
     * Return the original id of this descriptor.
     * 
     * @return the original id of this descriptor
     */
    public String getOriginalId() {
        if (originalId == null)
            return getId();
        return originalId;
    }

    /**
     * Returns <code>true</code> if this perspective has a custom definition.
     * 
     * @return whether this perspective has a custom definition
     */
    public boolean hasCustomDefinition() {
        return ((PerspectiveRegistry) WorkbenchPlugin.getDefault()
                .getPerspectiveRegistry()).hasCustomDefinition(this);
    }

    /**
     * Returns <code>true</code> if this perspective wants to be default.
     * 
     * @return whether this perspective wants to be default
     */
    public boolean hasDefaultFlag() {
        if (configElement == null)
            return false;
        
        return Boolean.valueOf(configElement.getAttribute(ATT_DEFAULT)).booleanValue();
    }

    /**
     * Returns <code>true</code> if this perspective is predefined by an extension.
     * 
     * @return whether this perspective is predefined by an extension
     */
    public boolean isPredefined() {
        return (getClassName() != null);
    }

    /**
     * Returns <code>true</code> if this perspective is a singleton.
     * 
     * @return whether this perspective is a singleton
     */
    public boolean isSingleton() {
        return configElement == null ? singleton : configElement.getAttributeAsIs(ATT_SINGLETON) != null;
    }

    /**
     * Restore the state of a perspective from a memento.
     * 
     * @param memento the memento to restore from
     * @return the <code>IStatus</code> of the operation
     * @see org.eclipse.ui.IPersistableElement
     */
    public IStatus restoreState(IMemento memento) {
        IMemento childMem = memento
                .getChild(IWorkbenchConstants.TAG_DESCRIPTOR);
        if (childMem != null) {
            id = childMem.getString(IWorkbenchConstants.TAG_ID);
            originalId = childMem.getString(IWorkbenchConstants.TAG_DESCRIPTOR);
            label = childMem.getString(IWorkbenchConstants.TAG_LABEL);
            className = childMem.getString(IWorkbenchConstants.TAG_CLASS);
            singleton = (childMem.getInteger(IWorkbenchConstants.TAG_SINGLETON) != null);

            //Find a descriptor in the registry.
            IPerspectiveDescriptor descriptor = WorkbenchPlugin.getDefault()
					.getPerspectiveRegistry().findPerspectiveWithId(
							getOriginalId());

            if (descriptor != null)
                //Copy the state from the registred descriptor.	
                image = descriptor.getImageDescriptor();
        }
        return new Status(IStatus.OK, PlatformUI.PLUGIN_ID, 0, "", null); //$NON-NLS-1$
    }

    /**
     * Revert to the predefined extension template.
     * Does nothing if this descriptor is user defined.
     */
    public void revertToPredefined() {
        if (isPredefined())
            deleteCustomDefinition();
    }

    /**
     * Save the state of a perspective to a memento.
     * 
     * @param memento the memento to restore from
     * @return the <code>IStatus</code> of the operation
     * @see org.eclipse.ui.IPersistableElement
     */
    public IStatus saveState(IMemento memento) {
        IMemento childMem = memento
                .createChild(IWorkbenchConstants.TAG_DESCRIPTOR);
        childMem.putString(IWorkbenchConstants.TAG_ID, getId());
        if (originalId != null)
            childMem.putString(IWorkbenchConstants.TAG_DESCRIPTOR, originalId);
        childMem.putString(IWorkbenchConstants.TAG_LABEL, getLabel());
        childMem.putString(IWorkbenchConstants.TAG_CLASS, getClassName());
        if (singleton)
            childMem.putInteger(IWorkbenchConstants.TAG_SINGLETON, 1);
        return new Status(IStatus.OK, PlatformUI.PLUGIN_ID, 0, "", null); //$NON-NLS-1$
    }

    /**
     * Return the configuration element used to create this perspective, if one was used.
     * 
     * @return the configuration element used to create this perspective
     * @since 3.0
     */
    public IConfigurationElement getConfigElement() {
        return configElement;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.activities.support.IPluginContribution#getLocalId()
     */
    public String getLocalId() {
        return getId();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.activities.support.IPluginContribution#getPluginId()
     */
    public String getPluginId() {
        return configElement == null ? pluginId : configElement.getNamespace();
    }
    
    /**
     * Returns the factory class name for this descriptor.
     * 
     * @return the factory class name for this descriptor
     * @since 3.1
     */
    public String getClassName() {
    	return configElement == null ? className : configElement.getAttribute(ATT_CLASS);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5759.java