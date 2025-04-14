error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9096.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9096.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9096.java
text:
```scala
D@@efaultFormBuilder builder = new DefaultFormBuilder(layout, main);

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
package org.columba.core.gui.logdisplay;

import java.awt.BorderLayout;
import java.util.logging.LogRecord;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.frapuccino.awt.WindowsUtil;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * A detailed view of a log record.
 * @author redsolo
 */
public class LogRecordPanel extends JPanel {

    private LogRecord logRecord;

    /**
     * Creates a panel for the record.
     *
     * @param record the log record to display.
     */
    public LogRecordPanel(LogRecord record) {
        super();
        logRecord = record;
        initComponents();
    }

    /**
     * Inits the components.
     */
    private void initComponents() {

        JPanel main = new JPanel();

        FormLayout layout = new FormLayout("right:pref, 3dlu, pref:grow, 7dlu, right:pref, 3dlu, pref:grow", //"right:max(40dlu;pref),3dlu,
                "");
        DefaultFormBuilder builder = new DefaultFormBuilder(main, layout);

        builder.appendSeparator("Details");

        String source;
        if (logRecord.getSourceClassName() == null) {
            source = "Unknown";
        } else {
            source = logRecord.getSourceClassName() + "." + logRecord.getSourceMethodName() + "()";
        }
        builder.append("Source:", new JLabel(source), 5);

        builder.append("Time:", new JLabel(Long.toString(logRecord.getMillis())));
        builder.append("level:", new JLabel(logRecord.getLevel().toString()));

        builder.append("Thread:", new JLabel(Integer.toString(logRecord.getThreadID())));
        builder.append("Seq nr:", new JLabel(Long.toString(logRecord.getSequenceNumber())));

        builder.appendSeparator("Message");

        JTextArea area = new JTextArea(logRecord.getMessage());
        area.setLineWrap(true);
        area.setRows(5);
        area.setEditable(false);
        builder.append(new JScrollPane(area), 7);

        Throwable thrown = logRecord.getThrown();
        if (thrown != null) {
            builder.appendSeparator("Exception");
            StringBuffer buffer = new StringBuffer();

            StackTraceElement[] stackTrace = thrown.getStackTrace();
            for (int i = 0; i < stackTrace.length; i++) {
                buffer.append(stackTrace[i]);
                buffer.append("\n");
            }
            area = new JTextArea(buffer.toString());
            area.setLineWrap(true);
            area.setRows(5);
            area.setEditable(false);
            builder.append(new JScrollPane(area), 7);
        }

        setLayout(new BorderLayout());
        add(main, BorderLayout.CENTER);
    }

    /**
     * Shows the record panel in a dialog.
     * @param owner owner to jframe.
     * @param record the log record to display.
     */
    public static void showRecord(JFrame owner, LogRecord record) {
        JDialog dialog = new JDialog(owner, "Log record", false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(new LogRecordPanel(record), BorderLayout.CENTER);
        dialog.pack();
        dialog.setVisible(true);
        WindowsUtil.centerInScreen(dialog);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9096.java