error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8063.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8063.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8063.java
text:
```scala
i@@ncomplete.setIsConcurrent(true);

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




package uci.uml.test.omg;

import java.util.*;
import java.beans.*;


import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Model_Management.*;

/** This is a very simple demo of how to represent a UML state machine
 *  that deals with dialing a telephone.  This example is taken from
 *  page 108 of the UML 1.1 notation guide (OMG document ad/97-08-05). */


public class ConcurrentSubstatesExample {
  public Model model;
  public MMClass courseClass;
  public CompositeState taking, incomplete, labTrack, projTrack, testTrack;
  public Pseudostate takingInitial, labInitial, projInitial,
    testInitial, labFinal, projFinal, testFinal;
  public State lab1, lab2, termProject, finalTest;
  public State passed, failed;

  public Transition t01, t02, t03, t04, t05, t06, t07, t08, t09, t10;
  public StateMachine sm;
  
  public ConcurrentSubstatesExample() {
    try {
      model = new Model("ConcurrentSubstatesExample");
      courseClass = new MMClass("Course");
      sm = new StateMachine("States", courseClass);
      
      taking = new CompositeState("Taking Class");
      
      taking.addSubstate(incomplete = new CompositeState("Incomplete"));
      taking.addSubstate(passed = new SimpleState("Passed"));
      taking.addSubstate(failed = new SimpleState("Failed"));
      taking.addSubstate(takingInitial = new Pseudostate(PseudostateKind.INITIAL));
      
      incomplete.setIsConcurent(true);
      incomplete.addSubstate(labTrack = new CompositeState("Lab Track"));
      incomplete.addSubstate(projTrack = new CompositeState("Term Project Track"));
      incomplete.addSubstate(testTrack = new CompositeState("Final Test Track"));

      labTrack.addSubstate(labInitial = new Pseudostate(PseudostateKind.INITIAL));
      labTrack.addSubstate(lab1 = new SimpleState("Lab1"));
      labTrack.addSubstate(lab2 = new SimpleState("Lab2"));
      labTrack.addSubstate(labFinal = new Pseudostate(PseudostateKind.FINAL));
      
      projTrack.addSubstate(projInitial = new Pseudostate(PseudostateKind.INITIAL));
      projTrack.addSubstate(termProject = new SimpleState("Term Project"));
      projTrack.addSubstate(projFinal = new Pseudostate(PseudostateKind.FINAL));

      testTrack.addSubstate(testInitial = new Pseudostate(PseudostateKind.INITIAL));
      testTrack.addSubstate(finalTest = new SimpleState("Final Test"));
      testTrack.addSubstate(testFinal = new Pseudostate(PseudostateKind.FINAL));


      sm.setTop(taking);


      // these go roughtly top to bottom, left to righ in the figure
      t01 = new Transition(takingInitial, incomplete);
      
      t02 = new Transition(labInitial, lab1);
      
      t03 = new Transition(lab1, lab2);
      t03.setTrigger(new Event("lab done"));

      t04 = new Transition(lab2, labFinal);
      t04.setTrigger(new Event("lab done"));

      t05 = new Transition(projInitial, termProject);

      t06 = new Transition(termProject, projFinal);
      t06.setTrigger(new Event("project done"));
      
      t07 = new Transition(testInitial, finalTest);

      t08 = new Transition(finalTest, testFinal);
      t08.setTrigger(new Event("pass"));

      t09 = new Transition(finalTest, failed);
      t09.setTrigger(new Event("fail"));

      t10 = new Transition(incomplete, passed);

      sm.addTransition(t01);
      sm.addTransition(t02);
      sm.addTransition(t03);
      sm.addTransition(t04);
      sm.addTransition(t05);
      sm.addTransition(t06);
      sm.addTransition(t07);
      sm.addTransition(t08);
      sm.addTransition(t09);
      sm.addTransition(t10);

      model.addPublicOwnedElement(courseClass);
    }
    catch (PropertyVetoException ex) {
      System.out.println("an veto execption in ConcurrentSubstatesExample");
    }
  }

  public void print() {       
    System.out.println(courseClass.dbgString());
    System.out.println(sm.dbgString());
  }


} /* end class ConcurrentSubstatesExample */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8063.java