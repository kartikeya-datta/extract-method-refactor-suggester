error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7969.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7969.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7969.java
text:
```scala
t@@his.input = new IOSInput(this.graphics);

package com.badlogic.gdx.backends.ios;

import cli.MonoTouch.UIKit.UIApplication;
import cli.MonoTouch.UIKit.UIApplicationDelegate;
import cli.MonoTouch.UIKit.UIScreen;
import cli.MonoTouch.UIKit.UIWindow;
import cli.System.Console;
import cli.System.Drawing.RectangleF;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;

public class IOSApplication implements Application {
	final UIApplication uiApp;
	final UIWindow uiWindow;
	final ApplicationListener listener;
	final IOSGraphics graphics;
	final IOSAudio audio;
	final IOSFiles files;
	final IOSInput input;
	int logLevel = Application.LOG_DEBUG;
	
	/**
	 * Should be called in AppDelegate#FinishedLaunching
	 * @param uiApp
	 */
	public IOSApplication(UIApplication uiApp, ApplicationListener listener) {
		this.uiApp = uiApp;
		this.listener = listener;
		
		Gdx.app = this;
		
		RectangleF bounds = UIScreen.get_MainScreen().get_Bounds();
		this.graphics = new IOSGraphics(bounds, this);
		this.files = new IOSFiles();
		this.audio = new IOSAudio();
		this.input = new IOSInput();
		
		Gdx.files = this.files;
		Gdx.graphics = this.graphics;
		Gdx.audio = this.audio;
		Gdx.input = this.input;
		
		this.uiWindow = new UIWindow(bounds);
		this.uiWindow.Add(graphics);
		this.graphics.Run();
		this.uiWindow.MakeKeyAndVisible();
	}
	
	@Override
	public Graphics getGraphics () {
		return null;
	}

	@Override
	public Audio getAudio () {
		return null;
	}

	@Override
	public Input getInput () {
		return null;
	}

	@Override
	public Files getFiles () {
		return null;
	}

	@Override
	public void log (String tag, String message) {
		if(logLevel > LOG_NONE) {
			Console.WriteLine("[info] " + tag + ": " + message);
		}
	}

	@Override
	public void log (String tag, String message, Exception exception) {
		if(logLevel > LOG_NONE) {
			Console.WriteLine("[info] " + tag + ": " + message);
			exception.printStackTrace();
		}
	}

	@Override
	public void error (String tag, String message) {
		if(logLevel >= LOG_ERROR) {
			Console.WriteLine("[error] " +  tag + ": " + message);
		}
	}

	@Override
	public void error (String tag, String message, Throwable exception) {
		if(logLevel >= LOG_ERROR) {
			Console.WriteLine("[error] " +  tag + ": " + message);
			exception.printStackTrace();
		}
	}

	@Override
	public void debug (String tag, String message) {
		if(logLevel >= LOG_DEBUG) {
			Console.WriteLine("[debug] " +  tag + ": " + message);
		}
	}

	@Override
	public void debug (String tag, String message, Throwable exception) {
		if(logLevel >= LOG_DEBUG) {
			Console.WriteLine("[error] " +  tag + ": " + message);
			exception.printStackTrace();
		}
	}

	@Override
	public void setLogLevel (int logLevel) {
		this.logLevel = logLevel;
	}

	@Override
	public ApplicationType getType () {
		return ApplicationType.iOS;
	}

	@Override
	public int getVersion () {
		return 0;
	}

	@Override
	public long getJavaHeap () {
		return 0;
	}

	@Override
	public long getNativeHeap () {
		return 0;
	}

	@Override
	public Preferences getPreferences (String name) {
		return null;
	}

	@Override
	public void postRunnable (Runnable runnable) {
		
	}

	@Override
	public void exit () {	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7969.java