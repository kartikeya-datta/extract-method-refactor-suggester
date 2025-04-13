error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8863.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8863.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,29]

error in qdox parser
file content:
```java
offset: 29
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8863.java
text:
```scala
{Act.TestException, 0, null }@@,

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

import javax.persistence.LockModeType;
import javax.persistence.TransactionRequiredException;

/**
 * Test JPA 2.0 em.refresh(LockMode) exception behaviors with "mixed"
 * lock manager.
 */
public class TestMixedLockManagerRefreshException extends SequencedActionsTest {
    public void setUp() {
        setUp(LockEmployee.class
            , "openjpa.LockManager", "mixed"
        );
        commonSetUp();
    }

    /**
     * TransactionRequiredException if there is no transaction
     */
    public void testRefreshNoTxReqExceptions() {
        Object[][] threadMainTxReqTest = {
            {Act.CreateEm},
            {Act.Find},
            {Act.SaveVersion},
            {Act.TestEmployee, 1, Default_FirstName},
            
            {Act.Refresh, 1, LockModeType.NONE },
            {Act.TestException, 0, null },
            
            {Act.Refresh, 1, LockModeType.READ },
            {Act.TestException, 0, null },
            
            {Act.Refresh, 1, LockModeType.WRITE },
            {Act.TestException, 0, null },
            
            {Act.Refresh, 1, LockModeType.OPTIMISTIC },
            {Act.TestException, 0, null },
            
            {Act.Refresh, 1, LockModeType.OPTIMISTIC_FORCE_INCREMENT },
            {Act.TestException, 0, null },
            
            {Act.Refresh, 1, LockModeType.PESSIMISTIC_READ},
            {Act.TestException, 0, null },
            
            {Act.Refresh, 1, LockModeType.PESSIMISTIC_WRITE},
            {Act.TestException, 0, null },
            
            {Act.Refresh, 1, LockModeType.PESSIMISTIC_FORCE_INCREMENT },
            {Act.TestException, 0, null },
        };
        launchActionSequence("testLockTxReqExceptions()",
            null, threadMainTxReqTest);
    }

    /**
     * TransactionRequiredException if there is no transaction
     */
    public void testRefreshTxReqExceptions() {
        Object[][] threadMainTxReqTest = {
            {Act.CreateEm},
            {Act.Find},
            {Act.SaveVersion},
            {Act.TestEmployee, 1, Default_FirstName},
            
            {Act.RefreshWithLock, 1, LockModeType.NONE },
            {Act.TestException, 0, TransactionRequiredException.class },
            
            {Act.RefreshWithLock, 1, LockModeType.READ },
            {Act.TestException, 0, TransactionRequiredException.class },
            
            {Act.RefreshWithLock, 1, LockModeType.WRITE },
            {Act.TestException, 0, TransactionRequiredException.class },
            
            {Act.RefreshWithLock, 1, LockModeType.OPTIMISTIC },
            {Act.TestException, 0, TransactionRequiredException.class },
            
            {Act.RefreshWithLock, 1, LockModeType.OPTIMISTIC_FORCE_INCREMENT },
            {Act.TestException, 0, TransactionRequiredException.class },
            
            {Act.RefreshWithLock, 1, LockModeType.PESSIMISTIC_READ},
            {Act.TestException, 0, TransactionRequiredException.class },
            
            {Act.RefreshWithLock, 1, LockModeType.PESSIMISTIC_WRITE},
            {Act.TestException, 0, TransactionRequiredException.class },
            
            {Act.RefreshWithLock, 1, LockModeType.PESSIMISTIC_FORCE_INCREMENT },
            {Act.TestException, 0, TransactionRequiredException.class },
        };
        launchActionSequence("testLockTxReqExceptions()",
            null, threadMainTxReqTest);
    }

