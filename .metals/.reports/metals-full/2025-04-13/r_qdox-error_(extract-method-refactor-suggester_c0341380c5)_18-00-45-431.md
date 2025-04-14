error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2895.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2895.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2895.java
text:
```scala
i@@f (icon == null) ; //System.out.println("UMLTreeCellRenderer: using default Icon for " + cName);

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

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.util.*;

/** UMTreeCellRenderer determines how the entries in the Navigationpane will be
 *  represented graphically. In particular it makes decisions about the icons
 *  to use in order to display a Navigationpane artifact depending on the kind
 *  of object to be displayed.
 */
public class UMLTreeCellRenderer extends DefaultTreeCellRenderer {
  ////////////////////////////////////////////////////////////////
  // class variables

  protected ImageIcon _ActionStateIcon = ResourceLoader.lookupIconResource("ActionState");
  protected ImageIcon _StateIcon = ResourceLoader.lookupIconResource("State");
  protected ImageIcon _InitialStateIcon = ResourceLoader.lookupIconResource("Initial");
  protected ImageIcon _DeepIcon = ResourceLoader.lookupIconResource("DeepHistory");
  protected ImageIcon _ShallowIcon = ResourceLoader.lookupIconResource("ShallowHistory");
  protected ImageIcon _ForkIcon = ResourceLoader.lookupIconResource("Fork");
  protected ImageIcon _JoinIcon = ResourceLoader.lookupIconResource("Join");
  protected ImageIcon _BranchIcon = ResourceLoader.lookupIconResource("Branch");
  protected ImageIcon _FinalStateIcon = ResourceLoader.lookupIconResource("FinalState");

  protected ImageIcon _RealizeIcon = ResourceLoader.lookupIconResource("Realization");

  protected ImageIcon _SignalIcon = ResourceLoader.lookupIconResource("SignalSending");

  protected ImageIcon _CommentIcon = ResourceLoader.lookupIconResource("Note");
  
  protected Hashtable _iconCache = new Hashtable();



  ////////////////////////////////////////////////////////////////
  // TreeCellRenderer implementation

  public Component getTreeCellRendererComponent(JTree tree, Object value,
						boolean sel,
						boolean expanded,
						boolean leaf, int row,
                                                boolean hasFocus) {

      Component r = super.getTreeCellRendererComponent(tree, value, sel,
                                                       expanded, leaf,
                                                       row, hasFocus);

      if (r instanceof JLabel) {
          JLabel lab = (JLabel) r;
          Icon icon = (Icon) _iconCache.get(value.getClass());

          if (value instanceof MPseudostate) {
              MPseudostate ps = (MPseudostate) value;
              MPseudostateKind kind = ps.getKind();
              if (MPseudostateKind.INITIAL.equals(kind)) icon = _InitialStateIcon;
              if (MPseudostateKind.DEEP_HISTORY.equals(kind)) icon = _DeepIcon;
              if (MPseudostateKind.SHALLOW_HISTORY.equals(kind)) icon = _ShallowIcon;
              if (MPseudostateKind.FORK.equals(kind)) icon = _ForkIcon;
              if (MPseudostateKind.JOIN.equals(kind)) icon = _JoinIcon;
              if (MPseudostateKind.BRANCH.equals(kind)) icon = _BranchIcon;
              //if (MPseudostateKind.FINAL.equals(kind)) icon = _FinalStateIcon;
          }
          if (value instanceof MAbstraction) {
                  icon = _RealizeIcon;
          }
          // needs more work: sending and receiving icons
          if (value instanceof MSignal) {
              icon = _SignalIcon;
          }
          
          if (value instanceof MComment) {
              icon = _CommentIcon;
          }

          if (icon == null) {
              String clsPackName = value.getClass().getName();
              if (clsPackName.startsWith("org") || clsPackName.startsWith("ru")) {
                  String cName =
                      clsPackName.substring(clsPackName.lastIndexOf(".")+1);
                  // special case "UML*" e.g. UMLClassDiagram
                  if (cName.startsWith("UML")) cName = cName.substring(3);
                  if (cName.startsWith("M"))
                      cName = cName.substring(1);
                  if (cName.endsWith("Impl"))
                      cName = cName.substring(0,cName.length() -4 );
                  icon = ResourceLoader.lookupIconResource(cName);
                  if (icon != null) _iconCache.put(value.getClass(), icon);
                  if (icon == null) System.out.println("UMLTreeCellRenderer: using default Icon for " + cName);
              }
          }

          if (icon != null) lab.setIcon(icon);

          String tip;
          if (value instanceof MModelElement)
              tip = ((MModelElement)value).getUMLClassName() + ": " +
                  ((MModelElement)value).getName() + " ";
	  else 
	      tip = (value == null) ? "null " : value.toString() + " ";
          lab.setToolTipText(tip);
          tree.setToolTipText(tip);

      }
      return r;
  }


} /* end class UMLTreeCellRenderer */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2895.java