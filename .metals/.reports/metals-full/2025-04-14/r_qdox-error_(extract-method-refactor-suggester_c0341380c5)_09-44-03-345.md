error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5587.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5587.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5587.java
text:
```scala
C@@olumbaLogger.log.info("Command cancelled");

//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the 
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.
package org.columba.core.command;

import org.columba.core.gui.statusbar.event.WorkerStatusChangeListener;
import org.columba.core.gui.statusbar.event.WorkerStatusChangedEvent;
import org.columba.core.gui.util.ExceptionDialog;
import org.columba.core.logging.ColumbaLogger;
import org.columba.core.util.SwingWorker;

import java.util.List;
import java.util.Vector;


/**
 * Worker additionally sends status information updates to the {@link TaskManager}.
 * <p>
 * This updates get displayed in the StatusBar.
 * <p>
 * Note that {@link Command} objects get {@link Worker} objects only
 * when executed.
 *
 * @author fdietz
 */
public class Worker extends SwingWorker implements WorkerStatusController {
    /**
     * Constant definining the delay used when using
     * clearDisplayTextWithDelay(). Defined to be 500 millisec.
     */
    private static final int CLEAR_DELAY = 500;
    protected Command op;
    protected int operationMode;
    protected DefaultProcessor boss;
    protected String displayText;
    protected int progressBarMax;
    protected int progressBarValue;
    protected boolean cancelled;
    protected List workerStatusChangeListeners;
    private int timeStamp;

    public Worker(DefaultProcessor parent) {
        super();

        this.boss = parent;

        displayText = "";
        progressBarValue = 0;
        progressBarMax = 0;

        cancelled = false;

        workerStatusChangeListeners = new Vector();
    }

    public void process(Command op, int operationMode, int timeStamp) {
        this.op = op;
        this.operationMode = operationMode;
        this.timeStamp = timeStamp;
    }

    public int getPriority() {
        return op.getPriority();
    }

    private void returnLocks(int opMode) {
        op.releaseAllFolderLocks(operationMode);
    }

    /*
    private void setWorkerStatusController() {
            FolderController[] controller = op.getFolderLocks();
            int size = Array.getLength(controller);

            for( int i=0; i<size; i++ ) {
                    controller[i].setWorkerStatusController(this);
            }
    }
    */
    public Object construct() {
        //setWorkerStatusController();
        try {
            op.process(this, operationMode);

            if (!cancelled() && (operationMode == Command.FIRST_EXECUTION)) {
                boss.getUndoManager().addToUndo(op);
            }
        } catch (CommandCancelledException e) {
            ColumbaLogger.log.debug("Command cancelled");
        } catch (Exception e) {
            // Must create a ExceptionProcessor
            e.printStackTrace();

            ExceptionDialog dialog = new ExceptionDialog();
            dialog.showDialog(e);
        }

        returnLocks(operationMode);

        return null;
    }

    public void finished() {
        try {
            op.finish();
        } catch (Exception e) {
            // Must create a ExceptionProcessor
            e.printStackTrace();
        }

        unregister();
        boss.operationFinished(op, this);
    }

    public void register(TaskManager t) {
        this.taskManager = t;

        taskManager.register(this);
    }

    public void unregister() {
        taskManager.unregister(threadVar);

        WorkerStatusChangedEvent e = new WorkerStatusChangedEvent(getTimeStamp());
        e.setType(WorkerStatusChangedEvent.FINISHED);
        fireWorkerStatusChanged(e);
        workerStatusChangeListeners.clear();
        displayText = "";
        progressBarValue = 0;
        progressBarMax = 0;
    }

    /**
     * Sets the maximum value for the progress bar.
     * @param max                New max. value for progress bar
     */
    public void setProgressBarMaximum(int max) {
        WorkerStatusChangedEvent e = new WorkerStatusChangedEvent(getTimeStamp());
        e.setType(WorkerStatusChangedEvent.PROGRESSBAR_MAX_CHANGED);
        e.setOldValue(new Integer(progressBarMax));

        progressBarMax = max;

        e.setNewValue(new Integer(progressBarMax));
        fireWorkerStatusChanged(e);
    }

    /**
     * Sets the current value of the progress bar.
     * @param value                New current value of progress bar
     */
    public void setProgressBarValue(int value) {
        WorkerStatusChangedEvent e = new WorkerStatusChangedEvent(getTimeStamp());
        e.setType(WorkerStatusChangedEvent.PROGRESSBAR_VALUE_CHANGED);
        e.setOldValue(new Integer(progressBarValue));

        progressBarValue = value;

        e.setNewValue(new Integer(progressBarValue));
        fireWorkerStatusChanged(e);
    }

    /**
     * Sets the progress bar value to zero, i.e. clears the progress bar.
     * This is the same as calling setProgressBarValue(0)
     */
    public void resetProgressBar() {
        setProgressBarValue(0);
    }

    /**
     * Returns the max. value for the progress bar
     */
    public int getProgessBarMaximum() {
        return progressBarMax;
    }

    /**
     * Returns the current value for the progress bar
     */
    public int getProgressBarValue() {
        return progressBarValue;
    }

    /**
     * Returns the text currently displayed in the status bar
     */
    public String getDisplayText() {
        return displayText;
    }

    /**
     * Set the text to be displayed in the status bar
     * @param         text        Text to display in status bar
     */
    public void setDisplayText(String text) {
        WorkerStatusChangedEvent e = new WorkerStatusChangedEvent(getTimeStamp());
        e.setType(WorkerStatusChangedEvent.DISPLAY_TEXT_CHANGED);
        e.setOldValue(displayText);

        displayText = text;

        e.setNewValue(displayText);
        fireWorkerStatusChanged(e);
    }

    /**
     * Clears the text displayed in the status bar - without any delay
     */
    public void clearDisplayText() {
        clearDisplayText(0);
    }

    /**
     * Clears the text displayed in the status bar - with a given delay.
     * The delay used is 500 ms.
     * <br>
     * If a new text is set within this delay, the text is not cleared.
     */
    public void clearDisplayTextWithDelay() {
        clearDisplayText(CLEAR_DELAY);
    }

    /**
     * Clears the text displayed in the status bar - with a given delay.
     * If a new text is set within this delay, the text is not cleared.
     * @param delay                Delay in milliseconds before clearing the text
     */
    private void clearDisplayText(int delay) {
        // init event
        WorkerStatusChangedEvent e = new WorkerStatusChangedEvent(getTimeStamp());
        e.setType(WorkerStatusChangedEvent.DISPLAY_TEXT_CLEARED);

        // "new value" is used to pass on the delay
        e.setNewValue(new Integer(delay));

        // set display text stored here to an empty string (~ cleared)
        displayText = "";

        // fire event
        fireWorkerStatusChanged(e);
    }

    public void addWorkerStatusChangeListener(WorkerStatusChangeListener l) {
        workerStatusChangeListeners.add(l);
    }

    public void removeWorkerStatusChangeListener(WorkerStatusChangeListener l) {
        workerStatusChangeListeners.remove(l);
    }

    protected void fireWorkerStatusChanged(WorkerStatusChangedEvent e) {
        // if we use the commented statement, the exceptio java.util.ConcurrentModificationException
        // is thrown ... is the worker not thread save?
        // for (Iterator it = workerStatusChangeListeners.iterator(); it.hasNext();) {
        // ((WorkerStatusChangeListener) it.next()).workerStatusChanged(e);
        for (int i = 0; i < workerStatusChangeListeners.size(); i++) {
            ((WorkerStatusChangeListener) workerStatusChangeListeners.get(i)).workerStatusChanged(e);
        }
    }

    public void cancel() {
        cancelled = true;
    }

    public boolean cancelled() {
        return cancelled;
    }

    /**
     * Returns the timeStamp.
     * @return int
     */
    public int getTimeStamp() {
        return timeStamp;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5587.java