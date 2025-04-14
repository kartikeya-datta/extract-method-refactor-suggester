error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5060.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5060.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5060.java
text:
```scala
public V@@ector _transitions = new Vector();

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




// Source file: Behavioral_Elements/State_Machines/StateMachine.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

/** By default, Statemachines are in the same Namespace as their
 *  context ModelElement, and Transitions and States and
 *  substatemachines within a StateMachine are in the same
 *  Namespace. */

public class StateMachine extends ModelElementImpl {
  public ModelElement _context;
  public State _top;
  //% public Transition _transitions[];
  public Vector _transitions;
  //% public SubmachineState _submachineState[];
  public Vector _submachineState;
  
  public StateMachine() { }
  public StateMachine(Name name) { super(name); }
  public StateMachine(Name name, ModelElement context) {
    super(name);
    try { setContext(context); }
    catch (PropertyVetoException pve) { }
  }
  public StateMachine(String nameStr) { super(new Name(nameStr)); }
  public StateMachine(String nameStr, ModelElement context) {
    this(new Name(nameStr), context);
  }

  public ModelElement getContext() { return _context; }
  public void setContext(ModelElement x) throws PropertyVetoException {
    if (_context == x) return;
    fireVetoableChange("context", _context, x);
    if (_context != null) _context.removeBehavior(this);
    _context = x;
    if (_context != null) {
      _context.addBehavior(this);
      //setNamespace(_context.getNamespace());
    }
  }

  public State getTop() { return _top; }
  public void setTop(State x) throws PropertyVetoException {
    if (_top == x) return;
    fireVetoableChange("top", _top, x);
    _top = x;
    _top.setStateMachine(this);
    _top.setNamespace(getNamespace());
  }

  public Vector getTransitions() { return _transitions; }
  public void setTransitions(Vector x) throws PropertyVetoException {
    if (_transitions == null) _transitions = new Vector();
    fireVetoableChangeNoCompare("transitions", _transitions, x);
    _transitions = x;
    java.util.Enumeration enum = _transitions.elements();
    while (enum.hasMoreElements()) {
      Transition t = (Transition) enum.nextElement();
      t.setNamespace(getNamespace());
    }
  }
  public void addTransition(Transition x) throws PropertyVetoException {
    if (_transitions == null) _transitions = new Vector();
    fireVetoableChange("transitions", _transitions, x);
    _transitions.addElement(x);
    x.setNamespace(getNamespace());
  }
  public void removeTransition(Transition x) throws PropertyVetoException {
    if (_transitions == null) return;
    fireVetoableChange("transitions", _transitions, x);
    _transitions.removeElement(x);
  }

  public Vector getSubmachineState() { return _submachineState; }
  public void setSubmachineState(Vector x) throws PropertyVetoException {
    if (_submachineState == null) _submachineState = new Vector();
    fireVetoableChangeNoCompare("submachineState", _submachineState, x);
    _submachineState = x;
    java.util.Enumeration enum = _submachineState.elements();
    while (enum.hasMoreElements()) {
      StateMachine sm = (StateMachine) enum.nextElement();
      sm.setNamespace(getNamespace());
    }
  }
  public void addSubmachineState(SubmachineState x) throws PropertyVetoException {
    if (_submachineState == null) _submachineState = new Vector();
    fireVetoableChange("submachineState", _submachineState, x);
    _submachineState.addElement(x);
    x.setNamespace(getNamespace());
  }
  public void removeSubmachineState(SubmachineState x)
       throws PropertyVetoException {
    if (_submachineState == null) return;
    fireVetoableChange("submachineState", _submachineState, x);
    _submachineState.removeElement(x);
  }

  ////////////////////////////////////////////////////////////////
  // debugging

  public String dbgString() {
    String s = "StateMachine " + (getName() == null?"(anon)":getName().getBody());
    s += " {\n";
    if (_top != null) {
      s += "\n\nStates ================\n";
      s += _top.dbgString();
    }
    if (_transitions != null) {
      s += "\n\nTransitions ================\n";
      java.util.Enumeration trans = _transitions.elements();
      while (trans.hasMoreElements()) {
	Transition t = (Transition) trans.nextElement();
	s += "  " + t.dbgString() + "\n";
      }
    }
    s += "}";
    return s;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5060.java