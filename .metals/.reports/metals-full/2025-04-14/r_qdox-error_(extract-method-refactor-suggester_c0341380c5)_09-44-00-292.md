error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15232.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15232.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15232.java
text:
```scala
j@@ar.addAsManifestResource("META-INF/jboss-ejb-client.xml", "jboss-ejb-client.xml");

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.as.test.multinode.remotecall;

import javax.ejb.EJBException;
import javax.naming.InitialContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Testing multinode communication - calling Stateless and Stateful beans (via remote and local and home interfaces).
 *
 * @author William DeCoste, Ondrej Chaloupka
 */
@RunWith(Arquillian.class)
public class RemoteLocalCallTestCase {
    private static final Logger log = Logger.getLogger(RemoteLocalCallTestCase.class);
    public static final String ARCHIVE_NAME_CLIENT = "remotelocalcall-test-client";
    public static final String ARCHIVE_NAME_SERVER = "remotelocalcall-test-server";

    @BeforeClass
    public static void printSysProps() {
        log.info("System properties:\n" + System.getProperties());
    }

    @Deployment(name = "server")
    @TargetsContainer("multinode-server")
    public static Archive<?> deployment0() {
        JavaArchive jar = createJar(ARCHIVE_NAME_SERVER);
        return jar;
    }

    @Deployment(name = "client")
    @TargetsContainer("multinode-client")
    public static Archive<?> deployment1() {
        JavaArchive jar = createJar(ARCHIVE_NAME_CLIENT);
        jar.addClasses(RemoteLocalCallTestCase.class);
        jar.addAsManifestResource("META-INF/jboss-ejb-client-receivers.xml", "jboss-ejb-client.xml");
        return jar;
    }

    private static JavaArchive createJar(String archiveName) {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, archiveName + ".jar");
        jar.addClasses(StatefulBean.class, StatefulLocal.class, StatefulLocalHome.class, StatefulRemote.class,
                StatefulRemoteHome.class, StatelessBean.class, StatelessLocal.class, StatelessLocalHome.class,
                StatelessRemote.class, StatelessRemoteHome.class);
        return jar;
    }

    @Test
    @OperateOnDeployment("client")
    public void testStatelessLocalFromRemote(@ArquillianResource InitialContext ctx) throws Exception {
        StatefulRemote bean = (StatefulRemote) ctx.lookup("java:module/" + StatefulBean.class.getSimpleName() + "!"
                + StatefulRemote.class.getName());
        Assert.assertNotNull(bean);

        try {
            bean.localCall();
            Assert.fail("should not be allowed to call local interface remotely");
        } catch (EJBException e) {
            // it's OK
        } catch (Exception ee) {
            if(!(ee.getCause() instanceof EJBException)) {
                Assert.fail("should be " + EJBException.class.getName());
            }
        }
    }

    @Test
    @OperateOnDeployment("client")
    public void testStatelessLocalHomeFromRemoteHome(@ArquillianResource InitialContext ctx) throws Exception {
        StatefulRemoteHome home = (StatefulRemoteHome) ctx.lookup("java:module/" + StatefulBean.class.getSimpleName() + "!"
                + StatefulRemoteHome.class.getName());
        StatefulRemote bean = home.create();
        Assert.assertNotNull(bean);

        try {
            bean.localHomeCall();
            Assert.fail("should not be allowed to call local interface remotely");
        } catch (EJBException e) {
            // it's OK
        } catch (Exception ee) {
            if(!(ee.getCause() instanceof EJBException)) {
                Assert.fail("should be " + EJBException.class.getName());
            }
        }
    }

    @Test
    @OperateOnDeployment("client")
    public void testStatelessRemoteFromRemote(@ArquillianResource InitialContext ctx) throws Exception {
        StatefulRemote bean = (StatefulRemote) ctx.lookup("java:module/" + StatefulBean.class.getSimpleName() + "!"
                + StatefulRemote.class.getName());
        Assert.assertNotNull(bean);

        int methodCount = bean.remoteCall();
        Assert.assertEquals(1, methodCount);
    }

    @Test
    @OperateOnDeployment("client")
    public void testStatelessRemoteHomeFromRemoteHome(@ArquillianResource InitialContext ctx) throws Exception {
        StatefulRemoteHome home = (StatefulRemoteHome) ctx.lookup("java:module/" + StatefulBean.class.getSimpleName() + "!"
                + StatefulRemoteHome.class.getName());
        StatefulRemote bean = home.create();
        Assert.assertNotNull(bean);

        int methodCount = bean.remoteHomeCall();
        Assert.assertEquals(1, methodCount);
    }

    @Test
    @OperateOnDeployment("client")
    public void testStatefulRemoteFromRemote(@ArquillianResource InitialContext ctx) throws Exception {
        StatelessRemote bean = (StatelessRemote) ctx.lookup("java:module/" + StatelessBean.class.getSimpleName() + "!"
                + StatelessRemote.class.getName());
        Assert.assertNotNull(bean);

        int methodCount = bean.remoteCall();
        Assert.assertEquals(1, methodCount);
    }

    @Test
    @OperateOnDeployment("client")
    public void testStatefulRemoteHomeFromRemoteHome(@ArquillianResource InitialContext ctx) throws Exception {
        StatelessRemoteHome home = (StatelessRemoteHome) ctx.lookup("java:module/" + StatelessBean.class.getSimpleName() + "!"
                + StatelessRemoteHome.class.getName());
        StatelessRemote bean = home.create();
        Assert.assertNotNull(bean);

        int methodCount = bean.remoteHomeCall();
        Assert.assertEquals(1, methodCount);
    }

    @Test
    @OperateOnDeployment("client")
    public void testStatefulLocalFromRemote(@ArquillianResource InitialContext ctx) throws Exception {
        InitialContext jndiContext = new InitialContext();
        StatelessRemote bean = (StatelessRemote) jndiContext.lookup("java:module/" + StatelessBean.class.getSimpleName() + "!"
                + StatelessRemote.class.getName());
        Assert.assertNotNull(bean);

        try {
            bean.localCall();
            Assert.fail("should not be allowed to call local interface remotely");
        } catch (EJBException e) {
            // it's OK
        } catch (Exception ee) {
            if(!(ee.getCause() instanceof EJBException)) {
                Assert.fail("should be " + EJBException.class.getName());
            }
        }
    }

    @Test
    @OperateOnDeployment("client")
    public void testStatefulLocalHomeFromRemoteHome(@ArquillianResource InitialContext ctx) throws Exception {
        InitialContext jndiContext = new InitialContext();
        StatelessRemoteHome home = (StatelessRemoteHome) jndiContext.lookup("java:module/"
                + StatelessBean.class.getSimpleName() + "!" + StatelessRemoteHome.class.getName());
        StatelessRemote bean = home.create();
        Assert.assertNotNull(bean);

        try {
            bean.localHomeCall();
            Assert.fail("should not be allowed to call local interface remotely");
        } catch (EJBException e) {
            // it's OK
        } catch (Exception ee) {
            if(!(ee.getCause() instanceof EJBException)) {
                Assert.fail("should be " + EJBException.class.getName());
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15232.java