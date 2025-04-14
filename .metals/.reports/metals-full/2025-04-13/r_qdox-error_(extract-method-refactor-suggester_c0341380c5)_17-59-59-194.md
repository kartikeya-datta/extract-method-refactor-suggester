error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7070.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7070.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7070.java
text:
```scala
.@@getResString("bouncy_castle_unavailable_message")); //$NON-NLS-1$

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.jmeter.assertions;

import java.io.Serializable;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.util.JMeterUtils;

public class SMIMEAssertionTestElement extends AbstractTestElement implements
        Serializable, Assertion {

    private static final long serialVersionUID = 1L;

    //+JMX file attributes - do not change values!
    private static final String VERIFY_SIGNATURE_KEY         = "SMIMEAssert.verifySignature"; // $NON-NLS-1$
    private static final String NOT_SIGNED_KEY               = "SMIMEAssert.notSigned"; // $NON-NLS-1$
    private static final String SIGNER_NO_CHECK_KEY          = "SMIMEAssert.signerNoCheck"; // $NON-NLS-1$
    private static final String SIGNER_CHECK_BY_FILE_KEY     = "SMIMEAssert.signerCheckByFile"; // $NON-NLS-1$
    private static final String SIGNER_CERT_FILE_KEY         = "SMIMEAssert.signerCertFile"; // $NON-NLS-1$
    private static final String SINGER_CHECK_CONSTRAINTS_KEY = "SMIMEAssert.signerCheckConstraints"; // $NON-NLS-1$
    private static final String SIGNER_SERIAL_KEY            = "SMIMEAssert.signerSerial"; // $NON-NLS-1$
    private static final String SIGNER_EMAIL_KEY             = "SMIMEAssert.signerEmail"; // $NON-NLS-1$
    private static final String SIGNER_DN_KEY                = "SMIMEAssert.signerDn"; // $NON-NLS-1$
    private static final String ISSUER_DN_KEY                = "SMIMEAssert.issuerDn"; // $NON-NLS-1$
    private static final String MESSAGE_POSITION             = "SMIMEAssert.messagePosition"; // $NON-NLS-1$
    //-JMX file attributes

    public SMIMEAssertionTestElement() {
        super();
    }

    @Override
    public AssertionResult getResult(SampleResult response) {
        try {
            return SMIMEAssertion.getResult(this, response, getName());
        } catch (NoClassDefFoundError e) {
            AssertionResult assertionResult = new AssertionResult(getName());
            assertionResult.setError(true);
            assertionResult.setResultForFailure(JMeterUtils
                .getResString("bouncy_castle_unavailable_message"));
            return assertionResult;
        }
    }

    public boolean isVerifySignature() {
        return getPropertyAsBoolean(VERIFY_SIGNATURE_KEY);
    }

    public void setVerifySignature(boolean verifySignature) {
        setProperty(VERIFY_SIGNATURE_KEY, verifySignature);
    }

    public String getIssuerDn() {
        return getPropertyAsString(ISSUER_DN_KEY);
    }

    public void setIssuerDn(String issuertDn) {
        setProperty(ISSUER_DN_KEY, issuertDn);
    }

    public boolean isSignerCheckByFile() {
        return getPropertyAsBoolean(SIGNER_CHECK_BY_FILE_KEY);
    }

    public void setSignerCheckByFile(boolean signerCheckByFile) {
        setProperty(SIGNER_CHECK_BY_FILE_KEY, signerCheckByFile);
    }

    public boolean isSignerCheckConstraints() {
        return getPropertyAsBoolean(SINGER_CHECK_CONSTRAINTS_KEY);
    }

    public void setSignerCheckConstraints(boolean signerCheckConstraints) {
        setProperty(SINGER_CHECK_CONSTRAINTS_KEY, signerCheckConstraints);
    }

    public boolean isSignerNoCheck() {
        return getPropertyAsBoolean(SIGNER_NO_CHECK_KEY);
    }

    public void setSignerNoCheck(boolean signerNoCheck) {
        setProperty(SIGNER_NO_CHECK_KEY, signerNoCheck);
    }

    public String getSignerCertFile() {
        return getPropertyAsString(SIGNER_CERT_FILE_KEY);
    }

    public void setSignerCertFile(String signerCertFile) {
        setProperty(SIGNER_CERT_FILE_KEY, signerCertFile);
    }

    public String getSignerDn() {
        return getPropertyAsString(SIGNER_DN_KEY);
    }

    public void setSignerDn(String signerDn) {
        setProperty(SIGNER_DN_KEY, signerDn);
    }

    public String getSignerSerial() {
        return getPropertyAsString(SIGNER_SERIAL_KEY);
    }

    public void setSignerSerial(String signerSerial) {
        setProperty(SIGNER_SERIAL_KEY, signerSerial);
    }

    public String getSignerEmail() {
        return getPropertyAsString(SIGNER_EMAIL_KEY);
    }

    public void setSignerEmail(String signerEmail) {
        setProperty(SIGNER_EMAIL_KEY, signerEmail);
    }

    public boolean isNotSigned() {
        return getPropertyAsBoolean(NOT_SIGNED_KEY);
    }

    public void setNotSigned(boolean notSigned) {
        setProperty(NOT_SIGNED_KEY, notSigned);
    }

    public String getSpecificMessagePosition() {
        return getPropertyAsString(MESSAGE_POSITION);
    }
    
    public int getSpecificMessagePositionAsInt() {
        return getPropertyAsInt(MESSAGE_POSITION, 0);
    }
    
    public void setSpecificMessagePosition(String position) {
        setProperty(MESSAGE_POSITION, position);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7070.java