error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13448.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13448.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,1]

error in qdox parser
file content:
```java
offset: 82
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13448.java
text:
```scala
public static class ObjectEventFactory implements EventFactory<MutableObject> {

p@@ackage backtype.storm.utils;

import com.lmax.disruptor.ClaimStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class allows publishers to start publishing before consumer is set up,
 * making it much easier to structure code around the disruptor.
 */
public class DisruptorQueue {
    private static final Object MARKER = new Object();
    private static final Object HALT_PROCESSING = new Object();
    Disruptor _disruptor;
    ExecutorService _executor;
    WaiterEventHandler _handler;
    RingBuffer<MutableObject> _buffer;
    
    
    public DisruptorQueue(ClaimStrategy claim, WaitStrategy wait) {
        _executor = Executors.newCachedThreadPool();
        _disruptor = new Disruptor(new ObjectEventFactory(), _executor, claim, wait);
        _handler = new WaiterEventHandler();
        _disruptor.handleEventsWith(_handler);
        _buffer = _disruptor.start();
    }
    
    public void setHandler(EventHandler handler) {
        _handler.setHandler(handler);
        publish(MARKER);
    }
    
    public void publish(Object o) {
        final long id = _buffer.next();
        final MutableObject m = _buffer.get(id);
        m.setObject(o);
        _buffer.publish(id);
    }
    
    public void haltProcessing() {
        publish(HALT_PROCESSING);
    }
    
    public void shutdown() {
        _disruptor.shutdown();
        _executor.shutdownNow();
    }
    
    static class WaiterEventHandler implements EventHandler<MutableObject> {
        AtomicBoolean started = new AtomicBoolean(false);
        volatile EventHandler _promise = null;
        EventHandler _cached = null;
        List<CachedEvent> _toHandle = new ArrayList<CachedEvent>();
        boolean halted = false;
        
        public void setHandler(EventHandler handler) {
            if(_promise!=null) {
                throw new RuntimeException("Cannot set event handler more than once");
            }
            _promise = handler;
        }
        
        @Override
        public void onEvent(MutableObject t, long sequence, boolean isBatchEnd) throws Exception {
            Object o = t.getObject();
            if(o==HALT_PROCESSING) {
                _cached = null;
                halted = true;
            }
            if(_cached!=null && o != MARKER) {
                handleEvent(o, sequence, isBatchEnd);
            } else {
                if(!halted) {
                    if(_promise!=null) {
                        _cached = _promise;
                        for(CachedEvent e: _toHandle) {
                            handleEvent(e.o, e.seq, e.isBatchEnd);
                        }
                        _toHandle.clear();
                        if(o != MARKER) handleEvent(o, sequence, isBatchEnd);
                    } else {
                        if(o==MARKER) {
                            throw new RuntimeException("Got marker object before promise was set");
                        }
                        _toHandle.add(new CachedEvent(o, sequence, isBatchEnd));
                    }
                }
            }
        }
        
        private void handleEvent(Object o, long sequence, boolean isBatchEnd) throws Exception {
            _cached.onEvent(o, sequence, isBatchEnd);
        }
        
    }
    
    static class CachedEvent {
        public Object o;
        public long seq;
        public boolean isBatchEnd;
        
        public CachedEvent(Object o, long seq, boolean isBatchEnd) {
            this.o = o;
            this.seq = seq;
            this.isBatchEnd = isBatchEnd;
        }
    }

    static class ObjectEventFactory implements EventFactory<MutableObject> {
        @Override
        public MutableObject newInstance() {
            return new MutableObject();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13448.java