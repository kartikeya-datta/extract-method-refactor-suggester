error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/367.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/367.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/367.java
text:
```scala
i@@nput = new ObjectInputStream(new GZIPInputStream(new FileInputStream(dir.getFD()), 8192 * 8));

package com.github.mobile.android;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.zip.GZIPInputStream;

/**
 * Reader of previously fetched request data
 */
public class RequestReader {

    private static final String TAG = "GHRR";

    private final File handle;

    private final int version;

    /**
     * Create request reader
     *
     * @param file
     * @param formatVersion
     */
    public RequestReader(File file, int formatVersion) {
        handle = file;
        version = formatVersion;
    }

    /**
     * Read request data
     *
     * @return read data
     */
    @SuppressWarnings("unchecked")
    public <V> V read() {
        if (!handle.exists() || handle.length() == 0)
            return null;

        RandomAccessFile dir = null;
        FileLock lock = null;
        ObjectInputStream input = null;
        boolean delete = false;
        try {
            dir = new RandomAccessFile(handle, "rw");
            lock = dir.getChannel().lock();
            input = new ObjectInputStream(new GZIPInputStream(new FileInputStream(dir.getFD()), 8192));
            int streamVersion = input.readInt();
            if (streamVersion != version) {
                delete = true;
                return null;
            }
            return (V) input.readObject();
        } catch (IOException e) {
            Log.d(TAG, "Exception reading cache " + handle.getName(), e);
            return null;
        } catch (ClassNotFoundException e) {
            Log.d(TAG, "Exception reading cache " + handle.getName(), e);
            return null;
        } finally {
            if (input != null)
                try {
                    input.close();
                } catch (IOException e) {
                    Log.d(TAG, "Exception closing stream", e);
                }
            if (delete)
                try {
                    dir.setLength(0);
                } catch (IOException e) {
                    Log.d(TAG, "Exception truncating file", e);
                }
            if (lock != null)
                try {
                    lock.release();
                } catch (IOException e) {
                    Log.d(TAG, "Exception unlocking file", e);
                }
            if (dir != null)
                try {
                    dir.close();
                } catch (IOException e) {
                    Log.d(TAG, "Exception closing file", e);
                }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/367.java