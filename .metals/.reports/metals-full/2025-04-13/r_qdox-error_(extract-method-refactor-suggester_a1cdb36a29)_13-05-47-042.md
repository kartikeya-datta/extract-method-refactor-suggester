error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/383.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/383.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/383.java
text:
```scala
protected v@@oid update(final int position, final OrgItemView view, final User user) {

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
package com.github.mobile.ui.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mobile.R.id;
import com.github.mobile.R.layout;
import com.github.mobile.R.string;
import com.github.mobile.ui.ItemListAdapter;
import com.github.mobile.ui.ItemView;
import com.github.mobile.util.AvatarLoader;

import java.util.List;

import org.eclipse.egit.github.core.User;

/**
 * Dropdown list adapter to display orgs. and other context-related activity links
 */
public class HomeDropdownListAdapter extends BaseAdapter {

    /**
     * Action for Gists
     */
    public static final int ACTION_GISTS = 0;

    /**
     * Action for the issues dashboard
     */
    public static final int ACTION_DASHBOARD = 1;

    /**
     * Action for issues filter
     */
    public static final int ACTION_FILTERS = 2;

    private static class OrgItemView extends ItemView {

        public final TextView nameText;

        public final ImageView avatarView;

        public OrgItemView(View view) {
            super(view);

            nameText = (TextView) view.findViewById(id.tv_org_name);
            avatarView = (ImageView) view.findViewById(id.iv_avatar);
        }
    }

    private static class OrgListAdapter extends ItemListAdapter<User, OrgItemView> {

        private final AvatarLoader avatars;

        public OrgListAdapter(final int viewId, final LayoutInflater inflater, final User[] elements,
                final AvatarLoader avatars) {
            super(viewId, inflater, elements);

            this.avatars = avatars;
        }

        @Override
        protected void update(final OrgItemView view, final User user) {
            view.nameText.setText(user.getLogin());
            avatars.bind(view.avatarView, user);
        }

        @Override
        protected OrgItemView createView(final View view) {
            return new OrgItemView(view);
        }

        @Override
        public long getItemId(final int position) {
            return getItem(position).getId();
        }
    }

    private int selected;

    private final Context context;

    private final OrgListAdapter listAdapter;

    private final OrgListAdapter dropdownAdapter;

    /**
     * Create adapter with initial orgs
     *
     * @param context
     * @param orgs
     * @param avatarHelper
     */
    public HomeDropdownListAdapter(final Context context, final List<User> orgs, final AvatarLoader avatarHelper) {
        this.context = context;

        LayoutInflater inflater = LayoutInflater.from(context);
        User[] orgItems = orgs.toArray(new User[orgs.size()]);

        listAdapter = new OrgListAdapter(layout.org_item, inflater, orgItems, avatarHelper);
        dropdownAdapter = new OrgListAdapter(layout.org_dropdown_item, inflater, orgItems, avatarHelper);
    }

    /**
     * Is the given position an org. selection position?
     *
     * @param position
     * @return true if org., false otherwise
     */
    public boolean isOrgPosition(final int position) {
        return position < listAdapter.getCount();
    }

    /**
     * Get action at given position
     *
     * @param position
     * @return action id
     */
    public int getAction(final int position) {
        return position - listAdapter.getCount();
    }

    /**
     * Set orgs to display
     *
     * @param orgs
     * @return this adapter
     */
    public HomeDropdownListAdapter setOrgs(final List<User> orgs) {
        User[] orgItems = orgs.toArray(new User[orgs.size()]);
        listAdapter.setItems(orgItems);
        dropdownAdapter.setItems(orgItems);
        notifyDataSetChanged();
        return this;
    }

    /**
     * @param selected
     * @return this adapter
     */
    public HomeDropdownListAdapter setSelected(int selected) {
        this.selected = selected;
        return this;
    }

    /**
     * @return selected
     */
    public int getSelected() {
        return selected;
    }

    @Override
    public int getCount() {
        return listAdapter.getCount() > 0 ? listAdapter.getCount() + 3 : 0;
    }

    @Override
    public Object getItem(int position) {
        switch (getAction(position)) {
        case ACTION_GISTS:
            return context.getString(string.gists);
        case ACTION_DASHBOARD:
            return context.getString(string.issue_dashboard);
        case ACTION_FILTERS:
            return context.getString(string.issue_filters);
        default:
            return listAdapter.getItem(position);
        }
    }

    @Override
    public long getItemId(int position) {
        switch (getAction(position)) {
        case ACTION_GISTS:
        case ACTION_DASHBOARD:
        case ACTION_FILTERS:
            return getItem(position).hashCode();
        default:
            return listAdapter.getItemId(position);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getAction(position)) {
        case ACTION_GISTS:
        case ACTION_DASHBOARD:
        case ACTION_FILTERS:
            return listAdapter.getView(selected, null, parent);
        default:
            return listAdapter.getView(position, null, parent);
        }
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        switch (getAction(position)) {
        case ACTION_GISTS:
        case ACTION_DASHBOARD:
        case ACTION_FILTERS:
            Object item = getItem(position);
            View root = LayoutInflater.from(context).inflate(layout.context_dropdown_item, null);
            ((TextView) root.findViewById(id.tv_item_name)).setText(item.toString());
            return root;
        default:
            return dropdownAdapter.getDropDownView(position, null, parent);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/383.java