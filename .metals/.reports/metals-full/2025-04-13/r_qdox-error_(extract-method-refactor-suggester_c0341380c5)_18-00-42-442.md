error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8793.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8793.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8793.java
text:
```scala
e@@vents.add(new ResourceChangeEvent(path, ResourceChangeEvent.Type.valueOf(change.getType().name())));

package org.wildfly.extension.undertow.deployment;

import io.undertow.UndertowLogger;
import io.undertow.UndertowMessages;
import io.undertow.server.handlers.resource.Resource;
import io.undertow.server.handlers.resource.ResourceChangeEvent;
import io.undertow.server.handlers.resource.ResourceChangeListener;
import io.undertow.server.handlers.resource.ResourceManager;
import org.jboss.vfs.VirtualFile;
import org.xnio.FileChangeCallback;
import org.xnio.FileChangeEvent;
import org.xnio.FileSystemWatcher;
import org.xnio.IoUtils;
import org.xnio.OptionMap;
import org.xnio.Xnio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @author Stuart Douglas
 */
public class VirtualFileResourceManager implements ResourceManager {

    private volatile VirtualFile base;
    private final List<ResourceChangeListener> listeners = new ArrayList<ResourceChangeListener>();
    private FileSystemWatcher fileSystemWatcher;

    /**
     * Size to use direct FS to network transfer (if supported by OS/JDK) instead of read/write
     */
    private final long transferMinSize;

    public VirtualFileResourceManager(final VirtualFile base, long transferMinSize) {
        if (base == null) {
            throw UndertowMessages.MESSAGES.argumentCannotBeNull("base");
        }
        this.base = base;
        this.transferMinSize = transferMinSize;
    }

    public VirtualFile getBase() {
        return base;
    }

    public VirtualFileResourceManager setBase(final VirtualFile base) {
        if (base == null) {
            throw UndertowMessages.MESSAGES.argumentCannotBeNull("base");
        }
        this.base = base;
        return this;
    }

    public Resource getResource(final String p) {
        String path = p;
        if (p.startsWith("/")) {
            path = p.substring(1);
        }
        try {
            VirtualFile file = base.getChild(p);
            if (file.exists()) {
                return new VirtualFileResource(file.getPhysicalFile(), this, path);
            } else {
                return null;
            }
        } catch (Exception e) {
            UndertowLogger.REQUEST_LOGGER.debugf(e, "Invalid path %s");
            return null;
        }
    }

    @Override
    public boolean isResourceChangeListenerSupported() {
        return false;
    }

    @Override
    public synchronized void registerResourceChangeListener(ResourceChangeListener listener) {
        listeners.add(listener);
        if (fileSystemWatcher == null) {
            fileSystemWatcher = Xnio.getInstance().createFileSystemWatcher("Watcher for " + base, OptionMap.EMPTY);
            try {
                final File base = this.base.getPhysicalFile();
                fileSystemWatcher.watchPath(base, new FileChangeCallback() {
                    @Override
                    public void handleChanges(Collection<FileChangeEvent> changes) {
                        synchronized (VirtualFileResourceManager.this) {
                            String basePath = base.getAbsolutePath();
                            final List<ResourceChangeEvent> events = new ArrayList<ResourceChangeEvent>();
                            for (FileChangeEvent change : changes) {
                                if (change.getFile().getAbsolutePath().startsWith(basePath)) {
                                    String path = change.getFile().getAbsolutePath().substring(basePath.length());
                                    events.add(new ResourceChangeEvent(getResource(path), ResourceChangeEvent.Type.valueOf(change.getType().name())));
                                }
                            }
                            for (ResourceChangeListener listener : listeners) {
                                listener.handleChanges(events);
                            }
                        }
                    }
                });
            } catch (IOException e) {

            }
        }
    }


    @Override
    public synchronized void removeResourceChangeListener(ResourceChangeListener listener) {
        listeners.remove(listener);
    }

    public long getTransferMinSize() {
        return transferMinSize;
    }

    @Override
    public void close() throws IOException {
        IoUtils.safeClose(fileSystemWatcher);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8793.java