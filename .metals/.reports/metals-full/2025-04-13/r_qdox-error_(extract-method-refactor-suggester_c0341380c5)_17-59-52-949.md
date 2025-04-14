error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11911.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11911.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11911.java
text:
```scala
N@@LS.bind(Messages.RemoveReadOnlyFlag, Messages.NewDecryptionCommand_3));

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
package org.eclipse.wst.xml.security.ui.commands;

import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;

import org.apache.xml.security.utils.XMLUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.wst.xml.security.core.decrypt.CreateDecryption;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;
import org.eclipse.wst.xml.security.ui.decrypt.NewDecryptionWizard;
import org.eclipse.wst.xml.security.ui.utils.Utils;
import org.w3c.dom.Document;

/**
 * <p>Command used to start the <b>XML Decryption Wizard</b> for a new decryption in the selected
 * XML document. The decryption process differs depending on whether editor content or a file via a
 * view should be decrypted.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class NewDecryptionCommand extends AbstractHandler {
    private ExecutionEvent event;
    /** The file to decrypt. */
    private IFile file = null;

    public Object execute(ExecutionEvent event) throws ExecutionException {
        this.event = event;

        createDecryption();

        return null;
    }

    private void createDecryption() {
        try {
            NewDecryptionWizard wizard = new NewDecryptionWizard();
            IDocument document = null;

            if (HandlerUtil.getActivePart(event) instanceof IEditorPart) {
                final IEditorPart editorPart = (IEditorPart) HandlerUtil.getActivePart(event);

                if (editorPart.isDirty()) {
                    if (null != editorPart.getTitle() && editorPart.getTitle().length() > 0) {
                        IRunnableWithProgress op = new IRunnableWithProgress() {
                            public void run(final IProgressMonitor monitor) {
                                editorPart.doSave(monitor);
                            }
                        };
                        try {
                            PlatformUI.getWorkbench().getProgressService().runInUI(XSTUIPlugin.getActiveWorkbenchWindow(),
                                    op, ResourcesPlugin.getWorkspace().getRoot());
                        } catch (InvocationTargetException ite) {
                            Utils.log("Error while saving editor content", ite); //$NON-NLS-1$
                        } catch (InterruptedException ie) {
                            Utils.log("Error while saving editor content", ie); //$NON-NLS-1$
                        }
                    } else {
                        editorPart.doSaveAs();
                    }
                }

                file = (IFile) editorPart.getEditorInput().getAdapter(IFile.class);
                document = (IDocument) editorPart.getAdapter(IDocument.class);
            } else {
                ISelection selection = HandlerUtil.getCurrentSelection(event);
                if (selection instanceof IStructuredSelection) {
                    file = (IFile) ((IStructuredSelection) selection).getFirstElement();
                }
            }

            if (file != null && file.isAccessible()) {
                wizard.init(file);

                CreateDecryption decryption = new CreateDecryption();
                String fileLocation = ""; //$NON-NLS-1$

                if (document == null) {
                    fileLocation = file.getLocation().toString();
                }

                decryptData(decryption, wizard, document, fileLocation);
            } else {
                MessageDialog.openInformation(HandlerUtil.getActiveShell(event), Messages.NewDecryptionCommand_0,
                        NLS.bind(Messages.RemoveReadOnlyFlag, "decrypt")); //$NON-NLS-1$
            }
        } catch (Exception ex) {
            Utils.showErrorDialog(HandlerUtil.getActiveShell(event), Messages.NewDecryptionCommand_0,
                    Messages.NewDecryptionCommand_1, ex);
            Utils.log("An error occured during decrypting", ex); //$NON-NLS-1$
        }
    }

    /**
     * Called when decrypting an XML resource inside an opened editor or via a view. The
     * output XML can not be pretty printed since this would break an existing XML
     * signature in the document.
     *
     * @param data The resource to decrypt
     * @param wizard The Decryption Wizard
     * @param document The document to decrypt, null if a file is decrypted directly
     * @param filename The filename, empty if editor content is decrypted
     * @throws Exception to indicate any exceptional condition
     */
    private void decryptData(final CreateDecryption data, final NewDecryptionWizard wizard,
            final IDocument document, final String filename) throws Exception {
        WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), wizard);
        dialog.create();
        dialog.open();

        if (dialog.getReturnCode() == Dialog.OK && wizard.getModel() != null) {
            Job job = new Job(Messages.NewDecryptionCommand_0) {
                public IStatus run(final IProgressMonitor monitor) {
                    try {
                        monitor.beginTask(Messages.NewDecryptionCommand_2, 6);

                        Document doc = data.decrypt(wizard.getModel(), monitor);

                        if (monitor.isCanceled()) {
                            return Status.CANCEL_STATUS;
                        }

                        if (doc != null) {
                            if (document != null) {
                                document.set(org.eclipse.wst.xml.security.core.utils.Utils.docToString(doc, false));
                            } else {
                                FileOutputStream fos = new FileOutputStream(filename);
                                XMLUtils.outputDOM(doc, fos);
                                fos.flush();
                                fos.close();
                            }
                        }
                    } catch (final Exception ex) {
                        HandlerUtil.getActiveShell(event).getDisplay().asyncExec(new Runnable() {
                            public void run() {
                                Utils.showErrorDialog(HandlerUtil.getActiveShell(event), Messages.NewDecryptionCommand_0,
                                        Messages.NewDecryptionCommand_1, ex);
                                Utils.log("An error occured during decrypting", ex); //$NON-NLS-1$
                            }
                        });
                    } finally {
                        monitor.done();
                    }

                    return Status.OK_STATUS;
                }
            };
            job.setUser(true);
            job.schedule();
        }

        dialog.close();
        wizard.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11911.java