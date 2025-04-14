error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18138.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18138.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18138.java
text:
```scala
p@@ublic class TestMixedLockManagerRefreshPermutation

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
package org.apache.openjpa.persistence.lockmgr;

import java.util.Arrays;

import javax.persistence.LockModeType;

/**
 * Test JPA 2.0 LockMode type permutation behaviors with "mixed" lock manager.
 */
public class MixedLockManagerRefreshPermutationTest 
    extends SequencedActionsTest {
    public void setUp() {
        setUp(LockEmployee.class
            , "openjpa.LockManager", "mixed"
        );
        commonSetUp();
    }

    /* ======== Thread 1 : Read Lock ============*/
    public void testRefreshReadRead() {
        commonRefreshTest(
            "testRefresh(Read,Commit/Read,Commit)",
            LockModeType.READ, Act.CommitTx, 2, null, 
            LockModeType.READ, Act.CommitTx, 2, ExpectingOptimisticLockExClass);
        commonRefreshTest(
            "testRefresh(Read,Commit/Read,Rollback)",
            LockModeType.READ, Act.CommitTx, 2, null,
            LockModeType.READ, Act.RollbackTx, 2, null);
    }
    
    public void testRefreshReadWrite() {
        commonRefreshTest(
            "testRefresh(Read,Commit/Write,Commit)",
            LockModeType.READ, Act.CommitTx, 2, null,
            LockModeType.WRITE, Act.CommitTx, 2,
                ExpectingOptimisticLockExClass);
        commonRefreshTest(
            "testRefresh(Read,Commit/Write,Rollback)",
            LockModeType.READ, Act.CommitTx, 2, null,
            LockModeType.WRITE, Act.RollbackTx, 2, null);
    }
    
    public void testRefreshReadPessimisticRead() {
        commonRefreshTest(
            "testRefresh(Read,Commit/PessimisticRead,Commit)",
            LockModeType.READ, Act.CommitTx, 2, null, // thread 2 tmo  
            LockModeType.PESSIMISTIC_READ, Act.CommitTx, 2, null);
        commonRefreshTest(
            "testRefresh(Read,Commit/PessimisticRead,Rollback)",
            LockModeType.READ, Act.CommitTx, 2, null,
            LockModeType.PESSIMISTIC_READ, Act.RollbackTx, 2, null);
    }
    
    public void testRefreshReadPessimisticWrite() {
        commonRefreshTest(
            "testRefresh(Read,Commit/PessimisticWrite,Commit)",
            LockModeType.READ, Act.CommitTx, 2, null, 
            LockModeType.PESSIMISTIC_WRITE, Act.CommitTx, 2, null);
        commonRefreshTest(
            "testRefresh(Read,Commit/PessimisticWrite,Rollback)",
            LockModeType.READ, Act.CommitTx, 2, null,
            LockModeType.PESSIMISTIC_WRITE, Act.RollbackTx, 2, null);
    }
    
    public void testRefreshReadPessimisticForceInc() {
        commonRefreshTest(
            "testRefresh(Read,Commit/PessimisticForceInc,Commit)",
            LockModeType.READ, Act.CommitTx, 2, ExpectingOptimisticLockExClass, 
            LockModeType.PESSIMISTIC_FORCE_INCREMENT, Act.CommitTx, 2, null);
        commonRefreshTest(
            "testRefresh(Read,Commit/PessimisticForceInc,Rollback)",
            LockModeType.READ, Act.CommitTx, 2, null,
            LockModeType.PESSIMISTIC_FORCE_INCREMENT, Act.RollbackTx, 2, null);
    }
    
    /* ======== Thread 1 : Write Lock ============*/
    public void testRefreshWriteRead() {
        commonRefreshTest(
            "testRefresh(Write,Commit/Read,Commit)",
            LockModeType.WRITE, Act.CommitTx, 2, null, 
            LockModeType.READ, Act.CommitTx, 2, ExpectingOptimisticLockExClass);
        commonRefreshTest(
            "testRefresh(Write,Commit/Read,Rollback)",
            LockModeType.WRITE, Act.CommitTx, 2, null,
            LockModeType.READ, Act.RollbackTx, 2, null);
    }
    
    public void testRefreshWriteWrite() {
        commonRefreshTest(
            "testRefresh(Write,Commit/Write,Commit)",
            LockModeType.WRITE, Act.CommitTx, 2, null,
            LockModeType.WRITE, Act.CommitTx, 2,
                ExpectingOptimisticLockExClass);
        commonRefreshTest(
            "testRefresh(Write,Commit/Write,Rollback)",
            LockModeType.WRITE, Act.CommitTx, 2, null,
            LockModeType.WRITE, Act.RollbackTx, 2, null);
    }
    
    public void testRefreshWritePessimisticRead() {
        commonRefreshTest(
            "testRefresh(Write,Commit/PessimisticRead,Commit)",
            LockModeType.WRITE, Act.CommitTx, 2, null, 
            LockModeType.PESSIMISTIC_READ, Act.CommitTx, 2, null);
        commonRefreshTest(
            "testRefresh(Write,Commit/PessimisticRead,Rollback)",
            LockModeType.WRITE, Act.CommitTx, 2, null,
            LockModeType.PESSIMISTIC_READ, Act.RollbackTx, 2, null);
    }
    
    public void testRefreshWritePessimisticWrite() {
        commonRefreshTest(
            "testRefresh(Write,Commit/PessimisticWrite,Commit)",
            LockModeType.WRITE, Act.CommitTx, 2, null, 
            LockModeType.PESSIMISTIC_WRITE, Act.CommitTx, 2, null);
        commonRefreshTest(
            "testRefresh(Write,Commit/PessimisticWrite,Rollback)",
            LockModeType.WRITE, Act.CommitTx, 2, null,
            LockModeType.PESSIMISTIC_WRITE, Act.RollbackTx, 2, null);
    }
    
    public void testRefreshWritePessimisticForceInc() {
        commonRefreshTest(
            "testRefresh(Write,Commit/PessimisticForceInc,Commit)",
            LockModeType.WRITE, Act.CommitTx, 2, ExpectingOptimisticLockExClass, 
            LockModeType.PESSIMISTIC_FORCE_INCREMENT, Act.CommitTx, 2, null);
        commonRefreshTest(
            "testRefresh(Write,Commit/PessimisticForceInc,Rollback)",
            LockModeType.WRITE, Act.CommitTx, 2, null,
            LockModeType.PESSIMISTIC_FORCE_INCREMENT, Act.RollbackTx, 2, null);
    }
    
    /* ======== Thread 1 : PessimisticRead Lock ============*/
    public void testRefreshPessimisticReadRead() {
        commonRefreshTest(
            "testRefresh(PessimisticRead,Commit/Read,Commit)",
            LockModeType.PESSIMISTIC_READ, Act.CommitTx, 2, null, 
            LockModeType.READ, Act.CommitTx, 2, ExpectingOptimisticLockExClass);
        commonRefreshTest(
            "testRefresh(PessimisticRead,Commit/Read,Rollback)",
            LockModeType.PESSIMISTIC_READ, Act.CommitTx, 2, null,
            LockModeType.READ, Act.RollbackTx, 2, null);
    }
    
    public void testRefreshPessimisticReadWrite() {
        commonRefreshTest(
            "testRefresh(PessimisticRead,Commit/Write,Commit)",
            LockModeType.PESSIMISTIC_READ, Act.CommitTx, 2, null,
            LockModeType.WRITE, Act.CommitTx, 2, 
                ExpectingOptimisticLockExClass);
        commonRefreshTest(
            "testRefresh(PessimisticRead,Commit/Write,Rollback)",
            LockModeType.PESSIMISTIC_READ, Act.CommitTx, 2, null,
            LockModeType.WRITE, Act.RollbackTx, 2, null);
    }
    
    public void testRefreshPessimisticReadPessimisticRead() {
        commonRefreshTest(
            "testRefresh(PessimisticRead,Commit/PessimisticRead,Commit)",
            LockModeType.PESSIMISTIC_READ, Act.CommitTx, 2, null, 
            LockModeType.PESSIMISTIC_READ, Act.CommitTx, 2, null);
        commonRefreshTest(
            "testRefresh(PessimisticRead,Commit/PessimisticRead,Rollback)",
            LockModeType.PESSIMISTIC_READ, Act.CommitTx, 2, null,
            LockModeType.PESSIMISTIC_READ, Act.RollbackTx, 2, null);
    }
    
    public void testRefreshPessimisticReadPessimisticWrite() {
        commonRefreshTest(
            "testRefresh(PessimisticRead,Commit/PessimisticWrite,Commit)",
            LockModeType.PESSIMISTIC_READ, Act.CommitTx, 2, null, 
            LockModeType.PESSIMISTIC_WRITE, Act.CommitTx, 2, null); 
        commonRefreshTest(
            "testRefresh(PessimisticRead,Commit/PessimisticWrite,Rollback)",
            LockModeType.PESSIMISTIC_READ, Act.CommitTx, 2, null,
            LockModeType.PESSIMISTIC_WRITE, Act.RollbackTx, 2, null);
    }
    
    public void testRefreshPessimisticReadPessimisticForceInc() {
        commonRefreshTest(
            "testRefresh(PessimisticRead,Commit/PessimisticForceInc,Commit)",
            LockModeType.PESSIMISTIC_READ, Act.CommitTx, 3, null, 
            LockModeType.PESSIMISTIC_FORCE_INCREMENT, Act.CommitTx, 3, null);
        commonRefreshTest(
            "testRefresh(PessimisticRead,Commit/PessimisticForceInc,Rollback)",
            LockModeType.PESSIMISTIC_READ, Act.CommitTx, 2, null,
            LockModeType.PESSIMISTIC_FORCE_INCREMENT, Act.RollbackTx, 2, null);
    }
    
    /* ======== Thread 1 : Pessimsitic Write Lock ============*/
    public void testRefreshPessimsiticWriteRead() {
        commonRefreshTest(
            "testRefresh(PessimsiticWrite,Commit/Read,Commit)",
            LockModeType.PESSIMISTIC_WRITE, Act.CommitTx, 2, null, 
            LockModeType.READ, Act.CommitTx, 2, ExpectingOptimisticLockExClass);
        commonRefreshTest(
            "testRefresh(PessimsiticWrite,Commit/Read,Rollback)",
            LockModeType.PESSIMISTIC_WRITE, Act.CommitTx, 2, null,
            LockModeType.READ, Act.RollbackTx, 2, null);
    }
    
    public void testRefreshPessimsiticWriteWrite() {
        commonRefreshTest(
            "testRefresh(PessimsiticWrite,Commit/Write,Commit)",
            LockModeType.PESSIMISTIC_WRITE, Act.CommitTx, 2, null,
            LockModeType.WRITE, Act.CommitTx, 2, 
                ExpectingOptimisticLockExClass);
        commonRefreshTest(
            "testRefresh(PessimsiticWrite,Commit/Write,Rollback)",
            LockModeType.PESSIMISTIC_WRITE, Act.CommitTx, 2, null,
            LockModeType.WRITE, Act.RollbackTx, 2, null);
    }
    
    public void testRefreshPessimsiticWritePessimisticRead() {
        commonRefreshTest(
            "testRefresh(PessimsiticWrite,Commit/PessimisticRead,Commit)",
            LockModeType.PESSIMISTIC_WRITE, Act.CommitTx, 2, null, 
            LockModeType.PESSIMISTIC_READ, Act.CommitTx, 2, null); 
        commonRefreshTest(
            "testRefresh(PessimsiticWrite,Commit/PessimisticRead,Rollback)",
            LockModeType.PESSIMISTIC_WRITE, Act.CommitTx, 2, null,
            LockModeType.PESSIMISTIC_READ, Act.RollbackTx, 2, null); 
    }
    
    public void testRefreshPessimsiticWritePessimisticWrite() {
        commonRefreshTest(
            "testRefresh(PessimsiticWrite,Commit/PessimisticWrite,Commit)",
            LockModeType.PESSIMISTIC_WRITE, Act.CommitTx, 2, null, 
            LockModeType.PESSIMISTIC_WRITE, Act.CommitTx, 2, null);
        commonRefreshTest(
            "testRefresh(PessimsiticWrite,Commit/PessimisticWrite,Rollback)",
            LockModeType.PESSIMISTIC_WRITE, Act.CommitTx, 2, null,
            LockModeType.PESSIMISTIC_WRITE, Act.RollbackTx, 2, null); 
    }
    
    public void testRefreshPessimsiticWritePessimisticForceInc() {
        commonRefreshTest(
            "testRefresh(PessimsiticWrite,Commit/PessimisticForceInc,Commit)",
            LockModeType.PESSIMISTIC_WRITE, Act.CommitTx, 3, null, 
            LockModeType.PESSIMISTIC_FORCE_INCREMENT, Act.CommitTx, 3, null); 
        commonRefreshTest(
            "testRefresh(PessimsiticWrite,Commit/PessimisticForceInc,Rollback)",
            LockModeType.PESSIMISTIC_WRITE, Act.CommitTx, 2, null,
            LockModeType.PESSIMISTIC_FORCE_INCREMENT, Act.RollbackTx, 2, null);
    }
    
    /* ======== Thread 1 : Pessimistic Force Increment Lock ============*/
    public void testRefreshPessimsiticForceIncRead() {
        commonRefreshTest(
            "testRefresh(PessimsiticForceInc,Commit/Read,Commit)",
            LockModeType.PESSIMISTIC_FORCE_INCREMENT, Act.CommitTx, 2, null, 
            LockModeType.READ, Act.CommitTx, 0, ExpectingOptimisticLockExClass);
        commonRefreshTest(
            "testRefresh(PessimsiticForceInc,Commit/Read,Rollback)",
            LockModeType.PESSIMISTIC_FORCE_INCREMENT, Act.CommitTx, 2, null,
            LockModeType.READ, Act.RollbackTx, 2, null);
    }
    
    public void testRefreshPessimsiticForceIncWrite() {
        commonRefreshTest(
            "testRefresh(PessimsiticForceInc,Commit/Write,Commit)",
            LockModeType.PESSIMISTIC_FORCE_INCREMENT, Act.CommitTx, 2, null,
            LockModeType.WRITE, Act.CommitTx, 0,
                ExpectingOptimisticLockExClass);
        commonRefreshTest(
            "testRefresh(PessimsiticForceInc,Commit/Write,Rollback)",
            LockModeType.PESSIMISTIC_FORCE_INCREMENT, Act.CommitTx, 2, null,
            LockModeType.WRITE, Act.RollbackTx, 2, null);
    }
    
    public void testRefreshPessimsiticForceIncPessimisticRead() {
        commonRefreshTest(
            "testRefresh(PessimsiticForceInc,Commit/PessimisticRead,Commit)",
            LockModeType.PESSIMISTIC_FORCE_INCREMENT, Act.CommitTx, 2, null, 
            LockModeType.PESSIMISTIC_READ, Act.CommitTx, 2, null); 
        commonRefreshTest(
            "testRefresh(PessimsiticForceInc,Commit/PessimisticRead,Rollback)",
            LockModeType.PESSIMISTIC_FORCE_INCREMENT, Act.CommitTx, 2, null,
            LockModeType.PESSIMISTIC_READ, Act.RollbackTx, 2, null); 
    }
    
    public void testRefreshPessimsiticForceIncPessimisticWrite() {
        commonRefreshTest(
            "testRefresh(PessimsiticForceInc,Commit/PessimisticWrite,Commit)",
            LockModeType.PESSIMISTIC_FORCE_INCREMENT, Act.CommitTx, 2, null, 
            LockModeType.PESSIMISTIC_WRITE, Act.CommitTx, 2, null); 
        commonRefreshTest(
            "testRefresh(PessimsiticForceInc,Commit/PessimisticWrite,Rollback)",
            LockModeType.PESSIMISTIC_FORCE_INCREMENT, Act.CommitTx, 2, null,
            LockModeType.PESSIMISTIC_WRITE, Act.RollbackTx, 2, null);
    }
    
    public void testRefreshPessimsiticForceIncPessimisticForceInc() {
        commonRefreshTest(
            "testRefresh(PessimsiticForceInc,Commit/" 
                + "PessimisticForceInc,Commit)",
            LockModeType.PESSIMISTIC_FORCE_INCREMENT, Act.CommitTx, 3, null, 
            LockModeType.PESSIMISTIC_FORCE_INCREMENT, Act.CommitTx, 3, null);
        commonRefreshTest(
            "testRefresh(PessimsiticForceInc,Commit/"
                + "PessimisticForceInc,Rollback)",
            LockModeType.PESSIMISTIC_FORCE_INCREMENT, Act.CommitTx, 2, null,
            LockModeType.PESSIMISTIC_FORCE_INCREMENT, Act.RollbackTx, 2, null); 
    }

    private void commonRefreshTest( String testName, 
        LockModeType t1Lock, Act t1IsCommit, int t1VersionInc, 
            Class<?>[] t1Exceptions, 
        LockModeType t2Lock, Act t2IsCommit, int t2VersionInc,
            Class<?>[] t2Exceptions ) {
        String[] parameters = new String[] {
            "Thread 1: lock= " + t1Lock + ", isCommit= " + t1IsCommit +
                ", versionInc= +" + t1VersionInc +
                ", expectedEx= " + Arrays.toString(t1Exceptions),
            "Thread 2: lock= " + t2Lock + ", isCommit= " + t2IsCommit + 
                ", versionInc= +" + t2VersionInc +
                ", expectedEx= " + Arrays.toString(t2Exceptions)};
            
        String t1Message1 = "Refresh in Thread 1";
        String t1Message2 = "Refresh in Thread 1 Again";
        String t2Message1 = "Refresh in Thread 2";
        
        Object[][] threadMain = {
            {Act.CreateEm},
            {Act.Find},
            {Act.SaveVersion},
            {Act.TestEmployee, 1, Default_FirstName},
            
            {Act.NewThread, 1 },
            {Act.NewThread, 2 },
            
            {Act.StartThread, 1 },
            {Act.Wait},
            
            {Act.StartThread, 2 },            
            {Act.WaitAllChildren},
            
            {Act.Find},
            {Act.TestEmployee, 1},
            {Act.TestException, 1, t1Exceptions },
            {Act.TestException, 2, t2Exceptions },
        };
        Object[][] thread1 = {
            {Act.CreateEm},
            {Act.Find, 1},
            {Act.SaveVersion},
            {Act.TestEmployee, 1, Default_FirstName},
            {Act.Notify, 0},
            {Act.Wait},
            
            {Act.StartTx},
            {Act.UpdateEmployee, 1, t1Message1},
            {Act.TestEmployee, 1, t1Message1},
            {Act.CommitTx},
            {Act.TestException},
            {Act.Notify, 2},
            {Act.Wait},
            
            {Act.StartTx},
            {Act.RefreshWithLock, 1, t1Lock },
            {Act.TestLockMode, 1, t1Lock},
            {Act.TestEmployee, 1, t1Message1},
            {Act.UpdateEmployee, 1, t1Message2},
            {Act.TestEmployee, 1, t1Message2},
            
            {t1IsCommit},
            {Act.Notify, 2},
            {Act.Notify, 2},
            
            {Act.Clear},
            {Act.Find},
            {Act.TestEmployee, 1, null, t1VersionInc}
        };
        Object[][] thread2 = {
            {Act.CreateEm},
            {Act.Find, 1},
            {Act.SaveVersion},
            {Act.TestEmployee, 1, Default_FirstName},
            {Act.Notify, 1},
            {Act.Wait},

            {Act.StartTx},
            {Act.UpdateEmployee, 1, t2Message1},
            {Act.TestEmployee, 1, t2Message1},
            {Act.RefreshWithLock, 1, t2Lock },
            {Act.TestLockMode, 1, t2Lock},
            {Act.TestEmployee, 1, t1Message1},
            
            {Act.Notify, 1},
            {Act.Wait},
            {t2IsCommit},
            {Act.Wait},
            
            {Act.Clear},
            {Act.Find},
            {Act.TestEmployee, 1, null, t2VersionInc},
        };
        
        launchActionSequence(testName, parameters, threadMain, thread1,
            thread2);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18138.java