    /*
     * IllegalArgumentException if the instance is not an entity or is a
     *      detached entity
     */
    public void testRefreshIllegalArgrumentExceptions() {
        // Test invalid entity argument throws IllegalArgumentException.
        Object[][] threadMainInvEntityIllegalArgTest = {
            {Act.CreateEm},
            {Act.Find},
            {Act.SaveVersion},
            {Act.TestEmployee, 1, Default_FirstName},
            {Act.StartTx},
      
            {Act.RefreshObject, null, LockModeType.NONE },
            {Act.TestException, 0, IllegalArgumentException.class },
            {Act.RefreshObject, "null", LockModeType.NONE },
            {Act.TestException, 0, IllegalArgumentException.class },
            
            {Act.RefreshObject, null, LockModeType.READ },
            {Act.TestException, 0, IllegalArgumentException.class },
            {Act.RefreshObject, "null", LockModeType.READ },
            {Act.TestException, 0, IllegalArgumentException.class },

            {Act.RefreshObject, null, LockModeType.WRITE },
            {Act.TestException, 0, IllegalArgumentException.class },
            {Act.RefreshObject, "null", LockModeType.WRITE },
            {Act.TestException, 0, IllegalArgumentException.class },

            {Act.RefreshObject, null, LockModeType.OPTIMISTIC },
            {Act.TestException, 0, IllegalArgumentException.class },
            {Act.RefreshObject, "null", LockModeType.OPTIMISTIC },
            {Act.TestException, 0, IllegalArgumentException.class },

            {Act.RefreshObject, null, 
                LockModeType.OPTIMISTIC_FORCE_INCREMENT },
            {Act.TestException, 0, IllegalArgumentException.class },
            {Act.RefreshObject, "null", 
                LockModeType.OPTIMISTIC_FORCE_INCREMENT },
            {Act.TestException, 0, IllegalArgumentException.class },

            {Act.RefreshObject, null, LockModeType.PESSIMISTIC_READ },
            {Act.TestException, 0, IllegalArgumentException.class },
            {Act.RefreshObject, "null", LockModeType.PESSIMISTIC_READ },
            {Act.TestException, 0, IllegalArgumentException.class },

            {Act.RefreshObject, null, LockModeType.PESSIMISTIC_WRITE },
            {Act.TestException, 0, IllegalArgumentException.class },
            {Act.RefreshObject, "null", LockModeType.PESSIMISTIC_WRITE },
            {Act.TestException, 0, IllegalArgumentException.class },

            {Act.RefreshObject, null, 
                LockModeType.PESSIMISTIC_FORCE_INCREMENT },
            {Act.TestException, 0, IllegalArgumentException.class },
            {Act.RefreshObject, "null", 
                LockModeType.PESSIMISTIC_FORCE_INCREMENT },
            {Act.TestException, 0, IllegalArgumentException.class },
        };
        launchActionSequence("testLockIllegalArgrumentExceptions()",
            "Test invalid entity.", threadMainInvEntityIllegalArgTest);
        
        // Test detached entity argument throws IllegalArgumentException.
        Object[][] threadMainDetachEntityIllegalArgTest = {
            {Act.CreateEm},
            {Act.Find},
            {Act.SaveVersion},
            {Act.TestEmployee, 1, Default_FirstName},
            {Act.StartTx},
            {Act.Detach, 1, 2},
            
            {Act.Refresh, 2, LockModeType.PESSIMISTIC_FORCE_INCREMENT },
            {Act.TestException, 0, IllegalArgumentException.class },
            
            {Act.Refresh, 2, LockModeType.NONE },
            {Act.TestException, 0, IllegalArgumentException.class },
          
            {Act.Refresh, 2, LockModeType.READ },
            {Act.TestException, 0, IllegalArgumentException.class },

            {Act.Refresh, 2, LockModeType.WRITE },
            {Act.TestException, 0, IllegalArgumentException.class },

            {Act.Refresh, 2, LockModeType.OPTIMISTIC },
            {Act.TestException, 0, IllegalArgumentException.class },

            {Act.Refresh, 2, LockModeType.OPTIMISTIC_FORCE_INCREMENT },
            {Act.TestException, 0, IllegalArgumentException.class },

            {Act.Refresh, 2, LockModeType.PESSIMISTIC_READ },
            {Act.TestException, 0, IllegalArgumentException.class },

            {Act.Refresh, 2, LockModeType.PESSIMISTIC_WRITE },
            {Act.TestException, 0, IllegalArgumentException.class },

            {Act.Refresh, 2, LockModeType.PESSIMISTIC_FORCE_INCREMENT },
            {Act.TestException, 0, IllegalArgumentException.class },
        };
        launchActionSequence("testLockIllegalArgrumentExceptions()",
            "Test detached entity.", threadMainDetachEntityIllegalArgTest);

        // Test detached argument from serialized entity throws 
        //  IllegalArgumentException.
        Object[][] threadMainDetachSerializeIllegalArgTest = {
            {Act.CreateEm},
            {Act.Find},
            {Act.SaveVersion},
            {Act.TestEmployee, 1, Default_FirstName},
            {Act.StartTx},
            {Act.DetachSerialize, 1, 2},
            
            {Act.Refresh, 2, LockModeType.NONE },
            {Act.TestException, 0, IllegalArgumentException.class },
        
            {Act.Refresh, 2, LockModeType.READ },
            {Act.TestException, 0, IllegalArgumentException.class },

            {Act.Refresh, 2, LockModeType.WRITE },
            {Act.TestException, 0, IllegalArgumentException.class },

            {Act.Refresh, 2, LockModeType.OPTIMISTIC },
            {Act.TestException, 0, IllegalArgumentException.class },

            {Act.Refresh, 2, LockModeType.OPTIMISTIC_FORCE_INCREMENT },
            {Act.TestException, 0, IllegalArgumentException.class },

            {Act.Refresh, 2, LockModeType.PESSIMISTIC_READ },
            {Act.TestException, 0, IllegalArgumentException.class },

            {Act.Refresh, 2, LockModeType.PESSIMISTIC_WRITE },
            {Act.TestException, 0, IllegalArgumentException.class },

            {Act.Refresh, 2, LockModeType.PESSIMISTIC_FORCE_INCREMENT },
            {Act.TestException, 0, IllegalArgumentException.class },
        };        
        launchActionSequence("testLockIllegalArgrumentExceptions()",
            "Test detached entity using serialization.",
            threadMainDetachSerializeIllegalArgTest);
        
        Object[][] threadMainRemoveIllegalArgTest = {
            {Act.CreateEm},
            {Act.Find},
            {Act.SaveVersion},
            {Act.TestEmployee, 1, Default_FirstName},
            {Act.StartTx},
            {Act.Remove},

            {Act.Refresh, 1, LockModeType.NONE },
            {Act.TestException},
      
            {Act.Refresh, 1, LockModeType.READ },
            {Act.TestException},

            {Act.Refresh, 1, LockModeType.WRITE },
            {Act.TestException},

            {Act.Refresh, 1, LockModeType.OPTIMISTIC },
            {Act.TestException},

            {Act.Refresh, 1, LockModeType.OPTIMISTIC_FORCE_INCREMENT },
            {Act.TestException},

            {Act.Refresh, 1, LockModeType.PESSIMISTIC_READ },
            {Act.TestException},

            {Act.Refresh, 1, LockModeType.PESSIMISTIC_WRITE },
            {Act.TestException},

            {Act.Refresh, 1, LockModeType.PESSIMISTIC_FORCE_INCREMENT },
            {Act.TestException},
            
            {Act.RollbackTx},
        };
        launchActionSequence(
            "testLockIllegalArgrumentExceptions()",
            "Test removed entity - no exception since it is still "
                + "in the context.",
            threadMainRemoveIllegalArgTest);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8863.java