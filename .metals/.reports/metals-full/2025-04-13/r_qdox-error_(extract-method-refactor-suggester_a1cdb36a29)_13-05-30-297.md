error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/790.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/790.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/790.java
text:
```scala
i@@f ( e.getButton() == MouseEvent.BUTTON1 ) treeController.selectFolder();

package org.columba.mail.gui.tree.action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.tree.TreePath;

import org.columba.mail.gui.tree.TreeController;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class FolderTreeMouseListener extends MouseAdapter
{
    private TreeController treeController;

    public FolderTreeMouseListener( TreeController t )
    {
        this.treeController = t;
    }

    protected JPopupMenu getPopupMenu()
    {
        return treeController.getPopupMenu();
    }

	// Use PopUpTrigger in both mousePressed and mouseReleasedMethods due to
	// different handling of *nix and windows

    public void mousePressed(MouseEvent e)
    {
         if ( e.isPopupTrigger() )
            {
                java.awt.Point point = e.getPoint();
                TreePath path = treeController.getView().getClosestPathForLocation( point.x, point.y );

                treeController.getView().clearSelection();
                treeController.getView().addSelectionPath( path );

                treeController.getActionListener().changeActions();


                getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
            }

    }

    public void mouseReleased(MouseEvent e)
    {
         if ( e.isPopupTrigger() )
            {
                java.awt.Point point = e.getPoint();
                TreePath path = treeController.getView().getClosestPathForLocation( point.x, point.y );

                treeController.getView().clearSelection();
                treeController.getView().addSelectionPath( path );

                treeController.getActionListener().changeActions();


                getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
            }
    }

    public void mouseClicked(MouseEvent e)
    {
    	treeController.selectFolder();
    	/*
        if ( e.getClickCount() == 1 )
        {
            treeController.selectFolder();
        }
        else if ( e.getClickCount() == 2 )
        {
            treeController.expandImapRootFolder();
        }
        */

    }

    /*
    private void maybeShowPopup(MouseEvent e)
    {
        if (e.isPopupTrigger())
        {
            getPopupMenu().show(e.getComponent(),
                       e.getX(), e.getY());
        }
    }
    */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/790.java