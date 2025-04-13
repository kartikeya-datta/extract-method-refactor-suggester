error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2594.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2594.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2594.java
text:
```scala
private static final i@@nt FORMAT_VERSION = 4;

/*
 * Copyright 2012 GitHub Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.mobile.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.github.mobile.RequestFuture;
import com.github.mobile.RequestReader;
import com.github.mobile.RequestWriter;
import com.github.mobile.accounts.AuthenticatedUserTask;
import com.github.mobile.core.issue.IssueFilter;
import com.github.mobile.persistence.OrganizationRepositories.Factory;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;

/**
 * Manager cache for an account
 */
public class AccountDataManager {

    private static final String TAG = "ADM";

    private static final Executor EXECUTOR = Executors.newFixedThreadPool(10);

    /**
     * Format version to bump if serialization format changes and cache should be ignored
     */
    private static final int FORMAT_VERSION = 2;

    @Inject
    private Context context;

    @Inject
    private DatabaseCache dbCache;

    @Inject
    private Factory allRepos;

    @Inject
    private Organizations userAndOrgsResource;

    @Inject
    @Named("cacheDir")
    private File root;

    /**
     * @return context
     */
    public Context getContext() {
        return context;
    }

    /**
     * Read data from file
     *
     * @param file
     * @return data
     */
    private <V> V read(File file) {
        long start = System.currentTimeMillis();
        long length = file.length();
        V data = new RequestReader(file, FORMAT_VERSION).read();
        if (data != null)
            Log.d(TAG,
                    MessageFormat.format("Cache hit to {0}, {1} ms to load {2} bytes", file.getName(),
                            (System.currentTimeMillis() - start), length));
        return data;
    }

    /**
     * Write data to file
     *
     * @param file
     * @param data
     * @return this manager
     */
    private AccountDataManager write(File file, Object data) {
        new RequestWriter(file, FORMAT_VERSION).write(data);
        return this;
    }

    /**
     * Query tables for columns
     *
     * @param helper
     * @param tables
     * @param columns
     * @return cursor
     */
    protected Cursor query(SQLiteOpenHelper helper, String tables, String[] columns) {
        return query(helper, tables, columns, null, null);
    }

    /**
     * Query tables for columns
     *
     * @param helper
     * @param tables
     * @param columns
     * @param selection
     * @param selectionArgs
     * @return cursor
     */
    protected Cursor query(SQLiteOpenHelper helper, String tables, String[] columns, String selection,
            String[] selectionArgs) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(tables);
        return builder.query(helper.getReadableDatabase(), columns, selection, selectionArgs, null, null, null);
    }

    /**
     * Get organizations
     * <p/>
     * This method may perform file and/or network I/O and should never be called on the UI-thread
     *
     * @return list of user and Orgs
     * @throws IOException
     */
    public List<User> getOrgs() throws IOException {
        return dbCache.loadOrRequest(userAndOrgsResource);
    }

    /**
     * Get repositories for given {@link User}
     * <p/>
     * This method may perform network I/O and should never be called on the UI-thread
     *
     * @param user
     * @param forceReload
     *            if true, cached data will not be returned
     * @return list of repositories
     * @throws IOException
     */
    public List<Repository> getRepos(final User user, boolean forceReload) throws IOException {
        OrganizationRepositories resource = allRepos.under(user);
        return forceReload ? dbCache.requestAndStore(resource) : dbCache.loadOrRequest(resource);
    }

    /**
     * Get bookmarked issue filters
     * <p/>
     * This method may perform network I/O and should never be called on the UI-thread
     *
     * @return non-null but possibly empty collection of issue filters
     */
    public Collection<IssueFilter> getIssueFilters() {
        final File cache = new File(root, "issue_filters.ser");
        Collection<IssueFilter> cached = read(cache);
        if (cached != null)
            return cached;
        return Collections.emptyList();
    }

    /**
     * Get bookmarked issue filters
     *
     * @param requestFuture
     */
    public void getIssueFilters(final RequestFuture<Collection<IssueFilter>> requestFuture) {
        new AuthenticatedUserTask<Collection<IssueFilter>>(context, EXECUTOR) {

            public Collection<IssueFilter> run() throws Exception {
                return getIssueFilters();
            }

            protected void onSuccess(Collection<IssueFilter> filters) throws Exception {
                requestFuture.success(filters);
            }

            ;
        }.execute();
    }

    /**
     * Add issue filter to store
     * <p/>
     * This method may perform file I/O and should never be called on the UI-thread
     *
     * @param filter
     */
    public void addIssueFilter(IssueFilter filter) {
        final File cache = new File(root, "issue_filters.ser");
        Collection<IssueFilter> filters = read(cache);
        if (filters == null)
            filters = new HashSet<IssueFilter>();
        if (filters.add(filter))
            write(cache, filters);
    }

    /**
     * Add issue filter to store
     *
     * @param filter
     * @param requestFuture
     */
    public void addIssueFilter(final IssueFilter filter, final RequestFuture<IssueFilter> requestFuture) {
        new AuthenticatedUserTask<IssueFilter>(context, EXECUTOR) {

            public IssueFilter run() throws Exception {
                addIssueFilter(filter);
                return filter;
            }

            protected void onSuccess(IssueFilter filter) throws Exception {
                requestFuture.success(filter);
            }

            protected void onException(Exception e) throws RuntimeException {
                Log.d(TAG, "Exception adding issue filter", e);
            }

        }.execute();
    }

    /**
     * Add issue filter from store
     * <p/>
     * This method may perform file I/O and should never be called on the UI-thread
     *
     * @param filter
     */
    public void removeIssueFilter(IssueFilter filter) {
        final File cache = new File(root, "issue_filters.ser");
        Collection<IssueFilter> filters = read(cache);
        if (filters != null && filters.remove(filter))
            write(cache, filters);
    }

    /**
     * Remove issue filter from store
     *
     * @param filter
     * @param requestFuture
     */
    public void removeIssueFilter(final IssueFilter filter, final RequestFuture<IssueFilter> requestFuture) {
        new AuthenticatedUserTask<IssueFilter>(context, EXECUTOR) {

            public IssueFilter run() throws Exception {
                removeIssueFilter(filter);
                return filter;
            }

            protected void onSuccess(IssueFilter filter) throws Exception {
                requestFuture.success(filter);
            }

            protected void onException(Exception e) throws RuntimeException {
                Log.d(TAG, "Exception removing issue filter", e);
            }

        }.execute();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2594.java