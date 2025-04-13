error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1140.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1140.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1140.java
text:
```scala
a@@ssertNotNull(pmf.getProperties().get("Platform"));

/*
 * TestOpenJPAEntityManagerFactoryImpl.java
 *
 * Created on October 13, 2006, 10:54 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.persistence.kernel;

import java.util.Map;

import org.apache.openjpa.persistence.kernel.common.apps.RuntimeTest1;
import org.apache.openjpa.persistence.kernel.common.apps.RuntimeTest4;
import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.conf.OpenJPAConfigurationImpl;
import org.apache.openjpa.event.LifecycleEvent;
import org.apache.openjpa.event.LoadListener;
import org.apache.openjpa.kernel.AbstractBrokerFactory;
import org.apache.openjpa.kernel.BrokerImpl;
import org.apache.openjpa.kernel.StoreManager;
import org.apache.openjpa.lib.conf.ConfigurationProvider;
import org.apache.openjpa.meta.MetaDataRepository;
import org.apache.openjpa.persistence.JPAFacadeHelper;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactory;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;
import org.apache.openjpa.persistence.OpenJPAPersistence;

public class TestPersistenceManagerFactoryImpl extends BaseKernelTest {

    private OpenJPAConfiguration _conf = null;

    /**
     * Creates a new instance of TestOpenJPAEntityManagerFactoryImpl
     */
    public TestPersistenceManagerFactoryImpl() {
    }

    public TestPersistenceManagerFactoryImpl(String test) {
        super(test);
    }

    public void setUp() {
        _conf = new OpenJPAConfigurationImpl();
        _conf.setConnection2UserName("user");
        _conf.setConnection2Password("pass");
        _conf.setConnection2URL("url");
    }

    /**
     * Test that configuration is frozen after retrieving a factory.
     *
     * This test case is for kodo persistencemanagerfactories and not openjpaentitymanagers.
     * therefore its been commented out. 
     * FIX ME: aokeke
     */
/*    public void testConfigurationFreeze() 
    {
        OpenJPAEntityManagerFactory pmf = getOpenJPAEntityManagerFactory(_conf.toProperties(false));
        assertEquals("user", pmf.getConfiguration().getConnection2UserName());
        assertEquals("url", pmf.getConfiguration().getConnection2URL());
        try 
        {
            pmf.getConfiguration().setConnection2URL("url2");
            fail("Allowed configuration change.");
        } 
        catch (Exception e) 
        {
        }
    }*/

    /**
     * Test that persistence manager factories are being pooled.
     *
     * This test case is for kodo persistencemanagerfactories. It doesnt apply to 
     * openjpaentitymanagerfactories therefore it will be commented out.
     * FIX ME: aokeke
     */
    /*public void testFactoryPooling() {
        Properties props = new Properties();
        props.putAll(_conf.toProperties(false));
        OpenJPAEntityManagerFactory pmf1 = getOpenJPAEntityManagerFactory(props);
        
        props = new Properties();
        props.putAll(_conf.toProperties(false));
        OpenJPAEntityManagerFactory pmf2 = getOpenJPAEntityManagerFactory(props);
        
        props = new Properties();
        props.putAll(_conf.toProperties(false));
        OpenJPAEntityManagerFactory pmf3 = getOpenJPAEntityManagerFactory(props);
        
        _conf.setConnectionURL("url2");
        props = new Properties();
        props.putAll(_conf.toProperties(false));
        OpenJPAEntityManagerFactory pmf4 = getOpenJPAEntityManagerFactory
                (_conf.toProperties(false));
        
        props = new Properties();
        props.putAll(_conf.toProperties(false));
        OpenJPAEntityManagerFactory pmf5 = getOpenJPAEntityManagerFactory(_conf.toProperties(false));
        
        assertTrue(JPAFacadeHelper.toBrokerFactory(pmf1) == JPAFacadeHelper.toBrokerFactory(pmf2));
        assertTrue(JPAFacadeHelper.toBrokerFactory(pmf1) == JPAFacadeHelper.toBrokerFactory(pmf3));
        assertTrue(JPAFacadeHelper.toBrokerFactory(pmf1) != JPAFacadeHelper.toBrokerFactory(pmf4));
        assertTrue(JPAFacadeHelper.toBrokerFactory(pmf4) == JPAFacadeHelper.toBrokerFactory(pmf5));
    }*/

    /**
     * Tests that lifecycle listeners are transferred from factory to
     * persistence managers.
     */
    public void testFactoryLifecycleListeners() {
        OpenJPAEntityManagerFactory pmf =
            (OpenJPAEntityManagerFactory) getEmf();
        OpenJPAEntityManagerFactorySPI pmfSPI =
            ((OpenJPAEntityManagerFactorySPI) OpenJPAPersistence.cast(pmf));

        //FIXME jthomas        
        LoadListener listener = new LoadListener() {
            public void afterLoad(LifecycleEvent ev) {
            }

            public void afterRefresh(LifecycleEvent ev) {
            }
        };

        pmfSPI
            .addLifecycleListener(listener, new Class[]{ RuntimeTest4.class });

        try {
            BrokerImpl broker = (BrokerImpl) JPAFacadeHelper.toBroker
                (pmf.createEntityManager());
            MetaDataRepository repos = broker.getConfiguration().
                getMetaDataRepositoryInstance();
            assertTrue("no listeners defined added to Runtimetest4",
                broker.getLifecycleEventManager().hasLoadListeners(
                    new RuntimeTest4("foo"),
                    repos.getMetaData(RuntimeTest4.class, null, true)));
            assertFalse("there should be listeners def for runtimetest1",
                broker.getLifecycleEventManager().hasLoadListeners
                    (new RuntimeTest1(), repos.getMetaData
                        (RuntimeTest1.class, null, true)));
            broker.close();
        } finally {
            pmfSPI.removeLifecycleListener(listener);
        }
    }

    /**
     * Tests that pooling is maintained on deserialization.
     * This test case is for kodo persistencemanagerfactories. It doesnt apply to 
     * openjpaentitymanagerfactories therefore it will be commented out.
     */
    /*
    public void testFactorySerialization()
    throws Exception {
        OpenJPAEntityManagerFactory pmf1 = getOpenJPAEntityManagerFactory(_conf.toProperties(false));
        Object pmf2 = roundtrip(pmf1, true);
        assertEquals(pmf1, pmf2);
        assertTrue(JPAFacadeHelper.toBrokerFactory(pmf1) ==
                JPAFacadeHelper.toBrokerFactory((OpenJPAEntityManagerFactory) pmf2));
    }
    */

    /**
     * Tests that the <code>Platform</code> property is set by the
     * concrete PMF implementation.
     */
    public void testPlatform() {
        OpenJPAEntityManagerFactory pmf =
            (OpenJPAEntityManagerFactory) getEmf();
        assertNotNull(pmf.getProperties().getProperty("Platform"));
    }

    protected OpenJPAEntityManagerFactory getEmf(Map props) {
        props.put("openjpa.BrokerFactory", BrokerFactoryTest.class.getName());
        return (OpenJPAEntityManagerFactory) super.getEmf(props);
    }

    public static class BrokerFactoryTest extends AbstractBrokerFactory {

        // standard brokerfactory getter implemented by subclasses
        public static synchronized BrokerFactoryTest getInstance(
            ConfigurationProvider cp) {
            Object key = toPoolKey(cp.getProperties());
            BrokerFactoryTest factory =
                (BrokerFactoryTest) getPooledFactoryForKey(key);
            if (factory != null)
                return factory;

            factory = newInstance(cp);
            pool(key, factory);
            return factory;
        }

        // standard brokerfactory getter implemented by subclasses
        public static BrokerFactoryTest newInstance(ConfigurationProvider cp) {
            OpenJPAConfigurationImpl conf = new OpenJPAConfigurationImpl();
            cp.setInto(conf);
            return new BrokerFactoryTest(conf);
        }

        protected BrokerFactoryTest(OpenJPAConfiguration conf) {
            super(conf);
        }

        protected StoreManager newStoreManager() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1140.java