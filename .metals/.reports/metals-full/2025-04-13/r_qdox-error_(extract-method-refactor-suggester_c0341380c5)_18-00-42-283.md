error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8622.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8622.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8622.java
text:
```scala
r@@eturn getClass().getSimpleName() + '[' + org.getLogin() + ']';

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

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.github.mobile.accounts.GitHubAccount;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.assistedinject.Assisted;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.WatcherService;

/**
 * Cache of repositories under a given organization
 */
public class OrganizationRepositories implements
        PersistableResource<Repository> {

    /**
     * Creation factory
     */
    public interface Factory {

        /**
         * Get repositories under given organization
         *
         * @param org
         * @return repositories
         */
        OrganizationRepositories under(User org);
    }

    private final User org;

    private final RepositoryService repos;

    private final WatcherService watcher;

    private final Provider<GitHubAccount> accountProvider;

    /**
     * Create repositories cache for a given organization
     *
     * @param orgs
     * @param repos
     * @param watcher
     * @param accountProvider
     */
    @Inject
    public OrganizationRepositories(@Assisted User orgs,
            RepositoryService repos, WatcherService watcher,
            Provider<GitHubAccount> accountProvider) {
        this.org = orgs;
        this.repos = repos;
        this.watcher = watcher;
        this.accountProvider = accountProvider;
    }

    @Override
    public Cursor getCursor(SQLiteDatabase readableDatabase) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables("repos JOIN users ON (repos.ownerId = users.id)");
        return builder.query(readableDatabase, new String[] {
                "repos.repoId, repos.name", "users.id", "users.name",
                "users.avatarurl", "repos.private", "repos.fork",
                "repos.description", "repos.forks", "repos.watchers",
                "repos.language", "repos.hasIssues", "repos.mirrorUrl" },
                "repos.orgId=?",
                new String[] { Integer.toString(org.getId()) }, null, null,
                null);
    }

    @Override
    public Repository loadFrom(Cursor cursor) {
        Repository repo = new Repository();
        repo.setId(cursor.getLong(0));
        repo.setName(cursor.getString(1));

        User owner = new User();
        owner.setId(cursor.getInt(2));
        owner.setLogin(cursor.getString(3));
        owner.setAvatarUrl(cursor.getString(4));
        repo.setOwner(owner);

        repo.setPrivate(cursor.getInt(5) == 1);
        repo.setFork(cursor.getInt(6) == 1);
        repo.setDescription(cursor.getString(7));
        repo.setForks(cursor.getInt(8));
        repo.setWatchers(cursor.getInt(9));
        repo.setLanguage(cursor.getString(10));
        repo.setHasIssues(cursor.getInt(11) == 1);
        repo.setMirrorUrl(cursor.getString(12));

        return repo;
    }

    @Override
    public void store(SQLiteDatabase db, List<Repository> repos) {
        db.delete("repos", "orgId=?",
                new String[] { Integer.toString(org.getId()) });
        for (Repository repo : repos) {
            User owner = repo.getOwner();
            ContentValues values = new ContentValues(12);

            values.put("repoId", repo.getId());
            values.put("name", repo.getName());
            values.put("orgId", org.getId());
            values.put("ownerId", owner.getId());
            values.put("private", repo.isPrivate() ? 1 : 0);
            values.put("fork", repo.isFork() ? 1 : 0);
            values.put("description", repo.getDescription());
            values.put("forks", repo.getForks());
            values.put("watchers", repo.getWatchers());
            values.put("language", repo.getLanguage());
            values.put("hasIssues", repo.isHasIssues() ? 1 : 0);
            values.put("mirrorUrl", repo.getMirrorUrl());
            db.replace("repos", null, values);

            values.clear();
            values.put("id", owner.getId());
            values.put("name", owner.getLogin());
            values.put("avatarurl", owner.getAvatarUrl());
            db.replace("users", null, values);
        }
    }

    @Override
    public List<Repository> request() throws IOException {
        if (isAuthenticatedUser()) {
            Set<Repository> all = new TreeSet<Repository>(
                    new Comparator<Repository>() {

                        public int compare(final Repository repo1,
                                final Repository repo2) {
                            final long id1 = repo1.getId();
                            final long id2 = repo2.getId();
                            if (id1 > id2)
                                return 1;
                            if (id1 < id2)
                                return -1;
                            return 0;
                        }
                    });
            all.addAll(repos.getRepositories());
            all.addAll(watcher.getWatched());
            return new ArrayList<Repository>(all);
        } else
            return repos.getOrgRepositories(org.getLogin());
    }

    private boolean isAuthenticatedUser() {
        return org.getLogin().equals(accountProvider.get().username);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + org.getLogin() + "]";
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8622.java