error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1010.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1010.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1010.java
text:
```scala
r@@eturn Base64.encodeToString(avatarUrl.getBytes(), Base64.NO_WRAP);

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

import static android.graphics.Bitmap.CompressFormat.PNG;
import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.view.View.VISIBLE;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.actionbarsherlock.app.ActionBar;
import com.github.kevinsawicki.http.HttpRequest;
import com.github.mobile.R.drawable;
import com.github.mobile.R.id;
import com.github.mobile.core.search.SearchUser;
import com.google.inject.Inject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.egit.github.core.CommitUser;
import org.eclipse.egit.github.core.Contributor;
import org.eclipse.egit.github.core.User;

import roboguice.util.RoboAsyncTask;

/**
 * Avatar utilities
 */
public class AvatarLoader {

    private static final String TAG = "AvatarLoader";

    private static final float CORNER_RADIUS_IN_DIP = 3;

    private static final int CACHE_SIZE = 75;

    private static abstract class FetchAvatarTask extends
            RoboAsyncTask<BitmapDrawable> {

        private static final Executor EXECUTOR = Executors
                .newFixedThreadPool(1);

        private FetchAvatarTask(Context context) {
            super(context, EXECUTOR);
        }

        @Override
        protected void onException(Exception e) throws RuntimeException {
            Log.d(TAG, "Avatar load failed", e);
        }
    }

    private final float cornerRadius;

    private final Map<Object, BitmapDrawable> loaded = new LinkedHashMap<Object, BitmapDrawable>(
            CACHE_SIZE, 1.0F) {

        private static final long serialVersionUID = -4191624209581976720L;

        @Override
        protected boolean removeEldestEntry(
                Map.Entry<Object, BitmapDrawable> eldest) {
            return size() >= CACHE_SIZE;
        }
    };

    private final Context context;

    private final File avatarDir;

    private final Drawable loadingAvatar;

    private final Options options;

    /**
     * Create avatar helper
     *
     * @param context
     */
    @Inject
    public AvatarLoader(final Context context) {
        this.context = context;

        loadingAvatar = context.getResources().getDrawable(
                drawable.gravatar_icon);

        avatarDir = new File(context.getCacheDir(), "avatars/github.com");
        if (!avatarDir.isDirectory())
            avatarDir.mkdirs();

        float density = context.getResources().getDisplayMetrics().density;
        cornerRadius = CORNER_RADIUS_IN_DIP * density;

        options = new Options();
        options.inDither = false;
        options.inPreferredConfig = ARGB_8888;
    }

    private BitmapDrawable getImageBy(final String userId, final String filename) {
        File avatarFile = new File(avatarDir + "/" + userId, filename);

        if (!avatarFile.exists() || avatarFile.length() == 0)
            return null;

        Bitmap bitmap = decode(avatarFile);
        if (bitmap != null)
            return new BitmapDrawable(context.getResources(), bitmap);
        else {
            avatarFile.delete();
            return null;
        }
    }

    private void deleteCachedUserAvatars(File userAvatarDir) {
        if (!userAvatarDir.isDirectory())
            return;

        for (File userAvatar : userAvatarDir.listFiles()) {
            userAvatar.delete();
        }
    }

    private Bitmap decode(final File file) {
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    private String getAvatarFilenameForUrl(String avatarUrl) {
        return Base64.encodeToString(avatarUrl.getBytes(), Base64.DEFAULT);
    }

    /**
     * Fetch avatar from URL
     *
     * @param url
     * @param cachedAvatarFilename
     * @return bitmap
     */
    protected BitmapDrawable fetchAvatar(final String url,
            final String userId, final String cachedAvatarFilename) {
        File userAvatarDir = new File(avatarDir, userId);
        if (!userAvatarDir.isDirectory())
            userAvatarDir.mkdirs();
        else
            deleteCachedUserAvatars(userAvatarDir);

        File rawAvatar = new File(userAvatarDir, cachedAvatarFilename + "-raw");
        HttpRequest request = HttpRequest.get(url);
        if (request.ok())
            request.receive(rawAvatar);

        if (!rawAvatar.exists() || rawAvatar.length() == 0)
            return null;

        Bitmap bitmap = decode(rawAvatar);
        if (bitmap == null) {
            rawAvatar.delete();
            return null;
        }

        bitmap = ImageUtils.roundCorners(bitmap, cornerRadius);
        if (bitmap == null) {
            rawAvatar.delete();
            return null;
        }

        File roundedAvatar = new File(userAvatarDir, cachedAvatarFilename);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(roundedAvatar);
            if (bitmap.compress(PNG, 100, output))
                return new BitmapDrawable(context.getResources(), bitmap);
            else
                return null;
        } catch (IOException e) {
            Log.d(TAG, "Exception writing rounded avatar", e);
            return null;
        } finally {
            if (output != null)
                try {
                    output.close();
                } catch (IOException e) {
                    // Ignored
                }
            rawAvatar.delete();
        }
    }

    /**
     * Sets the logo on the {@link ActionBar} to the user's avatar.
     *
     * @param actionBar
     * @param user
     * @return this helper
     */
    public AvatarLoader bind(final ActionBar actionBar, final User user) {
        return bind(actionBar, new AtomicReference<User>(user));
    }

    /**
     * Sets the logo on the {@link ActionBar} to the user's avatar.
     *
     * @param actionBar
     * @param userReference
     * @return this helper
     */
    public AvatarLoader bind(final ActionBar actionBar,
            final AtomicReference<User> userReference) {
        if (userReference == null)
            return this;

        final User user = userReference.get();
        final String userId = getId(user);
        if (userId == null)
            return this;

        final String avatarUrl = user.getAvatarUrl();
        if (TextUtils.isEmpty(avatarUrl))
            return this;

        BitmapDrawable loadedImage = loaded.get(userId);
        if (loadedImage != null) {
            actionBar.setLogo(loadedImage);
            return this;
        }

        new FetchAvatarTask(context) {

            @Override
            public BitmapDrawable call() throws Exception {
                final String avatarFilename = getAvatarFilenameForUrl(getAvatarUrl(user));
                final BitmapDrawable image = getImageBy(userId, avatarFilename);
                if (image != null)
                    return image;
                else
                    return fetchAvatar(avatarUrl, userId, avatarFilename);
            }

            @Override
            protected void onSuccess(BitmapDrawable image) throws Exception {
                if (userId.equals(getId(userReference.get())))
                    actionBar.setLogo(image);
            }
        }.execute();

        return this;
    }

    private AvatarLoader setImage(final Drawable image, final ImageView view) {
        return setImage(image, view, null);
    }

    private AvatarLoader setImage(final Drawable image, final ImageView view,
            Object tag) {
        view.setImageDrawable(image);
        view.setTag(id.iv_avatar, tag);
        view.setVisibility(VISIBLE);
        return this;
    }

    private String getAvatarUrl(String id) {
        if (!TextUtils.isEmpty(id))
            return "https://secure.gravatar.com/avatar/" + id + "?d=404";
        else
            return null;
    }

    private String getAvatarUrl(User user) {
        String avatarUrl = user.getAvatarUrl();
        if (TextUtils.isEmpty(avatarUrl)) {
            String gravatarId = user.getGravatarId();
            if (TextUtils.isEmpty(gravatarId))
                gravatarId = GravatarUtils.getHash(user.getEmail());
            avatarUrl = getAvatarUrl(gravatarId);
        }
        return avatarUrl;
    }

    private String getAvatarUrl(CommitUser user) {
        return getAvatarUrl(GravatarUtils.getHash(user.getEmail()));
    }

    private String getId(final User user) {
        if (user == null)
            return null;

        int id = user.getId();
        if (id > 0)
            return Integer.toString(id);

        String gravatarId = user.getGravatarId();
        if (!TextUtils.isEmpty(gravatarId))
            return gravatarId;
        else
            return GravatarUtils.getHash(user.getEmail());
    }

    /**
     * Bind view to image at URL
     *
     * @param view
     * @param user
     * @return this helper
     */
    public AvatarLoader bind(final ImageView view, final User user) {
        final String userId = getId(user);
        if (userId == null)
            return setImage(loadingAvatar, view);

        final String avatarUrl = getAvatarUrl(user);
        if (TextUtils.isEmpty(avatarUrl))
            return setImage(loadingAvatar, view);

        BitmapDrawable loadedImage = loaded.get(userId);
        if (loadedImage != null)
            return setImage(loadedImage, view);

        setImage(loadingAvatar, view, userId);
        fetchAvatarTask(avatarUrl, userId, view).execute();

        return this;
    }

    /**
     * Bind view to image at URL
     *
     * @param view
     * @param user
     * @return this helper
     */
    public AvatarLoader bind(final ImageView view, final CommitUser user) {
        if (user == null)
            return setImage(loadingAvatar, view);

        final String avatarUrl = getAvatarUrl(user);

        if (TextUtils.isEmpty(avatarUrl))
            return setImage(loadingAvatar, view);

        final String userId = user.getEmail();

        BitmapDrawable loadedImage = loaded.get(userId);
        if (loadedImage != null)
            return setImage(loadedImage, view);

        setImage(loadingAvatar, view, userId);
        fetchAvatarTask(avatarUrl, userId, view).execute();

        return this;
    }

    /**
     * Bind view to image at URL
     *
     * @param view
     * @param contributor
     * @return this helper
     */
    public AvatarLoader bind(final ImageView view, final Contributor contributor) {
        if (contributor == null)
            return setImage(loadingAvatar, view);

        final String avatarUrl = contributor.getAvatarUrl();

        if (TextUtils.isEmpty(avatarUrl))
            return setImage(loadingAvatar, view);

        final String contributorId = contributor.getLogin();

        BitmapDrawable loadedImage = loaded.get(contributorId);
        if (loadedImage != null)
            return setImage(loadedImage, view);

        setImage(loadingAvatar, view, contributorId);
        fetchAvatarTask(avatarUrl, contributorId, view).execute();

        return this;
    }

    /**
     * Bind view to image at URL
     *
     * @param view
     * @param user
     * @return this helper
     */
    public AvatarLoader bind(final ImageView view, final SearchUser user) {
        if (user == null)
            return setImage(loadingAvatar, view);

        final String avatarUrl = getAvatarUrl(user.getGravatarId());

        if (TextUtils.isEmpty(avatarUrl))
            return setImage(loadingAvatar, view);

        final String userId = user.getId();

        BitmapDrawable loadedImage = loaded.get(userId);
        if (loadedImage != null)
            return setImage(loadedImage, view);

        setImage(loadingAvatar, view, userId);
        fetchAvatarTask(avatarUrl, userId, view).execute();

        return this;
    }

    private FetchAvatarTask fetchAvatarTask(final String avatarUrl,
            final String userId, final ImageView view) {
        return new FetchAvatarTask(context) {

            @Override
            public BitmapDrawable call() throws Exception {
                if (!userId.equals(view.getTag(id.iv_avatar)))
                    return null;

                final String avatarFilename = getAvatarFilenameForUrl(avatarUrl);
                final BitmapDrawable image = getImageBy(userId, avatarFilename);
                if (image != null)
                    return image;
                else
                    return fetchAvatar(avatarUrl, userId, avatarFilename);
            }

            @Override
            protected void onSuccess(final BitmapDrawable image) throws Exception {
                if (image == null)
                    return;
                loaded.put(userId, image);
                if (userId.equals(view.getTag(id.iv_avatar)))
                    setImage(image, view);
            }
        };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1010.java