error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10215.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10215.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10215.java
text:
```scala
S@@tring cluster = "ee";

package org.jboss.as.test.clustering.cluster.dispatcher;

import static org.junit.Assert.*;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.test.clustering.EJBClientContextSelector;
import org.jboss.as.test.clustering.cluster.ClusterAbstractTestCase;
import org.jboss.as.test.clustering.cluster.dispatcher.bean.ClusterTopology;
import org.jboss.as.test.clustering.cluster.dispatcher.bean.ClusterTopologyRetriever;
import org.jboss.as.test.clustering.cluster.dispatcher.bean.ClusterTopologyRetrieverBean;
import org.jboss.as.test.clustering.ejb.EJBDirectory;
import org.jboss.as.test.clustering.ejb.RemoteEJBDirectory;
import org.jboss.as.test.shared.TimeoutUtil;
import org.jboss.ejb.client.ContextSelector;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@RunAsClient
public class CommandDispatcherTestCase extends ClusterAbstractTestCase {
    private static final long VIEW_CHANGE_WAIT = TimeoutUtil.adjust(2000);
    private static final Logger log = Logger.getLogger(CommandDispatcherTestCase.class);
    private static final String MODULE_NAME = "command-dispatcher";
    private static final String CLIENT_PROPERTIES = "cluster/ejb3/stateless/jboss-ejb-client.properties";

    @Deployment(name = DEPLOYMENT_1, managed = false, testable = false)
    @TargetsContainer(CONTAINER_1)
    public static Archive<?> createDeploymentForContainer1() {
        return createDeployment();
    }

    @Deployment(name = DEPLOYMENT_2, managed = false, testable = false)
    @TargetsContainer(CONTAINER_2)
    public static Archive<?> createDeploymentForContainer2() {
        return createDeployment();
    }

    private static Archive<?> createDeployment() {
        final JavaArchive ejbJar = ShrinkWrap.create(JavaArchive.class, MODULE_NAME + ".jar");
        ejbJar.addPackage(ClusterTopologyRetriever.class.getPackage());
        log.info(ejbJar.toString(true));
        return ejbJar;
    }

    @Test
    public void test() throws Exception {
        String cluster = "server";
        String nodeNameFormat = "%s/%s";
        String nodeName1 = String.format(nodeNameFormat, NODE_1, cluster);
        String nodeName2 = String.format(nodeNameFormat, NODE_2, cluster);

        ContextSelector<EJBClientContext> selector = EJBClientContextSelector.setup(CLIENT_PROPERTIES);

        try (EJBDirectory directory = new RemoteEJBDirectory(MODULE_NAME)) {
            ClusterTopologyRetriever bean = directory.lookupStateless(ClusterTopologyRetrieverBean.class, ClusterTopologyRetriever.class);
            ClusterTopology topology = bean.getClusterTopology();
            assertEquals(2, topology.getNodes().size());
            assertTrue(topology.getNodes().toString(), topology.getNodes().contains(nodeName1));
            assertTrue(topology.getNodes().toString(), topology.getNodes().contains(nodeName2));
            assertFalse(topology.getRemoteNodes().toString() + " should not contain " + topology.getLocalNode(), topology.getRemoteNodes().contains(topology.getLocalNode()));

            undeploy(DEPLOYMENT_2);

            topology = bean.getClusterTopology();
            assertEquals(1, topology.getNodes().size());
            assertTrue(topology.getNodes().contains(nodeName1));
            assertEquals(nodeName1, topology.getLocalNode());
            assertTrue(topology.getRemoteNodes().toString(), topology.getRemoteNodes().isEmpty());

            deploy(DEPLOYMENT_2);

            Thread.sleep(VIEW_CHANGE_WAIT);

            topology = bean.getClusterTopology();
            assertEquals(2, topology.getNodes().size());
            assertTrue(topology.getNodes().contains(nodeName1));
            assertTrue(topology.getNodes().contains(nodeName2));
            assertFalse(topology.getRemoteNodes().toString() + " should not contain " + topology.getLocalNode(), topology.getRemoteNodes().contains(topology.getLocalNode()));

            stop(CONTAINER_1);

            topology = bean.getClusterTopology();
            assertEquals(1, topology.getNodes().size());
            assertTrue(topology.getNodes().contains(nodeName2));
            assertEquals(nodeName2, topology.getLocalNode());
            assertTrue(topology.getRemoteNodes().toString(), topology.getRemoteNodes().isEmpty());

            start(CONTAINER_1);

            Thread.sleep(VIEW_CHANGE_WAIT);

            topology = bean.getClusterTopology();
            assertEquals(topology.getNodes().toString(), 2, topology.getNodes().size());
            assertTrue(topology.getNodes().toString() + " should contain " + nodeName1, topology.getNodes().contains(nodeName1));
            assertTrue(topology.getNodes().toString() + " should contain " + nodeName2, topology.getNodes().contains(nodeName2));
            assertFalse(topology.getRemoteNodes().toString() + " should not contain " + topology.getLocalNode(), topology.getRemoteNodes().contains(topology.getLocalNode()));
        } finally {
            // reset the selector
            if (selector != null) {
                EJBClientContext.setSelector(selector);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10215.java