error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5451.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5451.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5451.java
text:
```scala
public static final I@@mageIcon WIZ_ICON = ResourceLoader.lookupIconResource("Wiz", "Wiz");

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

package org.argouml.cognitive.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.tigris.gef.ui.*;
import org.tigris.gef.util.*;

import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.cognitive.*;
import org.argouml.cognitive.ui.*;

/** Each Critic may provide a Wizard to help fix the problem it
 *  identifies.  The "Next>" button will advance through the steps of
 *  the wizard, and increase the blue progress bar on the ToDoItem
 *  "sticky note" icon in ToDo tree pane.
 *
 * @see org.argouml.cognitive.critics.Critic
 * @see org.argouml.kernel.Wizard
 */

public class WizStep extends JPanel
implements TabToDoTarget, ActionListener, DocumentListener {

  ////////////////////////////////////////////////////////////////
  // constants

  public static final ImageIcon WIZ_ICON = Util.loadIconResource("Wiz", "Wiz");

  ////////////////////////////////////////////////////////////////
  // instance variables

  JPanel  _mainPanel = new JPanel();
  JButton _backButton = new JButton(" < Back ");
  JButton _nextButton = new JButton(" Next > ");
  JButton _finishButton = new JButton(" Finish ");
  JButton _helpButton = new JButton(" Help ");
  JPanel  _buttonPanel = new JPanel();

  Object _target;

  ////////////////////////////////////////////////////////////////
  // constructor

  public WizStep() {
    _backButton.setMnemonic('B');
    _nextButton.setMnemonic('N');
    _finishButton.setMnemonic('F');
    _helpButton.setMnemonic('H');
    _buttonPanel.setLayout(new GridLayout(1, 5));
    _buttonPanel.add(_backButton);
    _buttonPanel.add(_nextButton);
    _buttonPanel.add(new SpacerPanel());
    _buttonPanel.add(_finishButton);
    _buttonPanel.add(new SpacerPanel());
    _buttonPanel.add(_helpButton);

    _backButton.setMargin(new Insets(0, 0, 0, 0));
    _nextButton.setMargin(new Insets(0, 0, 0, 0));
    _finishButton.setMargin(new Insets(0, 0, 0, 0));
    _helpButton.setMargin(new Insets(0, 0, 0, 0));

    JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    southPanel.add(_buttonPanel);

    setLayout(new BorderLayout());
    add(_mainPanel, BorderLayout.CENTER);
    add(southPanel, BorderLayout.SOUTH);

    _backButton.addActionListener(this);
    _nextButton.addActionListener(this);
    _finishButton.addActionListener(this);
    _helpButton.addActionListener(this);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setTarget(Object item) {
    _target = item;
    enableButtons();
  }

  public void enableButtons() {
    if (_target == null) {
      _backButton.setEnabled(false);
      _nextButton.setEnabled(false);
      _finishButton.setEnabled(false);
      _helpButton.setEnabled(false);
    }
    else if (_target instanceof ToDoItem) {
      ToDoItem tdi = (ToDoItem) _target;
      Wizard w = getWizard();
      _backButton.setEnabled(w != null ? w.canGoBack() : false);
      _nextButton.setEnabled(w != null ? w.canGoNext() : false);
      _finishButton.setEnabled(w != null ? w.canFinish() : false);
      _helpButton.setEnabled(true);
    }
    else {
      //_description.setText("needs-more-work");
      return;
    }
  }

  public Object getTarget() { return _target; }

  public void refresh() { setTarget(_target); }

  public Wizard getWizard() {
    if (_target instanceof ToDoItem) {
      return ((ToDoItem)_target).getWizard();
    }
    return null;
  }

  ////////////////////////////////////////////////////////////////
  // actions

  public void doBack() {
    Wizard w = getWizard();
    if (w != null) {
      w.back();
      updateTabToDo();
    }
  }
  public void doNext() {
    Wizard w = getWizard();
    if (w != null) {
      w.next();
      updateTabToDo();
    }
  }
  public void doFinsh() {
    Wizard w = getWizard();
    if (w != null) {
      w.finish();
      updateTabToDo();
    }
  }
  public void doHelp() {
    if (!(_target instanceof ToDoItem)) return;
    ToDoItem item = (ToDoItem) _target;
    String urlString = item.getMoreInfoURL();
    TinyHTMLViewer viewer = new TinyHTMLViewer(urlString);
    viewer.setVisible(true);
    System.out.println("needs-more-work: display critic/wizard help");
  }

  protected void updateTabToDo() {
    // awkward: relying on getParent() is fragile.
    TabToDo ttd = (TabToDo) getParent(); //???
    JPanel ws = getWizard().getCurrentPanel();
    if (ws instanceof WizStep) ((WizStep)ws).setTarget(_target);
    ttd.showStep(ws);
  }

  ////////////////////////////////////////////////////////////////
  // ActionListener implementation

  public void actionPerformed(ActionEvent ae) {
    Object src = ae.getSource();
    if (src == _backButton) doBack();
    else if (src == _nextButton) doNext();
    else if (src == _finishButton) doFinsh();
    else if (src == _helpButton) doHelp();
  }

  ////////////////////////////////////////////////////////////////
  // DocumentListener implementation

  public void insertUpdate(DocumentEvent e) {
    enableButtons();
  }

  public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

  public void changedUpdate(DocumentEvent e) {
    // Apparently, this method is never called.
  }

} /* end class WizStep */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5451.java