error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2023.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2023.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2023.java
text:
```scala
m@@onitoredMap = new JmxMonitoredMap<String, SolrInfoMBean>(null, "", config);

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.solr.core;

import org.apache.lucene.util.LuceneTestCase;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrConfig.JmxConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.Query;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.net.ServerSocket;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Set;

/**
 * Test for JmxMonitoredMap
 *
 * @version $Id$
 * @since solr 1.3
 */
public class TestJmxMonitoredMap extends LuceneTestCase {

  private int port = 0;

  private JMXConnector connector;

  private MBeanServerConnection mbeanServer;

  private JmxMonitoredMap<String, SolrInfoMBean> monitoredMap;

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    int retries = 5;
    for (int i = 0; i < retries; i++) {
      try {
        ServerSocket server = new ServerSocket(0);
        try {
          port = server.getLocalPort();
        } finally {
          server.close();
        }
        // System.out.println("Using port: " + port);
        try {
          LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
          throw e;
        }
        String url = "service:jmx:rmi:///jndi/rmi://:" + port + "/solrjmx";
        JmxConfiguration config = new JmxConfiguration(true, null, url);
        monitoredMap = new JmxMonitoredMap<String, SolrInfoMBean>(null, config);
        JMXServiceURL u = new JMXServiceURL(url);
        connector = JMXConnectorFactory.connect(u);
        mbeanServer = connector.getMBeanServerConnection();
        break;
      } catch (Exception e) {
        if(retries == (i + 1)) {
          throw e;
        }
      }
    }
  }

  @Override
  @After
  public void tearDown() throws Exception {
    try {
      connector.close();
    } catch (Exception e) {
    }
    super.tearDown();
  }

  @Test
  public void testPutRemoveClear() throws Exception {
    MockInfoMBean mock = new MockInfoMBean();
    monitoredMap.put("mock", mock);

    Set<ObjectInstance> objects = mbeanServer.queryMBeans(null, Query.match(
            Query.attr("name"), Query.value("mock")));
    assertFalse("No MBean for mock object found in MBeanServer", objects
            .isEmpty());

    monitoredMap.remove("mock");
    objects = mbeanServer.queryMBeans(null, Query.match(Query.attr("name"),
            Query.value("mock")));
    assertTrue("MBean for mock object found in MBeanServer even after removal",
            objects.isEmpty());

    monitoredMap.put("mock", mock);
    monitoredMap.put("mock2", mock);
    objects = mbeanServer.queryMBeans(null, Query.match(Query.attr("name"),
            Query.value("mock")));
    assertFalse("No MBean for mock object found in MBeanServer", objects
            .isEmpty());

    monitoredMap.clear();
    objects = mbeanServer.queryMBeans(null, Query.match(Query.attr("name"),
            Query.value("mock")));
    assertTrue(
            "MBean for mock object found in MBeanServer even after clear has been called",
            objects.isEmpty());
  }

  private class MockInfoMBean implements SolrInfoMBean {
    public String getName() {
      return "mock";
    }

    public Category getCategory() {
      return Category.OTHER;
    }

    public String getDescription() {
      return "mock";
    }

    public URL[] getDocs() {
      // TODO Auto-generated method stub
      return null;
    }

    public String getVersion() {
      return "mock";
    }

    public String getSource() {
      return "mock";
    }

    @SuppressWarnings("unchecked")
    public NamedList getStatistics() {
      return null;
    }

    public String getSourceId() {
      return "mock";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2023.java