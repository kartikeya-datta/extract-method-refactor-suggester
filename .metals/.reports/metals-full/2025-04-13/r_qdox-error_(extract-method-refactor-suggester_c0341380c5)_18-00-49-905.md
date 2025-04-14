error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10174.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10174.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10174.java
text:
```scala
m@@ap.setMutator( new EmptyMapMutator<K, V>() );

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections.map;

import junit.framework.Assert;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

/**
 * Extension of {@link AbstractTestMap} for exercising the 
 * {@link CompositeMap} implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision$ $Date$
 *
 * @author Brian McCallister
 */
public class TestCompositeMap<K, V> extends AbstractTestIterableMap<K, V> {
    /** used as a flag in MapMutator tests */
    private boolean pass = false;
    
    public TestCompositeMap(String testName) {
        super(testName);
    }
    
    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.pass = false;
    }
    
    @Override
    public CompositeMap<K, V> makeObject() {
        CompositeMap<K, V> map = new CompositeMap<K, V>();
        map.addComposited(new HashMap<K, V>());
        map.setMutator( new EmptyMapMutator() );
        return map;
    }
    
    @SuppressWarnings("unchecked")
    private Map<K, V> buildOne() {
        HashMap<K, V> map = new HashMap<K, V>();
        map.put((K) "1", (V) "one");
        map.put((K) "2", (V) "two");
        return map;
    }
    
    @SuppressWarnings("unchecked")
    public Map<K, V> buildTwo() {
        HashMap<K, V> map = new HashMap<K, V>();
        map.put((K) "3", (V) "three");
        map.put((K) "4", (V) "four");
        return map;
    }
    
    public void testGet() {
        CompositeMap<K, V> map = new CompositeMap<K, V>(buildOne(), buildTwo());
        Assert.assertEquals("one", map.get("1"));
        Assert.assertEquals("four", map.get("4"));
    }
    
    @SuppressWarnings("unchecked")
    public void testAddComposited() {
        CompositeMap<K, V> map = new CompositeMap<K, V>(buildOne(), buildTwo());
        HashMap<K, V> three = new HashMap<K, V>();
        three.put((K) "5", (V) "five");
        map.addComposited(three);
        assertTrue(map.containsKey("5"));
        try {
            map.addComposited(three);
            fail("Expecting IllegalArgumentException.");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }
    
    @SuppressWarnings("unchecked")
    public void testRemoveComposited() {
        CompositeMap<K, V> map = new CompositeMap<K, V>(buildOne(), buildTwo());
        HashMap<K, V> three = new HashMap<K, V>();
        three.put((K) "5", (V) "five");
        map.addComposited(three);
        assertTrue(map.containsKey("5"));
        
        map.removeComposited(three);
        assertFalse(map.containsKey("5"));
        
        map.removeComposited(buildOne());
        assertFalse(map.containsKey("2"));
        
    }
    
    @SuppressWarnings("unchecked")
    public void testRemoveFromUnderlying() {
        CompositeMap<K, V> map = new CompositeMap<K, V>(buildOne(), buildTwo());
        HashMap<K, V> three = new HashMap<K, V>();
        three.put((K) "5", (V) "five");
        map.addComposited(three);
        assertTrue(map.containsKey("5"));
        
        //Now remove "5"
        three.remove("5");
        assertFalse(map.containsKey("5"));
    }
    
    @SuppressWarnings("unchecked")
    public void testRemoveFromComposited() {
        CompositeMap<K, V> map = new CompositeMap<K, V>(buildOne(), buildTwo());
        HashMap<K, V> three = new HashMap<K, V>();
        three.put((K) "5", (V) "five");
        map.addComposited(three);
        assertTrue(map.containsKey("5"));
        
        //Now remove "5"
        map.remove("5");
        assertFalse(three.containsKey("5"));
    }
    
    public void testResolveCollision() {
        CompositeMap<K, V> map = new CompositeMap<K, V>(buildOne(), buildTwo(), 
            new CompositeMap.MapMutator<K, V>() {
            private static final long serialVersionUID = 1L;

            public void resolveCollision(CompositeMap<K, V> composite,
            Map<K, V> existing,
            Map<K, V> added,
            Collection<K> intersect) {
                pass = true;
            }
            
            public V put(CompositeMap<K, V> map, Map<K, V>[] composited, K key, 
                V value) {
                throw new UnsupportedOperationException();
            }
            
            public void putAll(CompositeMap<K, V> map, Map<K, V>[] composited, Map<? extends K, ? extends V> t) {
                throw new UnsupportedOperationException();
            }
        });
        
        map.addComposited(buildOne());
        assertTrue(pass);
    }
    
    @SuppressWarnings("unchecked")
    public void testPut() {
        CompositeMap<K, V> map = new CompositeMap<K, V>(buildOne(), buildTwo(), 
            new CompositeMap.MapMutator<K, V>() {
            private static final long serialVersionUID = 1L;
            public void resolveCollision(CompositeMap<K, V> composite,
            Map<K, V> existing,
            Map<K, V> added,
            Collection<K> intersect) {
                throw new UnsupportedOperationException();
            }
            
            public V put(CompositeMap<K, V> map, Map<K, V>[] composited, K key, 
                V value) {
                pass = true;
                return (V) "foo";
            }
            
            public void putAll(CompositeMap<K, V> map, Map<K, V>[] composited, Map<? extends K, ? extends V> t) {
                throw new UnsupportedOperationException();
            }
        });
        
        map.put((K) "willy", (V) "wonka");
        assertTrue(pass);
    }
    
    public void testPutAll() {
        CompositeMap<K, V> map = new CompositeMap<K, V>(buildOne(), buildTwo(), 
            new CompositeMap.MapMutator<K, V>() {
            private static final long serialVersionUID = 1L;
            public void resolveCollision(CompositeMap<K, V> composite,
            Map<K, V> existing,
            Map<K, V> added,
            Collection<K> intersect) {
                throw new UnsupportedOperationException();
            }
            
            public V put(CompositeMap<K, V> map, Map<K, V>[] composited, K key, 
                V value) {
                throw new UnsupportedOperationException();
            }
            
            public void putAll(CompositeMap<K, V> map, Map<K, V>[] composited, Map<? extends K, ? extends V> t) {
                pass = true;
            }
        });
        
        map.putAll(null);
        assertTrue(pass);
    }

    @Override
    public String getCompatibilityVersion() {
        return "3.3";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) map, "/tmp/CompositeMap.emptyCollection.version3.3.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) map, "/tmp/CompositeMap.fullCollection.version3.3.obj");
//    }

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10174.java