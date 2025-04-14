error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2475.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2475.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2475.java
text:
```scala
s@@howInfo(Messages.signaturesView, Messages.noSignaturesInDocument);

/*******************************************************************************
 * Copyright (c) 2008 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.core.actions;

import java.util.ArrayList;

import org.apache.xml.security.keys.keyresolver.KeyResolverException;
import org.apache.xml.security.signature.XMLSignatureException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.xml.security.core.verify.SignatureView;
import org.eclipse.wst.xml.security.core.verify.VerificationResult;
import org.eclipse.wst.xml.security.core.verify.VerifyDocument;

/**
 * <p>Action class used to start the <b>XML Signatures</b> View of the XML Security Tools to
 * verify all XML Signatures contained in the selected XML document.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class VerifyNewAction extends XmlSecurityActionAdapter {
    /** Active editor. */
    private ITextEditor editor = null;
    /** The file to verify. */
    private IFile file = null;
    /** Error message for the logfile. */
    private static final String ERROR_TEXT = "An error occured during verification"; //$NON-NLS-1$
    /** Action type. */
    private static final String ACTION = "verify";

    /**
     * Called when the selection in the active workbench part changes.
     *
     * @param action The executed action
     * @param selection The selection
     */
    public void selectionChanged(final IAction action, final ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            file = (IFile) ((IStructuredSelection) selection).getFirstElement();
        }
    }

    /**
     * Called when clicked on the <i>New Verification...</i> entry in the plug-ins context menu.
     *
     * @param action The IAction
     */
    public void run(final IAction action) {
        createVerification();
    }

    /**
     * Takes the resource (selected file or editor content) and verifies all contained signatures.
     * The selected XML document is not changed at all.
     */
    private void createVerification() {
        VerifyDocument verify = new VerifyDocument();
        ArrayList<VerificationResult> results = new ArrayList<VerificationResult>();

        try {
            if (file != null) { // call in view
                results = verify.verify(file.getLocation().toString());
            } else { // call in editor
                if (editor.isDirty()) {
                    saveEditorContent(editor);
                }

                file = (IFile) editor.getEditorInput().getAdapter(IFile.class);
                if (file != null) {
                    results = verify.verify(file.getLocation().toString());
                } else {
                    showInfo(Messages.verificationImpossible,
                        NLS.bind(Messages.protectedDoc, ACTION));
                }
            }

            if (results.size() == 0) {
                showInfo(Messages.refreshImpossible, Messages.noSignaturesInDocument);
            }

            // show results
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            IViewPart vp = page.showView(SignatureView.ID);
            if (vp instanceof SignatureView) {
                ((SignatureView) vp).setInput(results);
            }
        } catch (XMLSignatureException xmlse) {
            showError(Messages.error, Messages.invalidValueElement + xmlse.getLocalizedMessage());
        } catch (KeyResolverException kre) {
            showError(Messages.error, Messages.invalidCertificate + kre.getLocalizedMessage());
        } catch (Exception ex) {
            showErrorDialog(Messages.error, Messages.verificationError, ex);
            log(ERROR_TEXT, ex);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2475.java