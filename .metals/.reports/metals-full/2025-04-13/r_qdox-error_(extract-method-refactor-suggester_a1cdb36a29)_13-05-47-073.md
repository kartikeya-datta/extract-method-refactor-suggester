error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4265.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4265.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4265.java
text:
```scala
public v@@oid log (String tag, String message, Throwable exception) {

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

package com.badlogic.gdx.backends.iosrobovm;

import java.io.File;

import org.robovm.cocoatouch.coregraphics.CGSize;
import org.robovm.cocoatouch.foundation.NSDictionary;
import org.robovm.cocoatouch.foundation.NSMutableDictionary;
import org.robovm.cocoatouch.uikit.UIApplication;
import org.robovm.cocoatouch.uikit.UIApplicationDelegate;
import org.robovm.cocoatouch.uikit.UIDevice;
import org.robovm.cocoatouch.uikit.UIInterfaceOrientation;
import org.robovm.cocoatouch.uikit.UIScreen;
import org.robovm.cocoatouch.uikit.UIUserInterfaceIdiom;
import org.robovm.cocoatouch.uikit.UIViewController;
import org.robovm.cocoatouch.uikit.UIWindow;

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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Clipboard;

public class IOSApplication implements Application {
	
	public static abstract class Delegate extends UIApplicationDelegate.Adapter {
		private IOSApplication app;
		
		protected abstract IOSApplication createApplication();

		@Override
		public boolean didFinishLaunching (UIApplication application, NSDictionary launchOptions) {
			application.addStrongRef(this); // Prevent this from being GCed until the ObjC UIApplication is deallocated
			this.app = createApplication();
			return app.didFinishLaunching(application, launchOptions);
		}

		@Override
		public void didBecomeActive (UIApplication application) {
			app.didBecomeActive(application);
		}
		
		@Override
		public void willResignActive (UIApplication application) {
			app.willResignActive(application);
		}

		@Override
		public void willTerminate (UIApplication application) {
			app.willTerminate(application);
		}
	}
	
	UIApplication uiApp;
	UIWindow uiWindow;
	ApplicationListener listener;
	IOSApplicationConfiguration config;
	IOSGraphics graphics;
	IOSAudio audio;
	IOSFiles files;
	IOSInput input;
	IOSNet net;
	int logLevel = Application.LOG_DEBUG;

	/** The display scale factor (1.0f for normal; 2.0f to use retina coordinates/dimensions). */
	float displayScaleFactor;

	Array<Runnable> runnables = new Array<Runnable>();
	Array<Runnable> executedRunnables = new Array<Runnable>();
	Array<LifecycleListener> lifecycleListeners = new Array<LifecycleListener>();

	public IOSApplication (ApplicationListener listener, IOSApplicationConfiguration config) {
		this.listener = listener;
		this.config = config;
	}
	
	final boolean didFinishLaunching (UIApplication uiApp, NSDictionary options) {
		Gdx.app = this;
		this.uiApp = uiApp;

		// enable or disable screen dimming
		UIApplication.getSharedApplication().setIdleTimerDisabled(config.preventScreenDimming);

		// fix the scale factor if we have a retina device (NOTE: iOS screen sizes are in "points" not pixels by default!)
		if (UIScreen.getMainScreen().getScale() == 2.0f) {
			// we have a retina device!
			if (UIDevice.getCurrentDevice().getUserInterfaceIdiom() == UIUserInterfaceIdiom.Pad) {
				// it's an iPad!
				displayScaleFactor = config.displayScaleLargeScreenIfRetina * 2.0f;
			} else {
				// it's an iPod or iPhone
				displayScaleFactor = config.displayScaleSmallScreenIfRetina * 2.0f;
			}
		} 
		else {
			// no retina screen: no scaling!
			if (UIDevice.getCurrentDevice().getUserInterfaceIdiom() == UIUserInterfaceIdiom.Pad) {
				// it's an iPad!
				displayScaleFactor = config.displayScaleLargeScreenIfNonRetina;
			} else {
				// it's an iPod or iPhone
				displayScaleFactor = config.displayScaleSmallScreenIfNonRetina;
			}
		}

		GL20 gl20 = new IOSGLES20();
		
		Gdx.gl = gl20;
		Gdx.gl20 = gl20;
		
		// setup libgdx
		this.input = new IOSInput(this);
		this.graphics = new IOSGraphics(getBounds(null), this, config, input, gl20);
		this.files = new IOSFiles();
		this.audio = new IOSAudio();
		this.net = new IOSNet(this);

		Gdx.files = this.files;
		Gdx.graphics = this.graphics;
		Gdx.audio = this.audio;
		Gdx.input = this.input;
		Gdx.net = this.net;

		this.input.setupPeripherals();

		this.uiWindow = new UIWindow(UIScreen.getMainScreen().getBounds());
		this.uiWindow.setRootViewController(this.graphics.viewController);
		this.uiWindow.makeKeyAndVisible();
		Gdx.app.debug("IOSApplication", "created");
		return true;
	}
	
	/**
	 * Return the UI view controller of IOSApplication
	 * @return the view controller of IOSApplication
	 */
	public UIViewController getUIViewController(){
		return graphics.viewController;
	}

	/** Returns our real display dimension based on screen orientation.
	 * 
	 * @param viewController The view controller.
	 * @return Or real display dimension. */
	CGSize getBounds (UIViewController viewController) {
		// or screen size (always portrait)
		CGSize bounds = UIScreen.getMainScreen().getBounds().size();

		// determine orientation and resulting width + height
		UIInterfaceOrientation orientation = viewController != null 
			? viewController.getInterfaceOrientation() : uiApp.getStatusBarOrientation();
		int width;
		int height;
		switch (orientation) {
		case LandscapeLeft:
		case LandscapeRight:
			height = (int)bounds.width();
			width = (int)bounds.height();
			break;
		default:
			// assume portrait
			width = (int)bounds.width();
			height = (int)bounds.height();
		}

		// update width/height depending on display scaling selected 
		width *= displayScaleFactor;
		height *= displayScaleFactor;

		// log screen dimensions
		Gdx.app.debug("IOSApplication", "View: " + orientation.toString() + " " + width + "x" + height);

		// return resulting view size (based on orientation)
		return new CGSize(width, height);
	}

	final void didBecomeActive (UIApplication uiApp) {
		Gdx.app.debug("IOSApplication", "resumed");
		// workaround for ObjectAL crash problem
		// see: https://groups.google.com/forum/?fromgroups=#!topic/objectal-for-iphone/ubRWltp_i1Q
		//	OALAudioSession.sharedInstance().forceEndInterrupt();
		graphics.makeCurrent();
		graphics.resume();
	}

	final void willResignActive (UIApplication uiApp) {
		Gdx.app.debug("IOSApplication", "paused");
		graphics.makeCurrent();
		graphics.pause();
		Gdx.gl.glFlush();
	}

	final void willTerminate (UIApplication uiApp) {
		Gdx.app.debug("IOSApplication", "disposed");
		graphics.makeCurrent();
		Array<LifecycleListener> listeners = lifecycleListeners;
		synchronized(listeners) {
			for(LifecycleListener listener: listeners) {
				listener.pause();
			}
		}
		listener.dispose();
		Gdx.gl.glFlush();
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return listener;
	}
	
	@Override
	public Graphics getGraphics () {
		return graphics;
	}

	@Override
	public Audio getAudio () {
		return audio;
	}

	@Override
	public Input getInput () {
		return input;
	}

	@Override
	public Files getFiles () {
		return files;
	}

	@Override
	public Net getNet () {
		return net;
	}

	@Override
	public void log (String tag, String message) {
		if (logLevel > LOG_NONE) {
			System.out.println("[info] " + tag + ": " + message);
		}
	}

	@Override
	public void log (String tag, String message, Exception exception) {
		if (logLevel > LOG_NONE) {
			System.out.println("[info] " + tag + ": " + message);
			exception.printStackTrace();
		}
	}

	@Override
	public void error (String tag, String message) {
		if (logLevel >= LOG_ERROR) {
			System.out.println("[error] " + tag + ": " + message);
		}
	}

	@Override
	public void error (String tag, String message, Throwable exception) {
		if (logLevel >= LOG_ERROR) {
			System.out.println("[error] " + tag + ": " + message);
			exception.printStackTrace();
		}
	}

	@Override
	public void debug (String tag, String message) {
		if (logLevel >= LOG_DEBUG) {
			System.out.println("[debug] " + tag + ": " + message);
		}
	}

	@Override
	public void debug (String tag, String message, Throwable exception) {
		if (logLevel >= LOG_DEBUG) {
			System.out.println("[error] " + tag + ": " + message);
			exception.printStackTrace();
		}
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
	public ApplicationType getType () {
		return ApplicationType.iOS;
	}

	@Override
	public int getVersion () {
		return Integer.parseInt(UIDevice.getCurrentDevice().getSystemVersion().split("\\.")[0]);
	}

	@Override
	public long getJavaHeap () {
		return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}

	@Override
	public long getNativeHeap () {
		return getJavaHeap();
	}

	@Override
	public Preferences getPreferences (String name) {
		File libraryPath = new File(System.getenv("HOME"), "Library");
		String finalPath = new File(libraryPath, name + ".plist").getAbsolutePath();
		
		Gdx.app.debug("IOSApplication", "Loading NSDictionary from file " + finalPath);
		NSMutableDictionary nsDictionary = NSMutableDictionary.fromFile(finalPath);

		// if it fails to get an existing dictionary, create a new one.
		if (nsDictionary == null) {
			Gdx.app.debug("IOSApplication", "NSDictionary not found, creating a new one");
			nsDictionary = new NSMutableDictionary();
			boolean fileWritten = nsDictionary.writeToFile(finalPath, false);
			if (fileWritten)
				Gdx.app.debug("IOSApplication", "NSDictionary file written");
			else 
				Gdx.app.debug("IOSApplication", "Failed to write NSDictionary to file " + finalPath);
		}
		return new IOSPreferences(nsDictionary, finalPath);
	}

	@Override
	public void postRunnable (Runnable runnable) {
		synchronized (runnables) {
			runnables.add(runnable);
		}
	}

	public void processRunnables () {
		synchronized (runnables) {
			executedRunnables.clear();
			executedRunnables.addAll(runnables);
			runnables.clear();
		}
		for (int i = 0; i < executedRunnables.size; i++) {
			try {
				executedRunnables.get(i).run();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	@Override
	public void exit () {
		System.exit(0);
	}

	@Override
	public Clipboard getClipboard () {
		// FIXME implement clipboard
		return new Clipboard() {
			@Override
			public void setContents (String content) {
			}

			@Override
			public String getContents () {
				return null;
			}
		};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4265.java