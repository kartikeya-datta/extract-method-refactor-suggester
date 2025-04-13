error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9890.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9890.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9890.java
text:
```scala
t@@askPanel.add(taskButton, BorderLayout.CENTER);

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

package org.columba.core.gui.statusbar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.columba.core.command.TaskManager;
import org.columba.core.command.TaskManagerEvent;
import org.columba.core.command.TaskManagerListener;
import org.columba.core.command.Worker;
import org.columba.core.gui.statusbar.event.WorkerStatusChangeListener;
import org.columba.core.gui.statusbar.event.WorkerStatusChangedEvent;
import org.columba.core.gui.toolbar.ToolbarButton;
import org.columba.core.gui.util.ImageLoader;
import org.columba.core.main.ConnectionStateImpl;

/**
 * A status bar intended to be displayed at the bottom of each window.
 */
public class StatusBar extends JComponent implements TaskManagerListener,
    ActionListener, WorkerStatusChangeListener, ChangeListener {
    
    protected static Icon onlineIcon = ImageLoader.getImageIcon("online.png");
    protected static Icon offlineIcon = ImageLoader.getImageIcon("offline.png");
    
    private JLabel label;
    private JProgressBar progressBar;
    private Border border;
    private JPanel mainRightPanel;
    private JButton taskButton;
    private JPanel leftMainPanel;
    private Worker displayedWorker;
    private TaskManager taskManager;
    private ImageSequenceTimer imageSequenceTimer;
    private JButton onlineButton;

    /** Timer to use when clearing status bar text after a certain timeout */
    private Timer clearTextTimer;

    public StatusBar(TaskManager tm) {
        taskManager = tm;
        tm.addTaskManagerListener(this);
        ConnectionStateImpl.getInstance().addChangeListener(this);

        imageSequenceTimer = new ImageSequenceTimer(tm);

        setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 2));

        label = new JLabel("");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        onlineButton = new ToolbarButton();
        onlineButton.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        onlineButton.setRolloverEnabled(true);
        onlineButton.setActionCommand("ONLINE");
        onlineButton.addActionListener(this);
        stateChanged(null);

        progressBar = new JProgressBar(0, 100);

        //progressBar.setAlignmentX(Component.RIGHT_ALIGNMENT);
        //progressBar.setAlignmentY(Component.CENTER_ALIGNMENT);
        progressBar.setStringPainted(false);
        progressBar.setBorderPainted(false);
        progressBar.setValue(0);

        taskButton = new ToolbarButton();
        taskButton.setIcon(ImageLoader.getImageIcon("group_small.png"));
        taskButton.setToolTipText("Show list of running tasks");
        taskButton.setRolloverEnabled(true);
        taskButton.setActionCommand("TASKMANAGER");
        taskButton.addActionListener(this);

        //taskButton.setMargin(new Insets(0, 0, 0, 0));
        taskButton.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        //taskButton.setBorder(null);
        setLayout(new BorderLayout());

        leftMainPanel = new JPanel();
        leftMainPanel.setLayout(new BorderLayout());

        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new BorderLayout());

        Border border = getDefaultBorder();
        Border margin = new EmptyBorder(0, 0, 0, 2);

        taskPanel.setBorder(new CompoundBorder(border, margin));

        //taskPanel.add(taskButton, BorderLayout.CENTER);

        leftMainPanel.add(taskPanel, BorderLayout.WEST);
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BorderLayout());
        margin = new EmptyBorder(0, 10, 0, 10);
        labelPanel.setBorder(new CompoundBorder(border, margin));

        margin = new EmptyBorder(0, 0, 0, 2);
        labelPanel.add(label, BorderLayout.CENTER);

        leftMainPanel.add(labelPanel, BorderLayout.CENTER);

        add(leftMainPanel, BorderLayout.CENTER);

        mainRightPanel = new JPanel();
        mainRightPanel.setLayout(new BorderLayout());

        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BorderLayout());
        progressPanel.setBorder(new CompoundBorder(border, margin));

        progressPanel.add(progressBar, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        rightPanel.add(progressPanel, BorderLayout.CENTER);

        JPanel onlinePanel = new JPanel();
        onlinePanel.setLayout(new BorderLayout());
        onlinePanel.setBorder(new CompoundBorder(border, margin));

        onlinePanel.add(onlineButton, BorderLayout.CENTER);

        rightPanel.add(onlinePanel, BorderLayout.EAST);
        add(rightPanel, BorderLayout.EAST);

        // init timer
        initClearTextTimer();
    }

    public Border getDefaultBorder() {
        return UIManager.getBorder("TableHeader.cellBorder");
    }

    public void displayTooltipMessage(String message) {
        setText(message);
    }

    protected void updateTaskCount() {
        Runnable run = new Runnable() {
                public void run() {
                    //taskButton.setText("Tasks: " + taskManager.count());
                }
            };

        try {
            SwingUtilities.invokeLater(run);
        } catch (Exception ex) {
        }
    }

    protected void setText(String s) {
        // *20031102, karlpeder* Setting a new text must cancel pending
        // requests for clearing the text
        clearTextTimer.stop();

        final String str = s;

        Runnable run = new Runnable() {
                public void run() {
                    label.setText(str);
                }
            };

        try {
            SwingUtilities.invokeLater(run);
        } catch (Exception ex) {
        }
    }

    protected void setMaximum(int i) {
        final int n = i;

        Runnable run = new Runnable() {
                public void run() {
                    progressBar.setValue(0);
                    progressBar.setMaximum(n);
                }
            };

        try {
            SwingUtilities.invokeLater(run);
        } catch (Exception ex) {
        }
    }

    protected void setMaximumAndValue(int v, int m) {
        final int max = m;
        final int val = v;

        Runnable run = new Runnable() {
                public void run() {
                    progressBar.setValue(val);
                    progressBar.setMaximum(max);
                }
            };

        try {
            SwingUtilities.invokeLater(run);
        } catch (Exception ex) {
        }
    }

    protected void setValue(int i) {
        final int n = i;

        Runnable run = new Runnable() {
                public void run() {
                    progressBar.setValue(n);
                }
            };

        try {
            SwingUtilities.invokeLater(run);
        } catch (Exception ex) {
        }
    }

    public void workerAdded(TaskManagerEvent e) {
        updateTaskCount();
        setDisplayedWorker(e.getWorker());
    }
    
    public void workerRemoved(TaskManagerEvent e) {
        updateTaskCount();
        if (e.getWorker() == displayedWorker) {
            Worker[] workers = taskManager.getWorkers();
            setDisplayedWorker(workers.length > 0 ? workers[0] : null);
        }
    }

    public void workerStatusChanged(WorkerStatusChangedEvent e) {
        switch (e.getType()) {
        case WorkerStatusChangedEvent.DISPLAY_TEXT_CHANGED:
            setText((String) e.getNewValue());

            break;

        /*
         * 20031102, karlpeder* Added handling of DISPLAY_TEXT_CLEARED:
         * The status bar text is cleared after a certain time out
         * found as e.getNewValue()
         */
        case WorkerStatusChangedEvent.DISPLAY_TEXT_CLEARED:
            clearTextTimer.stop();
            clearTextTimer.setInitialDelay(((Integer) e.getNewValue()).intValue());
            clearTextTimer.restart();

            break;

        case WorkerStatusChangedEvent.PROGRESSBAR_MAX_CHANGED:
            setMaximum(((Integer) e.getNewValue()).intValue());

            break;

        case WorkerStatusChangedEvent.PROGRESSBAR_VALUE_CHANGED:
            setValue(((Integer) e.getNewValue()).intValue());
        }
    }

    /**
     * Initializes the timer to use when the status bar text must be cleared
     * after a certain timeout.
     */
    private void initClearTextTimer() {
        clearTextTimer = new Timer(0,
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        // stop timer and clear status bar text when timer runs out
                        clearTextTimer.stop();
                        setText("");
                    }
                });
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("ONLINE")) {
        	ConnectionStateImpl.getInstance().setOnline(
                    !ConnectionStateImpl.getInstance().isOnline());
        } else if (command.equals("TASKMANAGER")) {
            TaskManagerDialog.createInstance();
        } else if (command.equals("CANCEL_ACTION")) {
            displayedWorker.cancel();
        }
    }
    
    /**
     * Sets the worker to be displayed.
     */
    protected void setDisplayedWorker(Worker w) {
        if (displayedWorker != null) {
            displayedWorker.removeWorkerStatusChangeListener(this);
        }
        displayedWorker = w;
        
        if (w == null) {
            setText("");
            setMaximumAndValue(0, 0);
        } else {
            setText(w.getDisplayText());
            setMaximumAndValue(w.getProgressBarValue(), w.getProgessBarMaximum());
            w.addWorkerStatusChangeListener(this);
        }
    }

    /**
     * Returns the worker currently displayed.
     */
    public Worker getDisplayedWorker() {
        return displayedWorker;
    }

    /**
     * Returns the imageSequenceTimer.
     * @return ImageSequenceTimer
     */
    public ImageSequenceTimer getImageSequenceTimer() {
        return imageSequenceTimer;
    }
    
    /**
     * Returns the task manager this status bar is attached to.
     */
    public TaskManager getTaskManager() {
        return taskManager;
    }
    
    public void stateChanged(ChangeEvent e) {
        if (ConnectionStateImpl.getInstance().isOnline()) {
            onlineButton.setIcon(onlineIcon);
            //TODO: i18n
            onlineButton.setToolTipText("You are in ONLINE state");
        } else {
            onlineButton.setIcon(offlineIcon);
            //TODO: i18n
            onlineButton.setToolTipText("You are in OFFLINE state");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9890.java