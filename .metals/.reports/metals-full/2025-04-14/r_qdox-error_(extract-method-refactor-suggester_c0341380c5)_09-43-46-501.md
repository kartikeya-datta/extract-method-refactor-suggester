error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1603.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1603.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1603.java
text:
```scala
r@@eturn new MemoryMapState(_id + partitionIndex);

package storm.trident.testing;

import backtype.storm.task.IMetricsContext;
import storm.trident.state.ITupleCollection;
import backtype.storm.tuple.Values;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import storm.trident.state.OpaqueValue;
import storm.trident.state.State;
import storm.trident.state.StateFactory;
import storm.trident.state.ValueUpdater;
import storm.trident.state.map.*;
import storm.trident.state.snapshot.Snapshottable;

public class MemoryMapState<T> implements Snapshottable<T>, ITupleCollection, MapState<T> {

    MemoryMapStateBacking<OpaqueValue> _backing;
    SnapshottableMap<T> _delegate;

    public MemoryMapState(String id) {
        _backing = new MemoryMapStateBacking(id);
        _delegate = new SnapshottableMap(OpaqueMap.build(_backing), new Values("$MEMORY-MAP-STATE-GLOBAL$"));
    }

    public T update(ValueUpdater updater) {
        return _delegate.update(updater);
    }

    public void set(T o) {
        _delegate.set(o);
    }

    public T get() {
        return _delegate.get();
    }

    public void beginCommit(Long txid) {
        _delegate.beginCommit(txid);
    }

    public void commit(Long txid) {
        _delegate.commit(txid);
    }

    public Iterator<List<Object>> getTuples() {
        return _backing.getTuples();
    }

    public List<T> multiUpdate(List<List<Object>> keys, List<ValueUpdater> updaters) {
        return _delegate.multiUpdate(keys, updaters);
    }

    public void multiPut(List<List<Object>> keys, List<T> vals) {
        _delegate.multiPut(keys, vals);
    }

    public List<T> multiGet(List<List<Object>> keys) {
        return _delegate.multiGet(keys);
    }

    public static class Factory implements StateFactory {

        String _id;

        public Factory() {
            _id = UUID.randomUUID().toString();
        }

        @Override
        public State makeState(Map conf, IMetricsContext metrics, int partitionIndex, int numPartitions) {
            return new MemoryMapState(_id);
        }
    }

    static ConcurrentHashMap<String, Map<List<Object>, Object>> _dbs = new ConcurrentHashMap<String, Map<List<Object>, Object>>();
    static class MemoryMapStateBacking<T> implements IBackingMap<T>, ITupleCollection {

        public static void clearAll() {
            _dbs.clear();
        }
        Map<List<Object>, T> db;
        Long currTx;

        public MemoryMapStateBacking(String id) {
            if (!_dbs.containsKey(id)) {
                _dbs.put(id, new HashMap());
            }
            this.db = (Map<List<Object>, T>) _dbs.get(id);
        }

        @Override
        public List<T> multiGet(List<List<Object>> keys) {
            List<T> ret = new ArrayList();
            for (List<Object> key : keys) {
                ret.add(db.get(key));
            }
            return ret;
        }

        @Override
        public void multiPut(List<List<Object>> keys, List<T> vals) {
            for (int i = 0; i < keys.size(); i++) {
                List<Object> key = keys.get(i);
                T val = vals.get(i);
                db.put(key, val);
            }
        }

        @Override
        public Iterator<List<Object>> getTuples() {
            return new Iterator<List<Object>>() {

                private Iterator<Map.Entry<List<Object>, T>> it = db.entrySet().iterator();

                public boolean hasNext() {
                    return it.hasNext();
                }

                public List<Object> next() {
                    Map.Entry<List<Object>, T> e = it.next();
                    List<Object> ret = new ArrayList<Object>();
                    ret.addAll(e.getKey());
                    ret.add(((OpaqueValue)e.getValue()).getCurr());
                    return ret;
                }

                public void remove() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1603.java