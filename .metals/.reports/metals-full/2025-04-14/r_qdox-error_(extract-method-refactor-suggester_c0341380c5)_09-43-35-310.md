error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14998.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14998.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14998.java
text:
```scala
c@@onfiguration.setModuleLoader(Module.getBootModuleLoader());

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.as.test.surefire.servermodule;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OUTCOME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUCCESS;

import java.net.InetAddress;
import java.util.Collections;
import java.util.Properties;

import junit.framework.Assert;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.protocol.StreamUtils;
import org.jboss.as.server.Bootstrap;
import org.jboss.as.server.Main;
import org.jboss.as.server.ServerEnvironment;
import org.jboss.dmr.ModelNode;
import org.jboss.modules.Module;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Here to prove the forked surefire plugin is capable of running
 * modular tests. This plugin will load up this test class in a module that can see
 * org.jboss.as.standalone.
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ServerInModuleStartupTestCase {

    static ServiceContainer container;
    @BeforeClass
    public static void startServer() throws Exception {
        ServerEnvironment serverEnvironment = Main.determineEnvironment(new String[0], new Properties(System.getProperties()), System.getenv());
        Assert.assertNotNull(serverEnvironment);
        final Bootstrap bootstrap = Bootstrap.Factory.newInstance();
        final Bootstrap.Configuration configuration = new Bootstrap.Configuration();
        configuration.setServerEnvironment(serverEnvironment);
        configuration.setModuleLoader(Module.getSystemModuleLoader());
        configuration.setPortOffset(0);

        container = bootstrap.startup(configuration, Collections.<ServiceActivator>emptyList()).get();
        Assert.assertNotNull(container);
    }

    @AfterClass
    public static void testServerStartupAndShutDown() throws Exception {
        container.shutdown();
        container.awaitTermination();
        Assert.assertTrue(container.isShutdownComplete());
    }

    @Test
    public void testXmlConfigDemo() throws Exception {
        ModelControllerClient client = ModelControllerClient.Factory.create(ModelControllerClient.Type.STANDALONE, InetAddress.getByName("localhost"), 9999);
        try {
            ModelNode request = new ModelNode();
            request.get("operation").set("read-config-as-xml");
            request.get("address").setEmptyList();
            ModelNode r = client.execute(request);

            Assert.assertEquals(SUCCESS, r.require(OUTCOME).asString());

            //TODO parse and compare the result to standlone.xml?
        } finally {
            StreamUtils.safeClose(client);
        }
    }

    @Test
    public void testDescriptionDemo() throws Exception {
        ModelControllerClient client = ModelControllerClient.Factory.create(ModelControllerClient.Type.STANDALONE, InetAddress.getByName("localhost"), 9999);
        try {
            ModelNode request = new ModelNode();
            request.get("operation").set("read-resource");
            request.get("address").setEmptyList();
            request.get("recursive").set(true);
            ModelNode r = client.execute(request);

            Assert.assertEquals(SUCCESS, r.require(OUTCOME).asString());
        } finally {
            StreamUtils.safeClose(client);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14998.java