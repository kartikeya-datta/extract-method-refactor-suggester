error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1048.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1048.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1048.java
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

package com.badlogic.gdx.backends.gwt;

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
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.badlogic.gdx.backends.gwt.preloader.Preloader.PreloaderCallback;
import com.badlogic.gdx.backends.gwt.preloader.Preloader.PreloaderState;
import com.badlogic.gdx.backends.gwt.soundmanager2.SoundManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Clipboard;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.TimeUtils;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/** Implementation of an {@link Application} based on GWT. Clients have to override {@link #getConfig()} and
 * {@link #getApplicationListener()}. Clients can override the default loading screen via
 * {@link #getPreloaderCallback()} and implement any loading screen drawing via GWT widgets.
 * @author mzechner */
public abstract class GwtApplication implements EntryPoint, Application {
	private ApplicationListener listener;
	private GwtApplicationConfiguration config;
	private GwtGraphics graphics;
	private GwtInput input;
	private GwtNet net;
	private Panel root = null;
	private TextArea log = null;
	private int logLevel = LOG_ERROR;
	private Array<Runnable> runnables = new Array<Runnable>();
	private Array<Runnable> runnablesHelper = new Array<Runnable>();
	private Array<LifecycleListener> lifecycleListeners = new Array<LifecycleListener>();
	private int lastWidth;
	private int lastHeight;
	Preloader preloader;
	private static AgentInfo agentInfo;
	private ObjectMap<String, Preferences> prefs = new ObjectMap<String, Preferences>();

	/** @return the configuration for the {@link GwtApplication}. */
	public abstract GwtApplicationConfiguration getConfig ();

	
	public String getPreloaderBaseURL()
	{
		return GWT.getHostPageBaseURL() + "assets/";
	}
	
	@Override
	public void onModuleLoad () {
		GwtApplication.agentInfo = computeAgentInfo();
		this.listener = getApplicationListener();
		this.config = getConfig();
		this.log = config.log;

		if (config.rootPanel != null) {
			this.root = config.rootPanel;
		} else {
			Element element = Document.get().getElementById("embed-" + GWT.getModuleName());
			if (element == null) {
				VerticalPanel panel = new VerticalPanel();
				panel.setWidth("" + config.width + "px");
				panel.setHeight("" + config.height + "px");
				panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
				RootPanel.get().add(panel);
				RootPanel.get().setWidth("" + config.width + "px");
				RootPanel.get().setHeight("" + config.height + "px");
				this.root = panel;
			} else {
				VerticalPanel panel = new VerticalPanel();
				panel.setWidth("" + config.width + "px");
				panel.setHeight("" + config.height + "px");
				panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
				element.appendChild(panel.getElement());
				root = panel;
			}
		}

		// initialize SoundManager2
		SoundManager.init(GWT.getModuleBaseURL(), 9, true, new SoundManager.SoundManagerCallback(){

			@Override
			public void onready () {
				final PreloaderCallback callback = getPreloaderCallback();
				preloader = createPreloader();
				preloader.preload("assets.txt", new PreloaderCallback() {
					@Override
					public void error (String file) {
						callback.error(file);
					}

					@Override
					public void update (PreloaderState state) {
						callback.update(state);
						if (state.hasEnded()) {
							getRootPanel().clear();
							setupLoop();
						}
					}
				});
			}

			@Override
			public void ontimeout (String status, String errorType) {
				error("SoundManager", status + " " + errorType);
			}
			
		});
	}

	void setupLoop () {
		// setup modules
		try {			
			graphics = new GwtGraphics(root, config);			
		} catch (Throwable e) {
			root.clear();
			root.add(new Label("Sorry, your browser doesn't seem to support WebGL"));
			return;
		}
		lastWidth = graphics.getWidth();
		lastHeight = graphics.getHeight();
		Gdx.app = this;
		Gdx.audio = new GwtAudio();
		Gdx.graphics = graphics;
		Gdx.gl20 = graphics.getGL20();
		Gdx.gl = graphics.getGLCommon();
		Gdx.files = new GwtFiles(preloader);
		this.input = new GwtInput(graphics.canvas);
		Gdx.input = this.input;
		this.net = new GwtNet();
		Gdx.net = this.net;

		// tell listener about app creation
		try {
			listener.create();
			listener.resize(graphics.getWidth(), graphics.getHeight());
		} catch (Throwable t) {
			error("GwtApplication", "exception: " + t.getMessage(), t);
			t.printStackTrace();
			throw new RuntimeException(t);
		}

		// setup rendering timer
		new Timer() {
			@Override
			public void run () {
				try {
					mainLoop();
				} catch (Throwable t) {
					error("GwtApplication", "exception: " + t.getMessage(), t);
					throw new RuntimeException(t);
				}
			}
		}.scheduleRepeating((int)((1f / config.fps) * 1000));
	}

