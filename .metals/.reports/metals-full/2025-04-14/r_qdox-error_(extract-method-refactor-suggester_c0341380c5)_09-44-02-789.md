error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8611.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8611.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8611.java
text:
```scala
i@@f (NewProgressViewer.DEBUG) System.err.println("found other from family " + otherRoot); //$NON-NLS-1$

package org.eclipse.ui.internal.progress;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.ui.actions.ActionFactory;

/**
 * This singleton remembers all JobTreeElements that should be
 * preserved (e.g. because their associated Jobs have the "keep" property set).
 */
class FinishedJobs {

	/*
	 * Interface for notify listeners.
	 */
    static interface KeptJobsListener {
    	/*
    	 * A job to be kept has finished
    	 */
        void finished(JobTreeElement jte);
        /*
         * A kept job has been removed.
         */
        void removed(JobTreeElement jte);
        /*
         * All kept jobs have been been viewed.
         */
        void infoVisited();
    }

    private static FinishedJobs theInstance;
    private static ListenerList listeners = new ListenerList();
    private IJobProgressManagerListener listener;
    private HashSet keptjobinfos = new HashSet();
    private long timeStamp;
    private HashMap finishedTime= new HashMap();

    
    static synchronized FinishedJobs getInstance() {
        if (theInstance == null) theInstance = new FinishedJobs();
        return theInstance;
    }

    private FinishedJobs() {
        listener = new IJobProgressManagerListener() {
            public void addJob(JobInfo info) {
            	checkForDuplicates(info);
            }
            public void addGroup(GroupInfo info) {
            	checkForDuplicates(info);
            }
            public void refreshJobInfo(JobInfo info) {
                checkTasks(info);
            }
            public void refreshGroup(GroupInfo info) {
            }
            public void refreshAll() {
            }
            public void removeJob(JobInfo info) {
                if (keep(info))
                    add(info);
            }
            public void removeGroup(GroupInfo group) {
            }
            public boolean showsDebug() {
                return false;
            }
        };
        ProgressManager.getInstance().addListener(listener);
    }
    
    /**
     * Returns true if JobInfo indicates that it must be kept.
     */
    static boolean keep(JobInfo info) {
        Job job = info.getJob();
        if (job != null) {
            Object prop = job.getProperty(NewProgressViewer.KEEP_PROPERTY);
            if (prop instanceof Boolean) {
                if (((Boolean) prop).booleanValue())
                    return true;
            }
            
            prop = job.getProperty(NewProgressViewer.KEEPONE_PROPERTY);
            if (prop instanceof Boolean) {
                if (((Boolean) prop).booleanValue())
                    return true;
            }
            
            IStatus status = job.getResult();
            if (status != null && status.getSeverity() == IStatus.ERROR)
                return true;
        }
        return false;
    }

    /**
     * Register for notification.
     */
    void addListener(KeptJobsListener l) {
        listeners.add(l);
    }

    /**
     * Deregister for notification.
     */
    void removeListener(KeptJobsListener l) {
        listeners.remove(l);
    }
    
    private void checkForDuplicates(GroupInfo info) {
    	Object[] objects = info.getChildren();
    	for (int i= 0; i < objects.length; i++) {
    		if (objects[i] instanceof JobInfo)
    			checkForDuplicates((JobInfo) objects[i]);
    	}
    }
    
    private void checkForDuplicates(JobTreeElement info) {
        JobTreeElement[] toBeRemoved= findJobsToRemove(info);        
        if (toBeRemoved != null) {
            for (int i = 0; i < toBeRemoved.length; i++) {
				remove(toBeRemoved[i]);
            }
        }    	
    }
    
    /**
     * Add given Job to list of kept jobs.
     */
    private void add(JobInfo info) {
        boolean fire = false;
        
//        JobTreeElement[] toBeRemoved= null;

        synchronized (keptjobinfos) {
            if (!keptjobinfos.contains(info)) {
                keptjobinfos.add(info);
                
                long now= System.currentTimeMillis();
            	finishedTime.put(info, new Long(now));

                Object parent = info.getParent();
                if (parent != null && !keptjobinfos.contains(parent)) {
                	keptjobinfos.add(parent);
                	finishedTime.put(parent, new Long(now));
                }
  
//            	toBeRemoved= findJobsToRemove(info);

                timeStamp++;
                fire = true;
            }
        }
        
//        if (toBeRemoved != null) {
//            for (int i = 0; i < toBeRemoved.length; i++) {
//				remove(null, toBeRemoved[i]);
//            }
//        }

        if (fire) {
            Object l[] = listeners.getListeners();
            for (int i = 0; i < l.length; i++) {
                KeptJobsListener jv = (KeptJobsListener) l[i];
                jv.finished(info);
            }
        }
    }

	static void disposeAction(JobTreeElement jte) {
		if (jte.isJobInfo()) {
			JobInfo ji= (JobInfo) jte;
			Job job= ji.getJob();
			if (job != null) {
				Object prop= job.getProperty(NewProgressViewer.GOTO_PROPERTY);
				if (prop instanceof ActionFactory.IWorkbenchAction)
					((ActionFactory.IWorkbenchAction)prop).dispose();
			}
		}
	}

    private JobTreeElement[] findJobsToRemove(JobTreeElement info) {
    	
    	if (info.isJobInfo()) {
    		Job myJob= null;
    		if (info instanceof JobInfo)
    			myJob= ((JobInfo)info).getJob();
    		else if (info instanceof SubTaskInfo) {
    			JobInfo parent= (JobInfo) ((SubTaskInfo)info).getParent();
    			if (parent != null)
    				myJob= parent.getJob();
    		}
    	
	    	if (myJob != null) {

	            Object prop = myJob.getProperty(NewProgressViewer.KEEPONE_PROPERTY);
	            if (prop instanceof Boolean && ((Boolean) prop).booleanValue()) {
		        	ArrayList found= null;
		        	Object myRoot= getRoot(info);
		        	JobTreeElement[] all;
		    		synchronized (keptjobinfos) {
		    			all= (JobTreeElement[]) keptjobinfos.toArray(new JobTreeElement[keptjobinfos.size()]);
		    		}
				    for (int i= 0; i < all.length; i++) {
			    		JobTreeElement jte= all[i];
			    		Object otherRoot= getRoot(jte);
			    		if (otherRoot != myRoot && jte.isJobInfo()) {
			    			JobInfo ji= (JobInfo) jte;
			    			Job job= ji.getJob();
			    			if (job != null && job.belongsTo(myJob)) {
			    				if (NewProgressViewer.DEBUG) System.err.println("found other from family " + otherRoot);
			    				if (found == null)
			    					found= new ArrayList();
			    				found.add(otherRoot);
			    			}
			    		}
		    		}
			    	if (found != null)
			    		return (JobTreeElement[]) found.toArray(new JobTreeElement[found.size()]);
	            }
	    	}
    	}
    	return null;
    }
    
    private static Object getRoot(JobTreeElement jte) {
    	Object parent;
    	while ((parent= jte.getParent()) != null)
    		jte= (JobTreeElement) parent;
    	return jte;
    }

    private void checkTasks(JobInfo info) {
        if (keep(info)) {
	        TaskInfo tinfo = info.getTaskInfo();
	        if (tinfo != null) {
	            JobTreeElement[] toBeRemoved= null;        
	            boolean fire = false;
	        	JobTreeElement element = (JobTreeElement) tinfo.getParent();
	        	synchronized (keptjobinfos) {
		        	if (element == info && !keptjobinfos.contains(tinfo)) {		        		
		        		toBeRemoved= findJobsToRemove(element);
		                keptjobinfos.add(tinfo);
		                finishedTime.put(tinfo, new Long(System.currentTimeMillis()));
		                timeStamp++;
		            }
	        	}
	        	
	            if (toBeRemoved != null) {
	                for (int i = 0; i < toBeRemoved.length; i++) {
	    				remove(toBeRemoved[i]);
	                }
	            }    	

	            if (fire) {
	                Object l[] = listeners.getListeners();
	                for (int i = 0; i < l.length; i++) {
	                    KeptJobsListener jv = (KeptJobsListener) l[i];
	                    jv.finished(info);
	                }
	            }
	        }
        }
    }

    void remove(JobTreeElement jte) {
        boolean fire = false;
   	
        synchronized (keptjobinfos) {
	        if (keptjobinfos.remove(jte)) {
	        	finishedTime.remove(jte);
	        	disposeAction(jte);
	            if (NewProgressViewer.DEBUG) System.err.println("FinishedJobs: sucessfully removed job"); //$NON-NLS-1$
	
	            // delete all elements that have jte as their direct or indirect parent
	            JobTreeElement jtes[] = (JobTreeElement[]) keptjobinfos.toArray(new JobTreeElement[keptjobinfos.size()]);
	            for (int i = 0; i < jtes.length; i++) {
	            	JobTreeElement parent = (JobTreeElement) jtes[i].getParent();
	                if (parent != null) {
	                	if (parent == jte || parent.getParent() == jte) {
	                		if (keptjobinfos.remove(jtes[i]))
	                			disposeAction(jtes[i]);
	                		finishedTime.remove(jtes[i]);
	                	}
	                }
	            }
	            fire = true;
	        }
        }
        
        if (fire) {
	        // notify listeners
	        Object l[] = listeners.getListeners();
	        for (int i = 0; i < l.length; i++) {
	            KeptJobsListener jv = (KeptJobsListener) l[i];
	            jv.removed(jte);
	        }
        }
    }
    
    void refresh() {
        if (NewProgressViewer.DEBUG) System.err.println("FinishedJobs: refresh"); //$NON-NLS-1$
        Object l[] = listeners.getListeners();
        for (int i = 0; i < l.length; i++) {
            KeptJobsListener jv = (KeptJobsListener) l[i];
            jv.infoVisited();
        }
    }

    JobTreeElement[] getJobInfos() {
        synchronized (keptjobinfos) {
            return (JobTreeElement[]) keptjobinfos.toArray(new JobTreeElement[keptjobinfos.size()]);
        }
    }

    public long getTimeStamp() {
        return timeStamp;
    }
    
    public Date getFinishDate(JobTreeElement jte) {
    	Object o= finishedTime.get(jte);
    	if (o instanceof Long)
    		return new Date(((Long)o).longValue());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8611.java