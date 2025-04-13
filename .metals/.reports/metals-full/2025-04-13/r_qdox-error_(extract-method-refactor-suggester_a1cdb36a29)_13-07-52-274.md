error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3826.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3826.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3826.java
text:
```scala
i@@f (titleArea.contains(location) && tabFolder.getItemCount() > 0) {

/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.presentations.util;

import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.internal.dnd.DragUtil;
import org.eclipse.ui.presentations.StackDropResult;

/**
 * @since 3.0
 */
public class ReplaceDragHandler extends TabDragHandler {

    private final class DragCookie {
        int insertPosition;

        public DragCookie(int pos) {
            insertPosition = pos;
        }
    }

    private AbstractTabFolder tabFolder;

    public ReplaceDragHandler(AbstractTabFolder folder) {
        this.tabFolder = folder;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.TabDragHandler#dragOver(org.eclipse.swt.widgets.Control, org.eclipse.swt.graphics.Point)
     */
    public StackDropResult dragOver(Control currentControl, Point location,
            int dragStart) {

        // Determine which tab we're currently dragging over
        //Point localPos = tabFolder.getControl().toControl(location);

        AbstractTabItem tabUnderPointer = tabFolder.getItem(location);

        // This drop target only deals with tabs... if we're not dragging over
        // a tab, exit.
        if (tabUnderPointer == null) {
            Rectangle titleArea = tabFolder.getTabArea();

            // If we're dragging over the title area, treat this as a drop in the last
            // tab position.
            if (titleArea.contains(location)) {
                int dragOverIndex = tabFolder.getItemCount();
                AbstractTabItem lastTab = tabFolder.getItem(dragOverIndex - 1);

                // Can't drag to end unless you can see the end
                if (!lastTab.isShowing()) {
                    return null;
                }
                
                // If we are unable to compute the bounds for this tab, then ignore the drop
                Rectangle lastTabBounds = lastTab.getBounds();
                if (lastTabBounds.isEmpty()) {
                    return null;
                }

                if (dragStart >= 0) {
                    dragOverIndex--;

                    return new StackDropResult(lastTabBounds, new Integer(
                            dragOverIndex));
                }

                // Make the drag-over rectangle look like a tab at the end of the tab region.
                // We don't actually know how wide the tab will be when it's dropped, so just
                // make it 3 times wider than it is tall.
                Rectangle dropRectangle = titleArea;

                dropRectangle.x = lastTabBounds.x + lastTabBounds.width;
                dropRectangle.width = 3 * dropRectangle.height;
                return new StackDropResult(dropRectangle, new Integer(
                        dragOverIndex));

            } else {
                // If the closest side is the side with the tabs, consider this a stack operation.
                // Otherwise, let the drop fall through to whatever the default behavior is
                Rectangle displayBounds = DragUtil.getDisplayBounds(tabFolder.getControl());
                int closestSide = Geometry.getClosestSide(displayBounds, location);
                if (closestSide == tabFolder.getTabPosition()) {
                    return new StackDropResult(displayBounds, null);
                }
                
                return null;
            }
        }

        if (!tabUnderPointer.isShowing()) {
            return null;
        }
        
        Rectangle tabBounds = tabUnderPointer.getBounds();
        
        if (tabBounds.isEmpty()) {
            return null;
        }

        return new StackDropResult(tabBounds, new DragCookie(tabFolder
                .indexOf(tabUnderPointer)));
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.TabDragHandler#getInsertionPosition(java.lang.Object)
     */
    public int getInsertionPosition(Object cookie) {
        if (cookie instanceof DragCookie) {
            return Math.min(tabFolder.getItemCount(),
                    ((DragCookie) cookie).insertPosition);
        }

        return tabFolder.getItemCount();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3826.java