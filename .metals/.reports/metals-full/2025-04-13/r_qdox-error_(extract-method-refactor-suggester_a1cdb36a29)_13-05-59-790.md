error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9525.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9525.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9525.java
text:
```scala
f@@ilterId.append(entry.getKey()).append('=').append(entry.getValue()).append(',');

package com.github.mobile.android;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.OrganizationService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;

import roboguice.util.RoboAsyncTask;

/**
 * Manager cache for an account
 */
public class AccountDataManager {

    private static final String TAG = "GHDM";

    private static final Executor EXECUTOR = Executors.newFixedThreadPool(10);

    /**
     * Format version to bump if serialization format changes and cache should be ignored
     */
    private static final int FORMAT_VERSION = 1;

    private static String digest(String value) {
        byte[] digested;
        try {
            digested = MessageDigest.getInstance("SHA-1").digest(value.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (UnsupportedEncodingException e) {
            return null;
        }

        String hashed = new BigInteger(1, digested).toString(16);
        int padding = 40 - hashed.length();
        if (padding == 0)
            return hashed;
        char[] zeros = new char[padding];
        Arrays.fill(zeros, '0');
        return new String(zeros) + hashed;
    }

    private final Context context;

    private final UserService users;

    private final OrganizationService orgs;

    private final RepositoryService repos;

    private final IssueService issues;

    private final File root;

    /**
     * Create manager storing data at given root folder
     *
     * @param context
     * @param root
     * @param users
     * @param orgs
     * @param repos
     * @param issues
     */
    public AccountDataManager(final Context context, final File root, UserService users, OrganizationService orgs,
            RepositoryService repos, IssueService issues) {
        this.context = context;
        this.root = root;
        this.users = users;
        this.orgs = orgs;
        this.repos = repos;
        this.issues = issues;
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
     * Get orgs
     *
     * @param requestFuture
     */
    public void getOrgs(final RequestFuture<List<User>> requestFuture) {
        final File cache = new File(root, "orgs.ser");
        new RoboAsyncTask<List<User>>(context, EXECUTOR) {

            public List<User> call() throws Exception {
                List<User> cached = read(cache);
                if (cached != null)
                    return cached;

                List<User> loaded = new ArrayList<User>(orgs.getOrganizations());
                Collections.sort(loaded, new Comparator<User>() {

                    public int compare(User u1, User u2) {
                        return u1.getLogin().compareToIgnoreCase(u2.getLogin());
                    }
                });
                loaded.add(0, users.getUser());
                write(cache, loaded);
                return loaded;
            }

            protected void onSuccess(List<User> orgs) throws Exception {
                requestFuture.success(orgs);
            };
        }.execute();
    }

    /**
     * Get repositories for user
     *
     * @param user
     * @param requestFuture
     */
    public void getRepos(final User user, final RequestFuture<List<Repository>> requestFuture) {
        final File folder = new File(root, user.getLogin());
        final File cache = new File(folder, "repos.ser");
        new RoboAsyncTask<List<Repository>>(context, EXECUTOR) {

            public List<Repository> call() throws Exception {
                List<Repository> cached = read(cache);
                if (cached != null)
                    return cached;

                List<Repository> loaded;
                if (!"User".equals(user.getType()))
                    loaded = repos.getOrgRepositories(user.getLogin());
                else if (user.getLogin().equals(repos.getClient().getUser()))
                    loaded = repos.getRepositories();
                else
                    loaded = repos.getRepositories(user.getLogin());
                Collections.sort(loaded, new Comparator<Repository>() {

                    public int compare(Repository r1, Repository r2) {
                        return r1.getName().compareToIgnoreCase(r2.getName());
                    }
                });
                write(cache, loaded);
                return loaded;
            }

            protected void onSuccess(List<Repository> repos) throws Exception {
                requestFuture.success(repos);
            };
        }.execute();
    }

    /**
     * Get repositories for user
     *
     * @param repository
     * @param filter
     * @param requestFuture
     */
    public void getIssues(final IRepositoryIdProvider repository, final Map<String, String> filter,
            final RequestFuture<List<Issue>> requestFuture) {
        final File folder = new File(root, repository.generateId());
        new RoboAsyncTask<List<Issue>>(context, EXECUTOR) {

            public List<Issue> call() throws Exception {
                StringBuilder filterId = new StringBuilder();
                for (Entry<String, String> entry : filter.entrySet())
                    filterId.append(entry.getKey()).append('=').append(entry.getKey()).append(',');
                File cache = new File(folder, digest(filterId.toString()));

                List<Issue> cached = read(cache);
                if (cached != null)
                    return cached;

                List<Issue> loaded = issues.getIssues(repository, filter);
                write(cache, loaded);
                return loaded;
            }

            protected void onSuccess(List<Issue> repos) throws Exception {
                requestFuture.success(repos);
            };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9525.java