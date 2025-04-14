error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9627.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9627.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,3]

error in qdox parser
file content:
```java
offset: 3
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9627.java
text:
```scala
 S@@WT.MULTI | SWT.READ_ONLY | SWT.LEFT_TO_RIGHT);

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
package org.eclipse.ui.internal.part;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @since 3.1
 */
public class StatusPart {
    
    boolean showingDetails = false;
    private Button detailsButton;
    private Composite detailsArea;
    private Control details = null;
    private IStatus reason;
    
    public StatusPart(final Composite parent, IStatus reason_) {
        this.reason = reason_;
        GridLayout layout = new GridLayout();
        
        layout.numColumns = 3;
        
        int spacing = 8;
        int margins = 8;
        layout.marginBottom = margins;
        layout.marginTop = margins;
        layout.marginLeft = margins;
        layout.marginRight = margins;
        layout.horizontalSpacing = spacing;
        layout.verticalSpacing = spacing;
        parent.setLayout(layout);
        
        Label imageLabel = new Label(parent, SWT.NONE);
        Image image = getImage();
        if (image != null) {
            image.setBackground(imageLabel.getBackground());
            imageLabel.setImage(image);
            imageLabel.setLayoutData(new GridData(
                    GridData.HORIZONTAL_ALIGN_CENTER
 GridData.VERTICAL_ALIGN_BEGINNING));
        }
        
        Text text = new Text(parent, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP);
        text.setBackground(text.getDisplay().getSystemColor(
                SWT.COLOR_WIDGET_BACKGROUND));

        //text.setForeground(JFaceColors.getErrorText(text.getDisplay()));
        text.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
        text.setText(reason.getMessage()); 
        
        detailsButton = new Button(parent, SWT.PUSH);
        detailsButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                showDetails(!showingDetails);
            }
        });
        
        detailsButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));
        detailsButton.setVisible(reason.getException() != null);
        
        updateDetailsText();
        
        detailsArea = new Composite(parent, SWT.NONE);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 3;
        data.verticalSpan = 1;
        detailsArea.setLayoutData(data);
        detailsArea.setLayout(new FillLayout());
        parent.layout(true);
    }
    
    /**
     * Return the image for the upper-left corner of this part
     *
     * @return
     */
    private Image getImage() {
        Display d = Display.getCurrent();
        
        switch(reason.getSeverity()) {
        case IStatus.ERROR: 
            return d.getSystemImage(SWT.ICON_ERROR);
        case IStatus.WARNING:
            return d.getSystemImage(SWT.ICON_WARNING);
        default:
            return d.getSystemImage(SWT.ICON_INFORMATION);
        }
    }
    
    private void showDetails(boolean shouldShow) {
        if (shouldShow == showingDetails) {
            return;
        }
        this.showingDetails = shouldShow;
        updateDetailsText();
    }
    
    private void updateDetailsText() {
        if (details != null) {
            details.dispose();
            details = null;
        }
        
        if (showingDetails) {
            detailsButton.setText(IDialogConstants.HIDE_DETAILS_LABEL);
            Text detailsText = new Text(detailsArea, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL 
 SWT.MULTI | SWT.READ_ONLY);
            detailsText.setText(getDetails(reason));
            detailsText.setBackground(detailsText.getDisplay().getSystemColor(
                    SWT.COLOR_LIST_BACKGROUND));
            details = detailsText;
            detailsArea.layout(true);
        } else {
            detailsButton.setText(IDialogConstants.SHOW_DETAILS_LABEL);
        }
    }

    
    private String getDetails(IStatus status) {
        if (status.getException() != null) {
            return getStackTrace(status.getException());
        }
        
        return ""; //$NON-NLS-1$
    }

    private String getStackTrace(Throwable throwable) {
        StringWriter swriter = new StringWriter();
        PrintWriter pwriter = new PrintWriter(swriter);
        throwable.printStackTrace(pwriter);
        pwriter.flush();
        pwriter.close();
        return swriter.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9627.java