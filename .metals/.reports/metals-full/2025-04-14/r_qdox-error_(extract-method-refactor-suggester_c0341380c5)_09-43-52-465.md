error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5862.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5862.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5862.java
text:
```scala
r@@eturn html.contains("<img");

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

import static android.util.Base64.DEFAULT;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.lang.Integer.MAX_VALUE;
import static org.eclipse.egit.github.core.client.IGitHubConstants.HOST_DEFAULT;
import android.accounts.Account;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html.ImageGetter;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.TextView;

import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.http.HttpRequest.HttpRequestException;
import com.github.mobile.R.drawable;
import com.github.mobile.accounts.AuthenticatedUserTask;
import com.google.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.ContentsService;

/**
 * Getter for an image
 */
public class HttpImageGetter implements ImageGetter {

    private static class LoadingImageGetter implements ImageGetter {

        private final Drawable image;

        private LoadingImageGetter(final Context context, final int size) {
            int imageSize = ServiceUtils.getIntPixels(context, size);
            image = context.getResources().getDrawable(
                    drawable.image_loading_icon);
            image.setBounds(0, 0, imageSize, imageSize);
        }

        @Override
        public Drawable getDrawable(String source) {
            return image;
        }
    }

    private static boolean containsImages(final String html) {
        return html.indexOf("<img") != -1;
    }

    private final LoadingImageGetter loading;

    private final Context context;

    private final File dir;

    private final int width;

    private final Map<Object, CharSequence> rawHtmlCache = new HashMap<Object, CharSequence>();

    private final Map<Object, CharSequence> fullHtmlCache = new HashMap<Object, CharSequence>();

    private final ContentsService service;

    /**
     * Create image getter for context
     *
     * @param context
     * @param service
     */
    @Inject
    public HttpImageGetter(Context context, ContentsService service) {
        this.context = context;
        this.service = service;
        dir = context.getCacheDir();
        width = ServiceUtils.getDisplayWidth(context);
        loading = new LoadingImageGetter(context, 24);
    }

    private HttpImageGetter show(final TextView view, final CharSequence html) {
        if (TextUtils.isEmpty(html))
            return hide(view);

        view.setText(html);
        view.setVisibility(VISIBLE);
        view.setTag(null);
        return this;
    }

    private HttpImageGetter hide(final TextView view) {
        view.setText(null);
        view.setVisibility(GONE);
        view.setTag(null);
        return this;
    }

    /**
     * Encode given HTML string and map it to the given id
     *
     * @param id
     * @param html
     * @return this image getter
     */
    public HttpImageGetter encode(final Object id, final String html) {
        if (TextUtils.isEmpty(html))
            return this;

        CharSequence encoded = HtmlUtils.encode(html, loading);
        // Use default encoding if no img tags
        if (containsImages(html))
            rawHtmlCache.put(id, encoded);
        else {
            rawHtmlCache.remove(id);
            fullHtmlCache.put(id, encoded);
        }
        return this;
    }

    /**
     * Bind text view to HTML string
     *
     * @param view
     * @param html
     * @param id
     * @return this image getter
     */
    public HttpImageGetter bind(final TextView view, final String html,
            final Object id) {
        if (TextUtils.isEmpty(html))
            return hide(view);

        CharSequence encoded = fullHtmlCache.get(id);
        if (encoded != null)
            return show(view, encoded);

        encoded = rawHtmlCache.get(id);
        if (encoded == null) {
            encoded = HtmlUtils.encode(html, loading);
            if (containsImages(html))
                rawHtmlCache.put(id, encoded);
            else {
                rawHtmlCache.remove(id);
                fullHtmlCache.put(id, encoded);
                return show(view, encoded);
            }
        }

        if (TextUtils.isEmpty(encoded))
            return hide(view);

        show(view, encoded);
        view.setTag(id);
        new AuthenticatedUserTask<CharSequence>(context) {

            @Override
            protected CharSequence run(Account account) throws Exception {
                return HtmlUtils.encode(html, HttpImageGetter.this);
            }

            @Override
            protected void onSuccess(final CharSequence html) throws Exception {
                rawHtmlCache.remove(id);
                fullHtmlCache.put(id, html);

                if (id.equals(view.getTag()))
                    show(view, html);
            }
        }.execute();
        return this;
    }

    /**
     * Request an image using the contents API if the source URI is a path to a
     * file already in the repository
     *
     * @param source
     * @return
     * @throws IOException
     */
    private Drawable requestRepositoryImage(final String source)
            throws IOException {
        if (TextUtils.isEmpty(source))
            return null;

        Uri uri = Uri.parse(source);
        if (!HOST_DEFAULT.equals(uri.getHost()))
            return null;

        List<String> segments = uri.getPathSegments();
        if (segments.size() < 5)
            return null;

        String prefix = segments.get(2);
        // Two types of urls supported:
        // github.com/github/android/raw/master/app/res/drawable-xhdpi/app_icon.png
        // github.com/github/android/blob/master/app/res/drawable-xhdpi/app_icon.png?raw=true
        if (!("raw".equals(prefix) || ("blob".equals(prefix) && !TextUtils
                .isEmpty(uri.getQueryParameter("raw")))))
            return null;

        String owner = segments.get(0);
        if (TextUtils.isEmpty(owner))
            return null;
        String name = segments.get(1);
        if (TextUtils.isEmpty(name))
            return null;
        String branch = segments.get(3);
        if (TextUtils.isEmpty(branch))
            return null;

        StringBuilder path = new StringBuilder(segments.get(4));
        for (int i = 5; i < segments.size(); i++) {
            String segment = segments.get(i);
            if (!TextUtils.isEmpty(segment))
                path.append('/').append(segment);
        }

        if (TextUtils.isEmpty(path))
            return null;

        List<RepositoryContents> contents = service.getContents(
                RepositoryId.create(owner, name), path.toString(), branch);
        if (contents != null && contents.size() == 1) {
            byte[] content = Base64.decode(contents.get(0).getContent(),
                    DEFAULT);
            Bitmap bitmap = ImageUtils.getBitmap(content, width, MAX_VALUE);
            if (bitmap == null)
                return loading.getDrawable(source);
            BitmapDrawable drawable = new BitmapDrawable(
                    context.getResources(), bitmap);
            drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            return drawable;
        } else
            return null;
    }

    @Override
    public Drawable getDrawable(final String source) {
        try {
            Drawable repositoryImage = requestRepositoryImage(source);
            if (repositoryImage != null)
                return repositoryImage;
        } catch (Exception e) {
            // Ignore and attempt request over regular HTTP request
        }

        File output = null;
        try {
            output = File.createTempFile("image", ".jpg", dir);
            HttpRequest request = HttpRequest.get(source);
            if (!request.ok())
                throw new IOException("Unexpected response code: "
                        + request.code());
            request.receive(output);
            Bitmap bitmap = ImageUtils.getBitmap(output, width, MAX_VALUE);
            if (bitmap == null)
                return loading.getDrawable(source);

            BitmapDrawable drawable = new BitmapDrawable(
                    context.getResources(), bitmap);
            drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            return drawable;
        } catch (IOException e) {
            return loading.getDrawable(source);
        } catch (HttpRequestException e) {
            return loading.getDrawable(source);
        } finally {
            if (output != null)
                output.delete();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5862.java