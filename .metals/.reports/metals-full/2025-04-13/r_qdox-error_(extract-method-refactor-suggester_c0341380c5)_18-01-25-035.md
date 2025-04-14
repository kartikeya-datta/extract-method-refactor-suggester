error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5644.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5644.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5644.java
text:
```scala
C@@acheWrapper.this.flushCache();

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.internal.layout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

/**
 * This class wraps a control with a complex computeSize method. It uses caching
 * to reduce the number of times the control's computeSize method is called. This
 * allows controls (such as Coolbars and wrapping text) with slow computeSize
 * operations to be used inside layouts and composites that use inefficient caching.
 * <p>
 * For example, you want to use Coolbar A inside composite B. Rather than making A
 * a direct child of B, place it inside a CacheWrapper and insert the CacheWrapper
 * into B. Any layout data that would normally be attached to the control itself
 * should be attached to the wrapper instead:
 * </p>
 * <code>
 * 
 *   // Unoptimized code
 *   Toolbar myToolbar = new Toolbar(someParent, SWT.WRAP);
 *   myToolbar.setLayoutData(someLayoutData);
 * </code>
 * <code>
 * 
 *   // Optimized code
 *   CacheWrapper myWrapper = new CacheWrapper(someParent);
 *   Toolbar myToolbar = new Toolbar(myWrapper.getControl(), SWT.WRAP);
 *   myWrapper.getControl().setLayoutData(someLayoutData);
 * </code>
 * <p>
 * CacheWrapper creates a Composite which should have exactly one child: the control
 * whose size should be cached. Note that CacheWrapper does NOT respect the flushCache
 * argument to layout() and computeSize(). This is intentional, since the whole point of
 * this class is to workaround layouts with poor caching, and such layouts will typically
 * be too eager about flushing the caches of their children. However, this means that you
 * MUST manually call flushCache() whenver the child's preferred size changes (and before
 * the parent is layed out).  
 * </p>
 * 
 * @since 3.0
 */
public class CacheWrapper {
    private Composite proxy;

    private SizeCache cache = new SizeCache();

    private Rectangle lastBounds = new Rectangle(0, 0, 0, 0);

    private class WrapperLayout extends Layout implements ICachingLayout {
        protected Point computeSize(Composite composite, int wHint, int hHint,
                boolean flushCache) {
            Control[] children = composite.getChildren();
            if (children.length != 1) {
                return new Point(0, 0);
            }

            cache.setControl(children[0]);

            return cache.computeSize(wHint, hHint);
        }

        protected void layout(Composite composite, boolean flushCache) {
            Control[] children = composite.getChildren();
            if (children.length != 1) {
                return;
            }

            Control child = children[0];
            Rectangle newBounds = composite.getClientArea();
            if (!newBounds.equals(lastBounds)) {
                child.setBounds(newBounds);
                lastBounds = newBounds;
            }

        }

        /* (non-Javadoc)
         * @see org.eclipse.ui.internal.layout.ICachingLayout#flush(org.eclipse.swt.widgets.Control)
         */
        public void flush(Control dirtyControl) {
            flushCache();
        }
    }

    /**
     * Creates a <code>CacheWrapper</code> with the given parent
     * 
     * @param parent
     */
    public CacheWrapper(Composite parent) {
        proxy = new Composite(parent, SWT.NONE);

        proxy.setLayout(new WrapperLayout());
    }

    /**
     * Flush the cache. Call this when the child has changed in order to force
     * the size to be recomputed in the next resize event.
     */
    public void flushCache() {
        cache.flush();
    }

    /**
     * Use this as the parent of the real control.
     * 
     * @return the proxy contol. It should be given exactly one child.
     */
    public Composite getControl() {
        return proxy;
    }

    /**
     * Dispose of any widgets created by this wrapper.
     */
    public void dispose() {
        if (proxy != null) {
            proxy.dispose();
            proxy = null;
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5644.java