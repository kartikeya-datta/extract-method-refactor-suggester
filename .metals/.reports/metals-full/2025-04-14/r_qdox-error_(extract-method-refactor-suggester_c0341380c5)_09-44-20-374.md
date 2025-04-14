error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1199.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1199.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1199.java
text:
```scala
i@@f (arg0.getActionCommand().equals("CANCEL")) {

// The contents of this file are subject to the Mozilla Public License Version
// 1.1
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
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.

package org.columba.mail.gui.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.columba.core.command.Command;
import org.columba.core.command.WorkerStatusController;
import org.columba.core.gui.statusbar.event.WorkerStatusChangeListener;
import org.columba.core.gui.statusbar.event.WorkerStatusChangedEvent;
import org.columba.core.gui.util.ButtonWithMnemonic;
import org.columba.core.gui.util.ImageLoader;

/**
 * Dialog shows progress while sending message.
 * <p>
 * Additionally offers the possibility to cancel the operation.
 * <p>
 * This is the first example of watching the progress for a specific worker,
 * using the timestamp attribute, which is created by {@link DefaultProcessor}
 * when executing the {@link Command}.
 * 
 * @author fdietz
 */
public class SendMessageDialog extends JDialog implements
		WorkerStatusChangeListener, ActionListener {
	private JProgressBar progressBar;

	private JButton cancelButton;

	private JLabel label;

	private WorkerStatusController worker;

	/**
	 * @param arg0
	 * @throws java.awt.HeadlessException
	 */
	public SendMessageDialog(WorkerStatusController worker)
			throws HeadlessException {
		super(new JFrame(), "Sending message...", false);

		setWorker(worker);

		initComponents();
		layoutComponents();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	protected void initComponents() {
		label = new JLabel("Sending message...");
		label.setIcon(ImageLoader.getSmallImageIcon("sending.png"));

		progressBar = new JProgressBar();
		progressBar.setPreferredSize(new Dimension(300, 20));

		cancelButton = new ButtonWithMnemonic("&Cancel");
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(this);
	}

	protected void layoutComponents() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		getContentPane().add(panel);

		JPanel top = new JPanel();
		top.setBorder(BorderFactory.createEmptyBorder(0, 12, 6, 0));
		top.setLayout(new BorderLayout());
		top.add(label, BorderLayout.WEST);

		panel.add(top, BorderLayout.NORTH);

		panel.add(progressBar, BorderLayout.CENTER);

		JPanel bottom = new JPanel();
		bottom.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
		bottom.add(cancelButton);

		panel.add(bottom, BorderLayout.SOUTH);
	}

	/** ********************** WorkerStatusListener ************************** */
public void workerStatusChanged(WorkerStatusChangedEvent e) {
        int ts = e.getTimeStamp();
        final WorkerStatusChangedEvent event = e;

        // only update if timestamp is equal
        if (worker.getTimeStamp() == ts) {
            switch (e.getType()) {
            case WorkerStatusChangedEvent.DISPLAY_TEXT_CHANGED:
            	javax.swing.SwingUtilities.invokeLater(new Runnable() {
    				public void run() {
    					 label.setText((String) event.getNewValue());
    				}
    			});
               

                break;

            case WorkerStatusChangedEvent.DISPLAY_TEXT_CLEARED:

                // implemented for completeness.
                // Time-out for clearing text is ignored here.
            	javax.swing.SwingUtilities.invokeLater(new Runnable() {
    				public void run() {
    					 label.setText("");
    				}
    			});

                break;

            case WorkerStatusChangedEvent.PROGRESSBAR_MAX_CHANGED: {
            	javax.swing.SwingUtilities.invokeLater(new Runnable() {
    				public void run() {
    					 progressBar.setMaximum(((Integer) event.getNewValue()).intValue());
    				}
    			});
               

                break;
            }

            case WorkerStatusChangedEvent.PROGRESSBAR_VALUE_CHANGED:
            	javax.swing.SwingUtilities.invokeLater(new Runnable() {
    				public void run() {
    					 progressBar.setValue(((Integer) event.getNewValue()).intValue());
    				}
    			});
               

                break;

                /*
				 * case WorkerStatusChangedEvent.FINISHED : setVisible(false);
				 * break;
				 */
            }
        }
    }
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.equals("CANCEL")) {
			// send cancel event to worker
			worker.cancel();
			setVisible(false);
		}
	}

	/**
	 * @param worker
	 *            The worker to set.
	 */
	public void setWorker(WorkerStatusController worker) {
		this.worker = worker;

		worker.addWorkerStatusChangeListener(this);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1199.java