error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15094.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15094.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15094.java
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.FilterIterator;
import org.apache.commons.collections.iterators.IteratorChain;
import org.apache.openjpa.lib.rop.ResultObjectProvider;
import org.apache.openjpa.lib.rop.ResultObjectProviderIterator;
import org.apache.openjpa.lib.util.Closeable;
import org.apache.openjpa.lib.util.ReferenceHashSet;
import org.apache.openjpa.lib.util.concurrent.ReentrantLock;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.MetaDataRepository;
import org.apache.openjpa.util.GeneralException;
import org.apache.openjpa.util.ImplHelper;
import org.apache.openjpa.util.OpenJPAException;

/**
 * Representation of all members of a persistent class.
 *
 * @author Abe White
 * @author Patrick Linskey
 * @nojavadoc
 */
public class ExtentImpl
    implements Extent {

    private static final ClassMetaData[] EMPTY_METAS = new ClassMetaData[0];

    private final Broker _broker;
    private final Class _type;
    private final boolean _subs;
    private final FetchConfiguration _fc;
    private final ReentrantLock _lock;
    private boolean _ignore = false;

    // set of open iterators
    private ReferenceHashSet _openItrs = null;

    /**
     * Constructor.
     *
     * @param broker the owning broker
     * @param type the candidate class
     * @param subs whether subclasses are included in the extent
     */
    ExtentImpl(Broker broker, Class type, boolean subs,
        FetchConfiguration fetch) {
        _broker = broker;
        _type = type;
        _subs = subs;
        if (fetch != null)
            _fc = fetch;
        else
            _fc = (FetchConfiguration) broker.getFetchConfiguration().clone();
        _ignore = broker.getIgnoreChanges();
        if (broker.getMultithreaded())
            _lock = new ReentrantLock();
        else
            _lock = null;
    }

    public FetchConfiguration getFetchConfiguration() {
        return _fc;
    }

    public boolean getIgnoreChanges() {
        return _ignore;
    }

    public void setIgnoreChanges(boolean ignoreChanges) {
        _ignore = ignoreChanges;
    }

    public List list() {
        List list = new ArrayList();
        Iterator itr = iterator();
        try {
            while (itr.hasNext())
                list.add(itr.next());
            return list;
        } finally {
            ImplHelper.close(itr);
        }
    }

    public Iterator iterator() {
        _broker.assertNontransactionalRead();
        CloseableIterator citr = null;
        try {
            // create an iterator chain; add pnew objects if transactional
            CloseableIteratorChain chain = new CloseableIteratorChain();
            boolean trans = !_ignore && _broker.isActive();
            if (trans)
                chain.addIterator(new FilterNewIterator());

            // add database iterators for each implementing class
            MetaDataRepository repos = _broker.getConfiguration().
                getMetaDataRepository();
            ClassMetaData meta = repos.getMetaData(_type,
                _broker.getClassLoader(), false);

            ClassMetaData[] metas;
            if (meta != null && (meta.isMapped() || (_subs
                && meta.getMappedPCSubclassMetaDatas().length > 0)))
                metas = new ClassMetaData[]{ meta };
            else if (meta == null && _subs)
                metas = repos.getImplementorMetaDatas(_type,
                    _broker.getClassLoader(), false);
            else
                metas = EMPTY_METAS;

            ResultObjectProvider rop;
            for (int i = 0; i < metas.length; i++) {
                rop = _broker.getStoreManager().executeExtent(metas[i],
                    _subs, _fc);
                if (rop != null)
                    chain.addIterator(new ResultObjectProviderIterator(rop));
            }

            // filter deleted objects if transactional
            if (trans)
                citr = new FilterDeletedIterator(chain);
            else
                citr = chain;
            citr.setRemoveOnClose(this);
        } catch (OpenJPAException ke) {
            throw ke;
        } catch (RuntimeException re) {
            throw new GeneralException(re);
        }

        lock();
        try {
            if (_openItrs == null)
                _openItrs = new ReferenceHashSet(ReferenceHashSet.WEAK);
            _openItrs.add(citr);
        } finally {
            unlock();
        }
        return citr;
    }

    public Broker getBroker() {
        return _broker;
    }

    public Class getElementType() {
        return _type;
    }

    public boolean hasSubclasses() {
        return _subs;
    }

    public void closeAll() {
        if (_openItrs == null)
            return;

        lock();
        try {
            CloseableIterator citr;
            for (Iterator itr = _openItrs.iterator(); itr.hasNext();) {
                citr = (CloseableIterator) itr.next();
                citr.setRemoveOnClose(null);
                try {
                    citr.close();
                } catch (Exception e) {
                }
            }
            _openItrs.clear();
        } catch (OpenJPAException ke) {
            throw ke;
        } catch (RuntimeException re) {
            throw new GeneralException(re);
        } finally {
            unlock();
        }
    }

    public void lock() {
        if (_lock != null)
            _lock.lock();
    }

    public void unlock() {
        if (_lock != null)
            _lock.unlock();
    }

    /**
     * Closeable iterator.
     */
    private static interface CloseableIterator
        extends Closeable, Iterator {

        /**
         * Set the extent to remove self from on close.
         */
        public void setRemoveOnClose(ExtentImpl extent);
    }

    /**
     * Closeable {@link IteratorChain}.
     */
    private static class CloseableIteratorChain
        extends IteratorChain
        implements CloseableIterator {

        private ExtentImpl _extent = null;
        private boolean _closed = false;

        public boolean hasNext() {
            return (_closed) ? false : super.hasNext();
        }

        public Object next() {
            if (_closed)
                throw new NoSuchElementException();
            return super.next();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void setRemoveOnClose(ExtentImpl extent) {
            _extent = extent;
        }

        public void close()
            throws Exception {
            if (_extent != null && _extent._openItrs != null) {
                _extent.lock();
                try {
                    _extent._openItrs.remove(this);
                } finally {
                    _extent.unlock();
                }
            }

            _closed = true;
            for (Iterator itr = getIterators().iterator(); itr.hasNext();)
                ((Closeable) itr.next()).close();
        }
    }

    /**
     * {@link FilterIterator} that skips deleted objects.
     */
    private static class FilterDeletedIterator
        extends FilterIterator
        implements CloseableIterator, Predicate {

        private ExtentImpl _extent = null;
        private boolean _closed = false;

        public FilterDeletedIterator(Iterator itr) {
            super(itr);
            setPredicate(this);
        }

        public boolean hasNext() {
            return (_closed) ? false : super.hasNext();
        }

        public Object next() {
            if (_closed)
                throw new NoSuchElementException();
            return super.next();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void setRemoveOnClose(ExtentImpl extent) {
            _extent = extent;
        }

        public void close()
            throws Exception {
            if (_extent != null && _extent._openItrs != null) {
                _extent.lock();
                try {
                    _extent._openItrs.remove(this);
                } finally {
                    _extent.unlock();
                }
            }

            _closed = true;
            ((Closeable) getIterator()).close();
        }

        public boolean evaluate(Object o) {
            return !_extent._broker.isDeleted(o);
        }
    }

    /**
     * Iterator over all new objects in this extent. This iterator is always
     * wrapped, so it doesn't need to keep track of whether it's closed.
     */
    private class FilterNewIterator
        extends FilterIterator
        implements Closeable, Predicate {

        public FilterNewIterator() {
            super(_broker.getTransactionalObjects().iterator());
            setPredicate(this);
        }

        public void close() {
        }

        public boolean evaluate(Object o) {
            if (!_broker.isNew(o))
                return false;

            Class type = o.getClass();
            if (!_subs && type != _type)
                return false;
            if (_subs && !_type.isAssignableFrom(type))
                return false;
            return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15094.java