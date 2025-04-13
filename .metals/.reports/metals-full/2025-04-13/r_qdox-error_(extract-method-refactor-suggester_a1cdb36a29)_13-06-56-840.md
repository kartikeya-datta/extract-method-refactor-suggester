error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7987.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7987.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7987.java
text:
```scala
i@@f (title == null) title = "";


package com.badlogic.gdx.backends.jglfw;

import static com.badlogic.jglfw.Glfw.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.jglfw.GlfwVideoMode;
import com.badlogic.jglfw.gl.GL;

import java.awt.Toolkit;

/** An implementation of the {@link Graphics} interface based on GLFW.
 * @author Nathan Sweet */
public class JglfwGraphics implements Graphics {
	static int glMajorVersion, glMinorVersion;

	long window;
	boolean fullscreen;
	long fullscreenMonitor;
	String title;
	boolean resizable;
	BufferFormat bufferFormat;
	boolean sync;
	volatile boolean isContinuous = true;
	volatile boolean requestRendering;

	float deltaTime;
	long frameStart, lastTime;
	int frames, fps;

	GLCommon gl;
	JglfwGL10 gl10;
	JglfwGL11 gl11;
	JglfwGL20 gl20;

	public JglfwGraphics (JglfwApplicationConfiguration config) {
		// Store values from config.
		bufferFormat = new BufferFormat(config.r, config.g, config.b, config.a, config.depth, config.stencil, config.samples, false);
		title = config.title;
		resizable = config.resizable;
		if (config.fullscreenMonitorIndex != -1) { // Use monitor specified in config if it is valid.
			long[] monitors = glfwGetMonitors();
			if (config.fullscreenMonitorIndex < monitors.length) fullscreenMonitor = monitors[config.fullscreenMonitorIndex];
		}

		// Create window.
		if (!createWindow(config.width, config.height, config.fullscreen)) {
			throw new GdxRuntimeException("Unable to create window: " + config.width + "x" + config.height + ", fullscreen: "
				+ config.fullscreen);
		}
		if (config.x != -1 && config.y != -1) glfwSetWindowPos(window, config.x, config.y);
		setVSync(config.vSync);

		// Create GL.
		String version = GL.glGetString(GL11.GL_VERSION);
		glMajorVersion = Integer.parseInt("" + version.charAt(0));
		glMinorVersion = Integer.parseInt("" + version.charAt(2));
		if (config.useGL20 && (glMajorVersion >= 2 || version.contains("2.1"))) { // special case for MESA, wtf...
			gl20 = new JglfwGL20();
			gl = gl20;
		} else {
			gl20 = null;
			if (glMajorVersion == 1 && glMinorVersion < 5) {
				gl10 = new JglfwGL10();
			} else {
				gl11 = new JglfwGL11();
				gl10 = gl11;
			}
			gl = gl10;
		}
		Gdx.gl = gl;
		Gdx.gl10 = gl10;
		Gdx.gl11 = gl11;
		Gdx.gl20 = gl20;
	}

	public boolean isGL11Available () {
		return gl11 != null;
	}

	public boolean isGL20Available () {
		return gl20 != null;
	}

	public GLCommon getGLCommon () {
		return gl;
	}

	public GL10 getGL10 () {
		return gl10;
	}

	public GL11 getGL11 () {
		return gl11;
	}

	public GL20 getGL20 () {
		return gl20;
	}

	public int getWidth () {
		return glfwGetWindowWidth(window);
	}

	public int getHeight () {
		return glfwGetWindowHeight(window);
	}

	void updateTime () {
		long time = System.nanoTime();
		deltaTime = (time - lastTime) / 1000000000.0f;
		lastTime = time;

		if (time - frameStart >= 1000000000) {
			fps = frames;
			frames = 0;
			frameStart = time;
		}
		frames++;
	}

	public float getDeltaTime () {
		return deltaTime;
	}

	public float getRawDeltaTime () {
		return deltaTime;
	}

	public int getFramesPerSecond () {
		return fps;
	}

	public GraphicsType getType () {
		return GraphicsType.JGLFW;
	}

	public float getPpiX () {
		return Toolkit.getDefaultToolkit().getScreenResolution();
	}

	public float getPpiY () {
		return Toolkit.getDefaultToolkit().getScreenResolution();
	}

	public float getPpcX () {
		return Toolkit.getDefaultToolkit().getScreenResolution() / 2.54f;
	}

	public float getPpcY () {
		return Toolkit.getDefaultToolkit().getScreenResolution() / 2.54f;
	}

	public float getDensity () {
		return Toolkit.getDefaultToolkit().getScreenResolution() / 160f;
	}

	public boolean supportsDisplayModeChange () {
		return true;
	}

	public DisplayMode[] getDisplayModes () {
		Array<DisplayMode> modes = new Array();
		for (GlfwVideoMode mode : glfwGetVideoModes(glfwGetWindowMonitor(window)))
			modes.add(new JglfwDisplayMode(mode.width, mode.height, 0, mode.redBits + mode.greenBits + mode.blueBits));
		return modes.toArray(DisplayMode.class);
	}

	public DisplayMode getDesktopDisplayMode () {
		GlfwVideoMode mode = glfwGetVideoMode(glfwGetWindowMonitor(window));
		return new JglfwDisplayMode(mode.width, mode.height, 0, mode.redBits + mode.greenBits + mode.blueBits);
	}

	public boolean setDisplayMode (DisplayMode displayMode) {
		bufferFormat = new BufferFormat( //
			displayMode.bitsPerPixel == 16 ? 5 : 8, //
			displayMode.bitsPerPixel == 16 ? 6 : 8, //
			displayMode.bitsPerPixel == 16 ? 6 : 8, //
			bufferFormat.a, bufferFormat.depth, bufferFormat.stencil, bufferFormat.samples, false);
		return createWindow(displayMode.width, displayMode.height, fullscreen);
	}

	public boolean setDisplayMode (int width, int height, boolean fullscreen) {
		if (window == 0 || fullscreen != this.fullscreen || this.fullscreen) return createWindow(width, height, fullscreen);

		glfwSetWindowSize(window, width, height);
		return true;
	}

	private boolean createWindow (int width, int height, boolean fullscreen) {
		if (fullscreenMonitor == 0) fullscreenMonitor = glfwGetPrimaryMonitor();

		glfwWindowHint(GLFW_RESIZABLE, resizable ? 1 : 0);
		glfwWindowHint(GLFW_RED_BITS, bufferFormat.r);
		glfwWindowHint(GLFW_GREEN_BITS, bufferFormat.g);
		glfwWindowHint(GLFW_BLUE_BITS, bufferFormat.b);
		glfwWindowHint(GLFW_ALPHA_BITS, bufferFormat.a);
		glfwWindowHint(GLFW_DEPTH_BITS, bufferFormat.depth);
		glfwWindowHint(GLFW_STENCIL_BITS, bufferFormat.stencil);
		glfwWindowHint(GLFW_SAMPLES, bufferFormat.samples);

		boolean mouseCaptured = window != 0 && glfwGetInputMode(window, GLFW_CURSOR_MODE) == GLFW_CURSOR_CAPTURED;

		long oldWindow = window;
		long newWindow = glfwCreateWindow(width, height, title, fullscreen ? fullscreenMonitor : 0, oldWindow);
		if (newWindow == 0) return false;
		if (oldWindow != 0) glfwDestroyWindow(oldWindow);
		glfwMakeContextCurrent(newWindow);
		window = newWindow;
		this.fullscreen = fullscreen;

		if (!mouseCaptured) glfwSetInputMode(window, GLFW_CURSOR_MODE, GLFW_CURSOR_NORMAL); // Prevent fullscreen from taking mouse.

		return true;
	}

	public void setTitle (String title) {
		if (title == null) glfwSetWindowTitle(window, "");
		glfwSetWindowTitle(window, title);
		this.title = title;
	}

	public void setVSync (boolean vsync) {
		this.sync = vsync;
		glfwSwapInterval(vsync ? 1 : 0);
	}

	public BufferFormat getBufferFormat () {
		return bufferFormat;
	}

	public boolean supportsExtension (String extension) {
		return glfwExtensionSupported(extension);
	}

	public void setContinuousRendering (boolean isContinuous) {
		this.isContinuous = isContinuous;
	}

	public boolean isContinuousRendering () {
		return isContinuous;
	}

	public void requestRendering () {
		synchronized (this) {
			requestRendering = true;
		}
	}

	public boolean isFullscreen () {
		return fullscreen;
	}

	/** Returns the JGLFW window handle. Note this should not be stored externally as it may change if the window is recreated to
	 * enter/exit fullscreen. */
	public long getWindow () {
		return window;
	}

	boolean shouldRender () {
		synchronized (this) {
			boolean requestRendering = this.requestRendering;
			this.requestRendering = false;
			return requestRendering || isContinuous;
		}
	}

	static class JglfwDisplayMode extends DisplayMode {
		protected JglfwDisplayMode (int width, int height, int refreshRate, int bitsPerPixel) {
			super(width, height, refreshRate, bitsPerPixel);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7987.java