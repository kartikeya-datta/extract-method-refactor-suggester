error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1054.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1054.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1054.java
text:
```scala
i@@mplements TabFigTarget, PropertyChangeListener, DelayedVChangeListener {

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
import com.sun.java.swing.tree.*;
//import com.sun.java.swing.border.*;

import uci.gef.Diagram;
import uci.gef.Fig;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Use_Cases.*;
import uci.uml.Model_Management.*;
import uci.uml.ui.props.*;

public class TabStyle extends TabSpawnable
implements TabFigTarget, PropertyChangeListener, DelayedVetoableChangeListener {
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected Fig           _target;
  protected boolean       _shouldBeEnabled    = false;
  protected JPanel        _blankPanel         = new JPanel();
  protected Hashtable     _panels             = new Hashtable();
  protected JPanel        _lastPanel          = null;
  protected TabFigTarget  _stylePanel          = null;
  protected String        _panelClassBaseName = "";

  ////////////////////////////////////////////////////////////////
  // constructor
  public TabStyle(String tabName, String panelClassBase) {
    super(tabName);
    _panelClassBaseName = panelClassBase;
    setLayout(new BorderLayout());
    //setFont(new Font("Dialog", Font.PLAIN, 10));
  }

  public TabStyle() {
    this("Style", "style.StylePanel");
  }


  ////////////////////////////////////////////////////////////////
  // accessors
  public void setTarget(Fig t) {
    if (_target != null) _target.removePropertyChangeListener(this);
    _target = t;
    if (_target != null) _target.addPropertyChangeListener(this);
    if (_lastPanel != null) remove(_lastPanel);
    if (t == null) {
      add(_blankPanel, BorderLayout.NORTH);
      _shouldBeEnabled = false;
      _lastPanel = _blankPanel;
      return;
    }
    _shouldBeEnabled = true;
    _stylePanel = null;
    Class targetClass = t.getClass();
    while (targetClass != null && _stylePanel == null) {
      _stylePanel = findPanelFor(targetClass);
      targetClass = targetClass.getSuperclass();
    }
    if (_stylePanel != null) {
      _stylePanel.setTarget(_target);
      add((JPanel)_stylePanel, BorderLayout.NORTH);
      _shouldBeEnabled = true;
      _lastPanel = (JPanel) _stylePanel;
    }
    else {
      add(_blankPanel, BorderLayout.NORTH);      
      _shouldBeEnabled = false;
      _lastPanel = _blankPanel;
    }
    validate();
  }

  public void refresh() { setTarget(_target); }

  public TabFigTarget findPanelFor(Class targetClass) {
    TabFigTarget p = (TabFigTarget) _panels.get(targetClass);
    if (p == null) {
      Class panelClass = panelClassFor(targetClass);
      if (panelClass == null) return null;
      try { p = (TabFigTarget) panelClass.newInstance(); }
      catch (IllegalAccessException ignore) { return null; }
      catch (InstantiationException ignore) { return null; }
      _panels.put(targetClass, p);
    }
    return p;
  }

  public Class panelClassFor(Class targetClass) {
    String pack = "uci.uml.ui";
    String base = getClassBaseName();

    String targetClassName = targetClass.getName();
    int lastDot = targetClassName.lastIndexOf(".");
    if (lastDot > 0) targetClassName = targetClassName.substring(lastDot+1);
    try {
      String panelClassName = pack + "." + base + targetClassName;
      Class cls = Class.forName(panelClassName);
      return cls;
    }
    catch (ClassNotFoundException ignore) { }
    targetClass = targetClass.getSuperclass();
    return null;
  }

  protected String getClassBaseName() { return _panelClassBaseName; }

  public Fig getTarget() { return _target; }

  public boolean shouldBeEnabled() { return _shouldBeEnabled; }


  ////////////////////////////////////////////////////////////////
  // PropertyChangeListener implementation

  public void propertyChange(PropertyChangeEvent pce) {
    DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
    SwingUtilities.invokeLater(delayedNotify);
  }

  public void delayedVetoableChange(PropertyChangeEvent pce) {
    if (_stylePanel != null) _stylePanel.refresh();
  }

} /* end class TabStyle */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1054.java