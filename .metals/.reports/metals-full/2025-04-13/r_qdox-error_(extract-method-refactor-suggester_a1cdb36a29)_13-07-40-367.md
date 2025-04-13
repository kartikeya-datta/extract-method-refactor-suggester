error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14527.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14527.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14527.java
text:
```scala
private static final I@@nteger WAIT_TIME_S = 10;

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

package org.jboss.as.test.integration.ejb.async;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.arquillian.api.ContainerResource;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests that a simple async annotation works. Enhanced test by migration [JIRA JBQA-5483].
 *
 * @author Stuart Douglas, Ondrej Chaloupka
 */
@RunWith(Arquillian.class)
public class AsyncMethodTestCase {
    private static final Logger log = Logger.getLogger(AsyncMethodTestCase.class);
    private static final String ARCHIVE_NAME = "AsyncTestCase";
    private static final Integer WAIT_TIME_S = 2;

    @ArquillianResource
    private InitialContext iniCtx;

    @ContainerResource
    private InitialContext remoteContext;

    @Deployment(name = "asynctest")
    public static Archive<?> deploy() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, ARCHIVE_NAME + ".jar");
        jar.addPackage(AsyncMethodTestCase.class.getPackage());
        jar.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        jar.addAsManifestResource(AsyncMethodTestCase.class.getPackage(), "ejb-jar.xml", "ejb-jar.xml");
        log.info(jar.toString(true));
        return jar;
    }

    protected <T> T lookup(Class<T> beanType) throws NamingException {
        return beanType.cast(iniCtx.lookup("java:global/" + ARCHIVE_NAME + "/" + beanType.getSimpleName() + "!"
                + beanType.getName()));
    }

    /**
     * Stateless - void returned
     */
    @Test
    public void testVoidAsyncStatelessMethod() throws Exception {
        AsyncBean.voidMethodCalled = false;
        AsyncBean bean = lookup(AsyncBean.class);
        Assert.assertFalse(AsyncBean.voidMethodCalled);
        final CountDownLatch latch = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        bean.asyncMethod(latch, latch2);
        latch.countDown();
        latch2.await();
        Assert.assertTrue(AsyncBean.voidMethodCalled);
    }

    /**
     * Stateless - future returned
     */
    @Test
    public void testFutureAsyncStatelessMethod() throws Exception {
        AsyncBean.futureMethodCalled = false;
        AsyncBean bean = lookup(AsyncBean.class);
        final CountDownLatch latch = new CountDownLatch(1);
        final Future<Boolean> future = bean.futureMethod(latch);
        latch.countDown();
        boolean result = future.get();
        Assert.assertTrue(AsyncBean.futureMethodCalled);
        Assert.assertTrue(result);
    }

    /**
     * Stateless request scope
     */
    @Test
    public void testRequestScopeActive() throws Exception {
        AsyncBean bean = lookup(AsyncBean.class);
        final CountDownLatch latch = new CountDownLatch(1);

        final Future<Integer> future = bean.testRequestScopeActive(latch);
        latch.countDown();
        int result = future.get();
        Assert.assertEquals(20, result);
    }

    /**
     * Stateful - void returned
     */
    @Test
    public void testVoidAsyncStatefulMethod() throws Exception {
        AsyncStateful bean = lookup(AsyncStateful.class);
        Assert.assertFalse(AsyncStateful.voidMethodCalled);
        final CountDownLatch latch = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        bean.asyncMethod(latch, latch2);
        latch.countDown();
        latch2.await();
        Assert.assertTrue(AsyncStateful.voidMethodCalled);
    }

    /**
     * Stateful - future returned
     */
    @Test
    public void testFutureAsyncStatefulMethod() throws Exception {
        AsyncStateful bean = lookup(AsyncStateful.class);
        final CountDownLatch latch = new CountDownLatch(1);
        final Future<Boolean> future = bean.futureMethod(latch);
        latch.countDown();
        boolean result = future.get();
        Assert.assertTrue(AsyncStateful.futureMethodCalled);
        Assert.assertTrue(result);
    }

    /**
     * Singleton - void returned
     */
    @Test
    public void testVoidAsyncSingletonMethod() throws Exception {
        AsyncSingleton singleton = lookup(AsyncSingleton.class);
        Assert.assertFalse(AsyncSingleton.voidMethodCalled);
        final CountDownLatch latch = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        singleton.asyncMethod(latch, latch2);
        latch.countDown();
        latch2.await();
        Assert.assertTrue(AsyncSingleton.voidMethodCalled);
    }

    /**
     * Singleton - future returned
     */
    @Test
    public void testFutureAsyncSingletonMethod() throws Exception {
        AsyncSingleton singleton = lookup(AsyncSingleton.class);
        final CountDownLatch latch = new CountDownLatch(1);
        final Future<Boolean> future = singleton.futureMethod(latch);
        latch.countDown();
        boolean result = future.get();
        Assert.assertTrue(AsyncSingleton.futureMethodCalled);
        Assert.assertTrue(result);
    }

    /**
     * Cancelling
     */
    @Ignore("EJBCLIENT-28")
    @Test
    public void testCancelAsyncMethod() throws Exception {
        AsyncBean bean = lookup(AsyncBean.class);
        final CountDownLatch latch = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final Future<String> future = bean.asyncCancelMethod(latch, latch2);
        latch.await(WAIT_TIME_S, TimeUnit.SECONDS);
        Assert.assertFalse(future.isDone()); // we are in async method
        Assert.assertFalse(future.isCancelled());
        boolean wasCanceled = future.cancel(true); // we are running - task can't be canceled
        Assert.assertTrue(future.isDone()); // we are inside of async method but after cancel method isDone should return true
        Assert.assertFalse(future.isCancelled()); // it was not cancelled
        latch2.countDown();
        String result = future.get();
        Assert.assertFalse(wasCanceled); // this should be false because task was not cancelled
        Assert.assertEquals("false;true", result); // the bean knows that it was cancelled
    }

    /**
     * Exception thrown
     */
    @Test
    public void testExceptionThrown() throws NamingException {
        AsyncBean bean = lookup(AsyncBean.class);
        Future<String> future = bean.asyncMethodWithException(true);
        try {
            future.get();
            Assert.fail("ExecutionException was expected");
        } catch (ExecutionException ee) {
            // expecting this and we are able to get caused exception
            Assert.assertNotNull(ee.getCause());
        } catch (Exception e) {
            Assert.fail("ExecutionException was expected and not " + e.getClass());
        }
    }

    /**
     * Asynchronous inherited from parent
     */
    @Test
    public void testVoidParentAsyncMethod() throws Exception {
        AsyncChildBean bean = lookup(AsyncChildBean.class);
        Assert.assertFalse(AsyncParentClass.voidMethodCalled);
        final CountDownLatch latch = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        bean.asyncMethod(latch, latch2);
        latch.countDown();
        latch2.await();
        Assert.assertTrue(AsyncParentClass.voidMethodCalled);
    }

    /**
     * Async declaration in descriptor
     */
    @Test
    public void testAsyncDescriptor() throws Exception {
        AsyncBeanDescriptor bean = lookup(AsyncBeanDescriptor.class);
        Assert.assertFalse(AsyncBeanDescriptor.futureMethodCalled);
        final CountDownLatch latch = new CountDownLatch(1);
        bean.futureMethod(latch);
        latch.await(WAIT_TIME_S, TimeUnit.SECONDS);
        Assert.assertTrue(AsyncBeanDescriptor.futureMethodCalled);
    }

    /**
     * Remote async void call
     */
    @Test
    @RunAsClient
    public void testRemoteAsynchronousVoidCall() throws Exception {
        AsyncBeanRemoteInterface bean = (AsyncBeanRemoteInterface) remoteContext.lookup(
                ARCHIVE_NAME + "/" + AsyncBeanRemote.class.getSimpleName() + "!" + AsyncBeanRemoteInterface.class.getName());
        bean.asyncMethod();
    }
    
    /**
     * Remote async return future call
     */
    @Test
    @RunAsClient
    public void testRemoteAsynchronousReturnFutureCall() throws Exception {
        AsyncBeanRemoteInterface bean = (AsyncBeanRemoteInterface) remoteContext.lookup(
                ARCHIVE_NAME + "/" + AsyncBeanRemote.class.getSimpleName() + "!" + AsyncBeanRemoteInterface.class.getName());
        Future<Boolean> future = bean.futureMethod();
        Assert.assertTrue("Supposing that future.get() method returns TRUE but it returned FALSE", future.get());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14527.java