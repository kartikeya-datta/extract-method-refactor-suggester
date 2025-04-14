error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5621.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5621.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5621.java
text:
```scala
(@@String) emProperties.get("openjpa.ConnectionDriverName");

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
package org.apache.openjpa.persistence.simple;

import java.util.Map;
import java.util.Set;

import org.apache.openjpa.kernel.AutoClear;
import org.apache.openjpa.lib.conf.Value;
import org.apache.openjpa.persistence.AutoClearType;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.test.SingleEMFTestCase;

/**
 * This test case tests the getProperties() and getSupportedProperties() methods
 * for the EntityManager and EntityManagerFactory.
 * 
 * @author Dianne Richards
 * 
 */
public class TestPropertiesMethods extends SingleEMFTestCase {
    OpenJPAEntityManager em;

    public void setUp() throws Exception {
        setUp("openjpa.DataCacheTimeout", "3",
            "openjpa.ConnectionURL",
            "jdbc:derby:target/database/jpa-test-database;create=true");
        assertNotNull(emf);
        em = emf.createEntityManager();
        assertNotNull(em);
    }
    
    /**
     * Test the EntityManager getProperties() method.
     */
    public void testEMGetProperties() {
        Map<String, Object> emProperties = em.getProperties();

        // First, check a default property
        String autoClear = (String) emProperties.get("openjpa.AutoClear");
        assertEquals(String.valueOf(AutoClear.CLEAR_DATASTORE), autoClear);
        
        // Next, check that the correct property key is returned for
        // some properties that can have 2 keys. The success of this test
        // case is dependent on the connection system values that are set
        // in the pom.xml file for the test harness. It assumes that the
        // system value keys are javax.persistence.jdbc.driver and 
        // openjpa.ConnectionProperties. If either one of these are changed,
        // this test case may fail.
        String javaxConnectionDriver =
            (String) emProperties.get("javax.persistence.jdbc.driver");
        assertNotNull(javaxConnectionDriver);
        String openjpaConnectionURL =
            (String) emProperties.get("openjpa.ConnectionURL");
        assertNotNull(openjpaConnectionURL);
        
        // Next, check that the javax.persistent property is returned instead
        // of the corresponding openjpa one when no value has been set.
        boolean javaxUserNameExists =
            emProperties.containsKey("javax.persistence.jdbc.user");
        assertTrue(javaxUserNameExists);
        boolean openjpaUserNameExists =
            emProperties.containsKey("openjpaConnectionUserName");
        assertFalse(openjpaUserNameExists);
        
        // Next, change a property and check for the changed value
        em.setAutoClear(AutoClearType.ALL);
        emProperties = em.getProperties();
        autoClear = (String) emProperties.get("openjpa.AutoClear");
        assertEquals(String.valueOf(AutoClear.CLEAR_ALL), autoClear);
        
        // Make sure the password property is not returned.
        boolean javaxPasswordExists =
            emProperties.containsKey("javax.persistence.jdbc.password");
        assertFalse(javaxPasswordExists);
        boolean openjpaPasswordExists =
            emProperties.containsKey("openjpa.ConnectionPassword");
        assertFalse(openjpaPasswordExists);
        
        // Add a dummy javax.persistence... equivalent key to one of the
        // values that can be changed to force the code down a specific path.
        Value autoClearValue = emf.getConfiguration().getValue("AutoClear");
        assertNotNull(autoClearValue);
        autoClearValue.addEquivalentKey("javax.persistence.AutoClear");
        emProperties = em.getProperties();
        assertFalse(emProperties.containsKey("openjpa.AutoClear"));
        assertTrue(emProperties.containsKey("javax.persistence.AutoClear"));
    }

    /**
     * Test the EntityManagerFactory getProperties() method.
     */
    public void testEMFGetProperties() {
        Map<String, Object> emfProperties = emf.getProperties();

        // First, check a default property
        String dataCacheManager =
            (String) emfProperties.get("openjpa.DataCacheManager");
        assertEquals("default", dataCacheManager);

        // Next, check a property that was set during emf creation
        String dataCacheTimeout =
            (String) emfProperties.get("openjpa.DataCacheTimeout");
        assertEquals(3, Integer.valueOf(dataCacheTimeout).intValue());

        // Next get the Platform value set by the JDBCBrokerFactory
        // or possibly a subclass
        String platform = (String) emfProperties.get("Platform");
        assertNotNull(platform);

        // Next get one of the values set by the AbstractBrokerFactory
        // or possibly a subclass
        String vendorName = (String) emfProperties.get("VendorName");
        assertNotNull(vendorName);
    }

    /**
     * Test the EntityManagerFactory getSupportedProperties() method.
     */
    public void testEMFGetSupportedProperties() {
        Set<String> emfSupportedProperties = emf.getSupportedProperties();
        assertNotNull(emfSupportedProperties);
        assertTrue(emfSupportedProperties.contains("openjpa.IgnoreChanges"));
    }

    /**
     * Test the EntityManager getSupportedProperties() method.
     */
    public void testEMGetSupportedProperties() {
        Set<String> emSupportedProperties = em.getSupportedProperties();
        assertNotNull(emSupportedProperties);
        assertTrue(emSupportedProperties.contains("openjpa.AutoDetach"));
        
        // Make sure the all possible keys are returned
        assertTrue(emSupportedProperties.contains(
            "javax.persistence.lock.timeout"));
        assertTrue(emSupportedProperties.contains("openjpa.LockTimeout"));
        
        // Make sure the spec property for query timeout, that only has one
        // key, is returned.
        assertTrue(emSupportedProperties.contains(
            "javax.persistence.query.timeout"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5621.java