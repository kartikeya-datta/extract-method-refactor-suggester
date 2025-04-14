error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8637.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8637.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8637.java
text:
```scala
i@@f ( !! navigator.getGamepads !! navigator.webkitGamepads || !! navigator.webkitGetGamepads) {

package com.badlogic.gdx.controllers.gwt.support;

import com.badlogic.gdx.utils.IntMap;
import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class GamepadSupport {
	
	private static Ticker ticker = new Ticker();

	private static GamepadSupportListener listener;

	private static IntMap<Gamepad> gamepads = new IntMap<Gamepad>();
	private static IntMap<Gamepad> gamepadsTemp = new IntMap<Gamepad>();
	
	public static void init(GamepadSupportListener listener) {
		GamepadSupport.listener = listener;
		nativeInit();
	}
	
	public static void startPolling() {
        consoleLog("startPolling");
		ticker.start();
	}
	
	public static void stopPolling() {
		ticker.stop();
	}
	
	public static void pollGamepads() {
		JsArray<Gamepad> currentGamepads = nativePollGamepads();
		if (currentGamepads != null) {
			gamepadsTemp.clear();
			gamepadsTemp.putAll(gamepads);
			for (int i = 0, j = currentGamepads.length(); i < j; i++) {
				Gamepad gamepad = currentGamepads.get(i);
				if (gamepad != null) {
					if (!gamepadsTemp.containsKey(gamepad.getIndex())) {
						onGamepadConnect(gamepad);
					}
					gamepadsTemp.remove(gamepad.getIndex());
				}				
			}
			for (Gamepad gamepad : gamepadsTemp.values()) {
				onGamepadDisconnect(gamepad);
			}
		}
	}
	
	public static void pollGamepadsStatus() {
		for (Gamepad gamepad : gamepads.values()) {
			if (gamepad.getPreviousTimestamp() != gamepad.getTimestamp()) {
				fireGamepadUpdated(gamepad.getIndex());
			}
			gamepad.setPreviousTimestamp(gamepad.getTimestamp());
		}
	}
	
	public static Gamepad getGamepad(int index) {
		return gamepads.get(index);
	}

	private static void onGamepadConnect(Gamepad gamepad) {
		consoleLog("onGamepadConnect: " + gamepad.getId());
		gamepads.put(gamepad.getIndex(), gamepad);
		fireGamepadConnected(gamepad.getIndex());
	}

	private static void onGamepadDisconnect(Gamepad gamepad) {
		consoleLog("onGamepadDisconnect: " + gamepad.getId());
		gamepads.remove(gamepad.getIndex());
		fireGamepadDisconnected(gamepad.getIndex());
	}

	private static void fireGamepadConnected(int index) {
		if (listener != null) {
			listener.onGamepadConnected(index);
		}
	}
	
	private static void fireGamepadDisconnected(int index) {
		if (listener != null) {
			listener.onGamepadDisconnected(index);
		}
	}
	
	private static void fireGamepadUpdated(int index) {
		if (listener != null) {
			listener.onGamepadUpdated(index);
		}
	}
	
	private static void handleGamepadConnect(GamepadEvent event) {
		onGamepadConnect(event.getGamepad());
	}
	private static void handleGamepadDisconnect(GamepadEvent event) {
		onGamepadDisconnect(event.getGamepad());
	}
	
	private static native void nativeInit() /*-{
        var gamepadSupportAvailable = !! navigator.getGamepads || !! navigator.webkitGetGamepads || !! navigator.webkitGamepads || (navigator.userAgent.indexOf('Firefox/') != -1);
        if (gamepadSupportAvailable) {
            $wnd.addEventListener('MozGamepadConnected', @com.badlogic.gdx.controllers.gwt.support.GamepadSupport::handleGamepadConnect(Lcom/badlogic/gdx/controllers/gwt/support/GamepadSupport$GamepadEvent;), false);
            $wnd.addEventListener('MozGamepadDisconnected', @com.badlogic.gdx.controllers.gwt.support.GamepadSupport::handleGamepadDisconnect(Lcom/badlogic/gdx/controllers/gwt/support/GamepadSupport$GamepadEvent;), false);
            if ( !! navigator.getGamepads || !! navigator.webkitGamepads || !! navigator.webkitGetGamepads) {
                @com.badlogic.gdx.controllers.gwt.support.GamepadSupport::startPolling()();
            }
        }
	}-*/;
	
	private static native JsArray<Gamepad> nativePollGamepads() /*-{
		return rawGamepads = (navigator.webkitGetGamepads && navigator.webkitGetGamepads()) || navigator.webkitGamepads;
	}-*/;
	
	public static native void consoleLog(String message) /*-{
		$wnd.console.log(message);
	}-*/;
	
	private static class Ticker implements AnimationScheduler.AnimationCallback {
		
		private boolean ticking = false;
		
		public void start() {
			if (!ticking) {
				ticking = true;
				AnimationScheduler.get().requestAnimationFrame(this);			
			}
		}
		
		public void stop() {
			ticking = false;
		}
		
		@Override
		public void execute(double timestamp) {
			if (ticking) {
				GamepadSupport.pollGamepads();
				GamepadSupport.pollGamepadsStatus();
				AnimationScheduler.get().requestAnimationFrame(this);
			}
		}
	}

	private static final class GamepadEvent extends JavaScriptObject {
		protected GamepadEvent() {
			// Required by GWT
		}
		
		public native Gamepad getGamepad() /*-{
			return this.gamepad;
		}-*/;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8637.java