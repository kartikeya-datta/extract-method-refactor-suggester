error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18072.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18072.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18072.java
text:
```scala
p@@ageCreateKey = new PageCreateKey(encryption, path, name);

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
package org.eclipse.wst.xml.security.ui.encrypt;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.xml.security.core.encrypt.Encryption;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;

/**
 * <p>This class prepares and adds all wizard pages to the wizard and launches the <i>XML Encryption
 * Wizard</i> afterwards.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class NewEncryptionWizard extends Wizard implements INewWizard {
    /** PageResource first wizard page. */
    private PageResource pageResource = null;
    /** PageUseKey second default wizard page. */
    private PageOpenKey pageOpenKey = null;
    /** PageCreateKey second alternative wizard page. */
    private PageCreateKey pageCreateKey = null;
    /** PageCreateKeystore second alternative wizard page. */
    private PageCreateKeystore pageCreateKeystore = null;
    /** PageAlgorithms third wizard page. */
    private PageAlgorithms pageAlgorithms = null;
    /** The XML document to encrypt. */
    private IFile file;
    /** The text selection in the editor. */
    private ITextSelection textSelection;
    /** The Encryption model. */
    private Encryption encryption;
    /** Stored setting for the encryption Keystore. */
    public static final String SETTING_KEYSTORE = "enc_keystore";
    /** Stored setting for the secret key name. */
    public static final String SETTING_KEY_NAME = "enc_key_name";
    /** Stored setting for the BSP encryption selection. */
    public static final String SETTING_BSP_COMPLIANT_ENCRYPTION = "enc_bsp_compliant";
    /** Stored setting for a plain root element. */
    public static final String SETTING_SET_PLAIN_ROOT_ELEMENT = "enc_plain_root_element";
    /** Stored setting for the Signature Wizard call after encrypting. */
    public static final String SETTING_CALL_SIGNATURE_WIZARD = "enc_sign";

    /**
     * Constructor for the wizard launcher.
     */
    public NewEncryptionWizard() {
        super();
        encryption = new Encryption();
        setWindowTitle(Messages.encryptionWizard);
        setDialogSettings(getEncryptionWizardSettings());
        ImageDescriptor image = AbstractUIPlugin.imageDescriptorFromPlugin(XSTUIPlugin.getId(),
                "icons/wiz_enc.gif");
        setDefaultPageImageDescriptor(image);
        setNeedsProgressMonitor(true);
    }

    /**
     * Return the settings used for all Encryption Wizard pages.
     *
     * @return The IDialogSettings for the Encryption Wizard
     */
    private IDialogSettings getEncryptionWizardSettings() {
        IDialogSettings workbenchSettings = XSTUIPlugin.getDefault().getDialogSettings();
        IDialogSettings section = workbenchSettings.getSection("EncryptionWizard"); // $NON-NLS-1$
        if (section == null) {
            section = workbenchSettings.addNewSection("EncryptionWizard"); // $NON-NLS-1$
        }
        return section;
    }

    /**
     * Initializes the wizard with a text selection.
     *
     * @param workbench The workbench
     * @param selection The text selection
     */
    public void init(final IWorkbench workbench, final IStructuredSelection selection) {
    }

    /**
     * Initializes the wizard with a selected file.
     *
     * @param file The selected IFile
     */
    public void init(final IFile file) {
        init(file, null);
    }

    /**
     * Initializes the wizard with a selected file and a text selection.
     *
     * @param file The selected file
     * @param textSelection The text selection
     */
    public void init(final IFile file, final ITextSelection textSelection) {
        this.file = file;
        this.textSelection = textSelection;
    }

    /**
     * Adds the five pages (<code>PageResource</code>,
     * <code>PageOpenKey</code>, <code>PageCreateKey</code>,
     * <code>PageCreateKeystore</code> and <code>PageAlgorithms</code>)
     * to the wizard.
     */
    public void addPages() {
        String path = file.getLocation().removeFileExtension().removeLastSegments(1).toString();
        String name = file.getLocation().removeFileExtension().removeLastSegments(1).lastSegment();

        pageResource = new PageResource(encryption, file, textSelection != null ? true : false);
        addPage(pageResource);
        pageOpenKey = new PageOpenKey(encryption, path);
        addPage(pageOpenKey);
        pageCreateKey = new PageCreateKey(encryption, path);
        addPage(pageCreateKey);
        pageCreateKeystore = new PageCreateKeystore(encryption, path, name);
        addPage(pageCreateKeystore);
        pageAlgorithms = new PageAlgorithms(encryption, file);
        addPage(pageAlgorithms);
    }

    /**
     * Checks the currently active wizard page. It is impossible to finish the <i>XML Encryption
     * Wizard</i> from any other than the last page. Only the last wizard page can successfully
     * generate the expected XML encryption.
     *
     * @return Wizard completion status
     */
    public boolean canFinish() {
        if (this.getContainer().getCurrentPage() != pageAlgorithms) {
            return false;
        }
        return pageAlgorithms.isPageComplete();
    }

    /**
     * Finishes the wizard.
     *
     * @return Finishing status
     */
    public boolean performFinish() {

        return pageAlgorithms.performFinish();
    }

    /**
     * Returns the Decryption Wizard model.
     *
     * @return The model
     */
    public Encryption getModel() {
        return encryption;
    }

    /**
     * @return the pageResource
     */
    public PageResource getPageResource() {
        return pageResource;
    }

    /**
     * @return the pageOpenKey
     */
    public PageOpenKey getPageOpenKey() {
        return pageOpenKey;
    }

    /**
     * @return the pageCreateKey
     */
    public PageCreateKey getPageCreateKey() {
        return pageCreateKey;
    }

    /**
     * @return the pageCreateKeystore
     */
    public PageCreateKeystore getPageCreateKeystore() {
        return pageCreateKeystore;
    }

    /**
     * @return the pageAlgorithms
     */
    public PageAlgorithms getPageAlgorithms() {
        return pageAlgorithms;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18072.java