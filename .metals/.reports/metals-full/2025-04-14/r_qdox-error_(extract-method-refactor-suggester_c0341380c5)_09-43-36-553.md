error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8517.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8517.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8517.java
text:
```scala
J@@Dialog  f = new JDialog(ProjectBrowser.getInstance());

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

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.cognitive.ui.TabToDoTarget;
import org.argouml.swingext.Orientation;
import org.argouml.uml.diagram.ui.TabDiagram;
import org.argouml.uml.ui.TabModelTarget;
import org.argouml.uml.ui.TabProps;

/** A subclass of JPanel that can act as a tab in the DetailsPane or
 *  MultiEditorPane.  When the tab is double-clicked, this JPanel will
 *  generate a separate window of the same size and with the same
 *  contents.  This is almost like "tearing off" a tab.
 */
public class TabSpawnable
    extends JPanel
    implements Cloneable, org.argouml.swingext.Orientable  {
        
    protected static Category cat = 
        Category.getInstance(TabProps.class);
    
  public final int OVERLAPP = 30;
  
    private static final String BUNDLE = "UMLMenu";

  ////////////////////////////////////////////////////////////////
  // instance variables
    
  String _title = "untitled";
  
  /**
   * if true, remove tab from parent JTabbedPane
   */
  boolean _tear = false;
  protected Orientation orientation;

  ////////////////////////////////////////////////////////////////
  // constructor

  public TabSpawnable() { this("untitled", false); }

  public TabSpawnable(String tag) { this(tag, false); }

  public TabSpawnable(String tag, boolean tear) {
    setTitle(tag);
    _tear = tear;
  }

    /** This is not a real clone since it doesn't copy anything from the
     * object it is cloning.
     * The
     * @see #spawn
     * method copies the title and in some cases when we are a
     * @see TabToDoTarget
     * or
     * @see TabModelTarget
     * also the Target.
     *
     * @return the new object or null if not possible.
     */
  public Object clone() {
    try { return this.getClass().newInstance(); }
    catch (Exception ex) {
        cat.error("exception in clone()", ex);
    }
    return null;
  }
  
    /*
     * Set the orientation of the property panel
     * @param orientation the new orientation for this preoprty panel
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }


  ////////////////////////////////////////////////////////////////
  // accessors

  public String getTitle() { return _title; }
  public void setTitle(String t) { _title = t; }


  ////////////////////////////////////////////////////////////////
  // actions
  
    /**
     * this should take its inspiration from org.tigris.gef.base.CmdSpawn
     *
     * <p>The spawned/cloned tab will be a JFrame.
     *
     * @return a copy of the frame or null if not clone-able.
     */
  public TabSpawnable spawn() {

    JDialog  f = new JDialog(ProjectBrowser.TheInstance);    
    f.getContentPane().setLayout(new BorderLayout());
    f.setTitle(Argo.localize(BUNDLE, _title));
    TabSpawnable newPanel = (TabSpawnable) clone();
    if (newPanel == null)
        return null; //failed to clone

    if (newPanel instanceof TabToDoTarget) {
      TabToDoTarget me = (TabToDoTarget) this;
      TabToDoTarget it = (TabToDoTarget) newPanel;
      it.setTarget(me.getTarget());
    }
    else if (newPanel instanceof TabModelTarget) {
      TabModelTarget me = (TabModelTarget) this;
      TabModelTarget it = (TabModelTarget) newPanel;
      it.setTarget(me.getTarget());
    }
    else if (newPanel instanceof TabDiagram) {
      TabDiagram me = (TabDiagram) this;
      TabDiagram it = (TabDiagram) newPanel;
      it.setTarget(me.getTarget());
    }
    
    newPanel.setTitle(Argo.localize(BUNDLE, _title));
    
    f.getContentPane().add(newPanel, BorderLayout.CENTER);
    Rectangle bounds = getBounds();
    bounds.height += OVERLAPP*2;
    f.setBounds(bounds);

    Point loc = new Point(0,0);
    SwingUtilities.convertPointToScreen(loc, this);
    loc.y -= OVERLAPP;
    f.setLocation(loc);
    f.setVisible(true);

    if (_tear && (getParent() instanceof JTabbedPane))
      ((JTabbedPane)getParent()).remove(this);

    return newPanel;
  }
  
} /* end class TabSpawnable */


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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8517.java