error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8713.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8713.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8713.java
text:
```scala
s@@uper("tab.todo-item");

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
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.argouml.cognitive.ui.*;

import org.argouml.kernel.*;
import org.argouml.cognitive.*;
import org.argouml.ui.*;
import org.argouml.uml.ui.*;

import org.argouml.swingext.Toolbar;

public class TabToDo extends TabSpawnable implements TabToDoTarget {
  ////////////////////////////////////////////////////////////////
  // static variables
  public static int _numHushes = 0;

  public static UMLAction _actionNewToDoItem = Actions.NewToDoItem;
  public static UMLAction _actionResolve = Actions.Resolve;
  public static UMLAction _actionEmailExpert = Actions.EmailExpert;
  //public static UMLAction _actionMoreInfo = Actions.MoreInfo;
  public static UMLAction _actionSnooze = Actions.Snooze;
  //public static UMLAction _actionRecordFix = Actions.RecordFix;
  //public static UMLAction _actionReplayFix = Actions.ReplayFix;
  //public static UMLAction _actionFixItNext = Actions.FixItNext;
  //public static UMLAction _actionFixItBack = Actions.FixItBack;
  //public static UMLAction _actionFixItFinish = Actions.FixItFinish;
  
  ////////////////////////////////////////////////////////////////
  // instance variables
  Object _target;  //not ToDoItem
  //JButton _newButton = new JButton("New");
  //JButton _resolveButton = new JButton("Resolve");
  //JButton _fixItButton = new JButton("FixIt");  //html
  //JButton _moreInfoButton = new JButton("More Info"); //html
  //JButton _emailExpertButton = new JButton("Email Expert"); //html
  //JButton _snoozeButton = new JButton("Snooze");
  //JTextArea _description = new JTextArea();
  WizDescription _description = new WizDescription();
  JPanel _lastPanel = null;
  

  ////////////////////////////////////////////////////////////////
  // constructor
  public TabToDo() {
    super("ToDoItem");
    setLayout(new BorderLayout());
//     JPanel buttonPane = new JPanel();
//     buttonPane.setFont(new Font("Dialog", Font.PLAIN, 9));
//     buttonPane.setLayout(new FlowLayout());
//     buttonPane.add(_newButton);
//     _newButton.setFont(new Font("Dialog", Font.PLAIN, 9));
//     buttonPane.add(_resolveButton);
//     _resolveButton.setFont(new Font("Dialog", Font.PLAIN, 9));
//     buttonPane.add(_fixItButton);
//     _fixItButton.setFont(new Font("Dialog", Font.PLAIN, 9));
//     buttonPane.add(_moreInfoButton);
//     _moreInfoButton.setFont(new Font("Dialog", Font.PLAIN, 9));
//     buttonPane.add(_emailExpertButton);
//     _emailExpertButton.setFont(new Font("Dialog", Font.PLAIN, 9));
//     buttonPane.add(_snoozeButton);
//     _snoozeButton.setFont(new Font("Dialog", Font.PLAIN, 9));
//     add(buttonPane, BorderLayout.NORTH);

    Toolbar toolBar = new Toolbar(JToolBar.VERTICAL);
    //toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.Y_AXIS));
    toolBar.add(_actionNewToDoItem);
    toolBar.add(_actionResolve);
    toolBar.add(_actionEmailExpert);
    //toolBar.add(_actionMoreInfo);
    toolBar.add(_actionSnooze);
    toolBar.addSeparator();
    
//     toolBar.add(_actionRecordFix);
//     toolBar.add(_actionReplayFix);

    //     toolBar.add(_actionFixItNext);
    //     toolBar.add(_actionFixItBack);
    //     toolBar.add(_actionFixItFinish);

    //     addTool(toolBar, "New");
    //     addTool(toolBar, "FixIt");
    //     addTool(toolBar, "Resolve");
    //     addTool(toolBar, "EmailExpert");
    //     addTool(toolBar, "MoreInfo");
    //     addTool(toolBar, "Snooze");
    //     //_description.setFont(new Font("Dialog", Font.PLAIN, 9));
    add(toolBar, BorderLayout.WEST);
//     _description.setLineWrap(true);
//     _description.setWrapStyleWord(true);

    //Font userFont = MetalLookAndFeel.getUserTextFont();
    //_description.setFont(userFont);
    //add(new JScrollPane(_description), BorderLayout.CENTER);
    //@ add(_description, BorderLayout.CENTER);
    setTarget(null);
  }

  public void showDescription() {
    if (_lastPanel != null) remove(_lastPanel);
    add(_description, BorderLayout.CENTER);
    _lastPanel = _description;
    validate();
    repaint();
  }

  public void showStep(JPanel ws) {
    if (_lastPanel != null) remove(_lastPanel);
    if (ws != null) {
      add(ws, BorderLayout.CENTER);
      _lastPanel = ws;
    }
    else {
      add(_description, BorderLayout.CENTER);
      _lastPanel = _description;
    }
    validate();
    repaint();
  }

  ////////////////////////////////////////////////////////////////
  // accessors
  public void setTarget(Object item) {  //ToDoItem
    _target = item;
    updateActionsEnabled();
    _description.setTarget(_target);
    Wizard w = null;
    if (_target instanceof ToDoItem) w = ((ToDoItem)_target).getWizard();
    if (w != null) showStep(w.getCurrentPanel());
    else { showDescription(); }
  }

  public Object getTarget() { return _target; }

  public void refresh() { setTarget(_target); }

  protected void updateActionsEnabled() {
    _actionResolve.updateEnabled(_target);
    _actionEmailExpert.updateEnabled(_target);
    _actionSnooze.updateEnabled(_target);
  }


} /* end class TabToDo */






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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8713.java