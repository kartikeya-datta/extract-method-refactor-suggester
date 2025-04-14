error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3101.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3101.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3101.java
text:
```scala
public static final S@@tring ICON_CODE = "\uf010";

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
package com.github.mobile.util;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Helpers for dealing with custom typefaces and measuring text to display
 */
public class TypefaceUtils {

    /**
     * Private repository icon
     */
    public static final String ICON_PRIVATE = "\uf26a";

    /**
     * Public repository icon
     */
    public static final String ICON_PUBLIC = "\uf201";

    /**
     * Fork icon
     */
    public static final String ICON_FORK = "\uf202";

    /**
     * Create icon
     */
    public static final String ICON_CREATE = "\uf203";

    /**
     * Delete icon
     */
    public static final String ICON_DELETE = "\uf204";

    /**
     * Push icon
     */
    public static final String ICON_PUSH = "\uf205";

    /**
     * Wiki icon
     */
    public static final String ICON_WIKI = "\uf207";

    /**
     * Upload icon
     */
    public static final String ICON_UPLOAD = "\uf20C";

    /**
     * Gist icon
     */
    public static final String ICON_GIST = "\uf20E";

    /**
     * Add member icon
     */
    public static final String ICON_ADD_MEMBER = "\uf21A";

    /**
     * Public mirror repository icon
     */
    public static final String ICON_MIRROR_PUBLIC = "\uf224";

    /**
     * Public mirror repository icon
     */
    public static final String ICON_MIRROR_PRIVATE = "\uf225";

    /**
     * Follow icon
     */
    public static final String ICON_FOLLOW = "\uf21C";

    /**
     * Star icon
     */
    public static final String ICON_STAR = "\uf02A";

    /**
     * Pull request icon
     */
    public static final String ICON_PULL_REQUEST = "\uf222";

    /**
     * Issue open icon
     */
    public static final String ICON_ISSUE_OPEN = "\uf226";

    /**
     * Issue reopen icon
     */
    public static final String ICON_ISSUE_REOPEN = "\uf227";

    /**
     * Issue close icon
     */
    public static final String ICON_ISSUE_CLOSE = "\uf228";

    /**
     * Issue comment icon
     */
    public static final String ICON_ISSUE_COMMENT = "\uf229";

    /**
     * Comment icon
     */
    public static final String ICON_COMMENT = "\uf22b";

    /**
     * News icon
     */
    public static final String ICON_NEWS = "\uf234";

    /**
     * Watch icon
     */
    public static final String ICON_WATCH = "\uf04e";

    /**
     * Team icon
     */
    public static final String ICON_TEAM = "\uf019";

    /**
     * Code icon
     */
    public static final String ICON_CODE = "\uf011";

    /**
     * Commit icon
     */
    public static final String ICON_COMMIT = "\uf01f";

    /**
     * Person icon
     */
    public static final String ICON_PERSON = "\uf218";

    /**
     * Add icon
     */
    public static final String ICON_ADD = "\uf05d";

    /**
     * Broadcast icon
     */
    public static final String ICON_BROADCAST = "\uf030";

    /**
     * Find the maximum number of digits in the given numbers
     *
     * @param numbers
     * @return max digits
     */
    public static int getMaxDigits(int... numbers) {
        int max = 1;
        for (int number : numbers)
            max = Math.max(max, (int) Math.log10(number) + 1);
        return max;
    }

    /**
     * Get width of number of digits
     *
     * @param view
     * @param numberOfDigits
     * @return number width
     */
    public static int getWidth(TextView view, int numberOfDigits) {
        Paint paint = new Paint();
        paint.setTypeface(view.getTypeface());
        paint.setTextSize(view.getTextSize());
        char[] text = new char[numberOfDigits];
        Arrays.fill(text, '0');
        return Math.round(paint.measureText(text, 0, text.length));
    }

    /**
     * Get octicons typeface
     *
     * @param context
     * @return octicons typeface
     */
    public static Typeface getOcticons(final Context context) {
        return getTypeface(context, "octicons-regular-webfont.ttf");
    }

    /**
     * Set octicons typeface on given text view(s)
     *
     * @param textViews
     */
    public static void setOcticons(final TextView... textViews) {
        if (textViews == null || textViews.length == 0)
            return;

        Typeface typeface = getOcticons(textViews[0].getContext());
        for (TextView textView : textViews)
            textView.setTypeface(typeface);
    }

    /**
     * Get typeface with name
     *
     * @param context
     * @param name
     * @return typeface
     */
    public static Typeface getTypeface(final Context context, final String name) {
        return Typeface.createFromAsset(context.getAssets(), name);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3101.java