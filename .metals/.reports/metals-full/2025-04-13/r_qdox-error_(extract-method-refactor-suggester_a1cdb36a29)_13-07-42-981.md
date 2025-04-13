error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3443.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3443.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3443.java
text:
```scala
i@@nit(id, title, "", iDesc, name, "");  //$NON-NLS-1$//$NON-NLS-2$

/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPart2;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.misc.StatusUtil;
import org.eclipse.ui.internal.misc.UIStats;
import org.eclipse.ui.internal.registry.ViewDescriptor;
import org.eclipse.ui.internal.util.Util;
import org.eclipse.ui.part.IWorkbenchPartOrientation;
import org.eclipse.ui.views.IViewDescriptor;
import org.eclipse.ui.views.IViewRegistry;

class ViewReference extends WorkbenchPartReference implements
        IViewReference {

    /**
     * 
     */
    private final ViewFactory factory;

    String secondaryId;

    private boolean create = true;
    
    private boolean creationInProgress = false;

    public ViewReference(ViewFactory factory, String id, String secondaryId, IMemento memento) {
        super();
        this.factory = factory;
        ViewDescriptor desc = (ViewDescriptor) this.factory.viewReg.find(id);
        ImageDescriptor iDesc = null;
        String title = null;
        if (desc != null) {
            iDesc = desc.getImageDescriptor();
            title = desc.getLabel();
        }

        String name = null;

        if (memento != null) {
            name = memento.getString(IWorkbenchConstants.TAG_PART_NAME);
        }
        if (name == null) {
            name = title;
        }

        init(id, title, null, iDesc, name, null);
        this.secondaryId = secondaryId;
    }
    
    protected PartPane createPane() {
        return new ViewPane(this, this.factory.page);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.WorkbenchPartReference#dispose()
     */
    public void dispose() {
        IViewPart view = getView(false);
        if (view != null) {
            // Free action bars, pane, etc.
            PartSite site = (PartSite) view.getSite();
            ViewActionBars actionBars = (ViewActionBars) site.getActionBars();
            actionBars.dispose();

            // Free the site.
            site.dispose();
        }
        
        super.dispose();
        create = false;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPartReference#getPage()
     */
    public IWorkbenchPage getPage() {
        return this.factory.page;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.WorkbenchPartReference#getRegisteredName()
     */
    public String getRegisteredName() {
        if (part != null && part.getSite() != null) {
            return part.getSite().getRegisteredName();
        }

        IViewRegistry reg = this.factory.viewReg;
        IViewDescriptor desc = reg.find(getId());
        if (desc != null)
            return desc.getLabel();
        return getTitle();
    }

    protected String computePartName() {
        if (part instanceof IWorkbenchPart2) {
            return super.computePartName();
        } else {
            return getRegisteredName();
        }
    }

    protected String computeContentDescription() {
        if (part instanceof IWorkbenchPart2) {
            return super.computeContentDescription();
        } else {
            String rawTitle = getRawTitle();

            if (!Util.equals(rawTitle, getRegisteredName())) {
                return rawTitle;
            }

            return ""; //$NON-NLS-1$
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IViewReference
     */
    public String getSecondaryId() {
        return secondaryId;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IViewReference#getView(boolean)
     */
    public IViewPart getView(boolean restore) {
        return (IViewPart) getPart(restore);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IViewReference#isFastView()
     */
    public boolean isFastView() {
        return this.factory.page.isFastView(this);
    }

    /**
     * Wrapper for restoring the view. First, this delegates to busyRestoreViewHelper
     * to do the real work of restoring the view. If unable to restore the view, this
     * method tries to substitute an error part and return success.
     *
     * @param factory TODO
     * @return
     */
    public IWorkbenchPart createPart() {
        
        // Check the status of this part
        
        IWorkbenchPart result = null;
        PartInitException exception = null;
        
        // Try to restore the view -- this does the real work of restoring the view
        //
        try {
            result = createPartHelper();
        } catch (PartInitException e) {
            exception = e;
        }
        
        // If unable to create the part, create an error part instead
        if (exception != null) {
            IStatus partStatus = exception.getStatus();
            IStatus displayStatus = StatusUtil.newStatus(partStatus,
                    NLS.bind(WorkbenchMessages.ViewFactory_initException, partStatus.getMessage()));
            
            IStatus logStatus = StatusUtil.newStatus(partStatus,
                    NLS.bind("Unable to create view ID {0}: {1}", getId(), partStatus.getMessage()));  //$NON-NLS-1$
            WorkbenchPlugin.log(logStatus);

            IViewDescriptor desc = factory.viewReg.find(getId());
            String label = getId();
            if (desc != null) {
                label = desc.getLabel();
            }
            
            ErrorViewPart part = new ErrorViewPart(displayStatus);

            PartPane pane = getPane();
            ViewSite site = new ViewSite(this, part, factory.page, getId(), PlatformUI.PLUGIN_ID, label);
            site.setActionBars(new ViewActionBars(factory.page.getActionBars(),
                    (ViewPane) pane));
            try {
                part.init(site);
            } catch (PartInitException e) {
                WorkbenchPlugin.log(e);
                return null;
            }
            part.setPartName(label);

            Composite parent = (Composite)pane.getControl();
            Composite content = new Composite(parent, SWT.NONE);
            content.setLayout(new FillLayout());
            
            try {
                part.createPartControl(content);
            } catch (Exception e) {
                content.dispose();
                WorkbenchPlugin.log(e);
                return null;
            }
            
            result = part;
        }
            
        return result;
    }

    private IWorkbenchPart createPartHelper() throws PartInitException {

        IWorkbenchPart result = null;
        
        String key = ViewFactory.getKey(this);
        IMemento stateMem = factory.getViewState(key);
        
        IViewDescriptor desc = factory.viewReg.find(getId());
        if (desc == null) {
            throw new PartInitException(WorkbenchMessages.ViewFactory_couldNotCreate);
        }
        
        // Create the part pane
        PartPane pane = getPane();
        
        // Create the pane's top-level control
        pane.createControl(factory.page.getClientComposite());
        
        String label = desc.getLabel(); // debugging only
    
        // Things that will need to be disposed if an exception occurs (they are listed here
        // in the order they should be disposed)
        Composite content = null;
        IViewPart initializedView = null;
        ViewSite site = null;
        ViewActionBars actionBars = null;
        // End of things that need to be explicitly disposed from the try block
        
        try {
            IViewPart view = null;
            try { 
                UIStats.start(UIStats.CREATE_PART, label);
                view = desc.createView();
            } finally {
                UIStats.end(UIStats.CREATE_PART, view, label);    
            }
    
            // Create site
            site = new ViewSite(this, view, factory.page, desc);
            actionBars = new ViewActionBars(factory.page.getActionBars(),
                    (ViewPane) pane);
            site.setActionBars(actionBars);
    
            try {
                UIStats.start(UIStats.INIT_PART, label);
                view.init(site, stateMem);
                // Once we've called init, we MUST dispose the view. Remember the fact that
                // we've initialized the view in case an exception is thrown.
                initializedView = view;
                
            } finally {
                UIStats.end(UIStats.INIT_PART, view, label);
            }
    
            if (view.getSite() != site) {
                throw new PartInitException(WorkbenchMessages.ViewFactory_siteException, null);
            }
            int style = SWT.NONE;
            if(view instanceof IWorkbenchPartOrientation) {
                style = ((IWorkbenchPartOrientation) view).getOrientation();
            }
   
            // Create the top-level composite
            {
                Composite parent = (Composite)pane.getControl();
                content = new Composite(parent, style);
                content.setLayout(new FillLayout());
   
                try {
                    UIStats.start(UIStats.CREATE_PART_CONTROL, label);
                    view.createPartControl(content);
   
                    parent.layout(true);
                } finally {
                    UIStats.end(UIStats.CREATE_PART_CONTROL, view, label);
                }
            }
            
            // Install the part's tools and menu
            {
                ViewActionBuilder builder = new ViewActionBuilder();
                builder.readActionExtensions(view);
                ActionDescriptor[] actionDescriptors = builder
                        .getExtendedActions();
                KeyBindingService keyBindingService = (KeyBindingService) view
                        .getSite().getKeyBindingService();
   
                if (actionDescriptors != null) {
                    for (int i = 0; i < actionDescriptors.length; i++) {
                        ActionDescriptor actionDescriptor = actionDescriptors[i];
   
                        if (actionDescriptor != null) {
                            IAction action = actionDescriptors[i]
                                    .getAction();
   
                            if (action != null
                                    && action.getActionDefinitionId() != null)
                                keyBindingService.registerAction(action);
                        }
                    }
                }
                site.getActionBars().updateActionBars();
            }
            
            // The editor should now be fully created. Exercise its public interface, and sanity-check
            // it wherever possible. If it's going to throw exceptions or behave badly, it's much better
            // that it does so now while we can still cancel creation of the part.
            PartTester.testView(view);
            
            result = view;
            
            IConfigurationElement element = (IConfigurationElement) desc
                    .getAdapter(IConfigurationElement.class);
            if (element != null) {
                factory.page.getExtensionTracker().registerObject(
                        element.getDeclaringExtension(), view,
                        IExtensionTracker.REF_WEAK);
            }
        } catch (Exception e) {
            // An exception occurred. First deallocate anything we've allocated in the try block (see the top
            // of the try block for a list of objects that need to be explicitly disposed)
            if (content != null) {
                try {
                    content.dispose();
                } catch (RuntimeException re) {
                    WorkbenchPlugin.log(re);
                }
            }
            
            if (initializedView != null) {
                try {
                    initializedView.dispose();
                } catch (RuntimeException re) {
                    WorkbenchPlugin.log(re);
                }
            }
    
            if (site != null) {
                try {
                    site.dispose();
                } catch (RuntimeException re) {
                    WorkbenchPlugin.log(re);
                }
            }
            
            if (actionBars != null) {
                try {
                    actionBars.dispose();
                } catch (RuntimeException re) {
                    WorkbenchPlugin.log(re);
                }
            }
            
            throw new PartInitException(WorkbenchPlugin.getStatus(e));
        }
        
        return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3443.java