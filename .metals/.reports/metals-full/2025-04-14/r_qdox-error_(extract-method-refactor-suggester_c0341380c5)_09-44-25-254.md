error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9932.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9932.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9932.java
text:
```scala
f@@inder.setText(id.tv_owner_name, getString(string.navigate_to_user, owner.getLogin()));

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

import static com.github.mobile.Intents.EXTRA_USER;
import static com.github.mobile.RequestCodes.REPOSITORY_VIEW;
import static com.github.mobile.ResultCodes.RESOURCE_CHANGED;
import static java.util.Locale.US;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.github.kevinsawicki.wishlist.ViewFinder;
import com.github.mobile.R.id;
import com.github.mobile.R.layout;
import com.github.mobile.R.string;
import com.github.mobile.ThrowableLoader;
import com.github.mobile.persistence.AccountDataManager;
import com.github.mobile.ui.HeaderFooterListAdapter;
import com.github.mobile.ui.ItemListFragment;
import com.github.mobile.ui.LightAlertDialog;
import com.github.mobile.ui.user.OrganizationSelectionListener;
import com.github.mobile.ui.user.OrganizationSelectionProvider;
import com.github.mobile.ui.user.UserViewActivity;
import com.github.mobile.util.AvatarLoader;
import com.google.inject.Inject;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;

/**
 * Fragment to display a list of {@link Repository} instances
 */
public class RepositoryListFragment extends ItemListFragment<Repository>
        implements OrganizationSelectionListener {

    @Inject
    private AccountDataManager cache;

    @Inject
    private AvatarLoader avatars;

    private final AtomicReference<User> org = new AtomicReference<User>();

    private RecentRepositories recentRepos;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        User org = this.org.get();
        if (org != null)
            outState.putSerializable(EXTRA_USER, org);
    }

    @Override
    protected void configureList(Activity activity, ListView listView) {
        super.configureList(activity, listView);

        listView.setDividerHeight(0);
        updateHeaders(items);
    }

    @Override
    public void onDetach() {
        OrganizationSelectionProvider selectionProvider = (OrganizationSelectionProvider) getActivity();
        if (selectionProvider != null)
            selectionProvider.removeListener(this);

        super.onDetach();
    }

    @Override
    public void onOrganizationSelected(final User organization) {
        User previousOrg = org.get();
        int previousOrgId = previousOrg != null ? previousOrg.getId() : -1;
        org.set(organization);

        if (recentRepos != null)
            recentRepos.saveAsync();

        // Only hard refresh if view already created and org is changing
        if (previousOrgId != organization.getId()) {
            Activity activity = getActivity();
            if (activity != null)
                recentRepos = new RecentRepositories(activity, organization);

            refreshWithProgress();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Activity activity = getActivity();
        User currentOrg = ((OrganizationSelectionProvider) activity)
                .addListener(this);
        if (currentOrg == null && savedInstanceState != null)
            currentOrg = (User) savedInstanceState.getSerializable(EXTRA_USER);
        org.set(currentOrg);
        if (currentOrg != null)
            recentRepos = new RecentRepositories(activity, currentOrg);

        setEmptyText(string.no_repositories);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Refresh if the viewed repository was (un)starred
        if (requestCode == REPOSITORY_VIEW && resultCode == RESOURCE_CHANGED) {
            forceRefresh();
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onListItemClick(ListView list, View v, int position, long id) {
        Repository repo = (Repository) list.getItemAtPosition(position);
        if (recentRepos != null)
            recentRepos.add(repo);

        startActivityForResult(RepositoryViewActivity.createIntent(repo),
                REPOSITORY_VIEW);
    }

    @Override
    public boolean onListItemLongClick(ListView list, View v, int position,
            long itemId) {
        if (!isUsable())
            return false;

        final Repository repo = (Repository) list.getItemAtPosition(position);
        if (repo == null)
            return false;

        final AlertDialog dialog = LightAlertDialog.create(getActivity());
        dialog.setCanceledOnTouchOutside(true);

        dialog.setTitle(repo.generateId());

        View view = getActivity().getLayoutInflater().inflate(
                layout.repo_dialog, null);
        ViewFinder finder = new ViewFinder(view);

        final User owner = repo.getOwner();
        avatars.bind(finder.imageView(id.iv_owner_avatar), owner);
        finder.setText(id.tv_owner_name, "Navigate to " + owner.getLogin());
        finder.onClick(id.ll_owner_area, new OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();

                viewUser(owner);
            }
        });

        if ((recentRepos != null) && (recentRepos.contains(repo))) {
            finder.find(id.divider).setVisibility(View.VISIBLE);
            finder.find(id.ll_recent_repo_area).setVisibility(View.VISIBLE);
            finder.onClick(id.ll_recent_repo_area, new OnClickListener() {

                public void onClick(View v) {
                    dialog.dismiss();

                    recentRepos.remove(repo);
                    refresh();
                }
            });
        }

        dialog.setView(view);
        dialog.show();

        return true;
    }

    private void viewUser(User user) {
        if (org.get().getId() != user.getId())
            startActivity(UserViewActivity.createIntent(user));
    }

    @Override
    public void onStop() {
        super.onStop();

        if (recentRepos != null)
            recentRepos.saveAsync();
    }

    private void updateHeaders(final List<Repository> repos) {
        HeaderFooterListAdapter<?> rootAdapter = getListAdapter();
        if (rootAdapter == null)
            return;

        DefaultRepositoryListAdapter adapter = (DefaultRepositoryListAdapter) rootAdapter
                .getWrappedAdapter();
        adapter.clearHeaders();

        if (repos.isEmpty())
            return;

        // Add recent header if at least one recent repository
        Repository first = repos.get(0);
        if (recentRepos.contains(first))
            adapter.registerHeader(first, getString(string.recently_viewed));

        // Advance past all recent repositories
        int index;
        Repository current = null;
        for (index = 0; index < repos.size(); index++) {
            Repository repository = repos.get(index);
            if (recentRepos.contains(repository.getId()))
                current = repository;
            else
                break;
        }

        if (index >= repos.size())
            return;

        if (current != null)
            adapter.registerNoSeparator(current);

        // Register header for first character
        current = repos.get(index);
        char start = Character.toLowerCase(current.getName().charAt(0));
        adapter.registerHeader(current,
                Character.toString(start).toUpperCase(US));

        char previousHeader = start;
        for (index = index + 1; index < repos.size(); index++) {
            current = repos.get(index);
            char repoStart = Character.toLowerCase(current.getName().charAt(0));
            if (repoStart <= start)
                continue;

            // Don't include separator for the last element of the previous
            // character
            if (previousHeader != repoStart)
                adapter.registerNoSeparator(repos.get(index - 1));

            adapter.registerHeader(current, Character.toString(repoStart)
                    .toUpperCase(US));
            previousHeader = repoStart;
            start = repoStart++;
        }

        // Don't include separator for last element
        adapter.registerNoSeparator(repos.get(repos.size() - 1));
    }

    @Override
    public Loader<List<Repository>> onCreateLoader(int id, final Bundle args) {
        return new ThrowableLoader<List<Repository>>(getActivity(), items) {

            @Override
            public List<Repository> loadData() throws Exception {
                User org = RepositoryListFragment.this.org.get();
                if (org == null)
                    return Collections.emptyList();

                List<Repository> repos = cache.getRepos(org,
                        isForceRefresh(args));
                Collections.sort(repos, recentRepos);
                updateHeaders(repos);
                return repos;
            }
        };
    }

    @Override
    protected SingleTypeAdapter<Repository> createAdapter(List<Repository> items) {
        return new DefaultRepositoryListAdapter(getActivity()
                .getLayoutInflater(),
                items.toArray(new Repository[items.size()]), org);
    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return string.error_repos_load;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9932.java