	void mainLoop() {
		graphics.update();
		if (Gdx.graphics.getWidth() != lastWidth || Gdx.graphics.getHeight() != lastHeight) {
			GwtApplication.this.listener.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			lastWidth = graphics.getWidth();
			lastHeight = graphics.getHeight();
			Gdx.gl.glViewport(0, 0, lastWidth, lastHeight);
		}
		runnablesHelper.addAll(runnables);
		runnables.clear();
		for (int i = 0; i < runnablesHelper.size; i++) {
			runnablesHelper.get(i).run();
		}
		runnablesHelper.clear();					
		listener.render();
		input.justTouched = false;
	}
	
	public Panel getRootPanel () {
		return root;
	}

	long loadStart = TimeUtils.nanoTime();

	public Preloader createPreloader() {
		return new Preloader(getPreloaderBaseURL());
	}

	public PreloaderCallback getPreloaderCallback () {
		final Panel preloaderPanel = new VerticalPanel();
		preloaderPanel.setStyleName("gdx-preloader");
		final Image logo = new Image(GWT.getModuleBaseURL() + "logo.png");
		logo.setStyleName("logo");		
		preloaderPanel.add(logo);
		final Panel meterPanel = new SimplePanel();
		meterPanel.setStyleName("gdx-meter");
		meterPanel.addStyleName("red");
		final InlineHTML meter = new InlineHTML();
		final Style meterStyle = meter.getElement().getStyle();
		meterStyle.setWidth(0, Unit.PCT);
		meterPanel.add(meter);
		preloaderPanel.add(meterPanel);
		getRootPanel().add(preloaderPanel);
		return new PreloaderCallback() {

			@Override
			public void error (String file) {
				System.out.println("error: " + file);
			}
			
			@Override
			public void update (PreloaderState state) {
				meterStyle.setWidth(100f * state.getProgress(), Unit.PCT);
			}			
			
		};
	}

	@Override
	public Graphics getGraphics () {
		return graphics;
	}

	@Override
	public Audio getAudio () {
		return Gdx.audio;
	}

	@Override
	public Input getInput () {
		return Gdx.input;
	}

	@Override
	public Files getFiles () {
		return Gdx.files;
	}
	
	@Override
	public Net getNet() {
		return Gdx.net;
	}

	private void checkLogLabel () {
		if (log == null) {
			log = new TextArea();
			log.setSize(graphics.getWidth() + "px", "200px");
			log.setReadOnly(true);
			root.add(log);
		}
	}

	@Override
	public void log (String tag, String message) {
		if (logLevel >= LOG_INFO) {
			checkLogLabel();
			log.setText(log.getText() + "\n" + tag + ": " + message);
			log.setCursorPos(log.getText().length() - 1);
			System.out.println(tag + ": " + message);
		}
	}

	@Override
	public void log (String tag, String message, Throwable exception) {
		if (logLevel >= LOG_INFO) {
			checkLogLabel();
			log.setText(log.getText() + "\n" + tag + ": " + message + "\n" + exception.getMessage() + "\n");
			log.setCursorPos(log.getText().length() - 1);
			System.out.println(tag + ": " + message + "\n" + exception.getMessage());
			System.out.println(getStackTrace(exception));
		}
	}

	@Override
	public void error (String tag, String message) {
		if (logLevel >= LOG_ERROR) {
			checkLogLabel();
			log.setText(log.getText() + "\n" + tag + ": " + message);
			log.setCursorPos(log.getText().length() - 1);
			System.err.println(tag + ": " + message);
		}
	}

