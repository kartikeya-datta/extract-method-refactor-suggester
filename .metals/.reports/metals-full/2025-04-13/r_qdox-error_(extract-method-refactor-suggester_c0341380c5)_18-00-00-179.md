error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6392.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6392.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6392.java
text:
```scala
s@@tartActivity(RepoBrowseActivity.createIntent(user));

package com.github.mobile.android;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mobile.android.R.layout;
import com.github.mobile.android.gist.GistsActivity;
import com.github.mobile.android.repo.RepoBrowseActivity;
import com.github.mobile.android.ui.WelcomeActivity;
import com.github.mobile.android.util.Avatar;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.User;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContextScopedProvider;
import roboguice.inject.InjectView;

/**
 * Home screen activity
 */
public class HomeActivity extends RoboActivity {

    private static final String TAG = "HA";
    private static final int CODE_LOGIN = 1;

    private class LinksListAdapter extends ArrayAdapter<String> {

        /**
         * @param objects
         */
        public LinksListAdapter(List<String> objects) {
            super(HomeActivity.this, R.layout.home_link_item, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final LinearLayout view = (LinearLayout) getLayoutInflater().inflate(layout.home_link_item, null);
            String name = getItem(position);
            ((TextView) view.findViewById(R.id.tv_home_link)).setText(name);
            ((ImageView) view.findViewById(R.id.iv_home_link)).setBackgroundResource(linkViews.get(name));
            return view;
        }
    }

    private class OrgListAdapter extends ArrayAdapter<User> {

        /**
         * @param objects
         */
        public OrgListAdapter(List<User> objects) {
            super(HomeActivity.this, R.layout.org_item, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final LinearLayout view = (LinearLayout) getLayoutInflater().inflate(layout.org_item, null);
            User user = getItem(position);
            ((TextView) view.findViewById(R.id.tv_org_name)).setText(user.getLogin());
            Avatar.bind(HomeActivity.this, ((ImageView) view.findViewById(R.id.iv_gravatar)), user);
            return view;
        }
    }

    private Map<String, Integer> linkViews = new LinkedHashMap<String, Integer>();

    @Inject
    private ContextScopedProvider<Account> accountProvider;

    @Inject
    private ContextScopedProvider<AccountDataManager> cache;

    @InjectView(R.id.lv_orgs)
    private ListView orgsList;

    @InjectView(R.id.lv_links)
    private ListView linksList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        linkViews.put("Dashboard", R.drawable.dashboard_icon);
        linkViews.put("Gists", R.drawable.gist_icon);

        linksList.setAdapter(new LinksListAdapter(new ArrayList<String>(linkViews.keySet())));
        linksList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                switch (position) {
                case 1:
                    startActivity(new Intent(HomeActivity.this, GistsActivity.class));
                    break;

                default:
                    break;
                }
            }
        });

        orgsList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> view, View arg1, int position, long id) {
                User user = (User) view.getItemAtPosition(position);
                startActivity(RepoBrowseActivity.createIntent(HomeActivity.this, user));
            }
        });
    }

    private void loadOrgs() {
        cache.get(this).getOrgs(new RequestFuture<List<User>>() {

            public void success(List<User> response) {
                orgsList.setAdapter(new OrgListAdapter(response));
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (CODE_LOGIN == requestCode && resultCode == RESULT_OK)
            loadOrgs();
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accountProvider.get(this) == null) {
            Log.d(TAG, "No account currently available, starting Welcome activity");
            startActivityForResult(new Intent(this, WelcomeActivity.class), CODE_LOGIN);
        } else {
            loadOrgs();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6392.java