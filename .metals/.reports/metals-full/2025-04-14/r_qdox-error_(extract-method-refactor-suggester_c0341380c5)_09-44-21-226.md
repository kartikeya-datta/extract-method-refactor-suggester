error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6623.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6623.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6623.java
text:
```scala
i@@nsert a total of 100'000 elements into the table.

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.txt file.  */


import org.apache.log4j.helpers.CyclicBuffer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Container;

/**
   The AppenderTable illustrates one possible implementation of
   an Table possibly containing a great many number of rows.

   <p>In this particular example we use a fixed size buffer
   (CyclicBuffer) although this data structure could be easily
   replaced by dynamically growing one, such as a Vector. The required
   properties of the data structure is 1) support for indexed element
   access 2) support for the insertion of new elements at the end.

   <p>Experimentation on my 1400Mhz AMD machine show that it takes
   about 45 micro-seconds to insert an element in the table. This
   number does not depend on the size of the buffer. It takes as much
   (or as little) time to insert one million elements to a buffer of
   size 10 as to a buffer of size 10'000. It takes about 4 seconds to
   insert a totaal of 100'000 elements.

   <p>On windows NT the test will run about twice as fast if you give
   the focus to the window that runs "java AppenderTable" and not the
   window that contains the Swing JFrame.  */
public class AppenderTable extends JTable {


  static Logger logger = Logger.getLogger(AppenderTable.class);

  static public void main(String[] args) {

    if(args.length != 2) {
      System.err.println(
      "Usage: java AppenderTable bufferSize runLength\n"
      +"  where bufferSize is the size of the cyclic buffer in the TableModel\n"
      +"  and runLength is the total number of elements to add to the table in\n"
      +"  this test run.");
      return;
    }

    JFrame frame = new JFrame("JTableAppennder test");
    Container container = frame.getContentPane();

    AppenderTable tableAppender = new AppenderTable();
    
    int bufferSize = Integer.parseInt(args[0]);
    AppenderTableModel model = new AppenderTableModel(bufferSize);
    tableAppender.setModel(model);

    int runLength = Integer.parseInt(args[1]);

    JScrollPane sp = new JScrollPane(tableAppender);
    sp.setPreferredSize(new Dimension(250, 80));
    
    container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
    container.add(sp);

    // The "ADD" button is intended for manual testing. It will
    // add one new logging event to the table.
    JButton button = new JButton("ADD");
    container.add(button);
    button.addActionListener(new JTableAddAction(tableAppender));

    frame.setSize(new Dimension(500,300));
    frame.setVisible(true);

    long before = System.currentTimeMillis();

    int i = 0;
    while(i++ < runLength) {      
      LoggingEvent event = new LoggingEvent("x", logger, Level.ERROR, 
					    "Message "+i, null);
      tableAppender.doAppend(event);
    }

    long after = System.currentTimeMillis();

    long totalTime = (after-before);
    
    System.out.println("Total time :"+totalTime+ " milliseconds for "+
		       "runLength insertions.");
    System.out.println("Average time per insertion :"
		       +(totalTime*1000/runLength)+ " micro-seconds.");


  }

  public
  AppenderTable() {
    this.setDefaultRenderer(Object.class, new Renderer());
  }

  /**
     When asked to append we just insert directly into the model. In a
     real appender we would use two buffers one for accumulating
     events and another to accumalte events but after filtering. Only
     the second buffer would be displayed in the table and made
     visible to the user.*/
  public
  void doAppend(LoggingEvent event) {
    ((AppenderTableModel)getModel()).insert(event);
  }

  /**
     The Renderer is required to display object in a friendlier from.
     This particular renderer is just a JTextArea.

     <p>The important point to note is that we only need *one*
     renderer. */
  class Renderer extends JTextArea implements TableCellRenderer {

    PatternLayout layout;

    public
    Renderer() {
      layout = new PatternLayout("%r %p %c [%t] -  %m");
    }

    public Component getTableCellRendererComponent(JTable table,
						   Object value,
						   boolean isSelected,
						   boolean hasFocus,
						   int row,
						   int column) {

      // If its a LoggingEvent than format it using our layout.
      if(value instanceof LoggingEvent) {
	LoggingEvent event = (LoggingEvent) value;
	String str = layout.format(event);
	setText(str);
      } else {
	setText(value.toString());
      }
      return this;
    }
  }
}

class AppenderTableModel extends AbstractTableModel {

  CyclicBuffer cb;
  
  AppenderTableModel(int size) {
    cb = new CyclicBuffer(size);
  }

  /**
     Insertion to the model always results in a fireTableDataChanged
     method call. Suprisingly enough this has no crippling impact on
     performance.  */
  public
  void insert(LoggingEvent event) {
    cb.add(event);
    fireTableDataChanged();
  }

  /**
     We assume only one column.
  */
  public 
  int getColumnCount() { 
    return 1; 
  }
  
  /**
     The row count is given by the number of elements in the
     buffer. This number is guaranteed to be between 0 and the buffer
     size (inclusive). */
  public int getRowCount() { 
    return cb.length();
  }
  
  /**
     Get the value in a given row and column. We suppose that there is
     only one colemn so we are only concerned with the row.

     <p>Interesting enough this method returns an object. This leaves
     the door open for a TableCellRenderer to render the object in
     a variety of ways.
  */
  public 
  Object getValueAt(int row, int col) {
    return cb.get(row);
  }
}


/**
   The JTableAddAction is called when the user clicks on the "ADD"
   button.
*/
class JTableAddAction implements ActionListener {
    
  AppenderTable appenderTable;
  Logger dummy = Logger.getLogger("x");
  int counter = 0;
  public
  JTableAddAction(AppenderTable appenderTable) {
    this.appenderTable = appenderTable;
  }
    
  public
  void actionPerformed(ActionEvent e) {
    counter++;
    LoggingEvent event = new LoggingEvent("x", dummy, Level.DEBUG, 
					  "Message "+counter, null);    
    appenderTable.doAppend(event);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6623.java