error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3747.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3747.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3747.java
text:
```scala
c@@ontext.startActivity(ViewIssueActivity.createIntent(issue));

package com.github.mobile.android.util;

import static android.content.Intent.ACTION_VIEW;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.mobile.android.core.gist.GistUrlMatcher;
import com.github.mobile.android.core.issue.IssueUrlMatcher;
import com.github.mobile.android.gist.ViewGistsActivity;
import com.github.mobile.android.issue.ViewIssueActivity;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.Issue;

/**
 * Helper to display an HTML block in a {@link WebView}
 */
public class HtmlViewer implements Runnable {

    private static final String URL_PAGE = "file:///android_asset/html-viewer.html";

    private static final String URL_RELOAD = "javascript:reloadHtml()";

    private static final String URL_UPDATE_HEIGHT = "javascript:updateHeight()";

    private final IssueUrlMatcher issueMatcher = new IssueUrlMatcher();

    private final GistUrlMatcher gistMatcher = new GistUrlMatcher();

    private boolean inLoad;

    private int height;

    private boolean loaded;

    private final WebView view;

    private final float scale;

    private String html = "";

    /**
     * Create viewer
     *
     * @param view
     */
    public HtmlViewer(final WebView view) {
        this.view = view;
        view.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals(URL_PAGE)) {
                    view.loadUrl(url);
                    return false;
                } else {
                    loadExternalUrl(view.getContext(), url);
                    return true;
                }
            }

            public void onPageFinished(WebView view, String url) {
                loaded = true;
            }
        });
        view.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100)
                    view.loadUrl(URL_UPDATE_HEIGHT);
            }
        });
        view.setHorizontalScrollBarEnabled(false);
        view.setVerticalScrollBarEnabled(false);
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setBuiltInZoomControls(false);
        view.addJavascriptInterface(this, "HtmlViewer");
        view.loadUrl(URL_PAGE);
        scale = view.getScale();
    }

    private void loadExternalUrl(final Context context, final String url) {
        int issueNumber = issueMatcher.getNumber(url);
        if (issueNumber > 0) {
            Issue issue = new Issue();
            issue.setNumber(issueNumber);
            issue.setHtmlUrl(url);
            context.startActivity(ViewIssueActivity.viewIssueIntentFor(issue));
            return;
        }

        String gistId = gistMatcher.getId(url);
        if (gistId != null) {
            Gist gist = new Gist().setId(gistId).setHtmlUrl(url);
            context.startActivity(ViewGistsActivity.createIntent(gist));
            return;
        }

        context.startActivity(new Intent(ACTION_VIEW, Uri.parse(url)));
    }

    /**
     * Set HTML to display
     *
     * @param html
     * @return this viewer
     */
    public HtmlViewer setHtml(String html) {
        if (html == null)
            html = "";
        if (!this.html.equals(html)) {
            this.html = html;
            if (loaded) {
                inLoad = true;
                view.loadUrl(URL_RELOAD);
                inLoad = false;
                run();
            }
        }
        return this;
    }

    /**
     * @return html
     */
    public String getHtml() {
        return html;
    }

    /**
     * @return view
     */
    public WebView getView() {
        return view;
    }

    /**
     * Update height
     *
     * @param height
     */
    public void setHeight(final int height) {
        int newHeight = view.getPaddingTop() + Math.round(height * scale + 0.5F) + view.getPaddingBottom();
        this.height = newHeight;
        if (!inLoad && view.getLayoutParams().height != newHeight)
            view.post(this);
    }

    public void run() {
        int newHeight = height;
        int currentHeight = view.getLayoutParams().height;
        if (newHeight == currentHeight)
            return;
        view.getLayoutParams().height = newHeight;
        if (!view.isLayoutRequested())
            view.requestLayout();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3747.java