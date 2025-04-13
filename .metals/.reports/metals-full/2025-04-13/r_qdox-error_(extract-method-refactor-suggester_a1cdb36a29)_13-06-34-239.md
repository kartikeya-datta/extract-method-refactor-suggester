error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8826.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8826.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8826.java
text:
```scala
W@@orkbenchPlugin.log("Could not open intro", new Status(IStatus.ERROR, WorkbenchPlugin.PI_WORKBENCH, IStatus.ERROR, "Could not open intro", e));	//$NON-NLS-1$ //$NON-NLS-2$

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.intro.IIntroConstants;
import org.eclipse.ui.internal.intro.IntroMessages;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.intro.IIntroPart;

/**
 * Workbench implementation of the IIntroManager interface.
 * 
 * @since 3.0
 */
public class WorkbenchIntroManager implements IIntroManager {

    private final Workbench workbench;

    /**
     * Create a new instance of the receiver.
     * 
     * @param workbench the workbench instance
     */
    WorkbenchIntroManager(Workbench workbench) {
        this.workbench = workbench;
    }

    /**
	 * The currently active introPart in this workspace, <code>null</code> if none.
	 */
	private IIntroPart introPart;
	
    
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#closeIntro(org.eclipse.ui.intro.IIntroPart)
	 */
	public boolean closeIntro(IIntroPart part) {
		if (introPart == null || !introPart.equals(part))
			return false;
		introPart = null;

        IViewPart introView = getViewIntroAdapterPart();
		if (introView != null) {
			getViewIntroAdapterPart().getSite().getPage().hideView(introView);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#showIntro(org.eclipse.ui.IWorkbenchWindow)
	 */
	public IIntroPart showIntro(IWorkbenchWindow preferredWindow, boolean standby) {
	    if (preferredWindow == null)
	        preferredWindow = this.workbench.getActiveWorkbenchWindow();
	    
	    if (preferredWindow == null)
	        return null;
	    
		if (getViewIntroAdapterPart() == null) {
			createIntro((WorkbenchWindow) preferredWindow);
		}
		else {
			try {
				ViewIntroAdapterPart viewPart = getViewIntroAdapterPart();				
				WorkbenchPage page = (WorkbenchPage) viewPart.getSite().getPage();
				WorkbenchWindow window = (WorkbenchWindow) page.getWorkbenchWindow();
				if (!window.equals(preferredWindow)) {
					window.getShell().setActive();
				}
				
				page.showView(IIntroConstants.INTRO_VIEW_ID);
//				IPerspectiveDescriptor [] perspDescriptors = page.getOpenedPerspectives();
//				for (int i = 0; i < perspDescriptors.length; i++) {
//					IPerspectiveDescriptor descriptor = perspDescriptors[i];
//					if (page.findPerspective(descriptor).containsView(viewPart)) {
//						if (!page.getPerspective().equals(descriptor)) {
//							page.setPerspective(descriptor);
//						}
//						break;
//					}
//				}
//				
//				page.getWorkbenchWindow().getShell().setActive();
//				page.showView(IIntroConstants.INTRO_VIEW_ID);
			} catch (PartInitException e) {
				WorkbenchPlugin.log(IntroMessages.getString("Intro.could_not_show_part"), new Status(IStatus.ERROR, WorkbenchPlugin.PI_WORKBENCH, IStatus.ERROR, IntroMessages.getString("Intro.could_not_show_part"), e));	//$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		setIntroStandby(introPart, standby);
		return introPart;
	}

	/**	 
	 * @param window the window to test
	 * @return whether the intro exists in the given window
	 * @since 3.0
	 */
	/*package*/ boolean isIntroInWindow(IWorkbenchWindow testWindow) {
		ViewIntroAdapterPart viewPart = getViewIntroAdapterPart();	
		if (viewPart == null)
			return false;
		
		WorkbenchPage page = (WorkbenchPage) viewPart.getSite().getPage();
		WorkbenchWindow window = (WorkbenchWindow) page.getWorkbenchWindow();
		if (window.equals(testWindow)) {
			return true;
		}
		return false;
	}
	
	/**
     * Create a new Intro area (a view, currently) in the provided window.  If there is no intro
     * descriptor for this workbench then no work is done.
     *
	 * @param preferredWindow the window to create the intro in.
	 * @since 3.0
	 */
	private void createIntro(WorkbenchWindow preferredWindow) {
		if (this.workbench.getIntroDescriptor() == null)
			return;
		
		WorkbenchPage workbenchPage = preferredWindow.getActiveWorkbenchPage();
		try {
			workbenchPage.showView(IIntroConstants.INTRO_VIEW_ID);			
		} catch (PartInitException e) {
			WorkbenchPlugin.log(IntroMessages.getString("Intro.could_not_create_part"), new Status(IStatus.ERROR, WorkbenchPlugin.PI_WORKBENCH, IStatus.ERROR, IntroMessages.getString("Intro.could_not_create_part"), e)); //$NON-NLS-1$ //$NON-NLS-2$
		}		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#setIntroStandby(org.eclipse.ui.intro.IIntroPart, boolean)
	 */
	public void setIntroStandby(IIntroPart part, boolean standby) {
		if (introPart == null || !introPart.equals(part))
			return;
		
		PartPane pane = ((PartSite)getViewIntroAdapterPart().getSite()).getPane();
		if (standby == !pane.isZoomed()) {
		    // the zoom state is already correct - just update the part's state.
		    getViewIntroAdapterPart().setStandby(standby);
			return;
		}
		
		((WorkbenchPage)getViewIntroAdapterPart().getSite().getPage()).toggleZoom(pane.getPartReference());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#isIntroStandby(org.eclipse.ui.intro.IIntroPart)
	 */
	public boolean isIntroStandby(IIntroPart part) {
		if (introPart == null || !introPart.equals(part))
			return false;

		return !((PartSite)getViewIntroAdapterPart().getSite()).getPane().isZoomed();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#findIntro()
	 */
	public IIntroPart getIntro() {
		return introPart;
	}
	
	/** 
	 * @return the <code>ViewIntroAdapterPart</code> for this workbench, <code>null</code> if it 
     * cannot be found.
	 * @since 3.0
	 */
	/*package*/ ViewIntroAdapterPart getViewIntroAdapterPart() {
		IWorkbenchWindow [] windows = this.workbench.getWorkbenchWindows();
		for (int i = 0; i < windows.length; i++) {
			IWorkbenchWindow window = windows[i];
			WorkbenchPage page = (WorkbenchPage) window.getActivePage();
			if (page == null) {
				continue;
			}
			IPerspectiveDescriptor [] perspDescs = page.getOpenedPerspectives();
			for (int j = 0; j < perspDescs.length; j++) {
				IPerspectiveDescriptor descriptor = perspDescs[j];
				IViewReference reference = page.findPerspective(descriptor).findView(IIntroConstants.INTRO_VIEW_ID);
				if (reference != null) {
					ViewIntroAdapterPart part = (ViewIntroAdapterPart) reference.getView(false);
					if (part != null)
						return part;
				}
			}
		}
		return null;
	}
		
	/**
	 * @return a new IIntroPart.  This has the side effect of setting the introPart field to the new
	 * value.
	 * @since 3.0
	 */
	/*package*/ IIntroPart createNewIntroPart() throws CoreException {	
		return introPart = workbench.getIntroDescriptor() == null ? null : workbench.getIntroDescriptor().createIntro();
	}		
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#hasIntro()
	 */
	public boolean hasIntro() {
		return workbench.getIntroDescriptor() != null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8826.java