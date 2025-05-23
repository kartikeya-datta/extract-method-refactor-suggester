error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7764.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7764.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7764.java
text:
```scala
r@@unnableMonitors.remove(job);

/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.progress;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IProgressMonitorWithBlocking;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.core.runtime.jobs.ProgressProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.progress.WorkbenchJob;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.dialogs.EventLoopProgressMonitor;
import org.eclipse.ui.internal.util.BundleUtility;
/**
 * JobProgressManager provides the progress monitor to the job manager and
 * informs any ProgressContentProviders of changes.
 */
public class ProgressManager extends ProgressProvider
		implements
			IProgressService {
	//The property for whether or not the job is running in the
	//dialog.
	private static final String IN_DIALOG = "inDialog";
	public static final QualifiedName PROPERTY_IN_DIALOG =
		new QualifiedName(JobView.PROPERTY_PREFIX,IN_DIALOG);

	private static ProgressManager singleton;
	final private Map jobs = Collections.synchronizedMap(new HashMap());
	final private Map familyListeners = Collections
			.synchronizedMap(new HashMap());
	final Object familyKey = new Object();
	final private Collection listeners = Collections
			.synchronizedList(new ArrayList());
	final Object listenerKey = new Object();
	final ErrorNotificationManager errorManager = new ErrorNotificationManager();
	final private ProgressFeedbackManager feedbackManager = new ProgressFeedbackManager();
	IJobChangeListener changeListener;
	static final String PROGRESS_VIEW_NAME = "org.eclipse.ui.views.ProgressView"; //$NON-NLS-1$
	static final String PROGRESS_FOLDER = "icons/full/progress/"; //$NON-NLS-1$
	
	private static final String PROGRESS_20 = "progress20.gif"; //$NON-NLS-1$
	private static final String PROGRESS_40 = "progress40.gif"; //$NON-NLS-1$
	private static final String PROGRESS_60 = "progress60.gif"; //$NON-NLS-1$
	private static final String PROGRESS_80 = "progress80.gif"; //$NON-NLS-1$
	private static final String PROGRESS_100 = "progress100.gif"; //$NON-NLS-1$
	private static final String SLEEPING_JOB = "sleeping.gif"; //$NON-NLS-1$
	private static final String WAITING_JOB = "waiting.gif"; //$NON-NLS-1$
	private static final String BLOCKED_JOB = "lockedstate.gif"; //$NON-NLS-1$
	private static final String BUSY_OVERLAY = "progressspin.gif"; //$NON-NLS-1$
	private static final String MAXIMIZE = "maximize.gif"; //$NON-NLS-1$
	private static final String MINIMIZE = "minimize.gif"; //$NON-NLS-1$
	private static final String PROGRESS_20_KEY = "PROGRESS_20"; //$NON-NLS-1$
	private static final String PROGRESS_40_KEY = "PROGRESS_40"; //$NON-NLS-1$
	private static final String PROGRESS_60_KEY = "PROGRESS_60"; //$NON-NLS-1$
	private static final String PROGRESS_80_KEY = "PROGRESS_80"; //$NON-NLS-1$
	private static final String PROGRESS_100_KEY = "PROGRESS_100"; //$NON-NLS-1$
	public static final String SLEEPING_JOB_KEY = "SLEEPING_JOB"; //$NON-NLS-1$
	public static final String WAITING_JOB_KEY = "WAITING_JOB"; //$NON-NLS-1$
	public static final String BLOCKED_JOB_KEY = "LOCKED_JOB"; //$NON-NLS-1$
	public static final String BUSY_OVERLAY_KEY = "BUSY_OVERLAY"; //$NON-NLS-1$
	public static final String MINIMIZE_KEY = "MINIMIZE_FLOATING"; //$NON-NLS-1$
	public static final String MAXIMIZE_KEY = "MAXIMIZE_FLOATING"; //$NON-NLS-1$
	//A list of keys for looking up the images in the image registry
	final static String[] keys = new String[]{PROGRESS_20_KEY, PROGRESS_40_KEY,
			PROGRESS_60_KEY, PROGRESS_80_KEY, PROGRESS_100_KEY};
	final Map runnableMonitors = Collections.synchronizedMap(new HashMap());
	/**
	 * Get the progress manager currently in use.
	 * 
	 * @return JobProgressManager
	 */
	public static ProgressManager getInstance() {
		if (singleton == null)
			singleton = new ProgressManager();
		return singleton;
	}
	/**
	 * Return the image for the percent done.
	 * 
	 * @param done.
	 *            int between 0 and 100.
	 * @return
	 */
	public static Image getProgressImageFor(int done) {
		int index = Math.min(4, (done / 20));
		return JFaceResources.getImage(keys[index]);
	}
	/**
	 * The JobMonitor is the inner class that handles the IProgressMonitor
	 * integration with the ProgressMonitor.
	 */
	class JobMonitor implements IProgressMonitorWithBlocking {
		Job job;
		String currentTaskName;
		IProgressMonitorWithBlocking listener;
		/**
		 * Create a monitor on the supplied job.
		 * 
		 * @param newJob
		 */
		JobMonitor(Job newJob) {
			job = newJob;
		}
		/**
		 * Add monitor as another monitor that
		 * 
		 * @param monitor
		 */
		void addProgressListener(IProgressMonitorWithBlocking monitor) {
			listener = monitor;
			JobInfo info = getJobInfo(job);
			TaskInfo currentTask = info.getTaskInfo();
			if (currentTask != null) {
				listener.beginTask(currentTaskName, currentTask.totalWork);
				listener.internalWorked(currentTask.preWork);
			}
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.IProgressMonitor#beginTask(java.lang.String,
		 *      int)
		 */
		public void beginTask(String taskName, int totalWork) {
			JobInfo info = getJobInfo(job);
			info.beginTask(taskName, totalWork);
			refreshJobInfo(info);
			currentTaskName = taskName;
			if (listener != null)
				listener.beginTask(taskName, totalWork);
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.IProgressMonitor#done()
		 */
		public void done() {
			JobInfo info = getJobInfo(job);
			info.clearTaskInfo();
			info.clearChildren();
			runnableMonitors.remove(this);
			if (listener != null)
				listener.done();
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.IProgressMonitor#internalWorked(double)
		 */
		public void internalWorked(double work) {
			JobInfo info = getJobInfo(job);
			if (info.hasTaskInfo()) {
				info.addWork(work);
				refreshJobInfo(info);
			}
			if (listener != null)
				listener.internalWorked(work);
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.IProgressMonitor#isCanceled()
		 */
		public boolean isCanceled() {
			JobInfo info = getJobInfo(job);
			return info.isCanceled();
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.IProgressMonitor#setCanceled(boolean)
		 */
		public void setCanceled(boolean value) {
			JobInfo info = getJobInfo(job);
			//Don't bother cancelling twice
			if (value && !info.isCanceled())
				info.cancel();
			if (listener != null)
				listener.setCanceled(value);
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.IProgressMonitor#setTaskName(java.lang.String)
		 */
		public void setTaskName(String taskName) {
			JobInfo info = getJobInfo(job);
			if (info.hasTaskInfo())
				info.setTaskName(taskName);
			else {
				beginTask(taskName, 100);
				return;
			}
			info.clearChildren();
			refreshJobInfo(info);
			currentTaskName = taskName;
			if (listener != null)
				listener.setTaskName(taskName);
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.IProgressMonitor#subTask(java.lang.String)
		 */
		public void subTask(String name) {
			if (name.length() == 0)
				return;
			JobInfo info = getJobInfo(job);
			info.clearChildren();
			info.addSubTask(name);
			refreshJobInfo(info);
			if (listener != null)
				listener.subTask(name);
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.IProgressMonitor#worked(int)
		 */
		public void worked(int work) {
			internalWorked(work);
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.IProgressMonitorWithBlocking#clearBlocked()
		 */
		public void clearBlocked() {
			JobInfo info = getJobInfo(job);
			info.setBlockedStatus(null);
			refreshJobInfo(info);
			if (listener != null)
				listener.clearBlocked();
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.IProgressMonitorWithBlocking#setBlocked(org.eclipse.core.runtime.IStatus)
		 */
		public void setBlocked(IStatus reason) {
			JobInfo info = getJobInfo(job);
			info.setBlockedStatus(null);
			refreshJobInfo(info);
			if (listener != null)
				listener.setBlocked(reason);
		}
	}
	/**
	 * Create a new instance of the receiver.
	 */
	ProgressManager() {
		Platform.getJobManager().setProgressProvider(this);
		createChangeListener();
		Platform.getJobManager().addJobChangeListener(this.changeListener);
		URL iconsRoot = BundleUtility.find(PlatformUI.PLUGIN_ID,
				ProgressManager.PROGRESS_FOLDER);
		try {
			setUpImage(iconsRoot, PROGRESS_20, PROGRESS_20_KEY);
			setUpImage(iconsRoot, PROGRESS_40, PROGRESS_40_KEY);
			setUpImage(iconsRoot, PROGRESS_60, PROGRESS_60_KEY);
			setUpImage(iconsRoot, PROGRESS_80, PROGRESS_80_KEY);
			setUpImage(iconsRoot, PROGRESS_100, PROGRESS_100_KEY);
			setUpImage(iconsRoot, SLEEPING_JOB, SLEEPING_JOB_KEY);
			setUpImage(iconsRoot, WAITING_JOB, WAITING_JOB_KEY);
			setUpImage(iconsRoot, BLOCKED_JOB, BLOCKED_JOB_KEY);
			setUpImage(iconsRoot, BUSY_OVERLAY, BUSY_OVERLAY_KEY);
			setUpImage(iconsRoot, MAXIMIZE, MAXIMIZE_KEY);
			setUpImage(iconsRoot, MINIMIZE, MINIMIZE_KEY);
			//Let the error manager set up its own icons
			errorManager.setUpImages(iconsRoot);
		} catch (MalformedURLException e) {
			ProgressManagerUtil.logException(e);
		}
	}
	/**
	 * Return the IJobChangeListener registered with the Job manager.
	 * 
	 * @return IJobChangeListener
	 */
	private void createChangeListener() {
		changeListener = new JobChangeAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.core.runtime.jobs.JobChangeAdapter#aboutToRun(org.eclipse.core.runtime.jobs.IJobChangeEvent)
			 */
			public void aboutToRun(IJobChangeEvent event) {
				JobInfo info = getJobInfo(event.getJob());
				refreshJobInfo(info);
				Iterator startListeners = busyListenersForJob(event.getJob())
						.iterator();
				while (startListeners.hasNext()) {
					IJobBusyListener next = (IJobBusyListener) startListeners
							.next();
					next.incrementBusy(event.getJob());
				}
				if (event.getJob().isUser()) {
					boolean inDialog = WorkbenchPlugin
							.getDefault()
							.getPreferenceStore()
							.getBoolean(
									IPreferenceConstants.SHOW_USER_JOBS_IN_DIALOG);
					if (inDialog) {
						showInDialog(null,event.getJob(),false);
						return;
					}
				}
			}
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.core.runtime.jobs.JobChangeAdapter#done(org.eclipse.core.runtime.jobs.IJobChangeEvent)
			 */
			public void done(IJobChangeEvent event) {
				if (!PlatformUI.isWorkbenchRunning())
					return;
				Iterator startListeners = busyListenersForJob(event.getJob())
						.iterator();
				while (startListeners.hasNext()) {
					IJobBusyListener next = (IJobBusyListener) startListeners
							.next();
					next.decrementBusy(event.getJob());
				}
				JobInfo info = getJobInfo(event.getJob());
				if (event.getResult().getSeverity() == IStatus.ERROR) {
					errorManager.addError(event.getResult(), event.getJob()
							.getName());
				}
				jobs.remove(event.getJob());
				//Only refresh if we are showing it
				removeJobInfo(info);
				//If there are no more left then refresh all on the last
				// displayed one
				if (hasNoRegularJobInfos()
						&& !isNonDisplayableJob(event.getJob(), false))
					refreshAll();
			}
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.core.runtime.jobs.JobChangeAdapter#scheduled(org.eclipse.core.runtime.jobs.IJobChangeEvent)
			 */
			public void scheduled(IJobChangeEvent event) {
				if (isNeverDisplayedJob(event.getJob()))
					return;
				if (jobs.containsKey(event.getJob()))
					refreshJobInfo(getJobInfo(event.getJob()));
				else {
					addJobInfo(new JobInfo(event.getJob()));
				}
			}
		};
	}
	/**
	 * Set up the image in the image regsitry.
	 * 
	 * @param iconsRoot
	 * @param fileName
	 * @param key
	 * @throws MalformedURLException
	 */
	private void setUpImage(URL iconsRoot, String fileName, String key)
			throws MalformedURLException {
		JFaceResources.getImageRegistry().put(key,
				ImageDescriptor.createFromURL(new URL(iconsRoot, fileName)));
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.ProgressProvider#createMonitor(org.eclipse.core.runtime.jobs.Job)
	 */
	public IProgressMonitor createMonitor(Job job) {
		return progressFor(job);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.ProgressProvider#getDefaultMonitor()
	 */
	public IProgressMonitor getDefaultMonitor() {
		//only need a default monitor for operations the UI thread
		//and only if there is a display
		Display display;
		if (PlatformUI.isWorkbenchRunning()) {
			display = PlatformUI.getWorkbench().getDisplay();
			if (!display.isDisposed()
					&& (display.getThread() == Thread.currentThread()))
				return new EventLoopProgressMonitor(new NullProgressMonitor());
		}
		return super.getDefaultMonitor();
	}
	/**
	 * Return a monitor for the job. Check if we cached a monitor for this job
	 * previously for a long operation timeout check.
	 * 
	 * @param job
	 * @return IProgressMonitor
	 */
	public JobMonitor progressFor(Job job) {
		if (runnableMonitors.containsKey(job)) {
			return (JobMonitor) runnableMonitors.get(job);
		} else {
			JobMonitor monitor = new JobMonitor(job);
			runnableMonitors.put(job, monitor);
			return monitor;
		}
	}
	/**
	 * Add an IJobProgressManagerListener to listen to the changes.
	 * 
	 * @param listener
	 */
	void addListener(IJobProgressManagerListener listener) {
		synchronized (listenerKey) {
			listeners.add(listener);
		}
	}
	/**
	 * Remove the supplied IJobProgressManagerListener from the list of
	 * listeners.
	 * 
	 * @param listener
	 */
	void removeListener(IJobProgressManagerListener listener) {
		synchronized (listenerKey) {
			listeners.remove(listener);
		}
	}
	/**
	 * Get the JobInfo for the job. If it does not exist create it.
	 * 
	 * @param job
	 * @return
	 */
	JobInfo getJobInfo(Job job) {
		JobInfo info = (JobInfo) jobs.get(job);
		if (info == null) {
			info = new JobInfo(job);
			jobs.put(job, info);
		}
		return info;
	}
	/**
	 * Refresh the IJobProgressManagerListeners as a result of a change in info.
	 * 
	 * @param info
	 */
	public void refreshJobInfo(JobInfo info) {
		GroupInfo group = info.getGroupInfo();
		if (group != null)
			refreshGroup(group);
		synchronized (listenerKey) {
			Iterator iterator = listeners.iterator();
			while (iterator.hasNext()) {
				IJobProgressManagerListener listener = (IJobProgressManagerListener) iterator
						.next();
				if (!isNonDisplayableJob(info.getJob(), listener.showsDebug()))
					listener.refreshJobInfo(info);
			}
		}
	}
	/**
	 * Refresh the IJobProgressManagerListeners as a result of a change in info.
	 * 
	 * @param info
	 */
	public void refreshGroup(GroupInfo info) {
		synchronized (listenerKey) {
			Iterator iterator = listeners.iterator();
			while (iterator.hasNext()) {
				IJobProgressManagerListener listener = (IJobProgressManagerListener) iterator
						.next();
				listener.refreshGroup(info);
			}
		}
	}
	/**
	 * Refresh all the IJobProgressManagerListener as a result of a change in
	 * the whole model.
	 * 
	 * @param info
	 */
	public void refreshAll() {
		synchronized (listenerKey) {
			Iterator iterator = listeners.iterator();
			while (iterator.hasNext()) {
				IJobProgressManagerListener listener = (IJobProgressManagerListener) iterator
						.next();
				listener.refreshAll();
			}
		}
	}
	/**
	 * Refresh the content providers as a result of a deletion of info.
	 * 
	 * @param info
	 *            JobInfo
	 */
	public void removeJobInfo(JobInfo info) {
		synchronized (listenerKey) {
			jobs.remove(info.getJob());
			Iterator iterator = listeners.iterator();
			while (iterator.hasNext()) {
				IJobProgressManagerListener listener = (IJobProgressManagerListener) iterator
						.next();
				if (!isNonDisplayableJob(info.getJob(), listener.showsDebug()))
					listener.removeJob(info);
			}
		}
	}
	/**
	 * Remove the group from the roots and inform the listeners.
	 * 
	 * @param group
	 *            GroupInfo
	 */
	public void removeGroup(GroupInfo group) {
		synchronized (listenerKey) {
			Iterator iterator = listeners.iterator();
			while (iterator.hasNext()) {
				IJobProgressManagerListener listener = (IJobProgressManagerListener) iterator
						.next();
				listener.removeGroup(group);
			}
		}
	}
	/**
	 * Refresh the content providers as a result of an addition of info.
	 * 
	 * @param info
	 */
	public void addJobInfo(JobInfo info) {
		GroupInfo group = info.getGroupInfo();
		if (group != null)
			refreshGroup(group);
		synchronized (listenerKey) {
			jobs.put(info.getJob(), info);
			Iterator iterator = listeners.iterator();
			while (iterator.hasNext()) {
				IJobProgressManagerListener listener = (IJobProgressManagerListener) iterator
						.next();
				if (!isNonDisplayableJob(info.getJob(), listener.showsDebug()))
					listener.addJob(info);
			}
		}
	}
	/**
	 * Refresh the content providers as a result of an addition of info.
	 * 
	 * @param info
	 */
	public void addGroup(GroupInfo info) {
		synchronized (listenerKey) {
			Iterator iterator = listeners.iterator();
			while (iterator.hasNext()) {
				IJobProgressManagerListener listener = (IJobProgressManagerListener) iterator
						.next();
				listener.addGroup(info);
			}
		}
	}
	/**
	 * Return whether or not this job is currently displayable.
	 * 
	 * @param job
	 * @param debug
	 *            If the listener is in debug mode.
	 * @return
	 */
	boolean isNonDisplayableJob(Job job, boolean debug) {
		if (isNeverDisplayedJob(job))
			return true;
		if (debug) //Always display in debug mode
			return false;
		else
			return job.isSystem() || job.getState() == Job.SLEEPING;
	}
	/**
	 * Return whether or not this job is ever displayable.
	 * 
	 * @param job
	 * @return
	 */
	private boolean isNeverDisplayedJob(Job job) {
		return job == null;
	}
	/**
	 * Return the current job infos filtered on debug mode.
	 * 
	 * @param debug
	 * @return
	 */
	public JobInfo[] getJobInfos(boolean debug) {
		synchronized (jobs) {
			Iterator iterator = jobs.keySet().iterator();
			Collection result = new ArrayList();
			while (iterator.hasNext()) {
				Job next = (Job) iterator.next();
				if (!isNonDisplayableJob(next, debug))
					result.add(jobs.get(next));
			}
			JobInfo[] infos = new JobInfo[result.size()];
			result.toArray(infos);
			return infos;
		}
	}
	/**
	 * Return the current root elements filtered on the debug mode.
	 * 
	 * @param debug
	 * @return JobTreeElement[]
	 */
	public JobTreeElement[] getRootElements(boolean debug) {
		synchronized (jobs) {
			Iterator iterator = jobs.keySet().iterator();
			Set result = new HashSet();
			while (iterator.hasNext()) {
				Job next = (Job) iterator.next();
				if (!isNonDisplayableJob(next, debug)) {
					JobInfo jobInfo = (JobInfo) jobs.get(next);
					GroupInfo group = jobInfo.getGroupInfo();
					if (group == null)
						result.add(jobInfo);
					else
						result.add(group);
				}
			}
			JobTreeElement[] infos = new JobTreeElement[result.size()];
			result.toArray(infos);
			return infos;
		}
	}
	/**
	 * Return whether or not there are any jobs being displayed.
	 * 
	 * @return boolean
	 */
	public boolean hasJobInfos() {
		synchronized (jobs) {
			Iterator iterator = jobs.keySet().iterator();
			while (iterator.hasNext()) {
				return true;
			}
			return false;
		}
	}
	/**
	 * Return true if there are no jobs or they are all debug.
	 * 
	 * @return boolean
	 */
	private boolean hasNoRegularJobInfos() {
		synchronized (jobs) {
			Iterator iterator = jobs.keySet().iterator();
			while (iterator.hasNext()) {
				Job next = (Job) iterator.next();
				if (!isNonDisplayableJob(next, false))
					return false;
			}
			return true;
		}
	}
	/**
	 * Returns the image descriptor with the given relative path.
	 * 
	 * @param source
	 * @return Image
	 */
	Image getImage(ImageData source) {
		ImageData mask = source.getTransparencyMask();
		return new Image(null, source, mask);
	}
	/**
	 * Returns the image descriptor with the given relative path.
	 * 
	 * @param fileSystemPath
	 *            The URL for the file system to the image.
	 * @param loader -
	 *            the loader used to get this data
	 * @return ImageData[]
	 */
	ImageData[] getImageData(URL fileSystemPath, ImageLoader loader) {
		try {
			InputStream stream = fileSystemPath.openStream();
			ImageData[] result = loader.load(stream);
			stream.close();
			return result;
		} catch (FileNotFoundException exception) {
			ProgressManagerUtil.logException(exception);
			return null;
		} catch (IOException exception) {
			ProgressManagerUtil.logException(exception);
			return null;
		}
	}
	/**
	 * Block the current thread until UIJob is served. The message is used to
	 * announce to the user a pending UI Job.
	 * 
	 * Note: This is experimental API and subject to change at any time.
	 * 
	 * @param job
	 * @param message
	 * @return IStatus
	 * @since 3.0
	 */
	public IStatus requestInUI(UIJob job, String message) {
		return feedbackManager.requestInUI(job, message);
	}
	/**
	 * Return the ProgressFeedbackManager for the receiver.
	 * 
	 * @return ProgressFeedbackManager
	 */
	ProgressFeedbackManager getFeedbackManager() {
		return feedbackManager;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.progress.IProgressService#busyCursorWhile(org.eclipse.jface.operation.IRunnableWithProgress)
	 */
	public void busyCursorWhile(final IRunnableWithProgress runnable)
			throws InvocationTargetException, InterruptedException {
		final ProgressMonitorJobsDialog dialog = new ProgressMonitorJobsDialog(
				null);
		dialog.setOpenOnRun(false);
		//create the job that will open the dialog after a delay
		scheduleProgressMonitorJob(dialog);
		final Display display = PlatformUI.getWorkbench().getDisplay();
		if (display == null)
			return;
		final InvocationTargetException[] invokes = new InvocationTargetException[1];
		final InterruptedException[] interrupt = new InterruptedException[1];
		//show a busy cursor until the dialog opens
		BusyIndicator.showWhile(display, new Runnable() {
			public void run() {
				try {
					dialog.setOpenOnRun(false);
					dialog.run(true, true, runnable);
				} catch (InvocationTargetException e) {
					invokes[0] = e;
				} catch (InterruptedException e) {
					interrupt[0] = e;
				}
			}
		});
		if (invokes[0] != null)
			throw invokes[0];
		if (interrupt[0] != null)
			throw interrupt[0];
	}
	/**
	 * Schedule the job that will open the progress monitor dialog
	 */
	private void scheduleProgressMonitorJob(
			final ProgressMonitorJobsDialog dialog) {
		final WorkbenchJob updateJob = new WorkbenchJob(ProgressMessages
				.getString("ProgressManager.openJobName")) {//$NON-NLS-1$
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
			 */
			public IStatus runInUIThread(IProgressMonitor monitor) {
				Display currentDisplay = getDisplay();
				if (currentDisplay == null || currentDisplay.isDisposed())
					return Status.CANCEL_STATUS;
				//If there is a modal shell open then wait
				Shell[] shells = currentDisplay.getShells();
				int modal = SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL
 SWT.PRIMARY_MODAL;
				for (int i = 0; i < shells.length; i++) {
					//Do not stop for shells that will not block the user.
					if (shells[i].isVisible()) {
						int style = shells[i].getStyle();
						if ((style & modal) != 0) {
							//try again in a few seconds
							schedule(LONG_OPERATION_MILLISECONDS);
							return Status.CANCEL_STATUS;
						}
					}
				}
				dialog.open();
				return Status.OK_STATUS;
			}
		};
		updateJob.schedule(LONG_OPERATION_MILLISECONDS);
	}
	/**
	 * Shutdown the receiver.
	 */
	public void shutdown() {
		synchronized (listenerKey) {
			this.listeners.clear();
		}
		Platform.getJobManager().setProgressProvider(null);
		Platform.getJobManager().removeJobChangeListener(this.changeListener);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.ProgressProvider#createProgressGroup()
	 */
	public IProgressMonitor createProgressGroup() {
		return new GroupInfo();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.ProgressProvider#createMonitor(org.eclipse.core.runtime.jobs.Job,
	 *      org.eclipse.core.runtime.IProgressMonitor, int)
	 */
	public IProgressMonitor createMonitor(Job job, IProgressMonitor group,
			int ticks) {
		JobMonitor monitor = progressFor(job);
		if (group instanceof GroupInfo) {
			GroupInfo groupInfo = (GroupInfo) group;
			JobInfo jobInfo = getJobInfo(job);
			jobInfo.setGroupInfo(groupInfo);
			jobInfo.setTicks(ticks);
			groupInfo.addJobInfo(jobInfo);
		}
		return monitor;
	}
	/**
	 * Add the listener to the family.
	 * 
	 * @param family
	 * @param listener
	 */
	void addListenerToFamily(Object family, IJobBusyListener listener) {
		synchronized (familyKey) {
			Collection currentListeners = new HashSet();
			if (familyListeners.containsKey(family))
				currentListeners = (Collection) familyListeners.get(family);
			currentListeners.add(listener);
			familyListeners.put(family, currentListeners);
		}
	}
	/**
	 * Remove the listener from all families.
	 * 
	 * @param family
	 * @param listener
	 */
	void removeListener(IJobBusyListener listener) {
		synchronized (familyKey) {
			Collection keysToRemove = new HashSet();
			Iterator families = familyListeners.keySet().iterator();
			while (families.hasNext()) {
				Object next = families.next();
				Collection currentListeners = (Collection) familyListeners
						.get(next);
				if (currentListeners.contains(listener))
					currentListeners.remove(listener);
				if (currentListeners.isEmpty())
					keysToRemove.add(next);
				else
					familyListeners.put(next, currentListeners);
			}
			//Remove any empty listeners
			Iterator keysIterator = keysToRemove.iterator();
			while (keysIterator.hasNext()) {
				familyListeners.remove(keysIterator.next());
			}
		}
	}
	/**
	 * Return the listeners for the job.
	 * 
	 * @param job
	 * @return Collection of IJobBusyListener
	 */
	private Collection busyListenersForJob(Job job) {
		if (job.isSystem())
			return new HashSet();
		synchronized (familyKey) {
			Collection returnValue = new HashSet();
			Iterator families = familyListeners.keySet().iterator();
			while (families.hasNext()) {
				Object next = families.next();
				if (job.belongsTo(next)) {
					Collection currentListeners = (Collection) familyListeners
							.get(next);
					returnValue.addAll(currentListeners);
				}
			}
			return returnValue;
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.progress.IProgressService#showInDialog(org.eclipse.swt.widgets.Shell, org.eclipse.core.runtime.jobs.Job, boolean)
	 */
	public void showInDialog(Shell shell, Job job, boolean runImmediately) {
		ProgressMonitorFocusJobDialog dialog = new ProgressMonitorFocusJobDialog(
				shell);
		dialog.setJobAndOpen(job, runImmediately);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7764.java