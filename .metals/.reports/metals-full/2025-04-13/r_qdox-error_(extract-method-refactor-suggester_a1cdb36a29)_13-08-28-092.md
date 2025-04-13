error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8915.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8915.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8915.java
text:
```scala
s@@uper("todo.perspective.decision");

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

import java.util.*;

import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;

import org.tigris.gef.util.*;

import org.argouml.cognitive.*;

public class ToDoByDecision extends ToDoPerspective
implements ToDoListListener {


  public ToDoByDecision() {
    super("By Decision");
    addSubTreeModel(new GoListToDecisionsToItems());
  }

  //public String toString() { return "Decision"; }

  ////////////////////////////////////////////////////////////////
  // DecisionModelListener implementation

  ////////////////////////////////////////////////////////////////
  // ToDoListListener implementation

  public void toDoItemsChanged(ToDoListEvent tde) {
    System.out.println("toDoItemChanged");
    Vector items = tde.getToDoItems();
    int nItems = items.size();
    Object path[] = new Object[2];
    path[0] = Designer.TheDesigner.getToDoList();

    Vector decs = Designer.TheDesigner.getDecisions();
    java.util.Enumeration enum = decs.elements();
    while (enum.hasMoreElements()) {
      Decision dec = (Decision) enum.nextElement();
      int nMatchingItems = 0;
      path[1] = dec;
      for (int i = 0; i < nItems; i++) {
	ToDoItem item = (ToDoItem) items.elementAt(i);
	if (!item.supports(dec)) continue;
	nMatchingItems++;
      }
      if (nMatchingItems == 0) continue;
      int childIndices[] = new int[nMatchingItems];
      Object children[] = new Object[nMatchingItems];
      nMatchingItems = 0;
      for (int i = 0; i < nItems; i++) {
	ToDoItem item = (ToDoItem) items.elementAt(i);
	if (!item.supports(dec)) continue;
	childIndices[nMatchingItems] = getIndexOfChild(dec, item);
	children[nMatchingItems] = item;
	nMatchingItems++;
      }
      fireTreeNodesChanged(this, path, childIndices, children);
    }
  }

  public void toDoItemsAdded(ToDoListEvent tde) {
    //System.out.println("toDoItemAdded");
    Vector items = tde.getToDoItems();
    int nItems = items.size();
    Object path[] = new Object[2];
    path[0] = Designer.TheDesigner.getToDoList();

    Vector decs = Designer.TheDesigner.getDecisions();
    java.util.Enumeration enum = decs.elements();
    while (enum.hasMoreElements()) {
      Decision dec = (Decision) enum.nextElement();
      int nMatchingItems = 0;
      path[1] = dec;
      for (int i = 0; i < nItems; i++) {
	ToDoItem item = (ToDoItem) items.elementAt(i);
	if (!item.supports(dec)) continue;
	nMatchingItems++;
      }
      if (nMatchingItems == 0) continue;
      int childIndices[] = new int[nMatchingItems];
      Object children[] = new Object[nMatchingItems];
      nMatchingItems = 0;
      for (int i = 0; i < nItems; i++) {
	ToDoItem item = (ToDoItem) items.elementAt(i);
	if (!item.supports(dec)) continue;
	childIndices[nMatchingItems] = getIndexOfChild(dec, item);
	children[nMatchingItems] = item;
	nMatchingItems++;
      }
      fireTreeNodesInserted(this, path, childIndices, children);
    }
  }

  public void toDoItemsRemoved(ToDoListEvent tde) {
    //System.out.println("toDoItemRemoved");
    ToDoList list = Designer.TheDesigner.getToDoList(); //source?
    Vector items = tde.getToDoItems();
    int nItems = items.size();
    Object path[] = new Object[2];
    path[0] = Designer.TheDesigner.getToDoList();

    Vector decs = Designer.TheDesigner.getDecisions();
    java.util.Enumeration enum = decs.elements();
    while (enum.hasMoreElements()) {
      Decision dec = (Decision) enum.nextElement();
      //System.out.println("toDoItemRemoved updating decision node!");
      boolean anyInDec = false;
      for (int i = 0; i < nItems; i++) {
	ToDoItem item = (ToDoItem) items.elementAt(i);
	if (item.supports(dec)) anyInDec = true;
      }
      if (!anyInDec) continue;
      path[1] = dec;
      //fireTreeNodesChanged(this, path, childIndices, children);
      fireTreeStructureChanged(path);
    }
  }

  public void toDoListChanged(ToDoListEvent tde) { }


//   public static Decision decisionUNCATEGORIZED =
//   new Decision("Uncategorized", 1);

//   protected boolean isNeeded(ToDoPseudoNode node) {
//     PredicateDecision pd = (PredicateDecision) node.getPredicate();
//     Decision d = pd.getDecision();
//     java.util.Enumeration items = _root.elements();
//     while (items.hasMoreElements()) {
//       ToDoItem item = (ToDoItem) items.nextElement();
//       if (item.getPoster().supports(d)) return true;
//     }
//     return false;
//   }

//   protected Vector addNewPseudoNodes(ToDoItem item) {
//     Vector newNodes = new Vector();
//     Vector decs = item.getPoster().getSupportedDecisions();
//     if (decs == null) {
//       addNodeIfNeeded(Decision.UNSPEC, newNodes);
//     }
//     else {
//       java.util.Enumeration enum = decs.elements();
//       while (enum.hasMoreElements()) {
// 	Decision itemDec = (Decision) enum.nextElement();
// 	addNodeIfNeeded(itemDec, newNodes);
//       }
//     }
//     return newNodes;
//   }


//   protected void addNodeIfNeeded(Decision itemDec, Vector newNodes) {
//     java.util.Enumeration enum2 = _pseudoNodes.elements();
//     while (enum2.hasMoreElements()) {
//       ToDoPseudoNode node = (ToDoPseudoNode) enum2.nextElement();
//       PredicateDecision pd = (PredicateDecision) node.getPredicate();
//       Decision nodeDec = pd.getDecision();
//       //if (nodeDec.getName().equals(itemDec.getName())) return;
//       if (nodeDec == itemDec) return;
//     }
//     PredicateDecision pred = new PredicateDecision(itemDec);
//     ToDoPseudoNode newNode = new ToDoPseudoNode(_root, pred);
//     newNode.setLabel(itemDec.getName());
//     _pseudoNodes.addElement(newNode);
//     newNodes.addElement(newNode);
//   }

} /* end class ToDoByDecision */


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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8915.java