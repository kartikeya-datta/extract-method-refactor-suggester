error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5400.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5400.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5400.java
text:
```scala
S@@ystem.out.println("valueForPathChanged NavPerspective");

// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.

package uci.uml.ui.nav;

import java.util.*;
import java.awt.*;
import java.io.Serializable;
import com.sun.java.swing.tree.*;
import com.sun.java.swing.event.*;
import uci.uml.ui.*;

/** This defines a NavPerspective as a kind of TreeModel that is made
 *  up of rules from the files whose names begin with "Go".  It also
 *  defines several useful navigational perspectives. */

public class NavPerspective extends TreeModelComposite
implements Serializable, TreeModel, Cloneable {

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected EventListenerList _listenerList = new EventListenerList();


  ////////////////////////////////////////////////////////////////
  // static variables

  protected static Vector _registeredPerspectives = new Vector();
  protected static Vector _rules = new Vector();


  static {
    // this are meant for pane-1 of NavigatorPane, they all have
    // Project as their only prerequiste.  Thesee trees tend to be 3
    // to 5 levels deep and sometimes have recursion.
    NavPerspective packageCentric = new NavPerspective("Package-centric");
    NavPerspective diagramCentric = new NavPerspective("Diagram-centric");
    NavPerspective inheritanceCentric = new NavPerspective("Inheritance-centric");
    NavPerspective classAssociation = new NavPerspective("Class Associations");
    NavPerspective navAssociation = new NavPerspective("Navigable Associations");
    NavPerspective associationCentric = new NavPerspective("Association-centric");
    NavPerspective aggregateCentric = new NavPerspective("Aggregate-centric");
    NavPerspective compositeCentric = new NavPerspective("Composite-centric");
    NavPerspective classStates = new NavPerspective("Class states");
    NavPerspective stateCentric = new NavPerspective("State-centric");
    NavPerspective stateTransitions = new NavPerspective("State-transitions");
    NavPerspective transitionCentric = new NavPerspective("Transitions-centric");
    NavPerspective transitionPaths = new NavPerspective("Transitions paths");
    NavPerspective useCaseCentric = new NavPerspective("UseCase-centric");
    NavPerspective depCentric = new NavPerspective("Dependency-centric");


    // These are intended for pane-2 of NavigatorPane, the tend to be
    // simple and shallow, and have something in pane-1 as a prerequiste
    NavPerspective classToBehStr = new NavPerspective("Features of Class");
    NavPerspective classToBeh = new NavPerspective("Methods of Class");
    NavPerspective classToStr = new NavPerspective("Attributes of Class");
    NavPerspective machineToState = new NavPerspective("States of Class");
    NavPerspective machineToTransition = new NavPerspective("Transitions of Class");

    packageCentric.addSubTreeModel(new GoProjectModel());
    packageCentric.addSubTreeModel(new GoModelToDiagram());
    packageCentric.addSubTreeModel(new GoModelToElements());
    packageCentric.addSubTreeModel(new GoClassifierToBeh());
    packageCentric.addSubTreeModel(new GoClassifierToStr());

    diagramCentric.addSubTreeModel(new GoProjectDiagram());
    diagramCentric.addSubTreeModel(new GoDiagramToNode());
    diagramCentric.addSubTreeModel(new GoDiagramToEdge());
    diagramCentric.addSubTreeModel(new GoClassifierToBeh());
    diagramCentric.addSubTreeModel(new GoClassifierToStr());

    inheritanceCentric.addSubTreeModel(new GoProjectModel());
    inheritanceCentric.addSubTreeModel(new GoModelToBaseElements());
    inheritanceCentric.addSubTreeModel(new GoGenElementToDerived());

    classAssociation.addSubTreeModel(new GoProjectModel());
    classAssociation.addSubTreeModel(new GoModelToDiagram());
    classAssociation.addSubTreeModel(new GoModelToClass());
    //classAssociation.addSubTreeModel(new GoClassifierToBeh());
    //classAssociation.addSubTreeModel(new GoClassifierToStr());
    classAssociation.addSubTreeModel(new GoClassToAssocdClass());

    navAssociation.addSubTreeModel(new GoProjectModel());
    navAssociation.addSubTreeModel(new GoModelToDiagram());
    navAssociation.addSubTreeModel(new GoModelToClass());
    //navAssociation.addSubTreeModel(new GoClassifierToBeh());
    //navAssociation.addSubTreeModel(new GoClassifierToStr());
    navAssociation.addSubTreeModel(new GoClassToNavigableClass());

    aggregateCentric.addSubTreeModel(new GoProjectModel());
    aggregateCentric.addSubTreeModel(new GoModelToDiagram());
    aggregateCentric.addSubTreeModel(new GoModelToClass());
    aggregateCentric.addSubTreeModel(new GoClassToAggrClass());

    compositeCentric.addSubTreeModel(new GoProjectModel());
    compositeCentric.addSubTreeModel(new GoModelToDiagram());
    compositeCentric.addSubTreeModel(new GoModelToClass());
    compositeCentric.addSubTreeModel(new GoClassToCompositeClass());


    associationCentric.addSubTreeModel(new GoProjectModel());
    associationCentric.addSubTreeModel(new GoModelToDiagram());
    associationCentric.addSubTreeModel(new GoModelToAssociation());
    associationCentric.addSubTreeModel(new GoAssocToSource());
    associationCentric.addSubTreeModel(new GoAssocToTarget());

//     classStates.addSubTreeModel(new GoProjectModel());
//     classStates.addSubTreeModel(new GoModelToDiagram());
//     classStates.addSubTreeModel(new GoModelToClass());
//     classStates.addSubTreeModel(new GoElementToMachine());
//     classStates.addSubTreeModel(new GoMachineToState());

    stateCentric.addSubTreeModel(new GoProjectModel());
    stateCentric.addSubTreeModel(new GoModelToDiagram());
    stateCentric.addSubTreeModel(new GoModelToClass());
    stateCentric.addSubTreeModel(new GoElementToMachine());
    stateCentric.addSubTreeModel(new GoMachineToState());
    stateCentric.addSubTreeModel(new GoStateToIncomingTrans());
    stateCentric.addSubTreeModel(new GoStateToOutgoingTrans());

    transitionCentric.addSubTreeModel(new GoProjectModel());
    transitionCentric.addSubTreeModel(new GoModelToDiagram());
    transitionCentric.addSubTreeModel(new GoModelToClass());
    transitionCentric.addSubTreeModel(new GoElementToMachine());
    transitionCentric.addSubTreeModel(new GoMachineToTrans());
    transitionCentric.addSubTreeModel(new GoTransToSourceState());
    transitionCentric.addSubTreeModel(new GoTransToTargetState());

    transitionPaths.addSubTreeModel(new GoProjectModel());
    transitionPaths.addSubTreeModel(new GoModelToDiagram());
    transitionPaths.addSubTreeModel(new GoModelToClass());
    transitionPaths.addSubTreeModel(new GoElementToMachine());

    //transitionPaths.addSubTreeModel(new GoMachineToStartState());
    GoFilteredChildren machineToFinalState =
      new GoFilteredChildren("State Machine->Final States",
			     new GoMachineToState(),
			     PredIsFinalState.TheInstance);
    GoFilteredChildren machineToInitialState =
      new GoFilteredChildren("State Machine->Initial States",
			     new GoMachineToState(),
			     PredIsStartState.TheInstance);
    transitionPaths.addSubTreeModel(machineToInitialState);

    GoFilteredChildren compositeToFinalStates =
      new GoFilteredChildren("State->Final Substates",
			     new GoStateToSubstate(),
			     PredIsFinalState.TheInstance);
    GoFilteredChildren compositeToInitialStates =
      new GoFilteredChildren("State->Initial Substates",
			     new GoStateToSubstate(),
			     PredIsStartState.TheInstance);
    transitionPaths.addSubTreeModel(compositeToInitialStates);

    transitionPaths.addSubTreeModel(new GoStateToDownstream());
//     transitionPaths.addSubTreeModel(new GoStateToOutgoingTrans());
//     transitionPaths.addSubTreeModel(new GoTransToTargetState());

    useCaseCentric.addSubTreeModel(new GoProjectModel());
    useCaseCentric.addSubTreeModel(new GoModelToDiagram());
    useCaseCentric.addSubTreeModel(new GoModelToUseCase());
    useCaseCentric.addSubTreeModel(new GoModelToActor());

    depCentric.addSubTreeModel(new GoProjectModel());
    depCentric.addSubTreeModel(new GoModelToDiagram());
    depCentric.addSubTreeModel(new GoModelToElements());
    depCentric.addSubTreeModel(new GoElementToDependentElement());

    classToBehStr.addSubTreeModel(new GoClassifierToStr());
    classToBehStr.addSubTreeModel(new GoClassifierToBeh());

    classToBeh.addSubTreeModel(new GoClassifierToBeh());

    classToStr.addSubTreeModel(new GoClassifierToStr());

    machineToState.addSubTreeModel(new GoMachineToState());

    machineToTransition.addSubTreeModel(new GoMachineToTrans());

    registerPerspective(packageCentric);
    registerPerspective(diagramCentric);
    registerPerspective(inheritanceCentric);
    registerPerspective(classAssociation);
    registerPerspective(navAssociation);
    registerPerspective(associationCentric);
//     registerPerspective(classStates);
    registerPerspective(stateCentric);
    registerPerspective(transitionCentric);
    registerPerspective(transitionPaths);
    registerPerspective(useCaseCentric);
    registerPerspective(depCentric);

    registerRule(new GoProjectModel());
    registerRule(new GoModelToDiagram());
    registerRule(new GoModelToElements());
    registerRule(new GoModelToClass());
    registerRule(new GoModelToAssociation());
    registerRule(new GoModelToBaseElements());
    registerRule(new GoProjectDiagram());
    registerRule(new GoClassifierToBeh());
    registerRule(new GoClassifierToStr());
    registerRule(new GoDiagramToNode());
    registerRule(new GoDiagramToEdge());
    registerRule(new GoGenElementToDerived());
    registerRule(new GoClassToAssocdClass());
    registerRule(new GoClassToAggrClass());
    registerRule(new GoClassToCompositeClass());
    registerRule(new GoElementToMachine());
    registerRule(new GoMachineToTrans());
    registerRule(new GoMachineToState());
    registerRule(machineToInitialState);
    registerRule(machineToFinalState);
    registerRule(new GoStateToSubstate());
    registerRule(compositeToInitialStates);
    registerRule(compositeToFinalStates);
    registerRule(new GoStateToIncomingTrans());
    registerRule(new GoStateToOutgoingTrans());
    registerRule(new GoStateToDownstream());
    registerRule(new GoStateToUpstream());
    registerRule(new GoTransToSourceState());
    registerRule(new GoTransToTargetState());
    registerRule(new GoModelToActor());
    registerRule(new GoModelToUseCase());
    registerRule(new GoAssocToTarget());
    registerRule(new GoAssocToSource());
    registerRule(new GoAssocToSource());
  }
  
  ////////////////////////////////////////////////////////////////
  // static methods

  public static void registerPerspective(NavPerspective np) {
    _registeredPerspectives.addElement(np);
  }

  public static void unregisterPerspective(NavPerspective np) {
    _registeredPerspectives.removeElement(np);
  }

  public static Vector getRegisteredPerspectives() {
    return _registeredPerspectives;
  }

  public static void registerRule(TreeModelPrereqs rule) {
    _rules.addElement(rule);
  }

  public static Vector getRegisteredRules() { return _rules; }
  
  ////////////////////////////////////////////////////////////////
  // constructor

  public NavPerspective(String name) { super(name); }

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  
  ////////////////////////////////////////////////////////////////
  // TreeModel implementation
  

  /**
   * Messaged when the user has altered the value for the item identified
   * by <I>path</I> to <I>newValue</I>.  If <I>newValue</I> signifies
   * a truly new value the model should post a treeNodesChanged
   * event.
   *
   * @param path path to the node that the user has altered.
   * @param newValue the new value from the TreeCellEditor.
   */
  public void valueForPathChanged(TreePath path, Object newValue) {
    // needs-more-work 
  }


  //
  //  Change Events
  //

  /*
   * Notify all listeners that have registered interest for
   * notification on this event type.  The event instance 
   * is lazily created using the parameters passed into 
   * the fire method.
   * @see EventListenerList
   */
  protected void fireTreeNodesChanged(Object source, Object[] path, 
				      int[] childIndices, 
				      Object[] children) {
    // Guaranteed to return a non-null array
    Object[] listeners = _listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==TreeModelListener.class) {
	// Lazily create the event:
	if (e == null)
	  e = new TreeModelEvent(source, path, 
				 childIndices, children);
	((TreeModelListener)listeners[i+1]).treeNodesChanged(e);
      }          
    }
  }

  /*
   * Notify all listeners that have registered interest for
   * notification on this event type.  The event instance 
   * is lazily created using the parameters passed into 
   * the fire method.
   * @see EventListenerList
   */
  protected void fireTreeNodesInserted(Object source, Object[] path, 
				       int[] childIndices, 
				       Object[] children) {
    // Guaranteed to return a non-null array
    Object[] listeners = _listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==TreeModelListener.class) {
	// Lazily create the event:
	if (e == null)
	  e = new TreeModelEvent(source, path, 
				 childIndices, children);
	((TreeModelListener)listeners[i+1]).treeNodesInserted(e);
      }          
    }
  }

  /*
   * Notify all listeners that have registered interest for
   * notification on this event type.  The event instance 
   * is lazily created using the parameters passed into 
   * the fire method.
   * @see EventListenerList
   */
  protected void fireTreeNodesRemoved(Object source, Object[] path, 
				      int[] childIndices, 
				      Object[] children) {
    // Guaranteed to return a non-null array
    Object[] listeners = _listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==TreeModelListener.class) {
	// Lazily create the event:
	if (e == null)
	  e = new TreeModelEvent(source, path, 
				 childIndices, children);
	((TreeModelListener)listeners[i+1]).treeNodesRemoved(e);
      }          
    }
  }


  protected void fireTreeStructureChanged(Object source, Object[] path) {
    fireTreeStructureChanged(source, path, null, null);
  }

  /*
   * Notify all listeners that have registered interest for
   * notification on this event type.  The event instance 
   * is lazily created using the parameters passed into 
   * the fire method.
   * @see EventListenerList
   */
  protected void fireTreeStructureChanged(Object source, Object[] path, 
					  int[] childIndices, 
					  Object[] children) {
    // Guaranteed to return a non-null array
    Object[] listeners = _listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==TreeModelListener.class) {
	// Lazily create the event:
	if (e == null)
	  e = new TreeModelEvent(source, path, 
				 childIndices, children);
	((TreeModelListener)listeners[i+1]).treeStructureChanged(e);
      }          
    }
  }


  public void addTreeModelListener(TreeModelListener l) {
    _listenerList.add(TreeModelListener.class, l);
  }

  public void removeTreeModelListener(TreeModelListener l) {
    _listenerList.remove(TreeModelListener.class, l);
  }

} /* end class NavPerspective */
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5400.java