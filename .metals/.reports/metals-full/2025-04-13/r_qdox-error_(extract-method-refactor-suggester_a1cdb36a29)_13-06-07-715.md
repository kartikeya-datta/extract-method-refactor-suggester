error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8508.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8508.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8508.java
text:
```scala
O@@bject target = item;

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
import javax.swing.*;
import java.text.MessageFormat;

import ru.novosoft.uml.foundation.core.MModelElement;

import org.argouml.application.api.Argo;
import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.Critic;
import org.argouml.ui.targetmanager.TargetManager;

public class WizDescription extends WizStep {

  ////////////////////////////////////////////////////////////////
  // instance variables

  JTextArea _description = new JTextArea();


  public WizDescription() {
    super();
    Argo.log.info("making WizDescription");

    _description.setLineWrap(true);
    _description.setWrapStyleWord(true);

    _mainPanel.setLayout(new BorderLayout());
    _mainPanel.add(new JScrollPane(_description), BorderLayout.CENTER);
  }

  public void setTarget(Object item) {
    String message = "";
    super.setTarget(item);
    Object target = TargetManager.getInstance().getTarget();
    if (target == null) {
      _description.setText(Argo.localize("Cognitive", "message.no-item-selected"));
    }
    else if (target instanceof ToDoItem) {
      ToDoItem tdi = (ToDoItem) target;
      _description.setEnabled(true);
      _description.setText(tdi.getDescription());
      _description.setCaretPosition(0);
    }
    else if (target instanceof PriorityNode) {
      message = MessageFormat. 
                format(Argo.localize("Cognitive", "message.branch-priority"),
                       new Object [] { target.toString() });
      _description.setText(message);

      return;
    }
    else if (target instanceof Critic) {
      message = MessageFormat. 
                format(Argo.localize("Cognitive", "message.branch-critic"),
                       new Object [] { target.toString() });
      _description.setText(message);

      return;
    }
    else if (target instanceof MModelElement) {
      message = MessageFormat. 
                format(Argo.localize("Cognitive", "message.branch-model"),
                       new Object [] { target.toString() });
      _description.setText(message);

      return;
    }
    else if (target instanceof Decision) {
      message = MessageFormat. 
                format(Argo.localize("Cognitive", "message.branch-decision"),
                       new Object [] { target.toString() });
      _description.setText(message);

      return;
    }
    else if (target instanceof Goal) {
      message = MessageFormat. 
                format(Argo.localize("Cognitive", "message.branch-goal"),
                       new Object [] { target.toString() });
      _description.setText(message);

      return;
    }
    else if (target instanceof KnowledgeTypeNode) {
      message = MessageFormat. 
                format(Argo.localize("Cognitive", "message.branch-knowledge"),
                       new Object [] { target.toString() });
      _description.setText(message);

      return;
    }
    else {
      _description.setText("");
      return;
    }
  }



} /* end class WizDescription */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8508.java