error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/947.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/947.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/947.java
text:
```scala
s@@uper(perspective.getId());

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
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
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.application.IWorkbenchPreferences;

public class PerspectiveBarContributionItem extends ContributionItem {

    private IPerspectiveDescriptor perspective;

    private IPreferenceStore preferenceStore = WorkbenchPlugin.getDefault()
            .getPreferenceStore();

    private ToolBar toolBar = null;

    private ToolItem toolItem = null;

    private WorkbenchPage workbenchPage;

    private IPropertyChangeListener propertyChangeListener = new IPropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (IWorkbenchPreferences.SHOW_TEXT_ON_PERSPECTIVE_BAR
                    .equals(propertyChangeEvent.getProperty())) {
                update();
                IContributionManager parent = getParent();
                if (parent != null) {
                    parent.update(true);
                    if (parent instanceof PerspectiveBarManager)
                            ((PerspectiveBarManager) parent).layout(true);
                }
            }
        }
    };

    public PerspectiveBarContributionItem(IPerspectiveDescriptor perspective,
            WorkbenchPage workbenchPage) {
        super(PerspectiveBarContributionItem.class.getName());
        this.perspective = perspective;
        this.workbenchPage = workbenchPage;
        preferenceStore.addPropertyChangeListener(propertyChangeListener);
    }

    public void fill(ToolBar parent, int index) {
        if (toolItem == null && parent != null) {
            toolBar = parent;
            if (index >= 0)
                toolItem = new ToolItem(parent, SWT.CHECK, index);
            else
                toolItem = new ToolItem(parent, SWT.CHECK);
            ImageDescriptor imageDescriptor = perspective.getImageDescriptor();
            if (imageDescriptor != null) {
                toolItem.setImage(imageDescriptor.createImage());
                toolItem.setHotImage(null);
            } else {
                toolItem.setImage(WorkbenchImages.getImageDescriptor(
                        IWorkbenchGraphicConstants.IMG_CTOOL_DEF_PERSPECTIVE)
                        .createImage());
                toolItem
                        .setHotImage(WorkbenchImages
                                .getImageDescriptor(
                                        IWorkbenchGraphicConstants.IMG_CTOOL_DEF_PERSPECTIVE_HOVER)
                                .createImage());
            }
            toolItem.setToolTipText(WorkbenchMessages.format(
                    "PerspectiveBarContributionItem.toolTip", //$NON-NLS-1$
                    new Object[] { perspective.getLabel()}));
            toolItem.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent event) {
                    if (workbenchPage.getPerspective() != perspective) {
                        workbenchPage.setPerspective(perspective);
                        update();
                        getParent().update(true);
                    }
                }
            });
            toolItem.setData(this); //TODO review need for this
            update();
        }
    }

    public void update() {
        if (!toolItem.isDisposed()) {
            toolItem
                    .setSelection(workbenchPage.getPerspective() == perspective);
            if (preferenceStore
                    .getBoolean(IWorkbenchPreferences.SHOW_TEXT_ON_PERSPECTIVE_BAR))
                toolItem.setText(shortenText(perspective.getLabel(), toolItem));
            else
                toolItem.setText(""); //$NON-NLS-1$            
        }
    }

    public void update(IPerspectiveDescriptor newDesc) {
        perspective = newDesc;
        if (!toolItem.isDisposed()) {
            ImageDescriptor imageDescriptor = perspective.getImageDescriptor();
            if (imageDescriptor != null) {
                toolItem.setImage(imageDescriptor.createImage());
                toolItem.setHotImage(null);
            } else {
                toolItem.setImage(WorkbenchImages.getImageDescriptor(
                        IWorkbenchGraphicConstants.IMG_CTOOL_DEF_PERSPECTIVE)
                        .createImage());
                toolItem
                        .setHotImage(WorkbenchImages
                                .getImageDescriptor(
                                        IWorkbenchGraphicConstants.IMG_CTOOL_DEF_PERSPECTIVE_HOVER)
                                .createImage());
            }
            toolItem.setToolTipText(WorkbenchMessages.format(
                    "PerspectiveBarContributionItem.toolTip", //$NON-NLS-1$
                    new Object[] { perspective.getLabel()}));
        }
        update();
    }

    WorkbenchPage getPage() {
        return workbenchPage;
    }

    IPerspectiveDescriptor getPerspective() {
        return perspective;
    }

    public boolean handles(IPerspectiveDescriptor perspective,
            WorkbenchPage workbenchPage) {
        return this.perspective == perspective
                && this.workbenchPage == workbenchPage;
    }

    public void setPerspective(IPerspectiveDescriptor newPerspective) {
        this.perspective = newPerspective;
    }

    // TODO review need for this method;
    void setSelection(boolean b) {
        if (!toolItem.isDisposed()) toolItem.setSelection(b);
    }

    private static final String ellipsis = "..."; //$NON-NLS-1$

    protected String shortenText(String textValue, ToolItem item) {
        if (textValue == null) return null;
        GC gc = new GC(item.getDisplay());
        int maxWidth = item.getImage().getBounds().width * 5;
        if (gc.textExtent(textValue).x < maxWidth) return textValue;
        for (int i = textValue.length(); i > 0; i--) {
            String test = textValue.substring(0, i);
            test = test + ellipsis;
            if (gc.textExtent(test).x < maxWidth) return test;
        }
        return textValue;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/947.java