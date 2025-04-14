error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4533.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4533.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4533.java
text:
```scala
r@@eturn "Diagram";  //TODO

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

package org.argouml.uml;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.table.*;

import ru.novosoft.uml.foundation.core.*;

import org.tigris.gef.base.Diagram;
import org.tigris.gef.graph.GraphModel;

import org.argouml.uml.diagram.state.ui.*;
import org.argouml.uml.diagram.static_structure.ui.*;
import org.argouml.uml.diagram.use_case.ui.*;

public class TMResults extends AbstractTableModel {
  ////////////////
  // instance vars
  Vector _rowObjects;
  Vector _diagrams;

  ////////////////
  // constructor
  public TMResults() { }

  ////////////////
  // accessors
  public void setTarget(Vector results, Vector diagrams) {
    _rowObjects = results;
    _diagrams = diagrams;
    fireTableStructureChanged();
  }

  ////////////////
  // TableModel implemetation
  public int getColumnCount() { return 4; }
  public int getRowCount() {
    if (_rowObjects == null) return 0;
    return _rowObjects.size();
  }

  public String  getColumnName(int c) {
    if (c == 0) return "Type";
    if (c == 1) return "Name";
    if (c == 2) return "In Diagram";
    if (c == 3) return "Description";
    return "XXX";
  }

  public Class getColumnClass(int c) {
    return String.class;
  }

  public boolean isCellEditable(int row, int col) {
    return false;
  }

  public Object getValueAt(int row, int col) {
    if (row < 0 || row >= _rowObjects.size()) return "bad row!";
    if (col < 0 || col >= 4) return "bad col!";
    Object rowObj = _rowObjects.elementAt(row);
    if (rowObj instanceof Diagram) {
      Diagram d = (Diagram) rowObj;
      switch (col) {
      case 0:
	if (d instanceof UMLClassDiagram) return "Class Diagram";
	if (d instanceof UMLStateDiagram) return "State Diagram";
	if (d instanceof UMLUseCaseDiagram) return "Use Case Diagram";
	return "Diagram";  //needs-more-work
      case 1: return d.getName();
      case 2: return "N/A";
      case 3:
	//GraphModel gm = d.getGraphModel();
	int numNodes = d.getNodes().size();
	int numEdges = d.getEdges().size();
	return numNodes + " nodes and "+ numEdges + " edges";
      }
    }
    if (rowObj instanceof MModelElement) {
      MModelElement me = (MModelElement) rowObj;
      Diagram d = null;
      if (_diagrams != null) d = (Diagram) _diagrams.elementAt(row);
      switch (col) {
      case 0: return me.getUMLClassName();
      case 1: return me.getName();
      case 2: return (d == null) ? "N/A" : d.getName();
      case 3: return "docs";
      }
    }
    switch (col) {
    case 0:
      String clsName = rowObj.getClass().getName();
      int lastDot = clsName.lastIndexOf(".");
      return clsName.substring(lastDot+1);
    case 1: return "";
    case 2: return "??";
    case 3: return "docs";
    }
    return "unknown!";
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex)  { }

} /* end class TMResults */

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4533.java