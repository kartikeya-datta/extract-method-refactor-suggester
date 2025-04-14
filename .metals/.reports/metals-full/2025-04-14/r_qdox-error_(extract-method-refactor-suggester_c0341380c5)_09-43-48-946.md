error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6022.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6022.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6022.java
text:
```scala
W@@orkbenchPlugin.log(getClass(), "addView(String)", e); //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.activities.WorkbenchActivityHelper;
import org.eclipse.ui.internal.registry.IViewDescriptor;

/**
 * This layout is used to define the initial set of views and placeholders
 * in a folder.
 * <p>
 * Views are added to the folder by ID. This id is used to identify 
 * a view descriptor in the view registry, and this descriptor is used to 
 * instantiate the <code>IViewPart</code>.
 * </p>
 */
public class FolderLayout implements IFolderLayout {
    private ViewStack folder;

    private PageLayout pageLayout;

    private ViewFactory viewFactory;

    /**
     * Create an instance of a <code>FolderLayout</code> belonging to a 
     * <code>PageLayout</code>.
     */
    public FolderLayout(PageLayout pageLayout, ViewStack folder,
            ViewFactory viewFactory) {
        super();
        this.folder = folder;
        this.viewFactory = viewFactory;
        this.pageLayout = pageLayout;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPlaceholderFolderLayout#addPlaceholder(java.lang.String)
     */
    public void addPlaceholder(String viewId) {
        if (!pageLayout.checkValidPlaceholderId(viewId)) {
            return;
        }

        // Create the placeholder.
        PartPlaceholder newPart = new PartPlaceholder(viewId);
        linkPartToPageLayout(viewId, newPart);

        // Add it to the folder layout.
        folder.add(newPart);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IFolderLayout#addView(java.lang.String)
     */
    public void addView(String viewId) {
        if (pageLayout.checkPartInLayout(viewId))
            return;

        try {
            IViewDescriptor descriptor = viewFactory.getViewRegistry().find(
                    viewId);
            if (WorkbenchActivityHelper.filterItem(descriptor)) {
                //create a placeholder instead.
                addPlaceholder(viewId);
                LayoutHelper.addViewActivator(pageLayout, viewId);
            } else {

                ViewPane newPart = LayoutHelper.createView(pageLayout
                        .getViewFactory(), viewId);
                linkPartToPageLayout(viewId, newPart);
                folder.add(newPart);
            }
        } catch (PartInitException e) {
            // cannot safely open the dialog so log the problem
            WorkbenchPlugin.log(e.getMessage());
        }
    }

    /**
     * Inform the page layout of the new part created
     * and the folder the part belongs to.
     */
    private void linkPartToPageLayout(String viewId, LayoutPart newPart) {
        pageLayout.setRefPart(viewId, newPart);
        pageLayout.setFolderPart(viewId, folder);
        // force creation of the view layout rec
        pageLayout.getViewLayoutRec(viewId, true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6022.java