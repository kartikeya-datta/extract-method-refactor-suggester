error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1945.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1945.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1945.java
text:
```scala
 "sh".@@equals(extension) //

package com.github.mobile.android.util;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Utilities for displaying source code in a {@link WebView}
 */
public class SourceEditor {

    /**
     * Does the source editor have a highlighter set to match the given file name extension?
     *
     * @param extension
     * @return true if highlighting available, false otherwise
     */
    public static boolean isValid(String extension) {
        return "actionscript3".equals(extension) //
 "applescript".equals(extension) //
 "as3".equals(extension) //
 "bash".equals(extension) //
 "c".equals(extension) //
 "cf".equals(extension) //
 "coldfusion".equals(extension) //
 "cpp".equals(extension) //
 "cs".equals(extension) //
 "css".equals(extension) //
 "delphi".equals(extension) //
 "diff".equals(extension) //
 "erl".equals(extension) //
 "erlang".equals(extension) //
 "groovy".equals(extension) //
 "html".equals(extension) //
 "java".equals(extension) //
 "js".equals(extension) //
 "pas".equals(extension) //
 "pascal".equals(extension) //
 "patch".equals(extension) //
 "pl".equals(extension) //
 "php".equals(extension) //
 "py".equals(extension) //
 "rb".equals(extension) //
 "sass".equals(extension) //
 "scala".equals(extension) //
 "scss".equals(extension) //
 "shell ".equals(extension) //
 "sql".equals(extension) //
 "txt".equals(extension) //
 "vb".equals(extension) //
 "vbnet".equals(extension) //
 "xhtml".equals(extension) //
 "xml".equals(extension) //
 "xslt".equals(extension);
    }

    /**
     * Bind {@link Object#toString()} to given {@link WebView}
     *
     * @param view
     * @param name
     * @param provider
     * @return view
     */
    public static WebView showSource(WebView view, String name, final Object provider) {
        view.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
        int suffix = name.lastIndexOf('.');
        String ext = null;
        if (suffix != -1 && suffix + 2 < name.length()) {
            ext = name.substring(suffix + 1);
            if (!isValid(ext))
                ext = null;
        }
        if (ext == null)
            ext = "txt";
        final String brush = "brush: " + ext + ";";
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setBuiltInZoomControls(true);
        view.addJavascriptInterface(new Object() {
            public String toString() {
                return "<script type=\"syntaxhighlighter\" class=\"toolbar:false;" + brush + "\"><![CDATA[\n"
                        + provider.toString() + "\n]]></script>";
            }

        }, "SourceProvider");
        view.loadUrl("file:///android_asset/source-editor.html");
        return view;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1945.java