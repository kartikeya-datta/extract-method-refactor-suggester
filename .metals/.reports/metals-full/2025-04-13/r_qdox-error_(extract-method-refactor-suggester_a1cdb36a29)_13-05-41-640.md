error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9406.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9406.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9406.java
text:
```scala
t@@hrow new UnsupportedOperationException("ActionUtilities is just a container for static methods");

/*
 * ActionUtilities.java
 */
package org.argouml.swingext;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.JPopupMenu;

/**
 * A collection of utility methods for Swing Actions.
 *
 * @author Eugenio Alvarez
 */
public class ActionUtilities {

  private ActionUtilities() {
      throw new Error("ActionUtilities is just a container for static methods");
  }
  /**
   * Intended for use inside an <code>actionPerformed</code> method eg:
   * <pre>
   *     public void actionPerformed(ActionEvent ae) {
   *         Container appRoot = ActionUtilities.getActionRoot(ae);
   *     }
   * </pre>
   * Returns the root object, usually a <code>JFrame, JDialog or JApplet</code> that is the owner
   * of the source event source object (JMenu, JMenuItem, JPopupMenu etc).
   */
  public static Container getActionRoot(ActionEvent ae) {
    return ActionUtilities.getActionRoot(ae.getSource());
  } // getActionRoot()

  /**
   * Intended for use inside an <code>actionPerformed</code> method eg:
   * <pre>
   *     public void actionPerformed(ActionEvent e) {
   *         Container appRoot = ActionUtilities.getActionRoot(e.getSource());
   *     }
   * </pre>
   * @return the root object, usually a JFrame, JDialog or JApplet
   *	     that is the owner of the source event source object 
   *         (JMenu, JMenuItem, JPopupMenu etc).
   *         null if none is found.
   */
  public static Container getActionRoot(Object source) {
      Container container = null;
      if (source instanceof Component) {
        Component component = (Component)source;
        container = ActionUtilities.getContainer(component);
        if (container == null) {
          if (source instanceof Container) {
            return (Container)source;
          } // end if
          return null;
        } // end if
        while(ActionUtilities.getContainer(container) != null) {
          container = ActionUtilities.getContainer(container);
        } // end while
      } // end if
      return container;
  } // end getActionRoot()

  /**
   * Helper method to find the <code>Container</code> of <code>Component</code>.
   */
  private static Container getContainer(Component source) {
      Container container = source.getParent();
      if (container != null) {
        return container;
      }
      if (source instanceof JPopupMenu) {
        JPopupMenu jPopupMenu = (JPopupMenu)source;
        Component component = jPopupMenu.getInvoker();
        if (component instanceof Container) {
            container = (Container)component;
        } // end if
      } // end if
      return container;
  } // end getContainer()

} // end class ActionUtilities


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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9406.java