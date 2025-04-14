error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4604.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4604.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4604.java
text:
```scala
protected b@@oolean removeEldestEntry(Map.Entry<Integer, Bitmap> eldest) {

package com.github.mobile.android.util;

import static android.view.View.VISIBLE;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.actionbarsherlock.app.ActionBar;
import com.github.kevinsawicki.http.HttpRequest;
import com.github.mobile.android.R.drawable;
import com.github.mobile.android.R.id;
import com.google.inject.Inject;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.eclipse.egit.github.core.User;

import roboguice.util.RoboAsyncTask;

/**
 * Avatar utilities
 */
public class AvatarHelper {

    private static final String TAG = "AvatarHelper";

    private static final float CORNER_RADIUS_IN_DIP = 6;

    private static final int LOGO_WIDTH = 28;

    private static final int CACHE_SIZE = 50;

    private static abstract class FetchAvatarTask extends RoboAsyncTask<Bitmap> {

        private static final Executor EXECUTOR = Executors.newFixedThreadPool(2);

        private FetchAvatarTask(Context context) {
            super(context, EXECUTOR);
        }

        @Override
        protected void onException(Exception e) throws RuntimeException {
            Log.d(TAG, "Avatar load failed", e);
        }
    }

    private final float cornerRadius;

    private final int logoWidth;

    private final Map<Integer, Bitmap> loaded = new LinkedHashMap<Integer, Bitmap>(50, 1.0F) {

        private static final long serialVersionUID = -4191624209581976720L;

        @Override
        protected boolean removeEldestEntry(Entry<Integer, Bitmap> eldest) {
            return size() >= CACHE_SIZE;
        }
    };

    private final Context context;

    private final File avatarDir;

    private final Drawable loadingAvatar;

    /**
     * Create avatar helper
     *
     * @param context
     */
    @Inject
    public AvatarHelper(final Context context) {
        this.context = context;

        loadingAvatar = context.getResources().getDrawable(drawable.gravatar_icon);

        avatarDir = new File(context.getCacheDir(), "avatars/github.com");
        if (!avatarDir.isDirectory())
            avatarDir.mkdirs();

        float density = context.getResources().getDisplayMetrics().density;
        cornerRadius = CORNER_RADIUS_IN_DIP * density;
        logoWidth = (int) Math.ceil(LOGO_WIDTH * density);
    }

    /**
     * Create bitmap from raw image and set to view
     *
     * @param image
     * @param view
     * @param user
     * @return this helper
     */
    protected AvatarHelper setImage(final Bitmap image, final ImageView view, final User user) {
        if (!Integer.valueOf(user.getId()).equals(view.getTag(id.iv_gravatar)))
            return this;

        view.setTag(id.iv_gravatar, null);

        if (image != null) {
            loaded.put(user.getId(), image);
            view.setImageBitmap(image);
            view.setVisibility(VISIBLE);
        }

        return this;
    }

    /**
     * Get image for user
     *
     * @param user
     * @return image
     */
    protected Bitmap getImage(final User user) {
        File avatarFile = new File(avatarDir, Integer.toString(user.getId()));

        if (avatarFile.exists() && avatarFile.length() > 0)
            return BitmapFactory.decodeFile(avatarFile.getAbsolutePath());
        else
            return null;
    }

    /**
     * Fetch avatar from URL
     *
     * @param url
     * @param userId
     * @return bitmap
     */
    protected synchronized Bitmap fetchAvatar(final String url, final Integer userId) {
        HttpRequest request = HttpRequest.get(url);
        if (!request.ok())
            return null;

        File avatarFile = new File(avatarDir, userId.toString());
        request.receive(avatarFile);

        if (!avatarFile.exists() || avatarFile.length() == 0)
            return null;

        Bitmap content = BitmapFactory.decodeFile(avatarFile.getAbsolutePath());
        content = Image.roundCorners(content, cornerRadius);
        return content;
    }

    /**
     * Sets the logo on the {@link ActionBar} to the user's avatar.
     *
     * @param actionBar
     * @param user
     * @return this helper
     */
    public AvatarHelper bind(final ActionBar actionBar, final User user) {
        if (user == null)
            return this;

        final String avatarUrl = user.getAvatarUrl();
        if (TextUtils.isEmpty(avatarUrl))
            return this;

        final Integer userId = Integer.valueOf(user.getId());

        Bitmap loadedImage = loaded.get(userId);
        if (loadedImage != null) {
            BitmapDrawable drawable = new BitmapDrawable(context.getResources(), loadedImage);
            drawable.setBounds(0, 0, logoWidth, logoWidth);
            actionBar.setLogo(drawable);
            return this;
        }

        new FetchAvatarTask(context) {

            @Override
            public Bitmap call() throws Exception {
                Bitmap bitmap = getImage(user);
                if (bitmap != null)
                    return bitmap;
                else
                    return fetchAvatar(avatarUrl, userId);
            }

            @Override
            protected void onSuccess(Bitmap image) throws Exception {
                if (image != null)
                    actionBar.setLogo(new BitmapDrawable(context.getResources(), image));
            }
        }.execute();

        return this;
    }

    /**
     * Bind view to image at URL
     *
     * @param view
     * @param user
     * @return this helper
     */
    public AvatarHelper bind(final ImageView view, final User user) {
        if (user == null) {
            view.setImageDrawable(loadingAvatar);
            return this;
        }

        final String avatarUrl = user.getAvatarUrl();
        if (TextUtils.isEmpty(avatarUrl)) {
            view.setImageDrawable(loadingAvatar);
            return this;
        }

        final Integer userId = Integer.valueOf(user.getId());

        Bitmap loadedImage = loaded.get(userId);
        if (loadedImage != null) {
            view.setImageBitmap(loadedImage);
            view.setVisibility(VISIBLE);
            view.setTag(id.iv_gravatar, null);
            return this;
        }

        view.setImageDrawable(loadingAvatar);
        view.setTag(id.iv_gravatar, userId);

        new FetchAvatarTask(context) {

            @Override
            public Bitmap call() throws Exception {
                if (!userId.equals(view.getTag(id.iv_gravatar)))
                    return null;

                Bitmap bitmap = getImage(user);
                if (bitmap != null)
                    return bitmap;
                else
                    return fetchAvatar(avatarUrl, userId);
            }

            @Override
            protected void onSuccess(Bitmap image) throws Exception {
                setImage(image, view, user);
            }

        }.execute();

        return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4604.java