error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1563.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1563.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1563.java
text:
```scala
n@@ew StatusPart(parent, e.getStatus());

/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.part;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.components.ComponentException;
import org.eclipse.ui.components.Components;
import org.eclipse.ui.components.FactoryMap;
import org.eclipse.ui.components.ServiceFactory;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.part.Part;
import org.eclipse.ui.part.services.IWorkbenchPartFactory;
import org.eclipse.ui.part.services.ISecondaryId;
import org.eclipse.ui.part.services.ISelectionHandler;

/**
 * Wraps a new-style Part in an IWorkbenchPart. This object will create and manage
 * the lifecycle of the part. Subclasses will support wrapping a Part in an
 * IViewPart and IEditorPart respectively. If you are interested in adapting
 * an existing Part rather than wrapping a new one, use <code>NewPartToOldAdapter</code>
 * instead.
 * 
 * @since 3.1
 */
abstract class NewPartToOldWrapper extends NewPartToWorkbenchPartAdapter {

    private Part part = null;
    private SelectionProviderAdapter selectionProvider;

    private PartServices services;
    private IWorkbenchPartSite partSite;
    
    private final class PartServices implements ISecondaryId, IAdaptable, ISelectionHandler {
       		
		/* (non-Javadoc)
		 * @see org.eclipse.ui.workbench.services.ISecondaryId#getSecondaryId()
		 */
		public String getSecondaryId() {
			return NewPartToOldWrapper.this.getSecondaryId();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
		 */
		public Object getAdapter(Class adapter) {
//		    return getViewSite().getAdapter(adapter);
//			if (adapter == IActionBars.class) {
//				return getViewSite().getActionBars();
//			} else if (adapter == IKeyBindingService.class) {
//				return getViewSite().getKeyBindingService();
//			}
			return null;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ui.part.interfaces.ISelectable#setSelection(org.eclipse.jface.viewers.ISelection)
		 */
		public void setSelection(ISelection newSelection) {
            ISelectionProvider newSelectionProvider = null;
            
            if (newSelection != null) {
                //getSelectionProvider().setSelection(newSelection);
                newSelectionProvider = getSelectionProvider();
                newSelectionProvider.setSelection(newSelection);
            }
            
            if (getSite().getSelectionProvider() != newSelectionProvider) {
                getSite().setSelectionProvider(newSelectionProvider); 
            }
		}

    };

    public NewPartToOldWrapper(IPartPropertyProvider provider) {
        super(provider);
        
        this.services = new PartServices();        
    }
    
    protected abstract IMemento getMemento();

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(Composite parent) {
        try {
        	FactoryMap args = new FactoryMap();
            args.addInstance(services);
            args.addInstance(getPropertyProvider());
            addServices(args);
            args.mapInstance(Composite.class, parent);
            parent.setLayout(new FillLayout());
        	
            part = createPart(parent, args); 
            //parent.layout(true);
        } catch (ComponentException e) {
            WorkbenchPlugin.getDefault().getLog().log(e.getStatus());
            new ErrorPart(parent);
        }
    }

    protected void addServices(FactoryMap context) {
        context.mapInstance(IPartPropertyProvider.class, getPropertyProvider());
    }
  
    /**
     * @since 3.1 
     *
     * @param args
     * @return
     */
    protected abstract Part createPart(Composite parent, ServiceFactory args) throws ComponentException;

    /**
     * @since 3.1 
     *
     * @return
     */
    protected abstract String getSecondaryId();

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPart#setFocus()
     */
    public void setFocus() {
    	if (part != null) {
    		part.getControl().setFocus();
    	}
    }
    
    private SelectionProviderAdapter getSelectionProvider() {
    	if (selectionProvider == null) {
    		selectionProvider = new SelectionProviderAdapter();
            getSite().setSelectionProvider(selectionProvider);
    	}
    	return selectionProvider;
    }
        
    protected Part getPart() {
        return part;
    }
    
    protected IWorkbenchPartFactory getFactory() {
        // Try to be well-behaved and get the factory from our site
        IWorkbenchPartFactory siteFactory = (IWorkbenchPartFactory) getSite().getAdapter(IWorkbenchPartFactory.class);
        
        // If the site doesn't want to play nicely, reach to the workbench page
        if (siteFactory == null) {
            return getSite().getPage().getPartFactory();
        }
        
        return siteFactory;
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
        this.partSite = site;
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPart#getSite()
     */
    public IWorkbenchPartSite getSite() {
        return partSite;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    public Object getAdapter(Class adapter) {
        if (adapter.isInstance(this)) {
            return this;
        }
        
        if (part != null) {
            return Components.getAdapter(part, adapter);
        }
        
        return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1563.java