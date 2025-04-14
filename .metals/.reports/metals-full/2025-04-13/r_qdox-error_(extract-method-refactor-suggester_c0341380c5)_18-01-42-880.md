error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16770.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16770.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16770.java
text:
```scala
protected T@@estResult testResult;

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
package org.apache.openjpa.persistence.test;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import junit.framework.TestCase;
import junit.framework.TestResult;
import org.apache.openjpa.kernel.AbstractBrokerFactory;
import org.apache.openjpa.kernel.Broker;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;
import org.apache.openjpa.persistence.JPAFacadeHelper;

/**
 * Base test class providing persistence utilities.
 */
public abstract class PersistenceTestCase
    extends TestCase {

    /**
     * Marker object you an pass to {@link #setUp} to indicate that the
     * database tables should be cleared.
     */
    protected static final Object CLEAR_TABLES = new Object();

    /**
     * The {@link TestResult} instance for the current test run.
     */
    private TestResult testResult;

    /**
     * Create an entity manager factory. Put {@link #CLEAR_TABLES} in
     * this list to tell the test framework to delete all table contents
     * before running the tests.
     *
     * @param props list of persistent types used in testing and/or
     * configuration values in the form key,value,key,value...
     */
    protected OpenJPAEntityManagerFactorySPI createEMF(Object... props) {
        return createNamedEMF(getPersistenceUnitName(), props);
    }

    /**
     * The name of the persistence unit that this test class should use
     * by default. This defaults to "test".
     */
    protected String getPersistenceUnitName() {
        return "test";
    }

    /**
     * Create an entity manager factory for persistence unit <code>pu</code>.
     * Put {@link #CLEAR_TABLES} in
     * this list to tell the test framework to delete all table contents
     * before running the tests.
     *
     * @param props list of persistent types used in testing and/or
     * configuration values in the form key,value,key,value...
     */
    protected OpenJPAEntityManagerFactorySPI createNamedEMF(String pu,
        Object... props) {
        Map map = new HashMap(System.getProperties());
        List<Class> types = new ArrayList<Class>();
        boolean prop = false;
        for (int i = 0; i < props.length; i++) {
            if (prop) {
                map.put(props[i - 1], props[i]);
                prop = false;
            } else if (props[i] == CLEAR_TABLES) {
                map.put("openjpa.jdbc.SynchronizeMappings",
                    "buildSchema(ForeignKeys=true," 
                    + "SchemaAction='add,deleteTableContents')");
            } else if (props[i] instanceof Class)
                types.add((Class) props[i]);
            else if (props[i] != null)
                prop = true;
        }

        if (!types.isEmpty()) {
            StringBuffer buf = new StringBuffer();
            for (Class c : types) {
                if (buf.length() > 0)
                    buf.append(";");
                buf.append(c.getName());
            }
            map.put("openjpa.MetaDataFactory",
                "jpa(Types=" + buf.toString() + ")");
        }

        return (OpenJPAEntityManagerFactorySPI) Persistence.
            createEntityManagerFactory(pu, map);
    }

    @Override
    public void run(TestResult testResult) {
        this.testResult = testResult;
        super.run(testResult);
    }

    @Override
    public void tearDown() throws Exception {
        try {
            super.tearDown();
        } catch (Exception e) {
            // if a test failed, swallow any exceptions that happen
            // during tear-down, as these just mask the original problem.
            if (testResult.wasSuccessful())
                throw e;
        }
    }

    /**
     * Safely close the given factory.
     */
    protected boolean closeEMF(EntityManagerFactory emf) {
        if (emf == null)
            return false;
        if (!emf.isOpen())
            return false;

        for (Iterator iter = ((AbstractBrokerFactory) JPAFacadeHelper
            .toBrokerFactory(emf)).getOpenBrokers().iterator();
            iter.hasNext(); ) {
            Broker b = (Broker) iter.next();
            if (b != null && !b.isClosed()) {
                EntityManager em = JPAFacadeHelper.toEntityManager(b);
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
                em.close();
            }
        }

        emf.close();
        return !emf.isOpen();
    }

    /**
     * Delete all instances of the given types using bulk delete queries.
     */
    protected void clear(EntityManagerFactory emf, Class... types) {
        if (emf == null || types.length == 0)
            return;

        List<ClassMetaData> metas = new ArrayList<ClassMetaData>(types.length);
        for (Class c : types) {
            ClassMetaData meta = JPAFacadeHelper.getMetaData(emf, c);
            if (meta != null)
                metas.add(meta);
        }
        clear(emf, metas.toArray(new ClassMetaData[metas.size()]));
    }

    /**
     * Delete all instances of the persistent types registered with the given
     * factory using bulk delete queries.
     */
    protected void clear(EntityManagerFactory emf) {
        if (emf == null)
            return;
        clear(emf, ((OpenJPAEntityManagerFactorySPI) emf).getConfiguration().
            getMetaDataRepositoryInstance().getMetaDatas());
    }

    /**
     * Delete all instances of the given types using bulk delete queries.
     */
    private void clear(EntityManagerFactory emf, ClassMetaData... types) {
        if (emf == null || types.length == 0)
            return;

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        for (ClassMetaData meta : types) {
            if (!meta.isMapped() || meta.isEmbeddedOnly() 
 Modifier.isAbstract(meta.getDescribedType().getModifiers()))
                continue;
            em.createQuery("DELETE FROM " + meta.getTypeAlias() + " o").
                executeUpdate();
        }
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Return the entity name for the given type.   
     */
    protected String entityName(EntityManagerFactory emf, Class c) {
        ClassMetaData meta = JPAFacadeHelper.getMetaData(emf, c);
        return (meta == null) ? null : meta.getTypeAlias();
    }

    public static void assertNotEquals(Object o1, Object o2) {
        if (o1 == o2)
            fail("expected args to be different; were the same instance.");
        else if (o1 == null || o2 == null)
            return;
        else if (o1.equals(o2))
            fail("expected args to be different; compared equal.");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16770.java