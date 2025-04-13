error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3781.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3781.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3781.java
text:
```scala
c@@at.debug("TODO DeploymentDiagramRenderer getFigNodeFor");

package org.argouml.uml.diagram.deployment.ui;

import java.util.*;
import java.util.Enumeration;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.behavior.common_behavior.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;

import org.argouml.uml.diagram.ui.*;
import org.apache.log4j.Category;
import org.argouml.uml.diagram.static_structure.ui.*;

public class DeploymentDiagramRenderer
implements GraphNodeRenderer, GraphEdgeRenderer {
    protected static Category cat = Category.getInstance(DeploymentDiagramRenderer.class);

  /** Return a Fig that can be used to represent the given node */

  public FigNode getFigNodeFor(GraphModel gm, Layer lay, Object node) {
    if (node instanceof MNode) return new FigMNode(gm, node);
    else if (node instanceof MNodeInstance) return new FigMNodeInstance(gm, node);
    else if (node instanceof MComponent) return new FigComponent(gm, node); 
    else if (node instanceof MComponentInstance) return new FigComponentInstance(gm, node);
    else if (node instanceof MClass) return new FigClass(gm, node); 
    else if (node instanceof MInterface) return new FigInterface(gm, node); 
    else if (node instanceof MObject) return new FigObject(gm, node);
    cat.debug("needs-more-work DeploymentDiagramRenderer getFigNodeFor");
    return null;
  }

  /** Return a Fig that can be used to represent the given edge */
  public FigEdge getFigEdgeFor(GraphModel gm, Layer lay, Object edge) {

    if (edge instanceof MAssociation) {
      MAssociation asc = (MAssociation) edge;
      FigAssociation ascFig = new FigAssociation(asc, lay);
      return ascFig;
    }
    if (edge instanceof MLink) {
      MLink lnk = (MLink) edge;
      FigLink lnkFig = new FigLink(lnk);
      Collection linkEnds = lnk.getConnections();
      if (linkEnds == null) cat.debug("null linkRoles....");
	  Object[] leArray = linkEnds.toArray();
      MLinkEnd fromEnd = (MLinkEnd) leArray[0];
      MInstance fromInst = fromEnd.getInstance();
      MLinkEnd toEnd = (MLinkEnd) leArray[1];
      MInstance toInst = toEnd.getInstance();
      FigNode fromFN = (FigNode) lay.presentationFor(fromInst);
      FigNode toFN = (FigNode) lay.presentationFor(toInst);
      lnkFig.setSourcePortFig(fromFN);
      lnkFig.setSourceFigNode(fromFN);
      lnkFig.setDestPortFig(toFN);
      lnkFig.setDestFigNode(toFN);
      return lnkFig;
    }
    if (edge instanceof MDependency) {
      MDependency dep = (MDependency) edge;
      FigDependency depFig = new FigDependency(dep);

      MModelElement supplier = (MModelElement)(((dep.getSuppliers().toArray())[0]));
      MModelElement client = (MModelElement)(((dep.getClients().toArray())[0]));

      FigNode supFN = (FigNode) lay.presentationFor(supplier);
      FigNode cliFN = (FigNode) lay.presentationFor(client);

      depFig.setSourcePortFig(cliFN);
      depFig.setSourceFigNode(cliFN);
      depFig.setDestPortFig(supFN);
      depFig.setDestFigNode(supFN);
      depFig.getFig().setDashed(true);
      return depFig;
    }

    return null;
  }

  static final long serialVersionUID = 8002278834226522224L;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3781.java