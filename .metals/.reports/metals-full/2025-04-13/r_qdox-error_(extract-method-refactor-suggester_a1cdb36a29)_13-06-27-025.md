error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7047.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7047.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7047.java
text:
```scala
t@@x.rollback();

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

import java.util.Collections;
import java.util.List;
import javax.persistence.EntityTransaction;

import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAQuery;

/**
 * A base test case that can be used to easily test scenarios where there
 * is only a single EntityManager at any given time.
 *
 * @author Marc Prud'hommeaux
 */
public abstract class SingleEMTestCase 
    extends SingleEMFTestCase {

    protected OpenJPAEntityManager em;

    @Override
    public void setUp(Object... props) {
        super.setUp(props);
        em = emf.createEntityManager(); 
    }

    @Override
    public void tearDown() {
        rollback();
        close();
        super.tearDown();
    }

    /** 
     * Start a new transaction if there isn't currently one active. 
     * @return  true if a transaction was started, false if one already existed
     */
    protected boolean begin() {
        EntityTransaction tx = em.getTransaction();
        if (tx.isActive())
            return false;

        tx.begin();
        return true;
    }

    /** 
     * Commit the current transaction, if it is active. 
     * @return true if the transaction was committed
     */
    protected boolean commit() {
        EntityTransaction tx = em.getTransaction();
        if (!tx.isActive())
            return false;

        tx.commit();
        return true;
    }

    /** 
     * Rollback the current transaction, if it is active. 
     * @return true if the transaction was rolled back
     */
    protected boolean rollback() {
        EntityTransaction tx = em.getTransaction();
        if (!tx.isActive())
            return false;

        tx.commit();
        return true;
    }

    /** 
     * Closes the current EntityManager if it is open. 
     * @return false if the EntityManager was already closed.
     */
    protected boolean close() {
        if (em == null)
            return false;

        rollback();

        if (!em.isOpen())
            return false;

        em.close();
        return !em.isOpen();
    }

    /** 
     * Delete all of the instances.
     *
     * If no transaction is running, then one will be started and committed.
     * Otherwise, the operation will take place in the current transaction.
     */
    protected void remove(Object... obs) {
        boolean tx = begin();
        for (Object ob : obs)
            em.remove(ob);
        if (tx) 
            commit();
    }

    /** 
     * Persist all of the instances.
     *
     * If no transaction is running, then one will be started and committed.
     * Otherwise, the operation will take place in the current transaction.
     */
    protected void persist(Object... obs) {
        boolean tx = begin();
        for (Object ob : obs)
            em.persist(ob);
        if (tx) 
            commit();
    }

    /** 
     * Creates a query in the current EntityManager with the specified string. 
     */
    protected OpenJPAQuery query(String str) {
        return em.createQuery(str);
    }

    /** 
     * Create a query against the specified class, which will be aliased
     * as "x". For example, query(Person.class, "where x.age = 21") will
     * create the query "select x from Person x where x.age = 21".
     *  
     * @param  c  the class to query against
     * @param  str  the query suffix
     * @param  params  the parameters, if any
     * @return the Query object
     */
    protected OpenJPAQuery query(Class c, String str, Object... params) {
        String query = "select x from " + entityName(emf, c) + " x "
            + (str == null ? "" : str);
        OpenJPAQuery q = em.createQuery(query);
        for (int i = 0; params != null && i < params.length; i++)
            q.setParameter(i + 1, params[i]);
        return q;
    }

    /** 
     * Returns a list of all instances of the specific class in the database. 
     *
     * @param c the class to find
     * @param q the query string suffix to use
     * @param params the positional parameter list value
     *
     * @see #query(java.lang.Class,java.lang.String)
     */
    protected <E> List<E> find(Class<E> c, String q, Object... params) {
        return Collections.checkedList(query(c, q, params).getResultList(), c);
    }

    /** 
     * Returns a list of all instances of the specific class in the database. 
     */
    protected <E> List<E> find(Class<E> c) {
        return find(c, null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7047.java