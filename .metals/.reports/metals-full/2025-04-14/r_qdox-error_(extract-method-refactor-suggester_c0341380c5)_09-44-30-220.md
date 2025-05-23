error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8577.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8577.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8577.java
text:
```scala
p@@referencesdir = config.preferencesLocation;

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

package com.badlogic.gdx.backends.jglfw;

import static com.badlogic.gdx.utils.SharedLibraryLoader.*;
import static com.badlogic.jglfw.Glfw.*;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Clipboard;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.jglfw.GlfwCallbackAdapter;
import com.badlogic.jglfw.GlfwCallbacks;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;

/** An OpenGL surface fullscreen or in a lightweight window using GLFW.
 * @author mzechner
 * @author Nathan Sweet */
public class JglfwApplication implements Application {
	JglfwGraphics graphics;
	JglfwFiles files;
	JglfwInput input;
	JglfwNet net;
	final ApplicationListener listener;
	private final Array<Runnable> runnables = new Array();
	private final Array<Runnable> executedRunnables = new Array();
	private final Array<LifecycleListener> lifecycleListeners = new Array();
	private final Map<String, Preferences> preferences = new HashMap();
	private final JglfwClipboard clipboard = new JglfwClipboard();
	private final GlfwCallbacks callbacks = new GlfwCallbacks();
	private int logLevel = LOG_INFO;
	volatile boolean running = true;
	boolean isPaused;
	protected String preferencesdir;

	private boolean forceExit, runOnEDT;
	private int foregroundFPS, backgroundFPS, hiddenFPS;

	public JglfwApplication (ApplicationListener listener) {
		this(listener, listener.getClass().getSimpleName(), 640, 480, false);
	}

	public JglfwApplication (ApplicationListener listener, String title, int width, int height, boolean useGL2) {
		this(listener, createConfig(title, width, height, useGL2));
	}

	static private JglfwApplicationConfiguration createConfig (String title, int width, int height, boolean useGL2) {
		JglfwApplicationConfiguration config = new JglfwApplicationConfiguration();
		config.title = title;
		config.width = width;
		config.height = height;
		config.useGL20 = useGL2;
		return config;
	}

	public JglfwApplication (final ApplicationListener listener, final JglfwApplicationConfiguration config) {
		if (listener == null) throw new IllegalArgumentException("listener cannot be null.");
		if (config == null) throw new IllegalArgumentException("config cannot be null.");

		this.listener = listener;

		Runnable runnable = new Runnable() {
			public void run () {
				try {
					initialize(config);
				} catch (Throwable ex) {
					exception(ex);
				}
			}
		};
		if (config.runOnEDT)
			EventQueue.invokeLater(runnable);
		else
			new Thread(runnable, "MainLoop").start();
	}

	/** Called when an uncaught exception happens in the game loop. Default implementation prints the exception and calls
	 * System.exit(0). */
	protected void exception (Throwable ex) {
		ex.printStackTrace();
		System.exit(0);
	}

	void initialize (JglfwApplicationConfiguration config) {
		forceExit = config.forceExit;
		runOnEDT = config.runOnEDT;
		foregroundFPS = config.foregroundFPS;
		backgroundFPS = config.backgroundFPS;
		hiddenFPS = config.hiddenFPS;
		preferencesdir = config.prefrencesLocation;

		final Thread glThread = Thread.currentThread();

		GdxNativesLoader.load();

		boolean inputCallbacksOnAppKitThread = isMac;
		if (inputCallbacksOnAppKitThread) java.awt.Toolkit.getDefaultToolkit(); // Ensure AWT is initialized before GLFW.

		if (!glfwInit()) throw new GdxRuntimeException("Unable to initialize GLFW.");

		Gdx.app = this;
		Gdx.graphics = graphics = new JglfwGraphics(config);
		Gdx.files = files = new JglfwFiles();
		Gdx.input = input = new JglfwInput(this, inputCallbacksOnAppKitThread);
		Gdx.net = net = new JglfwNet();

		callbacks.add(new GlfwCallbackAdapter() {
			public void windowSize (long window, final int width, final int height) {
				Runnable runnable = new Runnable() {
					public void run () {
						graphics.sizeChanged(width, height);
					}
				};
				if (Thread.currentThread() != glThread)
					postRunnable(runnable);
				else
					runnable.run();
			}

			public void windowPos (long window, final int x, final int y) {
				Runnable runnable = new Runnable() {
					public void run () {
						graphics.positionChanged(x, y);
					}
				};
				if (Thread.currentThread() != glThread)
					postRunnable(runnable);
				else
					runnable.run();
			}

			public void windowRefresh (long window) {
				if (Thread.currentThread() == glThread) render(System.nanoTime());
			}

			public void windowFocus (long window, boolean focused) {
				graphics.foreground = focused;
			}

			public void windowIconify (long window, boolean iconified) {
				graphics.minimized = iconified;
			}

			public boolean windowClose (long window) {
				if (shouldExit()) exit();
				return false;
			}

			public void error (int error, String description) {
				throw new GdxRuntimeException("GLFW error " + error + ": " + description);
			}
		});
		glfwSetCallback(callbacks);

		start();
	}

	/** Starts the game loop after the application internals have been initialized. */
	protected void start () {
		listener.create();
		listener.resize(graphics.getWidth(), graphics.getHeight());

		if (runOnEDT) {
			new Runnable() {
				public void run () {
					frame();
					if (running)
						EventQueue.invokeLater(this);
					else
						end();
				}
			}.run();
		} else {
			while (running)
				frame();
			end();
		}
	}

	/** Handles posted runnables, input, and rendering for each frame. */
	protected void frame () {
		if (!running) return;

		if (executeRunnables()) graphics.requestRendering();

		if (!running) return;

		input.update();

		long frameStartTime = System.nanoTime();
		int targetFPS = (graphics.isHidden() || graphics.isMinimized()) ? hiddenFPS : //
			(graphics.isForeground() ? foregroundFPS : backgroundFPS);

		if (targetFPS == -1) { // Rendering is paused.
			if (!isPaused) listener.pause();
			isPaused = true;
		} else {
			if (isPaused) listener.resume();
			isPaused = false;
			if (graphics.shouldRender()) render(frameStartTime);
		}

		if (targetFPS != 0) {
			if (targetFPS == -1)
				sleep(100);
			else
				Sync.sync(targetFPS);
		}
	}

	public boolean executeRunnables () {
		synchronized (runnables) {
			executedRunnables.addAll(runnables);
			runnables.clear();
		}
		if (executedRunnables.size == 0) return false;
		for (int i = 0; i < executedRunnables.size; i++)
			executedRunnables.get(i).run();
		executedRunnables.clear();
		return true;
	}

	void sleep (int millis) {
		try {
			if (millis > 0) Thread.sleep(millis);
		} catch (InterruptedException ignored) {
		}
	}

	void render (long time) {
		graphics.frameStart(time);
		listener.render();
		glfwSwapBuffers(graphics.window);
	}

	/** Called when the game loop has exited. */
	protected void end () {
		synchronized (lifecycleListeners) {
			for (LifecycleListener listener : lifecycleListeners) {
				listener.pause();
				listener.dispose();
			}
		}
		listener.pause();
		listener.dispose();
		glfwTerminate();
		if (forceExit) System.exit(-1);
	}

	public ApplicationListener getApplicationListener () {
		return listener;
	}

	public JglfwGraphics getGraphics () {
		return graphics;
	}

	public Audio getAudio () {
		return null;
	}

	public JglfwInput getInput () {
		return input;
	}

	public JglfwFiles getFiles () {
		return files;
	}

	public JglfwNet getNet () {
		return net;
	}

	public ApplicationType getType () {
		return ApplicationType.Desktop;
	}

	public int getVersion () {
		return 0;
	}

	public long getJavaHeap () {
		return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}

	public long getNativeHeap () {
		return getJavaHeap();
	}

	public Preferences getPreferences (String name) {
		if (preferences.containsKey(name))
			return preferences.get(name);
		else {
			Preferences prefs = new JglfwPreferences(name, this.preferencesdir);
			preferences.put(name, prefs);
			return prefs;
		}
	}

	public Clipboard getClipboard () {
		return clipboard;
	}

	public void postRunnable (Runnable runnable) {
		synchronized (runnables) {
			runnables.add(runnable);
			graphics.requestRendering();
		}
	}

	public boolean isPaused () {
		return isPaused;
	}

	public void setForegroundFPS (int foregroundFPS) {
		this.foregroundFPS = foregroundFPS;
	}

	public void setBackgroundFPS (int backgroundFPS) {
		this.backgroundFPS = backgroundFPS;
	}

	public void setHiddenFPS (int hiddenFPS) {
		this.hiddenFPS = hiddenFPS;
	}

	protected boolean shouldExit () {
		return true;
	}

	public void exit () {
		running = false;
	}

	public void setLogLevel (int logLevel) {
		this.logLevel = logLevel;
	}

	@Override
	public int getLogLevel () {
		return logLevel;
	}

	public void debug (String tag, String message) {
		if (logLevel >= LOG_DEBUG) {
			System.out.println(tag + ": " + message);
		}
	}

	public void debug (String tag, String message, Throwable exception) {
		if (logLevel >= LOG_DEBUG) {
			System.out.println(tag + ": " + message);
			exception.printStackTrace(System.out);
		}
	}

	public void log (String tag, String message) {
		if (logLevel >= LOG_INFO) {
			System.out.println(tag + ": " + message);
		}
	}

	public void log (String tag, String message, Throwable exception) {
		if (logLevel >= LOG_INFO) {
			System.out.println(tag + ": " + message);
			exception.printStackTrace(System.out);
		}
	}

	public void error (String tag, String message) {
		if (logLevel >= LOG_ERROR) {
			System.err.println(tag + ": " + message);
		}
	}

	public void error (String tag, String message, Throwable exception) {
		if (logLevel >= LOG_ERROR) {
			System.err.println(tag + ": " + message);
			exception.printStackTrace(System.err);
		}
	}

	public void addLifecycleListener (LifecycleListener listener) {
		synchronized (lifecycleListeners) {
			lifecycleListeners.add(listener);
		}
	}

	public void removeLifecycleListener (LifecycleListener listener) {
		synchronized (lifecycleListeners) {
			lifecycleListeners.removeValue(listener, true);
		}
	}

	public GlfwCallbacks getCallbacks () {
		return callbacks;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8577.java