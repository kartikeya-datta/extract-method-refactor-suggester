error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9948.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9948.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9948.java
text:
```scala
"Model",@@ "Subsystem", "Collaboration", "Permission", "Actor", "Node", "NodeInstance", "Link" };

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


package org.argouml.uml.ui;
import javax.swing.*;
import java.awt.event.*;
import java.lang.reflect.*;
import ru.novosoft.uml.*;

public class UMLMetaclassComboBox extends JComboBox implements UMLUserInterfaceComponent, ItemListener {

  private String[] _metaclasses =
    { "ModelElement", "Classifier", "Class", "Interface", "DataType", "Exception", "Signal",
        "Association", "AssociationEnd", "Attribute", "Operation", "Generalization", "Flow", "Usage", "BehavioralFeature",
        "CallEvent", "Abstraction", "Component", "Package", "Constraint", "Comment","ObjectFlowState",
        "Model", "Subsystem", "Collaboration", "Permission", "Actor" };

  private Method _getMethod;
  private Method _setMethod;
  private String _property;
  private UMLUserInterfaceContainer _container;
  private Object[] _noArgs = {};

  public UMLMetaclassComboBox(UMLUserInterfaceContainer container,String property,String getMethod,String setMethod) {
    setModel(new DefaultComboBoxModel(_metaclasses));
    _container = container;
    _property = property;
    addItemListener(this);
    try {
      _getMethod = container.getClass().getMethod(getMethod,new Class[] { });
    }
    catch(Exception e) {
      System.out.println("Error in UMLMetaclassComboBox:" + getMethod);
      e.printStackTrace();
    }
    try {
      _setMethod = container.getClass().getMethod(setMethod,new Class[] { String.class });
    }
    catch(Exception e) {
      System.out.println("Error in UMLMetaclassComboBox:" + setMethod);
      e.printStackTrace();
    }
  }

  public void propertySet(MElementEvent e) {
    String eventName = e.getName();
    if(eventName == null || _property == null || eventName.equals(_property)) {
      update();
    }
  }

  public void roleAdded(MElementEvent e) {
  }

  public void roleRemoved(MElementEvent e) {
  }

  public void listRoleItemSet(MElementEvent e) {
  }

  public void removed(MElementEvent e) {
  }

  public void recovered(MElementEvent e) {
  }

  public void targetChanged() {
    update();
  }

  public void targetReasserted() {
  }

  private void update() {
    String meta = "ModelElement";
    try {
      meta = (String) _getMethod.invoke(_container,_noArgs);
    }
    catch(Exception e) {
    }
    ComboBoxModel model = getModel();
    Object element;
    int size =  model.getSize();
    int i = 0;
    for(i = 0; i < size; i++) {
      element = model.getElementAt(i);
      if(element.equals(meta)) {
        model.setSelectedItem(element);
        break;
      }
    }
    if(i == size) {
      model.setSelectedItem(model.getElementAt(0));
    }
  }

  public void itemStateChanged(ItemEvent event) {
    if(event.getStateChange() == ItemEvent.SELECTED) {
      Object selectedItem = event.getItem();
      try {
        _setMethod.invoke(_container,new Object[] { selectedItem.toString() });
      }
      catch(Exception e) {
      }
    }
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9948.java