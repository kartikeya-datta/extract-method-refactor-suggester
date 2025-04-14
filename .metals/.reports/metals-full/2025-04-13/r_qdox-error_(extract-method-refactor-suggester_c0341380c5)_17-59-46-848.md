error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1515.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1515.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1515.java
text:
```scala
L@@ayerPerspective lay = new LayerPerspectiveMutable(m.getName(), gm);

// File: UMLDeploymentDiagram.java
// Classes: UmlDeploymentDiagram
// Author: Clemens Eichler (5eichler@informatik.uni-hamburg.de)

package org.argouml.uml.diagram.deployment.ui;

import java.util.*;
import java.awt.*;
import java.beans.*;
import javax.swing.*;

import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.common_behavior.*;

import org.tigris.gef.base.*;
import org.tigris.gef.ui.*;

import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.deployment.*;

public class UMLDeploymentDiagram extends UMLDiagram {

  ////////////////
  // actions for toolbar

  protected static Action _actionMNode =
    new CmdCreateNode(MNodeImpl.class, "Node");

  protected static Action _actionMNodeInstance =
    new CmdCreateNode(MNodeInstanceImpl.class, "NodeInstance");

  protected static Action _actionMComponent = 
    new CmdCreateNode(MComponentImpl.class, "Component");

  protected static Action _actionMComponentInstance = 
    new CmdCreateNode(MComponentInstanceImpl.class, "ComponentInstance");

  protected static Action _actionMClass = 
    new CmdCreateNode(MClassImpl.class, "Class");

  protected static Action _actionMInterface = 
    new CmdCreateNode(MInterfaceImpl.class, "Interface");

  protected static Action _actionMObject =
    new CmdCreateNode(MObjectImpl.class, "Object");

  protected static Action _actionMDependency =			
    new CmdSetMode(ModeCreatePolyEdge.class,
		 "edgeClass", MDependencyImpl.class,					
		 "Dependency");							

  protected static Action _actionMAssociation =			
    new CmdSetMode(ModeCreatePolyEdge.class,
		 "edgeClass", MAssociationImpl.class,					
 		 "Association");							

  protected static Action _actionMLink =
    new CmdSetMode(ModeCreatePolyEdge.class,
		 "edgeClass", MLinkImpl.class,
		 "Link");


  ////////////////////////////////////////////////////////////////
  // contructors
  protected static int _DeploymentDiagramSerial = 1;


  public UMLDeploymentDiagram() {
    try { setName("deployment diagram " + _DeploymentDiagramSerial++); 
    }
    catch (PropertyVetoException pve) { }
  }

  public UMLDeploymentDiagram(MNamespace m) {
    this();
    setNamespace(m);
  }

  public void setNamespace(MNamespace m) {
    super.setNamespace(m);
    DeploymentDiagramGraphModel gm = new DeploymentDiagramGraphModel(); 
    gm.setNamespace(m);
    setGraphModel(gm);
    LayerPerspective lay = new LayerPerspective(m.getName(), gm);   
    setLayer(lay);
    DeploymentDiagramRenderer rend = new DeploymentDiagramRenderer(); // singleton
    lay.setGraphNodeRenderer(rend);
    lay.setGraphEdgeRenderer(rend);
  }


  /** initialize the toolbar for this diagram type */
  protected void initToolBar() {
    //System.out.println("making deployment toolbar");
    _toolBar = new ToolBar();
    _toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
//     _toolBar.add(Actions.Cut);
//     _toolBar.add(Actions.Copy);
//     _toolBar.add(Actions.Paste);
//     _toolBar.addSeparator();

    _toolBar.add(_actionSelect);
    _toolBar.add(_actionBroom);
    _toolBar.addSeparator();

    _toolBar.add(_actionMNode);
    _toolBar.add(_actionMNodeInstance);
    _toolBar.add(_actionMComponent);
    _toolBar.add(_actionMComponentInstance);
    _toolBar.add(_actionMDependency);
    _toolBar.add(_actionMClass);
    _toolBar.add(_actionMInterface);
    _toolBar.add(_actionMAssociation);
    _toolBar.add(_actionMObject); 
    _toolBar.add(_actionMLink);
// other actions
    _toolBar.addSeparator();

    _toolBar.add(_actionRectangle);
    _toolBar.add(_actionRRectangle);
    _toolBar.add(_actionCircle);
    _toolBar.add(_actionLine);
    _toolBar.add(_actionText);
    _toolBar.add(_actionPoly);
    _toolBar.add(_actionSpline);
    _toolBar.add(_actionInk);
    _toolBar.addSeparator();

    _toolBar.add(_diagramName);
  }

  static final long serialVersionUID = -375918274062198744L;

} /* end class UMLDeploymentDiagram */
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1515.java