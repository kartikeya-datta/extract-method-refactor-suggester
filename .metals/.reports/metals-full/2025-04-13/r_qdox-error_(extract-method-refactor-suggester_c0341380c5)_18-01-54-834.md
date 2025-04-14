error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10033.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10033.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[21,1]

error in qdox parser
file content:
```java
offset: 873
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10033.java
text:
```scala
private static class Entry<K,V> implements Map.Entry<K, V> {

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
p@@ackage org.apache.openjpa.jdbc.kernel.exps;

import java.sql.SQLException;
import java.util.Map;

import org.apache.openjpa.jdbc.sql.Result;
import org.apache.openjpa.jdbc.sql.SQLBuffer;
import org.apache.openjpa.jdbc.sql.Select;
import org.apache.openjpa.meta.ClassMetaData;

/**
 * Returns the Map.Entry<K,V> of a map value.
 *
 * @author Catalina Wei
 * @since 2.0.0
 */
public class MapEntry
    extends AbstractVal {

    private final Val _key;
    private final Val _val;
    private ClassMetaData _meta = null;
    private Class _cast = null;
    private Class _type = null;

    /**
     * Constructor. Provide the map value to operate on.
     */
    public MapEntry(Val key, Val val) {
        ((PCPath) key).getKey();
        _key = key;
        _val = val;
    }

    /**
     * Expression state.
     */
    public static class EntryExpState
        extends ExpState {
        public ExpState key;
        public ExpState val;
    
        EntryExpState(ExpState key, ExpState val) {
            this.key = key;
            this.val = val;
        }
    }

    public void appendTo(Select sel, ExpContext ctx, ExpState state,
        SQLBuffer sql, int index) {
    }

    public void calculateValue(Select sel, ExpContext ctx, ExpState state,
        Val other, ExpState otherState) {
        _val.calculateValue(sel, ctx, state, other, otherState);
        _key.calculateValue(sel, ctx, state, other, otherState);
    }

    public void groupBy(Select sel, ExpContext ctx, ExpState state) {
    }

    public ExpState initialize(Select sel, ExpContext ctx, int flags) {
        ExpState val = _val.initialize(sel, ctx, flags);
        ExpState key = _key.initialize(sel, ctx, flags);
        return new EntryExpState(key, val);
    }

    public int length(Select sel, ExpContext ctx, ExpState state) {
        return 1;
    }

    public Object load(ExpContext ctx, ExpState state, Result res)
        throws SQLException {
        EntryExpState estate = (EntryExpState) state;
        Object key = _key.load(ctx, estate.key, res);
        Object val = _val.load(ctx, estate.val, res);
        if (key == null || val == null)
            return null;
        return new Entry(key, val);
    }

    public void orderBy(Select sel, ExpContext ctx, ExpState state, boolean asc)
    {
    }

    public void select(Select sel, ExpContext ctx, ExpState state, boolean pks)
    {
        selectColumns(sel, ctx, state, pks);
    }

    public void selectColumns(Select sel, ExpContext ctx, ExpState state,
        boolean pks) {
        EntryExpState estate = (EntryExpState) state;
        _key.selectColumns(sel, ctx, estate.key, pks);
        _val.selectColumns(sel, ctx, estate.val, pks);
    }

    public ClassMetaData getMetaData() {
        return _meta;
    }

    public Class getType() {
        return Map.Entry.class;
    }

    public void setImplicitType(Class type) {
    }

    public void setMetaData(ClassMetaData meta) {
        _meta = meta;        
    }

    private class Entry<K,V> implements Map.Entry<K, V> {
        private final K key;
        private final V value;

        public Entry(K k, V v) {
            key = k;
            value = v;
        }
        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V v) {
            throw new UnsupportedOperationException();
        }

        public boolean equals(Object other) {
            if (other instanceof Map.Entry == false)
                return false;
            Map.Entry that = (Map.Entry)other;
            return (this.key == null ?
                that.getKey() == null : key.equals(that.getKey())) &&
                (value == null ?
                that.getValue() == null : value.equals(that.getValue()));
        }

        public int hashCode() {
            return  (key == null   ? 0 : key.hashCode()) ^
            (value == null ? 0 : value.hashCode());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10033.java