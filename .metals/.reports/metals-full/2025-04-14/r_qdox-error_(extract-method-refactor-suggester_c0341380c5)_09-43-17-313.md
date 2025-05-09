error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1282.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1282.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1282.java
text:
```scala
d@@etachedManager = new SynchronizedEntityManagerWrapper(temp);

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIESOR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.jpa.container.context.transaction.impl;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;

import org.apache.aries.jpa.container.context.impl.NLS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A <code>PersistenceContextType.TRANSACTION</code> {@link EntityManager} instance
 */
public class JTAEntityManager implements EntityManager {
  /** Logger */
  private static final Logger _logger = LoggerFactory.getLogger("org.apache.aries.jpa.container.context");
  
  /** The {@link EntityManagerFactory} that can create new {@link EntityManager} instances */
  private final EntityManagerFactory emf;
  /** The map of properties to pass when creating EntityManagers */
  private final Map<String, Object> props;
  /** A registry for creating new persistence contexts */
  private final JTAPersistenceContextRegistry reg;
  /** The number of EntityManager instances that are open */
  private final AtomicLong instanceCount;
  /** A callback for when we're quiescing */
  private final DestroyCallback callback;
  
  
  /** 
   * The entity manager to use when there is no transaction. Note that there is one of these
   * per injection site.
   */
  private EntityManager detachedManager = null;
  
  public JTAEntityManager(EntityManagerFactory factory,
      Map<String, Object> properties, JTAPersistenceContextRegistry registry, AtomicLong activeCount,
      DestroyCallback onDestroy) {
    emf = factory;
    props = properties;
    reg = registry;
    instanceCount = activeCount;
    callback = onDestroy;
  }

  /**
   * Get the target persistence context
   * @param forceTransaction Whether the returned entity manager needs to be bound to a transaction
   * @throws TransactionRequiredException if forceTransaction is true and no transaction is available
   * @return
   */
  private EntityManager getPersistenceContext(boolean forceTransaction) 
  {
    if (forceTransaction) {
      return reg.getCurrentPersistenceContext(emf, props, instanceCount, callback);
    } else {
      if (reg.isTransactionActive()) {
        return reg.getCurrentPersistenceContext(emf, props, instanceCount, callback);
      } else {
        if(!!!reg.jtaIntegrationAvailable() && _logger.isDebugEnabled())
          _logger.debug("No integration with JTA transactions is available. No transaction context is active.");
        
        if (detachedManager == null) {
          EntityManager temp = emf.createEntityManager(props);
          
          synchronized (this) {
            if (detachedManager == null) {
              detachedManager = temp;
              temp = null;
            }
          }
          
          if (temp != null)
            temp.close();
        }
        return detachedManager;
      }
    }
  }
  
  /**
   * Called reflectively by blueprint
   */
  public void internalClose() {
    EntityManager temp = null;
    
    synchronized (this) {
      temp = detachedManager;
      detachedManager = null;
    }
    
    if (temp != null)
      temp.close();
  }
  
  public void clear()
  {
    getPersistenceContext(false).clear();
  }

  public void close()
  {
    throw new IllegalStateException(NLS.MESSAGES.getMessage("close.called.on.container.manged.em"));
  }

  public boolean contains(Object arg0)
  {
    EntityManager em = getPersistenceContext(false);
    try {
      return em.contains(arg0);
    } finally {
      if(em == detachedManager)
        em.clear();
    }
  }

  public Query createNamedQuery(String arg0)
  {
    EntityManager em = getPersistenceContext(false);
    try {
      return em.createNamedQuery(arg0);
    } finally {
      if(em == detachedManager)
        em.clear();
    }
  }

  public Query createNativeQuery(String arg0)
  {
    EntityManager em = getPersistenceContext(false);
    try {
      return em.createNativeQuery(arg0);
    } finally {
      if(em == detachedManager)
        em.clear();
    }
  }

  @SuppressWarnings("unchecked")
  public Query createNativeQuery(String arg0, Class arg1)
  {
    EntityManager em = getPersistenceContext(false);
    try {
      return em.createNativeQuery(arg0, arg1);
    } finally {
      if(em == detachedManager)
        em.clear();
    }
  }

  public Query createNativeQuery(String arg0, String arg1)
  {
    EntityManager em = getPersistenceContext(false);
    try {
      return em.createNativeQuery(arg0, arg1);
    } finally {
      if(em == detachedManager)
        em.clear();
    }
  }

  public Query createQuery(String arg0)
  {
    EntityManager em = getPersistenceContext(false);
    try {
      return em.createQuery(arg0);
    } finally {
      if(em == detachedManager)
        em.clear();
    }
  }

  public <T> T find(Class<T> arg0, Object arg1)
  {
    EntityManager em = getPersistenceContext(false);
    try {
      return em.find(arg0, arg1);
    } finally {
      if(em == detachedManager)
        em.clear();
    }
  }

  /**
   * @throws TransactionRequiredException
   */
  public void flush()
  {
    getPersistenceContext(true).flush();
  }

  public Object getDelegate()
  {
    return getPersistenceContext(false).getDelegate();
  }

  public FlushModeType getFlushMode()
  {
    return getPersistenceContext(false).getFlushMode();
  }

  public <T> T getReference(Class<T> arg0, Object arg1)
  {
    EntityManager em = getPersistenceContext(false);
    try {
      return em.getReference(arg0, arg1);
    } finally {
      if(em == detachedManager)
        em.clear();
    }
  }

  public EntityTransaction getTransaction()
  {
    throw new IllegalStateException(NLS.MESSAGES.getMessage("getTransaction.called.on.container.managed.em"));
  }

  public boolean isOpen()
  {
    return true;
  }

  public void joinTransaction()
  {
    //This should be a no-op for a JTA entity manager
  }

  /**
   * @throws TransactionRequiredException
   */
  public void lock(Object arg0, LockModeType arg1)
  {
    getPersistenceContext(true).lock(arg0, arg1);
  }

  /**
   * @throws TransactionRequiredException
   */
  public <T> T merge(T arg0)
  {
    return getPersistenceContext(true).merge(arg0);
  }

  /**
   * @throws TransactionRequiredException
   */
  public void persist(Object arg0)
  {
    getPersistenceContext(true).persist(arg0);
  }

  /**
   * @throws TransactionRequiredException
   */
  public void refresh(Object arg0)
  {
    getPersistenceContext(true).refresh(arg0);
  }

  /**
   * @throws TransactionRequiredException
   */
  public void remove(Object arg0)
  {
    getPersistenceContext(true).remove(arg0);
  }

  public void setFlushMode(FlushModeType arg0)
  {
    EntityManager em = getPersistenceContext(false);
    try {
      em.setFlushMode(arg0);
    } finally {
      if(em == detachedManager)
        em.clear();
    }
  }

  public <T> TypedQuery<T> createNamedQuery(String arg0, Class<T> arg1)
  {
    EntityManager em = getPersistenceContext(false);
    try {
      return em.createNamedQuery(arg0, arg1);
    } finally {
      if(em == detachedManager)
        em.clear();
    }
  }

  public <T> TypedQuery<T> createQuery(CriteriaQuery<T> arg0)
  {
    EntityManager em = getPersistenceContext(false);
    try {
      return em.createQuery(arg0);
    } finally {
      if(em == detachedManager)
        em.clear();
    }
  }

  public <T> TypedQuery<T> createQuery(String arg0, Class<T> arg1)
  {
    EntityManager em = getPersistenceContext(false);
    try {
      return em.createQuery(arg0, arg1);
    } finally {
      if(em == detachedManager)
        em.clear();
    }
  }

  public void detach(Object arg0)
  {
    EntityManager em = getPersistenceContext(false);
    //The detatched manager auto-detaches everything, so only
    //detach from a "real" entity manager
    if(em != detachedManager)
      em.detach(arg0);
  }

  public <T> T find(Class<T> arg0, Object arg1, Map<String, Object> arg2)
  {
    EntityManager em = getPersistenceContext(false);
    try {
      return em.find(arg0, arg1, arg2);
    } finally {
      if(em == detachedManager)
        em.clear();
    }
  }

  /**
   * @throws TransactionRequiredException if lock mode is not NONE
   */
  public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2)
  {
    EntityManager em = getPersistenceContext(arg2 != LockModeType.NONE);
    try {
      return em.find(arg0, arg1, arg2);
    } finally {
      if(em == detachedManager)
        em.clear();
    }
  }

  /**
   * @throws TransactionRequiredException if lock mode is not NONE
   */
  public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2, Map<String, Object> arg3)
  {
    EntityManager em = getPersistenceContext(arg2 != LockModeType.NONE);
    try {
      return em.find(arg0, arg1, arg2, arg3);
    } finally {
      if(em == detachedManager)
        em.clear();
    }
  }

  public CriteriaBuilder getCriteriaBuilder()
  {
    return getPersistenceContext(false).getCriteriaBuilder();
  }

  public EntityManagerFactory getEntityManagerFactory()
  {
    return emf;
  }

  /**
   * @throws TransactionRequiredException
   */
  public LockModeType getLockMode(Object arg0)
  {
    return getPersistenceContext(true).getLockMode(arg0);
  }

  public Metamodel getMetamodel()
  {
    return getPersistenceContext(false).getMetamodel();
  }

  public Map<String, Object> getProperties()
  {
    return getPersistenceContext(false).getProperties();
  }

  /**
   * @throws TransactionRequiredException
   */
  public void lock(Object arg0, LockModeType arg1, Map<String, Object> arg2)
  {
    getPersistenceContext(true).lock(arg0, arg1, arg2);
  }

  /**
   * @throws TransactionRequiredException
   */
  public void refresh(Object arg0, Map<String, Object> arg1)
  {
    getPersistenceContext(true).refresh(arg0, arg1);
  }

  /**
   * @throws TransactionRequiredException
   */
  public void refresh(Object arg0, LockModeType arg1)
  {
    getPersistenceContext(true).refresh(arg0, arg1);
  }

  /**
   * @throws TransactionRequiredException
   */
  public void refresh(Object arg0, LockModeType arg1, Map<String, Object> arg2)
  {
    getPersistenceContext(true).refresh(arg0, arg1, arg2);
  }

  public void setProperty(String arg0, Object arg1)
  {
    EntityManager em = getPersistenceContext(false);
    try {
      em.setProperty(arg0, arg1);
    } finally {
      if(em == detachedManager)
        em.clear();
    }
  }

  public <T> T unwrap(Class<T> arg0)
  {
    EntityManager em = getPersistenceContext(false);
    try {
      return em.unwrap(arg0);
    } finally {
      if(em == detachedManager)
        em.clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1282.java