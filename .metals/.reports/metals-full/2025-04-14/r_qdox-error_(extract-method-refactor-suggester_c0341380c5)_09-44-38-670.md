error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2269.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2269.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2269.java
text:
```scala
i@@mplements VetoableChangeListener, DelayedVChangeListener {

// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.




package uci.uml.ui;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;

import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.table.*;
import com.sun.java.swing.plaf.metal.MetalLookAndFeel;

import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;

public class TabTaggedValues extends TabSpawnable
implements TabModelTarget {
  ////////////////////////////////////////////////////////////////
  // constants
  public final static String DEFAULT_NAME = "tag";
  public final static String DEFAULT_VALUE = "value";

  ////////////////////////////////////////////////////////////////
  // instance variables
  Object _target;
  TableModelTaggedValues _tableModel = new TableModelTaggedValues();
  boolean _shouldBeEnabled = false;
  JTable _table = new JTable(10, 2);

  ////////////////////////////////////////////////////////////////
  // constructor
  public TabTaggedValues() {
    super("TaggedValues");

    _table.setModel(_tableModel);
    TableColumn keyCol = _table.getColumnModel().getColumn(0);
    TableColumn valCol = _table.getColumnModel().getColumn(1);
    keyCol.setMinWidth(50);
    keyCol.setWidth(150);
    valCol.setMinWidth(250);
    valCol.setWidth(550);

    _table.setRowSelectionAllowed(false);
    // _table.getSelectionModel().addListSelectionListener(this);
    JScrollPane sp = new JScrollPane(_table);
    Font labelFont = MetalLookAndFeel.getSubTextFont();
    _table.setFont(labelFont);

    setLayout(new BorderLayout());
    add(sp, BorderLayout.CENTER);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setTarget(Object t) {
    if (!(t instanceof ModelElement)) {
      _target = null;
      _shouldBeEnabled = false;
      return;
    }
    _target = t;
    _shouldBeEnabled = true;

    ModelElement me = (ModelElement) _target;
    Vector tvs = me.getTaggedValue();
    _tableModel.setTarget(me);
    validate();
  }
  public Object getTarget() { return _target; }

  public void refresh() { setTarget(_target); }

  public boolean shouldBeEnabled() { return _shouldBeEnabled; }

} /* end class TabTaggedValues */




class TableModelTaggedValues extends AbstractTableModel
implements VetoableChangeListener, DelayedVetoableChangeListener {
  ////////////////
  // instance varables
  ModelElement _target;

  ////////////////
  // constructor
  public TableModelTaggedValues() { }

  ////////////////
  // accessors
  public void setTarget(ModelElement t) {
    if (_target instanceof ElementImpl)
      ((ModelElementImpl)_target).removeVetoableChangeListener(this);
    _target = t;
    if (_target instanceof ElementImpl)
      ((ModelElementImpl)_target).addVetoableChangeListener(this);
    fireTableStructureChanged();
  }

  ////////////////
  // TableModel implemetation
  public int getColumnCount() { return 2; }

  public String  getColumnName(int c) {
    if (c == 0) return "Tag";
    if (c == 1) return "Value";
    return "XXX";
  }

  public Class getColumnClass(int c) {
    return String.class;
  }

  public boolean isCellEditable(int row, int col) {
    return true;
  }

  public int getRowCount() {
    if (_target == null) return 0;
    Vector tvs = _target.getTaggedValue();
    //if (tvs == null) return 1;
    return tvs.size() + 1;
  }

  public Object getValueAt(int row, int col) {
    Vector tvs = _target.getTaggedValue();
    //if (tvs == null) return "";
    if (row == tvs.size()) return ""; //blank line allows addition
    TaggedValue tv = (TaggedValue) tvs.elementAt(row);
    if (col == 0) {
      Name n = tv.getTag();
      if (n == null || n.getBody() == null) return "";
      return n.getBody();
    }
    if (col == 1) {
      Uninterpreted be = tv.getValue();
      if (be == null || be.getBody() == null) return "";
      return be.getBody();
    }
    return "TV-" + row*2+col; // for debugging
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    if (columnIndex != 0 && columnIndex != 1) return;
    if (!(aValue instanceof String)) return;
    Vector tvs = _target.getTaggedValue();
    if (tvs.size() == rowIndex) {
      TaggedValue tv = new TaggedValue();
      if (columnIndex == 0) tv.setTag(new Name((String) aValue));
      if (columnIndex == 1) tv.setValue(new Uninterpreted((String) aValue));
      tvs.addElement(tv);
      fireTableStructureChanged(); //?
    }
    else if ("".equals(aValue)) {
      tvs.removeElementAt(rowIndex);
      fireTableStructureChanged(); //?
    }
    else {
      TaggedValue tv = (TaggedValue) tvs.elementAt(rowIndex);
      if (columnIndex == 0) tv.setTag(new Name((String) aValue));
      if (columnIndex == 1) tv.setValue(new Uninterpreted((String) aValue));
    }
  }

  ////////////////
  // event handlers

  public void vetoableChange(PropertyChangeEvent pce) {
    DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
    SwingUtilities.invokeLater(delayedNotify);
  }

  public void delayedVetoableChange(PropertyChangeEvent pce) {
    fireTableStructureChanged();
  }

  
} /* end class TableModelTaggedValues */

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2269.java