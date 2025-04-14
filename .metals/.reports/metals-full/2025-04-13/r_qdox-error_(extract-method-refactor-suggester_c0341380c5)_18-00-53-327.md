error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7652.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7652.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7652.java
text:
```scala
_@@tabs.setVisible(firstEnabled != -1);

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

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.tigris.gef.base.*;
import org.tigris.gef.graph.presentation.*;

import org.argouml.util.*;
import org.argouml.uml.ui.*;
import org.argouml.uml.diagram.ui.*;


/** The upper right pane in the Argo/UML user interface.  It has
 *  several tabs with different kinds of "major" editors that allow
 *  the user to edit whatever is selected in the NavigatorPane. */

public class MultiEditorPane extends JPanel
implements ChangeListener, MouseListener {

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected Object _target;
  protected JTabbedPane _tabs = new JTabbedPane(JTabbedPane.BOTTOM);
  protected Editor _ed;
 // protected ForwardingPanel _awt_comp;
  protected Vector _tabPanels = new Vector();
  protected Component _lastTab;

  ////////////////////////////////////////////////////////////////
  // constructors

  public MultiEditorPane(StatusBar sb) {
    System.out.println("making MultiEditorPane");
    ConfigLoader.loadTabs(_tabPanels, "multi", sb);

    setLayout(new BorderLayout());
    add(_tabs, BorderLayout.CENTER);

    _tabs.addChangeListener(this);
    for (int i = 0; i < _tabPanels.size(); i++) {
      String title = "tab";
      JPanel t = (JPanel) _tabPanels.elementAt(i);
      if (t instanceof TabSpawnable)
	title = ((TabSpawnable)t).getTitle();
      _tabs.addTab("As " + title, t);
    } /* end for */

    for (int i = 0; i < _tabPanels.size(); i++)
      _tabs.setEnabledAt(i, false);


    _tabs.addChangeListener(this);
    _tabs.addMouseListener(this);
    setTarget(null);
  }



  ////////////////////////////////////////////////////////////////
  // accessors

  public Dimension getPreferredSize() { return new Dimension(400, 500); }
  public Dimension getMinimumSize() { return new Dimension(100, 100); }

  public void setTarget(Object target) {
    //System.out.println("MultiEditorPane setTarget: " + target);
    int firstEnabled = -1;
    boolean jumpToFirstEnabledTab = false;
    int currentTab = _tabs.getSelectedIndex();
    if (_target == target) return;
    _target = target;
    for (int i = 0; i < _tabPanels.size(); i++) {
      JPanel tab = (JPanel) _tabPanels.elementAt(i);
      if (tab instanceof TabModelTarget) {
	TabModelTarget tabMT = (TabModelTarget) tab;
	tabMT.setTarget(_target);
	boolean shouldEnable = tabMT.shouldBeEnabled();
	_tabs.setEnabledAt(i, shouldEnable);
	if (shouldEnable && firstEnabled == -1) firstEnabled = i;
	if (currentTab == i && !shouldEnable) {
	  jumpToFirstEnabledTab = true;
	}
      }
    }
    if (jumpToFirstEnabledTab && firstEnabled != -1 )
      _tabs.setSelectedIndex(firstEnabled);
    setVisible(firstEnabled != -1);
  }


  public Object getTarget() { return _target; }


  ////////////////////////////////////////////////////////////////
  // actions

  public int getIndexOfNamedTab(String tabName) {
    for (int i = 0; i < _tabPanels.size(); i++) {
      String title = _tabs.getTitleAt(i);
      if (title != null && title.equals(tabName)) return i;
    }
    return -1;
  }

  public void selectTabNamed(String tabName) {
    int index = getIndexOfNamedTab(tabName);
    if (index != -1) _tabs.setSelectedIndex(index);
  }

  public void selectNextTab() {
    int size = _tabPanels.size();
    int currentTab = _tabs.getSelectedIndex();
    for (int i = 1; i < _tabPanels.size(); i++) {
      int newTab = (currentTab + i) % size;
      if (_tabs.isEnabledAt(newTab)) {
	_tabs.setSelectedIndex(newTab);
	return;
      }
    }
  }

  public void select(Object o) {
    Component curTab = _tabs.getSelectedComponent();
    if (curTab instanceof TabDiagram) {
      JGraph jg = ((TabDiagram)curTab).getJGraph();
      jg.selectByOwnerOrFig(o);
    }
    //needs-more-work: handle tables
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** called when the user selects a tab, by clicking or otherwise. */
  public void stateChanged(ChangeEvent e) {
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    if (_lastTab != null) { _lastTab.setVisible(false); }
    _lastTab = _tabs.getSelectedComponent();
    //System.out.println("MultiEditorPane state changed:" +
    //  _lastTab.getClass().getName());
    _lastTab.setVisible(true);
    if (_lastTab instanceof TabModelTarget)
      ((TabModelTarget)_lastTab).refresh();
  }

  public void mousePressed(MouseEvent me) { }
  public void mouseReleased(MouseEvent me) { }
  public void mouseEntered(MouseEvent me) { }
  public void mouseExited(MouseEvent me) { }

  public void mouseClicked(MouseEvent me) {
    int tab = _tabs.getSelectedIndex();
    if (tab != -1) {
      Rectangle tabBounds = _tabs.getBoundsAt(tab);
      if (!tabBounds.contains(me.getX(), me.getY())) return;
      if (me.getClickCount() == 1) mySingleClick(tab);
      else if (me.getClickCount() >= 2) myDoubleClick(tab);
    }
  }


  /** called when the user clicks once on a tab. */
  public void mySingleClick(int tab) {
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    //System.out.println("single: " + _tabs.getComponentAt(tab).toString());
  }

  /** called when the user clicks twice on a tab. */
  public void myDoubleClick(int tab) {
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    //System.out.println("double: " + _tabs.getComponentAt(tab).toString());
    JPanel t = (JPanel) _tabPanels.elementAt(tab);
    if (t instanceof TabSpawnable) ((TabSpawnable)t).spawn();
  }

} /* end class MultiEditorPane */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7652.java