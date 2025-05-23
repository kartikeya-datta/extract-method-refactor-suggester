error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5251.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5251.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5251.java
text:
```scala
i@@f (d != null) cat.debug("go " + sel + " in " + d.getName());

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

package org.argouml.ui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.border.*;

import ru.novosoft.uml.foundation.core.*;

import org.tigris.gef.base.Diagram;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.util.*;
import org.tigris.gef.util.*;

import org.apache.log4j.Category;
import org.argouml.uml.*;
import org.argouml.uml.cognitive.*;

public class TabResults extends TabSpawnable
implements Runnable, MouseListener, ActionListener, ListSelectionListener {
    protected static Category cat = 
        Category.getInstance(TabResults.class);
    

  public static int _numJumpToRelated = 0;
  
  ////////////////////////////////////////////////////////////////
  // insatnce variables
  PredicateFind _pred;
  ChildGenerator _cg = null;
  Object _root = null;
  JSplitPane _mainPane;
  Vector _results = new Vector();
  Vector _related = new Vector();
  Vector _diagrams = new Vector();
  boolean _showRelated = false;

  JLabel    _resultsLabel = new JLabel("Search Results:");
  JTable    _resultsTable = new JTable(10, 4);
  TMResults _resultsModel = new TMResults();

  JLabel    _relatedLabel = new JLabel("Related Elements:");
  JTable    _relatedTable = new JTable(4, 4);
  TMResults _relatedModel = new TMResults();

  ////////////////////////////////////////////////////////////////
  // constructor
  public TabResults() {
    this(true);
  }

  public TabResults(boolean showRelated) {
    super("Results", true);
    _showRelated = showRelated;
    setLayout(new BorderLayout());

    Font labelFont = MetalLookAndFeel.getSubTextFont();

    JPanel resultsW = new JPanel();
    JScrollPane resultsSP =
      new JScrollPane(_resultsTable,
		      JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    resultsW.setLayout(new BorderLayout());
    resultsW.add(_resultsLabel, BorderLayout.NORTH);
    resultsW.add(resultsSP, BorderLayout.CENTER);
    _resultsTable.setFont(labelFont);
    _resultsTable.setModel(_resultsModel);
    _resultsTable.addMouseListener(this);
    _resultsTable.getSelectionModel().addListSelectionListener(this);
    resultsW.setMinimumSize(new Dimension(100, 100));
    resultsW.setPreferredSize(new Dimension(100, 200));

    JPanel relatedW = new JPanel();
    if (_showRelated) {
      JScrollPane relatedSP =
	new JScrollPane(_relatedTable,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      relatedW.setLayout(new BorderLayout());
      relatedW.add(_relatedLabel, BorderLayout.NORTH);
      relatedW.add(relatedSP, BorderLayout.CENTER);
      _relatedTable.setFont(labelFont);
      _relatedTable.setModel(_relatedModel);
      _relatedTable.addMouseListener(this);
      relatedW.setMinimumSize(new Dimension(100, 100));
      relatedW.setPreferredSize(new Dimension(100, 100));
    }

    if (_showRelated) {
      _mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				 resultsW, relatedW);
      _mainPane.setDividerSize(2);
      add(_mainPane, BorderLayout.CENTER);
    } else {
      add(resultsW, BorderLayout.CENTER);
    }

  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setPredicate(PredicateFind p) { _pred = p; }
  public void setRoot(Object root) { _root = root; }
  public void setGenerator(ChildGenerator gen) { _cg = gen; }

  public void setResults(Vector res, Vector dia) {
    _results = res;
    _diagrams = dia;
    _resultsLabel.setText("Search Results: " + _results.size() + " items");
    _resultsModel.setTarget(_results, _diagrams);
    _relatedModel.setTarget(null, null);
    _relatedLabel.setText("Related Elements: ");
  }

  public TabSpawnable spawn() {
    TabResults newPanel = (TabResults) super.spawn();
    newPanel.setResults(_results, _diagrams);
    return newPanel;
  }
  ////////////////////////////////////////////////////////////////
  // ActionListener implementation

  public void actionPerformed(ActionEvent ae) {
  }

  ////////////////////////////////////////////////////////////////
  // MouseListener implementation

  public void mousePressed(MouseEvent me) { }
  public void mouseReleased(MouseEvent me) { }
  public void mouseClicked(MouseEvent me) {
    if (me.getClickCount() >= 2) myDoubleClick(me.getSource());
  }
  public void mouseEntered(MouseEvent me) { }
  public void mouseExited(MouseEvent me) { }

  public void myDoubleClick(Object src) {
    int row = _resultsTable.getSelectionModel().getMinSelectionIndex();

    Object sel = null;
    Diagram d = null;
    if (src == _resultsTable) {
      sel = _results.elementAt(row);
      d = (Diagram) _diagrams.elementAt(row);
    }
    else {
      _numJumpToRelated++;
      sel = _related.elementAt(row);
    }

    cat.debug("go " + sel + " in " + d.getName());
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    if (d != null) pb.setTarget(d);
    pb.setTarget(sel);
  }

  ////////////////////////////////////////////////////////////////
  // ListSelectionListener implementation

  public void valueChanged(ListSelectionEvent lse) {
    if (lse.getValueIsAdjusting()) return;
    Object src = lse.getSource();
    if (_showRelated) {
      int row = lse.getFirstIndex();
      Object sel = _results.elementAt(row);
      cat.debug("selected " + sel);
      _related.removeAllElements();
      java.util.Enumeration enum = ChildGenRelated.SINGLETON.gen(sel);
      while (enum.hasMoreElements())
	_related.addElement(enum.nextElement());
      _relatedModel.setTarget(_related, null);
      _relatedLabel.setText("Related Elements: "+ _related.size() + " items");
    }
  }

  ////////////////////////////////////////////////////////////////
  // actions

  public void run() {
    _resultsLabel.setText("Searching...");
    _results.removeAllElements();
    depthFirst(_root, null);
    setResults(_results, _diagrams);
    _resultsLabel.setText("Search Results: " + _results.size() + " items");
    _resultsModel.setTarget(_results, _diagrams);
  }

  public void depthFirst(Object node, Diagram lastDiagram) {
    if (node instanceof Diagram) {
      lastDiagram = (Diagram) node;
      if (!_pred.matchDiagram(lastDiagram)) return;
      // diagrams are not placed in search results
    }
	java.util.Enumeration enum =  _cg.gen(node);
    while (enum.hasMoreElements()) {
      Object c = enum.nextElement();
      if (_pred.predicate(c)) {
	_results.addElement(c);
	_diagrams.addElement(lastDiagram);
      }
      depthFirst(c, lastDiagram);
    }
  }

} /* end class TabResults */


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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5251.java