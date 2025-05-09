error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7131.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7131.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7131.java
text:
```scala
P@@rogressManager.getInstance().addListenerToFamily(family,this);

/**********************************************************************
 * Copyright (c) 2003,2004 IBM Corporation and others. All rights reserved.   This
 * program and the accompanying materials are made available under the terms of
 * the Common Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: 
 * IBM - Initial API and implementation
 **********************************************************************/
package org.eclipse.ui.internal.progress;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.IPropertyChangeListener;

import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.WorkbenchPart;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.progress.WorkbenchJob;

import org.eclipse.ui.internal.PartSite;
/**
 * The WorkbenchSiteProgressService is the concrete implementation of the
 * WorkbenchSiteProgressService used by the workbench components.
 */
public class WorkbenchSiteProgressService implements IWorkbenchSiteProgressService, IJobBusyListener {
	PartSite site;
	private Collection busyJobs = Collections.synchronizedSet(new HashSet());
	private Object busyLock = new Object();
	IJobChangeListener listener;
	IPropertyChangeListener[] changeListeners = new IPropertyChangeListener[0];
	private Cursor waitCursor;
	private SiteUpdateJob updateJob; 

	
	private class SiteUpdateJob extends WorkbenchJob {
		private boolean busy;
		private boolean useWaitCursor;
		Object lock = new Object();
		/**
		 * Set whether we are updating with the wait or busy cursor.
		 * 
		 * @param cursorState
		 */
		void setBusy(boolean cursorState) {
			synchronized (lock) {
				busy = cursorState;
			}
		}
		private SiteUpdateJob() {
			super(ProgressMessages.getString("WorkbenchSiteProgressService.CursorJob"));//$NON-NLS-1$
		}
		/**
		 * Get the wait cursor. Initialize it if required.
		 * 
		 * @return Cursor
		 */
		private Cursor getWaitCursor(Display display) {
			if (waitCursor == null) {
				waitCursor = new Cursor(display, SWT.CURSOR_APPSTARTING);
			}
			return waitCursor;
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
		 */
		public IStatus runInUIThread(IProgressMonitor monitor) {
			Control control = site.getPane().getControl();
			if (control == null || control.isDisposed())
				return Status.CANCEL_STATUS;
			synchronized (lock) {
				//Update cursors if we are doing that
				if (useWaitCursor) {
					Cursor cursor = null;
					if (busy)
						cursor = getWaitCursor(control.getDisplay());
					control.setCursor(cursor);
				}
				
				site.getPane().setBusy(busy);
				
				IWorkbenchPart part = site.getPart();
				if (part instanceof WorkbenchPart)
					((WorkbenchPart) part).showBusy(busy);
			}
			return Status.OK_STATUS;
		}
		void clearCursors() {
			if (waitCursor != null) {
				waitCursor.dispose();
				waitCursor = null;
			}
		}
	}
	/**
	 * Create a new instance of the receiver with a site of partSite
	 * 
	 * @param partSite
	 *            PartSite.
	 */
	public WorkbenchSiteProgressService(final PartSite partSite) {
		site = partSite;
		
	}
	public void dispose() {
		
		if(updateJob != null)
			updateJob.cancel();
		
		if (waitCursor == null)
			return;
		waitCursor.dispose();
		waitCursor = null;
		
		ProgressManager.getInstance().removeListener(this);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.progress.IProgressService#requestInUI(org.eclipse.ui.progress.UIJob,
	 *      java.lang.String)
	 */
	public IStatus requestInUI(UIJob job, String message) {
		return site.getWorkbenchWindow().getWorkbench().getProgressService().requestInUI(job,
				message);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.progress.IProgressService#busyCursorWhile(org.eclipse.jface.operation.IRunnableWithProgress)
	 */
	public void busyCursorWhile(IRunnableWithProgress runnable) throws InvocationTargetException,
			InterruptedException {
		site.getWorkbenchWindow().getWorkbench().getProgressService().busyCursorWhile(runnable);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.progress.IWorkbenchSiteProgressService#schedule(org.eclipse.core.runtime.jobs.Job,
	 *      long, boolean)
	 */
	public void schedule(Job job, long delay, boolean useHalfBusyCursor) {
		job.addJobChangeListener(getJobChangeListener(job, useHalfBusyCursor));
		job.schedule(delay);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.progress.IWorkbenchSiteProgressService#schedule(org.eclipse.core.runtime.jobs.Job,
	 *      int)
	 */
	public void schedule(Job job, long delay) {
		schedule(job, delay, false);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.progress.IWorkbenchSiteProgressService#schedule(org.eclipse.core.runtime.jobs.Job)
	 */
	public void schedule(Job job) {
		schedule(job, 0L, false);
	}
	
	
    /* (non-Javadoc)
     * @see org.eclipse.ui.progress.IWorkbenchSiteProgressService#showBusyForFamily(java.lang.Object)
     */
    public void showBusyForFamily(Object family) {
      //ProgressManager.getInstance().addListenerToFamily(family,this);

    }
	
	/**
	 * Get the job change listener for this site.
	 * 
	 * @param job
	 * @param useHalfBusyCursor
	 * @return IJobChangeListener
	 */
	public IJobChangeListener getJobChangeListener(final Job job, boolean useHalfBusyCursor) {
		if (listener == null) {
			updateJob = new SiteUpdateJob();
			updateJob.setSystem(true);
			updateJob.useWaitCursor = useHalfBusyCursor;
			listener = new JobChangeAdapter() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.core.runtime.jobs.JobChangeAdapter#aboutToRun(org.eclipse.core.runtime.jobs.IJobChangeEvent)
				 */
				public void aboutToRun(IJobChangeEvent event) {
				    
				   incrementBusy(event.getJob());
				   
				}
				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.core.runtime.jobs.JobChangeAdapter#done(org.eclipse.core.runtime.jobs.IJobChangeEvent)
				 */
				public void done(IJobChangeEvent event) {
					decrementBusy(event.getJob());
				}
			};
		}
		return listener;
	}
	
	
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.progress.IJobBusyListener#decrementBusy(org.eclipse.core.runtime.jobs.Job)
     */
    public void decrementBusy(Job job) {
        
        synchronized(busyLock){
            busyJobs.remove(job);
           
            if(busyJobs.size() > 0)
                return;           
        }
        
        if(PlatformUI.isWorkbenchRunning()){
			updateJob.setBusy(false);
			updateJob.schedule(100);
		}
		else
			updateJob.cancel();

    }
    
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.progress.IJobBusyListener#incrementBusy(org.eclipse.core.runtime.jobs.Job)
     */
    public void incrementBusy(Job job) {
        
        synchronized(busyLock){
            busyJobs.add(job);
            //If it is greater than one we already set busy
            if(busyJobs.size() > 1)
                return;           
        }
        
        if(PlatformUI.isWorkbenchRunning()){
			updateJob.setBusy(true);
			updateJob.schedule(100);
		}
		else
			updateJob.cancel();

    }
    
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.progress.IWorkbenchSiteProgressService#warnOfContentChange()
     */
    public void warnOfContentChange() {
        site.getPane().showHighlight();

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7131.java