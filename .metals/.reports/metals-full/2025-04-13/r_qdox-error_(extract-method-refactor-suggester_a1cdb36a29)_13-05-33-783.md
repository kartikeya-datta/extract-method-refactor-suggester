error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2147.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2147.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2147.java
text:
```scala
p@@ = new Parameter(rt, ParameterDirectionKind.IN, Parameter.RETURN_NAME);

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



package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.*;

import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Foundation.Data_Types.*;


public abstract class BehavioralFeature extends Feature {
  public boolean _isQuery;
  //% public Parameter parameter[];
  public Vector _parameter = new Vector();
  //% public MMException raisedException[];
  public Vector _raisedException = new Vector();

  public BehavioralFeature() { }
  public BehavioralFeature(Name name) { super(name); }
  public BehavioralFeature(String nameStr) { super(new Name(nameStr)); }

  public boolean getIsQuery() { return _isQuery; }
  public void setIsQuery(boolean x) throws PropertyVetoException {
    fireVetoableChange("isQuery", _isQuery, x);
    _isQuery = x;
  }

  public Classifier getReturnType()  {
    Vector params = getParameter();
    if (params != null) {
      java.util.Enumeration enum = params.elements();
      while (enum.hasMoreElements()) {
	Parameter p = (Parameter) enum.nextElement();
	if (Parameter.RETURN_NAME.equals(p.getName())) {
	  return p.getType();
	}
      }
    }
    return null;
  }

  public void setReturnType(Classifier rt) throws PropertyVetoException {
    Parameter p = findParameter(Parameter.RETURN_NAME);
    if (p == null) {
      p = new Parameter(rt, Parameter.RETURN_NAME);
      addParameter(p);
      //System.out.println("just set return type");
    }
    else {
      p.setType(rt);
    }
  }

  public Parameter findParameter(Name n) {
    Vector params = getParameter();
    if (params == null) return null;
    int size = params.size();
    for (int i = 0; i < size; i++) {
      Parameter p = (Parameter) params.elementAt(i);
      Name pName = p.getName();
      if (pName == null) continue;
      if (pName.equals(n)) return p;
    }
    return null; // not found
  }

  public Vector getParameter() { return (Vector) _parameter;}
  public void setParameter(Vector x) throws PropertyVetoException {
    if (_parameter == null) _parameter = new Vector();
    fireVetoableChangeNoCompare("parameter", _parameter, x);
    _parameter = x;
//     java.util.Enumeration enum = _parameter.elements();
//     while (enum.hasMoreElements()) {
//       Parameter p = (Parameter) enum.nextElement();
//       p.setNamespace(getNamespace());
//     }
  }

  /** needs-more-work: explicitly set the return parameter! */
  public void addParameter(Parameter x) throws PropertyVetoException {
    if (_parameter == null) _parameter = new Vector();
    fireVetoableChange("parameter", _parameter, x);
    _parameter.addElement(x);
    //x.setNamespace(getNamespace());
  }
  public void removeParameter(Parameter x) throws PropertyVetoException {
    if (_parameter == null) return;
    fireVetoableChange("parameter", _parameter, x);
    _parameter.removeElement(x);
  }

  public Vector getRaisedException() { return (Vector) _raisedException;}
  public void setRaisedException(Vector x) throws PropertyVetoException {
    if (_raisedException == null) _raisedException = new Vector();
    fireVetoableChangeNoCompare("raisedException", _raisedException, x);
    _raisedException = x;
  }
  public void addRaisedException(MMException x) throws PropertyVetoException {
    if (_raisedException == null) _raisedException = new Vector();
    fireVetoableChange("raisedException", _raisedException, x);
    _raisedException.addElement(x);
  }
  public void removeRaisedException(MMException x)
       throws PropertyVetoException {
    if (_raisedException == null) return;
    fireVetoableChange("raisedException", _raisedException, x);
    _raisedException.removeElement(x);
  }

  static final long serialVersionUID = 7697916359219415113L;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2147.java