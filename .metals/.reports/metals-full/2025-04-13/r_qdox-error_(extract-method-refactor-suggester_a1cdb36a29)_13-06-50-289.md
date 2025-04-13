error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11011.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11011.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11011.java
text:
```scala
j@@ar.addAsManifestResource(PoolOverrideTestCase.class.getPackage(), "jboss-ejb3.xml", "jboss-ejb3.xml");

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

package org.jboss.as.test.integration.ejb.pool.override;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJBException;
import javax.naming.InitialContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.arquillian.api.ServerSetup;
import org.jboss.as.arquillian.api.ServerSetupTask;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.test.integration.ejb.remote.common.EJBManagementUtil;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests {@link org.jboss.ejb3.annotation.Pool} annotation usage and the &lt;pool&gt;
 * element usage in jboss-ejb3.xml for EJBs.
 *
 * @author Jaikiran Pai
 */
@RunWith(Arquillian.class)
@ServerSetup(PoolOverrideTestCase.PoolOverrideTestCaseSetup.class)
public class PoolOverrideTestCase {

    private static final Logger logger = Logger.getLogger(PoolOverrideTestCase.class);

    static class PoolOverrideTestCaseSetup implements ServerSetupTask {

        @Override
        public void setup(final ManagementClient managementClient, final String containerId) throws Exception {
            EJBManagementUtil.createStrictMaxPool(managementClient.getControllerClient(), PoolAnnotatedEJB.POOL_NAME, 1, 10, TimeUnit.MILLISECONDS);
            EJBManagementUtil.createStrictMaxPool(managementClient.getControllerClient(), PoolSetInDDBean.POOL_NAME_IN_DD, 1, 10, TimeUnit.MILLISECONDS);
            EJBManagementUtil.createStrictMaxPool(managementClient.getControllerClient(), PoolAnnotatedAndSetInDDBean.POOL_NAME, 1, 10, TimeUnit.MILLISECONDS);
        }

        @Override
        public void tearDown(final ManagementClient managementClient, final String containerId) throws Exception {
            EJBManagementUtil.removeStrictMaxPool(managementClient.getControllerClient(), PoolAnnotatedEJB.POOL_NAME);
            EJBManagementUtil.removeStrictMaxPool(managementClient.getControllerClient(), PoolSetInDDBean.POOL_NAME_IN_DD);
            EJBManagementUtil.removeStrictMaxPool(managementClient.getControllerClient(), PoolAnnotatedAndSetInDDBean.POOL_NAME);
        }
    }

    @Deployment
    public static Archive createDeployment() {

        final JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "ejb-pool-override-test.jar");
        jar.addPackage(PoolAnnotatedEJB.class.getPackage());
        jar.addAsManifestResource("ejb/pool/override/jboss-ejb3.xml", "jboss-ejb3.xml");
        return jar;
    }

    /**
     * Test that a stateless bean configured with the {@link org.jboss.ejb3.annotation.Pool} annotation
     * is processed correctly and the correct pool is used by the bean
     *
     * @throws Exception
     */
    @Test
    public void testSLSBWithPoolAnnotation() throws Exception {
        final PoolAnnotatedEJB bean = InitialContext.doLookup("java:module/" + PoolAnnotatedEJB.class.getSimpleName());
        this.testSimulatenousInvocationOnEJBsWithSingleInstanceInPool(bean);
    }

    /**
     * Test that a stateless bean configured with a pool reference in the jboss-ejb3.xml is processed correctly
     * and the correct pool is used by the bean
     *
     * @throws Exception
     */
    @Test
    public void testSLSBWithPoolReferenceInDD() throws Exception {
        final PoolSetInDDBean bean = InitialContext.doLookup("java:module/" + PoolSetInDDBean.class.getSimpleName());
        this.testSimulatenousInvocationOnEJBsWithSingleInstanceInPool(bean);
    }

    /**
     * Test that a stateless bean which has been annotated with a {@link org.jboss.ejb3.annotation.Pool} annotation
     * and also has a jboss-ejb3.xml with a bean instance pool reference, is processed correctly and the deployment
     * descriptor value overrides the annotation value. To make sure that the annotation value is overriden by the
     * deployment descriptor value, the {@link PoolAnnotatedAndSetInDDBean} is annotated with {@link org.jboss.ejb3.annotation.Pool}
     * whose value points to a non-existent pool name
     *
     * @throws Exception
     */
    @Test
    public void testPoolConfigInDDOverridesAnnotation() throws Exception {
        final PoolAnnotatedAndSetInDDBean bean = InitialContext.doLookup("java:module/" + PoolAnnotatedAndSetInDDBean.class.getSimpleName());
        this.testSimulatenousInvocationOnEJBsWithSingleInstanceInPool(bean);
    }

    /**
     * Submits 2 {@link Callable}s to a {@link java.util.concurrent.Executor} to invoke on the {@link AbstractSlowBean bean}
     * simulatenously. The bean is backed by a pool with <code>maxPoolSize</code> of 1 and the bean method "sleeps"
     * for 1 second. So the second invocation waits for the first to complete. The pool for these beans is configured
     * such that the instance acquisition timeout on the pool is (very) low compared to the time the bean method processing/sleep
     * time. As a result, the second invocation is expected to fail. This method just validates that the correct pool is being used
     * by the bean and not the default pool whose instance acquisition timeout is greater and these custom pools.
     *
     * @param bean The bean to invoke on
     * @throws Exception
     */
    private void testSimulatenousInvocationOnEJBsWithSingleInstanceInPool(final AbstractSlowBean bean) throws Exception {
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Void> firstBeanInvocationResult = null;
        Future<Void> secondBeanInvocationResult = null;
        try {
            final PooledBeanInvoker firstBeanInvoker = new PooledBeanInvoker(bean, 1000);
            firstBeanInvocationResult = executorService.submit(firstBeanInvoker);

            final PooledBeanInvoker secondBeanInvoker = new PooledBeanInvoker(bean, 1000);
            secondBeanInvocationResult = executorService.submit(secondBeanInvoker);
        } finally {
            executorService.shutdown();
        }
        boolean firstInvocationFailed = false;
        boolean secondInvocationFailed = false;
        try {
            firstBeanInvocationResult.get(5, TimeUnit.SECONDS);
        } catch (ExecutionException ee) {
            if (ee.getCause() instanceof EJBException) {
                logger.info("Got EJBException for first invocation ", ee.getCause());
                firstInvocationFailed = true;
            } else {
                throw ee;
            }
        }
        try {
            secondBeanInvocationResult.get(5, TimeUnit.SECONDS);
        } catch (ExecutionException ee) {
            if (ee.getCause() instanceof EJBException) {
                logger.info("Got EJBException for second invocation ", ee.getCause());
                secondInvocationFailed = true;
            } else {
                throw ee;
            }
        }
        // if both failed, then it's an error
        if (firstInvocationFailed && secondInvocationFailed) {
            Assert.fail("Both first and second invocations to EJB failed. Only one was expected to fail");
        }
        // if none failed, then it's an error too
        if (!firstInvocationFailed && !secondInvocationFailed) {
            Assert.fail("Both first and second invocations to EJB passed. Only one was expected to pass");
        }
    }

    /**
     * Invokes the {@link AbstractSlowBean#delay(long)} bean method
     */
    private class PooledBeanInvoker implements Callable<Void> {

        private AbstractSlowBean bean;
        private long beanProcessingTime;

        PooledBeanInvoker(final AbstractSlowBean bean, final long beanProcessingTime) {
            this.bean = bean;
            this.beanProcessingTime = beanProcessingTime;
        }

        @Override
        public Void call() throws Exception {
            this.bean.delay(this.beanProcessingTime);
            return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11011.java