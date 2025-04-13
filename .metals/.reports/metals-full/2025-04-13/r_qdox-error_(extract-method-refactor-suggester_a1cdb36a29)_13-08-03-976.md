error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2082.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2082.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2082.java
text:
```scala
B@@undle.INSTALLED | Bundle.RESOLVED | Bundle.STARTING | Bundle.ACTIVE | Bundle.STOPPING;

/**
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
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.util.tracker;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

/**
 * <p>This class supports the tracking of composite bundles. It allows clients to ignore any
 * events related to framework bundles, as it will automatically handle these events. In
 * order to use this class clients must create a subclass and implement the methods of the
 * <code>BundleTrackerCustomizer</code> interface. In spite of this, instances of this class
 * MUST NOT be passed as a parameter to any <code>BundleTracker</code>.</p> 
 * 
 * The model for using this is that classes should instantiate it
 * and pass it a 'vanilla' bundle tracker.
 * @author pradine
 *
 */
public final class RecursiveBundleTracker  {
    private static final int COMPOSITE_BUNDLE_MASK =
      Bundle.INSTALLED | Bundle.STARTING | Bundle.ACTIVE | Bundle.STOPPING;
    
    private final BundleTracker tracker;
        
    /**
     * Constructor
     * 
     * @param context - The <code>BundleContext</code> against which the tracking is done.
     * @param stateMask - The bit mask of the ORing of the bundle states to be tracked. The
     * mask must contain the flags <code>Bundle.INSTALLED | Bundle.STARTING | Bundle.ACTIVE | Bundle.STOPPING</code>
     * as a minimum.
     * @throws IllegalArgumentException - If the provided bit mask does not contain required
     * flags
     */
    public RecursiveBundleTracker(BundleContext context, int stateMask, BundleTrackerCustomizer customizer) {
      // We always need INSTALLED events so we can recursively listen to the frameworks
      if ((stateMask & COMPOSITE_BUNDLE_MASK) != COMPOSITE_BUNDLE_MASK)
            throw new IllegalArgumentException();
       if (areMultipleFrameworksAvailable(context)) {
          tracker = new InternalRecursiveBundleTracker(context, stateMask, customizer);
        } else {
         tracker = new BundleTracker(context, stateMask, customizer);
        }
    }
    
    private static boolean areMultipleFrameworksAvailable(BundleContext context) {
      ServiceReference sr = context.getServiceReference("org.osgi.service.framework.CompositeBundleFactory");
      return sr != null;
    }
    
    /**
     * Start tracking bundles that match the bit mask provided at creation time.
     * 
     * @see BundleTracker#open()
     */
    public void open() {
        tracker.open();
    }
    
    /**
     * Stop the tracking of bundles
     * 
     * @see BundleTracker#close()
     */
    public void close() {
        tracker.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2082.java