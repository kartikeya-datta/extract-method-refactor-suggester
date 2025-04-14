error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9791.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9791.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9791.java
text:
```scala
f@@StatusLine.setErrorStatus(null);

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Sebastian Davids <sdavids@gmx.de> - Fix for bug 19346 - Dialog
 *     font should be activated and used by other components.
 *******************************************************************************/
package org.eclipse.ui.dialogs;

import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.MessageLine;

/**
 * An abstract base class for dialogs with a status bar and ok/cancel buttons.
 * The status message must be passed over as StatusInfo object and can be
 * an error, warning or ok. The OK button is enabled or disabled depending
 * on the status.
 * 
 * @since 2.0
 */
public abstract class SelectionStatusDialog extends SelectionDialog {

    private MessageLine fStatusLine;

    private IStatus fLastStatus;

    private Image fImage;

    private boolean fStatusLineAboveButtons = false;

    /**
     * Creates an instance of a <code>SelectionStatusDialog</code>.
     * @param parent
     */
    public SelectionStatusDialog(Shell parent) {
        super(parent);
    }

    /**
     * Controls whether status line appears to the left of the buttons (default)
     * or above them.
     *
     * @param aboveButtons if <code>true</code> status line is placed above buttons; if
     * 	<code>false</code> to the right
     */
    public void setStatusLineAboveButtons(boolean aboveButtons) {
        fStatusLineAboveButtons = aboveButtons;
    }

    /**
     * Sets the image for this dialog.
     * @param image the image.
     */
    public void setImage(Image image) {
        fImage = image;
    }

    /**
     * Returns the first element from the list of results. Returns <code>null</code>
     * if no element has been selected.
     *
     * @return the first result element if one exists. Otherwise <code>null</code> is
     *  returned.
     */
    public Object getFirstResult() {
        Object[] result = getResult();
        if (result == null || result.length == 0)
            return null;
        return result[0];
    }

    /**
     * Sets a result element at the given position.
     * @param position
     * @param element
     */
    protected void setResult(int position, Object element) {
        Object[] result = getResult();
        result[position] = element;
        setResult(Arrays.asList(result));
    }

    /**
     * Compute the result and return it.
     */
    protected abstract void computeResult();

    /*
     * @see Window#configureShell(shell)
     */
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        if (fImage != null)
            shell.setImage(fImage);
    }

    /**
     * Update the dialog's status line to reflect the given status. It is safe to call
     * this method before the dialog has been opened.
     * @param status
     */
    protected void updateStatus(IStatus status) {
        fLastStatus = status;
        if (fStatusLine != null && !fStatusLine.isDisposed()) {
            updateButtonsEnableState(status);
            fStatusLine.setErrorStatus(status);
        }
    }

    /**
     * Update the status of the ok button to reflect the given status. Subclasses
     * may override this method to update additional buttons.
     * @param status
     */
    protected void updateButtonsEnableState(IStatus status) {
        Button okButton = getOkButton();
        if (okButton != null && !okButton.isDisposed())
            okButton.setEnabled(!status.matches(IStatus.ERROR));
    }

    /*
     * @see Dialog#okPressed()
     */
    protected void okPressed() {
        computeResult();
        super.okPressed();
    }

    /*
     * @see Window#create()
     */
    public void create() {
        super.create();
        if (fLastStatus != null)
            updateStatus(fLastStatus);
    }

    /*
     * @see Dialog#createButtonBar(Composite)
     */
    protected Control createButtonBar(Composite parent) {
        Font font = parent.getFont();
        Composite composite = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        if (fStatusLineAboveButtons) {
            layout.marginWidth = 5;
        } else {
            layout.numColumns = 2;
        }
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        composite.setFont(font);

        fStatusLine = new MessageLine(composite);
        fStatusLine.setAlignment(SWT.LEFT);
        fStatusLine.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fStatusLine.setErrorStatus(null); //$NON-NLS-1$
        fStatusLine.setFont(font);

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalIndent = convertWidthInCharsToPixels(1);
        fStatusLine.setLayoutData(gd);

        super.createButtonBar(composite);
        return composite;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9791.java