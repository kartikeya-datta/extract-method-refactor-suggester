error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10239.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10239.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10239.java
text:
```scala
s@@tartActivityForResult(ViewGistActivity.createIntent(gist), REQUEST_VIEW);

package com.github.mobile.android.gist;

import android.R;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mobile.android.R.id;
import com.github.mobile.android.R.layout;
import com.github.mobile.android.R.menu;
import com.github.mobile.android.R.string;
import com.google.inject.Inject;

import java.util.Collection;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.service.GistService;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContextScopedProvider;
import roboguice.util.RoboAsyncTask;

/**
 * Activity to display a list of Gists
 */
public class GistsActivity extends RoboFragmentActivity implements OnItemClickListener {

    private static final int REQUEST_CREATE = 1;

    private static final int REQUEST_VIEW = REQUEST_CREATE + 1;

    @Inject
    private Context context;

    @Inject
    private ContextScopedProvider<GistService> serviceProvider;

    private GistsFragment gists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.gists);

        gists = (GistsFragment) getSupportFragmentManager().findFragmentById(R.id.list);
        if (gists == null) {
            gists = new GistsFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.list, gists).commit();
        }
        gists.setClickListener(this);
    }

    private void randomGist() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage(getString(string.random_gist));
        progress.show();
        new RoboAsyncTask<Gist>(context) {

            public Gist call() throws Exception {
                GistService service = serviceProvider.get(context);
                PageIterator<Gist> pages = service.pagePublicGists(1);
                pages.next();
                int randomPage = 1 + (int) (Math.random() * ((pages.getLastPage() - 1) + 1));
                Collection<Gist> gists = service.pagePublicGists(randomPage, 1).next();
                if (gists.isEmpty())
                    throw new IllegalArgumentException("No Gists found");
                return service.getGist(gists.iterator().next().getId());
            }

            protected void onSuccess(Gist gist) throws Exception {
                progress.cancel();
                startActivity(ViewGistActivity.createIntent(gist));
            }

            protected void onException(Exception e) throws RuntimeException {
                progress.cancel();
                Toast.makeText(context, e.getMessage(), 5000).show();
            }
        }.execute();
    }

    private void openGist() {
        Builder prompt = new Builder(context);

        prompt.setTitle("Open Gist");
        prompt.setMessage("Enter id:");

        final EditText id = new EditText(context);
        prompt.setView(id);

        prompt.setPositiveButton("Open", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                String gistId = id.getText().toString();
                startActivityForResult(ViewGistActivity.createIntent(gistId), REQUEST_VIEW);
            }
        });
        prompt.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu options) {
        getMenuInflater().inflate(menu.gists, options);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case id.open_gist:
            openGist();
            return true;
        case id.random_gist:
            randomGist();
            return true;
        case id.create_gist:
            startActivityForResult(new Intent(context, ShareGistActivity.class), REQUEST_CREATE);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CREATE && ShareGistActivity.RESULT_CREATED == resultCode) {
            gists.refresh();
            return;
        }
        if (requestCode == REQUEST_VIEW && ViewGistActivity.RESULT_DELETED == resultCode) {
            gists.refresh();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onItemClick(AdapterView<?> list, View view, int position, long id) {
        Gist gist = (Gist) list.getItemAtPosition(position);
        startActivityForResult(ViewGistActivity.createIntent(gist.getId()), REQUEST_VIEW);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10239.java