error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6638.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6638.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6638.java
text:
```scala
&@@& !fetchState.requiresFetch(fms[i]))

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
package org.apache.openjpa.jdbc.kernel;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.BitSet;

import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.meta.FieldMapping;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.sql.Result;
import org.apache.openjpa.jdbc.sql.SQLBuffer;
import org.apache.openjpa.jdbc.sql.Select;
import org.apache.openjpa.jdbc.sql.SelectExecutor;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.kernel.StoreContext;
import org.apache.openjpa.lib.util.Closeable;
import org.apache.openjpa.util.InternalException;

/**
 * Object provider implementation that fetches one page of results at a
 * a time as it scrolls. If the {@link #getPagedFields} method returns a
 * non-null bit set, this this provider is a good fit for your configuration.
 * The method tests the following conditions:
 * <ul>
 * <li>The eager fetch mode is <code>parallel</code>.</li>
 * <li>The select's result should be treated as a large result set.</li>
 * <li>The mapping being selected has fields that use parallel selects
 * under the current fetch configuration.</li>
 * </ul>
 *  To use this provider, select the candidate mapping with eager fetch
 * mode set to <code>join</code>. This provider will take care of performing
 * <code>parallel</code> mode batch selects for each page it reads.
 *
 * @author Abe White
 * @nojavadoc
 */
public class PagingResultObjectProvider
    extends SelectResultObjectProvider {

    private final ClassMapping[] _mappings;
    private final Object[] _page;
    private final int[] _idxs;
    private final BitSet[] _paged;
    private int _pos = -1; // logical pos
    private int _pagePos = -1; // pos of page start

    /**
     * Return a bit set representing batch select fields that will be paged,
     * or null if no fields need paging, which indicates that this provider
     * should not be used.
     *
     * @see #PagingResultObjectProvider
     */
    public static BitSet getPagedFields(Select sel, ClassMapping mapping,
        JDBCStore store, JDBCFetchState fetchState, int eagerMode,
        long size) {
        JDBCFetchConfiguration fetch = fetchState.getJDBCFetchConfiguration();
        // if we have a range then we always use paging if there are any
        // eager select fields; otherwise it depends on lrs and fetch settings
        if (size == Long.MAX_VALUE || !sel.getAutoDistinct()) {
            // not lrs?
            if (!sel.isLRS())
                return null;

            // not configured for lazy loading?
            if (fetch.getFetchBatchSize() < 0)
                return null;
        }

        // not configured for eager selects?
        eagerMode = Math.min(eagerMode, fetch.getEagerFetchMode());
        if (eagerMode != fetch.EAGER_PARALLEL)
            return null;

        // are there any mappings that require batched selects?
        FieldMapping[] fms = mapping.getDefinedFieldMappings();
        BitSet paged = null;
        for (int i = 0; i < fms.length; i++) {
            if (fetchState != null
                && !fetchState.requiresSelect(fms[i], false))
                continue;

            if (fms[i].supportsSelect(sel, sel.EAGER_PARALLEL, null, store,
                fetch) > 0 && (fms[i].isEagerSelectToMany() || fms[i].
                supportsSelect(sel, sel.EAGER_OUTER, null, store, fetch) == 0))
            {
                if (paged == null)
                    paged = new BitSet();
                paged.set(fms[i].getIndex());
            }
        }
        return paged;
    }

    /**
     * Constructor.
     *
     * @param sel the select to execute
     * @param mapping the mapping of the result objects
     * @param store the store manager to delegate loading to
     * @param fetch the fetch configuration, or null for default
     * @param paged the bit set returned from {@link #getPagedFields}
     * @param size the known maximum size of the result, or
     * {@link Long#MAX_VALUE} for no known limit
     */
    public PagingResultObjectProvider(SelectExecutor sel,
        ClassMapping mapping, JDBCStore store, JDBCFetchState fetchState,
        BitSet paged, long size) {
        this(sel, new ClassMapping[]{ mapping }, store, fetchState,
            new BitSet[]{ paged }, size);
    }

    /**
     * Constructor.
     *
     * @param sel the select to execute
     * @param mappings the mappings for the independent classes of the
     * result objects
     * @param store the store manager to delegate loading to
     * @param fetch the fetch configuration, or null for default
     * @param paged the bit sets returned from {@link #getPagedFields}
     * for each select in the possible union
     * @param size the known maximum size of the result, or
     * {@link Long#MAX_VALUE} for no known limit
     */
    public PagingResultObjectProvider(SelectExecutor sel,
        ClassMapping[] mappings, JDBCStore store, JDBCFetchState fetchState,
        BitSet[] paged, long size) {
        super(sel, store, fetchState);
        _mappings = mappings;
        _paged = paged;

        // don't let system construct this type of rop for stupid sizes
        if (size <= 1)
            throw new InternalException("size=" + size);

        // try to find a good page size.  if the known size < batch size, use
        // it.  if the batch size is set, then use that; if it's sorta close
        // to the size, then use the size / 2 to get two full pages rather
        // than a possible big one and small one.  cap everything at 50.
        int batch = getFetchConfiguration().getFetchBatchSize();
        int pageSize;
        if (size <= batch && size <= 50)
            pageSize = (int) size;
        else if (batch > 0 && batch <= 50) {
            if (size <= batch * 2) {
                if (size % 2 == 0)
                    pageSize = (int) (size / 2);
                else
                    pageSize = (int) (size / 2 + 1);
            } else
                pageSize = batch;
        } else if (size <= 50)
            pageSize = (int) size;
        else if (size <= 100) {
            if (size % 2 == 0)
                pageSize = (int) (size / 2);
            else
                pageSize = (int) (size / 2 + 1);
        } else
            pageSize = 50;

        _page = new Object[pageSize];
        if (_paged.length > 1)
            _idxs = new int[pageSize];
        else
            _idxs = null;
    }

    /**
     * Return the page size in use.
     */
    public int getPageSize() {
        return _page.length;
    }

    public void open()
        throws SQLException {
        super.open();
        _pos = -1;
    }

    public boolean next()
        throws SQLException {
        _pos++;
        if (inPage())
            return _page[_pos - _pagePos] != null;
        if (!super.next()) {
            setSize(_pos);
            return false;
        }
        return true;
    }

    public boolean absolute(int pos)
        throws SQLException {
        _pos = pos;
        if (inPage())
            return _page[_pos - _pagePos] != null;
        return super.absolute(pos);
    }

    public Object getResultObject()
        throws SQLException {
        if (!inPage())
            fillPage();
        return _page[_pos - _pagePos];
    }

    /**
     * Test whether our current position is within the cached page of results.
     */
    private boolean inPage() {
        return _pagePos != -1 && _pos >= _pagePos
            && _pos < _pagePos + _page.length;
    }

    /**
     * Start a new page at the present position.
     */
    private void fillPage()
        throws SQLException {
        // clear page
        Arrays.fill(_page, null);

        // cache result objects
        JDBCStoreManager storeMgr = (JDBCStoreManager) getStore();
        ClassMapping mapping;
        Result res;
        int idx;
        for (int i = 0; i < _page.length; i++) {
            res = getResult();
            idx = res.indexOf();
            if (_idxs != null)
                _idxs[i] = idx;
            mapping = res.getBaseMapping();
            if (mapping == null)
                mapping = _mappings[idx];

            // rather than use the standard result.load(), we go direct to
            // the store manager so we can pass in our eager-fetched fields as
            // fields to exclude from the initial load of the objects
            _page[i] = storeMgr.load(mapping, getFetchState(),
                _paged[idx], res);
            if (i != _page.length - 1 && !getResult().next()) {
                setSize(_pos + i + 1);
                break;
            }
        }

        // load data for eager fields
        _pagePos = _pos;
        if (_page[0] != null) {
            if (_page.length > 1 && _page[1] == null)
                loadEagerFields();
            else
                executeEagerSelects();
        }
    }

    /**
     * When there is only one instance in a page, load fields as normal.
     */
    private void loadEagerFields()
        throws SQLException {
        int idx = (_idxs == null) ? 0 : _idxs[0];
        if (_paged[idx] == null)
            return;

        JDBCStore store = getStore();
        OpenJPAStateManager sm = store.getContext().getStateManager(_page[0]);
        for (int i = 0, len = _paged[idx].length(); i < len; i++) {
            if (_paged[idx].get(i)) {
                _mappings[idx].getFieldMapping(i).load(sm, store,
                    getFetchState());
            }
        }
    }

    /**
     * Load eager batch selects for current page of results.
     */
    private void executeEagerSelects()
        throws SQLException {
        if (_idxs == null) {
            executeEagerSelects(_mappings[0], _paged[0], 0, _page.length);
            return;
        }

        int start = 0;
        int idx = _idxs[0];
        int pos = 0;
        for (; pos < _page.length && _page[pos] != null; pos++) {
            if (idx != _idxs[pos]) {
                if (_paged[idx] != null)
                    executeEagerSelects(_mappings[idx], _paged[idx], start,
                        pos);
                start = pos;
                idx = _idxs[pos];
            }
        }
        if (start < pos && _paged[idx] != null) // cleanup remainder
            executeEagerSelects(_mappings[idx], _paged[idx], start, pos);
    }

    /**
     * Load eager batch selects for the given mapping and its superclasses.
     */
    private void executeEagerSelects(ClassMapping mapping, BitSet paged,
        int start, int end)
        throws SQLException {
        // base case
        if (mapping == null)
            return;

        // recurse on superclass
        executeEagerSelects(mapping.getJoinablePCSuperclassMapping(), paged,
            start, end);

        // figure out how many batch selects to do on this mapping
        FieldMapping[] fms = mapping.getDefinedFieldMappings();
        int sels = 0;
        for (int i = 0; i < fms.length; i++)
            if (paged.get(fms[i].getIndex()))
                sels++;
        if (sels == 0)
            return;

        // create where condition limiting instances to this page
        JDBCStore store = getStore();
        Select sel = store.getSQLFactory().newSelect();
        SQLBuffer buf = new SQLBuffer(store.getDBDictionary());
        Column[] pks = mapping.getPrimaryKeyColumns();
        if (pks.length == 1)
            inContains(sel, buf, mapping, pks, start, end);
        else
            orContains(sel, buf, mapping, pks, start, end);
        sel.where(buf);

        StoreContext ctx = store.getContext();
        JDBCFetchConfiguration fetch = getFetchConfiguration();
        JDBCFetchState fetchState = (JDBCFetchState) fetch.newFetchState();
        // do each batch select
        Object res;
        int esels = 0;
        SelectExecutor esel;
        int unions;
        for (int i = 0; i < fms.length; i++) {
            if (!paged.get(fms[i].getIndex()))
                continue;

            unions = fms[i].supportsSelect(sel, Select.EAGER_PARALLEL, null,
                store, fetch);
            if (unions == 0)
                continue;

            // we don't need to clone if this is the last select
            esels++;
            if (esels < sels || unions > 1)
                esel = sel.whereClone(unions);
            else
                esel = sel;

            // get result
            fms[i].selectEagerParallel(esel, null, store, fetchState,
                JDBCFetchConfiguration.EAGER_PARALLEL);
            res = esel.execute(store, fetch);
            try {
                // and load result into paged instances
                for (int j = start; j < end && _page[j] != null; j++)
                    res = fms[i].loadEagerParallel(ctx.getStateManager
                        (_page[j]), store, fetchState, res);
            } finally {
                if (res instanceof Closeable)
                    try {
                        ((Closeable) res).close();
                    } catch (Exception e) {
                    }
            }
        }
    }

    /**
     * Create an IN clause limiting the results to the current page.
     */
    private void inContains(Select sel, SQLBuffer buf, ClassMapping mapping,
        Column[] pks, int start, int end) {
        buf.append(sel.getColumnAlias(pks[0])).append(" IN (");
        for (int i = start; i < end && _page[i] != null; i++) {
            if (i > start)
                buf.append(", ");
            buf.appendValue(mapping.toDataStoreValue(_page[i], pks,
                getStore()), pks[0]);
        }
        buf.append(")");
    }

    /**
     * Create OR conditions limiting the results to the curent page.
     */
    private void orContains(Select sel, SQLBuffer buf, ClassMapping mapping,
        Column[] pks, int start, int end) {
        String[] aliases = new String[pks.length];
        for (int i = 0; i < pks.length; i++)
            aliases[i] = sel.getColumnAlias(pks[i]);

        Object[] vals;
        buf.append("(");
        for (int i = start; i < end && _page[i] != null; i++) {
            if (i > start)
                buf.append(" OR ");

            vals = (Object[]) mapping.toDataStoreValue(_page[i], pks,
                getStore());
            buf.append("(");
            for (int j = 0; j < vals.length; j++) {
                if (j > 0)
                    buf.append(" AND ");
                buf.append(aliases[j]);
                if (vals[j] == null)
                    buf.append(" IS ");
                else
                    buf.append(" = ");
                buf.appendValue(vals[j], pks[j]);
            }
            buf.append(")");
        }
        buf.append(")");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6638.java