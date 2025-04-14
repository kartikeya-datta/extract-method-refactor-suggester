error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14531.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14531.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14531.java
text:
```scala
r@@eturn new String[] { "1", "3", "5", "7", "2", "4", "6" };

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
package org.apache.commons.collections.collection;

import static java.util.Arrays.asList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.collection.IndexedCollection;
import org.junit.Test;

/**
 * Extension of {@link AbstractCollectionTest} for exercising the 
 * {@link IndexedCollection} implementation.
 *
 * @since 4.0
 * @version $Id$
 */
@SuppressWarnings("boxing")
public class IndexedCollectionTest extends AbstractCollectionTest<String> {

    public IndexedCollectionTest(String name) {
        super(name);
    }

   //------------------------------------------------------------------------

    protected Collection<String> decorateCollection(Collection<String> collection) {
        return IndexedCollection.uniqueIndexedCollection(collection, new IntegerTransformer());
    }

    private static final class IntegerTransformer implements Transformer<String, Integer>, Serializable {
        private static final long serialVersionUID = 809439581555072949L;

        public Integer transform(String input) {
            return Integer.valueOf(input);
        }
    }
    
    @Override
    public Collection<String> makeObject() {
        return decorateCollection(new ArrayList<String>());
    }

    @Override
    public Collection<String> makeConfirmedCollection() {
        return new ArrayList<String>();
    }

    @Override
    public String[] getFullElements() {
        return (String[]) new String[] { "1", "3", "5", "7", "2", "4", "6" };
    }

    @Override
    public String[] getOtherElements() {
        return new String[] {"9", "88", "678", "87", "98", "78", "99"};
    }

    @Override
    public Collection<String> makeFullCollection() {
        List<String> list = new ArrayList<String>();
        list.addAll(Arrays.asList(getFullElements()));
        return decorateCollection(list);
    }

    @Override
    public Collection<String> makeConfirmedFullCollection() {
        List<String> list = new ArrayList<String>();
        list.addAll(Arrays.asList(getFullElements()));
        return list;
    }

    @Override
    protected boolean skipSerializedCanonicalTests() {
        // FIXME: support canonical tests
        return true;
    }

    //------------------------------------------------------------------------

    @Override
    public void testCollectionAddAll() {
        // FIXME: does not work as we do not support multi-keys yet
    }

    @Test
    public void addedObjectsCanBeRetrievedByKey() throws Exception {
        Collection<String> coll = getCollection();
        coll.add("12");
        coll.add("16");
        coll.add("1");
        coll.addAll(asList("2","3","4"));
        
        @SuppressWarnings("unchecked")
        IndexedCollection<Integer, String> indexed = (IndexedCollection<Integer, String>) coll;
        assertEquals("12", indexed.get(12));
        assertEquals("16", indexed.get(16));
        assertEquals("1", indexed.get(1));
        assertEquals("2", indexed.get(2));
        assertEquals("3", indexed.get(3));
        assertEquals("4", indexed.get(4));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void ensureDuplicateObjectsCauseException() throws Exception {
        getCollection().add("1");
        getCollection().add("1");
    }
    
//    @Test
//    public void decoratedCollectionIsIndexedOnCreation() throws Exception {
//        original.add("1");
//        original.add("2");
//        original.add("3");
//        
//        indexed = IndexedCollection.uniqueIndexedCollection(original, new Transformer<String, Integer>() {
//            public Integer transform(String input) {
//                return Integer.parseInt(input);
//            }
//        });
//        assertEquals("1", indexed.get(1));
//        assertEquals("2", indexed.get(2));
//        assertEquals("3", indexed.get(3));
//    }
//    
//    @Test
//    public void reindexUpdatesIndexWhenTheDecoratedCollectionIsModifiedSeparately() throws Exception {
//        original.add("1");
//        original.add("2");
//        original.add("3");
//        
//        assertNull(indexed.get(1));
//        assertNull(indexed.get(2));
//        assertNull(indexed.get(3));
//        indexed.reindex();
//        assertEquals("1", indexed.get(1));
//        assertEquals("2", indexed.get(2));
//        assertEquals("3", indexed.get(3));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14531.java