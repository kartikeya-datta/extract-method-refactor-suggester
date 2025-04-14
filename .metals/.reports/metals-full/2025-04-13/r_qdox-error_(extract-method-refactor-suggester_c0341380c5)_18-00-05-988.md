error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2011.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2011.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2011.java
text:
```scala
l@@ayout = window.getCoolBarVisible() || window.getPerspectiveBarVisible();

/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.intro.IntroMessages;
import org.eclipse.ui.internal.util.PrefUtil;
import org.eclipse.ui.intro.IIntroPart;
import org.eclipse.ui.intro.IIntroSite;
import org.eclipse.ui.part.ViewPart;

/**
 * Simple view that will wrap an <code>IIntroPart</code>.
 * 
 * @since 3.0
 */
public final class ViewIntroAdapterPart extends ViewPart {

    private IIntroPart introPart;

    private IIntroSite introSite;

    private boolean handleZoomEvents = true;

    /**
     * Adds a listener that toggles standby state if the view pane is zoomed. 
     */
    private void addPaneListener() {
        IWorkbenchPartSite site = getSite();
        if (site instanceof PartSite) {        
            final WorkbenchPartReference ref = ((WorkbenchPartReference)((PartSite) site).getPartReference()); 
            ref.addInternalPropertyListener(
                    new IPropertyListener() {
                        public void propertyChanged(Object source, int propId) {
                            if (handleZoomEvents) {
                                if (propId == WorkbenchPartReference.INTERNAL_PROPERTY_ZOOMED) {
                                    setStandby(!ref.getPane().isZoomed());
                                }
                            }
                        }
                    });
        }
    }

    /**
     * Forces the standby state of the intro part.
     * 
     * @param standby update the standby state
     */
    public void setStandby(final boolean standby) {
        final Control control = ((PartSite) getSite()).getPane().getControl();
        BusyIndicator.showWhile(control.getDisplay(), new Runnable() {
            public void run() {
                try {
                    control.setRedraw(false);
                    introPart.standbyStateChanged(standby);
                } finally {
                    control.setRedraw(true);
                }

                setBarVisibility(standby);
            }
        });
    }

    /**
     * Toggles handling of zoom events.
     * 
     * @param handle whether to handle zoom events
     */
    public void setHandleZoomEvents(boolean handle) {
        handleZoomEvents = handle;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(Composite parent) {
        addPaneListener();
        introPart.createPartControl(parent);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPart#dispose()
     */
    public void dispose() {
    	setBarVisibility(true);
        super.dispose();
        getSite().getWorkbenchWindow().getWorkbench().getIntroManager()
                .closeIntro(introPart);
        introPart.dispose();
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    public Object getAdapter(Class adapter) {
        return introPart.getAdapter(adapter);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPart#getTitleImage()
     */
    public Image getTitleImage() {
        return introPart.getTitleImage();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#getTitle()
     */
    public String getTitle() {
    	// this method is called eagerly before our init method is called (and
    	// therefore before our intropart is created).  By default return 
    	// the view title from the view declaration.  We will fire a property
    	// change to set the title to the proper value in the init method.
    	return introPart == null ? super.getTitle() : introPart.getTitle();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IViewPart#init(org.eclipse.ui.IViewSite, org.eclipse.ui.IMemento)
     */
    public void init(IViewSite site, IMemento memento) throws PartInitException {
        super.init(site);
        Workbench workbench = (Workbench) site.getWorkbenchWindow()
                .getWorkbench();
        try {
            introPart = workbench.getWorkbenchIntroManager()
                    .createNewIntroPart();
            // reset the part name of this view to be that of the intro title
            setPartName(introPart.getTitle());
            introPart.addPropertyListener(new IPropertyListener() {
                public void propertyChanged(Object source, int propId) {
                    firePropertyChange(propId);
                }
            });
            introSite = new ViewIntroAdapterSite(site, workbench
                    .getIntroDescriptor());
            introPart.init(introSite, memento);
            
        } catch (CoreException e) {
            WorkbenchPlugin
                    .log(
                            IntroMessages.Intro_could_not_create_proxy, new Status(IStatus.ERROR, WorkbenchPlugin.PI_WORKBENCH, IStatus.ERROR, IntroMessages.Intro_could_not_create_proxy, e)); 
        }
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
    public void setFocus() {
        introPart.setFocus();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IViewPart#saveState(org.eclipse.ui.IMemento)
     */
    public void saveState(IMemento memento) {
        introPart.saveState(memento);
    }

	/**
	 * Sets whether the CoolBar/PerspectiveBar should be visible.
	 * 
	 * @param visible whether the CoolBar/PerspectiveBar should be visible
	 * @since 3.1
	 */
	private void setBarVisibility(final boolean visible) {
		WorkbenchWindow window = (WorkbenchWindow) getSite()
				.getWorkbenchWindow();
		
		boolean layout = false; // don't layout unless things have actually changed
		if (visible) {
			// Restore the last 'saved' state
			boolean coolbarVisible = PrefUtil
					.getInternalPreferenceStore().getBoolean(
							IPreferenceConstants.COOLBAR_VISIBLE);
			boolean persBarVisible = PrefUtil
					.getInternalPreferenceStore().getBoolean(
							IPreferenceConstants.PERSPECTIVEBAR_VISIBLE);
			layout = (coolbarVisible != window.getCoolBarVisible())
 (persBarVisible != window.getPerspectiveBarVisible());
			window.setCoolBarVisible(coolbarVisible);
			window.setPerspectiveBarVisible(persBarVisible);
		} else {
			layout = !window.getCoolBarVisible() || !window.getPerspectiveBarVisible();
			window.setCoolBarVisible(false);
			window.setPerspectiveBarVisible(false);
		}

		if (layout) {
			window.getShell().layout();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2011.java