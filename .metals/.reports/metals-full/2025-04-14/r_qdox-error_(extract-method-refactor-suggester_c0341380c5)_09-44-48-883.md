error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8730.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8730.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8730.java
text:
```scala
i@@nit(id, title, tooltip, iDesc, partName, ""); //$NON-NLS-1$

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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPart2;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.editorsupport.ComponentSupport;
import org.eclipse.ui.internal.misc.StatusUtil;
import org.eclipse.ui.internal.misc.UIStats;
import org.eclipse.ui.internal.part.services.NullActionBars;
import org.eclipse.ui.internal.part.services.NullEditorInput;
import org.eclipse.ui.internal.presentations.PresentablePart;
import org.eclipse.ui.internal.registry.EditorDescriptor;
import org.eclipse.ui.internal.util.Util;
import org.eclipse.ui.part.IWorkbenchPartOrientation;
import org.eclipse.ui.presentations.IPresentablePart;

public class EditorReference extends WorkbenchPartReference implements
        IEditorReference {

    /**
     * 
     */
    private final EditorManager manager;

    private IMemento editorMemento;

    /**
     * User-readable name of the editor's input
     */
    String name;

    String factoryId;

    IEditorInput restoredInput;
    
    public EditorReference(EditorManager manager, IEditorInput input, EditorDescriptor desc) {
        this.manager = manager;
        initListenersAndHandlers();
        restoredInput = input;
        init(desc.getId(), desc.getLabel(), "", desc.getImageDescriptor(), desc.getLabel(), "");  //$NON-NLS-1$//$NON-NLS-2$
    }
    
    /**
     * Constructs a new editor reference for use by editors being restored from a memento.
     */
    EditorReference(EditorManager manager, IMemento memento) {
        this.manager = manager;
        initListenersAndHandlers();
        this.editorMemento = memento;
        String id = memento.getString(IWorkbenchConstants.TAG_ID);
        String title = memento.getString(IWorkbenchConstants.TAG_TITLE);
        String tooltip = Util.safeString(memento
                .getString(IWorkbenchConstants.TAG_TOOLTIP));
        String partName = memento
                .getString(IWorkbenchConstants.TAG_PART_NAME);

        // For compatibility set the part name to the title if not found
        if (partName == null) {
            partName = title;
        }

        // Get the editor descriptor.
        EditorDescriptor desc = null;
        if (id != null) {
            desc = getDescriptor(id);
        }
        // desc may be null if id is null or desc is not found, but findImage below handles this
        String location = memento.getString(IWorkbenchConstants.TAG_PATH);
        IPath path = location == null ? null : new Path(location);
        ImageDescriptor iDesc = this.manager.findImage(desc, path);

        this.name = memento.getString(IWorkbenchConstants.TAG_NAME);
        if (this.name == null) {
            this.name = title;
        }
        setPinned("true".equals(memento.getString(IWorkbenchConstants.TAG_PINNED))); //$NON-NLS-1$

        IMemento inputMem = memento.getChild(IWorkbenchConstants.TAG_INPUT);
        if (inputMem != null) {
            this.factoryId = inputMem
                    .getString(IWorkbenchConstants.TAG_FACTORY_ID);
        }

        init(id, title, tooltip, iDesc, partName, null);
    }

    public EditorDescriptor getDescriptor() {
        return getDescriptor(getId());
    }
    
    /**
     * @since 3.1 
     *
     * @param id
     * @return
     */
    private EditorDescriptor getDescriptor(String id) {
        EditorDescriptor desc;
        IEditorRegistry reg = WorkbenchPlugin.getDefault()
                .getEditorRegistry();
        desc = (EditorDescriptor) reg.findEditor(id);
        return desc;
    }
    
    /**
     * Initializes the necessary editor listeners and handlers
     */
    private void initListenersAndHandlers() {
        // Create a property change listener to track the "close editors automatically"
        // preference and show/remove the pin icon on editors
        // Only 1 listener will be created in the EditorManager when necessary
        this.manager.checkCreateEditorPropListener();
        // Create a keyboard shortcut handler for pinning editors
        // Only 1 handler will be created in the EditorManager when necessary
        this.manager.checkCreatePinEditorShortcutKeyHandler();
    }

    public PartPane createPane() {
        return new EditorPane(this, this.manager.page, this.manager.editorPresentation.getActiveWorkbook());
    }
    
    /**
     * This method is called when there should be a change in the editor pin
     * status (added or removed) so that it will ask its presentable part
     * to fire a PROP_TITLE event in order for the presentation to request
     * the new icon for this editor
     */
    public void pinStatusUpdated() {
        PartPane partPane = getPane();
        EditorPane editorPane = null;
        if (partPane instanceof EditorPane) {
            editorPane = (EditorPane) partPane;
            IPresentablePart iPresPart = editorPane.getPresentablePart();
            if (iPresPart instanceof PresentablePart)
                ((PresentablePart) iPresPart)
                        .firePropertyChange(IWorkbenchPart.PROP_TITLE);
        }
    }
    
    public String getFactoryId() {
        IEditorPart editor = getEditor(false);
        if (editor != null) {
            IPersistableElement persistable = editor.getEditorInput()
                    .getPersistable();
            if (persistable != null)
                return persistable.getFactoryId();
            return null;
        }
        return factoryId;
    }

    protected String computePartName() {
        if (part instanceof IWorkbenchPart2) {
            return super.computePartName();
        } else {
            return getRawTitle();
        }
    }

    public String getName() {
        if (part != null)
            return getEditor(false).getEditorInput().getName();
        return name;
    }

    public IEditorPart getEditor(boolean restore) {
        return (IEditorPart)getPart(restore);
    }

    public void releaseReferences() {
        super.releaseReferences();
        editorMemento = null;
        name = null;
        factoryId = null;
        restoredInput = null;
    }

    void setName(String name) {
        this.name = name;
    }

    public IMemento getMemento() {
        return editorMemento;
    }

    public boolean isDirty() {
        if (part == null)
            return false;
        return ((IEditorPart) part).isDirty();
    }

    public IWorkbenchPage getPage() {
        return this.manager.page;
    }

    public void dispose() {
        if (part != null) {
            EditorSite site = (EditorSite) ((IEditorPart)part).getEditorSite();
            manager.disposeEditorActionBars((EditorActionBars) site.getActionBars());
            site.dispose();
        }
        
        this.manager.checkDeleteEditorResources();

        super.dispose();
        editorMemento = null;
    }

    public IEditorInput getEditorInput() throws PartInitException {
        IEditorPart part = getEditor(false);
        if (part != null) {
            return part.getEditorInput();
        }
        return getRestoredInput();
    }
    
    public IEditorInput getRestoredInput() throws PartInitException {
        if (restoredInput != null) {
            return restoredInput;
        }

        // Get the input factory.
        IMemento editorMem = getMemento();
        if (editorMem == null) {
            throw new PartInitException(NLS.bind(WorkbenchMessages.EditorManager_no_persisted_state, getId(), getName()));
        }
        IMemento inputMem = editorMem
                .getChild(IWorkbenchConstants.TAG_INPUT);
        String factoryID = null;
        if (inputMem != null) {
            factoryID = inputMem
                    .getString(IWorkbenchConstants.TAG_FACTORY_ID);
        }
        if (factoryID == null) {
            throw new PartInitException(NLS.bind(WorkbenchMessages.EditorManager_no_input_factory_ID, getId(), getName()));
        }
        IAdaptable input = null;
        String label = null; // debugging only
        if (UIStats.isDebugging(UIStats.CREATE_PART_INPUT)) {
            label = getName() != null ? getName() : factoryID;
        }
        try {
            UIStats.start(UIStats.CREATE_PART_INPUT, label);
            IElementFactory factory = PlatformUI.getWorkbench()
                    .getElementFactory(factoryID);
            if (factory == null) {
                throw new PartInitException(NLS.bind(WorkbenchMessages.EditorManager_bad_element_factory, new Object[] { factoryID, getId(), getName() }));
            }

            // Get the input element.
            input = factory.createElement(inputMem);
            if (input == null) {
                throw new PartInitException(NLS.bind(WorkbenchMessages.EditorManager_create_element_returned_null, new Object[] { factoryID, getId(), getName() }));
            }
        } finally {
            UIStats.end(UIStats.CREATE_PART_INPUT, input, label);
        }
        if (!(input instanceof IEditorInput)) {
            throw new PartInitException(NLS.bind(WorkbenchMessages.EditorManager_wrong_createElement_result, new Object[] { factoryID, getId(), getName() }));
        }
        restoredInput = (IEditorInput) input;
        return restoredInput;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPartReference#getTitleImage()
     * This method will append a pin to the icon of the editor
     * if the "automatically close editors" option in the 
     * preferences is enabled and the editor has been pinned.
     */
    public ImageDescriptor computeImageDescriptor() {
        ImageDescriptor descriptor = super.computeImageDescriptor();
        if (!isPinned())
            return descriptor;

        // Check if the pinned preference is set
        IPreferenceStore prefStore = WorkbenchPlugin.getDefault()
                .getPreferenceStore();
        boolean bUsePin = prefStore
                .getBoolean(IPreferenceConstants.REUSE_EDITORS_BOOLEAN);

        if (!bUsePin)
            return descriptor;

        ImageDescriptor pinDesc = this.manager.getEditorPinImageDesc();
        if (pinDesc == null)
            return descriptor;

        return new OverlayIcon(descriptor, pinDesc, new Point(16, 16));
    }

    /**
     * Wrapper for restoring the editor. First, this delegates to busyRestoreEditorHelper
     * to do the real work of restoring the view. If unable to restore the editor, this
     * method tries to substitute an error part and return success.
     *
     * @param ref_
     * @param manager TODO
     * @return
     */
    protected IWorkbenchPart createPart() {
                
        PartInitException exception = null;
        
        IWorkbenchPart result = null;
        
        // Try to restore the editor -- this does the real work of restoring the editor
        //
        try {
            result = createPartHelper();
        } catch (PartInitException e) {
            exception = e;
        }

        
        // If unable to create the part, create an error part instead
        if (exception != null) {
            
            IStatus originalStatus = exception.getStatus();
            IStatus logStatus = StatusUtil.newStatus(originalStatus, 
                    NLS.bind("Unable to create editor ID {0}: {1}",  //$NON-NLS-1$
                            getId(), originalStatus.getMessage()));
            WorkbenchPlugin.log(logStatus);
            
            IStatus displayStatus = StatusUtil.newStatus(originalStatus,
                    NLS.bind(WorkbenchMessages.EditorManager_unableToCreateEditor,
                            originalStatus.getMessage()));
            
            ErrorEditorPart part = new ErrorEditorPart(displayStatus);
            
            IEditorInput input;
            try {
                input = getEditorInput();
            } catch (PartInitException e1) {
                input = new NullEditorInput();
            }
            
            EditorPane pane = (EditorPane)getPane();
            
            pane.createControl((Composite) manager.page.getEditorPresentation().getLayoutPart().getControl());
            
            EditorDescriptor descr = getDescriptor();
            EditorSite site = new EditorSite(this, part, manager.page, descr);
            
            site.setActionBars(new EditorActionBars(new NullActionBars(), getId()));
            try {
                part.init(site, input);
            } catch (PartInitException e) {
                WorkbenchPlugin.log(e);
                return null;
            }

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

    private IEditorPart createPartHelper() throws PartInitException {
        
        // Things that will need to be disposed if an exception occurs (listed in the order they
        // need to be disposed, and set to null if they haven't been created yet)
        Composite content = null;
        IEditorPart initializedPart = null;
        EditorActionBars actionBars = null;
        EditorSite site = null;
        
        try {
            IEditorInput editorInput = getEditorInput();
            
            // Get the editor descriptor.
            String editorID = getId();
            EditorDescriptor desc = getDescriptor();
            
            if (desc == null) {
                throw new PartInitException(NLS.bind(WorkbenchMessages.EditorManager_missing_editor_descriptor, editorID)); //$NON-NLS-1$
            }
            
            IEditorPart part;
            
            if (desc.isInternal()) {    
                // Create an editor instance.
                try {
                    UIStats.start(UIStats.CREATE_PART, editorID);
                    part = manager.createPart(desc);
                } finally {
                    UIStats.end(UIStats.CREATE_PART, this, editorID);
                }
                
            } else if (desc.getId().equals(
                    IEditorRegistry.SYSTEM_INPLACE_EDITOR_ID)) {
                
                part = ComponentSupport.getSystemInPlaceEditor();
                
                if (part == null) {
                    throw new PartInitException(WorkbenchMessages.EditorManager_no_in_place_support); //$NON-NLS-1$
                }
            } else {
                throw new PartInitException(NLS.bind(WorkbenchMessages.EditorManager_invalid_editor_descriptor, editorID)); //$NON-NLS-1$
            }
            // Create a pane for this part
            PartPane pane = getPane();
    
            pane.createControl((Composite) manager.page.getEditorPresentation().getLayoutPart().getControl());
            
            // Create controls
            int style = SWT.NONE;
            if(part instanceof IWorkbenchPartOrientation){
                style = ((IWorkbenchPartOrientation) part).getOrientation();
            }
    
            // Link everything up to the part reference (the part reference itself should not have
            // been modified until this point)
            site = manager.createSite(this, part, desc, editorInput);
            
            // Remember the site and the action bars (now that we've created them, we'll need to dispose
            // them if an exception occurs)
            actionBars = (EditorActionBars) site.getActionBars();
            
            Composite parent = (Composite)pane.getControl();
            content = new Composite(parent, style);
    
            content.setLayout(new FillLayout());
    
            try {
                UIStats.start(UIStats.CREATE_PART_CONTROL, editorID);
                part.createPartControl(content);
            
                parent.layout(true);
            } finally {
                UIStats.end(UIStats.CREATE_PART_CONTROL, part, editorID);
            }
    
            // The editor should now be fully created. Exercise its public interface, and sanity-check
            // it wherever possible. If it's going to throw exceptions or behave badly, it's much better
            // that it does so now while we can still cancel creation of the part.
            PartTester.testEditor(part);
            
            return part;
            
        } catch (Exception e) {
            // Dispose anything which we allocated in the try block
            if (content != null) {
                try {
                    content.dispose();
                } catch (RuntimeException re) {
                    WorkbenchPlugin.log(re);
                }
            }
    
            if (initializedPart != null) {
                try {
                    initializedPart.dispose();
                } catch (RuntimeException re) {
                    WorkbenchPlugin.log(re);
                }
            }
            
            if (actionBars != null) {
                try {
                    manager.disposeEditorActionBars(actionBars);
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
            
            throw new PartInitException(StatusUtil.getLocalizedMessage(e), StatusUtil.getCause(e));
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8730.java