error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2948.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2948.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2948.java
text:
```scala
e@@xtends AbstractCachedEMFTestCase  {

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

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;

/**
 * Base class for OpenJPA-specific Test Case.
 * Provides utilities for configuration setup and persistent entity 
 * registration during setUp() method.
 * Derived classes can access protected EntityManagerFactory to create 
 * EntityManager. The protected EntityManagerFactory is declared to be
 * OpenJPA-extended SPI interface <code>OpenJPAEntityManagerFactorySPI</code>
 * so that derived classes can access internal mapping/metadata/configuration
 * and other structures.  
 *   
 */
public abstract class SingleEMFTestCase
    extends PersistenceTestCase {

    protected OpenJPAEntityManagerFactorySPI emf;

    /**
     * Call {@link #setUp(Object... props)} with no arguments so that the emf
     * set-up happens even if <code>setUp()</code> is not called from the
     * subclass.
     */
    public void setUp() throws Exception {
        setUp(new Object[0]);
    }

    /**
     * Initialize entity manager factory. Put {@link #CLEAR_TABLES} in
     * this list to tell the test framework to delete all table contents
     * before running the tests.
     *
     * @param props list of persistent types used in testing and/or 
     * configuration values in the form key,value,key,value...
     */
    protected void setUp(Object... props) {
        emf = createEMF(props);
    }

    /**
     * Closes the entity manager factory.
     */
    public void tearDown() throws Exception {
        super.tearDown();

        if (emf == null)
            return;

        try {
            clear(emf);
        } catch (Exception e) {
            // if a test failed, swallow any exceptions that happen
            // during tear-down, as these just mask the original problem.
            if (testResult.wasSuccessful())
                throw e;
        } finally {
            closeEMF(emf);
        }
    }
    
    /**
     * Get the class mapping for a given entity
     * 
     * @param name The Entity's name.
     * 
     * @return If the entity is a known type the ClassMapping for the Entity
     *         will be returned. Otherwise null
     */
    protected ClassMapping getMapping(String name) {
        return (ClassMapping) emf.getConfiguration()
                .getMetaDataRepositoryInstance().getMetaData(name,
                        getClass().getClassLoader(), true);
    }
    
    /**
     * Get the class mapping for a given entity
     * 
     * @param entityClass an entity class.
     * 
     * @return If the entity is a known type the ClassMapping for the Entity
     *         will be returned. Otherwise null
     */
    protected ClassMapping getMapping(Class<?> entityClass) {
        return (ClassMapping) emf.getConfiguration()
                .getMetaDataRepositoryInstance().getMetaData(entityClass,
                        getClass().getClassLoader(), true);
    }
    
    /**
     * Get number of instances by an aggregate query with the given alias.
     */
    public int count(String alias) {
        return ((Number)emf.createEntityManager().createQuery(
                "SELECT COUNT(p) FROM " + alias + " p")
                .getSingleResult()).intValue();
    }
    
    /**
     * Count number of instances of the given class assuming that the alias
     * for the class is its simple name.
     */
    public int count(Class<?> c) {
    	return count(getAlias(c));
    }
    
    /**
     * Get all the instances of given type.
     * The returned instances are obtained without a persistence context. 
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getAll(Class<T> t) {
        return (List<T>)emf.createEntityManager().createQuery(
                "SELECT p FROM " + getAlias(t) + " p").getResultList();
    }
    
    /**
     * Get all the instances of given type.
     * The returned instances are obtained within the given persistence context.
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getAll(EntityManager em, Class<T> t) {
    	return (List<T>)em.createQuery("SELECT p FROM " + getAlias(t) + " p")
				   .getResultList();
    }
    
    public String getAlias(Class<?> t) {
        return emf.getConfiguration().getMetaDataRepositoryInstance()
            .getMetaData(t, null, true).getTypeAlias();
    }

    protected ClassMapping [] getMappings() {
        return (ClassMapping [] ) emf.getConfiguration().getMetaDataRepositoryInstance().getMetaDatas();   
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2948.java