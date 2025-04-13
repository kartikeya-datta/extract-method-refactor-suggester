error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14926.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14926.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14926.java
text:
```scala
p@@ublic class TransactionManagerRegistryFacade

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
package org.apache.openjpa.ee;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.xa.XAResource;

import org.apache.openjpa.lib.util.Localizer;

/**
 * Implementation of the {@link ManagedRuntime} interface that uses 
 * the {@link TransactionSynchronizationRegistry} interface (new in JTA 1.1)
 * to create a {@link TransactionManager} facade for controlling transactions.
 *
 * @author Marc Prud'hommeaux
 * @since 1.0.0
 */
public class RegistryManagedRuntime
    implements ManagedRuntime {
    private String _registryName =
        "java:comp/TransactionSynchronizationRegistry";
    private TransactionManagerRegistryFacade _tm = null;
    
    private static Localizer _loc =
        Localizer.forPackage(RegistryManagedRuntime.class);

    /**
     * Return the cached TransactionManager instance.
     */
    public TransactionManager getTransactionManager() throws Exception {
        if (_tm == null) {
            Context ctx = new InitialContext();
            try {
                _tm = new TransactionManagerRegistryFacade
                    ((TransactionSynchronizationRegistry) ctx.
                        lookup(_registryName));
            } finally {
                ctx.close();
            }
        }
        return _tm;
    }

    public void setRollbackOnly(Throwable cause)
        throws Exception {
        // there is no generic support for setting the rollback cause
        getTransactionManager().getTransaction().setRollbackOnly();
    }

    public Throwable getRollbackCause()
        throws Exception {
        // there is no generic support for setting the rollback cause
        return null;
    }

    public void setRegistryName(String registryName) {
        _registryName = registryName;
    }

    public String getRegistryName() {
        return _registryName;
    }

    public Object getTransactionKey() throws Exception, SystemException {
        return _tm.getTransactionKey();
    }

    /** 
     *  A {@link TransactionManager} and {@link Transaction} facade
     *  that delegates the appropriate methods to the internally-held
     *  {@link TransactionSynchronizationRegistry}. Since the
     *  registry is not able to start or end transactions, all transaction
     *  control methods will just throw a {@link SystemException}.
     *  
     *  @author  Marc Prud'hommeaux
     */
    public static class TransactionManagerRegistryFacade
        implements TransactionManager, Transaction {
        private final TransactionSynchronizationRegistry _registry;

        public TransactionManagerRegistryFacade
            (TransactionSynchronizationRegistry registry) {
            _registry = registry;
        }


        public Transaction getTransaction()
            throws SystemException {
            return TransactionManagerRegistryFacade.this;
        }


        public void registerSynchronization(Synchronization sync)
            throws RollbackException, IllegalStateException, SystemException {
            _registry.registerInterposedSynchronization(sync);
        }


        public void setRollbackOnly()
            throws IllegalStateException, SystemException {
            _registry.setRollbackOnly();
        }


        public int getStatus()
            throws SystemException {
            return _registry.getTransactionStatus();
        }

        public Object getTransactionKey() {
            return _registry.getTransactionKey();
        }

        //////////////////////////////
        // Unsupported methods follow
        //////////////////////////////

        public void begin()
            throws NotSupportedException, SystemException {
            throw new NotSupportedException();
        }


        public void commit()
            throws RollbackException, HeuristicMixedException, SystemException,
                HeuristicRollbackException, SecurityException,
                IllegalStateException {
            throw new SystemException();
        }


        public void resume(Transaction tobj)
            throws InvalidTransactionException, IllegalStateException,
                SystemException {
            throw new SystemException();
        }


        public void rollback()
            throws IllegalStateException, SecurityException, SystemException {
            throw new SystemException();
        }


        public void setTransactionTimeout(int seconds)
            throws SystemException {
            throw new SystemException();
        }


        public Transaction suspend()
            throws SystemException {
            throw new SystemException();
        }


        public boolean delistResource(XAResource xaRes, int flag)
            throws IllegalStateException, SystemException {
            throw new SystemException();
        }


        public boolean enlistResource(XAResource xaRes)
            throws RollbackException, IllegalStateException, SystemException {
            throw new SystemException();
        }
    }
    
    /**
     * <P>
     * RegistryManagedRuntime cannot suspend transactions.
     * </P>
     */
    public void doNonTransactionalWork(Runnable runnable)
        throws NotSupportedException {
        throw new NotSupportedException(_loc.get("tsr-cannot-suspend")
            .getMessage());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14926.java