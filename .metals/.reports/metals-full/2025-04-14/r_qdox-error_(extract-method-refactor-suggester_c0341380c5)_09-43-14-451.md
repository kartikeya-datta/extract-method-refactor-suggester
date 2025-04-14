error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7748.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7748.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7748.java
text:
```scala
static b@@oolean DEBUG = true;

/*
 * Copyright 2010 Elijah Cornell
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 */

package com.badlogic.gdx.backends.android;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.surfaceview.GLBaseSurfaceViewLW;

import android.app.WallpaperManager;
import android.os.Bundle;
import android.service.wallpaper.WallpaperService;
import android.service.wallpaper.WallpaperService.Engine;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;


public abstract class AndroidLiveWallpaperService extends WallpaperService {
	final String TAG = "AndroidLiveWallpaperService";
	AndroidWallpaperEngine previousEngine;
	static boolean DEBUG = false;

	public AndroidLiveWallpaperService () {
		super();
	}

	@Override
	public void onCreate () {
		if (DEBUG) Log.d(TAG, " > LibdgxWallpaperService - onCreate()");
		super.onCreate();
	}

	@Override
	public Engine onCreateEngine () {
		return new AndroidWallpaperEngine(createListener(), createConfig());
	}
	
	/**
	 * @return a new {@link ApplicationListener} that implements the live wallpaper
	 */
	public abstract ApplicationListener createListener(); 
	
	/**
	 * @return a new {@link AndroidApplicationConfiguration} that specifies the config to be used for the live wall paper
	 */
	public abstract AndroidApplicationConfiguration createConfig();
	
	/**
	 * Called when the live wallpaper's offset changed. This method will be called
	 * on the rendering thread.
	 * @param xOffset
	 * @param yOffset
	 * @param xOffsetStep
	 * @param yOffsetStep
	 * @param xPixelOffset
	 * @param yPixelOffset
	 */
	public abstract void offsetChange (ApplicationListener listener, float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset);

	@Override
	public void onDestroy () {
		if (DEBUG) Log.d(TAG, " > LibdgxWallpaperService - onDestroy()");
		super.onDestroy();
	}
	
	public class AndroidWallpaperEngine extends Engine {
		protected final AndroidLiveWallpaper app;
		protected final ApplicationListener listener;
		protected GLBaseSurfaceViewLW view;

		public AndroidWallpaperEngine (ApplicationListener listener, AndroidApplicationConfiguration config) {
			if (AndroidLiveWallpaperService.DEBUG) Log.d(AndroidLiveWallpaperService.this.TAG, " > MyEngine() " + hashCode());
			this.app = new AndroidLiveWallpaper(AndroidLiveWallpaperService.this, this);
			this.app.initialize(listener, config);
			this.listener = listener;
			this.view = ((AndroidGraphicsLiveWallpaper)app.getGraphics()).getView();
			if(config.getTouchEventsForLiveWallpaper) {
//				this.setTouchEventsEnabled(true);
			}
		}

		@Override
		public Bundle onCommand (final String pAction, final int pX, final int pY, final int pZ, final Bundle pExtras,
			final boolean pResultRequested) {

			if (AndroidLiveWallpaperService.DEBUG)
				Log.d(TAG, " > onCommand(" + pAction + " " + pX + " " + pY + " " + pZ + " " + pExtras + " "
					+ pResultRequested + ")");

			if (pAction.equals(WallpaperManager.COMMAND_TAP)) {
				app.input.onTap(pX, pY);
			} else if (pAction.equals(WallpaperManager.COMMAND_DROP)) {
				app.input.onDrop(pX, pY);
			}
			return super.onCommand(pAction, pX, pY, pZ, pExtras, pResultRequested);
		}

		@Override
		public void onCreate (final SurfaceHolder surfaceHolder) {
			if (AndroidLiveWallpaperService.DEBUG) Log.d(TAG, " > onCreate() " + hashCode());
			super.onCreate(surfaceHolder);
			if (AndroidLiveWallpaperService.this.previousEngine != null) {
				AndroidLiveWallpaperService.this.previousEngine.view.onPause();
			}
			AndroidLiveWallpaperService.this.previousEngine = this;
			// FIXME
			// wallpaperListener.setIsPreview(this.isPreview());
		}

		@Override
		public void onDestroy () {
			if (AndroidLiveWallpaperService.DEBUG) Log.d(AndroidLiveWallpaperService.this.TAG, " > onDestroy() " + hashCode());
			view.onDestroy();
			super.onDestroy();
		}

		public void onPause () {
			if (AndroidLiveWallpaperService.DEBUG) Log.d(AndroidLiveWallpaperService.this.TAG, " > onPause() " + hashCode());
			app.onPause();
			view.onPause();
		}

		public void onResume () {
			if (AndroidLiveWallpaperService.DEBUG) Log.d(AndroidLiveWallpaperService.this.TAG, " > onResume() " + hashCode());
			app.onResume();
			view.onResume();
		}

		@Override
		public void onSurfaceChanged (final SurfaceHolder holder, final int format, final int width, final int height) {
			if (AndroidLiveWallpaperService.DEBUG)
				Log.d(AndroidLiveWallpaperService.this.TAG, " > onSurfaceChanged() " + isPreview() + " " + hashCode());
			super.onSurfaceChanged(holder, format, width, height);
		}

		@Override
		public void onSurfaceCreated (final SurfaceHolder holder) {
			if (AndroidLiveWallpaperService.DEBUG) Log.d(AndroidLiveWallpaperService.this.TAG, " > onSurfaceCreated() " + hashCode());
			super.onSurfaceCreated(holder);
		}

		@Override
		public void onSurfaceDestroyed (final SurfaceHolder holder) {
			if (AndroidLiveWallpaperService.DEBUG) Log.d(AndroidLiveWallpaperService.this.TAG, " > onSurfaceDestroyed() " + hashCode());
			super.onSurfaceDestroyed(holder);
		}

		@Override
		public void onVisibilityChanged (final boolean visible) {
			if (AndroidLiveWallpaperService.DEBUG)
				Log.d(AndroidLiveWallpaperService.this.TAG, " > onVisibilityChanged(" + visible + ") " + hashCode());
			if (visible) {
				onResume();
			} else {
				onPause();
			}

			super.onVisibilityChanged(visible);
		}

		@Override
		public void onTouchEvent (MotionEvent event) {
			app.input.onTouch(null, event);
		}

		@Override
		public void onOffsetsChanged (final float xOffset, final float yOffset, final float xOffsetStep, final float yOffsetStep, final int xPixelOffset,
			final int yPixelOffset) {

			if (AndroidLiveWallpaperService.DEBUG)
				Log.d(AndroidLiveWallpaperService.this.TAG, " > onVisibilityChanged(" + xOffset + " " + yOffset + " " + xOffsetStep + " "
					+ yOffsetStep + " " + xPixelOffset + " " + yPixelOffset + ") " + hashCode());

			// FIXME
			app.postRunnable(new Runnable() {
				@Override
				public void run () {
					AndroidLiveWallpaperService.this.offsetChange(listener, xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);	
				}
			});
			super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7748.java