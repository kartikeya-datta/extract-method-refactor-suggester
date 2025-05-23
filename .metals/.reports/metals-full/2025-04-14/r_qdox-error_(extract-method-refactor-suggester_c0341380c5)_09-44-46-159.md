error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7765.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7765.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7765.java
text:
```scala
o@@penLink("file:///" + filename); //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.dialogs;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.about.ISystemSummarySection;
import org.eclipse.ui.internal.IWorkbenchConstants;
import org.eclipse.ui.internal.IWorkbenchHelpContextIds;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchPlugin;

/**
 * Displays system information about the eclipse application.  The content of
 * what is displayed is selectable through the
 * <code>org.eclipse.ui.systemSummaryExtensions</code> extension point.
 */
public final class AboutSystemDialog extends ProductInfoDialog {

    private Text text;
    
    private final static int BROWSE_ERROR_LOG_BUTTON = IDialogConstants.CLIENT_ID;

    private final static int COPY_TO_CLIPBOARD_BUTTON = IDialogConstants.CLIENT_ID + 1;

    /**
     * Create an instance of this dialog.
     * 
     * @param parentShell the parent shell
     */
    public AboutSystemDialog(Shell parentShell) {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
    }

    /* (non-Javadoc)
     * Method declared on Window.
     */
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(WorkbenchMessages.SystemSummary_title);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(newShell,
				IWorkbenchHelpContextIds.SYSTEM_SUMMARY_DIALOG);
    }

    /* (non-Javadoc)
     * Method declared on Dialog.
     */
    protected void createButtonsForButtonBar(Composite parent) {
        parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Button button = createButton(parent, BROWSE_ERROR_LOG_BUTTON, WorkbenchMessages.AboutSystemDialog_browseErrorLogName, false);
        String filename = Platform.getLogFileLocation().toOSString();
        button.setEnabled(new File(filename).exists());

        createButton(parent, COPY_TO_CLIPBOARD_BUTTON, WorkbenchMessages.AboutSystemDialog_copyToClipboardName, false);

        new Label(parent, SWT.NONE).setLayoutData(new GridData(
                GridData.FILL_HORIZONTAL));
        GridLayout layout = (GridLayout) parent.getLayout();
        layout.numColumns++;
        layout.makeColumnsEqualWidth = false;

        createButton(parent, IDialogConstants.CLOSE_ID,
                IDialogConstants.CLOSE_LABEL, true);
    }

    /* (non-Javadoc)
     * Method declared on Dialog.
     */
    protected Control createDialogArea(Composite parent) {
        Composite outer = (Composite) super.createDialogArea(parent);

        text = new Text(outer, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY
 SWT.V_SCROLL | SWT.NO_FOCUS | SWT.H_SCROLL);
        text.setBackground(parent.getDisplay().getSystemColor(
                SWT.COLOR_LIST_BACKGROUND));
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL
 GridData.VERTICAL_ALIGN_FILL);
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        gridData.heightHint = convertVerticalDLUsToPixels(300);
        gridData.widthHint = convertHorizontalDLUsToPixels(400);
        text.setLayoutData(gridData);
        text.setText(getSystemSummary());
        text.setFont(JFaceResources.getTextFont());
        return outer;
    }

    private String getSystemSummary() {
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        writer.println(NLS.bind(WorkbenchMessages.SystemSummary_timeStamp,  new Date())); 

        appendExtensions(writer);
        writer.close();
        return out.toString();
    }

    /*
     * Appends the contents of all extentions to the configurationLogSections
     * extension point. 
     */
    private void appendExtensions(PrintWriter writer) {
        IConfigurationElement[] configElements = getSortedExtensions();
        for (int i = 0; i < configElements.length; ++i) {
            IConfigurationElement element = configElements[i];

            Object obj = null;
            try {
                obj = WorkbenchPlugin.createExtension(element,
                        IWorkbenchConstants.TAG_CLASS);
            } catch (CoreException e) {
                WorkbenchPlugin.log(
                        "could not create class attribute for extension", //$NON-NLS-1$
                        e.getStatus());
            }

            writer.println();
            writer.println(NLS.bind(WorkbenchMessages.SystemSummary_sectionTitle, element.getAttribute("sectionTitle") )); //$NON-NLS-1$

            if (obj instanceof ISystemSummarySection) {
                ISystemSummarySection logSection = (ISystemSummarySection) obj;
                logSection.write(writer);
            } else
                writer.println(WorkbenchMessages.SystemSummary_sectionError);
        }
    }

    private IConfigurationElement[] getSortedExtensions() {
        IConfigurationElement[] configElements = Platform
                .getExtensionRegistry().getConfigurationElementsFor(
                        PlatformUI.PLUGIN_ID,
                        IWorkbenchConstants.PL_SYSTEM_SUMMARY_SECTIONS);

        Arrays.sort(configElements, new Comparator() {
            Collator collator = Collator.getInstance(Locale.getDefault());

            public int compare(Object a, Object b) {
                IConfigurationElement element1 = (IConfigurationElement) a;
                IConfigurationElement element2 = (IConfigurationElement) b;

                String id1 = element1.getAttribute("id"); //$NON-NLS-1$
                String id2 = element2.getAttribute("id"); //$NON-NLS-1$

                if (id1 != null && id2 != null && !id1.equals(id2))
                    return collator.compare(id1, id2);

                String title1 = element1.getAttribute("sectionTitle"); //$NON-NLS-1$ 
                String title2 = element2.getAttribute("sectionTitle"); //$NON-NLS-1$

                if (title1 == null)
                    title1 = ""; //$NON-NLS-1$
                if (title2 == null)
                    title2 = ""; //$NON-NLS-1$

                return collator.compare(title1, title2);
            }
        });

        return configElements;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#buttonPressed(int)
     */
    protected void buttonPressed(int buttonId) {
        switch (buttonId) {
        case IDialogConstants.CLOSE_ID:
            close();
            break;
        case BROWSE_ERROR_LOG_BUTTON:
            openErrorLogBrowser();
            break;
        case COPY_TO_CLIPBOARD_BUTTON:
            runCopyToClipboard();
            break;
        }
        super.buttonPressed(buttonId);
    }

    private void openErrorLogBrowser() {
        String filename = Platform.getLogFileLocation().toOSString();

        File log = new File(filename);
        if (log.exists()) {
            openLink(filename);
            return;
        }

        MessageDialog.openInformation(getShell(), WorkbenchMessages.AboutSystemDialog_noLogTitle, 
                NLS.bind(WorkbenchMessages.AboutSystemDialog_noLogMessage,  filename ));
    }

    private void runCopyToClipboard() {
        if (text == null)
            return;

        Clipboard clipboard = null;
        try {
            clipboard = new Clipboard(getShell().getDisplay());
            clipboard.setContents(new Object[] { text.getText() },
                    new Transfer[] { TextTransfer.getInstance() });
        } finally {
            if (clipboard != null)
                clipboard.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7765.java