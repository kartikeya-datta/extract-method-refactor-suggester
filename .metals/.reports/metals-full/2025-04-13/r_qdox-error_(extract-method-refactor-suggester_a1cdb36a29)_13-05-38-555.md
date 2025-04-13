error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7073.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7073.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7073.java
text:
```scala
r@@unOnEDT = config.forceExit;


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
	private boolean forceExit;
	private boolean runOnEDT;
	volatile boolean running = true;
	private int logLevel = LOG_INFO;

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
				if (Thread.currentThread() == glThread) render();
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

	void frame () {
		if (glfwWindowShouldClose(graphics.window)) {
			exit();
			return;
		}

		synchronized (runnables) {
			executedRunnables.clear();
			executedRunnables.addAll(runnables);
			runnables.clear();
		}
		if (executedRunnables.size > 0) {
			for (int i = 0; i < executedRunnables.size; i++)
				executedRunnables.get(i).run();
			if (!running) return;
			graphics.requestRendering();
		}

		input.update();

		if (graphics.shouldRender())
			render();
		else {
			try {
				Thread.sleep(16); // Avoid wasting CPU when not rendering.
			} catch (InterruptedException ignored) {
			}
		}
	}

	void render () {
		graphics.frameStart();
		listener.render();
		glfwSwapBuffers(graphics.window);
	}

	void end () {
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
			Preferences prefs = new JglfwPreferences(name);
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

	public void exit () {
		running = false;
	}

	public void setLogLevel (int logLevel) {
		this.logLevel = logLevel;
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

	public void log (String tag, String message, Exception exception) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7073.java