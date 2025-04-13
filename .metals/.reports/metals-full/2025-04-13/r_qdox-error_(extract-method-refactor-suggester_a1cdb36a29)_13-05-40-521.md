error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7583.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7583.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7583.java
text:
```scala
public static final S@@tring EXTRA_ISSUE_NUMBERS = INTENT_EXTRA_PREFIX + "ISSUE_NUMBERS";

package com.github.mobile.android.util;

import static org.eclipse.egit.github.core.RepositoryId.createFromUrl;
import android.content.Intent;

import java.io.Serializable;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.User;

/**
 * Helper for creating intents
 */
public class GitHubIntents {

    /**
     * Prefix for all intents created
     */
    public static final String INTENT_PREFIX = "com.github.mobile.android.";

    /**
     * Prefix for all extra data added to intents
     */
    public static final String INTENT_EXTRA_PREFIX = INTENT_PREFIX + "extra.";

    /**
     * Repository handle
     */
    public static final String EXTRA_REPOSITORY = INTENT_EXTRA_PREFIX + "REPOSITORY";

    /**
     * Repository ids collection handle
     */
    public static final String EXTRA_REPOSITORIES = INTENT_EXTRA_PREFIX + "REPOSITORIES";

    /**
     * Repository name
     */
    public static final String EXTRA_REPOSITORY_NAME = INTENT_EXTRA_PREFIX + "REPOSITORY_NAME";

    /**
     * Repository owner
     */
    public static final String EXTRA_REPOSITORY_OWNER = INTENT_EXTRA_PREFIX + "REPOSITORY_OWNER";

    /**
     * Issue number
     */
    public static final String EXTRA_ISSUE_NUMBER = INTENT_EXTRA_PREFIX + "ISSUE_NUMBER";

    /**
     * Issue handle
     */
    public static final String EXTRA_ISSUE = INTENT_EXTRA_PREFIX + "ISSUE";

    /**
     * Issue number collection handle
     */
    public static final String EXTRA_ISSUE_NUMBERS = INTENT_EXTRA_PREFIX + "ISSUES_NUMBERS";

    /**
     * Gist id
     */
    public static final String EXTRA_GIST_ID = INTENT_EXTRA_PREFIX + "GIST_ID";

    /**
     * List of Gist ids
     */
    public static final String EXTRA_GIST_IDS = INTENT_EXTRA_PREFIX + "GIST_IDS";

    /**
     * Gist handle
     */
    public static final String EXTRA_GIST = INTENT_EXTRA_PREFIX + "GIST";

    /**
     * Gist file handle
     */
    public static final String EXTRA_GIST_FILE = INTENT_EXTRA_PREFIX + "GIST_FILE";

    /**
     * User handle
     */
    public static final String EXTRA_USER = INTENT_EXTRA_PREFIX + "USER";

    /**
     * Issue filter handle
     */
    public static final String EXTRA_ISSUE_FILTER = INTENT_EXTRA_PREFIX + "ISSUE_FILTER";

    /**
     * Comment body
     */
    public static final String EXTRA_COMMENT_BODY = INTENT_EXTRA_PREFIX + "COMMENT_BODY";

    /**
     * Comments handle
     */
    public static final String EXTRA_COMMENTS = INTENT_EXTRA_PREFIX + "COMMENTS";

    /**
     * Integer position
     */
    public static final String EXTRA_POSITION = INTENT_EXTRA_PREFIX + "POSITION";

    /**
     * Resolve the {@link RepositoryId} referenced by the given intent
     *
     * @param intent
     * @return repository id
     */
    public static RepositoryId repoFrom(Intent intent) {
        String repoName = intent.getStringExtra(EXTRA_REPOSITORY_NAME);
        String repoOwner = intent.getStringExtra(EXTRA_REPOSITORY_OWNER);
        return RepositoryId.create(repoOwner, repoName);
    }

    /**
     * Builder for generating an intent configured with extra data such as an issue, repository, or gist
     */
    public static class Builder {

        private final Intent intent;

        /**
         * Create builder with suffix
         *
         * @param actionSuffix
         */
        public Builder(String actionSuffix) {
            intent = new Intent(INTENT_PREFIX + actionSuffix); // actionSuffix = e.g. "repos.VIEW"
        }

        /**
         * Add repository id to intent being built up
         *
         * @param repositoryId
         * @return this builder
         */
        public Builder repo(RepositoryId repositoryId) {
            return add(EXTRA_REPOSITORY_NAME, repositoryId.getName()).add(EXTRA_REPOSITORY_OWNER,
                    repositoryId.getOwner());
        }

        /**
         * Add repository to intent being built up
         *
         * @param repository
         * @return this builder
         */
        public Builder repo(Repository repository) {
            return add(EXTRA_REPOSITORY, repository);
        }

        /**
         * Add issue to intent being built up
         *
         * @param issue
         * @return this builder
         */
        public Builder issue(Issue issue) {
            return repo(createFromUrl(issue.getHtmlUrl())).add(EXTRA_ISSUE, issue).add(EXTRA_ISSUE_NUMBER,
                    issue.getNumber());
        }

        /**
         * Add gist to intent being built up
         *
         * @param gist
         * @return this builder
         */
        public Builder gist(Gist gist) {
            return add(EXTRA_GIST, gist);
        }

        /**
         * Add gist id to intent being built up
         *
         * @param gist
         * @return this builder
         */
        public Builder gist(String gist) {
            return add(EXTRA_GIST_ID, gist);
        }

        /**
         * Add gist file to intent being built up
         *
         * @param file
         * @return this builder
         */
        public Builder gistFile(GistFile file) {
            return add(EXTRA_GIST_FILE, file);
        }

        /**
         * Add user to intent being built up
         *
         * @param user
         * @return this builder;
         */
        public Builder user(User user) {
            return add(EXTRA_USER, user);
        }

        /**
         * Add extra field data value to intent being built up
         *
         * @param fieldName
         * @param value
         * @return this builder
         */
        public Builder add(String fieldName, String value) {
            intent.putExtra(fieldName, value);
            return this;
        }

        /**
         * Add extra field data value to intent being built up
         *
         * @param fieldName
         * @param value
         * @return this builder
         */
        public Builder add(String fieldName, int value) {
            intent.putExtra(fieldName, value);
            return this;
        }

        /**
         * Add extra field data value to intent being built up
         *
         * @param fieldName
         * @param value
         * @return this builder
         */
        public Builder add(String fieldName, Serializable value) {
            intent.putExtra(fieldName, value);
            return this;
        }

        /**
         * Get built intent
         *
         * @return intent
         */
        public Intent toIntent() {
            return intent;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7583.java