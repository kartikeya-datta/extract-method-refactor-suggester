error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9014.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9014.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9014.java
text:
```scala
r@@epository.isFork(), repository.getMirrorUrl());

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
package com.github.mobile.ui.repo;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import android.view.LayoutInflater;
import android.view.View;

import com.actionbarsherlock.R.color;
import com.github.mobile.ui.StyledText;
import com.viewpagerindicator.R.layout;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;

/**
 * Adapter for the default account's repositories
 */
public class DefaultRepositoryListAdapter extends
        RepositoryListAdapter<Repository, RepositoryHeaderItemView> {

    private final AtomicReference<User> account;

    private final Map<Long, String> headers = new HashMap<Long, String>();

    private final Set<Long> noSeparators = new HashSet<Long>();

    /**
     * Create list adapter for repositories
     *
     * @param inflater
     * @param elements
     * @param account
     */
    public DefaultRepositoryListAdapter(LayoutInflater inflater,
            Repository[] elements, AtomicReference<User> account) {
        super(layout.repo_item, inflater, elements);

        this.account = account;
    }

    /**
     * Create list adapter for repositories
     *
     * @param inflater
     * @param account
     */
    public DefaultRepositoryListAdapter(LayoutInflater inflater,
            AtomicReference<User> account) {
        this(inflater, null, account);
    }

    /**
     * Clear registered header values
     *
     * @return this adapter
     */
    public DefaultRepositoryListAdapter clearHeaders() {
        headers.clear();
        noSeparators.clear();
        return this;
    }

    /**
     * Register section header
     *
     * @param repository
     * @param text
     * @return this adapter
     */
    public DefaultRepositoryListAdapter registerHeader(Repository repository,
            String text) {
        headers.put(repository.getId(), text);
        return this;
    }

    /**
     * Register repository to have no bottom separator
     *
     * @param repository
     * @return this adapter
     */
    public DefaultRepositoryListAdapter registerNoSeparator(
            Repository repository) {
        noSeparators.add(repository.getId());
        return this;
    }

    @Override
    protected void update(final int position,
            final RepositoryHeaderItemView view, final Repository repository) {
        String headerValue = headers.get(repository.getId());
        if (headerValue != null) {
            view.header.setVisibility(VISIBLE);
            view.headerText.setText(headerValue);
        } else
            view.header.setVisibility(GONE);

        if (noSeparators.contains(repository.getId()))
            view.separator.setVisibility(GONE);
        else
            view.separator.setVisibility(VISIBLE);

        StyledText name = new StyledText();
        if (!account.get().getLogin().equals(repository.getOwner().getLogin())) {
            int descriptionColor = view.repoName.getResources().getColor(
                    color.text_description);
            name.foreground(repository.getOwner().getLogin(), descriptionColor)
                    .foreground('/', descriptionColor);
        }
        name.bold(repository.getName());
        view.repoName.setText(name);

        updateDetails(view, repository.getDescription(),
                repository.getLanguage(), repository.getWatchers(),
                repository.getForks(), repository.isPrivate(),
                repository.isFork());
    }

    @Override
    protected RepositoryHeaderItemView createView(final View view) {
        return new RepositoryHeaderItemView(view);
    }

    @Override
    public long getItemId(final int position) {
        return getItem(position).getId();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9014.java