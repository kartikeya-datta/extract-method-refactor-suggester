error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7367.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7367.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7367.java
text:
```scala
d@@etails.setPreferredSize(new Dimension(900, 100));

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.txt file.  */
package org.apache.log4j.chainsaw;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

/**
 * The main application.
 *
 * @author <a href="mailto:oliver@puppycrawl.com">Oliver Burn</a>
 */
public class Main
    extends JFrame
{
    /** the default port number to listen on **/
    private static final int DEFAULT_PORT = 4445;

    /** name of property for port name **/
    public static final String PORT_PROP_NAME = "chainsaw.port";

    /** use to log messages **/
    private static final Category LOG = Category.getInstance(Main.class);


    /**
     * Creates a new <code>Main</code> instance.
     */
    private Main() {
        super("CHAINSAW - Log4J Log Viewer");
        // create the all important model
        final MyTableModel model = new MyTableModel();

        //Create the menu bar.
        final JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        final JMenu menu = new JMenu("File");
        menuBar.add(menu);

        try {
            final LoadXMLAction lxa = new LoadXMLAction(this, model);
            final JMenuItem loadMenuItem = new JMenuItem("Load file...");
            menu.add(loadMenuItem);
            loadMenuItem.addActionListener(lxa);
        } catch (NoClassDefFoundError e) {
            LOG.info("Missing classes for XML parser", e);
            JOptionPane.showMessageDialog(
                this,
                "XML parser not in classpath - unable to load XML events.",
                "CHAINSAW",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            LOG.info("Unable to create the action to load XML files", e);
            JOptionPane.showMessageDialog(
                this,
                "Unable to create a XML parser - unable to load XML events.",
                "CHAINSAW",
                JOptionPane.ERROR_MESSAGE);
        }

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        menu.add(exitMenuItem);
        exitMenuItem.addActionListener(ExitAction.INSTANCE);

        // Add control panel
        final ControlPanel cp = new ControlPanel(model);
        getContentPane().add(cp, BorderLayout.NORTH);

        // Create the table
        final JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Events: "));
        scrollPane.setPreferredSize(new Dimension(900, 300));

        // Create the details
        final JPanel details = new DetailPanel(table, model);
        details.setPreferredSize(new Dimension(900, 300));

        // Add the table and stack trace into a splitter
        final JSplitPane jsp =
            new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, details);
        getContentPane().add(jsp, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent aEvent) {
                    ExitAction.INSTANCE.actionPerformed(null);
                }
            });

        pack();
        setVisible(true);

        setupReceiver(model);
    }

    /**
     * Setup recieving messages.
     *
     * @param aModel a <code>MyTableModel</code> value
     */
    private void setupReceiver(MyTableModel aModel) {
        int port = DEFAULT_PORT;
        final String strRep = System.getProperty(PORT_PROP_NAME);
        if (strRep != null) {
            try {
                port = Integer.parseInt(strRep);
            } catch (NumberFormatException nfe) {
                LOG.fatal("Unable to parse " + PORT_PROP_NAME +
                          " property with value " + strRep + ".");
                JOptionPane.showMessageDialog(
                    this,
                    "Unable to parse port number from '" + strRep +
                    "', quitting.",
                    "CHAINSAW",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }

        try {
            final LoggingReceiver lr = new LoggingReceiver(aModel, port);
            lr.start();
        } catch (IOException e) {
            LOG.fatal("Unable to connect to socket server, quiting", e);
            JOptionPane.showMessageDialog(
                this,
                "Unable to create socket on port " + port + ", quitting.",
                "CHAINSAW",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    // static methods
    ////////////////////////////////////////////////////////////////////////////


    /** initialise log4j **/
    private static void initLog4J() {
        final Properties props = new Properties();
        props.setProperty("log4j.rootCategory", "DEBUG, A1");
        props.setProperty("log4j.appender.A1",
                          "org.apache.log4j.ConsoleAppender");
        props.setProperty("log4j.appender.A1.layout",
                          "org.apache.log4j.TTCCLayout");
        PropertyConfigurator.configure(props);
    }

    /**
     * The main method.
     *
     * @param aArgs ignored
     */
    public static void main(String[] aArgs) {
        initLog4J();
        new Main();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7367.java