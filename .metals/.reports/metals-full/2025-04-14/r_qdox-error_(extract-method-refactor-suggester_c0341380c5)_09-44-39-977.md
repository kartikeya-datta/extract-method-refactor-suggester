error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1782.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1782.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1782.java
text:
```scala
t@@oolbar.getParent().layout(); // must layout

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
package org.eclipse.ui.internal.progress;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.util.ImageSupport;
import org.eclipse.ui.progress.IProgressConstants;

/**
 * The ProgressAnimationItem is the animation items that uses
 * the progress bar.
 */
public class ProgressAnimationItem extends AnimationItem implements FinishedJobs.KeptJobsListener {

	ProgressBar bar;
	MouseListener mouseListener;
	Composite top;
	ToolBar toolbar;
	ToolItem toolButton;
	ProgressRegion progressRegion;
    Image noneImage, okImage, errorImage;
    boolean animationRunning;
    JobInfo lastJobInfo;
	
	/**
	 * Create an instance of the receiver in the supplied region.
	 * 
	 * @param region. The ProgressRegion that contains the receiver.
	 */
	ProgressAnimationItem(ProgressRegion region) {
		super(region.workbenchWindow);
		
	    FinishedJobs.getInstance().addListener(this);
		
		progressRegion= region;
		mouseListener = new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				doAction();
			}
		};
	}
		
	void doAction() {
		
		JobTreeElement[] jobTreeElements= FinishedJobs.getInstance().getJobInfos();
		// search from end (youngest)
		for (int i= jobTreeElements.length-1; i >= 0; i--) {
			if (jobTreeElements[i] instanceof JobInfo) {
				JobInfo ji= (JobInfo) jobTreeElements[i];
				Job job= ji.getJob();
				if (job != null) {
					
				    IStatus status= job.getResult();
				    if (status != null && status.getSeverity() == IStatus.ERROR) {
						String title= ProgressMessages.getString("NewProgressView.errorDialogTitle"); //$NON-NLS-1$
						String msg= ProgressMessages.getString("NewProgressView.errorDialogMessage"); //$NON-NLS-1$
						ErrorDialog.openError(toolbar.getShell(), title, msg, status);
						JobTreeElement topElement= (JobTreeElement) ji.getParent();
						if (topElement == null)
							topElement= ji;
						FinishedJobs.getInstance().remove(topElement);
				        return;
				    }
				
					IAction action= null;
					Object property= job.getProperty(IProgressConstants.ACTION_PROPERTY);
					if (property instanceof IAction)
						action= (IAction) property;
					if (action != null && action.isEnabled()) {
						action.run();
						JobTreeElement topElement= (JobTreeElement) ji.getParent();
						if (topElement == null)
							topElement= ji;
						FinishedJobs.getInstance().remove(topElement);
						return;
					}
				}
			}
		}
		
		progressRegion.processDoubleClick();
		refresh();		
	}
	
	private IAction getAction(Job job) {
		Object property= job.getProperty(IProgressConstants.ACTION_PROPERTY);
		if (property instanceof IAction)
			return (IAction) property;
		return null;
	}

	private void refresh() {
		
		// Abort the refresh if we are in the process of shutting down
		if (!PlatformUI.isWorkbenchRunning())
			return;
		
		if (toolbar == null || toolbar.isDisposed())
			return;
		
		lastJobInfo= null;

		JobTreeElement[] jobTreeElements= FinishedJobs.getInstance().getJobInfos();
		// search from end (youngest)
		for (int i= jobTreeElements.length-1; i >= 0; i--) {
			if (jobTreeElements[i] instanceof JobInfo) {
				JobInfo ji= (JobInfo) jobTreeElements[i];
				lastJobInfo= ji;
				Job job= ji.getJob();
				if (job != null) {
				    IStatus status= job.getResult();
				    if (status != null && status.getSeverity() == IStatus.ERROR) {
				    		// green arrow with error overlay
				    		initButton(errorImage, ProgressMessages.format("ProgressAnimationItem.error", new Object[] { job.getName() } )); //$NON-NLS-1$
				        return;
				    }
					IAction action= getAction(job);
					if (action != null && action.isEnabled()) {
						// green arrow with exclamation mark
						String tt= action.getToolTipText();
						if (tt == null || tt.trim().length() == 0)
							tt= ProgressMessages.format("ProgressAnimationItem.ok", new Object[] { job.getName() } ); //$NON-NLS-1$
						initButton(okImage, tt);
						return;			
					}
					// just the green arrow
					initButton(noneImage, ProgressMessages.getString("ProgressAnimationItem.tasks")); //$NON-NLS-1$
					return;
				}
			}
		}
		
		if (animationRunning) {			
			initButton(noneImage, ProgressMessages.getString("ProgressAnimationItem.tasks")); //$NON-NLS-1$
    			return;
		}
		
		// if nothing found hide tool item
   		toolbar.setVisible(false);
	}
	
	private void initButton(Image im, String tt) {
		toolButton.setImage(im);
		toolButton.setToolTipText(tt);
		toolbar.setVisible(true);
		toolbar.redraw();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.progress.AnimationItem#createAnimationItem(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createAnimationItem(Composite parent) {
	    	    
	    if (okImage == null) {
	        Display display= parent.getDisplay();	        
	        noneImage= ImageSupport.getImageDescriptor("icons/full/progress/progress_none.gif").createImage(display); //$NON-NLS-1$
	        okImage= ImageSupport.getImageDescriptor("icons/full/progress/progress_ok.gif").createImage(display); //$NON-NLS-1$
	        errorImage= ImageSupport.getImageDescriptor("icons/full/progress/progress_error.gif").createImage(display); //$NON-NLS-1$
	    }
		
		top = new Composite(parent, SWT.NULL);
		top.addDisposeListener(new DisposeListener() {
		    public void widgetDisposed(DisposeEvent e) {
        	    FinishedJobs.getInstance().removeListener(ProgressAnimationItem.this);
            noneImage.dispose();
    	   		okImage.dispose();
     	    errorImage.dispose();
           }
		});
		
		boolean isCarbon = "carbon".equals(SWT.getPlatform()); //$NON-NLS-1$
		
		GridLayout gl= new GridLayout();
		gl.numColumns= isCarbon ? 3 : 2;
		gl.marginHeight= 0;
		gl.marginWidth= 0;
		gl.horizontalSpacing= 2;
		top.setLayout(gl);
		
		bar = new ProgressBar(top, SWT.HORIZONTAL | SWT.INDETERMINATE);
		bar.setVisible(false);
		bar.addMouseListener(mouseListener);
		GridData gd= new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint= 12;
		bar.setLayoutData(gd);
		
		toolbar= new ToolBar(top, SWT.FLAT);
		toolbar.setVisible(false);
		if ("gtk".equals(SWT.getPlatform())) {	//$NON-NLS-1$
			// workaround for an SWT problem; see #62883
			gd= new GridData(GridData.FILL_VERTICAL);
			gd.widthHint= 23;
			toolbar.setLayoutData(gd);
		}
		
		toolButton= new ToolItem(toolbar, SWT.NONE);
		toolButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	doAction();
            }
        });
				
		if (isCarbon) // prevent that Mac growbox overlaps with toolbar item
		    new Label(top, SWT.NONE).setLayoutData(new GridData(4, 4));

		refresh();

		return top;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.progress.AnimationItem#getControl()
	 */
	public Control getControl() {
		return top;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.progress.AnimationItem#animationDone()
	 */
	void animationDone() {
		super.animationDone();
		animationRunning= false;
		if (bar.isDisposed())
			return;
		bar.setVisible(false);
		refresh();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.progress.AnimationItem#animationStart()
	 */
	void animationStart() {
		super.animationStart();
		animationRunning= true;
		if (bar.isDisposed())
			return;
		bar.setVisible(true);
		refresh();
	}
	
    public void removed(JobTreeElement info) {
	    final Display display= Display.getDefault();
	    display.asyncExec(new Runnable() {
	        public void run() {
	        	refresh();
	        }
	    });
    }
    
    public void finished(final JobTreeElement jte) {
	    final Display display= Display.getDefault();
	    display.asyncExec(new Runnable() {
	        public void run() {
	        	refresh();
	        }
	    });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1782.java