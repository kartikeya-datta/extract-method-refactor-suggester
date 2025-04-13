error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/712.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/712.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/712.java
text:
```scala
G@@L20 gl20;

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

package com.badlogic.gdx.backends.ios;

import cli.MonoTouch.Foundation.ExportAttribute;
import cli.MonoTouch.Foundation.NSSet;
import cli.MonoTouch.CoreAnimation.CAEAGLLayer;
import cli.MonoTouch.ObjCRuntime.Selector;
import cli.MonoTouch.OpenGLES.EAGLColorFormat;
import cli.MonoTouch.OpenGLES.EAGLRenderingAPI;
import cli.MonoTouch.UIKit.UIDevice;
import cli.MonoTouch.UIKit.UIEvent;
import cli.MonoTouch.UIKit.UIScreen;
import cli.MonoTouch.UIKit.UIUserInterfaceIdiom;
import cli.OpenTK.FrameEventArgs;
import cli.OpenTK.Platform.iPhoneOS.iPhoneOSGameView;
import cli.System.EventArgs;
import cli.System.Drawing.RectangleF;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.GLU;
import com.badlogic.gdx.graphics.Pixmap;

// FIXME add GL 1.x support by ripping Android's classes
public class IOSGraphics extends iPhoneOSGameView implements Graphics {

	private static final String tag = "IOSGraphics";

	IOSApplication app;
	IOSInput input;
	IOSGLES20 gl20;
	int width;
	int height;
	long lastFrameTime;
	float deltaTime;
	long framesStart;
	int frames;
	int fps;
	BufferFormat bufferFormat;
	String extensions;

	private float ppiX = 0;
	private float ppiY = 0;
	private float ppcX = 0;
	private float ppcY = 0;
	private float density = 1;

	public IOSGraphics (RectangleF bounds, IOSApplication app, IOSInput input) {
		super(bounds);

		// setup view and OpenGL
		width = (int)bounds.get_Width();
		height = (int)bounds.get_Height();
		app.debug(tag, bounds.get_Width() + "x" + bounds.get_Height() + ", " + UIScreen.get_MainScreen().get_Scale());
		this.app = app;
		this.input = input;
		set_LayerRetainsBacking(false);
		set_ContentScaleFactor(1);
		set_MultipleTouchEnabled(true);
		set_AutoResize(false);
		set_LayerColorFormat(EAGLColorFormat.RGB565);
		set_ContextRenderingApi(EAGLRenderingAPI.wrap(EAGLRenderingAPI.OpenGLES2));
		// FIXME fix this if we add rgba/depth/stencil flags to IOSApplicationConfiguration
		bufferFormat = new BufferFormat(5, 6, 5, 0, 16, 0, 0, false);
		gl20 = new IOSGLES20();
		Gdx.gl = gl20;
		Gdx.gl20 = gl20;

		// determine display density and PPI (PPI values via Wikipedia!)
		density = 1f;

		if ((UIScreen.get_MainScreen().RespondsToSelector(new Selector("scale")))) {
			float scale = UIScreen.get_MainScreen().get_Scale();
			app.debug(tag, "Calculating density, UIScreen.mainScreen.scale: " + scale);
			if (scale == 2f) density = 2f;
		}

		int ppi;
		if (UIDevice.get_CurrentDevice().get_UserInterfaceIdiom().Value == UIUserInterfaceIdiom.Pad) {
			// iPad
			ppi = Math.round(density * 132);
		} else {
			// iPhone or iPodTouch
			ppi = Math.round(density * 163);
		}
		ppiX = ppi;
		ppiY = ppi;
		ppcX = ppiX / 2.54f;
		ppcY = ppcY / 2.54f;
		app.debug(tag, "Display: ppi=" + ppi + ", density=" + density);

		// time + FPS
		lastFrameTime = System.nanoTime();
		framesStart = lastFrameTime;
	}

	@Override
	protected void ConfigureLayer (CAEAGLLayer layer) {
		layer.set_Opaque(true);
		super.ConfigureLayer(layer);
	}

	@Override
	protected void OnLoad (EventArgs arg0) {
		super.OnLoad(arg0);
		MakeCurrent();
		app.listener.create();
	}

	@Override
	protected void OnRenderFrame (FrameEventArgs arg0) {
		super.OnRenderFrame(arg0);

		long time = System.nanoTime();
		deltaTime = (time - lastFrameTime) / 1000000000.0f;
		lastFrameTime = time;

		frames++;
		if (time - framesStart >= 1000000000l) {
			framesStart = time;
			fps = frames;
			frames = 0;
		}

		MakeCurrent();
		((IOSInput)Gdx.input).processEvents();
		app.listener.render();
		SwapBuffers();
	}

	@Override
	protected void OnUpdateFrame (FrameEventArgs frameEventArgs) {
		super.OnUpdateFrame(frameEventArgs);
		app.processRunnables();
	}

	@Override
	protected void OnResize (EventArgs event) {
		super.OnResize(event);

		// not used on iOS
		// FIXME resize could happen if app supports both portrait and landscape, so this should be implemented
		Gdx.app.debug("IOSGraphics", "iOS OnResize(...) is not implement (don't think it is needed?).");
	}

	@ExportAttribute.Annotation("layerClass")
	static cli.MonoTouch.ObjCRuntime.Class LayerClass () {
		return iPhoneOSGameView.GetLayerClass();
	}

	@Override
	public boolean isGL11Available () {
		return false;
	}

	@Override
	public boolean isGL20Available () {
		return true;
	}

	@Override
	public GLCommon getGLCommon () {
		return gl20;
	}

	@Override
	public GL10 getGL10 () {
		return null;
	}

	@Override
	public GL11 getGL11 () {
		return null;
	}

	@Override
	public GL20 getGL20 () {
		return gl20;
	}

	@Override
	public GLU getGLU () {
		// FIXME implement this
		return null;
	}

	@Override
	public int getWidth () {
		return width;
	}

	@Override
	public int getHeight () {
		return height;
	}

	@Override
	public float getDeltaTime () {
		return deltaTime;
	}

	@Override
	public float getRawDeltaTime () {
		return deltaTime;
	}

	@Override
	public int getFramesPerSecond () {
		return fps;
	}

	@Override
	public GraphicsType getType () {
		return GraphicsType.iOSGL;
	}

	@Override
	public float getPpiX () {
		return ppiX;
	}

	@Override
	public float getPpiY () {
		return ppiY;
	}

	@Override
	public float getPpcX () {
		return ppcX;
	}

	@Override
	public float getPpcY () {
		return ppcY;
	}

	/** Returns the display density.
	 * 
	 * @return 1.0f for non-retina devices, 2.0f for retina devices. */
	@Override
	public float getDensity () {
		return density;
	}

	@Override
	public boolean supportsDisplayModeChange () {
		return false;
	}

	@Override
	public DisplayMode[] getDisplayModes () {
		return new DisplayMode[] {getDesktopDisplayMode()};
	}

	@Override
	public DisplayMode getDesktopDisplayMode () {
		return new IOSDisplayMode(getWidth(), getHeight(), 60, 0);
	}

	private static class IOSDisplayMode extends DisplayMode {
		protected IOSDisplayMode (int width, int height, int refreshRate, int bitsPerPixel) {
			super(width, height, refreshRate, bitsPerPixel);
		}
	}

	@Override
	public boolean setDisplayMode (DisplayMode displayMode) {
		return false;
	}

	@Override
	public boolean setDisplayMode (int width, int height, boolean fullscreen) {
		return false;
	}

	@Override
	public void setTitle (String title) {
	}

	@Override
	public void setVSync (boolean vsync) {
	}

	@Override
	public BufferFormat getBufferFormat () {
		return bufferFormat;
	}

	@Override
	public boolean supportsExtension (String extension) {
		if (extensions == null) extensions = Gdx.gl.glGetString(GL10.GL_EXTENSIONS);
		return extensions.contains(extension);
	}

	@Override
	public void setContinuousRendering (boolean isContinuous) {
		// FIXME implement this if possible
	}

	@Override
	public boolean isContinuousRendering () {
		// FIXME implement this if possible
		return true;
	}

	@Override
	public void requestRendering () {
		// FIXME implement this if possible
	}

	@Override
	public boolean isFullscreen () {
		return true;
	}

	@Override
	public void TouchesBegan (NSSet touches, UIEvent event) {
		super.TouchesBegan(touches, event);
		input.touchDown(touches, event);
	}

	@Override
	public void TouchesCancelled (NSSet touches, UIEvent event) {
		super.TouchesCancelled(touches, event);
		input.touchUp(touches, event);
	}

	@Override
	public void TouchesEnded (NSSet touches, UIEvent event) {
		super.TouchesEnded(touches, event);
		input.touchUp(touches, event);
	}

	@Override
	public void TouchesMoved (NSSet touches, UIEvent event) {
		super.TouchesMoved(touches, event);
		input.touchMoved(touches, event);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/712.java