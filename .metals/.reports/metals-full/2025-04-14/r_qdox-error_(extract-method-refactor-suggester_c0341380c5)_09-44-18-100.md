error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6543.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6543.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6543.java
text:
```scala
A@@bstractProgressViewer progressViewer;

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

import java.util.HashSet;

import org.eclipse.core.runtime.jobs.Job;

/**
 * The ProgressViewerContentProvider is the content provider progress
 * viewers.
 */
public class ProgressViewerContentProvider extends ProgressContentProvider
         {
    ProgressViewer progressViewer;

    /**
     * Create a new instance of the receiver.
     * @param structured The Viewer we are providing content for
     */
    public ProgressViewerContentProvider(ProgressViewer structured) {
        super();
        progressViewer = structured;
    }

    /**
     * Create a new instance of the receiver.
     * @param structured The Viewer we are providing content for
     * @param noDebug A flag to indicate if the debug flag is false.
     */
    public ProgressViewerContentProvider(ProgressViewer structured,
            boolean noDebug) {
        super(noDebug);
        progressViewer = structured;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.progress.IProgressUpdateCollector#add(org.eclipse.ui.internal.progress.JobTreeElement[])
     */
    public void add(Object[] elements) {
        progressViewer.setInput(this);
    }

    /**
     * Return only the elements that we want to display.
     * 
     * @param elements
     *            the array of elements.
     * @return the elements that we want to display.
     */
    public Object[] getDisplayedValues(Object[] elements) {
        HashSet showing = new HashSet();

        for (int i = 0; i < elements.length; i++) {
            JobTreeElement element = (JobTreeElement) elements[i];
            if (element.isActive()) {
                if (element.isJobInfo()
                        && ((JobInfo) element).getJob().getState() != Job.RUNNING)
                    continue;
                showing.add(element);
            }
        }

        return showing.toArray();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.progress.IProgressUpdateCollector#refresh()
     */
    public void refresh() {
        progressViewer.refresh(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.progress.IProgressUpdateCollector#refresh(org.eclipse.ui.internal.progress.JobTreeElement[])
     */
    public void refresh(Object[] elements) {
        Object[] refreshes = getRoots(elements, true);
        for (int i = 0; i < refreshes.length; i++) {
            progressViewer.refresh(refreshes[i], true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.progress.IProgressUpdateCollector#remove(org.eclipse.ui.internal.progress.JobTreeElement[])
     */
    public void remove(Object[] elements) {
        progressViewer.setInput(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement) {
        JobTreeElement[] elements = ProgressManager.getInstance()
                .getRootElements(!filterDebug);
        return getDisplayedValues(elements);
    }

    /**
     * Get the root elements of the passed elements as we only show roots.
     * Replace the element with its parent if subWithParent is true
     * 
     * @param elements
     *            the array of elements.
     * @param subWithParent
     *            sub with parent flag.
     * @return
     */
    private Object[] getRoots(Object[] elements, boolean subWithParent) {
        if (elements.length == 0)
            return elements;
        HashSet roots = new HashSet();
        for (int i = 0; i < elements.length; i++) {
            JobTreeElement element = (JobTreeElement) elements[i];
            if (element.isJobInfo()) {
                GroupInfo group = ((JobInfo) element).getGroupInfo();
                if (group == null)
                    roots.add(element);
                else {
                    if (subWithParent)
                        roots.add(group);
                }
            } else
                roots.add(element);
        }
        return roots.toArray();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6543.java