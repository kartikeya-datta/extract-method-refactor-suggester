error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10512.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10512.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10512.java
text:
```scala
i@@f (textSelection == null || textSelection.trim().length() == 0) {

/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.actions;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;
import org.eclipse.wst.xml.security.ui.dialogs.MissingPreferenceDialog;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * <p>Base class of all actions in the XML Security Tools. Contains common methods
 * for saving the editor content, logging and error/ information dialog handling.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public abstract class XmlSecurityActionAdapter implements IObjectActionDelegate {
    /** Active Workbench Part. */
    private IWorkbenchPart workbenchPart = null;
    /** The current shell. */
    private Shell shell = null;

    /**
     * Called when the action is executed. Must be overwritten in subclasses.
     *
     * @param action The executed action
     */
    public abstract void run(final IAction action);

    /**
     * Called when the selection in the active workbench part changes.
     *
     * @param action The executed action
     * @param selection The selection
     */
    public abstract void selectionChanged(final IAction action, final ISelection selection);

    /**
     * Returns the current shell.
     *
     * @return The shell
     */
    public Shell getShell() {
        return shell;
    }

    /**
     * Returns the current workbench part.
     *
     * @return The workbench part
     */
    public IWorkbenchPart getWorkbenchPart() {
        return workbenchPart;
    }

    /**
     * Sets the active part of the workbench.
     *
     * @param action The executed IAction
     * @param workbenchPart The IWorkbenchPart
     */
    public void setActivePart(final IAction action, final IWorkbenchPart workbenchPart) {
        this.workbenchPart = workbenchPart;
        shell = workbenchPart.getSite().getShell();
    }

    /**
     * Writes the message with status <i>error</i> into the workbench log file.
     *
     * @param message The message to log
     * @param ex The exception to log
     */
    protected void log(final String message, final Exception ex) {
        IStatus status = new Status(IStatus.ERROR, XSTUIPlugin.getDefault().getBundle().getSymbolicName(), 0, message, ex);
        XSTUIPlugin.getDefault().getLog().log(status);
    }

    /**
     * Shows an information dialog with a message.
     *
     * @param title The title of the message box
     * @param message The message to display
     */
    protected void showInfo(final String title, final String message) {
        MessageDialog.openInformation(shell, title, message);
    }

    /**
     * Shows an error dialog with a message.
     *
     * @param title The title of the message box
     * @param message The message to display
     */
    protected void showError(final String title, final String message) {
        MessageDialog.openError(shell, title, message);
    }

    /**
     * Shows an error dialog with an details button for detailed error information.
     *
     * @param title The title of the message box
     * @param message The message to display
     * @param ex The exception
     */
    protected void showErrorDialog(final String title, final String message, final Exception ex) {
        String reason = ex.getMessage();
        if (reason == null || "".equals(reason)) {
            reason = Messages.errorReasonUnavailable;
        }

        IStatus status = new Status(IStatus.ERROR, XSTUIPlugin.getDefault().getBundle().getSymbolicName(), 0, reason, ex);

        ErrorDialog.openError(shell, title, message, status);
    }

    /**
     * Shows a dialog with a message for a missing preference parameter.
     *
     * @param title The title of the message box
     * @param message The message to display
     * @param prefId The preference page id to show
     * @return The clicked button in the preferences dialog
     */
    protected int showMissingParameterDialog(final String title, final String message, final String prefId) {
        MissingPreferenceDialog dialog = new MissingPreferenceDialog(shell, title, message, prefId);
        return dialog.open();
    }

    /**
     * Called when there is a text selection and either the XML Signature Wizard or the XML Encryption Wizard is called.
     * If the selection is invalid, the radio button in the wizard is disabled. This method returns always
     * <code>true</code> if only element content (no &gt; or &lt; included) is selected.
     *
     * @param textSelection The text selection as a String value
     * @return true or false which activates or deactivates the selection radio button in the wizard
     */
    protected boolean parseSelection(final String textSelection) {
        if (textSelection == null || textSelection.length() == 0) {
            return false;
        }

        Pattern p = Pattern.compile("[^<>]+");
        Matcher m = p.matcher(textSelection);

        // a tag (or parts of it) are selected
        if (!m.matches()) {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            try {
                XMLReader xmlReader = spf.newSAXParser().getXMLReader();
                xmlReader.setErrorHandler(null);
                xmlReader.parse(new InputSource(new StringReader(textSelection)));
            } catch (IOException e) {
                return false;
            } catch (SAXException e) {
                return false;
            } catch (ParserConfigurationException e) {
                return false;
            }

            return true;
        }

        // only element content, no < or > selected, always return true
        return true;
    }

    /**
     * Saves the unsaved content of the active editor.
     *
     * @param openedEditor The opened editor
     */
    protected void saveEditorContent(final ITextEditor openedEditor) {
        if (null != openedEditor.getTitle() && openedEditor.getTitle().length() > 0) {
            IRunnableWithProgress op = new IRunnableWithProgress() {
                public void run(final IProgressMonitor monitor) {
                    openedEditor.doSave(monitor);
                }
            };
            try {
                PlatformUI.getWorkbench().getProgressService().runInUI(XSTUIPlugin.getActiveWorkbenchWindow(),
                        op, ResourcesPlugin.getWorkspace().getRoot());
            } catch (InvocationTargetException ite) {
                log("Error while saving editor content", ite); //$NON-NLS-1$
            } catch (InterruptedException ie) {
                log("Error while saving editor content", ie); //$NON-NLS-1$
            }
        } else {
            openedEditor.doSaveAs();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10512.java