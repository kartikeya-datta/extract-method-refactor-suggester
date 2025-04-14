error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1541.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1541.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1541.java
text:
```scala
r@@eturn Status.OK_STATUS;

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Markus Alexander Kuppe, Versant Corporation - bug #215797
 *     Sascha Zak - bug 282874
 *******************************************************************************/
package org.eclipse.ui.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.dynamichelpers.IExtensionChangeHandler;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPart3;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.IViewDescriptor;
import org.eclipse.ui.views.IViewRegistry;

/**
 * The ViewFactory is used to control the creation and disposal of views.  
 * It implements a reference counting strategy so that one view can be shared
 * by more than one client.
 */
public class ViewFactory implements IExtensionChangeHandler {

    private ReferenceCounter counter;

    private HashMap mementoTable = new HashMap();

    WorkbenchPage page;

    IViewRegistry viewReg;

    /**
     * Separates a view's primary id from its secondary id in view key strings.
     */
    static final String ID_SEP = ":"; //$NON-NLS-1$

    /**
     * Returns a string representing a view with the given id and (optional) secondary id,
     * suitable for use as a key in a map.  
     * 
     * @param id primary id of the view
     * @param secondaryId secondary id of the view or <code>null</code>
     * @return the key
     */
    static String getKey(String id, String secondaryId) {
        return secondaryId == null ? id : id + ID_SEP + secondaryId;
    }

    /**
     * Returns a string representing the given view reference, suitable for use as a key in a map.  
     * 
     * @param viewRef the view reference
     * @return the key
     */
    static String getKey(IViewReference viewRef) {
        return getKey(viewRef.getId(), viewRef.getSecondaryId());
    }

    /**
     * Extracts ths primary id portion of a compound id.
     * @param compoundId a compound id of the form: primaryId [':' secondaryId]
     * @return the primary id
     */
    static String extractPrimaryId(String compoundId) {
        int i = compoundId.lastIndexOf(ID_SEP);
        if (i == -1) {
			return compoundId;
		}
        return compoundId.substring(0, i);
    }

    /**
     * Extracts ths secondary id portion of a compound id.
     * @param compoundId a compound id of the form: primaryId [':' secondaryId]
     * @return the secondary id, or <code>null</code> if none
     */
    static String extractSecondaryId(String compoundId) {
        int i = compoundId.lastIndexOf(ID_SEP);
        if (i == -1) {
			return null;
		}
        return compoundId.substring(i + 1);
    }

    /**
     * Returns whether the given view id contains a wildcard. Wildcards cannot
     * be used in regular view ids, only placeholders.
     * 
     * @param viewId the view id
     * @return <code>true</code> if the given view id contains a wildcard,
     *         <code>false</code> otherwise
     * 
     * @since 3.1
     */
    static boolean hasWildcard(String viewId) {
        return viewId.indexOf(PartPlaceholder.WILD_CARD) >= 0;
    }
    
    /**
     * Constructs a new view factory.
     */
    public ViewFactory(WorkbenchPage page, IViewRegistry reg) {
        super();
        this.page = page;
        this.viewReg = reg;
        counter = new ReferenceCounter();
        page.getExtensionTracker().registerHandler(this, null);
    }
    
    /**
     * Creates an instance of a view defined by id.
     * 
     * This factory implements reference counting.  The first call to this
     * method will return a new view.  Subsequent calls will return the
     * first view with an additional reference count.  The view is
     * disposed when releaseView is called an equal number of times
     * to getView.
     */
    public IViewReference createView(final String id) throws PartInitException {
        return createView(id, null);
    }

    /**
     * Creates an instance of a view defined by id and secondary id.
     * 
     * This factory implements reference counting.  The first call to this
     * method will return a new view.  Subsequent calls will return the
     * first view with an additional reference count.  The view is
     * disposed when releaseView is called an equal number of times
     * to createView.
     */
    public IViewReference createView(String id, String secondaryId)
            throws PartInitException {
        IViewDescriptor desc = viewReg.find(id);
        // ensure that the view id is valid
        if (desc == null) {
			throw new PartInitException(NLS.bind(WorkbenchMessages.ViewFactory_couldNotCreate,  id ));
		}
        // ensure that multiple instances are allowed if a secondary id is given
        if (secondaryId != null) {
            if (!desc.getAllowMultiple()) {
                throw new PartInitException(NLS.bind(WorkbenchMessages.ViewFactory_noMultiple, id)); 
            }
        }
        String key = getKey(id, secondaryId);
        IViewReference ref = (IViewReference) counter.get(key);
        if (ref == null) {
            IMemento memento = (IMemento) mementoTable.get(key);
            ref = new ViewReference(this, id, secondaryId, memento);
            mementoTable.remove(key);
            counter.put(key, ref);
            getWorkbenchPage().partAdded((ViewReference)ref);
        } else {
            counter.addRef(key);
        }
        return ref;
    }
    
    /**
     * Returns the set of views being managed by this factory
     *
     * @return the set of views being managed by this factory
     */
    public IViewReference[] getViewReferences() {
        List values = counter.values();
        
        return (IViewReference[]) values.toArray(new IViewReference[values.size()]);
    }

    /**
     * Returns the view with the given id, or <code>null</code> if not found.
     */
    public IViewReference getView(String id) {
        return getView(id, null);
    }

    /**
     * Returns the view with the given id and secondary id, or <code>null</code> if not found.
     */
    public IViewReference getView(String id, String secondaryId) {
        String key = getKey(id, secondaryId);
        return (IViewReference) counter.get(key);
    }

    /**
     * @return the <code>IViewRegistry</code> used by this factory.
     * @since 3.0
     */
    public IViewRegistry getViewRegistry() {
        return viewReg;
    }

    /**
     * Returns a list of views which are open.
     */
    public IViewReference[] getViews() {
        List list = counter.values();
        IViewReference[] array = new IViewReference[list.size()];
        list.toArray(array);
        return array;
    }

    /**
     * @return the <code>WorkbenchPage</code> used by this factory.
     * @since 3.0
     */
    public WorkbenchPage getWorkbenchPage() {
        return page;
    }

    /**
     * 
     * @param viewRef
     * @return the current reference count for the given view
     */
    public int getReferenceCount(IViewReference viewRef) {
        String key = getKey(viewRef);
        IViewReference ref = (IViewReference) counter.get(key);
        return ref==null ? 0 : counter.getRef(key);
    }
    
    /**
     * Releases an instance of a view.
     *
     * This factory does reference counting.  For more info see
     * getView.
     */
    public void releaseView(IViewReference viewRef) {
        String key = getKey(viewRef);
        IViewReference ref = (IViewReference) counter.get(key);
        if (ref == null) {
			return;
		}
        int count = counter.removeRef(key);
        if (count <= 0) {
            getWorkbenchPage().partRemoved((ViewReference)ref);
        }
    }

    /**
     * Restore view states.
     *  
     * @param memento the <code>IMemento</code> to restore from.
     * @return <code>IStatus</code>
     */
    public IStatus restoreState(IMemento memento) {
        IMemento mem[] = memento.getChildren(IWorkbenchConstants.TAG_VIEW);
        for (int i = 0; i < mem.length; i++) {
            //for dynamic UI - add the next line to replace subsequent code that is commented out
            restoreViewState(mem[i]);
        }
        return new Status(IStatus.OK, PlatformUI.PLUGIN_ID, 0, "", null); //$NON-NLS-1$
    }

    /**
     * Save view states.
     * 
     * @param memento the <code>IMemento</code> to save to.
     * @return <code>IStatus</code>
     */
    public IStatus saveState(IMemento memento) {
        final MultiStatus result = new MultiStatus(PlatformUI.PLUGIN_ID,
                IStatus.OK, WorkbenchMessages.ViewFactory_problemsSavingViews, null); 

        final IViewReference refs[] = getViews();
        for (int i = 0; i < refs.length; i++) {
        	IViewDescriptor desc = viewReg.find(refs[i].getId());
			if (desc != null && desc.isRestorable()) {
				//for dynamic UI - add the following line to replace subsequent code which is commented out
				saveViewState(memento, refs[i], result);
			}
        }
        return result;
    }

    //	for dynamic UI
    public IMemento saveViewState(IMemento memento, IViewReference ref,
            MultiStatus res) {
        final MultiStatus result = res;
        final IMemento viewMemento = memento
                .createChild(IWorkbenchConstants.TAG_VIEW);
        viewMemento.putString(IWorkbenchConstants.TAG_ID, ViewFactory
                .getKey(ref));
        if (ref instanceof ViewReference) {
            viewMemento.putString(IWorkbenchConstants.TAG_PART_NAME,
                    ((ViewReference) ref).getPartName());
        }
        final IViewReference viewRef = ref;
        final IViewPart view = (IViewPart) ref.getPart(false);
        if (view != null) {
            SafeRunner.run(new SafeRunnable() {
                public void run() {
                	if (view instanceof IWorkbenchPart3) {
						Map properties = ((IWorkbenchPart3) view)
								.getPartProperties();
						if (!properties.isEmpty()) {
							IMemento propBag = viewMemento
									.createChild(IWorkbenchConstants.TAG_PROPERTIES);
							Iterator i = properties.entrySet().iterator();
							while (i.hasNext()) {
								Map.Entry entry = (Map.Entry) i.next();
								IMemento p = propBag.createChild(
										IWorkbenchConstants.TAG_PROPERTY,
										(String) entry.getKey());
								p.putTextData((String) entry.getValue());
							}
						}
					}
                    view.saveState(viewMemento
                            .createChild(IWorkbenchConstants.TAG_VIEW_STATE));
                }

                public void handleException(Throwable e) {
                    result
                            .add(new Status(
                                    IStatus.ERROR,
                                    PlatformUI.PLUGIN_ID,
                                    0,
                                    NLS.bind(WorkbenchMessages.ViewFactory_couldNotSave, viewRef.getTitle() ),
                                    e));
                }
            });
        } else {
        	IMemento mem = null;
        	IMemento props = null;
        	
        	// if we've created the reference once, any previous workbench
        	// state memento is there.  After once, there is no previous
        	// session state, so it should be null.
			if (ref instanceof ViewReference) {
				mem = ((ViewReference) ref).getMemento();
				if (mem!=null) {
					props = mem.getChild(IWorkbenchConstants.TAG_PROPERTIES);
				}
				if (mem!=null) {
					mem = mem.getChild(IWorkbenchConstants.TAG_VIEW_STATE);
				}
			}
			if (props != null) {
				viewMemento.createChild(IWorkbenchConstants.TAG_PROPERTIES)
						.putMemento(props);
			}
			if (mem != null) {
				IMemento child = viewMemento
						.createChild(IWorkbenchConstants.TAG_VIEW_STATE);
				child.putMemento(mem);
			}
        }
        return viewMemento;
    }

    // for dynamic UI
    public void restoreViewState(IMemento memento) {
        String compoundId = memento.getString(IWorkbenchConstants.TAG_ID);
        mementoTable.put(compoundId, memento);
    }

    IMemento getViewState(String key) {
        IMemento memento = (IMemento) mementoTable.get(key);

        if (memento == null) {
            return null;
        }

        return memento.getChild(IWorkbenchConstants.TAG_VIEW_STATE);
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.runtime.dynamicHelpers.IExtensionChangeHandler#removeExtension(org.eclipse.core.runtime.IExtension, java.lang.Object[])
     */
    public void removeExtension(IExtension source, Object[] objects) {
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof IViewPart) {
                IViewPart part = (IViewPart) objects[i];
                // String primaryViewId = part.getViewSite().getId();
                // String secondaryViewId = part.getViewSite().getSecondaryId();
                // IViewReference viewRef = page.findViewReference(
                // primaryViewId, secondaryViewId);
                // IPerspectiveDescriptor[] descs =
                // page.getOpenedPerspectives();
                // Perspective active = page.getActivePerspective();
                // for (int i = 0; i < descs.length; i++) {
                // Perspective nextPerspective = page.findPerspective(descs[i]);
                //
                // if (nextPerspective == null || active == nextPerspective)
                // continue;
                //
                // page.hideView(nextPerspective, viewRef);
                // }
                page.hideView(part);
            }

        }
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.core.runtime.dynamicHelpers.IExtensionChangeHandler#addExtension(org.eclipse.core.runtime.dynamicHelpers.IExtensionTracker, org.eclipse.core.runtime.IExtension)
     */
    public void addExtension(IExtensionTracker tracker,IExtension extension) {
        //Do nothing
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1541.java