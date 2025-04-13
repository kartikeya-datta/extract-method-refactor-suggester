error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9057.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9057.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9057.java
text:
```scala
P@@rogressManager.getInstance().refreshGroup(this);

/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Michael Fraenkel <fraenkel@us.ibm.com> - Fix for bug 60698 -
 *     [Progress] ConcurrentModificationException in NewProgressViewer.
 *******************************************************************************/
package org.eclipse.ui.internal.progress;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.util.NLS;

/**
 * The GroupInfo is the object used to display group properties.
 */

class GroupInfo extends JobTreeElement implements IProgressMonitor {

    private List infos = new ArrayList();

    private Object lock = new Object();

    private String taskName;

    boolean isActive = false;

    double total = -1;

    double currentWork;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.progress.JobTreeElement#getParent()
     */
    Object getParent() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.progress.JobTreeElement#hasChildren()
     */
    boolean hasChildren() {
        synchronized (lock) {
            return !infos.isEmpty();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.progress.JobTreeElement#getChildren()
     */
    Object[] getChildren() {
        synchronized (lock) {
            return infos.toArray();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.progress.JobTreeElement#getDisplayString()
     */
    String getDisplayString() {
        if (total < 0) {
			return taskName;
		}
        String[] messageValues = new String[2];
        messageValues[0] = taskName;
        messageValues[1] = String.valueOf(getPercentDone());
        return NLS.bind(ProgressMessages.JobInfo_NoTaskNameDoneMessage, messageValues); 

    }

    /**
     * Return an integer representing the amount of work completed.
     * 
     * @return int
     */
    int getPercentDone() {
        return (int) (currentWork * 100 / total);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.progress.JobTreeElement#isJobInfo()
     */
    boolean isJobInfo() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object arg0) {
        return getDisplayString().compareTo(
                ((JobTreeElement) arg0).getDisplayString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IProgressMonitor#beginTask(java.lang.String,
     *      int)
     */
    public void beginTask(String name, int totalWork) {
        taskName = name;
        total = totalWork;
        synchronized (lock) {
            isActive = true;
        }
        ProgressManager.getInstance().addGroup(this);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IProgressMonitor#done()
     */
    public void done() {
        synchronized (lock) {
            isActive = false;
        }
        ProgressManager.getInstance().removeGroup(this);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IProgressMonitor#internalWorked(double)
     */
    public void internalWorked(double work) {
        synchronized (lock) {
            currentWork += work;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IProgressMonitor#isCanceled()
     */
    public boolean isCanceled() {
        //Just a group so no cancel state
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IProgressMonitor#setCanceled(boolean)
     */
    public void setCanceled(boolean value) {
        cancel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IProgressMonitor#setTaskName(java.lang.String)
     */
    public void setTaskName(String name) {
        synchronized (this) {
            isActive = true;
        }
        taskName = name;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IProgressMonitor#subTask(java.lang.String)
     */
    public void subTask(String name) {
        //Not interesting for this monitor
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IProgressMonitor#worked(int)
     */
    public void worked(int work) {
        internalWorked(work);
    }

    /**
     * Remove the job from the list of jobs.
     * 
     * @param job
     */
    void removeJobInfo(final JobInfo job) {
        synchronized (lock) {
            infos.remove(job);
            if (infos.isEmpty()) {
                done();
            }
        }
    }

    /**
     * Remove the job from the list of jobs.
     * 
     * @param job
     */
    void addJobInfo(final JobInfo job) {
        synchronized (lock) {
            infos.add(job);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.progress.JobTreeElement#isActive()
     */
    boolean isActive() {
        return isActive;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.progress.JobTreeElement#cancel()
     */
    public void cancel() {
        Object[] jobInfos = getChildren();
        for (int i = 0; i < jobInfos.length; i++) {
            ((JobInfo) jobInfos[i]).cancel();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.progress.JobTreeElement#isCancellable()
     */
    public boolean isCancellable() {
        return true;
    }

    /**
     * Get the task name for the receiver.
     * @return String
     */
	String getTaskName() {
		return taskName;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9057.java