error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5545.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5545.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5545.java
text:
```scala
c@@li.sendLine("/subsystem=web/connector=test-connector:remove{allow-resource-service-restart=true}");

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
package org.jboss.as.test.integration.management.cli;

import org.jboss.as.test.integration.management.base.AbstractCliTestBase;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.test.integration.common.HttpRequest;
import org.jboss.as.test.integration.management.util.CLIOpResult;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Dominik Pospisil <dpospisi@redhat.com>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class GlobalOpsTestCase extends AbstractCliTestBase {

    @ArquillianResource URL url;

    @BeforeClass
    public static void before() throws Exception {
        AbstractCliTestBase.initCLI();
    }

    @AfterClass
    public static void after() throws Exception {
        AbstractCliTestBase.closeCLI();
    }

    @Deployment
    public static Archive<?> getDeployment() {
        JavaArchive ja = ShrinkWrap.create(JavaArchive.class, "dummy.jar");
        ja.addClass(GlobalOpsTestCase.class);
        return ja;
    }

    @Test
    public void testReadResource() throws Exception {
        testReadResource(false);
    }

    @Test
    public void testReadResourceRecursive() throws Exception {
        testReadResource(true);
    }

    private void testReadResource(boolean recursive) throws Exception {
        cli.sendLine("/subsystem=web:read-resource(recursive="+ String.valueOf(recursive) +")");
        CLIOpResult result = cli.readAllAsOpResult();

        assertTrue(result.isIsOutcomeSuccess());
        assertTrue(result.getResult() instanceof Map);
        Map map = (Map) result.getResult();

        assertTrue(map.get("virtual-server") instanceof Map);

        Map vServer = (Map) map.get("virtual-server");
        assertTrue(vServer.containsKey("default-host"));

        if (recursive) {
            assertTrue(vServer.get("default-host") instanceof Map);
            Map host = (Map) vServer.get("default-host");
            assertTrue(host.containsKey("alias"));
        } else {
            assertTrue(vServer.get("default-host").equals("undefined"));
        }
    }

    @Test
    public void testReadAttribute() throws Exception {
        cli.sendLine("/subsystem=web:read-attribute(name=native)");
        CLIOpResult result = cli.readAllAsOpResult();

        assertTrue(result.isIsOutcomeSuccess());
        assertTrue(result.getResult().equals("true") || result.getResult().equals("false"));

    }

    @Test
    public void testWriteAttribute() {
    }

    @Test
    public void testReadResourceDescription() throws Exception {
        cli.sendLine("/subsystem=web:read-resource-description");
        CLIOpResult result = cli.readAllAsOpResult();

        assertTrue(result.isIsOutcomeSuccess());
        assertTrue(result.getResult() instanceof Map);
        Map map = (Map) result.getResult();

        assertTrue(map.containsKey("description"));
        assertTrue(map.containsKey("attributes"));
    }

    @Test
    public void testReadOperationNames() throws Exception {
        cli.sendLine("/subsystem=web:read-operation-names");
        CLIOpResult result = cli.readAllAsOpResult();

        assertTrue(result.isIsOutcomeSuccess());
        assertTrue(result.getResult() instanceof List);
        List names = (List) result.getResult();

        assertTrue(names.contains("read-attribute"));
        assertTrue(names.contains("read-children-names"));
        assertTrue(names.contains("read-children-resources"));
        assertTrue(names.contains("read-children-types"));
        assertTrue(names.contains("read-operation-description"));
        assertTrue(names.contains("read-operation-names"));
        assertTrue(names.contains("read-resource"));
        assertTrue(names.contains("read-resource-description"));
        assertTrue(names.contains("write-attribute"));

    }

    @Test
    public void testReadOperationDescription() throws Exception {
        cli.sendLine("/subsystem=web:read-operation-description(name=add)");
        CLIOpResult result = cli.readAllAsOpResult();

        assertTrue(result.isIsOutcomeSuccess());
        assertTrue(result.getResult() instanceof Map);
        Map map = (Map) result.getResult();

        assertTrue(map.containsKey("operation-name"));
        assertTrue(map.containsKey("description"));
        assertTrue(map.containsKey("request-properties"));
    }

    @Test
    public void testReadChildrenTypes() throws Exception {
        cli.sendLine("/subsystem=web:read-children-types");
        CLIOpResult result = cli.readAllAsOpResult();

        assertTrue(result.isIsOutcomeSuccess());
        assertTrue(result.getResult() instanceof List);
        List types = (List) result.getResult();

        assertTrue(types.contains("virtual-server"));
        assertTrue(types.contains("connector"));
    }

    @Test
    public void testReadChildrenNames() throws Exception {
        cli.sendLine("/subsystem=web:read-children-names(child-type=connector)");
        CLIOpResult result = cli.readAllAsOpResult();

        assertTrue(result.isIsOutcomeSuccess());
        assertTrue(result.getResult() instanceof List);
        List names = (List) result.getResult();

        assertTrue(names.contains("http"));
    }

    @Test
    public void testReadChildrenResources() throws Exception {
        cli.sendLine("/subsystem=web:read-children-resources(child-type=connector)");
        CLIOpResult result = cli.readAllAsOpResult();

        assertTrue(result.isIsOutcomeSuccess());
        assertTrue(result.getResult() instanceof Map);
        Map res = (Map) result.getResult();
        assertTrue(res.get("http") instanceof Map);
        Map http = (Map) res.get("http");
        assertTrue(http.containsKey("enabled"));

    }

    @Test
    public void testAddRemoveOperation() throws Exception {

        // add new connector
        cli.sendLine("/socket-binding-group=standard-sockets/socket-binding=test:add(port=8181)");
        CLIOpResult result = cli.readAllAsOpResult();
        assertTrue(result.isIsOutcomeSuccess());

        cli.sendLine("/subsystem=web/connector=test-connector:add(socket-binding=test, scheme=http, protocol=\"HTTP/1.1\", enabled=true)");
        result = cli.readAllAsOpResult();
        assertTrue(result.isIsOutcomeSuccess());

        // check that the connector is live
        String cURL = "http://" + url.getHost() + ":8181";

        String response = HttpRequest.get(cURL, 10, TimeUnit.SECONDS);
        assertTrue("Invalid response: " + response, response.indexOf("JBoss") >=0);


        // remove connector
        cli.sendLine("/subsystem=web/connector=test-connector:remove");
        result = cli.readAllAsOpResult();
        assertTrue(result.isIsOutcomeSuccess());

        cli.sendLine("/socket-binding-group=standard-sockets/socket-binding=test:remove");
        result = cli.readAllAsOpResult();
        assertTrue(result.isIsOutcomeSuccess());

        // check that the connector is no longer live
        Thread.sleep(5000);
        boolean failed = false;
        try {
            response = HttpRequest.get(cURL, 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            failed = true;
        }
        assertTrue("Connector still live: " + response, failed);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5545.java