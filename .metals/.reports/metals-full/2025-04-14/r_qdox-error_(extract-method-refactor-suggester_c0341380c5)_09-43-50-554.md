error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1040.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1040.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1040.java
text:
```scala
public v@@oid log (String tag, String message, Exception exception) {

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

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
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
	protected WakeLock wakeLock = null;
	protected int logLevel = LOG_INFO;

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
		createWakeLock(config);
		hideStatusBar(config);
	}

	protected FrameLayout.LayoutParams createLayoutParams () {
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
			android.view.ViewGroup.LayoutParams.FILL_PARENT);
		layoutParams.gravity = Gravity.CENTER;
		return layoutParams;
	}

	protected void createWakeLock (AndroidApplicationConfiguration config) {
		if (config.useWakelock) {
			PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
			wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "libgdx wakelock");
		}
	}

	protected void hideStatusBar (AndroidApplicationConfiguration config) {
		if (!config.hideStatusBar || getVersion() < 11)
			return;

		View rootView = getWindow().getDecorView();

		try {
			Method m = View.class.getMethod("setSystemUiVisibility", int.class);
			m.invoke(rootView, 0x0);
			m.invoke(rootView, 0x1);
		} catch (Exception e) {
			log("AndroidApplication", "Can't hide status bar", e);
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

		Gdx.app = this;
		Gdx.input = this.getInput();
		Gdx.audio = this.getAudio();
		Gdx.files = this.getFiles();
		Gdx.graphics = this.getGraphics();
		Gdx.net = this.getNet();

		createWakeLock(config);
		hideStatusBar(config);
		return graphics.getView();
	}

	@Override
	protected void onPause () {
		if (wakeLock != null) wakeLock.release();
		boolean isContinuous = graphics.isContinuousRendering();
		graphics.setContinuousRendering(true);
		graphics.pause();

		input.unregisterSensorListeners();
		// erase pointer ids. this sucks donkeyballs...
		int[] realId = input.realId;
		for (int i = 0; i < realId.length; i++)
			realId[i] = -1;

		// erase touched state. this also sucks donkeyballs...
		boolean[] touched = input.touched;
		for (int i = 0; i < touched.length; i++)
			touched[i] = false;

		if (isFinishing()) {
			graphics.clearManagedCaches();
			graphics.destroy();
		}
		graphics.setContinuousRendering(isContinuous);

		if (graphics != null && graphics.view != null) {
			if (graphics.view instanceof GLSurfaceViewCupcake) ((GLSurfaceViewCupcake)graphics.view).onPause();
			if (graphics.view instanceof android.opengl.GLSurfaceView) ((android.opengl.GLSurfaceView)graphics.view).onPause();
		}

		super.onPause();
	}

	@Override
	protected void onResume () {
		if (wakeLock != null) wakeLock.acquire();
		Gdx.app = this;
		Gdx.input = this.getInput();
		Gdx.audio = this.getAudio();
		Gdx.files = this.getFiles();
		Gdx.graphics = this.getGraphics();
		Gdx.net = this.getNet();

		((AndroidInput)getInput()).registerSensorListeners();

		if (graphics != null && graphics.view != null) {
			if (graphics.view instanceof GLSurfaceViewCupcake) ((GLSurfaceViewCupcake)graphics.view).onResume();
			if (graphics.view instanceof android.opengl.GLSurfaceView) ((android.opengl.GLSurfaceView)graphics.view).onResume();
		}

		if (!firstResume) {
			graphics.resume();
		} else
			firstResume = false;
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
	public Clipboard getClipboard() {
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
	public int getLogLevel() {
		return logLevel;
	}

	@Override
	public void addLifecycleListener (LifecycleListener listener) {
		synchronized(lifecycleListeners) {
			lifecycleListeners.add(listener);
		}
	}

	@Override
	public void removeLifecycleListener (LifecycleListener listener) {
		synchronized(lifecycleListeners) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1040.java