error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1118.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1118.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1118.java
text:
```scala
.@@getImageDescriptor(IWorkbenchGraphicConstants.IMG_ETOOL_DEF_PERSPECTIVE);

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
package org.eclipse.ui.model;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.internal.WorkbenchMessages;

/**
 * A table label provider implementation for showing workbench perspectives 
 * (objects of type <code>IPerspectiveDescriptor</code>) in table- and 
 * tree-structured viewers.
 * <p>
 * Clients may instantiate this class. It is not intended to be subclassed.
 * </p>
 * 
 * @since 3.0
 */
public final class PerspectiveLabelProvider extends LabelProvider implements
        ITableLabelProvider {

    /**
     * List of all Image objects this label provider is responsible for.
     */
    private HashMap imageCache = new HashMap(5);

    /**
     * Indicates whether the default perspective is visually marked.
     */
    private boolean markDefault;

    /**
     * Creates a new label provider for perspectives.
     * The default perspective is visually marked.
     */
    public PerspectiveLabelProvider() {
        this(true);
    }

    /**
     * Creates a new label provider for perspectives.
     * 
     * @param markDefault <code>true</code> if the default perspective is to be
     * visually marked, and <code>false</code> if the default perspective is
     * not treated as anything special
     */
    public PerspectiveLabelProvider(boolean markDefault) {
        super();
        this.markDefault = markDefault;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ILabelProvider
     */
    public final Image getImage(Object element) {
        if (element instanceof IPerspectiveDescriptor) {
            IPerspectiveDescriptor desc = (IPerspectiveDescriptor) element;
            ImageDescriptor imageDescriptor = desc.getImageDescriptor();
            if (imageDescriptor == null) {
                imageDescriptor = WorkbenchImages
                        .getImageDescriptor(IWorkbenchGraphicConstants.IMG_ETOOL_DEF_PERSPECTIVE_HOVER);
            }
            Image image = (Image) imageCache.get(imageDescriptor);
            if (image == null) {
                image = imageDescriptor.createImage();
                imageCache.put(imageDescriptor, image);
            }
            return image;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ILabelProvider
     */
    public final void dispose() {
        for (Iterator i = imageCache.values().iterator(); i.hasNext();) {
            ((Image) i.next()).dispose();
        }
        imageCache.clear();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ILabelProvider
     */
    public final String getText(Object element) {
        if (element instanceof IPerspectiveDescriptor) {
            IPerspectiveDescriptor desc = (IPerspectiveDescriptor) element;
            String label = desc.getLabel();
            if (markDefault) {
                String def = PlatformUI.getWorkbench().getPerspectiveRegistry()
                        .getDefaultPerspective();
                if (desc.getId().equals(def)) {
                    label = WorkbenchMessages
                            .format(
                                    "PerspectivesPreference.defaultLabel", new Object[] { label }); //$NON-NLS-1$
                }
            }
            return label;
        }
        return WorkbenchMessages.getString("PerspectiveLabelProvider.unknown"); //$NON-NLS-1$
    }

    /**
     * @see ITableLabelProvider#getColumnImage
     */
    public final Image getColumnImage(Object element, int columnIndex) {
        return getImage(element);
    }

    /**
     * @see ITableLabelProvider#getColumnText
     */
    public final String getColumnText(Object element, int columnIndex) {
        return getText(element);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1118.java