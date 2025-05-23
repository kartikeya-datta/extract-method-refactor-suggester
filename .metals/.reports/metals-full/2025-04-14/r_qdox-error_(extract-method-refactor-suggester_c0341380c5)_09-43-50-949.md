error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11013.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11013.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11013.java
text:
```scala
j@@ar.addAsManifestResource(EJBClientXidTransactionTestCase.class.getPackage(), "persistence.xml", "persistence.xml");

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.test.integration.ejb.remote.client.api.tx;

import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;

import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionManagerImple;
import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionSynchronizationRegistryImple;
import com.arjuna.ats.jta.common.JTAEnvironmentBean;
import com.arjuna.ats.jta.common.jtaPropertyManager;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.ejb.client.EJBClient;
import org.jboss.ejb.client.EJBClientTransactionContext;
import org.jboss.ejb.client.StatelessEJBLocator;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Jaikiran Pai
 */
@RunWith(Arquillian.class)
@RunAsClient
public class EJBClientXidTransactionTestCase {

    private static final Logger logger = Logger.getLogger(EJBClientXidTransactionTestCase.class);

    private static final String APP_NAME = "ejb-remote-client-api-xidtx-test";

    private static final String MODULE_NAME = "ejb";

    private static TransactionManager txManager;

    private static TransactionSynchronizationRegistry txSyncRegistry;

    /**
     * Creates an EJB deployment
     *
     * @return
     */
    @Deployment
    public static Archive<?> createDeployment() {
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, APP_NAME + ".ear");

        final JavaArchive jar = ShrinkWrap.create(JavaArchive.class, MODULE_NAME + ".jar");
        jar.addPackage(EJBClientXidTransactionTestCase.class.getPackage());
        jar.addAsManifestResource("ejb/remote/client/tx/persistence.xml", "persistence.xml");

        ear.addAsModule(jar);

        return ear;
    }


    /**
     * Create and setup the remoting connection
     *
     * @throws Exception
     */
    @BeforeClass
    public static void beforeTestClass() throws Exception {
        // setup the tx manager and tx sync registry
        instantiateTxManagement();
    }

    /**
     * Create and setup the EJB client context backed by the remoting receiver
     *
     * @throws Exception
     */
    @Before
    public void beforeTest() throws Exception {
        // create a client side tx context
        final EJBClientTransactionContext txContext = EJBClientTransactionContext.create(txManager, txSyncRegistry);
        // associate the tx context
        EJBClientTransactionContext.setGlobalContext(txContext);
    }

    private static void instantiateTxManagement() {
        // These system properties are required or else we end up picking up JTS transaction manager,
        // which is not what we want
        System.setProperty(JTAEnvironmentBean.class.getSimpleName() + "." + "transactionManagerClassName", TransactionManagerImple.class.getName());
        System.setProperty(JTAEnvironmentBean.class.getSimpleName() + "." + "transactionSynchronizationRegistryClassName", TransactionSynchronizationRegistryImple.class.getName());
        txManager = jtaPropertyManager.getJTAEnvironmentBean().getTransactionManager();
        txSyncRegistry = jtaPropertyManager.getJTAEnvironmentBean().getTransactionSynchronizationRegistry();
    }

    /**
     * Tests that a CMT stateless bean method, with Mandatory tx attribute, invocation works as expected
     * when the transaction is remotely started on the client side using a client side transaction manager
     *
     * @throws Exception
     */
    @Test
    public void testSLSBMandatoryTx() throws Exception {
        final StatelessEJBLocator<CMTRemote> cmtRemoteBeanLocator = new StatelessEJBLocator<CMTRemote>(CMTRemote.class, APP_NAME, MODULE_NAME, CMTBean.class.getSimpleName(), "");
        final CMTRemote cmtRemoteBean = EJBClient.createProxy(cmtRemoteBeanLocator);

        // start the transaction
        txManager.begin();
        // invoke the bean
        cmtRemoteBean.mandatoryTxOp();
        // end the tx
        txManager.commit();
    }

    /**
     * Tests various transaction scenarios managed on the client side via the client side transaction manager
     *
     * @throws Exception
     */
    @Test
    public void testClientTransactionManagement() throws Exception {
        final StatelessEJBLocator<RemoteBatch> batchBeanLocator = new StatelessEJBLocator<RemoteBatch>(RemoteBatch.class, APP_NAME, MODULE_NAME, BatchCreationBean.class.getSimpleName(), "");
        final RemoteBatch batchBean = EJBClient.createProxy(batchBeanLocator);

        final StatelessEJBLocator<BatchRetriever> batchRetrieverLocator = new StatelessEJBLocator<BatchRetriever>(BatchRetriever.class, APP_NAME, MODULE_NAME, BatchFetchingBean.class.getSimpleName(), "");
        final BatchRetriever batchRetriever = EJBClient.createProxy(batchRetrieverLocator);

        final String batchName = "Simple Batch";
        // create a batch
        txManager.begin();
        try {
            batchBean.createBatch(batchName);
        } catch (Exception e) {
            txManager.rollback();
            throw e;
        }
        txManager.commit();

        // fetch the batch and make sure it contains the right state
        final Batch batchAfterCreation = batchRetriever.fetchBatch(batchName);
        Assert.assertNotNull("Batch was null after creation", batchAfterCreation);
        Assert.assertNull("Unexpected steps in batch, after creation", batchAfterCreation.getStepNames());

        // add step1 to the batch
        final String step1 = "Simple step1";
        txManager.begin();
        try {
            batchBean.step1(batchName, step1);
        } catch (Exception e) {
            txManager.rollback();
            throw e;
        }
        txManager.commit();

        String successFullyCompletedSteps = step1;

        // fetch the batch and make sure it contains the right state
        final Batch batchAfterStep1 = batchRetriever.fetchBatch(batchName);
        Assert.assertNotNull("Batch after step1 was null", batchAfterStep1);
        Assert.assertEquals("Unexpected steps in batch, after step1", successFullyCompletedSteps, batchAfterStep1.getStepNames());


        // now add a failing step2
        final String appExceptionStep2 = "App exception Step 2";
        txManager.begin();
        try {
            batchBean.appExceptionFailingStep2(batchName, appExceptionStep2);
            Assert.fail("Expected a application exception");
        } catch (SimpleAppException sae) {
            // expected
            txManager.rollback();
        }

        final Batch batchAfterAppExceptionStep2 = batchRetriever.fetchBatch(batchName);
        Assert.assertNotNull("Batch after app exception step2 was null", batchAfterAppExceptionStep2);
        Assert.assertEquals("Unexpected steps in batch, after app exception step2", successFullyCompletedSteps, batchAfterAppExceptionStep2.getStepNames());

        // now add a successful step2
        final String step2 = "Simple Step 2";
        txManager.begin();
        try {
            batchBean.successfulStep2(batchName, step2);
        } catch (Exception e) {
            txManager.rollback();
            throw e;
        }
        // don't yet commit and try and retrieve the batch
        final Batch batchAfterStep2BeforeCommit = batchRetriever.fetchBatch(batchName);
        Assert.assertNotNull("Batch after step2, before commit was null", batchAfterStep2BeforeCommit);
        Assert.assertEquals("Unexpected steps in batch, after step2 before commit", successFullyCompletedSteps, batchAfterStep2BeforeCommit.getStepNames());

        // now commit
        txManager.commit();
        // keep track of successfully completely steps
        successFullyCompletedSteps = successFullyCompletedSteps + "," + step2;

        // now retrieve and check the batch
        final Batch batchAfterStep2 = batchRetriever.fetchBatch(batchName);
        Assert.assertNotNull("Batch after step2 was null", batchAfterStep2);
        Assert.assertEquals("Unexpected steps in batch, after step2", successFullyCompletedSteps, batchAfterStep2.getStepNames());

        // now add independent Step3 (i.e. the bean method has a REQUIRES_NEW semantics, so that the
        // client side tx doesn't play a role)
        final String step3 = "Simple Step 3";
        txManager.begin();
        batchBean.independentStep3(batchName, step3);
        // rollback (but it shouldn't end up rolling back step3 because that was done in server side independent tx)
        txManager.rollback();
        // keep track of successfully completely steps
        successFullyCompletedSteps = successFullyCompletedSteps + "," + step3;

        // now retrieve and check the batch
        final Batch batchAfterStep3 = batchRetriever.fetchBatch(batchName);
        Assert.assertNotNull("Batch after step3 was null", batchAfterStep3);
        Assert.assertEquals("Unexpected steps in batch, after step3", successFullyCompletedSteps, batchAfterStep3.getStepNames());

        // now add step4 but don't commit
        final String step4 = "Simple Step 4";
        txManager.begin();
        batchBean.step4(batchName, step4);

        // now add a system exception throwing step
        final String sysExceptionStep2 = "Sys exception step2";
        try {
            batchBean.systemExceptionFailingStep2(batchName, sysExceptionStep2);
            Assert.fail("Expected a system exception");
        } catch (Exception e) {
            // expected
            //Assert.assertEquals("Unexpected transaction state", Status.STATUS_ROLLEDBACK, userTransaction.getStatus());
            txManager.rollback();
        }

        // now retrieve and check the batch
        final Batch batchAfterSysException = batchRetriever.fetchBatch(batchName);
        Assert.assertNotNull("Batch after system exception was null", batchAfterSysException);
        Assert.assertEquals("Unexpected steps in batch, after system exception", successFullyCompletedSteps, batchAfterSysException.getStepNames());

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11013.java