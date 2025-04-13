error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/115.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/115.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,25]

error in qdox parser
file content:
```java
offset: 25
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/115.java
text:
```scala
private static volatile M@@ap<Thread, AtomicLong> threadSleepTimes;

package backtype.storm.utils;

import java.util.concurrent.atomic.AtomicLong;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.log4j.Logger;


public class Time {
    public static Logger LOG = Logger.getLogger(Time.class);    
    
    private static AtomicBoolean simulating = new AtomicBoolean(false);
    //TODO: should probably use weak references here or something
    private static Map<Thread, AtomicLong> threadSleepTimes;
    private static final Object sleepTimesLock = new Object();
    
    private static AtomicLong simulatedCurrTimeMs; //should this be a thread local that's allowed to keep advancing?
    
    public static void startSimulating() {
        simulating.set(true);
        simulatedCurrTimeMs = new AtomicLong(0);
        threadSleepTimes = new ConcurrentHashMap<Thread, AtomicLong>();
    }
    
    public static void stopSimulating() {
        simulating.set(false);             
        threadSleepTimes = null;  
    }
    
    public static boolean isSimulating() {
        return simulating.get();
    }
    
    public static void sleepUntil(long targetTimeMs) throws InterruptedException {
        if(simulating.get()) {
            try {
                synchronized(sleepTimesLock) {
                    threadSleepTimes.put(Thread.currentThread(), new AtomicLong(targetTimeMs));
                }
                while(simulatedCurrTimeMs.get() < targetTimeMs) {
                    Thread.sleep(10);
                }
            } finally {
                synchronized(sleepTimesLock) {
                    threadSleepTimes.remove(Thread.currentThread());
                }
            }
        } else {
            long sleepTime = targetTimeMs-currentTimeMillis();
            if(sleepTime>0) 
                Thread.sleep(sleepTime);
        }
    }
    
    public static void sleep(long ms) throws InterruptedException {
        sleepUntil(currentTimeMillis()+ms);
    }
    
    public static long currentTimeMillis() {
        if(simulating.get()) {
            return simulatedCurrTimeMs.get();
        } else {
            return System.currentTimeMillis();
        }
    }
    
    public static int currentTimeSecs() {
        return (int) (currentTimeMillis() / 1000);
    }
    
    public static void advanceTime(long ms) {
        if(!simulating.get()) throw new IllegalStateException("Cannot simulate time unless in simulation mode");
        simulatedCurrTimeMs.set(simulatedCurrTimeMs.get() + ms);
    }
    
    public static boolean isThreadWaiting(Thread t) {
        if(!simulating.get()) throw new IllegalStateException("Must be in simulation mode");
        AtomicLong time;
        synchronized(sleepTimesLock) {
            time = threadSleepTimes.get(t);
        }
        return !t.isAlive() || time!=null && currentTimeMillis() < time.longValue();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/115.java