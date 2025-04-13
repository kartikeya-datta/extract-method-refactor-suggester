error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15099.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15099.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15099.java
text:
```scala
_@@repos = emf.getConfiguration().getMetaDataRepositoryInstance();

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
package org.apache.openjpa.persistence;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;

import org.apache.openjpa.datacache.DataCache;
import org.apache.openjpa.datacache.DelegatingDataCache;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.MetaDataRepository;

/**
 * Represents the L2 cache over the data store.
 *
 * @author Abe White
 * @since 4.0
 * @published
 */
public class StoreCache {

    public static final String NAME_DEFAULT = DataCache.NAME_DEFAULT;

    private final MetaDataRepository _repos;
    private final DelegatingDataCache _cache;

    /**
     * Constructor; supply delegate.
     */
    public StoreCache(EntityManagerFactoryImpl emf, DataCache cache) {
        _repos = emf.getConfiguration().getMetaDataRepository();
        _cache = new DelegatingDataCache(cache,
            PersistenceExceptions.TRANSLATOR);
    }

    /**
     * Delegate.
     */
    public DataCache getDelegate() {
        return _cache.getDelegate();
    }

    /**
     * Whether the cache contains data for the given oid.
     */
    public boolean contains(Class cls, Object oid) {
        return _cache.getDelegate() != null
            && _cache.contains(OpenJPAPersistence.toOpenJPAObjectId
            (getMetaData(cls), oid));
    }

    /**
     * Whether the cache contains data for the given oids.
     */
    public boolean containsAll(Class cls, Object... oids) {
        return containsAll(cls, Arrays.asList(oids));
    }

    /**
     * Whether the cache contains data for the given oids.
     */
    public boolean containsAll(Class cls, Collection oids) {
        if (_cache.getDelegate() == null)
            return oids.isEmpty();

        BitSet set = _cache.containsAll(OpenJPAPersistence.toOpenJPAObjectIds
            (getMetaData(cls), oids));
        for (int i = 0; i < oids.size(); i++)
            if (!set.get(i))
                return false;
        return true;
    }

    /**
     * Pin the data for the given oid to the cache.
     */
    public void pin(Class cls, Object oid) {
        if (_cache.getDelegate() != null)
            _cache.pin(
                OpenJPAPersistence.toOpenJPAObjectId(getMetaData(cls), oid));
    }

    /**
     * Pin the data for the given oids to the cache.
     */
    public void pinAll(Class cls, Object... oids) {
        pinAll(cls, Arrays.asList(oids));
    }

    /**
     * Pin the data for the given oids to the cache.
     */
    public void pinAll(Class cls, Collection oids) {
        if (_cache.getDelegate() != null)
            _cache
                .pinAll(OpenJPAPersistence.toOpenJPAObjectIds(getMetaData(cls),
                    oids));
    }

    /**
     * Unpin the data for the given oid from the cache.
     */
    public void unpin(Class cls, Object oid) {
        if (_cache.getDelegate() != null)
            _cache.unpin(OpenJPAPersistence.toOpenJPAObjectId(getMetaData(cls),
                oid));
    }

    /**
     * Unpin the data for the given oids from the cache.
     */
    public void unpinAll(Class cls, Object... oids) {
        unpinAll(cls, Arrays.asList(oids));
    }

    /**
     * Unpin the data for the given oids from the cache.
     */
    public void unpinAll(Class cls, Collection oids) {
        if (_cache.getDelegate() != null)
            _cache.unpinAll(
                OpenJPAPersistence.toOpenJPAObjectIds(getMetaData(cls),
                    oids));
    }

    /**
     * Remove data for the given oid from the cache.
     */
    public void evict(Class cls, Object oid) {
        if (_cache.getDelegate() != null)
            _cache.remove(OpenJPAPersistence.toOpenJPAObjectId(getMetaData(cls),
                oid));
    }

    /**
     * Remove data for the given oids from the cache.
     */
    public void evictAll(Class cls, Object... oids) {
        evictAll(cls, Arrays.asList(oids));
    }

    /**
     * Remove data for the given oids from the cache.
     */
    public void evictAll(Class cls, Collection oids) {
        if (_cache.getDelegate() != null)
            _cache.removeAll(
                OpenJPAPersistence.toOpenJPAObjectIds(getMetaData(cls),
                    oids));
    }

    /**
     * Clear the cache.
     */
    public void evictAll() {
        _cache.clear();
    }

    /**
     * Return metadata for the given class, throwing the proper exception
     * if not persistent.
     */
    private ClassMetaData getMetaData(Class cls) {
        try {
            return _repos.getMetaData(cls, null, true);
        } catch (RuntimeException re) {
            throw PersistenceExceptions.toPersistenceException(re);
        }
    }

    public int hashCode() {
        return _cache.hashCode();
    }

    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof StoreCache))
            return false;
        return _cache.equals (((StoreCache) other)._cache);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15099.java