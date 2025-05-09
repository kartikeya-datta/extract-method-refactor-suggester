error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2099.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2099.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2099.java
text:
```scala
c@@onfigElement.getNamespace(), strIcon);

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
package org.eclipse.ui.part;

import java.text.MessageFormat;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPart2;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.misc.Assert;
import org.eclipse.ui.internal.util.Util;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Abstract base implementation of all workbench parts.
 * <p>
 * This class is not intended to be subclassed by clients outside this
 * package; clients should instead subclass <code>ViewPart</code> or
 * <code>EditorPart</code>.
 * </p>
 * 
 * @see org.eclipse.ui.part.ViewPart
 * @see org.eclipse.ui.part.EditorPart
 */
public abstract class WorkbenchPart implements IWorkbenchPart2,
        IExecutableExtension, IWorkbenchPartOrientation {
    private String title = ""; //$NON-NLS-1$

    private ImageDescriptor imageDescriptor;

    private Image titleImage;

    private String toolTip = ""; //$NON-NLS-1$

    private IConfigurationElement configElement;

    private IWorkbenchPartSite partSite;

    private ListenerList propChangeListeners = new ListenerList(2);

    private String partName = ""; //$NON-NLS-1$

    private String contentDescription = ""; //$NON-NLS-1$

    /**
     * Creates a new workbench part.
     */
    protected WorkbenchPart() {
        super();
    }

    /* (non-Javadoc)
     * Method declared on IWorkbenchPart.
     */
    public void addPropertyListener(IPropertyListener l) {
        propChangeListeners.add(l);
    }

    /* (non-Javadoc)
     * Creates the SWT controls for this workbench part.
     * <p>
     * Subclasses must implement this method.  For a detailed description of the
     * requirements see <code>IWorkbenchPart</code>
     * </p>
     *
     * @param parent the parent control
     * @see IWorkbenchPart
     */
    public abstract void createPartControl(Composite parent);

    /**
     * The <code>WorkbenchPart</code> implementation of this 
     * <code>IWorkbenchPart</code> method disposes the title image
     * loaded by <code>setInitializationData</code>. Subclasses may extend.
     */
    public void dispose() {
        if (imageDescriptor != null) {
            JFaceResources.getResources().destroyImage(imageDescriptor);
        }

        // Clear out the property change listeners as we
        // should not be notifying anyone after the part
        // has been disposed.
        if (!propChangeListeners.isEmpty()) {
            propChangeListeners = new ListenerList(1);
        }
    }

    /**
     * Fires a property changed event.
     *
     * @param propertyId the id of the property that changed
     */
    protected void firePropertyChange(final int propertyId) {
        Object[] array = propChangeListeners.getListeners();
        for (int nX = 0; nX < array.length; nX++) {
            final IPropertyListener l = (IPropertyListener) array[nX];
            Platform.run(new SafeRunnable() {
                public void run() {
                    l.propertyChanged(WorkbenchPart.this, propertyId);
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     * 
     * Subclasses may override this method (however, if they do so, they
     * should invoke the method on their superclass to ensure that the
     * Platform's adapter manager is consulted).
     */
    public Object getAdapter(Class adapter) {

        /**
         * This implementation of the method declared by <code>IAdaptable</code>
         * passes the request along to the platform's adapter manager; roughly
         * <code>Platform.getAdapterManager().getAdapter(this, adapter)</code>.
         */

        return Platform.getAdapterManager().getAdapter(this, adapter);
    }

    /**
     * Returns the configuration element for this part. The configuration element
     * comes from the plug-in registry entry for the extension defining this part.
     *
     * @return the configuration element for this part
     */
    protected IConfigurationElement getConfigurationElement() {
        return configElement;
    }

    /**
     * Returns the default title image.
     *
     * @return the default image
     */
    protected Image getDefaultImage() {
        return PlatformUI.getWorkbench().getSharedImages().getImage(
                ISharedImages.IMG_DEF_VIEW);
    }

    /* (non-Javadoc)
     * Method declared on IWorkbenchPart.
     */
    public IWorkbenchPartSite getSite() {
        return partSite;
    }

    /**
     * {@inheritDoc}
     * <p>
     * It is considered bad practise to overload or extend this method.
     * Parts should set their title by calling setPartName and/or setContentDescription.
     * </p>
     */
    public String getTitle() {
        return title;
    }

    /* (non-Javadoc)
     * Method declared on IWorkbenchPart.
     */
    public Image getTitleImage() {
        if (titleImage != null) {
            return titleImage;
        }
        return getDefaultImage();
    }

    /* (non-Javadoc)
     * Gets the title tool tip text of this part.
     *
     * @return the tool tip text
     */
    public String getTitleToolTip() {
        return toolTip;
    }

    /* (non-Javadoc)
     * Method declared on IWorkbenchPart.
     */
    public void removePropertyListener(IPropertyListener l) {
        propChangeListeners.remove(l);
    }

    /* (non-Javadoc)
     * Asks this part to take focus within the workbench.
     * <p>
     * Subclasses must implement this method.  For a detailed description of the
     * requirements see <code>IWorkbenchPart</code>
     * </p>
     *
     * @see IWorkbenchPart
     */
    public abstract void setFocus();

    /**
     * {@inheritDoc}
     * The <code>WorkbenchPart</code> implementation of this
     * <code>IExecutableExtension</code> records the configuration element in
     * and internal state variable (accessible via <code>getConfigElement</code>).
     * It also loads the title image, if one is specified in the configuration element.
     * Subclasses may extend.
     * 
     * Should not be called by clients. It is called by the core plugin when creating
     * this executable extension.
     */
    public void setInitializationData(IConfigurationElement cfig,
            String propertyName, Object data) {

        // Save config element.
        configElement = cfig;

        // Part name and title.  
        partName = Util.safeString(cfig.getAttribute("name"));//$NON-NLS-1$;
        title = partName;

        // Icon.
        String strIcon = cfig.getAttribute("icon");//$NON-NLS-1$
        if (strIcon == null)
            return;

        imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(
                configElement.getDeclaringExtension().getNamespace(), strIcon);

        if (imageDescriptor == null)
            return;

        titleImage = JFaceResources.getResources().createImageWithDefault(imageDescriptor);
    }

    /**
     * Sets the part site.
     * <p>
     * Subclasses must invoke this method from <code>IEditorPart.init</code>
     * and <code>IViewPart.init</code>.
     *
     * @param site the workbench part site
     */
    protected void setSite(IWorkbenchPartSite site) {
        checkSite(site);
        this.partSite = site;
    }

    /**
     * Checks that the given site is valid for this type of part.
     * The default implementation does nothing.
     * 
     * @param site the site to check
     * @since 3.1
     */
    protected void checkSite(IWorkbenchPartSite site) {
        // do nothing
    }

    /**
     * Sets or clears the title of this part. Clients should call this method instead 
     * of overriding getTitle.
     * <p>
     * This may change a title that was previously set using setPartName or setContentDescription.
     * </p>
     *
     * @deprecated new code should use setPartName and setContentDescription
     *
     * @param title the title, or <code>null</code> to clear
     */
    protected void setTitle(String title) {
        title = Util.safeString(title);

        //Do not send changes if they are the same
        if (Util.equals(this.title, title))
            return;
        this.title = title;
        firePropertyChange(IWorkbenchPart.PROP_TITLE);
    }

    /**
     * Sets or clears the title image of this part.
     *
     * @param titleImage the title image, or <code>null</code> to clear
     */
    protected void setTitleImage(Image titleImage) {
        Assert.isTrue(titleImage == null || !titleImage.isDisposed());
        //Do not send changes if they are the same
        if (this.titleImage == titleImage)
            return;
        this.titleImage = titleImage;
        firePropertyChange(IWorkbenchPart.PROP_TITLE);
        if (imageDescriptor != null) {
            JFaceResources.getResources().destroyImage(imageDescriptor);
            imageDescriptor = null;
        }
    }

    /**
     * Sets or clears the title tool tip text of this part. Clients should
     * call this method instead of overriding <code>getTitleToolTip</code>
     *
     * @param toolTip the new tool tip text, or <code>null</code> to clear
     */
    protected void setTitleToolTip(String toolTip) {
        toolTip = Util.safeString(toolTip);
        //Do not send changes if they are the same
        if (Util.equals(this.toolTip, toolTip))
            return;
        this.toolTip = toolTip;
        firePropertyChange(IWorkbenchPart.PROP_TITLE);
    }

    /**
     * Show that this part is busy due to a Job running that it 
     * is listening to.
     * @param busy boolean to indicate that the busy state has started
     *  	or ended.
     * @see org.eclipse.ui.progress.IWorkbenchSiteProgressService#showBusyForFamily(Object)
     * @since 3.0
     */
    public void showBusy(boolean busy) {
        //By default do nothing
    }

    /**
     * {@inheritDoc}
     * <p>
     * It is considered bad practise to overload or extend this method.
     * Parts should call setPartName to change their part name. 
     * </p>
     */
    public String getPartName() {
        return partName;
    }

    /**
     * Sets the name of this part. The name will be shown in the tab area for 
     * the part. Clients should call this method instead of overriding getPartName.
     * Setting this to the empty string will cause a default part name to be used.
     * 
     * <p>
     * setPartName and setContentDescription are intended to replace setTitle.
     * This may change a value that was previously set using setTitle.  
     * </p>
     *
     * @param partName the part name, as it should be displayed in tabs.
     * 
     * @since 3.0
     */
    protected void setPartName(String partName) {

        internalSetPartName(partName);

        setDefaultTitle();
    }

    void setDefaultTitle() {
        String description = getContentDescription();
        String name = getPartName();
        String newTitle = name;

        if (!Util.equals(description, "")) { //$NON-NLS-1$
            newTitle = MessageFormat
                    .format(
                            WorkbenchMessages.WorkbenchPart_AutoTitleFormat, new String[] { name, description });
        }

        setTitle(newTitle);
    }

    /**
     * {@inheritDoc}
     * <p>
     * It is considered bad practise to overload or extend this method.
     * Parts should call setContentDescription to change their content description. 
     * </p>
     */
    public String getContentDescription() {
        return contentDescription;
    }

    /**
     * Sets the content description for this part. The content description is typically
     * a short string describing the current contents of the part. Setting this to the
     * empty string will cause a default content description to be used. Clients should
     * call this method instead of overriding getContentDescription(). For views, the
     * content description is shown (by default) in a line near the top of the view. For
     * editors, the content description is shown beside the part name when showing a
     * list of editors. If the editor is open on a file, this typically contains the path 
     * to the input file, without the filename or trailing slash.
     *
     * <p>
     * This may overwrite a value that was previously set in setTitle
     * </p>
     * 
     * @param description the content description
     * 
     * @since 3.0
     */
    protected void setContentDescription(String description) {
        internalSetContentDescription(description);

        setDefaultTitle();
    }

    void internalSetContentDescription(String description) {
        Assert.isNotNull(description);

        //Do not send changes if they are the same
        if (Util.equals(contentDescription, description))
            return;
        this.contentDescription = description;

        firePropertyChange(IWorkbenchPartConstants.PROP_CONTENT_DESCRIPTION);
    }

    void internalSetPartName(String partName) {
        partName = Util.safeString(partName);

        Assert.isNotNull(partName);

        //Do not send changes if they are the same
        if (Util.equals(this.partName, partName))
            return;
        this.partName = partName;

        firePropertyChange(IWorkbenchPartConstants.PROP_PART_NAME);

    }
    

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.IWorkbenchPartOrientation#getOrientation()
     */
    public int getOrientation(){
		//By default use the orientation in Window
    	return Window.getDefaultOrientation();
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2099.java