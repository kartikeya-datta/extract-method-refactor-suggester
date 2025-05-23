error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/74.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/74.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/74.java
text:
```scala
r@@eturn android.os.Build.VERSION.SDK_INT;

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.backends.android;

import java.lang.reflect.Method;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;
import com.badlogic.gdx.backends.android.surfaceview.GLSurfaceViewAPI18;
import com.badlogic.gdx.backends.android.surfaceview.GLSurfaceViewCupcake;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Clipboard;
import com.badlogic.gdx.utils.GdxNativesLoader;

/** An implementation of the {@link Application} interface for Android. Create an {@link Activity} that derives from this class. In
 * the {@link Activity#onCreate(Bundle)} method call the {@link #initialize(ApplicationListener, boolean)} method specifying the
 * configuration for the GLSurfaceView.
 * 
 * @author mzechner */
public class AndroidApplication extends Activity implements Application {
	static {
		GdxNativesLoader.load();
	}

	protected AndroidGraphics graphics;
	protected AndroidInput input;
	protected AndroidAudio audio;
	protected AndroidFiles files;
	protected AndroidNet net;
	protected ApplicationListener listener;
	public Handler handler;
	protected boolean firstResume = true;
	protected final Array<Runnable> runnables = new Array<Runnable>();
	protected final Array<Runnable> executedRunnables = new Array<Runnable>();
	protected final Array<LifecycleListener> lifecycleListeners = new Array<LifecycleListener>();
	protected int logLevel = LOG_INFO;
	protected boolean useImmersiveMode = false;
	protected boolean hideStatusBar = false;
	private int wasFocusChanged = -1;
	private boolean isWaitingForAudio = false;

	/** This method has to be called in the {@link Activity#onCreate(Bundle)} method. It sets up all the things necessary to get
	 * input, render via OpenGL and so on. If useGL20IfAvailable is set the AndroidApplication will try to create an OpenGL ES 2.0
	 * context which can then be used via {@link Graphics#getGL20()}. The {@link GL10} and {@link GL11} interfaces should not be
	 * used when OpenGL ES 2.0 is enabled. To query whether enabling OpenGL ES 2.0 was successful use the
	 * {@link Graphics#isGL20Available()} method. Uses a default {@link AndroidApplicationConfiguration}.
	 * 
	 * @param listener the {@link ApplicationListener} implementing the program logic
	 * @param useGL2IfAvailable whether to use OpenGL ES 2.0 if its available. */
	public void initialize (ApplicationListener listener, boolean useGL2IfAvailable) {
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useGL20 = useGL2IfAvailable;
		initialize(listener, config);
	}

	/** This method has to be called in the {@link Activity#onCreate(Bundle)} method. It sets up all the things necessary to get
	 * input, render via OpenGL and so on. If config.useGL20 is set the AndroidApplication will try to create an OpenGL ES 2.0
	 * context which can then be used via {@link Graphics#getGL20()}. The {@link GL10} and {@link GL11} interfaces should not be
	 * used when OpenGL ES 2.0 is enabled. To query whether enabling OpenGL ES 2.0 was successful use the
	 * {@link Graphics#isGL20Available()} method. You can configure other aspects of the application with the rest of the fields in
	 * the {@link AndroidApplicationConfiguration} instance.
	 * 
	 * @param listener the {@link ApplicationListener} implementing the program logic
	 * @param config the {@link AndroidApplicationConfiguration}, defining various settings of the application (use accelerometer,
	 *           etc.). */
	public void initialize (ApplicationListener listener, AndroidApplicationConfiguration config) {
		graphics = new AndroidGraphics(this, config, config.resolutionStrategy == null ? new FillResolutionStrategy()
			: config.resolutionStrategy);
		input = AndroidInputFactory.newAndroidInput(this, this, graphics.view, config);
		audio = new AndroidAudio(this, config);
		files = new AndroidFiles(this.getAssets(), this.getFilesDir().getAbsolutePath());
		net = new AndroidNet(this);
		this.listener = listener;
		this.handler = new Handler();
		this.useImmersiveMode = config.useImmersiveMode;
		this.hideStatusBar = config.hideStatusBar;

		Gdx.app = this;
		Gdx.input = this.getInput();
		Gdx.audio = this.getAudio();
		Gdx.files = this.getFiles();
		Gdx.graphics = this.getGraphics();
		Gdx.net = this.getNet();

		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		} catch (Exception ex) {
			log("AndroidApplication", "Content already displayed, cannot request FEATURE_NO_TITLE", ex);
		}
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		setContentView(graphics.getView(), createLayoutParams());
		createWakeLock(config.useWakelock);
		hideStatusBar(this.hideStatusBar);
		useImmersiveMode(this.useImmersiveMode);
		if (this.useImmersiveMode && getVersion() >= 19) {
			try {
				Class vlistener = Class.forName("com.badlogic.gdx.backends.android.AndroidVisibilityListener");
				Object o = vlistener.newInstance();
				Method method = vlistener.getDeclaredMethod("createListener", AndroidApplication.class);
				method.invoke(o, this);
			} catch (Exception e) {
				log("AndroidApplication", "Failed to create AndroidVisibilityListener");
			}
		}
	}

	protected FrameLayout.LayoutParams createLayoutParams () {
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
			android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		layoutParams.gravity = Gravity.CENTER;
		return layoutParams;
	}

	protected void createWakeLock (boolean use) {
		if (use) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}

	protected void hideStatusBar (boolean hide) {
		if (!hide || getVersion() < 11) return;

		View rootView = getWindow().getDecorView();

		try {
			Method m = View.class.getMethod("setSystemUiVisibility", int.class);
			m.invoke(rootView, 0x0);
			m.invoke(rootView, 0x1);
		} catch (Exception e) {
			log("AndroidApplication", "Can't hide status bar", e);
		}
	}

	@Override
	public void onWindowFocusChanged (boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		useImmersiveMode(this.useImmersiveMode);
		hideStatusBar(this.hideStatusBar);
		if (hasFocus) {
			this.wasFocusChanged = 1;
			if (this.isWaitingForAudio) {
				this.audio.resume();
				this.isWaitingForAudio = false;
			}
		} else {
			this.wasFocusChanged = 0;
		}
	}

	protected void useImmersiveMode (boolean use) {
		if (!use || getVersion() < 19) return;

		View view = getWindow().getDecorView();
		try {
			Method m = View.class.getMethod("setSystemUiVisibility", int.class);
			int code = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
			code ^= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
			code ^= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
			code ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
			code ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
			code ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
			m.invoke(view, code);
		} catch (Exception e) {
			log("AndroidApplication", "Can't set immersive mode", e);
		}
	}

	/** This method has to be called in the {@link Activity#onCreate(Bundle)} method. It sets up all the things necessary to get
	 * input, render via OpenGL and so on. If useGL20IfAvailable is set the AndroidApplication will try to create an OpenGL ES 2.0
	 * context which can then be used via {@link Graphics#getGL20()}. The {@link GL10} and {@link GL11} interfaces should not be
	 * used when OpenGL ES 2.0 is enabled. To query whether enabling OpenGL ES 2.0 was successful use the
	 * {@link Graphics#isGL20Available()} method. Uses a default {@link AndroidApplicationConfiguration}.
	 * <p/>
	 * Note: you have to add the returned view to your layout!
	 * 
	 * @param listener the {@link ApplicationListener} implementing the program logic
	 * @param useGL2IfAvailable whether to use OpenGL ES 2.0 if its available.
	 * @return the GLSurfaceView of the application */
	public View initializeForView (ApplicationListener listener, boolean useGL2IfAvailable) {
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useGL20 = useGL2IfAvailable;
		return initializeForView(listener, config);
	}

	/** This method has to be called in the {@link Activity#onCreate(Bundle)} method. It sets up all the things necessary to get
	 * input, render via OpenGL and so on. If config.useGL20 is set the AndroidApplication will try to create an OpenGL ES 2.0
	 * context which can then be used via {@link Graphics#getGL20()}. The {@link GL10} and {@link GL11} interfaces should not be
	 * used when OpenGL ES 2.0 is enabled. To query whether enabling OpenGL ES 2.0 was successful use the
	 * {@link Graphics#isGL20Available()} method. You can configure other aspects of the application with the rest of the fields in
	 * the {@link AndroidApplicationConfiguration} instance.
	 * <p/>
	 * Note: you have to add the returned view to your layout!
	 * 
	 * @param listener the {@link ApplicationListener} implementing the program logic
	 * @param config the {@link AndroidApplicationConfiguration}, defining various settings of the application (use accelerometer,
	 *           etc.).
	 * @return the GLSurfaceView of the application */
	public View initializeForView (ApplicationListener listener, AndroidApplicationConfiguration config) {
		graphics = new AndroidGraphics(this, config, config.resolutionStrategy == null ? new FillResolutionStrategy()
			: config.resolutionStrategy);
		input = AndroidInputFactory.newAndroidInput(this, this, graphics.view, config);
		audio = new AndroidAudio(this, config);
		files = new AndroidFiles(this.getAssets(), this.getFilesDir().getAbsolutePath());
		net = new AndroidNet(this);
		this.listener = listener;
		this.handler = new Handler();
		this.useImmersiveMode = config.useImmersiveMode;
		this.hideStatusBar = config.hideStatusBar;

		Gdx.app = this;
		Gdx.input = this.getInput();
		Gdx.audio = this.getAudio();
		Gdx.files = this.getFiles();
		Gdx.graphics = this.getGraphics();
		Gdx.net = this.getNet();

		createWakeLock(config.useWakelock);
		hideStatusBar(this.hideStatusBar);
		useImmersiveMode(this.useImmersiveMode);
		if (this.useImmersiveMode && getVersion() >= 19) {
			try {
				Class vlistener = Class.forName("com.badlogic.gdx.backends.android.AndroidVisibilityListener");
				Object o = vlistener.newInstance();
				Method method = vlistener.getDeclaredMethod("createListener", AndroidApplication.class);
				method.invoke(o, this);
			} catch (Exception e) {
				log("AndroidApplication", "Failed to create AndroidVisibilityListener");
			}
		}
		return graphics.getView();
	}

	@Override
	protected void onPause () {
		boolean isContinuous = graphics.isContinuousRendering();
		graphics.setContinuousRendering(true);
		graphics.pause();

		input.unregisterSensorListeners();

		int[] realId = input.realId;
		// erase pointer ids. this sucks donkeyballs...
		Arrays.fill(realId, -1);
		boolean[] touched = input.touched;
		// erase touched state. this also sucks donkeyballs...
		Arrays.fill(touched, false);

		if (isFinishing()) {
			graphics.clearManagedCaches();
			graphics.destroy();
		}
		graphics.setContinuousRendering(isContinuous);

		if (graphics != null && graphics.view != null) {
			if (graphics.view instanceof GLSurfaceViewCupcake) ((GLSurfaceViewCupcake)graphics.view).onPause();
			if (graphics.view instanceof GLSurfaceViewAPI18) ((GLSurfaceViewAPI18)graphics.view).onPause();
			if (graphics.view instanceof android.opengl.GLSurfaceView) ((android.opengl.GLSurfaceView)graphics.view).onPause();
		}

		super.onPause();
	}

	@Override
	protected void onResume () {
		Gdx.app = this;
		Gdx.input = this.getInput();
		Gdx.audio = this.getAudio();
		Gdx.files = this.getFiles();
		Gdx.graphics = this.getGraphics();
		Gdx.net = this.getNet();

		((AndroidInput)getInput()).registerSensorListeners();

		if (graphics != null && graphics.view != null) {
			if (graphics.view instanceof GLSurfaceViewCupcake) ((GLSurfaceViewCupcake)graphics.view).onResume();
			if (graphics.view instanceof GLSurfaceViewAPI18) ((GLSurfaceViewAPI18)graphics.view).onResume();
			if (graphics.view instanceof android.opengl.GLSurfaceView) ((android.opengl.GLSurfaceView)graphics.view).onResume();
		}

		if (!firstResume) {
			graphics.resume();
		} else
			firstResume = false;

		this.isWaitingForAudio = true;
		if (this.wasFocusChanged == 1 || this.wasFocusChanged == -1) {
			this.audio.resume();
			this.isWaitingForAudio = false;
		}
		super.onResume();
	}

	@Override
	protected void onDestroy () {
		super.onDestroy();
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return listener;
	}

	@Override
	public Audio getAudio () {
		return audio;
	}

	@Override
	public Files getFiles () {
		return files;
	}

	@Override
	public Graphics getGraphics () {
		return graphics;
	}

	@Override
	public Input getInput () {
		return input;
	}

	@Override
	public Net getNet () {
		return net;
	}

	@Override
	public ApplicationType getType () {
		return ApplicationType.Android;
	}

	@Override
	public int getVersion () {
		return Integer.parseInt(android.os.Build.VERSION.SDK);
	}

	@Override
	public long getJavaHeap () {
		return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}

	@Override
	public long getNativeHeap () {
		return Debug.getNativeHeapAllocatedSize();
	}

	@Override
	public Preferences getPreferences (String name) {
		return new AndroidPreferences(getSharedPreferences(name, Context.MODE_PRIVATE));
	}

	AndroidClipboard clipboard;

	@Override
	public Clipboard getClipboard () {
		if (clipboard == null) {
			clipboard = new AndroidClipboard(this);
		}
		return clipboard;
	}

	@Override
	public void postRunnable (Runnable runnable) {
		synchronized (runnables) {
			runnables.add(runnable);
			Gdx.graphics.requestRendering();
		}
	}

	@Override
	public void onConfigurationChanged (Configuration config) {
		super.onConfigurationChanged(config);
		boolean keyboardAvailable = false;
		if (config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) keyboardAvailable = true;
		input.keyboardAvailable = keyboardAvailable;
	}

	@Override
	public void exit () {
		handler.post(new Runnable() {
			@Override
			public void run () {
				AndroidApplication.this.finish();
			}
		});
	}

	@Override
	public void debug (String tag, String message) {
		if (logLevel >= LOG_DEBUG) {
			Log.d(tag, message);
		}
	}

	@Override
	public void debug (String tag, String message, Throwable exception) {
		if (logLevel >= LOG_DEBUG) {
			Log.d(tag, message, exception);
		}
	}

	@Override
	public void log (String tag, String message) {
		if (logLevel >= LOG_INFO) Log.i(tag, message);
	}

	@Override
	public void log (String tag, String message, Throwable exception) {
		if (logLevel >= LOG_INFO) Log.i(tag, message, exception);
	}

	@Override
	public void error (String tag, String message) {
		if (logLevel >= LOG_ERROR) Log.e(tag, message);
	}

	@Override
	public void error (String tag, String message, Throwable exception) {
		if (logLevel >= LOG_ERROR) Log.e(tag, message, exception);
	}

	@Override
	public void setLogLevel (int logLevel) {
		this.logLevel = logLevel;
	}

	@Override
	public int getLogLevel () {
		return logLevel;
	}

	@Override
	public void addLifecycleListener (LifecycleListener listener) {
		synchronized (lifecycleListeners) {
			lifecycleListeners.add(listener);
		}
	}

	@Override
	public void removeLifecycleListener (LifecycleListener listener) {
		synchronized (lifecycleListeners) {
			lifecycleListeners.removeValue(listener, true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/74.java