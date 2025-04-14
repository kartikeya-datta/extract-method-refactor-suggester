error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12647.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12647.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12647.java
text:
```scala
public v@@oid cleanupBefore(BigInteger txid) {

package backtype.storm.transactional.state;

import backtype.storm.transactional.TransactionalSpoutCoordinator;
import java.math.BigInteger;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A map from txid to a value. Automatically deletes txids that have been committed. 
 */
public class RotatingTransactionalState {
    public static interface StateInitializer {
        Object init(BigInteger txid, Object lastState);
    }
    
    private static final BigInteger ONE = new BigInteger("1");

    private TransactionalState _state;
    private String _subdir;
    private boolean _strictOrder;
    
    private TreeMap<BigInteger, Object> _curr = new TreeMap<BigInteger, Object>();
    
    public RotatingTransactionalState(TransactionalState state, String subdir, boolean strictOrder) {
        _state = state;
        _subdir = subdir;
        _strictOrder = strictOrder;
        state.mkdir(subdir);
        sync();
    }

    public RotatingTransactionalState(TransactionalState state, String subdir) {
        this(state, subdir, false);
    }    
    
    public Object getState(BigInteger txid, StateInitializer init) {
        if(!_curr.containsKey(txid)) {
            SortedMap<BigInteger, Object> prevMap = _curr.headMap(txid);
            SortedMap<BigInteger, Object> afterMap = _curr.tailMap(txid);            
            
            BigInteger prev = null;
            if(!prevMap.isEmpty()) prev = prevMap.lastKey();
            
            if(prev==null && !txid.equals(TransactionalSpoutCoordinator.INIT_TXID)) {
                throw new IllegalStateException("Trying to initialize transaction for which there should be a previous state");
            }
            if(_strictOrder && prev!=null && !prev.equals(txid.subtract(ONE))) {
                throw new IllegalStateException("Expecting previous txid state to be the previous transaction");
            }
            if(_strictOrder && !afterMap.isEmpty()) {
                throw new IllegalStateException("Expecting tx state to be initialized in strict order but there are txids after that have state");                
            }
            
            
            Object data;
            if(afterMap.isEmpty()) {
                Object prevData;
                if(prev!=null) {
                    prevData = _curr.get(prev);
                } else {
                    prevData = null;
                }
                data = init.init(txid, prevData);
            } else {
                data = null;
            }
            _curr.put(txid, data);
            _state.setData(txPath(txid), data);
        }
        return _curr.get(txid);
    }
    
    /**
     * Returns null if it was created, the value otherwise.
     */
    public Object getStateOrCreate(BigInteger txid, StateInitializer init) {
        if(_curr.containsKey(txid)) {
            return _curr.get(txid);
        } else {
            getState(txid, init);
            return null;
        }
    }
    
    public void commit(BigInteger txid) {
        SortedMap<BigInteger, Object> toDelete = _curr.headMap(txid);
        for(BigInteger tx: toDelete.keySet()) {
            _curr.remove(tx);
            _state.delete(txPath(tx));
        }
    }
    
    private void sync() {
        List<String> txids = _state.list(_subdir);
        for(String txid_s: txids) {
            Object data = _state.getData(txPath(txid_s));
            _curr.put(new BigInteger(txid_s), data);
        }
    }
    
    private String txPath(BigInteger tx) {
        return txPath(tx.toString());
    }

    private String txPath(String tx) {
        return _subdir + "/" + tx;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12647.java