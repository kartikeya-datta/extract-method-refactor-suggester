error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2498.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2498.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2498.java
text:
```scala
m@@odel = new Model("HumanResourcesExample");

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
 *  consists of three classes, three associations, and a note.  This
 *  example is taken from page 74 of the UML 1.1 notation guide (OMG
 *  document ad/97-08-05). */


public class HumanResourcesExample {
  public Model model;
  public MMClass personClass, companyClass, deptClass;
  public Vector personAttrs = new Vector();
  public Attribute at1, at2;
  public Association assoc1, assoc2, assoc3;
  public Constraint ageCalc, worksForCompanyConstraint;
  
  public HumanResourcesExample() {
    try {
      model = new Model("default");
      personClass = new MMClass("Person");
      companyClass = new MMClass("Company");
      deptClass = new MMClass("Deptartment");

      at1 = new Attribute("birthdate");
      at2 = new Attribute("age");
      at2.addStereotype(Stereotype.DERIVED);
    
      ageCalc = new Constraint(Name.UNSPEC, "age = currentDate - birthdate");
      at2.addConstraint(ageCalc);
    
      personClass.addStructuralFeature(at1);
      personClass.addStructuralFeature(at2);

      AssociationEnd ae11 =
	new AssociationEnd(new Name("employer"), companyClass,
			   Multiplicity.ONE, AggregationKind.AGG);
      AssociationEnd ae12 =
	new AssociationEnd(Name.UNSPEC, deptClass,
			   Multiplicity.ONE_OR_MORE, AggregationKind.NONE);

      AssociationEnd ae21 =
	new AssociationEnd(new Name("employer"), companyClass,
			   Multiplicity.ONE, AggregationKind.NONE);
      AssociationEnd ae22 =
	new AssociationEnd(Name.UNSPEC, personClass,
			   Multiplicity.ZERO_OR_MORE, AggregationKind.NONE);

      AssociationEnd ae31 =
	new AssociationEnd(new Name("deptartment"), deptClass,
			   Multiplicity.ONE, AggregationKind.NONE);
      AssociationEnd ae32 =
	new AssociationEnd(Name.UNSPEC, personClass,
			   Multiplicity.ZERO_OR_MORE, AggregationKind.NONE);


      assoc1 = new Association();
      assoc1.addConnection(ae11);
      assoc1.addConnection(ae12);
    
      assoc2 = new Association("WorksForCompany");
      assoc2.addConnection(ae21);
      assoc2.addConnection(ae22);
      assoc2.addStereotype(Stereotype.DERIVED);

      assoc3 = new Association("WorksForDepartment");
      assoc3.addConnection(ae31);
      assoc3.addConnection(ae32);

      worksForCompanyConstraint =
	new Constraint(Name.UNSPEC, "OCL",
		       "Person.employer=Person.department.employer");
      assoc3.addConstraint(worksForCompanyConstraint);
    
      System.out.println(personClass.dbgString());
      System.out.println(deptClass.dbgString());
      System.out.println(companyClass.dbgString());
      System.out.println(assoc1.dbgString());
      System.out.println(assoc2.dbgString());
      System.out.println(assoc3.dbgString());

      model.addPublicOwnedElement(companyClass);
      model.addPublicOwnedElement(personClass);
      model.addPublicOwnedElement(deptClass);
      model.addPublicOwnedElement(assoc1);
      model.addPublicOwnedElement(assoc2);
      model.addPublicOwnedElement(assoc3);
    }
    catch (PropertyVetoException ex) {
      System.out.println("an veto execption occured in HumanResourcesExample");
    }
  }

} /* end class HumanResourcesExample */


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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2498.java