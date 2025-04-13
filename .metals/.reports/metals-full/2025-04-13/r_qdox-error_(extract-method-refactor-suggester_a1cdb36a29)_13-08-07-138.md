error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10585.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10585.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10585.java
text:
```scala
s@@ignature.setKeyName(KEY_ALIAS);

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
package org.eclipse.wst.xml.security.core.tests.sign;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.apache.xml.security.utils.XMLUtils;
import org.eclipse.wst.xml.security.core.sign.CreateSignature;
import org.eclipse.wst.xml.security.core.sign.Signature;
import org.eclipse.wst.xml.security.core.tests.XMLSecurityToolsTestPlugin;
import org.eclipse.wst.xml.security.core.utils.Globals;
import org.eclipse.wst.xml.security.core.utils.Keystore;
import org.eclipse.wst.xml.security.core.verify.VerificationResult;
import org.eclipse.wst.xml.security.core.verify.VerifySignature;
import org.w3c.dom.Document;

/**
 * <p>JUnit test class for {@link org.eclipse.wst.xml.security.core.sign.CreateSignature}.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class CreateSignatureTest extends TestCase {
    /** The signature model used in these tests. */
    private Signature signature = null;
    /** The signature id. */
    private static final String SIGNATURE_ID = "JUnitTest";
    /** The temporary file containing the signature. */
    private static final String SIGNED_FILE_NAME = "result.xml";
    private static final String KEYSTORE_PATH = "resources/sample_keystore.jks";
    private static final String KEYSTORE_PASSWORD = "sampleKeystore";
    private static final String KEY_ALIAS = "sampleKey";
    private static final String KEY_PASSWORD = "sampleKey";

    /**
     * Sets up the Apache XML Security API and repares the SignatureWizard object.
     *
     * @throws Exception to indicate any exceptional condition
     */
    protected void setUp() throws Exception {
        org.apache.xml.security.Init.init();

        Keystore sampleKeyStore = new Keystore(XMLSecurityToolsTestPlugin.getTestFileLocation(KEYSTORE_PATH), KEYSTORE_PASSWORD,
                Globals.KEYSTORE_TYPE);
        sampleKeyStore.load();

        signature = new Signature();
        signature.setBsp(false);
        signature.setLaunchEncryptionWizard(false);
        signature.setCanonicalizationAlgorithm("Exclusive with comments");
        signature.setKeyAlias(KEY_ALIAS);
        signature.setKeyPassword(KEY_PASSWORD.toCharArray());
        signature.setDetachedFile(null);
        signature.setFile(XMLSecurityToolsTestPlugin.getTestFileLocation("resources/FirstSteps.xml"));
        signature.setKeystore(sampleKeyStore);
        signature.setKeystorePassword(KEYSTORE_PASSWORD.toCharArray());
        signature.setMessageDigestAlgorithm("SHA 1");
        signature.setResource("document");
        signature.setSignatureAlgorithm("DSA with SHA 1 (DSS)");
        signature.setSignatureId(SIGNATURE_ID);
        signature.setSignatureProperties(null);
        signature.setSignatureType("enveloped");
        signature.setTransformationAlgorithm("None");
        signature.setXpath(null);
    }

    /**
     * Kills the SignatureWizard object and deletes the signed XML document.
     *
     * @throws Exception to indicate any exceptional condition
     */
    public void tearDown() throws Exception {
        signature = null;

        try {
            File file = new File(XMLSecurityToolsTestPlugin.getTestFileLocation(SIGNED_FILE_NAME));
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception ex) {
            ;
        }
    }

    /**
     * Test method for
     * {@link org.eclipse.wst.xml.security.core.sign.CreateSignature#sign(org.eclipse.wst.xml.security.core.sign.Signature, org.eclipse.jface.text.ITextSelection)}
     *
     */
    public void testSign() {
        CreateSignature sign = new CreateSignature();
        VerifySignature verify = new VerifySignature();
        Document result = null;
        try {
            result = sign.sign(signature, null, null);
            
            String signedFilename = XMLSecurityToolsTestPlugin.getTestFileLocation(SIGNED_FILE_NAME);

            FileOutputStream fos = new FileOutputStream(signedFilename);
            if (result != null) {
                XMLUtils.outputDOM(result, fos);
            }
            fos.flush();
            fos.close();

            ArrayList<VerificationResult> signatures = verify
                    .verify(signedFilename, SIGNATURE_ID);
            assertEquals("valid", (signatures.get(0)).getStatus());

            signatures = verify.verify(signedFilename, "wrongID");
            assertEquals("unknown", (signatures.get(0)).getStatus());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.getLocalizedMessage());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10585.java