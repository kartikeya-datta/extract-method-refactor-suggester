error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3256.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3256.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3256.java
text:
```scala
B@@undle.INSTALLED | Bundle.STARTING | Bundle.ACTIVE | Bundle.STOPPING, customizer, true);

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
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.util;

import org.apache.aries.unittest.mocks.MethodCall;
import org.apache.aries.unittest.mocks.Skeleton;
import org.apache.aries.util.tracker.BundleTrackerFactory;
import org.apache.aries.util.tracker.InternalRecursiveBundleTracker;
import org.apache.aries.util.tracker.RecursiveBundleTracker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;
import org.osgi.service.framework.CompositeBundle;
import org.osgi.util.tracker.BundleTrackerCustomizer;

import static org.junit.Assert.*;

public class RecursiveBundleTrackerTest {
    BundleContext context;
    InternalRecursiveBundleTracker sut;
    
    @Before
    public void setup() {
        context = Skeleton.newMock(BundleContext.class);
        Skeleton.getSkeleton(context).setReturnValue(
                new MethodCall(BundleContext.class, "getServiceReference", "org.osgi.service.framework.CompositeBundleFactory"), 
                Skeleton.newMock(ServiceReference.class));
    }
    
    @After
    public void closeTrackes() {
        BundleTrackerFactory.unregisterAndCloseBundleTracker("test");
    }
    
    @Test
    public void testCompositeLifeCycle() {
        makeSUT();
        CompositeBundle  cb = composite("test.composite", "1.0.0");
        assertNoTrackers();
        
        // full lifecycle
        
        sut.addingBundle(cb, new BundleEvent(BundleEvent.INSTALLED, cb));
        assertTracker(cb);

        sut.modifiedBundle(cb, new BundleEvent(BundleEvent.RESOLVED, cb), cb);
        sut.modifiedBundle(cb, new BundleEvent(BundleEvent.STARTING, cb), cb);
        sut.modifiedBundle(cb, new BundleEvent(BundleEvent.STARTED, cb), cb);
        sut.modifiedBundle(cb, new BundleEvent(BundleEvent.STOPPING, cb), cb);
        sut.removedBundle(cb, new BundleEvent(BundleEvent.STOPPED, cb), cb);
        assertNoTrackers();
        
        // short lifecycle
        
        sut.addingBundle(cb, new BundleEvent(BundleEvent.INSTALLED, cb));
        assertTracker(cb);
        
        sut.modifiedBundle(cb, new BundleEvent(BundleEvent.RESOLVED, cb), cb);        
        sut.removedBundle(cb, new BundleEvent(BundleEvent.UNRESOLVED, cb), cb);
        assertNoTrackers();
        
        // shortest lifecycle
        
        sut.addingBundle(cb, new BundleEvent(BundleEvent.INSTALLED, cb));
        assertTracker(cb);
        
        sut.removedBundle(cb, new BundleEvent(BundleEvent.UNINSTALLED, cb), cb);
        assertNoTrackers();
    }
    
    
    @Test(expected=IllegalArgumentException.class)
    public void testMissingStopping() {
        new RecursiveBundleTracker(null, Bundle.INSTALLED | Bundle.RESOLVED | Bundle.STARTING | Bundle.ACTIVE, null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testMissingStarting() {
        new RecursiveBundleTracker(null, Bundle.INSTALLED | Bundle.RESOLVED | Bundle.ACTIVE | Bundle.STOPPING, null);        
    }

    @Test(expected=IllegalArgumentException.class)
    public void testMissingInstalled() {
        new RecursiveBundleTracker(null, Bundle.RESOLVED | Bundle.STARTING | Bundle.ACTIVE | Bundle.STOPPING, null);        
    }
    
    private void assertNoTrackers() {
        assertTrue(BundleTrackerFactory.getAllBundleTracker().isEmpty());        
    }
    
    private void assertTracker(CompositeBundle cb) {
        assertEquals(1, BundleTrackerFactory.getAllBundleTracker().size());
        assertEquals(1, BundleTrackerFactory.getBundleTrackerList(cb.getSymbolicName()+"_"+cb.getVersion()).size());        
    }
    
    private void makeSUT() {
        BundleTrackerCustomizer customizer = Skeleton.newMock(BundleTrackerCustomizer.class);

        sut = new InternalRecursiveBundleTracker(context, 
                Bundle.INSTALLED | Bundle.STARTING | Bundle.ACTIVE | Bundle.STOPPING, customizer);
        
        sut.open();
    }
    
    private CompositeBundle composite(String symbolicName, String version) {
        CompositeBundle cb = Skeleton.newMock(CompositeBundle.class);
        Skeleton cbSkel = Skeleton.getSkeleton(cb);
        cbSkel.setReturnValue(new MethodCall(CompositeBundle.class, "getSymbolicName"), symbolicName);
        cbSkel.setReturnValue(new MethodCall(CompositeBundle.class, "getVersion"), new Version(version));
        return cb;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3256.java