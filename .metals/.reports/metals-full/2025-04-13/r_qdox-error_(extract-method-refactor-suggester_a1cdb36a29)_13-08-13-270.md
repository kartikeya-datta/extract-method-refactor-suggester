error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3907.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3907.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3907.java
text:
```scala
l@@og.info("Server was sucessfully reloaded");

/*
* JBoss, Home of Professional Open Source.
* Copyright 2013, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.as.test.iiop.client;

import org.jboss.arquillian.container.test.api.ContainerController;
import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.arquillian.api.ServerSetup;
import org.jboss.as.arquillian.api.ServerSetupTask;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.test.integration.management.util.MgmtOperationException;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.ORBPackage.InvalidName;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.transaction.Status;

import java.io.IOException;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.*;

/**
 * @author Ondrej Chaloupka
 */
@RunWith(Arquillian.class)
@RunAsClient
@ServerSetup({
    IIOPTransactionPropagationTestCase.JTSSetup.class})
public class IIOPTransactionPropagationTestCase {
    private static final Logger log = Logger.getLogger(IIOPTransactionPropagationTestCase.class);
    
    @ArquillianResource
    private static ContainerController container;

    @ArquillianResource
    private Deployer deployer;

    public static final String DEFAULT_JBOSSAS = "iiop-client";
    private static final String ARCHIVE_NAME = "iiop-jts-ctx-propag-test";
    
    private static InitialContext context;
    

    @Deployment(name = ARCHIVE_NAME, managed = true)
    @TargetsContainer(DEFAULT_JBOSSAS)
    public static Archive<?> deploy() throws InvalidName, SystemException {       
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, ARCHIVE_NAME + ".jar")
                .addClass(IIOPTestBean.class)
                .addClass(IIOPTestBeanHome.class)
                .addClass(IIOPTestRemote.class)
                .addAsManifestResource(IIOPTransactionPropagationTestCase.class.getPackage(), "jboss-ejb3.xml", "jboss-ejb3.xml");
        // File testPackage = new File("/tmp", ARCHIVE_NAME + ".jar");
        // jar.as(ZipExporter.class).exportTo(testPackage, true);
        return jar;
    }
    
    @BeforeClass
    public static void beforeClass() throws InvalidName, SystemException, NamingException {
        // Orb presseting has to be done before the ORB is started to be used
        Util.presetOrb();
        context = Util.getContext();
    }
    
    @AfterClass
    public static void afterClass() throws NamingException {
        Util.tearDownOrb();
    }
    
    @Test
    public void testIIOPInvocation() throws Throwable {
        final Object iiopObj = context.lookup(IIOPTestBean.class.getSimpleName());
        final IIOPTestBeanHome beanHome = (IIOPTestBeanHome) PortableRemoteObject.narrow(iiopObj, IIOPTestBeanHome.class);
        final IIOPTestRemote bean = beanHome.create();
               
        try {
            Util.startCorbaTx();
            Assert.assertEquals(Status.STATUS_ACTIVE, bean.transactionStatus());
            Assert.assertEquals("transaction-attribute-mandatory", bean.callMandatory());
            Util.commitCorbaTx();
        } catch(Throwable e) {
            Util.rollbackCorbaTx();
            throw e;
        }
    }

    @Test
    public void testIIOPNeverCallInvocation() throws Throwable {
        final Object iiopObj = context.lookup(IIOPTestBean.class.getSimpleName());
        final IIOPTestBeanHome beanHome = (IIOPTestBeanHome) PortableRemoteObject.narrow(iiopObj, IIOPTestBeanHome.class);
        final IIOPTestRemote bean = beanHome.create();
        
        try {
            Util.startCorbaTx();
            Assert.assertEquals(Status.STATUS_ACTIVE, bean.transactionStatus());
            bean.callNever();
            Assert.fail("Exception is supposed to be here thrown from TransactionAttribute.NEVER method");
        } catch(Exception e) {
            // this is OK - is expected never throwing that TS exists
            Assert.assertEquals(Status.STATUS_ACTIVE, bean.transactionStatus());
        } finally {
            Util.rollbackCorbaTx();
        }
    }
    
    @Test
    public void testIIOPInvocationWithRollbackOnly() throws Throwable {
        final Object iiopObj = context.lookup(IIOPTestBean.class.getSimpleName());
        final IIOPTestBeanHome beanHome = (IIOPTestBeanHome) PortableRemoteObject.narrow(iiopObj, IIOPTestBeanHome.class);
        final IIOPTestRemote bean = beanHome.create();
        
        try {
            Util.startCorbaTx();
            Assert.assertEquals(Status.STATUS_ACTIVE, bean.transactionStatus());
            bean.callRollbackOnly();
            Assert.assertEquals(Status.STATUS_MARKED_ROLLBACK, bean.transactionStatus());
        } finally {
            Util.rollbackCorbaTx();
        }
    }
   
    
    /**
     * The setup tasks are bound to deploy/undeploy actions. 
     */
    static class JTSSetup implements ServerSetupTask {
        public static final String JACORB_TRANSACTIONS_JTA = "spec";
        public static final String JACORB_TRANSACTIONS_JTS = "on";
        
        private boolean isTransactionJTS = true;
        private String jacorbTransaction = JACORB_TRANSACTIONS_JTA; 
        
        ManagementClient managementClient = null;
        
        /**
         * The setup method is prepared here just for sure.
         * The jts switching on is done by xslt transformation before the test is launched.  
         */
        @Override
        public void setup(ManagementClient managementClient, String containerId) throws Exception {
            log.info("JTSSetup.setup");
            this.managementClient = managementClient;
            boolean isNeedReload = false;
            
            // check what is defined before
            isTransactionJTS = checkJTSOnTransactions();
            jacorbTransaction = checkTransactionsOnJacorb();
            
            if(!isTransactionJTS) {
                setTransactionJTS(true);
                isNeedReload = true;
            }
            if(JACORB_TRANSACTIONS_JTA.equalsIgnoreCase(jacorbTransaction)) {
                setJacorbJTS(true);
                isNeedReload = true;
            }
            
            if(isNeedReload) {
                reload();
            }
        }

        @Override
        public void tearDown(final ManagementClient managementClient, final String containerId) throws Exception {
            log.info("JTSSetup.tearDown");
            this.managementClient = managementClient;
            boolean isNeedReload = false;
            
            // get it back what was defined before
            // if it was not JTS, set it back to JTA (was set to JTS setup())
            // if it was JTA, set it back to JTA (was set to JTS in setup())
            if(JACORB_TRANSACTIONS_JTA.equalsIgnoreCase(jacorbTransaction)) {
                setJacorbJTS(false);
                isNeedReload = true;
            }
            if(!isTransactionJTS) {
                setTransactionJTS(false);
            }
            
            if(isNeedReload) {
                reload();
            }
        }
        
        public boolean checkJTSOnTransactions() throws IOException, MgmtOperationException {
            /*     /subsystem=transactions:read-attribute(name=jts)   */
            final ModelNode address = new ModelNode();
            address.add("subsystem", "transactions");
            final ModelNode operation = new ModelNode();
            operation.get(OP).set(READ_ATTRIBUTE_OPERATION);
            operation.get(OP_ADDR).set(address);
            operation.get("name").set("jts");

            return executeOperation(operation).asBoolean();
        }

        public String checkTransactionsOnJacorb() throws IOException, MgmtOperationException {
            /*     /subsystem=jacorb:read-attribute(name=transactions)   */
            final ModelNode address = new ModelNode();
            address.add("subsystem", "jacorb");
            final ModelNode operation = new ModelNode();
            operation.get(OP).set(READ_ATTRIBUTE_OPERATION);
            operation.get(OP_ADDR).set(address);
            operation.get("name").set("transactions");

            return executeOperation(operation).asString();
        }

        public void setTransactionJTS(boolean enabled)  throws IOException, MgmtOperationException {
            /*     /subsystem=transactions:write-attribute(name=jts,value=false|true)   */
            ModelNode address = new ModelNode();
            address.add("subsystem", "transactions");
            ModelNode operation = new ModelNode();
            operation.get(OP).set(WRITE_ATTRIBUTE_OPERATION);
            operation.get(OP_ADDR).set(address);
            operation.get("name").set("jts");
            operation.get("value").set(enabled);
            log.info("operation=" + operation);
            executeOperation(operation);
        }
        
        public void setJacorbJTS(boolean enabled) throws IOException, MgmtOperationException {
            String transactionsOnJacorb = (enabled) ? JACORB_TRANSACTIONS_JTS : JACORB_TRANSACTIONS_JTA;
            ModelNode address = new ModelNode();
            address.add("subsystem", "jacorb");
            ModelNode operation = new ModelNode();
            operation.get(OP).set(WRITE_ATTRIBUTE_OPERATION);
            operation.get(OP_ADDR).set(address);
            operation.get("name").set("transactions");
            operation.get("value").set(transactionsOnJacorb);
            log.info("operation=" + operation);
            executeOperation(operation);
        }
        
        public boolean reload() throws IOException, MgmtOperationException {
            /*      :reload()     */
            final ModelNode operation = new ModelNode();
            operation.get(OP).set("reload");
            log.info("operation=" + operation);

            try {
                executeOperation(operation);
            } catch (Exception e) {
                log.error("Exception applying reload operation. This is probably fine, as the server probably shut down before the response was sent", e);
            }
            boolean reloaded = false;
            int i = 0;
            while (!reloaded) {
                try {
                    Thread.sleep(2000);
                    if (managementClient.isServerInRunningState()) {
                        reloaded = true;
                        log.info("Server was successfully reloaded");
                    }
                } catch (Throwable t) {
                    // nothing to do, just waiting
                } finally {
                    if (!reloaded && i++ > 20) {
                        throw new MgmtOperationException("Server reloading failed");
                    }
                }
            }
            return reloaded;
        }
        
        private ModelNode executeOperation(final ModelNode op, boolean unwrapResult) throws IOException, MgmtOperationException {
            ModelNode ret = managementClient.getControllerClient().execute(op);
            if (!unwrapResult) return ret;

            if (!SUCCESS.equals(ret.get(OUTCOME).asString())) {
                throw new MgmtOperationException("Management operation failed: " + ret.get(FAILURE_DESCRIPTION), op, ret);
            }
            return ret.get(RESULT);
        }
        
        private ModelNode executeOperation(final ModelNode op) throws IOException, MgmtOperationException {
            return executeOperation(op, true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3907.java