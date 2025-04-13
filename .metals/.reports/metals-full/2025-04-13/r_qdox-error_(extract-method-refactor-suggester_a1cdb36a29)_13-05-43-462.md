error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15089.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15089.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15089.java
text:
```scala
g@@etMetaDataRepositoryInstance();

/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.openjpa.kernel;

import org.apache.openjpa.lib.rop.ResultObjectProvider;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.MetaDataRepository;
import org.apache.openjpa.util.ObjectNotFoundException;
import org.apache.openjpa.util.StoreException;

/**
 * Abstract implementation of {@link PCResultObjectProvider}
 * that implements {@link ResultObjectProvider#getResultObject}
 * by assembling the necessary information about the object to be loaded.
 *
 * @author Patrick Linskey
 */
public abstract class AbstractPCResultObjectProvider
    implements PCResultObjectProvider {

    /**
     * The {@link StoreContext} that this result object
     * provider will load objects into.
     */
    protected final StoreContext ctx;

    /**
     * Create a new provider for loading PC objects from the input
     * into <code>ctx</code>.
     */
    public AbstractPCResultObjectProvider(StoreContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Return the context this provider was constructed with.
     */
    public StoreContext getContext() {
        return ctx;
    }

    public void initialize(OpenJPAStateManager sm, PCState state,
        FetchState fetchState)
        throws Exception {
        sm.initialize(getPCType(), state);
        load(sm, fetchState);
    }

    public Object getResultObject()
        throws Exception {
        Class type = getPCType();
        MetaDataRepository repos = ctx.getConfiguration().
            getMetaDataRepository();
        ClassMetaData meta = repos.getMetaData
            (type, ctx.getClassLoader(), true);

        Object oid = getObjectId(meta);
        Object res = ctx.find(oid, null, null, this, 0);
        if (res == null)
            throw new ObjectNotFoundException(oid);
        return res;
    }

    /**
     * Implement this method to extract the object id value from the
     * current record of the input.
     */
    protected abstract Object getObjectId(ClassMetaData meta)
        throws Exception;

    /**
     * Implement this method to extract the type of the pc stored
     * in the current record of the input.
     */
    protected abstract Class getPCType()
        throws Exception;

    /**
     * Load data from the current input record into the given state
     * manager. Remember to call {@link OpenJPAStateManager#setVersion} to set
     * the optimistic versioning information, if it has any.
     */
    protected abstract void load(OpenJPAStateManager sm, FetchState fetch)
        throws Exception;

    /**
     * Override if desired. Does nothing by default.
     */
    public void open()
        throws Exception {
    }

    /**
     * Override if desired. Returns false by default.
     *
     * @see ResultObjectProvider#supportsRandomAccess
     */
    public boolean supportsRandomAccess() {
        return false;
    }

    /**
     * Implement this method to advance the input.
     *
     * @see ResultObjectProvider#next
     */
    public abstract boolean next()
        throws Exception;

    /**
     * Override if desired. Throws an exception by default.
     *
     * @see ResultObjectProvider#absolute
     */
    public boolean absolute(int pos)
        throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * Override if desired. Returns {@link Integer#MAX_VALUE} by default.
     *
     * @see ResultObjectProvider#size
     */
    public int size()
        throws Exception {
        return Integer.MAX_VALUE;
    }

    /**
     * Override if desired. Throws an exception by default.
     *
     * @see ResultObjectProvider#reset
     */
    public void reset()
        throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * Override if desired. Does nothing by default.
     *
     * @see ResultObjectProvider#close
     */
    public void close()
        throws Exception {
    }

    /**
     * Throws a {@link StoreException} by default.
     */
    public void handleCheckedException(Exception e) {
        throw new StoreException (e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15089.java