	@Override
	public void error (String tag, String message, Throwable exception) {
		if (logLevel >= LOG_ERROR) {
			checkLogLabel();
			log.setText(log.getText() + "\n" + tag + ": " + message + "\n" + exception.getMessage());
			log.setCursorPos(log.getText().length() - 1);
			System.err.println(tag + ": " + message + "\n" + exception.getMessage() + "\n");
			System.out.println(getStackTrace(exception));
		}
	}

	@Override
	public void debug (String tag, String message) {
		if (logLevel >= LOG_DEBUG) {
			checkLogLabel();
			log.setText(log.getText() + "\n" + tag + ": " + message + "\n");
			log.setCursorPos(log.getText().length() - 1);
			System.out.println(tag + ": " + message + "\n");
		}
	}

	@Override
	public void debug (String tag, String message, Throwable exception) {
		if (logLevel >= LOG_DEBUG) {
			checkLogLabel();
			log.setText(log.getText() + "\n" + tag + ": " + message + "\n" + exception.getMessage() + "\n");
			log.setCursorPos(log.getText().length() - 1);
			System.out.println(tag + ": " + message + "\n" + exception.getMessage());
			System.out.println(getStackTrace(exception));
		}
	}

	private String getStackTrace (Throwable e) {
		StringBuffer buffer = new StringBuffer();
		for (StackTraceElement trace : e.getStackTrace()) {
			buffer.append(trace.toString() + "\n");
		}
		return buffer.toString();
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
		return ApplicationType.WebGL;
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
		Preferences pref = prefs.get(name);
		if (pref == null) {
			pref = new GwtPreferences(name);
			prefs.put(name, pref);
		}
		return pref;
	}

	@Override
	public Clipboard getClipboard() {
		return new Clipboard() {
			@Override
			public String getContents () {
				return null;
			}

			@Override
			public void setContents (String content) {
			}			
		};		
	}
	
	@Override
	public void postRunnable (Runnable runnable) {
		runnables.add(runnable);
	}

	@Override
	public void exit () {
	}

	/** Contains precomputed information on the user-agent. Useful for dealing with browser and OS behavioral differences. Kindly
	 * borrowed from PlayN */
	public static AgentInfo agentInfo () {
		return agentInfo;
	}

	/** kindly borrowed from PlayN **/
	private static native AgentInfo computeAgentInfo () /*-{
																			var userAgent = navigator.userAgent.toLowerCase();
																			return {
																			// browser type flags
																			isFirefox : userAgent.indexOf("firefox") != -1,
																			isChrome : userAgent.indexOf("chrome") != -1,
																			isSafari : userAgent.indexOf("safari") != -1,
																			isOpera : userAgent.indexOf("opera") != -1,
																			isIE : userAgent.indexOf("msie") != -1,
																			// OS type flags
																			isMacOS : userAgent.indexOf("mac") != -1,
																			isLinux : userAgent.indexOf("linux") != -1,
																			isWindows : userAgent.indexOf("win") != -1
																			};
																			}-*/;

	/** Returned by {@link #agentInfo}. Kindly borrowed from PlayN. */
	public static class AgentInfo extends JavaScriptObject {
		public final native boolean isFirefox () /*-{
																return this.isFirefox;
																}-*/;

		public final native boolean isChrome () /*-{
																return this.isChrome;
																}-*/;

		public final native boolean isSafari () /*-{
																return this.isSafari;
																}-*/;

		public final native boolean isOpera () /*-{
															return this.isOpera;
															}-*/;

		public final native boolean isIE () /*-{
														return this.isIE;
														}-*/;

		public final native boolean isMacOS () /*-{
															return this.isMacOS;
															}-*/;

		public final native boolean isLinux () /*-{
															return this.isLinux;
															}-*/;

		public final native boolean isWindows () /*-{
																return this.isWindows;
																}-*/;

		protected AgentInfo () {
		}
	}

	public String getBaseUrl () {
		return preloader.baseUrl;
	}

	public Preloader getPreloader () {
		return preloader;
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
	
	native static public void consoleLog(String message) /*-{
		console.log( "GWT: " + message );
	}-*/;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1048.java