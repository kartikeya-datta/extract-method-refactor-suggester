error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8554.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8554.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8554.java
text:
```scala
I@@WorkbenchPage workbenchPage) {

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

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.internal.util.PrefUtil;

public class PerspectiveBarContributionItem extends ContributionItem {

    private IPerspectiveDescriptor perspective;

    private IPreferenceStore apiPreferenceStore = PrefUtil
            .getAPIPreferenceStore();

    private ToolItem toolItem = null;

    private Image image;

    private IWorkbenchPage workbenchPage;

    public PerspectiveBarContributionItem(IPerspectiveDescriptor perspective,
            IWorkbenchPage workbenchPage) {
        super(perspective.getId());
        this.perspective = perspective;
        this.workbenchPage = workbenchPage;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.ContributionItem#dispose()
     */
    public void dispose() {
        super.dispose();
        if (image != null && !image.isDisposed()) {
            image.dispose();
            image = null;
        }
        apiPreferenceStore = null;
        workbenchPage = null;
        perspective = null;

    }

    public void fill(ToolBar parent, int index) {
        if (toolItem == null && parent != null && !parent.isDisposed()) {

            if (index >= 0)
                toolItem = new ToolItem(parent, SWT.CHECK, index);
            else
                toolItem = new ToolItem(parent, SWT.CHECK);

            if (image == null || image.isDisposed()) {
                createImage();
            }
            toolItem.setImage(image);

            toolItem.setToolTipText(WorkbenchMessages.format(
                    "PerspectiveBarContributionItem.toolTip", //$NON-NLS-1$
                    new Object[] { perspective.getLabel() }));
            toolItem.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent event) {
                    select();
                }
            });
            toolItem.setData(this); //TODO review need for this
            update();
        }
    }

    private void createImage() {
        ImageDescriptor imageDescriptor = perspective.getImageDescriptor();
        if (imageDescriptor != null) {
            image = imageDescriptor.createImage();
        } else {
            image = WorkbenchImages.getImageDescriptor(
                    IWorkbenchGraphicConstants.IMG_ETOOL_DEF_PERSPECTIVE)
                    .createImage();
        }
    }

    Image getImage() {
        if (image == null) {
            createImage();
        }
        return image;
    }

    public void select() {
        if (workbenchPage.getPerspective() != perspective) {
            workbenchPage.setPerspective(perspective);
            update();
            getParent().update(true);
        } else
            toolItem.setSelection(true);
    }

    public void update() {
        if (toolItem != null && !toolItem.isDisposed()) {
            toolItem
                    .setSelection(workbenchPage.getPerspective() == perspective);
            if (apiPreferenceStore
                    .getBoolean(IWorkbenchPreferenceConstants.SHOW_TEXT_ON_PERSPECTIVE_BAR)) {
                if (apiPreferenceStore.getString(
                        IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR)
                        .equals(IWorkbenchPreferenceConstants.TOP_LEFT))
                    toolItem.setText(perspective.getLabel());
                else
                    toolItem.setText(shortenText(perspective.getLabel(),
                            toolItem));
            } else {
                toolItem.setText(""); //$NON-NLS-1$
            }
        }
    }

    public void update(IPerspectiveDescriptor newDesc) {
        perspective = newDesc;
        if (toolItem != null && !toolItem.isDisposed()) {
            ImageDescriptor imageDescriptor = perspective.getImageDescriptor();
            if (imageDescriptor != null) {
                toolItem.setImage(imageDescriptor.createImage());
            } else {
                toolItem.setImage(WorkbenchImages.getImageDescriptor(
                        IWorkbenchGraphicConstants.IMG_ETOOL_DEF_PERSPECTIVE)
                        .createImage());
            }
            toolItem.setToolTipText(WorkbenchMessages.format(
                    "PerspectiveBarContributionItem.toolTip", //$NON-NLS-1$
                    new Object[] { perspective.getLabel() }));
        }
        update();
    }

    IWorkbenchPage getPage() {
        return workbenchPage;
    }

    IPerspectiveDescriptor getPerspective() {
        return perspective;
    }

    ToolItem getToolItem() {
        return toolItem;
    }

    public boolean handles(IPerspectiveDescriptor perspective,
            WorkbenchPage workbenchPage) {
        return this.perspective == perspective
                && this.workbenchPage == workbenchPage;
    }

    public void setPerspective(IPerspectiveDescriptor newPerspective) {
        this.perspective = newPerspective;
    }

    // TODO review need for this method
    void setSelection(boolean b) {
        if (toolItem != null && !toolItem.isDisposed())
            toolItem.setSelection(b);
    }

    static int getMaxWidth(Image image) {
        return image.getBounds().width * 5;
    }

    private static final String ellipsis = "..."; //$NON-NLS-1$

    protected String shortenText(String textValue, ToolItem item) {
        if (textValue == null || toolItem == null || toolItem.isDisposed())
            return null;
        String returnText = textValue;
        GC gc = new GC(item.getDisplay());
        int maxWidth = getMaxWidth(item.getImage());
        if (gc.textExtent(textValue).x >= maxWidth) {
            for (int i = textValue.length(); i > 0; i--) {
                String test = textValue.substring(0, i);
                test = test + ellipsis;
                if (gc.textExtent(test).x < maxWidth) {
                    returnText = test;
                    break;
                }
            }
        }
        gc.dispose();
        return returnText;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8554.java