error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8927.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8927.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8927.java
text:
```scala
m@@odel = new Model("WindowExample");

// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
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


package uci.uml.test.omg;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;

/** This is a very simple demo of how to represent a UML design that
 *  consists of a single class.  This example is taken from page 25 of
 *  the UML 1.1 notation guide (OMG document ad/97-08-05). */


public class WindowExample {

  public Model model;
  public MMClass windowClass;
  public Vector tvs = new Vector();
  public TaggedValue tv1, tv2;
  public Vector ops = new Vector();
  public Operation op1, op2, op3, op4;
  public Vector attrs = new Vector();
  public Attribute at1, at2, at3, at4, at5;
  public MMClass areaType, rectType, xWindowPtrType;
  public DataType booleanType, voidType;
  
  public WindowExample() {
    try {
      model = new Model("default");
      
      windowClass = new MMClass("Window");
      windowClass.setIsAbstract(Boolean.TRUE);
      tvs.addElement(tv1 = new TaggedValue("author", "Joe"));
      tvs.addElement(tv2 = new TaggedValue("status", "tested"));
    
      booleanType = new DataType("Boolean");
      voidType = new DataType("void");
      areaType = new MMClass("Area");
      rectType = new MMClass("Rectangle");
      xWindowPtrType = new MMClass("Xwindow*");

      at1 = new Attribute("size", areaType, "(100, 100)");
      at1.setVisibility(VisibilityKind.PUBLIC);
      at2 = new Attribute("visibility", booleanType, "false");
      at2.setVisibility(VisibilityKind.PROTECTED);
      at3 = new Attribute("default-size", rectType);
      at3.setOwnerScope(ScopeKind.CLASSIFIER);
      at3.setVisibility(VisibilityKind.PUBLIC);
      at4 = new Attribute("maximum-size", rectType);
      at4.setVisibility(VisibilityKind.PROTECTED);
      at4.setOwnerScope(ScopeKind.CLASSIFIER);
      at5 = new Attribute("xprt", xWindowPtrType);
      at5.setVisibility(VisibilityKind.PRIVATE);
      attrs.addElement(at1);
      attrs.addElement(at2); 
      attrs.addElement(at3);
      attrs.addElement(at4);
      attrs.addElement(at5);
    
      op1 = new Operation(voidType, "display");
      op1.setVisibility(VisibilityKind.PUBLIC);
      op2 = new Operation(voidType, "hide");
      op2.setVisibility(VisibilityKind.PUBLIC);
      op3 = new Operation(voidType, "create");
      op3.setVisibility(VisibilityKind.PUBLIC);
      op3.setOwnerScope(ScopeKind.CLASSIFIER);
      op4 = new Operation(voidType, "attachXWindow", xWindowPtrType, "xwin");
      op4.setVisibility(VisibilityKind.PRIVATE);
      ops.addElement(op1);
      ops.addElement(op2);
      ops.addElement(op3);
      ops.addElement(op4);
    
      windowClass.setTaggedValue(tvs);
      windowClass.setStructuralFeature(attrs);
      windowClass.setBehavioralFeature(ops);

      model.addPublicOwnedElement(windowClass);

      System.out.println(windowClass.dbgString());
    }
    catch (PropertyVetoException ex) {
      System.out.println("an veto execption occured in WindowExample");
    }


  }

} /* end class WindowExample */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8927.java