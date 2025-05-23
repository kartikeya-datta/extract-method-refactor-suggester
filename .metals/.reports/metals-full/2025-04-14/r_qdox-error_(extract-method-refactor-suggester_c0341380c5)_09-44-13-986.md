error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/739.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/739.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/739.java
text:
```scala
l@@.close();

package org.apache.lucene.store;

/*
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

import java.io.IOException;
import java.io.File;

/**
 * Simple standalone tool that forever acquires & releases a
 * lock using a specific LockFactory.  Run without any args
 * to see usage.
 *
 * @see VerifyingLockFactory
 * @see LockVerifyServer
 */ 

public class LockStressTest {

  public static void main(String[] args) throws Exception {

    if (args.length != 6) {
      System.out.println("\nUsage: java org.apache.lucene.store.LockStressTest myID verifierHostOrIP verifierPort lockFactoryClassName lockDirName sleepTime\n" +
                         "\n" +
                         "  myID = int from 0 .. 255 (should be unique for test process)\n" +
                         "  verifierHostOrIP = host name or IP address where LockVerifyServer is running\n" +
                         "  verifierPort = port that LockVerifyServer is listening on\n" +
                         "  lockFactoryClassName = primary LockFactory class that we will use\n" +
                         "  lockDirName = path to the lock directory (only set for Simple/NativeFSLockFactory\n" +
                         "  sleepTimeMS = milliseconds to pause betweeen each lock obtain/release\n" +
                         "\n" +
                         "You should run multiple instances of this process, each with its own\n" +
                         "unique ID, and each pointing to the same lock directory, to verify\n" +
                         "that locking is working correctly.\n" +
                         "\n" +
                         "Make sure you are first running LockVerifyServer.\n" + 
                         "\n");
      System.exit(1);
    }

    final int myID = Integer.parseInt(args[0]);

    if (myID < 0 || myID > 255) {
      System.out.println("myID must be a unique int 0..255");
      System.exit(1);
    }

    final String verifierHost = args[1];
    final int verifierPort = Integer.parseInt(args[2]);
    final String lockFactoryClassName = args[3];
    final String lockDirName = args[4];
    final int sleepTimeMS = Integer.parseInt(args[5]);

    LockFactory lockFactory;
    try {
      lockFactory = Class.forName(lockFactoryClassName).asSubclass(LockFactory.class).newInstance();          
    } catch (IllegalAccessException e) {
      throw new IOException("IllegalAccessException when instantiating LockClass " + lockFactoryClassName);
    } catch (InstantiationException e) {
      throw new IOException("InstantiationException when instantiating LockClass " + lockFactoryClassName);
    } catch (ClassCastException e) {
      throw new IOException("unable to cast LockClass " + lockFactoryClassName + " instance to a LockFactory");
    } catch (ClassNotFoundException e) {
      throw new IOException("unable to find LockClass " + lockFactoryClassName);
    }

    File lockDir = new File(lockDirName);

    if (lockFactory instanceof FSLockFactory) {
      ((FSLockFactory) lockFactory).setLockDir(lockDir);
    }

    lockFactory.setLockPrefix("test");
    
    LockFactory verifyLF = new VerifyingLockFactory((byte) myID, lockFactory, verifierHost, verifierPort);

    Lock l = verifyLF.makeLock("test.lock");

    while(true) {

      boolean obtained = false;

      try {
        obtained = l.obtain(10);
      } catch (LockObtainFailedException e) {
        System.out.print("x");
      }

      if (obtained) {
        System.out.print("l");
        l.release();
      }
      Thread.sleep(sleepTimeMS);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/739.java